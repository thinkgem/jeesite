/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.modules.msg.entity;

import java.util.Date;

import javax.validation.constraints.NotBlank;

import org.hibernate.validator.constraints.Length;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.jeesite.common.entity.DataEntity;
import com.jeesite.common.mybatis.annotation.Column;
import com.jeesite.common.mybatis.annotation.Table;
import com.jeesite.common.mybatis.mapper.query.QueryType;

/**
 * 内部消息发送记录表Entity
 * @author ThinkGem
 * @version 2019-03-12
 */
@Table(name="${_prefix}sys_msg_inner_record", alias="a", columns={
		@Column(name="id", attrName="id", label="编号", isPK=true),
		@Column(name="msg_inner_id", attrName="msgInnerId", label="所属消息"),
		@Column(name="receive_user_code", attrName="receiveUserCode", label="接受者用户编码"),
		@Column(name="receive_user_name", attrName="receiveUserName", label="接受者用户姓名", queryType=QueryType.LIKE),
		@Column(name="read_status", attrName="readStatus", label="读取状态", comment="读取状态（0未送达 1已读 2未读）"),
		@Column(name="read_date", attrName="readDate", label="阅读时间"),
		@Column(name="is_star", attrName="isStar", label="是否标星"),
	}, orderBy="a.read_date ASC, a.id ASC"
)
public class MsgInnerRecord extends DataEntity<MsgInnerRecord> {

	// 读取状态（0未送达 1已读 2未读）
	public static final String READ_STATUS_READ = "1";
	public static final String READ_STATUS_UNREAD = "2";
	
	private static final long serialVersionUID = 1L;
	private String msgInnerId;			// 所属消息
	private String receiveUserCode;		// 接受者用户编码
	private String receiveUserName;		// 接受者用户姓名
	private String readStatus;			// 读取状态（0未送达 1已读 2未读）
	private Date readDate;				// 阅读时间
	private String isStar;				// 是否标星
	
	public MsgInnerRecord() {
		this(null);
	}

	public MsgInnerRecord(String id){
		super(id);
	}
	
	@NotBlank(message="所属消息不能为空")
	@Length(min=0, max=64, message="所属消息长度不能超过 64 个字符")
	public String getMsgInnerId() {
		return msgInnerId;
	}

	public void setMsgInnerId(String msgInnerId) {
		this.msgInnerId = msgInnerId;
	}
	
	@Length(min=0, max=64, message="接受者用户编码长度不能超过 64 个字符")
	public String getReceiveUserCode() {
		return receiveUserCode;
	}

	public void setReceiveUserCode(String receiveUserCode) {
		this.receiveUserCode = receiveUserCode;
	}
	
	@NotBlank(message="接受者用户姓名不能为空")
	@Length(min=0, max=100, message="接受者用户姓名长度不能超过 100 个字符")
	public String getReceiveUserName() {
		return receiveUserName;
	}

	public void setReceiveUserName(String receiveUserName) {
		this.receiveUserName = receiveUserName;
	}
	
	@NotBlank(message="读取状态不能为空")
	@Length(min=0, max=1, message="读取状态长度不能超过 1 个字符")
	public String getReadStatus() {
		return readStatus;
	}

	public void setReadStatus(String readStatus) {
		this.readStatus = readStatus;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	public Date getReadDate() {
		return readDate;
	}

	public void setReadDate(Date readDate) {
		this.readDate = readDate;
	}
	
	@Length(min=0, max=1, message="是否标星长度不能超过 1 个字符")
	public String getIsStar() {
		return isStar;
	}

	public void setIsStar(String isStar) {
		this.isStar = isStar;
	}
	
}