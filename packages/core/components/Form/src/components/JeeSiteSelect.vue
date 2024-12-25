<!--
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 * No deletion without permission, or be held responsible to law.
 * @description 支持字典类型、支持下拉框标签返回、支持 API 接口
 * @author Vben、ThinkGem
-->
<template>
  <div class="jeesite-select">
    <Select v-bind="getAttrs" v-model:value="state" :options="optionsRef" @click="handleFetch">
      <template #[item]="data" v-for="item in Object.keys($slots)">
        <slot :name="item" v-bind="data || {}"></slot>
      </template>
      <template #suffixIcon v-if="loading">
        <LoadingOutlined spin />
      </template>
      <template #notFoundContent v-if="loading">
        <span>
          <LoadingOutlined spin class="mr-1" />
          {{ t('component.form.apiSelectNotFound') }}
        </span>
      </template>
    </Select>
  </div>
</template>
<script lang="ts" setup name="JeeSiteSelect">
  import { ref, unref, computed, watch, onMounted } from 'vue';
  import { Select } from 'ant-design-vue';
  import { isEmpty, isFunction } from '@jeesite/core/utils/is';
  import { propTypes } from '@jeesite/core/utils/propTypes';
  import { useAttrs } from '@jeesite/core/hooks/core/useAttrs';
  import { useRuleFormItem } from '@jeesite/core/hooks/component/useFormItem';
  import { get } from 'lodash-es';
  import { LoadingOutlined } from '@ant-design/icons-vue';
  import { useI18n } from '@jeesite/core/hooks/web/useI18n';
  import { useDict } from '@jeesite/core/components/Dict';

  type OptionsItem = { label: string; value: string; disabled?: boolean };

  const props = defineProps({
    value: {
      type: [Array, Object, String, Number] as PropType<Array<any> | object | string | number>,
    },
    labelValue: {
      type: [Array, Object, String, Number] as PropType<Array<any> | object | string | number>,
    },
    labelInValue: propTypes.bool,
    options: {
      type: Array as PropType<Recordable[] | OptionsItem[]>,
      default: () => [],
    },
    api: {
      type: Function as PropType<(arg?: Recordable) => Promise<Recordable[] | OptionsItem[]>>,
      default: null,
    },
    params: {
      type: Object as PropType<Recordable>,
      default: () => ({}),
    },
    resultField: propTypes.string.def(''),
    immediate: propTypes.bool.def(false),
    each: propTypes.bool.def(false),
    dictType: propTypes.string,
    mode: propTypes.string,
  });

  const emit = defineEmits([
    'change',
    'update:value',
    'update:labelValue',
    'options-change',
    'click',
  ]);

  const { t } = useI18n();
  const attrs = useAttrs();
  const [state] = useRuleFormItem(props);
  const optionsRef = ref<Recordable[]>(props.options);
  const isFirstLoad = ref<boolean>(false);
  const loading = ref<boolean>(false);

  const getAttrs = computed(() => {
    return {
      showSearch: true,
      optionFilterProp: 'label',
      fieldNames: {
        value: 'value',
        label: 'label',
      },
      ...unref(attrs),
      ...(props as Recordable),
    };
  });

  if (!isEmpty(props.dictType)) {
    const { initSelectOptions } = useDict();
    initSelectOptions(optionsRef, props.dictType);
  }

  watch(
    () => props.options,
    () => {
      optionsRef.value = props.options;
      emit('options-change', unref(optionsRef));
    },
  );

  watch(
    () => props.params,
    () => {
      isFirstLoad.value && fetch();
    },
    { deep: true },
  );

  watch(
    () => props.immediate,
    (v) => {
      v && !isFirstLoad.value && fetch();
    },
  );

  watch(
    () => optionsRef.value,
    () => {
      // 如果没有给初始值，并不允许清空选择项和非多选的时候，默认选择第一个选项
      if (
        !state.value &&
        !getAttrs.value.allowClear &&
        getAttrs.value.mode != 'multiple' &&
        optionsRef.value.length > 0
      ) {
        const fieldNames = getAttrs.value.fieldNames;
        const firstValue = optionsRef.value[0];
        if (props.labelInValue) {
          state.value = {
            label: firstValue[fieldNames.label],
            value: firstValue[fieldNames.value],
          };
        } else {
          state.value = firstValue[fieldNames.value];
        }
      }
    },
  );

  onMounted(async () => {
    if (props.options && props.options.length > 0) {
      optionsRef.value = props.options;
    }
    if (props.immediate) {
      await fetch();
      isFirstLoad.value = true;
    }
  });

  async function fetch() {
    const { api } = props;
    if (!api || !isFunction(api)) return;
    optionsRef.value = [];
    try {
      loading.value = true;
      let res = await api(props.params);
      if (props.resultField) {
        res = get(res, props.resultField) || [];
      }
      if (Array.isArray(res)) {
        optionsRef.value = res;
      }
      emit('options-change', unref(optionsRef));
    } catch (error) {
      console.warn(error);
    } finally {
      loading.value = false;
    }
  }

  async function handleFetch() {
    if (!props.immediate && !unref(isFirstLoad)) {
      await fetch();
      isFirstLoad.value = true;
    } else if (props.each) {
      await fetch();
    }
    emit('click');
  }
</script>
<style lang="less">
  @prefix-cls: ~'jeesite-select';

  .@{prefix-cls} {
    width: 100%;

    .ant-select {
      width: 100%;
    }
  }
  // 嵌入在 input 内部 addonAfter 插槽时的样式设置
  .ant-input-group .ant-input-group-addon .@{prefix-cls} {
    margin: -5px -11px;
  }
</style>
