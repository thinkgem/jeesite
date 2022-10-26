<!--
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 * No deletion without permission, or be held responsible to law.
 * @description 用于文本展示
 * @author ThinkGem
-->
<template>
  <div class="jeesite-text">
    {{ labelValue || value }}
  </div>
</template>
<script lang="ts">
  import { defineComponent } from 'vue';

  export default defineComponent({
    name: 'JeeSiteText',
    // inheritAttrs: false,
    props: {
      value: {
        type: Object as PropType<object>,
      },
      labelValue: {
        type: Object as PropType<object>,
      },
    },
    setup() {
      return {};
    },
  });
</script>
<style lang="less">
  @prefix-cls: ~'jeesite-text';

  .@{prefix-cls} {
    width: 100%;
    padding-left: 5px;
  }
</style>
