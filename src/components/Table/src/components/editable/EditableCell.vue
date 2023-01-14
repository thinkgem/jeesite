<!--
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 * No deletion without permission, or be held responsible to law.
 * @author Vben、ThinkGem
-->
<script lang="tsx">
  import type { CSSProperties, PropType } from 'vue';
  import { computed, defineComponent, nextTick, ref, toRaw, unref, watchEffect } from 'vue';
  import type { BasicColumn } from '../../types/table';
  import type { EditRecordRow } from './index';
  import { CheckOutlined, CloseOutlined, FormOutlined } from '@ant-design/icons-vue';
  import { CellComponent } from './CellComponent';

  import { useDesign } from '/@/hooks/web/useDesign';
  import { useTableContext } from '../../hooks/useTableContext';
  import { formatCell } from '../../hooks/useColumns';

  import clickOutside from '/@/directives/clickOutsideSimple';

  import { propTypes } from '/@/utils/propTypes';
  import { isArray, isBoolean, isDef, isFunction, isNumber, isObject } from '/@/utils/is';
  import { createPlaceholderMessage } from './helper';
  import { pick, set } from 'lodash-es';
  // import { treeToList } from '/@/utils/helper/treeHelper';
  import { Spin } from 'ant-design-vue';
  import { DictLabel } from '/@/components/Dict';
  import { dateUtil } from '/@/utils/dateUtil';

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

      const getIsDateComp = computed(() => {
        const component = unref(getComponent);
        return ['DatePicker', 'MonthPicker', 'WeekPicker', 'TimePicker', 'RangePicker'].includes(component);
      });

      const getEditComponentProps = computed(() => {
        const { value: text, record, column, index } = props;
        let compProps = props.column?.editComponentProps ?? {};
        if (isFunction(compProps)) {
          compProps = compProps({ text, record, column, index }) ?? {};
        }
        return compProps;
      });

      const getComponentProps = computed(() => {
        const compProps = unref(getEditComponentProps);

        let value = unref(currentValueRef);
        const labelVal = unref(currentLabelValueRef);

        const isCheckValue = unref(getIsCheckComp);
        const valueField = isCheckValue ? 'checked' : 'value';

        if (isCheckValue) {
          value = isNumber(value) && isBoolean(value) ? value : !!value;
        } else if (value && unref(getIsDateComp)) {
          if (Array.isArray(value)) {
            const arr: any[] = [];
            for (const val of value) {
              arr.push(val ? dateUtil(val) : null);
            }
            value = arr;
          } else {
            value = dateUtil(value);
          }
        }

        return {
          size: 'small',
          getPopupContainer: () => unref(table?.wrapRef.value) ?? document.body,
          placeholder: createPlaceholderMessage(unref(getComponent)),
          dropdownMatchSelectWidth: false,
          ...compProps,
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
        if (props.column?.format && isDef(value)) {
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
          minWidth: 'calc(100% - 48px)',
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

        const editComponentProps = unref(getEditComponentProps);
        const format = editComponentProps?.format;
        if (format) {
          if (isObject(value) && value.format) {
            value = value?.format(format) ?? value;
          } else if (isArray(value) && value[0]?.format && value[1]?.format) {
            value = value.map((item) => item?.format(format));
          }
        }

        currentValueRef.value = value;
        currentLabelValueRef.value = labelValue;

        const onChange = editComponentProps?.onChange;
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
            editRule(currentValue, record as Recordable)
            .then(() => {
              ruleMessage.value = '';
              return true;
            })
            .catch((msg) => {
              ruleMessage.value = msg;
              ruleVisible.value = true;
              return false;
            });
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
        // const component = unref(getComponent);
        // if (component.includes('Input')) {
        //   handleCancel();
        // }
        // 自动取消上一个组件编辑状态
        if (props.column?.editAutoCancel) {
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
            props.record.editValueRefs[props.column.dataIndex as any] = currentValueRef;
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
    render() {
      return (
        <div class={this.prefixCls}>
          <div
            v-show={!this.isEdit}
            class={{ [`${this.prefixCls}__normal`]: true, 'ellipsis-cell': this.column.ellipsis }}
            onClick={this.handleEdit}
          >
            {this.column.dictType
              ? <DictLabel
                dictType={this.column.dictType}
                dictValue={this.currentValueRef}
                defaultValue={this.column.defaultValue}
              />
              : <div class="cell-content" title={this.column.ellipsis ? this.getValues ?? '' : ''}>
                {this.column.editRender
                  ? this.column.editRender({
                      text: this.value,
                      record: this.record as Recordable,
                      column: this.column,
                      index: this.index,
                    })
                  : this.getValues
                  ? this.getValues
                  : '\u00A0'}
              </div>
            }
            {!this.column.editRow && <FormOutlined class={`${this.prefixCls}__normal-icon`} />}
          </div>
          {this.isEdit && (
            <Spin spinning={this.spinning}>
              <div class={`${this.prefixCls}__wrapper`} v-click-outside={this.onClickOutside}>
                <CellComponent
                  {...this.getComponentProps}
                  component={this.getComponent}
                  style={this.getWrapperStyle}
                  popoverVisible={this.getRuleVisible}
                  rule={this.getRule}
                  ruleMessage={this.ruleMessage}
                  class={this.getWrapperClass}
                  ref="elRef"
                  onChange={this.handleChange}
                  onPressEnter={this.handleEnter}
                />
                {!this.getRowEditable && (
                  <div class={`${this.prefixCls}__action`}>
                    <CheckOutlined
                      class={[`${this.prefixCls}__icon`, 'mx-2']}
                      onClick={this.handleSubmitClick}
                    />
                    <CloseOutlined class={`${this.prefixCls}__icon `} onClick={this.handleCancel} />
                  </div>
                )}
              </div>
            </Spin>
          )}
        </div>
      );
    },
  });
</script>
<style lang="less">
  @prefix-cls: ~'jeesite-editable-cell';

  .edit-cell-align-left {
    text-align: left;

    input:not(.ant-calendar-picker-input):not(.ant-time-picker-input) {
      text-align: left;
    }
  }

  .edit-cell-align-center {
    text-align: center;

    input:not(.ant-calendar-picker-input):not(.ant-time-picker-input) {
      text-align: center;
    }
  }

  .edit-cell-align-right {
    text-align: right;

    input:not(.ant-calendar-picker-input):not(.ant-time-picker-input) {
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
      // display: inline-block; // 去掉，否则编辑表格的 ellipsis 省略号失效
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
