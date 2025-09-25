/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 * No deletion without permission, or be held responsible to law.
 * @author ThinkGem
 */
import type { PluginOption } from 'vite';
import monacoEditorPlugin from 'vite-plugin-monaco-editor-esm';

export function configMonacoEditorPlugin(): PluginOption {
  return monacoEditorPlugin({
    languageWorkers: ['editorWorkerService', 'json', 'html'],
    customDistPath: (root, buildOutDir) => `${buildOutDir}/monaco`,
    publicPath: 'monaco',
  });
}
