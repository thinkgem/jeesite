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
      <Icon :icon="getTitle.icon" class="m-1 pr-1" />
      <span> {{ getTitle.value }} </span>
    </template>
    <BasicForm @register="registerForm">
      <template #menuTrees>
        <div class="flex flex-row flex-wrap">
          <div class="mb-5 mr-5" v-for="item in sysCodeRef" :key="item.id">
            <BasicTree
              v-show="sysCodesRef.length == 0 || sysCodesRef.includes(item.value)"
              class="bg-gray"
              style="width: 500px"
              :title="item.name"
              :toolbar="true"
              :checkable="true"
              :checkStrictly="false"
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
<script lang="ts" setup name="ViewsSysRoleForm">
  import { ref, unref, computed } from 'vue';
  import { useI18n } from '/@/hooks/web/useI18n';
  import { useMessage } from '/@/hooks/web/useMessage';
  import { router } from '/@/router';
  import { Icon } from '/@/components/Icon';
  import { useDict } from '/@/components/Dict';
  import { BasicTree, TreeActionType } from '/@/components/Tree';
  import { BasicForm, FormSchema, useForm } from '/@/components/Form';
  import { BasicDrawer, useDrawerInner } from '/@/components/Drawer';
  import { Role, roleSave, checkRoleName, roleForm, roleMenuTreeData } from '/@/api/sys/role';

  const emit = defineEmits(['success', 'register']);

  const { t } = useI18n('sys.role');
  const { showMessage } = useMessage();
  const { meta } = unref(router.currentRoute);
  const record = ref<Role>({} as Role);
  const getTitle = computed(() => ({
    icon: meta.icon || 'ant-design:book-outlined',
    value: record.value.isNewRecord ? t('新增角色') : t('编辑角色'),
  }));
  const op = ref<string>('');
  const sysCodeRef = ref<Array<Recordable>>([]);
  const sysCodesRef = ref<Array<string>>([]);

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
          trigger: 'blur',
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
      label: t('排序号'),
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
      label: t('桌面地址'),
      field: 'desktopUrl',
      helpMessage: '仪表盘地址，如果当前多个角色，则根据角色的排序优先级选择。',
      component: 'Input',
      componentProps: {
        maxlength: 250,
      },
      ifShow: () => op.value === 'add' || op.value === 'edit',
    },
    {
      label: t('是否可见'),
      field: 'isShow',
      helpMessage: '切换身份列表中是否显示该角色',
      component: 'RadioGroup',
      componentProps: {
        dictType: 'sys_show_hide',
        allowClear: true,
      },
      ifShow: () => op.value === 'add' || op.value === 'edit',
    },
    {
      label: t('包含系统'),
      field: 'sysCodes',
      helpMessage: '展示子系统列表的时候会根据此条件进行过滤，否则展示全部子系统',
      component: 'Select',
      componentProps: {
        dictType: 'sys_menu_sys_code',
        allowClear: true,
        mode: 'multiple',
        onChange: (val) => {
          sysCodesRef.value = val.split(',');
        },
      },
      colProps: { lg: 24, md: 24 },
      ifShow: () => op.value === 'add' || op.value === 'auth',
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
    setDrawerProps({ loading: true });
    await resetFields();
    op.value = data.op || 'add';
    const res = await roleForm(data);
    record.value = (res.role || {}) as Role;
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
    sysCodeRef.value = await useDict().initGetDictList('sys_menu_sys_code');
    sysCodesRef.value = record.value.sysCodes?.split(',') || [];
  }

  async function loadTreeDatas() {
    const res = await roleMenuTreeData({ roleCode: record.value.roleCode });
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
      if (sysCodesRef.value.length == 0 || sysCodesRef.value.includes(key)) {
        const ks = treeRefs[key].getCheckedKeys();
        for (const k of ks as Array<any>) {
          menuData.push(k);
        }
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
        showMessage(t('common.validateError'));
      }
      console.log('error', error);
    } finally {
      setDrawerProps({ confirmLoading: false });
    }
  }
</script>
