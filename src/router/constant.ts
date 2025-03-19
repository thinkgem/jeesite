export const REDIRECT_NAME = 'Redirect';
export const PARENT_LAYOUT_NAME = 'ParentLayout';
export const PAGE_NOT_FOUND_NAME = 'PageNotFound';

/**
 * @description: default layout
 */
export const LAYOUT = () => import('/@/layouts/default/index.vue');
export const IFRAME_BLANK = () => import('/@/layouts/iframe/FrameBlank.vue');
export const IFRAME_SIMPLE = () => import('/@/layouts/iframe/FrameSimple.vue');
export const EXCEPTION_COMPONENT = () => import('/@/layouts/views/exception/Exception.vue');

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
