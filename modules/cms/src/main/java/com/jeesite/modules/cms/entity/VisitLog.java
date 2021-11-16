/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 * No deletion without permission, or be held responsible to law.
 */
package com.jeesite.modules.cms.entity;

import java.util.Date;

import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.jeesite.common.entity.BaseEntity;
import com.jeesite.common.entity.DataEntity;
import com.jeesite.common.mybatis.annotation.Column;
import com.jeesite.common.mybatis.annotation.Table;
import com.jeesite.common.mybatis.mapper.query.QueryType;

/**
 * 访问日志表Entity
 * @author 长春叭哥、ThinkGem
 * @version 2018-10-15
 */
@Table(name="${_prefix}cms_visit_log", alias="a", columns={
		@Column(name="id", attrName="id", label="编号", isPK=true),
		@Column(name="request_url", attrName="requestUrl", label="请求的URL地址"),
		@Column(name="request_url_host", attrName="requestUrlHost", label="受访域名"),
		@Column(name="source_referer", attrName="sourceReferer", label="来源页面/上一个页面"),
		@Column(name="source_referer_host", attrName="sourceRefererHost", label="来源域名"),
		@Column(name="source_type", attrName="sourceType", label="访问来源类型", comment="访问来源类型（1直接访问 2搜索引擎 3外部链接 4内部访问）"),
		@Column(name="search_engine", attrName="searchEngine", label="使用的搜索引擎"),
		@Column(name="search_word", attrName="searchWord", label="搜索的关键词"),
		@Column(name="remote_addr", attrName="remoteAddr", label="客户IP地址"),
		@Column(name="user_agent", attrName="userAgent", label="用户代理字符串"),
		@Column(name="user_language", attrName="userLanguage", label="客户机语言"),
		@Column(name="user_screen_size", attrName="userScreenSize", label="客户机屏幕大小0x0"),
		@Column(name="user_device", attrName="userDevice", label="客户机设备类型", comment="客户机设备类型（电脑、平板、手机、未知）"),
		@Column(name="user_os_name", attrName="userOsName", label="客户机操作系统", queryType=QueryType.LIKE),
		@Column(name="user_browser", attrName="userBrowser", label="客户机浏览器"),
		@Column(name="user_browser_version", attrName="userBrowserVersion", label="浏览器版本"),
		@Column(name="unique_visit_id", attrName="uniqueVisitId", label="唯一访问标识"),
		@Column(name="visit_date", attrName="visitDate", label="本次访问日期", comment="本次访问日期（年月日）"),
		@Column(name="visit_time", attrName="visitTime", label="本次访问时间"),
		@Column(name="is_new_visit", attrName="isNewVisit", label="是否新访问", comment="是否新访问（30分内）"),
		@Column(name="first_visit_time", attrName="firstVisitTime", label="首次访问时间戳", comment="首次访问时间戳（30分钟内）"),
		@Column(name="prev_remain_time", attrName="prevRemainTime", label="上页面停留时间", comment="上页面停留时间（秒）"),
		@Column(name="total_remain_time", attrName="totalRemainTime", label="本次访问总停留时间", comment="本次访问总停留时间（秒）"),
		@Column(name="site_code", attrName="siteCode", label="站点编码"),
		@Column(name="site_name", attrName="siteName", label="站点名称", queryType=QueryType.LIKE),
		@Column(name="category_code", attrName="categoryCode", label="栏目编码"),
		@Column(name="category_name", attrName="categoryName", label="栏目名称", queryType=QueryType.LIKE),
		@Column(name="content_id", attrName="contentId", label="栏目内容编号"),
		@Column(name="content_title", attrName="contentTitle", label="访问页面标题", queryType=QueryType.LIKE),
		@Column(name="visit_user_code", attrName="visitUserCode", label="访问用户编码"),
		@Column(name="visit_user_name", attrName="visitUserName", label="访问用户姓名", queryType=QueryType.LIKE),
		@Column(includeEntity=BaseEntity.class),
	}, orderBy="a.id DESC"
)
public class VisitLog extends DataEntity<VisitLog> {
	
