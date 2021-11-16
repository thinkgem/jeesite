/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 * No deletion without permission, or be held responsible to law.
 */
package com.jeesite.modules.cms.entity;

import java.util.List;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import com.jeesite.common.config.Global;
import com.jeesite.common.entity.DataEntity;
import com.jeesite.common.lang.StringUtils;
import com.jeesite.common.mybatis.annotation.Column;
import com.jeesite.common.mybatis.annotation.Table;
import com.jeesite.common.mybatis.mapper.query.QueryType;
import com.jeesite.modules.sys.utils.CorpUtils;
import com.jeesite.modules.sys.utils.UserUtils;

/**
 * 站点表Entity
 * @author 长春叭哥、ThinkGem
 * @version 2018-10-15
 */
@Table(name = "${_prefix}cms_site", alias = "a", columns = {
		@Column(name = "site_code", attrName = "siteCode", label = "站点编码", isPK = true),
		@Column(name = "site_name", attrName = "siteName", label = "站点名称", queryType = QueryType.LIKE),
		@Column(name = "site_sort", attrName = "siteSort", label = "站点排序号"),
		@Column(name = "title", attrName = "title", label = "站点标题", queryType = QueryType.LIKE),
		@Column(name = "logo", attrName = "logo", label = "站点Logo"),
		@Column(name = "domain", attrName = "domain", label = "站点域名"),
		@Column(name = "keywords", attrName = "keywords", label = "关键字"),
		@Column(name = "description", attrName = "description", label = "描述"),
		@Column(name = "theme", attrName = "theme", label = "主题"),
		@Column(name = "copyright", attrName = "copyright", label = "版权信息"),
		@Column(name = "custom_index_view", attrName = "customIndexView", label = "自定义站点首页视图"),
		@Column(includeEntity = DataEntity.class),
	}, orderBy = "a.site_sort, a.update_date DESC"
)
public class Site extends DataEntity<Site> {
	
	/**
	 * 获取默认站点编码
	 */
	public static final String MAIN_SITE_CODE = Global.getProperty("cms.mainSiteCode", "main");
	/**
	 * 模板路径
	 */
	public static final String TEMPLETE_BASE_DIRECTION = "views/modules/cmsfront/themes";
	/**
	 * 默认模版
	 */
	public static final String DEFAULT_TEMPLATE = "index";

	private static final long serialVersionUID = 1L;
	private String siteCode; 	// 站点编码
	private String siteName; 	// 站点名称
	private Integer siteSort; 	// 站点排序号
	private String title; 		// 站点标题
	private String logo; 		// 站点Logo
	private String domain; 		// 站点域名
	private String keywords; 	// 关键字
	private String description; // 描述
	private String theme; 		// 主题
	private String copyright; 	// 版权信息
	private String customIndexView; // 自定义站点首页视图
	
	private List<String> categoryCodes;		//某角色下某站点下授权的栏目id集合
	private List<Category> categoryList;	//某站点下所有栏目集合
	
	public Site() {
		this(null);
	}

	public Site(String id) {
		super(id);
	}

	public String getSiteCode() {
		return siteCode;
	}

	public void setSiteCode(String siteCode) {
		this.siteCode = siteCode;
	}

	@NotBlank(message = "站点名称不能为空")
	@Size(min = 0, max = 100, message = "站点名称长度不能超过 100 个字符")
	public String getSiteName() {
		return siteName;
	}

	public void setSiteName(String siteName) {
		this.siteName = siteName;
	}

	public Integer getSiteSort() {
		return siteSort;
	}

	public void setSiteSort(Integer siteSort) {
		this.siteSort = siteSort;
	}

	@NotBlank(message = "站点标题不能为空")
	@Size(min = 0, max = 100, message = "站点标题长度不能超过 100 个字符")
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	@Size(min = 0, max = 1000, message = "站点Logo长度不能超过 1000 个字符")
	public String getLogo() {
		return logo;
	}

	public void setLogo(String logo) {
		this.logo = logo;
	}

	@Size(min = 0, max = 500, message = "站点域名长度不能超过 500 个字符")
	public String getDomain() {
		return domain;
	}

	public void setDomain(String domain) {
		this.domain = domain;
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

	@Size(min = 0, max = 500, message = "主题长度不能超过 500 个字符")
	public String getTheme() {
		return theme;
	}

	public void setTheme(String theme) {
		this.theme = theme;
	}

	@Size(min = 0, max = 1000, message = "版权信息长度不能超过 1000 个字符")
	public String getCopyright() {
		return copyright;
	}

	public void setCopyright(String copyright) {
		this.copyright = copyright;
	}

	@Size(min = 0, max = 500, message = "自定义站点首页视图长度不能超过 500 个字符")
	public String getCustomIndexView() {
		return customIndexView;
	}

	public void setCustomIndexView(String customIndexView) {
		this.customIndexView = customIndexView;
	}

	/**
	 * 获取当前编辑的站点编号
	 */
	public static String getCurrentSiteCode() {
		String siteCode = (String) UserUtils.getCache("currentSiteCode");
		String defaultSiteCode = MAIN_SITE_CODE;
		return StringUtils.isNotBlank(siteCode) ? siteCode : defaultSiteCode;
	}

	/**
	 * 获得模板方案路径。如：/WEB-INF/views/modules/cmsfront/themes/jeesite
	 * @return
	 */
	public String getSolutionPath() {
		return TEMPLETE_BASE_DIRECTION + "/" + getTheme();
	}
	
	public List<String> getCategoryCodes() {
		return categoryCodes;
	}

	public void setCategoryCodes(List<String> categoryCodes) {
		this.categoryCodes = categoryCodes;
	}

	public List<Category> getCategoryList() {
		return categoryList;
	}

	public void setCategoryList(List<Category> categoryList) {
		this.categoryList = categoryList;
	}

	
	

	/**
	 * 判断是否为当前站点
	 */
	public Boolean getIsCurrentSite(){
		return getCurrentSiteCode().equals(siteCode);
	}
	
	/**
	 * 判断是否为默认（主站）站点
	 */
	public static boolean isMainSite(String siteCode){
		if (siteCode != null){
			String code = siteCode;
			if (Global.isUseCorpModel()){
				String corpCode = CorpUtils.getCurrentCorpCode();
				if (code.startsWith(corpCode + "_")){
					code = code.replaceFirst(corpCode + "_", "");
				}
			}
			return MAIN_SITE_CODE.equals(code);
		}
		return false;
	}
	
	
	
	
	
	
	
	
	
	
}