
-- 日志表新增执行时间字段
ALTER TABLE ${_prefix}sys_log ADD COLUMN execute_time decimal(19);
COMMENT ON COLUMN ${_prefix}sys_log.execute_time IS '执行时间';

-- 更新模块数据库版本
update ${_prefix}sys_module set current_version = '4.0.1' where module_code = 'core';
commit;