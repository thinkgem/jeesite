/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.cms.dao;

import java.util.List;
import java.util.Map;

import com.thinkgem.jeesite.common.persistence.TreeDao;
import com.thinkgem.jeesite.common.persistence.annotation.MyBatisDao;
import com.thinkgem.jeesite.modules.cms.entity.Category;

/**
 * 栏目DAO接口
 * @author ThinkGem
 * @version 2013-8-23
 */
@MyBatisDao
public interface CategoryDao extends TreeDao<Category> {
	
	public List<Category> findModule(Category category);
	
//	public List<Category> findByParentIdsLike(Category category);
//	{
//		return find("from Category where parentIds like :p1", new Parameter(parentIds));
//	}

	public List<Category> findByModule(String module);
//	{
//		return find("from Category where delFlag=:p1 and (module='' or module=:p2) order by site.id, sort", 
//				new Parameter(Category.DEL_FLAG_NORMAL, module));
//	}
	
	public List<Category> findByParentId(String parentId, String isMenu);
//	{
//		return find("from Category where delFlag=:p1 and parent.id=:p2 and inMenu=:p3 order by site.id, sort", 
//				new Parameter(Category.DEL_FLAG_NORMAL, parentId, isMenu));
//	}

	public List<Category> findByParentIdAndSiteId(Category entity);
	
	public List<Map<String, Object>> findStats(String sql);
//	{
//		return find("from Category where delFlag=:p1 and parent.id=:p2 and site.id=:p3 order by site.id, sort", 
//				new Parameter(Category.DEL_FLAG_NORMAL, parentId, siteId));
//	}
	
	//public List<Category> findByIdIn(String[] ids);
//	{
//		return find("from Category where id in (:p1)", new Parameter(new Object[]{ids}));
//	}
	//public List<Category> find(Category category);

//	@Query("select distinct c from Category c, Role r, User u where c in elements (r.categoryList) and r in elements (u.roleList)" +
//			" and c.delFlag='" + Category.DEL_FLAG_NORMAL + "' and r.delFlag='" + Role.DEL_FLAG_NORMAL + 
//			"' and u.delFlag='" + User.DEL_FLAG_NORMAL + "' and u.id=?1 or (c.user.id=?1 and c.delFlag='" + Category.DEL_FLAG_NORMAL +
//			"') order by c.site.id, c.sort")
//	public List<Category> findByUserId(Long userId);
	
}
