<template>
  <div :class="prefixCls">
    <div
      v-show="!isEdit"
      :class="{ [`${prefixCls}__normal`]: true, 'ellipsis-cell': column.ellipsis }"
      @click="handleEdit"
    >
      <DictLabel v-if="column.dictType" :dictType="column.dictType" :dictValue="currentValueRef" />
      <div v-else class="cell-content" :title="column.ellipsis ? getValues ?? '' : ''">
        {{ getValues ? getValues : '&nbsp;' }}
      </div>
      <FormOutlined :class="`${prefixCls}__normal-icon`" v-if="!column.editRow" />
    </div>

    <a-spin v-if="isEdit" :spinning="spinning">
      <div :class="`${prefixCls}__wrapper`" v-click-outside="onClickOutside">
        <CellComponent
          v-bind="getComponentProps"
          :component="getComponent"
          :style="getWrapperStyle"
          :popoverVisible="getRuleVisible"
          :rule="getRule"
          :ruleMessage="ruleMessage"
          :class="getWrapperClass"
          ref="elRef"
          @change="handleChange"
          @press-enter="handleEnter"
        />
        <div :class="`${prefixCls}__action`" v-if="!getRowEditable">
          <CheckOutlined :class="[`${prefixCls}__icon`, 'mx-2']" @click="handleSubmitClick" />
          <CloseOutlined :class="`${prefixCls}__icon `" @click="handleCancel" />
        </div>
      </div>
    </a-spin>
  </div>
