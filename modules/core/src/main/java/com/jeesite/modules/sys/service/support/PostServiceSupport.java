/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 * No deletion without permission, or be held responsible to law.
 */
package com.jeesite.modules.sys.service.support;

import com.jeesite.common.collect.ListUtils;
import com.jeesite.common.config.Global;
import com.jeesite.common.entity.Page;
import com.jeesite.common.lang.StringUtils;
import com.jeesite.common.service.CrudService;
import com.jeesite.common.utils.PageUtils;
import com.jeesite.modules.sys.dao.PostDao;
import com.jeesite.modules.sys.dao.PostRoleDao;
import com.jeesite.modules.sys.entity.*;
import com.jeesite.modules.sys.service.EmpUserService;
import com.jeesite.modules.sys.service.PostService;
import com.jeesite.modules.sys.utils.CorpUtils;
import com.jeesite.modules.sys.utils.UserUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 岗位管理Service
 * @author ThinkGem
 * @version 2017-03-25
 */
public class PostServiceSupport extends CrudService<PostDao, Post>
		implements PostService{

	@Autowired
	private PostRoleDao postRoleDao;
	@Autowired
	private EmpUserService empUserService;

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
	@Override
	public Post getByPostName(Post post) {
		Post where = new Post();
		where.setPostName(post.getPostName());
		return dao.getByEntity(where);
	}

	/**
	 * 查询数据
	 */
	@Override
	public List<Post> findList(Post entity) {
		return super.findList(entity);
	}

	/**
	 * 查询岗位
	 */
	@Override
	public Page<Post> findPage(Post post) {
		return super.findPage(post);
	}

	/**
	 * 查询岗位角色关系
	 */
	public List<PostRole> findPostRoleList(PostRole postRole) {
		return postRoleDao.findList(postRole);
	}

	/**
	 * 保存岗位
	 */
	@Override
	@Transactional
	public void save(Post post) {
		if (post.getIsNewRecord()){
			// 生成主键，并验证改主键是否存在，如存在则抛出验证信息
			genIdAndValid(post, post.getViewCode());
		}
		super.save(post);
		// 重新绑定岗位和角色之间的关系
		if (StringUtils.isNotBlank(post.getPostCode()) && post.getRoleCodes() != null) {
			PostRole where = new PostRole();
			where.setPostCode(post.getPostCode());
			postRoleDao.deleteByEntity(where);
			List<PostRole> list = ListUtils.newArrayList();
			for (String code : StringUtils.splitComma(post.getRoleCodes())) {
				PostRole e = new PostRole();
				e.setPostCode(post.getPostCode());
				e.setRoleCode(code);
				e.setIsNewRecord(true);
				list.add(e);
			}
			if (ListUtils.isNotEmpty(list)) {
				postRoleDao.insertBatch(list, null);
			}
		}
		clearCache(post);
	}

	/**
	 * 岗位编码生成规则
	 */
	public void genId(Post entity, String viewCode){
		if (StringUtils.isNotBlank(viewCode)){
			// 如果是租户模式，并且当前租户不是默认租户的时候，增加租户前缀防止编码重复
			if (Global.isUseCorpModel() && !CorpUtils.DEFAULT_CORP_CODE.equals(CorpUtils.getCurrentCorpCode())){
				entity.setId(entity.getCorpCode() + "_" + viewCode);
			}else{
				entity.setId(viewCode);
			}
		}
	}

	/**
	 * 更新岗位状态
	 */
	@Override
	@Transactional
	public void updateStatus(Post post) {
		super.updateStatus(post);
	}

	/**
	 * 删除岗位
	 */
	@Override
	@Transactional
	public void delete(Post post) {
		post.sqlMap().markIdDelete();
		super.delete(post);
		clearCache(post);
	}

	/**
	 * 根据岗位清理缓存
	 */
	protected void clearCache(Post post){
		// 清除该岗位下所有的用户缓存
		EmpUser where = new EmpUser();
		where.setCodes(new String[]{ post.getPostCode() });
		PageUtils.findList(where, null, e -> {
			List<EmpUser> userList = empUserService.findUserListByPostCodes((EmpUser)e);
			userList.forEach(UserUtils::clearCache);
			return !userList.isEmpty();
		});
	}

}