/**
 * Copyright &copy; 2012-2013 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 */
package com.thinkgem.jeesite.modules.cms.dao;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.thinkgem.jeesite.common.persistence.BaseDao;
import com.thinkgem.jeesite.common.persistence.BaseDaoImpl;
import com.thinkgem.jeesite.modules.cms.entity.Comment;

/**
 * 评论DAO接口
 * @author ThinkGem
 * @version 2013-01-15
 */
public interface CommentDao extends CommentDaoCustom, CrudRepository<Comment, Long> {

	@Modifying
	@Query("update Comment set delFlag=?2 where id = ?1")
	public int updateDelFlag(Long id, String status);
	
}

/**
 * DAO自定义接口
 * @author ThinkGem
 */
interface CommentDaoCustom extends BaseDao<Comment> {

}

/**
 * DAO自定义接口实现
 * @author ThinkGem
 */
@Repository
class CommentDaoImpl extends BaseDaoImpl<Comment> implements CommentDaoCustom {

}
