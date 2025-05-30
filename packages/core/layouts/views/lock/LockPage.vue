<template>
  <div :class="prefixCls" class="fixed inset-0 h-screen w-screen flex items-center justify-center bg-black">
    <div
      :class="`${prefixCls}__unlock`"
      class="sm:text-md absolute left-1/2 top-0 h-16 flex flex-col translate-x-1/2 transform cursor-pointer items-center justify-center pt-5 text-white xl:text-xl"
      @click="handleShowForm(false)"
      v-show="showDate"
    >
      <LockOutlined />
      <span>{{ t('sys.lock.unlock') }}</span>
    </div>

    <div class="h-screen w-screen flex items-center justify-center">
      <div :class="`${prefixCls}__hour`" class="relative mr-5 h-2/5 w-2/5 md:mr-20 md:h-4/5">
        <span>{{ hour }}</span>
        <span class="text-md meridiem absolute left-5 top-5 xl:text-xl" v-show="showDate">
          {{ meridiem }}
        </span>
      </div>
      <div :class="`${prefixCls}__minute w-2/5 h-2/5 md:h-4/5 `">
        <span> {{ minute }}</span>
      </div>
    </div>
    <transition name="fade-slide">
      <div :class="`${prefixCls}-entry`" v-show="!showDate">
        <div :class="`${prefixCls}-entry-content`">
          <div :class="`${prefixCls}-entry__header enter-x`">
            <img :src="userinfo.avatarUrl || headerImg" :class="`${prefixCls}-entry__header-img`" />
            <p :class="`${prefixCls}-entry__header-name`">
              {{ userinfo.userName }}
            </p>
          </div>
          <InputPassword :placeholder="t('sys.lock.placeholder')" class="enter-x" v-model:value="password" />
          <span :class="`${prefixCls}-entry__err-msg enter-x`" v-if="errMsg">
            {{ t('sys.lock.alert') }}
          </span>
          <div :class="`${prefixCls}-entry__footer enter-x`">
            <a-button
              type="link"
              size="small"
              class="enter-x mr-2 mt-2"
              :disabled="loading"
              @click="handleShowForm(true)"
            >
              {{ t('common.back') }}
            </a-button>
            <a-button type="link" size="small" class="enter-x mr-2 mt-2" :disabled="loading" @click="goLogin">
              {{ t('sys.lock.backToLogin') }}
            </a-button>
            <a-button class="mt-2" type="link" size="small" @click="unLock()" :loading="loading">
              {{ t('sys.lock.entry') }}
            </a-button>
          </div>
        </div>
      </div>
    </transition>

    <div class="enter-y absolute bottom-5 w-full text-center text-gray-300 2xl:text-3xl xl:text-xl">
      <div class="enter-x mb-4 text-5xl" v-show="!showDate">
        {{ hour }}:{{ minute }} <span class="text-3xl">{{ meridiem }}</span>
      </div>
      <div class="text-2xl">{{ year }}/{{ month }}/{{ day }} {{ week }}</div>
    </div>
  </div>
</template>
<script lang="ts" setup>
  import { ref, computed } from 'vue';
  import { Input } from 'ant-design-vue';
  import { useUserStore } from '@jeesite/core/store/modules/user';
  import { useLockStore } from '@jeesite/core/store/modules/lock';
  import { useI18n } from '@jeesite/core/hooks/web/useI18n';
  import { useNow } from './useNow';
  import { useDesign } from '@jeesite/core/hooks/web/useDesign';
  import { LockOutlined } from '@ant-design/icons-vue';
  import headerImg from '@jeesite/assets/images/header.jpg';

  const InputPassword = Input.Password;

  const password = ref('');
  const loading = ref(false);
  const errMsg = ref(false);
  const showDate = ref(true);

  const { prefixCls } = useDesign('lock-page');
  const lockStore = useLockStore();
  const userStore = useUserStore();

  const { hour, month, minute, meridiem, year, day, week } = useNow(true);

  const { t } = useI18n();

  const userinfo = computed(() => {
    return userStore.getUserInfo || {};
  });

  /**
   * @description: unLock
   */
  async function unLock() {
    if (!password.value) {
      return;
    }
    let pwd = password.value;
    try {
      loading.value = true;
      const res = await lockStore.unLock(pwd);
      errMsg.value = !res;
    } finally {
      loading.value = false;
    }
  }

  function goLogin() {
    userStore.logout(true);
    lockStore.resetLockInfo();
  }

  function handleShowForm(show = false) {
    showDate.value = show;
  }
</script>
<style lang="less">
  @prefix-cls: ~'jeesite-lock-page';

  .@{prefix-cls} {
    z-index: @lock-page-z-index;

    &__unlock {
      transform: translate(-50%, 0);
    }

    &__hour,
    &__minute {
      display: flex;
      font-weight: 700;
      color: #bababa;
      background-color: #141313;
      border-radius: 30px;
      justify-content: center;
      align-items: center;

      @media screen and (max-width: @screen-md) {
        span:not(.meridiem) {
          font-size: 160px;
        }
      }

      @media screen and (min-width: @screen-md) {
        span:not(.meridiem) {
          font-size: 160px;
        }
      }

      @media screen and (max-width: @screen-sm) {
        span:not(.meridiem) {
          font-size: 90px;
        }
      }

      @media screen and (min-width: @screen-lg) {
        span:not(.meridiem) {
          font-size: 220px;
        }
      }

      @media screen and (min-width: @screen-xl) {
        span:not(.meridiem) {
          font-size: 260px;
        }
      }

      @media screen and (min-width: @screen-2xl) {
        span:not(.meridiem) {
          font-size: 320px;
        }
      }
    }

    &-entry {
      position: absolute;
      top: 0;
      left: 0;
      display: flex;
      width: 100%;
      height: 100%;
      background-color: rgb(0 0 0 / 50%);
      backdrop-filter: blur(8px);
      justify-content: center;
      align-items: center;

      &-content {
        width: 260px;
      }

      &__header {
        text-align: center;

        &-img {
          width: 70px;
          margin: 0 auto;
          border-radius: 50%;
        }

        &-name {
          margin-top: 5px;
          font-weight: 500;
          color: #bababa;
        }
      }

      &__err-msg {
        display: inline-block;
        margin-top: 10px;
        color: @error-color;
      }

      &__footer {
        display: flex;
        justify-content: space-between;
      }
    }
  }
</style>
