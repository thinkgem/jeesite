<template>
  <div :class="[prefixCls, `${prefixCls}--${theme}`]">
    <a-breadcrumb :routes="routes">
      <template #itemRender="{ route, routes: routesMatched, paths }">
        <Icon :icon="getIcon(route)" v-if="getShowBreadCrumbIcon && getIcon(route)" />
        <span v-if="!hasRedirect(routesMatched, route)">
          {{ t(route.name || route.meta.title) }}
        </span>
        <router-link v-else to="" @click="handleClick(route, paths, $event)">
          {{ t(route.name || route.meta.title) }}
        </router-link>
      </template>
    </a-breadcrumb>
  </div>
</template>
<script lang="ts">
  import type { RouteLocationMatched } from 'vue-router';
  import { useRouter } from 'vue-router';
  import type { Menu } from '@jeesite/core/router/types';

  import { defineComponent, ref, watchEffect } from 'vue';

  import { Breadcrumb } from 'ant-design-vue';
  import { Icon } from '@jeesite/core/components/Icon';

  import { useDesign } from '@jeesite/core/hooks/web/useDesign';
  import { useRootSetting } from '@jeesite/core/hooks/setting/useRootSetting';
  import { useGo } from '@jeesite/core/hooks/web/usePage';
  import { useI18n } from '@jeesite/core/hooks/web/useI18n';

  import { propTypes } from '@jeesite/core/utils/propTypes';
  import { isString } from '@jeesite/core/utils/is';
  import { filter } from '@jeesite/core/utils/helper/treeHelper';
  import { getMenus } from '@jeesite/core/router/menus';

  import { REDIRECT_NAME } from '@jeesite/core/router/constant';
  import { getAllParentPath } from '@jeesite/core/router/helper/menuHelper';

  export default defineComponent({
    name: 'LayoutBreadcrumb',
    components: { Icon, [Breadcrumb.name as string]: Breadcrumb },
    props: {
      theme: propTypes.oneOf(['dark', 'light']),
    },
    setup() {
      const routes = ref<RouteLocationMatched[]>([]);
      const { currentRoute } = useRouter();
      const { prefixCls } = useDesign('layout-breadcrumb');
      const { getShowBreadCrumbIcon } = useRootSetting();
      const go = useGo();

      const { t } = useI18n();
      watchEffect(async () => {
        if (currentRoute.value.name === REDIRECT_NAME) return;
        const menus = await getMenus();

        const routeMatched = currentRoute.value.matched;
        const cur = routeMatched?.[routeMatched.length - 1];
        let path = currentRoute.value.path;

        if (cur && cur?.meta?.currentActiveMenu) {
          path = cur.meta.currentActiveMenu as string;
        }

        const parent = getAllParentPath(menus, path);
        const filterMenus = menus.filter((item) => item.path === parent[0]);
        const matched = getMatched(filterMenus, parent) as any;

        if (!matched || matched.length === 0) return;

        const breadcrumbList = filterItem(matched);

        if (currentRoute.value.meta?.currentActiveMenu) {
          breadcrumbList.push({
            ...currentRoute.value,
            name: currentRoute.value.meta?.title || currentRoute.value.name,
          } as unknown as RouteLocationMatched);
        }
        routes.value = breadcrumbList;
      });

      function getMatched(menus: Menu[], parent: string[]) {
        const metched: Menu[] = [];
        menus.forEach((item) => {
          if (parent.includes(item.path)) {
            metched.push({
              ...item,
              name: item.meta?.title || item.name,
            });
          }
          if (item.children?.length) {
            metched.push(...getMatched(item.children, parent));
          }
        });
        return metched;
      }

      function filterItem(list: RouteLocationMatched[]) {
        return filter(list, (item) => {
          const { meta, name } = item;
          if (!meta) {
            return !!name;
          }
          const { title, hideBreadcrumb, hideMenu } = meta;
          if (!title || hideBreadcrumb || hideMenu) {
            return false;
          }
          return true;
        }).filter((item) => !item.meta?.hideBreadcrumb || !item.meta?.hideMenu);
      }

      function handleClick(route: RouteLocationMatched, paths: string[], e: Event) {
        e?.preventDefault();
        const { children, redirect, meta } = route;

        if (children?.length && !redirect) {
          e?.stopPropagation();
          return;
        }
        if (meta?.carryParam) {
          return;
        }

        if (redirect && isString(redirect)) {
          go(redirect);
        } else {
          let goPath = '';
          if (paths.length === 1) {
            goPath = paths[0];
          } else {
            const ps = paths.slice(1);
            const lastPath = ps.pop() || '';
            goPath = `${lastPath}`;
          }
          goPath = /^\//.test(goPath) ? goPath : `/${goPath}`;
          go(goPath);
        }
      }

      function hasRedirect(routes: RouteLocationMatched[], route: RouteLocationMatched) {
        return routes.indexOf(route) !== routes.length - 1;
      }

      function getIcon(route) {
        return route.icon || route.meta?.icon;
      }

      return { routes, t, prefixCls, getIcon, getShowBreadCrumbIcon, handleClick, hasRedirect };
    },
  } as any);
</script>
<style lang="less">
  @prefix-cls: ~'jeesite-layout-breadcrumb';

  .@{prefix-cls} {
    display: flex;
    padding: 0 8px;
    align-items: center;

    .ant-breadcrumb-overlay-link {
      .anticon {
        margin-right: 4px;
        margin-bottom: 2px;
      }
    }

    &--light .ant-breadcrumb {
      .ant-breadcrumb-overlay-link {
        color: @breadcrumb-item-normal-color;

        &:hover a {
          color: @primary-color;
        }

        a {
          color: rgb(0 0 0 / 65%);

          &:hover {
            color: @primary-color;
          }
        }
      }

      .ant-breadcrumb-separator {
        color: @breadcrumb-item-normal-color;
      }

      .ant-breadcrumb-link {
        a,
        span {
          color: rgb(0 0 0 / 65%);
        }
      }
    }

    &--dark .ant-breadcrumb {
      .ant-breadcrumb-overlay-link {
        color: rgb(255 255 255 / 60%);

        &:hover a {
          color: @white;
        }

        a {
          color: rgb(255 255 255 / 80%);

          &:hover {
            color: @white;
          }
        }
      }

      .ant-breadcrumb-separator,
      .anticon {
        color: rgb(255 255 255 / 80%);
      }

      .ant-breadcrumb-link {
        a,
        span {
          color: rgb(255 255 255 / 80%);
        }
      }
    }
  }
</style>
