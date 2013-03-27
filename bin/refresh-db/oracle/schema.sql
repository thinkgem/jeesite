
-- prompt
-- prompt Creating table CMS_ARTICLE
-- prompt ==========================
-- prompt
drop table CMS_ARTICLE;
create table CMS_ARTICLE
(
  ID          NUMBER(20) not null,
  CATEGORY_ID NUMBER(20) not null,
  USER_ID     NUMBER(20) not null,
  TITLE       VARCHAR2(255) not null,
  COLOR       VARCHAR2(50),
  THUMB       VARCHAR2(255),
  KEYWORDS    VARCHAR2(255),
  DESCIPTION  VARCHAR2(255),
  STATUS      CHAR(1) default 0,
  WEIGHT      NUMBER(11),
  HITS        NUMBER(11),
  POSID       VARCHAR2(10),
  INPUT_DATE  DATE default sysdate,
  UPDATE_DATE DATE
);
alter table CMS_ARTICLE
  add constraint CMS_ARTICLE_PRIMARYKEY primary key (ID)
  using index ;
create index CMS_ARTICLE_INPUT_DATE on CMS_ARTICLE (INPUT_DATE);
create index CMS_ARTICLE_KEYWORDS on CMS_ARTICLE (KEYWORDS);
create index CMS_ARTICLE_STATUS on CMS_ARTICLE (STATUS);
create index CMS_ARTICLE_TITLE on CMS_ARTICLE (TITLE);
create index CMS_ARTICLE_UPDATE_DATE on CMS_ARTICLE (UPDATE_DATE);
create index CMS_ARTICLE_USER_ID on CMS_ARTICLE (USER_ID);
create index CMS_ARTICLE_WEIGHT on CMS_ARTICLE (WEIGHT);

-- prompt
-- prompt Creating table CMS_ARTICLE_DATA
-- prompt ===============================
-- prompt
drop table CMS_ARTICLE_DATA;
create table CMS_ARTICLE_DATA
(
  ID            NUMBER(20) not null,
  CONTENT       CLOB,
  COPYFROM      VARCHAR2(255),
  RELATION      VARCHAR2(255),
  ALLOW_COMMENT VARCHAR2(1)
);
alter table CMS_ARTICLE_DATA
  add constraint CMS_ARTICLE_DATA_PRIMARYKEY primary key (ID)
  using index ;

-- prompt
-- prompt Creating table CMS_CATEGORY
-- prompt ===========================
-- prompt
drop table CMS_CATEGORY;
create table CMS_CATEGORY
(
  ID            NUMBER(20) not null,
  SITE_ID       NUMBER(20),
  PARENT_ID     NUMBER(20) not null,
  PARENT_IDS    VARCHAR2(255) not null,
  MODULE        VARCHAR2(20),
  NAME          VARCHAR2(100) not null,
  IMAGE         VARCHAR2(255),
  HREF          VARCHAR2(255),
  TARGET        VARCHAR2(20),
  DESCIPTION    VARCHAR2(255),
  KEYWORDS      VARCHAR2(255),
  SORT          NUMBER(11),
  IN_MENU       VARCHAR2(1),
  IN_LIST       VARCHAR2(1),
  SHOW_MODES    VARCHAR2(1),
  ALLOW_COMMENT VARCHAR2(1),
  DEL_FLAG      CHAR(1) default 0
);
alter table CMS_CATEGORY
  add constraint CMS_CATEGORY_PRIMARYKEY primary key (ID)
  using index ;
create index CMS_CATEGORY_DEL_FLAG on CMS_CATEGORY (DEL_FLAG);
create index CMS_CATEGORY_MODULE on CMS_CATEGORY (MODULE);
create index CMS_CATEGORY_NAME on CMS_CATEGORY (NAME);
create index CMS_CATEGORY_PARENT_ID on CMS_CATEGORY (PARENT_ID);
create index CMS_CATEGORY_PARENT_IDS on CMS_CATEGORY (PARENT_IDS);
create index CMS_CATEGORY_SORT on CMS_CATEGORY (SORT);

