<?xml version="1.0" encoding="utf-8"?>
<template>
	<name>mapper</name>
	<filePath>src/main/resources/mappings/${lastPackageName}/${moduleName}/${subModuleName}</filePath>
	<fileName>${ClassName}Dao.xml</fileName>
	<content><![CDATA[
<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE mapper PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN" "http://mybatis.org/dtd/mybatis-3-mapper.dtd">
<mapper namespace="${packageName}.${moduleName}.dao<#if subModuleName != "">.${subModuleName}</#if>.${ClassName}Dao">
    
    <#-- 输出字段列 -->
	<sql id="${className}Columns">
		<#assign columnField>
			<#list table.columnList as c>
		a.${c.name} AS "${c.javaFieldId}",
			</#list>
			<#list table.columnList as c>
				<#if c.showType?? && c.showType == "userselect">
					<#list c.javaFieldAttrs as a>
		u${c_index + 1}.${a[1]} AS "${c.simpleJavaField}.${a[0]}",
					</#list>
				<#elseif c.showType?? && c.showType == "officeselect">
					<#list c.javaFieldAttrs as a>
		o${c_index + 1}.${a[1]} AS "${c.simpleJavaField}.${a[0]}",
					</#list>
				<#elseif c.showType?? && c.showType == "areaselect">
					<#list c.javaFieldAttrs as a>
		a${c_index + 1}.${a[1]} AS "${c.simpleJavaField}.${a[0]}",
					</#list>
				</#if>
				<#-- 父表关联字段 -->
				<#if table.parentExists && table.parentTableFk == c.name>
					<#list c.javaFieldAttrs as a>
		b.${a[1]} AS "${c.simpleJavaField}.${a[0]}",
					</#list>
				</#if>
			</#list>
		</#assign>
${columnField?substring(0, columnField?last_index_of(","))}
	</sql>
	
	<#-- 输出字段关联表 -->
	<sql id="${className}Joins">
		<#-- 关联父表 -->
		<#if table.parentExists>
		LEFT JOIN ${table.parent.name} b ON b.id = a.${table.parentTableFk}
		</#if>
		<#-- 关联系统表 -->
		<#list table.columnList as c>
			<#if c.showType?? && c.showType == "userselect">
		LEFT JOIN sys_user u${c_index + 1} ON u${c_index + 1}.id = a.${c.name}
			<#elseif c.showType?? && c.showType == "officeselect">
		LEFT JOIN sys_office o${c_index + 1} ON o${c_index + 1}.id = a.${c.name}
			<#elseif c.showType?? && c.showType == "areaselect">
		LEFT JOIN sys_area a${c_index + 1} ON a${c_index + 1}.id = a.${c.name}
			</#if>
		</#list>
	</sql>
    
	<select id="get" resultType="${ClassName}">
		SELECT 
			<include refid="${className}Columns"/>
		FROM ${table.name} a
		<include refid="${className}Joins"/>
		WHERE a.id = ${"#"}{id}
	</select>
	
	<select id="findList" resultType="${ClassName}">
		SELECT 
			<include refid="${className}Columns"/>
		FROM ${table.name} a
		<include refid="${className}Joins"/>
		<where>
			<#if table.delFlagExists>a.del_flag = ${"#"}{DEL_FLAG_NORMAL}</#if>
			<#list table.columnList as c>
				<#if (c.isQuery?? && c.isQuery == "1") || (table.parentExists && table.parentTableFk == c.name) || c.javaFieldId == 'parent.id' || c.javaFieldId == 'parentIds'>
					<#if c.queryType ?? && c.queryType == 'between'>
			<if test="begin${c.simpleJavaField?cap_first} != null and end${c.simpleJavaField?cap_first} != null <#if c.simpleJavaField != c.javaFieldId>and begin${c.javaFieldId?cap_first} != null and end${c.javaFieldId?cap_first} != null </#if>and begin${c.javaFieldId?cap_first} != '' and end${c.javaFieldId?cap_first} != ''">
					<#else>
			<if test="${c.simpleJavaField} != null<#if c.simpleJavaField != c.javaFieldId> and ${c.javaFieldId} != null</#if> and ${c.javaFieldId} != ''">
					</#if>
					<#if c.queryType ?? && c.queryType == 'between'>
				AND a.${c.name} BETWEEN ${"#"}{begin${c.simpleJavaField?cap_first}} AND ${"#"}{end${c.simpleJavaField?cap_first}}
					<#elseif c.queryType ?? && c.queryType == 'like'>
				AND a.${c.name} LIKE 
					<if test="dbName == 'oracle'">'%'||${"#"}{${c.javaFieldId}}||'%'</if>
					<if test="dbName == 'mssql'">'%'+${"#"}{${c.javaFieldId}}+'%'</if>
					<if test="dbName == 'mysql'">concat('%',${"#"}{${c.javaFieldId}},'%')</if>
					<#elseif c.queryType ?? && c.queryType == 'left_like'>
				AND a.${c.name} LIKE 
					<if test="dbName == 'oracle'">'%'||${"#"}{${c.javaFieldId}}</if>
					<if test="dbName == 'mssql'">'%'+${"#"}{${c.javaFieldId}}</if>
					<if test="dbName == 'mysql'">concat('%',${"#"}{${c.javaFieldId}})</if>
					<#elseif c.queryType ?? && c.queryType == 'right_like'>
				AND a.${c.name} LIKE 
					<if test="dbName == 'oracle'">${"#"}{${c.javaFieldId}}||'%'</if>
					<if test="dbName == 'mssql'">${"#"}{${c.javaFieldId}}+'%'</if>
					<if test="dbName == 'mysql'">concat(${"#"}{${c.javaFieldId}},'%')</if>
					<#else>
				AND a.${c.name} ${c.queryType} ${"#"}{${c.javaFieldId}}
					</#if>
			</if>
				</#if>
			</#list>
		</where>
		<#list table.columnList as c>
			<#if c.name == 'sort'>
		ORDER BY a.sort ASC
			</#if>
		</#list>
	</select>
	
	<select id="findAllList" resultType="${ClassName}">
		SELECT 
			<include refid="${className}Columns"/>
		FROM ${table.name} a
		<include refid="${className}Joins"/>
		<where>
			<#if table.delFlagExists>a.del_flag = ${"#"}{DEL_FLAG_NORMAL}</#if>
		</where>
		<#list table.columnList as c>
			<#if c.name == 'sort'>
		ORDER BY a.sort ASC
			</#if>
		</#list>
	</select>
	
	<select id="findByParentIdsLike" resultType="${ClassName}">
		SELECT
			a.id,
			a.parent_id AS "parent.id",
			a.parent_ids
		FROM ${table.name} a
		<include refid="${className}Joins"/>
		<where>
			<#if table.delFlagExists>a.del_flag = ${"#"}{DEL_FLAG_NORMAL}</#if>
			AND a.parent_ids LIKE ${"#"}{parentIds}
		</where>
		<#list table.columnList as c>
			<#if c.name == 'sort'>
		ORDER BY a.sort ASC
			</#if>
		</#list>
	</select>
	
	<insert id="insert">
		INSERT INTO ${table.name}(
		<#assign insertField>
			<#list table.columnList as c>
				<#if c.isInsert?? && c.isInsert == "1">
			${c.name},
				</#if>
			</#list>
		</#assign>
${insertField?substring(0, insertField?last_index_of(","))}
		) VALUES (
		<#assign insertJavaField>
			<#list table.columnList as c>
				<#if c.isInsert?? && c.isInsert == "1">
			${"#"}{${c.javaFieldId}},
				</#if>
			</#list>
		</#assign>
${insertJavaField?substring(0, insertJavaField?last_index_of(","))}
		)
	</insert>
	
	<update id="update">
		UPDATE ${table.name} SET 	
			<#assign updateField>		
				<#list table.columnList as c>
					<#if c.isEdit?? && c.isEdit == "1">
			${c.name} = ${"#"}{${c.javaFieldId}},
					</#if>
				</#list>
			</#assign>
${updateField?substring(0, updateField?last_index_of(","))}
		WHERE id = ${"#"}{id}
	</update>
	
	<update id="updateParentIds">
		UPDATE ${table.name} SET 
			parent_id = ${"#"}{parent.id}, 
			parent_ids = ${"#"}{parentIds}
		WHERE id = ${"#"}{id}
	</update>
	
	<update id="delete">
	<#if table.delFlagExists>
		UPDATE ${table.name} SET 
			del_flag = ${"#"}{DEL_FLAG_DELETE}
	<#else>
		DELETE FROM ${table.name}
	</#if>
	<#if table.parentExists>
		<#list table.columnList as c>
			<#if table.parentTableFk == c.name>
		<choose>
			<when test="id !=null and id != ''">
				WHERE id = ${"#"}{id}
			</when>
			<otherwise>
				WHERE ${table.parentTableFk} = ${"#"}{${c.javaFieldId}}
			</otherwise>
		</choose>
			</#if>
		</#list>
	<#else>
		WHERE id = ${"#"}{id} OR parent_ids LIKE '%,'||${"#"}{id}||',%'
	</#if>
	</update>
	
</mapper>]]>
	</content>
</template>