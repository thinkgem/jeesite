import { Ref, VNodeChild } from 'vue';
import type { PaginationProps } from './pagination';
import type { FormProps } from '@jeesite/core/components/Form';
import type { TableRowSelection as ITableRowSelection } from 'ant-design-vue/lib/table/interface';
import type { ColumnProps } from 'ant-design-vue/lib/table';
import type { TableDataIndex } from '@jeesite/types/record';

import { ComponentType } from './componentType';
import { VueNode } from '@jeesite/core/utils/propTypes';
import { RoleEnum } from '@jeesite/core/enums/roleEnum';
import { ActionItem } from './tableAction';
import { EditRecordRow } from '../components/editable';

export declare type SortOrder = 'ascend' | 'descend';

export interface TableCurrentDataSource<T = Recordable> {
  currentDataSource: T[];
}

export interface TableRowSelection<T = any> extends ITableRowSelection {
  /**
   * Callback executed when selected rows change
   * @type Function
   */
  onChange?: (selectedRowKeys: string[] | number[] | any, selectedRows: T[]) => any;

  /**
   * Callback executed when select/deselect one row
   * @type Function
   */
  onSelect?: (record: T, selected: boolean, selectedRows: object[], nativeEvent: Event) => any;

  /**
   * Callback executed when select/deselect all rows
   * @type Function
   */
  onSelectAll?: (selected: boolean, selectedRows: T[], changeRows: T[]) => any;

  /**
   * Callback executed when row selection is inverted
   * @type Function
   */
  onSelectInvert?: (selectedRows: string[] | number[] | any) => any;
}

export interface TableCustomRecord<T> {
  record?: T;
  index?: number;
}

export interface ExpandedRowRenderRecord<T> extends TableCustomRecord<T> {
  indent?: number;
  expanded?: boolean;
}

export interface ColumnFilterItem {
  text?: string;
  value?: string;
  children?: any;
}

export interface TableCustomRecord<T = Recordable> {
  record?: T;
  index?: number;
}

export interface SorterResult {
  column: ColumnProps;
  order: SortOrder;
  field: string;
  columnKey: string;
}

export interface FetchParams {
  searchInfo?: Recordable;
  page?: number;
  sortInfo?: Recordable;
  filterInfo?: Recordable;
  parentCode?: string;
  record?: Recordable;
}

export interface GetColumnsParams {
  ignoreIndex?: boolean;
  ignoreAction?: boolean;
  sort?: boolean;
}

export type SizeType = 'default' | 'middle' | 'small' | 'large';

export interface TableActionType {
  reload: (opt?: FetchParams) => Promise<void>;
  setProps: (props: Partial<BasicTableProps>) => void;
  setLoading: (loading: boolean) => void;
  getTableRef: () => Ref<ComponentRef>;
  redoHeight: () => void;
  scrollTo: (pos: string) => void; // pos: id | "top" | "bottom"
  getSize: () => SizeType;
  emit: EmitType;

  getColumns: (opt?: GetColumnsParams) => BasicColumn[];
  getCacheColumns: () => BasicColumn[];
  // setCacheColumnsByField?: (dataIndex: string | undefined, value: BasicColumn) => void;
  setColumns: (columns: BasicColumn[] | string[]) => void;
  updateColumn: (column: BasicColumn | BasicColumn[]) => void;

  getPagination: () => PaginationProps | boolean;
  setPagination: (info: Partial<PaginationProps>) => void;
  setShowPagination: (show: boolean) => void;
  getShowPagination: () => boolean;

  getDataSource: <T = Recordable>() => T[];
  getDelDataSource: <T = Recordable>() => T[];
  getRawDataSource: <T = Recordable>() => T;

  setTableData: <T = Recordable>(values: T[]) => void;
  updateTableData: (index: number, key: string, value: any) => Recordable;
  updateTableDataRecord: (rowKey: string | number, record: Recordable) => Recordable | void;
  deleteTableDataRecord: (record: Recordable | Recordable[]) => Recordable | void;
  insertTableDataRecord: (record: Recordable, index?: number) => Recordable | void;
  findTableDataRecord: (rowKey: string | number) => Recordable | void;

