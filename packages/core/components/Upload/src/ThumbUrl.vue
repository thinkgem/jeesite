<template>
  <span class="thumb">
    <Image
      v-if="fileUrl"
      :src="fileUrl"
      :preview="{
        src: previewUrl || fileUrl,
      }"
      :width="width"
    />
  </span>
</template>
<script lang="ts" setup>
  import { propTypes } from '@jeesite/core/utils/propTypes';
  import { Image } from 'antdv-next';

  defineProps({
    fileUrl: propTypes.string.def(''),
    fileName: propTypes.string.def(''),
    previewUrl: propTypes.string.def(''),
    width: propTypes.number.def(104),
  });
</script>
<style lang="less">
  .thumb {
    img {
      position: static;
      display: block;
      cursor: zoom-in;
      border-radius: 4px;
      object-fit: cover;
    }
  }
</style>
