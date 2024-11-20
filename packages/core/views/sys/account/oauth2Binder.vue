<template>
  <CollapseContainer :title="t('账号绑定')" :canExpan="false">
    <List>
      <template v-for="item in accountBindList" :key="item.key">
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
                @click="handleBind(item.title)"
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
  import Icon from '@jeesite/core/components/Icon';
  import { useI18n } from '@jeesite/core/hooks/web/useI18n';
  import { useMessage } from '@jeesite/core/hooks/web/useMessage';

  const { t } = useI18n();
  const { showMessage } = useMessage();

  interface ListItem {
    key: string;
    title: string;
    description: string;
    extra?: string;
    avatar?: string;
    color?: string;
  }

  const accountBindList: ListItem[] = [
    {
      key: '1',
      title: t('绑定QQ'),
      description: t('当前未绑定QQ账号'),
      extra: t('绑定'),
      avatar: 'ant-design:qq-circle-filled',
      color: '#2eabff',
    },
    {
      key: '2',
      title: t('绑定微信'),
      description: t('当前未绑定微信账号'),
      extra: t('绑定'),
      avatar: 'ant-design:wechat-filled',
      color: '#2aae67',
    },
    {
      key: '3',
      title: t('绑定钉钉'),
      description: t('当前未绑定钉钉账号'),
      extra: t('绑定'),
      avatar: 'ant-design:dingtalk-circle-filled',
      color: '#1890ff',
    },
  ];

  function handleBind(title: string) {
    showMessage(title + '，' + t('common.notYetRealized'));
  }
</script>
<style lang="less">
  .avatar {
    font-size: 40px !important;
  }

  .extra {
    float: right;
    margin-top: 10px;
    margin-right: 30px;
    cursor: pointer;
  }
</style>
