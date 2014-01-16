/**
 * There are <a href="https://github.com/thinkgem/jeesite">JeeSite</a> code generation
 */
package com.thinkgem.jeesite.modules.oa.service;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.activiti.engine.HistoryService;
import org.activiti.engine.IdentityService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.apache.commons.lang3.ObjectUtils;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.service.BaseService;
import com.thinkgem.jeesite.common.utils.StringUtils;
import com.thinkgem.jeesite.common.workflow.WorkflowUtils;
import com.thinkgem.jeesite.modules.oa.dao.LeaveDao;
import com.thinkgem.jeesite.modules.oa.entity.Leave;
import com.thinkgem.jeesite.modules.sys.entity.User;
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
	
	private String processDefinitionKey = "leave";
	
	public Leave get(String id) {
		return  leaveDao.get(id);
	}

	public Page<Leave> findTodoTasks(Page<Leave> page, Leave leave) {
		//获取所有未未完成任务
		User user = UserUtils.getUser();
		DetachedCriteria dc = leaveDao.createDetachedCriteria();
		if (StringUtils.isNotBlank(leave.getIds())){
			dc.add(Restrictions.in("id", getIdList(leave.getIds())));
		}
		if(leave.getCreateDateStart()!=null) {
			dc.add(Restrictions.ge("createDate", leave.getCreateDateStart()));
		} 
		if(leave.getCreateDateEnd()!=null) {
			dc.add(Restrictions.le("createDate", leave.getCreateDateEnd()));
		} 
		if(StringUtils.isNotBlank(leave.getLeaveType())) {
			dc.add(Restrictions.like("leaveType", leave.getLeaveType()));
		}
		dc.add(Restrictions.ne("processStatus","已完成"));
		dc.add(Restrictions.eq("delFlag", Leave.DEL_FLAG_NORMAL));
		dc.createAlias("createBy", "createBy");
		dc.createAlias("createBy.office", "office");
		dc.add(dataScopeFilter(UserUtils.getUser(), "office", "createBy"));
		dc.addOrder(Order.desc("id"));
		List<Leave> list = leaveDao.find(dc);
		List<Leave> result = Lists.newArrayList();
		//过滤出当前用户的任务
		if(list.size()>0) {
			List<Task> tasks =Lists.newArrayList();
			List<Task> todoList = taskService.createTaskQuery().processDefinitionKey(processDefinitionKey).taskAssignee(ObjectUtils.toString(user.getId())).active().list();
			List<Task> unsignedTasks = taskService.createTaskQuery().processDefinitionKey(processDefinitionKey).taskCandidateUser(ObjectUtils.toString(user.getId())).active().list();
			tasks.addAll(todoList);
			tasks.addAll(unsignedTasks);
			Set<String> processInstanceIds = Sets.newHashSet();
			for (Task task : tasks) {
				processInstanceIds.add(task.getProcessInstanceId());
			}
			for(Leave l:list) {
				if(processInstanceIds.contains(l.getProcessInstanceId())) {
					result.add(l);
				}
			}
		}
		page.setCount(result.size());
		page.setList(result.subList(page.getFirstResult(),page.getLastResult()));
		return page;
	}

	public Page<Leave> find(Page<Leave> page, Leave leave) {
		DetachedCriteria dc = leaveDao.createDetachedCriteria();
		if (StringUtils.isNotBlank(leave.getIds())){
			dc.add(Restrictions.in("id", getIdList(leave.getIds())));
		}
		if(leave.getCreateDateStart()!=null) {
			dc.add(Restrictions.ge("createDate", leave.getCreateDateStart()));
		} 
		if(leave.getCreateDateEnd()!=null) {
			dc.add(Restrictions.le("createDate", leave.getCreateDateEnd()));
		} 
		if(StringUtils.isNotBlank(leave.getLeaveType())) {
			dc.add(Restrictions.like("leaveType", leave.getLeaveType()));
		}
		dc.add(Restrictions.eq("delFlag", Leave.DEL_FLAG_NORMAL));
		dc.createAlias("createBy", "createBy");
		dc.createAlias("createBy.office", "office");
		dc.add(dataScopeFilter(UserUtils.getUser(), "office", "createBy"));
		dc.addOrder(Order.desc("id"));
	    return leaveDao.find(page, dc);
	}

	@Transactional(readOnly = false)
	public void save(Leave leave) {
		leaveDao.save(leave);
		leaveDao.flush();
		String businessKey = leave.getId().toString();
		// 用来设置启动流程的人员ID，引擎会自动把用户ID保存到activiti:initiator中
		identityService.setAuthenticatedUserId(ObjectUtils.toString(leave.getCreateBy().getId()));
		ProcessInstance processInstance = runtimeService.startProcessInstanceByKey(processDefinitionKey, businessKey);
		String processInstanceId = processInstance.getId();
		leave.setProcessInstanceId(processInstanceId);
		leave.setProcessStatus(taskService.createTaskQuery().processInstanceId(processInstanceId).singleResult().getName());
		leaveDao.save(leave);
	}
	

	//部门领导审批
	@Transactional(readOnly = false)
	public void deptLeaderAudit(Leave leave) {
		WorkflowUtils.claim(leave.getProcessInstanceId());
		Task task = taskService.createTaskQuery().processInstanceId(leave.getProcessInstanceId()).singleResult();
		//添加批注
		taskService.addComment(task.getId(), leave.getProcessInstanceId(), leave.getAuditRemarks());
		Map<String, Object> map = Maps.newHashMap();
		map.put("deptLeaderPass", leave.isPass());
		//完成任务
		taskService.complete(task.getId(),map);
		task = taskService.createTaskQuery().processInstanceId(leave.getProcessInstanceId()).singleResult();
		leave.setProcessStatus(task.getName());
		leaveDao.save(leave);
	}
	
	//人事审批
	@Transactional(readOnly = false)
	public void hrAudit(Leave leave) {
		WorkflowUtils.claim(leave.getProcessInstanceId());
		Task task = taskService.createTaskQuery().processInstanceId(leave.getProcessInstanceId()).singleResult();
		//添加批注
		taskService.addComment(task.getId(), leave.getProcessInstanceId(), leave.getAuditRemarks());
		Map<String, Object> map = Maps.newHashMap();
		map.put("hrPass", leave.isPass());
		//完成任务
		taskService.complete(task.getId(),map);
		task = taskService.createTaskQuery().processInstanceId(leave.getProcessInstanceId()).singleResult();
		leave.setProcessStatus(task.getName());
		leaveDao.save(leave);
	}
	
	//调整申请
	@Transactional(readOnly = false)
	public void modifyApply(Leave leave) {
		WorkflowUtils.claim(leave.getProcessInstanceId());
		Task task = taskService.createTaskQuery().processInstanceId(leave.getProcessInstanceId()).singleResult();
		Map<String, Object> map = Maps.newHashMap();
		map.put("reApply", leave.isPass());
		//完成任务
		taskService.complete(task.getId(),map);
		task = taskService.createTaskQuery().processInstanceId(leave.getProcessInstanceId()).singleResult();
		if(task==null) {
			leave.setProcessStatus("已完成");
			leave.setDelFlag(Leave.DEL_FLAG_DELETE);
		} else {
			leave.setProcessStatus(task.getName());
		}
		leaveDao.save(leave);
	}
	
	//销假
	@Transactional(readOnly = false)
	public void reportBack(Leave leave) {
		WorkflowUtils.claim(leave.getProcessInstanceId());
		Task task = taskService.createTaskQuery().processInstanceId(leave.getProcessInstanceId()).singleResult();
		//完成任务
		taskService.complete(task.getId());
		task = taskService.createTaskQuery().processInstanceId(leave.getProcessInstanceId()).singleResult();
		leave.setProcessStatus("已完成");
		leaveDao.save(leave);
	}
	
	
	@Transactional(readOnly = false)
	public void delete(String id) {
		leaveDao.deleteById(id);
	}
}
