<!--
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
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
  import '@wangeditor/editor/dist/css/style.css'; // 引入 css
  import { defineComponent, computed, watch, onBeforeUnmount, shallowRef, nextTick, onMounted } from 'vue';
  import { Editor, Toolbar } from '@wangeditor/editor-for-vue';
  import { i18nChangeLanguage, IDomEditor } from '@wangeditor/editor';
  import { useDesign } from '/@/hooks/web/useDesign';
  import { isNumber } from '/@/utils/is';
  import { useLocale } from '/@/locales/useLocale';
  import { useGlobSetting } from '/@/hooks/setting';
  import { uploadFile } from '/@/api/sys/upload';
  import { buildUUID } from '/@/utils/uuid';

  const editorProps = {
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
  };

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

  export default defineComponent({
    name: 'WangEditor',
    components: { Editor, Toolbar },
    inheritAttrs: false,
    props: editorProps,
    emits: ['update:value', 'change'],
    setup(props, { emit, attrs }) {
      const { prefixCls } = useDesign('editor-container');
      const { ctxAdminPath } = useGlobSetting();

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

      const toolbarConfig = {};
      const editorConfig = {
        placeholder: '请输入...',
        MENU_CONF: {
          uploadImage: {
            onBeforeUpload: (file: File) => file,
            customUpload: async (file: File, insertFn: InsertFnType) => {
              const { data } = await uploadFile(
                {
                  bizKey: props.bizKey,
                  bizType: props.bizType + '_editor_image',
                  uploadType: 'image',
                  fileMd5: buildUUID(), // 专业版支持 MD5 校验
                  fileName: file.name,
                  file,
                },
                () => {},
              );
              if (data.result == 'true' && data.fileUpload) {
                const url = ctxAdminPath + '/file/download/' + data.fileUpload.id;
                insertFn(url, data.fileUpload.fileName);
              }
            },
          },
          uploadVideo: {},
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
          () => attrs.disabled,
          () => {
            const editor = editorRef.value;
            if (editor == null) return;
            if (attrs.disabled) {
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
        editorRef.value = editor; // 记录 editor 实例，重要！
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

      return {
        prefixCls,
        containerWidth,
        containerHeight,
        editorRef,
        mode: 'default',
        toolbarConfig,
        editorConfig,
        handleCreated,
        handleChange,
      };
    },
  });
</script>
<style lang="less">
  @prefix-cls: ~'jeesite-editor-container';

  .@{prefix-cls} {
    border: 1px solid #ccc;

    .w-e-toolbar {
      border-bottom: 1px solid #ccc;
    }

    &.w-e-full-screen-container {
      z-index: 1000;
    }

    .editor {
      overflow-y: hidden;
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
