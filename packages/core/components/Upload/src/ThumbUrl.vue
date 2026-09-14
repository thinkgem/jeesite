<template>
  <span class="thumb">
    <Image
      v-if="fileUrl"
      :src="fileUrl"
      :preview="{
        src: previewUrl || fileUrl,
      }"
      :width="width"
      :height="height"
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
    height: propTypes.number.def(60),
  });
</script>
<style lang="less">
  .thumb {
    display: inline-flex;
    align-items: center;
    justify-content: center;

    img {
      position: static;
      display: block;
      cursor: zoom-in;
      border-radius: 4px;
      object-fit: contain;
      max-width: 100%;
      max-height: 100%;
      margin-top: 3px;
    }

    .ant-image .ant-image-cover {
      margin-bottom: -5px;
      border-radius: 2px;
    }
  }
</style>
