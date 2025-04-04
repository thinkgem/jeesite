<template>
  <Layout :class="prefixCls" v-bind="lockEvents">
    <LayoutFeatures />
    <LayoutHeader fixed v-if="getShowFullHeaderRef" />
    <Layout :class="[layoutClass]">
      <LayoutSideBar v-if="getShowSidebar || getIsMobile" />
      <Layout :class="`${prefixCls}-main`">
        <LayoutMultipleHeader />
        <LayoutContent />
        <LayoutFooter />
      </Layout>
    </Layout>
  </Layout>
</template>

<script lang="ts">
  import { defineComponent, computed, unref } from 'vue';
  import { Layout } from 'ant-design-vue';
  import { createAsyncComponent } from '@jeesite/core/utils/factory/createAsyncComponent';

  import LayoutHeader from './header/index.vue';
  import LayoutContent from './content/index.vue';
  import LayoutSideBar from './sider/index.vue';
  import LayoutMultipleHeader from './header/MultipleHeader.vue';

  import { useHeaderSetting } from '@jeesite/core/hooks/setting/useHeaderSetting';
  import { useMenuSetting } from '@jeesite/core/hooks/setting/useMenuSetting';
  import { useDesign } from '@jeesite/core/hooks/web/useDesign';
  import { useLockPage } from '@jeesite/core/hooks/web/useLockPage';

  import { useAppInject } from '@jeesite/core/hooks/web/useAppInject';
  import { switchSkin } from '@jeesite/core/api/sys/login';

  export default defineComponent({
    name: 'DefaultLayout',
    components: {
      LayoutFeatures: createAsyncComponent(() => import('@jeesite/core/layouts/default/feature/index.vue')),
      LayoutFooter: createAsyncComponent(() => import('@jeesite/core/layouts/default/footer/index.vue')),
      LayoutHeader,
      LayoutContent,
      LayoutSideBar,
      LayoutMultipleHeader,
      Layout,
    },
    setup() {
      const { prefixCls } = useDesign('default-layout');
      const { getIsMobile } = useAppInject();
      const { getShowFullHeaderRef } = useHeaderSetting();
      const { getShowSidebar, getIsMixSidebar, getShowMenu } = useMenuSetting();

      switchSkin();

      // Create a lock screen monitor
      const lockEvents = useLockPage();

      const layoutClass = computed(() => {
        let cls: string[] = ['ant-layout'];
        if (unref(getIsMixSidebar) || unref(getShowMenu)) {
          cls.push('ant-layout-has-sider');
        }
        return cls;
      });

      return {
        getShowFullHeaderRef,
        getShowSidebar,
        prefixCls,
        getIsMobile,
        getIsMixSidebar,
        layoutClass,
        lockEvents,
      };
    },
  });
</script>
<style lang="less">
  @prefix-cls: ~'jeesite-default-layout';
  .ant-layout.@{prefix-cls} {
    display: flex;
    width: 100%;
    min-height: 100%;
    flex-direction: column;
    background-color: @content-bg;

    // .ant-layout {
    //   min-height: 100%;
    //   background-color: @content-bg;
    // }

    &-main {
      width: 100%;
      margin-left: 1px;
    }
  }
</style>
