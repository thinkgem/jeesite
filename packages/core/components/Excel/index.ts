import { createAsyncComponent } from '@jeesite/core/utils/factory/createAsyncComponent';

export * from './src/typing';
export const ImpExcel = createAsyncComponent(() => import('./src/ImportExcel.vue'));
export const ExpExcelModal = createAsyncComponent(() => import('./src/ExportExcelModal.vue'));
