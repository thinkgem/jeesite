<template>
  <div class="bg-white">
    <div class="card-list-header">
      <div class="flex space-x-2">
        <BasicForm @register="registerForm" :baseColProps="{ md: 12, lg: 12 }" />
        <div class="mt-1.5">
          <slot name="header"></slot>
          <Tooltip>
            <template #title>
              <div class="w-50">每页显示数量</div>
              <Slider id="slider" v-bind="sliderProp" v-model:value="grid" @change="sliderChange" />
            </template>
            <a-button class="mr-2"><TableOutlined /></a-button>
          </Tooltip>
          <Tooltip @click="fetch">
            <template #title>刷新</template>
            <a-button class="mr-2"><RedoOutlined /></a-button>
          </Tooltip>
        </div>
      </div>
    </div>
    <Flex gap="middle" wrap="wrap" class="card-list-grid" :style="{ gridTemplateColumns: getGridColumns }">
      <div v-for="item in data" :key="item.id" class="card-list-item">
        <Card class="w-[160px]">
          <template #actions>
            <EditOutlined @click="showMessage('你点击了编辑图标')" />
            <Dropdown
              :trigger="['hover']"
              :dropMenuList="[
                {
                  text: '删除',
                  event: '1',
                  popConfirm: {
                    title: t('是否确认删除'),
                    confirm: handleDelete.bind(null, item.id),
                  },
                },
              ]"
              popconfirm
            >
              <EllipsisOutlined />
            </Dropdown>
          </template>
          <Avatar :src="getAvatar(item)" />
          <span class="pl-2">{{ item.userName }}</span>
        </Card>
      </div>
    </Flex>
    <div class="card-list-pagination mt-2">
      <Pagination
        :show-size-changer="false"
        :show-quick-jumper="true"
        :page-size="pageSize"
        :current="page"
        :total="total"
        :show-total="(total: number) => `总 ${total} 条`"
        @change="pageChange"
        @show-size-change="pageSizeChange"
      />
    </div>
  </div>
</template>
<script lang="ts" setup>
  import { computed, onMounted, ref } from 'vue';
  import { EditOutlined, EllipsisOutlined, RedoOutlined, TableOutlined } from '@antdv-next/icons';
  import { Flex, Card, Tooltip, Slider, Avatar, Pagination } from 'antdv-next';
  import { useI18n } from '@jeesite/core/hooks/web/useI18n';
  import { Dropdown } from '@jeesite/core/components/Dropdown';
  import { BasicForm, useForm } from '@jeesite/core/components/Form';
  import { propTypes } from '@jeesite/core/utils/propTypes';
  import { isFunction } from '@jeesite/core/utils/is';
  import { useSlider, grid } from './data';
  import { useGlobSetting } from '@jeesite/core/hooks/setting';
  import { useMessage } from '@jeesite/core/hooks/web/useMessage';

  interface CardListItem {
    id: number;
    userName: string;
    avatarUrl?: string;
  }

  const { t } = useI18n();
  const { showMessage } = useMessage();

  const getGridColumns = computed(() => {
    return 'repeat(auto-fill, minmax(200px, 1fr))';
  });

  // 获取slider属性
  const sliderProp = computed(() => useSlider(1));

  // 组件接收参数
  const props = defineProps({
    // 请求API的参数
    params: propTypes.object.def({}),
    // api
    api: propTypes.func,
  });

  // 暴露内部方法
  const emit = defineEmits(['getMethod', 'delete']);

  // 数据
  const data = ref<CardListItem[]>([]);

  // 表单
  const [registerForm, { validate }] = useForm({
    schemas: [{ field: 'loginCode', component: 'Input', label: '账号' }],
    labelWidth: 80,
    autoSubmitOnEnter: true,
    showActionButtonGroup: true,
    submitFunc: handleSubmit,
  });

  //表单提交
  async function handleSubmit() {
    const data = await validate();
    await fetch(data);
  }

  function sliderChange(n) {
    pageSize.value = n;
    fetch();
  }

  // 自动请求并暴露内部方法
  onMounted(() => {
    fetch();
    emit('getMethod', fetch);
  });

  async function fetch(p = {}) {
    const { api, params } = props;
    if (api && isFunction(api)) {
      const res = await api({ ...params, pageNo: page.value, pageSize: pageSize.value, ...p });
      data.value = res.list;
      total.value = res.count;
    }
  }
  //分页相关
  const page = ref(1);
  const pageSize = ref(18);
  const total = ref(0);
  const paginationProp = ref({
    showSizeChanger: false,
    showQuickJumper: true,
    pageSize,
    current: page,
    total,
    showTotal: (total: number) => `总 ${total} 条`,
    onChange: pageChange,
    onShowSizeChange: pageSizeChange,
  });

  function getAvatar(item: Recordable) {
    const { ctxPath } = useGlobSetting();
    let url = item.avatarUrl || '/ctxPath/static/images/user1.jpg';
    url = url.replace('/ctxPath/', ctxPath + '/');
    return url;
  }

  function pageChange(p: number, pz: number) {
    page.value = p;
    pageSize.value = pz;
    fetch();
  }
  function pageSizeChange(_current, size: number) {
    pageSize.value = size;
    fetch();
  }

  async function handleDelete(id: number) {
    emit('delete', id);
  }
</script>
