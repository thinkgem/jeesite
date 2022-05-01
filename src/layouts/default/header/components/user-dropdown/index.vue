<template>
  <div v-if="sidebar" :class="`${prefixCls}-sidebar hidden lg:block think gem`">
    <span :class="[prefixCls, `${prefixCls}--${theme}`]" class="flex">
      <img :class="`${prefixCls}__header`" :src="getUserInfo.avatarUrl" />
      <span :class="`${prefixCls}__info`">
        <span :class="`${prefixCls}__name`" class="truncate">
          {{ getUserInfo.userName }}
        </span>
        <span :class="`${prefixCls}__btns`" class="block">
          <a class="online"><Icon icon="fa:circle" /> {{ t('layout.header.sidebarOnline') }}</a>
          <a class="logout" @click="handleLoginOut">
            <Icon icon="fa:sign-out" /> {{ t('layout.header.sidebarLogout') }}
          </a>
        </span>
      </span>
    </span>
  </div>
  <Dropdown v-else placement="bottomLeft" :overlayClassName="`${prefixCls}-dropdown-overlay`">
    <span :class="[prefixCls, `${prefixCls}--${theme}`]" class="flex">
      <img :class="`${prefixCls}__header`" :src="getUserInfo.avatarUrl" />
      <span :class="`${prefixCls}__info hidden md:block`">
        <span :class="`${prefixCls}__name`" class="truncate">
          {{ getUserInfo.userName }}
        </span>
      </span>
    </span>
    <template #overlay>
      <Menu @click="handleMenuClick">
        <MenuItem key="accountCenter" :text="t('sys.account.center')" icon="ion:person-outline" />
        <MenuItem key="modifyPwd" :text="t('sys.account.modifyPwd')" icon="ion:key-outline" />
        <MenuDivider />
        <MenuItem
          key="doc"
          :text="t('layout.header.dropdownItemDoc')"
          icon="ion:document-text-outline"
          v-if="getShowDoc"
        />
        <MenuDivider v-if="getShowDoc" />
        <MenuItem
          v-if="getUseLockPage"
          key="lock"
          :text="t('layout.header.tooltipLock')"
          icon="ion:lock-closed-outline"
        />
        <MenuItem
          key="logout"
          :text="t('layout.header.dropdownItemLoginOut')"
          icon="ion:power-outline"
        />
      </Menu>
    </template>
  </Dropdown>
  <LockAction v-if="!sidebar" @register="registerModal" />
</template>
<script lang="ts">
  // components
  import { Dropdown, Menu } from 'ant-design-vue';

  import { defineComponent, computed } from 'vue';

  import { DOC_URL } from '/@/settings/siteSetting';

  import { useUserStore } from '/@/store/modules/user';
  import { useHeaderSetting } from '/@/hooks/setting/useHeaderSetting';
  import { useI18n } from '/@/hooks/web/useI18n';
  import { useDesign } from '/@/hooks/web/useDesign';
  import { useModal } from '/@/components/Modal';
  import { useGo } from '/@/hooks/web/usePage';

  import { propTypes } from '/@/utils/propTypes';
  import { openWindow } from '/@/utils';

  import { createAsyncComponent } from '/@/utils/factory/createAsyncComponent';
  import Icon from '/@/components/Icon/src/Icon.vue';

  type MenuEvent = 'accountCenter' | 'modifyPwd' | 'logout' | 'doc' | 'lock';

  export default defineComponent({
    name: 'UserDropdown',
    components: {
      Dropdown,
      Menu,
      MenuItem: createAsyncComponent(() => import('./DropMenuItem.vue')),
      MenuDivider: Menu.Divider,
      LockAction: createAsyncComponent(() => import('../lock/LockModal.vue'), { loading: true }),
      Icon,
    },
    props: {
      theme: propTypes.oneOf(['dark', 'light']),
      sidebar: propTypes.bool.def(false),
    },
    setup() {
      const { prefixCls } = useDesign('header-user-dropdown');
      const { t } = useI18n();
      const { getShowDoc, getUseLockPage } = useHeaderSetting();
      const userStore = useUserStore();
      const go = useGo();

      const getUserInfo = computed(() => {
        const { userName = '', avatarUrl, remarks } = userStore.getUserInfo || {};
        return { userName, avatarUrl, remarks };
      });

      const [registerModal, { openModal }] = useModal();

      function handleLoginOut() {
        userStore.confirmLoginOut();
      }

      function handleAccountCenter() {
        go('/account/center');
      }

      function handleModifyPwd() {
        go('/account/modPwd');
      }

      function handleOpenDoc() {
        openWindow(DOC_URL);
      }

      function handleLock() {
        openModal(true);
      }

      function handleMenuClick(e: { key: MenuEvent }) {
        switch (e.key) {
          case 'accountCenter':
            handleAccountCenter();
            break;
          case 'modifyPwd':
            handleModifyPwd();
            break;
          case 'logout':
            handleLoginOut();
            break;
          case 'doc':
            handleOpenDoc();
            break;
          case 'lock':
            handleLock();
            break;
        }
      }

      return {
        prefixCls,
        t,
        getUserInfo,
        handleMenuClick,
        getShowDoc,
        registerModal,
        getUseLockPage,
        handleLoginOut,
      };
    },
  });
</script>
<style lang="less">
  @prefix-cls: ~'jeesite-header-user-dropdown';
  @menu-dark-subsidiary-color: rgba(255, 255, 255, 0.7);

  .@{prefix-cls} {
    height: @header-height;
    padding: 0 0 0 10px;
    padding-right: 10px;
    overflow: hidden;
    font-size: 12px;
    cursor: pointer;
    align-items: center;

    img {
      width: 24px;
      height: 24px;
      margin-right: 10px;
      background: #eee;
    }

    &__header {
      border-radius: 50%;
    }

    &__name {
      font-size: 14px;
    }

    &--dark {
      &:hover {
        background-color: @header-dark-bg-hover-color;
      }
    }

    &--light {
      &:hover {
        background-color: @header-light-bg-hover-color;
      }

      .@{prefix-cls}__name {
        color: @text-color-base;
      }

      .@{prefix-cls}__desc {
        color: @header-light-desc-color;
      }
    }

    &-dropdown-overlay {
      .ant-dropdown-menu-item {
        min-width: 160px;
      }
    }

    &-sidebar {
      .@{prefix-cls} {
        height: auto;
        cursor: default;
        padding: 8px 10px 10px 10px;

        img {
          width: 45px;
          height: 45px;
          transition: all 0.1s;
        }

        &__name {
          font-weight: bold;
        }

        &__btns {
          padding-top: 3px;
          font-size: 10px;
          white-space: nowrap;

          .anticon {
            padding-right: 2px;
          }

          .online {
            padding-right: 9px;

            .anticon {
              color: #3c763d;
            }
          }

          .logout {
            .anticon {
              color: #a94442;
            }
          }
        }

        &--dark {
          color: @menu-dark-subsidiary-color;

          a {
            color: @menu-dark-subsidiary-color;
          }

          &:hover {
            background-color: transparent;
          }
        }

        &--light {
          color: @text-color-base;

          a {
            color: @text-color-base;
          }

          &:hover {
            background-color: transparent;
          }
        }
      }
    }
  }

  .ant-layout-sider-collapsed {
    .@{prefix-cls} {
      padding: 10px 0;
      justify-content: center;

      img {
        width: 25px;
        height: 25px;
        margin: 0;
      }

      &__info {
        display: none;
      }
    }
  }
</style>
