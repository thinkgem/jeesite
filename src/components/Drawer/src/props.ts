import type { PropType } from 'vue';

export const footerProps = {
  confirmLoading: { type: Boolean },
  /**
   * @description: Show close button
   */
  showCancelBtn: { type: Boolean, default: true },
  cancelButtonProps: Object as PropType<Recordable>,
  cancelText: { type: String },
  /**
   * @description: Show confirmation button
   */
  showOkBtn: { type: Boolean, default: true },
  okButtonProps: Object as PropType<Recordable>,
  okText: { type: String },
  okType: { type: String, default: 'primary' },
  okAuth: { type: String },
  showFooter: { type: Boolean },
  footerHeight: {
    type: [String, Number] as PropType<string | number>,
    default: 60,
  },
};

export const basicProps = {
  isDetail: { type: Boolean },
  title: { type: String, default: '' },
  loadingText: { type: String },
  showDetailBack: { type: Boolean, default: true },
  open: { type: Boolean },
  loading: { type: Boolean },
  maskClosable: { type: Boolean, default: true },
  getContainer: {
    type: [Object, String] as PropType<any>,
  },
  closeFunc: {
    type: [Function, Object] as PropType<any>,
    default: null,
  },
  destroyOnClose: { type: Boolean },
  wrapClassName: { type: String },
  // 是否允许拖拽调整抽屉宽度
  widthResize: { type: Boolean, default: true },
  ...footerProps,
  // eslint check
  width: { type: [Number, String] },
  mask: { type: Boolean },
  maskStyle: { type: Object },
};
