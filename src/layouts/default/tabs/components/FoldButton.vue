<template>
  <span :class="`${prefixCls}__extra-fold`" @click="handleFoldToggle">
    <Icon :icon="getIcon" />
  </span>
</template>
<script lang="ts">
  import { defineComponent, unref, computed, onMounted } from 'vue';
  import { Icon } from '/@/components/Icon';

  import { useDesign } from '/@/hooks/web/useDesign';
  import { useHeaderSetting } from '/@/hooks/setting/useHeaderSetting';
  import { useMenuSetting } from '/@/hooks/setting/useMenuSetting';
  import { triggerResize } from '/@/utils/event';

  export default defineComponent({
    name: 'FoldButton',
    components: { Icon },
    setup() {
      const { prefixCls } = useDesign('multiple-tabs-content');
      const { getShowMenu, getShowSidebar, setMenuSetting } = useMenuSetting();
      const { getShowHeader, setHeaderSetting } = useHeaderSetting();

      const getIsUnFold = computed(
        () => !unref(getShowMenu) && !unref(getShowHeader) && !unref(getShowSidebar),
      );

      const getIcon = computed(() =>
        unref(getIsUnFold)
          ? 'i-ant-design:fullscreen-exit-outlined'
          : 'i-ant-design:fullscreen-outlined',
      );

      function handleFoldToggle() {
        const isUnFold = unref(getIsUnFold);
        setMenuSetting({
          show: isUnFold,
          hidden: !isUnFold,
        });
        setHeaderSetting({ show: isUnFold });
        triggerResize();
      }

      return { prefixCls, getIcon, handleFoldToggle };
    },
  });
</script>
