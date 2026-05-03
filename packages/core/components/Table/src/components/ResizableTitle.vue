<script lang="ts">
  import { defineComponent, h, onBeforeUnmount, ref } from 'vue';
  import { BasicColumn } from '@jeesite/core/components/Table';

  interface ResizeInfo {
    size: {
      width: number;
    };
  }

  interface ResizableTitleProps {
    column?: BasicColumn;
    onResize?: (event: MouseEvent, info: ResizeInfo) => void;
  }

  export default defineComponent<ResizableTitleProps>({
    name: 'ResizableTitle',
    inheritAttrs: false,
    props: ['column', 'onResize'],
    setup(props, { slots, attrs }) {
      const dragging = ref(false);
      const stopNextClick = ref(false);
      const widthRef = ref(props.column?.width);
      let startX = 0;
      let startWidth = 0;

      const onMouseMove = (event: MouseEvent) => {
        if (!dragging.value) {
          return;
        }
        stopNextClick.value = true;
        const nextWidth = Math.max(startWidth + event.clientX - startX, 40);
        props.onResize?.(event, { size: { width: nextWidth } });
        widthRef.value = nextWidth;
      };

      const onMouseUp = () => {
        dragging.value = false;
        document.removeEventListener('mousemove', onMouseMove);
        document.removeEventListener('mouseup', onMouseUp);
        setTimeout(() => {
          stopNextClick.value = false;
        }, 0);
      };

      const onMouseDown = (event: MouseEvent) => {
        event.preventDefault();
        event.stopPropagation();
        dragging.value = true;
        stopNextClick.value = false;
        startX = event.clientX;
        startWidth =
          (props.column?.width as number) || (event.currentTarget as HTMLElement).parentElement?.offsetWidth || 0;
        document.addEventListener('mousemove', onMouseMove);
        document.addEventListener('mouseup', onMouseUp);
      };

      const onClickCapture = (event: MouseEvent) => {
        if (stopNextClick.value) {
          event.stopPropagation();
          event.preventDefault();
          stopNextClick.value = false;
        }
      };

      onBeforeUnmount(() => {
        document.removeEventListener('mousemove', onMouseMove);
        document.removeEventListener('mouseup', onMouseUp);
      });

      return () => {
        if (!props.column?.resizable || !props.column?.width) {
          return h('th', attrs, slots.default?.());
        }
        return h(
          'th',
          {
            ...attrs,
            class: ['resizable-title', attrs.class],
            style: { ...(attrs.style as any), width: `${props.column?.width}px` },
            onClickCapture,
          },
          [
            slots.default?.(),
            h('span', {
              class: ['resizable-handle', { dragging: dragging.value }],
              onMousedown: onMouseDown,
            }),
          ],
        );
      };
    },
  });
</script>
<style lang="less">
  .resizable-title {
    position: relative;
  }

  .resizable-handle {
    position: absolute;
    top: 0;
    right: -6px;
    width: 8px;
    height: 100%;
    cursor: col-resize;
    user-select: none;

    &:hover {
      border-left: rgb(170 170 170 / 30%) solid 2px;
    }

    &.dragging {
      border-left: rgb(170 170 170 / 90%) solid 2px;
    }
  }
</style>
