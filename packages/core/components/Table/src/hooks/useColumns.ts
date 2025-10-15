import type { BasicColumn, BasicTableProps, CellFormat, GetColumnsParams } from '../types/table';
import type { PaginationProps } from '../types/pagination';
import { ComputedRef, h } from 'vue';
import { Ref, ref, reactive, toRaw, unref, watch } from 'vue';
import { renderEditCell } from '../components/editable';
import { usePermission } from '@jeesite/core/hooks/web/usePermission';
import { useI18n } from '@jeesite/core/hooks/web/useI18n';
import { useDict } from '@jeesite/core/components/Dict';
import { isObject, isArray, isBoolean, isFunction, isMap, isString } from '@jeesite/core/utils/is';
import { deepMerge } from '@jeesite/core/utils';
import { error } from '@jeesite/core/utils/log';
import { cloneDeep, isEqual, uniqBy } from 'lodash-es';
import { formatToDate } from '@jeesite/core/utils/dateUtil';
import { ACTION_COLUMN_FLAG, DEFAULT_ALIGN, INDEX_COLUMN_FLAG, DRAG_COLUMN_FLAG, PAGE_SIZE } from '../const';
import { Icon } from '@jeesite/core/components/Icon';
import { useDebounceFn } from '@vueuse/core';

function handleItem(item: BasicColumn, ellipsis: boolean, dictTypes: Set<string>) {
  const { key, dataIndex, children } = item;
  item.align = item.align || DEFAULT_ALIGN;
  // 未设置宽度的列，不进行拖拽调整列宽
  if (item.width) {
    item.resizable = item.resizable || true;
  }
  if (ellipsis) {
    if (!key) {
      item.key = dataIndex as any;
    }
    if (!isBoolean(item.ellipsis)) {
      Object.assign(item, {
        ellipsis,
      });
    }
  }
  if (dataIndex && isString(dataIndex) && dataIndex.indexOf('.') != -1) {
    item.dataIndex = dataIndex.split('.');
    item.dataIndex_ = dataIndex;
  } else if (isArray(dataIndex)) {
    item.dataIndex_ = dataIndex.join('.');
  } else {
    item.dataIndex_ = dataIndex?.toString() || '';
  }
  if (children && children.length) {
    handleChildren(children, !!ellipsis, dictTypes);
  }
  if (item.filterDictType) {
    const { getDictList } = useDict();
    dictTypes.add(item.filterDictType);
    const filterList = getDictList(item.filterDictType);
    item.filters = filterList.map((item) => {
      return { text: item.name, value: item.value };
    });
  }
  if (item.dictType) {
    dictTypes.add(item.dictType);
    // if (!item.slots?.customRender) {
    //   if (!item.slots) {
    //     item.slots = {};
    //   }
    //   item.slots.customRender = 'dictLabelColumn';
    // }
    if (!item.slot) {
      item.slot = 'dictLabelColumn';
    }
  }
}

function handleChildren(children: BasicColumn[] | undefined, ellipsis: boolean, dictTypes: Set<string>) {
  if (!children) return;
  children.forEach((item) => {
    const { children } = item;
    handleItem(item, ellipsis, dictTypes);
    handleChildren(children, ellipsis, dictTypes);
  });
}

function handleIndexColumn(
  propsRef: ComputedRef<BasicTableProps>,
  getPaginationRef: ComputedRef<boolean | PaginationProps>,
  columns: BasicColumn[],
) {
  const { t } = useI18n();

  const { showIndexColumn, indexColumnProps, isTreeTable, canRowDrag } = unref(propsRef);

  if (unref(isTreeTable)) {
    return;
  }

  let pushIndexColumns = showIndexColumn;

  columns.forEach(() => {
    const indIndex = columns.findIndex((column) => column.flag === INDEX_COLUMN_FLAG);
    if (showIndexColumn) {
      pushIndexColumns = indIndex === -1;
    } else if (!showIndexColumn && indIndex !== -1) {
      columns.splice(indIndex, 1);
    }
  });

  if (!pushIndexColumns) return;

  // const isFixedLeft = columns.some((item) => item.fixed === 'left');

  if (canRowDrag) {
    columns.unshift({
      flag: DRAG_COLUMN_FLAG,
      title: '',
      width: 40,
      align: 'center',
      fixed: 'left',
      customRender: () => {
        return h(Icon, {
          icon: 'i-ant-design:drag-outlined',
          class: 'cursor-move',
          onMouseenter: (event: any) => {
            event.target.closest('tr').draggable = true;
          },
          onMouseleave: (event: any) => {
            event.target.closest('tr').draggable = false;
          },
        });
      },
      ...indexColumnProps,
    });
  }

  columns.unshift({
    flag: INDEX_COLUMN_FLAG,
    title: t('component.table.index'),
    width: 50,
    align: 'center',
    fixed: 'left',
    customRender: ({ index }) => {
      const getPagination = unref(getPaginationRef);
      if (isBoolean(getPagination)) {
        return `${index + 1}`;
      }
      const { current = 1, pageSize = PAGE_SIZE } = getPagination;
      return ((current < 1 ? 1 : current) - 1) * pageSize + index + 1;
    },
    ...indexColumnProps,
  });
}

