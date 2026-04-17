/**
 * @description download attachment menu
 * @author wangfupeng
 */

import { DomEditor, IDomEditor, SlateRange, t } from '@wangeditor/editor';
import { IButtonMenu } from '@wangeditor/editor';
import { DOWNLOAD_SVG } from '../../constants/icon-svg';
import { AttachmentElement } from '../custom-types';

class DownloadAttachmentMenu implements IButtonMenu {
  readonly title = t('attachment.download');
  readonly iconSvg = DOWNLOAD_SVG;
  readonly tag = 'button';
  readonly alwaysEnable = true;

  getValue(editor: IDomEditor): string | boolean {
    const attachmentElem = this.getSelectedElem(editor);
    if (attachmentElem == null) return '';
    return attachmentElem.link;
  }

  isActive(editor: IDomEditor): boolean {
    // 任何时候，都不用激活 menu
    return false;
  }

  exec(editor: IDomEditor, value: string | boolean) {
    if (!value) return;
    if (typeof value !== 'string') return;
    window.open(value, '_blank');
  }

  isDisabled(editor: IDomEditor): boolean {
    const { selection } = editor;
    if (selection == null) return true;
    if (SlateRange.isExpanded(selection)) return true; // 选区非折叠，禁用

    // 未匹配到 attachment node 则禁用
    const attachmentElem = this.getSelectedElem(editor);
    if (attachmentElem == null) return true;

    return false;
  }

  private getSelectedElem(editor: IDomEditor): AttachmentElement | null {
    const node = DomEditor.getSelectedNodeByType(editor, 'attachment');
    if (node == null) return null;
    return node as AttachmentElement;
  }
}

export default DownloadAttachmentMenu;
