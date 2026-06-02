<template>
  <Dropdown :dropMenuList="getDropMenuList" :trigger="getTrigger" @menu-event="handleMenuEvent">
    <div class="jeesite-multiple-tabs-content__info" @contextmenu="handleContext" v-if="getIsTabs">
      <span class="ml-1"><Icon v-if="getIcon" :icon="getIcon" class="mr-2" />{{ getTitle }}</span>
    </div>
    <span class="jeesite-multiple-tabs-content__extra-quick" v-else @click="handleContext">
      <Icon icon="i-ant-design:down-outlined" />
    </span>
  </Dropdown>
</template>
<script lang="ts">
  import type { PropType } from 'vue';
  import type { RouteLocationNormalized } from 'vue-router';

  import { defineComponent, computed, unref } from 'vue';
  import { Dropdown } from '@jeesite/core/components/Dropdown';
  import { Icon } from '@jeesite/core/components/Icon';

  import { TabContentProps } from '../types';

  import { useI18n } from '@jeesite/core/hooks/web/useI18n';
  import { useTabDropdown } from '../useTabDropdown';

  export default defineComponent({
    name: 'TabContent',
    components: { Dropdown, Icon },
    props: {
      tabItem: {
        type: Object as PropType<RouteLocationNormalized>,
        default: null,
      },
      isExtra: Boolean,
    },
    setup(props) {
      const { t } = useI18n();

      const getIcon = computed(() => {
        const { tabItem: { meta } = {} } = props;
        return meta && t((meta.tabIcon as string) || (meta.icon as string));
      });

      const getTitle = computed(() => {
        const { tabItem: { meta } = {} } = props;
        return meta && t(meta.title as string);
      });

      const getIsTabs = computed(() => !props.isExtra);

      const getTrigger = computed((): ('contextmenu' | 'click' | 'hover')[] =>
        unref(getIsTabs) ? ['contextmenu'] : ['click'],
      );

      const { getDropMenuList, handleMenuEvent, handleContextMenu } = useTabDropdown(
        props as TabContentProps,
        getIsTabs,
      );

      function handleContext(e) {
        props.tabItem && handleContextMenu(props.tabItem)(e);
      }

      return {
        getDropMenuList,
        handleMenuEvent,
        handleContext,
        getTrigger,
        getIsTabs,
        getIcon,
        getTitle,
      };
    },
  });
</script>
