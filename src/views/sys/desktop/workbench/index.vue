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
          <img class="mx-auto h-30 xl:h-50" src="/@/assets/svg/illustration.svg" />
        </Card>

        <SaleRadar :loading="loading" class="enter-y" />
      </div>
    </div>
  </PageWrapper>
</template>
<script lang="ts" setup name="Workbench">
  import { ref } from 'vue';
  import { Card } from 'ant-design-vue';
  import { PageWrapper } from '/@/components/Page';
  import WorkbenchHeader from './components/WorkbenchHeader.vue';
  import ProjectCard from './components/ProjectCard.vue';
  import QuickNav from './components/QuickNav.vue';
  import DynamicInfo from './components/DynamicInfo.vue';
  import SaleRadar from './components/SaleRadar.vue';

  const loading = ref(true);

  setTimeout(() => {
    loading.value = false;
  }, 800);
</script>
