/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 * No deletion without permission, or be held responsible to law.
 */
package com.jeesite.modules.cms.entity;

import javax.validation.constraints.Size;

import com.jeesite.common.entity.DataEntity;
import com.jeesite.common.mybatis.annotation.Column;
import com.jeesite.common.mybatis.annotation.Table;

/**
 * 内容举报表Entity
 * @author 长春叭哥、ThinkGem
 * @version 2018-10-15
 */
@Table(name="${_prefix}cms_report", alias="a", columns={
		@Column(name="id", attrName="id", label="编号", isPK=true),
		@Column(name="report_source", attrName="reportSource", label="举报来源", comment="举报来源（1文章、2评论）"),
		@Column(name="report_content", attrName="reportContent", label="举报内容", comment="举报内容（文章标题 评论内容）"),
		@Column(name="report_url", attrName="reportUrl", label="举报的URL"),
		@Column(name="report_type", attrName="reportType", label="举报类型", comment="举报类型（色情 政治...）"),
		@Column(name="report_cause", attrName="reportCause", label="举报原因"),
	}, orderBy="a.id DESC"
)
public class Report extends DataEntity<Report> {
	
	private static final long serialVersionUID = 1L;
	private String reportSource;	// 举报来源（1文章、2评论）
	private String reportContent;	// 举报内容（文章标题 评论内容）
	private String reportUrl;		// 举报的URL
	private String reportType;		// 举报类型（色情 政治...）
	private String reportCause;		// 举报原因
	
	public Report() {
		this(null);
	}

	public Report(String id){
		super(id);
	}
	
	@Size(min=0, max=1, message="举报来源长度不能超过 1 个字符")
	public String getReportSource() {
		return reportSource;
	}

	public void setReportSource(String reportSource) {
		this.reportSource = reportSource;
	}
	
	@Size(min=0, max=500, message="举报内容长度不能超过 500 个字符")
	public String getReportContent() {
		return reportContent;
	}

	public void setReportContent(String reportContent) {
		this.reportContent = reportContent;
	}
	
	@Size(min=0, max=1000, message="举报的URL长度不能超过 1000 个字符")
	public String getReportUrl() {
		return reportUrl;
	}

	public void setReportUrl(String reportUrl) {
		this.reportUrl = reportUrl;
	}
	
	@Size(min=0, max=1, message="举报类型长度不能超过 1 个字符")
	public String getReportType() {
		return reportType;
	}

	public void setReportType(String reportType) {
		this.reportType = reportType;
	}
	
	@Size(min=0, max=500, message="举报原因长度不能超过 500 个字符")
	public String getReportCause() {
		return reportCause;
	}

	public void setReportCause(String reportCause) {
		this.reportCause = reportCause;
	}
	
}