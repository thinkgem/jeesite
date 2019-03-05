
-- 删除sys_file_entity表的file_md5唯一索引
ALTER TABLE "${_prefix}sys_file_entity" 
  DROP CONSTRAINT "${_prefix}sys_file_entity_file_md5_key";
  
-- 集群的实例名字
ALTER TABLE "${_prefix}sys_job" 
  ADD COLUMN "instance_name" varchar(255) NOT NULL DEFAULT JeeSiteScheduler;
COMMENT ON COLUMN "${_prefix}sys_job"."instance_name" IS '集群的实例名字';