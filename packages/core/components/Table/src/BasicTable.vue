<!--
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 * No deletion without permission, or be held responsible to law.
 * @author Vben、ThinkGem
-->
<template>
  <div ref="wrapRef" :class="getWrapperClass">
    <TableHeader v-bind="getHeaderProps" :showSelectionBar="false">
      <template #tableTitle v-if="$slots.tableTitle">
        <slot name="tableTitle"></slot>
      </template>
      <template #toolbar v-if="$slots.tableTitle || $slots.toolbar">
        <a-button v-if="getBindValues.useSearchForm" @click="handleFormShowToggle()" :class="{ active: formShow }">
          <Icon icon="i-ant-design:filter-outlined" /> {{ formShow ? t('common.hideText') : t('common.queryText') }}
        </a-button>
        <slot v-if="$slots.toolbar" name="toolbar"></slot>
      </template>
      <template #headerTop v-if="$slots.headerTop">
        <slot name="headerTop"></slot>
      </template>
    </TableHeader>
    <BasicForm
      ref="formRef"
      submitOnReset
      v-show="formShow"
      v-bind="getFormProps"
      v-if="getBindValues.useSearchForm"
      :tableAction="tableAction"
      @register="registerForm"
      @submit="handleSearchInfoChange"
      @keypress.enter="handleSearchInfoChange"
      @advanced-change="redoHeight"
    >
      <template #[replaceFormSlotKey(item)]="data" v-for="item in getFormSlotKeys">
        <slot :name="item" v-bind="data || {}"></slot>
      </template>
    </BasicForm>
    <slot v-if="$slots.tableTop" name="tableTop"></slot>
    <div v-if="showSelectionBar" class="m-3 mt-0">
      <TableSelectionBar :clearSelectedRowKeys="getHeaderProps.clearSelectedRowKeys!" :count="getHeaderProps.count" />
    </div>
    <FormItemRest>
      <ATable
        ref="tableRef"
        v-bind="getBindValues"
        :rowClassName="getRowClassName"
        v-show="getEmptyDataIsShowTable"
        @change="handleTableChange"
      >
        <template #[item]="data" v-for="item in Object.keys($slots)" :key="item">
          <slot :name="item" v-bind="data || {}"></slot>
        </template>
        <template #headerCell="{ column }">
          <HeaderCell :column="column as any" />
        </template>
        <template #bodyCell="data: any">
          <template v-if="!data.column.customRender">
            <TableAction
              v-if="data.column.slot === 'tableActions'"
              :actions="tableActions.actions && tableActions.actions(data.record)"
              :dropDownActions="tableActions.dropDownActions && tableActions.dropDownActions(data.record)"
            />
            <DictLabel
              v-else-if="data.column.slot === 'dictLabelColumn'"
              :dictType="data.column.dictType"
              :dictValue="getColumnValue(data)"
              :defaultValue="data.column.defaultValue"
            />
            <slot v-else-if="data.column.slot" :name="data.column.slot" v-bind="getSlotData(data)"></slot>
          </template>
          <slot v-else name="bodyCell" v-bind="getSlotData(data)"></slot>
        </template>
      </ATable>
    </FormItemRest>
  </div>
