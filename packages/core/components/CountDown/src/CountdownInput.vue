<template>
  <a-input v-bind="$attrs" :class="prefixCls" :size="size" v-model:value="state">
    <template #addonAfter>
      <CountButton
        type="link"
        :size="size === 'large' ? 'middle' : 'small'"
        :count="count"
        :value="state"
        :beforeStartFunc="sendCodeApi"
      />
    </template>
    <template #[item]="data" v-for="item in Object.keys($slots).filter((k) => k !== 'addonAfter')">
      <slot :name="item" v-bind="data || {}"></slot>
    </template>
  </a-input>
</template>
<script lang="ts">
  import { defineComponent, PropType } from 'vue';
  import CountButton from './CountButton.vue';
  import { useDesign } from '@jeesite/core/hooks/web/useDesign';
  import { useRuleFormItem } from '@jeesite/core/hooks/component/useFormItem';

  const props = {
    value: { type: String },
    size: { type: String, validator: (v) => ['default', 'large', 'small'].includes(v) },
    count: { type: Number, default: 60 },
    sendCodeApi: {
      type: Function as PropType<() => Promise<boolean>>,
      default: null,
    },
  };

  export default defineComponent({
    name: 'CountDownInput',
    components: { CountButton },
    inheritAttrs: false,
    props,
    emits: ['change', 'update:value'],
    setup(props) {
      const { prefixCls } = useDesign('countdown-input');
      const [state] = useRuleFormItem(props);

      return { prefixCls, state };
    },
  });
</script>
<style lang="less">
  @prefix-cls: ~'jeesite-countdown-input';

  .@{prefix-cls} {
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
