<!--
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 * No deletion without permission, or be held responsible to law.
 * @author Vben、ThinkGem
-->
<template>
  <div v-bind="$attrs" class="jeesite-form-group">
    <div class="title">
      <slot></slot>
    </div>
  </div>
</template>
<script lang="ts">
  import { defineComponent } from 'vue';
  export default defineComponent({
    name: 'JeeSiteFormGroup',
    inheritAttrs: false,
  });
</script>
<style lang="less">
  @prefix-cls: ~'jeesite-form-group';

  .@{prefix-cls} {
    .title {
      //width: 100%;
      font-size: 15px;
      //font-style: oblique;
      padding: 0 0 3px 3px;
      margin: 0 0 20px 20px;
      border-bottom: 1px solid @header-light-bottom-border-color;
      color: @primary-color;
    }
  }
</style>