-- prompt
-- prompt Creating table CMS_COMMENT
-- prompt ==========================
-- prompt
drop table CMS_COMMENT;
create table CMS_COMMENT
(
  ID            NUMBER(20) not null,
  MODULE        VARCHAR2(20) not null,
  CONTENT_ID    NUMBER(20) not null,
  TITLE         VARCHAR2(255),
  CONTENT       VARCHAR2(255),
  NAME          VARCHAR2(100),
  IP            VARCHAR2(100),
  CREATE_DATE  DATE default sysdate,
  AUDIT_USER_ID NUMBER(20),
  AUDIT_DATE    DATE,
  STATUS      CHAR(1) default 0
);
alter table CMS_COMMENT
  add constraint CMS_COMMENT_PRIMARYKEY primary key (ID)
  using index ;
create index CMS_COMMENT_CONTENT_ID on CMS_COMMENT (CONTENT_ID);
create index CMS_COMMENT_MODULE on CMS_COMMENT (MODULE);
create index CMS_COMMENT_STATUS on CMS_COMMENT (STATUS);

-- prompt
-- prompt Creating table CMS_GUESTBOOK
-- prompt ============================
-- prompt
drop table CMS_GUESTBOOK;
create table CMS_GUESTBOOK
(
  ID          NUMBER(20) not null,
  TYPE        VARCHAR2(100) not null,
  CONTENT     VARCHAR2(255) not null,
  NAME        VARCHAR2(100) not null,
  EMAIL       VARCHAR2(100) not null,
  PHONE       VARCHAR2(100) not null,
  WORKUNIT    VARCHAR2(100) not null,
  IP          VARCHAR2(100) not null,
  CREATE_DATE DATE default sysdate,
  RE_USER_ID  NUMBER(20),
  RE_DATE     DATE,
  RE_CONTENT  VARCHAR2(100),
  STATUS      CHAR(1) default 0
);
alter table CMS_GUESTBOOK
  add constraint CMS_GUESTBOOK_PRIMARYKEY primary key (ID)
  using index ;
create index CMS_GUESTBOOK_STATUS on CMS_GUESTBOOK (STATUS);

-- prompt
-- prompt Creating table CMS_LINK
-- prompt =======================
-- prompt
drop table CMS_LINK;
create table CMS_LINK
(
  ID          NUMBER(20) not null,
  CATEGORY_ID NUMBER(20) not null,
  USER_ID     NUMBER(20) not null,
  TITLE       VARCHAR2(255) not null,
  COLOR       VARCHAR2(50),
  IMAGE       VARCHAR2(255),
  HREF        VARCHAR2(255),
  REMARKS     VARCHAR2(255),
  STATUS      CHAR(1) default 0,
  WEIGHT      NUMBER(11),
  INPUT_DATE  DATE default sysdate,
  UPDATE_DATE DATE
);
alter table CMS_LINK
  add constraint CMS_LINK_PRIMARYKEY primary key (ID)
  using index ;
create index CMS_LINK_INPUT_DATE on CMS_LINK (INPUT_DATE);
create index CMS_LINK_STATUS on CMS_LINK (STATUS);
create index CMS_LINK_TITLE on CMS_LINK (TITLE);
create index CMS_LINK_UPDATE_DATE on CMS_LINK (UPDATE_DATE);
create index CMS_LINK_USER_ID on CMS_LINK (USER_ID);
create index CMS_LINK_WEIGHT on CMS_LINK (WEIGHT);

