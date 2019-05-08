
-- 新增文件管理模块
INSERT INTO js_sys_module(module_code, module_name, description, main_class_name, current_version, upgrade_info, status, create_by, create_date, update_by, update_date, remarks)
VALUES ('filemanager', '文件管理', '公共文件柜、个人文件柜、文件分享', 'com.jeesite.modules.filemanager.web.FilemanagerController', '4.1.4', NULL, '0', 'system', now(), 'system', now(), NULL);

-- 员工附属机构关系表
CREATE TABLE js_sys_employee_office
(
	id varchar(64) NOT NULL UNIQUE,
	emp_code varchar(64) NOT NULL,
	office_code varchar(64) NOT NULL,
	post_code varchar(64),
	PRIMARY KEY (emp_code, office_code)
) WITHOUT OIDS;
COMMENT ON TABLE js_sys_employee_office IS '员工附属机构关系表';
COMMENT ON COLUMN js_sys_employee_office.id IS '编号';
COMMENT ON COLUMN js_sys_employee_office.emp_code IS '员工编码';
COMMENT ON COLUMN js_sys_employee_office.office_code IS '机构编码';
COMMENT ON COLUMN js_sys_employee_office.post_code IS '岗位编码';

