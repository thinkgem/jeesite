<!--
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 * No deletion without permission, or be held responsible to law.
 * @author Vben、ThinkGem
-->
<template>
  <Tooltip placement="topRight">
    <template #title>
      <span>{{ t('component.table.settingColumn') }}</span>
    </template>
    <Popover
      placement="bottomLeft"
      trigger="click"
      @open-change="handleOpenChange"
      :overlayClassName="`${prefixCls}__cloumn-list`"
      :getPopupContainer="getPopupContainer"
    >
      <template #title>
        <div :class="`${prefixCls}__popover-title`">
          <Checkbox
            :indeterminate="indeterminate"
            v-model:checked="checkAll"
            @change="onCheckAllChange"
          >
            {{ t('component.table.settingColumnShow') }}
          </Checkbox>

          <Checkbox
            v-model:checked="checkIndex"
            @change="handleIndexCheckChange"
            v-if="!isTreeTable"
          >
            {{ t('component.table.settingIndexColumnShow') }}
          </Checkbox>

          <Checkbox
            v-model:checked="checkSelect"
            @change="handleSelectCheckChange"
            :disabled="!defaultRowSelection"
          >
            {{ t('component.table.settingSelectColumnShow') }}
          </Checkbox>

          <a-button size="small" type="link" @click="reset">
            {{ t('common.resetText') }}
          </a-button>
        </div>
      </template>
      <template #content>
        <ScrollContainer>
          <CheckboxGroup v-model:value="checkedList" @change="onChange" ref="columnListRef">
            <template v-for="item in checkOptions" :key="item.value">
              <div :class="`${prefixCls}__check-item`" v-if="!('ifShow' in item && !item.ifShow)">
                <DragOutlined class="table-column-drag-icon" />
                <Checkbox :value="item.value">
                  {{ item.label }}
                </Checkbox>
                <Tooltip
                  placement="left"
                  :mouseLeaveDelay="0.4"
                  :getPopupContainer="getPopupContainer"
                >
                  <template #title>
                    {{ t('component.table.settingFixedLeft') }}
                  </template>
                  <Icon
                    icon="line-md:arrow-align-left"
                    :class="[
                      `${prefixCls}__fixed-left`,
                      {
                        active: item.fixed === 'left',
                        disabled: !checkedList.includes(item.value),
                      },
                    ]"
                    @click="handleColumnFixed(item, 'left')"
                  />
                </Tooltip>
                <Divider type="vertical" />
                <Tooltip
                  placement="right"
                  :mouseLeaveDelay="0.4"
                  :getPopupContainer="getPopupContainer"
                >
                  <template #title>
                    {{ t('component.table.settingFixedRight') }}
                  </template>
                  <Icon
                    icon="line-md:arrow-align-right"
                    :class="[
                      `${prefixCls}__fixed-right`,
                      {
                        active: item.fixed === 'right',
                        disabled: !checkedList.includes(item.value),
                      },
                    ]"
                    @click="handleColumnFixed(item, 'right')"
                  />
                </Tooltip>
              </div>
            </template>
          </CheckboxGroup>
        </ScrollContainer>
      </template>
      <SettingOutlined />
    </Popover>
  </Tooltip>
