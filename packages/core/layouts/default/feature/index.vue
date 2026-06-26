<script lang="ts">
  import { defineComponent, computed, unref } from 'vue';
  import { BackTop } from 'antdv-next';

  import { useRootSetting } from '@jeesite/core/hooks/setting/useRootSetting';
  import { useHeaderSetting } from '@jeesite/core/hooks/setting/useHeaderSetting';
  import { useUserStoreWithOut } from '@jeesite/core/store/modules/user';

  import { SettingButtonPositionEnum } from '@jeesite/core/enums/appEnum';
  import { createAsyncComponent } from '@jeesite/core/utils/factory/createAsyncComponent';

  import { useFullContent } from '@jeesite/core/hooks/web/useFullContent';

  export default defineComponent({
    name: 'LayoutFeatures',
    components: {
      ABackTop: BackTop,
      LayoutLockPage: createAsyncComponent(() => import('@jeesite/core/layouts/views/lock/index.vue')),
      SettingDrawer: createAsyncComponent(() => import('@jeesite/core/layouts/default/setting/index.vue')),
      SessionTimeoutLogin: createAsyncComponent(
        () => import('@jeesite/core/layouts/views/login/SessionTimeoutLogin.vue'),
      ),
    },
    setup() {
      const { getUseOpenBackTop, getShowSettingButton, getSettingButtonPosition } = useRootSetting();
      const userStore = useUserStoreWithOut();
      const { getShowHeader } = useHeaderSetting();
      const { getFullContent } = useFullContent();

      const getIsSessionTimeout = computed(() => userStore.getSessionTimeout);

      const getIsFixedSettingDrawer = computed(() => {
        if (!unref(getShowSettingButton)) {
          return false;
        }
        const settingButtonPosition = unref(getSettingButtonPosition);

        if (settingButtonPosition === SettingButtonPositionEnum.AUTO) {
          return !unref(getShowHeader) || unref(getFullContent);
        }
        return settingButtonPosition === SettingButtonPositionEnum.FIXED;
      });

      return {
        getTarget: () => document.body,
        getUseOpenBackTop,
        getIsFixedSettingDrawer,
        getIsSessionTimeout,
      };
    },
  });
</script>

<template>
  <LayoutLockPage />
  <ABackTop v-if="getUseOpenBackTop" :target="getTarget" />
  <SettingDrawer v-if="getIsFixedSettingDrawer" class="jeesite-setting-drawer-fearure" />
  <SessionTimeoutLogin v-if="getIsSessionTimeout" />
</template>

<style lang="less">
  .jeesite-setting-drawer-fearure {
    position: absolute;
    top: 45%;
    right: 0;
    z-index: 10;
    display: flex;
    padding: 10px;
    color: @white;
    cursor: pointer;
    background-color: @primary-color;
    border-radius: 6px 0 0 6px;
    justify-content: center;
    align-items: center;

    svg {
      width: 1em;
      height: 1em;
    }
  }
</style>
