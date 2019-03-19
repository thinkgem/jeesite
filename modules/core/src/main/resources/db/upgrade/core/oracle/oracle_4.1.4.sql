
-- 删除sys_file_entity表的file_md5唯一索引
-- ALTER TABLE ${_prefix}SYS_FILE_ENTITY DROP CONSTRAINT SYS_C0019398

-- 增加文件信息字段（可存储图片大小）
ALTER TABLE ${_prefix}SYS_FILE_ENTITY 
ADD (file_meta VARCHAR2(64) NULL );
COMMENT ON COLUMN ${_prefix}SYS_FILE_ENTITY.file_meta IS '文件信息(JSON格式)';

-- 集群的实例名字  
ALTER TABLE ${_prefix}SYS_JOB 
ADD (instance_name VARCHAR2(64) DEFAULT 'JeeSiteScheduler' NOT NULL );
COMMENT ON COLUMN ${_prefix}SYS_JOB.instance_name IS '集群的实例名字';

-- 内部消息菜单
INSERT INTO ${_prefix}sys_menu(menu_code, parent_code, parent_codes, tree_sort, tree_sorts, tree_leaf, tree_level, tree_names, menu_name, menu_type, menu_href, menu_target, menu_icon, menu_color, permission, weight, is_show, sys_code, module_codes, status, create_by, create_date, update_by, update_date, remarks, extend_s1, extend_s2, extend_s3, extend_s4, extend_s5, extend_s6, extend_s7, extend_s8, extend_i1, extend_i2, extend_i3, extend_i4, extend_f1, extend_f2, extend_f3, extend_f4, extend_d1, extend_d2, extend_d3, extend_d4)
VALUES ('1105443204287991808', '0', '0,', 9030, '0000009030,', '1', 0, '站内消息', '站内消息', '1', '/msg/msgInner/list', '', 'icon-speech', '', 'msg:msgInner', 40, '1', 'default', 'core', '0', 'system', sysdate, 'system', sysdate, '', '', '', '', '', '', '', '', '', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);

-- 内部消息新增字典数据
INSERT INTO ${_prefix}sys_dict_type(id, dict_name, dict_type, is_sys, status, create_by, create_date, update_by, update_date, remarks)
VALUES ('1105440848414543872', '消息状态', 'msg_inner_msg_status', '0', '0', 'system', sysdate, 'system', sysdate, '');
INSERT INTO ${_prefix}sys_dict_data(dict_code, parent_code, parent_codes, tree_sort, tree_sorts, tree_leaf, tree_level, tree_names, dict_label, dict_value, dict_type, is_sys, description, css_style, css_class, status, create_by, create_date, update_by, update_date, remarks, corp_code, corp_name, extend_s1, extend_s2, extend_s3, extend_s4, extend_s5, extend_s6, extend_s7, extend_s8, extend_i1, extend_i2, extend_i3, extend_i4, extend_f1, extend_f2, extend_f3, extend_f4, extend_d1, extend_d2, extend_d3, extend_d4)
VALUES ('1106135527342673920', '0', '0,', 20, '0000000020,', '1', 0, '全部', '全部', '0', 'msg_inner_receiver_type', '1', '', '', '', '0', 'system', sysdate, 'system', sysdate, '', '0', 'JeeSite', '', '', '', '', '', '', '', '', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);

-- 取消内部消息部分字段必填选项
ALTER TABLE ${_prefix}SYS_MSG_INNER MODIFY (RECEIVE_CODES NULL );
ALTER TABLE ${_prefix}SYS_MSG_INNER MODIFY (RECEIVE_NAMES NULL );
ALTER TABLE ${_prefix}SYS_MSG_INNER MODIFY (SEND_USER_CODE NULL );
ALTER TABLE ${_prefix}SYS_MSG_INNER MODIFY (SEND_USER_NAME NULL );
ALTER TABLE ${_prefix}SYS_MSG_INNER MODIFY (SEND_DATE NULL );
ALTER TABLE ${_prefix}SYS_MSG_INNER MODIFY (NOTIFY_TYPES NULL );

