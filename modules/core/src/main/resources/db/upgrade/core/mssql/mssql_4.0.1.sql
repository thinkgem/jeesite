
-- 日志表新增执行时间字段
ALTER TABLE [dbo].[${_prefix}sys_log] ADD [execute_time] decimal(19) NULL;

-- 重命名待推送为已完成消息表
execute sp_rename N'${_prefix}sys_msg_push_wait', N'${_prefix}sys_msg_pushed';

-- 更新模块数据库版本
update ${_prefix}sys_module set current_version = '4.0.1' where module_code = 'core';
