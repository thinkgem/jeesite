/**
 * @description upload attachment menu
 * @author wangfupeng
 */

import { DomEditor, IDomEditor, SlateRange, t } from '@wangeditor/editor';
import { IButtonMenu } from '@wangeditor/editor';
import { ATTACHMENT_SVG } from '../../constants/icon-svg';
import { IUploadConfigForAttachment } from './config';
import $ from '../../utils/dom';
import { insertAttachment, uploadAttachments } from './helper';

class UploadAttachmentMenu implements IButtonMenu {
  readonly title = t('attachment.upload');
  readonly iconSvg = ATTACHMENT_SVG;
  readonly tag = 'button';

  getValue(editor: IDomEditor): string | boolean {
    // 无需获取 val
    return '';
  }

  isActive(editor: IDomEditor): boolean {
    // 任何时候，都不用激活 menu
    return false;
  }

  exec(editor: IDomEditor, value: string | boolean) {
    const { allowedFileTypes = [], customBrowseAndUpload } = this.getMenuConfig(editor);

    // 自定义选择文件、并上传，如图床
    if (customBrowseAndUpload) {
      customBrowseAndUpload((fileName: string, link: string) => insertAttachment(editor, fileName, link));
      return;
    }

    // 设置选择文件的类型
    let acceptAttr = '';
    if (allowedFileTypes.length > 0) {
      acceptAttr = `accept="${allowedFileTypes.join(', ')}"`;
    }

    // 添加 file input（每次重新创建 input）
    const $body = $('body');
    const $inputFile = $(`<input type="file" ${acceptAttr} multiple/>`);
    $inputFile.hide();
    $body.append($inputFile);
    $inputFile.click();
    // 选中文件
    $inputFile.on('change', () => {
      const files = ($inputFile[0] as HTMLInputElement).files;
      uploadAttachments(editor, files); // 上传文件
    });
  }

  isDisabled(editor: IDomEditor): boolean {
    const { selection } = editor;
    if (selection == null) return true;
    if (SlateRange.isExpanded(selection)) return true; // 选区非折叠，禁用

    const selectedElems = DomEditor.getSelectedElems(editor);

    const hasVoidElem = selectedElems.some((elem) => editor.isVoid(elem));
    if (hasVoidElem) return true; // 选中了 void 元素，禁用

    const hasPreElem = selectedElems.some((elem) => DomEditor.getNodeType(elem) === 'pre');
    if (hasPreElem) return true; // 选中了 pre 原则，禁用

    return false;
  }

  private getMenuConfig(editor: IDomEditor): IUploadConfigForAttachment {
    // 获取配置，见 `./config.js`
    return editor.getMenuConfig('uploadAttachment') as IUploadConfigForAttachment;
  }
}

export default UploadAttachmentMenu;
