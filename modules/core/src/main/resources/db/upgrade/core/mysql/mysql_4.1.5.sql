
-- 新增文件管理模块
INSERT INTO js_sys_module(module_code, module_name, description, main_class_name, current_version, upgrade_info, status, create_by, create_date, update_by, update_date, remarks)
VALUES ('filemanager', '文件管理', '公共文件柜、个人文件柜、文件分享', 'com.jeesite.modules.filemanager.web.FilemanagerController', '4.1.4', NULL, '0', 'system', now(), 'system', now(), NULL);

-- 员工附属机构关系表
CREATE TABLE js_sys_employee_office
(
	id varchar(64) NOT NULL COMMENT '编号',
	emp_code varchar(64) NOT NULL COMMENT '员工编码',
	office_code varchar(64) NOT NULL COMMENT '机构编码',
	post_code varchar(64) COMMENT '岗位编码',
	PRIMARY KEY (emp_code, office_code),
	UNIQUE (id)
) COMMENT = '员工附属机构关系表';
