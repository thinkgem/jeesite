
-- 文件表增加排序字段
ALTER TABLE `${_prefix}sys_file_upload` 
ADD COLUMN `file_sort` decimal(10) NULL COMMENT '文件排序' AFTER `file_type`;

-- 新增 BPM 模块
INSERT INTO `${_prefix}sys_module`(`module_code`, `module_name`, `description`, `main_class_name`, `current_version`, `upgrade_info`, `status`, `create_by`, `create_date`, `update_by`, `update_date`, `remarks`)
VALUES ('bpm', '业务流程', '流程模型设计器、流程定义管理、待办任务、一般任务', 'com.jeesite.modules.bpm.entity.BpmEntity', '4.1.6', NULL, '0', 'system', '2019-08-11 22:36:24', 'system', '2019-08-11 22:36:24', NULL);
