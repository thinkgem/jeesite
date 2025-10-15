import type { BasicTableProps, TableRowSelection, BasicColumn } from '../types/table';
import { Ref, ComputedRef, ref } from 'vue';
import { computed, unref, nextTick, watch } from 'vue';
import { getViewportOffset } from '@jeesite/core/utils/domUtils';
import { isBoolean } from '@jeesite/core/utils/is';
import { useWindowSizeFn } from '@jeesite/core/hooks/event/useWindowSizeFn';
import { useModalContext } from '@jeesite/core/components/Modal';
import { onMountedOrActivated } from '@jeesite/core/hooks/core/onMountedOrActivated';
import { useDebounceFn, useResizeObserver } from '@vueuse/core';
import { useScroll } from '@jeesite/core/hooks/event/useScroll';

export function useTableScroll(
  propsRef: ComputedRef<BasicTableProps>,
  tableRef: Ref<ComponentRef>,
  columnsRef: Ref<BasicColumn[]>,
  rowSelectionRef: ComputedRef<TableRowSelection | null>,
  getDataSourceRef: Ref<Recordable[]>,
  wrapRef: Ref<ComponentRef>,
  formRef: Ref<ComponentRef>,
) {
  const tableHeightRef = ref<number | string | undefined>(167);
  const modalFn = useModalContext();

  const getCanResize = computed(() => {
    const { canResize, scroll } = unref(propsRef);
    return canResize && !(scroll || {}).y;
  });

  const tableScrollRef = ref();
  const { refY: tableScrollRefY } = useScroll(tableScrollRef);

  function calcTableHeight() {
    const { resizeHeightOffset, pagination, maxHeight, minHeight, isCanResizeParent, useSearchForm } = unref(propsRef);
    const tableData = unref(getDataSourceRef);

    const table = unref(tableRef);
    if (!table) return;

    const tableEl: HTMLElement = table.$el;
    if (!tableEl) return;

    const bodyEl = tableEl.querySelector('.ant-table-body') as HTMLElement;
    if (!bodyEl) return;

    tableScrollRef.value = bodyEl;
    bodyEl.scrollTop = tableScrollRefY.value;

    document.body.scrollTop = document.documentElement.scrollTop = 0;
    const hasScrollBarY = bodyEl.scrollHeight > bodyEl.clientHeight;
    const hasScrollBarX = bodyEl.scrollWidth > bodyEl.clientWidth;

    if (hasScrollBarY) {
      tableEl.classList.contains('hide-scrollbar-y') && tableEl.classList.remove('hide-scrollbar-y');
    } else {
      !tableEl.classList.contains('hide-scrollbar-y') && tableEl.classList.add('hide-scrollbar-y');
    }

    if (hasScrollBarX) {
      tableEl.classList.contains('hide-scrollbar-x') && tableEl.classList.remove('hide-scrollbar-x');
    } else {
      !tableEl.classList.contains('hide-scrollbar-x') && tableEl.classList.add('hide-scrollbar-x');
    }

    bodyEl!.style.height = 'unset';

    // if (!unref(getCanResize) || !unref(tableData) || tableData.length === 0) return;
    if (!unref(getCanResize) || !unref(tableData)) return;

    // Add a delay to get the correct bottomIncludeBody paginationHeight footerHeight headerHeight
    const headEl = tableEl.querySelector('.ant-table-thead') as HTMLElement;

    if (!headEl) return;

    // Table height from bottom height-custom offset
    let paddingHeight = 17;
    if (tableEl.closest('.jeesite-layout-content')) {
      paddingHeight += 11;
    }

    // Pagination height
    let paginationHeight = 2;
    const paginationEl = tableEl.querySelector('.ant-pagination') as HTMLElement;
    if (paginationEl) {
      paginationEl.style.display = 'flex';
      paginationHeight += paginationEl.offsetHeight || 0;
    } else {
      paginationHeight = -8;
    }

    // Footer height
    let footerHeight = 0;
    if (!isBoolean(pagination)) {
      const footerEl = tableEl.querySelector('.ant-table-footer') as HTMLElement;
      if (footerEl) {
        footerHeight += footerEl.offsetHeight || 0;
      }
    }

    // Summary height
    const summaryEl = tableEl.querySelector('.ant-table-summary') as HTMLElement;
    if (summaryEl) {
      footerHeight += summaryEl.offsetHeight || 0;
    }

    // Header height
    let headerHeight = 0;
    if (headEl) {
      headerHeight = (headEl as HTMLElement).offsetHeight;
    }

    // Table height
    let bottomIncludeBody = 0;
    if (unref(wrapRef) && isCanResizeParent) {
      const tablePadding = 12;
      const formMargin = 16;
      let paginationMargin = 10;
      const wrapHeight = unref(wrapRef)?.offsetHeight ?? 0;
      let formHeight = unref(formRef)?.$el.offsetHeight ?? 0;
      if (formHeight) {
        formHeight += formMargin;
      }
      if (isBoolean(pagination) && !pagination) {
        paginationMargin = 0;
      }
      if (isBoolean(useSearchForm) && !useSearchForm) {
        paddingHeight = 0;
      }
      const headerCellHeight = (tableEl.querySelector('.ant-table-title') as HTMLElement)?.offsetHeight ?? 0;
      bottomIncludeBody = wrapHeight - formHeight - headerCellHeight - tablePadding - paginationMargin;
    } else {
      bottomIncludeBody = getViewportOffset(headEl).bottomIncludeBody;
    }
    let height =
      bottomIncludeBody - (resizeHeightOffset || 0) - paddingHeight - paginationHeight - footerHeight - headerHeight;
    if (minHeight && height < minHeight) {
      height = minHeight;
    }
    height = (height > maxHeight! ? (maxHeight as number) : height) ?? height;

    // Set table height
    tableHeightRef.value = height;
    // Solve the problem of modal adaptive height calculation when the form is placed in the modal
    modalFn?.redoModalHeight?.();

    // Set body height
    bodyEl!.style.height = `${height}px`;

    // Set empty data height
    if (tableData.length === 0) {
      const emptyDataEl = tableEl.querySelector('.ant-table-expanded-row-fixed') as HTMLElement;
      if (emptyDataEl && emptyDataEl.style) {
        emptyDataEl.style.height = `${height - 9}px`;
      }
    }
  }

  function redoHeight() {
    nextTick(() => {
      calcTableHeight();
    });
  }

  watch(
    () => [unref(getCanResize), unref(getDataSourceRef)?.length],
    () => {
      calcTableHeight();
    },
    { flush: 'post' },
  );

  const tableWidthRef = ref();

  function calcTableWidth() {
    const table = unref(tableRef);
    tableWidthRef.value = table?.$el?.offsetWidth - 50 || 600; // 默认宽度不小于，列中指定的宽度总合
  }

  useResizeObserver(wrapRef, useDebounceFn(calcTableWidth, 100));

  onMountedOrActivated(() => {
    calcTableHeight();
    calcTableWidth();
  });

  useWindowSizeFn(() => {
    calcTableHeight();
    calcTableWidth();
  }, 200);

  const getScrollRef: ComputedRef<any> = computed(() => {
    let width = 0;
    // if (unref(rowSelectionRef)) {
    //   width += 60;
    // }

    const columns = unref(columnsRef).filter((item) => !item.defaultHidden);
    // let unsetWidthColumnSize = 0;
    columns.forEach((item) => {
      if (item.width) width += Number.parseFloat(item.width as string);
      // else unsetWidthColumnSize += 1;
    });

    // if (unsetWidthColumnSize !== 0) {
    //   width += unsetWidthColumnSize * 50;
    // }

    const tableWidth = tableWidthRef.value;
    const { canResize, scroll } = unref(propsRef);
    const canScrollX = tableWidth <= 0 || width <= 0 || tableWidth > width;
    return {
      x: canScrollX ? (canResize ? tableWidth : undefined) : tableWidth,
      y: canResize ? unref(tableHeightRef) : undefined,
      scrollToFirstRowOnChange: true,
      ...scroll,
    };
  });

  return { getScrollRef, redoHeight };
}
