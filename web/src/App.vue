<template>
  <AppProvider>
    <RouterView />
  </AppProvider>
</template>
<script lang="ts" setup name="App">
  import { AppProvider } from '@jeesite/core/components/Application';
</script>
