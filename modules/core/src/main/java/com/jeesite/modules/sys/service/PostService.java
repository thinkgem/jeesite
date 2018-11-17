/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.modules.sys.service;

import com.jeesite.common.entity.Page;
import com.jeesite.common.service.api.CrudServiceApi;
import com.jeesite.modules.sys.entity.Post;

/**
 * 岗位管理Service
 * @author ThinkGem
 * @version 2017-03-25
 */
public interface PostService extends CrudServiceApi<Post> {

	/**
	 * 查询岗位
	 */
	@Override
	public Post get(Post post);
	
	/**
	 * 根据名称查询岗位
	 */
	public Post getByPostName(Post post);

	/**
	 * 查询岗位
	 */
	@Override
	public Page<Post> findPage(Post post);

	/**
	 * 保存岗位
	 */
	@Override
	public void save(Post post);

	/**
	 * 更新岗位状态
	 */
	@Override
	public void updateStatus(Post post);

	/**
	 * 删除岗位
	 */
	@Override
	public void delete(Post post);

}