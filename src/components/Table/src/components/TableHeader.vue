<!--
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 * No deletion without permission, or be held responsible to law.
 * @author Vben、ThinkGem
-->
<template>
  <div style="width: 100%">
    <div v-if="$slots.headerTop" style="margin: 5px">
      <slot name="headerTop"></slot>
    </div>
    <div class="flex items-center">
      <div :class="`${prefixCls}__title`">
        <slot name="tableTitle" v-if="$slots.tableTitle"></slot>
        <TableTitle
          :helpMessage="titleHelpMessage"
          :title="title"
          v-if="!$slots.tableTitle && title"
        />
      </div>
      <div :class="`${prefixCls}__toolbar`">
        <slot name="toolbar"></slot>
        <Divider type="vertical" v-if="$slots.toolbar && showTableSetting" />
        <TableSetting
          :setting="tableSetting"
          v-if="showTableSetting"
          @columns-change="handleColumnChange"
        />
      </div>
    </div>
    <slot name="tableTop"></slot>
  </div>
</template>
<script lang="ts">
  import type { TableSetting, ColumnChangeParam } from '../types/table';
  import type { PropType } from 'vue';
  import { defineComponent } from 'vue';
  import { Divider } from 'ant-design-vue';
  import TableSettingComponent from './settings/index.vue';
  import TableTitle from './TableTitle.vue';
  import { useDesign } from '/@/hooks/web/useDesign';

  export default defineComponent({
    name: 'BasicTableHeader',
    components: {
      Divider,
      TableTitle,
      TableSetting: TableSettingComponent,
    },
    props: {
      title: {
        type: [Function, String] as PropType<string | ((data: Recordable) => string)>,
      },
      tableSetting: {
        type: Object as PropType<TableSetting>,
      },
      showTableSetting: {
        type: Boolean,
      },
      titleHelpMessage: {
        type: [String, Array] as PropType<string | string[]>,
        default: '',
      },
    },
    emits: ['columns-change'],
    setup(_, { emit }) {
      const { prefixCls } = useDesign('basic-table-header');
      function handleColumnChange(data: ColumnChangeParam[]) {
        emit('columns-change', data);
      }
      return { prefixCls, handleColumnChange };
    },
  });
</script>
<style lang="less">
  @prefix-cls: ~'jeesite-basic-table-header';

  .@{prefix-cls} {
    &__title {
      font-size: 16px;

      .anticon {
        color: @primary-color;
      }

      .ant-tabs-nav {
        margin: 0;
        &-list {
          .ant-tabs-tab {
            padding: 5px 8px;
            color: @text-color-call-out;
            .anticon {
              color: @text-color-call-out;
            }
            &-active {
              .anticon {
                color: @primary-color;
              }
            }
          }
        }
      }
    }

    &__toolbar {
      flex: 1;
      display: flex;
      align-items: center;
      justify-content: flex-end;
      flex-wrap: wrap;

      > * {
        margin-right: 8px;
      }

      .ant-btn {
        border-radius: 4px;
        padding: 2px 10px;
        line-height: 1.5;
        height: 30px;
      }
    }
  }
</style>
