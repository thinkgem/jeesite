// import { openWindow } from '..';
import { dataURLtoBlob, urlToBase64 } from './base64Conver';
import { defHttp } from '@jeesite/core/utils/http/axios';
import { message } from 'antdv-next';

/** 正在下载中的请求映射，用于防止短时间内重复点击下载 */
const downloadingMap = new Map<string, Promise<boolean>>();

/**
 * 根据请求参数生成唯一 key，用于防重复下载判断
 */
function getDownloadKey(url: string, params?: any, data?: any): string {
  return JSON.stringify({ url, params, data });
}

/**
 * Download online pictures
 * @param url
 * @param filename
 * @param mime
 * @param bom
 */
export function downloadByOnlineUrl(url: string, filename: string, mime?: string, bom?: BlobPart) {
  urlToBase64(url).then((base64) => {
    downloadByBase64(base64, filename, mime, bom);
  });
}

/**
 * Download pictures based on base64
 * @param buf
 * @param filename
 * @param mime
 * @param bom
 */
export function downloadByBase64(buf: string, filename: string, mime?: string, bom?: BlobPart) {
  const base64Buf = dataURLtoBlob(buf);
  downloadByData(base64Buf, filename, mime, bom);
}

/**
 * Download according to the background interface file stream
 * @param {*} data
 * @param {*} filename
 * @param {*} mime
 * @param {*} bom
 */
export function downloadByData(data: BlobPart, filename: string, mime?: string, bom?: BlobPart) {
  const blobData = typeof bom !== 'undefined' ? [bom, data] : [data];
  const blob = new Blob(blobData, { type: mime || 'application/octet-stream' });
  const nav = window.navigator as any;
  if (typeof nav.msSaveBlob !== 'undefined') {
    nav.msSaveBlob(blob, filename);
  } else {
    const blobURL = window.URL.createObjectURL(blob);
    const tempLink = document.createElement('a');
    tempLink.style.display = 'none';
    tempLink.href = blobURL;
    tempLink.setAttribute('download', filename);
    if (typeof tempLink.download === 'undefined') {
      tempLink.setAttribute('target', '_blank');
    }
    document.body.appendChild(tempLink);
    tempLink.click();
    document.body.removeChild(tempLink);
    window.URL.revokeObjectURL(blobURL);
  }
}

/**
 * Download file according to file address
 * @param {*} sUrl
 */
export async function downloadByUrl({
  url,
  params,
  data,
  // target = '_self',
  fileName,
  json,
}: {
  url: string;
  params?: any;
  data?: any;
  // target?: TargetContext;
  fileName?: string;
  json?: boolean;
}): Promise<boolean> {
  const key = getDownloadKey(url, params, data);

  // 如果同一请求正在下载中，提示用户并直接返回，防止重复下载
  if (downloadingMap.has(key)) {
    message.warning('文件正在下载中，请稍后...');
    return false;
  }

  const downloadPromise = (async (): Promise<boolean> => {
    try {
      const res = await defHttp[json ? 'postJson' : 'post'](
        { url, params, data, responseType: 'blob' },
        { isReturnNativeResponse: true, joinPrefix: false },
      );
      let name = res.headers['content-disposition'];
      if (name) {
        // 优先解析 RFC 5987 格式 filename*=UTF-8''xxx
        const filenameStar = name.match(/filename\*=([^;]*'[^']*')?([^;]+)/i);
        if (filenameStar) {
          name = filenameStar[2];
        } else {
          // 再解析普通 filename="xxx" 或 filename=xxx
          const filename = name.match(/filename=["']?([^";\n]+)["']?/i);
          name = filename ? filename[1] : '';
        }
        // 去掉两端引号及 RFC 5987 语言标记（如 utf-8'zh_cn'）
        name = name.replace(/^(?:["']|[a-zA-Z0-9_-]+'[a-zA-Z0-9_-]+')+|(?:["'])$/g, '');
        name = decodeURIComponent(name) || '';
      }
      // 调用方显式传入 fileName 时，优先使用调用方文件名，避免后端临时/转义文件名被直接使用
      name = fileName || name || 'jeesite';
      downloadByData(res.data, name);
      return true;
    } finally {
      // 无论成功或失败，下载完成后移除锁，允许下次下载
      downloadingMap.delete(key);
    }
  })();

  downloadingMap.set(key, downloadPromise);
  return downloadPromise;
}
