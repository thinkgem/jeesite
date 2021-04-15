/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.modules.cms.entity;

import java.util.Date;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.jeesite.common.entity.BaseEntity;
import com.jeesite.common.entity.DataEntity;
import com.jeesite.common.mybatis.annotation.Column;
import com.jeesite.common.mybatis.annotation.JoinTable;
import com.jeesite.common.mybatis.annotation.Table;
import com.jeesite.common.mybatis.mapper.query.QueryType;
import com.jeesite.modules.cms.utils.CmsUtils;

/**
 * 文章表Entity
 * @author 长春叭哥、ThinkGem
 * @version 2018-10-15
 */
@Table(name = "${_prefix}cms_article", alias = "a", columns = {
		@Column(name = "id", attrName = "id", label = "编号", isPK = true),
		@Column(name = "category_code", attrName = "category.categoryCode", label = "栏目编码"),
		@Column(name = "module_type", attrName = "moduleType", label = "模块类型"),
		@Column(name = "title", attrName = "title", label = "内容标题", queryType = QueryType.LIKE),
		@Column(name = "href", attrName = "href", label = "外部链接"),
		@Column(name = "color", attrName = "color", label = "标题颜色"),
		@Column(name = "image", attrName = "image", label = "内容图片"),
		@Column(name = "keywords", attrName = "keywords", label = "关键字", queryType = QueryType.LIKE),
		@Column(name = "description", attrName = "description", label = "描述"),
		@Column(name = "weight", attrName = "weight", label = "权重，越大越靠前", isQuery = false),
		@Column(name = "weight_date", attrName = "weightDate", label = "权重期限", isQuery = false),
		@Column(name = "source", attrName = "source", label = "来源", comment = "来源（转载/原创）"),
		@Column(name = "copyfrom", attrName = "copyfrom", label = "文章来源出处"),
		@Column(name = "hits", attrName = "hits", label = "点击数"),
		@Column(name = "hits_plus", attrName = "hitsPlus", label = "支持数"),
		@Column(name = "hits_minus", attrName = "hitsMinus", label = "反对数"),
		@Column(name = "word_count", attrName = "wordCount", label = "字数", comment = "字数（不包含html）"),
		@Column(name = "custom_content_view", attrName = "customContentView", label = "自定义内容视图"),
		@Column(name = "view_config", attrName = "viewConfig", label = "视图配置"),
		@Column(includeEntity = DataEntity.class),
		@Column(includeEntity = BaseEntity.class),
	}, joinTable = {
		@JoinTable(entity = Category.class, alias = "c",
			on = "c.category_code = a.category_code", columns = {
				@Column(name = "category_name"),
			}),
		@JoinTable(entity = Site.class, attrName = "category.site", alias = "s",
			on = "s.site_code = c.site_code", columns = {
				@Column(name = "site_code"),
				@Column(name = "site_name"),
			})
	}, orderBy = "a.update_date DESC"
)
public class Article extends DataEntity<Article> {
	
	public static final String DEFAULT_TEMPLATE = "viewArticle"; // 默认文章内容模板
	private static final long serialVersionUID = 1L;
	
	private Category category; 	// 栏目编码
	private String moduleType; 	// 模块类型
	private String title; 		// 内容标题
	private String href; 		// 外部链接
	private String color; 		// 标题颜色
	private String image; 		// 内容图片
	private String keywords; 	// 关键字
	private String description; // 描述
	private Integer weight; 	// 权重，越大越靠前
	private Date weightDate; 	// 权重期限
	private String source; 		// 来源（转载/原创）
	private String copyfrom; 	// 文章来源出处
	private Long hits; 			// 点击数
	
	private Integer hitsPlus; 			// 支持数
	private Integer hitsMinus; 			// 反对数
	private Integer wordCount; 			// 字数（不包含html）
	private String customContentView; 	// 自定义内容视图
	private String viewConfig; 			// 视图配置
	private ArticleData articleData; 	//文章副表
	private Date beginDate; 			// 开始时间
	private Date endDate; 				// 结束时间

