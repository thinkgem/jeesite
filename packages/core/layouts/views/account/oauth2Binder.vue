<template>
  <CollapseContainer :title="t('sys.account.bindingTab')" :canExpan="false">
    <div class="list-container">
      <div v-for="item in accountBindList" :key="item.key" class="list-item">
        <div class="list-item-meta">
          <div v-if="item.avatar" class="avatar-wrapper">
            <Icon class="avatar" :icon="item.avatar" :color="item.color" />
          </div>
          <div class="meta-content">
            <div class="meta-title">
              <span v-if="item.extra" class="cursor-pointer" @click="handleBind(item.title)">
                {{ item.title }}
              </span>
              <span v-else>{{ item.title }}</span>
              <a-button type="link" size="small" v-if="item.extra" class="extra" @click="handleBind(item.title)">
                {{ item.extra }}
              </a-button>
            </div>
            <div class="meta-description">
              {{ item.description }}
            </div>
          </div>
        </div>
      </div>
    </div>
  </CollapseContainer>
</template>
<script lang="ts" setup>
  import { CollapseContainer } from '@jeesite/core/components/Container';
  import Icon from '@jeesite/core/components/Icon';
  import { useI18n } from '@jeesite/core/hooks/web/useI18n';
  import { useMessage } from '@jeesite/core/hooks/web/useMessage';
  import { useGo } from '@jeesite/core/hooks/web/usePage';

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
    showMessage(title + '，专业版中实现。');
  }
</script>
