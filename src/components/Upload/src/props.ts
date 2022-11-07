import type { PropType } from 'vue';
import { FileBasicColumn } from './typing';
import { FileUpload, uploadFile } from '/@/api/sys/upload';

type UploadType = 'image' | 'media' | 'file' | 'all';

export const basicProps = {
  uploadText: {
    type: String as PropType<string>,
    default: '',
  },
  helpText: {
    type: String as PropType<string>,
    default: '',
  },
  // 文件最大多少MB
  maxSize: {
    type: Number as PropType<number>,
    default: 50,
  },
  // 最大数量的文件，Infinity不限制
  maxNumber: {
    type: Number as PropType<number>,
    default: Infinity,
  },
  // 根据后缀，或者其他
  accept: {
    type: Array as PropType<string[]>,
    default: () => [],
  },
  multiple: {
    type: Boolean as PropType<boolean>,
    default: true,
  },
  uploadParams: {
    type: Object as PropType<any>,
    default: {},
  },
  readonly: {
    type: Boolean as PropType<boolean>,
    default: false,
  },
  // 秒传功能（标准版/专业版）
  checkmd5: {
    type: Boolean as PropType<boolean>,
    default: true,
  },
  api: {
    type: Function as PropType<PromiseFn>,
    default: uploadFile,
  },
  isLazy: {
    type: Boolean as PropType<boolean>,
    default: false,
  },
  bizKey: {
    type: [String, Number] as PropType<string | number>,
    default: '',
  },
  bizType: {
    type: String as PropType<string>,
    default: '',
  },
  uploadType: {
    type: String as PropType<UploadType>,
    default: 'all',
  },
};

export const uploadContainerProps = {
  value: {
    type: Object as PropType<{}>,
    default: {},
  },
  ...basicProps,
  showPreview: {
    type: Boolean as PropType<boolean>,
    default: true,
  },
  showPreviewNumber: {
    type: Boolean as PropType<boolean>,
    default: true,
  },
  emptyHidePreview: {
    type: Boolean as PropType<boolean>,
    default: false,
  },
  // 加载时间戳，此为监听属性，方便刷新文件列表数据
  loadTime: {
    type: Number as PropType<Number>,
    default: 0,
  },
};

export const previewProps = {
  value: {
    type: Array as PropType<FileUpload[]>,
    default: () => [],
  },
};

export const fileListProps = {
  columns: {
    type: [Array] as PropType<FileBasicColumn[]>,
    default: null,
  },
  actionColumn: {
    type: Object as PropType<FileBasicColumn>,
    default: null,
  },
  dataSource: {
    type: Array as PropType<any[]>,
    default: null,
  },
};
