<template>
  <BasicModal
    width="800px"
    :title="t('component.upload.upload')"
    :okText="t('component.upload.save')"
    v-bind="$attrs"
    @register="register"
    @ok="handleOk"
    :closeFunc="handleCloseFunc"
    :maskClosable="false"
    :keyboard="false"
    wrapClassName="upload-modal"
    :okButtonProps="getOkButtonProps"
    :cancelButtonProps="{ disabled: uploading }"
  >
    <template #centerFooter>
      <a-button
        @click="handleStartUpload"
        color="success"
        :disabled="!getIsSelectFile"
        :loading="uploading"
        v-show="isLazy"
      >
        {{ getUploadBtnText }}
      </a-button>
    </template>

    <div class="upload-modal-toolbar">
      <Alert :message="getHelpText" type="info" banner class="upload-modal-toolbar__text" />
      <Upload
        :accept="getStringAccept"
        :multiple="multiple"
        :before-upload="beforeUpload"
        :show-upload-list="false"
        class="upload-modal-toolbar__btn"
      >
        <a-button type="primary">
          {{ t('component.upload.choose') }}
        </a-button>
      </Upload>
    </div>
    <FileList :dataSource="fileItemList" :columns="columns" :actionColumn="actionColumn" />
  </BasicModal>
</template>
<script lang="ts">
  import { defineComponent, ref, toRefs, unref, computed, PropType } from 'vue';
  import { Upload, Alert } from 'ant-design-vue';
  import { BasicModal, useModalInner } from '/@/components/Modal';
  // import { BasicTable, useTable } from '/@/components/Table';
  import FileList from './FileList.vue';
  import { useUploadType } from './useUpload';
  import { useMessage } from '/@/hooks/web/useMessage';
  import { FileItem, UploadResultStatus } from './typing';
  import { basicProps } from './props';
  import { createTableColumns, createActionColumn } from './data';
  import { checkImgType, getBase64WithFile, getMd5WithFile } from './helper';
  import { buildUUID } from '/@/utils/uuid';
  import { isFunction } from '/@/utils/is';
  import { warn } from '/@/utils/log';
  import { useI18n } from '/@/hooks/web/useI18n';
  import { FileUpload } from '/@/api/sys/upload';

  export default defineComponent({
    components: { BasicModal, Upload, Alert, FileList },
    props: {
      ...basicProps,
      previewFileList: {
        type: Array as PropType<FileUpload[]>,
        default: () => [],
      },
    },
    emits: ['change', 'register', 'delete'],
    setup(props, { emit }) {
      const { uploadType, accept, helpText, maxNumber, maxSize } = toRefs(props);
      const { t } = useI18n();
      const [register, { closeModal }] = useModalInner();
      const fileItemList = ref<FileItem[]>([]);
      const uploading = ref(false);

      const { getStringAccept, getHelpText } = useUploadType({
        uploadTypeRef: uploadType,
        acceptRef: accept,
        helpTextRef: helpText,
        maxNumberRef: maxNumber,
        maxSizeRef: maxSize,
      });

      const { createMessage } = useMessage();

      const getIsSelectFile = computed(() => {
        return (
          fileItemList.value.length > 0 &&
          !fileItemList.value.every((item) => item.status === UploadResultStatus.SUCCESS)
        );
      });

      const getOkButtonProps = computed(() => {
        const someSuccess = fileItemList.value.some(
          (item) => item.status === UploadResultStatus.SUCCESS,
        );
        return {
          disabled: uploading.value || fileItemList.value.length === 0 || !someSuccess,
        };
      });

      const getUploadBtnText = computed(() => {
        const someError = fileItemList.value.some(
          (item) => item.status === UploadResultStatus.ERROR,
        );
        return uploading.value
          ? t('component.upload.uploading')
          : someError
          ? t('component.upload.reUploadFailed')
          : t('component.upload.startUpload');
      });

      // 上传前校验
      function beforeUpload(file: File) {
        const { size, name } = file;
        const { maxSize, bizKey, bizType, uploadType } = props;
        // 设置最大值，则判断
        if (maxSize && file.size / 1024 / 1024 >= maxSize) {
          createMessage.error(t('component.upload.maxSizeMultiple', [maxSize]));
          return false;
        }
        const commonItem = {
          id: buildUUID(),
          file,
          size,
          name,
          percent: 0,
          type: name.split('.').pop(),
          fileMd5: buildUUID(),
          fileName: name,
          fileUploadId: '',
          fileEntityId: '',
          bizKey,
          bizType,
          uploadType,
        };
        // 生成图片缩略图
        if (checkImgType(file)) {
          getBase64WithFile(file).then(({ result: fileUrl }) => {
            addFileItem({
              fileUrl,
              ...commonItem,
            });
          });
        } else {
          addFileItem(commonItem);
        }
        return false;
      }

      function addFileItem(record: FileItem) {
        const { maxNumber } = props;
        if ((fileItemList.value.length + props.previewFileList?.length ?? 0) > maxNumber) {
          createMessage.warning(t('component.upload.maxNumber', [maxNumber]));
          return;
        }
        fileItemList.value = [...unref(fileItemList), record];
        if (!props.isLazy) {
          uploadApiByItem(fileItemList.value[fileItemList.value.length - 1]);
        }
      }

      // 删除
      function handleRemove(record: FileItem) {
        const index = fileItemList.value.findIndex((item) => item.id === record.id);
        if (index !== -1) {
          fileItemList.value.splice(index, 1);
        }
      }

      // 预览
      // function handlePreview(record: FileItem) {
      //   const { thumbUrl = '' } = record;
      //   createImgPreview({
      //     imageList: [thumbUrl],
      //   });
      // }

      async function uploadApiByItem(item: FileItem) {
        const { api } = props;
        if (!api || !isFunction(api)) {
          return warn('upload api must exist and be a function');
        }
        try {
          item.status = UploadResultStatus.UPLOADING;
          const { data } = await props.api?.(
            {
              bizKey: item.bizKey,
              bizType: item.bizType,
              uploadType: item.uploadType,
              fileMd5: item.fileMd5,
              fileName: item.fileName,
              fileUploadId: item.fileUploadId,
              fileEntityId: item.fileEntityId,
              ...(props.uploadParams || {}),
              file: item.file,
            },
            function onUploadProgress(progressEvent: ProgressEvent) {
              const complete = ((progressEvent.loaded / progressEvent.total) * 100) | 0;
              item.percent = complete;
            },
          );
          item.responseData = data;
          item.status = UploadResultStatus.SUCCESS;
          return {
            success: true,
            error: null,
          };
        } catch (e) {
          console.log(e);
          item.status = UploadResultStatus.ERROR;
          return {
            success: false,
            error: e,
          };
        }
      }

      // 点击开始上传
      async function handleStartUpload() {
        if (uploading.value) return;
        uploading.value = true;
        try {
          // 只上传不是成功状态的
          const uploadFileList =
            fileItemList.value.filter((item) => item.status !== UploadResultStatus.SUCCESS) || [];
          const data = await Promise.all(
            uploadFileList.map((item) => {
              return uploadApiByItem(item);
            }),
          );
          // 生产环境:抛出错误
          const errorList = data.filter((item: any) => item.result === 'false');
          if (errorList.length > 0) throw errorList;
        } catch (e) {
          throw e;
        } finally {
          uploading.value = false;
        }
      }

      // 点击保存
      function handleOk() {
        const { maxNumber } = props;

        if (fileItemList.value.length > maxNumber) {
          return createMessage.warning(t('component.upload.maxNumber', [maxNumber]));
        }
        if (uploading.value) {
          return createMessage.warning(t('component.upload.saveWarn'));
        }
        const fileList: FileUpload[] = [];

        for (const item of fileItemList.value) {
          const { status, responseData } = item;
          if (status === UploadResultStatus.SUCCESS && responseData) {
            fileList.push(responseData.fileUpload);
          }
        }
        // 存在一个上传成功的即可保存
        if (fileList.length <= 0) {
          return createMessage.warning(t('component.upload.saveError'));
        }
        fileItemList.value = [];
        closeModal();
        emit('change', fileList);
      }

      // 点击关闭：则所有操作不保存，包括上传的
      async function handleCloseFunc() {
        if (!uploading.value) {
          fileItemList.value = [];
          return true;
        } else {
          createMessage.warning(t('component.upload.uploadWait'));
          return false;
        }
      }

      return {
        columns: createTableColumns() as any[],
        actionColumn: createActionColumn(handleRemove) as any,
        register,
        closeModal,
        getHelpText,
        getStringAccept,
        getOkButtonProps,
        beforeUpload,
        // registerTable,
        fileItemList,
        uploading,
        handleStartUpload,
        handleOk,
        handleCloseFunc,
        getIsSelectFile,
        getUploadBtnText,
        t,
      };
    },
  });
</script>
<style lang="less">
  .upload-modal {
    .ant-upload-list {
      display: none;
    }

    .ant-table-wrapper .ant-spin-nested-loading {
      padding: 0;
    }

    &-toolbar {
      display: flex;
      align-items: center;
      margin-bottom: 8px;

      &__btn {
        margin-left: 8px;
        text-align: right;
        flex: 1;
      }
    }
  }
</style>
