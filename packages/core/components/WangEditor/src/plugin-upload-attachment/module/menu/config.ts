/**
 * @description menu config
 * @author wangfupeng
 */

import { IUploadConfig } from '@wangeditor/editor';
import { AttachmentElement } from '../custom-types';
import { UppyFile } from '@uppy/core';

type InsertFn = (fileName: string, link: string) => void;

// 在通用 uploadConfig 上，扩展 attachment 相关配置
export type IUploadConfigForAttachment = IUploadConfig & {
  allowedFileTypes?: string[];
  // 用户自定义插入附件
  customInsert?: (res: any, file: UppyFile, insertFn: InsertFn) => void;
  // 用户自定义上传附件
  customUpload?: (files: File, insertFn: InsertFn) => void;
  // 自定义选择附件，如图床
  customBrowseAndUpload?: (insertFn: InsertFn) => void;
  // 插入之后的回调
  onInsertedAttachment?: (elem: AttachmentElement) => void;
};

export function genUploadAttachmentMenuConfig(): IUploadConfigForAttachment {
  return {
    server: '', // server API 地址，需用户配置

    fieldName: 'wangeditor-uploaded-attachment', // formData 中，文件的 key
    maxFileSize: 10 * 1024 * 1024, // 10M
    maxNumberOfFiles: 5, // 最多上传 xx 个附件
    allowedFileTypes: ['*'],
    meta: {
      // 自定义上传参数，例如传递验证的 token 等。参数会被添加到 formData 中，一起上传到服务端。
      // 例如：token: 'xxxxx', x: 100
    },
    metaWithUrl: false,
    // headers: {
    //   // 自定义 http headers
    //   // 例如：Accept: 'text/x-json', a: 100,
    // },
    withCredentials: false,
    timeout: 30 * 1000, // 30s

    onBeforeUpload: (file: any) => file, // 返回 false 则终止上传
    onProgress: (progress: number) => {
      /* on progress */
    },
    onSuccess: (file: any, res: any) => {
      /* on success */
    },
    onFailed: (file: any, res: any) => {
      /* on failed */
    },
    onError: (file: any, err: any, res: any) => {
      /* on error */
      /* on timeout */
    },

    // 自定义插入附件，用户配置
    // customInsert: (res, file, insertFn) => {},

    // 自定义上传附件，用户配置
    // customUpload: (file, insertFn) => {},

    // 自定义选择，并上传附件，如：图床 （用户配置）
    // customBrowseAndUpload: insertFn => {},

    onInsertedAttachment(elem: AttachmentElement) {
      // 插入文件之后的 callback
    },
  };
}
