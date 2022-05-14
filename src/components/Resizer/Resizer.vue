<template>
  <div @mousedown="onMouseDown" class="resizer" :style="getWrapStyleRef">
    <div class="resiezer-toggler" @click="toggle" title="双击隐藏/显示">
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
  import { Icon } from '/@/components/Icon';
  import { useEventListener } from '/@/hooks/event/useEventListener';
  export default defineComponent({
    name: 'Resizer',
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
      const isCollapsed = ref<Boolean>(false);
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
          return 'ant-design:caret-up-outlined';
        }
        if (!isCollapsed.value && props.position == 'bottom') {
          return 'ant-design:caret-down-outlined';
        }
        if (!isCollapsed.value && props.position == 'left') {
          return 'ant-design:caret-left-outlined';
        }
        if (!isCollapsed.value && props.position == 'right') {
          return 'ant-design:caret-right-outlined';
        }
        if (isCollapsed.value && props.position == 'top') {
          return 'ant-design:caret-down-outlined';
        }
        if (isCollapsed.value && props.position == 'bottom') {
          return 'ant-design:caret-up-outlined';
        }
        if (isCollapsed.value && props.position == 'left') {
          return 'ant-design:caret-right-outlined';
        }
        if (isCollapsed.value && props.position == 'right') {
          return 'ant-design:caret-left-outlined';
        }
        return 'ant-design:caret-right-outlined';
      });

      useEventListener({
        el: document,
        name: 'mouseup',
        listener: onMouseUp,
        wait: 0,
      });

      // document.addEventListener('mouseup', onMouseUp);

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
  .resizer {
    z-index: 999;
    display: flex;
    justify-content: center;
    align-items: center;
    padding: 2px;
    .resiezer-toggler {
      padding: 2px;
      cursor: pointer;
      .resizer-content {
        font-size: 10px;
        color: @text-color-secondary;
        -moz-user-select: none;
        -webkit-user-select: none;
        -ms-user-select: none;
        -khtml-user-select: none;
        user-select: none;
      }
      :hover {
        background-color: @border-color-base;
        border-radius: @border-radius-base;
      }
    }
  }
</style>