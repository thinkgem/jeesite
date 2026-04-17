<!--
 * Copyright (c) 2013-Now https://jeesite.com All rights reserved.
 * No deletion without permission, or be held responsible to law.
 * @author ThinkGem
-->
<template>
  <div :class="prefixCls" :style="{ width: containerWidth }">
    <Toolbar class="toolbar" :editor="editorRef" :defaultConfig="toolbarConfig" :mode="mode" />
    <Editor
      class="editor"
      :style="{ height: containerHeight }"
      :defaultConfig="editorConfig"
      :mode="mode"
      @on-created="handleCreated"
      @on-change="handleChange"
    />
  </div>
</template>
<script lang="ts">
  import { Boot } from '@wangeditor/editor';
  import attachmentModule from './plugin-upload-attachment';
  // 要在创建编辑器之前注册，且只能注册一次，不可重复注册
  if (!window['isRegisterAttachmentModule']) {
    Boot.registerModule(attachmentModule);
    window['isRegisterAttachmentModule'] = true;
  }
</script>
<script lang="ts" setup name="WangEditor">
  import '@wangeditor/editor/dist/css/style.css'; // 引入 css
  import { computed, watch, onBeforeUnmount, shallowRef, nextTick, onMounted } from 'vue';
  import { Editor, Toolbar } from '@wangeditor/editor-for-vue';
  import { i18nChangeLanguage, IDomEditor, IEditorConfig, IToolbarConfig } from '@wangeditor/editor';
  import { useDesign } from '@jeesite/core/hooks/web/useDesign';
  import { isNumber } from '@jeesite/core/utils/is';
  import { useLocale } from '@jeesite/core/locales/useLocale';
  import { useGlobSetting } from '@jeesite/core/hooks/setting';
  import { uploadFile } from '@jeesite/core/api/sys/upload';
  import { buildUUID } from '@jeesite/core/utils/uuid';

  const props = defineProps({
    value: {
      type: String,
    },
    height: {
      type: [Number, String] as PropType<string | number>,
      required: false,
      default: 300,
    },
    width: {
      type: [Number, String] as PropType<string | number>,
      required: false,
      default: 'auto',
    },
    bizKey: {
      type: [String, Number] as PropType<string | number>,
      default: '',
    },
    bizType: {
      type: String as PropType<string>,
      default: '',
    },
    mode: {
      type: String as PropType<string>,
      default: 'default',
    },
    disabled: {
      type: Boolean as PropType<boolean>,
      default: false,
    },
  });

  type InsertFnType = (url: string, alt: string, href?: string) => void;

  let isLock = false;
  let lockList: any[] = [];
  async function lock(): Promise<Fn<any, any>> {
    function unlock() {
      let waitFunc = lockList.shift();
      if (waitFunc) {
        waitFunc.resolve(unlock);
      } else {
        isLock = false;
      }
    }
    if (isLock) {
      return new Promise((resolve, reject) => {
        lockList.push({ resolve, reject });
      });
    } else {
      isLock = true;
      return unlock;
    }
  }

  const emit = defineEmits(['update:value', 'change']);

  const { prefixCls } = useDesign('editor-container');
  const { ctxPath, ctxAdminPath } = useGlobSetting();

  const containerWidth = computed(() => {
    const width = props.width;
    if (isNumber(width)) {
      return `${width}px`;
    }
    return width;
  });

  const containerHeight = computed(() => {
    const height = props.height;
    if (isNumber(height)) {
      return `${height}px`;
    }
    return height;
  });

  const toolbarConfig: Partial<IToolbarConfig> = {
    insertKeys: {
      index: 21, // 自定义插入的位置
      keys: ['uploadAttachment'], // “上传附件”菜单
    },
  };

  const customUpload = async (file: File, insertFn: Function, uploadType: string) => {
    const { data } = await uploadFile(
      {
        bizKey: props.bizKey,
        bizType: props.bizType,
        uploadType: uploadType,
        fileMd5: buildUUID(), // 专业版支持 MD5 校验（秒传）
        fileName: file.name,
        file,
      },
      () => {},
    );
    if (data.result == 'true' && data.fileUpload) {
      const fileUpload = data.fileUpload;
      if (uploadType == 'image') {
        insertFn(ctxPath + fileUpload.fileUrl, fileUpload.fileName);
      } else if (uploadType == 'video') {
        insertFn(ctxPath + fileUpload.fileUrl, '');
      } else {
        insertFn(fileUpload.fileName, ctxAdminPath + '/file/download/' + fileUpload.id);
      }
    }
  };

  const editorConfig: Partial<IEditorConfig> = {
    placeholder: '请输入...',
    // 在编辑器中，点击选中“附件”节点时，要弹出的菜单
    hoverbarKeys: {
      attachment: {
        menuKeys: ['downloadAttachment'], // “下载附件”菜单
      },
    },
    MENU_CONF: {
      uploadAttachment: {
        onBeforeUpload: (file: File) => file,
        customUpload: async (file: File, insertFn: Function) => {
          return customUpload(file, insertFn, 'all');
        },
      },
      uploadImage: {
        onBeforeUpload: (file: File) => file,
        customUpload: async (file: File, insertFn: InsertFnType) => {
          return customUpload(file, insertFn, 'image');
        },
      },
      uploadVideo: {
        onBeforeUpload: (file: File) => file,
        customUpload: async (file: File, insertFn: InsertFnType) => {
          return customUpload(file, insertFn, 'video');
        },
      },
    },
  };

  // 编辑器实例，必须用 shallowRef
  const editorRef = shallowRef();

  let isSetHtml = false;
  function setHtml(val: string) {
    const editor = editorRef.value;
    if (editor == null) return;
    isSetHtml = true;
    editor.setHtml(val);
  }

  onMounted(() => {
    watch(
      () => props.value,
      (val) => {
        nextTick(async () => {
          let unlock = await lock();
          try {
            setHtml(val || '');
          } catch (e) {
            setTimeout(() => {
              setHtml(val || '');
            }, 500);
          } finally {
            unlock();
          }
        });
      },
      { immediate: true },
    );
    watch(
      () => props.disabled,
      () => {
        const editor = editorRef.value;
        if (editor == null) return;
        if (props.disabled) {
          editor.disable();
        } else {
          editor.enable();
        }
      },
    );
  });

  // 组件销毁时，也及时销毁编辑器
  onBeforeUnmount(() => {
    const editor = editorRef.value;
    if (editor == null) return;
    editor.destroy();
  });

  const handleCreated = (editor: IDomEditor) => {
    // 记录 editor 实例，重要！
    editorRef.value = editor;
    const lang = useLocale().getLocale.value;
    i18nChangeLanguage(lang == 'en' ? 'en' : 'zh-CN');
    // console.log(editor.getAllMenuKeys());
  };

  const handleChange = (editor: IDomEditor) => {
    if (isSetHtml) {
      isSetHtml = false;
      return;
    }
    let content = editor.getHtml();
    content = content.replace('<!--HTML-->', '');
    content = '<!--HTML-->' + content;
    emit('update:value', content);
    emit('change', content);
  };
