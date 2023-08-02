<template>
  <div>
    <Space>
      <a-button
        v-if="!(readonly || disabled)"
        type="primary"
        @click="openUploadModal"
        preIcon="carbon:cloud-upload"
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
        <a-button @click="openPreviewModal">
          <Icon icon="bi:eye" />
          <template v-if="fileList.length && showPreviewNumber">
            {{ fileList.length }}
          </template>
        </a-button>
      </Tooltip>
    </Space>
    <UploadModal
      v-bind="bindValue"
      :previewFileList="fileList"
      @register="registerUploadModal"
      @change="handleChange"
      @delete="handleDelete"
    />
    <UploadPreviewModal
      :value="fileList"
      :readonly="readonly || disabled"
      :imageThumbName="imageThumbName"
      @register="registerPreviewModal"
      @change="handlePreviewChange"
      @delete="handleDelete"
    />
  </div>
</template>
<script lang="ts">
  import { defineComponent, ref, watch, unref, computed } from 'vue';
  import UploadModal from './UploadModal.vue';
  import UploadPreviewModal from './UploadPreviewModal.vue';
  import { Icon } from '/@/components/Icon';
  import { Tooltip, Space } from 'ant-design-vue';
  import { useModal } from '/@/components/Modal';
  import { uploadContainerProps } from './props';
  import { omit } from 'lodash-es';
  import { useI18n } from '/@/hooks/web/useI18n';
  import { isArray } from '/@/utils/is';
  import { FileUpload, uploadFileList } from '/@/api/sys/upload';

  export default defineComponent({
    name: 'BasicUpload',
    components: { UploadModal, Space, UploadPreviewModal, Icon, Tooltip },
    props: uploadContainerProps,
    emits: ['change', 'delete', 'update:value', 'click'],

    setup(props, { emit, attrs }) {
      const { t } = useI18n();
      const [registerUploadModal, { openModal }] = useModal();
      const [registerPreviewModal, { openModal: openPreviewModal }] = useModal();

      function openUploadModal() {
        openModal();
        emit('click');
      }

      const dataMap = ref<Object>({});
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
          uploadFileList({
            bizKey: props.bizKey,
            bizType: props.bizType,
          }).then((res) => {
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
        emit('change', dataMap.value);
      }

      // 预览modal保存操作
      function handlePreviewChange(records: FileUpload[]) {
        fileList.value = [...(records || [])];
        dataMap.value[props.bizType] = fileList.value.map((item) => item.id).join(',');
        dataMap.value[props.bizType + '__len'] = fileList.value.length;
        emit('update:value', dataMap.value);
        emit('change', dataMap.value);
      }

      function handleDelete(record: FileUpload) {
        fileListDel.value.push(record);
        dataMap.value[props.bizType + '__del'] = fileListDel.value.map((item) => item.id).join(',');
        dataMap.value[props.bizType + '__len'] = fileList.value.length;
        emit('delete', record);
        emit('update:value', dataMap.value);
        emit('change', dataMap.value);
      }

      return {
        registerUploadModal,
        openUploadModal,
        handleChange,
        handlePreviewChange,
        registerPreviewModal,
        openPreviewModal,
        fileList,
        getShowPreview,
        bindValue,
        handleDelete,
        t,
      };
    },
  });
</script>
