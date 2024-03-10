<!--
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 * No deletion without permission, or be held responsible to law.
 * @author ThinkGem
-->
<template>
  <BasicModal
    v-bind="$attrs"
    :title="t('导入机构数据')"
    :okText="t('导入')"
    @register="registerModal"
    @ok="handleSubmit"
    :minHeight="120"
    :width="400"
  >
    <Upload
      accept=".xls,.xlsx"
      :file-list="fileList"
      :before-upload="beforeUpload"
      @remove="handleRemove"
    >
      <a-button> <Icon icon="ant-design:upload-outlined" /> {{ t('选择文件') }} </a-button>
      <span class="ml-4">{{ uploadInfo }}</span>
    </Upload>
    <div class="mt-4">
      <Checkbox v-model:checked="updateSupport">
        <Tooltip placement="bottom" :mouse-enter-delay="1" :overlay-style="{ width: '100px' }">
          <template #title>
            {{ t('如果机构编码已经存在，更新这条数据。') }}
          </template>
          {{ t('是否更新已经存在的机构数据') }}
        </Tooltip>
      </Checkbox>
      <a-button @click="handleDownloadTemplate()" type="text">
        <Icon icon="fa:file-excel-o" />
        {{ t('下载模板') }}
      </a-button>
    </div>
    <div class="mt-4">
      {{ t('提示：仅允许导入“xls”或“xlsx”格式文件！') }}
    </div>
  </BasicModal>
</template>
<script lang="ts" setup name="ViewsSysOfficeForm">
  import { ref } from 'vue';
  import { Upload, Checkbox, Tooltip } from 'ant-design-vue';
  import { useI18n } from '/@/hooks/web/useI18n';
  import { useMessage } from '/@/hooks/web/useMessage';
  import { useGlobSetting } from '/@/hooks/setting';
  import { downloadByUrl } from '/@/utils/file/download';
  import { Icon } from '/@/components/Icon';
  import { BasicModal, useModalInner } from '/@/components/Modal';
  import { officeImportData } from '/@/api/sys/office';
  import { FileType } from 'ant-design-vue/es/upload/interface';
  import { AxiosProgressEvent } from 'axios';

  const emit = defineEmits(['success', 'register']);

  const { t } = useI18n('sys.office');
  const { showMessage, showMessageModal } = useMessage();

  const fileList = ref<FileType[]>([]);
  const updateSupport = ref(false);
  const uploadInfo = ref('');

  const beforeUpload = (file: FileType) => {
    fileList.value = [file];
    return false;
  };

  const handleRemove = () => {
    fileList.value = [];
  };

  const [registerModal, { setModalProps, closeModal }] = useModalInner(() => {
    fileList.value = [];
    updateSupport.value = false;
    uploadInfo.value = '';
  });

  async function handleDownloadTemplate() {
    const { ctxAdminPath } = useGlobSetting();
    downloadByUrl({ url: ctxAdminPath + '/sys/office/importTemplate' });
  }

  function onUploadProgress(progressEvent: AxiosProgressEvent) {
    const complete = ((progressEvent.loaded / (progressEvent.total || 1)) * 100) | 0;
    if (complete != 100) {
      uploadInfo.value = t('正在导入，请稍后') + ' ' + complete + '%...';
    } else {
      uploadInfo.value = '';
    }
  }

  async function handleSubmit() {
    try {
      if (fileList.value.length == 0) {
        showMessage(t('请选择要导入的数据文件'));
        return;
      }
      setModalProps({ confirmLoading: true });
      const params = {
        file: fileList.value[0],
        updateSupport: updateSupport.value ? '1' : '0',
      };
      const { data } = await officeImportData(params, onUploadProgress);
      showMessageModal({ content: data.message });
      setTimeout(closeModal);
      emit('success');
    } catch (error: any) {
      if (error && error.errorFields) {
        showMessage(t('common.validateError'));
      }
      console.log('error', error);
    } finally {
      setModalProps({ confirmLoading: false });
    }
  }
</script>
