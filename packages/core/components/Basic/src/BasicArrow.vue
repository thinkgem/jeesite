<!--
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 * No deletion without permission, or be held responsible to law.
 * @author Vben、ThinkGem
-->
<template>
  <span :class="getClass">
    <Spin v-if="props.loading" size="small" :style="$attrs.iconStyle" />
    <Icon v-else :icon="getIcon" :style="$attrs.iconStyle" />
  </span>
</template>
<script lang="ts" setup>
  import { computed } from 'vue';
  import { Spin } from 'ant-design-vue';
  import { Icon } from '@jeesite/core/components/Icon';
  import { useDesign } from '@jeesite/core/hooks/web/useDesign';

  const props = defineProps({
    /**
     * Arrow expand state
     */
    expand: { type: Boolean },
    /**
     * Arrow up by default
     */
    up: { type: Boolean },
    /**
     * Arrow down by default
     */
    down: { type: Boolean },
    /**
     * Cancel padding/margin for inline
     */
    inset: { type: Boolean },
    /**
     * 是否是叶子节点
     */
    leaf: { type: Boolean },
    /**
     * 是否双箭头图标
     */
    double: { type: Boolean },
    /**
     * 是否加载状态
     */
    loading: { type: Boolean, defaultValue: false },
  });

  const { prefixCls } = useDesign('basic-arrow');

  const getIcon = computed(() => {
    const { leaf, double } = props;
    return leaf ? 'i-radix-icons:dot' : double ? 'i-ant-design:double-right-outlined' : 'i-ion:chevron-forward';
  });

  // get component class
  const getClass = computed(() => {
    const { expand, up, down, inset } = props;
    return [
      prefixCls,
      {
        [`${prefixCls}--active`]: expand,
        up,
        inset,
        down,
      },
    ];
  });
</script>
<style lang="less">
  @prefix-cls: ~'jeesite-basic-arrow';

  .@{prefix-cls} {
    display: inline-block;
    cursor: pointer;
    transform: rotate(0deg);
    transition: all 0.3s ease 0.1s;
    transform-origin: center center;

    &--active {
      transform: rotate(90deg);
    }

    &.inset {
      line-height: 0;
    }

    &.up {
      transform: rotate(-90deg);
    }

    &.down {
      transform: rotate(90deg);
    }

    &.up.@{prefix-cls}--active {
      transform: rotate(90deg);
    }

    &.down.@{prefix-cls}--active {
      transform: rotate(-90deg);
    }
  }
</style>
