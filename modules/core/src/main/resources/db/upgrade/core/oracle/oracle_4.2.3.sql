
-- 文件上传表增加 20 个扩展字段
ALTER TABLE ${_prefix}sys_file_upload 
ADD (extend_s1 nvarchar2(500),
    extend_s2 nvarchar2(500),
    extend_s3 nvarchar2(500),
    extend_s4 nvarchar2(500),
    extend_s5 nvarchar2(500),
    extend_s6 nvarchar2(500),
    extend_s7 nvarchar2(500),
    extend_s8 nvarchar2(500),
    extend_i1 number(19),
    extend_i2 number(19),
    extend_i3 number(19),
    extend_i4 number(19),
    extend_f1 number(19,4),
    extend_f2 number(19,4),
    extend_f3 number(19,4),
    extend_f4 number(19,4),
    extend_d1 timestamp,
    extend_d2 timestamp,
    extend_d3 timestamp,
    extend_d4 timestamp);
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