function handleActionColumn(propsRef: ComputedRef<BasicTableProps>, columns: BasicColumn[]) {
  const { actionColumn } = unref(propsRef);
  if (!actionColumn) return;

  const { t } = useI18n();
  const hasIndex = columns.findIndex((column) => column.flag === ACTION_COLUMN_FLAG);
  if (hasIndex === -1) {
    columns.push({
      ...columns[hasIndex],
      dataIndex: 'actions',
      title: t('操作'),
      fixed: 'right',
      slot: 'tableActions',
      align: 'left',
      ...actionColumn,
      flag: ACTION_COLUMN_FLAG,
    });
  }
}

export function useColumns(
  propsRef: ComputedRef<BasicTableProps>,
  getPaginationRef: ComputedRef<boolean | PaginationProps>,
) {
  const { hasPermission } = usePermission();

  let cacheColumns: BasicColumn[] = unref(propsRef).columns;
  let currentColumns: BasicColumn[] = cacheColumns;

  const getColumnsRef: Ref<BasicColumn[]> = ref([]);
  const getViewColumns: Ref<BasicColumn[]> = ref([]);

  const dictTypesRef: Ref<Set<string>> = ref([] as unknown as Set<string>);
  const updateColumnsDebounceFn = useDebounceFn(updateColumnsRef, 100);

  watch(
    () => unref(propsRef),
    async () => {
      await updateColumnsDebounceFn();
    },
  );

  function updateColumnsRef() {
    // const columns = cloneDeep(unref(columnsRef)); // 暂且注释，克隆会导致拖拽失效
    // const columns = unref(columnsRef);
    const columns = currentColumns;

    handleIndexColumn(propsRef, getPaginationRef, columns);
    handleActionColumn(propsRef, columns);

    if (!columns) {
      getColumnsRef.value = [];
      updateViewColumns();
      return;
    }

    const { ellipsis } = unref(propsRef);

    dictTypesRef.value = new Set<string>();
    columns.forEach((item) => {
      // const { customRender, slots } = item;
      handleItem(
        item,
        // Reflect.has(item, 'ellipsis') ? !!item.ellipsis : !!ellipsis && !customRender && !slots,
        Reflect.has(item, 'ellipsis') ? !!item.ellipsis : !!ellipsis && item.dataIndex !== 'actions', // 自定义渲染列应和非自定义的省略条件一样
        dictTypesRef.value,
      );
    });
    // console.log('column', columns);
    getColumnsRef.value = columns;
    updateViewColumns();
  }

  function updateViewColumns() {
    const viewColumns = sortFixedColumn(unref(getColumnsRef));
    const columns = cloneDeep(viewColumns);
    function buildColumns(columns: BasicColumn[]) {
      return columns
        .filter((column) => {
          return hasPermission(column.auth) && isIfShow(column);
        })
        .map((column) => {
          if (column.children) {
            buildColumns(column.children);
          }
          // const { slots, dataIndex, customRender, format, edit, editRow, flag } = column;
          const { customRender, format, edit, editRow, flag } = column;

          //if (!slots || !slots?.title) {
          //  //column.slots = { title: `header-${dataIndex}`, ...(slots || {}) };
          // column.customTitle = column.title as any;
          // Reflect.deleteProperty(column, 'title');
          //}
          const isDefaultAction = [INDEX_COLUMN_FLAG, ACTION_COLUMN_FLAG].includes(flag!);
          if (!customRender && format && !edit && !isDefaultAction) {
            column.customRender = ({ text, record, index }) => {
              return formatCell(text, format, record, index, column);
            };
          }

          // edit table
          if ((edit || editRow) && !isDefaultAction) {
            column.customRender = renderEditCell(column);
          }
          return reactive(column);
        });
    }
    getViewColumns.value = buildColumns(columns) as unknown as BasicColumn[];
  }

  function isIfShow(column: BasicColumn): boolean {
    const ifShow = column.ifShow;

    let isIfShow = true;

    if (isBoolean(ifShow)) {
      isIfShow = ifShow;
    }
    if (isFunction(ifShow)) {
      isIfShow = ifShow(column);
    }
    return isIfShow;
  }

  watch(
    () => unref(propsRef).columns,
    (columns) => {
      currentColumns = columns;
      updateColumnsRef();
      cacheColumns = columns?.filter((item) => !item.flag) ?? [];
    },
  );

  // function setCacheColumnsByField(dataIndex: string | undefined, value: Partial<BasicColumn>) {
  //   if (!dataIndex || !value) {
  //     return;
  //   }
  //   cacheColumns.forEach((item) => {
  //     if (item.dataIndex_ === dataIndex) {
  //       Object.assign(item, value);
  //       return;
  //     }
  //   });
  // }

  /**
   * set columns
   * @param columnList key｜column
   */
  function setColumns(columnList: Partial<BasicColumn>[] | (string | string[])[]) {
    const columns = cloneDeep(columnList);
    if (!isArray(columns)) return;

    if (columns.length <= 0) {
      currentColumns = [];
      updateColumnsRef();
      return;
    }

    const firstColumn = columns[0];
    const cacheKeys = cacheColumns.map((item) => item.dataIndex_);

    if (!isString(firstColumn) && !isArray(firstColumn)) {
      currentColumns = columns as BasicColumn[];
    } else {
      const columnKeys = (columns as (string | string[])[]).map((m) => m.toString());
      const newColumns: BasicColumn[] = [];
      cacheColumns.forEach((item) => {
        const column = {
          ...item,
          defaultHidden: !columnKeys.includes(item.dataIndex_ || (item.key as string)),
        };
        currentColumns.forEach((item) => {
          if (column.dataIndex_ == item.dataIndex_ && item.fixed) {
            column.fixed = item.fixed;
            return;
          }
        });
        newColumns.push(column);
      });
      // Sort according to another array
      if (!isEqual(cacheKeys, columns)) {
        newColumns.sort((prev, next) => {
          return columnKeys.indexOf(prev.dataIndex_ as string) - columnKeys.indexOf(next.dataIndex_ as string);
        });
      }
      currentColumns = newColumns;
    }
    updateColumnsRef();
  }

  function updateColumn(data: Partial<BasicColumn> | Partial<BasicColumn>[]) {
    let updateData: Partial<BasicColumn>[] = [];
    if (isObject(data)) {
      updateData.push(data as BasicColumn);
    }
    if (isArray(data)) {
      updateData = [...data];
    }
    const hasDataIndex = updateData.every((item) => Reflect.has(item, 'dataIndex') && item.dataIndex);
    if (!hasDataIndex) {
      error('必须包含 dataIndex 字段。');
      return;
    }

    const column: BasicColumn[] = [];
    updateData.forEach((item) => {
      currentColumns.forEach((val) => {
        if (val.dataIndex_ === item.dataIndex) {
          const newColumn = deepMerge(val, item);
          column.push(newColumn as BasicColumn);
        } else {
          column.push(val);
        }
      });
    });
    currentColumns = uniqBy(column, 'dataIndex');
    updateColumnsRef();
  }

  function getColumns(opt?: GetColumnsParams) {
    const { ignoreIndex, ignoreAction, sort } = opt || {};
    let columns = toRaw(unref(getColumnsRef));
    if (ignoreIndex) {
      columns = columns.filter((item) => item.flag !== INDEX_COLUMN_FLAG);
    }
    if (ignoreAction) {
      columns = columns.filter((item) => item.flag !== ACTION_COLUMN_FLAG);
    }

    if (sort) {
      columns = sortFixedColumn(columns);
    }

    return columns;
  }
  function getCacheColumns() {
    return cacheColumns;
  }

  return {
    getColumnsRef,
    getCacheColumns,
    getColumns,
    setColumns,
    updateColumn,
    getViewColumns,
    // setCacheColumnsByField,
    dictTypesRef,
  };
}

