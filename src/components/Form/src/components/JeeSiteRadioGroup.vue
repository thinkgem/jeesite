<!--
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 * No deletion without permission, or be held responsible to law.
 * @description 支持字典类型
 * @author ThinkGem
-->
<template>
  <RadioGroup v-bind="attrs" v-model:value="state">
    <template v-for="item in getOptions" :key="`${item.value}`">
      <Radio :value="item.value" :disabled="item.disabled">
        {{ item.label }}
      </Radio>
    </template>
  </RadioGroup>
</template>
<script lang="ts">
  import { defineComponent, PropType, computed, ref, unref, watch } from 'vue';
  import { Radio } from 'ant-design-vue';
  import { isEmpty, isString } from '/@/utils/is';
  import { propTypes } from '/@/utils/propTypes';
  import { useRuleFormItem } from '/@/hooks/component/useFormItem';
  import { useAttrs } from '/@/hooks/core/useAttrs';
  import { useDict } from '/@/components/Dict';

  type OptionsItem = { label: string; value: string | number | boolean; disabled?: boolean };
  type RadioItem = string | OptionsItem;

  export default defineComponent({
    name: 'JeeSiteRadioGroup',
    components: { RadioGroup: Radio.Group, Radio },
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
<style lang="less">
  .ant-radio-wrapper {
    margin-right: 0;
    margin-left: 4px;
  }

  .ant-radio-checked {
    .ant-radio-inner {
      opacity: 0.8;
    }
  }
</style>