</template>
<script lang="ts">
  import type { CSSProperties, PropType } from 'vue';
  import { computed, defineComponent, nextTick, ref, toRaw, unref, watchEffect } from 'vue';
  import type { BasicColumn } from '../../types/table';
  import type { EditRecordRow } from './index';
  import { CheckOutlined, CloseOutlined, FormOutlined } from '@ant-design/icons-vue';
  import { CellComponent } from './CellComponent';

  import { useDesign } from '/@/hooks/web/useDesign';
  import { useTableContext } from '../../hooks/useTableContext';
  import { formatCell } from '../../hooks/useColumns';

  import clickOutside from '/@/directives/clickOutside';

  import { propTypes } from '/@/utils/propTypes';
  import { isArray, isBoolean, isFunction, isNumber, isObject } from '/@/utils/is';
  import { createPlaceholderMessage } from './helper';
  import { omit, pick, set } from 'lodash-es';
  // import { treeToList } from '/@/utils/helper/treeHelper';
  import { Spin } from 'ant-design-vue';
  import { DictLabel } from '/@/components/Dict';

  export default defineComponent({
    name: 'EditableCell',
    components: {
      FormOutlined,
      CloseOutlined,
      CheckOutlined,
      CellComponent,
      ASpin: Spin,
      DictLabel,
    },
    directives: {
      clickOutside,
    },
    props: {
      value: {
        type: [String, Number, Boolean, Object] as PropType<string | number | boolean | Recordable>,
        default: '',
      },
      labelValue: {
        type: [Array, Object, String, Number] as PropType<Array<any> | object | string | number>,
      },
      record: {
        type: Object as PropType<EditRecordRow>,
      },
      column: {
        type: Object as PropType<BasicColumn>,
        default: () => ({}),
      },
      index: propTypes.number,
    },
    setup(props) {
      const table = useTableContext();
      const isEdit = ref(false);
      const elRef = ref();
      const ruleVisible = ref(false);
      const ruleMessage = ref('');
      const currentValueRef = ref<any>(props.value);
      const defaultValueRef = ref<any>(props.value);
      const currentLabelValueRef = ref<any>(props.labelValue);
      const defaultLabelValueRef = ref<any>(props.labelValue);

      const spinning = ref<boolean>(false);

      const { prefixCls } = useDesign('editable-cell');

      const getComponent = computed(() => props.column?.editComponent || 'Input');
      const getRule = computed(() => props.column?.editRule);

      const getRuleVisible = computed(() => {
        return unref(ruleMessage) && unref(ruleVisible);
      });

      const getIsCheckComp = computed(() => {
        const component = unref(getComponent);
        return ['Checkbox', 'Switch'].includes(component);
      });

      const getComponentProps = computed(() => {
        const compProps = props.column?.editComponentProps ?? {};

        const val = unref(currentValueRef);
        const labelVal = unref(currentLabelValueRef);

        const isCheckValue = unref(getIsCheckComp);
        const valueField = isCheckValue ? 'checked' : 'value';
        const value = isCheckValue ? (isNumber(val) && isBoolean(val) ? val : !!val) : val;

        return {
          size: 'small',
          getPopupContainer: () => unref(table?.wrapRef.value) ?? document.body,
          getCalendarContainer: () => unref(table?.wrapRef.value) ?? document.body,
          placeholder: createPlaceholderMessage(unref(getComponent)),
          ...omit(compProps, 'onChange'),
          [valueField]: value,
          labelValue: labelVal,
          labelInValue: !!props.column?.dataLabel,
        };
      });

      const getValues = computed(() => {
        // const { editComponentProps, editValueMap, dataLabel } = props.column;
        const value = unref(currentValueRef);
        const labelValue = unref(currentLabelValueRef);
        if (props.column?.dataLabel && labelValue) {
          return labelValue;
        }
        if (props.column?.format && value) {
          return formatCell(value, props.column.format, props.record as Recordable, props.index);
        }
        return value;
      });

      const getWrapperStyle = computed((): CSSProperties => {
        if (unref(getIsCheckComp) || unref(getRowEditable)) {
          return {};
        }
        return {
          width: 'calc(100% - 48px)',
        };
      });

      const getWrapperClass = computed(() => {
        const { align = 'center' } = props.column;
        return `edit-cell-align-${align}`;
      });

      const getRowEditable = computed(() => {
        const { editable } = props.record || {};
        return !!editable;
      });

      watchEffect(() => {
        defaultValueRef.value = props.value;
        currentValueRef.value = props.value;
        defaultLabelValueRef.value = props.labelValue;
        currentLabelValueRef.value = props.labelValue;
      });

      watchEffect(() => {
        const { editable } = props.column;
        if (isBoolean(editable) || isBoolean(unref(getRowEditable))) {
          isEdit.value = !!editable || unref(getRowEditable);
        }
      });

      function handleEdit() {
        if (unref(getRowEditable) || unref(props.column?.editRow)) return;
        ruleMessage.value = '';
        isEdit.value = true;
        nextTick(() => {
          const el = unref(elRef);
          el?.focus?.();
        });
      }

      async function handleChange(e: any, labelValue: any) {
        const component = unref(getComponent);
        let value;
        if (!e) {
          value = e;
        } else if (e?.target && Reflect.has(e.target, 'value')) {
          value = (e as ChangeEvent).target.value;
        } else if (component === 'Checkbox') {
          value = (e as ChangeEvent).target.checked;
        } else {
          // } else if (isString(e) || isBoolean(e) || isNumber(e)) {
          value = e;
        }

        const format = props.column?.editComponentProps?.format;
        if (format) {
          if (isObject(value)) {
            value = value._isAMomentObject ? value?.format(format) : value;
          }
          if (isArray(value) && value[0]?._isAMomentObject && value[1]?._isAMomentObject) {
            value = value.map((item) => item?.format(format));
          }
        }

        currentValueRef.value = value;
        currentLabelValueRef.value = labelValue;

        const onChange = props.column?.editComponentProps?.onChange;
        if (onChange && isFunction(onChange)) onChange(...arguments);

        table.emit?.('edit-change', {
          column: props.column,
          value: unref(currentValueRef),
          labelValue: unref(currentLabelValueRef),
          record: toRaw(props.record),
        });
        handleSubmitRule();
      }

      async function handleSubmitRule() {
        const { column, record } = props;
        const { editRule } = column;
        const currentValue = unref(currentValueRef);

        if (editRule) {
          if (isBoolean(editRule) && !currentValue && !isNumber(currentValue)) {
            ruleVisible.value = true;
            const component = unref(getComponent);
            ruleMessage.value = createPlaceholderMessage(component);
            return false;
          }
          if (isFunction(editRule)) {
            const res = await editRule(currentValue, record as Recordable);
            if (!!res) {
              ruleMessage.value = res;
              ruleVisible.value = true;
              return false;
            } else {
              ruleMessage.value = '';
              return true;
            }
          }
        }
        ruleMessage.value = '';
        return true;
      }

      async function handleSubmit(needEmit = true, valid = true) {
        if (valid) {
          const isPass = await handleSubmitRule();
          if (!isPass) return false;
        }

        const { column, index, record } = props;
        if (!record) return false;
        const { key, dataIndex, dataLabel } = column;
        const value = unref(currentValueRef);
        const labelValue = unref(currentLabelValueRef);
        if (!key || !dataIndex) return;

        const dataKey = (dataIndex || key) as string;

        if (!record.editable) {
          const { getBindValues } = table;

          const { beforeEditSubmit, columns } = unref(getBindValues);

          if (beforeEditSubmit && isFunction(beforeEditSubmit)) {
            spinning.value = true;
            const keys: string[] = columns
              .map((_column) => _column.dataIndex)
              .filter((field) => !!field) as string[];
            let result: any = true;
            try {
              result = await beforeEditSubmit({
                record: pick(record, keys),
                index,
                key,
                value,
              });
            } catch (e) {
              result = false;
            } finally {
              spinning.value = false;
            }
            if (result === false) {
              return;
            }
          }
        }

        set(record, dataKey, value || column.editDefaultValue || '');
        if (dataLabel) {
          set(record, dataLabel, labelValue || column.editDefaultLabel || '');
        }

        //const record = await table.updateTableData(index, dataKey, value);
        needEmit && table.emit?.('edit-end', { record, index, key, value, labelValue });
        isEdit.value = false;
      }

      async function handleEnter() {
        if (props.column?.editRow) {
          return;
        }
        handleSubmit();
      }

      function handleSubmitClick() {
        handleSubmit();
      }

      function handleCancel() {
        isEdit.value = false;
        currentValueRef.value = defaultValueRef.value;
        currentLabelValueRef.value = defaultLabelValueRef.value;
        const { column, index, record } = props;
        const { key, dataIndex } = column;
        table.emit?.('edit-cancel', {
          record,
          index,
          key: dataIndex || key,
          value: unref(currentValueRef),
          labelValue: unref(currentLabelValueRef),
        });
      }

      function onClickOutside() {
        if (props.column?.editable || unref(getRowEditable)) {
          return;
        }
        const component = unref(getComponent);

        if (component.includes('Input')) {
          handleCancel();
        }
      }

      function initCbs(cbs: 'submitCbs' | 'validCbs' | 'cancelCbs', handle: Fn) {
        if (props.record) {
          /* eslint-disable  */
          isArray(props.record[cbs])
            ? props.record[cbs]?.push(handle)
            : (props.record[cbs] = [handle]);
        }
      }

      watchEffect(() => {
        if (props.record) {
          initCbs('submitCbs', handleSubmit);
          initCbs('validCbs', handleSubmitRule);
          initCbs('cancelCbs', handleCancel);

          if (props.column.dataIndex) {
            if (!props.record.editValueRefs) props.record.editValueRefs = {};
            props.record.editValueRefs[props.column.dataIndex] = currentValueRef;
          }
          /* eslint-disable  */
          props.record.onCancelEdit = () => {
            isArray(props.record?.cancelCbs) && props.record?.cancelCbs.forEach((fn) => fn());
          };
          /* eslint-disable */
          props.record.onSubmitEdit = async () => {
            if (isArray(props.record?.submitCbs)) {
              if (!props.record?.onValid?.()) return;
              const submitFns = props.record?.submitCbs || [];
              submitFns.forEach((fn) => fn(false, false));
              table.emit?.('edit-row-end');
              return true;
            }
          };
        }
      });

      return {
        isEdit,
        prefixCls,
        handleEdit,
        currentValueRef,
        handleSubmit,
        handleChange,
        handleCancel,
        elRef,
        getComponent,
        getRule,
        onClickOutside,
        ruleMessage,
        getRuleVisible,
        getComponentProps,
        getWrapperStyle,
        getWrapperClass,
        getRowEditable,
        getValues,
        handleEnter,
        handleSubmitClick,
        spinning,
      };
    },
  });
