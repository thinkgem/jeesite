<template>
  <span>
    <slot></slot>
    {{ title }}
    <FormOutlined />
  </span>
</template>
<script lang="ts">
  import { defineComponent } from 'vue';
  import { FormOutlined } from '@antdv-next/icons';
  export default defineComponent({
    name: 'EditTableHeaderIcon',
    components: { FormOutlined },
    props: { title: { type: String, default: '' } },
  });
</script>
