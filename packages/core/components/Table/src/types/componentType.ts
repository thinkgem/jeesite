import { CheckboxGroup, RadioButtonGroup, RadioGroup } from '@jeesite/core/components/Form';
import { BasicUpload } from '@jeesite/core/components/Upload';
import { componentMap } from '@jeesite/core/components/Table/src/componentMap';

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
