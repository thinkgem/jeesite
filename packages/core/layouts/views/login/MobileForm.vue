<template>
  <template v-if="getShow">
    <LoginFormTitle class="enter-x" />
    <div class="gp mb-2 mt-4" v-if="demoMode"> Tip：演示系统未开放手机登录，请联系官方人员获取账号密码。 </div>
    <Form class="enter-x p-4" :model="formData" :rules="getFormRules" ref="formRef">
      <FormItem name="mobile" class="enter-x">
        <Input
          size="large"
          v-model:value="formData.mobile"
          :placeholder="t('sys.login.mobile')"
          class="fix-auto-fill"
        />
      </FormItem>
      <FormItem name="validCode" class="enter-x valid-code">
        <ValidCode size="large" v-model:value="formData.validCode" :refreshTime="validCodeRefreshTime" />
      </FormItem>
      <FormItem name="loginValidCode" class="enter-x">
        <CountdownInput
          size="large"
          class="fix-auto-fill"
          v-model:value="formData.loginValidCode"
          :placeholder="t('sys.login.smsCode')"
          :sendCodeApi="handleSendCodeApi"
        />
      </FormItem>
      <FormItem v-if="userOptions.length > 0" name="selectLoginCode" class="enter-x">
        <Select
          showSearch
          :options="userOptions"
          v-model:value="formData.selectLoginCode"
          :placeholder="t('sys.login.userPlaceholder')"
          size="large"
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
  import { reactive, ref, computed, unref, toRaw, onMounted } from 'vue';
  import { Form, Input, Button } from 'ant-design-vue';
  import { CountdownInput } from '@jeesite/core/components/CountDown';
  import LoginFormTitle from './LoginFormTitle.vue';
  import { useI18n } from '@jeesite/core/hooks/web/useI18n';
  import { useLoginState, useFormRules, useFormValid, LoginStateEnum } from './useLogin';
  import { ValidCode } from '@jeesite/core/components/ValidCode';
  import { getLoginValidCode, loginByValidCode } from '@jeesite/core/api/sys/account';
  import { useMessage } from '@jeesite/core/hooks/web/useMessage';
  import { Select } from '@jeesite/core/components/Form';
  import { useUserStore } from '@jeesite/core/store/modules/user';

  const props = defineProps({
    demoMode: { type: Boolean, default: false },
  });

  const FormItem = Form.Item;
  const { t } = useI18n();
  const { handleBackLogin, getLoginState } = useLoginState();
  const { showMessage, notification } = useMessage();
  const userStore = useUserStore();

  const formRef = ref();
  const loading = ref(false);
  const validCodeRefreshTime = ref(0);
  const userOptions = ref<Recordable[]>([]);

  const formData = reactive({
    mobile: '',
    validCode: '',
    loginValidCode: '',
    selectLoginCode: '',
  });

  const { getFormRules } = useFormRules(formData);
  const { validForm } = useFormValid(formRef);

  const getShow = computed(() => unref(getLoginState) === LoginStateEnum.MOBILE);

  async function handleSendCodeApi() {
    const data = await getLoginValidCode(formData);
    showMessage(data.message);
    if (data.result == 'true') {
      if (data.extMessage && data.userList) {
        showMessage(data.extMessage, 'warning');
        userOptions.value = data.userList.map((user) => ({
          label: user.userName + ' (' + user.loginCode + ')',
          value: user.loginCode,
        }));
      } else {
        userOptions.value = [];
      }
    } else {
      validCodeRefreshTime.value = new Date().getTime();
    }
    return data.result == 'true';
  }

  async function handleLogin() {
    try {
      const data = await validForm();
      if (!data) return;
      loading.value = true;
      const res = await loginByValidCode(data);
      if (res.result == 'true') {
        // 如果已经登录，根据业务需要，是否自动跳转到系统首页
        await userStore.afterLoginAction(res, true);
        notification.success({
          message: t('sys.login.loginSuccessTitle'),
          description: `${t('sys.login.loginSuccessDesc')}: ${res.user.userName}`,
          duration: 1,
        });
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
