package com.thinkgem.jeesite.modules.act.service.creator;

import java.lang.reflect.Field;
import java.util.List;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.impl.bpmn.behavior.UserTaskActivityBehavior;
import org.activiti.engine.impl.el.FixedValue;
import org.activiti.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.activiti.engine.impl.pvm.process.ActivityImpl;
import org.activiti.engine.impl.task.TaskDefinition;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.junit.Assert;
import org.springframework.beans.BeanUtils;

import com.thinkgem.jeesite.modules.act.utils.ProcessDefUtils;

public abstract class RuntimeActivityCreatorSupport {
	private static int SEQUNCE_NUMBER = 0;

	protected ActivityImpl cloneActivity(ProcessDefinitionEntity processDefinition, ActivityImpl prototypeActivity, String newActivityId,
			String... fieldNames) {
		ActivityImpl clone = processDefinition.createActivity(newActivityId);
		copyFields(prototypeActivity, clone, fieldNames);

		return clone;
	}

	protected TaskDefinition cloneTaskDefinition(TaskDefinition taskDefinition) {
		TaskDefinition newTaskDefinition = new TaskDefinition(taskDefinition.getTaskFormHandler());
		BeanUtils.copyProperties(taskDefinition, newTaskDefinition);
		return newTaskDefinition;
	}

	protected ActivityImpl createActivity(ProcessEngine processEngine, ProcessDefinitionEntity processDefinition, ActivityImpl prototypeActivity,
			String cloneActivityId, String assignee) {
		ActivityImpl clone = cloneActivity(processDefinition, prototypeActivity, cloneActivityId, "executionListeners", "properties");

		//设置assignee
		UserTaskActivityBehavior activityBehavior = (UserTaskActivityBehavior) (prototypeActivity.getActivityBehavior());

		TaskDefinition taskDefinition = cloneTaskDefinition(activityBehavior.getTaskDefinition());
		taskDefinition.setKey(cloneActivityId);
		if (assignee != null) {
			taskDefinition.setAssigneeExpression(new FixedValue(assignee));
		}

		UserTaskActivityBehavior cloneActivityBehavior = new UserTaskActivityBehavior(null, taskDefinition);
		clone.setActivityBehavior(cloneActivityBehavior);

		return clone;
	}

	protected ActivityImpl createActivity(ProcessEngine processEngine, ProcessDefinitionEntity processDefinition, String prototypeActivityId,
			String cloneActivityId, String assignee) {
		ActivityImpl prototypeActivity = ProcessDefUtils.getActivity(processEngine, processDefinition.getId(), prototypeActivityId);

		return createActivity(processEngine, processDefinition, prototypeActivity, cloneActivityId, assignee);
	}

	protected void createActivityChain(List<ActivityImpl> activities, ActivityImpl nextActivity) {
		for (int i = 0; i < activities.size(); i++) {
			//设置各活动的下线
			activities.get(i).getOutgoingTransitions().clear();
			activities.get(i).createOutgoingTransition("flow" + (i + 1))
					.setDestination(i == activities.size() - 1 ? nextActivity : activities.get(i + 1));
		}
	}

	protected String createUniqueActivityId(String processInstanceId, String prototypeActivityId) {
		return processInstanceId + ":" + prototypeActivityId + ":" + System.currentTimeMillis() + "-" + (SEQUNCE_NUMBER++);
	}

	private static void copyFields(Object source, Object target, String... fieldNames) {
		Assert.assertNotNull(source);
		Assert.assertNotNull(target);
		Assert.assertSame(source.getClass(), target.getClass());

		for (String fieldName : fieldNames) {
			try {
				Field field = FieldUtils.getField(source.getClass(), fieldName, true);
				field.setAccessible(true);
				field.set(target, field.get(source));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
