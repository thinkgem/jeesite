<template>
  <template v-if="getShow">
    <LoginFormTitle class="enter-x" />
    <div class="gp mb-2 mt-4" v-if="demoMode">
      Tip：演示系统无需注册，请联系官方人员获取账号密码。
    </div>
    <Form class="enter-x p-4" :model="formData" :rules="getFormRules" ref="formRef">
      <FormItem name="validType" class="enter-x">
        <Select
          :showSearch="false"
          :options="[
            { label: '使用手机号码找回您的密码', value: 'mobile' },
            { label: '使用电子邮箱找回您的密码', value: 'email' },
          ]"
          v-model:value="formData.validType"
          size="large"
        />
      </FormItem>
      <FormItem name="loginCode" class="enter-x">
        <Input
          class="fix-auto-fill"
          size="large"
          v-model:value="formData.loginCode"
          :placeholder="t('sys.login.account')"
        />
      </FormItem>
      <FormItem name="userName" class="enter-x">
        <Input
          class="fix-auto-fill"
          size="large"
          v-model:value="formData.userName"
          :placeholder="t('sys.login.userName')"
        />
      </FormItem>
      <FormItem name="mobile" class="enter-x" v-if="formData.validType == 'mobile'">
        <Input
          size="large"
          v-model:value="formData.mobile"
          :placeholder="t('sys.login.mobile')"
          class="fix-auto-fill"
        />
      </FormItem>
      <FormItem name="email" class="enter-x" v-if="formData.validType == 'email'">
        <Input
          size="large"
          v-model:value="formData.email"
          :placeholder="t('sys.login.email')"
          class="fix-auto-fill"
        />
      </FormItem>
      <FormItem name="validCode" class="enter-x valid-code">
        <ValidCode
          size="large"
          v-model:value="formData.validCode"
          :refreshTime="validCodeRefreshTime"
        />
      </FormItem>
      <FormItem name="regValidCode" class="enter-x">
        <CountdownInput
          size="large"
          class="fix-auto-fill"
          v-model:value="formData.regValidCode"
          :placeholder="
            formData.validType == 'mobile' ? t('sys.login.smsCode') : t('sys.login.emailCode')
          "
          :sendCodeApi="handleSendCodeApi"
        />
      </FormItem>
      <FormItem name="password" class="enter-x">
        <StrengthMeter
          size="large"
          v-model:value="formData.password"
          :placeholder="t('sys.login.password')"
        />
      </FormItem>
      <FormItem name="confirmPassword" class="enter-x">
        <InputPassword
          size="large"
          visibilityToggle
          v-model:value="formData.confirmPassword"
          :placeholder="t('sys.login.confirmPassword')"
          autocomplete="off"
        />
      </FormItem>

      <FormItem class="enter-x" name="policy">
        <!-- No logic, you need to deal with it yourself -->
        <Checkbox v-model:checked="formData.policy" size="small">
          {{ t('sys.login.policy') }}
        </Checkbox>
      </FormItem>

      <Button
        type="primary"
        class="enter-x"
        size="large"
        block
        @click="handleRegister"
        :loading="loading"
      >
        {{ t('sys.login.registerButton') }}
      </Button>
      <Button size="large" block class="enter-x mt-4" @click="handleBackLogin">
        {{ t('sys.login.backSignIn') }}
      </Button>
    </Form>
  </template>
</template>
<script lang="ts" setup>
  import { reactive, ref, unref, computed } from 'vue';
  import LoginFormTitle from './LoginFormTitle.vue';
  import { Form, Input, Button, Checkbox } from 'ant-design-vue';
  import { StrengthMeter } from '/@/components/StrengthMeter';
  import { CountdownInput } from '/@/components/CountDown';
  import { useI18n } from '/@/hooks/web/useI18n';
  import { useMessage } from '/@/hooks/web/useMessage';
  import { useLoginState, useFormRules, useFormValid, LoginStateEnum } from './useLogin';
  import { Select } from '/@/components/Form';
  import { ValidCode } from '/@/components/ValidCode';
  import { getRegValidCode, saveRegByValidCode } from '/@/api/sys/account';

  const props = defineProps({
    demoMode: { type: Boolean, default: false },
  });

  const FormItem = Form.Item;
  const InputPassword = Input.Password;
  const { t } = useI18n();
  const { showMessage, showMessageModal } = useMessage();
  const { handleBackLogin, getLoginState } = useLoginState();

  const formRef = ref();
  const loading = ref(false);
  const validCodeRefreshTime = ref(0);

  const formData = reactive({
    validType: '',
    loginCode: '',
    userName: '',
    mobile: '',
    email: '',
    userType: 'member',
    validCode: '',
    regValidCode: '',
    password: '',
    confirmPassword: '',
    policy: false,
  });

  const { getFormRules } = useFormRules(formData);
  const { validForm } = useFormValid(formRef);

  const getShow = computed(() => unref(getLoginState) === LoginStateEnum.REGISTER);

  async function handleSendCodeApi() {
    const data = await getRegValidCode({
      validType: formData.validType,
      loginCode: formData.loginCode,
      userName: formData.userName,
      email: formData.email,
      mobile: formData.mobile,
      userType: formData.userType,
      validCode: formData.validCode,
    });
    showMessage(data.message);
    if (data.result != 'true') {
      validCodeRefreshTime.value = new Date().getTime();
    }
  }

  async function handleRegister() {
    try {
      const data = await validForm();
      if (!data) return;
      loading.value = true;
      const res = await saveRegByValidCode(data);
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
