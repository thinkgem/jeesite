<?xml version="1.0" encoding="utf-8"?>
<template>
	<name>service</name>
	<filePath>src/main/java/${packageName}/${moduleName}/service/${subModuleName}</filePath>
	<fileName>${ClassName}Service.java</fileName>
	<content><![CDATA[
/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package ${packageName}.${moduleName}.service<#if subModuleName != "">.${subModuleName}</#if>;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.thinkgem.jeesite.common.service.TreeService;
import com.thinkgem.jeesite.common.utils.StringUtils;
import ${packageName}.${moduleName}.entity<#if subModuleName != "">.${subModuleName}</#if>.${ClassName};
import ${packageName}.${moduleName}.dao<#if subModuleName != "">.${subModuleName}</#if>.${ClassName}Dao;

/**
 * ${functionName}Service
 * @author ${functionAuthor}
 * @version ${functionVersion}
 */
@Service
@Transactional(readOnly = true)
public class ${ClassName}Service extends TreeService<${ClassName}Dao, ${ClassName}> {

	public ${ClassName} get(String id) {
		return super.get(id);
	}
	
	public List<${ClassName}> findList(${ClassName} ${className}) {
		if (StringUtils.isNotBlank(${className}.getParentIds())){
			${className}.setParentIds(","+${className}.getParentIds()+",");
		}
		return super.findList(${className});
	}
	
	@Transactional(readOnly = false)
	public void save(${ClassName} ${className}) {
		super.save(${className});
	}
	
	@Transactional(readOnly = false)
	public void delete(${ClassName} ${className}) {
		super.delete(${className});
	}
	
}]]>
	</content>
</template>