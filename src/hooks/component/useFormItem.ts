/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 * No deletion without permission, or be held responsible to law.
 * @description 超强兼容表单字段各种数据类型的支持
 * @author Vben、ThinkGem
 */
import type { UnwrapRef, Ref } from 'vue';
import {
  reactive,
  readonly,
  computed,
  getCurrentInstance,
  watchEffect,
  unref,
  nextTick,
  toRaw,
} from 'vue';

import { isEqual } from 'lodash-es';
import { isEmpty, isNumber, isObject } from '/@/utils/is';

export function useRuleFormItem<T extends Recordable>(
  props: T,
  key: keyof T = 'value',
  changeEvent = 'change',
  emitData?: Ref<any[]>,
) {
  const instance = getCurrentInstance();
  const emit = instance?.emit;
  const compName = instance?.type?.name || 'unknown';

  const isMultiple = computed(() => {
    if (['JeeSiteCheckboxGroup'].includes(compName)) {
      return true;
    }
    if (
      ['JeeSiteSelect', 'JeeSiteTreeSelect'].includes(compName) &&
      (props.mode === 'multiple' || props.mode === 'tags' || props.treeCheckable === true)
    ) {
      return true;
    }
    return false;
  });

  const isDictType = computed(() => !isEmpty(props.dictType));

  const innerState = reactive({
    value: props[key],
  });

  const defaultState = readonly(innerState);

  const setState = (val: UnwrapRef<T[keyof T]>): void => {
    innerState.value = val as T[keyof T];
  };

  watchEffect(() => {
    innerState.value = props[key];
  });

  const state: any = computed({
    get() {
      let value = toRaw(innerState.value) as any;
      if (!value) return undefined;
      if (props.labelInValue) {
        const values: Recordable = [];
        if (isMultiple.value && !(value instanceof Array)) {
          const vals = (value as string)?.split(',');
          const lbls = (props.labelValue as string)?.split(',');
          for (const i in vals) {
            values.push({ value: vals && vals[i], label: lbls && lbls[i] });
          }
          value = values as T[keyof T];
        } else if (!isObject(value) && !(value instanceof Array)) {
          value = { value: String(value), label: props.labelValue };
        } else if (value instanceof Array) {
          for (const i in value) {
            if (isObject(value[i])) break;
            values.push({ value: value[i] });
          }
          if (values.length > 0) {
            value = values as T[keyof T];
          }
        }
      } else if (isMultiple.value && !(value instanceof Array)) {
        value = (value as string).split(',');
      } else if (isDictType.value && isNumber(value)) {
        value = String(value);
      }
      // console.log('innerState', value);
      innerState.value = value as T[keyof T];
      return innerState.value;
    },
    set(value: any) {
      if (isEqual(value, defaultState.value)) return;
      innerState.value = value as T[keyof T];
      nextTick(() => {
        const extData = toRaw(unref(emitData)) || [];
        if (!value) {
          emit?.(changeEvent, undefined, undefined, ...extData);
          return;
        }
        // console.log('values', value);
        const values = value instanceof Array ? value : [value];
        if (props.labelInValue) {
          const vals: Recordable[] = [];
          const labs: Recordable[] = [];
          for (const item of values) {
            vals.push(item.value);
            labs.push(item.label);
          }
          emit?.(
            changeEvent,
            vals.length > 0 ? vals.join(',') : undefined,
            labs.length > 0 ? labs.join(',') : undefined,
            ...extData,
          );
        } else {
          emit?.(
            changeEvent,
            values.length > 0 ? values.join(',') : undefined,
            undefined,
            ...extData,
          );
        }
      });
    },
  });

  return [state, setState, defaultState];
}
