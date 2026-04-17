/**
 * @description attachment module entry
 * @author wangfupeng
 */

import './local'; // 多语言

import { IModuleConf } from '@wangeditor/editor';
import withAttachment from './plugin';
import renderElemConf from './render-elem';
import elemToHtmlConf from './elem-to-html';
import parseHtmlConf from './parse-elem-html';
import { uploadAttachmentMenuConf, downloadAttachmentMenuConf } from './menu/index';

const module: Partial<IModuleConf> = {
  editorPlugin: withAttachment,
  renderElems: [renderElemConf],
  elemsToHtml: [elemToHtmlConf],
  parseElemsHtml: [parseHtmlConf],
  menus: [uploadAttachmentMenuConf, downloadAttachmentMenuConf],
};

export default module;
