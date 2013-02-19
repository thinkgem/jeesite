/**
 * Copyright &copy; 2012-2013 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 */
package com.thinkgem.jeesite.modules.sys.dao;

import java.util.Date;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.thinkgem.jeesite.common.persistence.BaseDao;
import com.thinkgem.jeesite.common.persistence.BaseDaoImpl;
import com.thinkgem.jeesite.modules.sys.entity.User;

/**
 * 用户DAO接口
 * @author ThinkGem
 * @version 2013-01-15
 */
public interface UserDao extends UserDaoCustom, CrudRepository<User, Long> {
	
	@Query("from User where loginName = ?1 and delFlag = '" + User.DEL_FLAG_NORMAL + "'")
	public User findByLoginName(String loginName);

	@Modifying
	@Query("update User set delFlag='" + User.DEL_FLAG_DELETE + "' where id = ?1")
	public int deleteById(Long id);
	
	@Modifying
	@Query("update User set password=?1 where id = ?2")
	public int updatePasswordById(String newPassword, Long id);
	
	@Modifying
	@Transactional
	@Query("update User set loginIp=?1, loginDate=?2 where id = ?3")
	public int updateLoginInfo(String loginIp, Date loginDate, Long id);
}

/**
 * DAO自定义接口
 * @author ThinkGem
 */
interface UserDaoCustom extends BaseDao<User> {

}

/**
 * DAO自定义接口实现
 * @author ThinkGem
 */
@Component
class UserDaoImpl extends BaseDaoImpl<User> implements UserDaoCustom {

}
