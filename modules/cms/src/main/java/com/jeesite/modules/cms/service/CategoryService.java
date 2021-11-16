/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 * No deletion without permission, or be held responsible to law.
 */
package com.jeesite.modules.cms.service;

import java.util.List;

import org.apache.commons.text.StringEscapeUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeesite.common.lang.StringUtils;
import com.jeesite.common.service.TreeService;
import com.jeesite.modules.cms.dao.CategoryDao;
import com.jeesite.modules.cms.entity.Category;
import com.jeesite.modules.cms.utils.CmsUtils;
import com.jeesite.modules.file.utils.FileUploadUtils;

/**
 * 栏目表Service
 * @author 长春叭哥、ThinkGem
 * @version 2020-7-24
 */
@Service
@Transactional(readOnly = true)
public class CategoryService extends TreeService<CategoryDao, Category> {

	/**
	 * 获取单条数据
	 * @param category
	 * @return
	 */
	@Override
	public Category get(Category category) {
		return super.get(category);
	}
	
	/**
	 * 添加数据权限
	 */
	@Override
	public void addDataScopeFilter(Category entity) {
		
	}
	
	/**
	 * 查询列表数据
	 * @param category
	 * @return
	 */
	@Override
	public List<Category> findList(Category category) {
		return super.findList(category);
	}

	/**
	 * 保存数据（插入或更新）
	 * @param category
	 */
	@Override
	@Transactional(readOnly = false)
	public void save(Category category) {
		if (StringUtils.isNotBlank(category.getViewConfig())){
            category.setViewConfig(StringEscapeUtils.unescapeHtml4(category.getViewConfig()));
        }
		super.save(category);
		CmsUtils.removeCache("mainNavList_"+category.getSite().getId());
		// 保存上传图片
		FileUploadUtils.saveFileUpload(category, category.getId(), "category_image");
	}
	
	/**
	 * 更新子节点，并设置子节点[sysCode]与父类相同
	 */
	@Override
	protected void updateChildNode(Category childEntity, Category parentEntity) {
		childEntity.setSite(parentEntity.getSite());
		childEntity.getSqlMap().updateTreeDataExtSql("site_code = #{site.siteCode}");
		super.updateChildNode(childEntity, parentEntity);
	}

	/**
	 * 更新状态
	 * @param category
	 */
	@Override
	@Transactional(readOnly = false)
	public void updateStatus(Category category) {
		super.updateStatus(category);
	}

	/**
	 * 删除数据
	 * @param category
	 */
	@Override
	@Transactional(readOnly = false)
	public void delete(Category category) {
		super.delete(category);
	}

}