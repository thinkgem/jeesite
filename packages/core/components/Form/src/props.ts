import type { FieldMapToTime, FormSchema } from './types/form';
import type { CSSProperties, PropType } from 'vue';
import type { ColEx } from './types';
import type { TableActionType } from '@jeesite/core/components/Table';
import type { FormButtonProps as ButtonProps } from './types/form';
import type { RowProps } from 'ant-design-vue/lib/grid/Row';
import { propTypes } from '@jeesite/core/utils/propTypes';

export const basicProps = {
  model: {
    type: Object as PropType<Recordable>,
    default: {},
  },
  enctype: {
    type: String as PropType<string>,
    default: 'json', // json | form-data
  },
  // 标签宽度  固定宽度
  labelWidth: {
    type: [Number, String] as PropType<number | string>,
    default: 0,
  },
  fieldMapToTime: {
    type: Array as PropType<FieldMapToTime>,
    default: () => [],
  },
  compact: propTypes.bool,
  // 表单配置规则
  schemas: {
    type: [Array] as PropType<FormSchema[]>,
    default: () => [],
  },
  mergeDynamicData: {
    type: Object as PropType<Recordable>,
    default: null,
  },
  baseRowStyle: {
    type: Object as PropType<CSSProperties>,
  },
  baseColProps: {
    type: Object as PropType<Partial<ColEx>>,
  },
  autoSetPlaceHolder: propTypes.bool.def(true),
  // 在INPUT组件上单击回车时，是否自动提交
  autoSubmitOnEnter: propTypes.bool.def(true),
  submitOnReset: propTypes.bool,
  size: propTypes.oneOf(['default', 'small', 'large']).def('default'),
  // 禁用表单
  disabled: propTypes.bool,
  emptySpan: {
    type: [Number, Object] as PropType<number>,
    default: 0,
  },
  // 是否显示收起展开按钮
  showAdvancedButton: propTypes.bool,
  // 转化时间
  transformDateFunc: {
    type: Function as PropType<Fn>,
    default: (date: any) => {
      return date?.format('YYYY-MM-DD HH:mm:ss') ?? date;
    },
  },
  rulesMessageJoinLabel: propTypes.bool.def(true),
  // 表单验证触发规则（失去焦点的时候再触发）
  validateTrigger: propTypes.string.def('blur'),
  // 超过1行自动折叠
  autoAdvancedLine: propTypes.number.def(1),
  // 不受折叠影响的行数
  alwaysShowLines: propTypes.number.def(1),

  // 是否显示操作按钮
  showActionButtonGroup: propTypes.bool.def(false),
  // 操作列Col配置
  actionColOptions: Object as PropType<Partial<ColEx>>,
  // 显示重置按钮
  showResetButton: propTypes.bool.def(true),
  // 是否聚焦第一个输入框，只在第一个表单项为input的时候作用
  autoFocusFirstItem: propTypes.bool,
  // 重置按钮配置
  resetButtonOptions: Object as PropType<Partial<ButtonProps>>,

  // 显示确认按钮
  showSubmitButton: propTypes.bool.def(true),
  // 确认按钮配置
  submitButtonOptions: Object as PropType<Partial<ButtonProps>>,

  // 自定义重置函数
  resetFunc: Function as PropType<() => Promise<void>>,
  submitFunc: Function as PropType<() => Promise<void>>,

  // 以下为默认props
  hideRequiredMark: propTypes.bool,

  labelCol: Object as PropType<Partial<ColEx>>,

  layout: propTypes.oneOf(['horizontal', 'vertical', 'inline']).def('horizontal'),

  tableAction: {
    type: Object as PropType<Partial<TableActionType>>,
  },

  wrapperCol: Object as PropType<Partial<ColEx>>,

  colon: propTypes.bool.def(true),

  labelAlign: propTypes.string,

  rowProps: Object as PropType<RowProps>,
};
