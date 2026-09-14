<script lang="tsx">
  import { defineComponent, CSSProperties, nextTick, ref, watch } from 'vue';
  import { useResizeObserver } from '@vueuse/core';
  import { FileBasicColumn, UploadResultStatus } from './typing';
  import { formatSize } from './helper';
  import { isFunction } from '@jeesite/core/utils/is';
  import { useModalContext } from '@jeesite/core/components/Modal/src/hooks/useModalContext';
  import { get } from 'lodash-es';

  export default defineComponent({
    name: 'FileList',
    props: {
      columns: {
        type: [Array] as PropType<FileBasicColumn[]>,
        default: null,
      },
      actionColumn: {
        type: Object as PropType<FileBasicColumn>,
        default: null,
      },
      dataSource: {
        type: Array as PropType<any[]>,
        default: null,
      },
      emptyText: {
        type: String,
        default: '',
      },
      // 是否允许从资源管理器拖入文件上传
      dropUpload: {
        type: Boolean as PropType<boolean>,
        default: true,
      },
      // 列表展现形式：grid 网格（图片）、table 表格（文件）
      listType: {
        type: String as PropType<'grid' | 'table'>,
        default: 'table',
      },
      // 是否允许拖拽排序（图片网格、文件表格）
      dragSort: {
        type: Boolean as PropType<boolean>,
        default: true,
      },
    },
    emits: ['reorder', 'dropFiles'],
    setup(props, { emit }) {
      const modalFn = useModalContext();
      const listRef = ref<HTMLElement>();
      useResizeObserver(listRef, () => {
        nextTick(() => {
          // 弹窗关闭/隐藏时元素高度为 0，跳过避免关闭动画过程中高度重算导致的闪烁
          if (!listRef.value || !listRef.value.offsetHeight) return;
          modalFn?.redoModalHeight?.();
        });
      });

      // 拖拽排序
      const dragIndex = ref<number>();
      const dragOverIndex = ref<number>();
      // 从资源管理器拖入文件时，列表区域的高亮状态
      const isFileDragOver = ref(false);

      // 判断是否为从资源管理器拖入的外部文件（与列表内排序拖拽区分）
      function isExternalFileDrop(e: DragEvent): boolean {
        const dt = e.dataTransfer;
        if (!dt) return false;
        if (dt.files && dt.files.length > 0) return true;
        // dragover 阶段 files 不可读取，用 types 判断
        return Array.from(dt.types || []).includes('Files');
      }

      // 收集拖入的文件并抛出给父组件上传
      function emitDropFiles(e: DragEvent) {
        const files = e.dataTransfer ? Array.from(e.dataTransfer.files || []) : [];
        if (files.length > 0) emit('dropFiles', files);
      }

      // 容器（列表整体）拖放处理：仅响应外部文件拖入
      function onContainerDragOver(e: DragEvent) {
        if (!props.dropUpload || !isExternalFileDrop(e)) return;
        e.preventDefault();
        isFileDragOver.value = true;
      }

      function onContainerDragLeave(e: DragEvent) {
        if (!props.dropUpload || !isExternalFileDrop(e)) return;
        const related = e.relatedTarget as Node | null;
        if (!related || !(e.currentTarget as HTMLElement).contains(related)) {
          isFileDragOver.value = false;
        }
      }

      function onContainerDrop(e: DragEvent) {
        if (!props.dropUpload || !isExternalFileDrop(e)) return;
        e.preventDefault();
        e.stopPropagation();
        isFileDragOver.value = false;
        emitDropFiles(e);
      }

      function onDragStart(e: DragEvent, index: number) {
        if (!props.dragSort) {
          e.preventDefault();
          return;
        }
        dragIndex.value = index;
        if (e.dataTransfer) {
          e.dataTransfer.effectAllowed = 'move';
        }
      }

      function onDragOver(e: DragEvent, index: number) {
        // 外部文件拖入时，允许放下但不显示排序高亮，避免误触发排序
        if (props.dropUpload && isExternalFileDrop(e)) {
          e.preventDefault();
          return;
        }
        if (!props.dragSort) return;
        e.preventDefault();
        dragOverIndex.value = index;
      }

      function onDrop(e: DragEvent, index: number) {
        // 外部文件拖入到具体条目上时，交给上传处理（阻止冒泡避免与容器重复触发）
        if (props.dropUpload && isExternalFileDrop(e)) {
          e.preventDefault();
          e.stopPropagation();
          isFileDragOver.value = false;
          emitDropFiles(e);
          return;
        }
        if (!props.dragSort) return;
        e.preventDefault();
        const from = dragIndex.value;
        dragIndex.value = undefined;
        dragOverIndex.value = undefined;
        if (from == null || from === index) return;
        emit('reorder', { from, to: index });
      }

      function onDragEnd() {
        dragIndex.value = undefined;
        dragOverIndex.value = undefined;
      }

      // 上传时，自动滚动到正在上传文件（进度）的位置
      const scrolledIds = new Set<string>();
      watch(
        () => props.dataSource,
        (list) => {
          const items = (list || []) as any[];
          const idx = items.findIndex(
            (item) => item.status === UploadResultStatus.UPLOADING && !scrolledIds.has(item.id),
          );
          if (idx >= 0) {
            const item = items[idx];
            scrolledIds.add(item.id);
            nextTick(() => {
              const el = listRef.value?.querySelector(`[data-idx="${idx}"]`) as HTMLElement | null;
              el && el.scrollIntoView({ block: 'nearest', behavior: 'smooth' });
            });
          }
        },
        { deep: true },
      );

      // 渲染单元格内容
      function renderCell(column: FileBasicColumn | null | undefined, record: any, index: number) {
        if (!column) return null;
        const { dataIndex = '', customRender } = column;
        const render = customRender && isFunction(customRender);
        return render ? customRender?.({ text: get(record, dataIndex), record, index }) : get(record, dataIndex);
      }

      return () => {
        const { columns, actionColumn, dataSource, listType } = props;

        // 图片类型使用网格形式展现
        if (listType === 'grid') {
          const thumbColumn = columns.find((item) => item.dataIndex === 'fileUrl') || columns[0];
          const nameColumn = columns.find((item) => item.dataIndex === 'fileName');
          return (
            <div
              class={['file-grid', isFileDragOver.value ? 'is-file-dragover' : '']}
              ref={listRef}
              onDragover={onContainerDragOver}
              onDragleave={onContainerDragLeave}
              onDrop={onContainerDrop}
            >
              {dataSource.map((record = {}, index) => {
                const rec = record as any;
                const sizeVal = get(rec, 'size') ?? get(rec, 'fileEntity.fileSize') ?? 0;
                const sizeText = sizeVal ? formatSize(sizeVal) : '';
                const dateText = get(rec, 'createDate') || '';
                // 标题 | 大小 | 日期，鼠标悬停时通过 title 显示
                const titleText = [rec.fileName, sizeText, dateText].filter(Boolean).join(' | ');
                return (
                  <div
                    class={['file-grid-item', dragOverIndex.value === index && props.dragSort ? 'is-dragover' : '']}
                    key={rec.id || index}
                    data-idx={index}
                    title={titleText}
                    draggable={props.dragSort}
                    onDragstart={(e: DragEvent) => onDragStart(e, index)}
                    onDragover={(e: DragEvent) => onDragOver(e, index)}
                    onDrop={(e: DragEvent) => onDrop(e, index)}
                    onDragend={onDragEnd}
                  >
                    <div class="file-grid-item__thumb">
                      {renderCell(thumbColumn, rec, index)}
                      {<div class="file-grid-item__status">{renderCell(nameColumn, rec, index)}</div>}
                    </div>
                    <div class="file-grid-item__action">{renderCell(actionColumn, rec, index)}</div>
                  </div>
                );
              })}
              {dataSource.length == 0 && <div class="file-grid-empty">{props.emptyText}</div>}
            </div>
          );
        }

        // 文件类型使用表格形式展现
        const columnList = [...columns, actionColumn];
        return (
          <table
            class={['file-table', isFileDragOver.value ? 'is-file-dragover' : '']}
            ref={listRef}
            onDragover={onContainerDragOver}
            onDragleave={onContainerDragLeave}
            onDrop={onContainerDrop}
          >
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
                  <tr
                    class={['file-table-tr', dragOverIndex.value === index && props.dragSort ? 'is-dragover' : '']}
                    key={record.id || index}
                    data-idx={index}
                    draggable={props.dragSort}
                    onDragstart={(e: DragEvent) => onDragStart(e, index)}
                    onDragover={(e: DragEvent) => onDragOver(e, index)}
                    onDrop={(e: DragEvent) => onDrop(e, index)}
                    onDragend={onDragEnd}
                  >
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
                    <div class="file-table-empty">{props.emptyText}</div>
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
    border: 1px solid @table-border-color;
    border-collapse: separate;
    border-spacing: 0;
    border-radius: 8px;
    overflow: hidden;

    &.is-file-dragover {
      border: 2px dashed @primary-color;
      border-radius: 8px;
      box-sizing: border-box;
    }

    &-th,
    &-td {
      //border-right: 1px solid @table-border-color;
      border-bottom: 1px solid @table-border-color;
      line-height: 14px;
      padding: 4px;
      overflow-wrap: break-word;
      word-break: break-all;
      white-space: normal;
      min-width: 100px;
    }

    &-th {
      padding: 12px;
      font-weight: normal;
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

    &-tr:nth-of-type(even) {
      background: @background-color-light;
    }

    &-tr.is-dragover td {
      background-color: fade(@primary-color, 12%);
    }

    // 避免圆角处边框重复/缺失：最右列与 tbody 最后一行分别去掉对应边，保留 thead 底部分隔线
    tr > &-th:last-child,
    tr > &-td:last-child {
      border-right: none;
    }

    tbody > tr:last-child > &-td {
      border-bottom: none;
    }

    &-empty {
      padding: 8px;
      text-align: center;
      color: @text-color-secondary;
      border-radius: 8px;
    }
  }

  .file-grid {
    display: grid;
    grid-template-columns: repeat(auto-fill, minmax(150px, 1fr));
    gap: 8px;

    &.is-file-dragover {
      border: 2px dashed @primary-color;
      border-radius: 8px;
      box-sizing: border-box;
    }

    &-item {
      display: flex;
      flex-direction: column;
      padding: 6px;
      overflow: hidden;
      border: 1px solid @table-border-color;
      border-radius: 8px;
      cursor: default;

      &__thumb {
        position: relative;
        display: flex;
        align-items: center;
        justify-content: center;
        height: 90px;
        overflow: hidden;
        background-color: @background-color-light;
        border-radius: 6px;
      }

      // 图片网格模式下，上传进度/状态文字浮动叠加在缩略图底部，
      // 使用半透明遮罩确保在不同底色的缩略图上始终可读。
      &__status {
        position: absolute;
        left: 0;
        right: 0;
        bottom: 0;
        padding: 0 4px;
        pointer-events: none;
        background-color: rgb(0 0 0 / 55%);
        text-align: center;
        color: #fff;
        border-radius: 0 0 6px 6px;
        font-size: 12px;

        .ant-progress {
          margin: 0;
          line-height: 1;

          .ant-progress-text,
          .ant-progress-indicator {
            color: #fff !important;
          }
        }
      }

      &__action {
        display: flex;
        justify-content: center;
        margin-top: 2px;
      }

      // 拖拽排序相关
      &[draggable='true'] {
        cursor: grab;
      }

      &.is-dragover {
        border-color: @primary-color;
        box-shadow: 0 0 0 2px fade(@primary-color, 20%);
      }
    }

    &-empty {
      grid-column: 1 / -1;
      padding: 8px;
      text-align: center;
      color: @text-color-secondary;
      border: 1px solid @table-border-color;
      border-radius: 8px;
    }
  }

  // 图片网格模式下隐藏文件名文本（仅用于 title 提示）
  .file-grid-name-hidden {
    display: none;
  }

  html[data-theme='dark'] {
    .file-table {
      thead {
        background-color: #1a1a1a;
      }

      &-tr:nth-of-type(even) {
        background: #1a1a1a;
      }
    }

    .file-grid {
      &-item__thumb {
        background-color: #1a1a1a;
      }

      // 暗黑模式：上传状态遮罩反色，保证在深色缩略图上的可读性
      &-item__status {
        background-color: rgb(255 255 255 / 65%);
        color: #000;

        .ant-progress {
          .ant-progress-text,
          .ant-progress-indicator {
            color: #000 !important;
          }
        }
      }
    }
  }
</style>
