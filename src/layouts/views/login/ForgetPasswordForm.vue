<template>
  <template v-if="getShow">
    <LoginFormTitle class="enter-x" />
    <Form class="enter-x p-4" :model="formData" :rules="getFormRules" ref="formRef">
      <FormItem name="validType" class="enter-x">
        <Select
          :showSearch="false"
          :options="[
            { label: '使用手机号码找回您的密码', value: 'mobile' },
            { label: '使用电子邮箱找回您的密码', value: 'email' },
            { label: '使用保密问题找回您的密码', value: 'question' },
          ]"
          v-model:value="formData.validType"
          size="large"
        />
      </FormItem>
      <FormItem name="loginCode" class="enter-x">
        <Input size="large" v-model:value="formData.loginCode" :placeholder="t('sys.login.account')" />
      </FormItem>
      <FormItem name="validCode" class="enter-x valid-code">
        <ValidCode size="large" v-model:value="formData.validCode" :refreshTime="validCodeRefreshTime" />
      </FormItem>
      <FormItem name="fpValidCode" class="enter-x" v-if="formData.validType != 'question'">
        <CountdownInput
          size="large"
          class="fix-auto-fill"
          v-model:value="formData.fpValidCode"
          :placeholder="formData.validType == 'mobile' ? t('sys.login.smsCode') : t('sys.login.emailCode')"
          :sendCodeApi="handleSendCodeApi"
        />
      </FormItem>
      <FormItem name="getPwdQuestion" v-if="formData.validType == 'question'">
        <Button size="large" block @click="handleGetPwdQuestion">
          {{ t('sys.login.getPwdQuestion') }}
        </Button>
      </FormItem>
      <FormItem name="pwdQuestionAnswer" v-if="formData.validType == 'question'">
        <div class="pb-1">{{ t('sys.login.pwdQuestion') + 1 }}：{{ formData.pwdQuestion }}</div>
        <Input
          size="large"
          v-model:value="formData.pwdQuestionAnswer"
          :placeholder="t('sys.login.pwdQuestionAnswer') + 1"
        />
      </FormItem>
      <FormItem name="pwdQuestionAnswer2" v-if="formData.validType == 'question'">
        <div class="pb-1">{{ t('sys.login.pwdQuestion') + 2 }}：{{ formData.pwdQuestion2 }}</div>
        <Input
          size="large"
          v-model:value="formData.pwdQuestionAnswer2"
          :placeholder="t('sys.login.pwdQuestionAnswer') + 2"
        />
      </FormItem>
      <FormItem name="pwdQuestionAnswer3" class="enter-x" v-if="formData.validType == 'question'">
        <div class="pb-1">{{ t('sys.login.pwdQuestion') + 3 }}：{{ formData.pwdQuestion3 }}</div>
        <Input
          size="large"
          v-model:value="formData.pwdQuestionAnswer3"
          :placeholder="t('sys.login.pwdQuestionAnswer') + 3"
        />
      </FormItem>
      <FormItem name="password" class="enter-x">
        <StrengthMeter size="large" v-model:value="formData.password" :placeholder="t('sys.account.newPassword')" />
      </FormItem>
      <FormItem name="confirmPassword" class="enter-x">
        <Input.Password
          size="large"
          v-model:value="formData.confirmPassword"
          :placeholder="t('sys.account.confirmNewPassword')"
        />
      </FormItem>

      <FormItem class="enter-x">
        <Button type="primary" size="large" block @click="handleResetPwd" :loading="loading">
          {{ t('common.resetText') }}
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
  import LoginFormTitle from './LoginFormTitle.vue';
  import { Form, Input, Button } from 'ant-design-vue';
  import { useI18n } from '/@/hooks/web/useI18n';
  import { useLoginState, useFormRules, LoginStateEnum, useFormValid } from './useLogin';
  import { getFpValidCode, getPwdQuestion, savePwdByPwdQuestion, savePwdByValidCode } from '/@/api/sys/account';
  import { useMessage } from '/@/hooks/web/useMessage';
  import { CountdownInput } from '/@/components/CountDown';
  import { StrengthMeter } from '/@/components/StrengthMeter';
  import { ValidCode } from '/@/components/ValidCode';
  import { Select } from '/@/components/Form';

  const FormItem = Form.Item;
  const { t } = useI18n();
  const { handleBackLogin, getLoginState } = useLoginState();
  const { showMessage, showMessageModal } = useMessage();

  const formRef = ref();
  const loading = ref(false);
  const validCodeRefreshTime = ref(0);

  const formData = reactive({
    validType: '',
    loginCode: '',
    validCode: '',
    fpValidCode: '',
    pwdQuestion: '',
    pwdQuestionAnswer: '',
    pwdQuestion2: '',
    pwdQuestionAnswer2: '',
    pwdQuestion3: '',
    pwdQuestionAnswer3: '',
    password: '',
    confirmPassword: '',
  });

  const { getFormRules } = useFormRules(formData);
  const { validForm } = useFormValid(formRef);

  const getShow = computed(() => unref(getLoginState) === LoginStateEnum.RESET_PASSWORD);

  async function handleSendCodeApi() {
    const data = await getFpValidCode({
      validType: formData.validType,
      loginCode: formData.loginCode,
      validCode: formData.validCode,
    });
    showMessage(data.message);
    if (data.result != 'true') {
      validCodeRefreshTime.value = new Date().getTime();
    }
    return data.result == 'true';
  }

  async function handleGetPwdQuestion() {
    const data = await getPwdQuestion({
      loginCode: formData.loginCode,
      validCode: formData.validCode,
    });
    showMessage(data.message);
    if (data.result == 'true') {
      formData.pwdQuestion = data.pwdQuestion;
      formData.pwdQuestion2 = data.pwdQuestion2;
      formData.pwdQuestion3 = data.pwdQuestion3;
    }
  }

  async function handleResetPwd() {
    try {
      const data = await validForm();
      if (!data) return;
      loading.value = true;
      let res: Recordable;
      if (data.validType == 'question') {
        res = await savePwdByPwdQuestion(data);
      } else {
        res = await savePwdByValidCode(data);
      }
      if (res.result == 'true') {
        showMessageModal({ content: res.message });
        handleBackLogin();
      } else {
        showMessage(res.message);
      }
    } catch (error: any) {
      const err: string = error?.toString?.() ?? '';
      if (error?.code === 'ECONNABORTED' && err.indexOf('timeout of') !== -1) {
        showMessage(t('sys.api.apiTimeoutMessage'));
      } else if (err.indexOf('Network Error') !== -1) {
        showMessage(t('sys.api.networkExceptionMsg'));
      } else if (error?.code === 'ERR_BAD_RESPONSE') {
        showMessage(t('sys.api.apiRequestFailed'));
      }
      console.log(error);
    } finally {
      loading.value = false;
    }
  }
</script>
