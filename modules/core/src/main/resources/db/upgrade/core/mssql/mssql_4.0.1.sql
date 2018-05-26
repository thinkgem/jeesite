
-- 日志表新增执行时间字段
ALTER TABLE ${_prefix}sys_log ADD execute_time decimal(19) NULL;

-- 重命名待推送为已完成消息表
execute sp_rename N'${_prefix}sys_msg_push_wait', N'${_prefix}sys_msg_pushed';
