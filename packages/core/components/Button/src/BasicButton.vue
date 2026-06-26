<template>
  <Button v-bind="getBindValue" :class="getButtonClass">
    <template #default>
      <Icon :icon="preIcon" v-if="preIcon" :size="iconSize" />
      <slot></slot>
      <Icon :icon="postIcon" v-if="postIcon" :size="iconSize" />
    </template>
  </Button>
</template>
<script lang="ts" setup name="AButton">
  import { computed, unref } from 'vue';
  import { Button } from 'antdv-next';
  import { Icon } from '@jeesite/core/components/Icon';
  import { useAttrs } from '@jeesite/core/hooks/core/useAttrs';
  import { buttonProps } from './props';

  defineOptions({
    inheritAttrs: false,
  });

  const props = defineProps(buttonProps);

  const attrs = useAttrs({ excludeDefaultKeys: false });
  const getButtonClass = computed(() => {
    const { color, disabled } = props;
    return [
      {
        [`ant-btn-${color}`]: !!color,
        [`is-disabled`]: disabled,
      },
    ];
  });

  const getBindValue = computed(() => ({ ...unref(attrs), ...props }));
</script>
