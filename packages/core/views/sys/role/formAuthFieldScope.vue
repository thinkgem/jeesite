<!--
 * Copyright (c) 2013-Now https://jeesite.com All rights reserved.
 * No deletion without permission, or be held responsible to law.
 * @author ThinkGem
-->
<template>
  <BasicDrawer
    wrapClassName="jeesite-role-auth-field-scope"
    v-bind="$attrs"
    :showFooter="true"
    :showOkBtn="false"
    @register="registerDrawer"
    @close="handleClose"
    width="90%"
  >
    <template #title>
      <Icon :icon="getTitle.icon" class="m-1 pr-1" />
      <span> {{ getTitle.value }} </span>
    </template>
    <template #centerFooter>
      <a-button type="primary" @click="handleForm" v-auth="'sys:role:edit'">
        <Icon icon="i-fluent:add-12-filled" /> {{ t('添加权限') }}
      </a-button>
    </template>
    <Tabs class="jeesite-role-auth-field-scope-tabs" v-model:activeKey="activeKey">
      <Tabs.TabPane key="1" :forceRender="true">
        <template #tab>
          <Icon icon="i-ant-design:deployment-unit-outlined" />
          <span class="pr-1"> {{ t('角色字段权限') }} </span>
        </template>
        <RoleFieldScopeList ref="roleFieldScopeListRef" />
      </Tabs.TabPane>
      <Tabs.TabPane key="2" :forceRender="true">
        <template #tab>
          <Icon icon="i-ant-design:unordered-list-outlined" />
          <span class="pr-1"> {{ t('菜单字段权限') }} </span>
        </template>
        <MenuFieldScope ref="menuFieldScopeRef" />
      </Tabs.TabPane>
    </Tabs>
  </BasicDrawer>
</template>
<script lang="ts" setup name="ViewsSysRoleAuthFieldScope">
  import { computed, ref, unref } from 'vue';
  import { Tabs } from 'ant-design-vue';
  import { useI18n } from '@jeesite/core/hooks/web/useI18n';
  import { useMessage } from '@jeesite/core/hooks/web/useMessage';
  import { router } from '@jeesite/core/router';
  import { Icon } from '@jeesite/core/components/Icon';
  import { BasicDrawer, useDrawer, useDrawerInner } from '@jeesite/core/components/Drawer';
  import { useDict } from '@jeesite/core/components/Dict';
  import RoleFieldScopeList from './components/RoleFieldScopeList.vue';
  import MenuFieldScope from './components/MenuFieldScope.vue';
  import { roleForm } from '@jeesite/core/api/sys';

  const emit = defineEmits(['success', 'register']);

  const { t } = useI18n('sys.role');
  const { showMessage } = useMessage();
  const { meta } = unref(router.currentRoute);
  const record = ref<Recordable>({});
  const getTitle = computed(() => {
    const r = record.value;
    const { getDictLabel } = useDict();
    const type = getDictLabel('sys_user_type', r.userType, t('未设置'));
    return {
      icon: meta.icon || 'ant-design:book-outlined',
      value: t('角色分配字段权限') + ' (' + r.roleName + '-' + r.viewCode + '-' + type + ')',
    };
  });

  const activeKey = ref<string>('1');
  const roleFieldScopeListRef = ref<InstanceType<typeof RoleFieldScopeList>>();
  const menuFieldScopeRef = ref<InstanceType<typeof MenuFieldScope>>();

  const [registerDrawer, { setDrawerProps, closeDrawer }] = useDrawerInner(async (data) => {
    setDrawerProps({ loading: true });
    activeKey.value = '1';
    data.menuCode = 'true'; // 查询菜单字段权限
    const res = await roleForm(data);
    record.value = (res.role || {}) as Recordable;
    const roleFieldData = { roleCode: record.value.roleCode, menuCode: '0' };
    roleFieldScopeListRef.value?.loadFieldScopeFormData(roleFieldData);
    menuFieldScopeRef.value?.loadFieldScopeFormData(roleFieldData);
    setDrawerProps({ loading: false });
  });

  function handleForm() {
    const roleFieldData = { roleCode: record.value.roleCode, menuCode: '0' };
    if (activeKey.value == '1') {
      roleFieldScopeListRef.value?.handleForm(roleFieldData);
    } else {
      menuFieldScopeRef.value?.handleForm(roleFieldData);
    }
  }

  async function handleClose() {
    closeDrawer();
  }
</script>
<style lang="less">
  .jeesite-role-auth-field-scope {
    .scroll-container {
      > .scrollbar__wrap {
        > .scrollbar__view {
          margin: 6px !important;
        }
      }
    }

    &-tabs.ant-tabs {
      margin-top: 0 !important;
      margin-right: 15px !important;

      .ant-tabs-nav {
        margin-left: 20px;
      }

      .ant-tabs-tab {
        padding: 7px 0;
      }
    }
  }
</style>
