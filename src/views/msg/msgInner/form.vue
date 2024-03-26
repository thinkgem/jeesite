<!--
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 * No deletion without permission, or be held responsible to law.
 * @author ThinkGem
-->
<template>
  <BasicDrawer
    v-bind="$attrs"
    :showFooter="true"
    :okText="t('发布')"
    :okAuth="'msg:msgInner:edit'"
    :showOkBtn="!record.status || record.status == '9'"
    @register="registerDrawer"
    @ok="handleSubmit('0')"
    width="60%"
  >
    <template #title>
      <Icon :icon="getTitle.icon" class="m-1 pr-1" />
      <span> {{ getTitle.value }} </span>
    </template>
    <template #centerFooter>
      <a-button
        color="success"
        v-if="!record.status || record.status == '9'"
        @click="handleSubmit('9')"
      >
        <Icon icon="i-ant-design:save-outlined" />
        {{ t('草稿') }}
      </a-button>
    </template>
    <BasicForm @register="registerForm">
      <template #msgContent="{ model, field }">
        <WangEditor
          v-model:value="model[field]"
          :bizKey="record.id"
          :bizType="'msgInner_' + field"
          :height="300"
        />
      </template>
      <template #receiveCodes="{ model }">
        <Form.ItemRest>
          <TreeSelect
            v-show="model.receiveType === '1'"
            :value="receivers['c' + model.receiveType]"
            :labelValue="receivers['n' + model.receiveType]"
            :labelInValue="true"
            :api="officeTreeData"
            :params="{ isLoadUser: true, userIdPrefix: '', isAll: true }"
            :canSelectParent="true"
            :treeCheckable="true"
            @change="(a, b) => onReceiversChange(model.receiveType, a, b)"
          />
          <TreeSelect
            v-show="model.receiveType === '2'"
            :value="receivers['c' + model.receiveType]"
            :labelValue="receivers['n' + model.receiveType]"
            :labelInValue="true"
            :api="officeTreeData"
            :params="{ isAll: true }"
            :canSelectParent="true"
            :treeCheckable="true"
            @change="(a, b) => onReceiversChange(model.receiveType, a, b)"
          />
          <TreeSelect
            v-show="model.receiveType === '3'"
            :value="receivers['c' + model.receiveType]"
            :labelValue="receivers['n' + model.receiveType]"
            :labelInValue="true"
            :api="roleTreeData"
            :params="{ isAll: true }"
            :canSelectParent="true"
            :treeCheckable="true"
            @change="(a, b) => onReceiversChange(model.receiveType, a, b)"
          />
          <TreeSelect
            v-show="model.receiveType === '4'"
            :value="receivers['c' + model.receiveType]"
            :labelValue="receivers['n' + model.receiveType]"
            :labelInValue="true"
            :api="postTreeData"
            :params="{ isAll: true }"
            :canSelectParent="true"
            :treeCheckable="true"
            @change="(a, b) => onReceiversChange(model.receiveType, a, b)"
          />
        </Form.ItemRest>
      </template>
    </BasicForm>
  </BasicDrawer>
