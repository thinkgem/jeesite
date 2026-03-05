<!--
 * Copyright (c) 2013-Now https://jeesite.com All rights reserved.
 * No deletion without permission, or be held responsible to law.
 * @author ThinkGem
-->
<template>
  <ARow class="h-full pl-4">
    <ACol :span="8" :style="getTreeStyle">
      <BasicTree
        ref="treeRef"
        :search="true"
        :toolbar="true"
        :showIcon="true"
        :api="roleMenuTreeDataByRoleCode"
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
          <Icon v-if="dataRef.hasFieldScope" icon="i-ant-design:unordered-list-outlined" color="#f00" />
          <Icon v-else-if="isLeaf" icon="ant-design:file-outlined" />
          <Icon v-else-if="expanded" icon="i-ant-design:folder-open-outlined" />
          <Icon v-else icon="ant-design:folder-outlined" />
        </template>
      </BasicTree>
    </ACol>
    <ACol :span="16" :style="getMainStyle">
      <div v-show="record.menuCode != '0'" class="mr-2">
        <RoleFieldScopeList
          ref="roleFieldScopeListRef"
          :menuFieldScope="true"
          :resizeHeightOffset="85"
          @form="handleForm"
        />
      </div>
      <div v-if="record.menuCode == '0'" class="ml-4 mt-2">
        <Alert message="提示：请先选择菜单。" type="info" />
      </div>
    </ACol>
  </ARow>
</template>
<script lang="ts" setup name="ViewsSysMenuIndex">
  import { ref, computed, CSSProperties } from 'vue';
  import { Alert, Col, Row } from 'ant-design-vue';
  import { DownOutlined } from '@ant-design/icons-vue';
  import { useI18n } from '@jeesite/core/hooks/web/useI18n';
  import { useDict } from '@jeesite/core/components/Dict';
  import { Icon } from '@jeesite/core/components/Icon';
  import { Dropdown, DropMenu } from '@jeesite/core/components/Dropdown';
  import { BasicTree, TreeActionType } from '@jeesite/core/components/Tree';
  import { roleMenuTreeDataByRoleCode } from '@jeesite/core/api/sys/role';
  import { useWindowSizeFn } from '@jeesite/core/hooks/event/useWindowSizeFn';
  import { onMountedOrActivated } from '@jeesite/core/hooks/core/onMountedOrActivated';
  import RoleFieldScopeList from './RoleFieldScopeList.vue';

  const ARow = Row;
  const ACol = Col;

  const { t } = useI18n('sys.role');
  const record = ref<Recordable>({});

  const treeRef = ref<Nullable<TreeActionType>>(null);
  const apiParams = ref<Recordable>({ roleCode: '', sysCode: 'default' });

  const dropMenuList = ref<Array<DropMenu>>([]);
  const sysCode = ref<string>('default');
  const sysName = ref<string>(t('菜单'));

  const roleFieldScopeListRef = ref<InstanceType<typeof RoleFieldScopeList>>();

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
      overflowX: 'hidden',
      overflowY: 'auto',
    };
  });
  function calcTreeHeight() {
    contentHeight.value = document.documentElement.clientHeight;
  }
  useWindowSizeFn(calcTreeHeight, 280);
  onMountedOrActivated(calcTreeHeight);

  async function loadFieldScopeFormData(data: Recordable) {
    record.value = data;
    sysCode.value = 'default';
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
    // 加载数据权限表单
    roleFieldScopeListRef.value?.loadFieldScopeFormData(record.value);
  }

  async function handleSelect(_keys?: string[], obj?: any) {
    // 设置菜单图标状态
    if (record.value.menuCode != '0') {
      treeRef.value?.updateNodeByKey(record.value.menuCode, {
        hasFieldScope: !roleFieldScopeListRef.value?.isEmptyTable(),
      });
    }
    // 加载当前选择的菜单字段权限
    if (obj && obj.node && obj.node.dataRef) {
      const { id, rawName, permission } = obj.node.dataRef;
      record.value.menuCode = id;
      record.value.menuName = rawName;
      record.value.permission = permission;
      roleFieldScopeListRef.value?.loadFieldScopeFormData(record.value);
    }
  }

  function handleForm(data: Recordable) {
    data.menuCode = record.value.menuCode;
    roleFieldScopeListRef.value?.handleForm(data);
  }

  defineExpose({
    loadFieldScopeFormData,
    handleForm,
  });
</script>
