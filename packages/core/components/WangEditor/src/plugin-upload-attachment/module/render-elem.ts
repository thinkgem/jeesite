/**
 * @description render elem
 * @author wangfupeng
 */

import { h, VNode } from 'snabbdom';
import { DomEditor, IDomEditor, SlateElement } from '@wangeditor/editor';
import { AttachmentElement } from './custom-types';

function renderAttachment(elem: SlateElement, children: VNode[] | null, editor: IDomEditor): VNode {
  const isDisabled = editor.isDisabled();

  // 当前节点是否选中
  const selected = DomEditor.isNodeSelected(editor, elem);

  // 构建 vnode
  const { fileName = '', link = '' } = elem as AttachmentElement;
  const vnode = h(
    'span',
    {
      props: {
        contentEditable: false, // 不可编辑
      },
      style: {
        display: 'inline-block', // inline
        marginLeft: '3px',
        marginRight: '3px',
        border:
          selected && !isDisabled
            ? '2px solid var(--w-e-textarea-selected-border-color)' // wangEditor 提供了 css var https://www.wangeditor.com/v5/theme.html
            : '2px solid transparent',
        borderRadius: '3px',
        padding: '0 3px',
        backgroundColor: '#f1f1f1',
        cursor: isDisabled ? 'pointer' : 'inherit',
      },
      on: {
        // disable 时，点击下载附件
        click() {
          if (!isDisabled) return;
          if (link) {
            window.open(link, '_blank');
          }
        },
      },
    },
    [
      h('img', {
        props: {
          src: 'data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAACAAAAAgCAYAAABzenr0AAAAAXNSR0IArs4c6QAAAv5JREFUWEetl1nITVEUx3/fo5LkyRQeFA+SIS8eZHhQimRKMhMSoogyR5QxMs9DkplQEhFvxihCKUWkpCTP9P9a+1p3f2efe869367T3Xeftdf/v9e012mi9cZQYDzQBxgIfANeAO+BDSmYplbCX58HAnwFFgNXY7zWIHABmBQpfg10A9pH64OAZ36tUQLngClO4XVgC/DU1iYCF937N+aiylIjBM4A05xy+XljwqV/3bpkKjFRL4GTwMyU0gwSY4Abtn4NGBdk6iFwFJhbAjyI/gLaAR+BnvUSOATML2j22BCvgL7AbyPS/L6MBfYDC3NOrlS8BLzNcIGi/4mtPwSGlbXAHmBJDriCSgQU5UrJmIT8Ptb277OaUNgCu4BlBcCDSFWUGzFfCXV6WaEQge3A8hLgcSquBjbnBWxeDGwFVtXwuT9ZfHLtlY4wFB9xxUwGoViLfVGzxuCymqyXC57KAilb1wC44kVxUxM8i8Bs4HgJn8cnV6YoYwqBxwR6A/eBzrY7Vh5SLeUW1QjVisLgMQEFyQTbfQqY5ZTVAp8HHC4L7gnocrhiCj5ZpdKvxlJgd45b5gDHcsDzKmQlC3zUrwS2mcJ+wAPXWMRumQHIWimzh04pVSErBG4Do0zLaOCWzX2rpeZjqgPT/GwB8FTMNK+HQqSerZNJKgjVUGro9Go2NXwJnQycLwGebFYCgS9AF1PYEfhu8wXAQZurfh8AutcoMnGDGrvN8f5vAQVg6FJGAndNqq211SE1qzbb9evLaylw74I1wCbTvgLY4ZAGAzeBDhH6kYzmRARyfR6fILhAd7XubI0fQC/gpxNuY23YcOCDdbrP3fvSJw97/W14B5D5NR4DQ2K2if91g3sXaN4V+OxAlN+LgD8J4B7WbJTpjluoivuB6cBpJ6VquBd4BAST67tP1tHFIxKlfJ6KAb8+AriXcWqlarCUf60ashY4UdBlVWKpjkgRvzP6+MjSf9nA39UDHsdAlg7VhgFAf3sk89IefXq3+NotS+QfmNG0IS7jBhcAAAAASUVORK5CYII=',
        },
        style: {
          width: '1em',
          marginRight: '0.1em',
          minWidth: '0',
          minHeight: '0',
        },
      }),
      fileName,
    ],
  );

  return vnode;
}

const conf = {
  type: 'attachment', // 节点 type ，重要！！！
  renderElem: renderAttachment,
};

export default conf;
