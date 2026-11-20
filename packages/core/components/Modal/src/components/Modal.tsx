import { Modal } from 'antdv-next';
import { defineComponent, getCurrentInstance, toRefs, unref } from 'vue';
import { basicProps } from '../props';
import { useModalDragMove } from '../hooks/useModalDrag';
import { useModalResize } from '../hooks/useModalResize';
import { useAttrs } from '@jeesite/core/hooks/core/useAttrs';
import { extendSlots } from '@jeesite/core/utils/helper/tsxHelper';

export default defineComponent({
  name: 'Modal',
  inheritAttrs: false,
  props: basicProps,
  setup(props, { slots }) {
    const { open, draggable, destroyOnHidden, resizable, centered } = toRefs(props);
    const attrs = useAttrs();
    // 唯一标识：缩放时用于定位当前弹窗的 DOM，避免多个弹窗互相干扰
    const resizeMarker = `jeesite-modal-${getCurrentInstance()?.uid ?? Date.now()}`;
    useModalDragMove({
      open,
      destroyOnHidden,
      draggable,
    });
    useModalResize({
      open,
      resizable,
      centered,
      marker: resizeMarker,
    });

    return () => {
      const propsData = { classes: { container: 'jeesite-basic-modal' }, ...unref(attrs), ...props } as Recordable;
      delete propsData.onClose;
      propsData.wrapClassName = [propsData.wrapClassName, resizeMarker].filter(Boolean).join(' ');
      return <Modal {...propsData}>{extendSlots(slots)}</Modal>;
    };
  },
});
