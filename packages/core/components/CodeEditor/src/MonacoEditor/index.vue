<template>
  <div
    ref="editContainer"
    class="jeesite-code-editor relative"
    :class="{ bordered: props.bordered }"
    :style="getContainerStyle"
  >
    <div v-if="props.allowFullscreen || $slots.rightTools" class="absolute right-5 z-10 pt-[4px]">
      <slot name="rightTools"></slot>
      <span v-if="props.allowFullscreen" @click="isFullScreen = !isFullScreen" class="cursor-pointer">
        <Icon
          :icon="isFullScreen ? `i-material-symbols:close-fullscreen` : `i-material-symbols:open-in-full`"
          :title="t('缩放')"
        />
      </span>
    </div>
  </div>
</template>
<script lang="ts" setup name="MonacoEditor">
  import { computed, nextTick, onMounted, ref, watch } from 'vue';
  import 'monaco-editor/esm/nls.messages.zh-cn.js';
  import type { editor } from 'monaco-editor';
  import * as monaco from 'monaco-editor';
  import { Icon } from '@jeesite/core/components/Icon';
  // import { useWindowSizeFn } from '@jeesite/core/hooks/event/useWindowSizeFn';
  import { useAppStore } from '@jeesite/core/store/modules/app';
  import { useI18n } from '@jeesite/core/hooks/web/useI18n';

  interface MonacoEditorProps {
    config?: editor.IStandaloneEditorConstructionOptions;
    language?: string;
    lineNumbers?: 'off' | 'on';
    theme?: 'vs-light' | 'vs-dark';
    value?: string;
    readonly?: boolean;
    allowFullscreen?: boolean;
    bordered?: boolean;
    height?: number;
  }

  const props = withDefaults(defineProps<MonacoEditorProps>(), {
    config: () => ({
      selectOnLineNumbers: true,
      minimap: { enabled: false },
      padding: { top: 6, bottom: 6 },
    }),
    language: 'json',
    lineNumbers: 'on',
    value: '',
    readonly: false,
    theme: 'vs-light',
    allowFullscreen: true,
    bordered: true,
    height: 260,
  });

  const emit = defineEmits(['update:value', 'change']);

  const { t } = useI18n();
  const appStore = useAppStore();
  const isFullScreen = ref(false);

  const editContainer = ref<HTMLElement | null>(null);
  let monacoEditor: monaco.editor.IStandaloneCodeEditor | null = null;

  onMounted(() => {
    monacoEditor = monaco.editor.create(editContainer.value as HTMLElement, {
      ...props.config,
      automaticLayout: true,
      language: props.language,
      lineNumbers: props.lineNumbers,
      readOnly: props.readonly,
      scrollBeyondLastLine: false,
      theme: props.theme,
    });

    monacoEditor.onDidChangeModelContent(() => {
      const currenValue = monacoEditor?.getValue();
      emit('update:value', currenValue);
      emit('change', currenValue);
    });

    watch(
      () => props.value,
      async (value) => {
        await nextTick();
        const oldValue = monacoEditor?.getValue();
        if (value !== oldValue) {
          setValue(value ? value : '');
        }
      },
      { immediate: true },
    );

    watch(
      () => props.language,
      async (value) => {
        setLanguage(value);
      },
      { immediate: true },
    );

    watch(
      () => appStore.getDarkMode,
      async (value) => {
        if (value === 'dark') {
          monaco.editor.setTheme('vs-dark');
        } else {
          monaco.editor.setTheme('vs-light');
        }
      },
      { immediate: true },
    );

    // redoEditorHeight();
  });

  function setValue(text: string) {
    monacoEditor?.setValue(text || '');
  }

  function setLanguage(language: string) {
    const model = monacoEditor?.getModel();
    if (model) {
      if (language == 'groovy') {
        language = 'java';
      } else if (language == 'beetl') {
        language = 'html';
      }
      monaco.editor.setModelLanguage(model, language);
    }
  }

  function insertText(text: string) {
    // 获取光标位置
    const position = monacoEditor?.getPosition();
    if (!position) {
      return;
    }
    // 在光标位置插入值
    monacoEditor?.executeEdits('', [
      {
        range: new monaco.Range(position.lineNumber, position.column, position.lineNumber, position.column),
        text,
      },
    ]);
    // 更新光标位置
    monacoEditor?.setPosition({
      ...position,
      column: position.column + text.length,
    });
    monacoEditor?.focus();
  }

  const getContainerStyle = computed(() => {
    const style: any = {};
    if (props.height) {
      style.height = `${props.height}px`;
    }
    if (isFullScreen.value) {
      Object.assign(style, {
        position: 'fixed',
        top: '0',
        left: '0',
        right: '0',
        bottom: '0',
        zIndex: '2999',
        height: '100%',
      });
    }
    return style;
  });

  // function redoEditorHeight() {
  //   if (!editContainer.value) return;
  //   let container = editContainer.value.parentElement as HTMLElement;
  //   let newHeight = container.parentElement?.clientHeight;
  //   container.style.height = newHeight + 'px';
  //   monacoEditor?.layout();
  // }
  //
  // useWindowSizeFn(redoEditorHeight);

  defineExpose({
    setLanguage,
    insertText,
    setValue,
  });
</script>
<style lang="less">
  .jeesite-code-editor {
    width: 100%;

    &.bordered {
      border: 1px solid @border-color-light;
    }

    &.bordered,
    .monaco-editor,
    .overflow-guard {
      border-radius: 6px;
    }

    .monaco-editor {
      .view-overlays {
        .current-line-exact {
          border: 0;
        }
      }
    }
  }

  [data-theme='dark'] {
    .jeesite-code-editor {
      &.bordered {
        border-color: #444;
      }
    }
  }
</style>
