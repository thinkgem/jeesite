import type { ErrorMessageMode } from '@jeesite/types/axios';
import { useMessage } from '@jeesite/core/hooks/web/useMessage';
import { useI18n } from '@jeesite/core/hooks/web/useI18n';
// import router from '@jeesite/core/router';
// import { PageEnum } from '@jeesite/core/enums/pageEnum';
import { useUserStoreWithOut } from '@jeesite/core/store/modules/user';
import projectSetting from '@jeesite/core/settings/projectSetting';
import { SessionTimeoutProcessingEnum } from '@jeesite/core/enums/appEnum';

const { showMessageModal, showMessage } = useMessage();
// const error = createMessage.error!;
const stp = projectSetting.sessionTimeoutProcessing;

export function checkStatus(status: number, msg: string, errorMessageMode: ErrorMessageMode = 'message'): void {
  const { t } = useI18n();
  const userStore = useUserStoreWithOut();
  let errMessage = '';

  switch (status) {
    case 400:
      errMessage = `${msg}`;
      break;
    // 401: Not logged in
    // Jump to the login page if not logged in, and carry the path of the current page
    // Return to the current page after successful login. This step needs to be operated on the login page.
    case 401:
      userStore.setToken(undefined);
      errMessage = msg || t('sys.api.errMsg401');
      if (stp === SessionTimeoutProcessingEnum.PAGE_COVERAGE) {
        userStore.setSessionTimeout(true);
      } else {
        userStore.logout(true);
      }
      break;
    case 403:
      errMessage = msg || t('sys.api.errMsg403');
      break;
    // 404请求不存在
    case 404:
      errMessage = msg || t('sys.api.errMsg404');
      break;
    case 405:
      errMessage = msg || t('sys.api.errMsg405');
      break;
    case 408:
      errMessage = msg || t('sys.api.errMsg408');
      break;
    case 500:
      errMessage = msg || t('sys.api.errMsg500');
      break;
    case 501:
      errMessage = msg || t('sys.api.errMsg501');
      break;
    case 502:
      errMessage = msg || t('sys.api.errMsg502');
      break;
    case 503:
      errMessage = msg || t('sys.api.errMsg503');
      break;
    case 504:
      errMessage = msg || t('sys.api.errMsg504');
      break;
    case 505:
      errMessage = msg || t('sys.api.errMsg505');
      break;
    default:
  }

  if (errMessage) {
    if (errorMessageMode === 'modal') {
      showMessageModal({ title: t('sys.api.errorTip'), content: errMessage }, 'error');
    } else if (errorMessageMode === 'message') {
      showMessage({ content: errMessage, key: `global_error_message_status_${status}` }, 'error');
    }
  }
}
