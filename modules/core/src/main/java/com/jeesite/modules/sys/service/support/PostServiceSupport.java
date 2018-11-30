/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.modules.sys.service.support;

import org.springframework.transaction.annotation.Transactional;

import com.jeesite.common.entity.Page;
import com.jeesite.common.service.CrudService;
import com.jeesite.modules.sys.dao.PostDao;
import com.jeesite.modules.sys.entity.Post;
import com.jeesite.modules.sys.service.PostService;

/**
 * 岗位管理Service
 * @author ThinkGem
 * @version 2017-03-25
 */
@Transactional(readOnly=true)
public class PostServiceSupport extends CrudService<PostDao, Post>
		implements PostService{

	/**
	 * 查询岗位
	 */
	@Override
	public Post get(Post post) {
		return super.get(post);
	}
	
	/**
	 * 根据名称查询岗位
	 */
	public Post getByPostName(Post post) {
		Post where = new Post();
		where.setPostName(post.getPostName());
		return dao.getByEntity(where);
	}

	/**
	 * 查询岗位
	 */
	@Override
	public Page<Post> findPage(Post post) {
		return super.findPage(post);
	}

	/**
	 * 保存岗位
	 */
	@Override
	@Transactional(readOnly=false)
	public void save(Post post) {
		super.save(post);
	}

	/**
	 * 更新岗位状态
	 */
	@Override
	@Transactional(readOnly=false)
	public void updateStatus(Post post) {
		super.updateStatus(post);
	}

	/**
	 * 删除岗位
	 */
	@Override
	@Transactional(readOnly=false)
	public void delete(Post post) {
		super.delete(post);
	}

}