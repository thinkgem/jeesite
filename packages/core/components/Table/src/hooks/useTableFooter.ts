import type { ComputedRef, Ref } from 'vue';
import type { BasicTableProps } from '../types/table';
import { unref, computed, h, nextTick, watchEffect } from 'vue';
import TableFooter from '../components/TableFooter.vue';
import { useEventListener } from '@jeesite/core/hooks/event/useEventListener';

export function useTableFooter(
  propsRef: ComputedRef<BasicTableProps>,
  scrollRef: ComputedRef<any>,
  tableRef: Ref<ComponentRef>,
  getDataSourceRef: Ref<Recordable>,
) {
  const getIsEmptyData = computed(() => {
    return (unref(getDataSourceRef) || []).length === 0;
  });

  const getFooterProps = computed((): Recordable | undefined => {
    const { summaryFunc, showSummary, summaryData } = unref(propsRef);
    return showSummary && !unref(getIsEmptyData)
      ? () => h(TableFooter, { summaryFunc, summaryData, scroll: unref(scrollRef) })
      : undefined;
  });

  watchEffect(() => {
    handleSummary();
  });

  function handleSummary() {
    const { showSummary } = unref(propsRef);
    if (!showSummary || unref(getIsEmptyData)) return;

    nextTick(() => {
      const tableEl = unref(tableRef);
      if (!tableEl) return;
      const bodyDom = tableEl.$el.querySelector('.ant-table-content');
      useEventListener({
        el: bodyDom,
        name: 'scroll',
        listener: () => {
          const footerBodyDom = tableEl.$el.querySelector('.ant-table-footer .ant-table-content') as HTMLDivElement;
          if (!footerBodyDom || !bodyDom) return;
          footerBodyDom.scrollLeft = bodyDom.scrollLeft;
        },
        wait: 0,
        options: true,
      });
    });
  }
  return { getFooterProps };
}
