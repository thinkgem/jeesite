import { i18n } from '@jeesite/core/locales/setupI18n';

type I18nGlobalTranslation = {
  (key: string): string;
  (key: string, locale: string): string;
  (key: string, locale: string, list: unknown[]): string;
  (key: string, locale: string, named: Record<string, unknown>): string;
  (key: string, list: unknown[]): string;
  (key: string, named: Record<string, unknown>): string;
};

type I18nTranslationRestParameters = [string, any];

// 可翻译字符串，继承自 String，在大多数场景下可当作普通字符串使用
// 内部保存 i18n key 和 namespace，用于语言切换时重新翻译
class TranslatableString extends String {
  readonly __i18nTranslatable: boolean = true;
  readonly __i18nKey: string;
  readonly __i18nNamespace?: string;

  constructor(value: string, key: string, namespace?: string) {
    super(value);
    this.__i18nKey = key;
    this.__i18nNamespace = namespace;
  }
}

// 判断值是否为可翻译字符串
export function isTranslatableString(value: any): value is TranslatableString {
  return value instanceof TranslatableString || (value && value.__i18nTranslatable === true);
}

function getKey(namespace: string | undefined, key: string) {
  if (!namespace) {
    return key;
  }
  if (key.startsWith(namespace)) {
    return key;
  }
  return `${namespace}.${key}`;
}

function formatText(key: string, args: any[]) {
  if (!key) return key;
  if (!args || args.length <= 0) return key;
  let text = key;
  if (arguments.length > 0) {
    for (const i in args) {
      if (args[i] != undefined) {
        text = text.replace('{' + i + '}', args[i]);
      }
    }
  }
  return text;
}

export function useI18n(namespace?: string): {
  t: I18nGlobalTranslation;
} {
  if (!i18n) {
    return {
      t: (key: string, ...arg: any[]) => {
        return formatText(getKey(namespace, key), arg);
      },
    };
  }

  const { t, ...methods } = i18n.global;
  const tt = t as (arg0: string, ...arg: I18nTranslationRestParameters) => string;

  const tFn: I18nGlobalTranslation = (key: string, ...arg: any[]) => {
    if (!key) return '';

    const k = getKey(key.startsWith('common.') ? '' : namespace, key);
    const v = tt(k, ...(arg as I18nTranslationRestParameters));

    let result: string;
    if (k == v) {
      const k2 = getKey('common', key);
      const v2 = tt(k2, ...(arg as I18nTranslationRestParameters));
      if (k2 == v2) {
        result = formatText(key, arg);
      } else {
        result = v2;
      }
    } else {
      result = v;
    }

    // 返回可翻译字符串，保存 i18n key 和 namespace
    // 这样在语言切换时可以重新翻译
    return new TranslatableString(result, key, namespace) as unknown as string;
  };

  return {
    ...methods,
    t: tFn,
  };
}

// 为什么要编写此函数？
// 主要用于配合vscode i18nn ally插件。此功能仅用于路由和菜单。请在其他地方使用useI18n
export const t = (key: string) => key;

// 重新翻译配置对象中的所有可翻译字符串
// 遍历配置对象的已知结构，找到 TranslatableString 类型的值，重新翻译
export function retranslateConfig(config: any): any {
  if (!config || typeof config !== 'object') return config;
  if (!i18n) return config;

  const result = deepCloneWithTranslatable(config);
  retranslateObjectStrings(result);
  return result;
}

function deepCloneWithTranslatable(obj: any): any {
  if (obj === null || obj === undefined) return obj;
  if (isTranslatableString(obj)) return obj;
  if (typeof obj !== 'object') return obj;
  if (Array.isArray(obj)) return obj.map(deepCloneWithTranslatable);
  const result: any = {};
  for (const key of Object.keys(obj)) {
    result[key] = deepCloneWithTranslatable(obj[key]);
  }
  return result;
}

function retranslateObjectStrings(obj: any): void {
  if (!obj || typeof obj !== 'object') return;

  for (const key of Object.keys(obj)) {
    const value = obj[key];
    if (isTranslatableString(value)) {
      const { t: tFn } = useI18n(value.__i18nNamespace);
      obj[key] = tFn(value.__i18nKey);
    } else if (typeof value === 'object' && value !== null && !Array.isArray(value)) {
      retranslateObjectStrings(value);
    } else if (Array.isArray(value)) {
      value.forEach((item: any, index: number) => {
        if (isTranslatableString(item)) {
          const { t: tFn } = useI18n(item.__i18nNamespace);
          value[index] = tFn(item.__i18nKey);
        } else if (typeof item === 'object' && item !== null) {
          retranslateObjectStrings(item);
        }
      });
    }
  }
}
