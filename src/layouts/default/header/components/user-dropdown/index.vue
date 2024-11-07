<template>
  <div v-if="props.sidebar" :class="`${prefixCls}-sidebar md:hidden lg:block think gem`">
    <span :class="[prefixCls, `${prefixCls}--${props.theme}`]" class="flex">
      <img :class="`${prefixCls}__header`" :src="getUserInfo.avatarUrl" />
      <span :class="`${prefixCls}__info`">
        <span :class="`${prefixCls}__name`" class="truncate">
          {{ getUserInfo.userName }}
        </span>
        <span :class="`${prefixCls}__btns`" class="block">
          <a class="online"><Icon icon="i-fa:circle" /> {{ t('layout.header.sidebarOnline') }}</a>
          <a class="logout" @click="handleLoginOut">
            <Icon icon="i-fa:sign-out" /> {{ t('layout.header.sidebarLogout') }}
          </a>
        </span>
      </span>
    </span>
  </div>
  <Dropdown v-else placement="bottom" :overlayClassName="`${prefixCls}-dropdown-overlay`">
    <span :class="[prefixCls, `${prefixCls}--${props.theme}`]" class="flex">
      <img :class="`${prefixCls}__header`" :src="getUserInfo.avatarUrl" />
      <span :class="`${prefixCls}__info hidden md:block`">
        <span :class="`${prefixCls}__name`" class="truncate">
          {{ getUserInfo.userName }}
        </span>
      </span>
    </span>
    <template #overlay>
      <Menu @click="handleMenuClick">
        <MenuItem
          value="accountCenter"
          :text="t('sys.account.center')"
          icon="i-ion:person-outline"
        />
        <MenuItem value="modifyPwd" :text="t('sys.account.modifyPwd')" icon="i-ion:key-outline" />
        <MenuDivider />
        <MenuItem
          value="doc"
          :text="t('layout.header.dropdownItemDoc')"
          icon="i-ion:document-text-outline"
          v-if="getShowDoc"
        />
        <MenuDivider v-if="getShowDoc" />
        <MenuItem
          v-if="getUseLockPage"
          value="lock"
          :text="t('layout.header.tooltipLock')"
          icon="i-ion:lock-closed-outline"
        />
        <MenuItem
          value="logout"
          :text="t('layout.header.dropdownItemLoginOut')"
          icon="i-ion:power-outline"
        />
        <MenuDivider v-if="sysListRef.length > 0" />
        <MenuItem
          v-if="sysListRef.length > 0"
          :class="`${prefixCls}-menu-subtitle`"
          :text="t('系统切换：')"
        />
        <MenuItem
          v-for="item in sysListRef"
          :key="item.value"
          :value="'sysCode-' + item.value"
          :text="item.name"
          :icon="sysCodeRef == item.value ? 'i-ant-design:check-outlined' : 'i-radix-icons:dot'"
        />
        <template v-if="getUserInfo.postList.length > 0">
          <MenuDivider />
          <MenuItem :class="`${prefixCls}-menu-subtitle`" :text="t('选择岗位：')">
            <template #menuItemAfter>
              <Icon
                v-if="postCodeRef"
                icon="i-ant-design:close-circle-outlined"
                class="ml-1"
                @click="handleMenuClick({ key: 'postCode-' })"
                :title="t('取消设置')"
              />
            </template>
          </MenuItem>
          <MenuItem
            v-for="item in getUserInfo.postList"
            :key="item.postCode"
            :value="'postCode-' + item.postCode"
            :text="item.postName"
            :icon="
              postCodeRef == item.postCode ? 'i-ant-design:check-outlined' : 'i-radix-icons:dot'
            "
          />
        </template>
        <template v-else-if="getUserInfo.roleList.length > 0">
          <MenuDivider />
          <MenuItem :class="`${prefixCls}-menu-subtitle`" :text="t('选择身份：')">
            <template #menuItemAfter>
              <Icon
                v-if="roleCodeRef"
                icon="i-ant-design:close-circle-outlined"
                class="ml-1"
                @click="handleMenuClick({ key: 'roleCode-' })"
                :title="t('取消设置')"
              />
            </template>
          </MenuItem>
          <MenuItem
            v-for="item in getUserInfo.roleList"
            :key="item.roleCode"
            :value="'roleCode-' + item.roleCode"
            :text="item.roleName"
            :icon="
              roleCodeRef == item.roleCode ? 'i-ant-design:check-outlined' : 'i-radix-icons:dot'
            "
          />
        </template>
      </Menu>
    </template>
  </Dropdown>
  <LockAction v-if="!props.sidebar" @register="registerModal" />
