<!--
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 * No deletion without permission, or be held responsible to law.
 * @author ThinkGem
-->
<template>
  <ARow class="h-full pl-4">
    <ACol :span="9" :style="getTreeStyle">
      <BasicTree
        ref="treeRef"
        :search="true"
        :toolbar="true"
        :showIcon="true"
        :api="menuTreeDataByRoleCode"
        :params="apiParams"
        :immediate="false"
        :defaultExpandLevel="1"
        @select="handleSelect"
      >
        <template #headerTitle>
          <Dropdown class="cursor-pointer" :trigger="['hover']" :dropMenuList="dropMenuList">
            {{ sysName }} <DownOutlined />
          </Dropdown>
        </template>
        <template #icon="{ dataRef, isLeaf, expanded }">
          <Icon
            v-if="dataRef.hasDataScope"
            icon="i-ant-design:unordered-list-outlined"
            color="#f00"
          />
          <Icon v-else-if="isLeaf" icon="ant-design:file-outlined" />
          <Icon v-else-if="expanded" icon="i-ant-design:folder-open-outlined" />
          <Icon v-else icon="ant-design:folder-outlined" />
        </template>
      </BasicTree>
    </ACol>
    <ACol :span="15" :style="getMainStyle">
      <Tabs
        class="jeesite-role-auth-data-scope-tabs"
        v-model:activeKey="ruleType"
        @change="handleTabChange"
      >
        <Tabs.TabPane key="1" :forceRender="true">
          <template #tab>
            <Icon v-if="ruleType == '1'" icon="i-ant-design:check-circle-outlined" />
            <Icon v-else icon="i-ant-design:minus-circle-outlined" />
            <span class="pr-1"> {{ t('通用数据权限') }} </span>
          </template>
          <RoleDataScope ref="roleDataScopeRef" :menuDataScope="true" />
        </Tabs.TabPane>
        <Tabs.TabPane key="2" :forceRender="true">
          <template #tab>
            <Icon v-if="ruleType == '2'" icon="i-ant-design:check-circle-outlined" />
            <Icon v-else icon="i-ant-design:minus-circle-outlined" />
            <span class="pr-1"> {{ t('自定义条件规则') }} </span>
          </template>
          <RuleDataScope ref="ruleDataScopeRef" />
        </Tabs.TabPane>
        <Tabs.TabPane key="3" :forceRender="true">
          <template #tab>
            <Icon v-if="ruleType == '3'" icon="i-ant-design:check-circle-outlined" />
            <Icon v-else icon="i-ant-design:minus-circle-outlined" />
            <span class="pr-1"> {{ t('自定义SQL片段') }} </span>
          </template>
          <SqlDataScope ref="sqlDataScopeRef" />
        </Tabs.TabPane>
      </Tabs>
      <div class="ml-4 mt-2">
        <Alert
          v-if="!ruleType || ruleType === '0' || menuCode == '0'"
          message="提示：请先选择菜单，然后再选择权限类型。"
          type="info"
        />
      </div>
    </ACol>
  </ARow>
