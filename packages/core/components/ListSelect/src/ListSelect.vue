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
<script lang="ts" setup name="JeeSiteListSelect">
  import { ref, unref, computed, watch, onMounted, shallowRef } from 'vue';
  import { Input } from 'ant-design-vue';
  import { propTypes } from '@jeesite/core/utils/propTypes';
  import { useAttrs } from '@jeesite/core/hooks/core/useAttrs';

  import { useModal } from '@jeesite/core/components/Modal';
  import { createAsyncComponent } from '@jeesite/core/utils/factory/createAsyncComponent';

  const InputSearch = Input.Search;

  const props = defineProps({
    value: propTypes.string,
    labelValue: propTypes.string,
    selectList: propTypes.array,

    // 选择类型，加载 ./selectType/*.ts 的配置。
    selectType: propTypes.string.def('userSelect'),

    // 配置文件，是 selectType 更自由的设置，不限定路径
    configFile: propTypes.any, // 例如：import('./select'),

    // 请求参数（列表查询默认值）
    queryParams: propTypes.object.def({}),

    // 选择结果或回显数据中的编码和名称属性名（默认使用 selectType 里指定的）
    itemCode: propTypes.string,
    itemName: propTypes.string,

    checkbox: propTypes.bool, // 是否多选
    allowInput: propTypes.bool, // 允许输入
    readonly: propTypes.bool, // 是否只读
  });

  const emit = defineEmits(['change', 'select', 'click']);

  const attrs = useAttrs();
  const valueRef = ref<string>(props.value);
  const labelValueRef = ref<string>(props.labelValue);
  const selectListRef = ref<any[]>(props.selectList);
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

  watch(
    () => props.selectList,
    () => {
      setSelectList(props.selectList);
    },
    { deep: true },
  );

  function setSelectList(selectList: any[]) {
    selectListRef.value = selectList;
    const codes: string[] = [];
    const names: string[] = [];
    selectList &&
      selectList.forEach((e: Recordable) => {
        codes.push(e[itemCode.value]);
        names.push(e[itemName.value]);
      });
    valueRef.value = codes.join(',');
    labelValueRef.value = names.join(',');
  }

  onMounted(async () => {
    if (props.configFile) {
      configRef.value = (await props.configFile).default as any;
    } else {
      configRef.value = (await import(`./selectType/${props.selectType}.ts`)).default as any;
    }
    modalComponent.value = createAsyncComponent(() => import('./ListSelectModal.vue'));
    if (!itemCode.value) {
      itemCode.value = configRef.value.itemCode;
    }
    if (!itemName.value) {
      itemName.value = configRef.value.itemName;
    }
  });

  function openSelectModal() {
    let selectList: Recordable[];
    if (selectListRef.value) {
      selectList = selectListRef.value;
    } else {
      selectList = getSelectList();
    }
    openModal(true, { selectList, queryParams: props.queryParams });
  }

  function handleInputClick() {
    if (!props.readonly && !props.allowInput) {
      openSelectModal();
    }
  }

  async function handleInputSelect() {
    if (!props.readonly) {
      openSelectModal();
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
            '{' + keysToJsonPart(itemCode.value, codesArr[i]) + ',' + keysToJsonPart(itemName.value, namesArr[i]) + '}';
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

  defineExpose({
    openSelectModal,
    setSelectList,
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