	private static final long serialVersionUID = 1L;
	private String requestUrl;			// 请求的URL地址
	private String requestUrlHost;		// 受访域名
	private String sourceReferer;		// 来源页面/上一个页面
	private String sourceRefererHost;	// 来源域名
	private String sourceType;			// 访问来源类型（1直接访问 2搜索引擎 3外部链接 4内部访问）
	private String searchEngine;		// 使用的搜索引擎
	private String searchWord;			// 搜索的关键词
	private String remoteAddr;			// 客户IP地址
	private String userAgent;			// 用户代理字符串
	private String userLanguage;		// 客户机语言
	private String userScreenSize;		// 客户机屏幕大小0x0
	private String userDevice;			// 客户机设备类型（电脑、平板、手机、未知）
	private String userOsName;			// 客户机操作系统
	private String userBrowser;			// 客户机浏览器
	private String userBrowserVersion;	// 浏览器版本
	private String uniqueVisitId;		// 唯一访问标识
	private String visitDate;			// 本次访问日期（年月日）
	private Date visitTime;				// 本次访问时间
	private String isNewVisit;			// 是否新访问（30分内）
	private Long firstVisitTime;		// 首次访问时间戳（30分钟内）
	private Long prevRemainTime;		// 上页面停留时间（秒）
	private Long totalRemainTime;		// 本次访问总停留时间（秒）
	private String siteCode;			// 站点编码
	private String siteName;			// 站点名称
	private String categoryCode;		// 栏目编码
	private String categoryName;		// 栏目名称
	private String contentId;			// 栏目内容编号
	private String contentTitle;		// 访问页面标题
	private String visitUserCode;		// 访问用户编码
	private String visitUserName;		// 访问用户姓名
	
	public VisitLog() {
		this(null);
	}

	public VisitLog(String id){
		super(id);
	}
	
	@Size(min=0, max=1000, message="请求的URL地址长度不能超过 1000 个字符")
	public String getRequestUrl() {
		return requestUrl;
	}

	public void setRequestUrl(String requestUrl) {
		this.requestUrl = requestUrl;
	}
	
	@Size(min=0, max=128, message="受访域名长度不能超过 128 个字符")
	public String getRequestUrlHost() {
		return requestUrlHost;
	}

	public void setRequestUrlHost(String requestUrlHost) {
		this.requestUrlHost = requestUrlHost;
	}
	
	@Size(min=0, max=1000, message="来源页面/上一个页面长度不能超过 1000 个字符")
	public String getSourceReferer() {
		return sourceReferer;
	}

	public void setSourceReferer(String sourceReferer) {
		this.sourceReferer = sourceReferer;
	}
	
	@Size(min=0, max=128, message="来源域名长度不能超过 128 个字符")
	public String getSourceRefererHost() {
		return sourceRefererHost;
	}

	public void setSourceRefererHost(String sourceRefererHost) {
		this.sourceRefererHost = sourceRefererHost;
	}
	
	@Size(min=0, max=1, message="访问来源类型长度不能超过 1 个字符")
	public String getSourceType() {
		return sourceType;
	}

	public void setSourceType(String sourceType) {
		this.sourceType = sourceType;
	}
	
	@Size(min=0, max=200, message="使用的搜索引擎长度不能超过 200 个字符")
	public String getSearchEngine() {
		return searchEngine;
	}

	public void setSearchEngine(String searchEngine) {
		this.searchEngine = searchEngine;
	}
	
	@Size(min=0, max=200, message="搜索的关键词长度不能超过 200 个字符")
	public String getSearchWord() {
		return searchWord;
	}

	public void setSearchWord(String searchWord) {
		this.searchWord = searchWord;
	}
	
	@Size(min=0, max=50, message="客户IP地址长度不能超过 50 个字符")
	public String getRemoteAddr() {
		return remoteAddr;
	}

	public void setRemoteAddr(String remoteAddr) {
		this.remoteAddr = remoteAddr;
	}
	
	@Size(min=0, max=500, message="用户代理字符串长度不能超过 500 个字符")
	public String getUserAgent() {
		return userAgent;
	}

	public void setUserAgent(String userAgent) {
		this.userAgent = userAgent;
	}
	
	@Size(min=0, max=32, message="客户机语言长度不能超过 32 个字符")
	public String getUserLanguage() {
		return userLanguage;
	}

	public void setUserLanguage(String userLanguage) {
		this.userLanguage = userLanguage;
	}
	
	@Size(min=0, max=32, message="客户机屏幕大小0x0长度不能超过 32 个字符")
	public String getUserScreenSize() {
		return userScreenSize;
	}

	public void setUserScreenSize(String userScreenSize) {
		this.userScreenSize = userScreenSize;
	}
	
