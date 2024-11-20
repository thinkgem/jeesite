<template>
  <template v-if="getShow">
    <LoginFormTitle class="enter-x" />
    <div class="gp mb-2 mt-4"> Tip：演示系统未开放手机登录，请联系官方人员获取账号密码。 </div>
    <Form class="enter-x p-4" :model="formData" :rules="getFormRules" ref="formRef">
      <FormItem name="mobile" class="enter-x">
        <Input
          size="large"
          v-model:value="formData.mobile"
          :placeholder="t('sys.login.mobile')"
          class="fix-auto-fill"
        />
      </FormItem>
      <FormItem name="sms" class="enter-x">
        <CountdownInput
          size="large"
          class="fix-auto-fill"
          v-model:value="formData.sms"
          :placeholder="t('sys.login.smsCode')"
        />
      </FormItem>

      <FormItem class="enter-x">
        <Button type="primary" size="large" block @click="handleLogin" :loading="loading">
          {{ t('sys.login.loginButton') }}
        </Button>
        <Button size="large" block class="mt-4" @click="handleBackLogin">
          {{ t('sys.login.backSignIn') }}
        </Button>
      </FormItem>
    </Form>
  </template>
</template>
<script lang="ts" setup>
  import { reactive, ref, computed, unref } from 'vue';
  import { Form, Input, Button } from 'ant-design-vue';
  import { CountdownInput } from '@jeesite/core/components/CountDown';
  import LoginFormTitle from './LoginFormTitle.vue';
  import { useI18n } from '@jeesite/core/hooks/web/useI18n';
  import { useLoginState, useFormRules, useFormValid, LoginStateEnum } from './useLogin';

  const FormItem = Form.Item;
  const { t } = useI18n();
  const { handleBackLogin, getLoginState } = useLoginState();
  const { getFormRules } = useFormRules();

  const formRef = ref();
  const loading = ref(false);

  const formData = reactive({
    mobile: '',
    sms: '',
  });

  const { validForm } = useFormValid(formRef);

  const getShow = computed(() => unref(getLoginState) === LoginStateEnum.MOBILE);

  async function handleLogin() {
    const data = await validForm();
    if (!data) return;
    console.log(data);
  }
</script>
