<template>
  <a-col v-bind="actionColOpt" v-if="showActionButtonGroup">
    <div class="ant-form-action" :style="{ textAlign: actionColOpt.style.textAlign }">
      <FormItem>
        <slot name="submitBefore"></slot>
        <Button
          type="primary"
          class="ml-2"
          v-bind="getSubmitBtnOptions"
          @click="formContext.submitAction"
          v-if="showSubmitButton"
        >
          <Icon icon="i-ant-design:search-outlined" />
          {{ getSubmitBtnOptions.text }}
        </Button>
        <slot name="resetBefore"></slot>
        <Button
          type="default"
          class="ml-2"
          v-bind="getResetBtnOptions"
          @click="formContext.resetAction"
          v-if="showResetButton"
        >
          <Icon icon="i-ant-design:reload-outlined" />
          {{ getResetBtnOptions.text }}
        </Button>
        <slot name="advanceBefore"></slot>
        <Button
          type="link"
          size="small"
          class="ml-2"
          style="padding: 0"
          @click="toggleAdvanced"
          v-if="showAdvancedButton && !hideAdvanceBtn"
        >
          <!--{{ isAdvanced ? t('component.form.putAway') : t('component.form.unfold') }}-->
          <BasicArrow :expand="!isAdvanced" up double />
        </Button>
        <slot name="advanceAfter"></slot>
      </FormItem>
    </div>
  </a-col>
</template>
<script lang="ts" setup name="JeeSiteFormAction">
  import type { ColEx } from '../types/index';
  //import type { ButtonProps } from 'ant-design-vue/es/button/buttonTypes';
  import { defineComponent, computed, PropType } from 'vue';
  import { Form, Col } from 'ant-design-vue';
  import { Icon } from '/@/components/Icon';
  import { Button, ButtonProps } from '/@/components/Button';
  import { BasicArrow } from '/@/components/Basic';
  import { useFormContext } from '../hooks/useFormContext';
  import { useI18n } from '/@/hooks/web/useI18n';
  import { propTypes } from '/@/utils/propTypes';

  type ButtonOptions = Partial<ButtonProps> & { text: string };

  const FormItem = Form.Item;

  const props = defineProps({
    showActionButtonGroup: propTypes.bool.def(true),
    showResetButton: propTypes.bool.def(true),
    showSubmitButton: propTypes.bool.def(true),
    showAdvancedButton: propTypes.bool.def(true),
    resetButtonOptions: {
      type: Object as PropType<ButtonOptions>,
      default: () => ({}),
    },
    submitButtonOptions: {
      type: Object as PropType<ButtonOptions>,
      default: () => ({}),
    },
    actionColOptions: {
      type: Object as PropType<Partial<ColEx>>,
      default: () => ({}),
    },
    actionSpan: propTypes.number.def(6),
    isAdvanced: propTypes.bool,
    hideAdvanceBtn: propTypes.bool,
    baseColProps: Object as PropType<Partial<ColEx>>,
    size: propTypes.string,
  });

  const emit = defineEmits(['toggle-advanced']);

  const { t } = useI18n();

  const actionColOpt = computed(() => {
    const { showAdvancedButton, hideAdvanceBtn, baseColProps, actionColOptions } = props;
    // const { showAdvancedButton, baseColProps, actionSpan: span, actionColOptions } = props;
    // const actionSpan = 24 - span;
    // const advancedSpanObj = showAdvancedButton
    //   ? { span: actionSpan < 6 ? 24 : actionSpan }
    //   : {};
    const actionColOpt: Partial<ColEx> = {
      style: {
        paddingLeft: '10px',
        // showAdvancedButton && !hideAdvanceBtn ? 'right' : 'left',
        marginLeft: showAdvancedButton && !hideAdvanceBtn ? 'auto' : 'inherit',
      },
      ...baseColProps,
      // span: showAdvancedButton ? 6 : 4,
      // ...advancedSpanObj,
      ...actionColOptions,
    };
    return actionColOpt;
  });

  const getResetBtnOptions = computed((): ButtonOptions => {
    return Object.assign(
      {
        text: t('common.resetText'),
        size: props.size,
      },
      props.resetButtonOptions,
    );
  });

  const getSubmitBtnOptions = computed(() => {
    return Object.assign(
      {
        text: t('common.queryText'),
        size: props.size,
      },
      props.submitButtonOptions,
    );
  });

  const formContext = useFormContext();

  function toggleAdvanced() {
    emit('toggle-advanced');
  }
</script>
