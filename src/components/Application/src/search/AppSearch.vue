<script lang="tsx">
  import { defineComponent, ref, unref } from 'vue';
  import { Tooltip } from 'ant-design-vue';
  import { Icon } from '/@/components/Icon';
  import AppSearchModal from './AppSearchModal.vue';
  import { useI18n } from '/@/hooks/web/useI18n';

  export default defineComponent({
    name: 'AppSearch',
    setup() {
      const showModal = ref(false);
      const { t } = useI18n();

      function changeModal(show: boolean) {
        showModal.value = show;
      }

      return () => {
        return (
          <div class="p-1" onClick={changeModal.bind(null, true)}>
            <Tooltip>
              {{
                title: () => t('common.searchText'),
                // default: () => <SearchOutlined />,
                default: () => <Icon icon="i-ant-design:search-outlined" />,
              }}
            </Tooltip>
            <AppSearchModal onClose={changeModal.bind(null, false)} open={unref(showModal)} />
          </div>
        );
      };
    },
  });
</script>
