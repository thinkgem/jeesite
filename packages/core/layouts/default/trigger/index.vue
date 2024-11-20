<template>
  <SiderTrigger v-if="sider" />
  <HeaderTrigger v-else :theme="theme" />
</template>
<script lang="ts">
  import { defineComponent } from 'vue';
  import { createAsyncComponent } from '@jeesite/core/utils/factory/createAsyncComponent';
  import { propTypes } from '@jeesite/core/utils/propTypes';
  import HeaderTrigger from './HeaderTrigger.vue';

  export default defineComponent({
    name: 'LayoutTrigger',
    components: {
      SiderTrigger: createAsyncComponent(() => import('./SiderTrigger.vue')),
      HeaderTrigger: HeaderTrigger,
    },
    props: {
      sider: propTypes.bool.def(true),
      theme: propTypes.oneOf(['light', 'dark']),
    },
  });
</script>