</template>
<script lang="ts" setup name="BasicTable">
  import type { BasicTableProps, TableActionType, SizeType, ColumnChangeParam } from './types/table';
  import { ref, computed, unref, toRaw, inject, watchEffect, useSlots } from 'vue';
  import { Table, Form } from 'ant-design-vue';
  import { BasicForm, useForm } from '@jeesite/core/components/Form';
  import { PageWrapperFixedHeightKey } from '@jeesite/core/components/Page';
  import expandIcon from './components/ExpandIcon';
  import HeaderCell from './components/HeaderCell.vue';
  import TableAction from './components/TableAction.vue';
  import TableHeader from './components/TableHeader.vue';
  import { InnerHandlers } from './types/table';
  import { DictLabel } from '@jeesite/core/components/Dict';
  import { Icon } from '@jeesite/core/components/Icon';

  import { useI18n } from '@jeesite/core/hooks/web/useI18n';
  import { usePagination } from './hooks/usePagination';
  import { useColumns } from './hooks/useColumns';
  import { useDataSource } from './hooks/useDataSource';
  import { useLoading } from './hooks/useLoading';
  import { useRowSelection } from './hooks/useRowSelection';
  import { useTableScroll } from './hooks/useTableScroll';
  import { useTableScrollTo } from './hooks/useScrollTo';
  import { useCustomRow } from './hooks/useCustomRow';
  import { useTableStyle } from './hooks/useTableStyle';
  // import { useTableHeader } from './hooks/useTableHeader';
  import { useTableExpand } from './hooks/useTableExpand';
  import { createTableContext } from './hooks/useTableContext';
  import { useTableFooter } from './hooks/useTableFooter';
  import { useTableForm } from './hooks/useTableForm';
  import { useDesign } from '@jeesite/core/hooks/web/useDesign';

  import { omit } from 'lodash-es';
  import { basicProps } from './props';
  import { isFunction, isArray, isString } from '@jeesite/core/utils/is';
  import { warn } from '@jeesite/core/utils/log';
  import TableSelectionBar from '@jeesite/core/components/Table/src/components/TableSelectionBar.vue';
  import { useAttrs } from '@jeesite/core/hooks/core/useAttrs';

  const ATable = Table;
  const FormItemRest = Form.ItemRest;

  const props = defineProps(basicProps);
  const emit = defineEmits([
    'fetch-success',
    'fetch-error',
    'selection-change',
    'register',
    'row-click',
    'row-db-click',
    'row-contextmenu',
    'row-mouseenter',
    'row-mouseleave',
    'row-dragstart',
    'row-dragover',
    'row-drop',
    'edit-end',
    'edit-cancel',
    'edit-row-end',
    'edit-change',
    'expanded-rows-change',
    'change',
    'expand',
    'columns-change',
  ]);
  const attrs = useAttrs();
  const slots = useSlots();

  const { t } = useI18n();
  const tableRef = ref<ComponentRef>(null);
  const tableData = ref<Recordable[]>([]);

  const wrapRef = ref<ComponentRef>(null);
  const formRef = ref<ComponentRef>(null);
  const innerPropsRef = ref<Partial<BasicTableProps>>();

  const { prefixCls } = useDesign('basic-table');
  const [registerForm, formActions] = useForm();

  const getProps = computed(() => {
    return { ...props, ...unref(innerPropsRef) } as BasicTableProps;
  });

  const isFixedHeightPage = inject(PageWrapperFixedHeightKey, false);
  watchEffect(() => {
    unref(isFixedHeightPage) &&
      props.canResize &&
      warn("'canResize' of BasicTable may not work in PageWrapper with 'fixedHeight' (especially in hot updates)");
  });

  const { getLoading, setLoading } = useLoading(getProps);
  const { getPaginationInfo, getPagination, setPagination, setShowPagination, getShowPagination } =
    usePagination(getProps);

  const {
    getRowSelection,
    getRowSelectionRef,
    getSelectRows,
    clearSelectedRowKeys,
    getSelectRowKeys,
    deleteSelectRowByKey,
    setSelectedRowKeys,
  } = useRowSelection(getProps, tableData, emit);

  const getDefaultRowSelection = () => {
    return unref(getProps).defaultRowSelection || getRowSelection();
  };

  const { getExpandOption, expandAll, collapseAll, expandRows, expandCollapse } = useTableExpand(
    getProps,
    tableData,
    formActions.getFieldsValue,
    emit,
    setLoading,
  );

  const {
    handleTableChange: onTableChange,
    getDataSourceRef,
    getDataSource,
    getDelDataSource,
    getRawDataSource,
    setTableData,
    updateTableDataRecord,
    deleteTableDataRecord,
    insertTableDataRecord,
    findTableDataRecord,
    getRowKey,
    getAutoCreateKey,
    updateTableData,
    reload,
  } = useDataSource(
    getProps,
    {
      tableData,
      getPaginationInfo,
      setLoading,
      setPagination,
      getFieldsValue: formActions.getFieldsValue,
      clearSelectedRowKeys,
      collapseAll,
      expandCollapse,
    },
    emit,
  );

  function handleTableChange(pagination: any, filters: any, sorter: any, extra: any) {
    onTableChange(pagination, filters, sorter);
    emit('change', pagination, filters, sorter, extra);
    // 解决通过useTable注册onChange时不起作用的问题
    const { onChange } = unref(getProps);
    onChange && isFunction(onChange) && onChange(pagination, filters, sorter, extra);
  }

  function handleTableExpand(expanded: any, record: any) {
    emit('expand', expanded, record);
    // 解决通过useTable注册onExpand时不起作用的问题
    const { onExpand } = unref(getProps);
    onExpand && isFunction(onExpand) && onExpand(expanded, record);
  }

  const {
    getViewColumns,
    getColumns,
    // setCacheColumnsByField,
    setColumns,
    updateColumn,
    getColumnsRef,
    getCacheColumns,
  } = useColumns(getProps, getPaginationInfo);

  const { getScrollRef, redoHeight } = useTableScroll(
    getProps,
    tableRef,
    getColumnsRef,
    getRowSelectionRef,
    getDataSourceRef,
    wrapRef,
    formRef,
  );

  const { scrollTo } = useTableScrollTo(tableRef, getDataSourceRef);

  const { customRow } = useCustomRow(getProps, {
    setSelectedRowKeys,
    getSelectRowKeys,
    clearSelectedRowKeys,
    getAutoCreateKey,
    getDataSourceRef,
    tableRef,
    emit,
  });

  const { getRowClassName } = useTableStyle(getProps, prefixCls);

  const handlers: InnerHandlers = {
    onColumnsChange: (data: ColumnChangeParam[]) => {
      emit('columns-change', data);
      // support useTable
      unref(getProps).onColumnsChange?.(data);
    },
  };

  // const { getHeaderProps } = useTableHeader(getProps, slots, handlers, methods);
  const getHeaderProps = computed(() => {
    const { title, showTableSetting, titleHelpMessage, tableSetting, showSelectionBar } = unref(getProps);
    const hideTitle = !title && !slots.tableTitle && !slots.toolbar && !slots.headerTop && !showTableSetting;
    if (hideTitle && !isString(title)) {
      return { class: 'hidden' };
    }
    return {
      title,
      titleHelpMessage,
      showTableSetting,
      tableSetting,
      onColumnsChange: handlers.onColumnsChange,
      class: prefixCls + '-header-container',
      showSelectionBar,
      clearSelectedRowKeys,
      count: getSelectRowKeys().length,
    } as Recordable;
  });

  const { getFooterProps } = useTableFooter(getProps, getScrollRef, tableRef, getDataSourceRef);

  const { getFormProps, replaceFormSlotKey, getFormSlotKeys, handleSearchInfoChange } = useTableForm(
    getProps,
    slots,
    reload,
    getLoading,
  );

  const getBindValues = computed(() => {
    const { isTreeTable } = unref(getProps);
    let propsData: Recordable = {
      size: 'middle',
      showSorterTooltip: false,
      // ...(dataSource.length === 0 ? { getPopupContainer: () => document.body } : {}),
      ...attrs,
      customRow,
      expandIcon:
        (!isTreeTable && slots.expandedRowRender) || slots.expandIcon
          ? undefined
          : expandIcon(expandCollapse, handleTableExpand, !!slots.expandedRowRender),
      ...unref(getProps),
      // ...unref(getHeaderProps),
      scroll: unref(getScrollRef),
      loading: unref(getLoading),
      tableLayout: 'fixed',
      rowSelection: unref(getRowSelectionRef),
      rowKey: unref(getRowKey),
      columns: toRaw(unref(getViewColumns)),
      pagination: toRaw(unref(getPaginationInfo)),
      dataSource: unref(getDataSourceRef),
      footer: unref(getFooterProps),
      ...unref(getExpandOption),
      onExpand: handleTableExpand,
      onResizeColumn: (w, col) => {
        col.width = w;
        return false;
      },
    };
    // if (slots.expandedRowRender) { // 带展开的表格不显示水平滚动条问题
    //   propsData = omit(propsData, 'scroll');
    // }

    propsData = omit(propsData, ['class', 'onChange', 'title']);
    return propsData;
  });

  const tableActions = computed(() => {
    const { actionColumn } = unref(getProps);
    return {
      actions: actionColumn?.actions,
      dropDownActions: actionColumn?.dropDownActions,
    };
  });

  const getWrapperClass = computed(() => {
    const values = unref(getBindValues);
    return [
      prefixCls,
      attrs.class,
      {
        [`${prefixCls}-header-hidden`]: getHeaderProps.value.class === 'hidden',
        [`${prefixCls}-form-container`]: values.useSearchForm,
        [`${prefixCls}--inset`]: values.inset,
      },
    ];
  });

  const getEmptyDataIsShowTable = computed(() => {
    const { emptyDataIsShowTable, useSearchForm } = unref(getProps);
    if (emptyDataIsShowTable || !useSearchForm) {
      return true;
    }
    return !!unref(getDataSourceRef).length;
  });

  function setProps(props: Partial<BasicTableProps>) {
    innerPropsRef.value = { ...unref(innerPropsRef), ...props };
  }

  function getColumnValue(data: any) {
    const dataIndex = data.column.dataIndex;
    if (isArray(dataIndex)) {
      return dataIndex.reduce((pre, cur) => {
        return pre[cur];
      }, data.record);
    }
    return data.record[dataIndex];
  }

  function getSlotData(rawData: Recordable) {
    let data = rawData || {};
    if (!data.record) data.record = {};
    if (!data.record.dataMap) data.record.dataMap = {};
    return data;
  }

  const formShow = ref(unref(getProps).showSearchForm);

  function handleFormShowToggle() {
    formShow.value = !formShow.value;
    redoHeight();
  }

  function getSize() {
    return unref(getBindValues).size as SizeType;
  }

  function getTableRef() {
    return tableRef;
  }

  const tableAction: Partial<TableActionType> = {
    reload,
    setProps,
    setLoading,
    getTableRef,
    redoHeight,
    scrollTo,
    getSize,
    emit,

    getColumns,
    getCacheColumns,
    setColumns,
    updateColumn,
    // setCacheColumnsByField,

    getPagination,
    setPagination,
    getShowPagination,
    setShowPagination,

    getDataSource,
    getDelDataSource,
    getRawDataSource,

    setTableData,
    updateTableData,
    updateTableDataRecord,
    deleteTableDataRecord,
    insertTableDataRecord,
    findTableDataRecord,

    getRowSelection,
    getDefaultRowSelection,
    getSelectRows,
    getSelectRowKeys,
    setSelectedRowKeys,
    deleteSelectRowByKey,
    clearSelectedRowKeys,

    expandAll,
    expandRows,
    collapseAll,
    expandCollapse,
  };

  createTableContext({ ...(tableAction as TableActionType), wrapRef, getBindValues });
  emit('register', tableAction, formActions);

  defineExpose(tableAction);
