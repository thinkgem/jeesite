import { ComputedRef, Ref } from 'vue';
import type { BasicTableProps } from '../types/table';
import { unref } from 'vue';
import { clone } from 'lodash-es';
import { ROW_KEY } from '../const';
import { isString, isFunction } from '@jeesite/core/utils/is';

interface Options {
  setSelectedRowKeys: (keys: string[] | number[]) => void;
  getSelectRowKeys: () => string[] | number[];
  clearSelectedRowKeys: () => void;
  getAutoCreateKey: ComputedRef<boolean | undefined>;
  getDataSourceRef: Ref<Recordable<any>[]>;
  tableRef: Ref<ComponentRef>;
  emit: EmitType;
}

function getKey(
  record: Recordable,
  rowKey: string | ((record: Recordable, defaultValue?: any) => string) | undefined,
  autoCreateKey?: boolean,
) {
  if (!rowKey || autoCreateKey) {
    return record[ROW_KEY];
  }
  if (isString(rowKey)) {
    return record[rowKey];
  }
  if (isFunction(rowKey)) {
    return record[rowKey(record)];
  }
  return null;
}

export function useCustomRow(
  propsRef: ComputedRef<BasicTableProps>,
  {
    setSelectedRowKeys,
    getSelectRowKeys,
    getAutoCreateKey,
    clearSelectedRowKeys,
    getDataSourceRef,
    tableRef,
    emit,
  }: Options,
) {
  function handleRowClick(record, _index, event) {
    const { rowSelection, rowKey, clickToRowSelect } = unref(propsRef);
    if (!rowSelection || !clickToRowSelect) return;
    const keys = clone(getSelectRowKeys());
    const key = getKey(record, rowKey, unref(getAutoCreateKey));
    if (!key) return;
    if (rowSelection.type === 'checkbox') {
      // 找到tr
      const tr: HTMLElement = (event as MouseEvent)
        .composedPath?.()
        .find((dom) => (dom as HTMLElement).tagName === 'TR') as HTMLElement;
      if (!tr) return;
      // 找到Checkbox，检查是否为disabled
      const checkBox = tr.querySelector('input[type=checkbox]');
      if (!checkBox || checkBox.hasAttribute('disabled')) return;
      // toggle checkbox
      if (!keys.includes(key as never)) {
        keys.push(key as never);
      } else {
        const keyIndex = keys.findIndex((item) => item === key);
        keys.splice(keyIndex, 1);
      }
      setSelectedRowKeys(keys);
      return;
    } else if (rowSelection.type === 'radio') {
      if (!keys.includes(key as never)) {
        if (keys.length) {
          clearSelectedRowKeys();
        }
        setSelectedRowKeys([key]);
        return;
      }
      //clearSelectedRowKeys(); // 双击不进行清空选择
    }
  }

  const customRow = (record: Recordable, index: number) => {
    const { canRowDrag } = unref(propsRef);
    return {
      onClick: (event: Event) => {
        event?.stopPropagation();
        handleRowClick(record, index, event);
        emit('row-click', record, index, event);
      },
      onDblclick: (event: Event) => {
        emit('row-db-click', record, index, event);
      },
      onContextmenu: (event: Event) => {
        emit('row-contextmenu', record, index, event);
      },
      onMouseenter: (event: any) => {
        emit('row-mouseenter', record, index, event);
      },
      onMouseleave: (event: any) => {
        emit('row-mouseleave', record, index, event);
      },
      onDragstart: (event: any) => {
        emit('row-dragstart', record, index, event);
      },
      onDragover: (event: any) => {
        emit('row-dragover', record, index, event);
      },
      onDrop: (event: any) => {
        emit('row-drop', record, index, event);
      },
    };
  };

  return {
    customRow,
  };
}