-- prompt
-- prompt Creating table CMS_SITE
-- prompt =======================
-- prompt
drop table CMS_SITE;
create table CMS_SITE
(
  ID         NUMBER(20) not null,
  NAME       VARCHAR2(100) not null,
  TITLE      VARCHAR2(100) not null,
  DESCIPTION VARCHAR2(255),
  KEYWORDS   VARCHAR2(255),
  THEME      VARCHAR2(255),
  COPYRIGHT  CLOB,
  DEL_FLAG   CHAR(1) default 0
);
alter table CMS_SITE
  add constraint CMS_SITE_PRIMARYKEY primary key (ID)
  using index ;
create index CMS_SITE_DEL_FLAG on CMS_SITE (DEL_FLAG);

-- prompt
-- prompt Creating table SYS_AREA
-- prompt =======================
-- prompt
drop table SYS_AREA;
create table SYS_AREA
(
  ID         NUMBER(20) not null,
  PARENT_ID  NUMBER(20) not null,
  PARENT_IDS VARCHAR2(255) not null,
  CODE       VARCHAR2(100),
  NAME       VARCHAR2(100),
  REMARKS    VARCHAR2(255),
  DEL_FLAG   CHAR(1) default 0
);
alter table SYS_AREA
  add constraint SYS_AREA_PRIMARYKEY primary key (ID)
  using index ;
create index SYS_AREA_DEL_FLAG on SYS_AREA (DEL_FLAG);
create index SYS_AREA_PARENT_ID on SYS_AREA (PARENT_ID);
create index SYS_AREA_PARENT_IDS on SYS_AREA (PARENT_IDS);

-- prompt
-- prompt Creating table SYS_DICT
-- prompt =======================
-- prompt
drop table SYS_DICT;
create table SYS_DICT
(
  ID         NUMBER(20) not null,
  LABEL      VARCHAR2(100) not null,
  VALUE      VARCHAR2(100) not null,
  TYPE       VARCHAR2(100) not null,
  DESCIPTION VARCHAR2(100) not null,
  SORT       NUMBER(11) not null,
  DEL_FLAG   CHAR(1) default 0
);
alter table SYS_DICT
  add constraint SYS_DICT_PRIMARYKEY primary key (ID)
  using index ;
create index SYS_DICT_DEL_FLAG on SYS_DICT (DEL_FLAG);
create index SYS_DICT_LABEL on SYS_DICT (LABEL);
create index SYS_DICT_VALUE on SYS_DICT (VALUE);

-- prompt
-- prompt Creating table SYS_MENU
-- prompt =======================
-- prompt
drop table SYS_MENU;
create table SYS_MENU
(
  ID         NUMBER(20) not null,
  PARENT_ID  NUMBER(20) not null,
  PARENT_IDS VARCHAR2(255) not null,
  NAME       VARCHAR2(100) not null,
  HREF       VARCHAR2(255),
  TARGET     VARCHAR2(20),
  ICON       VARCHAR2(100),
  SORT       NUMBER(11) not null,
  IS_SHOW    VARCHAR2(1) not null,
  PERMISSION VARCHAR2(200),
  DEL_FLAG   CHAR(1) default 0
);
alter table SYS_MENU
  add constraint SYS_MENU_PRIMARYKEY primary key (ID)
  using index ;
create index SYS_MENU_DEL_FLAG on SYS_MENU (DEL_FLAG);
create index SYS_MENU_PARENT_ID on SYS_MENU (PARENT_ID);
create index SYS_MENU_PARENT_IDS on SYS_MENU (PARENT_IDS);

-- prompt
-- prompt Creating table SYS_OFFICE
-- prompt =========================
-- prompt
drop table SYS_OFFICE;
create table SYS_OFFICE
(
  ID         NUMBER(20) not null,
  PARENT_ID  NUMBER(20) not null,
  PARENT_IDS VARCHAR2(255) not null,
  AREA_ID    NUMBER(20) not null,
  CODE       VARCHAR2(100),
  NAME       VARCHAR2(100) not null,
  REMARKS    VARCHAR2(255),
  DEL_FLAG   CHAR(1) default 0
);
alter table SYS_OFFICE
  add constraint SYS_OFFICE_PRIMARYKEY primary key (ID)
  using index ;
