
-- 日志表新增执行时间字段
ALTER TABLE ${_prefix}sys_log
ADD COLUMN `execute_time` decimal(19,0) NULL COMMENT '执行时间' AFTER `browser_name`;

-- 重命名待推送为已完成消息表
RENAME TABLE ${_prefix}sys_msg_push_wait TO ${_prefix}sys_msg_pushed;

commit;