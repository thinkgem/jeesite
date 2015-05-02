/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.act.service;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.activiti.bpmn.converter.BpmnXMLConverter;
import org.activiti.bpmn.model.BpmnModel;
import org.activiti.editor.constants.ModelDataJsonConstants;
import org.activiti.editor.language.json.converter.BpmnJsonConverter;
import org.activiti.engine.ActivitiException;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.ModelQuery;
import org.activiti.engine.repository.ProcessDefinition;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.node.ObjectNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.service.BaseService;

/**
 * 流程模型相关Controller
 * @author ThinkGem
 * @version 2013-11-03
 */
@Service
@Transactional(readOnly = true)
public class ActModelService extends BaseService {

	@Autowired
	private RepositoryService repositoryService;

	/**
	 * 流程模型列表
	 */
	public Page<org.activiti.engine.repository.Model> modelList(Page<org.activiti.engine.repository.Model> page, String category) {

		ModelQuery modelQuery = repositoryService.createModelQuery().latestVersion().orderByLastUpdateTime().desc();
		
		if (StringUtils.isNotEmpty(category)){
			modelQuery.modelCategory(category);
		}
		
		page.setCount(modelQuery.count());
		page.setList(modelQuery.listPage(page.getFirstResult(), page.getMaxResults()));

		return page;
	}

	/**
	 * 创建模型
	 * @throws UnsupportedEncodingException 
	 */
	@Transactional(readOnly = false)
	public org.activiti.engine.repository.Model create(String name, String key, String description, String category) throws UnsupportedEncodingException {
		ObjectMapper objectMapper = new ObjectMapper();
		ObjectNode editorNode = objectMapper.createObjectNode();
		editorNode.put("id", "canvas");
		editorNode.put("resourceId", "canvas");
		ObjectNode stencilSetNode = objectMapper.createObjectNode();
		stencilSetNode.put("namespace", "http://b3mn.org/stencilset/bpmn2.0#");
		editorNode.put("stencilset", stencilSetNode);
		org.activiti.engine.repository.Model modelData = repositoryService.newModel();

		description = StringUtils.defaultString(description);
		modelData.setKey(StringUtils.defaultString(key));
		modelData.setName(name);
		modelData.setCategory(category);
		modelData.setVersion(Integer.parseInt(String.valueOf(repositoryService.createModelQuery().modelKey(modelData.getKey()).count()+1)));

		ObjectNode modelObjectNode = objectMapper.createObjectNode();
		modelObjectNode.put(ModelDataJsonConstants.MODEL_NAME, name);
		modelObjectNode.put(ModelDataJsonConstants.MODEL_REVISION, modelData.getVersion());
		modelObjectNode.put(ModelDataJsonConstants.MODEL_DESCRIPTION, description);
		modelData.setMetaInfo(modelObjectNode.toString());
		
		repositoryService.saveModel(modelData);
		repositoryService.addModelEditorSource(modelData.getId(), editorNode.toString().getBytes("utf-8"));
		
		return modelData;
	}

	/**
	 * 根据Model部署流程
	 */
	@Transactional(readOnly = false)
	public String deploy(String id) {
		String message = "";
		try {
			org.activiti.engine.repository.Model modelData = repositoryService.getModel(id);
			BpmnJsonConverter jsonConverter = new BpmnJsonConverter();
			JsonNode editorNode = new ObjectMapper().readTree(repositoryService.getModelEditorSource(modelData.getId()));
			BpmnModel bpmnModel = jsonConverter.convertToBpmnModel(editorNode);
			BpmnXMLConverter xmlConverter = new BpmnXMLConverter();
			byte[] bpmnBytes = xmlConverter.convertToXML(bpmnModel);
			
			String processName = modelData.getName();
			if (!StringUtils.endsWith(processName, ".bpmn20.xml")){
				processName += ".bpmn20.xml";
			}
//			System.out.println("========="+processName+"============"+modelData.getName());
			ByteArrayInputStream in = new ByteArrayInputStream(bpmnBytes);
			Deployment deployment = repositoryService.createDeployment().name(modelData.getName())
					.addInputStream(processName, in).deploy();
//					.addString(processName, new String(bpmnBytes)).deploy();
			
			// 设置流程分类
			List<ProcessDefinition> list = repositoryService.createProcessDefinitionQuery().deploymentId(deployment.getId()).list();
			for (ProcessDefinition processDefinition : list) {
				repositoryService.setProcessDefinitionCategory(processDefinition.getId(), modelData.getCategory());
				message = "部署成功，流程ID=" + processDefinition.getId();
			}
			if (list.size() == 0){
				message = "部署失败，没有流程。";
			}
		} catch (Exception e) {
			throw new ActivitiException("设计模型图不正确，检查模型正确性，模型ID="+id, e);
		}
		return message;
	}
	
	/**
	 * 导出model的xml文件
	 * @throws IOException 
	 * @throws JsonProcessingException 
	 */
	public void export(String id, HttpServletResponse response) {
		try {
			org.activiti.engine.repository.Model modelData = repositoryService.getModel(id);
			BpmnJsonConverter jsonConverter = new BpmnJsonConverter();
			JsonNode editorNode = new ObjectMapper().readTree(repositoryService.getModelEditorSource(modelData.getId()));
			BpmnModel bpmnModel = jsonConverter.convertToBpmnModel(editorNode);
			BpmnXMLConverter xmlConverter = new BpmnXMLConverter();
			byte[] bpmnBytes = xmlConverter.convertToXML(bpmnModel);

			ByteArrayInputStream in = new ByteArrayInputStream(bpmnBytes);
			IOUtils.copy(in, response.getOutputStream());
			String filename = bpmnModel.getMainProcess().getId() + ".bpmn20.xml";
			response.setHeader("Content-Disposition", "attachment; filename=" + filename);
			response.flushBuffer();
		} catch (Exception e) {
			throw new ActivitiException("导出model的xml文件失败，模型ID="+id, e);
		}
		
	}

	/**
	 * 更新Model分类
	 */
	public void updateCategory(String id, String category) {
		org.activiti.engine.repository.Model modelData = repositoryService.getModel(id);
		modelData.setCategory(category);
		repositoryService.saveModel(modelData);
	}
	
	/**
	 * 删除模型
	 * @param id
	 * @return
	 */
	public void delete(String id) {
		repositoryService.deleteModel(id);
	}
}
