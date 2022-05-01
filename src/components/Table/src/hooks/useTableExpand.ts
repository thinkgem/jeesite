/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 * No deletion without permission, or be held responsible to law.
 * @author Vben、ThinkGem
 */
import type { ComputedRef, Ref } from 'vue';
import type { BasicTableProps } from '../types/table';
import { computed, unref, ref } from 'vue';
import { ROW_KEY } from '../const';
import { isFunction } from '/@/utils/is';

export function useTableExpand(
  propsRef: ComputedRef<BasicTableProps>,
  tableData: Ref<Recordable[]>,
  getFormData: Fn,
  emit: EmitType,
  setLoading: Fn,
) {
  const expandedRowKeys = ref<string[]>([]);
  const currentLevel = ref(0);
  const treeLevel = ref(0);

  const getAutoCreateKey = computed(() => {
    return unref(propsRef).autoCreateKey && !unref(propsRef).rowKey;
  });

  const getRowKey = computed(() => {
    const { rowKey } = unref(propsRef);
    return unref(getAutoCreateKey) ? ROW_KEY : rowKey;
  });

  const getExpandOption = computed(() => {
    const { isTreeTable } = unref(propsRef);
    if (!isTreeTable) return {};

    return {
      expandedRowKeys: unref(expandedRowKeys),
      onExpandedRowsChange: (keys: string[]) => {
        expandedRowKeys.value = keys;
        emit('expanded-rows-change', keys);
      },
    };
  });

  async function expandAll() {
    setLoading(true);
    treeLevel.value = 0;
    await expandOneLevel();
    currentLevel.value += 1;
    setLoading(false);
  }

  async function expandOneLevel(data?: Recordable[]) {
    const rowKey = unref(getRowKey) as string;
    const { childrenColumnName } = unref(propsRef);
    const status = getFormData()?.status;
    for (const item of data || unref(tableData)) {
      await expandCollapse(item, true, status === ''); // 加载并展开行的数据
      expandedRowKeys.value.push(item[rowKey]);
      if (treeLevel.value < currentLevel.value) {
        const children = item[childrenColumnName || 'children'];
        if (children?.length) {
          await expandOneLevel(children);
        }
      }
    }
    treeLevel.value += 1;
  }

  function collapseAll() {
    expandedRowKeys.value = [];
    currentLevel.value = 0;
  }

  async function expandCollapse(record: Recordable, onlyLoadData = false, forceLoad = false) {
    if ((record.treeLeaf === '0' && !record.childList) || forceLoad) {
      record.isLoading = true;
      const { api, pagination } = unref(propsRef);
      if (!api || !isFunction(api)) return;
      const rowKey = unref(getRowKey) as string;
      let list = await api({
        parentCode: record[rowKey] as string,
        status: forceLoad ? '' : getFormData()?.status,
      });
      if (pagination && list.list) {
        list = list.list;
      }
      if (list && list.length > 0) {
        record.childList = list;
        record.treeLeaf = '0';
      } else {
        record.childList = [];
        record.treeLeaf = '1';
      }
      record.isLoading = false;
    }
    if (!onlyLoadData) {
      let expanded = true;
      const rowKey = unref(getRowKey) as string;
      for (const index in expandedRowKeys.value) {
        if (record[rowKey] == expandedRowKeys.value[index]) {
          expandedRowKeys.value.splice(index as unknown as number, 1);
          expanded = false;
          break;
        }
      }
      if (expanded) {
        expandedRowKeys.value.push(record[unref(getRowKey) as string]);
      }
      return expanded;
    }
  }

  return { getExpandOption, expandAll, collapseAll, expandCollapse };
}
