import type { ComponentRenderProxy, VNode, VNodeChild, PropType as VuePropType } from 'vue';

declare global {
  const __APP_INFO__: {
    pkg: {
      name: string;
      version: string;
      dependencies: Recordable<string>;
      devDependencies: Recordable<string>;
    };
    lastBuildTime: string;
  };

  interface ViteEnv {
    VITE_PORT: number;
    VITE_PUBLIC_PATH: string;
    VITE_OUTPUT_DIR: string;
    VITE_PROXY: [string, string, boolean][];
    VITE_GLOB_APP_TITLE: string;
    VITE_GLOB_APP_SHORT_NAME: string;
    VITE_DROP_CONSOLE: boolean;
    VITE_BUILD_COMPRESS: 'gzip' | 'brotli' | 'none';
    VITE_BUILD_COMPRESS_DELETE_ORIGIN_FILE: boolean;
    VITE_GLOB_API_URL: string;
    VITE_GLOB_API_URL_PREFIX: string;
    VITE_GLOB_ADMIN_PATH: string;
    VITE_FILE_PREVIEW: string;
    VITE_USE_PWA: boolean;
    VITE_LEGACY: boolean;
  }

  // interface Window {
  //   __APP__: App<Element>;
  // }

  function parseInt(s: string | number, radix?: number): number;

  function parseFloat(string: string | number): number;

  type PropType<T> = VuePropType<T>;
  type VueNode = VNodeChild | JSX.Element;

  type Nullable<T> = T | null;
  type Recordable<T = any> = Record<string, T>;
  type TimeoutHandle = ReturnType<typeof setTimeout>;
  type IntervalHandle = ReturnType<typeof setInterval>;
  type DeepPartial<T> = {
    [P in keyof T]?: DeepPartial<T[P]>;
  };

  // interface ChangeEvent extends Event {
  //   target: HTMLInputElement;
  // }
  type ChangeEvent = Event &
    EventInterface.ChangeEvent & {
      target: HTMLInputElement & {
        value?: string | undefined;
        checked?: boolean | undefined;
      };
    };

  // interface WheelEvent {
  //   path?: EventTarget[];
  // }

  interface Fn<T = any, R = T> {
    (...arg: T[]): R;
  }

  interface PromiseFn<T = any, R = T> {
    (...arg: T[]): Promise<R>;
  }

  type RefType<T> = T | null;

  type LabelValueOptions = {
    label: string;
    value: any;
    [key: string]: string | number | boolean;
  }[];

  type EmitType = ReturnType<typeof defineEmits> | Fn;

  type TargetContext = '_self' | '_blank';

  interface ComponentElRef<T extends HTMLElement = HTMLDivElement> {
    $el: T;
  }

  type ComponentRef<T extends HTMLElement = HTMLDivElement> =
    | Ref<Nullable<HTMLElement>, Nullable<HTMLElement>>
    | ComponentElRef<T>
    | HTMLElement
    | null;

  type ElRef<T extends HTMLElement = HTMLDivElement> = Nullable<T>;

  namespace JSX {
    type Element = VNode;
    type ElementClass = ComponentRenderProxy;
    interface ElementAttributesProperty {
      $props: any;
    }
    interface IntrinsicAttributes {
      [elem: string]: any;
    }
    interface IntrinsicElements {
      [elem: string]: any;
    }
  }
}
