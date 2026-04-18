import type { App } from 'vue';
import type { I18nOptions } from 'vue-i18n';
import { createI18n } from 'vue-i18n';
import { setHtmlPageLang, setLoadLocalePool } from './helper';
import { localeSetting } from '@jeesite/core/settings/localeSetting';
import { useLocaleStoreWithOut } from '@jeesite/core/store/modules/locale';

const { fallback, availableLocales } = localeSetting;

export let i18n: ReturnType<typeof createI18n>;

// 使用 eager 模式同步加载所有语言包
const localeModules = import.meta.glob('./lang/*.ts', { eager: true });

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
  return 'zh_CN';
}

// 从预加载的模块中获取语言消息
function getLocaleMessages(locale: string): Record<string, any> {
  const modulePath = `./lang/${locale}.ts`;
  const defaultLocal = (localeModules[modulePath] as any)?.default;
  return defaultLocal?.message ?? {};
}

// 创建 i18n 配置对象
function createI18nConfig(locale: string, messages: Record<string, any>): I18nOptions {
  return {
    legacy: false,
    locale,
    fallbackLocale: fallback,
    messages: {
      [locale]: messages,
    },
    availableLocales: availableLocales,
    sync: true,
    silentTranslationWarn: true,
    missingWarn: false,
    silentFallbackWarn: true,
    fallbackWarn: false,
  };
}

// 立即创建一个基础的 i18n 实例（同步）
function initBaseI18n() {
  const locale = getCurrentLocale();
  const messages = getLocaleMessages(locale);
  i18n = createI18n(createI18nConfig(locale, messages));
}

// 在模块加载时立即创建基础实例
initBaseI18n();

// 异步加载语言包并更新 i18n 配置
async function updateI18nConfig() {
  const locale = getCurrentLocale();

  setHtmlPageLang(locale as any);
  setLoadLocalePool((loadLocalePool) => {
    loadLocalePool.push(locale as any);
  });

  return {
    locale,
    messages: {
      [locale]: getLocaleMessages(locale),
    },
  };
}

// 安装 i18n 实例
export async function setupI18n(app: App) {
  // 如果还没有基础实例，先创建一个
  if (!i18n) {
    initBaseI18n();
  }

  // 更新语言配置（设置 HTML lang 等）
  const config = await updateI18nConfig();

  // 如果用户切换了语言，更新 i18n 实例
  if (config.locale !== i18n.global.locale) {
    i18n.global.setLocaleMessage(config.locale, config.messages[config.locale] || {});
    (i18n.global.locale as any).value = config.locale;
  }

  app.use(i18n);
}