</template>
<script lang="ts" setup name="ViewsSysMenuIndex">
  import { ref, computed, CSSProperties } from 'vue';
  import { Alert, Col, Row, Tabs } from 'ant-design-vue';
  import { DownOutlined } from '@ant-design/icons-vue';
  import { useI18n } from '@jeesite/core/hooks/web/useI18n';
  import { useDict } from '@jeesite/core/components/Dict';
  import { Icon } from '@jeesite/core/components/Icon';
  import { Dropdown, DropMenu } from '@jeesite/core/components/Dropdown';
  import { BasicTree, TreeActionType } from '@jeesite/core/components/Tree';
  import { menuTreeDataByRoleCode } from '@jeesite/core/api/sys/role';
  import { useWindowSizeFn } from '@jeesite/core/hooks/event/useWindowSizeFn';
  import { onMountedOrActivated } from '@jeesite/core/hooks/core/onMountedOrActivated';
  import { encryptByBase64 } from '@jeesite/core/utils/cipher';
  import RoleDataScope from './RoleDataScope.vue';
  import RuleDataScope from './RuleDataScope.vue';
  import SqlDataScope from './SqlDataScope.vue';

  const ARow = Row;
  const ACol = Col;

  const { t } = useI18n('sys.role');
  const record = ref<Recordable>({});
  const menuDataScopes = ref<any>({});

  const treeRef = ref<Nullable<TreeActionType>>(null);
  const apiParams = ref<Recordable>({ roleCode: '', sysCode: 'default' });

  const dropMenuList = ref<Array<DropMenu>>([]);
  const sysCode = ref<string>('default');
  const sysName = ref<string>(t('菜单'));

  const menuCode = ref<string>('0');
  const ruleType = ref<string>('0');
  const roleDataScopeRef = ref<InstanceType<typeof RoleDataScope>>();
  const ruleDataScopeRef = ref<InstanceType<typeof RuleDataScope>>();
  const sqlDataScopeRef = ref<InstanceType<typeof SqlDataScope>>();

  const contentHeight = ref(400);
  const getTreeStyle = computed((): CSSProperties => {
    const treeHeight = contentHeight.value - 220;
    return {
      height: `${treeHeight}px`,
      minHeight: `${treeHeight}px`,
    };
  });
  const getMainStyle = computed((): CSSProperties => {
    const mainHeight = contentHeight.value - 220 + 30;
    return {
      height: `${mainHeight}px`,
      minHeight: `${mainHeight}px`,
      overflowX: 'auto',
    };
  });
  function calcTreeHeight() {
    contentHeight.value = document.documentElement.clientHeight;
  }
  useWindowSizeFn(calcTreeHeight, 280);
  onMountedOrActivated(calcTreeHeight);

  async function loadDataScopeFormData(res: Recordable) {
    // 初始化变量值
    const { roleCode, roleName } = (res.role || {}) as Recordable;
    record.value = { roleCode, roleName, dataScope: '0' };
    sysCode.value = 'default';
    menuCode.value = '0';
    ruleType.value = '0';
    // 加载菜单树
    apiParams.value = { roleCode: record.value.roleCode, sysCode: sysCode.value };
    dropMenuList.value = (await useDict().initGetDictList('sys_menu_sys_code')).map((item) => {
      if (item.value == sysCode.value) {
        sysName.value = item.name;
      }
      return {
        text: item.name,
        event: item.value,
        icon: 'i-radix-icons:dot',
        onClick: () => {
          sysCode.value = item.value;
          sysName.value = item.name;
          apiParams.value.sysCode = item.value;
          treeRef.value?.reload();
        },
      };
    });
    treeRef.value?.reload();
    treeRef.value?.setSelectedKeys([]);
    // 读取存储的数据
    menuDataScopes.value = {};
    for (const menuDataScope of res.menuDataScopeList) {
      menuDataScopes.value[menuDataScope.menuCode] = {
        menuCode: menuDataScope.menuCode,
        roleCode: menuDataScope.roleCode,
        ruleName: menuDataScope.ruleName,
        ruleType: menuDataScope.ruleType,
        ...menuDataScope.ruleConfigMap,
        roleDataScopeList: [],
      };
    }
    for (const roleDataScope of res.roleDataScopeList) {
      if (roleDataScope.menuCode && roleDataScope.menuCode !== '0') {
        const mds = menuDataScopes.value[roleDataScope.menuCode] || {};
        if (mds.roleDataScopeList) {
          mds.roleDataScopeList.push(roleDataScope);
        }
      }
    }
    // 加载数据权限表单
    roleDataScopeRef.value?.loadDataScopeFormData(record.value, res);
    ruleDataScopeRef.value?.loadDataScopeFormData(record.value, res);
    sqlDataScopeRef.value?.loadDataScopeFormData(record.value, res);
  }

  async function handleSelect(_keys?: string[], obj?: any) {
    // 存储上一个菜单数据权限
    if (ruleType.value === '1') {
      const formData = await roleDataScopeRef.value?.getDataScopeFormData();
      if (formData.menuCode) {
        formData.ruleType = ruleType.value;
        menuDataScopes.value[formData.menuCode] = formData;
        treeRef.value?.updateNodeByKey(formData.menuCode, {
          hasDataScope: formData.dataScope && formData.dataScope !== '0',
        });
      }
    } else if (ruleType.value === '2') {
      const formData = await ruleDataScopeRef.value?.getDataScopeFormData();
      if (formData.menuCode) {
        formData.ruleType = ruleType.value;
        menuDataScopes.value[formData.menuCode] = formData;
        treeRef.value?.updateNodeByKey(formData.menuCode, {
          hasDataScope: formData.ruleList && formData.ruleList.length > 0,
        });
      }
    } else if (ruleType.value === '3') {
      const formData = await sqlDataScopeRef.value?.getDataScopeFormData();
      if (formData.menuCode) {
        formData.ruleType = ruleType.value;
        menuDataScopes.value[formData.menuCode] = formData;
        treeRef.value?.updateNodeByKey(formData.menuCode, {
          hasDataScope: formData.sqlWhere && formData.sqlWhere !== '',
        });
      }
    }
    // 加载当前选择的菜单数据权限
    if (obj && obj.node && obj.node.dataRef) {
      const { id, rawName, permission } = obj.node.dataRef;
      menuCode.value = id;
      menuDataScopes.value[id] = {
        ...(menuDataScopes.value[id] || record.value),
        menuCode: id,
        menuName: rawName,
        permission: permission,
      };
      const menuDataScope = menuDataScopes.value[id];
      ruleType.value = menuDataScope.ruleType || '1';
      roleDataScopeRef.value?.loadDataScopeFormData(menuDataScope);
      ruleDataScopeRef.value?.loadDataScopeFormData(menuDataScope);
      sqlDataScopeRef.value?.loadDataScopeFormData(menuDataScope);
    }
  }

  function handleTabChange() {
    if (menuCode.value == '0') {
      ruleType.value = '0';
    }
    // 更新菜单图标状态
    if (ruleType.value === '1') {
      const formData = menuDataScopes.value[menuCode.value] as Recordable;
      if (formData && formData.menuCode) {
        treeRef.value?.updateNodeByKey(formData.menuCode, {
          hasDataScope: formData.dataScope && formData.dataScope !== '0',
        });
      }
    } else if (ruleType.value === '2') {
      const formData = menuDataScopes.value[menuCode.value] as Recordable;
      if (formData && formData.menuCode) {
        treeRef.value?.updateNodeByKey(formData.menuCode, {
          hasDataScope: formData.ruleList && formData.ruleList.length > 0,
        });
      }
    } else if (ruleType.value === '3') {
      const formData = menuDataScopes.value[menuCode.value] as Recordable;
      if (formData && formData.menuCode) {
        treeRef.value?.updateNodeByKey(formData.menuCode, {
          hasDataScope: formData.sqlWhere && formData.sqlWhere !== '',
        });
      }
    }
  }

  async function getDataScopeFormData() {
    await handleSelect(); // 获取最后一个设置
    let menuDataScopeList: any = [];
    let roleDataScopeList: any = [];
    for (const key in menuDataScopes.value) {
      const menuDataScope = menuDataScopes.value[key];
      // 1 角色数据范围 2自定义条件规则 3自定义SQL
      if (menuDataScope.ruleType === '1') {
        if (menuDataScope.dataScope && menuDataScope.dataScope !== '0') {
          menuDataScopeList.push({
            menuCode: menuDataScope.menuCode,
            ruleName: record.value.roleName,
            ruleType: menuDataScope.ruleType,
            ruleConfigMap: {
              dataScope: menuDataScope.dataScope,
            },
          });
          if (menuDataScope.dataScope === '2') {
            roleDataScopeList = roleDataScopeList.concat(menuDataScope.roleDataScopeList);
          }
        }
      } else if (menuDataScope.ruleType === '2') {
        if (menuDataScope.ruleList && menuDataScope.ruleList.length > 0) {
          menuDataScopeList.push({
            menuCode: menuDataScope.menuCode,
            ruleName: record.value.roleName,
            ruleType: menuDataScope.ruleType,
            ruleConfigMap: {
              ruleList: menuDataScope.ruleList,
            },
          });
        }
      } else if (menuDataScope.ruleType === '3') {
        if (menuDataScope.sqlWhere && menuDataScope.sqlWhere !== '') {
          menuDataScopeList.push({
            menuCode: menuDataScope.menuCode,
            ruleName: record.value.roleName,
            ruleType: menuDataScope.ruleType,
            ruleConfigMap: {
              sqlWhere: encryptByBase64(menuDataScope.sqlWhere),
            },
          });
        }
      }
    }
    return {
      menuDataScopeList,
      roleDataScopeList,
    } as any;
  }

  defineExpose({
    loadDataScopeFormData,
    getDataScopeFormData,
  });
</script>
