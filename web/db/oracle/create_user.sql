
create user jeesite
	identified by jeesite
	quota unlimited on users;
  
grant connect,resource,create session,select any table,
		create any view,create any table,create any index,
		drop any table,drop any view,drop any index
	to jeesite;