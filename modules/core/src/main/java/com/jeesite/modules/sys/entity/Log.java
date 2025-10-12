/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 * No deletion without permission, or be held responsible to law.
 */
package com.jeesite.modules.sys.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.jeesite.common.codec.EncodeUtils;
import com.jeesite.common.collect.MapUtils;
import com.jeesite.common.entity.BaseEntity;
import com.jeesite.common.entity.DataEntity;
import com.jeesite.common.lang.StringUtils;
import com.jeesite.common.lang.TimeUtils;
import com.jeesite.common.mybatis.annotation.Column;
import com.jeesite.common.mybatis.annotation.Table;
import com.jeesite.common.mybatis.mapper.query.QueryType;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.Date;
import java.util.Map;

/**
 * 操作日志Entity
 * @author ThinkGem
 * @version 2017-03-19
 */
@Table(name="${_prefix}sys_log", alias="a", label="操作日志", columns={
		@Column(includeEntity=BaseEntity.class),
		@Column(name="id", 				attrName="id", 				label="编码", isPK=true),
		@Column(name="log_type", 		attrName="logType", 		label="日志类型"),
		@Column(name="log_title", 		attrName="logTitle", 		label="日志标题", queryType=QueryType.LIKE),
		@Column(name="create_by", 		attrName="createBy", 		label="创建者", isUpdate=false),
		@Column(name="create_by_name", 	attrName="createByName",	label="创建者名称", isUpdate=false, queryType=QueryType.LIKE),
		@Column(name="create_date", 	attrName="createDate", 		label="创建时间", isUpdate=false, isQuery=false),
		@Column(name="request_uri", 	attrName="requestUri", 		label="请求URI", queryType=QueryType.LIKE),
		@Column(name="request_method", 	attrName="requestMethod", 	label="操作方式"),
		@Column(name="request_params", 	attrName="requestParams", 	label="操作提交的数据", queryType=QueryType.LIKE),
		@Column(name="diff_modify_data",attrName="diffModifyData", 	label="差异修改数据", queryType=QueryType.LIKE),
		@Column(name="biz_key", 		attrName="bizKey", 			label="业务主键"),
		@Column(name="biz_type", 		attrName="bizType", 		label="业务类型"),
		@Column(name="remote_addr", 	attrName="remoteAddr", 		label="操作IP地址"),
		@Column(name="server_addr", 	attrName="serverAddr", 		label="请求服务器地址"),
		@Column(name="is_exception", 	attrName="isException", 	label="是否异常"),
		@Column(name="exception_info", 	attrName="exceptionInfo", 	label="异常信息"),
		@Column(name="user_agent", 		attrName="userAgent", 		label="用户代理"),
		@Column(name="device_name", 	attrName="deviceName", 		label="设备名称/操作系统", queryType=QueryType.LIKE),
		@Column(name="browser_name", 	attrName="browserName", 	label="浏览器名称", queryType=QueryType.LIKE),
		@Column(name="execute_time", 	attrName="executeTime", 	label="执行时间"),
	}, orderBy="a.create_date DESC"
)
public class Log extends DataEntity<Log> {

	// 日志类型（access：接入日志；update：修改日志；select：查询日志；loginLogout：登录登出；）
	public static final String TYPE_ACCESS = "access";
	public static final String TYPE_UPDATE = "update";
	public static final String TYPE_SELECT = "select";
	public static final String TYPE_LOGIN_LOGOUT = "loginLogout";
	
	private static final long serialVersionUID = 1L;
	
	private String logType;			// 日志类型
	private String logTitle;		// 日志标题
	private String requestUri;		// 请求URI
	private String requestMethod;	// 操作方式
	private String requestParams;	// 操作提交的数据
	private String diffModifyData;	// 差异修改数据
	private String bizKey;			// 业务主键
	private String bizType;			// 业务类型
	private String remoteAddr;		// 操作IP地址
	private String serverAddr;		// 请求服务器地址
	private String isException;		// 是否有异常
	private String exceptionInfo;	// 异常信息
	private String userAgent;		// 用户代理
	private String deviceName;		// 设备名称/操作系统
	private String browserName;		// 浏览器名称
	private Long executeTime;		// 执行时间
	
	private Map<String, String[]> paramsMap; 		// 操作提交的数据，临时存储用
	
	public Log() {
		this(null);
	}

	public Log(String id){
		super(id);
	}
	
	@NotBlank(message="日志类型不能为空")
	@Size(min=0, max=1, message="日志类型长度不能超过 1 个字符")
	public String getLogType() {
		return logType;
	}

	public void setLogType(String logType) {
		this.logType = logType;
	}
	
	@NotBlank(message="日志标题不能为空")
	@Size(min=0, max=500, message="日志标题长度不能超过 500 个字符")
	public String getLogTitle() {
		return logTitle;
	}

	public void setLogTitle(String logTitle) {
		this.logTitle = logTitle;
	}
	
