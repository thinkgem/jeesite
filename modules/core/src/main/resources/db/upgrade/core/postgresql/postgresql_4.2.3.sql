
-- 文件上传表增加 20 个扩展字段
ALTER TABLE ${_prefix}sys_file_upload 
ADD COLUMN extend_s1 varchar(500),
ADD COLUMN extend_s2 varchar(500),
ADD COLUMN extend_s3 varchar(500),
ADD COLUMN extend_s4 varchar(500),
ADD COLUMN extend_s5 varchar(500),
ADD COLUMN extend_s6 varchar(500),
ADD COLUMN extend_s7 varchar(500),
ADD COLUMN extend_s8 varchar(500),
ADD COLUMN extend_i1 decimal(19),
ADD COLUMN extend_i2 decimal(19),
ADD COLUMN extend_i3 decimal(19),
ADD COLUMN extend_i4 decimal(19),
ADD COLUMN extend_f1 decimal(19,4),
ADD COLUMN extend_f2 decimal(19,4),
ADD COLUMN extend_f3 decimal(19,4),
ADD COLUMN extend_f4 decimal(19,4),
ADD COLUMN extend_d1 timestamp,
ADD COLUMN extend_d2 timestamp,
ADD COLUMN extend_d3 timestamp,
ADD COLUMN extend_d4 timestamp;
COMMENT ON COLUMN ${_prefix}sys_file_upload.extend_s1 IS '扩展 String 1';
COMMENT ON COLUMN ${_prefix}sys_file_upload.extend_s2 IS '扩展 String 2';
COMMENT ON COLUMN ${_prefix}sys_file_upload.extend_s3 IS '扩展 String 3';
COMMENT ON COLUMN ${_prefix}sys_file_upload.extend_s4 IS '扩展 String 4';
COMMENT ON COLUMN ${_prefix}sys_file_upload.extend_s5 IS '扩展 String 5';
COMMENT ON COLUMN ${_prefix}sys_file_upload.extend_s6 IS '扩展 String 6';
COMMENT ON COLUMN ${_prefix}sys_file_upload.extend_s7 IS '扩展 String 7';
COMMENT ON COLUMN ${_prefix}sys_file_upload.extend_s8 IS '扩展 String 8';
COMMENT ON COLUMN ${_prefix}sys_file_upload.extend_i1 IS '扩展 Integer 1';
COMMENT ON COLUMN ${_prefix}sys_file_upload.extend_i2 IS '扩展 Integer 2';
COMMENT ON COLUMN ${_prefix}sys_file_upload.extend_i3 IS '扩展 Integer 3';
COMMENT ON COLUMN ${_prefix}sys_file_upload.extend_i4 IS '扩展 Integer 4';
COMMENT ON COLUMN ${_prefix}sys_file_upload.extend_f1 IS '扩展 Float 1';
COMMENT ON COLUMN ${_prefix}sys_file_upload.extend_f2 IS '扩展 Float 2';
COMMENT ON COLUMN ${_prefix}sys_file_upload.extend_f3 IS '扩展 Float 3';
COMMENT ON COLUMN ${_prefix}sys_file_upload.extend_f4 IS '扩展 Float 4';
COMMENT ON COLUMN ${_prefix}sys_file_upload.extend_d1 IS '扩展 Date 1';
COMMENT ON COLUMN ${_prefix}sys_file_upload.extend_d2 IS '扩展 Date 2';
COMMENT ON COLUMN ${_prefix}sys_file_upload.extend_d3 IS '扩展 Date 3';
COMMENT ON COLUMN ${_prefix}sys_file_upload.extend_d4 IS '扩展 Date 4';
