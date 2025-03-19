<!--
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
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
    width="60%"
  >
    <template #title>
      <Icon :icon="getTitle.icon" class="m-1 pr-1" />
      <span> {{ getTitle.value }} </span>
    </template>
    <template #centerFooter>
      <a-button v-if="isCustomModule" type="primary" danger @click="handleSubmit('2')">
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
  const { showMessage } = useMessage();
  const { meta } = unref(router.currentRoute);
  const record = ref<Module>({} as Module);
  const genBaseDir = ref<string>('');
  const genBaseDirList = ref<DropMenu[]>([]);
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
      label: t('主类全名'),
      field: 'mainClassName',
      component: 'Input',
      componentProps: {
        maxlength: 500,
      },
      colProps: { md: 24, lg: 24 },
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

  const [registerDrawer, { setDrawerProps, closeDrawer }] = useDrawerInner(async (data) => {
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
    genTplCategoryList.value = res.config?.moduleTplCategory?.tplCategoryList || [];
    isCustomModule.value = !moduleNames.includes(record.value.moduleCode || '');
    setFieldsValue(record.value);
    updateSchema([
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

  async function handleSubmit(flag: string) {
    try {
      const data = await validate();
      setDrawerProps({ confirmLoading: true });
      const params: any = {
        isNewRecord: record.value.isNewRecord,
        moduleCode: record.value.moduleCode,
      };
      data.genBaseDir = genBaseDir.value;
      data.genFlag = flag ? flag : '1';
      data.replaceFile = data.replaceFile ? '1' : '0';
      //console.log('submit', params, data, record);
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
