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
 * 销假后处理器
 * <p>设置销假时间</p>
 * <p>使用Spring代理，可以注入Bean，管理事物</p>
 *
 * @author liuj
 */
@Service
@Transactional
public class LeaveReportProcessor implements TaskListener {

	private static final long serialVersionUID = 1L;

	@Autowired
	LeaveDao leaveDao;
	
	@Autowired
	RuntimeService runtimeService;
	
	/* (non-Javadoc)
	 * @see org.activiti.engine.delegate.TaskListener#notify(org.activiti.engine.delegate.DelegateTask)
	 */
	public void notify(DelegateTask delegateTask) {
		String processInstanceId = delegateTask.getProcessInstanceId();
		ProcessInstance processInstance = runtimeService.createProcessInstanceQuery().processInstanceId(processInstanceId).singleResult();
		Leave leave = leaveDao.findOne(new Long(processInstance.getBusinessKey()));
		
		Object realityStartTime = delegateTask.getVariable("realityStartTime");
		leave.setRealityStartTime((Date) realityStartTime);
		Object realityEndTime = delegateTask.getVariable("realityEndTime");
		leave.setRealityEndTime((Date) realityEndTime);
		
		leaveDao.save(leave);
	}

}
