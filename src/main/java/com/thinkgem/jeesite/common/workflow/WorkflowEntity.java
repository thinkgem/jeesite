package com.thinkgem.jeesite.common.workflow;

import java.util.List;
import java.util.Map;

import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.history.HistoricVariableInstance;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Comment;
import org.activiti.engine.task.Task;
import org.apache.commons.lang3.StringUtils;

import com.google.common.collect.Maps;
import com.thinkgem.jeesite.common.utils.Collections3;

public class WorkflowEntity {
	
	private List<Task> tasks;
	
	private  List<HistoricVariableInstance> historicVariableInstances;
	
	private List<HistoricTaskInstance> historicTaskInstances;
	
	private ProcessInstance processInstance;
	
	private HistoricProcessInstance historicProcessInstance;
	
	private ProcessDefinition processDefinition;
	
	private List<Comment> comments;

	public List<Task> getTasks() {
		return tasks;
	}
	public void setTasks(List<Task> tasks) {
		this.tasks = tasks;
	}
	
	public Task getTask(){
		if(Collections3.isEmpty(tasks)) {
			return null;
		} else {
			return tasks.get(0);
		}
	}

	public Map<String, Task> getTaskMap(){
		Map<String, Task> map = Maps.newHashMap();
		if(tasks!=null && tasks.size()>0) {
			for(Task task:tasks) {
				map.put(task.getName(),task);
			}
		}
		return map;
	}
	
	public List<HistoricVariableInstance> getHistoricVariableInstances() {
		return historicVariableInstances;
	}
	public void setHistoricVariableInstances( List<HistoricVariableInstance> historicVariableInstances) {
		this.historicVariableInstances = historicVariableInstances;
	}
	public List<HistoricTaskInstance> getHistoricTaskInstances() {
		return historicTaskInstances;
	}
	public void setHistoricTaskInstances(List<HistoricTaskInstance> historicTaskInstances) {
		this.historicTaskInstances = historicTaskInstances;
	}
	public ProcessInstance getProcessInstance() {
		return processInstance;
	}
	public void setProcessInstance(ProcessInstance processInstance) {
		this.processInstance = processInstance;
	}
	public HistoricProcessInstance getHistoricProcessInstance() {
		return historicProcessInstance;
	}
	public void setHistoricProcessInstance(HistoricProcessInstance historicProcessInstance) {
		this.historicProcessInstance = historicProcessInstance;
	}
	public ProcessDefinition getProcessDefinition() {
		return processDefinition;
	}
	public void setProcessDefinition(ProcessDefinition processDefinition) {
		this.processDefinition = processDefinition;
	}
	public List<Comment> getComments() {
		return comments;
	}
	public void setComments(List<Comment> comments) {
		this.comments = comments;
	}
	public Map<String, Object> getVariableMap(){
		Map<String, Object> map = Maps.newHashMap();
		if(historicVariableInstances!=null && historicVariableInstances.size()>0) {
			for(HistoricVariableInstance historicVariableInstance:historicVariableInstances) {
				map.put(historicVariableInstance.getVariableName(), historicVariableInstance.getValue());
			}
		}
		return map;
	}
	public Map<String, Object> getCommentMap(){
		Map<String, Object> map = Maps.newHashMap();
		if(comments!=null && comments.size()>0) {
			for(Comment comment:comments) {
				map.put(comment.getTaskId(),comment.getFullMessage());
			}
		}
		return map;
	}
	public boolean isEditable() {
		return StringUtils.isBlank(getHistoricTaskInstances().get(getHistoricTaskInstances().size()-1).getAssignee());
	}
	
	
}
