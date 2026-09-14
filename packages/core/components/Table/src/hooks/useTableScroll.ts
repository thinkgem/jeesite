import type { BasicTableProps, TableRowSelection, BasicColumn } from '../types/table';
import { Ref, ComputedRef, ref } from 'vue';
import { computed, unref, nextTick, watch } from 'vue';
import { getRefElement, getViewportOffset } from '@jeesite/core/utils/domUtils';
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
  wrapRef: Ref<ElRef>,
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

    const tableEl = getRefElement(tableRef);
    if (!tableEl) return;

    const bodyEl = tableEl.querySelector('.ant-table-body') as HTMLElement;

    if (bodyEl) {
      tableScrollRef.value = bodyEl;
      bodyEl.scrollTop = tableScrollRefY.value;

      document.body.scrollTop = document.documentElement.scrollTop = 0;
      const hasScrollBarY = bodyEl.scrollHeight > bodyEl.clientHeight;
      const hasScrollBarX = bodyEl.scrollWidth > bodyEl.clientWidth;

      const tableClassList = tableEl.classList;
      if (hasScrollBarY) {
        tableClassList.contains('hide-scrollbar-y') && tableClassList.remove('hide-scrollbar-y');
      } else {
        !tableClassList.contains('hide-scrollbar-y') && tableClassList.add('hide-scrollbar-y');
      }

      if (hasScrollBarX) {
        tableClassList.contains('hide-scrollbar-x') && tableClassList.remove('hide-scrollbar-x');
      } else {
        !tableClassList.contains('hide-scrollbar-x') && tableClassList.add('hide-scrollbar-x');
      }

      bodyEl.style.height = 'unset';
    }

    // if (!unref(getCanResize) || !unref(tableData) || tableData.length === 0) return;
    if (!unref(getCanResize)) return;

    // Add a delay to get the correct bottomIncludeBody paginationHeight footerHeight headerHeight
    const headEl = tableEl.querySelector('.ant-table-thead') as HTMLElement;
    if (!headEl) return;

    // Table height from bottom height-custom offset
    let paddingHeight = 15;
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

    // Title height
    const wrapEl = getRefElement(unref(wrapRef));
    const titleHeight =
      (wrapEl?.querySelector('.jeesite-basic-table-header-container') as HTMLElement)?.offsetHeight ?? 0;

    // Table height
    let bottomIncludeBody = 0;
    let titleIncluded = false;
    if (wrapEl && isCanResizeParent) {
      const tablePadding = 12;
      const formMargin = 16;
      let paginationMargin = 10;
      const wrapHeight = wrapEl.offsetHeight;
      let formHeight = getRefElement(unref(formRef))?.offsetHeight ?? 0;
      if (formHeight) {
        formHeight += formMargin;
      }
      if (isBoolean(pagination) && !pagination) {
        paginationMargin = 0;
      }
      if (isBoolean(useSearchForm) && !useSearchForm) {
        paddingHeight = 0;
      }
      bottomIncludeBody = wrapHeight - formHeight - titleHeight - tablePadding - paginationMargin;
      titleIncluded = true;
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
    if (bodyEl) {
      bodyEl.style.height = `${height}px`;
    }

    // Set empty data height
    if (!tableData || tableData.length === 0) {
      const emptyDataEl = tableEl.querySelector('.ant-empty') as HTMLElement;
      if (!emptyDataEl) return;

      const contentEl = tableEl.querySelector('.ant-table-content') as HTMLElement;
      const hasScrollBarX = contentEl ? contentEl.scrollWidth > contentEl.clientWidth : false;
      const emptyOffset = (titleIncluded ? 0 : titleHeight) + (hasScrollBarX ? 10 : 0) + 50;
      emptyDataEl.style.height = `${height - emptyOffset}px`;

      const td = emptyDataEl.parentElement as HTMLElement;
      if (!td.classList.contains('ant-table-expanded-row-fixed')) {
        const fixedEl = document.createElement('div');
        fixedEl.classList.add('ant-table-expanded-row-fixed');
        fixedEl.style.width = `${tableEl.clientWidth - 16}px`;
        fixedEl.style.position = 'sticky';
        fixedEl.style.left = '0';
        fixedEl.style.overflow = 'hidden';
        td.replaceChild(fixedEl, emptyDataEl);
        fixedEl.appendChild(emptyDataEl);
      }

      // 避免新版antdv-next切换页签后空数据的表格高度未自适应问题
      setTimeout(() => {
        const emptyDataEl = tableEl.querySelector('.ant-empty') as HTMLElement;
        if (!emptyDataEl) return;

        emptyDataEl.style.height = `${height - emptyOffset}px`;
      });
    }
  }

  function redoHeight() {
    nextTick(() => {
      calcTableHeight();
    }).then();
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
    const tableEl = getRefElement(tableRef);
    tableWidthRef.value = tableEl ? tableEl.offsetWidth - 50 || 600 : 600; // 默认宽度不小于，列中指定的宽度总合
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
    const tableData = unref(getDataSourceRef);
    return {
      x: canScrollX ? (canResize ? tableWidth : undefined) : tableWidth,
      y: canResize && tableData && tableData.length > 0 ? unref(tableHeightRef) : undefined,
      scrollToFirstRowOnChange: true,
      ...scroll,
    };
  });

  return { getScrollRef, redoHeight };
}
