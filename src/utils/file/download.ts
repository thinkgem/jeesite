// import { openWindow } from '..';
import { dataURLtoBlob, urlToBase64 } from './base64Conver';
import { defHttp } from '/@/utils/http/axios';

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
}: {
  url: string;
  params?: any;
  data?: any;
  // target?: TargetContext;
  fileName?: string;
}): Promise<boolean> {
  const res = await defHttp.post(
    { url, params, data, responseType: 'blob' },
    { isReturnNativeResponse: true, joinPrefix: false },
  );
  let name = res.headers['content-disposition'];
  name = name && name.match(/filename[^;=\n]*=((['"]).*?\2|[^;\n]*)/);
  name = name && name.length >= 1 && name[1].replace("utf-8'zh_cn'", '');
  name = name && (decodeURIComponent(name) || fileName || 'jeesite');
  downloadByData(res.data, name);
  // axios({
  //   url: url,
  //   method: 'post',
  //   data: data,
  //   responseType: 'blob',
  // })
  //   .then((response) => {
  //     let name = response.headers['content-disposition'];
  //     name = name && name.match(/filename[^;=\n]*=((['"]).*?\2|[^;\n]*)/);
  //     name = name && name.length >= 1 && name[1].replace("utf-8'zh_cn'", '');
  //     name = name && (decodeURIComponent(name) || fileName || 'jeesite');
  //     downloadByData(response.data, name);
  //   })
  //   .catch((error) => {
  //     console.error(error);
  //   });

  // const isChrome = window.navigator.userAgent.toLowerCase().indexOf('chrome') > -1;
  // const isSafari = window.navigator.userAgent.toLowerCase().indexOf('safari') > -1;

  // if (/(iP)/g.test(window.navigator.userAgent)) {
  //   console.error('Your browser does not support download!');
  //   return false;
  // }
  // if (isChrome || isSafari) {
  //   const link = document.createElement('a');
  //   link.href = url;
  //   link.target = target;

  //   if (link.download !== undefined) {
  //     link.download = fileName || url.substring(url.lastIndexOf('/') + 1, url.length);
  //   }

  //   if (document.createEvent) {
  //     const e = document.createEvent('MouseEvents');
  //     e.initEvent('click', true, true);
  //     link.dispatchEvent(e);
  //     return true;
  //   }
  // }
  // if (url.indexOf('?') === -1) {
  //   url += '?download';
  // }

  // openWindow(url, { target });
  return true;
}
