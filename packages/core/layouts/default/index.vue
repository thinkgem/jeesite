<template>
  <Layout class="jeesite-default-layout" v-bind="lockEvents">
    <LayoutFeatures />
    <LayoutHeader fixed v-if="getShowFullHeaderRef" />
    <Layout :class="[layoutClass]">
      <LayoutSideBar v-if="getShowSidebar || getIsMobile" />
      <Layout class="jeesite-default-layout-main">
        <LayoutMultipleHeader />
        <LayoutContent />
        <LayoutFooter />
      </Layout>
    </Layout>
  </Layout>
</template>

<script lang="ts" setup name="DefaultLayout">
  import { computed, unref } from 'vue';
  import { Layout } from 'antdv-next';
  import { createAsyncComponent } from '@jeesite/core/utils/factory/createAsyncComponent';

  import LayoutHeader from './header/index.vue';
  import LayoutContent from './content/index.vue';
  import LayoutSideBar from './sider/index.vue';
  import LayoutMultipleHeader from './header/MultipleHeader.vue';

  import { useHeaderSetting } from '@jeesite/core/hooks/setting/useHeaderSetting';
  import { useMenuSetting } from '@jeesite/core/hooks/setting/useMenuSetting';
  import { useLockPage } from '@jeesite/core/hooks/web/useLockPage';

  import { useAppInject } from '@jeesite/core/hooks/web/useAppInject';
  import { switchSkin } from '@jeesite/core/api/sys/login';

  const LayoutFeatures = createAsyncComponent(() => import('@jeesite/core/layouts/default/feature/index.vue'));
  const LayoutFooter = createAsyncComponent(() => import('@jeesite/core/layouts/default/footer/index.vue'));

  const { getIsMobile } = useAppInject();
  const { getShowFullHeaderRef } = useHeaderSetting();
  const { getShowSidebar, getIsMixSidebar, getShowMenu } = useMenuSetting();

  switchSkin();

  const lockEvents = useLockPage();

  const layoutClass = computed(() => {
    let cls: string[] = ['ant-layout'];
    if (unref(getIsMixSidebar) || unref(getShowMenu)) {
      cls.push('ant-layout-has-sider');
    }
    return cls;
  });
</script>
<style lang="less">
  .ant-layout.jeesite-default-layout {
    display: flex;
    width: 100%;
    height: 100%;
    min-height: 0;
    flex-direction: column;
    background-color: @content-bg;

    // .ant-layout {
    //   min-height: 100%;
    //   background-color: @content-bg;
    // }

    &-main {
      width: 100%;
      margin-left: 1px;
      background-color: @content-bg;
    }
  }
</style>
