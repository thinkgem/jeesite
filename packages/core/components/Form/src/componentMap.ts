import type { Component } from 'vue';
import type { ComponentType } from './types/index';
import {
  Input,
  InputGroup,
  InputPassword,
  InputSearch,
  TextArea,
  Checkbox,
  AutoComplete,
  Cascader,
  DatePicker,
  DateMonthPicker,
  DateRangePicker,
  DateWeekPicker,
  InputNumber,
  Switch,
  TimePicker,
  Slider,
  Rate,
  Divider,
} from 'antdv-next';

import JeeSiteText from './components/JeeSiteText.vue';
import JeeSiteSelect from './components/JeeSiteSelect.vue';
import JeeSiteTreeSelect from './components/JeeSiteTreeSelect.vue';
import JeeSiteRadioGroup from './components/JeeSiteRadioGroup.vue';
import JeeSiteCheckboxGroup from './components/JeeSiteCheckboxGroup.vue';
import JeeSiteRadioButtonGroup from './components/JeeSiteRadioButtonGroup.vue';
import FormGroup from './components/FormGroup.vue';

import { ListSelect } from '@jeesite/core/components/ListSelect';
import { BasicUpload } from '@jeesite/core/components/Upload';
import { StrengthMeter } from '@jeesite/core/components/StrengthMeter';
import { IconPicker } from '@jeesite/core/components/Icon';
import { CountdownInput } from '@jeesite/core/components/CountDown';

const componentMap = new Map<ComponentType, Component>();

componentMap.set('Input', Input);
componentMap.set('InputGroup', InputGroup);
componentMap.set('InputPassword', InputPassword);
componentMap.set('InputSearch', InputSearch);
componentMap.set('InputTextArea', TextArea);
componentMap.set('InputNumber', InputNumber);
componentMap.set('AutoComplete', AutoComplete);

componentMap.set('Text', JeeSiteText);
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
componentMap.set('MonthPicker', DateMonthPicker);
componentMap.set('RangePicker', DateRangePicker);
componentMap.set('WeekPicker', DateWeekPicker);
componentMap.set('TimePicker', TimePicker);
componentMap.set('StrengthMeter', StrengthMeter);
componentMap.set('IconPicker', IconPicker);
componentMap.set('InputCountDown', CountdownInput);

componentMap.set('ListSelect', ListSelect);
componentMap.set('Upload', BasicUpload);
componentMap.set('None', Input);
componentMap.set('Divider', Divider);
componentMap.set('FormGroup', FormGroup);

export function add(compName: ComponentType, component: Component) {
  componentMap.set(compName, component);
}

export function del(compName: ComponentType) {
  componentMap.delete(compName);
}

export { componentMap };
