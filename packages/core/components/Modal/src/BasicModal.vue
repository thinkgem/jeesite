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
      <ModalHeader
        :helpMessage="getProps.helpMessage"
        :title="getMergeProps.title"
        @dblclick="handleTitleDbClick"
      >
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
      :useWrapper="getProps.useWrapper"
      :footerOffset="wrapperFooterOffset"
      :fullScreen="fullScreenRef"
      ref="modalWrapperRef"
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
  const emit = defineEmits([
    'open-change',
    'height-change',
    'cancel',
    'ok',
    'register',
    'update:open',
  ]);
  const attrs = useAttrs();
  const openRef = ref(false);
  const propsRef = ref<Partial<ModalProps> | null>(null);
  const modalWrapperRef = ref<any>(null);

  // modal   Bottom and top height
  const extHeightRef = ref(0);
  const modalMethods: ModalMethods = {
    setModalProps,
    emitOpen: undefined,
    redoModalHeight: () => {
      nextTick(() => {
        if (unref(modalWrapperRef)) {
          (unref(modalWrapperRef) as any).setModalHeight();
        }
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
    const opt = {
      ...unref(getMergeProps),
      // open: unref(openRef),
      okButtonProps: undefined,
      cancelButtonProps: undefined,
      title: undefined,
    };
    return {
      ...opt,
      wrapClassName: unref(getWrapClassName),
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
    getProps: () => getProps.value,
    setProps: setModalProps,
  });
</script>

<style lang="less">
  @prefix-cls: ~'jeesite-basic-modal';

  .ant-modal.@{prefix-cls} {
    .ant-modal {
      &-body {
        padding: 0;
        background-color: @component-background;

        > .scrollbar {
          > .scrollbar__wrap {
            margin: 12px 15px 0;
            padding: 1px;

            > .scrollbar__view > div {
              > form:first-child {
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
      }

      &-header {
        padding: 14px;
        margin-bottom: 0;
        border-bottom: 1px solid fade(@border-color-base, 50%);
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
        width: auto;
        height: auto;
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

  .fullscreen-modal {
    overflow: hidden;

    .ant-modal {
      top: 0 !important; /* stylelint-disable-line */
      right: 0 !important; /* stylelint-disable-line */
      bottom: 0 !important; /* stylelint-disable-line */
      left: 0 !important; /* stylelint-disable-line */
      width: 100% !important;
      max-width: calc(100vw - 15px) !important;

      &-content {
        height: 100%;
      }
    }
  }
</style>
