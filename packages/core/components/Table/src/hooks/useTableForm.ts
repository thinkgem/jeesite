import type { ComputedRef, Slots } from 'vue';
import type { BasicTableProps, FetchParams } from '../types/table';
import { unref, computed } from 'vue';
import type { FormProps } from '@jeesite/core/components/Form';
import { isFunction } from '@jeesite/core/utils/is';

export function useTableForm(
  propsRef: ComputedRef<BasicTableProps>,
  slots: Slots,
  reload: (opt?: FetchParams | undefined) => Promise<void>,
  getLoading: ComputedRef<boolean | undefined>,
) {
  const getFormProps = computed((): Partial<FormProps | any> => {
    const { formConfig } = unref(propsRef);
    const { submitButtonOptions } = formConfig || {};
    return {
      colon: false,
      compact: true,
      enctype: 'form-data',
      showActionButtonGroup: true,
      showAdvancedButton: true,
      submitButtonOptions: { loading: unref(getLoading), ...submitButtonOptions },
      ...formConfig,
    };
  });

  const getFormSlotKeys: ComputedRef<string[]> = computed(() => {
    const keys = Object.keys(slots);
    return keys
      .map((item) => (item.startsWith('form-') ? item : null))
      .filter((item) => !!item) as string[];
  });

  function replaceFormSlotKey(key: string) {
    if (!key) return '';
    return key?.replace?.(/form-/, '') ?? '';
  }

  function handleSearchInfoChange(info: Recordable) {
    const { handleSearchInfoFn } = unref(propsRef);
    if (handleSearchInfoFn && isFunction(handleSearchInfoFn)) {
      info = handleSearchInfoFn(info) || info;
    }
    reload({ searchInfo: info, page: 1 });
  }

  return {
    getFormProps,
    replaceFormSlotKey,
    getFormSlotKeys,
    handleSearchInfoChange,
  };
}
