<template>
  <div class="jeesite-collapse-container">
    <CollapseHeader v-bind="$props" :prefixCls="'jeesite-collapse-container'" :show="show" @expand="handleExpand">
      <template #title>
        <slot name="title"></slot>
      </template>
      <template #action>
        <slot name="action"></slot>
      </template>
    </CollapseHeader>

    <div class="p-2" v-show="show">
      <CollapseTransition :enable="canExpan">
        <Skeleton v-if="loading" :active="loading" />
        <div class="jeesite-collapse-container__body" v-else>
          <slot></slot>
        </div>
      </CollapseTransition>
    </div>
    <div class="jeesite-collapse-container__footer" v-if="$slots.footer">
      <slot name="footer"></slot>
    </div>
  </div>
</template>
<script lang="ts" setup>
  import type { PropType } from 'vue';
  import { ref, watch } from 'vue';
  // component
  import { Skeleton } from 'antdv-next';
  import { CollapseTransition } from '@jeesite/core/components/Transition';
  import CollapseHeader from './CollapseHeader.vue';
  import { triggerResize } from '@jeesite/core/utils/event';
  // hook
  import { useTimeoutFn } from '@jeesite/core/hooks/core/useTimeout';

  const props = defineProps({
    title: { type: String, default: '' },
    loading: { type: Boolean },
    /**
     *  Can it be expanded
     */
    canExpan: { type: Boolean, default: true },
    /**
     *  expanded
     */
    expand: { type: Boolean, default: true },
    /**
     * Warm reminder on the right side of the title
     */
    helpMessage: {
      type: [Array, String] as PropType<string[] | string>,
      default: '',
    },
    /**
     * Whether to trigger window.resize when expanding and contracting,
     * Can adapt to tables and forms, when the form shrinks, the form triggers resize to adapt to the height
     */
    triggerWindowResize: { type: Boolean },
    /**
     * Delayed loading time
     */
    lazyTime: { type: Number, default: 0 },
  });

  const emit = defineEmits(['update:expand']);

  const show = ref(true);

  watch(
    () => props.expand,
    (newExpand) => {
      if (show.value != newExpand) {
        handleExpand();
      }
    },
    { immediate: true },
  );

  /**
   * @description: Handling development events
   */
  function handleExpand() {
    show.value = !show.value;
    if (props.triggerWindowResize) {
      // 200 milliseconds here is because the expansion has animation,
      useTimeoutFn(triggerResize, 200);
    }
    emit('update:expand', show.value);
  }
</script>
<style lang="less">
  .jeesite-collapse-container {
    background-color: @component-background;
    border-radius: 2px;
    transition: all 0.3s ease-in-out;

    &__header {
      display: flex;
      height: 32px;
      justify-content: space-between;
      align-items: center;
      border-bottom: 1px solid @border-color-light;
    }

    &__footer {
      border-top: 1px solid @border-color-light;
    }

    &__action {
      display: flex;
      text-align: right;
      flex: 1;
      align-items: center;
      justify-content: flex-end;
    }
  }

  [data-theme='dark'] .jeesite-collapse-container__header {
    border-bottom: 1px solid #525252;
  }
</style>
