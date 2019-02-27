
-- 删除sys_file_entity表的file_md5唯一索引
ALTER TABLE "js_sys_file_entity" 
  DROP CONSTRAINT "js_sys_file_entity_file_md5_key";