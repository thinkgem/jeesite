import type { Ref } from 'vue';
import { nextTick, unref } from 'vue';
import { warn } from '@jeesite/core/utils/log';

export function useTableScrollTo(tableRef: Ref<ComponentRef>, getDataSourceRef: Ref<Recordable[]>) {
  let bodyEl: HTMLElement | null;

  async function findTargetRowToScroll(targetRowData: Recordable) {
    const { id } = targetRowData;
    const targetRowEl: HTMLElement | null | undefined = bodyEl?.querySelector(`[data-row-key="${id}"]`);
    //Add a delay to get new dataSource
    await nextTick();
    bodyEl?.scrollTo({
      top: targetRowEl?.offsetTop ?? 0,
      behavior: 'smooth',
    });
  }

  function scrollTo(pos: string): void {
    const table = unref(tableRef);
    if (!table) return;

    const tableEl: Element = table.$el;
    if (!tableEl) return;

    if (!bodyEl) {
      bodyEl = tableEl.querySelector('.ant-table-body');
      if (!bodyEl) return;
    }

    const dataSource = unref(getDataSourceRef);
    if (!dataSource) return;

    // judge pos type
    if (pos === 'top') {
      findTargetRowToScroll(dataSource[0]);
    } else if (pos === 'bottom') {
      findTargetRowToScroll(dataSource[dataSource.length - 1]);
    } else {
      const targetRowData = dataSource.find((data) => data.id === pos);
      if (targetRowData) {
        findTargetRowToScroll(targetRowData);
      } else {
        warn(`id: ${pos} doesn't exist`);
      }
    }
  }

  return { scrollTo };
}
