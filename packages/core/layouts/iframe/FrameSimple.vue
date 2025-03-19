<template>
  <div v-if="showFrame">
    <template v-for="frame in getFramePages" :key="frame.path">
      <template v-if="frame.meta.frameSrc && showIframe(frame)">
        <FramePage :frame="frame" :fullHeight="true" />
      </template>
    </template>
  </div>
</template>
<script lang="ts">
  import { computed, defineComponent, unref } from 'vue';
  import FramePage from '@jeesite/core/layouts/iframe/FramePage.vue';
  import { useFrameKeepAlive } from '@jeesite/core/layouts/iframe/useFrameKeepAlive';

  export default defineComponent({
    name: 'FrameSimple',
    components: { FramePage },
    setup() {
      const { getFramePages, hasRenderFrame, showIframe } = useFrameKeepAlive();
      const showFrame = computed(() => unref(getFramePages).length > 0);
      return { getFramePages, hasRenderFrame, showIframe, showFrame };
    },
  });
</script>
