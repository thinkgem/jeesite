-- set global read_only=0;
-- set global optimizer_switch='derived_merge=off';
create user 'nacos'@'%' identified by 'nacos';
create database nacos DEFAULT CHARSET 'utf8mb4' COLLATE 'utf8mb4_unicode_ci';
-- MySql 5.x
-- grant all privileges on nacos.* to 'nacos'@'%' identified by 'nacos';
-- MySql 8.x
grant all privileges on nacos.* to 'nacos'@'%';
flush privileges;
