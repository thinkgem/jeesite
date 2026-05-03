import type { Ref } from 'vue';
import type { BasicTableProps, TableActionType } from '../types/table';
import { provide, inject, ComputedRef } from 'vue';

const key = Symbol('basic-table');

type Instance = TableActionType & {
  wrapRef: Ref<Nullable<HTMLElement>>;
  getProps: ComputedRef<Recordable>;
  getBindValues: ComputedRef<Recordable>;
};

export type TableInstance = Omit<Instance, 'getProps' | 'getBindValues'> & {
  getProps: ComputedRef<BasicTableProps>;
  getBindValues: ComputedRef<Recordable>;
};

export function createTableContext(instance: Instance) {
  provide(key, instance);
}

export function useTableContext(): TableInstance {
  return inject(key) as TableInstance;
}
