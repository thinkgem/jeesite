<template>
  <div :class="prefixCls" class="relative h-full w-full bg-light-400 px-4">
    <AppDarkModeToggle class="enter-x absolute right-12 top-5" v-if="!sessionTimeout" />

    <span class="-enter-x lg:hidden">
      <AppLogo :alwaysShowTitle="true" />
    </span>

    <div class="relative mx-auto h-full py-2 container">
      <div class="h-full flex">
        <div class="mr-4 min-h-full pl-4 md:hidden lg:w-[54.17%] lg:flex lg:flex-col">
          <div class="my-auto">
            <AppLogo class="-enter-x logo" />
            <img :alt="title" src="@jeesite/assets/svg/login-box-bg.svg" class="-enter-x w-1/2" />
            <div class="-enter-x mt-10 text-white font-medium">
              <span class="mt-4 inline-block text-3xl"></span>
            </div>
            <div class="-enter-x text-md mt-5 text-white font-normal dark:text-gray-500">
              JeeSite 是一个专业的平台，是一个让你使用放心的平台。<br />
              前端基于 Vue3、Vite、TypeScript、Ant-Design-Vue、Vben Admin，<br />
              后台基于 Spring Boot、Apache MyBatis 等，最先进、最经典的技术栈。<br />
              精致的 UI、规范的代码书写、匠心著作、封装细节、专注业务、快速开发。<br />
            </div>
          </div>
        </div>
        <div class="h-full w-full flex overflow-y-auto overflow-x-hidden py-5 lg:my-0 lg:h-auto lg:w-[45.83%] lg:py-0">
          <div
            :class="`${prefixCls}-form`"
            class="enter-x relative mx-auto my-auto w-full px-5 py-8 shadow-md lg:ml-16 lg:w-1/2 lg:w-auto sm:w-3/4 lg:px-10 lg:py-9 sm:px-8"
          >
            <Tabs v-if="getShow" :activeKey="getLoginState as unknown as string" @change="handleChange">
              <TabPane :key="LoginStateEnum.LOGIN" :tab="t('sys.login.signInFormTitle')">
                <LoginForm @demo-mode="demoMode = $event" />
              </TabPane>
              <TabPane :key="LoginStateEnum.MOBILE" :tab="t('sys.login.mobileSignInFormTitle')">
                <MobileForm :demoMode="demoMode" />
              </TabPane>
              <TabPane :key="LoginStateEnum.QR_CODE" :tab="t('sys.login.qrSignInFormTitle')">
                <QrCodeForm :demoMode="demoMode" />
              </TabPane>
            </Tabs>
            <ForgetPasswordForm :demoMode="demoMode" />
            <RegisterForm :demoMode="demoMode" />
          </div>
        </div>
      </div>
    </div>
  </div>
</template>
<script lang="ts" setup>
  import { computed, ref, unref } from 'vue';
  import { AppLogo } from '@jeesite/core/components/Application';
  import { AppDarkModeToggle } from '@jeesite/core/components/Application';
  import { useGlobSetting } from '@jeesite/core/hooks/setting';
  import { useDesign } from '@jeesite/core/hooks/web/useDesign';
  import { Tabs, TabPane } from 'antdv-next';
  import { useI18n } from '@jeesite/core/hooks/web/useI18n';

  import LoginForm from './LoginForm.vue';
  import MobileForm from './MobileForm.vue';
  import QrCodeForm from './QrCodeForm.vue';
  import ForgetPasswordForm from './ForgetPasswordForm.vue';
  import RegisterForm from './RegisterForm.vue';
  import { LoginStateEnum, useLoginState } from './useLogin';

  const { t } = useI18n();

  const { getLoginState, setLoginState } = useLoginState();

  const getShow = computed(() => {
    const lse = unref(getLoginState);
    return lse === LoginStateEnum.LOGIN || lse === LoginStateEnum.MOBILE || lse === LoginStateEnum.QR_CODE;
  });

  function handleChange(key: any) {
    setLoginState(key);
  }

  /* import { onMounted } from 'vue';
  import { useMessage } from '@jeesite/core/hooks/web/useMessage';
  const { createConfirm } = useMessage();
  onMounted(() => {
    if (!import.meta.env.DEV) {
      createConfirm({
        content: [
          '<div onclick="window.open(\'https://gitee.com/thinkgem/jeesite-vue\')">',
          '进入 <strong style="color: #FF0036;">JeeSite Vue</strong> 源码仓库页面，',
          '点右上角 <strong style="color: #FF0036;">Star</strong> 加星关注',
          '</div>',
        ].join(''),
        width: 480,
        iconType: 'info',
        maskClosable: false,
        cancelText: '我已 Star',
        okText: '带我去 Star',
        onOk: () => {
          window.open('https://gitee.com/thinkgem/jeesite-vue');
        },
      });
    }
  }); */

  defineProps({
    sessionTimeout: {
      type: Boolean,
    },
  });

  const globSetting = useGlobSetting();
  const { prefixCls } = useDesign('login');
  const title = computed(() => globSetting?.title ?? '');
  const demoMode = ref(false);
