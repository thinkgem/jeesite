<template>
  <div class="jeesite-resizer" :style="getWrapStyleRef" @mousedown="onMouseDown">
    <div class="resiezer-toggler" :title="`${t('common.hideText')}/${t('common.showText')}`" @click="toggle">
      <span class="resizer-content">
        <slot v-if="$slots.default"></slot>
        <span v-else>
          <Icon :icon="getCaretIcon" />
        </span>
      </span>
    </div>
  </div>
</template>
<script lang="ts">
  import { computed, defineComponent, reactive, ref, unref, watch } from 'vue';
  import { useI18n } from '/@/hooks/web/useI18n';
  import { Icon } from '/@/components/Icon';
  import { useEventListener } from '/@/hooks/event/useEventListener';

  export default defineComponent({
    name: 'JeeSiteResizer',
    components: { Icon },
    props: {
      position: {
        type: String as PropType<'left' | 'right' | 'top' | 'bottom'>,
        default: 'left',
      },
      collapsed: {
        type: Boolean,
        default: false,
      },
    },
    emits: ['move', 'update:collapsed'],
    setup(props, { emit }) {
      const { t } = useI18n();
      const isCollapsed = ref<boolean>(false);
      const drift = reactive({
        lastX: 0,
        lastY: 0,
      });

      watch(
        () => props.collapsed,
        () => {
          isCollapsed.value = props.collapsed;
        },
        { immediate: true },
      );

      const getWrapStyleRef = computed(() => {
        if (props.position === 'top' || props.position === 'bottom') {
          return { width: '100%', cursor: isCollapsed.value ? 'default' : 's-resize' };
        } else {
          return { cursor: isCollapsed.value ? 'default' : 'w-resize' };
        }
      });

      const getCaretIcon = computed(() => {
        if (!isCollapsed.value && props.position == 'top') {
          return 'i-ant-design:caret-up-outlined';
        }
        if (!isCollapsed.value && props.position == 'bottom') {
          return 'i-ant-design:caret-down-outlined';
        }
        if (!isCollapsed.value && props.position == 'left') {
          return 'i-ant-design:caret-left-outlined';
        }
        if (!isCollapsed.value && props.position == 'right') {
          return 'i-ant-design:caret-right-outlined';
        }
        if (isCollapsed.value && props.position == 'top') {
          return 'i-ant-design:caret-down-outlined';
        }
        if (isCollapsed.value && props.position == 'bottom') {
          return 'i-ant-design:caret-up-outlined';
        }
        if (isCollapsed.value && props.position == 'left') {
          return 'i-ant-design:caret-right-outlined';
        }
        if (isCollapsed.value && props.position == 'right') {
          return 'i-ant-design:caret-left-outlined';
        }
        return 'i-ant-design:caret-right-outlined';
      });

      useEventListener({
        el: document,
        name: 'mouseup',
        listener: onMouseUp,
        wait: 0,
      });

      function onMouseDown(event) {
        if (isCollapsed.value) {
          return;
        }
        drift.lastX = event.screenX;
        drift.lastY = event.screenY;
        document.addEventListener('mousemove', onMouseMove);
      }

      function onMouseMove(event) {
        emit('move', event, drift.lastX - event.screenX, drift.lastY - event.screenY);
        drift.lastX = event.screenX;
        drift.lastY = event.screenY;
      }

      function onMouseUp() {
        if (isCollapsed.value) {
          return;
        }
        document.removeEventListener('mousemove', onMouseMove);
        drift.lastX = 0;
        drift.lastY = 0;
      }

      function toggle() {
        isCollapsed.value = !isCollapsed.value;
        emit('update:collapsed', isCollapsed.value);
      }

      return {
        t,
        getWrapStyleRef,
        ...unref(props),
        getCaretIcon,
        onMouseDown,
        toggle,
      };
    },
  });
</script>
<style lang="less">
  .jeesite-resizer {
    z-index: 100;
    display: flex;
    justify-content: center;
    align-items: center;
    padding: 1px;

    .resiezer-toggler {
      cursor: pointer;

      .resizer-content {
        padding: 15px 1px;
        font-size: 10px;
        color: @text-color-secondary;
        user-select: none;
      }

      :hover {
        background-color: @border-color-base;
        border-radius: @border-radius-base;
      }

      .anticon span:hover {
        background-color: @text-color-base;
      }
    }
  }
</style>
