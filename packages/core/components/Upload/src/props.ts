import type { PropType } from 'vue';
import { FileBasicColumn } from './typing';
import { FileUpload, uploadFile } from '@jeesite/core/api/sys/upload';
import { useGlobSetting } from '@jeesite/core/hooks/setting';
import type { SizeType } from '@jeesite/core/components/Table';
import { DEFAULT_SIZE } from '@jeesite/core/components/Table/src/const';

type UploadType = 'image' | 'media' | 'file' | 'all';

/**
 * Copyright (c) 2013-Now https://jeesite.com All rights reserved.
 * No deletion without permission, or be held responsible to law.
 * @author ThinkGem
 */
const { ctxAdminPath } = useGlobSetting();

/**
 * 公共基础配置：所有上传相关组件共享
 * - 上传限制、API 地址、业务参数、图片处理、分片/秒传等
 */
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
    default: null, // 默认从后台获取
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
    default: null, // 默认从后台获取 v5.14.0
  },
  // 图片压缩最大高度
  imageMaxHeight: {
    type: Number as PropType<number>,
    default: null, // 默认从后台获取
  },
  // 如果开启了图片缩略图，这里可以指定缩略图名称，例如：150x150.jpg  v5.4.2
  imageThumbName: {
    type: String as PropType<string>,
    default: '',
  },
  // 是否启用秒传（标准版/专业版）
  checkmd5: {
    type: Boolean as PropType<boolean>,
    default: null, // 默认从后台获取
  },
  // 是否开启分片上传（标准版/专业版） v5.14.1
  chunked: {
    type: Boolean as PropType<boolean>,
    default: null, // 默认从后台获取
  },
  // 分片大小（字节）（标准版/专业版）
  chunkSize: {
    type: Number as PropType<number>,
    default: null, // 默认从后台获取
  },
  // 最大上传线程数（标准版/专业版）
  threads: {
    type: Number as PropType<number>,
    default: null, // 默认从后台获取
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
  // 是否允许拖拽排序
  dragSort: {
    type: Boolean as PropType<boolean>,
    default: true,
  },
};

/**
 * 展示控制配置：控制上传按钮、预览区域、加载行为等
 * BasicUpload 通过 bindValue 透传给 UploadModal / UploadPreview，
 * 因此这些属性也需要在 uploadPreviewProps 中声明，避免落入 attrs。
 */
const showProps = {
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
  // 直接在表单里显示预览文件列表（嵌入模式）
  showPreviewList: {
    type: Boolean as PropType<boolean>,
    default: false,
  },
  // 是否显示上传按钮的文字
  showUploadText: {
    type: Boolean as PropType<boolean>,
    default: true,
  },
  // 上传按钮的类型（primary、default、dashed、link、text）
  uploadButtonType: {
    type: String as PropType<string>,
    default: 'primary',
  },
  // 加载时间戳，此为监听属性，方便刷新文件列表数据
  loadTime: {
    type: Number as PropType<number>,
    default: 0,
  },
};

/**
 * BasicUpload 容器组件属性
 * 用于表单场景，包含 v-model value 及完整的展示控制配置
 */
export const uploadContainerProps = {
  ...basicProps,
  ...showProps,
  value: {
    type: Object as PropType<any>,
    default: {},
  },
};

/**
 * UploadModal / UploadPreview 组件属性
 * 通过 BasicUpload 的 bindValue 接收 basicProps + showProps，
 * 并额外接收已上传文件列表 previewFileList
 */
export const uploadPreviewProps = {
  ...basicProps,
  ...showProps,
  previewFileList: {
    type: Array as PropType<FileUpload[]>,
    default: () => [],
  },
};
