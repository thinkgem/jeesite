package com.thinkgem.jeesite.common.workflow;

import java.util.List;
import java.util.Map;

import org.activiti.engine.HistoryService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.apache.commons.lang3.ObjectUtils;

import com.google.common.collect.Maps;
import com.thinkgem.jeesite.common.utils.SpringContextHolder;
import com.thinkgem.jeesite.common.utils.StringUtils;
import com.thinkgem.jeesite.modules.sys.utils.UserUtils;
/**
 * 
 *
 * @author HenryYan
 */
public class WorkflowUtils {

	/**
	 * 转换流程节点类型为中文说明
	 * @param type	英文名称
	 * @return	翻译后的中文名称
	 */
	public static String parseToZhType(String type) {
		Map<String, String> types = Maps.newHashMap();
		types.put("userTask", "用户任务");
		types.put("serviceTask", "系统任务");
		types.put("startEvent", "开始节点");
		types.put("endEvent", "结束节点");
		types.put("exclusiveGateway", "条件判断节点(系统自动根据条件处理)");
		types.put("inclusiveGateway", "并行处理任务");
		types.put("callActivity", "子流程");
		return types.get(type) == null ? type: types.get(type);
	}
	
	public static WorkflowEntity getWorkflowEntity(String processInstanceId) {
		RuntimeService runtimeService= SpringContextHolder.getBean(RuntimeService.class);
		TaskService taskService= SpringContextHolder.getBean(TaskService.class);
		HistoryService historyService= SpringContextHolder.getBean(HistoryService.class);
		RepositoryService repositoryService= SpringContextHolder.getBean(RepositoryService.class);
		
		WorkflowEntity workflowEntity = new WorkflowEntity();
		List<Task> tasks= taskService.createTaskQuery().processInstanceId(processInstanceId).active().list();
		workflowEntity.setTasks(tasks);
		HistoricProcessInstance historicProcessInstance = historyService.createHistoricProcessInstanceQuery().processInstanceId(processInstanceId).singleResult();
		if(historicProcessInstance!=null) {
			workflowEntity.setHistoricProcessInstance(historicProcessInstance);
			workflowEntity.setProcessDefinition(repositoryService.createProcessDefinitionQuery().processDefinitionId(historicProcessInstance.getProcessDefinitionId()).singleResult());
		} else {
			ProcessInstance processInstance = runtimeService.createProcessInstanceQuery().processInstanceId(processInstanceId).active().singleResult();
			workflowEntity.setProcessInstance(processInstance);
			workflowEntity.setProcessDefinition(repositoryService.createProcessDefinitionQuery().processDefinitionId(processInstance.getProcessDefinitionId()).singleResult());
		}
		workflowEntity.setHistoricTaskInstances(historyService.createHistoricTaskInstanceQuery().processInstanceId(processInstanceId).orderByHistoricTaskInstanceEndTime().asc().list());
		workflowEntity.setHistoricVariableInstances(historyService.createHistoricVariableInstanceQuery().processInstanceId(processInstanceId).list());
		workflowEntity.setComments(taskService.getProcessInstanceComments(processInstanceId));
		return workflowEntity;
	}
	
	public static void claim(String processInstanceId) {
		TaskService taskService= SpringContextHolder.getBean(TaskService.class);
		//如果没有签收，签收
		Task task = taskService.createTaskQuery().processInstanceId(processInstanceId).singleResult();
		if(task != null && StringUtils.isEmpty(task.getAssignee())){
			taskService.claim(task.getId(), ObjectUtils.toString(UserUtils.getUser().getId()));
		}
	}
	
	
}
