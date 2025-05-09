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

  import { useAppStore } from '/@/store/modules/app';
  // import { usePermissionStore } from '/@/store/modules/permission';
  // import { useMultipleTabStore } from '/@/store/modules/multipleTab';
  // import { useUserStore } from '/@/store/modules/user';

  import { useDesign } from '/@/hooks/web/useDesign';
  import { useI18n } from '/@/hooks/web/useI18n';
  import { useMessage } from '/@/hooks/web/useMessage';
  import { useCopyToClipboard } from '/@/hooks/web/useCopyToClipboard';

  import { updateColorWeak } from '/@/logics/theme/updateColorWeak';
  import { updateGrayMode } from '/@/logics/theme/updateGrayMode';
  import { Persistent } from '/@/utils/cache/persistent';
  import defaultSetting from '/@/settings/projectSetting';

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
