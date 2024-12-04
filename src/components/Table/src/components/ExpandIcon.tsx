import { BasicArrow } from '/@/components/Basic';
import { isEmpty } from '/@/utils/is';

export default (expandCollapse: Fn, handleTableExpand: Fn, expandedRowRender = false) => {
  return (props: Recordable) => {
    // if (!props.expandable) {
    //   if (props.needIndentSpaced) {
    //     return <span class="ant-table-row-expand-icon ant-table-row-spaced" />;
    //   } else {
    //     return <span />;
    //   }
    // }
    const { treeLeaf, isLoading, children, childList } = props.record;
    // if (treeLeaf && treeLeaf === '1') {
    //   return <span class="ant-table-row-expand-icon ant-table-row-spaced" />;
    // }
    const leaf = !isEmpty(treeLeaf) ? treeLeaf === '1' : isEmpty(children || childList);
    return (
      <BasicArrow
        style={
          expandedRowRender
            ? ''
            : leaf
              ? 'margin-left: 1px; margin-right: 7px; opacity: 0.7;'
              : 'margin-right: 8px;'
        }
        onClick={async (_e: Event) => {
          if (leaf) return;
          if (expandedRowRender) {
            props.onExpand(props.record, _e);
          } else {
            // 提升展开折叠性能 by jeesite
            const expanded = await expandCollapse(props.record);
            handleTableExpand(expanded, props.record);
          }
        }}
        onDblclick={async (_e: Event) => {
          if (expandedRowRender) return;
          if (children || childList) return;
          // 当没有子节点的时候，尝试强制加载非正常状态的节点 by jeesite
          const expanded = await expandCollapse(props.record, false, true);
          handleTableExpand(expanded, props.record);
        }}
        expand={props.expanded}
        leaf={leaf}
        loading={isLoading}
      />
    );
  };
};
