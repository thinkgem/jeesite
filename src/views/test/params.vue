<template>
  <PageWrapper title="参数测试" content="支持多级参数">
    <p>props：{{ props }}</p>
    <p>query：{{ query }}</p>
    <p>params：{{ params }}</p>
    <p>输入参数切换路由：</p>
    <p><Input v-model:value="value" placeholder="建议为url标准字符，输入后点击切换" /></p>
    <p><a-button type="primary" @click="handleClickGo">切换路由</a-button></p>
  </PageWrapper>
</template>
<script lang="ts" setup>
  import { Input } from 'ant-design-vue';
  import { computed, ref, unref } from 'vue';
  import { useRouter } from 'vue-router';
  import { PageWrapper } from '/@/components/Page';
  import { propTypes } from '/@/utils/propTypes';

  const props = defineProps({
    id: propTypes.string,
    aa: propTypes.string,
    bb: propTypes.string,
  });

  const { currentRoute, push } = useRouter();
  const value = ref<string>('');

  // 注意：在 Vue Router 4.1.4 中 params 被移除，请使用 query 代替
  // https://github.com/vuejs/router/blob/main/packages/router/CHANGELOG.md#414-2022-08-22
  const params = computed(() => {
    return unref(currentRoute).params;
  });

  const query = computed(() => {
    return unref(currentRoute).query;
  });

  const handleClickGo = () => {
    const { name } = unref(currentRoute);
    push({ name: name!, params: { aa: unref(value) }, query: { bb: unref(value) } });
  };
</script>
