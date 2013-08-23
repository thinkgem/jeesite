/**
 * There are <a href="https://github.com/thinkgem/jeesite">JeeSite</a> code generation
 */
package com.thinkgem.jeesite.modules.oa.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.activiti.engine.HistoryService;
import org.activiti.engine.IdentityService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Lists;
import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.service.BaseService;
import com.thinkgem.jeesite.common.utils.Collections3;
import com.thinkgem.jeesite.modules.oa.dao.LeaveDao;
import com.thinkgem.jeesite.modules.oa.entity.Leave;
import com.thinkgem.jeesite.modules.oa.utils.workflow.ProcessDefinitionKey;
import com.thinkgem.jeesite.modules.sys.utils.UserUtils;

/**
 * 请假Service
 * @author liuj
 * @version 2013-04-05
 */
@Service
@Transactional(readOnly = true)
public class LeaveService extends BaseService {

	@Autowired
	private LeaveDao leaveDao;
	@Autowired
	private RuntimeService runtimeService;
	@Autowired
	protected TaskService taskService;
	@Autowired
	protected HistoryService historyService;
	@Autowired
	protected RepositoryService repositoryService;
	@Autowired
	private IdentityService identityService;

	/**
	 *获取流程详细及工作流参数
	 * @param id
	 */
	@SuppressWarnings("unchecked")
	public Leave get(Long id) {
		Leave leave= leaveDao.get(id);
		Map<String,Object> variables=null;
		HistoricProcessInstance historicProcessInstance = historyService.createHistoricProcessInstanceQuery().processInstanceId(leave.getProcessInstanceId()).singleResult();
		if(historicProcessInstance!=null) {
			variables = Collections3.extractToMap(historyService.createHistoricVariableInstanceQuery().processInstanceId(historicProcessInstance.getId()).list(), "variableName", "value");
		} else {
			variables = runtimeService.getVariables(runtimeService.createProcessInstanceQuery().processInstanceId(leave.getProcessInstanceId()).active().singleResult().getId());
		}
		leave.setVariables(variables);
		return leave;
	}
	
	/**
	 * 启动流程
	 * @param entity
	 */
	public ProcessInstance save(Leave entity, Map<String, Object> variables) {
		leaveDao.save(entity);
		logger.debug("save entity: {}", entity);
		String businessKey = entity.getId().toString();
		
		// 用来设置启动流程的人员ID，引擎会自动把用户ID保存到activiti:initiator中
		identityService.setAuthenticatedUserId(ObjectUtils.toString(entity.getCreateBy().getId()));
		
		ProcessInstance processInstance = runtimeService.startProcessInstanceByKey(ProcessDefinitionKey.Leave.getKey(), businessKey, variables);
		String processInstanceId = processInstance.getId();
		entity.setProcessInstanceId(processInstanceId);
		leaveDao.updateProcessInstanceId(entity.getId(), entity.getProcessInstanceId());
		logger.debug("start process of {key={}, bkey={}, pid={}, variables={}}", new Object[] { ProcessDefinitionKey.Leave.getKey(), businessKey,processInstanceId, variables });
		return processInstance;
	}

	/**
	 * 查询待办任务
	 * 
	 * @param userId 用户ID
	 * @return
	 */
	@Transactional(readOnly = true)
	public List<Leave> findTodoTasks(String userId) {
		List<Leave> results = new ArrayList<Leave>();
		List<Task> tasks = new ArrayList<Task>();
		// 根据当前人的ID查询
		List<Task> todoList = taskService.createTaskQuery().processDefinitionKey(ProcessDefinitionKey.Leave.getKey()).taskAssignee(userId).active().orderByTaskPriority().desc().orderByTaskCreateTime().desc().list();
		// 根据当前人未签收的任务
		List<Task> unsignedTasks = taskService.createTaskQuery().processDefinitionKey(ProcessDefinitionKey.Leave.getKey()).taskCandidateUser(userId).active().orderByTaskPriority().desc().orderByTaskCreateTime().desc().list();
		// 合并
		tasks.addAll(todoList);
		tasks.addAll(unsignedTasks);
		// 根据流程的业务ID查询实体并关联
		for (Task task : tasks) {
			String processInstanceId = task.getProcessInstanceId();
			ProcessInstance processInstance = runtimeService.createProcessInstanceQuery().processInstanceId(processInstanceId).active()
					.singleResult();
			String businessKey = processInstance.getBusinessKey();
			Leave leave = leaveDao.get(new Long(businessKey));
			leave.setTask(task);
			leave.setProcessInstance(processInstance);
			leave.setProcessDefinition(repositoryService.createProcessDefinitionQuery().processDefinitionId((processInstance.getProcessDefinitionId())).singleResult());
			results.add(leave);
		}
		return results;
	}

	public Page<Leave> find(Page<Leave> page, Leave leave) {
		DetachedCriteria dc = leaveDao.createDetachedCriteria();
		if (StringUtils.isNotBlank(leave.getIds())){
			String ids =leave.getIds().trim().replace("　", ",").replace(" ",",").replace("，", ",");
			List<Long> idList =Lists.newArrayList();
			for(String id:ids.split(",")) {
				if(id.matches("\\d*")) {
					idList.add(Long.valueOf(id));
				}
			}
			if(idList.size()>0) {
				dc.add(Restrictions.in("id",idList));
			}
		}
		if(leave.getCreateDateStart()!=null) {
			dc.add(Restrictions.ge("createDate", leave.getCreateDateStart()));
		} 
		if(leave.getCreateDateEnd()!=null) {
			dc.add(Restrictions.ge("createDate", leave.getCreateDateEnd()));
		} 
		if(StringUtils.isNotBlank(leave.getLeaveType())) {
			dc.add(Restrictions.like("leaveType", leave.getLeaveType()));
		}
		dc.createAlias("createBy", "createBy");
		dc.createAlias("createBy.office", "office");
		dc.add(dataScopeFilter(UserUtils.getUser(), "office", "createBy"));
		dc.addOrder(Order.desc("id"));
		Page<Leave> result= leaveDao.find(page, dc);
		for(Leave item:result.getList()) {
			String processInstanceId=item.getProcessInstanceId();
			Task task = taskService.createTaskQuery().processInstanceId(processInstanceId).active().singleResult();
			item.setTask(task);
			HistoricProcessInstance historicProcessInstance = historyService.createHistoricProcessInstanceQuery().processInstanceId(processInstanceId).singleResult();
			if(historicProcessInstance!=null) {
				item.setHistoricProcessInstance(historicProcessInstance);
				item.setProcessDefinition(repositoryService.createProcessDefinitionQuery().processDefinitionId(historicProcessInstance.getProcessDefinitionId()).singleResult());
			} else {
				ProcessInstance processInstance = runtimeService.createProcessInstanceQuery().processInstanceId(processInstanceId).active().singleResult();
				item.setProcessInstance(processInstance);
				item.setProcessDefinition(repositoryService.createProcessDefinitionQuery().processDefinitionId(processInstance.getProcessDefinitionId()).singleResult());
			}
		}
		return result;
	}
}
