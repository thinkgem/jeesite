import { createAsyncComponent } from '@jeesite/core/utils/factory/createAsyncComponent';

export * from './src/codemirror/typing';
export const CodeEditor = createAsyncComponent(() => import('./src/codemirror/CodeEditor.vue'));
export const JsonPreview = createAsyncComponent(() => import('./src/json-preview/JsonPreview.vue'));
export const MonacoEditor = createAsyncComponent(() => import('./src/MonacoEditor/index.vue'));
