<template>
  <PageWrapper title="页面水印示例">
    <a-button type="primary" class="mr-2" @click="setWatermark('Hello JeeSite !')">
      创建水印 1
    </a-button>
    <a-button color="error" class="mr-2" @click="clear"> 清理水印 1 </a-button>
    <a-button type="primary" class="mr-2" @click="setWatermark2('https://jeesite.com')">
      创建水印 2
    </a-button>
    <a-button color="error" class="mr-2" @click="clearAll"> 清理全部 </a-button>
  </PageWrapper>
</template>
<script lang="ts" setup>
  import { onUnmounted, ref } from 'vue';
  import { useWatermark } from '@jeesite/core/hooks/web/useWatermark';
  import { PageWrapper } from '@jeesite/core/components/Page';

  const app = ref(document.body);

  const { setWatermark, clear, clearAll } = useWatermark();
  const { setWatermark: setWatermark2 } = useWatermark(app, {
    fontColor: 'red',
    fontSize: 12,
    rotate: 30,
  });

  onUnmounted(() => {
    clearAll();
  });
</script>
