<template>
  <vue-json-pretty :path="'res'" :deep="4" :showLength="true" :data="data" />
</template>
<script lang="ts" setup>
  import VueJsonPretty from 'vue-json-pretty';
  import 'vue-json-pretty/lib/styles.css';

  defineProps({
    data: Object,
  });
</script>
<style lang="less">
  .vjs-value-string {
    color: @text-color-base;
  }
</style>
