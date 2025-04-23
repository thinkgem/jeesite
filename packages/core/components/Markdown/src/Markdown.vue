<template>
  <div ref="wrapRef"></div>
</template>
<script lang="ts" setup>
  import './adapter.js';
  import type { Ref } from 'vue';
  import { ref, unref, nextTick, computed, watch, onBeforeUnmount, onDeactivated, useAttrs } from 'vue';
  import Vditor from 'vditor';
  import 'vditor/dist/index.css';
  import { useLocale } from '@jeesite/core/locales/useLocale';
  import { useModalContext } from '../../Modal';
  import { useRootSetting } from '@jeesite/core/hooks/setting/useRootSetting';
  import { onMountedOrActivated } from '@jeesite/core/hooks/core/onMountedOrActivated';
  import { useGlobSetting } from '@jeesite/core/hooks/setting';
  import { getToken } from '@jeesite/core/utils/auth';
  import { getTheme } from './getTheme';
  import { buildUUID } from '@jeesite/core/utils/uuid';

  type Lang = 'zh_CN' | 'en_US' | 'ja_JP' | 'ko_KR' | undefined;

  defineOptions({ inheritAttrs: false });

  const props = defineProps({
    height: { type: Number, default: 360 },
    value: { type: String, default: '' },
    bizKey: {
      type: [String, Number] as PropType<string | number>,
      default: '',
    },
    bizType: {
      type: String as PropType<string>,
      default: '',
    },
  });

  const emit = defineEmits(['change', 'get', 'update:value']);

  const attrs = useAttrs();

  const wrapRef = ref(null);
  const vditorRef = ref(null) as Ref<Vditor | null>;
  const initedRef = ref(false);

  const modalFn = useModalContext();

  const { getLocale } = useLocale();
  const { getDarkMode } = useRootSetting();
  const valueRef = ref(props.value || '');

  watch(
    [() => getDarkMode.value, () => initedRef.value],
    ([val, inited]) => {
      if (!inited) {
        return;
      }
      instance.getVditor()?.setTheme(getTheme(val) as any, getTheme(val, 'content'), getTheme(val, 'code'));
    },
    {
      immediate: true,
      flush: 'post',
    },
  );

  watch(
    () => props.value,
    (v) => {
      if (v !== valueRef.value) {
        instance.getVditor()?.setValue(v);
      }
      valueRef.value = v;
    },
  );

  const getCurrentLang = computed((): 'zh_CN' | 'en_US' | 'ja_JP' | 'ko_KR' => {
    let lang: Lang;
    switch (unref(getLocale)) {
      case 'en':
        lang = 'en_US';
        break;
      case 'ja':
        lang = 'ja_JP';
        break;
      case 'ko':
        lang = 'ko_KR';
        break;
      default:
        lang = 'zh_CN';
    }
    return lang;
  });

  const { ctxAdminPath } = useGlobSetting();

  function init() {
    const wrapEl = unref(wrapRef);
    if (!wrapEl) return;
    const bindValue = { ...attrs, ...props };
    const insEditor = new Vditor(wrapEl, {
      theme: getTheme(getDarkMode.value) as any,
      lang: unref(getCurrentLang),
      mode: 'ir', // 编辑模式：wysiwyg 所见即所得、ir 即时渲染、sv 分屏预览
      fullscreen: {
        index: 520,
      },
      preview: {
        theme: {
          current: getTheme(getDarkMode.value, 'content'),
        },
        hljs: {
          style: getTheme(getDarkMode.value, 'code'),
        },
        actions: [],
      },
      toolbar: [
        'emoji',
        'headings',
        'bold',
        'italic',
        'strike',
        'link',
        '|',
        'list',
        'ordered-list',
        'check',
        'outdent',
        'indent',
        '|',
        'quote',
        'line',
        'code',
        'inline-code',
        'insert-before',
        'insert-after',
        '|',
        'upload',
        'record',
        'table',
        '|',
        'undo',
        'redo',
        '|',
        'fullscreen',
        'edit-mode',
        {
          name: 'more',
          toolbar: ['both', 'code-theme', 'content-theme', 'export', 'outline', 'preview'],
        },
      ],
      input: (v) => {
        valueRef.value = v;
        emit('update:value', v);
        emit('change', v);
      },
      after: () => {
        nextTick(() => {
          modalFn?.redoModalHeight?.();
          insEditor.setValue(valueRef.value);
          vditorRef.value = insEditor;
          initedRef.value = true;
          emit('get', instance);
        });
      },
      blur: () => {
        //unref(vditorRef)?.setValue(props.value);
      },
      upload: {
        max: 10 * 1024 * 1024, // 10M
        url: ctxAdminPath + '/file/vditor/upload',
        fieldName: 'files',
        extraData: {
          bizKey: String(props.bizKey),
          bizType: props.bizType + '_markdown',
          uploadType: 'all',
        },
        setHeaders: () => {
          const token = getToken();
          return { 'x-token': String(token || '') };
        },
        file: async (files: File[]) => {
          const md5s: string[] = [];
          for (const file of files) {
            md5s.push(buildUUID()); // 专业版支持 MD5 校验
          }
          if (insEditor.vditor.options.upload && insEditor.vditor.options.upload.extraData) {
            insEditor.vditor.options.upload.extraData.md5s = md5s.join(',');
          }
          return files;
        },
      },
      ...bindValue,
      cache: {
        enable: false,
      },
    });
  }

  const instance = {
    getVditor: (): Vditor => vditorRef.value!,
  };

  function destroy() {
    const vditorInstance = unref(vditorRef);
    if (!vditorInstance) return;
    try {
      vditorInstance?.destroy?.();
    } catch (error) {
      //
    }
    vditorRef.value = null;
    initedRef.value = false;
  }

  onMountedOrActivated(init);
  onBeforeUnmount(destroy);
  onDeactivated(destroy);
</script>
