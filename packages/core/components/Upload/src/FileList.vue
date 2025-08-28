<script lang="tsx">
  import { defineComponent, CSSProperties, nextTick, ref } from 'vue';
  import { useResizeObserver } from '@vueuse/core';
  import { fileListProps } from './props';
  import { isFunction } from '@jeesite/core/utils/is';
  import { useModalContext } from '@jeesite/core/components/Modal/src/hooks/useModalContext';
  import { get } from 'lodash-es';

  export default defineComponent({
    name: 'FileList',
    props: fileListProps,
    setup(props) {
      const modalFn = useModalContext();
      const tableRef = ref<HTMLTableElement>();
      useResizeObserver(tableRef, () => {
        nextTick(() => {
          modalFn?.redoModalHeight?.();
        });
      });
      return () => {
        const { columns, actionColumn, dataSource } = props;
        const columnList = [...columns, actionColumn];
        return (
          <table class="file-table" ref={tableRef}>
            <colgroup>
              {columnList.map((item) => {
                const { width = 0, dataIndex } = item;
                const style: CSSProperties = {
                  width: `${width}px`,
                  minWidth: `${width}px`,
                };
                return <col style={width ? style : {}} key={dataIndex} />;
              })}
            </colgroup>
            <thead>
              <tr class="file-table-tr">
                {columnList.map((item) => {
                  const { title = '', align = 'center', dataIndex } = item;
                  return (
                    dataIndex && (
                      <th class={['file-table-th', align]} key={dataIndex}>
                        {title}
                      </th>
                    )
                  );
                })}
              </tr>
            </thead>
            <tbody>
              {dataSource.map((record = {}, index) => {
                return (
                  <tr class="file-table-tr" key={`${index + record.name || ''}`}>
                    {columnList.map((item) => {
                      const { dataIndex = '', customRender, align = 'center' } = item;
                      const render = customRender && isFunction(customRender);
                      return (
                        dataIndex && (
                          <td class={['file-table-td', align]} key={dataIndex}>
                            {render
                              ? customRender?.({ text: get(record, dataIndex), record, index })
                              : get(record, dataIndex)}
                          </td>
                        )
                      );
                    })}
                  </tr>
                );
              })}
              {dataSource.length == 0 && (
                <tr class="file-table-tr">
                  <td class="file-table-td center" colspan={columnList.length}>
                    {props.emptyText}
                  </td>
                </tr>
              )}
            </tbody>
          </table>
        );
      };
    },
  });
</script>
<style lang="less">
  .file-table {
    width: 100%;
    border-top: 1px solid @border-color-base;
    border-right: 1px solid @border-color-base;
    border-collapse: separate;
    border-spacing: 0;

    &-th,
    &-td {
      border-left: 1px solid @border-color-base;
      border-bottom: 1px solid @border-color-base;
      padding: 12px 8px;
      overflow-wrap: break-word;
      word-break: break-all;
      white-space: normal;
      min-width: 100px;
    }

    thead {
      background-color: @background-color-light;
    }

    .center {
      text-align: center;
    }

    .left {
      text-align: left;
    }

    .right {
      text-align: right;
    }
  }

  html[data-theme='dark'] {
    .file-table {
      thead {
        background-color: #1a1a1a;
      }
    }
  }
</style>
