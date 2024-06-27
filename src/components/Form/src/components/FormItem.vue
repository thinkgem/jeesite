<!--
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 * No deletion without permission, or be held responsible to law.
 * @author Vben、ThinkGem
-->
<script lang="tsx">
  import type { PropType, Ref } from 'vue';
  import type { FormActionType, FormProps } from '../types/form';
  import type { FormSchema } from '../types/form';
  import type { Rule } from 'ant-design-vue/lib/form';
  import type { TableActionType } from '/@/components/Table';
  import { defineComponent, computed, unref, toRefs } from 'vue';
  import { Form, Col } from 'ant-design-vue';
  import { componentMap } from '../componentMap';
  import { BasicHelp } from '/@/components/Basic';
  import { isBoolean, isFunction, isNull, isEmpty, isString } from '/@/utils/is';
  import { getSlot } from '/@/utils/helper/tsxHelper';
  import { createPlaceholderMessage, setComponentRuleType } from '../helper';
  import { upperFirst, cloneDeep } from 'lodash-es';
  import { useItemLabelWidth } from '../hooks/useLabelWidth';
  import { useI18n } from '/@/hooks/web/useI18n';

  export default defineComponent({
    name: 'JeeSiteFormItem',
    inheritAttrs: false,
    props: {
      schema: {
        type: Object as PropType<FormSchema>,
        default: () => ({}),
      },
      formProps: {
        type: Object as PropType<FormProps>,
        default: () => ({}),
      },
      allDefaultValues: {
        type: Object as PropType<Recordable>,
        default: () => ({}),
      },
      formModel: {
        type: Object as PropType<Recordable>,
        default: () => ({}),
      },
      setFormModel: {
        type: Function as PropType<
          (key: string, value: any, labelKey?: string, labelValue?: any) => void
        >,
        default: null,
      },
      tableAction: {
        type: Object as PropType<TableActionType>,
      },
      formActionType: {
        type: Object as PropType<FormActionType>,
      },
      colLayout: {
        type: Boolean,
        default: true,
      },
    },
    setup(props, { slots }) {
      const { t } = useI18n();

      const { schema, formProps } = toRefs(props) as {
        schema: Ref<FormSchema>;
        formProps: Ref<FormProps>;
      };

      const itemLabelWidthProp = useItemLabelWidth(schema, formProps);

      const getValues = computed(() => {
        const { allDefaultValues, formModel, schema } = props;
        const { mergeDynamicData } = props.formProps;
        return {
          field: schema.field,
          model: formModel,
          values: {
            ...mergeDynamicData,
            ...allDefaultValues,
            ...formModel,
          } as Recordable,
          schema: schema,
        };
      });

      const getComponentsProps = computed(() => {
        const { schema, formModel, tableAction, formActionType } = props;
        let { componentProps = {} } = schema;
        if (isFunction(componentProps)) {
          componentProps = componentProps({ schema, formModel, tableAction, formActionType }) ?? {};
        }
        if (schema.component === 'InputPassword' && !componentProps['autocomplete']) {
          componentProps['autocomplete'] = 'false';
        }
        if (schema.component === 'Divider') {
          componentProps = Object.assign({ type: 'horizontal' }, componentProps, {
            orientation: 'left',
            plain: true,
          });
        }
        return componentProps as Recordable;
      });

      const getDisable = computed(() => {
        const { disabled: globDisabled } = props.formProps;
        const { dynamicDisabled } = props.schema;
        const { disabled: itemDisabled = false } = unref(getComponentsProps);
        let disabled = !!globDisabled || itemDisabled;
        if (isBoolean(dynamicDisabled)) {
          disabled = dynamicDisabled;
        }
        if (isFunction(dynamicDisabled)) {
          disabled = dynamicDisabled(unref(getValues));
        }
        return disabled;
      });

      function getShow(): { isShow: boolean; isIfShow: boolean } {
        const { show, ifShow } = props.schema;
        const { showAdvancedButton } = props.formProps;
        const itemIsAdvanced = showAdvancedButton
          ? isBoolean(props.schema.isAdvanced)
            ? props.schema.isAdvanced
            : true
          : true;

        let isShow = true;
        let isIfShow = true;

        if (isBoolean(show)) {
          isShow = show;
        }
        if (isBoolean(ifShow)) {
          isIfShow = ifShow;
        }
        if (isFunction(show)) {
          isShow = show(unref(getValues));
        }
        if (isFunction(ifShow)) {
          isIfShow = ifShow(unref(getValues));
        }
        isShow = isShow && itemIsAdvanced;
        return { isShow, isIfShow };
      }

      function handleRules(): Rule[] {
        const {
          rules: defRules = [],
          component,
          rulesMessageJoinLabel,
          label,
          dynamicRules,
          required,
        } = props.schema;

        if (isFunction(dynamicRules)) {
          return dynamicRules(unref(getValues)) as Rule[];
        }

        let rules: Rule[] = cloneDeep(defRules) as Rule[];
        const { rulesMessageJoinLabel: globalRulesMessageJoinLabel } = props.formProps;

        const joinLabel = Reflect.has(props.schema, 'rulesMessageJoinLabel')
          ? rulesMessageJoinLabel
          : globalRulesMessageJoinLabel;
        const defaultMsg = createPlaceholderMessage(component) + `${joinLabel ? label : ''}`;

        function validator(rule: any, value: any) {
          const msg = rule.message || defaultMsg;
          if (value === undefined || isNull(value)) {
            // 空值
            return Promise.reject(msg);
          } else if (Array.isArray(value) && value.length === 0) {
            // 数组类型
            return Promise.reject(msg);
          } else if (typeof value === 'string' && value.trim() === '') {
            // 空字符串
            return Promise.reject(msg);
          } else if (
            typeof value === 'object' &&
            Reflect.has(value, 'checked') &&
            Reflect.has(value, 'halfChecked') &&
            Array.isArray(value.checked) &&
            Array.isArray(value.halfChecked) &&
            value.checked.length === 0 &&
            value.halfChecked.length === 0
          ) {
            // 非关联选择的tree组件
            return Promise.reject(msg);
          }
          return Promise.resolve();
        }

        const getRequired = isFunction(required) ? required(unref(getValues)) : required;

        if ((!rules || rules.length === 0) && getRequired) {
          rules = [{ required: getRequired, validator }];
        }

        const requiredRuleIndex: number = rules.findIndex(
          (rule) => Reflect.has(rule, 'required') && !Reflect.has(rule, 'validator'),
        );

        if (requiredRuleIndex !== -1) {
          const rule = rules[requiredRuleIndex];
          const { isShow } = getShow();
          if (!isShow) {
            rule.required = false;
          }
          // rules: [{ required: true }] 时的验证应与直接写 required: true 的验证相同
          if (rule.required && !rule.validator) {
            rule.validator = validator;
          }
          if (component) {
            if (!Reflect.has(rule, 'type')) {
              rule.type = component === 'InputNumber' ? 'number' : 'string';
            }

            rule.message = rule.message || defaultMsg;

            if (component.includes('Input') || component.includes('Textarea')) {
              rule.whitespace = true;
            }
            const valueFormat = unref(getComponentsProps)?.valueFormat;
            setComponentRuleType(rule, component, valueFormat);
          }
        }

        // Maximum input length rule check
        const characterInx = rules.findIndex((val) => val.max);
        if (characterInx !== -1 && !rules[characterInx].validator) {
          rules[characterInx].message =
            rules[characterInx].message ||
            t('component.form.maxTip', [rules[characterInx].max] as Recordable);
        }
        return rules;
      }

      function renderComponent() {
        const {
          renderComponentContent,
          component,
          field,
          fieldLabel,
          changeEvent = 'change',
          valueField,
          labelField,
          defaultValue,
          defaultLabel,
        } = props.schema;

        const isCheck = component && ['Switch', 'Checkbox'].includes(component);

        const eventKey = `on${upperFirst(changeEvent)}`;

        const on = {
          [eventKey]: (...args: Nullable<Recordable>[]) => {
            // console.log('event', eventKey, ...args);
            const [e, labelValue] = args;
            const target = typeof e === 'boolean' ? { checked: e } : e ? e.target : null;
            const value = target ? (isCheck ? target.checked : target.value) : e;
            props.setFormModel(
              field,
              value || (typeof value == 'number' ? value : defaultValue || ''),
              fieldLabel,
              labelValue || defaultLabel || '',
            );
            if (propsData[eventKey]) {
              propsData[eventKey](...args);
            }
          },
        };
        const Comp = componentMap.get(component) as ReturnType<typeof defineComponent>;

        const { autoSetPlaceHolder, size } = props.formProps;
        const propsData: Recordable = {
          // allowClear: true, // 默认不显示清除按钮
          getPopupContainer: () => document.body, //(trigger: Element) => trigger.parentNode,
          size,
          ...unref(getComponentsProps),
          disabled: unref(getDisable),
        };

        const isCreatePlaceholder = !propsData.disabled && autoSetPlaceHolder;
        // RangePicker place is an array
        if (isCreatePlaceholder && component && component !== 'RangePicker') {
          propsData.placeholder =
            unref(getComponentsProps)?.placeholder || createPlaceholderMessage(component);
        }
        propsData.codeField = field;
        propsData.formValues = unref(getValues);

        if (defaultValue && !props.formModel[field]) {
          if (defaultLabel && fieldLabel && !props.formModel[fieldLabel]) {
            props.setFormModel(field, defaultValue, defaultLabel);
          } else {
            props.setFormModel(field, defaultValue);
          }
        }

        let value = props.formModel[field];
        if (component === 'RangePicker' && isString(value)) {
          const vs = value.split(',');
          if (vs.length > 1) value = [vs[0], vs[1]];
          else value = [vs[0], ''];
        }

        const bindValue: Recordable = {
          [valueField || (isCheck ? 'checked' : 'value')]: value ?? defaultValue,
        };

        if (fieldLabel) {
          bindValue[labelField || 'labelValue'] = props.formModel[fieldLabel] ?? defaultLabel;
          // console.log('bindValue', bindValue, props.formModel);
          bindValue.labelInValue = true;
          // bindValue.treeCheckable = true;
        }

        const compAttr: Recordable = {
          ...propsData,
          ...on,
          ...bindValue,
        };

        if (!renderComponentContent) {
          return <Comp {...compAttr} />;
        }
        const compSlot = isFunction(renderComponentContent)
          ? { ...renderComponentContent(unref(getValues)) }
          : {
              default: () => renderComponentContent,
            };
        return <Comp {...compAttr}>{compSlot}</Comp>;
      }

      function renderLabelHelpMessage(colon = false) {
        const { label, helpMessage, helpComponentProps, subLabel } = props.schema;
        if (!label) return;
        const colonText = colon && props.formProps.colon && label != '　' ? '：' : '';
        const renderLabel = subLabel ? (
          <span>
            {label} <span class="text-secondary">{subLabel}</span> {colonText}
          </span>
        ) : (
          label + colonText
        );
        const getHelpMessage = isFunction(helpMessage)
          ? helpMessage(unref(getValues))
          : helpMessage;
        if (!getHelpMessage || (Array.isArray(getHelpMessage) && getHelpMessage.length === 0)) {
          return renderLabel;
        }
        return (
          <span>
            {renderLabel}
            <BasicHelp placement="top" text={getHelpMessage} {...helpComponentProps} />
          </span>
        );
      }

      function renderItem() {
        const { itemProps, slot, render, label, field, fieldLabel, suffix, component } =
          props.schema;
        const { labelCol, wrapperCol } = unref(itemLabelWidthProp);

        if (component === 'None') {
          return ''; // 占位符，什么也不输出
        } else if (component === 'Divider' || component === 'FormGroup') {
          const Comp = componentMap.get(component) as ReturnType<typeof defineComponent>;
          return (
            <Col span={24}>
              <Comp {...unref(getComponentsProps)}>{renderLabelHelpMessage()}</Comp>
            </Col>
          );
        } else {
          const getContent = () => {
            return slot
              ? getSlot(slots, slot, unref(getValues))
              : render
                ? render(unref(getValues))
                : renderComponent();
          };

          const showSuffix = !!suffix;
          const getSuffix = isFunction(suffix) ? suffix(unref(getValues)) : suffix;

          return (
            <Form.Item
              name={field}
              colon={false}
              class={{ 'suffix-item': showSuffix, 'no-label': isEmpty(label) }}
              {...(itemProps as Recordable)}
              label={renderLabelHelpMessage(true)}
              rules={handleRules()}
              labelCol={labelCol}
              wrapperCol={wrapperCol}
            >
              {showSuffix ? (
                <div style="display: flex">
                  <div style="flex: 1">{getContent()}</div>
                  <span class="suffix">{getSuffix}</span>
                </div>
              ) : (
                getContent()
              )}
              {fieldLabel ? <Form.Item name={fieldLabel} v-show={false} /> : null}
            </Form.Item>
          );
        }
      }

      return () => {
        const { colProps = {}, colSlot, renderColContent, component } = props.schema;
        if (!componentMap.has(component)) {
          return null;
        }

        const { baseColProps = {} } = props.formProps;
        const realColProps = props.colLayout ? { ...baseColProps, ...colProps } : {};
        const { isIfShow, isShow } = getShow();
        const values = unref(getValues);

        const getContent = () => {
          return colSlot
            ? getSlot(slots, colSlot, values)
            : renderColContent
              ? renderColContent(values)
              : renderItem();
        };

        return (
          isIfShow && (
            <Col {...realColProps} v-show={isShow}>
              {getContent()}
            </Col>
          )
        );
      };
    },
  });
</script>
