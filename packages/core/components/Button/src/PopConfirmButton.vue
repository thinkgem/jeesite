<script lang="ts">
  import { computed, defineComponent, h, unref } from 'vue';
  import BasicButton from './BasicButton.vue';
  import { Popconfirm } from 'ant-design-vue';
  import { extendSlots } from '@jeesite/core/utils/helper/tsxHelper';
  import { omit } from 'lodash-es';
  import { useAttrs } from '@jeesite/core/hooks/core/useAttrs';
  import { useI18n } from '@jeesite/core/hooks/web/useI18n';

  const props = {
    /**
     * Whether to enable the drop-down menu
     * @default: true
     */
    enable: {
      type: Boolean,
      default: true,
    },
  };

  export default defineComponent({
    name: 'PopButton',
    inheritAttrs: false,
    props,
    setup(props, { slots }) {
      const { t } = useI18n();
      const attrs = useAttrs();

      // get inherit binding value
      const getBindValues = computed(() => {
        return Object.assign(
          {
            okText: t('common.okText'),
            cancelText: t('common.cancelText'),
            placement: 'left',
          },
          { ...props, ...unref(attrs) },
        );
      });

      return () => {
        const bindValues = omit(unref(getBindValues), 'icon') as Recordable;
        const btnBind = omit(bindValues, 'title') as Recordable;
        if (btnBind.disabled) btnBind.color = '';
        const Button = h(BasicButton, btnBind, extendSlots(slots));

        // If it is not enabled, it is a normal button
        if (!props.enable) {
          return Button;
        }
        return h(Popconfirm, bindValues, { default: () => Button });
      };
    },
  });
</script>
