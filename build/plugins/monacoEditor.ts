/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 * No deletion without permission, or be held responsible to law.
 * @author ThinkGem
 */
import type { PluginOption } from 'vite';
import monacoEditorPlugin from 'vite-plugin-monaco-editor';

export function configMonacoEditorPlugin(): PluginOption {
  return (monacoEditorPlugin as any).default({
    languageWorkers: ['editorWorkerService', 'typescript', 'json', 'html'],
  });
}
