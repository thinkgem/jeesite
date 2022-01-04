import type { Component } from 'vue';
import type { ComponentType } from './types/index';

/**
 * Component list, register here to setting it in the form
 */
import {
  Input,
  // Select,
  // Radio,
  Checkbox,
  AutoComplete,
  Cascader,
  DatePicker,
  InputNumber,
  Switch,
  TimePicker,
  // TreeSelect,
  Slider,
  Rate,
  Divider,
} from 'ant-design-vue';

import JeeSiteSelect from './components/JeeSiteSelect.vue';
import JeeSiteTreeSelect from './components/JeeSiteTreeSelect.vue';
import JeeSiteRadioGroup from './components/JeeSiteRadioGroup.vue';
import JeeSiteCheckboxGroup from './components/JeeSiteCheckboxGroup.vue';
import JeeSiteRadioButtonGroup from './components/JeeSiteRadioButtonGroup.vue';
import FormGroup from './components/FormGroup.vue';

import { BasicUpload } from '/@/components/Upload';
import { StrengthMeter } from '/@/components/StrengthMeter';
import { IconPicker } from '/@/components/Icon';
import { CountdownInput } from '/@/components/CountDown';

const componentMap = new Map<ComponentType, Component>();

componentMap.set('Input', Input);
componentMap.set('InputGroup', Input.Group);
componentMap.set('InputPassword', Input.Password);
componentMap.set('InputSearch', Input.Search);
componentMap.set('InputTextArea', Input.TextArea);
componentMap.set('InputNumber', InputNumber);
componentMap.set('AutoComplete', AutoComplete);

componentMap.set('Select', JeeSiteSelect); //Select);
componentMap.set('TreeSelect', JeeSiteTreeSelect); //TreeSelect);
componentMap.set('Switch', Switch);
componentMap.set('RadioButtonGroup', JeeSiteRadioButtonGroup);
componentMap.set('RadioGroup', JeeSiteRadioGroup); //Radio.Group);
componentMap.set('Checkbox', Checkbox);
componentMap.set('CheckboxGroup', JeeSiteCheckboxGroup); //Checkbox.Group);
componentMap.set('Cascader', Cascader);
componentMap.set('Slider', Slider);
componentMap.set('Rate', Rate);

componentMap.set('DatePicker', DatePicker);
componentMap.set('MonthPicker', DatePicker.MonthPicker);
componentMap.set('RangePicker', DatePicker.RangePicker);
componentMap.set('WeekPicker', DatePicker.WeekPicker);
componentMap.set('TimePicker', TimePicker);
componentMap.set('StrengthMeter', StrengthMeter);
componentMap.set('IconPicker', IconPicker);
componentMap.set('InputCountDown', CountdownInput);

componentMap.set('Upload', BasicUpload);
componentMap.set('Divider', Divider);
componentMap.set('FormGroup', FormGroup);

export function add(compName: ComponentType, component: Component) {
  componentMap.set(compName, component);
}

export function del(compName: ComponentType) {
  componentMap.delete(compName);
}

export { componentMap };
