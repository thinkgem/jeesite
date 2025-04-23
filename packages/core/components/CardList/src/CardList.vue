<template>
  <div class="bg-white">
    <List
      :grid="{ gutter: 5, xs: 1, sm: 2, md: 4, lg: 4, xl: 6, xxl: 6 }"
      :data-source="data"
      :pagination="paginationProp"
    >
      <template #header>
        <div class="flex space-x-2">
          <BasicForm @register="registerForm" />
          <slot name="header"></slot>
          <Tooltip :overlayStyle="{ maxWidth: '500px' }">
            <template #title>
              <div class="w-50">每页显示数量</div>
              <Slider id="slider" class="w-90" v-bind="sliderProp" v-model:value="grid" @change="sliderChange" />
            </template>
            <a-button><TableOutlined /></a-button>
          </Tooltip>
          <Tooltip @click="fetch">
            <template #title>刷新</template>
            <a-button><RedoOutlined /></a-button>
          </Tooltip>
        </div>
      </template>
      <template #renderItem="{ item }">
        <ListItem style="padding: 10px; margin: 10px 0 0">
          <Card>
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
        </ListItem>
      </template>
    </List>
  </div>
</template>
<script lang="ts" setup>
  import { computed, onMounted, ref } from 'vue';
  import { EditOutlined, EllipsisOutlined, RedoOutlined, TableOutlined } from '@ant-design/icons-vue';
  import { List, Card, Image, Typography, Tooltip, Slider, Avatar } from 'ant-design-vue';
  import { useI18n } from '@jeesite/core/hooks/web/useI18n';
  import { Dropdown } from '@jeesite/core/components/Dropdown';
  import { BasicForm, useForm } from '@jeesite/core/components/Form';
  import { propTypes } from '@jeesite/core/utils/propTypes';
  import { isFunction } from '@jeesite/core/utils/is';
  import { useSlider, grid } from './data';
  import { useGlobSetting } from '@jeesite/core/hooks/setting';
  import { useMessage } from '@jeesite/core/hooks/web/useMessage';

  const { t } = useI18n();
  const { showMessage } = useMessage();

  const ListItem = List.Item;

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
  const data = ref([]);

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
  const pageSize = ref(36);
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
