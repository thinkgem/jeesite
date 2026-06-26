<template>
  <PageWrapper>
    <template #headerTitle>
      <Icon :icon="getTitle.icon" class="m-1 pr-1" />
      <span> {{ getTitle.value }} </span>
    </template>
    <ScrollContainer>
      <div class="account-setting">
        <Tabs tabPlacement="start" :tabBarStyle="{ width: '220px' }">
          <TabPane key="1" :tab="t('sys.account.basicTab')">
            <UserInfo />
          </TabPane>
          <TabPane key="2" :tab="t('sys.account.securityTab')">
            <SecureSettings />
          </TabPane>
          <TabPane key="3" :tab="t('sys.account.bindingTab')">
            <Oauth2Binder />
          </TabPane>
        </Tabs>
      </div>
    </ScrollContainer>
  </PageWrapper>
</template>
<script lang="ts" setup name="AccountCenter">
  import { Tabs, TabPane } from 'antdv-next';
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

    .list-container {
      padding: 5px 0;

      .list-item {
        display: flex;
        align-items: center;
        justify-content: space-between;
        padding: 16px 20px;
        border-bottom: 1px solid #f0f0f0;

        &:last-child {
          border-bottom: none;
        }

        .list-item-meta {
          display: flex;
          align-items: center;
          flex: 1;

          .avatar-wrapper {
            display: flex;
            align-items: center;
            justify-content: center;
            margin-right: 16px;

            .avatar {
              font-size: 32px;
            }
          }

          .meta-content {
            flex: 1;

            .meta-title {
              display: flex;
              align-items: center;
              justify-content: space-between;
              font-size: 14px;
              font-weight: 500;
              color: rgb(0 0 0 / 85%);
              margin-bottom: 4px;

              .extra {
                color: #1890ff;
                font-size: 14px;
                font-weight: 400;
                cursor: pointer;

                &:hover {
                  color: #40a9ff;
                }
              }
            }

            .meta-description {
              font-size: 13px;
              color: rgb(0 0 0 / 45%);
            }
          }
        }
      }
    }
  }
</style>
