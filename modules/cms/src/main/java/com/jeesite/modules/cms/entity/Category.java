/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 * No deletion without permission, or be held responsible to law.
 */
package com.jeesite.modules.cms.entity;

import java.util.List;

import javax.validation.constraints.NotBlank;

import javax.validation.constraints.Size;

import com.jeesite.common.collect.ListUtils;
import com.jeesite.common.entity.DataEntity;
import com.jeesite.common.entity.Extend;
import com.jeesite.common.entity.TreeEntity;
import com.jeesite.common.mybatis.annotation.Column;
import com.jeesite.common.mybatis.annotation.JoinTable;
import com.jeesite.common.mybatis.annotation.Table;
import com.jeesite.common.mybatis.mapper.query.QueryType;
import com.jeesite.modules.cms.utils.CmsUtils;

/**
 * 栏目表Entity
 * @author 长春叭哥、ThinkGem
 * @version 2018-10-15
 */
@Table(name = "${_prefix}cms_category", alias = "a", columns = {
		@Column(name = "category_code", attrName = "categoryCode", label = "栏目编码", isPK = true), @Column(includeEntity = TreeEntity.class),
		@Column(name = "category_name", attrName = "categoryName", label = "栏目名称", queryType = QueryType.LIKE, isTreeName = true),
		@Column(name = "site_code", attrName = "site.siteCode", label = "站点编码", queryType = QueryType.EQ),
		@Column(name = "module_type", attrName = "moduleType", label = "模块类型"), @Column(name = "image", attrName = "image", label = "栏目图片"),
		@Column(name = "href", attrName = "href", label = "链接"), @Column(name = "target", attrName = "target", label = "目标"),
		@Column(name = "keywords", attrName = "keywords", label = "关键字"), @Column(name = "description", attrName = "description", label = "描述"),
		@Column(name = "in_menu", attrName = "inMenu", label = "是否在导航中显示"), @Column(name = "in_list", attrName = "inList", label = "是否在分类页中显示列表"),
		@Column(name = "show_modes", attrName = "showModes", label = "展现模式"),
		@Column(name = "is_need_audit", attrName = "isNeedAudit", label = "是否需要审核"),
		@Column(name = "is_can_comment", attrName = "isCanComment", label = "是否允许评论"),
		@Column(name = "custom_list_view", attrName = "customListView", label = "自定义列表视图"),
		@Column(name = "custom_content_view", attrName = "customContentView", label = "自定义内容视图"),
		@Column(name = "view_config", attrName = "viewConfig", label = "视图配置"), @Column(includeEntity = DataEntity.class),
		@Column(includeEntity = Extend.class, attrName = "extend"),
	}, joinTable = {
		@JoinTable(entity = Site.class, alias = "s",
			on = "s.site_code = a.site_code", columns = {
				@Column(name = "site_name"),
			})
	}, orderBy = "a.tree_sorts, a.category_code"
)
public class Category extends TreeEntity<Category> {

	public static final String DEFAULT_TEMPLATE = "list"; // 默认文章列表模板
	
	public static final String SHOW_MODES_AUTO = "1"; 			// 默认展现方式
	public static final String SHOW_MODES_CENTENT_LIST = "2"; 	// 首栏目内容列表
	public static final String SHOW_MODES_FIRST_CONTENT = "3"; 	// 简介类栏目，栏目第一条内容
	
	private static final long serialVersionUID = 1L;
	private String categoryCode; 	// 栏目编码
	private String categoryName; 	// 栏目名称
	private Site site; 				// 归属站点		
	private String moduleType; 		// 模块类型
	private String image; 			// 栏目图片
	private String href; 			// 链接
	private String target; 			// 目标
	private String keywords; 		// 关键字
	private String description; 	// 描述
	private String inMenu; 			// 是否在导航中显示
	private String inList; 			// 是否在分类页中显示列表
	private String showModes; 		// 展现模式
	private String isNeedAudit; 	// 是否需要审核
	private String isCanComment; 	// 是否允许评论
	private String customListView; 	// 自定义列表视图
	private String customContentView; // 自定义内容视图
	private String viewConfig; 		// 视图配置
	private Extend extend; 			// 扩展字段

	private List<Integer> sortGradeList = ListUtils.newArrayList(); 	// 根据树的层级级别查询栏目列表
	private List<String> categoryCodeList = ListUtils.newArrayList(); 	// 根据分类栏目编号查询栏目列表
	private List<String> roleCodeList = ListUtils.newArrayList(); 		// 根据角色查询有权限的栏目列表

	public Category() {
		this(null);
	}

	public Category(String id) {
		super(id);
	}

