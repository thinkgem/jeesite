package com.thinkgem.jeesite.modules.act.service.creator;

import java.util.List;

/**
 * RuntimeActivityDefinitionEntity的解释类（代理类）
 * 主要用以解释properties字段的值，如为get("name")提供getName()方法
 * 
 * @author bluejoe2008@gmail.com
 * 
 */
public class RuntimeActivityDefinitionEntityIntepreter
{
	RuntimeActivityDefinitionEntity _entity;

	public RuntimeActivityDefinitionEntityIntepreter(RuntimeActivityDefinitionEntity entity)
	{
		super();
		_entity = entity;
	}

	public List<String> getAssignees()
	{
		return _entity.getProperty("assignees");
	}

	public String getCloneActivityId()
	{
		return _entity.getProperty("cloneActivityId");
	}

	public List<String> getCloneActivityIds()
	{
		return _entity.getProperty("cloneActivityIds");
	}

	public String getNextActivityId()
	{
		return _entity.getProperty("nextActivityId");
	}

	public String getPrototypeActivityId()
	{
		return _entity.getProperty("prototypeActivityId");
	}

	public boolean getSequential()
	{
		return (Boolean) _entity.getProperty("sequential");
	}

	public void setAssignees(List<String> assignees)
	{
		_entity.setProperty("assignees", assignees);
	}

	public void setCloneActivityId(String cloneActivityId)
	{
		_entity.setProperty("cloneActivityId", cloneActivityId);
	}

	public void setCloneActivityIds(List<String> cloneActivityIds)
	{
		_entity.setProperty("cloneActivityIds", cloneActivityIds);
	}

	public void setNextActivityId(String nextActivityId)
	{
		_entity.setProperty("nextActivityId", nextActivityId);
	}

	public void setPrototypeActivityId(String prototypeActivityId)
	{
		_entity.setProperty("prototypeActivityId", prototypeActivityId);
	}

	public void setSequential(boolean sequential)
	{
		_entity.setProperty("sequential", sequential);
	}
}
