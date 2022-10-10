<!--
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 * No deletion without permission, or be held responsible to law.
 * @description 列表选择框、表格选择框
 * @author ThinkGem
-->
<template>
  <div class="jeesite-listselect">
    <InputSearch
      v-bind="getAttrs"
      v-model:value="labelValueRef"
      @click="handleInputClick"
      @search="handleInputSelect"
    />
    <component
      :is="modalComponent"
      :config="configRef"
      :checkbox="checkbox"
      @register="registerModal"
      @select="handleSelect"
    />
  </div>
</template>
<script lang="ts">
  import { defineComponent, ref, unref, computed, watch, onMounted, shallowRef } from 'vue';
  import { Input } from 'ant-design-vue';
  import { propTypes } from '/@/utils/propTypes';
  import { useAttrs } from '/@/hooks/core/useAttrs';
  import { useI18n } from '/@/hooks/web/useI18n';

  import { useModal } from '/@/components/Modal';
  import { createAsyncComponent } from '/@/utils/factory/createAsyncComponent';

  export default defineComponent({
    name: 'JeeSiteListSelect',
    components: { InputSearch: Input.Search },
    // inheritAttrs: false,
    props: {
      value: propTypes.string,
      labelValue: propTypes.string,

      // 选择类型，加载 ./selectType/*.ts 的配置。
      selectType: {
        type: String as PropType<string>,
        default: 'userSelect',
      },

      // 选择结果或回显数据中的编码和名称属性名（默认使用 selectType 里指定的）
      itemCode: propTypes.string,
      itemName: propTypes.string,

      checkbox: propTypes.bool, // 是否多选
      allowInput: propTypes.bool, // 允许输入
      readonly: propTypes.bool, // 是否只读
    },
    emits: ['change', 'select', 'click'],
    setup(props, { emit }) {
      const { t } = useI18n();
      const attrs = useAttrs();
      const valueRef = ref<string>(props.value);
      const labelValueRef = ref<string>(props.labelValue);
      const itemCode = ref<string>(props.itemCode);
      const itemName = ref<string>(props.itemName);
      const configRef = ref<any>();
      const modalComponent = shallowRef<Nullable<any>>(null);

      const [registerModal, { openModal }] = useModal();

      const getAttrs = computed(() => {
        return {
          ...unref(attrs),
          ...(props as Recordable),
          readonly: !props.allowInput || props.readonly,
        };
      });

      watch(
        () => props.value,
        () => {
          valueRef.value = props.value;
        },
      );

      watch(
        () => props.labelValue,
        () => {
          labelValueRef.value = props.labelValue;
        },
      );

      onMounted(async () => {
        configRef.value = (await import(`./selectType/${props.selectType}.ts`)).default as any;
        modalComponent.value = createAsyncComponent(() => import('./ListSelectModal.vue'));
        if (!itemCode.value) {
          itemCode.value = configRef.value.itemCode;
        }
        if (!itemName.value) {
          itemName.value = configRef.value.itemName;
        }
      });

      function handleInputClick() {
        if (!props.readonly && !props.allowInput) {
          const selectList = getSelectList();
          openModal(true, { selectList });
        }
      }

      async function handleInputSelect() {
        if (!props.readonly) {
          const selectList = getSelectList();
          openModal(true, { selectList });
        }
        emit('click');
      }

      function keysToJsonPart(key: string, value: string) {
        const num = key.split('.').length - 1;
        let part = key.replace(/\./g, '":{"');
        if (num >= 0) {
          part = '"' + part + '":"' + value + '"';
        }
        for (var i = 0; i < num; i++) {
          part = part + '}';
        }
        return part as string;
      }

      function getSelectList() {
        var selectList: Recordable[] = [];
        const codes = valueRef.value;
        const names = labelValueRef.value;
        if (codes != null && codes != '' && names != null && names != '') {
          const codesArr = codes.split(',') as string[];
          const namesArr = names.split(',') as string[];
          if (codesArr && namesArr && codesArr.length == namesArr.length) {
            for (var i = 0; i < codesArr.length; i++) {
              const json =
                '{' +
                keysToJsonPart(itemCode.value, codesArr[i]) +
                ',' +
                keysToJsonPart(itemName.value, namesArr[i]) +
                '}';
              selectList.push(JSON.parse(json));
            }
          }
        }
        return selectList;
      }

      function handleSelect(values: Recordable[]) {
        valueRef.value = Array.from(values)
          .map((item) => item[configRef.value.itemCode])
          .join(',');
        labelValueRef.value = Array.from(values)
          .map((item) => item[configRef.value.itemName])
          .join(',');
        emit('change', valueRef.value, labelValueRef.value);
        emit('select', values);
      }

      return {
        t,
        getAttrs,
        labelValueRef,
        modalComponent,
        configRef,
        registerModal,
        handleInputClick,
        handleInputSelect,
        handleSelect,
      };
    },
  });
</script>
<style lang="less">
  .jeesite-listselect {
    .ant-input-group {
      .ant-input {
        height: 32px;
      }
    }
  }
</style>
