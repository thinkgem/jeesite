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
  import { CollapseContainer } from '/@/components/Container';
  import Icon from '/@/components/Icon';
  import { useI18n } from '/@/hooks/web/useI18n';
  import { useMessage } from '/@/hooks/web/useMessage';
  import { useGo } from '/@/hooks/web/usePage';

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

  const accountBindList: ListItem[] = [
    {
      key: '1',
      title: t('绑定 QQ 账号'),
      description: t('可通过 QQ 账号快速进入本系统'),
      extra: t('绑定'),
      avatar: 'i-ant-design:qq-circle-filled',
      color: '#2eabff',
    },
    {
      key: '2',
      title: t('绑定 Gitee 账号'),
      description: t('可通过 Gitee 账号快速进入本系统'),
      extra: t('绑定'),
      avatar: 'i-ant-design:github-outlined',
      color: '#3d3d3d',
    },
    {
      key: '3',
      title: t('绑定 更多第三方 账号'),
      description: t('可通过第三方账号快速进入本系统'),
      extra: t('绑定'),
      avatar: 'i-ant-design:ant-design-outlined',
      color: '#2aae67',
    },
  ];

  function handleBind(title: string) {
    // showMessage(title + '，' + t('common.notYetRealized'));
    go('/oauth2/list');
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
