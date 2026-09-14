<template>
  <div class="jeesite-basic-upload">
    <Space>
      <a-button
        v-if="!props.showPreviewList && !props.readonly"
        :type="getButtonType"
        :disabled="props.disabled"
        :size="props.size"
        @click="openUploadModal"
      >
        <Icon :icon="getButtonIcon" />
        <span v-if="showUploadText" class="pl-1">{{ getButtonText }}</span>
      </a-button>
      <Tooltip placement="bottom" v-if="!props.showPreviewList && getShowNumber">
        <template #title>
          {{ t('component.upload.uploaded') }}
          <template v-if="props.showPreviewNumber">
            <span class="ml-1">{{ fileList.length }}</span>
          </template>
        </template>
        <a-button @click="openUploadModal" :size="props.size">
          <Icon icon="i-bi:eye" />
          <template v-if="props.showPreviewNumber">
            <span class="ml-1">{{ fileList.length }}</span>
          </template>
        </a-button>
      </Tooltip>
    </Space>
    <UploadModal
      v-bind="bindValue"
      :previewFileList="fileList"
      :apiUploadUrl="apiUploadUrl"
      :apiDownloadUrl="apiDownloadUrl"
      :readonly="previewOnly"
      @register="registerUploadModal"
      @change="handleChange"
      @delete="handleDelete"
    />
    <UploadPreview
      v-if="props.showPreviewList"
      v-bind="bindValue"
      :previewFileList="fileList"
      :readonly="previewOnly"
      :imageThumbName="imageThumbName"
      :apiDownloadUrl="apiDownloadUrl"
      @change="handlePreviewChange"
      @delete="handleDelete"
    />
  </div>
</template>
<script lang="ts" setup name="BasicUpload">
  import { ref, watch, computed, useAttrs } from 'vue';
  import { Tooltip, Space } from 'antdv-next';
  import { Icon } from '@jeesite/core/components/Icon';
  import { useModal } from '@jeesite/core/components/Modal';
  import { uploadContainerProps } from './props';
  import { omit } from 'lodash-es';
  import { useI18n } from '@jeesite/core/hooks/web/useI18n';
  import { isArray } from '@jeesite/core/utils/is';
  import { FileUpload, uploadFileList } from '@jeesite/core/api/sys/upload';
  import UploadModal from './UploadModal.vue';
  import UploadPreview from './UploadPreview.vue';

  const props = defineProps(uploadContainerProps);
  const emit = defineEmits(['change', 'delete', 'update:value', 'click']);
  const attrs = useAttrs();

  const { t } = useI18n();
  const [registerUploadModal, { openModal }] = useModal();

  function openUploadModal() {
    openModal(true, { loadTime: new Date().getTime() });
    emit('click');
  }

  const dataMap = ref<object>({});
  const fileList = ref<FileUpload[]>([]);
  const fileListDel = ref<FileUpload[]>([]);

  // 只读或禁用时，只能预览，不能上传
  const previewOnly = computed(() => props.readonly || props.disabled);

  // 只读时，上传按钮显示为预览样式
  const getButtonIcon = computed(() => (props.readonly ? 'i-bi:eye' : 'i-carbon:cloud-upload'));

  const getButtonType = computed(() => (props.readonly ? 'default' : props.uploadButtonType));

  const getButtonText = computed(() => {
    if (props.uploadText) return props.uploadText;
    return props.readonly ? t('component.upload.view') : t('component.upload.upload');
  });

  // 上传按钮后面显示上传个数
  const getShowNumber = computed(() => {
    const { showPreview, showPreviewList, showPreviewNumber } = props;
    if (showPreviewList && !showPreviewNumber) return false;
    return showPreview && fileList.value.length;
  });

  const bindValue = computed(() => {
    const value = { ...attrs, ...props };
    return omit(value, 'onChange', 'class');
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
        }
        dataMap.value[props.bizType + '__len'] = fileList.value.length;
        emit('update:value', dataMap.value);
        emit('change', dataMap.value, fileList.value);
      });
    }
  }

  // 上传modal保存操作（记录完整顺序，含已上传文件与本次上传成功文件，替换而非追加）
  function handleChange(records: FileUpload[]) {
    fileList.value = [...(records || [])];
    dataMap.value[props.bizType] = fileList.value.map((item) => item.id).join(',');
    dataMap.value[props.bizType + '__len'] = fileList.value.length;
    emit('update:value', dataMap.value);
    emit('change', dataMap.value, fileList.value);
  }

  // 预览列表保存操作
  function handlePreviewChange(records: FileUpload[]) {
    fileList.value = [...(records || [])];
    dataMap.value[props.bizType] = fileList.value.map((item) => item.id).join(',');
    dataMap.value[props.bizType + '__len'] = fileList.value.length;
    emit('update:value', dataMap.value);
    emit('change', dataMap.value, fileList.value);
  }

  function handleDelete(record: FileUpload) {
    const index = fileList.value.findIndex((item) => item.id === record.id);
    if (index !== -1) {
      fileList.value.splice(index, 1);
    }
    fileListDel.value.push(record);
    dataMap.value[props.bizType + '__del'] = fileListDel.value.map((item) => item.id).join(',');
    dataMap.value[props.bizType + '__len'] = fileList.value.length;
    emit('delete', record);
    emit('update:value', dataMap.value);
    emit('change', dataMap.value, fileList.value);
  }
</script>
