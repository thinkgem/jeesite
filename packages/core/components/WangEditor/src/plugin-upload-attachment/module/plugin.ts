/**
 * @description attachment plugin
 * @author wangfupeng
 */

import { DomEditor, IDomEditor } from '@wangeditor/editor';

function withAttachment<T extends IDomEditor>(editor: T) {
  const { isInline, isVoid } = editor;
  const newEditor = editor;

  // 重写 isInline
  newEditor.isInline = (elem) => {
    const type = DomEditor.getNodeType(elem);
    if (type === 'attachment') {
      return true;
    }

    return isInline(elem);
  };

  // 重写 isVoid
  newEditor.isVoid = (elem) => {
    const type = DomEditor.getNodeType(elem);
    if (type === 'attachment') {
      return true;
    }

    return isVoid(elem);
  };

  return newEditor;
}

export default withAttachment;
