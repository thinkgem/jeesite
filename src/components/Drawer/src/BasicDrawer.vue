<template>
  <Drawer v-bind="getBindValues" :closable="false" @close="onClose">
    <template #title v-if="!$slots.title">
      <DrawerHeader
        :title="getMergeProps.title"
        :isDetail="isDetail"
        :showDetailBack="showDetailBack"
        @close="onClose"
      >
        <template #titleToolbar>
          <slot name="titleToolbar"></slot>
        </template>
      </DrawerHeader>
    </template>
    <template v-else #title>
      <slot name="title"></slot>
    </template>
    <template #extra>
      <Tooltip :title="t('component.drawer.cancelText')" placement="bottom">
        <CloseOutlined @click="onClose" />
      </Tooltip>
    </template>

    <ScrollContainer
      :style="getScrollContentStyle"
      v-loading="getLoading"
      :loading-tip="loadingText || t('common.loadingText')"
    >
      <slot></slot>
    </ScrollContainer>
    <DrawerFooter v-bind="getProps" @close="onClose" @ok="handleOk" :height="getFooterHeight">
      <template #[item]="data" v-for="item in Object.keys($slots)">
        <slot :name="item" v-bind="data || {}"></slot>
      </template>
    </DrawerFooter>
  </Drawer>
</template>
<script lang="ts">
  import type { DrawerInstance, DrawerProps } from './typing';
  import {
    defineComponent,
    ref,
    computed,
    watch,
    unref,
    toRaw,
    getCurrentInstance,
    CSSProperties,
    watchEffect,
  } from 'vue';
  import { Drawer } from 'ant-design-vue';
  import { useI18n } from '/@/hooks/web/useI18n';
  import { isFunction, isNumber } from '/@/utils/is';
  import { deepMerge } from '/@/utils';
  import { Tooltip } from 'ant-design-vue';
  import { CloseOutlined } from '@ant-design/icons-vue';
  import DrawerFooter from './components/DrawerFooter.vue';
  import DrawerHeader from './components/DrawerHeader.vue';
  import { ScrollContainer } from '/@/components/Container';
  import { basicProps } from './props';
  import { useDesign } from '/@/hooks/web/useDesign';
  import { useAttrs } from '/@/hooks/core/useAttrs';

  export default defineComponent({
    components: { Drawer, Tooltip, CloseOutlined, ScrollContainer, DrawerFooter, DrawerHeader },
    inheritAttrs: false,
    props: basicProps,
    emits: ['open-change', 'ok', 'close', 'register', 'update:open'],
    setup(props, { emit }) {
      const openRef = ref(false);
      const attrs = useAttrs();
      const propsRef = ref<Partial<Nullable<DrawerProps>>>(null);

      const { t } = useI18n();
      const { prefixVar, prefixCls } = useDesign('basic-drawer');

      const drawerInstance: DrawerInstance = {
        setDrawerProps: setDrawerProps,
        emitOpen: undefined,
      };

      const instance = getCurrentInstance();
      if (instance) {
        emit('register', drawerInstance, instance.uid);
      }

      const getMergeProps = computed((): DrawerProps => {
        return deepMerge(toRaw(props), unref(propsRef));
      });

      const getWrapClassName = computed(() => {
        return `${prefixCls} ${props.wrapClassName || ''}`;
      });

      const getProps = computed((): DrawerProps => {
        const opt = {
          placement: 'right',
          ...unref(attrs),
          ...unref(getMergeProps),
          open: unref(openRef),
        };
        opt.title = undefined;
        const { isDetail, width, class: wrapClassName, getContainer } = opt;
        if (isDetail) {
          if (!width) {
            opt.width = '100%';
          }
          const detailCls = `${prefixCls}__detail`;
          opt.class = wrapClassName ? `${wrapClassName} ${detailCls}` : detailCls;

          if (!getContainer) {
            // TODO type error?
            opt.getContainer = `.${prefixVar}-layout-content` as any;
          }
        }
        return {
          ...opt,
          class: unref(getWrapClassName),
        } as DrawerProps;
      });

      const getBindValues = computed((): DrawerProps => {
        const values = {
          ...attrs,
          ...unref(getProps),
          open: unref(openRef),
          class: unref(getWrapClassName),
        };
        delete values['wrapClassName'];
        return values;
      });

      // Custom implementation of the bottom button,
      const getFooterHeight = computed(() => {
        const { footerHeight, showFooter } = unref(getProps);
        if (showFooter && footerHeight) {
          return isNumber(footerHeight)
            ? `${footerHeight}px`
            : `${footerHeight.replace('px', '')}px`;
        }
        return `0px`;
      });

      const getScrollContentStyle = computed((): CSSProperties => {
        const footerHeight = unref(getFooterHeight);
        return {
          position: 'relative',
          height: `calc(100% - ${footerHeight})`,
        };
      });

      const getLoading = computed(() => {
        return !!unref(getProps)?.loading;
      });

      watchEffect(() => {
        openRef.value = !!props.open;
      });

      watch(
        () => unref(openRef),
        (v) => {
          emit('open-change', v);
          emit('update:open', v);
          instance && drawerInstance.emitOpen?.(v, instance.uid);
        },
        {
          immediate: false,
        },
      );

      // Cancel event
      async function onClose(e: Recordable) {
        const { closeFunc } = unref(getProps);
        if (closeFunc && isFunction(closeFunc)) {
          const res = await closeFunc();
          openRef.value = !res;
          return;
        }
        openRef.value = false;
        emit('close', e);
      }

      function setDrawerProps(props: Partial<DrawerProps>): void {
        if (typeof props.loading != 'undefined') {
          props.confirmLoading = props.loading;
        }
        // Keep the last setDrawerProps
        propsRef.value = deepMerge(unref(propsRef) || ({} as any), props);

        if (Reflect.has(props, 'open')) {
          openRef.value = !!props.open;
        }
      }

      function handleOk(e: Event) {
        emit('ok', e);
      }

      return {
        onClose,
        t,
        prefixCls,
        getMergeProps: getMergeProps as any,
        getScrollContentStyle,
        getProps: getProps as any,
        getLoading,
        getBindValues,
        getFooterHeight,
        handleOk,
      };
    },
  });
