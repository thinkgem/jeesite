
-- 删除sys_file_entity表的file_md5唯一索引
-- ALTER TABLE "${_prefix}SYS_FILE_ENTITY" DROP CONSTRAINT "SYS_C0019398"

ALTER TABLE "${_prefix}SYS_JOB" 
ADD ("instance_name" VARCHAR2(64) DEFAULT JeeSiteScheduler NOT NULL );
COMMENT ON COLUMN "${_prefix}SYS_JOB"."instance_name" IS '集群的实例名字'