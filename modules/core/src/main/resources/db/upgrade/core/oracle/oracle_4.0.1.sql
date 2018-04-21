
-- 日志表新增执行时间字段
ALTER TABLE ${_prefix}sys_log ADD execute_time NUMBER(19) NULL;
COMMENT ON COLUMN ${_prefix}sys_log.execute_time IS '执行时间';

-- 重命名待推送为已完成消息表
ALTER TABLE ${_prefix}sys_msg_push_wait RENAME TO ${_prefix}sys_msg_pushed;

commit;