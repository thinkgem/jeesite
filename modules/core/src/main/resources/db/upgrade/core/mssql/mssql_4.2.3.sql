
-- 文件上传表增加 20 个扩展字段
ALTER TABLE ${_prefix}sys_file_upload 
ADD extend_s1 nvarchar(500),
    extend_s2 nvarchar(500),
    extend_s3 nvarchar(500),
    extend_s4 nvarchar(500),
    extend_s5 nvarchar(500),
    extend_s6 nvarchar(500),
    extend_s7 nvarchar(500),
    extend_s8 nvarchar(500),
    extend_i1 decimal(19),
    extend_i2 decimal(19),
    extend_i3 decimal(19),
    extend_i4 decimal(19),
    extend_f1 decimal(19,4),
    extend_f2 decimal(19,4),
    extend_f3 decimal(19,4),
    extend_f4 decimal(19,4),
    extend_d1 datetime,
    extend_d2 datetime,
    extend_d3 datetime,
    extend_d4 datetime;
