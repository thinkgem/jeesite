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
      <Icon :icon="getTitle.icon" class="m-1 pr-1" />
      <span> {{ getTitle.value }} </span>
    </template>
    <BasicForm @register="registerForm" />
  </BasicDrawer>
</template>
<script lang="ts" setup name="ViewsSysMenuForm">
  import { ref, unref, computed } from 'vue';
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
  const { meta } = unref(router.currentRoute);
  const record = ref<Menu>({} as Menu);
  const getTitle = computed(() => ({
    icon: meta.icon || 'ant-design:book-outlined',
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
        '一、路由规则：',
        '　1、设置菜单的路由地址，对应组件目录为 /views/ 下的 vue 文件',
        '　2、路由名称定义规则为：Views + 去除地址的 “/”，并后一个字母大写，',
        '　　　例如：路由地址为：/sys/menu/list，路径名称为：ViewsSysMenuList，',
        '　3、组件名称定义规则为：/views/ 目录 + 路由地址 目录，上面举例的路由地址',
        '　4、对应的组件名称应为：/views/sys/menu/list，组件名称：与路由名称相同',
        '　5、注意：如果组件名称与路由名称不同，会造成页面缓存失效。',
        '二、链接前缀：',
        '　1、链接支持 http(s):// 开头的链接，则自动使用 iframe 打开',
        '　2、如果以  /// 开头，则代表是站点根路径（结果：http://localhost/{href}）',
        '　3、如果以  // 开头，则代表是工程根路径（结果：http://localhost/{ctxPath}/{href}）',
        '　4、如果以  / 开头，则代表是管理根路径（默认）（结果：',
        '　　　http://localhost/{ctxPath}/{adminPath}/{href}）',
        '三、可带变量： 格式为  {变量名} ',
        '　1、{ssoToken} : 单点登录的token编码，url参数中的参数分隔符请使用“%26”进行转义，',
        '　　　例如：{projectUrl}/sso/{ssoToken}?url=/sys/user/list?p1=v1%26p2=v2&relogin=true',
        '　　　{sessionId} : 当前会话编号 v5.3.0',
        '　2、{userCode} : 当前用户编码',
        '　3、{userName} : 当前用户名称',
        '　4、{userType} : 当前用户类型',
        '　5、{corpCode} : 当前用户编码',
        '　6、{corpName} : 当前用户名称',
        '　7、{menuCode} : 当前菜单编码',
        '　8、{menuParentCode} : 当前菜单上级编码',
        '　9、{menuParentCodes} : 当前菜单所有上级编码',
        '　10、userCache 中的 Key 可作为变量名',
        '　11、yml 或 sys_config 中的 Key 可作为变量名',
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
        '只有个性化的时候才需要设置，如果填写 IFRAME 则强制使用 iframe 打开链接；',
        '如果填写 LAYOUT 将不在 Beetl 视图中显示菜单项',
        '如果填写 BEETL 则只在 Beetl 视图中显示菜单项',
        '如果填写 BLANK 则不显示主框架（仅顶部菜单设置）',
      ],
      component: 'Input',
      componentProps: {
        maxlength: 500,
      },
      // show: ({ values }) => values.menuType === '1',
    },
    {
      label: t('组件参数'),
      field: 'params',
      helpMessage: [
        '可选，给组件传参，请填写 JSON 格式，前端通过定义 props 获取',
        "举例：链接地址填写：/test/params   组件参数填写：{aa:'aa1',bb:'bb2'}",
      ],
      component: 'Input',
      componentProps: {
        maxlength: 500,
      },
      // show: ({ values }) => values.menuType === '1',
    },

    {
      label: t('排序号'),
      field: 'treeSort',
      helpMessage: '升序，当前级别的排序号',
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
    setDrawerProps({ loading: true });
    await resetFields();
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
      data.oldParentCode = record.value.parentCode;
      // console.log('submit', params, data, record);
      const res = await menuSave(params, data);
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
