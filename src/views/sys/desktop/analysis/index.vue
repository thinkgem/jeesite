<template>
  <div>
    <GrowCard class="enter-y" />
    <SiteAnalysis class="!my-4 enter-y" />
    <div class="md:flex enter-y">
      <VisitRadar class="md:w-1/3 w-full" />
      <VisitSource class="md:w-1/3 !md:mx-4 !md:my-0 !my-4 w-full" />
      <SalesProductPie class="md:w-1/3 w-full" />
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
