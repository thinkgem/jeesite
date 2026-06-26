<template>
  <div :class="getClass" :style="getDragBarStyle"></div>
</template>
<script lang="ts">
  import { defineComponent, computed, unref } from 'vue';

  import { useMenuSetting } from '@jeesite/core/hooks/setting/useMenuSetting';

  export default defineComponent({
    name: 'DargBar',
    props: {
      mobile: Boolean,
    },
    setup(props) {
      const { getMiniWidthNumber, getCollapsed, getCanDrag } = useMenuSetting();

      const getDragBarStyle = computed(() => {
        if (unref(getCollapsed)) {
          return { left: `${unref(getMiniWidthNumber)}px` };
        }
        return {};
      });

      const getClass = computed(() => {
        return [
          'jeesite-darg-bar',
          {
            ['jeesite-darg-bar--hide']: !unref(getCanDrag) || props.mobile,
          },
        ];
      });

      return {
        getDragBarStyle,
        getClass,
      };
    },
  });
</script>
<style lang="less">
  .jeesite-darg-bar {
    position: absolute;
    top: 0;
    right: -2px;
    z-index: @side-drag-z-index;
    width: 2px;
    height: 100%;
    cursor: col-resize;
    border-top: none;
    border-bottom: none;

    &--hide {
      display: none;
    }

    &:hover {
      background-color: @primary-color;
      box-shadow: 0 0 4px 0 rgb(28 36 56 / 15%);
    }
  }
</style>
