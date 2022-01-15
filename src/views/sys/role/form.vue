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
      <template #menuTrees>
        <div class="flex flex-row flex-wrap">
          <div class="mb-5 ml-5" v-for="item in sysCodeRef" :key="item.id">
            <BasicTree
              class="bg-gray"
              style="width: 500px"
              :title="item.name"
              :toolbar="true"
              :checkable="true"
              :immediate="false"
              :defaultExpandLevel="2"
              :ref="setTreeRefs(item.value)"
            />
          </div>
        </div>
      </template>
    </BasicForm>
  </BasicDrawer>
</template>
<script lang="ts">
  export default defineComponent({
    name: 'ViewsSysRoleForm',
  });
</script>
<script lang="ts" setup>
  import { defineComponent, ref, computed } from 'vue';
  import { useI18n } from '/@/hooks/web/useI18n';
  import { useMessage } from '/@/hooks/web/useMessage';
  import { router } from '/@/router';
  import { Icon } from '/@/components/Icon';
  import { useDict } from '/@/components/Dict';
  import { BasicTree, TreeActionType } from '/@/components/Tree';
  import { BasicForm, FormSchema, useForm } from '/@/components/Form';
  import { BasicDrawer, useDrawerInner } from '/@/components/Drawer';
  import { roleSave, checkRoleName, roleForm, menuTreeData } from '/@/api/sys/role';

  const emit = defineEmits(['success', 'register']);

  const { t } = useI18n('sys.role');
  const { showMessage } = useMessage();
  const record = ref<Recordable>({});
  const getTitle = computed(() => ({
    icon: router.currentRoute.value.meta.icon || 'ant-design:book-outlined',
    value: record.value.isNewRecord ? t('新增角色') : t('编辑角色'),
  }));
  const op = ref<String>('');
  const sysCodeRef = ref<Array<Recordable>>([]);

  const inputFormSchemas: FormSchema[] = [
    {
      label: t('角色名称'),
      field: 'roleName',
      component: 'Input',
      componentProps: {
        maxlength: 100,
      },
      rules: [
        { required: true },
        { pattern: /^[\u0391-\uFFE5\w]+$/, message: t('不能输入特殊字符') },
        {
          validator(_rule, value) {
            return new Promise((resolve, reject) => {
              if (!value || value === '') return resolve();
              checkRoleName(record.value.roleName || '', value)
                .then((res) => (res ? resolve() : reject(t('角色名称已存在'))))
                .catch((err) => reject(err.message || t('验证失败')));
            });
          },
        },
      ],
    },
    {
      label: t('角色编码'),
      field: 'roleCode',
      component: 'Input',
      componentProps: {
        maxlength: 500,
      },
      required: true,
      ifShow: () => !record.value.isNewRecord,
    },
    {
      label: t('角色代码'),
      field: 'viewCode',
      component: 'Input',
      componentProps: {
        maxlength: 500,
      },
      required: true,
      ifShow: () => record.value.isNewRecord,
    },
    {
      label: t('排序'),
      field: 'roleSort',
      helpMessage: '升序',
      component: 'InputNumber',
      defaultValue: '30',
      componentProps: {
        maxlength: 10,
      },
      required: true,
      ifShow: () => op.value === 'add' || op.value === 'edit',
    },
    {
      label: t('角色分类'),
      field: 'roleType',
      component: 'Select',
      componentProps: {
        dictType: 'sys_role_type',
        allowClear: true,
      },
      ifShow: () => op.value === 'add' || op.value === 'edit',
    },
    {
      label: t('系统角色'),
      field: 'isSys',
      component: 'RadioGroup',
      defaultValue: '1',
      componentProps: {
        dictType: 'sys_yes_no',
        allowClear: true,
      },
      ifShow: () => op.value === 'add' || op.value === 'edit',
    },
    {
      label: t('用户类型'),
      field: 'userType',
      component: 'Select',
      componentProps: {
        dictType: 'sys_user_type',
        allowClear: true,
      },
      ifShow: () => op.value === 'add' || op.value === 'edit',
    },
    {
      label: t('备注信息'),
      field: 'remarks',
      component: 'InputTextArea',
      componentProps: {
        maxlength: 500,
      },
      colProps: { lg: 24, md: 24 },
      ifShow: () => op.value === 'add' || op.value === 'edit',
    },
    {
      label: t('授权功能菜单'),
      field: 'authMenuInfo',
      component: 'FormGroup',
      colProps: { lg: 24, md: 24 },
      ifShow: () => op.value === 'add' || op.value === 'auth',
    },
    {
      label: '',
      field: 'roleMenuListJson',
      component: 'Input',
      colProps: { lg: 24, md: 24 },
      slot: 'menuTrees',
      ifShow: () => op.value === 'add' || op.value === 'auth',
    },
  ];

  const treeRefs: Recordable<TreeActionType> = {};
  const setTreeRefs = (key: string) => (el: any) => {
    if (el) treeRefs[key] = el;
  };

  const [registerForm, { resetFields, setFieldsValue, updateSchema, validate }] = useForm({
    labelWidth: 120,
    schemas: inputFormSchemas,
    baseColProps: { lg: 12, md: 24 },
  });

  const [registerDrawer, { setDrawerProps, closeDrawer }] = useDrawerInner(async (data) => {
    resetFields();
    setDrawerProps({ loading: true });
    op.value = data.op || 'add';
    const res = await roleForm(data);
    record.value = (res.role || {}) as Recordable;
    setFieldsValue(record.value);
    updateSchema([
      {
        field: 'roleCode',
        componentProps: {
          disabled: !record.value.isNewRecord,
        },
      },
      {
        field: 'roleName',
        componentProps: {
          disabled: op.value === 'auth',
        },
      },
    ]);
    if (op.value === 'add' || op.value === 'auth') {
      await loadSysCode();
      await loadTreeDatas();
    }
    setDrawerProps({ loading: false });
  });

  async function loadSysCode() {
    const { initDict, getDictList } = useDict();
    const dictType = 'sys_menu_sys_code';
    await initDict([dictType]);
    sysCodeRef.value = getDictList(dictType);
  }

  async function loadTreeDatas() {
    const res = await menuTreeData({ roleCode: record.value.roleCode });
    const checkedKeys = {};
    (res.roleMenuList || []).forEach((item) => {
      if (!checkedKeys[item.sysCode]) {
        checkedKeys[item.sysCode] = [];
      }
      checkedKeys[item.sysCode].push(item.id);
    });
    for (const key in res.menuMap) {
      treeRefs[key].setTreeData(res.menuMap[key]);
      treeRefs[key].setCheckedKeys(checkedKeys[key]);
    }
  }

  function getRoleMenuListJson() {
    const keys = Object.keys(treeRefs);
    let menuData: Array<any> = [];
    for (const key of keys) {
      const ks = treeRefs[key].getCheckedKeys();
      for (const k of ks as Array<any>) {
        menuData.push(k);
      }
    }
    return JSON.stringify(menuData);
  }

  async function handleSubmit() {
    try {
      const data = await validate();
      setDrawerProps({ confirmLoading: true });
      const params: any = {
        isNewRecord: record.value.isNewRecord,
        roleCode: record.value.roleCode,
        op: op.value,
      };
      if (!data.viewCode) {
        data.viewCode = record.value.viewCode;
      }
      if (op.value === 'add' || op.value === 'auth') {
        data.roleMenuListJson = getRoleMenuListJson();
      }
      // console.log('submit', params, data, record);
      const res = await roleSave(params, data);
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
