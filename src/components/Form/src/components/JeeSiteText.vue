<!--
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 * No deletion without permission, or be held responsible to law.
 * @description 用于文本展示
 * @author ThinkGem
-->
<template>
  <div class="jeesite-text">
    <DictLabel v-if="dictType" :dictType="dictType" :dictValue="value" />
    <!-- eslint-disable vue/no-v-html -->
    <div v-else-if="isHtml" v-html="labelValue || value" class="p-1"></div>
    <div v-else>
      {{ labelValue || value }}
    </div>
  </div>
</template>
<script lang="ts" setup name="JeeSiteText">
  import { propTypes } from '/@/utils/propTypes';
  import { DictLabel } from '/@/components/Dict';

  defineProps({
    value: {
      type: [Array, Object, String, Number] as PropType<Array<any> | object | string | number>,
    },
    labelValue: {
      type: [Array, Object, String, Number] as PropType<Array<any> | object | string | number>,
    },
    dictType: propTypes.string,
    isHtml: propTypes.bool,
  });
</script>
<style lang="less">
  @prefix-cls: ~'jeesite-text';

  .@{prefix-cls} {
    width: 100%;
    //border: 1px solid #ddd;
    //padding: 4px 8px;
    //border-radius: 5px;
  }
</style>
