/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 * No deletion without permission, or be held responsible to law.
 * @author ThinkGem
 */
export default {
  root: true,
  plugins: ['stylelint-prettier', 'stylelint-less', '@stylistic/stylelint-plugin'],
  extends: ['stylelint-config-standard', 'stylelint-config-standard-less'],
  ignoreFiles: ['**/*.js', '**/*.jsx', '**/*.tsx', '**/*.ts'],
  overrides: [
    {
      files: ['*.(html|vue)', '**/*.(html|vue)'],
      customSyntax: 'postcss-html',
      extends: ['stylelint-config-recommended-vue'],
      rules: {
        'selector-pseudo-class-no-unknown': [
          true,
          {
            ignorePseudoClasses: ['global', 'deep'],
          },
        ],
        'selector-pseudo-element-no-unknown': [
          true,
          {
            ignorePseudoElements: ['v-deep', 'v-global', 'v-slotted'],
          },
        ],
      },
    },
    {
      files: ['*.less', '**/*.less'],
      customSyntax: 'postcss-less',
      extends: ['stylelint-config-recommended-less'],
    },
    {
      files: ['*.scss', '**/*.scss'],
      customSyntax: 'postcss-scss',
      extends: ['stylelint-config-recommended-scss', 'stylelint-config-recommended-vue/scss'],
    },
  ],
  rules: {
    'prettier/prettier': [
      true,
      {
        preferPositionShorthand: 'never',
        preferOverflowShorthand: 'separate',
      },
    ],
    'media-feature-range-notation': null,
    'selector-not-notation': null,
    'import-notation': null,
    'function-no-unknown': null,
    'selector-class-pattern': null,
    'at-rule-prelude-no-invalid': null,
    'declaration-property-value-no-unknown': null,
    'declaration-property-value-keyword-no-deprecated': null,
    'declaration-property-value-disallowed-list': {
      inset: [/.*/], // 禁止所有 inset 属性值
    },
    // 允许使用传统属性
    'declaration-property-value-allowed-list': {
      top: [/.*/],
      right: [/.*/],
      bottom: [/.*/],
      left: [/.*/],
    },
    'selector-pseudo-class-no-unknown': [
      true,
      {
        ignorePseudoClasses: ['global', 'deep'],
      },
    ],
    'selector-pseudo-element-no-unknown': [
      true,
      {
        ignorePseudoElements: ['v-deep'],
      },
    ],
    'at-rule-no-unknown': [
      true,
      {
        ignoreAtRules: [
          'extends',
          'ignores',
          'include',
          'mixin',
          'if',
          'else',
          'media',
          'for',
          'at-root',
          'unocss',
          'apply',
          'variants',
          'responsive',
          'screen',
          'function',
          'each',
          'use',
          'forward',
          'return',
        ],
      },
    ],
    'no-empty-source': null,
    'named-grid-areas-no-invalid': null,
    'no-descending-specificity': null,
    'font-family-no-missing-generic-family-keyword': null,
    'media-query-no-invalid': null,
    'rule-empty-line-before': [
      'always',
      {
        ignore: ['after-comment', 'first-nested'],
      },
    ],
    'unit-no-unknown': [true, { ignoreUnits: ['rpx'] }],
    'no-duplicate-selectors': null,
  },
};
