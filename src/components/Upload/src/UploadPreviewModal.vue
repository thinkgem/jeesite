<template>
  <BasicModal
    width="800px"
    :title="t('component.upload.view')"
    :cancelText="t('component.modal.okText')"
    wrapClassName="upload-preview-modal"
    v-bind="$attrs"
    @register="register"
    :showOkBtn="false"
  >
    <FileList :dataSource="fileList" :columns="columns" :actionColumn="actionColumn" />
  </BasicModal>
</template>
<script lang="ts">
  import { defineComponent, watch, ref } from 'vue';
  import { BasicModal, useModalInner } from '/@/components/Modal';
  // import { BasicTable, useTable } from '/@/components/Table';
  import FileList from './FileList.vue';
  import { previewProps } from './props';
  import { downloadByUrl } from '/@/utils/file/download';
  import { createPreviewColumns, createPreviewActionColumn } from './data';
  import { useI18n } from '/@/hooks/web/useI18n';
  import { isArray } from '/@/utils/is';
  import { FileUpload } from '/@/api/sys/upload';
  import { useGlobSetting } from '/@/hooks/setting';

  export default defineComponent({
    components: { BasicModal, FileList },
    props: previewProps,
    emits: ['change', 'register', 'delete'],
    setup(props, { emit }) {
      const [register, { closeModal }] = useModalInner();
      const { t } = useI18n();
      const { apiUrl } = useGlobSetting();
      const fileList = ref<FileUpload[]>([]);

      watch(
        () => props.value,
        (value) => {
          if (!isArray(value)) value = [];
          fileList.value = value;
        },
        { immediate: true },
      );

      // 删除
      function handleRemove(record: FileUpload) {
        const index = fileList.value.findIndex((item) => item.id === record.id);
        if (index !== -1) {
          const removed = fileList.value.splice(index, 1);
          emit('delete', removed[0]);
          emit('change', fileList.value);
        }
      }

      // // 预览
      // function handlePreview(record: PreviewFileItem) {
      //   const { url = '' } = record;
      //   createImgPreview({
      //     imageList: [url],
      //   });
      // }

      // 下载
      function handleDownload(record: FileUpload) {
        const { fileUrl } = record;
        let url = fileUrl || '';
        if (url.indexOf('://') == -1) {
          url = apiUrl + url;
        }
        downloadByUrl({ url });
      }

      return {
        t,
        register,
        closeModal,
        fileList,
        columns: createPreviewColumns() as any[],
        actionColumn: createPreviewActionColumn({ handleRemove, handleDownload }) as any,
      };
    },
  });
</script>
<style lang="less">
  .upload-preview-modal {
    .ant-upload-list {
      display: none;
    }

    .ant-table-wrapper .ant-spin-nested-loading {
      padding: 0;
    }
  }
</style>
