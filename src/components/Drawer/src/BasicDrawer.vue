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
        <Icon
          icon="i-ant-design:close-outlined"
          class="anticon-close cursor-pointer"
          @click="onClose"
        />
      </Tooltip>
    </template>
    <div v-if="widthResize" class="ew-resize" @mousedown="onMousedown"></div>
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
<script lang="ts" setup>
  import type { DrawerInstance, DrawerProps } from './typing';
  import {
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
  import { Icon } from '/@/components/Icon';
  import DrawerFooter from './components/DrawerFooter.vue';
  import DrawerHeader from './components/DrawerHeader.vue';
  import { ScrollContainer } from '/@/components/Container';
  import { basicProps } from './props';
  import { useDesign } from '/@/hooks/web/useDesign';
  import { useAttrs } from '/@/hooks/core/useAttrs';

  defineOptions({
    name: 'BasicDrawer',
    inheritAttrs: false,
  });

  const props = defineProps(basicProps);
  const emit = defineEmits(['open-change', 'ok', 'close', 'register', 'update:open']);
  const attrs = useAttrs();
  const openRef = ref(false);
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
      ...deepMerge(toRaw(props), unref(propsRef)),
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
    } else {
      opt.class = unref(getWrapClassName);
    }
    return opt as DrawerProps;
  });

  const getBindValues = computed((): DrawerProps => {
    const values = {
      ...attrs,
      ...unref(getProps),
      open: unref(openRef),
    } as any;
    if (typeof values?.width === 'string') {
      let width = Number(values.width);
      if (!isNaN(width)) values.width = width;
    }
    delete values['wrapClassName'];
    return values;
  });

  // Custom implementation of the bottom button,
  const getFooterHeight = computed(() => {
    const { footerHeight, showFooter } = unref(getProps);
    if (showFooter && footerHeight) {
      return isNumber(footerHeight) ? `${footerHeight}px` : `${footerHeight.replace('px', '')}px`;
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

  function handleOk(e: Event) {
    emit('ok', e);
  }

  const onMousedown = function (e) {
    const wrapper = e.target.closest('.ant-drawer-content-wrapper') as HTMLElement;
    if (!wrapper) return;
    const w = wrapper.clientWidth;
    const x = e.clientX;
    const l = e.target.offsetLeft;
    let isDown = true;
    window.onmousemove = (e) => {
      if (!isDown) {
        return;
      }
      const nl = e.clientX - (x - l);
      wrapper.style.width = w - nl + 'px';
    };
    window.onmouseup = () => {
      window.onmousemove = null;
    };
  };

  function setDrawerProps(props: Partial<DrawerProps>): void {
    if (typeof props.loading != 'undefined') {
      props.confirmLoading = props.loading;
    }
    // Keep the last setDrawerProps
    // propsRef.value = deepMerge(unref(propsRef) || ({} as any), props);
    propsRef.value = { ...(unref(propsRef) as Recordable), ...props } as Recordable;
    if (Reflect.has(props, 'open')) {
      openRef.value = !!props.open;
    }
  }

  defineExpose({
    open: (loading = false) => {
      setDrawerProps({ open: true, loading });
    },
    close: () => {
      setDrawerProps({ open: false });
    },
    loading: () => {
      setDrawerProps({ loading: true });
    },
    closeLoading: () => {
      setDrawerProps({ loading: false });
    },
    confirmLoading: () => {
      setDrawerProps({ confirmLoading: true });
    },
    closeConfirmLoading: () => {
      setDrawerProps({ confirmLoading: false });
    },
    getProps: () => getProps.value,
    setProps: setDrawerProps,
  });
</script>
<style lang="less">
  @header-height: 60px;
  @detail-header-height: 40px;
  @prefix-cls: ~'jeesite-basic-drawer';
  @prefix-cls-detail: ~'jeesite-basic-drawer__detail';

  .ant-drawer .@{prefix-cls} {
    overflow: hidden;

    .ew-resize {
      position: absolute;
      left: 0;
      height: 90%;
      width: 5px;
      z-index: 1000;
      user-select: none;

      &:hover {
        cursor: ew-resize;
      }
    }

    .ant-drawer {
      &-body {
        height: calc(100% - @header-height);
        padding: 0;
        background-color: @component-background;

        > .scrollbar {
          > .scrollbar__wrap {
            margin: 16px 16px 5px;
            padding-bottom: 30px;

            > .scrollbar__view {
              > form:first-child,
              > .ant-tabs:first-child {
                margin-top: 13px;
                margin-right: 5px;
              }
            }
          }

          > .is-horizontal {
            display: none;
          }
        }
      }

      &-title {
        font-weight: normal;

        .anticon {
          color: @primary-color;
        }
      }

      &-extra {
        .anticon-close {
          opacity: 0.6;
          color: @text-color-base;

          &:hover {
            color: @error-color;
            opacity: 1;
          }
        }
      }
    }
  }

  .@{prefix-cls-detail} {
    position: absolute;
    overflow: hidden;

    .ant-drawer {
      &-body {
        height: calc(100% - @detail-header-height);
        padding: 0 !important;

        > .scrollbar {
          .scrollbar__wrap {
            margin: 10px;
            padding-bottom: 10px;
          }

          .is-horizontal {
            display: none;
          }
        }
      }

      &-header {
        width: 100%;
        height: @detail-header-height;
        padding: 0;
        border-top: 1px solid @border-color-base;
        box-sizing: border-box;
      }

      &-title {
        height: 100%;
      }

      &-close {
        height: @detail-header-height;
        line-height: @detail-header-height;
      }
    }
  }
</style>