  getRowSelection: () => TableRowSelection<Recordable>;
  getDefaultRowSelection: () => TableRowSelection<Recordable>;
  getSelectRows: <T = Recordable>() => T[];
  getSelectRowKeys: () => string[] | number[];
  setSelectedRowKeys: (rowKeys: string[] | number[]) => void;
  deleteSelectRowByKey: (key: string) => void;
  clearSelectedRowKeys: () => void;

  expandAll: () => void;
  expandRows: (keys: string[]) => void;
  collapseAll: () => void;
  expandCollapse: (record: Recordable) => void;
}

export interface FetchSetting {
  // 请求接口当前页数
  pageField: string;
  // 每页显示多少条
  sizeField: string;
  // 请求结果列表字段  支持 a.b.c
  listField: string;
  // 请求结果总数字段  支持 a.b.c
  totalField: string;
}

export interface TableSetting {
  redo?: boolean;
  size?: boolean;
  setting?: boolean;
  fullScreen?: boolean;
}

export interface BasicTableProps<T = any> {
  // 是否树表
  isTreeTable?: boolean;
  // 点击行选中
  clickToRowSelect?: boolean;
  // 自定义排序方法
  sortFn?: (sortInfo: SorterResult) => any;
  // 自定义过滤方法
  filterFn?: (data: Partial<Recordable<string[]>>) => any;
  // 取消表格的默认padding
  inset?: boolean;
  // 显示表格设置
  showTableSetting?: boolean;
  tableSettingStore?: boolean;
  tableSettingStoreKey?: string;
  tableSetting?: TableSetting;
  // 斑马纹
  striped?: boolean;
  // 是否自动生成key
  autoCreateKey?: boolean;
  // 计算合计行的方法
  summaryFunc?: (...arg: any) => Recordable[];
  // 自定义合计表格内容
  summaryData?: Recordable[];
  // 是否显示合计行
  showSummary?: boolean;
  // 是否可拖拽行
  canRowDrag?: boolean;
  // 接口请求对象
  api?: (...arg: any) => Promise<any>;
  // 请求之前处理参数
  beforeFetch?: Fn;
  // 自定义处理接口返回参数
  afterFetch?: Fn;
  // 查询条件请求之前处理
  handleSearchInfoFn?: Fn;
  // 请求接口配置
  fetchSetting?: Partial<FetchSetting>;
  // 立即请求接口
  immediate?: boolean;
  // 在开起搜索表单的时候，如果没有数据是否显示表格
  emptyDataIsShowTable?: boolean;
  // 额外的请求参数
  searchInfo?: Recordable;
  // 默认的排序参数
  defSort?: Recordable;
  // 使用搜索表单
  useSearchForm?: boolean;
  // 是否显示搜索表单
  showSearchForm?: boolean;
  // 表单配置
  formConfig?: Partial<FormProps<T>>;
  // 列配置
  columns: BasicColumn<T>[];
  // 是否显示序号列
  showIndexColumn?: boolean;
  // 序号列配置
  indexColumnProps?: BasicColumn<T>;
  actionColumn?: BasicColumn<T>;
  // 文本超过宽度是否显示。。。
  ellipsis?: boolean;
  // 是否继承父级高度（父级高度-表单高度-padding高度）
  isCanResizeParent?: boolean;
  // 是否可以自适应高度
  canResize?: boolean;
  // 自适应高度偏移， 计算结果-偏移量
  resizeHeightOffset?: number;
  // 主键名称
  rowKey?: string | ((record: Recordable, defaultValue?: any) => string);
  // 数据
  dataSource?: Recordable[];
  // 标题右侧提示
  titleHelpMessage?: string | string[];
  // 表格滚动最大高度
  maxHeight?: number;
  // 表格最小高度（仅 canResize 时有效）
  minHeight?: number;
  // 是否显示边框
  bordered?: boolean;
  // 分页配置
  pagination?: PaginationProps | boolean;
  // loading加载
  loading?: boolean;

