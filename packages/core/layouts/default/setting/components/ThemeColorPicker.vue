<template>
  <div class="jeesite-setting-theme-picker">
    <template v-for="color in colorList || []" :key="color">
      <span
        @click="handleClick(color)"
        :class="[
          'jeesite-setting-theme-picker__item',
          {
            ['jeesite-setting-theme-picker__item--active']: def === color,
          },
        ]"
        :style="{ background: color }"
      >
        <CheckOutlined />
      </span>
    </template>
  </div>
</template>
<script lang="ts">
  import { defineComponent, PropType } from 'vue';
  import { CheckOutlined } from '@antdv-next/icons';

  import { baseHandler } from '../handler';
  import { HandlerEnum } from '../enum';

  export default defineComponent({
    name: 'ThemeColorPicker',
    components: { CheckOutlined },
    props: {
      colorList: {
        type: Array as PropType<string[]>,
        defualt: [],
      },
      event: {
        type: Number as PropType<HandlerEnum>,
      },
      def: {
        type: String,
      },
    },
    setup(props) {
      function handleClick(color: string) {
        props.event && baseHandler(props.event, color);
      }
      return {
        handleClick,
      };
    },
  });
</script>
<style lang="less">
  .jeesite-setting-theme-picker {
    display: flex;
    flex-wrap: wrap;
    margin: 16px 0;
    justify-content: space-around;
    color: @text-color-base;

    &__item {
      width: 20px;
      height: 20px;
      cursor: pointer;
      border: 1px solid #ddd;
      border-radius: 2px;

      svg {
        display: none;
      }

      &--active {
        border: 1px solid lighten(@primary-color, 10%);

        .anticon {
          vertical-align: 1px;
        }

        svg {
          display: inline-block;
          margin: 0 0 3px 3px;
          font-size: 12px;
          fill: #ddd !important;
        }
      }
    }
  }
</style>
