<!--
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 * No deletion without permission, or be held responsible to law.
 * @author ThinkGem
-->
<template>
  <BasicDrawer
    v-bind="$attrs"
    :showFooter="true"
    :okAuth="'sys:empUser:edit'"
    @register="registerDrawer"
    @ok="handleSubmit"
    width="60%"
  >
    <template #title>
      <Icon :icon="getTitle.icon" class="m-1 pr-1" />
      <span> {{ getTitle.value }} </span>
    </template>
    <BasicForm @register="registerForm">
      <template #dataScopeTrees>
        <div class="flex flex-row flex-wrap">
          <template v-for="item in dataScopes" :key="item.moduleCode">
            <div
              class="mb-5 mr-5"
              v-if="moduleCodes.includes(item.moduleCode) && ['0', '1'].includes(item.ctrlPermi)"
            >
              <BasicTree
                class="bg-gray"
                style="min-width: 300px"
                :title="t(item['ctrlName_' + localeStore.getLocale] || item.ctrlName)"
                :toolbar="true"
                :checkable="true"
                :api="ctrlDataTreeData"
                :params="{ url: item.ctrlDataUrl, ctrlPermi }"
                :immediate="immediate"
                :defaultExpandLevel="2"
                :ref="setTreeRefs(item.ctrlType)"
                @tree-data-change="handleTreeDataChange"
              />
            </div>
          </template>
        </div>
      </template>
    </BasicForm>
  </BasicDrawer>
</template>
<script lang="ts" setup name="ViewsSysEmpUserAuthDataScope">
  import { ref, unref, nextTick } from 'vue';
  import { useI18n } from '/@/hooks/web/useI18n';
  import { useMessage } from '/@/hooks/web/useMessage';
  import { useLocaleStore } from '/@/store/modules/locale';
  import { router } from '/@/router';
  import { Icon } from '/@/components/Icon';
  import { BasicForm, FormSchema, useForm } from '/@/components/Form';
  import { BasicDrawer, useDrawerInner } from '/@/components/Drawer';
  import { formAuthDataScope, ctrlDataTreeData, saveAuthDataScope } from '/@/api/sys/empUser';
  import { BasicTree, TreeActionType } from '/@/components/Tree';

  const emit = defineEmits(['success', 'register']);

  const { t } = useI18n('sys.empUser');
  const { showMessage } = useMessage();
  const { meta } = unref(router.currentRoute);
  const localeStore = useLocaleStore();
  const record = ref<Recordable>({});
  const getTitle = {
    icon: meta.icon || 'ant-design:book-outlined',
    value: t('数据权限'),
  };
  const ctrlPermi = ref<string>('');
  const moduleCodes = ref<Array<string>>([]);
  const dataScopes = ref<Array<Recordable>>([]);
  const userDataScopeList = ref<Array<Recordable>>([]);
  const immediate = ref(false);

  const inputFormSchemas: FormSchema[] = [
    {
      label: t('登录账号'),
      field: 'loginCode',
      component: 'Input',
      componentProps: {
        disabled: true,
      },
    },
    {
      label: t('用户昵称'),
      field: 'userName',
      component: 'Input',
      componentProps: {
        disabled: true,
      },
    },
    {
      label: t('用户分配数据权限'),
      field: 'dataScopeInfo',
      component: 'FormGroup',
      colProps: { md: 24, lg: 24 },
    },
    {
      field: 'userDataScopeListJson',
      component: 'Input',
      colProps: { md: 24, lg: 24 },
      slot: 'dataScopeTrees',
    },
  ];

  const treeRefs: Recordable<TreeActionType> = {};
  const setTreeRefs = (key: string) => (el: any) => {
    if (el) treeRefs[key] = el;
  };

  const [registerForm, { resetFields, setFieldsValue, validate }] = useForm({
    schemas: inputFormSchemas,
    baseColProps: { md: 24, lg: 12 },
    labelWidth: 120,
  });

  const [registerDrawer, { setDrawerProps, closeDrawer }] = useDrawerInner(async (data) => {
    setDrawerProps({ loading: true });
    await resetFields();
    const res = await formAuthDataScope(data);
    record.value = (res.empUser || {}) as Recordable;
    ctrlPermi.value = res.ctrlPermi || '2';
    moduleCodes.value = res.moduleCodes || [];
    dataScopes.value = res.dataScopes || [];
    userDataScopeList.value = res.userDataScopeList || [];
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
      userDataScopeList.value.forEach((item) => {
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

  function getUserDataScopeListJson() {
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
        userCode: record.value.userCode,
        userDataScopeListJson: getUserDataScopeListJson(),
      };
      // console.log('submit', params, data, record);
      const res = await saveAuthDataScope(params);
      showMessage(res.message);
      setTimeout(closeDrawer);
      emit('success', data);
    } catch (error: any) {
      if (error && error.errorFields) {
        showMessage(error.message || t('common.validateError'));
      }
      console.log('error', error);
    } finally {
      setDrawerProps({ confirmLoading: false });
    }
  }
</script>
