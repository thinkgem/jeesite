<template>
  <Modal v-bind="getBindValue" @cancel="handleCancel">
    <template #closeIcon v-if="!$slots.closeIcon">
      <ModalClose
        :canFullscreen="getProps.canFullscreen"
        :fullScreen="fullScreenRef"
        @cancel="handleCancel"
        @fullscreen="handleFullScreen"
      />
    </template>

    <template #title v-if="!$slots.title">
      <ModalHeader :helpMessage="getProps.helpMessage" :title="getMergeProps.title" @dblclick="handleTitleDbClick">
        <template #[item]="data" v-for="item in Object.keys($slots)">
          <slot :name="item" v-bind="data || {}"></slot>
        </template>
      </ModalHeader>
    </template>

    <template #footer v-if="!$slots.footer">
      <ModalFooter v-bind="getBindValue" @ok="handleOk" @cancel="handleCancel">
        <template #[item]="data" v-for="item in Object.keys($slots)">
          <slot :name="item" v-bind="data || {}"></slot>
        </template>
      </ModalFooter>
    </template>

    <ModalWrapper
      ref="modalWrapperRef"
      :useWrapper="getProps.useWrapper"
      :footerOffset="wrapperFooterOffset"
      :fullScreen="fullScreenRef"
      :loading="getProps.loading"
      :loading-tip="getProps.loadingTip"
      :minHeight="getProps.minHeight"
      :height="getWrapperHeight"
      :open="openRef"
      :modalFooterHeight="footer !== undefined && !footer ? 0 : undefined"
      v-bind="omit(getProps.wrapperProps, 'open', 'height', 'modalFooterHeight')"
      @ext-height="handleExtHeight"
      @height-change="handleHeightChange"
    >
      <slot></slot>
    </ModalWrapper>

    <template #[item]="data" v-for="item in Object.keys(omit($slots, 'default'))">
      <slot :name="item" v-bind="data || {}"></slot>
    </template>
  </Modal>
