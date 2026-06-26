<!--
 * Copyright (c) 2013-Now https://jeesite.com All rights reserved.
 * No deletion without permission, or be held responsible to law.
 * @author Vben、ThinkGem
-->
<template>
  <div style="width: 100%">
    <div v-if="$slots.headerTop" style="margin: 5px">
      <slot name="headerTop"></slot>
    </div>
    <div class="flex items-center">
      <div class="jeesite-basic-table-header__title">
        <slot name="tableTitle" v-if="$slots.tableTitle"></slot>
        <TableTitle :helpMessage="titleHelpMessage" :title="title" v-if="!$slots.tableTitle && title" />
      </div>
      <div class="jeesite-basic-table-header__toolbar">
        <slot name="toolbar"></slot>
        <!-- <Divider type="vertical" v-if="$slots.toolbar && showTableSetting" /> -->
        <TableSetting :setting="tableSetting" v-if="showTableSetting" @columns-change="handleColumnChange" />
      </div>
    </div>
    <div v-if="showSelectionBar" class="m-1 mt-2">
      <TableSelectionBar :clearSelectedRowKeys="props.clearSelectedRowKeys!" :count="props.count" />
    </div>
  </div>
</template>
<script lang="ts">
  import type { TableSetting, ColumnChangeParam, TableActionType } from '../types/table';
  import type { PropType } from 'vue';
  import { defineComponent } from 'vue';
  // import { Divider } from 'antdv-next';
  import TableSettingComponent from './settings/index.vue';
  import TableTitle from './TableTitle.vue';
  import TableSelectionBar from '../components/TableSelectionBar.vue';

  export default defineComponent({
    name: 'BasicTableHeader',
    components: {
      // Divider,
      TableTitle,
      TableSetting: TableSettingComponent,
      TableSelectionBar,
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
      showSelectionBar: {
        type: Boolean,
        default: false,
      },
      clearSelectedRowKeys: {
        type: Function as PropType<TableActionType['clearSelectedRowKeys']>,
      },
      count: {
        type: Number,
        default: 0,
      },
    },
    emits: ['columns-change'],
    setup(props, { emit }) {
      function handleColumnChange(data: ColumnChangeParam[]) {
        emit('columns-change', data);
      }
      return { props, handleColumnChange };
    },
  });
</script>
<style lang="less">
  .jeesite-basic-table-header {
    &__title {
      font-size: 16px;

      > .anticon {
        color: @primary-color;
      }

      > .ant-tabs {
        & .ant-tabs-tab {
          padding: 4px 0;
          margin-left: 5px;

          & + .ant-tabs-tab {
            margin: 0 0 0 15px;
          }

          .ant-tabs-tab-btn {
            .anticon {
              margin-left: 3px;
              margin-right: 8px;
            }
          }

          .ant-tabs-tab-btn,
          .ant-tabs-tab-btn .anticon {
            color: fade(@text-color-base, 60);
            font-size: 16px;
            text-shadow: none;
          }

          &.ant-tabs-tab-active {
            .ant-tabs-tab-btn,
            .ant-tabs-tab-btn .anticon {
              color: fade(@primary-color, 90);
            }
          }
        }
      }

      > .ant-tabs-top,
      > .ant-tabs-bottom {
        & > .ant-tabs-nav,
        & > div > .ant-tabs-nav {
          margin: 0;
        }
      }

      > .ant-tabs-top {
        // margin-bottom: -15px;
        & > .ant-tabs-nav {
          &::before {
            border-bottom: 0;
          }
        }
      }

      > .ant-tabs-card .ant-tabs-nav {
        margin: 0;

        &-list {
          .ant-tabs-tab {
            padding: 5px 8px;
            border-bottom: 0;
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
    }

    &__title,
    &__toolbar {
      .ant-btn {
        padding: 2px 10px;
        line-height: 1.5;
        height: 30px;
        margin-top: 2px;
      }
    }
  }
</style>