</script>
<style lang="less">
  @prefix-cls: ~'jeesite-basic-table';

  html[data-theme='dark'] {
    .@{prefix-cls} {
      a,
      .ant-btn-link {
        color: #42a4e0;
      }

      .ant-table {
        &-tbody > tr > td.ant-table-column-sort {
          background-color: #1e1e1e;
        }

        .ant-table-tbody > tr {
          &:hover > td.ant-table-cell-row-hover {
            background-color: #262626;
          }

          &.ant-table-row-selected,
          &.ant-table-row-selected:hover {
            td,
            td.ant-table-cell-fix-left,
            td.ant-table-cell-fix-right {
              background-color: #2c2c2c;
            }
          }
        }
      }

      &-header-container {
        color: rgb(255 255 255 / 75%);
      }
    }
  }

  .@{prefix-cls} {
    max-width: 100%;
    background-color: @component-background;
    border-radius: 5px;

    a,
    .ant-btn-link {
      color: @primary-color;
    }

    .ant-table-wrapper {
      padding: 0 6px 6px;
      background-color: @component-background;
      border-radius: 5px;

      .ant-table {
        .ant-table-container {
          border-radius: 8px !important;
          border: 1px solid @table-border-color !important;

          .ant-table-thead > tr > th {
            font-weight: normal;
          }
        }

        .ant-table-column-sorter {
          margin: 0 -4px 0 -1px;
        }

        .ant-table-title {
          min-height: 40px;
          padding: 6px 0 8px !important;
          border: none !important;
        }

        &.ant-table-bordered > .ant-table-container {
          & > .ant-table-header > table,
          & > .ant-table-content > table {
            border-top: 0 !important;

            & > thead > tr > th:last-child {
              border-right: 0 !important;
            }
          }
        }

        td.ant-table-cell-fix-left,
        td.ant-table-cell-fix-right {
          .jeesite-basic-arrow {
            vertical-align: middle;
          }

          .ant-table-cell-content {
            margin-left: 3px;
            display: inline-block;
            vertical-align: middle;
          }
        }
      }

      .@{prefix-cls}-row__striped {
        td,
        td.ant-table-cell-fix-left,
        td.ant-table-cell-fix-right {
          background-color: @content-bg-striped;

          .ant-table-cell-content {
            width: auto;
          }
        }
      }

      .ant-table-tbody > tr {
        &:hover > td.ant-table-cell-row-hover {
          background-color: lighten(@primary-color, 43%); // #f3f5fe
        }

        &.ant-table-row-selected,
        &.ant-table-row-selected:hover {
          td,
          td.ant-table-cell-fix-left,
          td.ant-table-cell-fix-right {
            background-color: lighten(@primary-color, 41%);
          }
        }
      }

      .ant-pagination.ant-table-pagination {
        transition: all 0.3s;
        margin: 10px 0 0;
        display: none;

        &.ant-pagination-mini {
          .ant-pagination-prev,
          .ant-pagination-next {
            line-height: 23px;
          }
          //.ant-pagination-item {
          //  line-height: 20px;
          //}

          .ant-pagination-options-quick-jumper {
            input {
              width: 42px;
            }
          }

          .ant-select {
            margin-left: 5px;

            .ant-select-arrow {
              right: 8px;
            }

            .ant-select-selection-item {
              padding-right: 18px;
            }
          }
        }
      }

      .ant-table-row-expand-icon {
        margin-left: 7px;
      }

      tr.dragover {
        &-top td {
          border-top: 2px solid rgb(22 119 255 / 30%) !important;
          border-top-left-radius: 0 !important;
          border-top-right-radius: 0 !important;
        }

        &-bottom td {
          border-bottom: 2px solid rgb(22 119 255 / 30%) !important;
          border-bottom-left-radius: 0 !important;
          border-bottom-right-radius: 0 !important;
        }
      }
    }

    &-header-container {
      color: fade(@text-color-base, 75);
      padding: 5px 2px 5px 7px;
      border-radius: 5px;
      min-height: 44px;
    }

    &-form-container {
      // padding: 15px 15px 0 15px;

      .ant-form {
        padding: 12px 10px 6px;
        background-color: @component-background;
        // border-radius: 5px;
        border-top: 1px solid @table-border-color !important;
        margin-bottom: 0;
      }
    }

    &-header-hidden {
      .ant-form {
        padding-top: 0;
        border-top: 0 !important;
      }
    }

    .ant-tag {
      margin-right: 0;
    }

    // .ant-table {
    //   width: 100%;
    //   overflow-x: hidden;

    //   &-title {
    //     display: flex;
    //     padding: 8px 6px;
    //     border-bottom: none;
    //     justify-content: space-between;
    //     align-items: center;
    //   }

    //   &-tbody > tr > td.ant-table-column-sort {
    //     background-color: @content-bg-striped;
    //   }

    //   //.ant-table-tbody > tr.ant-table-row-selected td {
    //   //background-color: fade(@primary-color, 8%) !important;
    //   //}
    // }

    // 选择列为 0 的时候，不显示选择列
    .ant-table-selection-column {
      overflow: hidden;
    }

    .ant-table-footer {
      padding: 0;

      .ant-table-wrapper {
        padding: 0;
      }

      table {
        border: none !important;
      }

      .ant-table-body {
        overflow-x: hidden !important;
        //  overflow-y: scroll !important;
      }

      td {
        padding: 12px 8px;
      }
    }

    &--inset {
      .ant-table-wrapper {
        padding: 0;
      }

      .ant-table-placeholder {
        padding: 10px;

        .ant-empty-normal {
          margin: 0;

          .ant-empty-image {
            display: none;
          }

          .ant-empty-description {
            padding: 0;
            margin: 0;
          }
        }
      }

      .ant-table-wrapper .ant-table {
        &.ant-table-bordered {
          &.ant-table-small {
            > .ant-table-container {
              > .ant-table-content,
              > .ant-table-body {
                > table > tbody > tr > td {
                  > .ant-table-expanded-row-fixed {
                    margin: -15px -9px;
                  }
                }
              }
            }
          }
        }
      }

      textarea.ant-input {
        min-height: 32px;
        height: 32px;

        &-sm {
          min-height: 24px;
          height: 24px;
        }
      }
    }
  }
</style>
