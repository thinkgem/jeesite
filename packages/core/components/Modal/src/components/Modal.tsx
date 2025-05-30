import { Modal } from 'ant-design-vue';
import { defineComponent, toRefs, unref } from 'vue';
import { basicProps } from '../props';
import { useModalDragMove } from '../hooks/useModalDrag';
import { useAttrs } from '@jeesite/core/hooks/core/useAttrs';
import { extendSlots } from '@jeesite/core/utils/helper/tsxHelper';

export default defineComponent({
  name: 'Modal',
  inheritAttrs: false,
  props: basicProps,
  setup(props, { slots }) {
    const { open, draggable, destroyOnClose } = toRefs(props);
    const attrs = useAttrs();
    useModalDragMove({
      open,
      destroyOnClose,
      draggable,
    });

    return () => {
      const propsData = { class: 'jeesite-basic-modal', ...unref(attrs), ...props } as Recordable;
      return <Modal {...propsData}>{extendSlots(slots)}</Modal>;
    };
  },
});
