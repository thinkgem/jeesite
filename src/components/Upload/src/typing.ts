import { UploadApiResult } from '/@/api/sys/upload';

export enum UploadResultStatus {
  SUCCESS = 'success',
  ERROR = 'error',
  UPLOADING = 'uploading',
}

export interface FileItem {
  file: File;
  size: string | number;
  type?: string;
  percent: number;
  status?: UploadResultStatus;
  responseData?: UploadApiResult;
  // jeesite
  id: string;
  fileMd5: string;
  fileName: string;
  fileUploadId: string;
  fileEntityId: string;
  bizKey: string;
  bizType: string;
  uploadType: string;
  imageMaxWidth?: string | number;
  imageMaxHeight?: string | number;
  fileUrl?: string;
}

// export interface PreviewFileItem extends FileItem {
//   url: string;
//   name: string;
//   type: string;
// }

export interface FileBasicColumn {
  /**
   * Renderer of the table cell. The return value should be a VNode, or an object for colSpan/rowSpan config
   * @type Function | ScopedSlot
   */
  customRender?: Function;
  /**
   * Title of this column
   * @type any (string | slot)
   */
  title: string;

  /**
   * Width of this column
   * @type string | number
   */
  width?: number;
  /**
   * Display field of the data record, could be set like a.b.c
   * @type string
   */
  dataIndex: string;
  /**
   * specify how content is aligned
   * @default 'left'
   * @type string
   */
  align?: 'left' | 'right' | 'center';
}
