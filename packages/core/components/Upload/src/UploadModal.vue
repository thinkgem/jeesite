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
        :directory="directory"
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
<script lang="ts" setup>
  import { ref, toRefs, unref, computed, useAttrs } from 'vue';
  import { Upload, Alert } from 'ant-design-vue';
  import { BasicModal, useModalInner } from '@jeesite/core/components/Modal';
  // import { BasicTable, useTable } from '@jeesite/core/components/Table';
  import FileList from './FileList.vue';
  import { uploadProps } from './props';
  import { useUploadType } from './useUpload';
  import { useMessage } from '@jeesite/core/hooks/web/useMessage';
  import { FileItem, UploadResultStatus } from './typing';
  import { createTableColumns, createActionColumn } from './data';
  import { checkImgType, getBase64WithFile } from './helper';
  import { buildUUID } from '@jeesite/core/utils/uuid';
  import { isFunction } from '@jeesite/core/utils/is';
  import { warn } from '@jeesite/core/utils/log';
  import { useI18n } from '@jeesite/core/hooks/web/useI18n';
  import { useGlobSetting } from '@jeesite/core/hooks/setting';
  import { FileUpload } from '@jeesite/core/api/sys/upload';
  import { openWindowLayer } from '@jeesite/core/utils';

  const props = defineProps(uploadProps);
  const emit = defineEmits(['change', 'register', 'delete']);

  const { uploadType, accept, helpText, maxNumber, maxSize, directory } = toRefs(props);
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
    const someError = fileItemList.value.some((item) => item.status === UploadResultStatus.ERROR);
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
      fileMd5: buildUUID(), // 专业版支持 MD5 秒传
      fileName: name,
      fileUploadId: '',
      fileEntityId: '',
      bizKey,
      bizType,
      uploadType,
    } as FileItem;
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
    if (fileItemList.value.length + (props.previewFileList?.length || 0) >= maxNumber) {
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
      const removed = fileItemList.value.splice(index, 1);
      const item = removed[0] as FileItem;
      if (item && item.responseData?.fileUpload) {
        emit('delete', item.responseData?.fileUpload);
      }
    }
  }

  async function uploadApiByItem(item: FileItem) {
    const { checkmd5, api } = props;
    if (!api || !isFunction(api)) {
      return warn('upload api must exist and be a function');
    }
    try {
      item.status = UploadResultStatus.UPLOADING;
      if (checkmd5) {
        const data = await api(
          {
            bizKey: item.bizKey,
            bizType: item.bizType,
            fileMd5: item.fileMd5,
            fileName: item.fileName,
          },
          () => {},
          props.apiUploadUrl,
        );
        if (data.result == 'true') {
          item.percent = 100;
          item.responseData = data;
        } else {
          item.fileUploadId = data.fileUploadId;
          item.fileEntityId = data.fileEntityId;
        }
      }
      if (item.percent != 100) {
        const { data } = await api(
          {
            bizKey: item.bizKey,
            bizType: item.bizType,
            uploadType: item.uploadType,
            fileMd5: item.fileMd5,
            fileName: item.fileName,
            fileUploadId: item.fileUploadId,
            fileEntityId: item.fileEntityId,
            imageMaxWidth: props.imageMaxWidth || '',
            imageMaxHeight: props.imageMaxHeight || '',
            ...(props.uploadParams || {}),
            file: item.file,
          },
          (progressEvent: ProgressEvent) => {
            const complete = ((progressEvent.loaded / progressEvent.total) * 100) | 0;
            item.percent = complete;
          },
          props.apiUploadUrl,
        );
        item.responseData = data;
      }
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

  const columns = createTableColumns() as any[];

  const actionColumn = createActionColumn(handleRemove) as any;
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
