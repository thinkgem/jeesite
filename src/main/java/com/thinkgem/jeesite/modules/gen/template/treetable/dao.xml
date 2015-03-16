<?xml version="1.0" encoding="utf-8"?>
<template>
	<name>dao</name>
	<filePath>src/main/java/${packageName}/${moduleName}/dao/${subModuleName}</filePath>
	<fileName>${ClassName}Dao.java</fileName>
	<content><![CDATA[
/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package ${packageName}.${moduleName}.dao<#if subModuleName != "">.${subModuleName}</#if>;

import com.thinkgem.jeesite.common.persistence.TreeDao;
import com.thinkgem.jeesite.common.persistence.annotation.MyBatisDao;
import ${packageName}.${moduleName}.entity<#if subModuleName != "">.${subModuleName}</#if>.${ClassName};

/**
 * ${functionName}DAO接口
 * @author ${functionAuthor}
 * @version ${functionVersion}
 */
@MyBatisDao
public interface ${ClassName}Dao extends TreeDao<${ClassName}> {
	
}]]>
	</content>
</template>