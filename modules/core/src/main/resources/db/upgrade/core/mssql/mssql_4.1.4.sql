
-- 删除sys_file_entity表的file_md5唯一索引
-- ALTER TABLE [${_prefix}sys_file_entity] DROP CONSTRAINT [UQ__js_sys_f__3BD63C9E32E0915F];

ALTER TABLE [${_prefix}sys_job] ADD [instance_name] varchar(64) DEFAULT JeeSiteScheduler NOT NULL;
