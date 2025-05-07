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
    width="70%"
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
  // import { moduleSelectData } from '/@/api/sys/module';

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
      label: t('基本信息'),
      field: 'basicInfo',
      component: 'FormGroup',
      colProps: { md: 24, lg: 24 },
    },
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
        // api: moduleSelectData,
        // allowClear: true,
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
        '　1、设置菜单的路由地址，对应组件目录为 /views/ 下的 .vue 文件',
        '　2、路由名称生成规则为：Views + 去除地址的 “/”，并后一个字母大写，',
        '　　　例如：路由地址为：/sys/menu/list，则生成路由名称为：ViewsSysMenuList',
        '　3、Vue 组件路径存放规则为：/views/ + 路由地址（上面举例的路由地址）',
        '　　　那么：组件路径应为：/views/sys/menu/list，组件名称应与路由名称相同',
        '　4、组件名称定义在 script 标签的 name 属性，如 name="ViewsSysMenuList"',
        '　5、注意：如果组件名称与路由名称不同，则会造成页面缓存失效。',
        '二、链接前缀：',
        '　1、使用 / 开头（默认）则为管理根路径，例如：http://host/{ctxPath}/{adminPath}/{href}',
        '　2、使用 // 开头，则代表是工程根路径，例如：http://host/{ctxPath}/{href}',
        '　3、使用 /// 开头，则代表是站点根路径，例如：http://host/{href}',
        '　4、使用 http:// 或 https:// 开头，则为链接外部页面',
        '三、可带变量： 格式为  {变量名} ',
        '　1、{ssoToken} : 单点登录的token编码，url参数中的参数分隔符请使用“%26”进行转义，',
        '　　　例如：{projectUrl}/sso/{ssoToken}?url=/sys/user/list?p1=v1%26p2=v2&relogin=true',
        '　2、{sessionId} : 当前会话编号 v5.3.0',
        '　3、{userCode} : 当前用户编码',
        '　4、{userName} : 当前用户名称',
        '　5、{userType} : 当前用户类型',
        '　6、{corpCode} : 当前用户编码',
        '　7、{corpName} : 当前用户名称',
        '　8、{menuCode} : 当前菜单编码',
        '　9、{menuParentCode} : 当前菜单上级编码',
        '　10、{menuParentCodes} : 当前菜单所有上级编码',
        '　11、userCache 中的 Key 可作为变量名',
        '　12、yml 或 sys_config 中的 Key 可作为变量名',
        '　13、上述没有的变量，交由 Vue 路由，详见 params.vue ',
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
      label: t('组件路径'),
      field: 'component',
      helpMessage: [
        '一、组件路径说明：',
        '　1、自定义 Vue 组件路径，一般不需要填写，默认是根据 “链接地址” 进行自动生成',
        '　2、当 “链接地址” 或 “路由地址” 与 Vue 组件路径匹配规则不一致的时候配置',
        '二、也可以设置内置组件名称：',
        '　1、填写 IFRAME 则强制使用 iframe 打开链接',
        '　2、填写 LAYOUT 将不在 Beetl 视图中显示菜单项',
        '　3、填写 BEETL 则只在 Beetl 视图中显示菜单项',
        '　4、填写 BLANK 则不显示主框架（仅顶部菜单设置即可）',
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
        'Vue：const props = defineProps({ aa: String, bb: String })',
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
      label: t('其它信息'),
      field: 'otherInfo',
      component: 'FormGroup',
      colProps: { md: 24, lg: 24 },
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
      colProps: { md: 24, lg: 24 },
    },
  ];

  const [registerForm, { resetFields, setFieldsValue, updateSchema, validate }] = useForm({
    labelWidth: 120,
    schemas: inputFormSchemas,
    baseColProps: { md: 24, lg: 12 },
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
        showMessage(error.message || t('common.validateError'));
      }
      console.log('error', error);
    } finally {
      setDrawerProps({ confirmLoading: false });
    }
  }
</script>
