<!--
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 * No deletion without permission, or be held responsible to law.
 * @author ThinkGem
-->
<template>
  <PageWrapper :sidebarWidth="230">
    <template #sidebar>
      <BasicTree
        :search="true"
        :toolbar="true"
        :showIcon="true"
        :api="menuTreeData"
        :params="apiParams"
        :defaultExpandLevel="1"
        @select="handleSelect"
      >
        <template #headerTitle>
          <Dropdown class="cursor-pointer" :trigger="['hover']" :dropMenuList="dropMenuList">
            {{ sysName }} <DownOutlined />
          </Dropdown>
        </template>
      </BasicTree>
    </template>
    <ListView :treeCode="treeCode" :sysCode="sysCode" />
  </PageWrapper>
</template>
<script lang="ts" setup name="ViewsSysMenuIndex">
  import { ref, onMounted } from 'vue';
  import { DownOutlined } from '@ant-design/icons-vue';
  import { useI18n } from '/@/hooks/web/useI18n';
  import { useDict } from '/@/components/Dict';
  import { Dropdown, DropMenu } from '/@/components/Dropdown';
  import { PageWrapper } from '/@/components/Page';
  import { BasicTree } from '/@/components/Tree';
  import { Menu, menuIndex, menuTreeData } from '/@/api/sys/menu';
  import ListView from './list.vue';

  const { t } = useI18n('sys.menu');
  const treeCode = ref<string>('');
  const apiParams = ref<Recordable>({ sysCode: 'default' });

  const dropMenuList = ref<Array<DropMenu>>([]);
  const sysCode = ref<string>('default');
  const sysName = ref<string>(t('菜单'));

  onMounted(async () => {
    const res = await menuIndex();
    const menu = (res.menu || {}) as Menu;
    sysCode.value = menu.sysCode || 'default';
    loadSysCode();
  });

  async function loadSysCode() {
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
        },
      };
    });
  }

  function handleSelect(keys: string[]) {
    treeCode.value = keys[0];
  }
</script>