	@Size(min=0, max=500, message="请求URI长度不能超过 500 个字符")
	public String getRequestUri() {
		return requestUri;
	}

	public void setRequestUri(String requestUri) {
		this.requestUri = requestUri;
	}
	
	@Size(min=0, max=10, message="操作方式长度不能超过 10 个字符")
	public String getRequestMethod() {
		return requestMethod;
	}

	public void setRequestMethod(String requestMethod) {
		this.requestMethod = requestMethod;
	}
	
	public String getRequestParams() {
		return requestParams;
	}

	public void setRequestParams(String requestParams) {
		this.requestParams = requestParams;
	}
	
	public String getDiffModifyData() {
		return diffModifyData;
	}

	public void setDiffModifyData(String diffModifyData) {
		this.diffModifyData = diffModifyData;
	}

	@Size(min=0, max=64, message="业务主键长度不能超过 64 个字符")
	public String getBizKey() {
		return bizKey;
	}

	public void setBizKey(String bizKey) {
		this.bizKey = bizKey;
	}
	
	@Size(min=0, max=64, message="业务类型长度不能超过 64 个字符")
	public String getBizType() {
		return bizType;
	}

	public void setBizType(String bizType) {
		this.bizType = bizType;
	}
	
	@NotBlank(message="操作IP地址不能为空")
	@Size(min=0, max=255, message="操作IP地址长度不能超过 255 个字符")
	public String getRemoteAddr() {
		return remoteAddr;
	}

	public void setRemoteAddr(String remoteAddr) {
		this.remoteAddr = remoteAddr;
	}
	
	@NotBlank(message="请求服务器地址不能为空")
	@Size(min=0, max=255, message="请求服务器地址长度不能超过 255 个字符")
	public String getServerAddr() {
		return serverAddr;
	}

	public void setServerAddr(String serverAddr) {
		this.serverAddr = serverAddr;
	}
	
	public String getIsException() {
		return isException;
	}

	public void setIsException(String isException) {
		this.isException = isException;
	}

	public String getExceptionInfo() {
		return exceptionInfo;
	}

	public void setExceptionInfo(String exceptionInfo) {
		this.exceptionInfo = exceptionInfo;
	}
	
	@Size(min=0, max=500, message="用户代理长度不能超过 500 个字符")
	public String getUserAgent() {
		return userAgent;
	}

	public void setUserAgent(String userAgent) {
		this.userAgent = userAgent;
	}
	
	@Size(min=0, max=100, message="设备名称/操作系统长度不能超过 100 个字符")
	public String getDeviceName() {
		return deviceName;
	}

	public void setDeviceName(String deviceName) {
		this.deviceName = deviceName;
	}
	
	@Size(min=0, max=100, message="浏览器名称长度不能超过 100 个字符")
	public String getBrowserName() {
		return browserName;
	}

	public void setBrowserName(String browserName) {
		this.browserName = browserName;
	}
	
	public Long getExecuteTime() {
		return executeTime;
	}

	public void setExecuteTime(Long executeTime) {
		this.executeTime = executeTime;
	}

	@ApiModelProperty("格式化后的执行时间")
	public String getExecuteTimeFormat(){
		if (executeTime == null) {
			executeTime = 0L;
		}
		return TimeUtils.formatTime(executeTime);
	}

	/**
	 * 设置请求参数
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void setRequestParams(Map paramsMap){
		if (paramsMap == null){
			return;
		}
		if (this.paramsMap == null){
			this.paramsMap = MapUtils.newHashMap();
		}
		StringBuilder params = new StringBuilder();
		for (Map.Entry<String, String[]> param : ((Map<String, String[]>)paramsMap).entrySet()){
			if (params.length() != 0) {
				params.append("&");
			}
			params.append(param.getKey() + "=");
			if (StringUtils.endsWithIgnoreCase(param.getKey(), "password")){
				params.append("*");
			}else if (param.getValue() != null) {
				params.append(EncodeUtils.xssFilter(StringUtils.abbr(
						StringUtils.joinComma(param.getValue()), 1000)));
			}
			String[] values = param.getValue();
			if (values != null) {
				for (int i=0; i<values.length; i++) {
					values[i] = EncodeUtils.xssFilter(values[i]);
				}
			}
			this.paramsMap.put(param.getKey(), values);
		}
		this.requestParams = params.toString();
	}

	/**
	 * 根据名称获取参数（只有先执行setParams(Map)后才有效）
	 */
	public String getRequestParam(String name) {
		if (paramsMap == null){
			return null;
		}
        String[] values = (String[])paramsMap.get(name);
        return values != null && values.length > 0 ? values[0] : null;
    }

	@Override
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	public Date getCreateDate() {
		return super.getCreateDate();
	}
	
//	@Override
//	public String toString() {
//		return ReflectionToStringBuilder.toString(this);
//	}
	
}