<!--
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 * No deletion without permission, or be held responsible to law.
 * @description 树结构生成封装兼容并统一多种数据格式
 * @author Vben、ThinkGem
-->
<script lang="tsx">
  import type { ReplaceFields, Keys, CheckKeys, TreeActionType, TreeItem } from './typing';

  import {
    defineComponent,
    reactive,
    computed,
    unref,
    ref,
    watchEffect,
    toRaw,
    watch,
    CSSProperties,
    onMounted,
  } from 'vue';
  import { Tree, Empty, Spin } from 'ant-design-vue';
  import { TreeIcon } from './TreeIcon';
  import TreeHeader from './TreeHeader.vue';
  import { ScrollContainer } from '/@/components/Container';

  import { omit, get, difference, intersection, cloneDeep } from 'lodash-es';
  import { isArray, isBoolean, isEmpty, isFunction } from '/@/utils/is';
  import { extendSlots, getSlot } from '/@/utils/helper/tsxHelper';
  import { eachTree, filter, listToTree, treeToList } from '/@/utils/helper/treeHelper';

  import { useTree } from './useTree';
  import { useContextMenu } from '/@/hooks/web/useContextMenu';
  import { useDesign } from '/@/hooks/web/useDesign';
  import { useDict } from '/@/components/Dict';

  import { basicProps } from './props';
  import { CreateContextOptions } from '/@/components/ContextMenu';

  import { CheckEvent } from './typing';

  interface State {
    expandedKeys: Keys;
    selectedKeys: Keys;
    checkedKeys: CheckKeys;
    halfCheckedKeys: Keys;
    checkStrictly: boolean;
  }
  export default defineComponent({
    name: 'BasicTree',
    inheritAttrs: false,
    props: basicProps,
    emits: [
      'update:expandedKeys',
      'update:selectedKeys',
      'update:value',
      'change',
      'check',
      'update:searchValue',
      'reload',
      'tree-data-change',
    ],
    setup(props, { attrs, slots, emit, expose }) {
      const state = reactive<State>({
        checkStrictly: props.checkStrictly,
        expandedKeys: props.expandedKeys || [],
        selectedKeys: props.selectedKeys || [],
        checkedKeys: props.checkedKeys || [],
        halfCheckedKeys: [],
      });

      const searchState = reactive({
        startSearch: false,
        searchText: '',
        searchData: [] as TreeItem[],
      });

      const treeDataRef = ref<TreeItem[]>(props.treeData as TreeItem[]);

      if (props.dictType && props.dictType !== '') {
        const { initSelectTreeData } = useDict();
        initSelectTreeData(treeDataRef, props.dictType, true);
      }

      const [createContextMenu] = useContextMenu();
      const { prefixCls } = useDesign('basic-tree');

      const getReplaceFields = computed((): Required<ReplaceFields> => {
        const { replaceFields } = props;
        return {
          key: 'id',
          title: 'name',
          children: 'children',
          ...replaceFields,
        };
      });

      const getBindValues = computed(() => {
        let propsData = {
          blockNode: true,
          ...attrs,
          ...props,
          expandedKeys: state.expandedKeys,
          selectedKeys: state.selectedKeys,
          checkedKeys: state.checkedKeys,
          checkStrictly: state.checkStrictly,
          replaceFields: unref(getReplaceFields),
          'onUpdate:expandedKeys': (v: Keys) => {
            state.expandedKeys = v;
            emit('update:expandedKeys', v);
          },
          'onUpdate:selectedKeys': (v: Keys) => {
            state.selectedKeys = v;
            emit('update:selectedKeys', v);
          },
          onCheck: (v: CheckKeys, e: CheckEvent) => {
            let currentValue = toRaw(state.checkedKeys) as Keys;
            if (isArray(currentValue) && searchState.startSearch) {
              const { key } = unref(getReplaceFields);
              currentValue = difference(currentValue, getChildrenKeys(e.node.$attrs.node[key]));
              if (e.checked) {
                currentValue.push(e.node.$attrs.node[key]);
              }
              state.checkedKeys = currentValue;
            } else {
              state.checkedKeys = v;
              state.halfCheckedKeys = e.halfCheckedKeys || [];
            }
            const rawVal = toRaw(state.checkedKeys);
            emit('update:value', rawVal);
            emit('check', rawVal, e);
          },
          onRightClick: handleRightClick,
        };
        return omit(propsData, 'treeData', 'class');
      });

      const getTreeData = computed((): TreeItem[] =>
        searchState.startSearch ? searchState.searchData : unref(treeDataRef),
      );

      const getNotFound = computed((): boolean => {
        return !getTreeData.value || getTreeData.value.length === 0;
      });

      const {
        deleteNodeByKey,
        insertNodeByKey,
        insertNodesByKey,
        filterByLevel,
        updateNodeByKey,
        getAllKeys,
        getChildrenKeys,
        getEnabledKeys,
      } = useTree(treeDataRef, getReplaceFields);

      function getIcon(params: Recordable, icon?: string) {
        if (!icon) {
          if (props.renderIcon && isFunction(props.renderIcon)) {
            return props.renderIcon(params);
          }
        }
        return icon;
      }

      async function handleRightClick({ event, node }: Recordable) {
        const { rightMenuList: menuList = [], beforeRightClick } = props;
        let contextMenuOptions: CreateContextOptions = { event, items: [] };

        if (beforeRightClick && isFunction(beforeRightClick)) {
          let result = await beforeRightClick(node, event);
          if (Array.isArray(result)) {
            contextMenuOptions.items = result;
          } else {
            Object.assign(contextMenuOptions, result);
          }
        } else {
          contextMenuOptions.items = menuList;
        }
        if (!contextMenuOptions.items?.length) return;
        createContextMenu(contextMenuOptions);
      }

      function setExpandedKeys(keys: Keys) {
        state.expandedKeys = keys;
      }

      function getExpandedKeys() {
        return state.expandedKeys;
      }
      function setSelectedKeys(keys: Keys) {
        state.selectedKeys = keys;
      }

      function getSelectedKeys() {
        return state.selectedKeys;
      }

      function setCheckedKeys(keys: Keys) {
        if (!state.checkStrictly && keys && keys.length > 0) {
          const parentKeys = getEnabledKeys(undefined, true);
          state.checkedKeys = intersection(keys, parentKeys);
        } else {
          state.checkedKeys = keys;
        }
      }

      function getCheckedKeys() {
        let checkedKeys: Keys;
        if (isArray(state.checkedKeys)) {
          checkedKeys = [...state.checkedKeys, ...state.halfCheckedKeys];
        } else if (state.checkedKeys.checked) {
          checkedKeys = state.checkedKeys.checked;
        } else {
          checkedKeys = [];
        }
        return checkedKeys;
      }

      function reload() {
        loadTreeData();
        emit('reload');
      }

      function checkAll(checkAll: boolean) {
        state.checkedKeys = checkAll ? getEnabledKeys() : ([] as Keys);
      }

      function expandAll(expandAll: boolean) {
        state.expandedKeys = expandAll ? getAllKeys() : ([] as Keys);
      }

      function onStrictlyChange(strictly: boolean) {
        state.checkStrictly = strictly;
      }

      watch(
        () => props.searchValue,
        (val) => {
          if (val !== searchState.searchText) {
            searchState.searchText = val;
          }
        },
        {
          immediate: true,
        },
      );

      const isFirstLoaded = ref<Boolean>(false);
      const loading = ref(false);

      watch(
        () => props.params,
        () => {
          isFirstLoaded.value && loadTreeData();
        },
        { deep: true },
      );

      watch(
        () => props.immediate,
        (v) => {
          v && !isFirstLoaded.value && loadTreeData();
        },
      );

      onMounted(() => {
        if (props.immediate) {
          loadTreeData();
          isFirstLoaded.value = true;
        }
      });

      async function loadTreeData() {
        const { api } = props;
        if (!api || !isFunction(api)) return;
        loading.value = true;
        treeDataRef.value = [];
        let result;
        try {
          result = await api(props.params);
          result = listToTree(result);
        } catch (e) {
          console.error(e);
        } finally {
          loading.value = false;
        }
        if (!result) return;
        if (!isArray(result)) {
          result = get(result, props.resultField);
        }
        treeDataRef.value = (result as TreeItem[]) || [];
        emit('tree-data-change', treeDataRef.value);
      }

      watch(
        () => props.treeData,
        () => {
          setTreeData(props.treeData);
        },
      );

      function setTreeData(treeData: Recordable[] | undefined) {
        if (!treeData) {
          loading.value = true;
          return;
        }
        if (props.treeDataSimpleMode) {
          treeDataRef.value = listToTree(treeData);
        } else {
          treeDataRef.value = treeData as TreeItem[];
        }
        loading.value = false;
        emit('tree-data-change', treeDataRef.value);
      }

      watch(
        () => treeDataRef.value,
        (val) => {
          if (val) {
            // 展开默认级别，而不是 onMounted 时调用
            expandDefaultLevel();
            // 执行搜索过滤
            handleSearch(searchState.searchText);
          }
        },
      );

      function expandDefaultLevel() {
        const level = parseInt(props.defaultExpandLevel);
        if (level > 0) {
          state.expandedKeys = filterByLevel(level);
        } else if (props.defaultExpandAll) {
          expandAll(true);
        }
      }

      function handleSearch(searchValue: string) {
        if (searchValue !== searchState.searchText) searchState.searchText = searchValue;
        emit('update:searchValue', searchValue);
        if (!searchValue) {
          searchState.startSearch = false;
          return;
        }
        const { filterFn, checkable, expandOnSearch, checkOnSearch, selectedOnSearch } =
          unref(props);
        searchState.startSearch = true;
        const { title: titleField, key: keyField } = unref(getReplaceFields);

        const matchedKeys: string[] = [];
        searchState.searchData = filter(
          unref(treeDataRef),
          (node) => {
            const result = filterFn
              ? filterFn(searchValue, node, unref(getReplaceFields))
              : node[titleField]?.includes(searchValue) ?? false;
            if (result) {
              matchedKeys.push(node[keyField]);
            }
            return result;
          },
          unref(getReplaceFields),
        );

        if (expandOnSearch) {
          const expandKeys = treeToList(searchState.searchData).map((val) => {
            return val[keyField];
          });
          if (expandKeys && expandKeys.length) {
            setExpandedKeys(expandKeys);
          }
        }

        if (checkOnSearch && checkable && matchedKeys.length) {
          setCheckedKeys(matchedKeys);
        }

        if (selectedOnSearch && matchedKeys.length) {
          setSelectedKeys(matchedKeys);
        }
      }

      function handleClickNode(key: string, children: TreeItem[]) {
        if (props.showIcon || !props.clickRowToExpand || !children || children.length === 0) return;
        if (!state.expandedKeys.includes(key)) {
          setExpandedKeys([...state.expandedKeys, key]);
        } else {
          const keys = [...state.expandedKeys];
          const index = keys.findIndex((item) => item === key);
          if (index !== -1) {
            keys.splice(index, 1);
          }
          setExpandedKeys(keys);
        }
      }

      // watchEffect(() => {
      //   treeDataRef.value = props.treeData as TreeItem[];
      // });

      // onMounted(() => {
      //   const level = parseInt(props.defaultExpandLevel);
      //   if (level > 0) {
      //     state.expandedKeys = filterByLevel(level);
      //   } else if (props.defaultExpandAll) {
      //     expandAll(true);
      //   }
      // });

      watchEffect(() => {
        state.expandedKeys = props.expandedKeys;
      });

      watchEffect(() => {
        state.selectedKeys = props.selectedKeys;
      });

      watchEffect(() => {
        state.checkedKeys = props.checkedKeys;
      });

      watch(
        () => props.value,
        () => {
          state.checkedKeys = toRaw(props.value || []);
        },
      );

      watch(
        () => state.checkedKeys,
        () => {
          const v = toRaw(state.checkedKeys);
          emit('update:value', v);
          emit('change', v);
        },
      );

      // watchEffect(() => {
      //   console.log('======================');
      //   console.log(props.value);
      //   console.log('======================');
      //   if (props.value) {
      //     state.checkedKeys = props.value;
      //   }
      // });

      watchEffect(() => {
        state.checkStrictly = props.checkStrictly;
      });

      const instance: TreeActionType = {
        setExpandedKeys,
        getExpandedKeys,
        setSelectedKeys,
        getSelectedKeys,
        setCheckedKeys,
        getCheckedKeys,
        insertNodeByKey,
        insertNodesByKey,
        deleteNodeByKey,
        updateNodeByKey,
        checkAll,
        expandAll,
        filterByLevel: (level: number) => {
          state.expandedKeys = filterByLevel(level);
        },
        setSearchValue: (value: string) => {
          handleSearch(value);
        },
        getSearchValue: () => {
          return searchState.searchText;
        },
        setTreeData,
        reload,
      };

      expose(instance);

      function renderAction(node: TreeItem) {
        const { actionList } = props;
        if (!actionList || actionList.length === 0) return;
        return actionList.map((item, index) => {
          let nodeShow = true;
          if (isFunction(item.show)) {
            nodeShow = item.show?.(node);
          } else if (isBoolean(item.show)) {
            nodeShow = item.show;
          }

          if (!nodeShow) return null;

          return (
            <span key={index} class={`${prefixCls}__action`}>
              {item.render(node)}
            </span>
          );
        });
      }

      const treeData = computed((): TreeItem[] | undefined => {
        const data = cloneDeep(getTreeData.value);
        if (!data) return undefined;
        eachTree(data, (item, _parent) => {
          const searchText = searchState.searchText;
          const { highlight } = unref(props);
          const {
            title: titleField,
            key: keyField,
            children: childrenField,
          } = unref(getReplaceFields);

          const icon = getIcon(item, item.icon);
          const title = get(item, titleField);

          const searchIdx = searchText ? title.indexOf(searchText) : -1;
          const isHighlight =
            searchState.startSearch && !isEmpty(searchText) && highlight && searchIdx !== -1;
          const highlightStyle = `color: ${isBoolean(highlight) ? '#f50' : highlight}`;

          const titleDom = isHighlight ? (
            <span class={unref(getBindValues)?.blockNode ? `${prefixCls}__content` : ''}>
              <span>{title.substr(0, searchIdx)}</span>
              <span style={highlightStyle}>{searchText}</span>
              <span>{title.substr(searchIdx + (searchText as string).length)}</span>
            </span>
          ) : (
            <span innerHTML={title} />
          );

          item.isLeaf = !(item.children && item.children.length > 0);

          item[titleField] = (
            <span
              class={`${prefixCls}-title pl-2`}
              onClick={handleClickNode.bind(null, item[keyField], item[childrenField])}
            >
              {slots?.title ? (
                getSlot(slots, 'title', item)
              ) : (
                <>
                  {icon && <TreeIcon icon={icon} />}
                  {titleDom}
                  {/*{get(item, titleField)}*/}
                  <span class={`${prefixCls}__actions`}>{renderAction(item)}</span>
                </>
              )}
            </span>
          );
          return item;
        });
        return data;
      });

      return () => {
        const { title, helpMessage, toolbar, search, checkable, showIcon } = props;
        const showTitle = title || toolbar || search || slots.headerTitle;
        const scrollStyle: CSSProperties = {
          height: 'calc(100%' + (showTitle ? ' - 35px)' : ')'),
        };
        const TreeComp = showIcon ? Tree.DirectoryTree : Tree;
        return (
          <div class={[prefixCls, 'h-full', attrs.class]}>
            {showTitle && (
              <TreeHeader
                checkable={checkable}
                checkAll={checkAll}
                expandAll={expandAll}
                reload={reload}
                title={title}
                search={search}
                toolbar={toolbar}
                helpMessage={helpMessage}
                onStrictlyChange={onStrictlyChange}
                onSearch={handleSearch}
                searchText={searchState.searchText}
              >
                {extendSlots(slots)}
              </TreeHeader>
            )}

            <ScrollContainer style={scrollStyle} v-show={!unref(getNotFound) || unref(loading)}>
              <TreeComp
                {...unref(getBindValues)}
                v-show={!unref(loading)}
                treeData={treeData.value}
              />
              <div class="flex justify-center w-full mt-10 mb-10" v-show={unref(loading)}>
                <Spin />
              </div>
            </ScrollContainer>

            <Empty
              v-show={unref(getNotFound) && !unref(loading)}
              image={Empty.PRESENTED_IMAGE_SIMPLE}
              class="!mt-4"
            />
          </div>
        );
      };
    },
  });
