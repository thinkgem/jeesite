<template>
  <BasicModal
    :footer="null"
    :title="t('layout.header.lockScreen')"
    v-bind="$attrs"
    :class="prefixCls"
    @register="register"
  >
    <div :class="`${prefixCls}__entry`">
      <div :class="`${prefixCls}__header`">
        <img :src="avatar" :class="`${prefixCls}__header-img`" />
        <p :class="`${prefixCls}__header-name`">
          {{ getRealName }}
        </p>
      </div>

      <BasicForm @register="registerForm" />

      <div :class="`${prefixCls}__footer`">
        <a-button type="primary" block class="mt-2" @click="handleLock">
          {{ t('layout.header.lockScreenBtn') }}
        </a-button>
      </div>
    </div>
  </BasicModal>
</template>
<script lang="ts">
  import { defineComponent, computed } from 'vue';
  import { useI18n } from '/@/hooks/web/useI18n';
  import { useDesign } from '/@/hooks/web/useDesign';
  import { BasicModal, useModalInner } from '/@/components/Modal';
  import { BasicForm, useForm } from '/@/components/Form';

  import { useUserStore } from '/@/store/modules/user';
  import { useLockStore } from '/@/store/modules/lock';

  export default defineComponent({
    name: 'LockModal',
    components: { BasicModal, BasicForm },

    setup() {
      const { t } = useI18n();
      const { prefixCls } = useDesign('header-lock-modal');
      const userStore = useUserStore();
      const lockStore = useLockStore();

      const getRealName = computed(() => userStore.getUserInfo?.userName);
      const [register, { closeModal }] = useModalInner();

      const [registerForm, { validateFields, resetFields }] = useForm({
        showActionButtonGroup: false,
        labelWidth: 100,
        schemas: [
          {
            field: 'password',
            label: t('layout.header.lockScreenPassword'),
            component: 'InputPassword',
            required: true,
          },
        ],
        baseColProps: { md: 23, lg: 23 },
      });

      async function handleLock() {
        const values = (await validateFields()) as any;
        const password: string | undefined = values.password;
        closeModal();

        lockStore.setLockInfo({
          isLock: true,
          pwd: password,
        });
        await resetFields();
      }

      const avatar = computed(() => {
        const { avatarUrl } = userStore.getUserInfo;
        return avatarUrl;
      });

      return {
        t,
        prefixCls,
        getRealName,
        register,
        registerForm,
        handleLock,
        avatar,
      };
    },
  });
</script>
<style lang="less">
  @prefix-cls: ~'jeesite-header-lock-modal';

  .@{prefix-cls} {
    &__entry {
      position: relative;
      //height: 240px;
      padding: 130px 30px 30px;
      border-radius: 10px;
    }

    &__header {
      position: absolute;
      top: 0;
      left: calc(50% - 45px);
      width: auto;
      text-align: center;

      &-img {
        width: 70px;
        border-radius: 50%;
      }

      &-name {
        margin-top: 5px;
      }
    }

    &__footer {
      text-align: center;
    }
  }
</style>
