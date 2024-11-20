/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 * No deletion without permission, or be held responsible to law.
 * @author Vben、Think Gem
 */
import { isArray, isFunction, isObject, isString, isNullOrUnDef } from '@jeesite/core/utils/is';
import { dateUtil } from '@jeesite/core/utils/dateUtil';
import { unref } from 'vue';
import type { Ref, ComputedRef } from 'vue';
import type { FormProps, FormSchema } from '../types/form';
import { set } from 'lodash-es';

interface UseFormValuesContext {
  defaultValueRef: Ref<any>;
  getSchema: ComputedRef<FormSchema[]>;
  getProps: ComputedRef<FormProps>;
  formModel: Recordable;
}
export function useFormValues({
  defaultValueRef,
  getSchema,
  formModel,
  getProps,
}: UseFormValuesContext) {
  /**
   * Processing form values
   */
  function handleFormValues(values: Recordable) {
    if (!isObject(values)) {
      return {};
    }
    const res: Recordable = {};
    const { transformDateFunc, enctype } = unref(getProps);
    // console.log('handleFormValues', values);
    for (const item of Object.entries(values)) {
      // console.log('handleFormValuesItem', item);
      let [, value] = item;
      const [key] = item;
      if (!key || (isArray(value) && value.length === 0) || isFunction(value)) {
        continue;
      }
      if (isObject(value) && value?.format) {
        value = transformDateFunc?.(value);
      } else if (isArray(value) && value[0]?.format && value[1]?.format) {
        value = value.map((item) => transformDateFunc?.(item));
      }
      // Remove spaces
      if (isString(value)) {
        value = value.trim();
      }
      // 表单编码类型
      if (enctype === 'json') {
        set(res, key, value);
      } else if (enctype === 'form-data') {
        res[key] = value;
      }
    }
    return handleRangeTimeValue(res);
  }

  /**
   * @description: Processing time interval parameters
   */
  function handleRangeTimeValue(values: Recordable) {
    const fieldMapToTime = unref(getProps).fieldMapToTime;

    if (!fieldMapToTime || !Array.isArray(fieldMapToTime)) {
      return values;
    }

    for (const [field, [startTimeKey, endTimeKey], format = 'YYYY-MM-DD'] of fieldMapToTime) {
      if (!field || !startTimeKey || !endTimeKey || !values[field]) {
        continue;
      }

      const [startTime, endTime]: string[] = values[field];

      values[startTimeKey] = dateUtil(startTime).format(format);
      values[endTimeKey] = dateUtil(endTime).format(format);
      Reflect.deleteProperty(values, field);
    }

    return values;
  }

  function initDefault() {
    const schemas = unref(getSchema);
    const obj: Recordable = {};
    schemas.forEach((item) => {
      const { defaultValue = '' } = item;
      if (!isNullOrUnDef(defaultValue)) {
        obj[item.field] = defaultValue;
        formModel[item.field] = defaultValue;
      }
    });
    defaultValueRef.value = obj;
  }

  return { handleFormValues, initDefault };
}
