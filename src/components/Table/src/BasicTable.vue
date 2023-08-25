<!--
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 * No deletion without permission, or be held responsible to law.
 * @author Vben、ThinkGem
-->
<template>
  <div ref="wrapRef" :class="getWrapperClass">
    <BasicForm
      ref="formRef"
      submitOnReset
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
    <FormItemRest>
      <ATable
        ref="tableElRef"
        v-bind="getBindValues"
        :rowClassName="getRowClassName"
        v-show="getEmptyDataIsShowTable"
        @change="handleTableChange"
      >
        <template #[item]="data" v-for="item in Object.keys($slots)" :key="item">
          <slot :name="item" v-bind="data || {}"></slot>
        </template>
        <template #headerCell="{ column }">
          <HeaderCell :column="column" />
        </template>
        <template #bodyCell="data">
          <template v-if="!data.column.customRender">
            <TableAction
              v-if="data.column.slot === 'tableActions'"
              :actions="tableActions.actions && tableActions.actions(data.record)"
              :dropDownActions="
                tableActions.dropDownActions && tableActions.dropDownActions(data.record)
              "
            />
            <DictLabel
              v-else-if="data.column.slot === 'dictLabelColumn'"
              :dictType="data.column.dictType"
              :dictValue="getColumnValue(data)"
              :defaultValue="data.column.defaultValue"
            />
            <slot
              v-else-if="data.column.slot"
              :name="data.column.slot"
              v-bind="getSlotData(data)"
            ></slot>
          </template>
          <slot v-else name="bodyCell" v-bind="getSlotData(data)"></slot>
        </template>
      </ATable>
    </FormItemRest>
  </div>
