import type { PropType } from 'vue';
import type { FieldNames, TreeActionItem, Keys, CheckKeys, ContextMenuOptions, TreeItem } from './typing';
import type { ContextMenuItem } from '@jeesite/core/hooks/web/useContextMenu';
import type { TreeDataItem } from 'ant-design-vue/es/tree';
import { propTypes } from '@jeesite/core/utils/propTypes';

export const basicProps = {
  value: {
    type: [Object, Array] as PropType<Keys | CheckKeys>,
  },
  renderIcon: {
    type: Function as PropType<(params: Recordable) => string>,
  },

  helpMessage: {
    type: [String, Array] as PropType<string | string[]>,
    default: '',
  },

  title: propTypes.string,
  toolbar: propTypes.bool,
  search: propTypes.bool,
  searchValue: propTypes.string,
  checkStrictly: propTypes.bool,
  showIcon: propTypes.bool.def(false),
  clickRowToExpand: propTypes.bool.def(true),
  checkable: propTypes.bool.def(false),
  defaultExpandLevel: {
    type: [String, Number] as PropType<string | number>,
    default: '',
  },
  defaultExpandAll: propTypes.bool.def(false),

  // formatter for ztree
  treeDataSimpleMode: propTypes.bool.def(true),

  fieldNames: {
    type: Object as PropType<FieldNames>,
  },

  treeData: {
    type: Array as PropType<TreeDataItem[]>,
  },

  api: { type: Function as PropType<(arg?: Recordable) => Promise<Recordable>> },
  params: { type: Object as PropType<Recordable> },
  canSelectParent: propTypes.bool.def(true),
  immediate: { type: Boolean, default: true },
  resultField: propTypes.string.def(''),
  dictType: propTypes.string,

  actionList: {
    type: Array as PropType<TreeActionItem[]>,
    default: () => [],
  },

  expandedKeys: {
    type: Array as PropType<Keys>,
    default: () => [],
  },

  selectedKeys: {
    type: Array as PropType<Keys>,
    default: () => [],
  },

  checkedKeys: {
    type: Array as PropType<CheckKeys>,
    default: () => [],
  },

  beforeRightClick: {
    type: Function as PropType<(...arg: any) => ContextMenuItem[] | ContextMenuOptions>,
    default: null,
  },

  rightMenuList: {
    type: Array as PropType<ContextMenuItem[]>,
  },
  // 自定义数据过滤判断方法(注: 不是整个过滤方法，而是内置过滤的判断方法，用于增强原本仅能通过title进行过滤的方式)
  filterFn: {
    type: Function as PropType<(searchValue: any, node: TreeItem, fieldNames: FieldNames) => boolean>,
    default: null,
  },
  // 高亮搜索值，仅高亮具体匹配值（通过title）值为true时使用默认色值，值为#xxx时使用此值替代且高亮开启
  highlight: {
    type: [Boolean, String] as PropType<boolean | string>,
    default: false,
  },
  // 搜索完成时自动展开结果
  expandOnSearch: propTypes.bool.def(true),
  // 搜索完成自动选中所有结果,当且仅当 checkable===true 时生效
  checkOnSearch: propTypes.bool.def(false),
  // 搜索完成自动select所有结果
  selectedOnSearch: propTypes.bool.def(false),
  // 只搜索树表指定的层级，获得该层级下所有结果
  onlySearchLevel: propTypes.number,
};

export const treeNodeProps = {
  actionList: {
    type: Array as PropType<TreeActionItem[]>,
    default: () => [],
  },
  fieldNames: {
    type: Object as PropType<FieldNames>,
  },
  treeData: {
    type: Array as PropType<TreeDataItem[]>,
    default: () => [],
  },
};