create index SYS_OFFICE_DEL_FLAG on SYS_OFFICE (DEL_FLAG);
create index SYS_OFFICE_PARENT_ID on SYS_OFFICE (PARENT_ID);
create index SYS_OFFICE_PARENT_IDS on SYS_OFFICE (PARENT_IDS);

-- prompt
-- prompt Creating table SYS_ROLE
-- prompt =======================
-- prompt
drop table SYS_ROLE;
create table SYS_ROLE
(
  ID       NUMBER(20) not null,
  NAME     VARCHAR2(100) not null,
  DEL_FLAG CHAR(1) default 0
);
alter table SYS_ROLE
  add constraint SYS_ROLE_PRIMARYKEY primary key (ID)
  using index ;
create index SYS_ROLE_DEL_FLAG on SYS_ROLE (DEL_FLAG);

-- prompt
-- prompt Creating table SYS_ROLE_CATEGORY
-- prompt ================================
-- prompt
drop table SYS_ROLE_CATEGORY;
create table SYS_ROLE_CATEGORY
(
  ROLE_ID     NUMBER(20) not null,
  CATEGORY_ID NUMBER(20) not null
);
alter table SYS_ROLE_CATEGORY
  add constraint SYS_ROLE_CATEGORY_PRIMARYKEY primary key (ROLE_ID, CATEGORY_ID)
  using index ;

-- prompt
-- prompt Creating table SYS_ROLE_MENU
-- prompt ============================
-- prompt
drop table SYS_ROLE_MENU;
create table SYS_ROLE_MENU
(
  ROLE_ID NUMBER(20) not null,
  MENU_ID NUMBER(20) not null
);
alter table SYS_ROLE_MENU
  add constraint SYS_ROLE_MENU_PRIMARYKEY primary key (ROLE_ID, MENU_ID)
  using index ;

-- prompt
-- prompt Creating table SYS_USER
-- prompt =======================
-- prompt
drop table SYS_USER;
create table SYS_USER
(
  ID          NUMBER(20) not null,
  AREA_ID     NUMBER(20) not null,
  OFFICE_ID   NUMBER(20) not null,
  LOGIN_NAME  VARCHAR2(100) not null,
  PASSWORD    VARCHAR2(100),
  NAME        VARCHAR2(100),
  EMAIL       VARCHAR2(200),
  PHONE       VARCHAR2(200),
  MOBILE      VARCHAR2(200),
  REMARKS     VARCHAR2(255),
  USER_TYPE   VARCHAR2(100),
  CREATE_DATE DATE default sysdate,
  DEL_FLAG    CHAR(1) default 0,
  LOGIN_IP    VARCHAR2(100),
  LOGIN_DATE  DATE
);
alter table SYS_USER
  add constraint SYS_USER_PRIMARYKEY primary key (ID)
  using index ;
create index SYS_USER_AREA_ID on SYS_USER (AREA_ID);
create index SYS_USER_DEL_FLAG on SYS_USER (DEL_FLAG);
create index SYS_USER_LOGIN_NAME on SYS_USER (LOGIN_NAME);
create index SYS_USER_OFFICE_ID on SYS_USER (OFFICE_ID);

-- prompt
-- prompt Creating table SYS_USER_ROLE
-- prompt ============================
-- prompt
drop table SYS_USER_ROLE;
create table SYS_USER_ROLE
(
  USER_ID NUMBER(20) not null,
  ROLE_ID NUMBER(20) not null
);
alter table SYS_USER_ROLE
  add constraint SYS_USER_ROLE_PRIMARYKEY primary key (USER_ID, ROLE_ID)
  using index ;

-- prompt
-- prompt Creating sequence SEQ_CMS_ARTICLE
-- prompt =================================
-- prompt
drop sequence SEQ_CMS_ARTICLE;
create sequence SEQ_CMS_ARTICLE
minvalue 1
maxvalue 999999999999999999999999999
start with 54
increment by 1
cache 20;

