<template>
  <CollapseContainer title="账号绑定" :canExpan="false">
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
  import Icon from '/@/components/Icon/index';
  import { useMessage } from '/@/hooks/web/useMessage';

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
      title: '绑定QQ',
      description: '当前未绑定QQ账号',
      extra: '绑定',
      avatar: 'ant-design:qq-circle-filled',
      color: '#2eabff',
    },
    {
      key: '2',
      title: '绑定微信',
      description: '当前未绑定微信账号',
      extra: '绑定',
      avatar: 'ant-design:wechat-filled',
      color: '#2aae67',
    },
    {
      key: '3',
      title: '绑定钉钉',
      description: '当前未绑定钉钉账号',
      extra: '绑定',
      avatar: 'ant-design:dingtalk-circle-filled',
      color: '#1890ff',
    },
  ];

  function handleBind(title: string) {
    showMessage(title + '，暂未实现');
  }
</script>
<style lang="less" scoped>
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
