<!--
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 * No deletion without permission, or be held responsible to law.
 * @author Vben、ThinkGem
-->
<template>
  <div :class="[prefixCls, getAlign]" @click="onCellClick">
    <template v-for="(action, index) in getActions" :key="`${index}-${action.label}`">
      <Tooltip v-if="action.tooltip" v-bind="getTooltip(action.tooltip)">
        <PopConfirmButton v-bind="action">
          <Icon :icon="action.icon" :class="{ 'mr-1': !!action.label }" :title="action.iconTitle" v-if="action.icon" />
          <template v-if="action.label">
            <span :title="action.iconTitle">{{ action.label }}</span>
          </template>
        </PopConfirmButton>
      </Tooltip>
      <PopConfirmButton v-else v-bind="action">
        <Icon :icon="action.icon" :class="{ 'mr-1': !!action.label }" :title="action.iconTitle" v-if="action.icon" />
        <template v-if="action.label">
          <span :title="action.iconTitle">{{ action.label }}</span>
        </template>
      </PopConfirmButton>
      <!--<Divider type="vertical" class="action-divider" v-if="props.divider && index < getActions.length - 1" />-->
    </template>
    <Popover
      v-if="props.dropDownActions && getDropdownList.length > 0"
      :trigger="['hover']"
      :dropMenuList="getDropdownList"
      popconfirm
    >
      <slot name="more"></slot>
      <a-button type="link" size="small" v-if="!$slots.more">
        <MoreOutlined class="icon-more" />
      </a-button>
    </Popover>
  </div>
</template>
<script lang="ts">
  import { defineComponent, PropType, computed, toRaw, unref } from 'vue';
  import { MoreOutlined } from '@ant-design/icons-vue';
  import { Divider, Tooltip, TooltipProps } from 'ant-design-vue';
  import Icon from '/@/components/Icon';
  import { ActionItem, TableActionType } from '/@/components/Table';
  import { PopConfirmButton } from '/@/components/Button';
  import { Popover } from '/@/components/Popover';
  import { useDesign } from '/@/hooks/web/useDesign';
  import { useTableContext } from '../hooks/useTableContext';
  import { usePermission } from '/@/hooks/web/usePermission';
  import { isBoolean, isFunction, isString } from '/@/utils/is';
  import { propTypes } from '/@/utils/propTypes';
  import { ACTION_COLUMN_FLAG } from '../const';

  const props = {
    actions: {
      type: Array as PropType<ActionItem[]>,
      default: null,
    },
    dropDownActions: {
      type: Array as PropType<ActionItem[]>,
      default: null,
    },
    divider: propTypes.bool.def(true),
    outside: propTypes.bool.def(false),
    stopButtonPropagation: propTypes.bool.def(false),
    align: propTypes.string,
  };

  export default defineComponent({
    name: 'TableAction',
    components: { Icon, PopConfirmButton, Divider, Popover, MoreOutlined, Tooltip },
    props,
    setup(props: any) {
      const { prefixCls } = useDesign('basic-table-action');
      let table: Partial<TableActionType> = {};
      if (!props.outside) {
        table = useTableContext();
      }

      const { hasPermission } = usePermission();
      function isIfShow(action: ActionItem): boolean {
        const ifShow = action.ifShow;

        let isIfShow = true;

        if (isBoolean(ifShow)) {
          isIfShow = ifShow;
        }
        if (isFunction(ifShow)) {
          isIfShow = ifShow(action);
        }
        return isIfShow;
      }

      const getActions = computed(() => {
        return (toRaw(props.actions) || [])
          .filter((action) => {
            return hasPermission(action.auth) && isIfShow(action);
          })
          .map((action) => {
            const { popConfirm } = action;
            return {
              // getPopupContainer: () => unref((table as any)?.wrapRef.value) ?? document.body, // 子表按钮被挤到一列
              type: 'link',
              size: 'small',
              ...action,
              ...(popConfirm || {}),
              iconTitle: action.title,
              onConfirm: popConfirm?.confirm,
              onCancel: popConfirm?.cancel,
              enable: !!popConfirm,
            };
          });
      });

      const getDropdownList = computed((): any[] => {
        const list = (toRaw(props.dropDownActions) || []).filter((action) => {
          return hasPermission(action.auth) && isIfShow(action);
        });
        return list.map((action, index) => {
          const { label, popConfirm } = action;
          return {
            ...action,
            ...popConfirm,
            iconTitle: action.title,
            onConfirm: popConfirm?.confirm,
            onCancel: popConfirm?.cancel,
            text: label,
            // divider: index < list.length - 1 ? props.divider : false,
            divider: index < list.length - 1 ? action.divider : false,
          };
        });
      });

      const getAlign = computed(() => {
        if (props.align) return props.align;
        const columns = (table as TableActionType)?.getColumns?.() || [];
        const actionColumn = columns.find((item) => item.flag === ACTION_COLUMN_FLAG);
        return actionColumn?.align ?? 'center';
      });

      function getTooltip(data: string | TooltipProps): TooltipProps {
        return {
          getPopupContainer: () => unref((table as any)?.wrapRef.value) ?? document.body,
          placement: 'bottom',
          ...(isString(data) ? { title: data } : data),
        };
      }

      function onCellClick(e: MouseEvent) {
        if (!props.stopButtonPropagation) return;
        const path = e.composedPath() as HTMLElement[];
        const isInButton = path.find((ele) => {
          return ele.tagName?.toUpperCase() === 'BUTTON';
        });
        isInButton && e.stopPropagation();
      }

      return { props, prefixCls, getActions, getDropdownList, getAlign, onCellClick, getTooltip };
    },
  });
</script>
<style lang="less">
  @prefix-cls: ~'jeesite-basic-table-action';

  .@{prefix-cls} {
    display: flex;
    align-items: center;

    .action-divider {
      display: table;
    }

    &.left {
      justify-content: flex-start;
    }

    &.center {
      justify-content: center;
    }

    &.right {
      justify-content: flex-end;
    }

    button.ant-btn,
    button.ant-btn.ant-btn-sm {
      display: flex;
      align-items: center;
      padding: 3px 6px;

      .anticon {
        opacity: 0.9;
      }
    }

    .ant-divider,
    .ant-divider-vertical {
      margin: 0 2px;
    }

    .icon-more {
      transform: rotate(90deg);

      svg {
        font-size: 1.1em;
        font-weight: 700;
        vertical-align: baseline;
      }
    }

    .anticon {
      font-size: 18px;
    }
  }
</style>
