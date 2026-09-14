import type { BasicColumn, ActionItem } from '@jeesite/core/components/Table';
import { FileItem, UploadResultStatus } from './typing';
import { formatSize, isImgTypeByName, getFileUploadId } from './helper';
import { Avatar, Progress, Tag } from 'antdv-next';
import { Icon } from '@jeesite/core/components/Icon';
import TableAction from '@jeesite/core/components/Table/src/components/TableAction.vue';
import ThumbUrl from './ThumbUrl.vue';
import { useI18n } from '@jeesite/core/hooks/web/useI18n';
import { useGlobSetting } from '@jeesite/core/hooks/setting';

const { ctxPath } = useGlobSetting();

/**
 * 列表展现形式：图片类型使用网格，其它类型（文件）使用表格
 * @author ThinkGem
 */
export function getListType(props: any): 'grid' | 'table' {
  return props.uploadType === 'image' ? 'grid' : 'table';
}

/**
 * 图标或缩略图的最大宽高（第一列）
 * @author ThinkGem
 */
export function getThumbSize(props: any): { width: number; height: number } {
  return getListType(props) === 'grid' ? { width: 100, height: 100 } : { width: 32, height: 32 };
}

// 上传预览列表（已上传的文件和本次上传的文件合并显示）
export function createTableColumns(props: any): BasicColumn[] {
  const { t } = useI18n();
  const isImage = props.uploadType === 'image';
  const { width: thumbWidth, height: thumbHeight } = getThumbSize(props);
  return [
    {
      dataIndex: 'fileUrl',
      title: isImage ? t('component.upload.image') : t('component.upload.legend'),
      width: isImage ? 200 : 100,
      customRender: ({ record, index }) => {
        const { fileUrl, type, fileEntity, fileUpload } = (record as FileItem) || {};
        let url = fileUrl || '';
        let previewUrl;
        if (!url.startsWith('data:image/') && url.indexOf('://') == -1) {
          url = ctxPath + url;
          // 已上传的图片，如果开启了缩略图，则使用缩略图显示 v5.4.2
          if (fileUpload && props.imageThumbName) {
            previewUrl = url;
            if (url.indexOf('?') == -1) {
              url += '.' + props.imageThumbName;
            } else {
              url = url.replace('?', '.' + props.imageThumbName + '?');
            }
          }
        }
        if (isImgTypeByName(url)) {
          return <ThumbUrl fileUrl={url} previewUrl={previewUrl} width={thumbWidth} height={thumbHeight} />;
        }
        const ext = type || fileEntity?.fileExtension || <Icon icon="i-ant-design:file-outlined" />;
        const color = ['#f56a00', '#7265e6', '#ffbf00', '#00a2ae'][index % 4];
        return <Avatar style={{ backgroundColor: color, verticalAlign: 'middle' }}>{ext}</Avatar>;
      },
    },
    {
      dataIndex: 'fileName',
      title: t('component.upload.fileName'),
      align: 'left',
      customRender: ({ text, record }) => {
        const { percent, status: uploadStatus, newUpload, responseData } = (record as FileItem) || {};
        // 图片网格模式下，文件名隐藏（仅作为 title 在鼠标悬停时显示），保留上传进度
        const isImage = props.uploadType === 'image';
        const nameNode = isImage ? (
          <span class="file-grid-name-hidden" title={text}>
            {text}
          </span>
        ) : (
          <p class="mb-0" title={text}>
            {text}
          </p>
        );
        // 从数据库回显的文件（newUpload 不为 true）不显示上传进度
        if (!newUpload) {
          return nameNode;
        }
        let status: 'normal' | 'exception' | 'active' | 'success' = 'normal';
        if (uploadStatus === UploadResultStatus.ERROR) {
          status = 'exception';
        } else if (uploadStatus === UploadResultStatus.UPLOADING) {
          status = 'active';
        } else if (uploadStatus === UploadResultStatus.SUCCESS) {
          status = 'success';
        }
        // 图片网格模式：上传成功后显示后端返回的结果文字
        if (isImage && uploadStatus === UploadResultStatus.SUCCESS) {
          return <span>{responseData?.message || ''}</span>;
        }
        return (
          <span>
            {nameNode}
            <Progress percent={percent} size="small" status={status} />
          </span>
        );
      },
    },
    {
      dataIndex: 'size',
      title: t('component.upload.fileSize'),
      width: 100,
      customRender: ({ text = 0 }) => {
        return text && formatSize(text);
      },
    },
    {
      dataIndex: 'status',
      title: t('component.upload.createDate') + '/' + t('component.upload.fileStatue'),
      width: 140,
      align: 'center',
      customRender: ({ text, record }) => {
        const { responseData, createDate, newUpload } = (record as FileItem) || {};
        if (!newUpload) {
          return <span class="truncate">{createDate || ''}</span>;
        }
        if (text === UploadResultStatus.SUCCESS) {
          return <Tag color="green">{() => responseData?.message || t('component.upload.uploadSuccess')}</Tag>;
        } else if (text === UploadResultStatus.ERROR) {
          return <Tag color="red">{() => responseData?.message || t('component.upload.uploadError')}</Tag>;
        } else if (text === UploadResultStatus.UPLOADING) {
          return <Tag color="blue">{() => responseData?.message || t('component.upload.uploading')}</Tag>;
        }
        return <Tag>{() => t('component.upload.waitUpload')}</Tag>;
      },
    },
  ];
}

export function createActionColumn(
  {
    handleRemove,
    handleDownload,
  }: {
    handleRemove: Fn;
    handleDownload: Fn;
  },
  readonly = false,
): BasicColumn {
  const { t } = useI18n();
  return {
    width: readonly ? 100 : 140,
    title: t('component.upload.operating'),
    dataIndex: 'actions',
    align: 'center',
    fixed: false,
    customRender: ({ record }) => {
      const actions: ActionItem[] = [];
      if (!readonly) {
        actions.push({
          label: t('component.upload.del'),
          color: 'error',
          popConfirm: {
            title: t('component.upload.delConfirm'),
            confirm: handleRemove.bind(null, record),
          },
        });
      }
      // 已上传服务端的文件，才可以预览和下载
      if (getFileUploadId(record)) {
        actions.unshift({
          label: t('component.upload.download'),
          onClick: handleDownload.bind(null, record),
        });
      }
      return <TableAction actions={actions} outside={true} />;
    },
  };
}
