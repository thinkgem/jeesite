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
  import type { ErrorLogInfo } from '/#/store';
  import { watch, ref, unref, nextTick } from 'vue';
  import { cloneDeep } from 'lodash-es';
  import { BasicTable, useTable, TableAction } from '/@/components/Table/index';
  import { useModal } from '/@/components/Modal';
  import { useMessage } from '/@/hooks/web/useMessage';
  import { useI18n } from '/@/hooks/web/useI18n';
  import { useRootSetting } from '/@/hooks/setting/useRootSetting';
  import { useErrorLogStore } from '/@/store/modules/errorLog';
  import { defHttp } from '/@/utils/http/axios';
  import { isDevMode } from '/@/utils/env';
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
