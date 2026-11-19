<template>
  <div class="jeesite-basic-upload">
    <Space>
      <a-button
        v-if="!props.showPreviewList && !props.readonly"
        :type="getButtonType"
        :disabled="props.disabled"
        :size="props.size"
        @click="openUploadModal"
      >
        <Icon :icon="getButtonIcon" />
        <span v-if="showUploadText" class="pl-1">{{ getButtonText }}</span>
      </a-button>
      <Tooltip placement="bottom" v-if="!props.showPreviewList && getShowNumber">
        <template #title>
          {{ t('component.upload.uploaded') }}
          <template v-if="props.showPreviewNumber">
            <span class="ml-1">{{ fileList.length }}</span>
          </template>
        </template>
        <a-button @click="openUploadModal" :size="props.size">
          <Icon icon="i-bi:eye" />
          <template v-if="props.showPreviewNumber">
            <span class="ml-1">{{ fileList.length }}</span>
          </template>
        </a-button>
      </Tooltip>
    </Space>
    <UploadModal
      v-bind="bindValue"
      :previewFileList="fileList"
      :apiUploadUrl="apiUploadUrl"
      :apiDownloadUrl="apiDownloadUrl"
      :readonly="previewOnly"
      @register="registerUploadModal"
      @change="handleChange"
      @delete="handleDelete"
    />
    <!-- 内嵌预览列表：点击该区域可聚焦，作为 Ctrl+V 粘贴上传的目标（多上传组件时只有聚焦的生效） -->
    <div
      v-if="props.showPreviewList"
      ref="pasteAreaRef"
      class="jeesite-basic-upload-paste"
      tabindex="-1"
      :class="{ 'is-disabled': previewOnly }"
      @click="handlePasteAreaClick"
      @paste="handlePaste"
    >
      <UploadPreview
        ref="previewRef"
        v-bind="bindValue"
        :previewFileList="fileList"
        :readonly="previewOnly"
        :imageThumbName="imageThumbName"
        :apiDownloadUrl="apiDownloadUrl"
        @change="handlePreviewChange"
        @delete="handleDelete"
      />
    </div>
  </div>
