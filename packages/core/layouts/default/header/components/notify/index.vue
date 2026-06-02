<template>
  <div class="jeesite-header-notify">
    <Popover title="" trigger="click" :classes="{ root: 'jeesite-header-notify__overlay' }">
      <Badge :count="count" dot :numberStyle="numberStyle">
        <BellOutlined />
      </Badge>
      <template #content>
        <Tabs>
          <template v-for="item in listData" :key="item.key">
            <TabPane>
              <template #tab>
                {{ item.name }}
                <span v-if="item.list.length !== 0">({{ item.list.length }})</span>
              </template>
              <!-- 绑定title-click事件的通知列表中标题是“可点击”的-->
              <NoticeList :list="item.list" v-if="item.key === '1'" @title-click="onNoticeClick" />
              <NoticeList :list="item.list" v-else />
            </TabPane>
          </template>
        </Tabs>
      </template>
    </Popover>
  </div>
</template>
<script lang="ts">
  import { computed, defineComponent, ref } from 'vue';
  import { Popover, Tabs, TabPane, Badge } from 'antdv-next';
  import { BellOutlined } from '@antdv-next/icons';
  import { tabListData, ListItem } from './data';
  import NoticeList from './NoticeList.vue';
  import { useMessage } from '@jeesite/core/hooks/web/useMessage';

  export default defineComponent({
    components: { Popover, BellOutlined, Tabs, TabPane, Badge, NoticeList },
    setup() {
      const { createMessage } = useMessage();
      const listData = ref(tabListData);

      const count = computed(() => {
        let count = 0;
        for (let i = 0; i < tabListData.length; i++) {
          count += tabListData[i].list.length;
        }
        return count;
      });

      function onNoticeClick(record: ListItem) {
        createMessage.success('你点击了通知，ID=' + record.id);
        // 可以直接将其标记为已读（为标题添加删除线）,此处演示的代码会切换删除线状态
        record.titleDelete = !record.titleDelete;
      }

      return {
        listData,
        count,
        onNoticeClick,
        numberStyle: {},
      };
    },
  });
</script>
<style lang="less">
  .jeesite-header-notify {
    padding-top: 2px;

    &__overlay {
      max-width: 360px;

      .ant-popover-content {
        box-shadow: none;
        width: 300px;
      }
    }

    .ant-badge {
      font-size: 18px;

      .ant-badge {
        &-multiple-words {
          padding: 0 4px;
        }

        &-dot {
          top: -2px;
          right: 4px;
        }
      }

      svg {
        width: 0.9em;
      }
    }
  }
</style>
