<template>
  <CollapseContainer :title="t('sys.account.securityTab')" :canExpan="false">
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

  const secureSettingList: ListItem[] = [
    {
      key: '1',
      title: t('sys.account.modifyPwd'),
      description: '',
      extra: t('common.modifyText'),
    },
    {
      key: '3',
      title: t('sys.account.modifyPqa'),
      description: '',
      extra: t('common.modifyText'),
    },
  ];

  function handleSetting(key: string, title: string) {
    switch (key) {
      case '1':
        go('/account/modPwd');
        break;
      default:
        showMessage(title + '，' + t('common.notYetRealized'));
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