</template>
<script lang="ts" setup name="BasicModal">
  import type { ModalProps, ModalMethods } from './typing';
  import {
    computed,
    ref,
    watch,
    unref,
    watchEffect,
    toRef,
    getCurrentInstance,
    nextTick,
    useAttrs,
    shallowRef,
  } from 'vue';
  import Modal from './components/Modal';
  import ModalWrapper from './components/ModalWrapper.vue';
  import ModalClose from './components/ModalClose.vue';
  import ModalFooter from './components/ModalFooter.vue';
  import ModalHeader from './components/ModalHeader.vue';
  import { isFunction } from '@jeesite/core/utils/is';
  // import { deepMerge } from '@jeesite/core/utils';
  import { basicProps } from './props';
  import { useFullScreen } from './hooks/useModalFullScreen';
  import { omit } from 'lodash-es';

  defineOptions({
    inheritAttrs: false,
  });

  const props = defineProps(basicProps);
  const emit = defineEmits(['open-change', 'height-change', 'cancel', 'ok', 'register', 'update:open']);
  const attrs = useAttrs();
  const openRef = ref(false);
  const propsRef = ref<Partial<ModalProps> | null>(null);
  const modalWrapperRef = shallowRef<InstanceType<typeof ModalWrapper> | null>(null);

  // modal   Bottom and top height
  const extHeightRef = ref(0);
  const modalMethods: ModalMethods = {
    getModalProps,
    setModalProps,
    emitOpen: undefined,
    redoModalHeight: () => {
      nextTick(() => {
        modalWrapperRef.value?.setModalHeight();
      });
    },
  };

  const instance = getCurrentInstance();
  if (instance) {
    emit('register', modalMethods, instance.uid);
  }

  // Custom title component: get title
  const getMergeProps = computed((): Recordable => {
    return {
      ...props,
      ...(unref(propsRef) as any),
    };
  });

  const { handleFullScreen, getWrapClassName, fullScreenRef } = useFullScreen({
    modalWrapperRef,
    extHeightRef,
    wrapClassName: toRef(getMergeProps.value, 'wrapClassName'),
  });

  // modal component does not need title and origin buttons
  const getProps = computed((): Recordable => {
    return {
      ...unref(getMergeProps),
      // open: unref(openRef),
      okButtonProps: undefined,
      cancelButtonProps: undefined,
      title: undefined,
    };
  });

  const getBindValue = computed((): Recordable => {
    const values = {
      ...attrs,
      ...unref(getMergeProps),
      open: unref(openRef),
      wrapClassName: unref(getWrapClassName),
      maskTransitionName: 'ant-fade',
      transitionName: 'ant-fade',
    } as any;
    if (typeof values?.width === 'string') {
      let width = Number(values.width);
      if (!isNaN(width)) values.width = width;
    }
    delete values['loading'];
    if (unref(fullScreenRef)) {
      return omit(values, ['height', 'title']);
    }
    return omit(values, 'title');
  });

  const getWrapperHeight = computed(() => {
    if (unref(fullScreenRef)) return undefined;
    return unref(getProps).height;
  });

  watchEffect(() => {
    openRef.value = !!props.open;
    fullScreenRef.value = !!props.defaultFullscreen;
  });

  watch(
    () => unref(openRef),
    (v) => {
      emit('open-change', v);
      emit('update:open', v);
      instance && modalMethods.emitOpen?.(v, instance.uid);
      nextTick(() => {
        if (props.scrollTop && v && unref(modalWrapperRef)) {
          (unref(modalWrapperRef) as any).scrollTop();
        }
      });
    },
    {
      immediate: false,
    },
  );

  // 取消事件
  async function handleCancel(e: Event) {
    e?.stopPropagation();

    if (props.closeFunc && isFunction(props.closeFunc)) {
      const isClose: boolean = await props.closeFunc();
      openRef.value = !isClose;
      return;
    }

    openRef.value = false;
    emit('cancel', e);
  }

  function handleOk(e: Event) {
    emit('ok', e);
  }

  function handleHeightChange(height: string) {
    emit('height-change', height);
  }

  function handleExtHeight(height: number) {
    extHeightRef.value = height;
  }

  function handleTitleDbClick(e) {
    if (!props.canFullscreen) return;
    e.stopPropagation();
    handleFullScreen(e);
  }

  function getModalProps(): Partial<ModalProps> {
    return getProps.value;
  }

  function setModalProps(props: Partial<ModalProps>): void {
    if (typeof props.loading != 'undefined') {
      props.confirmLoading = props.loading;
    }
    // Keep the last setModalProps
    // propsRef.value = deepMerge(unref(propsRef) || ({} as any), props);
    propsRef.value = { ...(unref(propsRef) as Recordable), ...props } as Recordable;
    if (Reflect.has(props, 'open')) {
      openRef.value = !!props.open;
    }
    if (Reflect.has(props, 'defaultFullscreen')) {
      fullScreenRef.value = !!props.defaultFullscreen;
    }
  }

  defineExpose({
    open: (loading = false) => {
      setModalProps({ open: true, loading });
    },
    close: () => {
      setModalProps({ open: false });
    },
    loading: () => {
      setModalProps({ loading: true });
    },
    closeLoading: () => {
      setModalProps({ loading: false });
    },
    confirmLoading: () => {
      setModalProps({ confirmLoading: true });
    },
    closeConfirmLoading: () => {
      setModalProps({ confirmLoading: false });
    },
    getProps: getModalProps,
    setProps: setModalProps,
  });
</script>

