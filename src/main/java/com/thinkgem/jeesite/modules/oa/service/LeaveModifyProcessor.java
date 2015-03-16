package com.thinkgem.jeesite.modules.oa.service;

import java.util.Date;

import org.activiti.engine.RuntimeService;
import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.TaskListener;
import org.activiti.engine.runtime.ProcessInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.thinkgem.jeesite.modules.oa.dao.LeaveDao;
import com.thinkgem.jeesite.modules.oa.entity.Leave;

/**
 * 调整请假内容处理器
 * @author liuj
 */
@Service
@Transactional
public class LeaveModifyProcessor implements TaskListener {
	
	private static final long serialVersionUID = 1L;

	@Autowired
	private LeaveDao leaveDao;
	@Autowired
	private RuntimeService runtimeService;

	public void notify(DelegateTask delegateTask) {
		String processInstanceId = delegateTask.getProcessInstanceId();
		ProcessInstance processInstance = runtimeService.createProcessInstanceQuery().processInstanceId(processInstanceId).singleResult();
		Leave leave = new Leave(processInstance.getBusinessKey());
		leave.setLeaveType((String) delegateTask.getVariable("leaveType"));
		leave.setStartTime((Date) delegateTask.getVariable("startTime"));
		leave.setEndTime((Date) delegateTask.getVariable("endTime"));
		leave.setReason((String) delegateTask.getVariable("reason"));
		leave.preUpdate();
		leaveDao.update(leave);
	}

}
