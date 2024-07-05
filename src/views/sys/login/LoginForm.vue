<template>
  <LoginFormTitle v-show="getShow" class="enter-x" />
  <Form
    class="enter-x p-4"
    :model="formData"
    :rules="getFormRules"
    ref="formRef"
    v-show="getShow"
    @keypress.enter="handleLogin"
  >
    <FormItem name="account" class="enter-x">
      <Input
        size="large"
        v-model:value="formData.account"
        :placeholder="t('sys.login.userName')"
        class="fix-auto-fill"
      />
    </FormItem>
    <FormItem name="password" class="enter-x">
      <InputPassword
        size="large"
        visibilityToggle
        v-model:value="formData.password"
        :placeholder="t('sys.login.password')"
        autocomplete="false"
      />
    </FormItem>
    <FormItem v-if="isValidCodeLogin" name="validCode" class="enter-x valid-code">
      <Input
        size="large"
        visibilityToggle
        v-model:value="formData.validCode"
        :placeholder="t('sys.login.validCode')"
      >
        <!-- addonAfter suffix -->
        <template #suffix>
          <img
            :src="getValidCodeImg"
            @click="refreshValidCodeImg"
            class="cursor-pointer"
            width="100"
          />
        </template>
      </Input>
    </FormItem>

    <div class="gp" v-if="gp">
      Tip：发送 <a href="https://gitee.com/thinkgem/jeesite5" target="_blank">JeeSite</a> 和
      <a href="https://gitee.com/thinkgem/jeesite-vue" target="_blank">JeeSite Vue</a> 仓库 Star
      截图到邮箱：<br />&nbsp; &nbsp; &nbsp; &nbsp; &nbsp;
      <span @click="selectText('st')" id="st">jeesite_demo@163.com</span>
      免费 获取账号密码。
    </div>

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
          <!-- No logic, you need to deal with it yourself -->
          <Button type="link" size="small" @click="setLoginState(LoginStateEnum.RESET_PASSWORD)">
            {{ t('sys.login.forgetPassword') }}
          </Button>
        </FormItem>
      </ACol>
    </ARow>

    <FormItem class="enter-x">
      <Button type="primary" size="large" block @click="handleLogin" :loading="loading">
        {{ t('sys.login.loginButton') }}
      </Button>
      <!-- <Button size="large" class="mt-4 enter-x" block @click="handleRegister">
        {{ t('sys.login.registerButton') }}
      </Button> -->
    </FormItem>
    <ARow class="enter-x md:pl-3">
      <ACol :md="7" :xs="24">
        <Button block @click="setLoginState(LoginStateEnum.MOBILE)">
          {{ t('sys.login.mobileSignInFormTitle') }}
        </Button>
      </ACol>
      <ACol :md="8" :xs="24" class="xs:mx-0 !my-2 md:mx-2 !md:my-0">
        <Button block @click="setLoginState(LoginStateEnum.QR_CODE)">
          {{ t('sys.login.qrSignInFormTitle') }}
        </Button>
      </ACol>
      <ACol :md="7" :xs="24">
        <Button block @click="setLoginState(LoginStateEnum.REGISTER)">
          {{ t('sys.login.registerButton') }}
        </Button>
      </ACol>
    </ARow>

    <Divider class="enter-x">{{ t('sys.login.otherSignIn') }}</Divider>

    <div class="enter-x flex justify-evenly" :class="`${prefixCls}-sign-in-way`">
      <Icon icon="i-ant-design:qq-circle-filled" size="32" @click="handleOauth2" />
      <Icon icon="i-ant-design:wechat-filled" size="32" @click="handleOauth2" />
      <Icon icon="i-ant-design:github-filled" size="32" @click="handleOauth2" />
      <a href="https://gitee.com/thinkgem/jeesite-client" target="_blank">
        <Icon icon="i-ant-design:windows-filled" size="32" style="vertical-align: middle" />
        <span class="pl-1" style="vertical-align: middle"> {{ t('客户端下载') }}</span>
      </a>
    </div>
  </Form>
</template>
<script lang="ts" setup>
  import { reactive, ref, toRaw, unref, computed, onMounted } from 'vue';

  import { Checkbox, Form, Input, Row, Col, Button, Divider, message } from 'ant-design-vue';
  import { Icon } from '/@/components/Icon';
  import LoginFormTitle from './LoginFormTitle.vue';

  import { useI18n } from '/@/hooks/web/useI18n';
  import { useMessage } from '/@/hooks/web/useMessage';

  import { useUserStore } from '/@/store/modules/user';
  import { LoginStateEnum, useLoginState, useFormRules, useFormValid } from './useLogin';
  import { useDesign } from '/@/hooks/web/useDesign';
  import { useGlobSetting } from '/@/hooks/setting';
  import { userInfoApi } from '/@/api/sys/login';
  // import { onKeyStroke } from '@vueuse/core';

  const ACol = Col;
  const ARow = Row;
  const FormItem = Form.Item;
  const InputPassword = Input.Password;
  const { t } = useI18n();
  const { showMessage, notification } = useMessage();
  const { prefixCls } = useDesign('login');
  const { ctxPath } = useGlobSetting();
  const userStore = useUserStore();

  const { setLoginState, getLoginState } = useLoginState();
  const { getFormRules } = useFormRules();

  const formRef = ref();
  const loading = ref(false);
  const rememberMe = ref(false);
  const isValidCodeLogin = ref(false);
  const gp = ref(false);

  const formData = reactive({
    account: 'system',
    password: '',
    validCode: '',
  });

  const { validForm } = useFormValid(formRef);

  //onKeyStroke('Enter', handleLogin);

  const getShow = computed(() => unref(getLoginState) === LoginStateEnum.LOGIN);

  const getValidCodeImg = ref('');

  function refreshValidCodeImg() {
    getValidCodeImg.value =
      ctxPath + '/validCode' + '?__sid=' + userStore.getToken + '&t=' + new Date().getTime();
  }

  // is show jee site valid data.
  function refreshValidCodeStatus(res: Recordable) {
    isValidCodeLogin.value = res.isValidCodeLogin || false;
    if (isValidCodeLogin.value) {
      refreshValidCodeImg();
    }
  }

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
      refreshValidCodeStatus(res);
      gp.value = res.demoMode || false;
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
      refreshValidCodeStatus(res);
      if (res.result === 'true') {
        notification.success({
          message: t('sys.login.loginSuccessTitle'),
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

  function selectText(id: string) {
    const text = document.getElementById(id);
    const selection = window.getSelection();
    const range = document.createRange();
    if (text && selection) {
      range.selectNodeContents(text);
      selection.removeAllRanges();
      selection.addRange(range);
    }
  }

  function handleOauth2(event: Event) {
    showMessage('未开放第三方登录，看 OAuth2 演示，请访问 demo.jeesite.com ');
    event.preventDefault();
  }
</script>
<style>
  .gp {
    padding-bottom: 15px;
    font-size: 16px;
  }

  .gp,
  .gp a {
    color: #d21919;
  }
</style>
