<!--
 * Copyright (c) 2013-Now https://jeesite.com All rights reserved.
 * No deletion without permission, or be held responsible to law.
 * @author ThinkGem
-->
<template>
  <BasicDrawer
    v-bind="$attrs"
    :showFooter="true"
    :okAuth="'sys:module:edit'"
    @register="registerDrawer"
    @ok="handleSubmit"
    width="70%"
  >
    <template #title>
      <Icon :icon="getTitle.icon" class="m-1 pr-1" />
      <span> {{ getTitle.value }} </span>
    </template>
    <template #centerFooter>
      <a-button v-if="isCustomModule" type="primary" danger :loading="confirmLoading" @click="handleSubmitAndGen">
        <Icon icon="i-ant-design:bug-outlined" /> {{ t('确认并生成代码') }}
      </a-button>
    </template>
    <BasicForm @register="registerForm">
      <template #genBaseDir>
        <Input v-model:value="genBaseDir">
          <template #addonAfter>
            <Dropdown class="cursor-pointer" :trigger="['click']" :dropMenuList="genBaseDirList">
              {{ t('生成路径快速选择') }} <Icon icon="i-ant-design:down-outlined" />
            </Dropdown>
          </template>
        </Input>
      </template>
      <template #genFrontDir>
        <Input v-model:value="genFrontDir">
          <template #addonAfter>
            <Dropdown class="cursor-pointer" :trigger="['click']" :dropMenuList="genFrontDirList">
              {{ t('生成路径快速选择') }} <Icon icon="i-ant-design:down-outlined" />
            </Dropdown>
          </template>
        </Input>
      </template>
    </BasicForm>
  </BasicDrawer>