</template>
<script lang="ts" setup name="BasicUpload">
  import { ref, watch, computed, toValue, useAttrs } from 'vue';
  import { useEventListener } from '@vueuse/core';
  import { Tooltip, Space } from 'antdv-next';
  import { Icon } from '@jeesite/core/components/Icon';
  import { useModal } from '@jeesite/core/components/Modal';
  import { uploadContainerProps } from './props';
  import { omit } from 'lodash-es';
  import { useI18n } from '@jeesite/core/hooks/web/useI18n';
  import { useMessage } from '@jeesite/core/hooks/web/useMessage';
  import { isArray } from '@jeesite/core/utils/is';
  import { FileUpload, uploadFileList } from '@jeesite/core/api/sys/upload';
  import UploadModal from './UploadModal.vue';
  import UploadPreview from './UploadPreview.vue';
  import {
    getClipboardImageFiles,
    isEditableElement,
    onDocumentPaste,
    uploadFiles as uploadFilesAction,
  } from './usePasteUpload';

  const props = defineProps(uploadContainerProps);
  const emit = defineEmits(['change', 'delete', 'update:value', 'click']);
  const attrs = useAttrs();

  const { t } = useI18n();
  const { showMessage } = useMessage();
  const [registerUploadModal, { openModal }] = useModal();

  function openUploadModal() {
    openModal(true, { loadTime: new Date().getTime() });
    emit('click');
  }

  const dataMap = ref<object>({});
  const fileList = ref<FileUpload[]>([]);
  const fileListDel = ref<FileUpload[]>([]);
  // 内部上传（粘贴上传等）后自增，触发上传弹窗内的文件列表刷新
  const innerLoadTime = ref(0);

  // 业务主键：表单场景可能传入 computed/ref，统一解包为字符串，避免上传关联到错误主键
  const bizKeyValue = computed(() => {
    const value = toValue(props.bizKey as any);
    return value == undefined || value === '' ? '' : String(value);
  });

  // 刷新时间戳：同样兼容 computed/ref
  const loadTimeValue = computed(() => {
    const value = Number(toValue(props.loadTime as any));
    return isNaN(value) ? 0 : value;
  });

  // 只读或禁用时，只能预览，不能上传
  const previewOnly = computed(() => props.readonly || props.disabled);

  // 只读时，上传按钮显示为预览样式
  const getButtonIcon = computed(() => (props.readonly ? 'i-bi:eye' : 'i-carbon:cloud-upload'));

  const getButtonType = computed(() => (props.readonly ? 'default' : props.uploadButtonType));

  const getButtonText = computed(() => {
    if (props.uploadText) return props.uploadText;
    return props.readonly ? t('component.upload.view') : t('component.upload.upload');
  });

  // 上传按钮后面显示上传个数
  const getShowNumber = computed(() => {
    const { showPreview, showPreviewList, showPreviewNumber } = props;
    if (showPreviewList && !showPreviewNumber) return false;
    return showPreview && fileList.value.length;
  });

  const bindValue = computed(() => {
    const value = { ...attrs, ...props, loadTime: loadTimeValue.value + innerLoadTime.value };
    return omit(value, 'onChange', 'class');
  });

  watch(
    () => props.value,
    (value) => {
      dataMap.value = value || {};
      emit('update:value', dataMap.value);
    },
    { immediate: true },
  );

  watch(
    () => [bizKeyValue.value, loadTimeValue.value],
    () => {
      // 切换记录或外部刷新时先清空，避免短暂显示上一条记录的文件
      fileList.value = [];
      loadFileList();
    },
    { immediate: true },
  );

  /**
   * 从服务端加载文件列表（以服务端数据为准）
   */
  async function loadFileList() {
    fileListDel.value = [];
    const bizKey = bizKeyValue.value;
    if (!bizKey) {
      fileList.value = [];
      return;
    }
    try {
      const res: any = await uploadFileList(
        {
          bizKey,
          bizType: props.bizType,
        },
        props.apiFileListUrl,
      );
      // 接口返回 JSON 字符串时先解析
      let list = res;
      if (typeof list == 'string') {
        try {
          list = JSON.parse(list);
        } catch {
          list = undefined;
        }
      }
      // 接口未返回数组时保留当前列表，避免把刚上传成功的文件清空
      if (isArray(list)) {
        fileList.value = (list as FileUpload[]).filter((item) => !!item);
      }
    } catch {
      return;
    }
    if (dataMap.value) {
      dataMap.value[props.bizType + '__len'] = fileList.value.length;
      emit('update:value', dataMap.value);
      emit('change', dataMap.value, fileList.value);
    }
  }

  // 上传modal保存操作（记录完整顺序，含已上传文件与本次上传成功文件，替换而非追加）
  function handleChange(records: FileUpload[]) {
    // 过滤空值，避免脏数据导致后续取值报错
    fileList.value = (records || []).filter((item) => !!item);
    if (dataMap.value) {
      dataMap.value[props.bizType] = fileList.value.map((item) => item.id).join(',');
      dataMap.value[props.bizType + '__len'] = fileList.value.length;
      emit('update:value', dataMap.value);
    }
    emit('change', dataMap.value, fileList.value);
  }

  // 预览列表保存操作
  function handlePreviewChange(records: FileUpload[]) {
    fileList.value = (records || []).filter((item) => !!item);
    dataMap.value[props.bizType] = fileList.value.map((item) => item.id).join(',');
    dataMap.value[props.bizType + '__len'] = fileList.value.length;
    emit('update:value', dataMap.value);
    emit('change', dataMap.value, fileList.value);
  }

  function handleDelete(record: FileUpload) {
    const index = fileList.value.findIndex((item) => item.id === record.id);
    if (index !== -1) {
      fileList.value.splice(index, 1);
    }
    fileListDel.value.push(record);
    dataMap.value[props.bizType + '__del'] = fileListDel.value.map((item) => item.id).join(',');
    dataMap.value[props.bizType + '__len'] = fileList.value.length;
    emit('delete', record);
    emit('update:value', dataMap.value);
    emit('change', dataMap.value, fileList.value);
  }

  /**
   * 直接上传文件列表（对外接口）
   * 外部输入框粘贴图片时，可调用该方法上传，上传完成后会触发 change 事件
   */
  async function uploadFiles(files: File[]) {
    if (props.readonly || props.disabled || !files?.length) {
      return;
    }
    // 业务主键为空时（如尚未选择记录、未新建会话）无法建立关联，给出提示
    if (!bizKeyValue.value) {
      showMessage(t('请先选择数据再上传'));
      return;
    }
    let res: Awaited<ReturnType<typeof uploadFilesAction>>;
    try {
      res = await uploadFilesAction(files, {
        bizKey: bizKeyValue.value,
        bizType: props.bizType,
        uploadType: props.uploadType || 'image',
        maxNumber: props.maxNumber,
        fileCount: fileList.value.length,
        onMessage: (message) => showMessage(message),
      });
    } catch (e: any) {
      // 上传异常时给出提示，避免粘贴后界面无任何反馈
      showMessage(e?.message || t('上传失败'));
      return;
    }
    // 上传成功：合并到当前文件列表（内嵌区域的粘贴上传走 beforeUpload 流程，不经过这里）
    const uploaded = (res.fileUploads || []).filter((item) => !!item);
    if (uploaded.length > 0) {
      handleChange([...fileList.value, ...uploaded]);
    }
    innerLoadTime.value += 1;
    return res;
  }

  /**
   * 粘贴上传（对外接口）
   * 从剪贴板事件中提取图片并直接上传，无图片时不做处理
   */
  async function pasteUpload(event: ClipboardEvent) {
    const files = getClipboardImageFiles(event);
    if (files.length == 0) {
      return;
    }
    event.preventDefault();
    return await uploadFiles(files);
  }

  // 内嵌预览列表的粘贴区域：点击时聚焦，作为 Ctrl+V 粘贴上传的目标
  const pasteAreaRef = ref<HTMLElement>();
  const previewRef = ref<InstanceType<typeof UploadPreview>>();
  // 当前是否接收粘贴：页面存在多个上传组件时，只有最后点击/聚焦的那个生效
  const pasteActive = ref(false);

  /**
   * 粘贴上传：把剪贴板图片交给上传预览组件处理，
   * 与其「选择文件」「拖入文件」走同一套 beforeUpload 逻辑，不再单独实现上传
   */
  function handlePaste(event: ClipboardEvent) {
    if (previewOnly.value) {
      return;
    }
    const files = getClipboardImageFiles(event);
    if (files.length == 0) {
      return;
    }
    event.preventDefault();
    previewRef.value?.addFiles(files);
  }

  function handlePasteAreaClick(event: MouseEvent) {
    if (previewOnly.value) {
      return;
    }
    pasteActive.value = true;
    const target = event.target as HTMLElement | null;
    // 点击区域内的按钮（上传、删除、预览等）由按钮自行处理，不抢焦点
    if (target?.closest('button, a, input, textarea, .ant-upload')) {
      return;
    }
    pasteAreaRef.value?.focus();
  }

  // 点击到区域外时取消粘贴接收状态，避免多个上传组件互相干扰
  useEventListener(document, 'mousedown', (event: MouseEvent) => {
    const area = pasteAreaRef.value;
    if (area && !area.contains(event.target as Node)) {
      pasteActive.value = false;
    }
  });

  // 键盘 Tab 切换焦点时同步粘贴接收状态
  useEventListener(document, 'focusin', (event: FocusEvent) => {
    const area = pasteAreaRef.value;
    if (area) {
      pasteActive.value = area.contains(event.target as Node);
    }
  });

  // 兜底：浏览器只向当前聚焦元素派发 paste 事件，焦点落在 body 等外层元素时区域自身收不到，
  // 故在 document 上再捕获一次；事件已由区域自身的 @paste 处理时跳过，避免重复上传
  onDocumentPaste(
    (event) => {
      const area = pasteAreaRef.value;
      if (!area || !props.showPreviewList || !pasteActive.value || previewOnly.value) {
        return false;
      }
      if (event.target instanceof Node && area.contains(event.target)) {
        return false;
      }
      return !isEditableElement(document.activeElement);
    },
    (event) => handlePaste(event),
  );

  defineExpose({ uploadFiles, pasteUpload, focus: () => pasteAreaRef.value?.focus() });
</script>
<style lang="less" scoped>
  .jeesite-basic-upload-paste {
    border-radius: 6px;
    outline: none;
    transition: box-shadow 0.2s;

    // 内部预览框的边框参与聚焦高亮，过渡与输入框保持一致
    .upload-preview {
      transition: border-color 0.2s;
    }

    // 聚焦或内部元素聚焦时，与输入框的聚焦样式保持一致：边框变主题色 + 2px 外发光
    &:not(.is-disabled):focus,
    &:not(.is-disabled):focus-within {
      box-shadow: 0 0 0 2px var(--ant-control-outline, rgb(5 145 255 / 10%));

      .upload-preview {
        border-color: var(--ant-color-primary, #1677ff);
      }
    }

    &.is-disabled {
      cursor: not-allowed;
    }
  }
</style>
