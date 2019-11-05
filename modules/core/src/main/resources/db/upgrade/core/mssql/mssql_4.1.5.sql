
-- 新增文件管理模块
INSERT INTO ${_prefix}sys_module(module_code, module_name, description, main_class_name, current_version, upgrade_info, status, create_by, create_date, update_by, update_date, remarks)
VALUES ('filemanager', '文件管理', '公共文件柜、个人文件柜、文件分享', 'com.jeesite.modules.filemanager.web.FilemanagerController', '4.1.4', NULL, '0', 'system', getdate(), 'system', getdate(), NULL);

-- 员工附属机构关系表
CREATE TABLE [${_prefix}sys_employee_office]
(
	[id] varchar(64) NOT NULL UNIQUE,
	[emp_code] varchar(64) NOT NULL,
	[office_code] varchar(64) NOT NULL,
	[post_code] varchar(64),
	PRIMARY KEY ([emp_code], [office_code])
);

-- 新增日语（日本语）语言包
INSERT INTO ${_prefix}sys_dict_data(dict_code, parent_code, parent_codes, tree_sort, tree_sorts, tree_leaf, tree_level, tree_names, dict_label, dict_value, dict_type, is_sys, description, css_style, css_class, status, create_by, create_date, update_by, update_date, remarks, corp_code, corp_name, extend_s1, extend_s2, extend_s3, extend_s4, extend_s5, extend_s6, extend_s7, extend_s8, extend_i1, extend_i2, extend_i3, extend_i4, extend_f1, extend_f2, extend_f3, extend_f4, extend_d1, extend_d2, extend_d3, extend_d4)
VALUES ('1126375062364020736', '0', '0,', 60, '0000000060,', '1', 0, '日本語', '日本語', 'ja_JP', 'sys_lang_type', '1', '', '', '', '0', 'system', getdate(), 'system', getdate(), '', '0', 'JeeSite', '', '', '', '', '', '', '', '', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
