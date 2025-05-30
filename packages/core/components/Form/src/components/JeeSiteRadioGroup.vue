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
<script lang="ts" setup name="JeeSiteRadioGroup">
  import { PropType, computed, ref, unref, watch } from 'vue';
  import { Radio } from 'ant-design-vue';
  import { isEmpty, isString } from '@jeesite/core/utils/is';
  import { propTypes } from '@jeesite/core/utils/propTypes';
  import { useRuleFormItem } from '@jeesite/core/hooks/component/useFormItem';
  import { useAttrs } from '@jeesite/core/hooks/core/useAttrs';
  import { useDict } from '@jeesite/core/components/Dict';

  type OptionsItem = { label: string; value: string | number | boolean; disabled?: boolean };
  type RadioItem = string | OptionsItem;

  const RadioGroup = Radio.Group;

  const props = defineProps({
    value: {
      type: [String, Number, Boolean, Object] as PropType<string | number | boolean | object>,
    },
    options: {
      type: Array as PropType<RadioItem[]>,
      default: () => [],
    },
    dictType: propTypes.string,
  });

  const emit = defineEmits(['change', 'update:value']);

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
</script>
<style lang="less">
  .ant-radio-wrapper {
    margin-right: 0;
    margin-left: 4px;
  }

  .ant-radio-checked {
    .ant-radio-inner {
      opacity: 0.9;
    }
  }
</style>
