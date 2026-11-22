<template>
  <PageWrapper :contentFullHeight="true">
    <DfmDesigner ref="designerRef" :formMode="true" @save="handleSave" @close="handleClose" />
  </PageWrapper>
</template>
<script lang="ts" setup name="ViewsDfmDesignerIndex">
  import { PageWrapper } from '@jeesite/core/components/Page';
  import { useMessage } from '@jeesite/core/hooks/web/useMessage';
  import { useTabs } from '@jeesite/core/hooks/web/useTabs';
  import { DfmDesigner } from '@jeesite/dfm';

  // import { pluginManager } from '@jeesite/dfm';
  // import { onMounted } from 'vue';
  //
  // onMounted(() => {
  //   console.log(pluginManager.component.getComponents());
  //   console.log(pluginManager.component.getComponentConfigs());
  //   console.log(pluginManager.component.getComponent('input'));
  //   console.log(pluginManager.component.getComponentConfigByType('input'));
  //   pluginManager.component.registerComponent(componentConfig);
  //   pluginManager.component.removeComponent('input');
  // });

  const { showMessage } = useMessage();
  const { closeCurrent } = useTabs();

  // 关闭当前页签（演示页面未做修改校验）
  async function handleClose() {
    try {
      await closeCurrent();
    } catch (error) {
      // 页签不存在时返回上一页
      console.warn('[DFM] 关闭页签失败：', error);
      window.history.back();
    }
  }

  function handleSave(data: string) {
    showMessage('在业务中实现.');
    console.log(data);
  }
</script>
