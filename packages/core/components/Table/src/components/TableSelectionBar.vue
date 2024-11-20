<template>
  <a-alert type="info" showIcon :class="[prefixCls]">
    <template #message>
      <span v-if="props.count > 0">
        {{ t('component.table.selectionBarTips', { count: props.count }) }}
      </span>
      <span v-else>
        {{ t('component.table.selectionBarEmpty') }}
      </span>
      <a-button type="link" @click="clearSelectedRowKeys" size="small" v-show="props.count > 0">
        {{ t('component.table.selectionBarClear') }}
      </a-button>
    </template>
  </a-alert>
</template>

<script lang="ts" setup name="TableSelectBar">
  import type { TableActionType } from '../types/table';
  import { Alert as AAlert } from 'ant-design-vue';
  import { useI18n } from '@jeesite/core/hooks/web/useI18n';
  import { useDesign } from '@jeesite/core/hooks/web/useDesign';

  const { t } = useI18n();
  const { prefixCls } = useDesign('table-select-bar');

  const props = withDefaults(
    defineProps<{
      count?: number;
      clearSelectedRowKeys: TableActionType['clearSelectedRowKeys'];
    }>(),
    {
      count: () => 0,
    },
  );
</script>

<style lang="less">
  @prefix-cls: ~'jeesite-table-select-bar';

  .@{prefix-cls} {
    flex-grow: 1;
    padding: 2px 8px;

    :deep(.ant-btn-link) {
      height: 20px;
      line-height: 20px;
    }
  }
</style>
