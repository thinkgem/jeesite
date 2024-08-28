<!--
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 * No deletion without permission, or be held responsible to law.
 * @description 支持字典类型、支持下拉框标签返回、支持 API 接口
 * @author Vben、ThinkGem
-->
<template>
  <div class="jeesite-tree-select">
    <TreeSelect
      v-bind="getAttrs"
      v-model:value="state"
      :treeData="treeDataRef"
      @click="handleFetch"
    >
      <template #[item]="data" v-for="item in Object.keys($slots)">
        <slot :name="item" v-bind="data || {}"></slot>
      </template>
      <template #notFoundContent v-if="loading">
        <span>
          <LoadingOutlined spin class="mr-1" />
          {{ t('component.form.apiSelectNotFound') }}
        </span>
      </template>
    </TreeSelect>
  </div>
</template>
<script lang="ts">
  import { defineComponent, ref, unref, computed, watch, onMounted } from 'vue';
  import { TreeSelect } from 'ant-design-vue';
  import { isEmpty, isFunction } from '/@/utils/is';
  import { propTypes } from '/@/utils/propTypes';
  import { listToTree } from '/@/utils/helper/treeHelper';
  import { useAttrs } from '/@/hooks/core/useAttrs';
  import { useRuleFormItem } from '/@/hooks/component/useFormItem';
  import { get, omit } from 'lodash-es';
  import { LoadingOutlined } from '@ant-design/icons-vue';
  import { useI18n } from '/@/hooks/web/useI18n';
  import { useDict } from '/@/components/Dict';
  import { TreeItem } from '/@/components/Tree';

  const props: any = {
    value: {
      type: [Array, Object, String, Number] as PropType<Array<any> | object | string | number>,
    },
    labelValue: {
      type: [Array, Object, String, Number] as PropType<Array<any> | object | string | number>,
    },
    labelInValue: propTypes.bool,
    treeData: {
      type: Array as PropType<Recordable[] | TreeItem[]>,
      default: () => [],
    },
    api: {
      type: Function as PropType<(arg?: Recordable) => Promise<Recordable[] | TreeItem[]>>,
      default: null,
    },
    params: {
      type: Object as PropType<Recordable>,
      default: () => ({}),
    },
    isDisable: {
      type: Function as PropType<(node: Recordable) => boolean>,
      default: null,
    },
    resultField: propTypes.string.def(''),
    immediate: propTypes.bool.def(false),
    dictType: propTypes.string,
    treeCheckable: propTypes.bool,
    treeDataSimpleMode: propTypes.bool.def(true),
    canSelectParent: propTypes.bool.def(true),
    // 是否返回全路径，包含所有上级信息，以 returnFullNameSplit 参数分隔
    returnFullName: propTypes.bool.def(false),
    // 是否返回全路径，的分隔符，默认“/”
    returnFullNameSplit: propTypes.string.def('/'),
  };

  export default defineComponent({
    name: 'JeeSiteTreeSelect',
    components: { TreeSelect, LoadingOutlined },
    // inheritAttrs: false,
    props,
    emits: ['change', 'update:value', 'update:labelValue', 'options-change', 'click'],
    setup(props: any, { emit }) {
      const { t } = useI18n();
      const attrs = useAttrs();
      const treeDataRef = ref<Recordable[]>(props.treeData);
      const isFirstLoad = ref<Boolean>(false);
      const loading = ref<Boolean>(false);

      const getAttrs = computed(() => {
        let propsData = {
          virtual: false,
          showSearch: true,
          treeNodeFilterProp: 'name',
          fieldNames: {
            value: props.dictType ? 'value' : 'id',
            label: 'name',
          },
          treeDataSimpleMode: false,
          dropdownStyle: { maxHeight: '300px' },
          popupClassName: 'jeesite-tree-select-popup',
          getPopupContainer: () => document.body,
          ...unref(attrs),
          ...(props as Recordable),
        } as any;
        if (props.returnFullName) {
          propsData.treeNodeLabelProp = '_fullName';
        }
        return omit(propsData, 'treeData');
      });

      const [state] = useRuleFormItem(props);

      if (!isEmpty(props.dictType)) {
        const { initSelectTreeData } = useDict();
        initSelectTreeData(treeDataRef, props.dictType, true);
      }

      watch(
        () => props.treeData,
        () => {
          treeDataRef.value = getTreeData(props.treeData);
          emit('options-change', unref(treeDataRef));
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
        if (props.treeData && props.treeData.length > 0) {
          treeDataRef.value = getTreeData(props.treeData);
        }
        if (props.immediate) {
          await fetch();
          isFirstLoad.value = true;
        }
      });

      async function fetch() {
        const { api } = props;
        if (!api || !isFunction(api)) return;
        treeDataRef.value = [];
        try {
          loading.value = true;
          let res = await api(props.params);
          if (props.resultField) {
            res = get(res, props.resultField) || [];
          }
          if (Array.isArray(res)) {
            treeDataRef.value = getTreeData(res);
          }
          emit('options-change', unref(treeDataRef));
        } catch (error) {
          console.warn(error);
        } finally {
          loading.value = false;
        }
      }

      function getTreeData(treeData: Recordable[]) {
        if (props.treeDataSimpleMode) {
          return listToTree(treeData, {
            callback: (parent, node) => {
              if (props.isDisable && node) {
                node.disabled = props.isDisable(node);
              }
              if (!props.canSelectParent && parent) {
                if (parent.children && parent.children.length > 0) {
                  parent.disabled = true;
                }
              }
            },
            fullNameSplit: props.returnFullNameSplit,
          });
        }
        return treeData;
      }

      async function handleFetch() {
        if (!props.immediate && !unref(isFirstLoad)) {
          await fetch();
          isFirstLoad.value = true;
        }
        emit('click');
      }

      return { t, getAttrs, state, treeDataRef, loading, handleFetch };
    },
  });
</script>
<style lang="less">
  @prefix-cls: ~'jeesite-tree-select';

  .@{prefix-cls} {
    width: 100%;

    .ant-select {
      width: 100%;
    }
  }

  .ant-select-dropdown.@{prefix-cls}-popup .ant-select-tree {
    .ant-select-tree-switcher {
      // &.ant-select-tree-switcher-noop {
      //   width: 15px;
      // }
      .ant-select-tree-switcher-icon svg {
        margin-top: -3px;
      }
    }
  }
</style>
