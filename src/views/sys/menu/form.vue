<!--
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 * No deletion without permission, or be held responsible to law.
 * @author ThinkGem
-->
<template>
  <BasicDrawer
    v-bind="$attrs"
    :showFooter="true"
    :okAuth="'sys:menu:edit'"
    @register="registerDrawer"
    @ok="handleSubmit"
    width="60%"
  >
    <template #title>
      <Icon :icon="getTitle.icon" class="pr-1 m-1" />
      <span> {{ getTitle.value }} </span>
    </template>
    <BasicForm @register="registerForm" />
  </BasicDrawer>
</template>
<script lang="ts">
  export default defineComponent({
    name: 'ViewsSysMenuForm',
  });
</script>
<script lang="ts" setup>
  import { defineComponent, ref, computed } from 'vue';
  import { useI18n } from '/@/hooks/web/useI18n';
  import { useMessage } from '/@/hooks/web/useMessage';
  import { router } from '/@/router';
  import { Icon } from '/@/components/Icon';
  import { BasicForm, FormSchema, useForm } from '/@/components/Form';
  import { BasicDrawer, useDrawerInner } from '/@/components/Drawer';
  import { Menu, menuSave, menuForm, menuTreeData } from '/@/api/sys/menu';

  const emit = defineEmits(['success', 'register']);

  const { t } = useI18n('sys.menu');
  const { showMessage } = useMessage();
  const record = ref<Menu>({} as Menu);
  const getTitle = computed(() => ({
    icon: router.currentRoute.value.meta.icon || 'ant-design:book-outlined',
    value: record.value.isNewRecord ? t('新增菜单') : t('编辑菜单'),
  }));

  const inputFormSchemas: FormSchema[] = [
    {
      label: t('上级菜单'),
      field: 'parentCode',
      fieldLabel: 'parentName',
      component: 'TreeSelect',
      componentProps: {
        allowClear: true,
      },
    },
    {
      label: t('菜单类型'),
      field: 'menuType',
      component: 'RadioButtonGroup',
      componentProps: {
        dictType: 'sys_menu_type',
      },
      defaultValue: '1',
    },

    {
      label: t('菜单名称'),
      field: 'menuNameRaw',
      component: 'Input',
      componentProps: {
        maxlength: 50,
      },
      required: true,
    },
    {
      label: t('归属模块'),
      field: 'moduleCodes',
      component: 'Select',
      componentProps: {
        mode: 'multiple',
      },
      defaultValue: 'core',
      required: true,
    },

    {
      label: t('链接地址'),
      field: 'menuHref',
      helpMessage: [
        '设置菜单的路由地址，对应组件目录为 /views/ 下的 vue 文件',
        '路由名称定义规则为：Views + 去除地址的 “/”，并后一个字母大写，',
        '例如：路由地址为：/sys/menu/list，路径名称为：ViewsSysMenuList，',
        '组件名称定义规则为：/views/ 目录 + 路由地址 目录，上面举例的路由地址',
        '对应的组件名称应为：/views/sys/menu/list，组件名称：与路由名称相同',
        '注意：如果组件名称与路由名称不同，会造成页面缓存失效。',
        '链接支持 http(s):// 开头的链接，则自动使用 iframe 打开',
      ],
      component: 'Input',
      componentProps: {
        maxlength: 2000,
      },
    },
    {
      label: t('链接目标'),
      field: 'menuTarget',
      helpMessage: '如果 “链接地址” 为 http(s):// 开头，目标设置为 _blank，则为新窗口打开',
      component: 'Input',
      componentProps: {
        maxlength: 10,
      },
    },

    {
      label: t('组件名称'),
      field: 'component',
      helpMessage: [
        '设置 vue 组件名称，一般不需要填写，默认是根据 “链接地址” 进行自动生成，',
        '只有个性化的时候才需要设置，如果填写 IFRAME 则强制使用 iframe 打开链接',
      ],
      component: 'Input',
      componentProps: {
        maxlength: 500,
      },
      show: ({ values }) => values.menuType === '1',
    },
    {
      label: t('组件参数'),
      field: 'params',
      helpMessage: '可选，给组件传参，请填写 JSON 格式',
      component: 'Input',
      componentProps: {
        maxlength: 500,
      },
      show: ({ values }) => values.menuType === '1',
    },

    {
      label: t('排序号'),
      field: 'treeSort',
      helpMessage: '升序',
      component: 'InputNumber',
      defaultValue: '30',
      componentProps: {
        maxlength: 10,
      },
      required: true,
    },
    {
      label: t('权限标识'),
      field: 'permission',
      helpMessage: '控制器中定义的权限标识，如：@RequiresPermissions("权限标识")',
      component: 'Input',
      componentProps: {
        maxlength: 100,
      },
    },

    {
      label: t('菜单图标'),
      field: 'menuIcon',
      component: 'IconPicker',
      show: ({ values }) => values.menuType === '1',
    },
    {
      label: t('字体颜色'),
      field: 'menuColor',
      component: 'Input',
      componentProps: {
        maxlength: 50,
      },
      show: ({ values }) => values.menuType === '1',
    },

    {
      label: t('页签标题'),
      field: 'menuTitle',
      component: 'Input',
      componentProps: {
        maxlength: 50,
      },
      show: ({ values }) => values.menuType === '1',
    },
    {
      label: t('是否可见'),
      field: 'isShow',
      component: 'RadioGroup',
      defaultValue: '0',
      componentProps: {
        dictType: 'sys_show_hide',
      },
    },

    {
      label: t('菜单权重'),
      field: 'weight',
      component: 'Select',
      defaultValue: 40,
      componentProps: {
        dictType: 'sys_menu_weight',
      },
      required: true,
    },

    {
      label: t('备注信息'),
      field: 'remarks',
      component: 'InputTextArea',
      componentProps: {
        maxlength: 200,
      },
      colProps: { lg: 24, md: 24 },
    },
  ];

  const [registerForm, { resetFields, setFieldsValue, updateSchema, validate }] = useForm({
    labelWidth: 120,
    schemas: inputFormSchemas,
    baseColProps: { lg: 12, md: 24 },
  });

  const [registerDrawer, { setDrawerProps, closeDrawer }] = useDrawerInner(async (data) => {
    resetFields();
    setDrawerProps({ loading: true });
    const res = await menuForm(data);
    record.value = (res.menu || {}) as Menu;
    if (data.parentCode && data.parentName) {
      record.value.parentCode = data.parentCode;
      record.value.parentName = data.parentName;
    }
    setFieldsValue(record.value);
    updateSchema([
      {
        field: 'parentCode',
        componentProps: {
          api: menuTreeData,
          params: {
            sysCode: record.value.sysCode,
            excludeCode: record.value.menuCode,
            isShowRawName: true,
          },
        },
      },
      {
        field: 'moduleCodes',
        componentProps: {
          options: res.moduleList.map((item) => ({
            label: item.moduleName,
            value: item.moduleCode,
          })),
        },
      },
    ]);
    setDrawerProps({ loading: false });
  });

  async function handleSubmit() {
    try {
      const data = await validate();
      setDrawerProps({ confirmLoading: true });
      const params: any = {
        isNewRecord: record.value.isNewRecord,
        menuCode: record.value.menuCode,
      };
      data.sysCode = record.value.sysCode;
      // console.log('submit', params, data, record);
      const res = await menuSave(params, data);
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
