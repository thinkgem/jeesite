/**
 * @description elem to html
 * @author wangfupeng
 */

import { SlateElement } from '@wangeditor/editor';
import { AttachmentElement } from './custom-types';

// 生成 html 的函数
function attachmentToHtml(elem: SlateElement, childrenHtml: string): string {
  const { link = '', fileName = '' } = elem as AttachmentElement;
  return `<a data-w-e-type="attachment" data-w-e-is-void data-w-e-is-inline href="${link}" download="${fileName}">${fileName}</a>`;
}

// 配置
const conf = {
  type: 'attachment', // 节点 type ，重要！！！
  elemToHtml: attachmentToHtml,
};

export default conf;
