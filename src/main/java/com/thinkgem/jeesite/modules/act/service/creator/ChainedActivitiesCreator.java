package com.thinkgem.jeesite.modules.act.service.creator;

import java.util.ArrayList;
import java.util.List;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.activiti.engine.impl.pvm.process.ActivityImpl;
import org.springframework.util.CollectionUtils;

import com.thinkgem.jeesite.modules.act.utils.ProcessDefUtils;

public class ChainedActivitiesCreator extends RuntimeActivityCreatorSupport implements RuntimeActivityCreator {
	
	@SuppressWarnings("unchecked")
	public ActivityImpl[] createActivities(ProcessEngine processEngine, ProcessDefinitionEntity processDefinition,
			RuntimeActivityDefinitionEntity info) {
		info.setFactoryName(ChainedActivitiesCreator.class.getName());
		RuntimeActivityDefinitionEntityIntepreter radei = new RuntimeActivityDefinitionEntityIntepreter(info);

		if (radei.getCloneActivityIds() == null) {
			radei.setCloneActivityIds(CollectionUtils.arrayToList(new String[radei.getAssignees().size()]));
		}

		return createActivities(processEngine, processDefinition, info.getProcessInstanceId(), radei.getPrototypeActivityId(),
				radei.getNextActivityId(), radei.getAssignees(), radei.getCloneActivityIds());
	}

	private ActivityImpl[] createActivities(ProcessEngine processEngine, ProcessDefinitionEntity processDefinition, String processInstanceId,
			String prototypeActivityId, String nextActivityId, List<String> assignees, List<String> activityIds) {
		ActivityImpl prototypeActivity = ProcessDefUtils.getActivity(processEngine, processDefinition.getId(), prototypeActivityId);

		List<ActivityImpl> activities = new ArrayList<ActivityImpl>();
		for (int i = 0; i < assignees.size(); i++) {
			if (activityIds.get(i) == null) {
				String activityId = createUniqueActivityId(processInstanceId, prototypeActivityId);
				activityIds.set(i, activityId);
			}

			ActivityImpl clone = createActivity(processEngine, processDefinition, prototypeActivity, activityIds.get(i), assignees.get(i));
			activities.add(clone);
		}

		ActivityImpl nextActivity = ProcessDefUtils.getActivity(processEngine, processDefinition.getId(), nextActivityId);
		createActivityChain(activities, nextActivity);

		return activities.toArray(new ActivityImpl[0]);
	}
}
