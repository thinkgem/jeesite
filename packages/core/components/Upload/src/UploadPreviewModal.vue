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
    <FileList
      :dataSource="fileList"
      :columns="columns"
      :actionColumn="actionColumn"
      :readonly="readonly"
    />
  </BasicModal>
</template>
<script lang="ts" setup>
  import { watch, ref, useAttrs } from 'vue';
  import { BasicModal, useModalInner } from '@jeesite/core/components/Modal';
  // import { BasicTable, useTable } from '@jeesite/core/components/Table';
  import FileList from './FileList.vue';
  import { previewProps } from './props';
  import { downloadByUrl } from '@jeesite/core/utils/file/download';
  import { createPreviewColumns, createPreviewActionColumn } from './data';
  import { useI18n } from '@jeesite/core/hooks/web/useI18n';
  import { useGlobSetting } from '@jeesite/core/hooks/setting';
  import { isArray } from '@jeesite/core/utils/is';
  import { FileUpload } from '@jeesite/core/api/sys/upload';

  const props = defineProps(previewProps);
  const emit = defineEmits(['change', 'register', 'delete']);

  const [register] = useModalInner();
  const { t } = useI18n();
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

  // 下载
  function handleDownload(record: FileUpload) {
    downloadByUrl({ url: props.apiDownloadUrl + '/' + record.id });
  }

  const columns = createPreviewColumns(props) as any[];

  const actionColumn = createPreviewActionColumn(
    {
      handleRemove,
      handleDownload,
    },
    props.readonly,
  ) as any;
</script>
<style lang="less">
  .upload-preview-modal {
    .file-table-td {
      word-wrap: break-word;
      word-break: break-all;
    }

    .ant-upload-list {
      display: none;
    }

    .ant-table-wrapper .ant-spin-nested-loading {
      padding: 0;
    }
  }
</style>