</template>
<script lang="ts" setup name="ViewsMsgMsgInnerForm">
  import { ref, unref, computed } from 'vue';
  import { Form } from 'ant-design-vue';
  import { useI18n } from '/@/hooks/web/useI18n';
  import { useMessage } from '/@/hooks/web/useMessage';
  import { router } from '/@/router';
  import { Icon } from '/@/components/Icon';
  import { BasicForm, FormSchema, useForm } from '/@/components/Form';
  import { BasicDrawer, useDrawerInner } from '/@/components/Drawer';
  import { MsgInner, msgInnerSave, msgInnerForm } from '/@/api/msg/msgInner';
  import { WangEditor } from '/@/components/WangEditor';
  import { TreeSelect } from '/@/components/Form';
  import { officeTreeData } from '/@/api/sys/office';
  import { roleTreeData } from '/@/api/sys/role';
  import { postTreeData } from '/@/api/sys/post';

  const emit = defineEmits(['success', 'register']);

  const { t } = useI18n('msg.msgInner');
  const { showMessage } = useMessage();
  const { meta } = unref(router.currentRoute);
  const record = ref<MsgInner>({} as MsgInner);
  const getTitle = computed(() => ({
    icon: meta.icon || 'ant-design:book-outlined',
    value: record.value.isNewRecord ? t('新增消息') : t('编辑消息'),
  }));
  const receivers = ref({});

  const inputFormSchemas: FormSchema[] = [
    {
      label: t('标题'),
      field: 'msgTitle',
      component: 'Input',
      componentProps: {
        maxlength: 200,
      },
      required: true,
      colProps: { lg: 24, md: 24 },
    },
    {
      label: t('等级'),
      field: 'contentLevel',
      component: 'RadioGroup',
      componentProps: {
        dictType: 'msg_inner_content_level',
      },
      required: true,
    },
    {
      label: t('类型'),
      field: 'contentType',
      component: 'RadioGroup',
      componentProps: {
        dictType: 'msg_inner_content_type',
      },
    },
    {
      label: t('内容'),
      field: 'msgContent',
      component: 'InputTextArea',
      required: true,
      colProps: { lg: 24, md: 24 },
      slot: 'msgContent',
    },
    {
      label: t('附件'),
      field: 'dataMap',
      component: 'Upload',
      componentProps: {
        loadTime: computed(() => record.value.__t),
        bizKey: computed(() => record.value.id),
        bizType: 'msgInner_file',
        uploadType: 'all',
      },
      // rules: [
      //   { required: true, message: t('请上传附件') },
      //   {
      //     validator(_rule, value) {
      //       return new Promise<void>((resolve, reject) => {
      //         const bizType = 'msgInner_file';
      //         if (!value || (value[bizType + '__len'] as number) > 0) resolve();
      //         else reject(t('请上传附件'));
      //       });
      //     },
      //   },
      // ],
      colProps: { lg: 24, md: 24 },
    },
    {
      label: t('接受者信息'),
      field: 'receiveInfo',
      component: 'FormGroup',
      colProps: { lg: 24, md: 24 },
    },
    {
      label: t('接受者'),
      field: 'receiveType',
      component: 'RadioGroup',
      componentProps: {
        dictType: 'msg_inner_receiver_type',
      },
      required: true,
      colProps: { lg: 24, md: 24 },
    },
    {
      label: '　',
      field: 'receiveCodes',
      fieldLabel: 'receiveNames',
      component: 'Input',
      colProps: { lg: 24, md: 24 },
      slot: 'receiveCodes',
      show: ({ values }) => values.receiveType != '0',
    },
    {
      label: t('通知'),
      field: 'notifyTypes',
      component: 'CheckboxGroup',
      componentProps: {
        dictType: 'sys_msg_type',
      },
      colProps: { lg: 24, md: 24 },
    },
    {
      label: t('发送者信息'),
      field: 'receiveInfo',
      component: 'FormGroup',
      colProps: { lg: 24, md: 24 },
    },
    {
      label: t('发送者'),
      field: 'sendUserName',
      component: 'Input',
      componentProps: {
        maxlength: 100,
        disabled: true,
      },
    },
    {
      label: t('发送时间'),
      field: 'sendDate',
      component: 'DatePicker',
      componentProps: {
        format: 'YYYY-MM-DD HH:mm',
        showTime: { format: 'HH:mm' },
        disabled: true,
      },
    },
  ];

  const [registerForm, { resetFields, setFieldsValue, validate }] = useForm({
    labelWidth: 120,
    schemas: inputFormSchemas,
    baseColProps: { lg: 12, md: 24 },
  });

  const [registerDrawer, { setDrawerProps, closeDrawer }] = useDrawerInner(async (data) => {
    setDrawerProps({ loading: true });
    await resetFields();
    const res = await msgInnerForm(data);
    record.value = (res.msgInner || {}) as MsgInner;
    record.value.__t = new Date().getTime();
    onReceiversChange(
      record.value.receiveType,
      record.value.receiveCodes,
      record.value.receiveNames,
    );
    setFieldsValue(record.value);
    setDrawerProps({ loading: false });
  });

  function onReceiversChange(receiveType, value, labelValue) {
    receivers.value = {};
    receivers.value['c' + receiveType] = value;
    receivers.value['n' + receiveType] = labelValue;
  }

  async function handleSubmit(status: string) {
    try {
      const data = await validate();
      data.status = status;
      data.receiveCodes = receivers.value['c' + data.receiveType];
      data.receiveNames = receivers.value['n' + data.receiveType];
      setDrawerProps({ confirmLoading: true });
      const params: any = {
        isNewRecord: record.value.isNewRecord,
        id: record.value.id,
      };
      // console.log('submit', params, data, record);
      const res = await msgInnerSave(params, data);
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