  /**
   * The column contains children to display
   * @default 'children'
   * @type string | string[]
   */
  childrenColumnName?: string;

  /**
   * Override default table elements
   * @type object
   */
  components?: object;

  /**
   * Expand all rows initially
   * @default false
   * @type boolean
   */
  defaultExpandAllRows?: boolean;

  /**
   * Initial expanded row keys
   * @type string[]
   */
  defaultExpandedRowKeys?: string[];

  /**
   * Current expanded row keys
   * @type string[]
   */
  expandedRowKeys?: string[];

  /**
   * Expanded container render for each row
   * @type Function
   */
  expandedRowRender?: (record?: ExpandedRowRenderRecord<T>) => VNodeChild | JSX.Element;

  /**
   * Customize row expand Icon.
   * @type Function | VNodeChild
   */
  expandIcon?: Function | VNodeChild | JSX.Element;

  /**
   * Whether to expand row by clicking anywhere in the whole row
   * @default false
   * @type boolean
   */
  expandRowByClick?: boolean;

  /**
   * The index of `expandIcon` which column will be inserted when `expandIconAsCell` is false. default 0
   */
  expandIconColumnIndex?: number;

  /**
   * Table footer renderer
   * @type Function | VNodeChild
   */
  footer?: Function | VNodeChild | JSX.Element;

  /**
   * Indent size in pixels of tree data
   * @default 15
   * @type number
   */
  indentSize?: number;

  /**
   * i18n text including filter, sort, empty text, etc
   * @default { filterConfirm: 'Ok', filterReset: 'Reset', emptyText: 'No Data' }
   * @type object
   */
  locale?: object;

  /**
   * Row's className
   * @type Function
   */
  rowClassName?: (record: TableCustomRecord<T>, index: number) => string;

  /**
   * Row selection config
   * @type object
   */
  rowSelection?: TableRowSelection;

  // 默认不展示复选框，但是通过右上角给表格设置复选框的时候加载默认参数
  defaultRowSelection?: TableRowSelection;

  // 重载表格数据的时候清空已选择选项
  clearSelectedOnReload?: boolean;

  // 是否在表格上方显示多选状态栏
  showSelectionBar?: boolean;

  /**
   * Set horizontal or vertical scrolling, can also be used to specify the width and height of the scroll area.
   * It is recommended to set a number for x, if you want to set it to true,
   * you need to add style .ant-table td { white-space: nowrap; }.
   * @type object
   */
  scroll?: { x?: number | true; y?: number; scrollToFirstRowOnChange?: boolean };

  /**
   * Whether to show table header
   * @default true
   * @type boolean
   */
  showHeader?: boolean;

  /**
   * Size of table
   * @default 'default'
   * @type string
   */
  size?: SizeType;

  /**
   * Table title renderer
   * @type Function | ScopedSlot
   */
  title?: VNodeChild | JSX.Element | string | ((data: Recordable) => string);

  /**
   * Set props on per header row
   * @type Function
   */
  customHeaderRow?: (column: ColumnProps, index: number) => object;

  /**
   * Set props on per row
   * @type Function
   */
  customRow?: (record: T, index: number) => object;

  /**
   * `table-layout` attribute of table element
   * `fixed` when header/columns are fixed, or using `column.ellipsis`
   *
   * @see https://developer.mozilla.org/en-US/docs/Web/CSS/table-layout
   * @version 1.5.0
   */
  tableLayout?: 'auto' | 'fixed' | string;

  /**
   * the render container of dropdowns in table
   * @param triggerNode
   * @version 1.5.0
   */
  getPopupContainer?: (triggerNode?: HTMLElement) => HTMLElement;

  /**
   * Data can be changed again before rendering.
   * The default configuration of general user empty data.
   * You can configured globally through [ConfigProvider](https://antdv.com/components/config-provider-cn/)
   *
   * @version 1.5.4
   */
  transformCellText?: Function;

