package com.thinkgem.jeesite.modules.act.service.cmd;

import java.util.Map;

import org.activiti.engine.impl.context.Context;
import org.activiti.engine.impl.interceptor.Command;
import org.activiti.engine.impl.interceptor.CommandContext;
import org.activiti.engine.impl.persistence.entity.ExecutionEntity;
import org.activiti.engine.impl.persistence.entity.TaskEntity;
import org.activiti.engine.impl.pvm.process.ActivityImpl;
import org.activiti.engine.impl.pvm.runtime.AtomicOperation;

public class CreateAndTakeTransitionCmd implements Command<java.lang.Void> {
	
	private TaskEntity currentTaskEntity;
	private ActivityImpl activity;
	protected Map<String, Object> variables;

	public CreateAndTakeTransitionCmd(TaskEntity currentTaskEntity, ActivityImpl activity, Map<String, Object> variables) {
		this.currentTaskEntity = currentTaskEntity;
		this.activity = activity;
		this.variables = variables;
	}

	@Override
	public Void execute(CommandContext commandContext) {
		if (currentTaskEntity != null) {

			ExecutionEntity execution = commandContext.getExecutionEntityManager().findExecutionById(currentTaskEntity.getExecutionId());
			execution.setActivity(activity);
			execution.performOperation(AtomicOperation.TRANSITION_CREATE_SCOPE);

			if (variables != null) {
				if (currentTaskEntity.getExecutionId() != null) {
					currentTaskEntity.setExecutionVariables(variables);
				} else {
					currentTaskEntity.setVariables(variables);
				}
			}
			//删除当前的任务，不能删除当前正在执行的任务，所以要先清除掉关联
			Context.getCommandContext().getTaskEntityManager().deleteTask(currentTaskEntity, TaskEntity.DELETE_REASON_DELETED, false);
		}
		return null;
	}
}