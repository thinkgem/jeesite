export interface BarMapItem {
  offset: string;
  scroll: string;
  scrollSize: string;
  size: string;
  key: string;
  axis: string;
  client: string;
  direction: string;
}

export interface BarMap {
  vertical: BarMapItem;
  horizontal: BarMapItem;
}

export interface ScrollbarType {
  wrap: ElRef;
}

export type StyleValue = string | CSSProperties | Array<StyleValue>;

export type Merge<O extends object, T extends object> = {
  [K in keyof O | keyof T]: K extends keyof T ? T[K] : K extends keyof O ? O[K] : never;
};

/**
 * T = [
 *  { name: string; age: number; },
 *  { sex: 'male' | 'female'; age: string }
 * ]
 * =>
 * MergeAll<T> = {
 *  name: string;
 *  sex: 'male' | 'female';
 *  age: string
 * }
 */
export type MergeAll<T extends object[], R extends object = any> = T extends [
  infer F extends object,
  ...infer Rest extends object[],
]
  ? MergeAll<Rest, Merge<R, F>>
  : R;
