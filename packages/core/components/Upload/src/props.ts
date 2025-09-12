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
  // 选择文件后，是否需要点击上传按钮再上传文件
  isLazy: {
    type: Boolean as PropType<boolean>,
    default: false,
  },
  // 业务主键
  bizKey: {
    type: [String, Number] as PropType<string | number>,
    default: '',
  },
  // 业务类型
  bizType: {
    type: String as PropType<string>,
    default: '',
  },
  // 上传类型
  uploadType: {
    type: String as PropType<UploadType>,
    default: 'all',
  },
  // 图片压缩最大宽度
  imageMaxWidth: {
    type: Number as PropType<number>,
  },
  // 图片压缩最大高度
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
  // 预览按钮大小
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
  // 是否显示预览按钮
  showPreview: {
    type: Boolean as PropType<boolean>,
    default: true,
  },
  // 是否显示已上传的文件个数
  showPreviewNumber: {
    type: Boolean as PropType<boolean>,
    default: true,
  },
  // 直接在表单里显示预览文件列表
  showPreviewList: {
    type: Boolean as PropType<boolean>,
    default: false,
  },
  // 预览表格为空的时候，是否不显示预览框
  emptyHidePreview: {
    type: Boolean as PropType<boolean>,
    default: true,
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
  ...uploadContainerProps,
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
  emptyText: {
    type: String,
    default: '',
  },
};