</script>
<style lang="less">
  @prefix-cls: ~'jeesite-editor-container';

  .@{prefix-cls} {
    border: 1px solid #d9d9d9;
    border-radius: 6px;

    .w-e-toolbar {
      border-bottom: 1px solid #d9d9d9;
      border-radius: 6px 6px 0 0;
    }

    &.w-e-full-screen-container {
      z-index: 1000;
    }

    .editor {
      overflow-y: hidden;
      border-radius: 0 0 6px 6px;
    }
  }

  [data-theme='dark'] {
    // textarea - css vars
    --w-e-textarea-bg-color: #151515;
    --w-e-textarea-color: #c9d1d9;
    --w-e-textarea-border-color: #303030;
    --w-e-textarea-slight-border-color: #303030;
    --w-e-textarea-slight-color: #c9d1d9;
    --w-e-textarea-slight-bg-color: #151515;
    --w-e-textarea-selected-border-color: #303030; // 选中的元素，如选中了分割线
    --w-e-textarea-handler-bg-color: #4290f7; // 工具，如图片拖拽按钮

    // toolbar - css vars
    --w-e-toolbar-color: #878787;
    --w-e-toolbar-bg-color: #1c1c1c;
    --w-e-toolbar-active-color: #fff;
    --w-e-toolbar-active-bg-color: #393939;
    --w-e-toolbar-disabled-color: #5f5f5f;
    --w-e-toolbar-border-color: #303030;

    // modal - css vars
    --w-e-modal-button-bg-color: #151515;
    --w-e-modal-button-border-color: #303030;
    .@{prefix-cls} {
      border-color: #303030;

      .w-e-toolbar {
        border-bottom-color: #303030;
      }
    }
  }
</style>
