import type { TreeDataItem } from 'ant-design-vue/es/tree';
import { ContextMenuItem } from '@jeesite/core/hooks/web/useContextMenu';

export interface TreeActionItem {
  render: (record: Recordable) => any;
  show?: boolean | ((record: Recordable) => boolean);
}

export interface TreeItem extends TreeDataItem {
  icon?: any;
  id?: string;
  pId?: string;
  name?: string;
  children?: TreeItem[];
}

export interface FieldNames {
  children?: string;
  title?: string;
  key?: string;
}

export type Keys = (string | number)[];
export type CheckKeys = (string | number)[] | { checked: (string | number)[]; halfChecked: (string | number)[] };

export interface TreeActionType {
  checkAll: (checkAll: boolean) => void;
  expandAll: (expandAll: boolean) => void;
  setExpandedKeys: (keys: Keys) => void;
  getExpandedKeys: () => Keys;
  setSelectedKeys: (keys: Keys) => void;
  getSelectedKeys: () => Keys;
  setCheckedKeys: (keys: Keys) => void;
  getCheckedKeys: () => Keys;
  filterByLevel: (level: number) => void;
  insertNodeByKey: (opt: InsertNodeParams) => void;
  insertNodesByKey: (opt: InsertNodeParams) => void;
  deleteNodeByKey: (key: string) => void;
  updateNodeByKey: (key: string, node: Omit<TreeDataItem, 'key'>, list?: TreeDataItem[]) => void;
  setSearchValue: (value: string) => void;
  getSearchValue: () => string;
  setTreeData: (treeData: Recordable[] | undefined) => void;
  reload: () => void;
}

export interface InsertNodeParams {
  parentKey: string | null;
  node: TreeDataItem;
  list?: TreeDataItem[];
  push?: 'push' | 'unshift';
}

export interface ContextMenuOptions {
  icon?: string;
  styles?: any;
  items?: ContextMenuItem[];
}
