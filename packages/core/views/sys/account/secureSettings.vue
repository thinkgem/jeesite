<template>
  <CollapseContainer :title="t('sys.account.securityTab')" :canExpan="false">
    <List>
      <template v-for="item in secureSettingList" :key="item.key">
        <List.Item>
          <List.Item.Meta>
            <template #avatar>
              <Icon v-if="item.avatar" class="avatar" :icon="item.avatar" :color="item.color" />
            </template>
            <template #title>
              {{ item.title }}
              <a-button
                type="link"
                size="small"
                v-if="item.extra"
                class="extra"
                @click="handleSetting(item.key, item.title)"
              >
                {{ item.extra }}
              </a-button>
            </template>
            <template #description>
              <div>{{ item.description }}</div>
            </template>
          </List.Item.Meta>
        </List.Item>
      </template>
    </List>
  </CollapseContainer>
</template>
<script lang="ts" setup>
  import { List } from 'ant-design-vue';
  import { CollapseContainer } from '@jeesite/core/components/Container';
  import { useI18n } from '@jeesite/core/hooks/web/useI18n';
  import { useMessage } from '@jeesite/core/hooks/web/useMessage';
  import { useGo } from '@jeesite/core/hooks/web/usePage';
  import Icon from '@jeesite/core/components/Icon';

  const { t } = useI18n();
  const { showMessage } = useMessage();
  const go = useGo();

  interface ListItem {
    key: string;
    title: string;
    description: string;
    extra?: string;
    avatar?: string;
    color?: string;
  }

  const secureSettingList: ListItem[] = [
    {
      key: '1',
      title: t('sys.account.modifyPwd'),
      description: t('修改您的登录账号密码'),
      extra: t('common.modifyText'),
      avatar: 'i-ant-design:key-outlined',
      color: '#277eb8',
    },
    {
      key: '3',
      title: t('sys.account.modifyPqa'),
      description: t('修改您的登录账号密保问题和答案'),
      avatar: 'i-ant-design:safety-certificate-outlined',
      extra: t('common.modifyText'),
      color: '#128846',
    },
  ];

  function handleSetting(key: string, title: string) {
    switch (key) {
      case '1':
        go('/account/modPwd');
        break;
      default:
        // showMessage(title + '，' + t('common.notYetRealized'));
        go('/account/modPwdQuestion');
    }
  }
</script>
<style lang="less">
  .extra {
    float: right;
    margin-top: 10px;
    margin-right: 30px;
    font-weight: normal;
    color: #1890ff;
    cursor: pointer;
  }
</style>
