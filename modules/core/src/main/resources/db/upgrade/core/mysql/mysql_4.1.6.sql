
-- 角色表增加适应业务范围
ALTER TABLE `${_prefix}sys_role` 
ADD COLUMN `biz_scope` varchar(255) NULL COMMENT '适应业务范围（不同的功能，不同的数据权限支持）' AFTER `data_scope`;

-- 角色业务范围字典数据
INSERT INTO `${_prefix}sys_dict_type`(`id`, `dict_name`, `dict_type`, `is_sys`, `status`, `create_by`, `create_date`, `update_by`, `update_date`, `remarks`) 
VALUES ('1149344200121085952', '角色业务范围', 'sys_role_biz_scope', '1', '0', 'system', now(), 'system', now(), '');
INSERT INTO `${_prefix}sys_dict_data`(`dict_code`, `parent_code`, `parent_codes`, `tree_sort`, `tree_sorts`, `tree_leaf`, `tree_level`, `tree_names`, `dict_label`, `dict_value`, `dict_type`, `is_sys`, `description`, `css_style`, `css_class`, `status`, `create_by`, `create_date`, `update_by`, `update_date`, `remarks`, `corp_code`, `corp_name`, `extend_s1`, `extend_s2`, `extend_s3`, `extend_s4`, `extend_s5`, `extend_s6`, `extend_s7`, `extend_s8`, `extend_i1`, `extend_i2`, `extend_i3`, `extend_i4`, `extend_f1`, `extend_f2`, `extend_f3`, `extend_f4`, `extend_d1`, `extend_d2`, `extend_d3`, `extend_d4`) 
VALUES ('1149344606834356224', '0', '0,', 30, '0000000030,', '1', 0, '组织管理', '组织管理', 'office_user', 'sys_role_biz_scope', '1', '', '', '', '0', 'system', now(), 'system', now(), '', '0', 'JeeSite', '', '', '', '', '', '', '', '', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);

-- 角色表增加扩展字段
ALTER TABLE `${_prefix}sys_role` 
ADD COLUMN extend_s1 varchar(500) COMMENT '扩展 String 1',
ADD COLUMN extend_s2 varchar(500) COMMENT '扩展 String 2',
ADD COLUMN extend_s3 varchar(500) COMMENT '扩展 String 3',
ADD COLUMN extend_s4 varchar(500) COMMENT '扩展 String 4',
ADD COLUMN extend_s5 varchar(500) COMMENT '扩展 String 5',
ADD COLUMN extend_s6 varchar(500) COMMENT '扩展 String 6',
ADD COLUMN extend_s7 varchar(500) COMMENT '扩展 String 7',
ADD COLUMN extend_s8 varchar(500) COMMENT '扩展 String 8',
ADD COLUMN extend_i1 decimal(19) COMMENT '扩展 Integer 1',
ADD COLUMN extend_i2 decimal(19) COMMENT '扩展 Integer 2',
ADD COLUMN extend_i3 decimal(19) COMMENT '扩展 Integer 3',
ADD COLUMN extend_i4 decimal(19) COMMENT '扩展 Integer 4',
ADD COLUMN extend_f1 decimal(19,4) COMMENT '扩展 Float 1',
ADD COLUMN extend_f2 decimal(19,4) COMMENT '扩展 Float 2',
ADD COLUMN extend_f3 decimal(19,4) COMMENT '扩展 Float 3',
ADD COLUMN extend_f4 decimal(19,4) COMMENT '扩展 Float 4',
ADD COLUMN extend_d1 datetime COMMENT '扩展 Date 1',
ADD COLUMN extend_d2 datetime COMMENT '扩展 Date 2',
ADD COLUMN extend_d3 datetime COMMENT '扩展 Date 3',
ADD COLUMN extend_d4 datetime COMMENT '扩展 Date 4';
