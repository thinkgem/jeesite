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
<script lang="ts" setup name="JeeSiteFormGroup"></script>
<style lang="less">
  @prefix-cls: ~'jeesite-form-group';

  .@{prefix-cls} {
    .title {
      //width: 100%;
      font-size: 15px;
      //font-style: oblique;
      padding: 0 0 3px 8px;
      margin: 0 0 20px 15px;
      border-bottom: 1px solid @header-light-bottom-border-color;
      color: fade(@primary-color, 90%);
    }

    &::before {
      content: '';
      position: absolute;
      top: 4px;
      left: 15px;
      width: 3px;
      height: 17px;
      background-color: fade(@primary-color, 80%);
      border-radius: 4px;
    }
  }
</style>
