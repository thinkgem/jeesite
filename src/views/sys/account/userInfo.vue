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
            :btnProps="{ preIcon: 'ant-design:cloud-upload-outlined' }"
            @change="updateAvatar"
            width="150"
          />
        </div>
      </ACol>
    </ARow>
    <div class="ml-30">
      <Button type="primary" @click="handleSubmit">
        <Icon icon="ant-design:check-outlined" /> {{ t('sys.account.updateBtn') }}
      </Button>
    </div>
  </CollapseContainer>
</template>
<script lang="ts" setup>
  import { computed, onMounted, ref } from 'vue';
  import { Button, Row, Col } from 'ant-design-vue';
  import { useI18n } from '/@/hooks/web/useI18n';
  import { useMessage } from '/@/hooks/web/useMessage';
  import { Icon } from '/@/components/Icon';
  import { BasicForm, FormSchema, useForm } from '/@/components/Form/index';
  import { CollapseContainer } from '/@/components/Container';
  import { CropperAvatar } from '/@/components/Cropper';
  import headerImg from '/@/assets/images/header.jpg';
  import { useUserStore } from '/@/store/modules/user';
  import { userInfo, infoSaveBase } from '/@/api/sys/user';
  // import { uploadApi } from '/@/api/sys/upload';

  const { t } = useI18n();
  const { showMessage } = useMessage();
  const avatarBase64 = ref<String>('');
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
      const userInfoRes = await userInfo();
      const user = userInfoRes.user;
      if (avatarBase64.value != '') {
        user.avatarUrl = avatarBase64.value;
      }
      userStore.setUserInfo(userInfoRes);
      showMessage(res.message);
    } catch (error: any) {
      if (error && error.errorFields) {
        showMessage(t('common.validateError'));
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
