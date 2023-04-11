import { defHttp } from '/@/utils/http/axios';
import { UploadFileParams } from '/#/axios';
import { useGlobSetting } from '/@/hooks/setting';
import { BasicModel } from '../model/baseModel';
import { AxiosProgressEvent } from 'axios';

const { ctxPath, adminPath, uploadUrl } = useGlobSetting();

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
) {
  if (params.file != undefined) {
    return defHttp.uploadFile<UploadApiResult>(
      {
        url: ctxPath + adminPath + '/file/' + uploadUrl,
        onUploadProgress,
      },
      params,
    );
  } else {
    return defHttp.post(
      { url: adminPath + '/file/' + uploadUrl, params },
      { errorMessageMode: 'none' },
    );
  }
}

export const uploadFileList = (params?: FileUpload | any) =>
  defHttp.get<FileUpload[]>(
    { url: adminPath + '/file/fileList', params },
    { errorMessageMode: 'none' },
  );

export const uploadParams = () => defHttp.get<UploadParams>({ url: adminPath + '/file/params' });
