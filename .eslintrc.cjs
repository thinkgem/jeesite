module.exports = {
  root: true,
  env: {
    browser: true,
    node: true,
    es6: true,
  },
  parser: 'vue-eslint-parser',
  parserOptions: {
    parser: '@typescript-eslint/parser',
    ecmaVersion: 2020,
    sourceType: 'module',
    jsxPragma: 'React',
    ecmaFeatures: {
      jsx: true,
    },
  },
  plugins: ['vue', '@typescript-eslint'],
  extends: [
    'eslint:recommended',
    'plugin:vue/vue3-recommended',
    'plugin:@typescript-eslint/recommended',
    'plugin:prettier/recommended',
    '@unocss',
  ],
  rules: {
    'no-unused-vars': 'off',
    'no-case-declarations': 'off',
    'no-use-before-define': 'off',
    'no-extra-boolean-cast': 'off',
    'space-before-function-paren': 'off',

    // 'import/first': 'error',
    // 'import/newline-after-import': 'error',
    // 'import/no-duplicates': 'error',

    // '@typescript-eslint/no-unused-vars': [
    //   'error',
    //   {
    //     argsIgnorePattern: '^_',
    //     varsIgnorePattern: '^_',
    //   },
    // ],
    '@typescript-eslint/no-unused-vars': 'off',

    '@typescript-eslint/ban-ts-ignore': 'off',
    '@typescript-eslint/ban-ts-comment': 'off',
    '@typescript-eslint/ban-types': 'off',
    '@typescript-eslint/explicit-function-return-type': 'off',
    '@typescript-eslint/no-explicit-any': 'off',
    '@typescript-eslint/no-var-requires': 'off',
    '@typescript-eslint/no-empty-function': 'off',
    '@typescript-eslint/no-use-before-define': 'off',
    '@typescript-eslint/no-unused-expressions': 'off',
    '@typescript-eslint/no-unsafe-function-type': 'off',
    '@typescript-eslint/no-non-null-assertion': 'off',
    '@typescript-eslint/explicit-module-boundary-types': 'off',
    'vue/script-setup-uses-vars': 'error',
    'vue/no-reserved-component-names': 'off',
    'vue/custom-event-name-casing': 'off',
    'vue/attributes-order': 'off',
    'vue/one-component-per-file': 'off',
    'vue/html-closing-bracket-newline': 'off',
    'vue/max-attributes-per-line': 'off',
    'vue/multiline-html-element-content-newline': 'off',
    'vue/singleline-html-element-content-newline': 'off',
    'vue/attribute-hyphenation': 'off',
    'vue/require-default-prop': 'off',
    'vue/require-explicit-emits': 'off',
    'vue/html-self-closing': [
      'error',
      {
        html: {
          void: 'always',
          normal: 'never',
          component: 'always',
        },
        svg: 'always',
        math: 'always',
      },
    ],
    'vue/multi-word-component-names': 'off',
    'no-undef': 'off',

    // /**
    //  * Strict Mode
    //  */
    // '@typescript-eslint/ban-ts-comment': [
    //   'error',
    //   {
    //     'ts-expect-error': 'allow-with-description',
    //     'ts-ignore': 'allow-with-description',
    //     'ts-nocheck': 'allow-with-description',
    //     'ts-check': false,
    //   },
    // ],
    //
    // /**
    //  * 【强制】关键字前后有一个空格
    //  * @link https://github.com/typescript-eslint/typescript-eslint/blob/master/packages/eslint-plugin/docs/rules/keyword-spacing.md
    //  */
    // 'keyword-spacing': 'off',
    // '@typescript-eslint/keyword-spacing': [
    //   'error',
    //   {
    //     before: true,
    //     after: true,
    //     overrides: {
    //       return: { after: true },
    //       throw: { after: true },
    //       case: { after: true },
    //     },
    //   },
    // ],
    //
    // /**
    //  * 禁止出现空函数，普通函数（非 async/await/generator）、箭头函数、类上的方法除外
    //  * @link https://github.com/typescript-eslint/typescript-eslint/blob/master/packages/eslint-plugin/docs/rules/no-empty-function.md
    //  */
    // 'no-empty-function': 'off',
    // '@typescript-eslint/no-empty-function': [
    //   'error',
    //   {
    //     allow: ['arrowFunctions', 'functions', 'methods'],
    //   },
    // ],
    //
    // /**
    //  * 优先使用 interface 而不是 type 定义对象类型
    //  * @link https://github.com/typescript-eslint/typescript-eslint/blob/master/packages/eslint-plugin/docs/rules/consistent-type-definitions.md
    //  */
    // '@typescript-eslint/consistent-type-definitions': ['warn', 'interface'],
    //
    // 'vue/attributes-order': 'error',
    // 'vue/require-default-prop': 'error',
  },
  globals: { defineOptions: 'readonly' },
};
