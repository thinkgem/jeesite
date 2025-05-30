<template>
  <CollapseContainer :title="t('sys.account.basicTab')" :canExpan="false" class="overflow-x-hidden">
    <ARow :gutter="24" class="mt-3">
      <ACol :span="14">
        <BasicForm @register="register" />
      </ACol>
      <ACol :span="10">
        <div class="change-avatar mt-6">
          <CropperAvatar
            :value="avatar"
            :btnText="t('sys.account.changeAvatar')"
            :btnProps="{ preIcon: 'i-ant-design:cloud-upload-outlined' }"
            @change="updateAvatar"
            width="150"
          />
        </div>
      </ACol>
    </ARow>
    <div class="ml-30">
      <Button type="primary" @click="handleSubmit">
        <Icon icon="i-ant-design:check-outlined" /> {{ t('sys.account.updateBtn') }}
      </Button>
    </div>
  </CollapseContainer>
</template>
<script lang="ts" setup>
  import { computed, onMounted, ref } from 'vue';
  import { Button, Row, Col } from 'ant-design-vue';
  import { useI18n } from '@jeesite/core/hooks/web/useI18n';
  import { useMessage } from '@jeesite/core/hooks/web/useMessage';
  import { Icon } from '@jeesite/core/components/Icon';
  import { BasicForm, FormSchema, useForm } from '@jeesite/core/components/Form';
  import { CollapseContainer } from '@jeesite/core/components/Container';
  import { CropperAvatar } from '@jeesite/core/components/Cropper';
  import headerImg from '@jeesite/assets/images/header.jpg';
  import { useUserStore } from '@jeesite/core/store/modules/user';
  import { infoSaveBase } from '@jeesite/core/api/sys/user';
  import { userInfoApi } from '@jeesite/core/api/sys';
  // import { uploadApi } from '@jeesite/core/api/sys/upload';

  const { t } = useI18n();
  const { showMessage } = useMessage();
  const avatarBase64 = ref<string>('');
  const userStore = useUserStore();
  const ARow = Row;
  const ACol = Col;

  const userInfoSchemas: FormSchema[] = [
    {
      field: 'userName',
      component: 'Input',
      label: t('sys.account.userName'),
      colProps: { span: 18 },
    },
    {
      field: 'email',
      component: 'Input',
      label: t('sys.account.email'),
      colProps: { span: 18 },
    },
    {
      field: 'mobile',
      component: 'Input',
      label: t('sys.account.mobile'),
      colProps: { span: 18 },
    },
    {
      field: 'phone',
      component: 'Input',
      label: t('sys.account.phone'),
      colProps: { span: 18 },
    },
    {
      field: 'sign',
      component: 'InputTextArea',
      label: t('sys.account.sign'),
      colProps: { span: 18 },
    },
  ];

  const [register, { setFieldsValue, validate }] = useForm({
    labelWidth: 120,
    schemas: userInfoSchemas,
    showActionButtonGroup: false,
  });

  onMounted(async () => {
    const data = userStore.getUserInfo;
    // console.log(data);
    setFieldsValue(data);
  });

  const avatar = computed(() => {
    const { avatarUrl } = userStore.getUserInfo;
    return avatarUrl || headerImg;
  });

  function updateAvatar(source: string) {
    avatarBase64.value = source;
  }

  async function handleSubmit() {
    try {
      const data = await validate();
      if (avatarBase64.value != '') {
        data.avatarBase64 = avatarBase64.value;
      }
      // console.log('submit', data);
      const res = await infoSaveBase(data);
      const userInfoRes = await userInfoApi();
      const user = userInfoRes.user;
      if (avatarBase64.value != '') {
        user.avatarUrl = avatarBase64.value;
      }
      userStore.setUserInfo(userInfoRes);
      showMessage(res.message);
    } catch (error: any) {
      if (error && error.errorFields) {
        showMessage(error.message || t('common.validateError'));
      }
      console.log('error', error);
    }
  }
</script>
<style lang="less">
  .change-avatar {
    img {
      display: block;
      margin-bottom: 15px;
      border-radius: 50%;
    }
  }
</style>