  /**
   * Callback executed before editable cell submit value, not for row-editor
   *
   * The cell will not submit data while callback return false
   */
  beforeEditSubmit?: (data: { record: Recordable; index: number; key: string | number; value: any }) => Promise<any>;

  /**
   * Callback executed when pagination, filters or sorter is changed
   * @param pagination
   * @param filters
   * @param sorter
   * @param currentDataSource
   */
  onChange?: (pagination: any, filters: any, sorter: any, extra: any) => void;

  /**
   * Callback executed when the row expand icon is clicked
   *
   * @param expanded
   * @param record
   */
  onExpand?: (expanded: boolean, record: T) => void;

  /**
   * Callback executed when the expanded rows change
   * @param expandedRows
   */
  onExpandedRowsChange?: (expandedRows: string[] | number[]) => void;

  // 表格列更改事件
  onColumnsChange?: (data: ColumnChangeParam[]) => void;
}

export type CellFormat<T = Recordable> =
  | string
  | ((text: string, record: T, index: number, column?: BasicColumn) => string | number)
  | Map<string | number, any>;

export interface BasicColumn<T = Recordable> extends ColumnProps<T> {
  dataIndex?: TableDataIndex<T>;
  dataIndex_?: string;
  children?: BasicColumn<T>[];
  // filters?: {
  //   text: VueNode;
  //   value: string | number | boolean;
  //   children?: unknown[] | (((props: Record<string, unknown>) => unknown[]) & (() => unknown[]));
  // }[];

  // 标记为内置列
  flag?: 'INDEX' | 'DRAG' | 'DEFAULT' | 'CHECKBOX' | 'RADIO' | 'ACTION';

  // Antdv 3.0 中，不推荐使用 slots 所以新增 slot 指定插槽名称
  slot?: string;

  // Whether to hide the column by default, it can be displayed in the column configuration
  defaultHidden?: boolean;

  // Help text for table column header
  helpMessage?: string | string[];

  // 单元格格式化
  format?: CellFormat<T>;

  // Editable
  edit?: boolean;
  editRow?: boolean;
  editable?: boolean;
  editAutoCancel?: boolean;
  editComponent?: ComponentType;
  editComponentProps?:
    | ((opt: { text: any; record: EditRecordRow | Recordable; column: BasicColumn<T>; index: number }) => Recordable)
    | any;
  // 自定义修改后显示的内容
  editRender?: (opt: {
    text: string | number | boolean | T;
    record: T;
    column: BasicColumn<T>;
    index: number;
    attrs?: object;
  }) => VNodeChild | JSX.Element;
  editRule?: boolean | ((text: any, record: T) => Promise<void>);
  // editValueMap?: (value: any) => string;
  onEditRow?: () => void;

  // 默认值
  editDefaultValue?: any;
  editDefaultLabel?: any;

  // 权限编码控制是否显示
  auth?: RoleEnum | RoleEnum[] | string | string[];
  // 业务控制是否显示
  ifShow?: boolean | ((column: BasicColumn<T>) => boolean);

  // 数据的标签显示，举例 dataIndex 是 userCode，dataLabel 是 userName
  dataLabel?: string;

  // JeeSite的字典类型
  dictType?: string;

  // JeeSite的字典类型
  filterDictType?: string;

  // 没有找到字典标签的时候显示的默认值
  defaultValue?: string;

  // 列表操作列选项
  actions?: (record: T) => ActionItem[];
  dropDownActions?: (record: T) => ActionItem[];
}

export type ColumnChangeParam = {
  dataIndex?: string;
  dataIndex_?: string;
  fixed: boolean | 'left' | 'right' | undefined;
  open: boolean;
};

export interface InnerHandlers {
  onColumnsChange: (data: ColumnChangeParam[]) => void;
}

export interface InnerMethods {
  clearSelectedRowKeys: TableActionType['clearSelectedRowKeys'];
  getSelectRowKeys: TableActionType['getSelectRowKeys'];
}
