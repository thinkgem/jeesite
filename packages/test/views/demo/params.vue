<template>
  <PageWrapper title="组件传参实例">
    <p class="mb-3">
      <strong>1、菜单组件参数：</strong>
    </p>
    <p class="mb-3"> 如设置组件参数：{ aa: 'aa1', bb: 'bb2' } </p>
    <p class="mb-3"> 当前接受参数是：{{ props }} </p>
    <p class="mb-3">
      <strong>2、路由请求参数：</strong>
    </p>
    <p class="mb-3">
      路由请求参数：
      <Input
        v-model:value="value"
        placeholder="建议为url标准字符，输入后点击切换"
        style="width: 150px; margin-right: 10px"
      />
      <a-button type="primary" @click="handleClickGo">切换路由</a-button>
      (输入参数后，点击切换路由按钮)
    </p>
    <p class="mb-3"> 接受请求参数是：{{ query }} </p>
    <p class="mb-3">
      接受路径参数是：{{ params }} <br />
      注意：在 Vue Router 4.1.4 中 push params 被移除，请使用 query 代替 <br />
      原文：https://github.com/vuejs/router/blob/main/packages/router/CHANGELOG.md#414-2022-08-22
      <br />
      那么如何使用 params 组件参数呢？一般这样的地址会作为隐形路由使用（隐藏的菜单或权限菜单）<br />
      菜单管理：链接地址：/demo/params/{test1}，组件路径：/demo/params，是否可见：隐藏 <br />
      尝试地址栏填写：http://127.0.0.1:3100/demo/params/test123 <br />
      这样 unref(currentRoute).params.test1 将得到 test123 的值
    </p>
  </PageWrapper>
</template>
<script lang="ts" setup>
  import { Input } from 'ant-design-vue';
  import { computed, ref, unref } from 'vue';
  import { useRouter } from 'vue-router';
  import { PageWrapper } from '@jeesite/core/components/Page';
  import { propTypes } from '@jeesite/core/utils/propTypes';

  const props = defineProps({
    id: propTypes.string,
    aa: propTypes.string,
    bb: propTypes.string,
  });

  const { currentRoute, push } = useRouter();
  const value = ref<string>('');

  const params = computed(() => {
    return unref(currentRoute).params;
  });

  const query = computed(() => {
    return unref(currentRoute).query;
  });

  const handleClickGo = () => {
    const { name } = unref(currentRoute);
    push({
      name: name!,
      params: { param: unref(value) },
      query: { query: unref(value) },
    });
  };
</script>
