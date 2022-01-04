<template>
  <CollapseContainer title="安全设置" :canExpan="false">
    <List>
      <template v-for="item in secureSettingList" :key="item.key">
        <List.Item>
          <List.Item.Meta>
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
  import { CollapseContainer } from '/@/components/Container';
  import { useMessage } from '/@/hooks/web/useMessage';
  import { useGo } from '/@/hooks/web/usePage';

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
      title: '账户密码',
      description: '当前密码强度：强',
      extra: '修改',
    },
    {
      key: '3',
      title: '密保问题',
      description: '未设置密保问题，密保问题可有效保护账户安全',
      extra: '修改',
    },
  ];

  function handleSetting(key: string, title: string) {
    switch (key) {
      case '1':
        go('/account/modPwd');
        break;
      default:
        showMessage(title + '，暂未实现');
    }
  }
</script>
<style lang="less" scoped>
  .extra {
    float: right;
    margin-top: 10px;
    margin-right: 30px;
    font-weight: normal;
    color: #1890ff;
    cursor: pointer;
  }
</style>
