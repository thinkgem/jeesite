<template>
  <LoginFormTitle v-show="getShow" class="enter-x" />
  <Form
    class="p-4 enter-x"
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
      Tip：发送 <a href="https://gitee.com/thinkgem/jeesite4" target="_blank">JeeSite</a> 和
      <a href="https://gitee.com/thinkgem/jeesite-vue" target="_blank">JeeSite Vue</a> 仓库 Star
      截图到邮箱：<br />&nbsp; &nbsp; &nbsp; &nbsp; &nbsp;
      <span @click="selectText('st')" id="st">jeesite_demo@163.com</span>
      免费 获取账号密码
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
      <ACol :md="8" :xs="24" class="!my-2 !md:my-0 xs:mx-0 md:mx-2">
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

    <div class="flex justify-evenly enter-x" :class="`${prefixCls}-sign-in-way`">
      <GithubFilled />
      <WechatFilled />
      <AlipayCircleFilled />
      <GoogleCircleFilled />
      <TwitterCircleFilled />
    </div>
  </Form>
</template>
<script lang="ts" setup>
  import { reactive, ref, toRaw, unref, computed, onMounted } from 'vue';

  import { Checkbox, Form, Input, Row, Col, Button, Divider } from 'ant-design-vue';
  import {
    GithubFilled,
    WechatFilled,
    AlipayCircleFilled,
    GoogleCircleFilled,
    TwitterCircleFilled,
  } from '@ant-design/icons-vue';
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
  const { notification } = useMessage();
  const { prefixCls } = useDesign('login');
  const { apiUrl } = useGlobSetting();
  const userStore = useUserStore();

  const { setLoginState, getLoginState } = useLoginState();
  const { getFormRules } = useFormRules();

  const formRef = ref();
  const loading = ref(false);
  const rememberMe = ref(false);
  const isValidCodeLogin = ref(false);

  const formData = reactive({
    account: 'system',
    password: 'admin',
    validCode: '',
  });

  const { validForm } = useFormValid(formRef);

  //onKeyStroke('Enter', handleLogin);

  const getShow = computed(() => unref(getLoginState) === LoginStateEnum.LOGIN);

  const getValidCodeImg = ref('');

  function refreshValidCodeImg() {
    getValidCodeImg.value =
      apiUrl + '/validCode' + '?__sid=' + userStore.getToken + '&t=' + new Date().getTime();
  }

  // is show jee site valid data.
  function refreshValidCodeStatus(res: Recordable) {
    isValidCodeLogin.value = res.isValidCodeLogin || false;
    if (isValidCodeLogin.value) {
      refreshValidCodeImg();
    }
  }

  onMounted(async () => {
    const res = await userInfoApi('none');
    refreshValidCodeStatus(res);
  });

  async function handleLogin() {
    const data = await validForm();
    if (!data) return;
    try {
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
          duration: 3,
        });
      }
    } catch (error) {
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

  const gp = location.href.indexOf('.jeesite.com') != -1 || import.meta.env.DEV;
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
