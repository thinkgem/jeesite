/**
 * Copyright (c) 2013-Now https://jeesite.com All rights reserved.
 * No deletion without permission, or be held responsible to law.
 */
package com.jeesite.modules.cms.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * CmsProperties
 * @author ThinkGem
 * @version 2025-12-23
 */
@ConfigurationProperties(prefix = "cms")
public class CmsProperties {

	private Bpm bpm = new Bpm();

	public static class Bpm {

		/**
		 * 是否启用 BPM 审核
		 */
		private Boolean enabled = false;

		public Boolean getEnabled() {
			return enabled;
		}

		public void setEnabled(Boolean enabled) {
			this.enabled = enabled;
		}

	}

	public Bpm getBpm() {
		return bpm;
	}

	public void setBpm(Bpm bpm) {
		this.bpm = bpm;
	}

	private Template template = new Template();

	public static class Template {

		/**
		 * 是否开启在线模版编辑
		 */
		private Boolean edit = false;

		public Boolean getEdit() {
			return edit;
		}

		public void setEdit(Boolean edit) {
			this.edit = edit;
		}
	}

	public Template getTemplate() {
		return template;
	}

	public void setTemplate(Template template) {
		this.template = template;
	}

	private PageCache pageCache = new PageCache();

	public static class PageCache {

		/**
		 * 是否开启页面静态化缓存
		 */
		private Boolean enabled = false;

		/**
		 * 缓存名称标识
		 */
		private String cacheName = "cmsPageCache";

		/**
		 * 拦截的网页地址
		 */
		private String urlPatterns = "${frontPath}/*";

		/**
		 * 只静态化 .html 后缀的网页
		 */
		private String urlSuffixes = ".html";

		public Boolean getEnabled() {
			return enabled;
		}

		public void setEnabled(Boolean enabled) {
			this.enabled = enabled;
		}

		public String getCacheName() {
			return cacheName;
		}

		public void setCacheName(String cacheName) {
			this.cacheName = cacheName;
		}

		public String getUrlPatterns() {
			return urlPatterns;
		}

		public void setUrlPatterns(String urlPatterns) {
			this.urlPatterns = urlPatterns;
		}

		public String getUrlSuffixes() {
			return urlSuffixes;
		}

		public void setUrlSuffixes(String urlSuffixes) {
			this.urlSuffixes = urlSuffixes;
		}
	}

	public PageCache getPageCache() {
		return pageCache;
	}

	public void setPageCache(PageCache pageCache) {
		this.pageCache = pageCache;
	}
}
