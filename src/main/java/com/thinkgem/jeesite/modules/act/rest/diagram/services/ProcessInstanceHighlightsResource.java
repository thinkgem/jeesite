/* Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.thinkgem.jeesite.modules.act.rest.diagram.services;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.activiti.engine.HistoryService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.history.HistoricActivityInstance;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.activiti.engine.impl.pvm.PvmTransition;
import org.activiti.engine.impl.pvm.process.ActivityImpl;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.apache.shiro.authz.annotation.RequiresUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.thinkgem.jeesite.common.mapper.JsonMapper;

@RestController
public class ProcessInstanceHighlightsResource {

	@Autowired
	private RuntimeService runtimeService;

	@Autowired
	private RepositoryService repositoryService;

	@Autowired
	private HistoryService historyService;

	protected ObjectMapper objectMapper = new ObjectMapper();

	@RequiresUser
	@RequestMapping(value = "/act/service/process-instance/{processInstanceId}/highlights", method = RequestMethod.GET, produces = "application/json")
	public ObjectNode getHighlighted(@PathVariable String processInstanceId) {

		ObjectNode responseJSON = objectMapper.createObjectNode();

		responseJSON.put("processInstanceId", processInstanceId);

		ArrayNode activitiesArray = objectMapper.createArrayNode();
		ArrayNode flowsArray = objectMapper.createArrayNode();

		try {
			ProcessInstance processInstance = runtimeService.createProcessInstanceQuery().processInstanceId(processInstanceId).singleResult();
			String processDefinitionId = null;
			List<String> highLightedActivities = Collections.<String> emptyList();
			List<HistoricActivityInstance> historicActivityInstances = null;
			if (processInstance == null) {
				HistoricProcessInstance historicProcessInstance = historyService.createHistoricProcessInstanceQuery()
						.processInstanceId(processInstanceId).singleResult();
				processDefinitionId = historicProcessInstance.getProcessDefinitionId();
				historicActivityInstances = historyService.createHistoricActivityInstanceQuery().processInstanceId(processInstanceId).list();

				System.out.println(JsonMapper.toJsonString(historicActivityInstances));

			} else {
				processDefinitionId = processInstance.getProcessDefinitionId();
				highLightedActivities = runtimeService.getActiveActivityIds(processInstanceId);
			}

			ProcessDefinition processDefinition = repositoryService.getProcessDefinition(processDefinitionId);
			ProcessDefinitionEntity processDefinitionEntity = (ProcessDefinitionEntity) processDefinition;

			responseJSON.put("processDefinitionId", processDefinitionId);

			List<String> highLightedFlows = getHighLightedFlows(processDefinitionEntity, historicActivityInstances);//getHighLightedFlows(processDefinitionEntity, processInstanceId);

			for (String activityId : highLightedActivities) {
				activitiesArray.add(activityId);
			}

			for (String flow : highLightedFlows) {
				flowsArray.add(flow);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		responseJSON.put("activities", activitiesArray);
		responseJSON.put("flows", flowsArray);

		return responseJSON;
	}

	private List<String> getHighLightedFlows(ProcessDefinitionEntity processDefinitionEntity, List<HistoricActivityInstance> historicActivityInstances) {

		List<String> highFlows = new ArrayList<String>();// 用 以保存高亮的线flowId
		for (int i = 0; i < historicActivityInstances.size() - 1; i++) {// 对历史流程节点进行遍历
			ActivityImpl activityImpl = processDefinitionEntity.findActivity(historicActivityInstances.get(i).getActivityId());// 得到节点定义的详细信息

			List<ActivityImpl> sameStartTimeNodes = new ArrayList<ActivityImpl>(); // 用以保存后需开始时间相同的节点
			ActivityImpl sameActivityImpl1 = processDefinitionEntity.findActivity(historicActivityInstances.get(i + 1).getActivityId()); // 将后面第一个节点放在时间相同节点的集合里
			sameStartTimeNodes.add(sameActivityImpl1);
			for (int j = i + 1; j < historicActivityInstances.size() - 1; j++) {
				HistoricActivityInstance activityImpl1 = historicActivityInstances.get(j); // 后续第一个节点
				HistoricActivityInstance activityImpl2 = historicActivityInstances.get(j + 1); // 后续第二个节点
				if (activityImpl1.getStartTime().equals(activityImpl2.getStartTime())) { // 如果第一个节点和第二个节点开始时间相同保存
					ActivityImpl sameActivityImpl2 = processDefinitionEntity.findActivity(activityImpl2.getActivityId());
					sameStartTimeNodes.add(sameActivityImpl2);
				} else {
					break;
				}
			}
			List<PvmTransition> pvmTransitions = activityImpl.getOutgoingTransitions();// 取出节点的所有出去的线
			for (PvmTransition pvmTransition : pvmTransitions) { // 对所有的线进行遍历
				ActivityImpl pvmActivityImpl = (ActivityImpl) pvmTransition.getDestination();// 如果取出的线的目标节点存在时间相同的节点里，保存该线的id，进行高亮显示
				if (sameStartTimeNodes.contains(pvmActivityImpl)) {
					highFlows.add(pvmTransition.getId());
				}
			}

		}
		return highFlows;
	}

	/**
	 * getHighLightedFlows
	 * 
	 * @param processDefinition
	 * @param processInstanceId
	 * @return
	 */
	private List<String> getHighLightedFlows(ProcessDefinitionEntity processDefinition, String processInstanceId) {

		List<String> highLightedFlows = new ArrayList<String>();

		List<HistoricActivityInstance> historicActivityInstances = historyService.createHistoricActivityInstanceQuery()
				.processInstanceId(processInstanceId)
				//order by startime asc is not correct. use default order is correct.
				//.orderByHistoricActivityInstanceStartTime().asc()/*.orderByActivityId().asc()*/
				.list();

		LinkedList<HistoricActivityInstance> hisActInstList = new LinkedList<HistoricActivityInstance>();
		hisActInstList.addAll(historicActivityInstances);

		getHighlightedFlows(processDefinition.getActivities(), hisActInstList, highLightedFlows);

		return highLightedFlows;
	}

	/**
	 * getHighlightedFlows
	 * 
	 * code logic: 1. Loop all activities by id asc order; 2. Check each activity's outgoing transitions and eventBoundery outgoing transitions, if
	 * outgoing transitions's destination.id is in other executed activityIds, add this transition to highLightedFlows List; 3. But if activity is not
	 * a parallelGateway or inclusiveGateway, only choose the earliest flow.
	 * 
	 * @param activityList
	 * @param hisActInstList
	 * @param highLightedFlows
	 */
	private void getHighlightedFlows(List<ActivityImpl> activityList, LinkedList<HistoricActivityInstance> hisActInstList,
			List<String> highLightedFlows) {

		//check out startEvents in activityList
		List<ActivityImpl> startEventActList = new ArrayList<ActivityImpl>();
		Map<String, ActivityImpl> activityMap = new HashMap<String, ActivityImpl>(activityList.size());
		for (ActivityImpl activity : activityList) {

			activityMap.put(activity.getId(), activity);

			String actType = (String) activity.getProperty("type");
			if (actType != null && actType.toLowerCase().indexOf("startevent") >= 0) {
				startEventActList.add(activity);
			}
		}

		//These codes is used to avoid a bug: 
		//ACT-1728 If the process instance was started by a callActivity, it will be not have the startEvent activity in ACT_HI_ACTINST table 
		//Code logic:
		//Check the first activity if it is a startEvent, if not check out the startEvent's highlight outgoing flow.
		HistoricActivityInstance firstHistActInst = hisActInstList.getFirst();
		String firstActType = (String) firstHistActInst.getActivityType();
		if (firstActType != null && firstActType.toLowerCase().indexOf("startevent") < 0) {
			PvmTransition startTrans = getStartTransaction(startEventActList, firstHistActInst);
			if (startTrans != null) {
				highLightedFlows.add(startTrans.getId());
			}
		}

		while (!hisActInstList.isEmpty()) {
			HistoricActivityInstance histActInst = hisActInstList.removeFirst();
			ActivityImpl activity = activityMap.get(histActInst.getActivityId());
			if (activity != null) {
				boolean isParallel = false;
				String type = histActInst.getActivityType();
				if ("parallelGateway".equals(type) || "inclusiveGateway".equals(type)) {
					isParallel = true;
				} else if ("subProcess".equals(histActInst.getActivityType())) {
					getHighlightedFlows(activity.getActivities(), hisActInstList, highLightedFlows);
				}

				List<PvmTransition> allOutgoingTrans = new ArrayList<PvmTransition>();
				allOutgoingTrans.addAll(activity.getOutgoingTransitions());
				allOutgoingTrans.addAll(getBoundaryEventOutgoingTransitions(activity));
				List<String> activityHighLightedFlowIds = getHighlightedFlows(allOutgoingTrans, hisActInstList, isParallel);
				highLightedFlows.addAll(activityHighLightedFlowIds);
			}
		}
	}

	/**
	 * Check out the outgoing transition connected to firstActInst from startEventActList
	 * 
	 * @param startEventActList
	 * @param firstActInst
	 * @return
	 */
	private PvmTransition getStartTransaction(List<ActivityImpl> startEventActList, HistoricActivityInstance firstActInst) {
		for (ActivityImpl startEventAct : startEventActList) {
			for (PvmTransition trans : startEventAct.getOutgoingTransitions()) {
				if (trans.getDestination().getId().equals(firstActInst.getActivityId())) {
					return trans;
				}
			}
		}
		return null;
	}

	/**
	 * getBoundaryEventOutgoingTransitions
	 * 
	 * @param activity
	 * @return
	 */
	private List<PvmTransition> getBoundaryEventOutgoingTransitions(ActivityImpl activity) {
		List<PvmTransition> boundaryTrans = new ArrayList<PvmTransition>();
		for (ActivityImpl subActivity : activity.getActivities()) {
			String type = (String) subActivity.getProperty("type");
			if (type != null && type.toLowerCase().indexOf("boundary") >= 0) {
				boundaryTrans.addAll(subActivity.getOutgoingTransitions());
			}
		}
		return boundaryTrans;
	}

	/**
	 * find out single activity's highlighted flowIds
	 * 
	 * @param activity
	 * @param hisActInstList
	 * @param isExclusive if true only return one flowId(Such as exclusiveGateway, BoundaryEvent On Task)
	 * @return
	 */
	private List<String> getHighlightedFlows(List<PvmTransition> pvmTransitionList, LinkedList<HistoricActivityInstance> hisActInstList,
			boolean isParallel) {

		List<String> highLightedFlowIds = new ArrayList<String>();

		PvmTransition earliestTrans = null;
		HistoricActivityInstance earliestHisActInst = null;

		for (PvmTransition pvmTransition : pvmTransitionList) {

			String destActId = pvmTransition.getDestination().getId();
			HistoricActivityInstance destHisActInst = findHisActInst(hisActInstList, destActId);
			if (destHisActInst != null) {
				if (isParallel) {
					highLightedFlowIds.add(pvmTransition.getId());
				} else if (earliestHisActInst == null || (earliestHisActInst.getId().compareTo(destHisActInst.getId()) > 0)) {
					earliestTrans = pvmTransition;
					earliestHisActInst = destHisActInst;
				}
			}
		}

		if ((!isParallel) && earliestTrans != null) {
			highLightedFlowIds.add(earliestTrans.getId());
		}

		return highLightedFlowIds;
	}

	private HistoricActivityInstance findHisActInst(LinkedList<HistoricActivityInstance> hisActInstList, String actId) {
		for (HistoricActivityInstance hisActInst : hisActInstList) {
			if (hisActInst.getActivityId().equals(actId)) {
				return hisActInst;
			}
		}
		return null;
	}
}
