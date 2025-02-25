<template>
  <a-input
    v-bind="$attrs"
    v-model:value="state"
    visibilityToggle
    :class="prefixCls"
    :size="size"
    :placeholder="t('sys.login.validCode')"
  >
    <!-- addonAfter suffix -->
    <template #suffix>
      <img :src="getValidCodeImg" @click="refreshValidCodeImg" class="cursor-pointer" width="100" />
    </template>
  </a-input>
</template>
<script lang="ts" setup name="JeeSiteValidCode">
  import { onMounted, ref, watch } from 'vue';
  import { useDesign } from '/@/hooks/web/useDesign';
  import { useRuleFormItem } from '/@/hooks/component/useFormItem';
  import { useI18n } from '/@/hooks/web/useI18n';
  import { useGlobSetting } from '/@/hooks/setting';
  import { useUserStore } from '/@/store/modules/user';

  const props = defineProps({
    value: { type: String },
    size: { type: String, validator: (v: string) => ['default', 'large', 'small'].includes(v) },
    refreshTime: { type: Number, default: 0 },
  });

  const emit = defineEmits(['change', 'update:value']);

  const { t } = useI18n();
  const { ctxPath } = useGlobSetting();
  const userStore = useUserStore();

  const { prefixCls } = useDesign('jeesite-valid-code');
  const [state] = useRuleFormItem(props);

  const getValidCodeImg = ref('');

  function refreshValidCodeImg() {
    getValidCodeImg.value =
      ctxPath + '/validCode' + '?__sid=' + userStore.getToken + '&t=' + new Date().getTime();
  }

  watch(
    () => props.refreshTime,
    () => refreshValidCodeImg(),
  );

  onMounted(() => {
    refreshValidCodeImg();
  });

  defineExpose({
    refreshValidCodeImg,
  });
</script>
<style lang="less">
  @prefix-cls: ~'jeesite-countdown-input';

  .@{prefix-cls} {
    .ant-input-group-addon {
      padding-right: 0;
      background-color: transparent;
      border: none;

      button {
        font-size: 14px;

        &.ant-btn-sm {
          font-size: 13px;
          height: 22px;
          padding: 0 7px;
        }
      }
    }
  }
</style>
