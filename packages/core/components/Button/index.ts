import type { ExtractPropTypes } from 'vue';
import { buttonProps } from './src/props';

export { default as Button } from './src/BasicButton.vue';
export { default as PopConfirmButton } from './src/PopConfirmButton.vue';
export declare type ButtonProps = Partial<ExtractPropTypes<typeof buttonProps>>;