	@Size(min=0, max=32, message="客户机设备类型长度不能超过 32 个字符")
	public String getUserDevice() {
		return userDevice;
	}

	public void setUserDevice(String userDevice) {
		this.userDevice = userDevice;
	}
	
	@Size(min=0, max=32, message="客户机操作系统长度不能超过 32 个字符")
	public String getUserOsName() {
		return userOsName;
	}

	public void setUserOsName(String userOsName) {
		this.userOsName = userOsName;
	}
	
	@Size(min=0, max=32, message="客户机浏览器长度不能超过 32 个字符")
	public String getUserBrowser() {
		return userBrowser;
	}

	public void setUserBrowser(String userBrowser) {
		this.userBrowser = userBrowser;
	}
	
	@Size(min=0, max=16, message="浏览器版本长度不能超过 16 个字符")
	public String getUserBrowserVersion() {
		return userBrowserVersion;
	}

	public void setUserBrowserVersion(String userBrowserVersion) {
		this.userBrowserVersion = userBrowserVersion;
	}
	
	@Size(min=0, max=64, message="唯一访问标识长度不能超过 64 个字符")
	public String getUniqueVisitId() {
		return uniqueVisitId;
	}

	public void setUniqueVisitId(String uniqueVisitId) {
		this.uniqueVisitId = uniqueVisitId;
	}
	
	@Size(min=0, max=8, message="本次访问日期长度不能超过 8 个字符")
	public String getVisitDate() {
		return visitDate;
	}

	public void setVisitDate(String visitDate) {
		this.visitDate = visitDate;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	public Date getVisitTime() {
		return visitTime;
	}

	public void setVisitTime(Date visitTime) {
		this.visitTime = visitTime;
	}
	
	@Size(min=0, max=1, message="是否新访问长度不能超过 1 个字符")
	public String getIsNewVisit() {
		return isNewVisit;
	}

	public void setIsNewVisit(String isNewVisit) {
		this.isNewVisit = isNewVisit;
	}
	
	public Long getFirstVisitTime() {
		return firstVisitTime;
	}

	public void setFirstVisitTime(Long firstVisitTime) {
		this.firstVisitTime = firstVisitTime;
	}
	
	public Long getPrevRemainTime() {
		return prevRemainTime;
	}

	public void setPrevRemainTime(Long prevRemainTime) {
		this.prevRemainTime = prevRemainTime;
	}
	
	public Long getTotalRemainTime() {
		return totalRemainTime;
	}

	public void setTotalRemainTime(Long totalRemainTime) {
		this.totalRemainTime = totalRemainTime;
	}
	
	@Size(min=0, max=64, message="站点编码长度不能超过 64 个字符")
	public String getSiteCode() {
		return siteCode;
	}

	public void setSiteCode(String siteCode) {
		this.siteCode = siteCode;
	}
	
	@Size(min=0, max=100, message="站点名称长度不能超过 100 个字符")
	public String getSiteName() {
		return siteName;
	}

	public void setSiteName(String siteName) {
		this.siteName = siteName;
	}
	
	@Size(min=0, max=64, message="栏目编码长度不能超过 64 个字符")
	public String getCategoryCode() {
		return categoryCode;
	}

	public void setCategoryCode(String categoryCode) {
		this.categoryCode = categoryCode;
	}
	
	@Size(min=0, max=100, message="栏目名称长度不能超过 100 个字符")
	public String getCategoryName() {
		return categoryName;
	}

	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}
	
	@Size(min=0, max=64, message="栏目内容编号长度不能超过 64 个字符")
	public String getContentId() {
		return contentId;
	}

	public void setContentId(String contentId) {
		this.contentId = contentId;
	}
	
	@Size(min=0, max=255, message="访问页面标题长度不能超过 255 个字符")
	public String getContentTitle() {
		return contentTitle;
	}

	public void setContentTitle(String contentTitle) {
		this.contentTitle = contentTitle;
	}
	
	@Size(min=0, max=100, message="访问用户编码长度不能超过 100 个字符")
	public String getVisitUserCode() {
		return visitUserCode;
	}

	public void setVisitUserCode(String visitUserCode) {
		this.visitUserCode = visitUserCode;
	}
	
	@Size(min=0, max=100, message="访问用户姓名长度不能超过 100 个字符")
	public String getVisitUserName() {
		return visitUserName;
	}

	public void setVisitUserName(String visitUserName) {
		this.visitUserName = visitUserName;
	}
	
}