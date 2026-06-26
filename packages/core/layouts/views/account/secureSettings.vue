<template>
  <CollapseContainer :title="t('sys.account.securityTab')" :canExpan="false">
    <div class="list-container">
      <div v-for="item in secureSettingList" :key="item.key" class="list-item">
        <div class="list-item-meta">
          <div v-if="item.avatar" class="avatar-wrapper">
            <Icon class="avatar" :icon="item.avatar" :color="item.color" />
          </div>
          <div class="meta-content">
            <div class="meta-title">
              <span v-if="item.extra" class="cursor-pointer" @click="handleSetting(item.key, item.title)">
                {{ item.title }}
              </span>
              <span v-else>{{ item.title }}</span>
              <a-button
                type="link"
                size="small"
                v-if="item.extra"
                class="extra"
                @click="handleSetting(item.key, item.title)"
              >
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
