<template>
  <div class="mb-4">
    <GrowCard class="enter-y" />
    <SiteAnalysis class="enter-y !my-4" />
    <div class="enter-y md:flex">
      <VisitRadar class="w-full md:w-1/3" />
      <VisitSource class="w-full !my-4 md:w-1/3 !md:mx-4 !md:my-0" />
      <SalesProductPie class="w-full md:w-1/3" />
    </div>
  </div>
</template>
<script lang="ts" setup name="Analysis">
  import GrowCard from './components/GrowCard.vue';
  import SiteAnalysis from './components/SiteAnalysis.vue';
  import VisitSource from './components/VisitSource.vue';
  import VisitRadar from './components/VisitRadar.vue';
  import SalesProductPie from './components/SalesProductPie.vue';
</script>
