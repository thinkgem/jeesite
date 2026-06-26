<template>
  <span :class="getClass">
    <slot></slot>
    <BasicHelp class="jeesite-basic-title-help" v-if="helpMessage" :text="helpMessage" />
  </span>
</template>
<script lang="ts" setup>
  import type { PropType } from 'vue';
  import { useSlots, computed } from 'vue';
  import BasicHelp from './BasicHelp.vue';

  const props = defineProps({
    /**
     * Help text list or string
     * @default: ''
     */
    helpMessage: {
      type: [String, Array] as PropType<string | string[]>,
      default: '',
    },
    /**
     * Whether the color block on the left side of the title
     * @default: false
     */
    span: { type: Boolean },
    /**
     * Whether to default the text, that is, not bold
     * @default: false
     */
    normal: { type: Boolean },
  });

  const slots = useSlots();
  const getClass = computed(() => [
    'jeesite-basic-title',
    { ['jeesite-basic-title-show-span']: props.span && slots.default },
    { ['jeesite-basic-title-normal']: props.normal },
  ]);
</script>
<style lang="less">
  .jeesite-basic-title {
    position: relative;
    display: flex;
    padding-left: 3px;
    font-size: 16px;
    font-weight: 500;
    line-height: 24px;
    color: fade(@text-color-base, 75);
    //cursor: pointer;
    user-select: none;

    &-normal {
      font-size: 14px;
      font-weight: 500;
    }

    &-show-span::before {
      position: absolute;
      top: 4px;
      left: 0;
      width: 3px;
      height: 16px;
      margin-right: 4px;
      background-color: @primary-color;
      content: '';
    }

    &-help {
      margin-left: 10px;
    }
  }
</style>
