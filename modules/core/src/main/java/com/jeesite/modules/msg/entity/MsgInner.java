/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.modules.msg.entity;

import java.util.Date;

import javax.validation.constraints.NotBlank;

import org.hibernate.validator.constraints.Length;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.jeesite.common.entity.DataEntity;
import com.jeesite.common.lang.DateUtils;
import com.jeesite.common.mybatis.annotation.Column;
import com.jeesite.common.mybatis.annotation.Table;
import com.jeesite.common.mybatis.mapper.query.QueryType;

/**
 * 内部消息Entity
 * @author ThinkGem
 * @version 2019-03-12
 */
@Table(name="${_prefix}sys_msg_inner", alias="a", columns={
		@Column(name="id", attrName="id", label="编号", isPK=true),
		@Column(name="msg_title", attrName="msgTitle", label="消息标题", queryType=QueryType.LIKE),
		@Column(name="content_level", attrName="contentLevel", label="内容等级", comment="内容等级（1普通 2一般 3紧急）"),
		@Column(name="content_type", attrName="contentType", label="内容类型", comment="内容类型（1公告 2新闻 3会议 4其它）"),
		@Column(name="msg_content", attrName="msgContent", label="消息内容"),
		@Column(name="receive_type", attrName="receiveType", label="接受者类型", comment="接受者类型（1用户 2部门 3角色 4岗位）"),
		@Column(name="receive_codes", attrName="receiveCodes", label="接受者字符串"),
		@Column(name="receive_names", attrName="receiveNames", label="接受者名称字符串", queryType=QueryType.LIKE),
		@Column(name="send_user_code", attrName="sendUserCode", label="发送者用户编码"),
		@Column(name="send_user_name", attrName="sendUserName", label="发送者用户姓名", queryType=QueryType.LIKE),
		@Column(name="send_date", attrName="sendDate", label="发送时间"),
		@Column(name="is_attac", attrName="isAttac", label="是否有附件"),
		@Column(name="notify_types", attrName="notifyTypes", label="通知类型", comment="通知类型（PC APP 短信 邮件 微信）多选"),
		@Column(includeEntity=DataEntity.class, comment="状态（0正常 1删除 4审核 5驳回 9草稿）"),
	}, orderBy="a.update_date DESC"
)
public class MsgInner extends DataEntity<MsgInner> {

	// 接受者类型（0所有 1用户 2部门 3角色 4岗位）
	public static final String RECEIVE_TYPE_ALL = "0";
	public static final String RECEIVE_TYPE_USER = "1";
	public static final String RECEIVE_TYPE_OFFICE = "2";
	public static final String RECEIVE_TYPE_ROLE = "3";
	public static final String RECEIVE_TYPE_POST = "4";
	
	// 内容级别（1普通 2一般 3紧急）
	public static final String CONTENT_LEVEL_1 = "1";
	public static final String CONTENT_LEVEL_2 = "2";
	public static final String CONTENT_LEVEL_3 = "3";
	
	private static final long serialVersionUID = 1L;
	private String msgTitle;		// 消息标题
	private String contentLevel;	// 内容等级（1普通 2一般 3紧急）
	private String contentType;		// 内容类型（1公告 2新闻 3会议 4其它）
	private String msgContent;		// 消息内容
	private String receiveType;		// 接受者类型（1用户 2部门 3角色 4岗位）
	private String receiveCodes;	// 接受者字符串
	private String receiveNames;	// 接受者名称字符串
	private String sendUserCode;	// 发送者用户编码
	private String sendUserName;	// 发送者用户姓名
	private Date sendDate;			// 发送时间
	private String isAttac;			// 是否有附件
	private String notifyTypes;		// 通知类型（PC APP 短信 邮件 微信）多选
	
	private MsgInnerRecord record;	// 消息记录状态
	
	public MsgInner() {
		this(null);
	}

	public MsgInner(String id){
		super(id);
	}
	
	@NotBlank(message="消息标题不能为空")
	@Length(min=0, max=200, message="消息标题长度不能超过 200 个字符")
	public String getMsgTitle() {
		return msgTitle;
	}

	public void setMsgTitle(String msgTitle) {
		this.msgTitle = msgTitle;
	}
	
	@NotBlank(message="内容级别不能为空")
	@Length(min=0, max=1, message="内容级别长度不能超过 1 个字符")
	public String getContentLevel() {
		return contentLevel;
	}

	public void setContentLevel(String contentLevel) {
		this.contentLevel = contentLevel;
	}
	
	@Length(min=0, max=1, message="内容类型长度不能超过 1 个字符")
	public String getContentType() {
		return contentType;
	}

	public void setContentType(String contentType) {
		this.contentType = contentType;
	}
	
	@NotBlank(message="消息内容不能为空")
	public String getMsgContent() {
		return msgContent;
	}

	public void setMsgContent(String msgContent) {
		this.msgContent = msgContent;
	}
	
	@NotBlank(message="接受者类型不能为空")
	@Length(min=0, max=1, message="接受者类型长度不能超过 1 个字符")
	public String getReceiveType() {
		return receiveType;
	}

	public void setReceiveType(String receiveType) {
		this.receiveType = receiveType;
	}
	
	public String getReceiveCodes() {
		return receiveCodes;
	}

	public void setReceiveCodes(String receiveCodes) {
		this.receiveCodes = receiveCodes;
	}
	
	public String getReceiveNames() {
		return receiveNames;
	}

	public void setReceiveNames(String receiveNames) {
		this.receiveNames = receiveNames;
	}
	
	@Length(min=0, max=64, message="发送者用户编码长度不能超过 64 个字符")
	public String getSendUserCode() {
		return sendUserCode;
	}

	public void setSendUserCode(String sendUserCode) {
		this.sendUserCode = sendUserCode;
	}
	
	@Length(min=0, max=100, message="发送者用户姓名长度不能超过 100 个字符")
	public String getSendUserName() {
		return sendUserName;
	}

	public void setSendUserName(String sendUserName) {
		this.sendUserName = sendUserName;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	public Date getSendDate() {
		return sendDate;
	}

	public void setSendDate(Date sendDate) {
		this.sendDate = sendDate;
	}
	
	public Date getSendDate_gte() {
		return sqlMap.getWhere().getValue("send_date", QueryType.GTE);
	}

	public void setSendDate_gte(Date sendDate) {
		sendDate = DateUtils.getOfDayFirst(sendDate);
		sqlMap.getWhere().and("send_date", QueryType.GTE, sendDate);
	}
	
	public Date getSendDate_lte() {
		return sqlMap.getWhere().getValue("send_date", QueryType.LTE);
	}

	public void setSendDate_lte(Date sendDate) {
		sendDate = DateUtils.getOfDayLast(sendDate);
		sqlMap.getWhere().and("send_date", QueryType.LTE, sendDate);
	}
	
	@Length(min=0, max=1, message="是否有附件长度不能超过 1 个字符")
	public String getIsAttac() {
		return isAttac;
	}

	public void setIsAttac(String isAttac) {
		this.isAttac = isAttac;
	}
	
	@Length(min=0, max=100, message="通知类型长度不能超过 100 个字符")
	public String getNotifyTypes() {
		return notifyTypes;
	}

	public void setNotifyTypes(String notifyTypes) {
		this.notifyTypes = notifyTypes;
	}

	public MsgInnerRecord getRecord() {
		return record;
	}

	public void setRecord(MsgInnerRecord record) {
		this.record = record;
	}
	
}