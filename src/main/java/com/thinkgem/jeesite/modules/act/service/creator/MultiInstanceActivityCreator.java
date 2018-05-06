package com.thinkgem.jeesite.modules.act.service.creator;

import java.util.List;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.impl.bpmn.behavior.MultiInstanceActivityBehavior;
import org.activiti.engine.impl.bpmn.behavior.ParallelMultiInstanceBehavior;
import org.activiti.engine.impl.bpmn.behavior.SequentialMultiInstanceBehavior;
import org.activiti.engine.impl.bpmn.behavior.TaskActivityBehavior;
import org.activiti.engine.impl.el.FixedValue;
import org.activiti.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.activiti.engine.impl.pvm.PvmTransition;
import org.activiti.engine.impl.pvm.process.ActivityImpl;

import com.thinkgem.jeesite.modules.act.utils.ProcessDefUtils;

public class MultiInstanceActivityCreator extends RuntimeActivityCreatorSupport implements RuntimeActivityCreator {
	
	public ActivityImpl[] createActivities(ProcessEngine processEngine, ProcessDefinitionEntity processDefinition,
			RuntimeActivityDefinitionEntity info) {
		info.setFactoryName(MultiInstanceActivityCreator.class.getName());
		RuntimeActivityDefinitionEntityIntepreter radei = new RuntimeActivityDefinitionEntityIntepreter(info);

		if (radei.getCloneActivityId() == null) {
			String cloneActivityId = createUniqueActivityId(info.getProcessInstanceId(), radei.getPrototypeActivityId());
			radei.setCloneActivityId(cloneActivityId);
		}

		return new ActivityImpl[] { createMultiInstanceActivity(processEngine, processDefinition, info.getProcessInstanceId(),
				radei.getPrototypeActivityId(), radei.getCloneActivityId(), radei.getSequential(), radei.getAssignees()) };
	}

	private ActivityImpl createMultiInstanceActivity(ProcessEngine processEngine, ProcessDefinitionEntity processDefinition,
			String processInstanceId, String prototypeActivityId, String cloneActivityId, boolean isSequential, List<String> assignees) {
		ActivityImpl prototypeActivity = ProcessDefUtils.getActivity(processEngine, processDefinition.getId(), prototypeActivityId);

		//拷贝listener，executionListeners会激活历史记录的保存
		ActivityImpl clone = cloneActivity(processDefinition, prototypeActivity, cloneActivityId, "executionListeners", "properties");
		//拷贝所有后向链接
		for (PvmTransition trans : prototypeActivity.getOutgoingTransitions()) {
			clone.createOutgoingTransition(trans.getId()).setDestination((ActivityImpl) trans.getDestination());
		}

		MultiInstanceActivityBehavior multiInstanceBehavior = isSequential ? new SequentialMultiInstanceBehavior(clone,
				(TaskActivityBehavior) prototypeActivity.getActivityBehavior()) : new ParallelMultiInstanceBehavior(clone,
				(TaskActivityBehavior) prototypeActivity.getActivityBehavior());

		clone.setActivityBehavior(multiInstanceBehavior);

		clone.setScope(true);
		clone.setProperty("multiInstance", isSequential ? "sequential" : "parallel");

		//设置多实例节点属性
		multiInstanceBehavior.setLoopCardinalityExpression(new FixedValue(assignees.size()));
		multiInstanceBehavior.setCollectionExpression(new FixedValue(assignees));
		return clone;
	}
}