</template>
<script lang="ts" setup name="ViewsSysModuleForm">
  import { ref, unref, computed } from 'vue';
  import { Input } from 'ant-design-vue';
  import { useI18n } from '@jeesite/core/hooks/web/useI18n';
  import { useMessage } from '@jeesite/core/hooks/web/useMessage';
  import { router } from '@jeesite/core/router';
  import { Icon } from '@jeesite/core/components/Icon';
  import { BasicForm, FormSchema, useForm } from '@jeesite/core/components/Form';
  import { BasicDrawer, useDrawerInner } from '@jeesite/core/components/Drawer';
  import { Module, moduleSave, moduleForm } from '@jeesite/core/api/sys/module';
  import { Dropdown, DropMenu } from '@jeesite/core/components/Dropdown';

  const emit = defineEmits(['success', 'register']);

  const { t } = useI18n('sys.module');
  const { showMessage, createConfirm } = useMessage();
  const { meta } = unref(router.currentRoute);
  const record = ref<Module>({} as Module);
  const genBaseDir = ref<string>('');
  const genBaseDirList = ref<DropMenu[]>([]);
  const genFrontDir = ref<string>('');
  const genFrontDirList = ref<DropMenu[]>([]);
  const genTplCategoryList = ref<string[]>([]);
  const getTitle = computed(() => ({
    icon: meta.icon || 'ant-design:book-outlined',
    value: record.value.isNewRecord ? t('新增模块') : t('编辑模块'),
  }));
  const moduleNames = [
    'app',
    'bpm',
    'cms',
    'core',
    'filemanager',
    'filepreview',
    'oauth2',
    'oss-client',
    'sharding',
    'swagger',
    'ureport',
    'visual',
    'weixin',
  ];
  const isCustomModule = ref<boolean>(false);

  const inputFormSchemas: FormSchema[] = [
    {
      label: t('基本信息'),
      field: 'basicInfo',
      component: 'FormGroup',
      colProps: { md: 24, lg: 24 },
    },
    {
      label: t('模块名称'),
      field: 'moduleName',
      component: 'Input',
      componentProps: {
        maxlength: 100,
      },
      required: true,
    },
    {
      label: t('模块编码'),
      field: 'moduleCode',
      component: 'Input',
      componentProps: {
        maxlength: 64,
      },
      rules: [
        { required: true },
        {
          pattern: /^[a-zA-Z]([a-zA-Z0-9_-])*[a-zA-Z0-9]$/,
          message: t('请输入2个以上字符，字母开头、允许字母数字下划线或减号、字母数字结尾'),
        },
      ],
    },
    {
      label: t('模块描述'),
      field: 'description',
      component: 'InputTextArea',
      componentProps: {
        maxlength: 500,
        rows: 3,
      },
      colProps: { md: 24, lg: 24 },
    },
    {
      label: t('主类全名'),
      helpMessage:
        '该模块的状态验证类，如果该类检测不存在，则该模块状态提示 “未安装” ，验证原理：\n' +
        'Class.forName(“com.jeesite.modules.sys.web.LoginController”)；在微服务下不进行验证。',
      field: 'mainClassName',
      component: 'Input',
      componentProps: {
        maxlength: 500,
      },
      colProps: { md: 24, lg: 24 },
      required: true,
    },
    {
      label: t('基础包名'),
      helpMessage: '该模块所属的基础包名',
      field: 'packageName',
      component: 'Input',
      componentProps: {
        maxlength: 500,
      },
      required: true,
    },
    {
      label: t('排序号'),
      field: 'moduleSort',
      helpMessage: '升序',
      component: 'InputNumber',
      defaultValue: '30',
      componentProps: {
        maxlength: 10,
      },
      required: true,
    },
    {
      label: t('版本信息'),
      field: 'versionInfo',
      component: 'FormGroup',
      colProps: { md: 24, lg: 24 },
    },
    {
      label: t('当前版本'),
      field: 'currentVersion',
      component: 'Input',
      componentProps: {
        maxlength: 50,
      },
    },
    {
      label: t('升级信息'),
      field: 'upgradeInfo',
      component: 'Input',
      componentProps: {
        maxlength: 300,
        disabled: true,
      },
      ifShow: () => !record.value.isNewRecord,
    },

    {
      label: t('生成工程代码'),
      field: 'genModule',
      component: 'FormGroup',
      colProps: { md: 24, lg: 24 },
      ifShow: () => isCustomModule.value,
    },
    {
      label: t('生成基础路径'),
      field: 'genBaseDir',
      component: 'Input',
      slot: 'genBaseDir',
      colProps: { md: 24, lg: 24 },
      ifShow: () => isCustomModule.value,
    },
    {
      label: t('生成前端路径'),
      field: 'genFrontDir',
      component: 'Input',
      slot: 'genFrontDir',
      colProps: { md: 24, lg: 24 },
      ifShow: () => isCustomModule.value,
    },
    {
      label: t('代码生成模板'),
      field: 'tplCategory',
      component: 'Select',
      componentProps: {
        options: genTplCategoryList,
        allowClear: true,
      },
      ifShow: () => isCustomModule.value,
    },
    {
      field: 'replaceFile',
      component: 'Checkbox',
      renderComponentContent: t('是否替换现有文件'),
      ifShow: () => isCustomModule.value,
    },
  ];

  const [registerForm, { resetFields, setFieldsValue, updateSchema, validate }] = useForm({
    labelWidth: 120,
    schemas: inputFormSchemas,
    baseColProps: { md: 24, lg: 12 },
  });

  const [registerDrawer, { setDrawerProps, closeDrawer, getDrawerProps }] = useDrawerInner(async (data) => {
    setDrawerProps({ loading: true });
    await resetFields();
    const res = await moduleForm(data);
    record.value = (res.module || {}) as Module;
    genBaseDir.value = res.genBaseDir || '';
    genBaseDirList.value = (res.genBaseDirList || []).map((s: string) => {
      return {
        text: s,
        onClick: () => {
          genBaseDir.value = s;
        },
      };
    });
    genFrontDir.value = res.genFrontDir || '';
    genFrontDirList.value = (res.genFrontDirList || []).map((s: string) => {
      return {
        text: s,
        onClick: () => {
          genFrontDir.value = s;
        },
      };
    });
    genTplCategoryList.value = res.config?.moduleTplCategory?.tplCategoryList || [];
    isCustomModule.value = !moduleNames.includes(record.value.moduleCode || '');
    // record.value.tplCategory = ''; // 不回显代码生成模版，选择生成模版后再编译或生成模版
    await setFieldsValue(record.value);
    await updateSchema([
      {
        field: 'moduleCode',
        componentProps: {
          disabled: !record.value.isNewRecord,
        },
      },
      {
        field: 'currentVersion',
        componentProps: {
          disabled: !record.value.isNewRecord,
        },
      },
    ]);
    setDrawerProps({ loading: false });
  });

  const confirmLoading = computed(() => {
    return getDrawerProps().confirmLoading || false;
  });

  async function handleSubmitAndGen() {
    createConfirm({
      title: t('提示'),
      content: t('是否要生成模块源码到 ‘' + genBaseDir.value + '’ <br/>和 ‘' + genFrontDir.value + '’ 目录下？'),
      iconType: 'warning',
      width: '50%',
      onOk: () => {
        handleSubmit('2');
      },
    });
  }

  async function handleSubmit(flag: string) {
    try {
      const data = await validate();
      setDrawerProps({ confirmLoading: true });
      const params: any = {
        isNewRecord: record.value.isNewRecord,
        moduleCode: record.value.moduleCode,
      };
      data.genBaseDir = genBaseDir.value;
      data.genFrontDir = genFrontDir.value;
      data.genFlag = flag;
      data.replaceFile = data.replaceFile ? '1' : '0';
      // console.log('submit', params, data, record);
      const res = await moduleSave(params, data);
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
