<template>
  <a-alert type="info" showIcon :class="['jeesite-table-select-bar']">
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
  import { Alert as AAlert } from 'antdv-next';
  import { useI18n } from '@jeesite/core/hooks/web/useI18n';

  const { t } = useI18n();

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
  .jeesite-table-select-bar {
    flex-grow: 1;
    padding: 2px 8px;

    .ant-btn-link {
      height: 20px;
      line-height: 20px;
    }
  }
</style>
