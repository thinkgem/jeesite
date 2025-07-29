<template>
  <PageWrapper :contentFullHeight="true">
    <DDesigner :hiddenHeader="false" />
  </PageWrapper>
</template>
<script setup>
  import { PageWrapper } from '@jeesite/core/components/Page';
  import { DDesigner } from '@jeesite/dfm';
  // import { pluginManager } from '@jeesite/dfm';
  // import { onMounted } from 'vue';
  //
  // onMounted(() => {
  //   console.log(pluginManager.getComponents());
  //   console.log(pluginManager.getComponentConfigs());
  //   console.log(pluginManager.getComponent('Input'));
  //   console.log(pluginManager.getComponentConfig('Input'));
  //   pluginManager.registerComponent(componentConfig);
  //   pluginManager.removeComponent('Input');
  // });
</script>
