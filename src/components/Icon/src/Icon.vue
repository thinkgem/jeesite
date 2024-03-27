<!--
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 * No deletion without permission, or be held responsible to law.
 * @author Vben、ThinkGem
-->
<template>
  <img
    v-if="isImgIcon"
    :src="`${publicPath}/resource/img/${getImgIcon}`"
    :style="`width: ${size}px; height: ${size}px`"
    :class="getClass"
    alt=""
  />
  <span v-else ref="elRef" :style="getWrapStyle" :class="getClass"></span>
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
  import { isString } from '/@/utils/is';
  import { propTypes } from '/@/utils/propTypes';
  import { publicPath } from '/@/utils/env';

  export default defineComponent({
    name: 'BasicIcon',
    props: {
      icon: propTypes.string.def(''),
      color: propTypes.string.def(''),
      size: {
        type: [String, Number] as PropType<string | number>,
      },
      spin: propTypes.bool.def(false),
    },
    setup(props, { attrs }) {
      const elRef = ref<ElRef>(null);
      const getClass = computed(() => {
        const prefixCls = 'jeesite-icon';
        return [
          attrs.class,
          `${prefixCls} anticon`,
          {
            [`${prefixCls}-spin`]: props.spin,
            [`${prefixCls}-fa`]: unref(getIconRef).startsWith('i-fa:'),
          },
        ];
      });
      const isImgIcon = computed(() => props.icon?.includes('.'));
      const getImgIcon = computed(() => props.icon);
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
        if (unref(isImgIcon)) return;
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
        if (fs && icon && icon.startsWith('i-fa:')) {
          fs = (fs as number) - 1; // fa 图标偏大，整体缩小下
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
        getClass,
        getWrapStyle,
        isImgIcon,
        getImgIcon,
        publicPath,
        getIconRef,
      };
    },
  });
</script>
<style lang="less">
  .jeesite-icon {
    display: inline-block;

    &-spin {
      animation: loadingCircle 1s infinite linear;
    }

    &-fa {
      opacity: 0.8;
    }

    //span {
    //  min-width: 1em;
    //  min-height: 1em;
    //  border-radius: 100%;
    //  background-color: @iconify-bg-color;
    //}
  }
</style>
