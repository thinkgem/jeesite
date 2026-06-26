import { Modal } from 'antdv-next';
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
    const { open, draggable, destroyOnHidden } = toRefs(props);
    const attrs = useAttrs();
    useModalDragMove({
      open,
      destroyOnHidden,
      draggable,
    });

    return () => {
      const propsData = { classes: { container: 'jeesite-basic-modal' }, ...unref(attrs), ...props } as Recordable;
      delete propsData.onClose;
      return <Modal {...propsData}>{extendSlots(slots)}</Modal>;
    };
  },
});