</script>
<style lang="less">
  @header-height: 60px;
  @detail-header-height: 40px;
  @prefix-cls: ~'jeesite-basic-drawer';
  @prefix-cls-detail: ~'jeesite-basic-drawer__detail';

  .ant-drawer .@{prefix-cls} {
    overflow: hidden;
    .ant-drawer-wrapper-body {
      overflow: hidden;
    }

    .ant-drawer-title {
      font-weight: normal;
      .anticon {
        color: @primary-color;
      }
    }

    .ant-drawer-extra .anticon-close {
      opacity: 0.6;
      &:hover {
        color: @error-color;
        opacity: 1;
      }
    }

    .ant-drawer-body {
      height: calc(100% - @header-height);
      padding: 0;
      background-color: @component-background;

      > .scrollbar > .scrollbar__wrap {
        padding: 16px !important;
        margin-bottom: 0 !important;
      }

      > .scrollbar > .scrollbar__bar.is-horizontal {
        display: none;
      }
    }
  }

  .@{prefix-cls-detail} {
    position: absolute;

    .ant-drawer-header {
      width: 100%;
      height: @detail-header-height;
      padding: 0;
      border-top: 1px solid @border-color-base;
      box-sizing: border-box;
    }

    .ant-drawer-title {
      height: 100%;
    }

    .ant-drawer-close {
      height: @detail-header-height;
      line-height: @detail-header-height;
    }

    .scrollbar__wrap {
      padding: 0 !important;
    }

    .ant-drawer-body {
      height: calc(100% - @detail-header-height);
    }
  }
</style>