</script>
<style lang="less">
  @prefix-cls: ~'jeesite-basic-tree';

  .@{prefix-cls} {
    background-color: @component-background;
    border-radius: 5px;

    .ant-tree {
      margin: 5px 15px 10px 10px;

      .ant-tree-node-content-wrapper {
        position: relative;
        height: 26px;
        line-height: 27px;

        .ant-tree-title {
          // position: absolute;
          left: 0;
          width: 100%;
        }
      }

      &.ant-tree-directory {
        .@{prefix-cls}-title {
          padding-left: 3px;
        }
        li span {
          .ant-tree-switcher,
          .ant-tree-iconEle {
            width: 20px;
          }
          .ant-tree-iconEle {
            color: fade(@text-color-base, 60);
          }
        }
        > li span.ant-tree-node-content-wrapper,
        .ant-tree-child-tree > li span.ant-tree-node-content-wrapper {
          padding-left: 2px;
          span {
            display: inline-block;
          }
          &:hover::before {
            border-radius: 3px;
          }
        }
        > li.ant-tree-treenode-selected > span,
        .ant-tree-child-tree > li.ant-tree-treenode-selected > span {
          color: @text-color-base;
          &.ant-tree-switcher {
            color: @text-color-base;
          }
          &.ant-tree-node-content-wrapper {
            background-color: fade(@primary-color, 15);
            border-radius: 3px;
          }
          &.ant-tree-node-content-wrapper::before {
            background-color: transparent;
          }
        }
      }
    }

    &-title {
      position: relative;
      display: flex;
      align-items: center;
      width: 100%;
      padding: 0 5px;

      &:hover {
        .@{prefix-cls}__action {
          visibility: visible;
        }
      }
    }

    &__content {
      overflow: hidden;
    }

    &__actions {
      position: absolute;
      top: 2px;
      right: 3px;
      display: flex;
    }

    &__action {
      margin-left: 4px;
      visibility: hidden;
    }
  }

  html[data-theme='light'] {
    .@{prefix-cls}.bg-gray {
      background-color: #f9f9f9;
      border: 1px solid #ddd;
    }
  }

  html[data-theme='dark'] {
    .@{prefix-cls}.bg-gray {
      background-color: #1d1d1d;
      border: 1px solid #383838;
    }
    .@{prefix-cls} {
      .ant-tree {
        &.ant-tree-directory {
          > li.ant-tree-treenode-selected > span,
          .ant-tree-child-tree > li.ant-tree-treenode-selected > span {
            &.ant-tree-node-content-wrapper {
              background-color: fade(@primary-color, 50) !important;
            }
          }
        }
      }
    }
  }
</style>
