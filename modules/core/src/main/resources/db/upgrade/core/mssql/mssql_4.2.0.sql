
-- 新增权限审计菜单
INSERT INTO ${_prefix}sys_menu(menu_code, parent_code, parent_codes, tree_sort, tree_sorts, tree_leaf, tree_level, tree_names, menu_name, menu_type, menu_href, menu_target, menu_icon, menu_color, menu_title, permission, weight, is_show, sys_code, module_codes, status, create_by, create_date, update_by, update_date, remarks, extend_s1, extend_s2, extend_s3, extend_s4, extend_s5, extend_s6, extend_s7, extend_s8, extend_i1, extend_i2, extend_i3, extend_i4, extend_f1, extend_f2, extend_f3, extend_f4, extend_d1, extend_d2, extend_d3, extend_d4) 
VALUES ('1238121573028433920', '0', '0,', 10, '0000000010,', '1', 0, '安全审计', '安全审计', '1', '/sys/audit/list', '', 'icon-energy', '', '', 'sys:audit', 80, '1', 'default', 'core', '0', 'system', getdate(), 'system', getdate(), '', '', '', '', '', '', '', '', '', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);

-- 文件表增加文件预览标记
ALTER TABLE ${_prefix}sys_file_entity ADD file_preview char(1) NULL;
