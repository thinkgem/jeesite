<template>
  <PageWrapper>
    <template #headerTitle>
      <Icon :icon="getTitle.icon" class="m-1 pr-1" />
      <span> {{ getTitle.value }} </span>
    </template>
    <div v-if="getModifyPasswordMsg" class="pl-8 pr-8">
      <Alert :message="getModifyPasswordMsg" type="info" show-icon />
    </div>
    <div class="flex flex-col items-center justify-center bg-white py-8">
      <BasicForm @register="register" class="w-9/12" />
      <div class="mt-6 flex justify-center">
        <a-button @click="resetFields">
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
  import { computed } from 'vue';
  import { useI18n } from '/@/hooks/web/useI18n';
  import { Icon } from '/@/components/Icon';
  import { PageWrapper } from '/@/components/Page';
  import { BasicForm, FormSchema, useForm } from '/@/components/Form';
  import { useMessage } from '/@/hooks/web/useMessage';
  import { infoSavePwd } from '/@/api/sys/user';
  import { useUserStore } from '/@/store/modules/user';
  import { PageEnum } from '/@/enums/pageEnum';
  import { Alert } from 'ant-design-vue';
  import { publicPath } from '/@/utils/env';

  const userStore = useUserStore();
  const getModifyPasswordMsg = computed(() => {
    return userStore.getPageCacheByKey('modifyPasswordMsg');
  });

  const { t } = useI18n();
  const { showMessage, createSuccessModal } = useMessage();
  const getTitle = {
    icon: 'i-ion:key-outline',
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
    baseColProps: { span: 24 },
  });

  async function handleSubmit() {
    try {
      const data = await validate();
      const res = await infoSavePwd(data);
      if (res.result == 'true') {
        if (getModifyPasswordMsg.value) {
          userStore.setPageCache('modifyPasswordMsg', undefined);
        }
        createSuccessModal({
          content: res.message,
          onOk: () => {
            window.location.href = publicPath + PageEnum.BASE_HOME;
          },
        });
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
