import { withInstall } from '@jeesite/core/utils';
import codeEditor from './src/CodeEditor.vue';
import jsonPreview from './src/json-preview/JsonPreview.vue';

export const CodeEditor = withInstall(codeEditor);
export const JsonPreview = withInstall(jsonPreview);

export { default as MonacoEditor } from './src/MonacoEditor/index.vue';

export * from './src/typing';
