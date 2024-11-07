<template>
  <div :class="prefixCls" class="relative h-full w-full bg-light-400 px-4">
    <AppLocalePicker
      class="enter-x absolute right-4 top-4 text-white lg:text-gray-600"
      :showText="false"
      v-if="!sessionTimeout && showLocale"
    />
    <AppDarkModeToggle class="enter-x absolute right-7 top-3" v-if="!sessionTimeout" />

    <span class="-enter-x lg:hidden">
      <AppLogo :alwaysShowTitle="true" />
    </span>

    <div class="relative mx-auto h-full py-2 container">
      <div class="h-full flex">
        <div class="mr-4 min-h-full pl-4 md:hidden lg:w-13/24 lg:flex lg:flex-col">
          <div class="my-auto">
            <AppLogo class="-enter-x logo" />
            <img :alt="title" src="/@/assets/svg/login-box-bg.svg" class="-enter-x w-1/2" />
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
        <div class="h-full w-full flex overflow-auto py-5 lg:my-0 lg:h-auto lg:w-11/24 lg:py-0">
          <div
            :class="`${prefixCls}-form`"
            class="enter-x relative mx-auto my-auto w-full rounded-xl px-5 py-8 shadow-md lg:ml-16 lg:w-2/4 lg:w-auto sm:w-3/4 lg:px-10 lg:py-9 sm:px-8"
          >
            <LoginForm />
            <ForgetPasswordForm />
            <RegisterForm />
            <MobileForm />
            <QrCodeForm />
          </div>
        </div>
      </div>
    </div>
  </div>
</template>
<script lang="ts" setup>
  import { computed } from 'vue';
  import { AppLogo } from '/@/components/Application';
  import { AppLocalePicker, AppDarkModeToggle } from '/@/components/Application';
  import LoginForm from './LoginForm.vue';
  import ForgetPasswordForm from './ForgetPasswordForm.vue';
  import RegisterForm from './RegisterForm.vue';
  import MobileForm from './MobileForm.vue';
  import QrCodeForm from './QrCodeForm.vue';
  import { useGlobSetting } from '/@/hooks/setting';
  import { useDesign } from '/@/hooks/web/useDesign';
  import { useLocaleStore } from '/@/store/modules/locale';

  /* import { onMounted } from 'vue';
  import { useMessage } from '/@/hooks/web/useMessage';
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
  const localeStore = useLocaleStore();
  const showLocale = localeStore.getShowPicker;
  const title = computed(() => globSetting?.title ?? '');
</script>
<style lang="less">
  @prefix-cls: ~'jeesite-login';
  @logo-prefix-cls: ~'jeesite-app-logo';
  @countdown-prefix-cls: ~'jeesite-countdown-input';
  @dark-bg: #293146;

  html[data-theme='dark'] {
    .@{prefix-cls} {
      background-color: @dark-bg;

      &::before {
        background-image: url('/@/assets/svg/login-bg-dark.svg');
      }

      .ant-input,
      .ant-input-password {
        background-color: #232a3b;
      }

      .ant-btn:not(.ant-btn-link):not(.ant-btn-primary) {
        border: 1px solid #4a5569;
      }

      &-form {
        background: transparent !important;
        box-shadow: none;
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

  .@{prefix-cls} {
    min-height: 100%;
    overflow: hidden;
    //background-color: #f2fafd;

    @media (max-width: @screen-lg) {
      background-color: #3f60b5;

      .@{prefix-cls}-form {
        box-shadow: none;
      }
    }

    &-form {
      top: -20px;
      margin: auto;
      background-color: #fff;
      box-shadow: 0 0 8px #ddd;

      .ant-form-item {
        margin-bottom: 15px;
      }
    }

    &::before {
      position: absolute;
      top: 0;
      left: 0;
      width: 100%;
      height: 100%;
      margin-left: -48%;
      background-image: url('/@/assets/svg/login-bg.svg');
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
        font-size: 16px;
        color: #eee;
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
  }
</style>
