<!--
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 * No deletion without permission, or be held responsible to law.
 * @author Vben、ThinkGem
-->
<template>
  <Form
    v-bind="getBindValue"
    :class="getFormClass"
    ref="formElRef"
    :model="formModel"
    @keypress.enter="handleEnterPress"
  >
    <slot name="formTop"></slot>
    <Row v-bind="getRow">
      <slot name="formHeader"></slot>
      <template v-for="schema in getSchema" :key="schema.field">
        <FormItem
          :tableAction="props.tableAction"
          :formActionType="formActionType"
          :schema="schema"
          :formProps="getProps"
          :allDefaultValues="defaultValueRef"
          :formModel="formModel"
          :setFormModel="setFormModel"
        >
          <template #[item]="data" v-for="item in Object.keys($slots)">
            <slot :name="item" v-bind="data || {}"></slot>
          </template>
        </FormItem>
      </template>

      <FormAction v-bind="getFormActionBindProps" @toggle-advanced="handleToggleAdvanced">
        <template #[item]="data" v-for="item in ['resetBefore', 'submitBefore', 'advanceBefore', 'advanceAfter']">
          <slot :name="item" v-bind="data || {}"></slot>
        </template>
      </FormAction>
      <slot name="formFooter"></slot>
    </Row>
    <slot name="formBottom"></slot>
  </Form>
</template>
<script lang="ts" setup name="BasicForm">
  import type { FormActionType, FormProps, FormSchema } from './types/form';
  import type { AdvanceState } from './types/hooks';
  import type { Ref } from 'vue';

  import { reactive, ref, computed, unref, onMounted, watch, nextTick } from 'vue';
  import { Form, Row } from 'ant-design-vue';
  import FormItem from './components/FormItem.vue';
  import FormAction from './components/FormAction.vue';

  import { dateItemType } from './helper';
  import { dateUtil } from '@jeesite/core/utils/dateUtil';

  // import { cloneDeep } from 'lodash-es';
  import { deepMerge } from '@jeesite/core/utils';

  import { useFormValues } from './hooks/useFormValues';
  import useAdvanced from './hooks/useAdvanced';
  import { useFormEvents } from './hooks/useFormEvents';
  import { createFormContext } from './hooks/useFormContext';
  import { useAutoFocus } from './hooks/useAutoFocus';
  import { useModalContext } from '@jeesite/core/components/Modal';

  import { basicProps } from './props';
  import { useDesign } from '@jeesite/core/hooks/web/useDesign';
  import { isString, isArray } from '@jeesite/core/utils/is';
  import { useAttrs } from '@jeesite/core/hooks/core/useAttrs';

  const props = defineProps(basicProps);
  const emit = defineEmits(['advanced-change', 'reset', 'submit', 'register']);
  const attrs = useAttrs();

  const formModel = reactive<Recordable>({});
  const modalFn = useModalContext();

  const advanceState = reactive<AdvanceState>({
    isAdvanced: true,
    hideAdvanceBtn: false,
    isLoad: false,
    actionSpan: 6,
  });

  const defaultValueRef = ref<Recordable>({});
  const isInitedDefaultRef = ref(false);
  const propsRef = ref<Partial<FormProps>>({});
  const schemaRef = ref<Nullable<FormSchema[]>>(null);
  const formElRef = ref<Nullable<FormActionType>>(null);

  const { prefixCls } = useDesign('basic-form');

  // Get the basic configuration of the form
  const getProps = computed((): FormProps => {
    return { ...props, ...(unref(propsRef) as unknown as FormProps) } as FormProps;
  });

  const getFormClass = computed(() => {
    return [
      prefixCls,
      {
        [`${prefixCls}--compact`]: unref(getProps).compact,
      },
    ];
  });

  // Get uniform row style and Row configuration for the entire form
  const getRow = computed((): Recordable => {
    const { baseRowStyle = {}, rowProps } = unref(getProps);
    return {
      style: baseRowStyle,
      ...rowProps,
    };
  });

  const getBindValue = computed(() => ({ ...attrs, ...props, ...unref(getProps) }) as Recordable);

  const getSchema = computed((): FormSchema[] => {
    const schemas: FormSchema[] = unref(schemaRef) || (unref(getProps).schemas as any);
    for (const schema of schemas) {
      const { defaultValue, component } = schema;
      // 日期类型处理，如果 默认值 是字符串的时候再进行转换，否则会出现日期类型转换错误
      if (defaultValue && dateItemType.includes(component)) {
        if (isString(defaultValue)) {
          schema.defaultValue = dateUtil(defaultValue);
        } else if (isArray(defaultValue)) {
          const def: any[] = [];
          defaultValue.forEach((item) => {
            if (isString(item)) {
              def.push(dateUtil(item));
            }
          });
          schema.defaultValue = def;
        }
      }
    }
    if (unref(getProps).showAdvancedButton) {
      return schemas.filter((schema) => schema.component !== 'Divider') as FormSchema[];
    } else {
      return schemas as FormSchema[];
    }
  });

  const { handleToggleAdvanced } = useAdvanced({
    advanceState,
    emit,
    getProps,
    getSchema,
    formModel,
    defaultValueRef,
  });

  const { handleFormValues, initDefault } = useFormValues({
    getProps,
    defaultValueRef,
    getSchema,
    formModel,
  });

  useAutoFocus({
    getSchema,
    getProps,
    isInitedDefault: isInitedDefaultRef,
    formElRef: formElRef as Ref<FormActionType>,
  });

  const {
    handleSubmit,
    setFieldsValue,
    clearValidate,
    validate,
    validateFields,
    getFieldsValue,
    updateSchema,
    resetSchema,
    appendSchemaByField,
    removeSchemaByFiled,
    resetFields,
    scrollToField,
  } = useFormEvents({
    emit,
    getProps,
    formModel,
    getSchema,
    defaultValueRef,
    formElRef: formElRef as Ref<FormActionType>,
    schemaRef: schemaRef as Ref<FormSchema[]>,
    handleFormValues,
  });

  createFormContext({
    resetAction: resetFields,
    submitAction: handleSubmit,
  });

  watch(
    () => unref(getProps).model,
    () => {
      const { model } = unref(getProps);
      if (!model) return;
      setFieldsValue(model);
    },
    {
      immediate: true,
    },
  );

  watch(
    () => unref(getProps).schemas,
    (schemas) => {
      resetSchema(schemas ?? []);
    },
  );

  watch(
    () => getSchema.value,
    (schema) => {
      nextTick(() => {
        //  Solve the problem of modal adaptive height calculation when the form is placed in the modal
        modalFn?.redoModalHeight?.();
      });
      if (unref(isInitedDefaultRef)) {
        return;
      }
      if (schema?.length) {
        initDefault();
        isInitedDefaultRef.value = true;
      }
    },
  );

  async function setProps(formProps: Partial<FormProps>): Promise<void> {
    propsRef.value = deepMerge(unref(propsRef) || {}, formProps);
  }

  function setFormModel(key: string, value: any, labelKey?: string, labelValue?: any) {
    formModel[key] = value;
    if (labelKey) {
      formModel[labelKey] = labelValue;
    }
    // const { validateTrigger } = unref(getBindValue);
    // if (!validateTrigger || validateTrigger === 'change') {
    //   setTimeout(() => {
    //     validateFields([key]).catch((_) => {});
    //   });
    // }
  }

  function handleEnterPress(e: KeyboardEvent) {
    const { autoSubmitOnEnter } = unref(getProps);
    if (!autoSubmitOnEnter) return;
    if (e.key === 'Enter' && e.target && e.target instanceof HTMLElement) {
      const target: HTMLElement = e.target as HTMLElement;
      if (target && target.tagName && target.tagName.toUpperCase() == 'INPUT') {
        handleSubmit();
      }
    }
  }

  const getFormActionBindProps = computed((): Recordable => ({ ...getProps.value, ...advanceState }));

  const formActionType: Partial<FormActionType> = {
    getFieldsValue,
    setFieldsValue,
    resetFields,
    updateSchema,
    resetSchema,
    setProps,
    removeSchemaByFiled,
    appendSchemaByField,
    clearValidate,
    validateFields,
    validate,
    submit: handleSubmit,
    scrollToField: scrollToField,
  };

  onMounted(() => {
    initDefault();
    emit('register', formActionType);
  });

  defineExpose(formActionType);
