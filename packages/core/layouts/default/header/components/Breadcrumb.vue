<template>
  <div :class="['jeesite-layout-breadcrumb', `jeesite-layout-breadcrumb--${theme}`]">
    <Breadcrumb :items="itemsRef">
      <template #itemRender="{ route }">
        <Icon class="mr-1" :icon="getIcon(route)" v-if="getShowBreadCrumbIcon && getIcon(route)" />
        <span class="ant-breadcrumb-overlay-link cursor-pointer">{{ t(route['name'] || route['meta']?.title) }}</span>
      </template>
    </Breadcrumb>
  </div>
</template>
<script lang="ts" setup name="LayoutBreadcrumb">
  import type { RouteLocationMatched } from 'vue-router';
  import { useRouter } from 'vue-router';
  import type { Menu } from '@jeesite/core/router/types';

  import { ref, watchEffect } from 'vue';

  import { Breadcrumb } from 'antdv-next';
  import { Icon } from '@jeesite/core/components/Icon';

  import { useRootSetting } from '@jeesite/core/hooks/setting/useRootSetting';
  import { useGo } from '@jeesite/core/hooks/web/usePage';
  import { useI18n } from '@jeesite/core/hooks/web/useI18n';

  import { propTypes } from '@jeesite/core/utils/propTypes';
  import { getMenus } from '@jeesite/core/router/menus';

  import { REDIRECT_NAME } from '@jeesite/core/router/constant';
  import { getAllParentPath } from '@jeesite/core/router/helper/menuHelper';

  const props = defineProps({
    theme: propTypes.oneOf(['dark', 'light']),
  });

  const itemsRef = ref<any[]>([]);
  const { currentRoute } = useRouter();
  const { getShowBreadCrumbIcon } = useRootSetting();
  const { t } = useI18n();
  const go = useGo();

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
    const matched = getItems(filterMenus, parent) as any;

    if (!matched || matched.length === 0) return;

    const breadcrumbList = matched;

    if (currentRoute.value.meta?.currentActiveMenu) {
      breadcrumbList.push({
        ...currentRoute.value,
        name: currentRoute.value.meta?.title || currentRoute.value.name,
      } as unknown as RouteLocationMatched);
    }
    itemsRef.value = breadcrumbList;
  });

  function getItems(menus: Menu[], parent: string[]) {
    const items: Menu[] = [];
    menus.forEach((item) => {
      const { meta } = item;
      if (!meta) {
        return;
      }
      const { title, hideBreadcrumb, hideMenu } = meta;
      if (!title || hideBreadcrumb || hideMenu) {
        return;
      }
      if (!(!item.meta?.hideBreadcrumb || !item.meta?.hideMenu)) {
        return;
      }
      const subItems: any[] = [];
      const children = item.children;
      if (children && children.length) {
        children.forEach((subItem: any) => {
          subItems.push({
            title: subItem.title,
            key: subItem.path,
          });
        });
      }
      if (parent.includes(item.path)) {
        const newItem: any = {
          ...item,
          name: item.meta?.title || item.name,
        };
        if (subItems.length > 0) {
          newItem.menu = { items: subItems, onClick: handleMenuClick };
        }
        items.push(newItem);
      }
      if (item.children?.length) {
        items.push(...getItems(item.children, parent));
      }
    });
    return items;
  }

  function handleMenuClick(item: any) {
    let goPath = item.key;
    goPath = /^\//.test(goPath) ? goPath : `/${goPath}`;
    go(goPath);
  }

  function getIcon(route: any) {
    return route.icon || route.meta?.icon;
  }
</script>
<style lang="less">
  .jeesite-layout-breadcrumb {
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
