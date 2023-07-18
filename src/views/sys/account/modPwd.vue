<template>
  <PageWrapper>
    <template #headerTitle>
      <Icon :icon="getTitle.icon" class="m-1 pr-1" />
      <span> {{ getTitle.value }} </span>
    </template>
    <div v-if="getModifyPasswordMsg" class="pl-8 pr-8">
      <Alert :message="getModifyPasswordMsg" type="info" show-icon />
    </div>
    <div class="py-8 bg-white flex flex-col justify-center items-center">
      <BasicForm @register="register" class="w-9/12" />
      <div class="flex justify-center">
        <a-button @click="resetFields">
          <Icon icon="ant-design:undo-outlined" /> {{ t('common.resetText') }}
        </a-button>
        <a-button class="!ml-4" type="primary" @click="handleSubmit">
          <Icon icon="ant-design:check-outlined" /> {{ t('common.submitText') }}
        </a-button>
      </div>
    </div>
  </PageWrapper>
</template>
<script lang="ts" setup name="AccountModPwd">
  import { computed } from 'vue';
  import { useI18n } from '/@/hooks/web/useI18n';
  import { Icon } from '/@/components/Icon';
  import { PageWrapper } from '/@/components/Page';
  import { BasicForm, FormSchema, useForm } from '/@/components/Form';
  import { useMessage } from '/@/hooks/web/useMessage';
  import { infoSavePwd } from '/@/api/sys/user';
  import { useUserStore } from '/@/store/modules/user';
  import { Alert } from 'ant-design-vue';
  import { router } from '/@/router';

  const userStore = useUserStore();
  const getModifyPasswordMsg = computed(() => {
    return userStore.getPageCacheByKey('modifyPasswordMsg');
  });

  const { t } = useI18n();
  const { showMessage } = useMessage();
  const getTitle = {
    icon: 'ion:key-outline',
    value: t('sys.account.modifyPwd'),
  };

  const formSchema: FormSchema[] = [
    {
      field: 'oldPassword',
      label: t('sys.account.oldPassword'),
      component: 'InputPassword',
      required: true,
    },
    {
      field: 'newPassword',
      label: t('sys.account.newPassword'),
      component: 'StrengthMeter',
      componentProps: {
        placeholder: t('sys.account.newPassword'),
      },
      rules: [
        {
          required: true,
          message: t('sys.account.newPasswordInputTip'),
        },
      ],
    },
    {
      field: 'confirmNewPassword',
      label: t('sys.account.confirmNewPassword'),
      component: 'InputPassword',
      dynamicRules: ({ values }) => {
        return [
          {
            required: true,
            validator: (_, value) => {
              if (!value) {
                return Promise.reject(t('sys.account.newPasswordNotBlank'));
              }
              if (value !== values.newPassword) {
                return Promise.reject(t('sys.account.newPasswordNotEquals'));
              }
              return Promise.resolve();
            },
          },
        ];
      },
    },
  ];

  const [register, { validate, resetFields }] = useForm({
    size: 'large',
    labelWidth: 150,
    showActionButtonGroup: false,
    schemas: formSchema,
  });

  async function handleSubmit() {
    try {
      const data = await validate();
      const res = await infoSavePwd(data);
      showMessage(res.message);
      if (res.result == 'true' && getModifyPasswordMsg.value) {
        userStore.setPageCache('modifyPasswordMsg', undefined);
        router.replace('/');
      }
    } catch (error: any) {
      if (error && error.errorFields) {
        showMessage(t('common.validateError'));
      }
      console.log('error', error);
    }
  }
</script>
