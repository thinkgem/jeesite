<template>
  <PageWrapper>
    <template #headerTitle>
      <Icon :icon="getTitle.icon" class="m-1 pr-1" />
      <span> {{ getTitle.value }} </span>
    </template>
    <ScrollContainer>
      <div ref="wrapperRef" class="account-setting">
        <Tabs tab-position="left" :tabBarStyle="{ width: '220px' }">
          <Tabs.TabPane key="1" tab="基本设置">
            <UserInfo />
          </Tabs.TabPane>
          <Tabs.TabPane key="2" tab="安全设置">
            <SecureSettings />
          </Tabs.TabPane>
          <Tabs.TabPane key="3" tab="账号绑定">
            <Oauth2Binder />
          </Tabs.TabPane>
        </Tabs>
      </div>
    </ScrollContainer>
  </PageWrapper>
</template>
<script lang="ts">
  export default defineComponent({
    name: 'AccountCenter',
  });
</script>
<script lang="ts" setup>
  import { defineComponent } from 'vue';
  import { Tabs } from 'ant-design-vue';
  import { useI18n } from '/@/hooks/web/useI18n';
  import { Icon } from '/@/components/Icon';
  import { PageWrapper } from '/@/components/Page';
  import { ScrollContainer } from '/@/components/Container/index';

  import UserInfo from './userInfo.vue';
  import SecureSettings from './secureSettings.vue';
  import Oauth2Binder from './oauth2Binder.vue';

  const { t } = useI18n();
  const getTitle = {
    icon: 'ion:person-outline',
    value: t('sys.account.center'),
  };
</script>
<style lang="less">
  .account-setting {
    .base-title {
      padding-left: 0;
    }

    .ant-tabs-tab-active {
      background-color: @item-active-bg;
    }
  }
</style>
