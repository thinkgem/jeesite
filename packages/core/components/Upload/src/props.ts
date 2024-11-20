import type { PropType } from 'vue';
import { FileBasicColumn } from './typing';
import { FileUpload, uploadFile } from '@jeesite/core/api/sys/upload';
import { useGlobSetting } from '@jeesite/core/hooks/setting';
import type { SizeType } from '@jeesite/core/components/Table';
import { DEFAULT_SIZE } from '@jeesite/core/components/Table/src/const';

type UploadType = 'image' | 'media' | 'file' | 'all';

const { ctxAdminPath } = useGlobSetting();

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
  disabled: {
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
  apiUploadUrl: {
    type: String as PropType<string>,
    default: ctxAdminPath + '/file/upload',
  },
  apiDownloadUrl: {
    type: String as PropType<string>,
    default: ctxAdminPath + '/file/download',
  },
  apiFileListUrl: {
    type: String as PropType<string>,
    default: ctxAdminPath + '/file/fileList',
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
  imageMaxWidth: {
    type: Number as PropType<number>,
  },
  imageMaxHeight: {
    type: Number as PropType<number>,
  },
  // 如果开启了图片缩略图，这里可以指定缩略图名称，例如：150x150.jpg  v5.4.2
  imageThumbName: {
    type: String as PropType<string>,
    default: '',
  },
  // 是否文件夹上传（caniuse）
  directory: {
    type: Boolean as PropType<boolean>,
    default: false,
  },
  size: {
    type: String as PropType<SizeType>,
    default: DEFAULT_SIZE,
  },
};

export const uploadContainerProps = {
  ...basicProps,
  value: {
    type: Object as PropType<any>,
    default: {},
  },
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
    type: Number as PropType<number>,
    default: 0,
  },
};

export const uploadProps = {
  ...basicProps,
  previewFileList: {
    type: Array as PropType<FileUpload[]>,
    default: () => [],
  },
};

export const previewProps = {
  ...basicProps,
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
