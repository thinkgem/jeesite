import { CheckboxGroup, RadioButtonGroup, RadioGroup } from '/@/components/Form';
import { BasicUpload } from '/@/components/Upload';
import { componentMap } from '/@/components/Table/src/componentMap';

export type ComponentType =
  | 'Input'
  | 'InputTextArea'
  | 'InputNumber'
  | 'Select'
  | 'AutoComplete'
  | 'TreeSelect'
  | 'ListSelect'
  | 'RadioButtonGroup'
  | 'RadioGroup'
  | 'CheckboxGroup'
  | 'Upload'
  | 'Checkbox'
  | 'Switch'
  | 'DatePicker'
  | 'TimePicker';
