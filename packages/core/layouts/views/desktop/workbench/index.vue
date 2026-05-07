<template>
  <PageWrapper>
    <template #headerContent> <WorkbenchHeader /> </template>
    <div class="lg:flex">
      <div class="enter-y w-full !mr-4 lg:w-7/10">
        <ProjectCard :loading="loading" class="enter-y" />
        <DynamicInfo :loading="loading" class="enter-y !my-4" />
      </div>
      <div class="enter-y w-full lg:w-3/10">
        <QuickNav :loading="loading" class="enter-y" />

        <Card class="enter-y !my-4" :loading="loading">
          <img class="mx-auto h-30 xl:h-50" src="@jeesite/assets/svg/illustration.svg" />
        </Card>

        <SaleRadar :loading="loading" class="enter-y" />
      </div>
    </div>
  </PageWrapper>
</template>
<script lang="ts" setup name="Workbench">
  import { ref } from 'vue';
  import { Card } from 'antdv-next';
  import { PageWrapper } from '@jeesite/core/components/Page';
  import { createAsyncComponent } from '@jeesite/core/utils/factory/createAsyncComponent';
  const WorkbenchHeader = createAsyncComponent(() => import('./components/WorkbenchHeader.vue'));
  const ProjectCard = createAsyncComponent(() => import('./components/ProjectCard.vue'));
  const QuickNav = createAsyncComponent(() => import('./components/QuickNav.vue'));
  const DynamicInfo = createAsyncComponent(() => import('./components/DynamicInfo.vue'));
  const SaleRadar = createAsyncComponent(() => import('./components/SaleRadar.vue'));

  const loading = ref(true);

  setTimeout(() => {
    loading.value = false;
  }, 800);
</script>
