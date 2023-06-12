/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 * No deletion without permission, or be held responsible to law.
 */
package com.jeesite.modules.sys.service;

import com.jeesite.common.entity.Page;
import com.jeesite.common.service.api.CrudServiceApi;
import com.jeesite.modules.sys.entity.Post;
import com.jeesite.modules.sys.entity.PostRole;

import java.util.List;

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
	Post get(Post post);
	
	/**
	 * 根据名称查询岗位
	 */
	Post getByPostName(Post post);

	/**
	 * 查询岗位
	 */
	@Override
	Page<Post> findPage(Post post);

	/**
	 * 查询岗位角色列表
	 */
	List<PostRole> findPostRoleList(PostRole postRole);

	/**
	 * 保存岗位
	 */
	@Override
	void save(Post post);

	/**
	 * 更新岗位状态
	 */
	@Override
	void updateStatus(Post post);

	/**
	 * 删除岗位
	 */
	@Override
	void delete(Post post);

}