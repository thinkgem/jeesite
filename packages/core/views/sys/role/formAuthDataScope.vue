<!--
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 * No deletion without permission, or be held responsible to law.
 * @author ThinkGem
-->
<template>
  <BasicDrawer
    wrapClassName="jeesite-role-auth-data-scope"
    v-bind="$attrs"
    :showFooter="true"
    :okAuth="'sys:role:edit'"
    @register="registerDrawer"
    @ok="handleSubmit"
    width="90%"
  >
    <template #title>
      <Icon :icon="getTitle.icon" class="m-1 pr-1" />
      <span> {{ getTitle.value }} </span>
    </template>
    <Tabs class="jeesite-role-auth-data-scope-tabs" v-model:activeKey="activeKey">
      <Tabs.TabPane key="1" :forceRender="true">
        <template #tab>
          <Icon icon="i-ant-design:deployment-unit-outlined" />
          <span class="pr-1"> {{ t('角色数据权限') }} </span>
        </template>
        <RoleDataScope ref="roleDataScopeRef" />
      </Tabs.TabPane>
      <Tabs.TabPane key="2" :forceRender="true">
        <template #tab>
          <Icon icon="i-ant-design:unordered-list-outlined" />
          <span class="pr-1"> {{ t('菜单数据权限') }} </span>
        </template>
        <MenuDataScope ref="menuDataScopeRef" />
      </Tabs.TabPane>
    </Tabs>
  </BasicDrawer>
</template>
<script lang="ts" setup name="ViewsSysRoleAuthDataScope">
  import { computed, ref, unref } from 'vue';
  import { Tabs } from 'ant-design-vue';
  import { useI18n } from '@jeesite/core/hooks/web/useI18n';
  import { useMessage } from '@jeesite/core/hooks/web/useMessage';
  import { router } from '@jeesite/core/router';
  import { Icon } from '@jeesite/core/components/Icon';
  import { BasicDrawer, useDrawerInner } from '@jeesite/core/components/Drawer';
  import { roleFormAuthDataScope, roleSaveAuthDataScope } from '@jeesite/core/api/sys/role';
  import { useDict } from '@jeesite/core/components/Dict';
  import RoleDataScope from './components/RoleDataScope.vue';
  import MenuDataScope from './components/MenuDataScope.vue';
  import { omit } from 'lodash-es';

  const emit = defineEmits(['success', 'register']);

  const { t } = useI18n('sys.role');
  const { showMessage } = useMessage();
  const { meta } = unref(router.currentRoute);
  const record = ref<Recordable>({});
  const getTitle = computed(() => {
    const r = record.value;
    const { getDictLabel } = useDict();
    return {
      icon: meta.icon || 'ant-design:book-outlined',
      value:
        t('角色分配数据权限') +
        ' (' +
        r.roleName +
        '-' +
        r.viewCode +
        '-' +
        getDictLabel('sys_user_type', r.userType, t('未设置')) +
        ')',
    };
  });

  const activeKey = ref<string>('1');
  const roleDataScopeRef = ref<InstanceType<typeof RoleDataScope>>();
  const menuDataScopeRef = ref<InstanceType<typeof MenuDataScope>>();

  const [registerDrawer, { setDrawerProps, closeDrawer }] = useDrawerInner(async (data) => {
    setDrawerProps({ loading: true });
    activeKey.value = '1';
    data.menuCode = 'true'; // 查询菜单数据权限
    const res = await roleFormAuthDataScope(data);
    record.value = (res.role || {}) as Recordable;
    roleDataScopeRef.value?.loadDataScopeFormData(record.value, res);
    menuDataScopeRef.value?.loadDataScopeFormData(res);
    setDrawerProps({ loading: false });
  });

  async function handleSubmit() {
    try {
      setDrawerProps({ confirmLoading: true });
      const roleDataScope = await roleDataScopeRef.value?.getDataScopeFormData();
      const menuDataScope = await menuDataScopeRef.value?.getDataScopeFormData();
      const params: any = {
        ...omit(roleDataScope, ['roleDataScopeList']),
        roleDataScopeListJson: JSON.stringify(roleDataScope.roleDataScopeList.concat(menuDataScope.roleDataScopeList)),
        menuDataScopeListJson: JSON.stringify(menuDataScope.menuDataScopeList),
        isNewRecord: record.value.isNewRecord,
        roleCode: record.value.roleCode,
        menuCode: 'true', // 存储菜单数据权限
      };
      // console.log('submit', params, data, record);
      const res = await roleSaveAuthDataScope(params);
      showMessage(res.message);
      setTimeout(closeDrawer);
      emit('success', params);
    } catch (error: any) {
      if (error && error.errorFields) {
        showMessage(error.message || t('common.validateError'));
      }
      console.log('error', error);
    } finally {
      setDrawerProps({ confirmLoading: false });
    }
  }
</script>
<style lang="less">
  .jeesite-role-auth-data-scope {
    .scroll-container {
      > .scrollbar__wrap {
        > .scrollbar__view {
          margin: 6px !important;
        }
      }
    }

    &-tabs.ant-tabs {
      margin-top: 0 !important;
      padding-right: 5px;

      .ant-tabs-nav {
        margin-left: 20px;
      }

      .ant-tabs-tab {
        padding: 7px 0;
      }
    }
  }
</style>