</script>
<style lang="less">
  @prefix-cls: ~'jeesite-login';
  @logo-prefix-cls: ~'jeesite-app-logo';
  @countdown-prefix-cls: ~'jeesite-countdown-input';
  @dark-bg: #293146;

  .@{prefix-cls} {
    min-height: 100%;
    overflow: hidden;
    //background-color: #f2fafd;

    &-form {
      transform: translateY(-20px);
      margin: auto;
      background-color: #fff;
      box-shadow: 0 0 8px #ddd;
      border-radius: 20px;

      .ant-form-item {
        margin-bottom: 15px;
      }

      .ant-tabs > .ant-tabs-nav {
        margin-bottom: 10px;

        &::before {
          border-bottom: 0;
        }

        .ant-tabs-tab {
          padding: 0 6px 5px;

          & + .ant-tabs-tab {
            margin: 0 0 0 15px;
          }

          .ant-tabs-tab-btn {
            font-size: 18px;
            opacity: 0.9;
          }

          &-active {
            .ant-tabs-tab-btn {
              opacity: 1;
            }
          }
        }
      }
    }

    &::before {
      position: absolute;
      top: 0;
      left: 0;
      width: 100%;
      height: 100%;
      margin-left: -48%;
      background-image: url('@jeesite/assets/svg/login-bg.svg');
      background-position: 100%;
      background-repeat: no-repeat;
      background-size: auto 100%;
      content: '';

      @media (max-width: @screen-lg) {
        display: none;
      }
    }

    .@{logo-prefix-cls} {
      // position: absolute;
      // top: 12px;
      // height: 30px;

      &.logo {
        margin-top: -110px;
        padding-bottom: 80px;
      }

      &__title {
        font-size: 18px;
      }

      img {
        width: 32px;
      }
    }

    .container {
      max-width: 1280px;

      .@{logo-prefix-cls} {
        display: flex;
        width: 60%;
        height: 80px;

        &__title {
          font-size: 28px;
          color: #eee;
        }

        img {
          width: 48px;
        }
      }
    }

    &-sign-in-way {
      .anticon,
      .iconfont {
        font-size: 28px;
        color: #888;
        cursor: pointer;

        &:hover {
          color: @primary-color;
        }
      }
    }

    input:not([type='checkbox']) {
      min-width: 360px;

      @media (max-width: @screen-xl) {
        min-width: 320px;
      }

      @media (max-width: @screen-lg) {
        min-width: 260px;
      }

      @media (max-width: @screen-md) {
        min-width: 240px;
      }

      @media (max-width: @screen-sm) {
        min-width: 160px;
      }
    }

    .valid-code input {
      min-width: auto;
    }

    .@{countdown-prefix-cls} input {
      min-width: unset;
    }

    .ant-divider-inner-text {
      font-size: 14px;
      color: @text-color-secondary;
    }

    .ant-btn {
      border-radius: 8px;
    }
  }

  html[data-theme='dark'] {
    .@{prefix-cls} {
      background-color: @dark-bg;

      &::before {
        background-image: url('@jeesite/assets/svg/login-bg-dark.svg');
      }

      .ant-input,
      .ant-input-password {
        background-color: #141822;
      }

      .ant-btn:not(.ant-btn-link):not(.ant-btn-primary) {
        border: 1px solid #4a5569;
      }

      &-form {
        background: #22283c !important;
        box-shadow: 0 0 8px #344466;
      }

      .@{logo-prefix-cls} {
        &__title {
          color: #eee;
        }
      }

      .jeesite-icon {
        color: #fff;
      }
    }

    input.fix-auto-fill,
    .fix-auto-fill input {
      -webkit-text-fill-color: #c9d1d9 !important;
      box-shadow: inherit !important;
    }
  }
</style>