</script>
<style lang="less">
  @prefix-cls: ~'jeesite-basic-form';

  .@{prefix-cls} {
    padding-top: 8px;
    padding-right: 20px;

    .ant-form-item {
      margin-bottom: 20px;

      &-label {
        line-height: 14px;

        label::after {
          margin: 0 4px 0 2px !important;
        }
      }

      &-explain {
        position: absolute;

        &-error {
          position: absolute;
          top: -2px;
        }
      }

      &.suffix-item {
        .ant-form-item-children {
          display: flex;
        }

        .ant-form-item-control {
          margin-top: 4px;
        }

        .suffix {
          display: inline-flex;
          padding-left: 6px;
          line-height: 1;
          align-items: center;
        }
      }

      &.no-label {
        margin-left: 20px;
      }
    }

    .ant-row > .ant-col:last-child > .ant-form-item {
      &:not(.ant-form-item-with-help) {
        margin-bottom: 10px;
      }
    }

    .ant-form-explain {
      font-size: 14px;
    }

    .ant-input-disabled,
    .ant-input-number-disabled,
    .ant-radio-wrapper-disabled,
    .ant-select-disabled .ant-select-selector,
    .ant-select-disabled .ant-select-selection-item {
      color: fade(@text-color-base, 75) !important;
      background: rgb(0 0 0 / 2%) !important;
    }

    &--compact {
      .ant-form-item {
        margin-bottom: 7px !important;
      }

      .jeesite-basic-help {
        margin-left: 3px;
      }

      .ant-form-item-label > label {
        height: 31px;
      }

      .ant-form-action {
        width: 100%;
        white-space: nowrap;

        .ant-btn {
          padding-left: 12px;
          padding-right: 12px;
          //margin-top: 1px;
          //font-size: 13px;
          //height: 30px;
          //border-radius: 4px;

          &.ant-btn-link {
            z-index: 1;
          }
        }
      }

      // .ant-input-group {
      //   &-addon {
      //     .ant-btn {
      //       margin: 0;
      //       padding: 0;
      //     }
      //   }
      // }
    }

    .ant-form-item-label > label {
      padding-right: 3px;
      white-space: normal;
      min-height: 32px;
      height: auto;
    }

    .ant-input-number,
    .ant-input-number-group-wrapper,
    .ant-picker-default {
      width: 100%;
    }
  }
</style>
