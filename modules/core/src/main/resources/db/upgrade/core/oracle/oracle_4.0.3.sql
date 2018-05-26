
INSERT INTO ${_prefix}sys_menu (menu_code, parent_code, parent_codes, tree_sort, tree_sorts, tree_leaf, tree_level, tree_names, menu_name, menu_type, menu_href, menu_target, menu_icon, menu_color, permission, weight, is_show, sys_code, module_codes, status, create_by, create_date, update_by, update_date, remarks) 
VALUES ('989516261885186048', '0', '0,', '9030', '0000009030,', '0', '0', '消息推送', '消息推送', '1', '', '', 'icon-envelope-letter', '', '', '60', '0', 'default', 'core', '0', 'system', sysdate, 'system', sysdate, '');
INSERT INTO ${_prefix}sys_menu (menu_code, parent_code, parent_codes, tree_sort, tree_sorts, tree_leaf, tree_level, tree_names, menu_name, menu_type, menu_href, menu_target, menu_icon, menu_color, permission, weight, is_show, sys_code, module_codes, status, create_by, create_date, update_by, update_date, remarks) 
VALUES ('989516673799393280', '989516261885186048', '0,989516261885186048,', '30', '0000009030,0000000030,', '1', '1', '消息推送/未完成消息', '未完成消息', '1', '/msg/msgPush/list', '', '', '', 'msg:msgPush', '60', '1', 'default', 'core', '0', 'system', sysdate, 'system', sysdate, '');
INSERT INTO ${_prefix}sys_menu (menu_code, parent_code, parent_codes, tree_sort, tree_sorts, tree_leaf, tree_level, tree_names, menu_name, menu_type, menu_href, menu_target, menu_icon, menu_color, permission, weight, is_show, sys_code, module_codes, status, create_by, create_date, update_by, update_date, remarks) 
VALUES ('989516723044716544', '989516261885186048', '0,989516261885186048,', '60', '0000009030,0000000060,', '1', '1', '消息推送/已完成消息', '已完成消息', '1', '/msg/msgPush/list?pushed=true', '', '', '', 'msg:msgPush', '60', '1', 'default', 'core', '0', 'system', sysdate, 'system', sysdate, '');
INSERT INTO ${_prefix}sys_menu (menu_code, parent_code, parent_codes, tree_sort, tree_sorts, tree_leaf, tree_level, tree_names, menu_name, menu_type, menu_href, menu_target, menu_icon, menu_color, permission, weight, is_show, sys_code, module_codes, status, create_by, create_date, update_by, update_date, remarks) 
VALUES ('989516785057501184', '989516261885186048', '0,989516261885186048,', '90', '0000009030,0000000090,', '1', '1', '消息推送/消息模板管理', '消息模板管理', '1', '/msg/msgTemplate/list', '', '', '', 'msg:msgTemplate', '60', '1', 'default', 'core', '0', 'system', sysdate, 'system', sysdate, '');

ALTER TABLE ${_prefix}sys_msg_push MODIFY ( push_date TIMESTAMP );
ALTER TABLE ${_prefix}sys_msg_push MODIFY ( read_date TIMESTAMP );
ALTER TABLE ${_prefix}sys_msg_pushed MODIFY ( push_date TIMESTAMP );
ALTER TABLE ${_prefix}sys_msg_pushed MODIFY ( read_date TIMESTAMP );

INSERT INTO ${_prefix}sys_job (job_name, job_group, description, invoke_target, cron_expression, misfire_instruction, concurrent, status, create_by, create_date, update_by, update_date, remarks)
VALUES ('MsgLocalMergePushTask', 'SYSTEM', '消息推送服务 (延迟)', 'msgLocalMergePushTask.execute()', '0 0/30 * * * ?', '2', '0', '2', 'system', sysdate, 'system', sysdate, NULL);
INSERT INTO ${_prefix}sys_job (job_name, job_group, description, invoke_target, cron_expression, misfire_instruction, concurrent, status, create_by, create_date, update_by, update_date, remarks)
VALUES ('MsgLocalPushTask', 'SYSTEM', '消息推送服务 (即时)', 'msgLocalPushTask.execute()', '0/3 * * * * ?', '2', '0', '2', 'system', sysdate, 'system', sysdate, NULL);

commit;