</template>
<script lang="ts">
  import { defineComponent, computed, ref, onMounted } from 'vue';
  import { Dropdown, Menu } from 'ant-design-vue';
  import { DOC_URL } from '/@/settings/siteSetting';
  import { useUserStore } from '/@/store/modules/user';
  import { useHeaderSetting } from '/@/hooks/setting/useHeaderSetting';
  import { useI18n } from '/@/hooks/web/useI18n';
  import { useDesign } from '/@/hooks/web/useDesign';
  import { useModal } from '/@/components/Modal';
  import { useGo } from '/@/hooks/web/usePage';
  import { propTypes } from '/@/utils/propTypes';
  import { openWindow } from '/@/utils';
  import { useDict } from '/@/components/Dict';
  import { switchSys, switchRole, switchPost } from '/@/api/sys/login';
  import { PageEnum } from '/@/enums/pageEnum';
  import { Icon } from '/@/components/Icon';
  import MenuItem from './DropMenuItem.vue';
  import LockAction from '../lock/LockModal.vue';

  type MenuEvent = 'accountCenter' | 'modifyPwd' | 'logout' | 'doc' | 'lock' | 'roleCode-';

  const props = {
    theme: propTypes.oneOf(['dark', 'light']),
    sidebar: propTypes.bool.def(false),
  };

  export default defineComponent({
    name: 'UserDropdown',
    components: {
      Dropdown,
      Menu,
      MenuItem,
      MenuDivider: Menu.Divider,
      LockAction,
      Icon,
    },
    props,
    setup(props: any) {
      const { prefixCls } = useDesign('header-user-dropdown');
      const { t } = useI18n();
      const { getShowDoc, getUseLockPage } = useHeaderSetting();
      const userStore = useUserStore();
      const go = useGo();

      const sysCodeRef = ref<string>('default');
      const sysListRef = ref<Recordable[]>([]);
      const roleCodeRef = ref<string>('');
      const postCodeRef = ref<string>('');

      const getUserInfo = computed(() => {
        const {
          userName = '',
          avatarUrl,
          remarks,
          roleList,
          postList,
        } = userStore.getUserInfo || {};
        return {
          userName,
          avatarUrl,
          remarks,
          roleList: (roleList || []).filter((e) => e.isShow == '1'),
          postList: postList || [],
        };
      });

      if (!props.sidebar) {
        onMounted(async () => {
          sysCodeRef.value = userStore.getPageCacheByKey('sysCode', 'default');
          roleCodeRef.value = userStore.getPageCacheByKey('roleCode', '');
          postCodeRef.value = userStore.getPageCacheByKey('postCode', '');
          const sysList = await useDict().initGetDictList('sys_menu_sys_code');
          if (sysList.length > 1) {
            var sysCodes: string[] = [];
            for (let role of getUserInfo.value.roleList) {
              if (role.sysCodes) {
                for (let code of role.sysCodes.split(',')) {
                  if (code != '') sysCodes.push(code);
                }
              }
            }
            sysListRef.value =
              sysCodes.length === 0 ? sysList : sysList.filter((e) => sysCodes.includes(e.value));
          }
        });
      }

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

      async function handleMenuClick(e: { key: MenuEvent } | any) {
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
          default:
            const sysCodePrefix = 'sysCode-';
            if (String(e.key).startsWith(sysCodePrefix)) {
              go(userStore.getUserInfo.homePath || PageEnum.BASE_HOME);
              const sysCode = String(e.key).substring(sysCodePrefix.length);
              await switchSys(sysCode);
              location.reload();
            }
            const roleCodePrefix = 'roleCode-';
            if (String(e.key).startsWith(roleCodePrefix)) {
              go(userStore.getUserInfo.homePath || PageEnum.BASE_HOME);
              const roleCode = String(e.key).substring(roleCodePrefix.length);
              await switchRole(roleCode);
              location.reload();
            }
            const postCodePrefix = 'postCode-';
            if (String(e.key).startsWith(postCodePrefix)) {
              go(userStore.getUserInfo.homePath || PageEnum.BASE_HOME);
              const postCode = String(e.key).substring(postCodePrefix.length);
              await switchPost(postCode);
              location.reload();
            }
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
        sysCodeRef,
        sysListRef,
        roleCodeRef,
        postCodeRef,
        props,
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

      .@{prefix-cls}__info {
        color: @menu-dark-subsidiary-color;
      }

      .@{prefix-cls}__desc {
        color: @menu-dark-subsidiary-color;
      }
    }

    &--light {
      &:hover {
        background-color: @header-light-bg-hover-color;
      }

      .@{prefix-cls}__info {
        color: @text-color-base;
      }

      .@{prefix-cls}__desc {
        color: @header-light-desc-color;
      }
    }

    &-dropdown-overlay {
      .ant-dropdown-menu-item {
        min-width: 115px;
      }
    }

    &-menu-subtitle {
      line-height: 13px;

      span {
        font-weight: bold;
        opacity: 0.7;

        svg {
          padding-top: 3px;
        }
      }
    }

    &-sidebar {
      .@{prefix-cls} {
        height: auto;
        cursor: default;
        padding: 8px 10px 10px;

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
          font-size: 11px;
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
