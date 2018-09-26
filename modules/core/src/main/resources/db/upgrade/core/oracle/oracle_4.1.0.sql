
-- 代码生成添加数据源字段
ALTER TABLE ${_prefix}gen_table ADD data_source_name varchar(64) NULL;
COMMENT ON COLUMN ${_prefix}gen_table.data_source_name IS '数据源名称';

-- 新增在线文档Swagger菜单
INSERT INTO ${_prefix}sys_menu (menu_code, parent_code, parent_codes, tree_sort, tree_sorts, tree_leaf, tree_level, tree_names, menu_name, menu_type, menu_href, menu_target, menu_icon, menu_color, permission, weight, is_show, sys_code, module_codes, status, create_by, create_date, update_by, update_date, remarks) 
VALUES ('1044942498470014976', '0', '0,', '9040', '0000009040,', '1', 0, '在线文档', '在线文档', '1', '//swagger-ui.html', '', 'icon-book-open', '', 'sys:swagger', 80, '0', 'default', 'core', '0', 'system', sysdate, 'system', sysdate, '');

commit;