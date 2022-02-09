import type { BasicColumn, ActionItem } from '/@/components/Table';
import { FileItem, UploadResultStatus } from './typing';
import { formatSize, isImgTypeByName } from './helper';
import { Avatar, Progress, Tag } from 'ant-design-vue';
import { FileOutlined } from '@ant-design/icons-vue';
import TableAction from '/@/components/Table/src/components/TableAction.vue';
import ThumbUrl from './ThumbUrl.vue';
import { useI18n } from '/@/hooks/web/useI18n';
import { FileUpload } from '../../../api/sys/upload';
import { useGlobSetting } from '/@/hooks/setting';

const { t } = useI18n();
const { apiUrl } = useGlobSetting();

// 文件上传列表
export function createTableColumns(): BasicColumn[] {
  return [
    {
      dataIndex: 'fileUrl',
      title: t('component.upload.legend'),
      width: 100,
      customRender: ({ record, index }) => {
        const { fileUrl, type, fileEntity } = (record as FileUpload) || {};
        let url = fileUrl || '';
        if (!url.startsWith('data:image/') && url.indexOf('://') == -1) {
          url = apiUrl + url;
        }
        if (isImgTypeByName(url)) {
          return <ThumbUrl fileUrl={url} />;
        }
        const ext = type || fileEntity?.fileExtension || <FileOutlined />
        const color = ['#f56a00', '#7265e6', '#ffbf00', '#00a2ae'][index % 4];
        return <Avatar style={{ backgroundColor: color, verticalAlign: 'middle' }}>{ext}</Avatar>;
      },
    },
    {
      dataIndex: 'fileName',
      title: t('component.upload.fileName'),
      align: 'left',
      customRender: ({ text, record }) => {
        const { percent, status: uploadStatus } = (record as FileItem) || {};
        let status: 'normal' | 'exception' | 'active' | 'success' = 'normal';
        if (uploadStatus === UploadResultStatus.ERROR) {
          status = 'exception';
        } else if (uploadStatus === UploadResultStatus.UPLOADING) {
          status = 'active';
        } else if (uploadStatus === UploadResultStatus.SUCCESS) {
          status = 'success';
        }
        return (
          <span>
            <p class="truncate mb-1" title={text}>
              {text}
            </p>
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
      title: t('component.upload.fileStatue'),
      width: 100,
      customRender: ({ text, record }) => {
        const { responseData } = (record as FileItem) || {};
        if (text === UploadResultStatus.SUCCESS) {
          return <Tag color="green">{() => responseData?.message || t('component.upload.uploadSuccess')}</Tag>;
        } else if (text === UploadResultStatus.ERROR) {
          return <Tag color="red">{() => responseData?.message || t('component.upload.uploadError')}</Tag>;
        } else if (text === UploadResultStatus.UPLOADING) {
          return <Tag color="blue">{() => responseData?.message || t('component.upload.uploading')}</Tag>;
        }
        return text;
      },
    },
  ];
}
export function createActionColumn(handleRemove: Function): BasicColumn {
  return {
    width: 120,
    title: t('component.upload.operating'),
    dataIndex: 'actions',
    align: 'center',
    fixed: false,
    customRender: ({ record }) => {
      const actions: ActionItem[] = [
        {
          label: t('component.upload.del'),
          color: 'error',
          popConfirm: {
            title: t('component.upload.delConfirm'),
            confirm: handleRemove.bind(null, record),
          },
        },
      ];
      // if (checkImgType(record)) {
      //   actions.unshift({
      //     label: t('component.upload.preview'),
      //     onClick: handlePreview.bind(null, record),
      //   });
      // }
      return <TableAction actions={actions} outside={true} />;
    },
  };
}
// 文件预览列表
export function createPreviewColumns(): BasicColumn[] {
  return [
    {
      dataIndex: 'fileUrl',
      title: t('component.upload.legend'),
      width: 100,
      customRender: ({ record, index }) => {
        const { fileUrl, type, fileEntity } = (record as FileUpload) || {};
        let url = fileUrl || '';
        if (!url.startsWith('data:image/') && url.indexOf('://') == -1) {
          url = apiUrl + url;
        }
        if (isImgTypeByName(url)) {
          return <ThumbUrl fileUrl={url} />;
        }
        const ext = type || fileEntity?.fileExtension || <FileOutlined />
        const color = ['#f56a00', '#7265e6', '#ffbf00', '#00a2ae'][index % 4];
        return <Avatar style={{ backgroundColor: color, verticalAlign: 'middle' }}>{ext}</Avatar>;
      },
    },
    {
      dataIndex: 'fileName',
      title: t('component.upload.fileName'),
      align: 'left',
    },
    {
      dataIndex: 'fileEntity.fileSize',
      title: t('component.upload.fileSize'),
      width: 100,
      customRender: ({ text = 0 }) => {
        return text && formatSize(text);
      },
    },
    {
      title: t('上传时间'),
      dataIndex: 'createDate',
      width: 130,
      align: 'center',
    },
  ];
}

export function createPreviewActionColumn({
  handleRemove,
  handleDownload,
}: {
  handleRemove: Fn;
  handleDownload: Fn;
}): BasicColumn {
  return {
    width: 160,
    title: t('component.upload.operating'),
    dataIndex: 'actions',
    align: 'center',
    fixed: false,
    customRender: ({ record }) => {
      const actions: ActionItem[] = [
        {
          label: t('component.upload.del'),
          color: 'error',
          popConfirm: {
            title: t('component.upload.delConfirm'),
            confirm: handleRemove.bind(null, record),
          },
        },
        {
          label: t('component.upload.download'),
          onClick: handleDownload.bind(null, record),
        },
      ];
      // if (checkImgType(record)) {
      //   actions.unshift({
      //     label: t('component.upload.preview'),
      //     onClick: handlePreview.bind(null, record),
      //   });
      // }
      return <TableAction actions={actions} outside={true} />;
    },
  };
}