</template>
<script lang="ts">
  import {
    defineComponent,
    ref,
    reactive,
    toRefs,
    watchEffect,
    nextTick,
    unref,
    computed,
    watch,
  } from 'vue';
  import { Tooltip, Popover, Checkbox, Divider } from 'ant-design-vue';
  import type { CheckboxChangeEvent } from 'ant-design-vue/lib/checkbox/interface';
  import { SettingOutlined, DragOutlined } from '@ant-design/icons-vue';
  import { Icon } from '/@/components/Icon';
  import { ScrollContainer } from '/@/components/Container';
  import { useI18n } from '/@/hooks/web/useI18n';
  import { useTableContext } from '../../hooks/useTableContext';
  import { useDesign } from '/@/hooks/web/useDesign';
  // import { useSortable } from '/@/hooks/web/useSortable';
  import type { BasicColumn, ColumnChangeParam } from '../../types/table';
  import { isFunction, isNullAndUnDef } from '/@/utils/is';
  import { getPopupContainer as getParentContainer } from '/@/utils';
  import { cloneDeep, omit } from 'lodash-es';
  import Sortablejs from 'sortablejs';
  // import type Sortable from 'sortablejs';

  interface State {
    isInit?: boolean;
    checkAll: boolean;
    checkIndex: boolean;
    checkSelect: boolean;
    checkOptions: Options[];
    checkedList: string[];
  }

  interface Options {
    label: string;
    value: string;
    fixed?: boolean | 'left' | 'right';
  }

  export default defineComponent({
    name: 'ColumnSetting',
    components: {
      SettingOutlined,
      Popover,
      Tooltip,
      Checkbox,
      CheckboxGroup: Checkbox.Group,
      DragOutlined,
      ScrollContainer,
      Divider,
      Icon,
    },
    emits: ['columns-change'],

    setup(_, { emit, attrs }) {
      const { t } = useI18n();
      const { prefixCls } = useDesign('basic-column-setting');

      const table = useTableContext();

      const defaultRowSelection = omit(
        table.getDefaultRowSelection && table.getDefaultRowSelection(),
        'selectedRowKeys',
      );
      const columnListRef = ref<ComponentRef>(null);
      const cacheCheckIndex = ref<boolean>(true);
      const cacheCheckSelect = ref<boolean>(false);
      const cacheCheckList = ref<string[]>([]);
      const cacheCheckOptions = ref<Options[]>([]);
      let isInitSortable = false;

      const state = reactive<State>({
        checkAll: true,
        checkIndex: true,
        checkSelect: false,
        checkOptions: [],
        checkedList: [],
      });

      watchEffect(() => {
        setTimeout(() => {
          const columns = table.getColumns();
          if (columns.length && !state.isInit) {
            init();
          }
        }, 0);
      });

      watch(
        [
          () => unref(table?.getBindValues).showIndexColumn,
          () => unref(table?.getBindValues).rowSelection,
        ],
        () => {
          const values = unref(table?.getBindValues) || {};
          state.checkIndex = !!values.showIndexColumn;
          state.checkSelect = !!values.rowSelection;
        },
      );

      const isTreeTable = computed(() => unref(table?.getBindValues).isTreeTable);

      function getColumns() {
        const ret: Options[] = [];
        table.getColumns({ ignoreIndex: true, ignoreAction: true }).forEach((item) => {
          ret.push({
            label: (item.title as string) || (item.customTitle as string),
            value: (item.dataIndex_ || item.title) as string,
            ...item,
          });
        });
        return ret;
      }

      function init() {
        const values = unref(table?.getBindValues) || {};
        cacheCheckIndex.value = !!values.showIndexColumn;
        cacheCheckSelect.value = !!values.rowSelection;

        const columns = getColumns();

        const checkList = table
          .getColumns({ ignoreIndex: true, ignoreAction: true })
          .map((item) => {
            if (item.defaultHidden) {
              return '';
            }
            return item.dataIndex_ || item.title;
          })
          .filter(Boolean) as string[];

        if (!state.checkOptions.length) {
          state.checkOptions = columns;
          cacheCheckOptions.value = columns;
          cacheCheckList.value = checkList;
        } else {
          // const fixedColumns = columns.filter((item) =>
          //   Reflect.has(item, 'fixed')
          // ) as BasicColumn[];
          state.checkOptions.forEach((item: BasicColumn) => {
            const findItem = columns.find((col: BasicColumn) => col.dataIndex_ === item.dataIndex_);
            if (findItem) {
              item.fixed = findItem.fixed;
            }
          });
        }
        state.checkedList = checkList;
        state.isInit = true;
      }

      // checkAll change
      function onCheckAllChange(e: CheckboxChangeEvent) {
        const checkList = state.checkOptions.map((item) => item.value);
        if (e.target.checked) {
          state.checkedList = checkList;
          setColumns(checkList);
        } else {
          state.checkedList = [];
          setColumns([]);
        }
      }

      const indeterminate = computed(() => {
        const len = state.checkOptions.length;
        const checkedLen = state.checkedList.length;
        if (checkedLen == len) return undefined;
        return checkedLen >= 0 && checkedLen < len;
      });

      // Trigger when check/uncheck a column
      function onChange(checkedList: string[] | any) {
        const len = state.checkOptions.length;
        state.checkAll = checkedList.length === len && len > 0;
        const sortList = state.checkOptions.map((item) => item.value);
        checkedList.sort((prev, next) => {
          return sortList.indexOf(prev) - sortList.indexOf(next);
        });
        setColumns(checkedList);
      }

      // reset columns
      function reset() {
        state.checkAll = true;
        table.setProps({
          showIndexColumn: unref(cacheCheckIndex),
          rowSelection: unref(cacheCheckSelect) ? defaultRowSelection : undefined,
        });
        state.checkOptions = cloneDeep(unref(cacheCheckOptions));
        state.checkedList = cloneDeep(unref(cacheCheckList));
        setColumns(table.getCacheColumns());
        // sortable.sort(sortableOrder);
      }

      // let sortable: Sortable;
      // let sortableOrder: string[] = [];

      // Drag and drop sort
      function handleOpenChange() {
        if (isInitSortable) return;
        nextTick(() => {
          const columnListEl = unref(columnListRef);
          if (!columnListEl) return;
          const el = columnListEl.$el as any;
          if (!el) return;
          // sortable = Sortablejs.create(unref(el), {
          Sortablejs.create(unref(el), {
            animation: 500,
            delay: 400,
            delayOnTouchOnly: true,
            handle: '.table-column-drag-icon ',
            onEnd: (evt) => {
              const { oldIndex, newIndex } = evt;
              if (isNullAndUnDef(oldIndex) || isNullAndUnDef(newIndex) || oldIndex === newIndex) {
                return;
              }
              // Sort column
              const columns = cloneDeep(state.checkOptions);

              if (oldIndex > newIndex) {
                columns.splice(newIndex, 0, columns[oldIndex]);
                columns.splice(oldIndex + 1, 1);
              } else {
                columns.splice(newIndex + 1, 0, columns[oldIndex]);
                columns.splice(oldIndex, 1);
              }

              state.checkOptions = columns;

              setColumns(
                columns
                  .map((col: Options) => col.value)
                  .filter((value: string) => state.checkedList.includes(value)),
              );
            },
          });
          // 记录原始order 序列
          // sortableOrder = sortable.toArray();
          isInitSortable = true;
        });
      }

      // Control whether the serial number column is displayed
      function handleIndexCheckChange(e: CheckboxChangeEvent) {
        table.setProps({
          showIndexColumn: e.target.checked,
        });
      }

      // Control whether the check box is displayed
      function handleSelectCheckChange(e: CheckboxChangeEvent) {
        table.setProps({
          rowSelection: e.target.checked ? defaultRowSelection : undefined,
        });
      }

      function handleColumnFixed(item: BasicColumn, fixed?: 'left' | 'right') {
        if (!state.checkedList.includes(item.dataIndex_ as string)) return;

        const columns = getColumns() as BasicColumn[];
        const isFixed = item.fixed === fixed ? false : fixed;
        const index = columns.findIndex((col) => col.dataIndex_ === item.dataIndex_);
        if (index !== -1) {
          columns[index].fixed = isFixed;
        }
        item.fixed = isFixed;

        if (isFixed && !item.width) {
          item.width = 100;
        }
        // table.setCacheColumnsByField?.(item.dataIndex_ as string, { fixed: isFixed });
        setColumns(columns);
      }

      function setColumns(columns: BasicColumn[] | string[]) {
        table.setColumns(columns);

        const data: ColumnChangeParam[] = state.checkOptions.map((col) => {
          const open =
            columns.findIndex(
              (c: BasicColumn | string) =>
                c === col.value || (typeof c !== 'string' && c.dataIndex_ === col.value),
            ) !== -1;
          return { dataIndex_: col.value, fixed: col.fixed, open };
        });
        emit('columns-change', data);
      }

      function getPopupContainer() {
        return isFunction(attrs.getPopupContainer)
          ? attrs.getPopupContainer()
          : getParentContainer();
      }

      return {
        t,
        ...toRefs(state),
        isTreeTable,
        indeterminate,
        onCheckAllChange,
        onChange,
        reset,
        prefixCls,
        columnListRef,
        handleOpenChange,
        handleIndexCheckChange,
        handleSelectCheckChange,
        defaultRowSelection,
        handleColumnFixed,
        getPopupContainer,
      };
    },
  });
