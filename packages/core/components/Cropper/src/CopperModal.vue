<!--
 * Copyright (c) 2013-Now https://jeesite.com All rights reserved.
 * No deletion without permission, or be held responsible to law.
 * @author ThinkGem
-->
<template>
  <BasicModal
    v-bind="$attrs"
    @register="register"
    :title="t('component.cropper.modalTitle')"
    width="800px"
    :canFullscreen="false"
    @ok="handleOk"
    :okText="t('component.cropper.okText')"
  >
    <div class="jeesite-cropper-am">
      <div class="jeesite-cropper-am-left">
        <div class="jeesite-cropper-am-cropper">
          <CropperImage
            v-if="src"
            :src="src"
            height="300px"
            :circled="circled"
            @cropend="handleCropend"
            @ready="handleReady"
          />
        </div>

        <div class="jeesite-cropper-am-toolbar">
          <Upload :fileList="[]" accept="image/*" :beforeUpload="handleBeforeUpload">
            <Tooltip :title="t('component.cropper.selectImage')" placement="bottom">
              <a-button size="small" preIcon="i-ant-design:upload-outlined" type="primary" />
            </Tooltip>
          </Upload>
          <Space>
            <Tooltip :title="t('component.cropper.btn_reset')" placement="bottom">
              <a-button
                type="primary"
                preIcon="i-ant-design:reload-outlined"
                size="small"
                :disabled="!src"
                @click="handlerToolbar('reset')"
              />
            </Tooltip>
            <Tooltip :title="t('component.cropper.btn_rotate_left')" placement="bottom">
              <a-button
                type="primary"
                preIcon="i-ant-design:rotate-left-outlined"
                size="small"
                :disabled="!src"
                @click="handlerToolbar('rotate', -45)"
              />
            </Tooltip>
            <Tooltip :title="t('component.cropper.btn_rotate_right')" placement="bottom">
              <a-button
                type="primary"
                preIcon="i-ant-design:rotate-right-outlined"
                size="small"
                :disabled="!src"
                @click="handlerToolbar('rotate', 45)"
              />
            </Tooltip>
            <Tooltip :title="t('component.cropper.btn_scale_x')" placement="bottom">
              <a-button
                type="primary"
                preIcon="i-vaadin:arrows-long-h"
                size="small"
                :disabled="!src"
                @click="handlerToolbar('scaleX')"
              />
            </Tooltip>
            <Tooltip :title="t('component.cropper.btn_scale_y')" placement="bottom">
              <a-button
                type="primary"
                preIcon="i-vaadin:arrows-long-v"
                size="small"
                :disabled="!src"
                @click="handlerToolbar('scaleY')"
              />
            </Tooltip>
            <Tooltip :title="t('component.cropper.btn_zoom_in')" placement="bottom">
              <a-button
                type="primary"
                preIcon="i-ant-design:zoom-in-outlined"
                size="small"
                :disabled="!src"
                @click="handlerToolbar('zoom', 0.1)"
              />
            </Tooltip>
            <Tooltip :title="t('component.cropper.btn_zoom_out')" placement="bottom">
              <a-button
                type="primary"
                preIcon="i-ant-design:zoom-out-outlined"
                size="small"
                :disabled="!src"
                @click="handlerToolbar('zoom', -0.1)"
              />
            </Tooltip>
          </Space>
        </div>
      </div>
      <div class="jeesite-cropper-am-right">
        <div class="jeesite-cropper-am-preview">
          <img :src="previewSource" v-if="previewSource" :alt="t('component.cropper.preview')" />
        </div>
        <template v-if="previewSource">
          <div class="jeesite-cropper-am-group">
            <Avatar :src="previewSource" size="large" />
            <Avatar :src="previewSource" :size="48" />
            <Avatar :src="previewSource" :size="64" />
            <Avatar :src="previewSource" :size="80" />
          </div>
        </template>
      </div>
    </div>
  </BasicModal>
