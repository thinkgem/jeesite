<!--
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 * No deletion without permission, or be held responsible to law.
 * @description 支持字典类型
 * @author ThinkGem
-->
<template>
  <CheckboxGroup v-bind="getAttrs" v-model:value="state">
    <template v-for="item in getOptions" :key="`${item.value}`">
      <Checkbox :value="item.value" :disabled="item.disabled">
        {{ item.label }}
      </Checkbox>
    </template>
  </CheckboxGroup>
</template>
<script lang="ts">
  import { defineComponent, PropType, computed, ref, unref, watch } from 'vue';
  import { Checkbox } from 'ant-design-vue';
  import { isEmpty, isString } from '/@/utils/is';
  import { propTypes } from '/@/utils/propTypes';
  import { useRuleFormItem } from '/@/hooks/component/useFormItem';
  import { useAttrs } from '/@/hooks/core/useAttrs';
  import { useDict } from '/@/components/Dict';

  type OptionsItem = { label: string; value: string | number | boolean; disabled?: boolean };
  type CheckboxItem = string | OptionsItem;

  export default defineComponent({
    name: 'JeeSiteCheckboxGroup',
    components: { CheckboxGroup: Checkbox.Group, Checkbox },
    inheritAttrs: false,
    props: {
      value: {
        type: [Array, Object, String, Number] as PropType<Array<any> | object | string | number>,
      },
      options: {
        type: Array as PropType<CheckboxItem[]>,
        default: () => [],
      },
      dictType: propTypes.string,
    },
    setup(props) {
      const attrs = useAttrs();
      const [state] = useRuleFormItem(props);
      const optionsRef = ref<CheckboxItem[]>(props.options);

      const getAttrs = computed(() => {
        return { ...unref(attrs), ...(props as Recordable) };
      });

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

      return { getAttrs, state, getOptions };
    },
  });
</script>
<style lang="less">
  .ant-checkbox-wrapper {
    margin-right: 0;
    margin-left: 4px;
  }

  .ant-checkbox-checked {
    .ant-checkbox-inner {
      opacity: 0.9;
    }
  }
</style>
