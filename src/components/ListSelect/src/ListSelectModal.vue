<!--
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 * No deletion without permission, or be held responsible to law.
 * @description 列表选择框、表格选择框
 * @author ThinkGem
-->
<template>
  <BasicModal v-bind="$attrs" @register="register" title="人员选择" @ok="handleSubmit" width="80%">
    <ARow>
      <ACol :span="18">
        <BasicTable @register="registerTable" @row-click="rowClick" @row-db-click="rowDbClick" />
      </ACol>
      <ACol :span="6" class="pl-3">
        {{ t('当前已选择') }} {{ selectList.length }} {{ t('项') }}：
        <div class="mt-2" v-if="selectList && selectList.length > 0">
          <Tag
            v-for="(item, index) in selectList"
            :key="item[props.config.itemCode]"
            :closable="true"
            @close="closeTag(index)"
            color="#2a50ec"
          >
            {{ item[props.config.itemName] + ' (' + item[props.config.itemCode] + ')' }}
          </Tag>
        </div>
      </ACol>
    </ARow>
  </BasicModal>
</template>
<script lang="ts" setup>
  import { ref } from 'vue';
  import { Row, Col, Tag } from 'ant-design-vue';
  import { useI18n } from '/@/hooks/web/useI18n';
  import { BasicModal, useModalInner } from '/@/components/Modal';
  import { BasicTable, useTable, BasicTableProps, TableRowSelection } from '/@/components/Table';

  const ARow = Row;
  const ACol = Col;

  const { t } = useI18n('listselect');

  const props = defineProps({
    config: { type: Object as PropType<any> },
    checkbox: { type: Boolean },
  });

  const emit = defineEmits(['select', 'register']);

  const selectList = ref<Recordable[]>([]);

  const rowSelection: TableRowSelection = {
    type: 'checkbox',
    onChange: (_selectedRowKeys: string[], selectedRows: Recordable[]) => {
      selectList.value = selectedRows;
    },
  };

  const tableProps: BasicTableProps = {
    showTableSetting: false,
    useSearchForm: true,
    canResize: true,
    resizeHeightOffset: 100,
    ...(props.checkbox ? { rowSelection } : {}),
    ...props.config?.tableProps,
  };

  const [registerTable] = useTable(tableProps);

  const [register, { closeModal, setModalProps }] = useModalInner((data) => {
    //setModalProps({ loading: true });
    //console.log(data);
    selectList.value = data.selectList;
    setModalProps({ loading: false });
  });

  const rowClick = (record: Recordable) => {
    if (!props.checkbox) {
      selectList.value = [record];
    }
  };

  const rowDbClick = (record: Recordable) => {
    rowClick(record);
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
</script>