</script>
<style lang="less">
  @prefix-cls: ~'jeesite-basic-column-setting';

  .table-column-drag-icon {
    margin: 0 5px;
    cursor: move;
  }

  .@{prefix-cls} {
    &__popover-title {
      position: relative;
      display: flex;
      align-items: center;
      justify-content: space-between;
    }

    &__check-item {
      display: flex;
      align-items: center;
      min-width: 100%;
      padding: 4px 16px 8px 0;

      .ant-checkbox-wrapper {
        width: 100%;

        &:hover {
          color: @primary-color;
        }
      }
    }

    &__fixed-left,
    &__fixed-right {
      font-size: 16px;
      color: fade(@text-color-base, 75);
      cursor: pointer;

      &.active {
        color: @primary-color;
      }

      &:hover {
        transform: scale(1.1);
      }

      &.disabled {
        color: @disabled-color;
        cursor: not-allowed;
      }
    }

    &__cloumn-list {
      svg {
        width: 1em !important;
        height: 1em !important;
      }

      .ant-popover-inner-content {
        // max-height: 360px;
        padding-right: 0;
        padding-left: 0;
        // overflow: auto;
      }

      .ant-checkbox-group {
        min-width: 260px;
        // width: 100%;
        width: 300px;
        // flex-wrap: wrap;
      }

      .scrollbar {
        height: 220px;
      }
    }
  }
</style>