</template>
<script lang="ts">
  import type {
    BasicTableProps,
    TableActionType,
    SizeType,
    ColumnChangeParam,
  } from './types/table';

  import { defineComponent, ref, computed, unref, toRaw, inject, watchEffect } from 'vue';
  import { Table, Form } from 'ant-design-vue';
  import { BasicForm, useForm } from '/@/components/Form/index';
  import { PageWrapperFixedHeightKey } from '/@/components/Page';
  import expandIcon from './components/ExpandIcon';
  import HeaderCell from './components/HeaderCell.vue';
  import TableAction from './components/TableAction.vue';
  import { InnerHandlers } from './types/table';
  import { DictLabel } from '/@/components/Dict';

  import { usePagination } from './hooks/usePagination';
  import { useColumns } from './hooks/useColumns';
  import { useDataSource } from './hooks/useDataSource';
  import { useLoading } from './hooks/useLoading';
  import { useRowSelection } from './hooks/useRowSelection';
  import { useTableScroll } from './hooks/useTableScroll';
  import { useTableScrollTo } from './hooks/useScrollTo';
  import { useCustomRow } from './hooks/useCustomRow';
  import { useTableStyle } from './hooks/useTableStyle';
  import { useTableHeader } from './hooks/useTableHeader';
  import { useTableExpand } from './hooks/useTableExpand';
  import { createTableContext } from './hooks/useTableContext';
  import { useTableFooter } from './hooks/useTableFooter';
  import { useTableForm } from './hooks/useTableForm';
  import { useDesign } from '/@/hooks/web/useDesign';

  import { omit } from 'lodash-es';
  import { basicProps } from './props';
  import { isFunction, isArray } from '/@/utils/is';
  import { warn } from '/@/utils/log';

  export default defineComponent({
    components: {
      ATable: Table,
      FormItemRest: Form.ItemRest,
      BasicForm,
      HeaderCell,
      TableAction,
      DictLabel,
    },
    props: basicProps,
    emits: [
      'fetch-success',
      'fetch-error',
      'selection-change',
      'register',
      'row-click',
      'row-dbClick',
      'row-contextmenu',
      'row-mouseenter',
      'row-mouseleave',
      'edit-end',
      'edit-cancel',
      'edit-row-end',
      'edit-change',
      'expanded-rows-change',
      'change',
      'expand',
      'columns-change',
    ],
    setup(props, { attrs, emit, slots, expose }) {
      const tableElRef = ref(null);
      const tableData = ref<Recordable[]>([]);

      const wrapRef = ref(null);
      const formRef = ref(null);
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
          warn(
            "'canResize' of BasicTable may not work in PageWrapper with 'fixedHeight' (especially in hot updates)",
          );
      });

      const { getLoading, setLoading } = useLoading(getProps);
      const {
        getPaginationInfo,
        getPagination,
        setPagination,
        setShowPagination,
        getShowPagination,
      } = usePagination(getProps);

      const {
        getRowSelection,
        getRowSelectionRef,
        getSelectRows,
        clearSelectedRowKeys,
        getSelectRowKeys,
        deleteSelectRowByKey,
        setSelectedRowKeys,
      } = useRowSelection(getProps, tableData, emit);

      const { getExpandOption, expandAll, collapseAll, expandRows, expandCollapse } =
        useTableExpand(getProps, tableData, formActions.getFieldsValue, emit, setLoading);

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
        fetch,
        getRowKey,
        reload,
        getAutoCreateKey,
        updateTableData,
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

      function handleTableChange(...args: any[]) {
        onTableChange.call(undefined, ...args);
        emit('change', ...args);
        // 解决通过useTable注册onChange时不起作用的问题
        const { onChange } = unref(getProps);
        onChange && isFunction(onChange) && onChange.call(undefined, ...args);
      }

      function handleTableExpand(...args: any[]) {
        emit('expand', ...args);
        // 解决通过useTable注册onExpand时不起作用的问题
        const { onExpand } = unref(getProps);
        onExpand && isFunction(onExpand) && onExpand.call(undefined, ...args);
      }

      const {
        getViewColumns,
        getColumns,
        setCacheColumnsByField,
        setColumns,
        updateColumn,
        getColumnsRef,
        getCacheColumns,
      } = useColumns(getProps, getPaginationInfo);

      const { getScrollRef, redoHeight } = useTableScroll(
        getProps,
        tableElRef,
        getColumnsRef,
        getRowSelectionRef,
        getDataSourceRef,
        wrapRef,
        formRef,
      );

      const { scrollTo } = useTableScrollTo(tableElRef, getDataSourceRef);

      const { customRow } = useCustomRow(getProps, {
        setSelectedRowKeys,
        getSelectRowKeys,
        clearSelectedRowKeys,
        getAutoCreateKey,
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

      const { getHeaderProps } = useTableHeader(getProps, slots, handlers);

      const { getFooterProps } = useTableFooter(
        getProps,
        getScrollRef,
        tableElRef,
        getDataSourceRef,
      );

      const { getFormProps, replaceFormSlotKey, getFormSlotKeys, handleSearchInfoChange } =
        useTableForm(getProps, slots, fetch, getLoading);

      const getBindValues = computed(() => {
        const dataSource = unref(getDataSourceRef);
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
          ...unref(getHeaderProps),
          scroll: unref(getScrollRef),
          loading: unref(getLoading),
          tableLayout: 'fixed',
          rowSelection: unref(getRowSelectionRef),
          rowKey: unref(getRowKey),
          columns: toRaw(unref(getViewColumns)),
          pagination: toRaw(unref(getPaginationInfo)),
          dataSource,
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

        propsData = omit(propsData, ['class', 'onChange']);
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

      const tableAction: TableActionType = {
        reload,
        getSelectRows,
        clearSelectedRowKeys,
        getSelectRowKeys,
        deleteSelectRowByKey,
        setPagination,
        setTableData,
        updateTableDataRecord,
        deleteTableDataRecord,
        insertTableDataRecord,
        findTableDataRecord,
        redoHeight,
        setSelectedRowKeys,
        setColumns,
        updateColumn,
        setLoading,
        getDataSource,
        getDelDataSource,
        getRawDataSource,
        setProps,
        getRowSelection,
        getPaginationRef: getPagination,
        getColumns,
        getCacheColumns,
        emit,
        updateTableData,
        setShowPagination,
        getShowPagination,
        setCacheColumnsByField,
        expandAll,
        expandRows,
        collapseAll,
        expandCollapse,
        scrollTo,
        getSize: () => {
          return unref(getBindValues).size as SizeType;
        },
      };
      createTableContext({ ...tableAction, wrapRef, getBindValues });

      expose(tableAction);

      emit('register', tableAction, formActions);

      function getSlotData(data: Recordable) {
        if (data) {
          if (!data.record) {
            data.record = {};
          }
          if (!data.record.dataMap) {
            data.record.dataMap = {};
          }
        }
        return data || {};
      }

      return {
        formRef,
        tableElRef,
        getBindValues,
        getLoading,
        registerForm,
        handleSearchInfoChange,
        getEmptyDataIsShowTable,
        handleTableChange,
        handleTableExpand,
        getRowClassName,
        wrapRef,
        tableAction,
        redoHeight,
        getFormProps: getFormProps as any,
        replaceFormSlotKey,
        getFormSlotKeys,
        getWrapperClass,
        columns: getViewColumns,
        getColumnValue,
        tableActions,
        getSlotData,
      };
    },
  });
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
    }
  }

  .@{prefix-cls} {
    max-width: 100%;

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
          border-radius: 8px;

          .ant-table-thead > tr > th {
            font-weight: normal;
          }
        }

        .ant-table-title {
          min-height: 40px;
          padding: 6px 0 8px 0 !important;
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
      }

      .@{prefix-cls}-row__striped {
        td,
        td.ant-table-cell-fix-left,
        td.ant-table-cell-fix-right {
          background-color: @content-bg-striped;
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

      .ant-table-pagination.ant-pagination {
        margin: 10px 0 0 0;
        &.ant-pagination-mini {
          .ant-pagination-item {
            line-height: 20px;
          }
          .ant-select {
            margin-left: 5px;
            &.ant-select-in-form-item {
              width: auto;
            }
          }
        }
      }

      .ant-table-row-expand-icon {
        margin-left: 7px;
      }
    }

    &-form-container {
      // padding: 15px 15px 0 15px;

      .ant-form {
        padding: 12px 10px 6px 10px;
        margin-bottom: 15px;
        background-color: @component-background;
        border-radius: 5px;
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
    }
  }
</style>
