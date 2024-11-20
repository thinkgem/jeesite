<template>
  <div>
    <template v-for="src in imgList" :key="src">
      <img :src="src" v-show="false" />
    </template>
    <BasicTable @register="register" class="error-handle-table" @row-click="handleDetail">
      <template #toolbar>
        <a-button @click="fireVueError" type="primary">
          {{ t('sys.errorLog.fireVueError') }}
        </a-button>
        <a-button @click="fireResourceError" type="primary">
          {{ t('sys.errorLog.fireResourceError') }}
        </a-button>
        <a-button @click="fireAjaxError" type="primary">
          {{ t('sys.errorLog.fireAjaxError') }}
        </a-button>
      </template>
      <template #action="{ record }">
        <TableAction
          :actions="[
            { label: t('sys.errorLog.tableActionDesc'), onClick: handleDetail.bind(null, record) },
          ]"
        />
      </template>
    </BasicTable>
    <DetailModal :info="rowInfo" @register="registerModal" />
  </div>
</template>

<script lang="ts" setup>
  import type { ErrorLogInfo } from '@jeesite/types/store';
  import { watch, ref, unref, nextTick } from 'vue';
  import { cloneDeep } from 'lodash-es';
  import { BasicTable, useTable, TableAction } from '@jeesite/core/components/Table';
  import { useModal } from '@jeesite/core/components/Modal';
  import { useMessage } from '@jeesite/core/hooks/web/useMessage';
  import { useI18n } from '@jeesite/core/hooks/web/useI18n';
  import { useRootSetting } from '@jeesite/core/hooks/setting/useRootSetting';
  import { useErrorLogStore } from '@jeesite/core/store/modules/errorLog';
  import { defHttp } from '@jeesite/core/utils/http/axios';
  import { isDevMode } from '@jeesite/core/utils/env';
  import { getColumns } from './data';
  import DetailModal from './DetailModal.vue';

  const rowInfo = ref<ErrorLogInfo>();
  const imgList = ref<string[]>([]);

  const { t } = useI18n();
  const errorLogStore = useErrorLogStore();
  const [register, { setTableData }] = useTable({
    title: t('sys.errorLog.tableTitle'),
    columns: getColumns(),
    actionColumn: {
      width: 80,
      title: '操作',
      dataIndex: 'action',
      slot: 'action',
    },
    canResize: true,
  });
  const [registerModal, { openModal }] = useModal();

  watch(
    () => errorLogStore.getErrorLogInfoList,
    (list) => {
      nextTick(() => {
        setTableData(cloneDeep(list));
      });
    },
    {
      immediate: true,
    },
  );

  const { getUseErrorHandle } = useRootSetting();
  const { createMessage } = useMessage();
  if (isDevMode() && !unref(getUseErrorHandle)) {
    createMessage.info(t('sys.errorLog.enableMessage'));
  }
  // 查看详情
  function handleDetail(row: ErrorLogInfo) {
    rowInfo.value = row;
    openModal(true);
  }

  function fireVueError() {
    throw new Error('fire vue error!');
  }

  function fireResourceError() {
    imgList.value.push(`${new Date().getTime()}.png`);
  }

  async function fireAjaxError() {
    await defHttp.get({ url: '/error' });
  }
</script>
