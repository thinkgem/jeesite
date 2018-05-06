/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.act.utils;

import java.util.List;

import org.activiti.engine.RepositoryService;
import org.activiti.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.activiti.engine.impl.pvm.process.ActivityImpl;
import org.activiti.engine.repository.ProcessDefinition;
import org.apache.commons.lang3.ObjectUtils;

import com.thinkgem.jeesite.common.utils.CacheUtils;
import com.thinkgem.jeesite.common.utils.SpringContextHolder;

/**
 * 流程定义缓存
 * @author ThinkGem
 * @version 2013-12-05
 */
public class ProcessDefCache {

	private static final String ACT_CACHE = "actCache";
	private static final String ACT_CACHE_PD_ID_ = "pd_id_";
	
	/**
	 * 获得流程定义对象
	 * @param procDefId
	 * @return
	 */
	public static ProcessDefinition get(String procDefId) {
		ProcessDefinition pd = (ProcessDefinition)CacheUtils.get(ACT_CACHE, ACT_CACHE_PD_ID_ + procDefId);
		if (pd == null) {
			RepositoryService repositoryService = SpringContextHolder.getBean(RepositoryService.class);
//			pd = (ProcessDefinitionEntity) ((RepositoryServiceImpl) repositoryService).getDeployedProcessDefinition(pd);
			pd = repositoryService.createProcessDefinitionQuery().processDefinitionId(procDefId).singleResult();
			if (pd != null) {
				CacheUtils.put(ACT_CACHE, ACT_CACHE_PD_ID_ + procDefId, pd);
			}
		}
		return pd;
	}

	/**
	 * 获得流程定义的所有活动节点
	 * @param procDefId
	 * @return
	 */
	public static List<ActivityImpl> getActivitys(String procDefId) {
		ProcessDefinition pd = get(procDefId);
		if (pd != null) {
			return ((ProcessDefinitionEntity) pd).getActivities();
		}
		return null;
	}
	
	/**
	 * 获得流程定义活动节点
	 * @param procDefId
	 * @param activityId
	 * @return
	 */
	public static ActivityImpl getActivity(String procDefId, String activityId) {
		ProcessDefinition pd = get(procDefId);
		if (pd != null) {
			List<ActivityImpl> list = getActivitys(procDefId);
			if (list != null){
				for (ActivityImpl activityImpl : list) {
					if (activityId.equals(activityImpl.getId())){
						return activityImpl;
					}
				}
			}
		}
		return null;
	}

	/**
	 * 获取流程定义活动节点名称
	 * @param procDefId
	 * @param activityId
	 * @return
	 */
	@SuppressWarnings("deprecation")
	public static String getActivityName(String procDefId, String activityId) {
		ActivityImpl activity = getActivity(procDefId, activityId);
		if (activity != null) {
			return ObjectUtils.toString(activity.getProperty("name"));
		}
		return null;
	}

}
