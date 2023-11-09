<!--
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 * No deletion without permission, or be held responsible to law.
 * @description 列表选择框、表格选择框
 * @author ThinkGem
-->
<template>
  <BasicModal
    v-bind="$attrs"
    @register="register"
    :title="props.config.modalProps?.title || t('数据选择')"
    wrapClassName="jeesite-listselect"
    @ok="handleSubmit"
    width="80%"
  >
    <ARow>
      <ACol :span="4" v-if="props.config.treeProps" :style="getTreeStyle">
        <BasicTree
          :title="props.config.treeProps.title"
          :search="true"
          :toolbar="true"
          :showIcon="true"
          :api="props.config.treeProps.api"
          :params="props.config.treeProps.params"
          :defaultExpandLevel="2"
          @select="handleTreeSelect"
        />
      </ACol>
      <ACol :span="props.config.treeProps ? 17 : 18">
        <BasicTable
          @register="registerTable"
          @row-db-click="rowDbClick"
          :canResize="true"
          :minHeight="tableHeight"
          :maxHeight="tableHeight"
          class="jeesite-listselect-table"
        />
      </ACol>
      <ACol :span="props.config.treeProps ? 3 : 6" class="pl-3 pt-3">
        {{ t('当前已选择 {0} 项', [selectList.length]) }}：
        <div class="mt-2" v-if="selectList && selectList.length > 0">
          <Tag
            v-for="(item, index) in selectList"
            :key="item[props.config.itemCode]"
            :closable="true"
            @close="closeTag(index)"
            style="margin: 3px 5px 0 0"
            :color="token.colorPrimary"
          >
            {{ item[props.config.itemName] + ' (' + item[props.config.itemCode] + ')' }}
          </Tag>
        </div>
      </ACol>
    </ARow>
  </BasicModal>
</template>
<script lang="ts" setup>
  import { ref, CSSProperties, computed } from 'vue';
  import { Row, Col, Tag, theme } from 'ant-design-vue';
  import { useI18n } from '/@/hooks/web/useI18n';
  import { BasicTree } from '/@/components/Tree';
  import { BasicModal, useModalInner } from '/@/components/Modal';
  import { BasicTable, useTable, BasicTableProps, TableRowSelection } from '/@/components/Table';
  import { useWindowSizeFn } from '/@/hooks/event/useWindowSizeFn';
  import { onMountedOrActivated } from '/@/hooks/core/onMountedOrActivated';

  const ARow = Row;
  const ACol = Col;

  const { token } = theme.useToken();
  const { t } = useI18n('listselect');

  const props = defineProps({
    config: { type: Object as PropType<any> },
    checkbox: { type: Boolean },
  });

  const emit = defineEmits(['select', 'register']);

  const treeHeight = ref(400);
  const tableHeight = ref(400);
  const getTreeStyle = computed((): CSSProperties => {
    return {
      height: `${treeHeight.value}px`,
      minHeight: `${treeHeight.value}px`,
    };
  });
  function calcTreeHeight() {
    let height = document.documentElement.clientHeight;
    treeHeight.value = height - 280;
    tableHeight.value = height - 380;
  }
  useWindowSizeFn(calcTreeHeight, 280);
  onMountedOrActivated(calcTreeHeight);

  const selectList = ref<Recordable[]>([]);

  type Key = string | number;
  const selectedRowKeys = ref<Key[]>([]);
  const initialize = ref<boolean>(false);

  const rowSelection: TableRowSelection = {
    type: props.checkbox ? 'checkbox' : 'radio',
    columnWidth: props.checkbox ? undefined : 0,
    selectedRowKeys: selectedRowKeys as unknown as Key[],
    onChange: (_selectedRowKeys: Key[], selectedRows: Recordable[]) => {
      if (!initialize.value) return; // 首次加载不更新状态，会将初始值清空
      selectedRowKeys.value = _selectedRowKeys;
      selectList.value = selectedRows;
    },
  };

  const tableProps: BasicTableProps = {
    showTableSetting: false,
    useSearchForm: true,
    canResize: true,
    resizeHeightOffset: 100,
    // ...(props.checkbox ? { rowSelection } : {}),
    rowSelection,
    ...props.config?.tableProps,
    afterFetch: () => {
      initialize.value = true;
    },
  };

  const [registerTable, tableAction] = useTable(tableProps);

  const [register, { closeModal, setModalProps }] = useModalInner(async (data) => {
    //setModalProps({ loading: true });
    //console.log(data);
    selectList.value = data.selectList;
    selectedRowKeys.value = selectList.value.map((e) => e[props.config.itemCode]);
    if (data.queryParams) {
      const params = Object.assign(tableAction.getForm().getFieldsValue(), data.queryParams);
      await tableAction.getForm().setFieldsValue(params);
    }
    setModalProps({ loading: false });
  });

  // const rowClick = (record: Recordable) => {
  //   if (!props.checkbox) {
  //     selectList.value = [record];
  //   }
  // };

  const rowDbClick = (record: Recordable) => {
    // rowClick(record);
    if (!props.checkbox) {
      emit('select', [record]);
      closeModal();
    }
  };

  const closeTag = (index: number) => {
    selectList.value.splice(index, 1);
  };

  const handleSubmit = () => {
    emit('select', selectList.value);
    closeModal();
  };

  async function handleTreeSelect(keys: string[]) {
    const values = {};
    values[props.config.treeTableFieldName] = keys[0];
    await tableAction.getForm().setFieldsValue(values);
    await tableAction.reload();
  }
</script>
<style lang="less">
  .jeesite-listselect-table {
    .ant-table-wrapper {
      .ant-table {
        td {
          cursor: pointer;
        }
      }
    }
    &.jeesite-basic-table-form-container {
      .ant-form.jeesite-basic-form {
        border-top: 0 !important;
      }
    }
  }
  .jeesite-listselect {
    .ant-modal.jeesite-basic-modal {
      .ant-modal {
        &-body {
          > .scrollbar {
            .scrollbar__wrap {
              margin-top: 0;
              margin-bottom: 0;
            }
          }
        }
      }
    }
  }
</style>
