export const REDIRECT_NAME = 'Redirect';
export const PARENT_LAYOUT_NAME = 'ParentLayout';
export const PAGE_NOT_FOUND_NAME = 'PageNotFound';

/**
 * @description: default layout
 */
export const LAYOUT = () => import('@jeesite/core/layouts/default/index.vue');
export const IFRAME_BLANK = () => import('@jeesite/core/layouts/iframe/FrameBlank.vue');
export const IFRAME_SIMPLE = () => import('@jeesite/core/layouts/iframe/FrameSimple.vue');
export const EXCEPTION_COMPONENT = () => import('@jeesite/core/layouts/views/exception/Exception.vue');

/**
 * @description: parent-layout
 */
export const getParentLayout = (_name?: string) => {
  return () =>
    new Promise((resolve) => {
      resolve({
        name: PARENT_LAYOUT_NAME,
      });
    });
};