<style lang="less">
  @prefix-cls: ~'jeesite-basic-modal';

  .@{prefix-cls} {
    padding: 0 !important;

    .ant-modal {
      &-body {
        padding: 0;
        background-color: @component-background;

        > .scrollbar {
          > .scrollbar__wrap {
            > .scrollbar__view {
              margin: 12px 15px 1px;

              > .spinDiv {
                > .ant-form,
                > .ant-tabs {
                  margin-right: 25px;

                  &:first-child {
                    margin-top: 20px;
                  }

                  .jeesite-form-group {
                    .title {
                      margin-right: -12px;
                    }
                  }
                }
              }
            }
          }

          > .is-horizontal {
            display: none;
          }
        }
      }

      &-title {
        font-size: 16px;
        font-weight: normal;
        line-height: 16px;

        .base-title {
          cursor: move !important;
        }

        .anticon {
          color: @primary-color;
        }
      }

      &-large {
        top: 60px;

        &--mini {
          top: 16px;
        }
      }

      &-content {
        box-shadow:
          0 4px 8px 0 rgb(0 0 0 / 20%),
          0 6px 20px 0 rgb(0 0 0 / 19%);
        padding: 0;
        border-radius: 10px;
      }

      &-header {
        display: flex;
        align-items: center;
        // 与抽屉统一：标题栏高度 52px
        height: 52px;
        padding: 0 16px;
        box-sizing: border-box;
        margin-bottom: 0;
        border-bottom: 1px solid fade(@border-color-base, 50%);
        border-radius: 10px 10px 0 0;
      }

      &-footer {
        padding: 14px;
        margin-top: 0;
        // border-top: 1px solid fade(@border-color-base, 50%);

        button + button {
          margin-left: 10px;
        }
      }

      &-close {
        position: absolute;
        top: 0;
        right: 0;
        display: flex;
        align-items: center;
        // 与标题栏等高，图标靠 flex 垂直居中
        height: 52px;
        width: auto;
        font-weight: normal;
        outline: none;

        &:hover {
          background-color: transparent;
        }
      }

      // 注释掉，防止点击全屏误触关闭
      //&-close-x {
      //  display: inline-block;
      //  width: 96px;
      //  height: 55px;
      //  line-height: 55px;
      //}

      &-confirm-body {
        .ant-modal-confirm-content {
          // color: #fff;

          > * {
            color: @text-color-help-dark;
          }
        }
      }

      &-confirm-confirm.error .ant-modal-confirm-body > .anticon {
        color: @error-color;
      }

      &-confirm-btns {
        .ant-btn:last-child {
          margin-right: 0;
        }
      }

      &-confirm-info {
        .ant-modal-confirm-body > .anticon {
          color: @warning-color;
        }
      }

      &-confirm-confirm.success {
        .ant-modal-confirm-body > .anticon {
          color: @success-color;
        }
      }
    }

    .ant-modal-confirm .ant-modal-body {
      padding: 24px !important;
    }
  }

  // .ant-modal.@{prefix-cls} {
  //   top: 150px !important;
  //   vertical-align: top !important;
  // }
  // @media screen and (max-height: 600px) {
  //   .ant-modal.@{prefix-cls} {
  //     top: 60px !important;
  //   }
  // }
  // @media screen and (max-height: 540px) {
  //   .ant-modal.@{prefix-cls} {
  //     top: 30px !important;
  //   }
  // }
  // @media screen and (max-height: 480px) {
  //   .ant-modal.@{prefix-cls} {
  //     top: 10px !important;
  //   }
  // }

  // 右下角拖拽缩放手柄（basicProps.resizable 为 true 时由 useModalResize 动态插入）
  .jeesite-modal-resizer {
    position: absolute;
    right: -2px;
    bottom: -2px;
    z-index: 9;
    width: 16px;
    height: 16px;
    pointer-events: auto;
    cursor: nwse-resize;
    touch-action: none;
  }

  // 拖拽缩放后的尺寸：antd 的 Panel 每次渲染都会用 props 的 width/height 重写内联样式，
  // 因此改用标记属性 + CSS 变量 + !important 接管，避免关闭/重渲染时闪回原始尺寸
  .ant-modal[data-resized='true'] {
    width: var(--jeesite-modal-width, auto) !important;
    height: var(--jeesite-modal-height, auto) !important;
    max-width: none !important;
  }

  // 最大化时卡片与视口之间的边距
  @fullscreen-gap: 8px;

  .fullscreen-modal {
    overflow: hidden;

    // 全屏已铺满视口，不再提供缩放手柄
    .jeesite-modal-resizer {
      display: none;
    }

    .ant-modal {
      top: 0 !important; /* stylelint-disable-line */
      right: 0 !important; /* stylelint-disable-line */
      bottom: 0 !important; /* stylelint-disable-line */
      left: 0 !important; /* stylelint-disable-line */
      // 四周留出边距：居中布局会把差额平均分配到上下、左右
      width: calc(100% - @fullscreen-gap * 2) !important;
      max-width: calc(100vw - @fullscreen-gap * 2) !important;
      height: calc(100% - @fullscreen-gap * 2) !important;
    }

    // antdv-next 的内容层类名是 -container（-content 已不存在），撑满后由 flex 分配高度
    .ant-modal-container {
      display: flex;
      flex-direction: column;
      height: 100%;
      min-height: 0;
    }

    .ant-modal-body {
      flex: 1;
      min-height: 0;

      > .scrollbar,
      > .scrollbar > .scrollbar__wrap,
      > .scrollbar > .scrollbar__wrap > .scrollbar__view {
        height: 100%;
      }

      // 全屏时 ModalWrapper 会写内联高度（按页头/页脚估算），这里统一改成撑满
      .spinDiv {
        height: 100% !important;
        max-height: none !important;
      }
    }
  }
</style>