</template>
<script lang="ts" setup name="CropperModal">
  import { ref } from 'vue';
  import CropperImage from './Cropper.vue';
  import { Space, Upload, Avatar, Tooltip } from 'antdv-next';
  import { BasicModal, useModalInner } from '@jeesite/core/components/Modal';
  // import { dataURLtoBlob } from '@jeesite/core/utils/file/base64Conver';
  // import { isFunction } from '@jeesite/core/utils/is';
  import { useI18n } from '@jeesite/core/hooks/web/useI18n';

  import type { CropendResult, CropperToolbarApi } from './typing';
  type apiFunParams = { file: Blob; name: string; filename: string };

  const props = defineProps({
    value: { type: String, default: '' },
    circled: { type: Boolean, default: true },
    uploadApi: {
      type: Function as PropType<(params: apiFunParams) => Promise<any>>,
    },
  });

  const emit = defineEmits(['upload-success', 'register']);

  const src = ref(props.value);
  const previewSource = ref('');
  const cropper = ref<CropperToolbarApi>();
  let filename = '';

  const [register, { closeModal, setModalProps }] = useModalInner();
  const { t } = useI18n();

  // Block upload
  function handleBeforeUpload(file: File) {
    const reader = new FileReader();
    reader.readAsDataURL(file);
    src.value = '';
    previewSource.value = '';
    reader.onload = function (e) {
      src.value = (e.target?.result as string) ?? '';
      filename = file.name;
    };
    return false;
  }

  function handleCropend({ imgBase64 }: CropendResult) {
    previewSource.value = imgBase64;
  }

  function handleReady(cropperInstance: CropperToolbarApi) {
    cropper.value = cropperInstance;
  }

  function handlerToolbar(event: string, arg?: number) {
    cropper?.value?.[event]?.(arg);
  }

  async function handleOk() {
    try {
      setModalProps({ confirmLoading: true });
      // const uploadApi = props.uploadApi;
      // if (uploadApi && isFunction(uploadApi)) {
      //   const blob = dataURLtoBlob(previewSource.value);
      //   const result = await uploadApi({ name: 'file', file: blob, filename });
      //   emit('upload-success', { source: previewSource.value, data: result.data });
      // }
      emit('upload-success', { source: previewSource.value, filename });
      closeModal();
    } finally {
      setModalProps({ confirmLoading: false });
    }
  }
</script>

<style lang="less">
  .jeesite-cropper-am {
    display: flex;

    &-left,
    &-right {
      height: 340px;
    }

    &-left {
      width: 55%;
    }

    &-right {
      width: 45%;
    }

    &-cropper {
      height: 300px;
      background: #eee;
      background-image:
        linear-gradient(45deg, rgb(0 0 0 / 25%) 25%, transparent 0, transparent 75%, rgb(0 0 0 / 25%) 0),
        linear-gradient(45deg, rgb(0 0 0 / 25%) 25%, transparent 0, transparent 75%, rgb(0 0 0 / 25%) 0);
      background-position:
        0 0,
        12px 12px;
      background-size: 24px 24px;
    }

    &-toolbar {
      display: flex;
      justify-content: space-between;
      align-items: center;
      margin-top: 10px;
    }

    &-preview {
      width: 220px;
      height: 220px;
      margin: 0 auto;
      overflow: hidden;
      border: 1px solid @border-color-base;
      border-radius: 50%;

      img {
        width: 100%;
        height: 100%;
      }
    }

    &-group {
      display: flex;
      padding-top: 8px;
      margin-top: 8px;
      border-top: 1px solid @border-color-base;
      justify-content: space-around;
      align-items: center;
    }
  }

  html[data-theme='dark'] {
    .jeesite-cropper-am {
      &-cropper {
        background: #1f1f1f;
        background-image:
          linear-gradient(45deg, rgb(255 255 255 / 8%) 25%, transparent 0, transparent 75%, rgb(255 255 255 / 8%) 0),
          linear-gradient(45deg, rgb(255 255 255 / 8%) 25%, transparent 0, transparent 75%, rgb(255 255 255 / 8%) 0);
        background-position:
          0 0,
          12px 12px;
        background-size: 24px 24px;
      }
    }
  }
</style>
