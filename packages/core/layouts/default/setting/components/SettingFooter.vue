<template>
  <div :class="prefixCls">
    <a-button type="primary" block @click="handleCopy">
      <CopyOutlined class="mr-2" />
      {{ t('layout.setting.copyBtn') }}
    </a-button>

    <!-- <a-button color="warning" block @click="handleResetSetting" class="mt-3">
      <RedoOutlined class="mr-2" />
      {{ t('common.resetText') }}
    </a-button> -->

    <a-button color="error" block @click="handleClearAndRedo" class="mb-2 mt-3">
      <RedoOutlined class="mr-2" />
      {{ t('layout.setting.clearBtn') }}
    </a-button>
  </div>
</template>
<script lang="ts">
  import { defineComponent, unref } from 'vue';

  import { CopyOutlined, RedoOutlined } from '@ant-design/icons-vue';

  import { useAppStore } from '@jeesite/core/store/modules/app';
  // import { usePermissionStore } from '@jeesite/core/store/modules/permission';
  // import { useMultipleTabStore } from '@jeesite/core/store/modules/multipleTab';
  // import { useUserStore } from '@jeesite/core/store/modules/user';

  import { useDesign } from '@jeesite/core/hooks/web/useDesign';
  import { useI18n } from '@jeesite/core/hooks/web/useI18n';
  import { useMessage } from '@jeesite/core/hooks/web/useMessage';
  import { useCopyToClipboard } from '@jeesite/core/hooks/web/useCopyToClipboard';

  import { updateColorWeak } from '@jeesite/core/logics/theme/updateColorWeak';
  import { updateGrayMode } from '@jeesite/core/logics/theme/updateGrayMode';
  import { Persistent } from '@jeesite/core/utils/cache/persistent';
  import defaultSetting from '@jeesite/core/settings/projectSetting';

  export default defineComponent({
    name: 'SettingFooter',
    components: { CopyOutlined, RedoOutlined },
    setup() {
      // const permissionStore = usePermissionStore();
      const { prefixCls } = useDesign('setting-footer');
      const { t } = useI18n();
      const { createSuccessModal, createMessage } = useMessage();
      // const tabStore = useMultipleTabStore();
      // const userStore = useUserStore();
      const appStore = useAppStore();

      function handleCopy() {
        const { isSuccessRef } = useCopyToClipboard(JSON.stringify(unref(appStore.getProjectConfig), null, 2));
        unref(isSuccessRef) &&
          createSuccessModal({
            title: t('layout.setting.operatingTitle'),
            content: t('layout.setting.operatingContent'),
          });
      }
      function handleResetSetting() {
        try {
          appStore.setProjectConfig(defaultSetting);
          const { colorWeak, grayMode } = defaultSetting;
          // updateTheme(themeColor);
          updateColorWeak(colorWeak);
          updateGrayMode(grayMode);
          createMessage.success(t('layout.setting.resetSuccess'));
        } catch (error: any) {
          createMessage.error(error);
        }
        location.reload();
      }

      function handleClearAndRedo() {
        Persistent.clearAll(true);
        // appStore.resetAllState();
        // tabStore.resetState();
        // permissionStore.resetState();
        // userStore.resetState();
        location.reload();
      }
      return {
        prefixCls,
        t,
        handleCopy,
        handleResetSetting,
        handleClearAndRedo,
      };
    },
  });
</script>
<style lang="less">
  @prefix-cls: ~'jeesite-setting-footer';

  .@{prefix-cls} {
    display: flex;
    flex-direction: column;
    align-items: center;
  }
</style>
