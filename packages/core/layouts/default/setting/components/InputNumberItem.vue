<template>
  <div class="jeesite-setting-input-number-item">
    <span> {{ title }}</span>
    <InputNumber
      v-bind="$attrs"
      size="small"
      class="jeesite-setting-input-number-item-input-number"
      @change="handleChange"
    />
  </div>
</template>
<script lang="ts">
  import { defineComponent, PropType } from 'vue';

  import { InputNumber } from 'antdv-next';
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
      function handleChange(e) {
        props.event && baseHandler(props.event, e);
      }
      return {
        handleChange,
      };
    },
  });
</script>
<style lang="less">
  .jeesite-setting-input-number-item {
    display: flex;
    justify-content: space-between;
    margin: 16px 0;
    color: @text-color-base;

    &-input-number {
      width: 126px !important;
    }
  }
</style>
