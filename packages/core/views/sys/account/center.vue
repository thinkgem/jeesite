<template>
  <PageWrapper>
    <template #headerTitle>
      <Icon :icon="getTitle.icon" class="m-1 pr-1" />
      <span> {{ getTitle.value }} </span>
    </template>
    <ScrollContainer>
      <div ref="wrapperRef" class="account-setting">
        <Tabs tab-position="left" :tabBarStyle="{ width: '220px' }">
          <Tabs.TabPane key="1" :tab="t('sys.account.basicTab')">
            <UserInfo />
          </Tabs.TabPane>
          <Tabs.TabPane key="2" :tab="t('sys.account.securityTab')">
            <SecureSettings />
          </Tabs.TabPane>
          <Tabs.TabPane key="3" :tab="t('sys.account.bindingTab')">
            <Oauth2Binder />
          </Tabs.TabPane>
        </Tabs>
      </div>
    </ScrollContainer>
  </PageWrapper>
</template>
<script lang="ts" setup name="AccountCenter">
  import { Tabs } from 'ant-design-vue';
  import { useI18n } from '@jeesite/core/hooks/web/useI18n';
  import { Icon } from '@jeesite/core/components/Icon';
  import { PageWrapper } from '@jeesite/core/components/Page';
  import { ScrollContainer } from '@jeesite/core/components/Container';

  import UserInfo from './userInfo.vue';
  import SecureSettings from './secureSettings.vue';
  import Oauth2Binder from './oauth2Binder.vue';

  const { t } = useI18n();
  const getTitle = {
    icon: 'i-ion:person-outline',
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
