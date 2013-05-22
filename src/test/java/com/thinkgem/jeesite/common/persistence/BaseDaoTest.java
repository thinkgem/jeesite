package com.thinkgem.jeesite.common.persistence;

import java.util.Map;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.thinkgem.jeesite.common.test.SpringTransactionalContextTests;
import com.thinkgem.jeesite.modules.sys.dao.UserDao;
import com.thinkgem.jeesite.modules.sys.entity.User;

/**
 * BaseDaoTest
 * @author ThinkGem
 * @version 2013-05-15
 */
public class BaseDaoTest extends SpringTransactionalContextTests {
	
	@Autowired
	private UserDao userDao;

	@Test
	public void find(){
		Page<Object[]> objPage = new Page<Object[]>(1, 3);
		Page<Map<String, Object>> mapPage = new Page<Map<String, Object>>(1, 3);
		Page<User> entityPage = new Page<User>(1, 3);
 
		System.out.print("===== exe hql, return type: Object[] =====\n");
		String qlString = "select u.name, u.office.name as office_name from User u";
		objPage = userDao.find(objPage, qlString);
		for (Object[] o : objPage.getList()) {
			System.out.print(o[0]+", "+o[1]+"\n");
		}
		
		System.out.print("===== exe hql, return type: Map =====\n");
		qlString = "select new map(u.name, u.office.name as office_name) from User u";
		mapPage = userDao.find(mapPage, qlString);
		for (Map<String, Object> o : mapPage.getList()) {
			System.out.print(o.get("name")+", "+o.get("office_name")+"\n");
		}
		
		System.out.print("===== exe hql, return type: Entity =====\n");
		qlString = "select u from User u join u.office o where o.id=1";
		entityPage = userDao.find(entityPage, qlString);
		for (User o : entityPage.getList()) {
			System.out.print(o.getName()+", "+o.getOffice().getName()+"\n");
		}
		
		System.out.print("===== exe sql, return type: Object[] =====\n");
		String sqlString = "select u.name, o.name as office_name from sys_user u join sys_office o on o.id=u.office_id";
		objPage = userDao.findBySql(objPage, sqlString);
		for (Object[] o : objPage.getList()) {
			System.out.print(o[0]+", "+o[1]+"\n");
		}
		
		System.out.print("===== exe sql, return type: Map =====\n");
		mapPage = userDao.findBySql(mapPage, sqlString, Map.class);
		for (Map<String, Object> o : mapPage.getList()) {
			System.out.print(o.get("name")+", "+o.get("office_name")+"\n");
		}
		
		System.out.print("===== exe sql, return type: Entity =====\n");
		sqlString = "select u.* from sys_user u join sys_office o on o.id=u.office_id";
		entityPage = userDao.findBySql(entityPage, sqlString, User.class);
		for (User o : entityPage.getList()) {
			System.out.print(o.getName()+", "+o.getOffice().getName()+"\n");
		}
		
		System.out.print("========================================\n");
		System.out.print("userDao: "+userDao+"\n");
	}
	
}