<template>
  <span class="jeesite-basic-menu-item-content- flex items-center" :style="`color: ${getColor}`">
    <Icon v-if="getIcon" :icon="getIcon" :size="18" class="jeesite-basic-menu-item-content-wrapper__icon mr-2" />
    {{ getI18nName }}
  </span>
</template>
<script lang="ts">
  import { computed, defineComponent } from 'vue';

  import Icon from '@jeesite/core/components/Icon';
  import { useI18n } from '@jeesite/core/hooks/web/useI18n';
  import { contentProps } from '../props';
  const { t } = useI18n();

  export default defineComponent({
    name: 'MenuItemContent',
    components: {
      Icon,
    },
    props: contentProps,
    setup(props) {
      const getI18nName = computed(() => t(props.item?.name));
      const getIcon = computed(() => props.item?.icon);
      const getColor = computed(() => props.item?.color);

      return {
        getI18nName,
        getIcon,
        getColor,
      };
    },
  });
</script>
