/**
 * There are <a href="https://github.com/thinkgem/jeesite">JeeSite</a> code generation
 */
package com.thinkgem.jeesite.modules.oa.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.validator.constraints.Length;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.thinkgem.jeesite.common.persistence.IdEntity;
import com.thinkgem.jeesite.modules.sys.utils.DictUtils;

/**
 * 请假Entity
 * @author liuj
 * @version 2013-04-05
 */
@Entity
@Table(name = "oa_leave")
@DynamicInsert @DynamicUpdate
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Leave extends IdEntity<Leave> {
	
	private static final long serialVersionUID = 1L;
	private String reason; 	// 请假原因
	private String processInstanceId; // 流程实例编号
	private Date startTime;	// 请假开始日期
	private Date endTime;	// 请假结束日期
	private Date realityStartTime;	// 实际开始时间
	private Date realityEndTime;	// 实际结束时间
	private String leaveType;	// 假种
	private String processStatus; //流程状态
	
	private boolean pass;
	private boolean audit;
	private String auditRemarks; 

	public Leave() {
		super();
	}

	public Leave(String id){
		this();
		this.id = id;
	}
	
	public String getLeaveType() {
		return leaveType;
	}

	public void setLeaveType(String leaveType) {
		this.leaveType = leaveType;
	}
	
	@Transient
	public String getLeaveTypeDictLabel() {
		return DictUtils.getDictLabel(leaveType, "oa_leave_type", "");
	}
	
	@Length(min=1, max=255)
	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public String getProcessInstanceId() {
		return processInstanceId;
	}

	public void setProcessInstanceId(String processInstanceId) {
		this.processInstanceId = processInstanceId;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	public Date getRealityStartTime() {
		return realityStartTime;
	}

	public void setRealityStartTime(Date realityStartTime) {
		this.realityStartTime = realityStartTime;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	public Date getRealityEndTime() {
		return realityEndTime;
	}

	public void setRealityEndTime(Date realityEndTime) {
		this.realityEndTime = realityEndTime;
	}
	
	public String getProcessStatus() {
		return processStatus;
	}

	public void setProcessStatus(String processStatus) {
		this.processStatus = processStatus;
	}

	@Transient
	public boolean isPass() {
		return pass;
	}

	@Transient
	public void setPass(boolean pass) {
		this.pass = pass;
	}

	@Transient
	public String getAuditRemarks() {
		return auditRemarks;
	}

	@Transient
	public void setAuditRemarks(String auditRemarks) {
		this.auditRemarks = auditRemarks;
	}

	@Transient
	public boolean isAudit() {
		return audit;
	}

	@Transient
	public void setAudit(boolean audit) {
		this.audit = audit;
	}
	
	
}


