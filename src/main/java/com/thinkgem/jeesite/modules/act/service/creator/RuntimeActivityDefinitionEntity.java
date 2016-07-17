package com.thinkgem.jeesite.modules.act.service.creator;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonProcessingException;

public interface RuntimeActivityDefinitionEntity
{
	/**
	 * 反序列化PropertiesText到Map
	 */
	void deserializeProperties() throws IOException;

	/**
	 * 获取工厂名
	 */
	String getFactoryName();

	/**
	 * 获取流程定义的ID
	 */
	String getProcessDefinitionId();

	/**
	 * 获取流程实例的ID
	 */
	String getProcessInstanceId();

	/**
	 * 获取PropertiesText，它是一个JSON字符串
	 */
	String getPropertiesText();

	/**
	 * 获取指定的属性值
	 */
	<T> T getProperty(String name);

	/**
	 * 序列化Map至PropertiesText
	 */
	void serializeProperties() throws JsonProcessingException;

	/**
	 * 设置工厂名
	 */
	void setFactoryName(String factoryName);

	/**
	 * 设置流程定义ID
	 */
	void setProcessDefinitionId(String processDefinitionId);

	/**
	 * 设置流程实例ID
	 */
	void setProcessInstanceId(String processInstanceId);

	/**
	 * 设置PropertiesText
	 */
	void setPropertiesText(String propertiesText);

	<T> void setProperty(String name, T value);
}