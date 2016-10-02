package com.thinkgem.jeesite.modules.act.service.cmd;

import java.io.ByteArrayInputStream;

import org.activiti.bpmn.converter.BpmnXMLConverter;
import org.activiti.bpmn.model.BpmnModel;
import org.activiti.editor.language.json.converter.BpmnJsonConverter;
import org.activiti.engine.ActivitiException;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.impl.cfg.ProcessEngineConfigurationImpl;
import org.activiti.engine.impl.context.Context;
import org.activiti.engine.impl.db.DbSqlSession;
import org.activiti.engine.impl.interceptor.Command;
import org.activiti.engine.impl.interceptor.CommandContext;
import org.activiti.engine.impl.persistence.entity.DeploymentEntity;
import org.activiti.engine.impl.persistence.entity.ResourceEntity;
import org.activiti.engine.impl.persistence.entity.ResourceEntityManager;
import org.activiti.engine.impl.util.IoUtil;
import org.activiti.engine.repository.ProcessDefinition;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * 模型部署或更新到流程定义
 * @author ThinkGem
 * @version 2016年8月2日
 */
public class ModelDeployProcessDefinitionCmd implements Command<java.lang.Void> {
	
	private String modelId;
	private String procDefKey;
	private String procDefName;

	public ModelDeployProcessDefinitionCmd(String modelId, String procDefKey, String procDefName) {
		this.modelId = modelId;
		this.procDefKey = procDefKey;
		this.procDefName = procDefName;
	}

	@Override
	public Void execute(CommandContext commandContext) {
		RepositoryService repositoryService = Context.getProcessEngineConfiguration()
				.getRepositoryService();
		try{
			// 生成部署名称和数据 ThinkGem
			JsonNode editorNode = new ObjectMapper().readTree(repositoryService
						.getModelEditorSource(modelId));
			BpmnModel bpmnModel = new BpmnJsonConverter().convertToBpmnModel(editorNode);
			byte[] bpmnBytes = new BpmnXMLConverter().convertToXML(bpmnModel);
			
			// 查询流程定义是否已经存在了 ThinkGem
			ProcessDefinition processDefinition = Context.getProcessEngineConfiguration()
					.getRepositoryService().createProcessDefinitionQuery()
					.processDefinitionKey(procDefKey).latestVersion().singleResult();
			if (processDefinition != null){
				ResourceEntityManager resourceEntityManager = commandContext.getResourceEntityManager();
				DeploymentEntity deployment = (DeploymentEntity)repositoryService.createDeploymentQuery()
						.deploymentId(processDefinition.getDeploymentId()).singleResult();
				
				// 删除原资源
				resourceEntityManager.deleteResourcesByDeploymentId(deployment.getId());
				Context.getCommandContext().getSession(DbSqlSession.class).flush();
				
				// 插入新资源
			    ResourceEntity resource = new ResourceEntity();
			    resource.setDeploymentId(deployment.getId());
			    resource.setName(procDefName + ".bpmn20.xml");
			    resource.setBytes(bpmnBytes);
				deployment.addResource(resource);
				resourceEntityManager.insertResource(resource);
				
				// 插入资源图片
				ProcessEngineConfigurationImpl processEngineConfiguration = Context.getProcessEngineConfiguration();
				byte[] diagramBytes = IoUtil.readInputStream(processEngineConfiguration.
	                    getProcessDiagramGenerator().generateDiagram(bpmnModel, "png", processEngineConfiguration.getActivityFontName(),
	                        processEngineConfiguration.getLabelFontName(),processEngineConfiguration.getAnnotationFontName(), processEngineConfiguration.getClassLoader()), null);
				ResourceEntity diagramResource = new ResourceEntity();
				diagramResource.setDeploymentId(deployment.getId());
				diagramResource.setName(procDefName + "." + processDefinition.getKey() + ".png");
				diagramResource.setBytes(diagramBytes);
				deployment.addResource(diagramResource);
				resourceEntityManager.insertResource(diagramResource);
			}
			// 不存在部署一个新的流程 ThinkGem
			else{ 
				repositoryService.createDeployment().name(procDefName).addInputStream(
						procDefName + ".bpmn20.xml", new ByteArrayInputStream(bpmnBytes)).deploy();
			}
		}catch(Exception e){
			throw new ActivitiException("模型部署到流程定义错误", e);
		}
		return null;
	}
}