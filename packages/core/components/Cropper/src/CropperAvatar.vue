<template>
  <div :class="getClass" :style="getStyle">
    <div :class="`${prefixCls}-image-wrapper`" :style="getImageWrapperStyle" @click="openModal">
      <div :class="`${prefixCls}-image-mask`" :style="getImageWrapperStyle">
        <Icon
          icon="i-ant-design:cloud-upload-outlined"
          :size="getIconWidth"
          :style="getImageWrapperStyle"
          color="#d6d6d6"
        />
      </div>
      <img :src="sourceValue" v-if="sourceValue" alt="avatar" />
    </div>
    <a-button :class="`${prefixCls}-upload-btn`" @click="openModal" v-if="showBtn" v-bind="btnProps">
      {{ btnText ? btnText : t('component.cropper.selectImage') }}
    </a-button>

    <CopperModal
      @register="register"
      @upload-success="handleUploadSuccess"
      :uploadApi="uploadApi"
      :value="sourceValue"
    />
  </div>
</template>
<script lang="ts">
  import { defineComponent, computed, CSSProperties, unref, ref, watchEffect, watch, PropType } from 'vue';
  import CopperModal from './CopperModal.vue';
  import { useDesign } from '@jeesite/core/hooks/web/useDesign';
  import { useModal } from '@jeesite/core/components/Modal';
  // import { useMessage } from '@jeesite/core/hooks/web/useMessage';
  import { useI18n } from '@jeesite/core/hooks/web/useI18n';
  import type { ButtonProps } from '@jeesite/core/components/Button';
  import { Icon } from '@jeesite/core/components/Icon';

  export default defineComponent({
    name: 'CropperAvatar',
    components: { CopperModal, Icon },
    props: {
      width: { type: [String, Number], default: '200px' },
      value: { type: String },
      showBtn: { type: Boolean, default: true },
      btnProps: { type: Object as PropType<ButtonProps> },
      btnText: { type: String, default: '' },
      uploadApi: {
        type: Function as PropType<({ file, name }: { file: Blob; name: string }) => Promise<void>>,
      },
    },
    emits: ['update:value', 'change'],
    setup(props, { emit, expose }) {
      const sourceValue = ref(props.value || '');
      const { prefixCls } = useDesign('cropper-avatar');
      const [register, { openModal, closeModal }] = useModal();
      // const { createMessage } = useMessage();
      const { t } = useI18n();

      const getClass = computed(() => [prefixCls]);

      const getWidth = computed(() => `${props.width}`.replace(/px/, '') + 'px');

      const getIconWidth = computed(() => parseInt(`${props.width}`.replace(/px/, '')) / 2 + 'px');

      const getStyle = computed((): CSSProperties => ({ width: unref(getWidth) }));

      const getImageWrapperStyle = computed((): CSSProperties => ({ width: unref(getWidth), height: unref(getWidth) }));

      watchEffect(() => {
        sourceValue.value = props.value || '';
      });

      watch(
        () => sourceValue.value,
        (v: string) => {
          emit('update:value', v);
        },
      );

      function handleUploadSuccess({ source }) {
        sourceValue.value = source;
        emit('change', source);
        // createMessage.success(t('component.cropper.uploadSuccess'));
      }

      expose({ openModal: openModal.bind(null, true), closeModal });

      return {
        t,
        prefixCls,
        register,
        openModal: openModal as any,
        getIconWidth,
        sourceValue,
        getClass,
        getImageWrapperStyle,
        getStyle,
        handleUploadSuccess,
      };
    },
  });
</script>

<style lang="less">
  @prefix-cls: ~'jeesite-cropper-avatar';

  .@{prefix-cls} {
    display: inline-block;
    text-align: center;

    &-image-wrapper {
      overflow: hidden;
      cursor: pointer;
      background: @component-background;
      border: 1px solid @border-color-base;
      border-radius: 50%;

      img {
        width: 100%;
      }
    }

    &-image-mask {
      opacity: 0;
      position: absolute;
      width: inherit;
      height: inherit;
      border-radius: inherit;
      border: inherit;
      background: rgb(0 0 0 / 40%);
      cursor: pointer;
      transition: opacity 0.4s;

      ::v-deep(svg) {
        margin: auto;
      }
    }

    &-image-mask:hover {
      opacity: 40;
    }

    &-upload-btn {
      margin: 10px auto;
    }
  }
</style>
