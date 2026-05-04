<template>
  <div v-if="getShow">
    <div class="enter-x min-h-80 text-center pt-2">
      <QrCode :value="qrCodeUrl" class="enter-x flex justify-center xl:justify-start" :width="280" />
      <Divider class="enter-x size">{{ t('专业版支持微信扫描二维码登录！') }}</Divider>
      <Button size="large" block class="enter-x mt-4" @click="handleBackLogin">
        {{ t('sys.login.backSignIn') }}
      </Button>
    </div>
  </div>
</template>
<script lang="ts" setup>
  import { computed, unref } from 'vue';
  import { Button, Divider } from 'antdv-next';
  import { QrCode } from '@jeesite/core/components/Qrcode';
  import { useI18n } from '@jeesite/core/hooks/web/useI18n';
  import { useLoginState, LoginStateEnum } from './useLogin';

  const qrCodeUrl = 'https://jeesite.com';

  const { t } = useI18n();
  const { handleBackLogin, getLoginState } = useLoginState();

  const getShow = computed(() => unref(getLoginState) === LoginStateEnum.QR_CODE);
</script>
