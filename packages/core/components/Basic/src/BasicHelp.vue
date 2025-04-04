<script lang="tsx">
  import type { CSSProperties, PropType } from 'vue';
  import { defineComponent, computed, unref } from 'vue';
  import { Tooltip } from 'ant-design-vue';
  import { getPopupContainer } from '@jeesite/core/utils';
  import { isString, isArray } from '@jeesite/core/utils/is';
  import { getSlot } from '@jeesite/core/utils/helper/tsxHelper';
  import { useDesign } from '@jeesite/core/hooks/web/useDesign';
  import { Icon } from '@jeesite/core/components/Icon';

  const props = {
    /**
     * Help text max-width
     * @default: 600px
     */
    maxWidth: { type: String, default: '600px' },
    /**
     * Whether to display the serial number
     * @default: false
     */
    showIndex: { type: Boolean },
    /**
     * Help text font color
     * @default: #ffffff
     */
    color: { type: String, default: '#ffffff' },
    /**
     * Help text font size
     * @default: 14px
     */
    fontSize: { type: String, default: '14px' },
    /**
     * Help text list
     */
    placement: { type: String, default: 'right' },
    /**
     * Help text list
     */
    text: { type: [Array, String] as PropType<string[] | string> },
  };

  export default defineComponent({
    name: 'BasicHelp',
    components: { Icon, Tooltip },
    props,
    setup(props, { slots }) {
      const { prefixCls } = useDesign('basic-help');

      const getTooltipStyle = computed((): CSSProperties => ({ color: props.color, fontSize: props.fontSize }));

      const getOverlayStyle = computed((): CSSProperties => ({ maxWidth: props.maxWidth }));

      function renderTitle() {
        const textList = props.text;

        if (isString(textList)) {
          return <p>{textList}</p>;
        }

        if (isArray(textList)) {
          return textList.map((text, index) => {
            return (
              <p key={text}>
                <>
                  {props.showIndex ? `${index + 1}. ` : ''}
                  {text}
                </>
              </p>
            );
          });
        }
        return null;
      }

      return () => {
        return (
          <Tooltip
            overlayClassName={`${prefixCls}__wrap`}
            title={<div style={unref(getTooltipStyle)}>{renderTitle()}</div>}
            autoAdjustOverflow={true}
            overlayStyle={unref(getOverlayStyle)}
            placement={props.placement as 'right'}
            getPopupContainer={() => getPopupContainer()}
          >
            <span class={prefixCls}>{getSlot(slots) || <Icon icon="i-ant-design:question-circle-outlined" />}</span>
          </Tooltip>
        );
      };
    },
  });
</script>
<style lang="less">
  @prefix-cls: ~'jeesite-basic-help';

  .@{prefix-cls} {
    display: inline-block;
    font-size: 13px;
    color: @text-color-help-dark;
    vertical-align: middle;
    cursor: pointer;

    &:hover {
      color: @primary-color;
    }

    &__wrap {
      p {
        margin-bottom: 0;
      }
    }
  }

  .ant-form-item-label .@{prefix-cls} {
    vertical-align: baseline;
    margin-left: -7px;
    margin-right: -4px;
    opacity: 0.8;
  }
</style>
