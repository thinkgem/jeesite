<template>
  <div :class="prefixCls" :style="getStyle" v-if="showFooter || $slots.footer">
    <template v-if="!$slots.footer">
      <slot name="insertFooter"></slot>
      <a-button v-bind="cancelButtonProps" @click="handleClose" class="mr-2" v-if="showCancelBtn">
        <Icon icon="ant-design:close-outlined" />
        {{
          cancelText || (getOkAuth && showOkBtn ? t('common.cancelText') : t('common.closeText'))
        }}
      </a-button>
      <slot name="centerFooter"></slot>
      <a-button
        :type="okType"
        @click="handleOk"
        v-bind="okButtonProps"
        class="mr-2"
        :loading="confirmLoading"
        v-if="showOkBtn && getOkAuth"
      >
        <Icon icon="ant-design:check-outlined" />
        {{ okText || t('common.okText') }}
      </a-button>
      <slot name="appendFooter"></slot>
    </template>

    <template v-else>
      <slot name="footer"></slot>
    </template>
  </div>
</template>
<script lang="ts">
  import type { CSSProperties } from 'vue';
  import { defineComponent, computed } from 'vue';
  import { useDesign } from '/@/hooks/web/useDesign';
  import { usePermission } from '/@/hooks/web/usePermission';
  import { useI18n } from '/@/hooks/web/useI18n';
  import { Icon } from '/@/components/Icon';

  import { footerProps } from '../props';
  export default defineComponent({
    name: 'BasicDrawerFooter',
    components: { Icon },
    props: {
      ...footerProps,
      height: {
        type: String,
        default: '60px',
      },
    },
    emits: ['ok', 'close'],
    setup(props, { emit }) {
      const { t } = useI18n();
      const { hasPermission } = usePermission();
      const { prefixCls } = useDesign('basic-drawer-footer');

      const getStyle = computed((): CSSProperties => {
        const heightStr = `${props.height}`;
        return {
          height: heightStr,
          lineHeight: heightStr,
        };
      });

      const getOkAuth = computed(() => {
        return hasPermission(props.okAuth);
      });

      function handleOk() {
        emit('ok');
      }

      function handleClose() {
        emit('close');
      }
      return { t, prefixCls, getStyle, getOkAuth, handleOk, handleClose };
    },
  });
</script>

<style lang="less">
  @prefix-cls: ~'jeesite-basic-drawer-footer';

  .@{prefix-cls} {
    position: absolute;
    bottom: 0;
    width: 100%;
    padding-right: 8px;
    text-align: right;
    background-color: @component-background;
    // border-top: 1px solid @border-color-base;
    z-index: 100; // 设置下，否则 BasicTable 空白图标会覆盖 actions 上边框线

    > * {
      margin-right: 8px;
    }
  }
</style>
