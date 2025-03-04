<template>
  <PageWrapper>
    <template #headerTitle>
      <Icon :icon="getTitle.icon" class="m-1 pr-1" />
      <span> {{ getTitle.value }} </span>
    </template>
    <div v-if="getPwdQuestionMsg" class="pl-8 pr-8 pt-3">
      <Alert :message="getPwdQuestionMsg" type="info" show-icon />
    </div>
    <div class="flex flex-col items-center justify-center bg-white py-8 pr-20">
      <BasicForm @register="register" class="w-10/12" />
      <div class="mt-6 flex justify-center">
        <a-button @click="handleReset">
          <Icon icon="i-ant-design:undo-outlined" /> {{ t('common.resetText') }}
        </a-button>
        <a-button class="!ml-4" type="primary" @click="handleSubmit">
          <Icon icon="i-ant-design:check-outlined" /> {{ t('common.submitText') }}
        </a-button>
      </div>
    </div>
  </PageWrapper>
</template>
<script lang="ts" setup name="AccountModPwd">
  import { computed, onMounted, ref } from 'vue';
  import { useI18n } from '@jeesite/core/hooks/web/useI18n';
  import { Icon } from '@jeesite/core/components/Icon';
  import { PageWrapper } from '@jeesite/core/components/Page';
  import { BasicForm, FormSchema, useForm } from '@jeesite/core/components/Form';
  import { useMessage } from '@jeesite/core/hooks/web/useMessage';
  import { infoSavePqa, userInfo } from '@jeesite/core/api/sys/user';
  import { Alert } from 'ant-design-vue';

  const { t } = useI18n();
  const { showMessage, showMessageModal } = useMessage();
  const getTitle = {
    icon: 'i-ant-design:safety-certificate-outlined',
    value: t('sys.account.pwdQuestion'),
  };

  const record = ref<Recordable>();
  const getPwdQuestionMsg = computed(() => {
    if (record.value?.pwdQuestion && record.value?.pwdQuestion !== '') return '';
    return t('您还未设置过密保问题，您可以根据登录密码设置新的密保问题及答案。');
  });

  const formSchema: FormSchema[] = [
    {
      field: 'validPassword',
      label: t('sys.account.oldPassword'),
      component: 'InputPassword',
      required: true,
      ifShow: () => !!getPwdQuestionMsg.value,
      colProps: { md: 24, lg: 24 },
    },

    {
      label: t('sys.account.oldPwdQuestion'),
      field: 'oldPwdQuestionInfo',
      component: 'FormGroup',
      colProps: { md: 24, lg: 24 },
      ifShow: () => !getPwdQuestionMsg.value,
    },
    {
      field: 'oldPwdQuestion',
      label: t('sys.account.oldPwdQuestion') + 1,
      component: 'Input',
      componentProps: {
        disabled: true,
      },
      required: true,
      ifShow: () => !getPwdQuestionMsg.value,
    },
    {
      field: 'oldPwdQuestionAnswer',
      label: t('sys.account.oldPwdQuestionAnswer') + 1,
      component: 'Input',
      required: true,
      ifShow: () => !getPwdQuestionMsg.value,
    },
    {
      field: 'oldPwdQuestion2',
      label: t('sys.account.oldPwdQuestion') + 2,
      component: 'Input',
      componentProps: {
        disabled: true,
      },
      required: true,
      ifShow: () => !getPwdQuestionMsg.value,
    },
    {
      field: 'oldPwdQuestionAnswer2',
      label: t('sys.account.oldPwdQuestionAnswer') + 2,
      component: 'Input',
      required: true,
      ifShow: () => !getPwdQuestionMsg.value,
    },
    {
      field: 'oldPwdQuestion3',
      label: t('sys.account.oldPwdQuestion') + 3,
      component: 'Input',
      componentProps: {
        disabled: true,
      },
      required: true,
      ifShow: () => !getPwdQuestionMsg.value,
    },
    {
      field: 'oldPwdQuestionAnswer3',
      label: t('sys.account.oldPwdQuestionAnswer') + 3,
      component: 'Input',
      required: true,
      ifShow: () => !getPwdQuestionMsg.value,
    },

    {
      label: t('sys.account.newPwdQuestion'),
      field: 'newPwdQuestionInfo',
      component: 'FormGroup',
      colProps: { md: 24, lg: 24 },
    },
    {
      field: 'pwdQuestion',
      label: t('sys.account.newPwdQuestion') + 1,
      component: 'Input',
      required: true,
    },
    {
      field: 'pwdQuestionAnswer',
      label: t('sys.account.newPwdQuestionAnswer') + 1,
      component: 'Input',
      required: true,
    },
    {
      field: 'pwdQuestion2',
      label: t('sys.account.newPwdQuestion') + 2,
      component: 'Input',
      required: true,
    },
    {
      field: 'pwdQuestionAnswer2',
      label: t('sys.account.newPwdQuestionAnswer') + 2,
      component: 'Input',
      required: true,
    },
    {
      field: 'pwdQuestion3',
      label: t('sys.account.newPwdQuestion') + 3,
      component: 'Input',
      required: true,
    },
    {
      field: 'pwdQuestionAnswer3',
      label: t('sys.account.newPwdQuestionAnswer') + 3,
      component: 'Input',
      required: true,
    },
  ];

  const [register, { validate, resetFields, setFieldsValue }] = useForm({
    size: 'large',
    labelWidth: 180,
    showActionButtonGroup: false,
    schemas: formSchema,
    baseColProps: { md: 24, lg: 12 },
  });

  async function handleReset() {
    record.value = await userInfo({ op: 'pqa' });
    await resetFields();
    await setFieldsValue({
      oldPwdQuestion: record.value.pwdQuestion,
      oldPwdQuestion2: record.value.pwdQuestion2,
      oldPwdQuestion3: record.value.pwdQuestion3,
      newPwdQuestion: record.value.pwdQuestion,
      newPwdQuestion2: record.value.pwdQuestion2,
      newPwdQuestion3: record.value.pwdQuestion3,
    });
  }

  onMounted(async () => {
    await handleReset();
  });

  async function handleSubmit() {
    try {
      const data = await validate();
      const res = await infoSavePqa(data);
      if (res.result == 'true') {
        await handleReset();
        showMessageModal({ content: res.message });
      } else {
        showMessage(res.message);
      }
    } catch (error: any) {
      if (error && error.errorFields) {
        showMessage(error.message || t('common.validateError'));
      }
      console.log('error', error);
    }
  }
</script>