	private Boolean isQueryArticleData; // 是否查询文章内容

	public Article() {
		super();
		//this.weight = 0;
		//this.hits = 0l;
	}

	public Article(String id) {
		super(id);
	}

	public Article(Category category) {
		this();
		this.category = category;
	}

	@NotNull
	public Category getCategory() {
		if (category == null) {
			category = new Category();
		}
		return category;
	}

	public void setCategory(Category category) {
		this.category = category;
	}

	@Length(min = 0, max = 50, message = "模块类型长度不能超过 50 个字符")
	public String getModuleType() {
		return moduleType;
	}

	public void setModuleType(String moduleType) {
		this.moduleType = moduleType;
	}

	@NotBlank(message = "内容标题不能为空")
	@Length(min = 0, max = 255, message = "内容标题长度不能超过 255 个字符")
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	@Length(min = 0, max = 1000, message = "外部链接长度不能超过 1000 个字符")
	public String getHref() {
		return href;
	}

	public void setHref(String href) {
		this.href = href;
	}

	@Length(min = 0, max = 50, message = "标题颜色长度不能超过 50 个字符")
	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}

	@Length(min = 0, max = 1000, message = "内容图片长度不能超过 1000 个字符")
	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	@Length(min = 0, max = 500, message = "关键字长度不能超过 500 个字符")
	public String getKeywords() {
		return keywords;
	}

	public void setKeywords(String keywords) {
		this.keywords = keywords;
	}

	@Length(min = 0, max = 500, message = "描述长度不能超过 500 个字符")
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Integer getWeight() {
		return weight;
	}

	public void setWeight(Integer weight) {
		this.weight = weight;
	}

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	public Date getWeightDate() {
		return weightDate;
	}

	public void setWeightDate(Date weightDate) {
		this.weightDate = weightDate;
	}

	@Length(min = 0, max = 1, message = "来源长度不能超过 1 个字符")
	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	@Length(min = 0, max = 255, message = "文章来源出处长度不能超过 255 个字符")
	public String getCopyfrom() {
		return copyfrom;
	}

	public void setCopyfrom(String copyfrom) {
		this.copyfrom = copyfrom;
	}

	public Long getHits() {
		return hits;
	}

	public void setHits(Long hits) {
		this.hits = hits;
	}

	

	public Integer getHitsPlus() {
		return hitsPlus;
	}

	public void setHitsPlus(Integer hitsPlus) {
		this.hitsPlus = hitsPlus;
	}

	public Integer getHitsMinus() {
		return hitsMinus;
	}

	public void setHitsMinus(Integer hitsMinus) {
		this.hitsMinus = hitsMinus;
	}

	public Integer getWordCount() {
		return wordCount;
	}

	public void setWordCount(Integer wordCount) {
		this.wordCount = wordCount;
	}

	@Length(min = 0, max = 255, message = "自定义内容视图长度不能超过 255 个字符")
	public String getCustomContentView() {
		return customContentView;
	}

	public void setCustomContentView(String customContentView) {
		this.customContentView = customContentView;
	}

	@Length(min = 0, max = 1000, message = "视图配置长度不能超过 1000 个字符")
	public String getViewConfig() {
		return viewConfig;
	}

	public void setViewConfig(String viewConfig) {
		this.viewConfig = viewConfig;
	}

	public ArticleData getArticleData() {
		return articleData;
	}

	public void setArticleData(ArticleData articleData) {
		this.articleData = articleData;
	}

	public Date getBeginDate() {
		return beginDate;
	}

	public void setBeginDate(Date beginDate) {
		this.beginDate = beginDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	/**
	 * 是否查询文章内容
	 * @return
	 */
	public Boolean getIsQueryArticleData() {
		return isQueryArticleData;
	}

	public void setIsQueryArticleData(Boolean isQueryArticleData) {
		this.isQueryArticleData = isQueryArticleData;
	}

	 

	public String getUrl() {
		return CmsUtils.getUrlDynamic(this);
	}

}