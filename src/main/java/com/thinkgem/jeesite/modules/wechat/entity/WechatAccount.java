/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.wechat.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.thinkgem.jeesite.common.persistence.DataEntity;
import org.hibernate.validator.constraints.Length;

import java.util.Date;

/**
 * 微信的账户管理Entity
 * @author mazekkkk
 * @version 2016-07-27
 */
public class WechatAccount extends DataEntity<WechatAccount> {
	
	private static final long serialVersionUID = 1L;
	private String appId;		// 微信 app_id
	private String appSecret;		// 微信 app密码
	private String token;		// 微信 token
	private String accessToken;		// 访问 token
	private String aesKey;		// 加密key
	private String aesEncodingKey;		// 加密EncodeKey
	private String agentId;		// 代理id
	private Long expiresTime;		// 过期时间
	private String oauth2redirectUri;		// 授权地址
	private String httpProxyHost;		// 代理主机
	private Integer httpProxyPort;		// 代理主机端口
	private String httpProxyUsername;		// 代理主机用户名
	private String httpProxyPassword;		// 代理主机密码
	private String jsapiTicket;		// jsapi票根
	private Date jsapiTicketExpiresTime;		// jsapi 票根过期时间
	private String tmpDirFile;		// 临时文件目录
	private Integer status;		// 状态
	private Date addTime;		// 添加时间
	
	public WechatAccount() {
		super();
	}

	public WechatAccount(String id){
		super(id);
	}

	@Length(min=1, max=64, message="微信 app_id长度必须介于 1 和 64 之间")
	public String getAppId() {
		return appId;
	}

	public void setAppId(String appId) {
		this.appId = appId;
	}
	
	@Length(min=1, max=64, message="微信 app密码长度必须介于 1 和 64 之间")
	public String getAppSecret() {
		return appSecret;
	}

	public void setAppSecret(String appSecret) {
		this.appSecret = appSecret;
	}
	
	@Length(min=1, max=64, message="微信 token长度必须介于 1 和 64 之间")
	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}
	
	@Length(min=0, max=64, message="访问 token长度必须介于 0 和 64 之间")
	public String getAccessToken() {
		return accessToken;
	}

	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}
	
	@Length(min=0, max=64, message="加密key长度必须介于 0 和 64 之间")
	public String getAesKey() {
		return aesKey;
	}

	public void setAesKey(String aesKey) {
		this.aesKey = aesKey;
	}
	
	@Length(min=0, max=64, message="加密EncodeKey长度必须介于 0 和 64 之间")
	public String getAesEncodingKey() {
		return aesEncodingKey;
	}

	public void setAesEncodingKey(String aesEncodingKey) {
		this.aesEncodingKey = aesEncodingKey;
	}
	
	@Length(min=0, max=64, message="代理id长度必须介于 0 和 64 之间")
	public String getAgentId() {
		return agentId;
	}

	public void setAgentId(String agentId) {
		this.agentId = agentId;
	}
	
	public Long getExpiresTime() {
		return expiresTime;
	}

	public void setExpiresTime(Long expiresTime) {
		this.expiresTime = expiresTime;
	}
	
	@Length(min=0, max=64, message="授权地址长度必须介于 0 和 64 之间")
	public String getOauth2redirectUri() {
		return oauth2redirectUri;
	}

	public void setOauth2redirectUri(String oauth2redirectUri) {
		this.oauth2redirectUri = oauth2redirectUri;
	}
	
	@Length(min=0, max=255, message="代理主机长度必须介于 0 和 255 之间")
	public String getHttpProxyHost() {
		return httpProxyHost;
	}

	public void setHttpProxyHost(String httpProxyHost) {
		this.httpProxyHost = httpProxyHost;
	}
	
	public Integer getHttpProxyPort() {
		return httpProxyPort;
	}

	public void setHttpProxyPort(Integer httpProxyPort) {
		this.httpProxyPort = httpProxyPort;
	}
	
	@Length(min=0, max=64, message="代理主机用户名长度必须介于 0 和 64 之间")
	public String getHttpProxyUsername() {
		return httpProxyUsername;
	}

	public void setHttpProxyUsername(String httpProxyUsername) {
		this.httpProxyUsername = httpProxyUsername;
	}
	
	@Length(min=0, max=64, message="代理主机密码长度必须介于 0 和 64 之间")
	public String getHttpProxyPassword() {
		return httpProxyPassword;
	}

	public void setHttpProxyPassword(String httpProxyPassword) {
		this.httpProxyPassword = httpProxyPassword;
	}
	
	@Length(min=0, max=255, message="jsapi票根长度必须介于 0 和 255 之间")
	public String getJsapiTicket() {
		return jsapiTicket;
	}

	public void setJsapiTicket(String jsapiTicket) {
		this.jsapiTicket = jsapiTicket;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	public Date getJsapiTicketExpiresTime() {
		return jsapiTicketExpiresTime;
	}

	public void setJsapiTicketExpiresTime(Date jsapiTicketExpiresTime) {
		this.jsapiTicketExpiresTime = jsapiTicketExpiresTime;
	}
	
	@Length(min=0, max=255, message="临时文件目录长度必须介于 0 和 255 之间")
	public String getTmpDirFile() {
		return tmpDirFile;
	}

	public void setTmpDirFile(String tmpDirFile) {
		this.tmpDirFile = tmpDirFile;
	}
	
	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	public Date getAddTime() {
		return addTime;
	}

	public void setAddTime(Date addTime) {
		this.addTime = addTime;
	}
	
}