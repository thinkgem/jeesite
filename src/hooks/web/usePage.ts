import type { RouteLocationRaw, Router } from 'vue-router';

import { PageEnum } from '/@/enums/pageEnum';
import { isString } from '/@/utils/is';
import { computed, unref } from 'vue';

import { useRouter } from 'vue-router';
import { REDIRECT_NAME } from '/@/router/constant';

export type RouteLocationRawEx = Omit<RouteLocationRaw, 'path'> & {
  path: PageEnum | string;
  query?: object;
};

function handleError(e: Error) {
  console.error(e);
}

// page switch
export function useGo(_router?: Router) {
  let router: any;
  if (!_router) {
    router = useRouter();
  }
  const { push, replace } = _router || router;
  async function go(
    opt: PageEnum | RouteLocationRawEx | string = PageEnum.BASE_HOME,
    isReplace = false,
  ) {
    if (!opt) {
      return;
    }
    if (isString(opt)) {
      isReplace ? await replace(opt).catch(handleError) : await push(opt).catch(handleError);
    } else {
      const o = opt as RouteLocationRaw;
      isReplace ? await replace(o).catch(handleError) : await push(o).catch(handleError);
    }
  }
  return go;
}

/**
 * @description: redo current page
 */
export const useRedo = (_router?: Router) => {
  const { push, currentRoute } = _router || useRouter();
  const { query, params = {}, name, fullPath } = unref(currentRoute.value);
  function redo(): Promise<boolean> {
    return new Promise((resolve) => {
      if (name === REDIRECT_NAME) {
        resolve(false);
        return;
      }
      if (name && Object.keys(params).length > 0) {
        params['_redirect_type'] = 'name';
        params['path'] = String(name);
      } else {
        params['_redirect_type'] = 'path';
        params['path'] = fullPath;
      }
      push({ name: REDIRECT_NAME, params, query }).then(() => resolve(true));
    });
  }
  return redo;
};

export function useQuery(_router?: Router) {
  let router;
  if (!_router) {
    router = useRouter();
  }
  const query = computed(() => {
    return unref(router.currentRoute).query;
  });
  return query;
}
