-- set global read_only=0;
-- set global optimizer_switch='derived_merge=off';
create user 'jeesite'@'%' identified by 'jeesite';
create database jeesite DEFAULT CHARSET 'utf8mb4' COLLATE 'utf8mb4_unicode_ci';
-- MySql 5.x
-- grant all privileges on jeesite.* to 'jeesite'@'%' identified by 'jeesite';
-- MySql 8.x
grant all privileges on jeesite.* to 'jeesite'@'%';
flush privileges;