function sortFixedColumn(columns: BasicColumn[]) {
  const fixedLeftColumns: BasicColumn[] = [];
  const fixedRightColumns: BasicColumn[] = [];
  const defColumns: BasicColumn[] = [];
  for (const column of columns) {
    if (column.fixed === 'left') {
      fixedLeftColumns.push(column);
      continue;
    }
    if (column.fixed === 'right') {
      fixedRightColumns.push(column);
      continue;
    }
    defColumns.push(column);
  }
  return [...fixedLeftColumns, ...defColumns, ...fixedRightColumns].filter((item) => !item.defaultHidden);
}

// format cell
export function formatCell(text: string, format: CellFormat, record: Recordable, index: number, column: BasicColumn) {
  if (!format) {
    return text;
  }

  // custom function
  if (isFunction(format)) {
    return format(text, record, index, column);
  }

  try {
    // date type
    const DATE_FORMAT_PREFIX = 'date|';
    if (isString(format) && format.startsWith(DATE_FORMAT_PREFIX) && text) {
      const dateFormat = format.replace(DATE_FORMAT_PREFIX, '');

      if (!dateFormat) {
        return text;
      }
      return formatToDate(text, dateFormat);
    }

    // Map
    if (isMap(format)) {
      return format.get(text);
    }
  } catch (error) {
    return text;
  }
}
