<template>
  <div :class="getClass">
    <template v-if="canFullscreen">
      <Tooltip :title="t('component.modal.restore')" placement="bottom" v-if="fullScreen">
        <Icon icon="i-ant-design:fullscreen-exit-outlined" role="full" @click="handleFullScreen" />
      </Tooltip>
      <Tooltip :title="t('component.modal.maximize')" placement="bottom" v-else>
        <Icon icon="i-ant-design:fullscreen-outlined" role="close" @click="handleFullScreen" />
      </Tooltip>
    </template>
    <Tooltip :title="t('component.modal.close')" placement="bottom">
      <Icon
        icon="i-ant-design:close-outlined"
        class="anticon-close cursor-pointer"
        @click="handleCancel"
      />
    </Tooltip>
  </div>
</template>
<script lang="ts">
  import { defineComponent, computed } from 'vue';
  import { Icon } from '@jeesite/core/components/Icon';
  import { useDesign } from '@jeesite/core/hooks/web/useDesign';
  import { Tooltip } from 'ant-design-vue';
  import { useI18n } from '@jeesite/core/hooks/web/useI18n';

  export default defineComponent({
    name: 'ModalClose',
    components: { Tooltip, Icon },
    props: {
      canFullscreen: { type: Boolean, default: true },
      fullScreen: { type: Boolean },
    },
    emits: ['cancel', 'fullscreen'],
    setup(props, { emit }) {
      const { prefixCls } = useDesign('basic-modal-close');
      const { t } = useI18n();

      const getClass = computed(() => {
        return [
          prefixCls,
          `${prefixCls}--custom`,
          {
            [`${prefixCls}--can-full`]: props.canFullscreen,
          },
        ];
      });

      function handleCancel(e: Event) {
        emit('cancel', e);
      }

      function handleFullScreen(e: Event) {
        e?.stopPropagation();
        e?.preventDefault();
        emit('fullscreen');
      }

      return {
        t,
        getClass,
        prefixCls,
        handleCancel,
        handleFullScreen,
      };
    },
  });
</script>
<style lang="less">
  @prefix-cls: ~'jeesite-basic-modal-close';
  .@{prefix-cls} {
    float: right;
    text-align: right;
    margin-right: 10px;

    .jeesite-icon {
      padding: 16px 9px;
      font-size: 18px;
      color: @text-color-base !important;
      cursor: pointer;
      opacity: 0.6;

      &:hover {
        opacity: 1;
      }
    }

    .anticon-close {
      &:hover {
        color: @error-color !important;
        opacity: 1;
      }
    }
  }
</style>
