
// 深层字符串路径：a.b.c 或 a.0.name 等
type DeepStringPath<T, Depth extends any[] = []> =
  T extends string | number | boolean | null | undefined | symbol | Map<string, any>
    ? never
    : T extends Array<infer U>
      ? `${number}` | (Depth['length'] extends 5 ? never : `${number}.${DeepStringPath<U, [...Depth, 0]>}`)
      : T extends object
        ? {
            [K in keyof T & (string | number)]:
              | `${K}`
              | (Depth['length'] extends 5 ? never : `${K}.${DeepStringPath<T[K], [...Depth, 0]>}`);
          }[keyof T & (string | number)]
        : never;

// 深层数组路径：[string | number, ...]
type DeepArrayPath<T, Depth extends any[] = []> =
  T extends string | number | boolean | null | undefined | symbol | Map<string, any>
    ? never
    : T extends Array<infer U>
      ? [number] | (Depth['length'] extends 5 ? never : [number, ...DeepArrayPath<U, [...Depth, 0]>])
      : T extends object
        ? {
            [K in keyof T & (string | number)]:
              | [K]
              | (Depth['length'] extends 5 ? never : [K, ...DeepArrayPath<T[K], [...Depth, 0]>]);
          }[keyof T & (string | number)]
        : never;

// 合并路径类型
type DynamicDataIndex<T> = DeepStringPath<T> | DeepArrayPath<T> | readonly (DeepStringPath<T> | number)[];

// 判断是否为默认泛型（即未指定具体结构）
type IsDefaultGeneric<T> = T extends Record<string, any> ? (unknown extends T ? true : false) : false;

// 表格 DataIndex 类型
export type TableDataIndex<T = any> = IsDefaultGeneric<T> extends true ? string | number | readonly (string | number)[] : DynamicDataIndex<T>;
export type TableRecordable<T = any> = Record<TableDataIndex, T>;

// 表单 Field 类型
export type FormField<T = any> = IsDefaultGeneric<T> extends true ? string : DeepStringPath<T>;
export type FormRecordable<T = any> = Record<FormField, T>;
