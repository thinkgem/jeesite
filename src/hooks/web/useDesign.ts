import { useAppProviderContext } from '/@/components/Application';
import { theme } from 'ant-design-vue/lib';
// import { computed } from 'vue';
// import { lowerFirst } from 'lodash-es';

export function useDesign(scope: string) {
  const values = useAppProviderContext();
  const token = theme.useToken();
  // const $style = cssModule ? useCssModule() : {};

  // const style: Record<string, string> = {};
  // if (cssModule) {
  //   Object.keys($style).forEach((key) => {
  //     // const moduleCls = $style[key];
  //     const k = key.replace(new RegExp(`^${values.prefixCls}-?`, 'ig'), '');
  //     style[lowerFirst(k)] = $style[key];
  //   });
  // }
  return {
    // prefixCls: computed(() => `${values.prefixCls}-${scope}`),
    prefixCls: `${values.prefixCls}-${scope}`,
    prefixVar: values.prefixCls,
    hashId: token.hashId.value,
    // style,
  };
}