-- prompt
-- prompt Creating sequence SEQ_CMS_ARTICLE_DATA
-- prompt ======================================
-- prompt
drop sequence SEQ_CMS_ARTICLE_DATA;
create sequence SEQ_CMS_ARTICLE_DATA
minvalue 1
maxvalue 999999999999999999999999999
start with 54
increment by 1
cache 20;

-- prompt
-- prompt Creating sequence SEQ_CMS_CATEGORY
-- prompt ==================================
-- prompt
drop sequence SEQ_CMS_CATEGORY;
create sequence SEQ_CMS_CATEGORY
minvalue 1
maxvalue 999999999999999999999999999
start with 28
increment by 1
cache 20;

-- prompt
-- prompt Creating sequence SEQ_CMS_COMMENT
-- prompt =================================
-- prompt
drop sequence SEQ_CMS_COMMENT;
create sequence SEQ_CMS_COMMENT
minvalue 1
maxvalue 999999999999999999999999999
start with 1
increment by 1
cache 20;

-- prompt
-- prompt Creating sequence SEQ_CMS_GUESTBOOK
-- prompt ===================================
-- prompt
drop sequence SEQ_CMS_GUESTBOOK;
create sequence SEQ_CMS_GUESTBOOK
minvalue 1
maxvalue 999999999999999999999999999
start with 1
increment by 1
cache 20;

-- prompt
-- prompt Creating sequence SEQ_CMS_LINK
-- prompt ==============================
-- prompt
drop sequence SEQ_CMS_LINK;
create sequence SEQ_CMS_LINK
minvalue 1
maxvalue 999999999999999999999999999
start with 13
increment by 1
cache 20;

-- prompt
-- prompt Creating sequence SEQ_CMS_SITE
-- prompt ==============================
-- prompt
drop sequence SEQ_CMS_SITE;
create sequence SEQ_CMS_SITE
minvalue 1
maxvalue 999999999999999999999999999
start with 3
increment by 1
cache 20;

-- prompt
-- prompt Creating sequence SEQ_SYS_AREA
-- prompt ==============================
-- prompt
drop sequence SEQ_SYS_AREA;
create sequence SEQ_SYS_AREA
minvalue 1
maxvalue 999999999999999999999999999
start with 6
increment by 1
cache 20;

-- prompt
-- prompt Creating sequence SEQ_SYS_DICT
-- prompt ==============================
-- prompt
drop sequence SEQ_SYS_DICT;
create sequence SEQ_SYS_DICT
minvalue 1
maxvalue 999999999999999999999999999
start with 35
increment by 1
cache 20;

-- prompt
-- prompt Creating sequence SEQ_SYS_MENU
-- prompt ==============================
-- prompt
drop sequence SEQ_SYS_MENU;
create sequence SEQ_SYS_MENU
minvalue 1
maxvalue 999999999999999999999999999
start with 60
increment by 1
cache 20;

-- prompt
-- prompt Creating sequence SEQ_SYS_OFFICE
-- prompt ================================
-- prompt
drop sequence SEQ_SYS_OFFICE;
create sequence SEQ_SYS_OFFICE
minvalue 1
maxvalue 999999999999999999999999999
start with 8
increment by 1
cache 20;

-- prompt
-- prompt Creating sequence SEQ_SYS_ROLE
-- prompt ==============================
-- prompt
drop sequence SEQ_SYS_ROLE;
create sequence SEQ_SYS_ROLE
minvalue 1
maxvalue 999999999999999999999999999
start with 3
increment by 1
cache 20;

-- prompt
-- prompt Creating sequence SEQ_SYS_USER
-- prompt ==============================
-- prompt
drop sequence SEQ_SYS_USER;
create sequence SEQ_SYS_USER
minvalue 1
maxvalue 999999999999999999999999999
start with 15
increment by 1
cache 20;
