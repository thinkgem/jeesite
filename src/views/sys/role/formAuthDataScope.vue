<!--
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 * No deletion without permission, or be held responsible to law.
 * @author ThinkGem
-->
<template>
  <BasicDrawer
    v-bind="$attrs"
    :showFooter="true"
    :okAuth="'sys:role:edit'"
    @register="registerDrawer"
    @ok="handleSubmit"
    width="80%"
  >
    <template #title>
      <Icon :icon="getTitle.icon" class="pr-1 m-1" />
      <span> {{ getTitle.value }} </span>
    </template>
    <BasicForm @register="registerForm">
      <template #dataScopeTrees>
        <div class="flex flex-row flex-wrap">
          <div class="mr-5 mb-5" v-for="item in dataScopes" :key="item.moduleCode">
            <BasicTree
              v-if="moduleCodes.includes(item.moduleCode) && ['0', '1'].includes(item.ctrlPermi)"
              class="bg-gray"
              style="min-width: 300px"
              :title="t(item['ctrlName' + localeStore.getLocale] || item.ctrlName)"
              :toolbar="true"
              :checkable="true"
              :api="ctrlDataTreeData"
              :params="{ url: item.ctrlDataUrl, ctrlPermi: '2' }"
              :immediate="immediate"
              :defaultExpandLevel="2"
              :ref="setTreeRefs(item.ctrlType)"
              @tree-data-change="handleTreeDataChange"
            />
          </div>
        </div>
      </template>
    </BasicForm>
  </BasicDrawer>
</template>
<script lang="ts">
  export default defineComponent({
    name: 'ViewsSysRoleAuthDataScope',
  });
</script>
<script lang="ts" setup>
  import { defineComponent, ref, nextTick } from 'vue';
  import { useI18n } from '/@/hooks/web/useI18n';
  import { useMessage } from '/@/hooks/web/useMessage';
  import { useLocaleStore } from '/@/store/modules/locale';
  import { router } from '/@/router';
  import { Icon } from '/@/components/Icon';
  import { BasicTree, TreeActionType } from '/@/components/Tree';
  import { BasicForm, FormSchema, useForm } from '/@/components/Form';
  import { BasicDrawer, useDrawerInner } from '/@/components/Drawer';
  import { formAuthDataScope, ctrlDataTreeData, saveAuthDataScope } from '/@/api/sys/role';

  const emit = defineEmits(['success', 'register']);

  const { t } = useI18n('sys.role');
  const { showMessage } = useMessage();
  const localeStore = useLocaleStore();
  const record = ref<Recordable>({});
  const getTitle = {
    icon: router.currentRoute.value.meta.icon || 'ant-design:book-outlined',
    value: t('数据权限'),
  };
  const moduleCodes = ref<Array<String>>([]);
  const dataScopes = ref<Array<Recordable>>([]);
  const roleDataScopeList = ref<Array<Recordable>>([]);
  const immediate = ref(false);

  const inputFormSchemas: FormSchema[] = [
    {
      label: t('角色名称'),
      field: 'roleName',
      component: 'Input',
      componentProps: {
        disabled: true,
      },
    },
    {
      label: t('角色编码'),
      field: 'roleCode',
      component: 'Input',
      componentProps: {
        disabled: true,
      },
    },
    {
      label: t('数据范围'),
      field: 'dataScope',
      component: 'RadioGroup',
      componentProps: {
        dictType: 'sys_role_data_scope',
        allowClear: true,
      },
      colProps: { lg: 24, md: 24 },
    },
    {
      label: t('业务范围'),
      field: 'bizScope',
      component: 'Select',
      componentProps: {
        dictType: 'sys_role_biz_scope',
        allowClear: true,
        mode: 'multiple',
      },
      colProps: { lg: 24, md: 24 },
    },
    {
      label: t('授权数据权限'),
      field: 'authDataScopeInfo',
      component: 'FormGroup',
      colProps: { lg: 24, md: 24 },
      show: ({ values }) => values.dataScope === '2',
    },
    {
      field: 'roleDataScopeListJson',
      component: 'Input',
      colProps: { lg: 24, md: 24 },
      slot: 'dataScopeTrees',
      show: ({ values }) => values.dataScope === '2',
    },
  ];

  const treeRefs: Recordable<TreeActionType> = {};
  const setTreeRefs = (key: string) => (el: any) => {
    if (el) treeRefs[key] = el;
  };

  const [registerForm, { resetFields, setFieldsValue, validate }] = useForm({
    labelWidth: 120,
    schemas: inputFormSchemas,
    baseColProps: { lg: 12, md: 24 },
  });

  const [registerDrawer, { setDrawerProps, closeDrawer }] = useDrawerInner(async (data) => {
    setDrawerProps({ loading: true });
    await resetFields();
    const res = await formAuthDataScope(data);
    record.value = (res.role || {}) as Recordable;
    moduleCodes.value = res.moduleCodes || [];
    dataScopes.value = res.dataScopes || [];
    roleDataScopeList.value = res.roleDataScopeList || [];
    setFieldsValue(record.value);
    await loadTreeDatas();
    setDrawerProps({ loading: false });
  });

  let loadTreeDataNum: number;
  async function loadTreeDatas() {
    loadTreeDataNum = 0;
    nextTick(() => {
      if (immediate.value) {
        for (const key of Object.keys(treeRefs)) {
          treeRefs[key].setCheckedKeys([]);
          treeRefs[key].reload();
        }
      } else {
        immediate.value = true;
      }
    });
  }

  function handleTreeDataChange() {
    const keys = Object.keys(treeRefs);
    if (++loadTreeDataNum == keys.length) {
      let checkedKeys = {};
      roleDataScopeList.value.forEach((item) => {
        if (!checkedKeys[item.ctrlType]) {
          checkedKeys[item.ctrlType] = [];
        }
        checkedKeys[item.ctrlType].push(item.ctrlData);
      });
      for (const key of keys) {
        treeRefs[key].setCheckedKeys(checkedKeys[key] || []);
      }
    }
  }

  function getRoleDataScopeListJson() {
    const keys = Object.keys(treeRefs);
    let dataScopeData: Array<any> = [];
    for (const key of keys) {
      const ks = treeRefs[key].getCheckedKeys();
      for (const k of ks as Array<any>) {
        dataScopeData.push({
          ctrlType: key,
          ctrlData: k,
        });
      }
    }
    return JSON.stringify(dataScopeData);
  }

  async function handleSubmit() {
    try {
      const data = await validate();
      setDrawerProps({ confirmLoading: true });
      const params: any = {
        ...data,
        isNewRecord: record.value.isNewRecord,
        roleCode: record.value.roleCode,
        roleDataScopeListJson: getRoleDataScopeListJson(),
      };
      // console.log('submit', params, data, record);
      const res = await saveAuthDataScope(params);
      showMessage(res.message);
      setTimeout(closeDrawer);
      emit('success', data);
    } catch (error: any) {
      if (error && error.errorFields) {
        showMessage(t('您填写的信息有误，请根据提示修正。'));
      }
      console.log('error', error);
    } finally {
      setDrawerProps({ confirmLoading: false });
    }
  }
</script>
