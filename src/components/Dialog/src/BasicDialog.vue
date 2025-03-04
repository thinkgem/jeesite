<template>
  <component :is="dialogComponent" ref="dialogComponentRef" v-bind="getAttrs">
    <template #[item]="data" v-for="item in Object.keys($slots)">
      <slot :name="item" v-bind="data || {}"></slot>
    </template>
  </component>
</template>
<script lang="ts" setup>
  import { computed, ref, shallowRef, useAttrs } from 'vue';
  import { BasicModal, BasicModalInstance, ModalProps } from '/@/components/Modal';
  import { BasicDrawer, BasicDrawerInstance, DrawerProps } from '/@/components/Drawer';
  import { propTypes } from '/@/utils/propTypes';

  type BasicDialogProps = Partial<ModalProps> & Partial<DrawerProps>;
  type BasicDialogInstance = BasicModalInstance | BasicDrawerInstance;

  defineOptions({
    name: 'BasicDialog',
  });

  const props = defineProps({
    dialogType: propTypes.oneOf(['modal', 'drawer']).def('drawer'),
  });

  const dialogComponent = shallowRef<Nullable<any>>(null);
  const dialogComponentRef = ref<BasicDialogInstance>();

  if (props.dialogType === 'modal') {
    dialogComponent.value = BasicModal;
  } else if (props.dialogType === 'drawer') {
    dialogComponent.value = BasicDrawer;
  }

  const getAttrs = computed(() => {
    return {
      ...useAttrs(),
      ...props,
    };
  });

  defineExpose({
    open: (loading = false) => {
      dialogComponentRef.value?.open(loading);
    },
    close: () => {
      dialogComponentRef.value?.close();
    },
    loading: () => {
      dialogComponentRef.value?.loading();
    },
    closeLoading: () => {
      dialogComponentRef.value?.closeLoading();
    },
    confirmLoading: () => {
      dialogComponentRef.value?.confirmLoading();
    },
    closeConfirmLoading: () => {
      dialogComponentRef.value?.closeConfirmLoading();
    },
    getProps: () => {
      dialogComponentRef.value?.getProps();
    },
    setProps: (props: BasicDialogProps) => {
      dialogComponentRef.value?.setProps(props);
    },
  });
</script>
