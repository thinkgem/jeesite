alter table SYS_ROLE add (is_sys varchar2(64),useable varchar2(64));
comment on column SYS_ROLE.is_sys is '是否系统数据';
comment on column SYS_ROLE.useable is '是否启用';
update SYS_ROLE set useable='1';

alter table SYS_USER add (LOGIN_FLAG varchar2(64),PHOTO varchar2(100));
comment on column SYS_USER.LOGIN_FLAG is '是否可登陆';
comment on column SYS_USER.PHOTO is '头像';
update SYS_USER set LOGIN_FLAG='1';

alter table SYS_OFFICE add (USEABLE varchar2(64),PRIMARY_PERSON varchar2(64),DEPUTY_PERSON varchar2(64));
comment on column SYS_OFFICE.USEABLE is '是否可用';
comment on column SYS_OFFICE.PRIMARY_PERSON is '主负责人';
comment on column SYS_OFFICE.DEPUTY_PERSON is '副负责人';
update SYS_OFFICE set USEABLE='1';

insert into SYS_DICT (ID, VALUE, LABEL, TYPE, DESCRIPTION, SORT, PARENT_ID, CREATE_BY, CREATE_DATE, UPDATE_BY, UPDATE_DATE, REMARKS, DEL_FLAG)
values ('9619c52073564b5782451bfc40c48b36', '3', '小组', 'sys_office_type', '机构类型', '80', '0', '1', to_timestamp('08-01-2014 11:28:13.953000', 'dd-mm-yyyy hh24:mi:ss.ff'), '1', to_timestamp('08-01-2014 11:28:13.953000', 'dd-mm-yyyy hh24:mi:ss.ff'), '小组', '0');
insert into SYS_DICT (ID, VALUE, LABEL, TYPE, DESCRIPTION, SORT, PARENT_ID, CREATE_BY, CREATE_DATE, UPDATE_BY, UPDATE_DATE, REMARKS, DEL_FLAG)
values ('3d80ae9c017748cdb9515749486c81b7', '4', '其他', 'sys_office_type', '机构类型', '90', '0', '1', to_timestamp('08-01-2014 11:28:13.953000', 'dd-mm-yyyy hh24:mi:ss.ff'), '1', to_timestamp('08-01-2014 11:28:13.953000', 'dd-mm-yyyy hh24:mi:ss.ff'), '其他组织', '0');

insert into SYS_DICT (ID, VALUE, LABEL, TYPE, DESCRIPTION, SORT, PARENT_ID, CREATE_BY, CREATE_DATE, UPDATE_BY, UPDATE_DATE, REMARKS, DEL_FLAG)
values ('2a5ce7bd9ae44f8ca72555297e6c9066', 'zhb', '综合部', 'sys_office_common', '快捷通用部门', '10', '0', '1', to_timestamp('08-01-2014 11:28:13.953000', 'dd-mm-yyyy hh24:mi:ss.ff'), '1', to_timestamp('08-01-2014 11:28:13.953000', 'dd-mm-yyyy hh24:mi:ss.ff'), '综合部', '0');
insert into SYS_DICT (ID, VALUE, LABEL, TYPE, DESCRIPTION, SORT, PARENT_ID, CREATE_BY, CREATE_DATE, UPDATE_BY, UPDATE_DATE, REMARKS, DEL_FLAG)
values ('6588dd604ca24c5183d765ebcda2e245', 'kfb', '开发部', 'sys_office_common', '快捷通用部门', '10', '0', '1', to_timestamp('08-01-2014 11:28:13.953000', 'dd-mm-yyyy hh24:mi:ss.ff'), '1', to_timestamp('08-01-2014 11:28:13.953000', 'dd-mm-yyyy hh24:mi:ss.ff'), '开发部', '0');
insert into SYS_DICT (ID, VALUE, LABEL, TYPE, DESCRIPTION, SORT, PARENT_ID, CREATE_BY, CREATE_DATE, UPDATE_BY, UPDATE_DATE, REMARKS, DEL_FLAG)
values ('ee1185d31e5b41d8b0cdb45dd83a95d1', 'rlb', '人力部', 'sys_office_common', '快捷通用部门', '10', '0', '1', to_timestamp('08-01-2014 11:28:13.953000', 'dd-mm-yyyy hh24:mi:ss.ff'), '1', to_timestamp('08-01-2014 11:28:13.953000', 'dd-mm-yyyy hh24:mi:ss.ff'), '人力部', '0');

