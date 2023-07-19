// axios配置  可自行根据项目进行更改，只需更改该文件即可，其他文件可以不动
// The axios configuration can be changed according to the project, just change the file, other files can be left unchanged

import type { AxiosResponse } from 'axios';
import type { RequestOptions, Result } from '/#/axios';
import type { AxiosTransform, CreateAxiosOptions } from './axiosTransform';
import { VAxios } from './Axios';
import { checkStatus } from './checkStatus';
import { useGlobSetting } from '/@/hooks/setting';
import { useMessage } from '/@/hooks/web/useMessage';
import { RequestEnum, ContentTypeEnum } from '/@/enums/httpEnum';
import { isString } from '/@/utils/is';
import { getToken } from '/@/utils/auth';
import { setObjToUrlParams, deepMerge } from '/@/utils';
import { useErrorLogStoreWithOut } from '/@/store/modules/errorLog';
import { useI18n } from '/@/hooks/web/useI18n';
import { joinTimestamp, formatRequestDate } from './helper';
import { useUserStoreWithOut } from '/@/store/modules/user';
import { router } from '/@/router';
import { PageEnum } from '/@/enums/pageEnum';

const globSetting = useGlobSetting();
const urlPrefix = globSetting.urlPrefix;
const { showMessageModal, showMessage } = useMessage();
let isShowMessage = false;
let isShowMessageModal = false;

/**
 * @description: 数据处理，方便区分多种处理方式
 */
const transform: AxiosTransform = {
  /**
   * @description: 处理请求数据。如果数据不是预期格式，可直接抛出错误
   */
  transformRequestHook: (res: AxiosResponse<Result>, options: RequestOptions) => {
    const { t } = useI18n();
    const { isTransformResponse, isReturnNativeResponse } = options;
    // 是否返回原生响应头 比如：需要获取响应头时使用该属性
    if (isReturnNativeResponse) {
      return res;
    }
    // 不进行任何处理，直接返回
    // 用于页面代码可能需要直接获取code，data，message这些信息时开启
    if (!isTransformResponse) {
      return res.data;
    }
    // 错误的时候返回

    const { data, config } = res;
    // if (!data) { // boolean false return error
    //   // return '[HTTP] Request has no return value';
    //   throw new Error(t('sys.api.apiRequestFailed'));
    // }

    // 非对象类型的直接返回数据
    if (typeof data !== 'object') return data;

    //  这里 code，result，message为 后台统一的字段，需要在 types.ts内修改为项目自己的接口返回格式
    // const { code, result, message } = data;
    const { sessionid, result, message } = data;

    // 设置会话编码
    const userStore = useUserStoreWithOut();
    if (data && Reflect.has(data, 'sessionid')) {
      userStore.setToken(sessionid);
    }

    if (data && Reflect.has(data, 'result')) {
      if (result === 'login' && options.errorMessageMode !== 'none') {
        if (!isShowMessage) {
          isShowMessage = true;
          // userStore.resetState();
          const currentRoute = router.currentRoute.value;
          if (currentRoute.path !== '/') {
            showMessage(data.message, undefined, 180);
          }
          if (config.url?.indexOf('__notUpdateSession=true') == -1) {
            let path = PageEnum.BASE_LOGIN as string;
            if (currentRoute.path !== '/' && currentRoute.path !== PageEnum.BASE_LOGIN) {
              path = path + '?redirect=' + currentRoute.fullPath;
            }
            router.replace(path);
          }
          setTimeout(() => (isShowMessage = false), 1000);
        }
        throw new Error(t('sys.api.timeoutMessage'));
      } else if (result === 'false') {
        const errorMessage = message || t('sys.api.apiRequestFailed');
        if (options.errorMessageMode === 'modal') {
          if (!isShowMessageModal) {
            isShowMessageModal = true;
            showMessageModal({
              content: errorMessage,
              onOk() {
                isShowMessageModal = false;
                return Promise.resolve();
              },
            });
          }
          throw new Error(errorMessage);
        } else if (options.errorMessageMode === 'message') {
          if (!isShowMessage) {
            isShowMessage = true;
            showMessage(errorMessage);
            setTimeout(() => (isShowMessage = false), 1000);
          }
          throw new Error(errorMessage);
        }
      }
    }
    return data;
  },

  // 请求之前处理config
  beforeRequestHook: (config, options) => {
    const { apiUrl, joinPrefix, joinParamsToUrl, formatDate, joinTime = true, urlPrefix } = options;

    if (joinPrefix) {
      config.url = `${urlPrefix}${config.url}`;
    }

    if (apiUrl && isString(apiUrl)) {
      config.url = `${apiUrl}${config.url}`;
    }
    const params = config.params || {};
    const data = config.data || false;
    formatDate && data && !isString(data) && formatRequestDate(data);
    if (config.method?.toUpperCase() === RequestEnum.GET) {
      if (!isString(params)) {
        // 给 get 请求加上时间戳参数，避免从缓存中拿数据。
        config.params = Object.assign(params || {}, joinTimestamp(joinTime, false));
      } else {
        // 兼容restful风格
        config.url = config.url + params + `${joinTimestamp(joinTime, true)}`;
        config.params = undefined;
      }
    } else {
      if (!isString(params)) {
        formatDate && formatRequestDate(params);
        if (Reflect.has(config, 'data') && config.data && Object.keys(config.data).length > 0) {
          config.data = data;
          config.params = params;
        } else {
          // 非GET请求如果没有提供data，则将params视为data
          config.data = params;
          config.params = undefined;
        }
        if (joinParamsToUrl) {
          config.url = setObjToUrlParams(
            config.url as string,
            Object.assign({}, config.params, config.data),
          );
        }
      } else {
        // 兼容restful风格
        config.url = config.url + params;
        config.params = undefined;
      }
    }
    return config;
  },

  /**
   * @description: 请求拦截器处理
   */
  requestInterceptors: (config: Recordable, options) => {
    // 请求之前处理config
    const token = getToken();
    if (token && config?.requestOptions?.withToken !== false) {
      // // jwt token
      // config.headers.Authorization = options.authenticationScheme
      //   ? `${options.authenticationScheme} ${token}`
      //   : token;
      config.headers['x-token'] = options.authenticationScheme
        ? `${options.authenticationScheme} ${token}`
        : token;
    }
    return config;
  },

  /**
   * @description: 响应拦截器处理
   */
  responseInterceptors: (res: AxiosResponse<any>) => {
    return res;
  },

  /**
   * @description: 响应错误处理
   */
  responseInterceptorsCatch: (error: any) => {
    const { t } = useI18n();
    const errorLogStore = useErrorLogStoreWithOut();
    errorLogStore.addAjaxErrorInfo(error);
    const { response, code, message, config } = error || {};
    const errorMessageMode = config?.requestOptions?.errorMessageMode || 'none';
    const msg: string = response?.data?.message ?? '';
    const err: string = error?.toString?.() ?? '';
    let errMessage = '';

    try {
      if (code === 'ECONNABORTED' && message.indexOf('timeout') !== -1) {
        errMessage = t('sys.api.apiTimeoutMessage');
      } else if (err?.includes('Network Error')) {
        errMessage = t('sys.api.networkExceptionMsg');
      } else if (code === 'ERR_BAD_RESPONSE') {
        errMessage = t('sys.api.apiRequestFailed');
      }

      if (errMessage) {
        if (errorMessageMode === 'modal') {
          if (!isShowMessageModal) {
            isShowMessageModal = true;
            showMessageModal({
              title: t('sys.api.errorTip'),
              content: msg || errMessage,
              onOk() {
                isShowMessageModal = false;
                return Promise.resolve();
              },
            });
          }
        } else if (errorMessageMode === 'message') {
          if (!isShowMessage) {
            isShowMessage = true;
            showMessage({ content: msg || errMessage }, 'error');
            setTimeout(() => (isShowMessage = false), 1000);
          }
        }
        return Promise.reject(error);
      }
    } catch (error: any) {
      throw new Error(error);
    }

    checkStatus(error?.response?.status, msg, errorMessageMode);
    return Promise.reject(error);
  },
};

