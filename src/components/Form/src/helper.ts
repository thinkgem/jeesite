import type { ValidationRule } from 'ant-design-vue/lib/form/Form';
import type { ComponentType } from './types/index';
import { useI18n } from '/@/hooks/web/useI18n';
import { dateUtil } from '/@/utils/dateUtil';
import { isNumber } from '/@/utils/is';

const { t } = useI18n();

/**
 * @description: 生成placeholder
 */
export function createPlaceholderMessage(component: ComponentType) {
  if (component.includes('Input') || component.includes('Complete')) {
    return t('common.inputText');
  }
  if (component.includes('Picker')) {
    return t('common.chooseText');
  }
  if (
    component.includes('Select') ||
    component.includes('Cascader') ||
    component.includes('Checkbox') ||
    component.includes('Radio') ||
    component.includes('Switch')
  ) {
    // return `请选择${label}`;
    return t('common.chooseText');
  }
  return '';
}

const DATE_TYPE = ['DatePicker', 'MonthPicker', 'WeekPicker', 'TimePicker'];

function genType() {
  return [...DATE_TYPE, 'RangePicker'];
}

export const dateItemType = genType();

export function setComponentRuleType(
  rule: ValidationRule,
  component: ComponentType,
  valueFormat: string,
) {
  if (['DatePicker', 'MonthPicker', 'WeekPicker', 'TimePicker'].includes(component)) {
    rule.type = valueFormat ? 'string' : 'object';
  } else if (['RangePicker', 'Upload', 'CheckboxGroup', 'TimePicker'].includes(component)) {
    rule.type = 'array';
  } else if (['InputNumber'].includes(component)) {
    rule.type = 'number';
  }
}

export function processDateValue(
  value: Recordable | any,
  component: ComponentType,
  componentProps: any,
) {
  if (!value || !component) return value;
  const { valueFormat } = componentProps;
  if (dateItemType.includes(component)) {
    if (Array.isArray(value)) {
      const arr: any[] = [];
      for (const val of value) {
        if (valueFormat) {
          arr.push(val ? dateUtil(val).format(valueFormat) : null);
        } else {
          arr.push(val ? dateUtil(val) : null);
        }
      }
      return arr;
    } else if (valueFormat) {
      return dateUtil(value).format(valueFormat);
    } else {
      return dateUtil(value);
    }
  }
  return value;
}

export function processNumberValue(value: Recordable, component: ComponentType) {
  if (!value || !component) return value;
  if (['Input', 'InputPassword', 'InputSearch', 'InputTextArea'].includes(component)) {
    return isNumber(value) ? `${value}` : value;
  }
  return value;
}
