<!--
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 * No deletion without permission, or be held responsible to law.
 * @author Vben、ThinkGem
-->
<template>
  <SvgIcon
    v-if="isSvgIcon"
    :name="getSvgIcon"
    :class="[$attrs.class, 'anticon']"
    :size="size"
    :spin="spin"
  />
  <img
    v-else-if="isImgIcon"
    :src="`${publicPath}/resource/img/${getImgIcon}`"
    :style="`width: ${size}px; height: ${size}px`"
    :class="[$attrs.class, 'anticon']"
    :alt="getImgIcon"
  />
  <span
    v-else
    ref="elRef"
    :class="[$attrs.class, 'app-iconify anticon', spin && 'app-iconify-spin']"
    :style="getWrapStyle"
  ></span>
</template>
<script lang="ts">
  import type { PropType } from 'vue';
  import {
    defineComponent,
    ref,
    watch,
    onMounted,
    nextTick,
    unref,
    computed,
    CSSProperties,
  } from 'vue';
  import SvgIcon from './SvgIcon.vue';
  import { isString } from '/@/utils/is';
  import { propTypes } from '/@/utils/propTypes';
  import { publicPath } from '/@/utils/env';

  const SVG_END_WITH_FLAG = '|svg';
  const IMG_END_WITH_FLAG = '|img';

  export default defineComponent({
    name: 'BasicIcon',
    components: { SvgIcon },
    props: {
      icon: propTypes.string.def(''),
      color: propTypes.string.def(''),
      size: {
        type: [String, Number] as PropType<string | number>,
      },
      spin: propTypes.bool.def(false),
    },
    setup(props) {
      const elRef = ref<ElRef>(null);

      const isSvgIcon = computed(() => props.icon?.endsWith(SVG_END_WITH_FLAG));
      const isImgIcon = computed(() => props.icon?.endsWith(IMG_END_WITH_FLAG));
      const getSvgIcon = computed(() => props.icon?.replace(SVG_END_WITH_FLAG, ''));
      const getImgIcon = computed(() => props.icon?.replace(IMG_END_WITH_FLAG, ''));

      const getIconRef = computed(() => {
        const prefix = 'i-';
        let icon = props.icon || '';
        if (!icon.startsWith(prefix)) {
          icon = prefix + icon;
        }
        if (icon.startsWith(prefix + 'icon-')) {
          icon = prefix + 'simple-line-icons:' + icon.substring(7);
        } else if (icon.startsWith(prefix + 'fa fa-')) {
          icon = prefix + 'fa:' + icon.substring(8);
        }
        return icon;
      });

      const update = async () => {
        if (unref(isSvgIcon)) return;
        const el = unref(elRef);
        if (!el) return;
        await nextTick();
        let icon = unref(getIconRef);
        if (!icon) return;
        const span = document.createElement('span');
        span.className = icon;
        el.textContent = '';
        el.appendChild(span);
      };

      const getWrapStyle = computed((): CSSProperties => {
        const { size, color } = props;
        let fs = size;
        if (isString(size)) {
          fs = parseInt(size, 10);
        }

        const icon = unref(getIconRef);
        if (fs && icon && icon.startsWith('fa:')) {
          fs = (fs as number) - 2; // fa 图标偏大，整体缩小下
        }

        return {
          fontSize: `${fs}px`,
          color: color,
          display: 'inline-flex',
          justifyContent: 'center',
          alignItems: 'center',
        };
      });

      watch(() => props.icon, update, { flush: 'post' });

      onMounted(update);

      return {
        elRef,
        getWrapStyle,
        isSvgIcon,
        isImgIcon,
        getSvgIcon,
        getImgIcon,
        getIconRef,
        publicPath,
      };
    },
  });
</script>
<style lang="less">
  .app-iconify {
    display: inline-block;
    // vertical-align: middle;

    &-spin {
      svg {
        animation: loadingCircle 1s infinite linear;
      }
    }

    svg.iconify--fa {
      opacity: 0.8;
    }
  }

  span.iconify {
    display: block;
    min-width: 1em;
    min-height: 1em;
    background-color: @iconify-bg-color;
    border-radius: 100%;
  }
</style>
