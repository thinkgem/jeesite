import { Tag } from 'ant-design-vue';
import { BasicColumn } from '/@/components';
import { ErrorTypeEnum } from '/@/enums/exceptionEnum';
import { useI18n } from '/@/hooks/web/useI18n';

const { t } = useI18n();

export function getColumns(): BasicColumn[] {
  return [
    {
      dataIndex: 'type',
      title: t('sys.errorLog.tableColumnType'),
      width: 80,
      customRender: ({ text }) => {
        const color =
          text === ErrorTypeEnum.VUE
            ? 'green'
            : text === ErrorTypeEnum.RESOURCE
              ? 'cyan'
              : text === ErrorTypeEnum.PROMISE
                ? 'blue'
                : ErrorTypeEnum.AJAX
                  ? 'red'
                  : 'purple';
        return <Tag color={color}>{() => text}</Tag>;
      },
    },
    {
      dataIndex: 'url',
      title: t('地址'),
      width: 200,
    },
    {
      dataIndex: 'time',
      title: t('sys.errorLog.tableColumnDate'),
      width: 160,
    },
    {
      dataIndex: 'file',
      title: t('sys.errorLog.tableColumnFile'),
      width: 200,
    },
    {
      dataIndex: 'name',
      title: t('名称'),
      width: 200,
    },
    {
      dataIndex: 'message',
      title: t('sys.errorLog.tableColumnMsg'),
      width: 300,
    },
    {
      dataIndex: 'stack',
      title: t('sys.errorLog.tableColumnStackMsg'),
    },
  ];
}

export function getDescSchema(): any {
  return getColumns().map((column) => {
    return {
      field: column.dataIndex!,
      label: column.title,
    };
  });
}
