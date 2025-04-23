<template>
  <PageWrapper :contentFullHeight="true">
    <DDesigner :hiddenHeader="false" />
  </PageWrapper>
</template>
<script setup>
  import { PageWrapper } from '@jeesite/core/components/Page';
  import { DDesigner } from '@jeesite/dfm';
</script>
