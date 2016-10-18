/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.act.entity;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.activiti.engine.history.HistoricActivityInstance;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.thinkgem.jeesite.common.persistence.BaseEntity;
import com.thinkgem.jeesite.common.utils.StringUtils;
import com.thinkgem.jeesite.common.utils.TimeUtils;
import com.thinkgem.jeesite.modules.act.utils.Variable;

/**
 * 工作流Entity
 * @author ThinkGem
 * @version 2013-11-03
 */
public class Act extends BaseEntity<Act> {
	
	private static final long serialVersionUID = 1L;

	private String taskId; 		// 任务编号
	private String taskName; 	// 任务名称
	private String taskDefKey; 	// 任务定义Key（任务环节标识）

	private String procInsId; 	// 流程实例ID
	private String procDefId; 	// 流程定义ID
	private String procDefKey; 	// 流程定义Key（流程定义标识）

	private String businessTable;	// 业务绑定Table
	private String businessId;		// 业务绑定ID
	
	private String title; 		// 任务标题

	private String status; 		// 任务状态（todo/claim/finish）

//	private String procExecUrl; 	// 流程执行（办理）RUL
	private String comment; 	// 任务意见
	private String flag; 		// 意见状态
	
	private Task task; 			// 任务对象
	private ProcessDefinition procDef; 	// 流程定义对象
	private ProcessInstance procIns;	// 流程实例对象
	private HistoricTaskInstance histTask; // 历史任务
	private HistoricActivityInstance histIns;	//历史活动任务

	private String assignee; // 任务执行人编号
	private String assigneeName; // 任务执行人名称

	private Variable vars; 		// 流程变量
//	private Variable taskVars; 	// 流程任务变量
	
	private Date beginDate;	// 开始查询日期
	private Date endDate;	// 结束查询日期

	private List<Act> list; // 任务列表

	public Act() {
		super();
	}

	public String getTaskId() {
		if (taskId == null && task != null){
			taskId = task.getId();
		}
		return taskId;
	}

	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}

	public String getTaskName() {
		if (taskName == null && task != null){
			taskName = task.getName();
		}
		return taskName;
	}

	public void setTaskName(String taskName) {
		this.taskName = taskName;
	}

	public String getTaskDefKey() {
		if (taskDefKey == null && task != null){
			taskDefKey = task.getTaskDefinitionKey();
		}
		return taskDefKey;
	}

	public void setTaskDefKey(String taskDefKey) {
		this.taskDefKey = taskDefKey;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	public Date getTaskCreateDate() {
		if (task != null){
			return task.getCreateTime();
		}
		return null;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	public Date getTaskEndDate() {
		if (histTask != null){
			return histTask.getEndTime();
		}
		return null;
	}
	
	@JsonIgnore
	public Task getTask() {
		return task;
	}

	public void setTask(Task task) {
		this.task = task;
	}

	@JsonIgnore
	public ProcessDefinition getProcDef() {
		return procDef;
	}

	public void setProcDef(ProcessDefinition procDef) {
		this.procDef = procDef;
	}
	
	public String getProcDefName() {
		return procDef.getName();
	}

	@JsonIgnore
	public ProcessInstance getProcIns() {
		return procIns;
	}

	public void setProcIns(ProcessInstance procIns) {
		this.procIns = procIns;
		if (procIns != null && procIns.getBusinessKey() != null){
			String[] ss = procIns.getBusinessKey().split(":");
			setBusinessTable(ss[0]);
			setBusinessId(ss[1]);
		}
	}

//	public String getProcExecUrl() {
//		return procExecUrl;
//	}
//
//	public void setProcExecUrl(String procExecUrl) {
//		this.procExecUrl = procExecUrl;
//	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}
	
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	@JsonIgnore
	public HistoricTaskInstance getHistTask() {
		return histTask;
	}

	public void setHistTask(HistoricTaskInstance histTask) {
		this.histTask = histTask;
	}

	@JsonIgnore
	public HistoricActivityInstance getHistIns() {
		return histIns;
	}

	public void setHistIns(HistoricActivityInstance histIns) {
		this.histIns = histIns;
	}

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	public Date getBeginDate() {
		return beginDate;
	}

	public void setBeginDate(Date beginDate) {
		this.beginDate = beginDate;
	}

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public String getFlag() {
		return flag;
	}

	public void setFlag(String flag) {
		this.flag = flag;
	}

	public String getProcDefId() {
		if (procDefId == null && task != null){
			procDefId = task.getProcessDefinitionId();
		}
		return procDefId;
	}

	public void setProcDefId(String procDefId) {
		this.procDefId = procDefId;
	}

	public String getProcInsId() {
		if (procInsId == null && task != null){
			procInsId = task.getProcessInstanceId();
		}
		return procInsId;
	}

	public void setProcInsId(String procInsId) {
		this.procInsId = procInsId;
	}

	public String getBusinessId() {
		return businessId;
	}

	public void setBusinessId(String businessId) {
		this.businessId = businessId;
	}

	public String getBusinessTable() {
		return businessTable;
	}

	public void setBusinessTable(String businessTable) {
		this.businessTable = businessTable;
	}

	public String getAssigneeName() {
		return assigneeName;
	}

	public void setAssigneeName(String assigneeName) {
		this.assigneeName = assigneeName;
	}

	public String getAssignee() {
		if (assignee == null && task != null){
			assignee = task.getAssignee();
		}
		return assignee;
	}

	public void setAssignee(String assignee) {
		this.assignee = assignee;
	}

	public List<Act> getList() {
		return list;
	}

	public void setList(List<Act> list) {
		this.list = list;
	}

	public Variable getVars() {
		return vars;
	}

	public void setVars(Variable vars) {
		this.vars = vars;
	}
	
	/**
	 * 通过Map设置流程变量值
	 * @param map
	 */
	public void setVars(Map<String, Object> map) {
		this.vars = new Variable(map);
	}

//	public Variable getTaskVars() {
//		return taskVars;
//	}
//
//	public void setTaskVars(Variable taskVars) {
//		this.taskVars = taskVars;
//	}
//
//	/**
//	 * 通过Map设置流程任务变量值
//	 * @param map
//	 */
//	public void setTaskVars(Map<String, Object> map) {
//		this.taskVars = new Variable(map);
//	}

	/**
	 * 获取流程定义KEY
	 * @return
	 */
	public String getProcDefKey() {
		if (StringUtils.isBlank(procDefKey) && StringUtils.isNotBlank(procDefId)){
			procDefKey = StringUtils.split(procDefId, ":")[0];
		}
		return procDefKey;
	}

	public void setProcDefKey(String procDefKey) {
		this.procDefKey = procDefKey;
	}
	
	/**
	 * 获取过去的任务历时
	 * @return
	 */
	public String getDurationTime(){
		if (histIns!=null && histIns.getDurationInMillis() != null){
			return TimeUtils.toTimeString(histIns.getDurationInMillis());
		}
		return "";
	}
	
	/**
	 * 是否是一个待办任务
	 * @return
	 */
	public boolean isTodoTask(){
		return "todo".equals(status) || "claim".equals(status);
	}
	
	/**
	 * 是否是已完成任务
	 * @return
	 */
	public boolean isFinishTask(){
		return "finish".equals(status) || StringUtils.isBlank(taskId);
	}

	@Override
	public void preInsert() {
		
	}

	@Override
	public void preUpdate() {
		
	}

}


