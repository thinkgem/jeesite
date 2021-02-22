
-- 文件上传表增加 20 个扩展字段
ALTER TABLE `${_prefix}sys_file_upload` 
ADD COLUMN extend_s1 varchar(500) COMMENT '扩展 String 1',
ADD COLUMN extend_s2 varchar(500) COMMENT '扩展 String 2',
ADD COLUMN extend_s3 varchar(500) COMMENT '扩展 String 3',
ADD COLUMN extend_s4 varchar(500) COMMENT '扩展 String 4',
ADD COLUMN extend_s5 varchar(500) COMMENT '扩展 String 5',
ADD COLUMN extend_s6 varchar(500) COMMENT '扩展 String 6',
ADD COLUMN extend_s7 varchar(500) COMMENT '扩展 String 7',
ADD COLUMN extend_s8 varchar(500) COMMENT '扩展 String 8',
ADD COLUMN extend_i1 decimal(19) COMMENT '扩展 Integer 1',
ADD COLUMN extend_i2 decimal(19) COMMENT '扩展 Integer 2',
ADD COLUMN extend_i3 decimal(19) COMMENT '扩展 Integer 3',
ADD COLUMN extend_i4 decimal(19) COMMENT '扩展 Integer 4',
ADD COLUMN extend_f1 decimal(19,4) COMMENT '扩展 Float 1',
ADD COLUMN extend_f2 decimal(19,4) COMMENT '扩展 Float 2',
ADD COLUMN extend_f3 decimal(19,4) COMMENT '扩展 Float 3',
ADD COLUMN extend_f4 decimal(19,4) COMMENT '扩展 Float 4',
ADD COLUMN extend_d1 datetime COMMENT '扩展 Date 1',
ADD COLUMN extend_d2 datetime COMMENT '扩展 Date 2',
ADD COLUMN extend_d3 datetime COMMENT '扩展 Date 3',
ADD COLUMN extend_d4 datetime COMMENT '扩展 Date 4';
