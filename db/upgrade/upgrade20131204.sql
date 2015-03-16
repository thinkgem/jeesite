alter table SYS_MENU modify PARENT_IDS VARCHAR2(2000);

alter table SYS_AREA modify PARENT_IDS VARCHAR2(2000);
alter table SYS_AREA add sort NUMBER(10);
comment on column SYS_AREA.sort is '排序';
update SYS_AREA set sort = 30;

alter table SYS_OFFICE modify PARENT_IDS VARCHAR2(2000);
alter table SYS_OFFICE add sort NUMBER(10);
comment on column SYS_OFFICE.sort is '排序';
update SYS_OFFICE set sort = 30;

alter table SYS_DICT modify PARENT_IDS VARCHAR2(2000);