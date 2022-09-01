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
      <BasicForm @register="register" />
      <div class="flex justify-center">
        <a-button @click="resetFields"> <Icon icon="ant-design:undo-outlined" /> 重置 </a-button>
        <a-button class="!ml-4" type="primary" @click="handleSubmit">
          <Icon icon="ant-design:check-outlined" /> 提交
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
      label: '当前密码',
      component: 'InputPassword',
      required: true,
    },
    {
      field: 'newPassword',
      label: '新密码',
      component: 'StrengthMeter',
      componentProps: {
        placeholder: '新密码',
      },
      rules: [
        {
          required: true,
          message: '请输入新密码',
        },
      ],
    },
    {
      field: 'confirmNewPassword',
      label: '确认密码',
      component: 'InputPassword',
      dynamicRules: ({ values }) => {
        return [
          {
            required: true,
            validator: (_, value) => {
              if (!value) {
                return Promise.reject('密码不能为空');
              }
              if (value !== values.newPassword) {
                return Promise.reject('两次输入的密码不一致!');
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
    labelWidth: 100,
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
        showMessage(t('您填写的信息有误，请根据提示修正。'));
      }
      console.log('error', error);
    }
  }
</script>
