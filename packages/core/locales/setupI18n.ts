import type { App } from 'vue';

import { createI18n } from 'vue-i18n';
import { setHtmlPageLang, setLoadLocalePool } from './helper';
import { localeSetting } from '@jeesite/core/settings/localeSetting';
import { useLocaleStoreWithOut } from '@jeesite/core/store/modules/locale';

const { fallback, availableLocales } = localeSetting;

export let i18n: ReturnType<typeof createI18n>;

// 静态导入默认语言包，确保同步模块（如依赖中直接 import 的文件）可正常使用 i18n
import fallbackLocale from './lang/zh_CN';

// 懒加载其他语言包，每个语言文件会成为独立 chunk（zh_CN 已静态导入，getLocaleMessages 中跳过）
const localeLoaders = import.meta.glob<{ default: { message: Record<string, any> } }>('./lang/*.ts');

// 已加载的语言包缓存
const loadedLocaleMessages: Record<string, Record<string, any>> = {};

// 获取当前语言（优先从 store 获取，失败则使用默认语言）
function getCurrentLocale(): string {
  try {
    const localeStore = useLocaleStoreWithOut();
    const storeLocale = localeStore.getLocale;
    if (storeLocale) {
      return storeLocale;
    }
  } catch (e) {
    // store 可能还未初始化
  }
  return fallback;
}

// 异步加载指定语言的消息
export async function getLocaleMessages(locale: string): Promise<Record<string, any>> {
  if (loadedLocaleMessages[locale]) {
    return loadedLocaleMessages[locale];
  }

  // 默认语言包已静态加载，直接返回
  if (locale === fallback) {
    const messages = fallbackLocale.message;
    loadedLocaleMessages[locale] = messages;
    return messages;
  }

  const modulePath = `./lang/${locale}.ts`;
  const loader = localeLoaders[modulePath];
  if (!loader) {
    console.warn(`[i18n] 语言包 "${locale}" 不存在: ${modulePath}`);
    return {};
  }
  const module = await loader();
  const messages = module?.default?.message ?? {};
  loadedLocaleMessages[locale] = messages;
  return messages;
}

// 创建 i18n 实例，默认语言包已同步可用
i18n = createI18n({
  legacy: false,
  locale: getCurrentLocale(),
  fallbackLocale: fallback,
  messages: {
    [fallback]: fallbackLocale.message,
  },
  availableLocales: availableLocales,
  sync: true,
  silentTranslationWarn: true,
  missingWarn: false,
  silentFallbackWarn: true,
  fallbackWarn: false,
});

// 安装 i18n 实例，异步加载当前语言包（如果与默认语言不同）
export async function setupI18n(app: App) {
  const locale = getCurrentLocale();

  // 当前语言不是默认语言时，需要异步加载
  if (locale !== fallback) {
    const messages = await getLocaleMessages(locale);
    if (messages && Object.keys(messages).length > 0) {
      i18n.global.setLocaleMessage(locale, messages);
    }
  } else {
    // 默认语言已静态加载，标记为已缓存
    loadedLocaleMessages[fallback] = fallbackLocale.message;
  }

  (i18n.global.locale as any).value = locale;
  setHtmlPageLang(locale as any);
  setLoadLocalePool((loadLocalePool) => {
    if (!loadLocalePool.includes(locale as any)) {
      loadLocalePool.push(locale as any);
    }
    if (!loadLocalePool.includes(fallback as any)) {
      loadLocalePool.push(fallback as any);
    }
  });

  app.use(i18n);
}
