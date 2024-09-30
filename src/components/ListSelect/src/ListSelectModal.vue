<!--
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 * No deletion without permission, or be held responsible to law.
 * @description 列表选择框、表格选择框
 * @author ThinkGem
-->
<template>
  <BasicModal
    v-bind="$attrs"
    wrapClassName="jeesite-listselect"
    :title="props.config.modalProps?.title || t('数据选择')"
    :canFullscreen="false"
    @register="registerModal"
    @ok="handleSubmit"
    width="90%"
  >
    <template #appendHeader>
      <template v-for="(item, _index) in headerButtons" :key="_index">
        <a-button
          size="small"
          class="ml-4"
          v-auth="item.props.buttonAuth"
          @click="item.handleOpenModal(item.props.buttonComp)"
        >
          <Icon icon="i-fluent:add-12-filled" /> {{ t(item.props.buttonText) }}
        </a-button>
        <component
          :is="item.modalComponent"
          v-model:open="item.modalComponentOpen"
          @register="item.registerModal"
          @success="item.handleSuccess(tableAction)"
        />
      </template>
    </template>
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
      <ACol :span="props.config.treeProps ? 17 : 18" class="overflow-hidden">
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
        {{ t('common.selectedItems', [selectList.length]) }}：
        <div
          class="jeesite-listselect-tags mt-2"
          v-if="selectList && selectList.length > 0"
          :style="`height:${treeHeight}px`"
        >
          <Tag
            v-for="(item, index) in selectList"
            :key="item[props.config.itemCode]"
            color="processing"
          >
            <span
              :title="
                item[props.config.itemName] +
                (props.config.isShowCode !== false ? ' (' + item[props.config.itemCode] + ')' : '')
              "
            >
              {{ item[props.config?.itemName] }}
              {{
                props.config?.isShowCode !== false ? ' (' + item[props.config?.itemCode] + ')' : ''
              }}
            </span>
            <Icon icon="i-ant-design:close-outlined" @click="closeTag(index)" />
          </Tag>
        </div>
      </ACol>
    </ARow>
  </BasicModal>
</template>
<script lang="ts" setup>
  import { defineComponent, ref, CSSProperties, computed, shallowRef } from 'vue';
  import { Row, Col, Tag } from 'ant-design-vue';
  import { useI18n } from '/@/hooks/web/useI18n';
  import { BasicTree } from '/@/components/Tree';
  import { Icon } from '/@/components/Icon';
  import { BasicModal, useModal, useModalInner } from '/@/components/Modal';
  import { BasicTable, useTable, BasicTableProps, TableRowSelection } from '/@/components/Table';
  import { useWindowSizeFn } from '/@/hooks/event/useWindowSizeFn';
  import { onMountedOrActivated } from '/@/hooks/core/onMountedOrActivated';
  import { dynamicImport } from '/@/router/helper/routeHelper';
  import { createAsyncComponent } from '/@/utils/factory/createAsyncComponent';

  const ARow = Row;
  const ACol = Col;

  const { t } = useI18n();

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
    let padding = 280;
    treeHeight.value = height - padding;
    tableHeight.value = height - padding - 90;
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
    resizeHeightOffset: 60,
    rowSelection,
    clearSelectedOnReload: false,
    ...props.config?.tableProps,
    afterFetch: (data: any[]) => {
      initialize.value = true;
      if (props.config?.tableProps?.afterFetch) {
        return props.config?.tableProps?.afterFetch(data);
      }
    },
  };

  const [registerTable, tableAction] = useTable(tableProps);
  const headerButtons = ref<any[]>([]);

  const [registerModal, { closeModal, setModalProps }] = useModalInner(async (data) => {
    //setModalProps({ loading: true });
    //console.log(data);
    selectList.value = data.selectList;
    selectedRowKeys.value = selectList.value.map((e) => e[props.config.itemCode]);
    if (data.queryParams) {
      const params = Object.assign(tableAction.getForm().getFieldsValue(), data.queryParams);
      await tableAction.getForm().setFieldsValue(params);
    }
    if (props.config.headerButtons) {
      headerButtons.value = [];
      for (const item of props.config.headerButtons) {
        const [registerModal, inputAction] = useModal();
        const modalComponent = shallowRef<Nullable<any>>(null);
        const modalComponentOpen = ref<boolean>(false);
        headerButtons.value.push({
          props: item,
          registerModal,
          modalComponent,
          modalComponentOpen,
          handleOpenModal: (buttonComp) => {
            let component: ReturnType<typeof defineComponent>;
            const imp = dynamicImport(buttonComp);
            if (imp) component = createAsyncComponent(imp);
            if (component) {
              modalComponent.value = component;
              inputAction.setModalData({});
              modalComponentOpen.value = true;
            }
          },
          handleSuccess: (tableAction: any) => {
            tableAction.reload();
          },
        });
      }
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
  .jeesite-listselect {
    .jeesite-basic-tree-header {
      padding: 2px 0 4px;
      border: 0;
    }

    &-table {
      .ant-table-wrapper {
        .ant-table {
          td {
            cursor: pointer;
          }
        }
      }
    }

    &-tags {
      overflow: auto;

      .ant-tag {
        margin: 4px 0 0;
        padding: 0 4px;
        width: 100%;
        display: flex;
        justify-content: space-between;

        span {
          overflow: hidden;
          white-space: nowrap;
          cursor: default;
        }

        .anticon {
          margin-left: 4px !important;
          height: 20px;
          cursor: pointer;
        }
      }
    }
  }
</style>
