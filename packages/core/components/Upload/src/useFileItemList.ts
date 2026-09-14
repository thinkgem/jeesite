/**
 * Copyright (c) 2013-Now https://jeesite.com All rights reserved.
 * No deletion without permission, or be held responsible to law.
 * @author ThinkGem
 */
import { ref, unref, computed, watch } from 'vue';
import { useUpload } from './useUpload';
import { useMessage } from '@jeesite/core/hooks/web/useMessage';
import { FileItem, UploadResultStatus } from './typing';
import { createTableColumns, createActionColumn, getListType } from './data';
import { checkImgType, getBase64WithFile, getFileUploadId, formatSize } from './helper';
import { buildUUID } from '@jeesite/core/utils/uuid';
import { isFunction, isArray } from '@jeesite/core/utils/is';
import { warn } from '@jeesite/core/utils/log';
import { useI18n } from '@jeesite/core/hooks/web/useI18n';
import { FileUpload } from '@jeesite/core/api/sys/upload';
import { downloadByUrl } from '@jeesite/core/utils/file/download';

/**
 * 上传/预览文件列表的公共逻辑（弹窗与内嵌共用）
 * @param props uploadProps（弹窗与内嵌共用）
 * @param emit change/delete 事件
 * @param options.embed 是否内嵌模式：内嵌无上传工具栏，删除/排序后立即提交，不弹保存按钮
 * @author ThinkGem
 */
