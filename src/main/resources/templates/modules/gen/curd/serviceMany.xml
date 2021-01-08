<?xml version="1.0" encoding="utf-8"?>
<template>
	<name>service</name>
	<filePath>src/main/java/${packageName}/${moduleName}/service/${subModuleName}</filePath>
	<fileName>${ClassName}Service.java</fileName>
	<content><![CDATA[
/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package ${packageName}.${moduleName}.service<#if subModuleName != "">.${subModuleName}</#if>;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.service.CrudService;
import com.thinkgem.jeesite.common.utils.StringUtils;
import ${packageName}.${moduleName}.entity<#if subModuleName != "">.${subModuleName}</#if>.${ClassName};
import ${packageName}.${moduleName}.dao<#if subModuleName != "">.${subModuleName}</#if>.${ClassName}Dao;
<#list table.childList as c>
import ${packageName}.${moduleName}.entity<#if subModuleName != "">.${subModuleName}</#if>.${c.className?cap_first};
import ${packageName}.${moduleName}.dao<#if subModuleName != "">.${subModuleName}</#if>.${c.className?cap_first}Dao;
</#list>

/**
 * ${functionName}Service
 * @author ${functionAuthor}
 * @version ${functionVersion}
 */
@Service
@Transactional(readOnly = true)
public class ${ClassName}Service extends CrudService<${ClassName}Dao, ${ClassName}> {

	<#list table.childList as c>
	@Autowired
	private ${c.className?cap_first}Dao ${c.className?uncap_first}Dao;
	</#list>
	
	public ${ClassName} get(String id) {
		${ClassName} ${className} = super.get(id);
		<#list table.childList as c>
		${className}.set${c.className?cap_first}List(${c.className?uncap_first}Dao.findList(new ${c.className?cap_first}(${className})));
		</#list>
		return ${className};
	}
	
	public List<${ClassName}> findList(${ClassName} ${className}) {
		return super.findList(${className});
	}
	
	public Page<${ClassName}> findPage(Page<${ClassName}> page, ${ClassName} ${className}) {
		return super.findPage(page, ${className});
	}
	
	@Transactional(readOnly = false)
	public void save(${ClassName} ${className}) {
		super.save(${className});
	<#list table.childList as c>
		for (${c.className?cap_first} ${c.className?uncap_first} : ${className}.get${c.className?cap_first}List()){
			if (${c.className?uncap_first}.getId() == null){
				continue;
			}
			if (${c.className?cap_first}.DEL_FLAG_NORMAL.equals(${c.className?uncap_first}.getDelFlag())){
				if (StringUtils.isBlank(${c.className?uncap_first}.getId())){
					<#if c.parentExists>
						<#list c.columnList as cc>
							<#if c.parentTableFk == cc.name>
					${c.className?uncap_first}.set${cc.simpleJavaField?cap_first}(${className});
							</#if>
						</#list>
					</#if>
					${c.className?uncap_first}.preInsert();
					${c.className?uncap_first}Dao.insert(${c.className?uncap_first});
				}else{
					${c.className?uncap_first}.preUpdate();
					${c.className?uncap_first}Dao.update(${c.className?uncap_first});
				}
			}else{
				${c.className?uncap_first}Dao.delete(${c.className?uncap_first});
			}
		}
	</#list>
	}
	
	@Transactional(readOnly = false)
	public void delete(${ClassName} ${className}) {
		super.delete(${className});
	<#list table.childList as c>
		${c.className?uncap_first}Dao.delete(new ${c.className?cap_first}(${className}));
	</#list>
	}
	
}]]>
	</content>
</template>