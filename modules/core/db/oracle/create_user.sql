
create user jeesite
  identified by jeesite
  quota unlimited on users;
  
grant connect,resource,create any view to jeesite;