function createAxios(opt?: Partial<CreateAxiosOptions>) {
  return new VAxios(
    deepMerge(
      {
        // See https://developer.mozilla.org/en-US/docs/Web/HTTP/Authentication#authentication_schemes
        // authentication schemes，e.g: Bearer
        // authenticationScheme: 'Bearer',
        authenticationScheme: '',
        // 请求超时时间，默认3分钟
        timeout: 3 * 60 * 1000,
        // 基础接口地址
        // baseURL: globSetting.apiUrl,
        // 默认请求头设置
        // headers: { 'Content-Type': ContentTypeEnum.JSON },
        // 如果是form-data格式
        headers: {
          'content-type': ContentTypeEnum.FORM_URLENCODED,
          'x-requested-with': 'XMLHttpRequest',
          'x-ajax': 'json',
        },
        // 数据处理方式
        transform,
        // 配置项，下面的选项都可以在独立的接口请求中覆盖
        requestOptions: {
          // 默认将prefix 添加到url
          joinPrefix: true,
          // 是否返回原生响应头 比如：需要获取响应头时使用该属性
          isReturnNativeResponse: false,
          // 需要对返回数据进行处理
          isTransformResponse: true,
          // post请求的时候添加参数到url
          joinParamsToUrl: false,
          // 格式化提交参数时间
          formatDate: false,
          // 消息提示类型
          errorMessageMode: 'modal',
          // 接口地址
          apiUrl: globSetting.apiUrl,
          // 接口拼接地址
          urlPrefix: urlPrefix,
          //  是否加入时间戳
          joinTime: true,
          // 忽略重复请求
          ignoreCancelToken: true,
          // 是否携带token
          withToken: true,
        },
      },
      opt || {},
    ),
  );
}
export const defHttp = createAxios();

// other api url
// export const otherHttp = createAxios({
//   requestOptions: {
//     apiUrl: 'xxx',
//     urlPrefix: 'xxx',
//   },
// });
