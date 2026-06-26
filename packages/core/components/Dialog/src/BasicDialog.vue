<template>
  <component :is="dialogComponent" ref="dialogComponentRef" v-bind="getAttrs">
    <template #[item]="data" v-for="item in Object.keys($slots)">
      <slot :name="item" v-bind="data || {}"></slot>
    </template>
  </component>
</template>
<script lang="ts" setup>
  import { computed, shallowRef } from 'vue';
  import { BasicModal, ModalProps } from '@jeesite/core/components/Modal';
  import { BasicDrawer, DrawerProps } from '@jeesite/core/components/Drawer';
  import { propTypes } from '@jeesite/core/utils/propTypes';
  import { useAttrs } from '@jeesite/core/hooks/core/useAttrs';

  type BasicDialogProps = Partial<ModalProps> & Partial<DrawerProps>;
  type BasicDialogInstance = InstanceType<typeof BasicModal> | InstanceType<typeof BasicDrawer>;

  defineOptions({
    name: 'BasicDialog',
    inheritAttrs: false,
  });

  const props = defineProps({
    dialogType: propTypes.oneOf(['modal', 'drawer']).def('drawer'),
  });

  const attrs = useAttrs();

  const dialogComponent = shallowRef<Nullable<any>>(null);
  const dialogComponentRef = shallowRef<BasicDialogInstance>();

  if (props.dialogType === 'modal') {
    dialogComponent.value = BasicModal;
  } else if (props.dialogType === 'drawer') {
    dialogComponent.value = BasicDrawer;
  }

  const getAttrs = computed(() => {
    return {
      ...attrs,
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
      return dialogComponentRef.value?.getProps();
    },
    setProps: (props: BasicDialogProps) => {
      dialogComponentRef.value?.setProps(props);
    },
  });
</script>
