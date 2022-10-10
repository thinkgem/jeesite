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
      v-model="valueHtml"
      :defaultConfig="editorConfig"
      :mode="mode"
      @on-created="handleCreated"
      @on-change="handleChange"
    />
  </div>
</template>

<script lang="ts">
  import '@wangeditor/editor/dist/css/style.css'; // 引入 css
  import { defineComponent, computed, ref, watch, onBeforeUnmount, shallowRef } from 'vue';
  import { Editor, Toolbar } from '@wangeditor/editor-for-vue';
  import { i18nChangeLanguage, IDomEditor } from '@wangeditor/editor';
  import { useDesign } from '/@/hooks/web/useDesign';
  import { isNumber } from '/@/utils/is';
  import { useLocale } from '/@/locales/useLocale';

  const tinymceProps = {
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
  };

  export default defineComponent({
    name: 'WangEditor',
    components: { Editor, Toolbar },
    inheritAttrs: false,
    props: tinymceProps,
    emits: ['update:value', 'change'],
    setup(props, { emit, attrs }) {
      const { prefixCls } = useDesign('editor-container');

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
            fieldName: 'your-fileName',
            base64LimitSize: 10 * 1024 * 1024, // 10M 以下插入 base64
          },
          uploadVideo: {},
        },
      };

      // 编辑器实例，必须用 shallowRef
      const editorRef = shallowRef();
      const valueHtml = ref('');

      watch(
        () => props.value,
        (val: string, _prevVal: string) => {
          valueHtml.value = val;
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
        valueHtml,
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

    .toolbar {
      border-bottom: 1px solid #ccc;
    }

    .editor {
      overflow-y: hidden;
    }
  }
</style>
