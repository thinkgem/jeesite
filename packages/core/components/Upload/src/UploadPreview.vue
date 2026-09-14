<template>
  <div class="upload-preview">
    <div class="upload-preview-toolbar" v-if="!props.readonly">
      <Alert :title="getAlertTitle" type="info" banner class="upload-preview-toolbar__text" />
      <Upload
        :accept="getStringAccept"
        :multiple="multiple"
        :before-upload="beforeUpload"
        :directory="directory"
        :show-upload-list="false"
        class="upload-preview-toolbar__btn"
      >
        <a-button type="primary">
          {{ t('component.upload.choose') }}
        </a-button>
      </Upload>
      <a-button
        v-if="isLazy"
        @click="handleStartUpload"
        color="success"
        :disabled="!getIsSelectFile"
        :loading="uploading"
        class="upload-preview-toolbar__upload"
      >
        {{ getUploadBtnText }}
      </a-button>
    </div>
    <div :class="listType === 'grid' ? 'overflow-y-auto' : 'overflow-x-auto'">
      <FileList
        :dataSource="fileItemList"
        :columns="columns"
        :actionColumn="actionColumn"
        :listType="listType"
        :dragSort="props.dragSort && !props.readonly"
        :emptyText="getEmptyText"
        :dropUpload="!props.readonly && !props.disabled"
        @reorder="handleReorder"
        @dropFiles="handleDropFiles"
      />
    </div>
  </div>
</template>
<script lang="ts" setup>
  /**
   * Copyright (c) 2013-Now https://jeesite.com All rights reserved.
   * No deletion without permission, or be held responsible to law.
   * @author ThinkGem
   */
  import { Upload, Alert } from 'antdv-next';
  import { uploadPreviewProps } from './props';
  import { useI18n } from '@jeesite/core/hooks/web/useI18n';
  import { useFileItemList } from './useFileItemList';
  import FileList from './FileList.vue';

  const props = defineProps(uploadPreviewProps);
  const emit = defineEmits(['change', 'delete']);

  const { t } = useI18n();

  const {
    fileItemList,
    columns,
    listType,
    actionColumn,
    getEmptyText,
    handleReorder,
    getStringAccept,
    getAlertTitle,
    beforeUpload,
    getUploadBtnText,
    getIsSelectFile,
    handleStartUpload,
    uploading,
  } = useFileItemList(props, emit, {
    embed: true,
  });

  // 从资源管理器拖入文件直接上传（与点击「选择」走同一 beforeUpload 逻辑）
  function handleDropFiles(files: File[]) {
    if (props.readonly) return;
    for (const file of files) {
      beforeUpload(file);
    }
  }
</script>
<style lang="less">
  .upload-preview {
    border: solid 1px @border-color-base;
    border-radius: 6px;
    padding: 10px;
  }

  .upload-preview-toolbar {
    display: flex;
    align-items: center;
    margin-bottom: 8px;

    &__btn {
      margin-left: 8px;
      text-align: right;
      flex: 1;
    }

    &__upload {
      margin-left: 8px;
    }

    .ant-alert.ant-alert-banner {
      border-radius: 8px;
    }
  }
</style>
