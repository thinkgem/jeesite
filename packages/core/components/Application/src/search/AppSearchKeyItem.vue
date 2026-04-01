<template>
  <span :class="$attrs.class as string">
    <Icon :icon="icon" />
  </span>
</template>
<script lang="ts" setup>
  import { Icon } from '@jeesite/core/components/Icon';
  defineProps({
    icon: String,
  });
</script>
