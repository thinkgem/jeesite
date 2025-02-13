<template>
  <span :class="`${prefixCls}__extra-fold`" @click="handleToggle">
    <Icon :icon="getIcon" />
  </span>
</template>
<script lang="ts">
  import { defineComponent, unref, computed, onMounted } from 'vue';
  import { Icon } from '@jeesite/core/components/Icon';

  import { useDesign } from '@jeesite/core/hooks/web/useDesign';
  import { useHeaderSetting } from '@jeesite/core/hooks/setting/useHeaderSetting';
  import { useMenuSetting } from '@jeesite/core/hooks/setting/useMenuSetting';
  import { triggerResize } from '@jeesite/core/utils/event';

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

      function handleFold(isUnFold: boolean) {
        setMenuSetting({
          show: isUnFold,
          hidden: !isUnFold,
        });
        setHeaderSetting({ show: isUnFold });
        triggerResize();
      }

      function handleToggle() {
        handleFold(unref(getIsUnFold));
      }

      onMounted(() => {
        handleFold(true);
      });

      return { prefixCls, getIcon, handleToggle };
    },
  });
</script>
