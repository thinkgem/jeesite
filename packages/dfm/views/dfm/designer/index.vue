<template>
  <PageWrapper :contentFullHeight="true">
    <DDesigner :hiddenHeader="false" :formMode="true" />
  </PageWrapper>
</template>
<script setup>
  import { PageWrapper } from '@jeesite/core/components/Page';
  import { DDesigner } from '@jeesite/dfm';
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
</script>