	public Category(String id, Site site) {
		super(id);
		this.setSite(site);
	}

	@Override
	public Category getParent() {
		return parent;
	}

	@Override
	public void setParent(Category parent) {
		this.parent = parent;
	}

	public String getCategoryCode() {
		return categoryCode;
	}

	public void setCategoryCode(String categoryCode) {
		this.categoryCode = categoryCode;
	}

	@NotBlank(message = "栏目名称不能为空")
	@Size(min = 0, max = 100, message = "栏目名称长度不能超过 100 个字符")
	public String getCategoryName() {
		return categoryName;
	}

	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}

	public Site getSite() {
		if (site == null) {
			site = new Site();
		}
		return site;
	}

	public void setSite(Site site) {
		this.site = site;
	}

	@Size(min = 0, max = 50, message = "模块类型长度不能超过 50 个字符")
	public String getModuleType() {
		return moduleType;
	}

	public void setModuleType(String moduleType) {
		this.moduleType = moduleType;
	}

	@Size(min = 0, max = 255, message = "栏目图片长度不能超过 255 个字符")
	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	@Size(min = 0, max = 255, message = "链接长度不能超过 255 个字符")
	public String getHref() {
		return href;
	}

	public void setHref(String href) {
		this.href = href;
	}

	@Size(min = 0, max = 20, message = "目标长度不能超过 20 个字符")
	public String getTarget() {
		return target;
	}

	public void setTarget(String target) {
		this.target = target;
	}

	@Size(min = 0, max = 500, message = "关键字长度不能超过 500 个字符")
	public String getKeywords() {
		return keywords;
	}

	public void setKeywords(String keywords) {
		this.keywords = keywords;
	}

	@Size(min = 0, max = 500, message = "描述长度不能超过 500 个字符")
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Size(min = 0, max = 1, message = "是否在导航中显示长度不能超过 1 个字符")
	public String getInMenu() {
		return inMenu;
	}

	public void setInMenu(String inMenu) {
		this.inMenu = inMenu;
	}

	@Size(min = 0, max = 1, message = "是否在分类页中显示列表长度不能超过 1 个字符")
	public String getInList() {
		return inList;
	}

	public void setInList(String inList) {
		this.inList = inList;
	}

	@Size(min = 0, max = 1, message = "展现模式长度不能超过 1 个字符")
	public String getShowModes() {
		return showModes;
	}

	public void setShowModes(String showModes) {
		this.showModes = showModes;
	}

	@Size(min = 0, max = 1, message = "是否需要审核长度不能超过 1 个字符")
	public String getIsNeedAudit() {
		return isNeedAudit;
	}

	public void setIsNeedAudit(String isNeedAudit) {
		this.isNeedAudit = isNeedAudit;
	}

	@Size(min = 0, max = 1, message = "是否允许评论长度不能超过 1 个字符")
	public String getIsCanComment() {
		return isCanComment;
	}

	public void setIsCanComment(String isCanComment) {
		this.isCanComment = isCanComment;
	}

	@Size(min = 0, max = 255, message = "自定义列表视图长度不能超过 255 个字符")
	public String getCustomListView() {
		return customListView;
	}

	public void setCustomListView(String customListView) {
		this.customListView = customListView;
	}

	@Size(min = 0, max = 255, message = "自定义内容视图长度不能超过 255 个字符")
	public String getCustomContentView() {
		return customContentView;
	}

	public void setCustomContentView(String customContentView) {
		this.customContentView = customContentView;
	}

	@Size(min = 0, max = 1000, message = "视图配置长度不能超过 1000 个字符")
	public String getViewConfig() {
		return viewConfig;
	}

	public void setViewConfig(String viewConfig) {
		this.viewConfig = viewConfig;
	}

	public Extend getExtend() {
		return extend;
	}

	public void setExtend(Extend extend) {
		this.extend = extend;
	}

	public List<Integer> getSortGradeList() {
		return sortGradeList;
	}

	public void setSortGradeList(List<Integer> sortGradeList) {
		this.sortGradeList = sortGradeList;
	}

	public List<String> getCategoryCodeList() {
		return categoryCodeList;
	}

	public void setCategoryCodeList(List<String> categoryCodeList) {
		this.categoryCodeList = categoryCodeList;
	}

	public List<String> getRoleCodeList() {
		return roleCodeList;
	}

	public void setRoleCodeList(List<String> roleCodeList) {
		this.roleCodeList = roleCodeList;
	}

	public String getUrl() {
		return CmsUtils.getUrlDynamic(this);
	}

	public String getAdminUrl() {
		return CmsUtils.getAdminUrlDynamic(this);
	}

}