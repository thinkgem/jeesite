import { defHttp } from '@jeesite/core/utils/http/axios';
import { UploadFileParams } from '@jeesite/types/axios';
import { useGlobSetting } from '@jeesite/core/hooks/setting';
import { BasicModel } from '@jeesite/core/api/model/baseModel';
import { AxiosProgressEvent } from 'axios';

const { ctxPath, adminPath } = useGlobSetting();

export interface UploadApiResult {
  code: string;
  // url: string;
  result: string;
  message: string;
  fileEntityId: string;
  fileUploadId: string;
  fileUpload: FileUpload;
}
export interface FileEntity extends BasicModel<FileEntity> {
  fileId: string;
  fileMd5: string;
  filePath: string;
  fileContentType: string;
  fileExtension: string;
  fileSize: number;
  fileMeta: string;
  fileMetaMap: any;
  filePreview: string;
}
export interface FileUpload extends BasicModel<FileUpload> {
  fileEntity: FileEntity;
  fileName: string;
  fileType: string;
  fileSort: number;
  bizKey: string;
  bizType: string;
  bizKeyIsLike: string;
  fileUrl?: string;
}
export interface UploadParams {
  imageAllowSuffixes: string;
  mediaAllowSuffixes: string;
  fileAllowSuffixes: string;
  chunked?: boolean;
  chunkSize?: number;
  threads?: number;
  imageMaxWidth?: number;
  imageMaxHeight?: number;
}

/**
 * @description: Upload interface
 */
export function uploadFile(
  params: UploadFileParams,
  onUploadProgress: (progressEvent: ProgressEvent | AxiosProgressEvent) => void,
  apiUploadUrl?: string,
) {
  if (params.file != undefined) {
    return defHttp.uploadFile<UploadApiResult>(
      {
        url: apiUploadUrl || ctxPath + adminPath + '/file/upload',
        onUploadProgress,
      },
      params,
    );
  } else {
    return defHttp.post(
      { url: apiUploadUrl || ctxPath + adminPath + '/file/upload', params },
      { errorMessageMode: 'none', apiUrl: '', urlPrefix: '' },
    );
  }
}

export const uploadFileList = (params?: FileUpload | any, apiFileListUrl?: string) =>
  defHttp.get<FileUpload[]>(
    { url: apiFileListUrl || ctxPath + adminPath + '/file/fileList', params },
    { errorMessageMode: 'none', apiUrl: '', urlPrefix: '' },
  );

export const uploadParams = () => defHttp.get<UploadParams>({ url: adminPath + '/file/params' });
