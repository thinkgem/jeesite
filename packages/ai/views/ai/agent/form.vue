<!--
 * Copyright (c) 2013-Now https://jeesite.com All rights reserved.
 * No deletion without permission, or be held responsible to law.
 * @author ThinkGem
-->
<template>
  <BasicDrawer
    v-bind="$attrs"
    :showFooter="true"
    :okAuth="'ai:agent:edit'"
    @register="registerDrawer"
    @ok="handleSubmit"
    width="70%"
  >
    <template #title>
      <Icon :icon="getTitle.icon" class="m-1 pr-1" />
      <span> {{ getTitle.value }} </span>
    </template>
    <BasicForm @register="registerForm">
      <template #agentAvatar="{ model }">
        <!-- 不使用全局 .change-avatar 样式，其 img margin-bottom 会挤压圆形头像造成裁切 -->
        <div class="agent-avatar">
          <!-- 点击头像或右侧按钮均可裁剪上传；未设置时显示默认头像 -->
          <CropperAvatar
            :value="model.agentAvatar ? getAvatarUrl(model.agentAvatar) : defaultAgentAvatar"
            :btnText="t('上传头像')"
            @change="(val) => handleAvatarChange(val, model)"
            width="38"
          />
        </div>
      </template>
      <template #problem="{ model }">
        <LinesInput
          v-model:value="model.problem"
          :addText="t('添加预设问题')"
          :removeText="t('删除')"
          :placeholder="t('请输入预设问题')"
          :maxlength="200"
          :max="20"
        />
      </template>
      <template #shortcut="{ model }">
        <LinesInput
          v-model:value="model.shortcut"
          :addText="t('添加快捷指令')"
          :removeText="t('删除')"
          :placeholder="t('请输入快捷指令')"
          :maxlength="200"
          :max="20"
        />
      </template>
      <!-- 系统提示词：使用 Markdown 编辑器，支持标题、列表、代码块等格式，便于编写结构化的提示词 -->
      <template #systemPrompt="{ model }">
        <Markdown v-model:value="model.systemPrompt" :bizKey="record.id" bizType="ai-agent-prompt" :height="200" />
      </template>
    </BasicForm>
    <!-- 编辑已有智能体时，可直接跳转到对话页，验证该智能体的提示词与问答效果 -->
    <template #centerFooter>
      <a-button v-if="record.id && !record.isNewRecord" @click="handleChat">
        <Icon icon="i-ant-design:message-outlined" /> {{ t('保存并进入对话') }}
      </a-button>
    </template>
  </BasicDrawer>
