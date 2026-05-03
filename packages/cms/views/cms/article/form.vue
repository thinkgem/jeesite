<!--
 * Copyright (c) 2013-Now https://jeesite.com All rights reserved.
 * No deletion without permission, or be held responsible to law.
 * @author ThinkGem
-->
<template>
  <CollapseForm
    :config="formConfig"
    :loading="loadingRef"
    :okLoading="okLoadingRef"
    :okAuth="'cms:article:edit'"
    @close="handleClose"
    @ok="handleSubmit"
  >
    <template #title>
      <Icon :icon="getTitle.icon" class="m-1 pr-1" />
      <span> {{ getTitle.value }} </span>
    </template>
    <template #main>
      <FormBasic ref="formBasicRef" />
    </template>
    <template #detail>
      <FormDetail ref="formDetailRef" />
    </template>
    <template #other>
      <FormOther ref="formOtherRef" />
    </template>
    <template #view>
      <FormView ref="formViewRef" />
    </template>
    <template #extend>
      <FormExtend ref="formExtendRef" :title="false" :collapsed="false" />
    </template>
    <template #actions>
      <div>
        <a-button type="default" @click="handleClose" v-auth="'cms:article:edit'">
          <Icon icon="i-ant-design:close-outlined" /> {{ t('common.closeText') }}
        </a-button>
        <a-button
          v-if="record.isNewRecord || record.status == '9'"
          color="success"
          @click="handleDraft"
          :loading="okLoadingRef"
        >
          <Icon icon="i-ant-design:save-outlined" /> {{ t('草稿') }}
        </a-button>
        <a-button type="primary" @click="handlePublish" :loading="okLoadingRef">
          <Icon icon="i-ant-design:check-outlined" /> {{ record.status == '0' ? t('更新') : t('发布') }}
        </a-button>
      </div>
    </template>
  </CollapseForm>
</template>
<script lang="ts" setup name="ViewsCmsArticleForm">
  import { ref, shallowRef, unref, computed, onMounted } from 'vue';
  import { useEmitter } from '@jeesite/core/store/modules/user';
  import { useI18n } from '@jeesite/core/hooks/web/useI18n';
  import { useMessage } from '@jeesite/core/hooks/web/useMessage';
  import { router } from '@jeesite/core/router';
  import { Icon } from '@jeesite/core/components/Icon';
  import { CollapseForm } from '@jeesite/core/components/CollapseForm';
  import { Article, articleSave, articleForm } from '@jeesite/cms/api/cms/article';
  import { useQuery } from '@jeesite/core/hooks/web/usePage';
  import { useTabs } from '@jeesite/core/hooks/web/useTabs';

  import FormBasic from './formBasic.vue';
  import FormDetail from './formDetail.vue';
  import FormView from './formView.vue';
  import FormOther from './formOther.vue';
  import { FormExtend } from '@jeesite/core/components/Form';

  const emitter = useEmitter();

  const { t } = useI18n('cms.article');
  const { showMessage } = useMessage();
  //const { meta } = unref(router.currentRoute);
  const { setTitle, close } = useTabs(router);
  const record = ref<Article>({} as Article);
  const isCanUseAuth = ref(false);
  const isNeedAudit = ref(false);

  const loadingRef = ref<boolean>(false);
  const okLoadingRef = ref<boolean>(false);
  const query = useQuery();

  const getTitle = computed(() => ({
    icon: /*meta.icon || */ 'i-ant-design:book-outlined',
    value: record.value.isNewRecord ? t('新增文章') : t('编辑文章'),
  }));

  const formConfig = ref<any[]>([
    {
      label: t('基本信息'),
      value: 'main',
      open: true,
    },
    {
      label: t('内容正文'),
      value: 'detail',
      open: true,
    },
    {
      label: t('其他信息'),
      value: 'other',
      open: true,
    },
    {
      label: t('视图配置'),
      value: 'view',
      open: true,
    },
    {
      label: t('扩展字段'),
      value: 'extend',
      open: false,
    },
  ]);

  const formBasicRef = shallowRef<InstanceType<typeof FormBasic>>();
  const formDetailRef = shallowRef<InstanceType<typeof FormDetail>>();
  const formOtherRef = shallowRef<InstanceType<typeof FormOther>>();
  const formViewRef = shallowRef<InstanceType<typeof FormView>>();
  const formExtendRef = shallowRef<InstanceType<typeof FormExtend>>();

  async function resetFields() {
    await formBasicRef.value?.resetFields();
    await formDetailRef.value?.resetFields();
    await formOtherRef.value?.resetFields();
    await formViewRef.value?.resetFields();
    await formExtendRef.value?.resetFields();
  }

  async function setFieldsValue(values: Recordable, res: any) {
    await formBasicRef.value?.setFieldsValue(values, isNeedAudit);
    await formDetailRef.value?.setFieldsValue(values);
    await formOtherRef.value?.setFieldsValue(values);
    await formViewRef.value?.setFieldsValue(values, res);
    if (record.value.articleData) {
      await formExtendRef.value?.setFieldsValue(record.value.articleData.extend);
    }
  }

  async function validate(): Promise<Recordable<Article>> {
    return Object.assign(
      await formBasicRef.value?.validate(),
      await formDetailRef.value?.validate(),
      await formOtherRef.value?.validate(),
      await formViewRef.value?.validate(),
    );
  }

  onMounted(async () => {
    loadingRef.value = true;
    await resetFields();
    const res = await articleForm(unref(query));
    record.value = (res.article || {}) as Article;
    record.value.__t = new Date().getTime();
    isCanUseAuth.value = res.isCanUseAuth as boolean;
    isNeedAudit.value = record.value.category.isNeedAudit == '1';
    await setFieldsValue(record.value, res);
    await setTitle(unref(getTitle).value);
    loadingRef.value = false;
  });

  async function handleClose() {
    setTimeout(close);
  }

  async function handleDraft() {
    record.value.status = '9';
    await handleSubmit();
  }

  async function handlePublish() {
    record.value.status = '0';
    await handleSubmit();
  }

  async function handleSubmit(event?: any) {
    try {
      okLoadingRef.value = true;
      const data = event?.formData || (await validate()); // 文章审核，提交到 BPM 流程引擎（专业版）
      if (isCanUseAuth.value) {
        data.bpm = Object.assign(data.bpm || {}, record.value.bpm); // 流程信息
      }
      data.status = record.value.status; // 提交状态
      const params: any = {
        isNewRecord: record.value.isNewRecord,
        id: record.value.id,
      };
      if (!data.articleData) data.articleData = {};
      data.articleData.extend = await formExtendRef.value?.validate();
      // console.log('submit', params, data, record);
      const res = await articleSave(params, data);
      showMessage(res.message);
      handleSuccess();
      setTimeout(close);
    } catch (error: any) {
      if (error && error.errorFields) {
        showMessage(error.message || t('common.validateError'));
      }
      console.log('error', error);
    } finally {
      okLoadingRef.value = false;
    }
  }

  function handleSuccess() {
    emitter.emit('cms-article-reload');
  }
</script>
