/**
 * Multi-language related operations
 */
import type { LocaleType } from '@jeesite/types/config';

import { i18n, getLocaleMessages } from './setupI18n';
import { useLocaleStoreWithOut } from '@jeesite/core/store/modules/locale';
import { unref, computed } from 'vue';
import { loadLocalePool, setHtmlPageLang } from './helper';

function setI18nLanguage(locale: LocaleType) {
  const localeStore = useLocaleStoreWithOut();

  if (i18n.mode === 'legacy') {
    i18n.global.locale = locale;
  } else {
    (i18n.global.locale as any).value = locale;
  }
  localeStore.setLocaleInfo({ locale });
  setHtmlPageLang(locale);
}

export function useLocale() {
  const localeStore = useLocaleStoreWithOut();
  const getLocale = computed(() => localeStore.getLocale);
  const getShowLocalePicker = computed(() => localeStore.getShowPicker);

  const getAntdLocale = computed((): any => {
    const localeMessage = i18n.global.getLocaleMessage<{ antdLocale: any }>(unref(getLocale));
    return localeMessage?.antdLocale ?? {};
  });

  // 切换语言时异步加载对应语言包
  async function changeLocale(locale: LocaleType) {
    const globalI18n = i18n.global;
    const currentLocale = unref(globalI18n.locale);
    if (currentLocale === locale) {
      return locale;
    }

    if (loadLocalePool.includes(locale)) {
      setI18nLanguage(locale);
      return locale;
    }

    // 异步加载语言包
    const message = await getLocaleMessages(locale);
    if (!message || Object.keys(message).length === 0) return;

    globalI18n.setLocaleMessage(locale, message);
    loadLocalePool.push(locale);

    setI18nLanguage(locale);
    return locale;
  }

  return {
    getLocale,
    getShowLocalePicker,
    changeLocale,
    getAntdLocale,
  };
}