</template>
<script lang="ts" setup name="ViewsAiAgentForm">
  import { ref, unref, computed, h } from 'vue';
  import { Checkbox } from 'antdv-next';
  import { useI18n } from '@jeesite/core/hooks/web/useI18n';
  import { useMessage } from '@jeesite/core/hooks/web/useMessage';
  import { router } from '@jeesite/core/router';
  import { Icon } from '@jeesite/core/components/Icon';
  import { BasicForm, FormSchema, useForm } from '@jeesite/core/components/Form';
  import { BasicDrawer, useDrawerInner } from '@jeesite/core/components/Drawer';
  import { AiAgent, aiAgentSave, aiAgentForm } from '@jeesite/ai/api/ai/agent';
  import { aiMcpListData, aiToolListData, LinesInput } from '@jeesite/ai';
  import { CropperAvatar } from '@jeesite/core/components/Cropper';
  import { Markdown } from '@jeesite/core/components/Markdown';
  import { useGlobSetting } from '@jeesite/core/hooks/setting';
  import { dataURLtoBlob } from '@jeesite/core/utils/file/base64Conver';
  import { uploadFile } from '@jeesite/core/api/sys/upload';
  import defaultAgentAvatar from '@jeesite/assets/images/logo.png';
  import { buildUUID } from '@jeesite/core/utils/uuid';

  const emit = defineEmits(['success', 'register']);

  const { t } = useI18n('ai.agent');
  const { showMessage } = useMessage();
  const { ctxPath } = useGlobSetting();
  const { meta } = unref(router.currentRoute);
  const record = ref<AiAgent>({} as AiAgent);
  // 头像上传状态：裁剪结果为 base64，需先上传到文件服务再保存文件地址
  const avatarUploading = ref(false);

  // 头像预览地址：相对路径需拼接应用上下文路径
  function getAvatarUrl(avatar?: string) {
    if (!avatar || avatar.startsWith('data:') || avatar.indexOf('://') > -1) {
      return avatar || '';
    }
    return ctxPath + avatar;
  }

  // 头像变更：上传裁剪后的图片，仅保存文件地址（agentAvatar 字段长度有限，不能存 base64）
  async function handleAvatarChange(base64: string, model: Recordable) {
    if (!base64) {
      model.agentAvatar = '';
      return;
    }
    if (!base64.startsWith('data:')) {
      model.agentAvatar = base64;
      return;
    }
    avatarUploading.value = true;
    try {
      const fileName = `avatar-${new Date().getTime()}.png`;
      const file = new File([dataURLtoBlob(base64)], fileName, { type: 'image/png' });
      const fileMd5 = buildUUID();
      const resp: any = await uploadFile(
        {
          file,
          fileMd5, // 专业版支持 MD5 校验（秒传）
          fileName,
          bizKey: record.value.id,
          bizType: 'ai-agent-avatar',
          uploadType: 'image',
        } as any,
        () => {},
      );
      // 兼容接口返回被包一层 data 的情况
      const res = resp?.data && typeof resp.data == 'object' ? resp.data : resp;
      if (res?.result === 'false') {
        showMessage(res.message || t('头像上传失败'));
        return;
      }
      const fileUrl = res?.fileUpload?.fileUrl;
      if (fileUrl) {
        model.agentAvatar = fileUrl;
      } else {
        showMessage(t('头像上传失败'));
      }
    } catch (e: any) {
      showMessage(e?.message || t('头像上传失败'));
    } finally {
      avatarUploading.value = false;
    }
  }

  const getTitle = computed(() => ({
    icon: meta.icon || 'i-ant-design:book-outlined',
    value: record.value.isNewRecord ? t('新增智能体') : t('编辑智能体'),
  }));

  const inputFormSchemas: FormSchema[] = [
    {
      label: t('基本信息'),
      field: 'basicInfo',
      component: 'FormGroup',
      colProps: { md: 24, lg: 24 },
    },
    {
      label: t('头像'),
      field: 'agentAvatar',
      component: 'Input',
      componentProps: {
        maxlength: 100,
      },
      slot: 'agentAvatar',
      colProps: { md: 24, lg: 24 },
    },
    {
      label: t('名称'),
      field: 'agentName',
      component: 'Input',
      componentProps: {
        maxlength: 100,
      },
    },
    {
      label: t('AI 模型'),
      field: 'model.modelCode',
      component: 'ListSelect',
      componentProps: {
        configFile: import('../model/select'),
      },
    },
    {
      label: t('简介'),
      field: 'agentIntro',
      component: 'InputTextArea',
      componentProps: {
        maxlength: 100,
        rows: 2,
      },
      colProps: { md: 24, lg: 24 },
    },
    {
      label: t('权重'),
      field: 'weight',
      component: 'InputNumber',
      componentProps: {
        maxlength: 8,
        min: 0,
        precision: 0,
      },
      helpMessage: t('数值越大排序越靠前'),
      // 权重后缀置顶开关，勾选后权重设为 9999
      suffix: ({ model }) => {
        return h(
          Checkbox,
          {
            checked: model.weight == 9999,
            onChange: (e: any) => {
              model.weight = e.target?.checked ? 9999 : 0;
            },
          },
          () => t('置顶'),
        );
      },
      rules: [{ pattern: /^\d+$/, message: t('请输入一个正整数') }],
    },
    {
      label: t('对话配置'),
      field: 'chatConfig',
      component: 'FormGroup',
      colProps: { md: 24, lg: 24 },
    },
    {
      label: t('系统提示词'),
      field: 'systemPrompt',
      component: 'Input',
      slot: 'systemPrompt',
      helpMessage: t('设定智能体的角色、能力和回答风格，如：你是一名资深的 Java 开发专家'),
      colProps: { md: 24, lg: 24 },
    },
    {
      label: t('欢迎语'),
      field: 'welcome',
      component: 'InputTextArea',
      helpMessage: t('进入对话时展示在页面中间的欢迎语'),
      componentProps: {
        maxlength: 200,
        rows: 2,
      },
      colProps: { md: 24, lg: 24 },
    },
    {
      label: t('预设问题'),
      field: 'problem',
      component: 'Input',
      slot: 'problem',
      helpMessage: t('每行一个问题，对话页最多展示 6 个'),
      colProps: { md: 24, lg: 24 },
    },
    {
      label: t('快捷指令'),
      field: 'shortcut',
      component: 'Input',
      slot: 'shortcut',
      helpMessage: t('每行一个指令'),
      colProps: { md: 24, lg: 24 },
    },
    {
      label: t('输入框提示'),
      field: 'placeholder',
      component: 'Input',
      componentProps: {
        maxlength: 200,
      },
      colProps: { md: 24, lg: 24 },
    },
    {
      label: t('能力配置'),
      field: 'capabilities',
      component: 'FormGroup',
      colProps: { md: 24, lg: 24 },
    },
    {
      label: t('工具调用'),
      field: 'toolCodes',
      fieldLabel: 'toolNames',
      component: 'ListSelect',
      helpMessage: t('从工具管理中选择该智能体可调用的工具'),
      componentProps: {
        configFile: import('../tool/select'),
        checkbox: true,
      },
      colProps: { md: 24, lg: 24 },
    },
    {
      label: t('MCP 服务'),
      field: 'mcpCodes',
      fieldLabel: 'mcpNames',
      component: 'ListSelect',
      helpMessage: t('选择 MCP 服务后，其同步的工具会一并挂载到该智能体'),
      componentProps: {
        configFile: import('../mcp/select'),
        checkbox: true,
      },
      colProps: { md: 24, lg: 24 },
    },
    {
      label: t('向量库绑定'),
      field: 'vectorBizTypes',
      fieldLabel: 'vectorBizNames',
      component: 'ListSelect',
      helpMessage: t('选择「向量库业务」作为知识库，检索条数、相似度阈值取业务配置中的设置'),
      componentProps: {
        configFile: import('../vectorBiz/select'),
        checkbox: true,
      },
      colProps: { md: 24, lg: 24 },
    },
    {
      label: t('回答模版'),
      field: 'optionMap.qaPromptTemplate',
      component: 'InputTextArea',
      // 含占位符大括号，不能走 i18n（vue-i18n 会把 {xxx} 当插值）
      helpMessage:
        '知识库问答的提示词模板，须包含 {question_answer_context}（检索到的参考资料），可用 {query} 引用用户问题；留空使用默认模板',
      componentProps: {
        rows: 3,
        placeholder:
          '你是一个智能助手，可以基于以下参考资料回答用户的问题。\n\n参考资料：\n{question_answer_context}\n\n问题：{query}',
      },
      colProps: { md: 24, lg: 24 },
    },
    {
      label: t('备注信息'),
      field: 'remarks',
      component: 'InputTextArea',
      componentProps: {
        maxlength: 500,
        rows: 2,
      },
      colProps: { md: 24, lg: 24 },
    },
    {
      label: t('任务计划'),
      field: 'optionMap.todoWrite',
      component: 'Switch',
      helpMessage: t('开启后，模型会把多步骤任务拆成任务清单，并在对话中实时显示完成进度'),
      colProps: { md: 8, lg: 6 },
    },
  ];

  const [registerForm, { resetFields, setFieldsValue, validate }] = useForm({
    labelWidth: 120,
    schemas: inputFormSchemas,
    baseColProps: { md: 24, lg: 12 },
  });

  /**
   * 关联工具/MCP 名称兜底：后端未返回名称（或返回为空）时，按编码从列表数据补齐
   */
  async function fillRelationNames() {
    const agent = record.value;
    const toolCodes = (agent.toolCodes || '').split(',').filter(Boolean);
    const mcpCodes = (agent.mcpCodes || '').split(',').filter(Boolean);
    try {
      if (toolCodes.length && !agent.toolNames) {
        const res: any = await aiToolListData({ isAll: true });
        const nameMap: Record<string, string> = {};
        (res?.list || []).forEach((e: any) => (nameMap[e.toolCode] = e.toolName || e.toolCode));
        agent.toolNames = toolCodes.map((code) => nameMap[code] || code).join(',');
      }
      if (mcpCodes.length && !agent.mcpNames) {
        const res: any = await aiMcpListData({ isAll: true });
        const nameMap: Record<string, string> = {};
        (res?.list || []).forEach((e: any) => (nameMap[e.mcpCode] = e.mcpName || e.mcpCode));
        agent.mcpNames = mcpCodes.map((code) => nameMap[code] || code).join(',');
      }
    } catch (e) {
      console.log('fill relation names error', e);
    }
  }

  const [registerDrawer, { setDrawerProps, closeDrawer }] = useDrawerInner(async (data) => {
    setDrawerProps({ loading: true });
    await resetFields();
    const res = await aiAgentForm(data);
    record.value = (res.aiAgent || {}) as AiAgent;
    record.value.__t = new Date().getTime();
    await fillRelationNames();
    await setFieldsValue(record.value);
    setDrawerProps({ loading: false });
  });

  // 跳转到智能体对话页，通过 id 参数锁定当前智能体
  function openChat() {
    // 路由列表中存在 path 为空的路由（如布局、重定向），需先过滤再匹配，避免 path 为 undefined 报错
    const chatRoute = router
      .getRoutes()
      .find((item) => typeof item.path === 'string' && item.path.endsWith('/ai/agent/chat'));
    router.push({
      path: chatRoute?.path || unref(router.currentRoute).path.replace(/\/list$/, '/chat'),
      query: { id: record.value.id },
    });
  }

  // 先保存并关闭抽屉，保存成功后再进入对话页，避免带着未保存的修改去对话
  async function handleChat() {
    if (!record.value.id) {
      return;
    }
    const success = await handleSubmit();
    if (success) {
      closeDrawer();
      // 抽屉默认挂载在 body 上，等关闭动画（约 300ms）走完再跳转，避免抽屉残留遮挡对话页
      setTimeout(openChat, 300);
    }
  }

  async function handleSubmit(): Promise<boolean> {
    try {
      const data = await validate();
      setDrawerProps({ confirmLoading: true });
      const params: any = {
        isNewRecord: record.value.isNewRecord,
        id: data.id || record.value.id,
      };
      // console.log('submit', params, data, record);
      const res = await aiAgentSave(params, data);
      showMessage(res.message);
      setTimeout(closeDrawer);
      emit('success', data);
      // 保存成功标识，供「智能体对话」按钮判断是否继续跳转
      return res?.result !== 'false';
    } catch (error: any) {
      if (error && error.errorFields) {
        showMessage(error.message || t('common.validateError'));
      }
      console.log('error', error);
      return false;
    } finally {
      setDrawerProps({ confirmLoading: false });
    }
  }
</script>
<style lang="less" scoped>
  // 头像：与表单行高保持协调，头像与上传按钮横向排列且垂直居中
  .agent-avatar {
    display: flex;
    align-items: center;
    min-height: 32px;

    // 组件默认 inline-block（按钮在头像下方居中），这里改为横向排列
    :deep(.jeesite-cropper-avatar) {
      display: flex;
      align-items: center;
      gap: 12px;
      text-align: left;

      img {
        display: block;
        margin-bottom: 0;
      }
    }

    // 上传按钮默认带 10px 上下外边距，去掉后与头像垂直居中对齐
    :deep(.jeesite-cropper-avatar-upload-btn) {
      margin: 0;
    }
  }
</style>
