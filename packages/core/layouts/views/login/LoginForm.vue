<template>
  <div v-if="getShow">
    <Form class="enter-x p-4" :model="formData" :rules="getFormRules" ref="formRef" @keypress.enter="handleLogin">
      <div class="font-size mb-5 text-center font-size-4 text-red">
        <div v-if="demoMode">
          💡提示：当前您连接的后端服务，可能是
          <a class="!text-red" href="https://vue.jeesite.com" target="_blank"> vue.jeesite.com </a> <br />
          这是一个演示服务器，请进入文档：《
          <a
            class="!text-red"
            href="https://jeesite.com/docs/vue-install-deploy/#%E9%85%8D%E7%BD%AE%E5%90%8E%E7%AB%AF%E6%8E%A5%E5%8F%A3"
            target="_blank"
          >
            配置服务端接口
          </a>
          》
        </div>
      </div>
      <FormItem name="account" class="enter-x">
        <Input
          size="large"
          v-model:value="formData.account"
          :placeholder="t('sys.login.account')"
          class="fix-auto-fill"
        />
      </FormItem>
      <FormItem name="password" class="enter-x">
        <InputPassword
          size="large"
          visibilityToggle
          v-model:value="formData.password"
          :placeholder="t('sys.login.password')"
          autoComplete="off"
        />
      </FormItem>
      <FormItem v-if="validCodeRefreshTime" name="validCode" class="enter-x valid-code">
        <ValidCode size="large" v-model:value="formData.validCode" :refreshTime="validCodeRefreshTime" />
      </FormItem>

      <ARow class="enter-x">
        <ACol :span="12">
          <FormItem>
            <!-- No logic, you need to deal with it yourself -->
            <Checkbox v-model:checked="rememberMe" size="small">
              {{ t('sys.login.rememberMe') }}
            </Checkbox>
          </FormItem>
        </ACol>
        <ACol :span="12">
          <FormItem :style="{ 'text-align': 'right' }">
            <Button type="link" size="small" @click="setLoginState(LoginStateEnum.RESET_PASSWORD)">
              {{ t('sys.login.forgetPassword') }}
            </Button>
            <!--<Button type="link" size="small" @click="setLoginState(LoginStateEnum.REGISTER)">
              {{ t('sys.login.registerButton') }}
            </Button>-->
          </FormItem>
        </ACol>
      </ARow>

      <FormItem class="enter-x">
        <Button type="primary" size="large" block @click="handleLogin" :loading="loading">
          {{ t('sys.login.loginButton') }}
        </Button>
        <Button size="large" class="mt-4 enter-x" block @click="setLoginState(LoginStateEnum.REGISTER)">
          {{ t('sys.login.registerButton') }}
        </Button>
      </FormItem>

      <Divider class="enter-x">{{ t('sys.login.otherSignIn') }}</Divider>

      <div class="enter-x flex justify-evenly jeesite-login-sign-in-way">
        <Icon icon="i-simple-icons:gitee" color="#d81e06" size="28" @click="handleOauth2" />
        <Icon icon="i-ant-design:qq-circle-filled" color="#2178e3" size="32" @click="handleOauth2" />
        <Icon icon="i-ant-design:wechat-filled" color="#2eb60d" size="32" @click="handleOauth2" />
        <Icon icon="i-ant-design:github-filled" color="#2c2c2c" size="32" @click="handleOauth2" />
        <a href="https://gitee.com/thinkgem/jeesite-client" target="_blank" style="padding-top: 5px">
          <Icon icon="i-ant-design:windows-filled" size="32" style="vertical-align: middle" />
          <span class="pl-1" style="vertical-align: middle"> {{ t('客户端下载') }}</span>
        </a>
      </div>
    </Form>
  </div>
</template>
<script lang="ts" setup>
  import { reactive, ref, toRaw, unref, computed, onMounted, shallowRef } from 'vue';

  import { Checkbox, Form, FormItem, Input, Row, Col, Button, Divider, message } from 'antdv-next';
  import { Icon } from '@jeesite/core/components/Icon';

  import { useI18n } from '@jeesite/core/hooks/web/useI18n';
  import { useMessage } from '@jeesite/core/hooks/web/useMessage';

  import { useUserStore } from '@jeesite/core/store/modules/user';
  import { LoginStateEnum, useLoginState, useFormRules, useFormValid } from './useLogin';
  import { userInfoApi } from '@jeesite/core/api/sys/login';
  import { ValidCode } from '@jeesite/core/components/ValidCode';

  const ACol = Col;
  const ARow = Row;
  const InputPassword = Input.Password;
  const { t } = useI18n();
  const { showMessage, notification } = useMessage();
  const userStore = useUserStore();

  const { setLoginState, getLoginState } = useLoginState();

  const formRef = shallowRef<InstanceType<typeof Form>>();
  const loading = ref(false);
  const rememberMe = ref(false);
  const validCodeRefreshTime = ref(0);
  const demoMode = ref(false);

  const emit = defineEmits(['demoMode', 'useCorpModel', 'loginCodeCorpUnique', 'corpOptions']);

  const formData = reactive({
    account: 'system',
    password: '',
    validCode: '',
  });

  const { getFormRules } = useFormRules(formData);
  const { validForm } = useFormValid(formRef);

  //onKeyStroke('Enter', handleLogin);

  const getShow = computed(() => unref(getLoginState) === LoginStateEnum.LOGIN);

  onMounted(async () => {
    setTimeout(() => message.destroy());
    try {
      const res = await userInfoApi('none');
      if (res.result == 'true') {
        // 如果已经登录，根据业务需要，是否自动跳转到系统首页
        await userStore.afterLoginAction(res, true);
        return;
      }
      userStore.initPageCache(res);
      demoMode.value = res.demoMode || false;
      emit('demoMode', demoMode.value);
      if (res.isValidCodeLogin) {
        validCodeRefreshTime.value = new Date().getTime();
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
    }
  });

  async function handleLogin() {
    try {
      const data = await validForm();
      if (!data) return;
      loading.value = true;
      const res = await userStore.login(
        toRaw({
          password: data.password,
          username: data.account,
          validCode: data.validCode,
          rememberMe: unref(rememberMe.value),
        }),
      );
      if (res.isValidCodeLogin) {
        validCodeRefreshTime.value = new Date().getTime();
      }
      if (res.result === 'true') {
        notification.success({
          title: t('sys.login.loginSuccessTitle'),
          description: `${t('sys.login.loginSuccessDesc')}: ${res.user.userName}`,
          duration: 1,
        });
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

  function handleOauth2(event: Event) {
    window.location.href = 'https://vue.jeesite.com/js/oauth2/login/gitee?state=vue';
    event.preventDefault();
  }
</script>
