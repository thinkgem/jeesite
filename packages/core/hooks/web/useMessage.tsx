/**
 * Copyright (c) 2013-Now https://jeesite.com All rights reserved.
 * No deletion without permission, or be held responsible to law.
 * @author Vben、ThinkGem
 */
import { Modal, message, notification } from 'antdv-next';
import { InfoCircleFilled, CheckCircleFilled, CloseCircleFilled } from '@antdv-next/icons';

import { useI18n } from './useI18n';
import { isString } from '@jeesite/core/utils/is';
import { Icon } from '@jeesite/core/components/Icon';
import { ModalFuncProps } from 'antdv-next/dist/modal/interface';

export interface ModalOptionsEx extends Omit<ModalFuncProps, 'iconType'> {
  iconType?: 'warning' | 'success' | 'error' | 'info';
  content?: any;
}

export type ModalOptionsPartial = Partial<ModalOptionsEx> & Pick<ModalOptionsEx, 'content'>;

function getIcon(iconType: string) {
  if (iconType === 'warning') {
    return <InfoCircleFilled class="modal-icon-warning" />;
  } else if (iconType === 'success') {
    return <CheckCircleFilled class="modal-icon-success" />;
  } else if (iconType === 'info') {
    return <InfoCircleFilled class="modal-icon-info" />;
  } else {
    return <CloseCircleFilled class="modal-icon-error" />;
  }
}

function renderContent({ content }: Pick<ModalOptionsEx, 'content'>) {
  if (isString(content)) {
    return <div innerHTML={`<div>${content as string}</div>`}></div>;
  } else {
    return content;
  }
}

const getBaseOptions = () => {
  const { t } = useI18n();
  return {
    okText: t('common.okText'),
    cancelText: t('common.cancelText'),
    centered: true,
  };
};

function createConfirm(options: ModalOptionsEx) {
  const iconType = options.iconType || 'info';
  Reflect.deleteProperty(options, 'iconType');
  const opt: any = {
    closable: true,
    maskClosable: true,
    autoFocusButton: 'cancel',
    ...getBaseOptions(),
    ...options,
    icon: getIcon(iconType),
    content: renderContent(options),
  };
  return Modal.confirm(opt);
}

function createModalOptions(options: ModalOptionsPartial, icon: string): ModalOptionsPartial | any {
  return {
    ...getBaseOptions(),
    ...options,
    content: renderContent(options),
    icon: getIcon(icon),
  };
}

function createSuccessModal(options: ModalOptionsPartial) {
  return Modal.success(createModalOptions(options, 'success'));
}

function createErrorModal(options: ModalOptionsPartial) {
  return Modal.error(createModalOptions(options, 'close'));
}

function createInfoModal(options: ModalOptionsPartial) {
  return Modal.info(createModalOptions(options, 'info'));
}

function createWarningModal(options: ModalOptionsPartial) {
  return Modal.warning(createModalOptions(options, 'warning'));
}

function contains(str, searchs) {
  if (typeof str === 'object' && str.content) {
    str = str.content;
  }
  if (typeof str === 'object' && str.props?.innerHTML) {
    str = str.props?.innerHTML;
  }
  if (typeof str === 'string' && searchs) {
    const ss = searchs.split(',');
    for (let i = 0; i < ss.length; i++) {
      if (str.indexOf(ss[i]) >= 0) {
        return true;
      }
    }
  }
  return false;
}

function showMessageModal(options: ModalOptionsPartial | string, type?: string) {
  const { t } = useI18n();
  if (typeof options === 'string') options = { content: options };
  if (typeof options.content === 'string' && options.content.startsWith('posfull:')) {
    options.content = '<div class="modal-posfull-content">' + options.content.substring(8) + '</div>';
    options.width = '80%';
  }
  if (type === 'error' || contains(options.content, t('sys.message.error'))) {
    return Modal.error(createModalOptions(options, 'close'));
  } else if (type === 'warning' || contains(options.content, t('sys.message.warning'))) {
    return Modal.warning(createModalOptions(options, 'warning'));
  } else if (type === 'success' || contains(options.content, t('sys.message.success'))) {
    return Modal.success(createModalOptions(options, 'success'));
  } else {
    return Modal.info(createModalOptions(options, 'info'));
  }
}

function showMessage(content: any, type?: string, duration?: number, onClose?: any) {
  const { t } = useI18n();
  let messageRemove = () => {};
  if (typeof content === 'string' && content.startsWith('posfull:')) {
    content = {
      content: (
        <div style="position: relative;" onMouseout={() => setTimeout(messageRemove, 1000)}>
          <div style="position: absolute; right: -6px; top: -20px;" onClick={() => messageRemove()}>
            <Icon icon="i-ant-design:close-outlined" color="#555" class="cursor-pointer" />
          </div>
          <div class="text-left" innerHTML={content.substring(8)}></div>
        </div>
      ),
      duration,
    };
  }
  if (type === 'error' || contains(content, t('sys.message.error'))) {
    messageRemove = message.error(content, duration, onClose);
  } else if (type === 'warning' || contains(content, t('sys.message.warning'))) {
    messageRemove = message.warning(content, duration, onClose);
  } else if (type === 'success' || contains(content, t('sys.message.success'))) {
    messageRemove = message.success(content, duration, onClose);
  } else {
    messageRemove = message.info(content, duration, onClose);
  }
  return messageRemove;
}

notification.config({
  placement: 'topRight',
  duration: 3,
});

/**
 * @description: message
 */
export function useMessage() {
  return {
    notification,
    createMessage: message,
    createConfirm,
    createSuccessModal,
    createErrorModal,
    createInfoModal,
    createWarningModal,
    showMessageModal,
    showMessage,
  };
}
