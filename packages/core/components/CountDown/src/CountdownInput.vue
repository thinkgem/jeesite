<template>
  <a-input v-bind="$attrs" class="jeesite-countdown-input" :size="size" v-model:value="state" autocomplete="off">
    <template #suffix>
      <CountButton
        type="link"
        :size="size === 'large' ? 'middle' : 'small'"
        :count="count"
        :value="state"
        :beforeStartFunc="sendCodeApi"
      />
    </template>
    <template #[item]="data" v-for="item in Object.keys($slots).filter((k) => k !== 'suffix')">
      <slot :name="item" v-bind="data || {}"></slot>
    </template>
  </a-input>
</template>
<script lang="ts" setup name="CountDownInput">
  import { PropType } from 'vue';
  import { useRuleFormItem } from '@jeesite/core/hooks/component/useFormItem';
  import CountButton from './CountButton.vue';

  const props = defineProps({
    value: { type: String },
    size: { type: String, validator: (v: string) => ['default', 'large', 'small'].includes(v) },
    count: { type: Number, default: 60 },
    sendCodeApi: {
      type: Function as PropType<() => Promise<boolean>>,
      default: () => {},
    },
  });

  const emit = defineEmits(['change', 'update:value']);

  const [state] = useRuleFormItem(props);
</script>
<style lang="less">
  .jeesite-countdown-input {
    .ant-input-group-addon {
      padding-right: 0;
      background-color: transparent;
      border: none;

      button {
        font-size: 14px;

        &.ant-btn-sm {
          font-size: 13px;
          height: 22px;
          padding: 0 7px;
        }
      }
    }
  }
</style>
