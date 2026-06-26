<template>
  <span class="jeesite-multiple-tabs-content__extra-redo" @click="handleRedo">
    <Icon icon="i-ant-design:redo-outlined" :spin="loading" />
  </span>
</template>
<script lang="ts">
  import { defineComponent, ref } from 'vue';
  import { useTabs } from '@jeesite/core/hooks/web/useTabs';
  import { Icon } from '@jeesite/core/components/Icon';

  export default defineComponent({
    name: 'TabRedo',
    components: { Icon },

    setup() {
      const loading = ref(false);

      const { refreshPage } = useTabs();

      async function handleRedo() {
        loading.value = true;
        await refreshPage();
        setTimeout(() => {
          loading.value = false;
          // Animation execution time
        }, 1200);
      }
      return { handleRedo, loading };
    },
  });
</script>