export function useFileItemList(props: any, emit: (event: any, ...args: any[]) => void, options?: { embed?: boolean }) {
  const embed = options?.embed ?? false;
  const { t } = useI18n();
  const fileItemList = ref<FileItem[]>([]);
  const uploading = ref(false);
  // 列表顺序是否发生变化（拖拽排序后变为 true，用于启用确定按钮并保存顺序）
  const reordered = ref(false);
  // 弹窗模式下待删除的文件（点击确定后才真正提交删除）
  const deletedFileList = ref<FileUpload[]>([]);

  const { getStringAccept, getHelpText, getMaxFileSize, getUploadParams } = useUpload(props);

  const { createMessage } = useMessage();

  // 已上传文件列表（弹窗用 previewFileList，内嵌用 value）
  function getPreviewList(): FileUpload[] {
    const list = props.previewFileList || props.value;
    return (isArray(list) ? list : []) as FileUpload[];
  }

  // 获取服务端已上传的文件列表
  function getPreviewFileItems(): FileItem[] {
    // if (!props.showPreview) return [];
    return getPreviewList().map((fileUpload) => toFileItem(fileUpload));
  }

  // 已上传的文件转换为文件列表项，与本次上传的文件合并显示
  function toFileItem(fileUpload: FileUpload): FileItem {
    const fileEntity = (fileUpload.fileEntity || {}) as any;
    return {
      id: fileUpload.id,
      file: undefined as any,
      size: fileEntity.fileSize || 0,
      type: fileEntity.fileExtension || '',
      percent: 100,
      status: UploadResultStatus.SUCCESS,
      fileName: fileUpload.fileName,
      fileUrl: fileUpload.fileUrl || '',
      fileMd5: '',
      fileUploadId: fileUpload.id,
      fileEntityId: fileEntity.id || '',
      bizKey: props.bizKey as string,
      bizType: props.bizType,
      uploadType: props.uploadType,
      fileEntity: fileUpload.fileEntity,
      createDate: fileUpload.createDate,
      fileUpload,
      // 保留本次会话新上传标记与响应数据（uploadApiByItem 成功后写入 fileUpload）
      newUpload: (fileUpload as any).newUpload,
      responseData: (fileUpload as any).responseData,
    } as FileItem;
  }

  // 打开弹窗时，加载已上传的文件
  function initFileItemList() {
    fileItemList.value = getPreviewFileItems();
    reordered.value = false;
    deletedFileList.value = [];
  }

  // 已上传的文件发生变化时，同步到列表
  watch(
    () => [props.previewFileList, props.value],
    () => {
      const previewItems = getPreviewFileItems();
      if (embed) {
        // 内嵌模式：与 value 保持一致，但需保留尚未提交（仍在进行上传）的本地文件，
        // 避免任一文件上传完成触发 commitChange 后，回显列表覆盖了仍在进行中的其它文件，
        // 导致一次性选择多个文件上传时只显示一条。
        const committedIds = new Set(previewItems.map((item) => item.id));
        const pending = fileItemList.value.filter(
          (item) => !!item.file && !item.fileUpload && !committedIds.has(item.id),
        );
        fileItemList.value = [...previewItems, ...pending];
        reordered.value = false;
      } else if (fileItemList.value.length === 0) {
        // 弹窗模式：仅在列表为空时初始化，避免覆盖本次已选择/已上传的本地文件
        fileItemList.value = getPreviewFileItems();
      }
    },
  );

  const getIsSelectFile = computed(() => {
    const items = fileItemList.value.filter((item) => item.newUpload);
    return items.length > 0 && !items.every((item) => item.status === UploadResultStatus.SUCCESS);
  });

  const getOkButtonProps = computed(() => {
    const someSuccess = fileItemList.value.some((item) => item.newUpload && item.status === UploadResultStatus.SUCCESS);
    // 存在新上传成功的文件，或列表顺序被拖拽调整后，均可保存
    return {
      disabled: uploading.value || (!someSuccess && !reordered.value),
    };
  });

  const getUploadBtnText = computed(() => {
    const someError = fileItemList.value.some((item) => item.status === UploadResultStatus.ERROR);
    return uploading.value
      ? t('component.upload.uploading')
      : someError
        ? t('component.upload.reUploadFailed')
        : t('component.upload.startUpload');
  });

  // 上传前校验
  function beforeUpload(file: File) {
    const { size, name } = file;
    const { bizKey, bizType, uploadType } = props;
    // 设置最大值，则判断
    if (file.size >= unref(getMaxFileSize)) {
      createMessage.error(t('component.upload.maxSizeMultiple', [unref(getMaxFileSize) / 1024 / 1024]));
      return false;
    }
    const id = buildUUID();
    const commonItem = {
      id,
      file,
      size,
      name,
      percent: 0,
      type: name.split('.').pop(),
      fileMd5: id, // 专业版支持 MD5 校验（秒传）
      fileName: name,
      fileUploadId: '',
      fileEntityId: '',
      bizKey,
      bizType,
      uploadType,
      newUpload: true,
    } as FileItem;
    function addFileItem() {
      // 生成图片缩略图
      if (checkImgType(file)) {
        getBase64WithFile(file).then(({ result: fileUrl }) => {
          addFileItemList({
            fileUrl,
            ...commonItem,
          });
        });
      } else {
        addFileItemList(commonItem);
      }
    }
    addFileItem();
    return false;
  }

  function addFileItemList(record: FileItem) {
    const { maxNumber } = props;
    if (fileItemList.value.length >= maxNumber) {
      createMessage.warning(t('component.upload.maxNumber', [maxNumber]));
      return;
    }
    fileItemList.value = [...unref(fileItemList), record];
    if (!props.isLazy) {
      uploadApiByItem(fileItemList.value[fileItemList.value.length - 1]);
    }
  }

  // 删除（已上传的文件或本次上传的文件）
  function handleRemove(record: FileItem) {
    const index = fileItemList.value.findIndex((item) => item.id === record.id);
    if (index !== -1) {
      const removed = fileItemList.value.splice(index, 1);
      const item = removed[0] as FileItem;
      if (item) {
        const fileUpload = item.fileUpload || item.responseData?.fileUpload;
        if (fileUpload) {
          if (embed) {
            // 内嵌模式：删除后立即提交
            emit('delete', fileUpload);
          } else {
            // 弹窗模式：先记录待删除，点击确定后再提交
            deletedFileList.value.push(fileUpload);
          }
        }
      }
      // 内嵌模式：删除后立即提交
      if (embed) {
        commitChange();
      }
    }
  }

  // 拖拽排序
  function handleReorder({ from, to }: { from: number; to: number }) {
    const list = [...unref(fileItemList)];
    if (from < 0 || from >= list.length || to < 0 || to >= list.length) return;
    const [moved] = list.splice(from, 1);
    list.splice(to, 0, moved);
    fileItemList.value = list;
    reordered.value = true;
    // 内嵌模式：排序后立即提交
    if (embed) {
      commitChange();
    }
  }

  // 下载文件
  async function handleDownload(record: FileItem) {
    const fileUploadId = getFileUploadId(record);
    if (fileUploadId) {
      await downloadByUrl({ url: props.apiDownloadUrl + '/' + fileUploadId, fileName: record.fileName });
    }
  }

  // 开始上传文件
  async function uploadApiByItem(item: FileItem) {
    const { api } = props;
    if (!api || !isFunction(api)) {
      return warn('upload api must exist and be a function');
    }

    try {
      item.status = UploadResultStatus.UPLOADING;
      if (item.percent != 100) {
        await uploadFileByWhole(item);
      }
      item.percent = 100;
      item.status = UploadResultStatus.SUCCESS;
      // 上传成功后填充服务端文件信息，使本地文件与回显数据保持一致
      // newUpload / responseData 写入 fileUpload，保证 watch 重建时仍能识别为本次新上传并显示结果
      const fileUpload = item.responseData?.fileUpload;
      if (fileUpload) {
        (fileUpload as any).newUpload = true;
        (fileUpload as any).responseData = item.responseData;
        item.fileUpload = fileUpload;
        item.fileUploadId = fileUpload.id;
        item.createDate = fileUpload.createDate;
        item.fileUrl = fileUpload.fileUrl || item.fileUrl;
        item.fileEntity = fileUpload.fileEntity || item.fileEntity;
      }
      return {
        success: true,
        error: null,
      };
    } catch (e) {
      console.log(e);
      item.status = UploadResultStatus.ERROR;
      return {
        success: false,
        error: e,
      };
    } finally {
      // 内嵌模式：上传完成后立即同步；newUpload 标志在 beforeUpload 时已设置，
      // 且 watch 会保留 newUpload=true 的本地文件，避免被回显版本替换后丢失「新上传」状态
      if (embed) {
        commitChange();
      }
    }
  }

  // 整体上传文件
  async function uploadFileByWhole(item: FileItem) {
    const { api } = props;
    try {
      const params = unref(getUploadParams);
      const fileSize = item.file.size;
      const { data } = await api(
        {
          bizKey: item.bizKey,
          bizType: item.bizType,
          uploadType: item.uploadType,
          fileMd5: item.fileMd5,
          fileName: item.fileName,
          fileUploadId: item.fileUploadId,
          fileEntityId: item.fileEntityId,
          imageMaxWidth: params.imageMaxWidth || '',
          imageMaxHeight: params.imageMaxHeight || '',
          ...(props.uploadParams || {}),
          file: item.file,
        },
        (progressEvent: ProgressEvent) => {
          // 当并发上传多个文件时，progressEvent.total 可能是所有文件的总大小，
          // 导致只有最后一个文件显示 100% 进度。使用文件自身大小作为 total 参考值。
          const total = progressEvent.total > fileSize ? fileSize : progressEvent.total;
          item.percent = total > 0 ? ((progressEvent.loaded / total) * 100) | 0 : 0;
        },
        props.apiUploadUrl,
      );
      item.responseData = data;
    } catch (e) {
      throw e;
    }
  }

  // 点击开始上传
  async function handleStartUpload() {
    if (uploading.value) return;
    uploading.value = true;
    try {
      // 只上传不是成功状态的（已上传的文件不重复上传）
      const uploadFileList =
        fileItemList.value.filter((item) => !item.fileUpload && item.status !== UploadResultStatus.SUCCESS) || [];
      const data = await Promise.all(
        uploadFileList.map((item) => {
          return uploadApiByItem(item);
        }),
      );
      // 生产环境:抛出错误
      const errorList = data.filter((item: any) => item.result === 'false');
      if (errorList.length > 0) throw errorList;
    } finally {
      uploading.value = false;
    }
  }

  // 收集可保存的文件列表（已上传文件保持顺序，含拖拽排序后的顺序；本次上传成功文件追加）
  function getSaveFileList(): FileUpload[] {
    const fileList: FileUpload[] = [];
    for (const item of fileItemList.value) {
      const { status, responseData, fileUpload } = item;
      if (fileUpload) {
        fileList.push(fileUpload);
      } else if (status === UploadResultStatus.SUCCESS && responseData) {
        fileList.push(responseData.fileUpload);
      }
    }
    return fileList;
  }

  // 内嵌模式：删除/排序/上传完成后立即同步当前列表
  function commitChange() {
    emit('change', getSaveFileList());
  }

  // 点击保存（弹窗模式）：校验通过返回可保存列表，由外壳负责关闭弹窗并触发 change
  function handleOk(): FileUpload[] | null {
    const { maxNumber } = props;
    if (fileItemList.value.length > maxNumber) {
      createMessage.warning(t('component.upload.maxNumber', [maxNumber]));
      return null;
    }
    if (uploading.value) {
      createMessage.warning(t('component.upload.saveWarn'));
      return null;
    }
    const fileList = getSaveFileList();
    if (fileList.length <= 0) {
      createMessage.warning(t('component.upload.saveError'));
      return null;
    }
    // 弹窗模式：提交本次待删除的文件
    for (const fileUpload of deletedFileList.value) {
      emit('delete', fileUpload);
    }
    deletedFileList.value = [];
    return fileList;
  }

  // 点击关闭：则所有操作不保存，包括上传的
  async function handleCloseFunc() {
    if (uploading.value) {
      createMessage.warning(t('component.upload.uploadWait'));
      return false;
    }
    // 清空待删除记录，避免取消后误删
    deletedFileList.value = [];
    // 不在此处清空列表，避免关闭动画过程中内容塌陷闪现小窗口，列表在下次打开时重新初始化
    return true;
  }

  const columns = createTableColumns(props) as any[];

  // 图片类型使用网格形式展现，其它类型使用表格形式展现
  const listType = computed(() => getListType(props));

  const actionColumn = computed(
    () =>
      createActionColumn(
        {
          handleRemove,
          handleDownload,
        },
        props.readonly,
      ) as any,
  );

  // 空列表提示文案：图片类型提示「还没有上传图片」，其它类型提示「还没有上传文件」
  const getEmptyText = computed(() =>
    props.uploadType === 'image' ? t('component.upload.imageListEmpty') : t('component.upload.fileListEmpty'),
  );

  // 统计信息：总共 x 个文件（xx MB/GB/TB）
  const getStatisticsText = computed(() => {
    const count = fileItemList.value.length;
    const totalSize = fileItemList.value.reduce((sum, item) => sum + (Number(item.size) || 0), 0);
    return `共 ${count} 个文件（${formatSize(totalSize) || '0B'}）`;
  });

  // 提示文字：统计信息 + 帮助文案（没有文件时不显示统计信息）
  const getAlertTitle = computed(() => {
    const helpText = unref(getHelpText);
    const statisticsText = props.showPreviewNumber && fileItemList.value.length > 0 ? unref(getStatisticsText) : '';
    if (!statisticsText) return helpText;
    return helpText ? `${statisticsText}，${helpText}` : statisticsText;
  });

  return {
    fileItemList,
    uploading,
    columns,
    listType,
    actionColumn,
    getEmptyText,
    getIsSelectFile,
    getOkButtonProps,
    getUploadBtnText,
    getStringAccept,
    getStatisticsText,
    getAlertTitle,
    initFileItemList,
    beforeUpload,
    handleReorder,
    handleStartUpload,
    handleOk,
    handleCloseFunc,
  };
}
