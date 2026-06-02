<template>
  <div class="jeesite-basic-table-img mx-auto flex items-center" v-if="imgList && imgList.length" :style="getWrapStyle">
    <Badge :count="!showBadge || imgList.length == 1 ? 0 : imgList.length" v-if="simpleShow">
      <div class="img-div">
        <PreviewGroup>
          <template v-for="(img, index) in imgList" :key="img">
            <Image
              :width="size"
              :style="{
                display: index === 0 ? '' : 'none !important',
              }"
              :src="srcPrefix + img"
            />
          </template>
        </PreviewGroup>
      </div>
    </Badge>
    <PreviewGroup v-else>
      <template v-for="(img, index) in imgList" :key="img">
        <Image :width="size" :style="{ marginLeft: index === 0 ? 0 : margin }" :src="srcPrefix + img" />
      </template>
    </PreviewGroup>
  </div>
</template>
<script lang="ts">
  import type { CSSProperties } from 'vue';
  import { defineComponent, computed } from 'vue';
  import { Image, Badge } from 'antdv-next';
  import { propTypes } from '@jeesite/core/utils/propTypes';

  const props = {
    imgList: propTypes.arrayOf(propTypes.string),
    size: propTypes.number.def(40),
    // 是否简单显示（只显示第一张图片）
    simpleShow: propTypes.bool,
    // 简单模式下是否显示图片数量的badge
    showBadge: propTypes.bool.def(true),
    // 图片间距
    margin: propTypes.number.def(4),
    // src前缀，将会附加在imgList中每一项之前
    srcPrefix: propTypes.string.def(''),
  };

  export default defineComponent({
    name: 'TableImage',
    components: { Image, PreviewGroup: Image.PreviewGroup, Badge },
    props,
    setup(props) {
      const getWrapStyle = computed((): CSSProperties => {
        const { size } = props;
        const s = `${size}px`;
        return { height: s, width: s };
      });

      return { getWrapStyle };
    },
  });
</script>
<style lang="less">
  .jeesite-basic-table-img {
    .ant-image {
      margin-right: 4px;
      cursor: zoom-in;

      img {
        border-radius: 2px;
      }
    }

    .img-div {
      display: inline-grid;
    }
  }
</style>