</script>
<style lang="less">
  @prefix-cls: ~'jeesite-editable-cell';

  .edit-cell-align-left {
    text-align: left;

    input:not(.ant-calendar-picker-input, .ant-time-picker-input) {
      text-align: left;
    }
  }

  .edit-cell-align-center {
    text-align: center;

    input:not(.ant-calendar-picker-input, .ant-time-picker-input) {
      text-align: center;
    }
  }

  .edit-cell-align-right {
    text-align: right;

    input:not(.ant-calendar-picker-input, .ant-time-picker-input) {
      text-align: right;
    }
  }

  .edit-cell-rule-popover {
    .ant-popover-inner-content {
      padding: 4px 8px;
      color: @error-color;
      // border: 1px solid @error-color;
      border-radius: 2px;
    }
  }
  .@{prefix-cls} {
    position: relative;

    &__wrapper {
      display: flex;
      align-items: center;
      justify-content: center;

      > .ant-select {
        min-width: calc(100% - 50px);
      }
    }

    &__icon {
      &:hover {
        transform: scale(1.2);

        svg {
          color: @primary-color;
        }
      }
    }

    .ellipsis-cell {
      .cell-content {
        overflow-wrap: break-word;
        word-break: break-word;
        overflow: hidden;
        white-space: nowrap;
        text-overflow: ellipsis;
      }
    }

    &__normal {
      &-icon {
        position: absolute;
        top: 4px;
        right: 0;
        display: none;
        width: 20px;
        cursor: pointer;
      }
    }

    &:hover {
      .@{prefix-cls}__normal-icon {
        display: inline-block;
      }
    }
  }
</style>
