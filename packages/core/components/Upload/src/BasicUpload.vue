<template>
  <div class="jeesite-basic-upload">
    <Space>
      <a-button
        v-if="!(readonly || disabled)"
        type="primary"
        @click="openUploadModal"
        preIcon="i-carbon:cloud-upload"
        :size="props.size"
      >
        {{ uploadText || t('component.upload.upload') }}
      </a-button>
      <Tooltip placement="bottom" v-if="getShowPreview">
        <template #title>
          {{ t('component.upload.uploaded') }}
          <template v-if="fileList.length">
            {{ fileList.length }}
          </template>
        </template>
        <a-button @click="openPreviewModal" :size="size">
          <Icon icon="i-bi:eye" />
          <template v-if="fileList.length && showPreviewNumber">
            {{ fileList.length }}
          </template>
        </a-button>
      </Tooltip>
    </Space>
    <UploadModal
      v-bind="bindValue"
      :previewFileList="fileList"
      :apiUploadUrl="apiUploadUrl"
      :apiDownloadUrl="apiDownloadUrl"
      @register="registerUploadModal"
      @change="handleChange"
      @delete="handleDelete"
    />
    <UploadPreviewModal
      :value="fileList"
      :readonly="readonly || disabled"
      :imageThumbName="imageThumbName"
      :apiDownloadUrl="apiDownloadUrl"
      @register="registerPreviewModal"
      @change="handlePreviewChange"
      @delete="handleDelete"
    />
  </div>
</template>
<script lang="ts" setup name="BasicUpload">
  import { defineComponent, ref, watch, unref, computed, useAttrs } from 'vue';
  import UploadModal from './UploadModal.vue';
  import UploadPreviewModal from './UploadPreviewModal.vue';
  import { Icon } from '@jeesite/core/components/Icon';
  import { Tooltip, Space } from 'ant-design-vue';
  import { useModal } from '@jeesite/core/components/Modal';
  import { uploadContainerProps } from './props';
  import { omit } from 'lodash-es';
  import { useI18n } from '@jeesite/core/hooks/web/useI18n';
  import { isArray } from '@jeesite/core/utils/is';
  import { FileUpload, uploadFileList } from '@jeesite/core/api/sys/upload';

  const props = defineProps(uploadContainerProps);
  const emit = defineEmits(['change', 'delete', 'update:value', 'click']);
  const attrs = useAttrs();

  const { t } = useI18n();
  const [registerUploadModal, { openModal }] = useModal();
  const [registerPreviewModal, { openModal: openPreviewModal }] = useModal();

  function openUploadModal() {
    openModal();
    emit('click');
  }

  const dataMap = ref<object>({});
  const fileList = ref<FileUpload[]>([]);
  const fileListDel = ref<FileUpload[]>([]);

  const getShowPreview = computed(() => {
    const { showPreview, emptyHidePreview } = props;
    if (!showPreview) return false;
    if (!emptyHidePreview) return true;
    return emptyHidePreview ? fileList.value.length > 0 : true;
  });

  const bindValue = computed(() => {
    const value = { ...attrs, ...props };
    return omit(value, 'onChange');
  });

  watch(
    () => props.value,
    (value) => {
      dataMap.value = value;
      emit('update:value', dataMap.value);
    },
    { immediate: true },
  );

  watch(
    () => [props.bizKey, props.loadTime],
    () => {
      loadFileList();
    },
    { immediate: true },
  );

  function loadFileList() {
    fileList.value = [];
    fileListDel.value = [];
    if (props.bizKey != '') {
      uploadFileList(
        {
          bizKey: props.bizKey,
          bizType: props.bizType,
        },
        props.apiFileListUrl,
      ).then((res) => {
        if (isArray(res)) {
          fileList.value = res;
          dataMap.value[props.bizType + '__len'] = fileList.value.length;
          emit('update:value', dataMap.value);
          emit('change', dataMap.value);
        }
      });
    }
  }

  // 上传modal保存操作
  function handleChange(records: FileUpload[]) {
    fileList.value = [...unref(fileList), ...(records || [])];
    dataMap.value[props.bizType] = fileList.value.map((item) => item.id).join(',');
    dataMap.value[props.bizType + '__len'] = fileList.value.length;
    emit('update:value', dataMap.value);
    emit('change', dataMap.value, fileList.value);
  }

  // 预览modal保存操作
  function handlePreviewChange(records: FileUpload[]) {
    fileList.value = [...(records || [])];
    dataMap.value[props.bizType] = fileList.value.map((item) => item.id).join(',');
    dataMap.value[props.bizType + '__len'] = fileList.value.length;
    emit('update:value', dataMap.value);
    emit('change', dataMap.value, fileList.value);
  }

  function handleDelete(record: FileUpload) {
    fileListDel.value.push(record);
    dataMap.value[props.bizType + '__del'] = fileListDel.value.map((item) => item.id).join(',');
    dataMap.value[props.bizType + '__len'] = fileList.value.length;
    emit('delete', record);
    emit('update:value', dataMap.value);
    emit('change', dataMap.value, fileList.value);
  }
</script>
