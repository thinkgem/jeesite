
-- 员工表新增工号字段
ALTER TABLE ${_prefix}sys_employee ADD emp_no varchar(100) NULL;
COMMENT ON COLUMN ${_prefix}sys_employee.emp_no IS '员工工号';

-- 新增 BPM 模块
INSERT INTO ${_prefix}sys_module(module_code, module_name, description, main_class_name, current_version, upgrade_info, status, create_by, create_date, update_by, update_date, remarks)
VALUES ('bpm', '业务流程', '流程设计器、流程监管控制、流程办理、流程追踪', 'com.jeesite.modules.bpm.entity.BpmEntity', '4.1.x', NULL, '0', 'system', now(), 'system', now(), NULL);
