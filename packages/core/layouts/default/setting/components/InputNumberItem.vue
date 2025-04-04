<template>
  <div :class="prefixCls">
    <span> {{ title }}</span>
    <InputNumber v-bind="$attrs" size="small" :class="`${prefixCls}-input-number`" @change="handleChange" />
  </div>
</template>
<script lang="ts">
  import { defineComponent, PropType } from 'vue';

  import { InputNumber } from 'ant-design-vue';
  import { useDesign } from '@jeesite/core/hooks/web/useDesign';
  import { baseHandler } from '../handler';
  import { HandlerEnum } from '../enum';

  export default defineComponent({
    name: 'InputNumberItem',
    components: { InputNumber },
    props: {
      event: {
        type: Number as PropType<HandlerEnum>,
      },
      title: {
        type: String,
      },
    },
    setup(props) {
      const { prefixCls } = useDesign('setting-input-number-item');

      function handleChange(e) {
        props.event && baseHandler(props.event, e);
      }
      return {
        prefixCls,
        handleChange,
      };
    },
  });
</script>
<style lang="less">
  @prefix-cls: ~'jeesite-setting-input-number-item';

  .@{prefix-cls} {
    display: flex;
    justify-content: space-between;
    margin: 16px 0;
    color: @text-color-base;

    &-input-number {
      width: 126px !important;
    }
  }
</style>
