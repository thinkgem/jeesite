<!--
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 * No deletion without permission, or be held responsible to law.
 * @description 支持字典类型
 * @author Vben、ThinkGem
-->
<template>
  <RadioGroup v-bind="attrs" v-model:value="state" button-style="solid">
    <template v-for="item in getOptions" :key="`${item.value}`">
      <RadioButton :value="item.value" :disabled="item.disabled">
        {{ item.label }}
      </RadioButton>
    </template>
  </RadioGroup>
</template>
<script lang="ts">
  import { defineComponent, PropType, computed, ref, unref, watch } from 'vue';
  import { Radio } from 'ant-design-vue';
  import { isEmpty, isString } from '/@/utils/is';
  import { propTypes } from '/@/utils/propTypes';
  import { useAttrs } from '/@/hooks/core/useAttrs';
  import { useRuleFormItem } from '/@/hooks/component/useFormItem';
  import { useDict } from '/@/components/Dict';

  type OptionsItem = { label: string; value: string | number | boolean; disabled?: boolean };
  type RadioItem = string | OptionsItem;

  export default defineComponent({
    name: 'JeeSiteRadioButtonGroup',
    components: { RadioGroup: Radio.Group, RadioButton: Radio.Button },
    inheritAttrs: false,
    props: {
      value: {
        type: [String, Number, Boolean, Object] as PropType<string | number | boolean | Object>,
      },
      options: {
        type: Array as PropType<RadioItem[]>,
        default: () => [],
      },
      dictType: propTypes.string,
    },
    emits: ['change'],
    setup(props) {
      const attrs = useAttrs();
      const [state] = useRuleFormItem(props);
      const optionsRef = ref<RadioItem[]>(props.options);

      if (!isEmpty(props.dictType)) {
        const { initSelectOptions } = useDict();
        initSelectOptions(optionsRef, props.dictType);
      }

      watch(
        () => props.options,
        () => {
          optionsRef.value = props.options;
        },
      );

      const getOptions = computed((): OptionsItem[] => {
        const options = unref(optionsRef);
        if (!options || options?.length === 0) return [];
        const isStringArr = options.some((item) => isString(item));
        if (!isStringArr) return options as OptionsItem[];
        return options.map((item) => ({ label: item, value: item })) as OptionsItem[];
      });

      return { attrs, state, getOptions };
    },
  });
</script>
