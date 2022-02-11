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
<script lang="ts">
  import { defineComponent, ref, unref, computed, watch, onMounted } from 'vue';
  import { Select } from 'ant-design-vue';
  import { isEmpty, isFunction } from '/@/utils/is';
  import { propTypes } from '/@/utils/propTypes';
  import { useAttrs } from '/@/hooks/core/useAttrs';
  import { useRuleFormItem } from '/@/hooks/component/useFormItem';
  import { get } from 'lodash-es';
  import { LoadingOutlined } from '@ant-design/icons-vue';
  import { useI18n } from '/@/hooks/web/useI18n';
  import { useDict } from '/@/components/Dict';

  type OptionsItem = { label: string; value: string; disabled?: boolean };

  export default defineComponent({
    name: 'JeeSiteSelect',
    components: { Select, LoadingOutlined },
    // inheritAttrs: false,
    props: {
      value: {
        type: [Array, Object, String, Number] as PropType<Array<any> | object | string | number>,
      },
      labelValue: {
        type: [Array, Object, String, Number] as PropType<Array<any> | object | string | number>,
      },
      labelInValue: propTypes.bool,
      options: {
        type: Array as PropType<OptionsItem[]>,
        default: () => [],
      },
      api: {
        type: Function as PropType<(arg?: Recordable) => Promise<OptionsItem[]>>,
        default: null,
      },
      params: {
        type: Object as PropType<Recordable>,
        default: () => ({}),
      },
      resultField: propTypes.string.def(''),
      immediate: propTypes.bool.def(false),
      dictType: propTypes.string,
      mode: propTypes.string,
    },
    emits: ['options-change', 'change', 'click'],
    setup(props, { emit }) {
      const { t } = useI18n();
      const attrs = useAttrs();
      const optionsRef = ref<OptionsItem[]>(props.options);
      const isFirstLoad = ref<Boolean>(false);
      const loading = ref<Boolean>(false);

      const getAttrs = computed(() => {
        return {
          ...unref(attrs),
          ...(props as Recordable),
        };
      });

      const [state] = useRuleFormItem(props);

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
        const api = props.api;
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
        }
        emit('click');
      }

      return { t, getAttrs, state, optionsRef, loading, handleFetch };
    },
  });
</script>
<style lang="less">
  @prefix-cls: ~'jeesite-select';

  .@{prefix-cls} {
    width: 100%;
  }
</style>
