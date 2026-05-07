<template>
  <PageWrapper title="Markdown 组件实例" :contentFullHeight="false">
    <CollapseContainer title="Markdown 演示" :expand="true">
      <Markdown :bizKey="'123456'" :bizType="'testData'" v-model:value="valueRef" @change="handleChange" />
      <a-button @click="clearValue" class="mt-2 mr-2" type="default"> 清空内容 </a-button>
      <a href="https://ld246.com/article/1583308420519" target="_blank">语法速查手册</a>、
      <a href="https://ld246.com/article/1583129520165" target="_blank">基础语法</a>、
      <a href="https://ld246.com/article/1583305480675" target="_blank">扩展语法</a>、
      <a href="https://ld246.com/article/1582778815353" target="_blank">键盘快捷键</a>
    </CollapseContainer>
    <CollapseContainer title="Markdown 预览" :expand="false">
      <MarkdownViewer :value="valueRef" />
    </CollapseContainer>
    <CollapseContainer title="Markdown 表单" :expand="false">
      <BasicForm
        :labelWidth="100"
        :schemas="schemas"
        :actionColOptions="{ span: 24, style: 'text-align: center' }"
        :baseColProps="{ span: 24 }"
        :showActionButtonGroup="true"
        @submit="handleSubmit"
      />
    </CollapseContainer>
  </PageWrapper>
</template>
<script lang="ts" setup>
  import { ref, h } from 'vue';
  import { Markdown, MarkdownViewer } from '@jeesite/core/components/Markdown';
  import { PageWrapper } from '@jeesite/core/components/Page';

  import { BasicForm, FormSchema } from '@jeesite/core/components/Form';
  import { CollapseContainer } from '@jeesite/core/components/Container';
  import { useMessage } from '@jeesite/core/hooks/web/useMessage';

  const valueRef = ref(`
# 标题h1

##### 标题h5

**加粗**
*斜体*
~~删除线~~
[链接](https://jeesite.com)
↓分割线↓

---

* 无序列表1
  * 无序列表1.1

1. 有序列表1
2. 有序列表2

* [ ] 任务列表1
* [x] 任务列表2

> 引用示例

\`\`\`js
// 代码块:
(() => {
  var htmlRoot = document.getElementById('htmlRoot');
  var theme = window.localStorage.getItem('__APP__DARK__MODE__');
  if (htmlRoot && theme) {
    htmlRoot.setAttribute('data-theme', theme);
    theme = htmlRoot = null;
  }
})();
\`\`\`

| 表格 | 示例 | 🎉️ |
| --- | --- | --- |
| 1 | 2 | 3 |
| 4 | 5 | 6 |
`);

  function handleChange(v: string) {
    valueRef.value = v;
  }

  function clearValue() {
    valueRef.value = '';
  }

  // 在表单中展示
  const schemas: FormSchema[] = [
    {
      field: 'title',
      component: 'Input',
      label: '标题',
      defaultValue: '标题',
      rules: [{ required: true }],
    },
    {
      field: 'markdown',
      component: 'Input',
      label: '内容',
      defaultValue: '内容',
      rules: [{ required: true, trigger: 'blur' }],
      render: ({ model, field }) => {
        return h(Markdown, {
          bizKey: '123456',
          bizType: 'testData',
          value: model[field],
          onChange: (value: string) => {
            model[field] = value;
          },
        });
      },
    },
  ];
  const { createMessage } = useMessage();

  function handleSubmit(values: any) {
    createMessage.success('values:' + JSON.stringify(values));
  }
</script>
