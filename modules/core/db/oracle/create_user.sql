
create user jeesite
	identified by jeesite
	quota unlimited on users;
  
grant connect,resource,create session,select any table,
		create any view,create any table,create any index,
		drop any table,drop any view,drop any index
	to jeesite;
	
-- 提示 oracle.jdbc.xa.OracleXAResource.recover 错误需要授权：
grant select on sys.dba_pending_transactions to jeesite;
grant select on sys.pending_trans$ to jeesite;
grant select on sys.dba_2pc_pending to jeesite;
grant execute on sys.dbms_system to jeesite;
