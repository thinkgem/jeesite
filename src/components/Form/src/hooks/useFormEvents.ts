/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 * No deletion without permission, or be held responsible to law.
 * @author Vben、ThinkGem
 */
import type { ComputedRef, Ref } from 'vue';
import type { FormProps, FormSchema, FormActionType } from '../types/form';
import type { NamePath } from 'ant-design-vue/lib/form/interface';
import { unref, toRaw } from 'vue';
import { isArray, isFunction, isObject, isString } from '/@/utils/is';
import { deepMerge } from '/@/utils';
import { processNumberValue, processDateValue } from '../helper';
import { cloneDeep, get, uniqBy } from 'lodash-es';
import { error } from '/@/utils/log';

interface UseFormActionContext {
  emit: EmitType;
  getProps: ComputedRef<FormProps>;
  getSchema: ComputedRef<FormSchema[]>;
  formModel: Recordable;
  defaultValueRef: Ref<Recordable>;
  formElRef: Ref<FormActionType>;
  schemaRef: Ref<FormSchema[]>;
  handleFormValues: Fn;
}
export function useFormEvents({
  emit,
  getProps,
  formModel,
  getSchema,
  defaultValueRef,
  formElRef,
  schemaRef,
  handleFormValues,
}: UseFormActionContext) {
  async function resetFields(): Promise<void> {
    const { resetFunc, submitOnReset } = unref(getProps);
    resetFunc && isFunction(resetFunc) && (await resetFunc());

    const formEl = unref(formElRef);
    if (!formEl) return;

    Object.keys(formModel).forEach((key) => {
      formModel[key] = defaultValueRef.value[key];
    });
    clearValidate();
    emit('reset', toRaw(formModel));
    submitOnReset && handleSubmit();
  }

  /**
   * @description: Set form value
   * JeeSite 重写支持嵌套值，特殊类型转换
   */
  async function setFieldsValue(values: Recordable): Promise<void> {
    if (!values || Object.keys(values).length === 0) return;
    // const validKeys: string[] = [];
    for (const schema of unref(getSchema)) {
      const component = schema?.component;
      const props = getComponentProps(schema);
      const key = schema.field;
      let value = get(values, key);
      if (value) {
        value = processNumberValue(value, component);
        value = processDateValue(value, component, props);
        // console.log('1', key, value);
      }
      formModel[key] = value;
      const labelKey = schema?.fieldLabel;
      if (labelKey) {
        const labelValue = get(values, labelKey);
        // console.log('2', labelKey, labelValue);
        formModel[labelKey] = labelValue;
      }
      // validKeys.push(key);
    }
    // validateFields(validKeys).catch((_) => {});
  }

  /**
   * @description: get component props
   */
  function getComponentProps(schema: any) {
    let { componentProps = {} } = schema;
    if (isFunction(componentProps)) {
      componentProps = componentProps({ schema, formModel }) ?? {};
    }
    return componentProps as Recordable;
  }

  /**
   * @description: Delete based on field name
   */
  async function removeSchemaByFiled(fields: string | string[]): Promise<void> {
    const schemaList: FormSchema[] = cloneDeep(unref(getSchema));
    if (!fields) {
      return;
    }

    let fieldList: string[] = isString(fields) ? [fields] : fields;
    if (isString(fields)) {
      fieldList = [fields];
    }
    for (const field of fieldList) {
      _removeSchemaByFiled(field, schemaList);
    }
    schemaRef.value = schemaList;
  }

  /**
   * @description: Delete based on field name
   */
  function _removeSchemaByFiled(field: string, schemaList: FormSchema[]): void {
    if (isString(field)) {
      const index = schemaList.findIndex((schema) => schema.field === field);
      if (index !== -1) {
        delete formModel[field];
        schemaList.splice(index, 1);
      }
    }
  }

  /**
   * @description: Insert after a certain field, if not insert the last
   */
  async function appendSchemaByField(schema: FormSchema, prefixField?: string, first = false) {
    const schemaList: FormSchema[] = cloneDeep(unref(getSchema));

    const index = schemaList.findIndex((schema) => schema.field === prefixField);
    const hasInList = schemaList.some((item) => item.field === prefixField || schema.field);

    if (!hasInList) return;

    if (!prefixField || index === -1 || first) {
      first ? schemaList.unshift(schema) : schemaList.push(schema);
      schemaRef.value = schemaList;
      return;
    }
    if (index !== -1) {
      schemaList.splice(index + 1, 0, schema);
    }
    schemaRef.value = schemaList;
  }

  async function resetSchema(data: Partial<FormSchema> | Partial<FormSchema>[]) {
    let updateData: Partial<FormSchema>[] = [];
    if (isObject(data)) {
      updateData.push(data as FormSchema);
    }
    if (isArray(data)) {
      updateData = [...data];
    }

    const hasField = updateData.every(
      (item) =>
        item.component === 'None' ||
        item.component === 'Divider' ||
        item.component === 'FormGroup' ||
        (Reflect.has(item, 'field') && item.field),
    );

    if (!hasField) {
      error('重载的元素不能是 None、Divider 或 FormGroup 组件，必须包含 field 字段。');
      return;
    }
    schemaRef.value = updateData as FormSchema[];
  }

  async function updateSchema(data: Partial<FormSchema> | Partial<FormSchema>[]) {
    let updateData: Partial<FormSchema>[] = [];
    if (isObject(data)) {
      updateData.push(data as FormSchema);
    }
    if (isArray(data)) {
      updateData = [...data];
    }

    const hasField = updateData.every(
      (item) =>
        item.component === 'None' ||
        item.component === 'Divider' ||
        item.component === 'FormGroup' ||
        (Reflect.has(item, 'field') && item.field),
    );

    if (!hasField) {
      error('更新的元素不能是 None、Divider 或 FormGroup 组件，必须包含 field 字段。');
      return;
    }
    const schema: FormSchema[] = [];
    updateData.forEach((item) => {
      unref(getSchema).forEach((val) => {
        if (val.field === item.field) {
          const newSchema = deepMerge(val, item);
          schema.push(newSchema as FormSchema);
        } else {
          schema.push(val);
        }
      });
    });
    schemaRef.value = uniqBy(schema, 'field');
  }

  function getFieldsValue(): Recordable {
    const formEl = unref(formElRef);
    if (!formEl) return {};
    return handleFormValues(toRaw(unref(formModel)));
  }

  async function validateFields(nameList?: NamePath[] | undefined) {
    const values = await unref(formElRef)?.validateFields(nameList);
    const res = handleFormValues(toRaw(values));
    return res;
  }

  async function validate(nameList?: NamePath[] | undefined) {
    const values = await unref(formElRef)?.validate(nameList);
    const res = handleFormValues(toRaw(values));
    return res;
  }

  async function clearValidate(name?: string | string[]) {
    await unref(formElRef)?.clearValidate(name);
  }

  async function scrollToField(name: NamePath, options?: ScrollOptions | undefined) {
    await unref(formElRef)?.scrollToField(name, options);
  }

  /**
   * @description: Form submission
   */
  async function handleSubmit(e?: Event): Promise<void> {
    e && e.preventDefault();
    const { submitFunc } = unref(getProps);
    if (submitFunc && isFunction(submitFunc)) {
      await submitFunc();
      return;
    }
    const formEl = unref(formElRef);
    if (!formEl) return;
    try {
      const data = await validate();
      emit('submit', data);
    } catch (error: any) {
      throw new Error(error);
    }
  }

  return {
    handleSubmit,
    clearValidate,
    validate,
    validateFields,
    getFieldsValue,
    updateSchema,
    resetSchema,
    appendSchemaByField,
    removeSchemaByFiled,
    resetFields,
    setFieldsValue,
    scrollToField,
  };
}
