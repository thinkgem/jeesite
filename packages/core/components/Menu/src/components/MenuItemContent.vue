<template>
  <span :class="`${prefixCls}- flex items-center `" :style="`color: ${getColor}`">
    <Icon v-if="getIcon" :icon="getIcon" :size="18" :class="`${prefixCls}-wrapper__icon mr-2`" />
    {{ getI18nName }}
  </span>
</template>
<script lang="ts">
  import { computed, defineComponent } from 'vue';

  import Icon from '@jeesite/core/components/Icon';
  import { useI18n } from '@jeesite/core/hooks/web/useI18n';
  import { useDesign } from '@jeesite/core/hooks/web/useDesign';
  import { contentProps } from '../props';
  const { t } = useI18n();

  export default defineComponent({
    name: 'MenuItemContent',
    components: {
      Icon,
    },
    props: contentProps,
    setup(props) {
      const { prefixCls } = useDesign('basic-menu-item-content');
      const getI18nName = computed(() => t(props.item?.name));
      const getIcon = computed(() => props.item?.icon);
      const getColor = computed(() => props.item?.color);

      return {
        prefixCls,
        getI18nName,
        getIcon,
        getColor,
      };
    },
  });
</script>
