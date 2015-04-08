<?xml version="1.0" encoding="utf-8"?>
<template>
	<name>viewForm</name>
	<filePath>src/main/webapp/WEB-INF/views/${lastPackageName}/${moduleName}/${subModuleName}</filePath>
	<fileName>${className}Form.jsp</fileName>
	<content><![CDATA[
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>${functionNameSimple}管理</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		$(document).ready(function() {
			//$("#name").focus();
			$("#inputForm").validate({
				submitHandler: function(form){
					loading('正在提交，请稍等...');
					form.submit();
				},
				errorContainer: "#messageBox",
				errorPlacement: function(error, element) {
					$("#messageBox").text("输入有误，请先更正。");
					if (element.is(":checkbox")||element.is(":radio")||element.parent().is(".input-append")){
						error.appendTo(element.parent().parent());
					} else {
						error.insertAfter(element);
					}
				}
			});
		});
	<#list table.childList as c>
		function addRow(list, idx, tpl, row){
			$(list).append(Mustache.render(tpl, {
				idx: idx, delBtn: true, row: row
			}));
			$(list+idx).find("select").each(function(){
				$(this).val($(this).attr("data-value"));
			});
			$(list+idx).find("input[type='checkbox'], input[type='radio']").each(function(){
				var ss = $(this).attr("data-value").split(',');
				for (var i=0; i<ss.length; i++){
					if($(this).val() == ss[i]){
						$(this).attr("checked","checked");
					}
				}
			});
		}
		function delRow(obj, prefix){
			var id = $(prefix+"_id");
			var delFlag = $(prefix+"_delFlag");
			if (id.val() == ""){
				$(obj).parent().parent().remove();
			}else if(delFlag.val() == "0"){
				delFlag.val("1");
				$(obj).html("&divide;").attr("title", "撤销删除");
				$(obj).parent().parent().addClass("error");
			}else if(delFlag.val() == "1"){
				delFlag.val("0");
				$(obj).html("&times;").attr("title", "删除");
				$(obj).parent().parent().removeClass("error");
			}
		}
		<#break/>
	</#list>
	</script>
</head>
<body>
	<ul class="nav nav-tabs">
		<li><a href="${r"${ctx}"}/${urlPrefix}/">${functionNameSimple}列表</a></li>
		<li class="active"><a href="${r"${ctx}"}/${urlPrefix}/form?id=${"${"+className+".id}"}">${functionNameSimple}<shiro:hasPermission name="${permissionPrefix}:edit">${r"${not empty "+className+".id?'修改':'添加'}"}</shiro:hasPermission><shiro:lacksPermission name="${permissionPrefix}:edit">查看</shiro:lacksPermission></a></li>
	</ul><br/>
	<form:form id="inputForm" modelAttribute="${className}" action="${r"${ctx}"}/${urlPrefix}/save" method="post" class="form-horizontal">
		<form:hidden path="id"/>
		<sys:message content="${r"${message}"}"/>		
		<#list table.columnList as c>
			<#if c.isEdit?? && c.isEdit == "1" && (c.isNotBaseField || c.simpleJavaField == 'remarks')>
		<div class="control-group">
			<label class="control-label">${c.comments}：</label>
			<div class="controls">
			<#if c.showType == "input">
				<form:input path="${c.javaFieldId}" htmlEscape="false"<#if c.dataLength != "0"> maxlength="${c.dataLength}"</#if> class="input-xlarge <#if c.isNull != "1">required</#if><#if c.javaType == "Long" || c.javaType == "Integer"> digits</#if><#if c.javaType == "Double"> number</#if>"/>
			<#elseif c.showType == "textarea">
				<form:textarea path="${c.javaFieldId}" htmlEscape="false" rows="4"<#if c.dataLength != "0"> maxlength="${c.dataLength}"</#if> class="input-xxlarge <#if c.isNull != "1">required</#if>"/>
			<#elseif c.showType == "select">
				<form:select path="${c.javaFieldId}" class="input-xlarge <#if c.isNull != "1">required</#if>">
					<form:option value="" label=""/>
					<form:options items="${"$"}{fns:getDictList('${c.dictType}')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
				</form:select>
			<#elseif c.showType == "checkbox">
				<form:checkboxes path="${c.javaFieldId}" items="${"$"}{fns:getDictList('${c.dictType}')}" itemLabel="label" itemValue="value" htmlEscape="false" class="<#if c.isNull != "1">required</#if>"/>
			<#elseif c.showType == "radiobox">
				<form:radiobuttons path="${c.javaFieldId}" items="${"$"}{fns:getDictList('${c.dictType}')}" itemLabel="label" itemValue="value" htmlEscape="false" class="<#if c.isNull != "1">required</#if>"/>
			<#elseif c.showType == "dateselect">
				<input name="${c.javaFieldId}" type="text" readonly="readonly" maxlength="20" class="input-medium Wdate <#if c.isNull != "1">required</#if>"
					value="<fmt:formatDate value="${"$"}{${className}.${c.javaFieldId}}" pattern="yyyy-MM-dd HH:mm:ss"/>"
					onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',isShowClear:false});"/>
			<#elseif c.showType == "userselect">
				<sys:treeselect id="${c.simpleJavaField}" name="${c.javaFieldId}" value="${"$"}{${className}.${c.javaFieldId}}" labelName="${c.javaFieldName}" labelValue="${"$"}{${className}.${c.javaFieldName}}"
					title="用户" url="/sys/office/treeData?type=3" cssClass="<#if c.isNull != "1">required</#if>" allowClear="true" notAllowSelectParent="true"/>
			<#elseif c.showType == "officeselect">
				<sys:treeselect id="${c.simpleJavaField}" name="${c.javaFieldId}" value="${"$"}{${className}.${c.javaFieldId}}" labelName="${c.javaFieldName}" labelValue="${"$"}{${className}.${c.javaFieldName}}"
					title="部门" url="/sys/office/treeData?type=2" cssClass="<#if c.isNull != "1">required</#if>" allowClear="true" notAllowSelectParent="true"/>
			<#elseif c.showType == "areaselect">
				<sys:treeselect id="${c.simpleJavaField}" name="${c.javaFieldId}" value="${"$"}{${className}.${c.javaFieldId}}" labelName="${c.javaFieldName}" labelValue="${"$"}{${className}.${c.javaFieldName}}"
					title="区域" url="/sys/area/treeData" cssClass="<#if c.isNull != "1">required</#if>" allowClear="true" notAllowSelectParent="true"/>
			<#elseif c.showType == "fileselect">
				<form:hidden id="${c.simpleJavaField}" path="${c.javaFieldId}" htmlEscape="false"<#if c.dataLength != "0"> maxlength="${c.dataLength}"</#if> class="input-xlarge"/>
				<sys:ckfinder input="${c.simpleJavaField}" type="files" uploadPath="/${moduleName}<#if subModuleName != "">/${subModuleName}</#if>/${className}" selectMultiple="true"/>
			</#if>
			<#if c.isNull != "1">
				<span class="help-inline"><font color="red">*</font> </span>
			</#if>
			</div>
		</div>
			</#if>
		</#list>
		<#list table.childList as child>
			<div class="control-group">
				<label class="control-label">${child.comments}：</label>
				<div class="controls">
					<table id="contentTable" class="table table-striped table-bordered table-condensed">
						<thead>
							<tr>
								<th class="hide"></th>
								<#assign childColumnCount = 0 /><#list child.columnList as c>
									<#if c.isEdit?? && c.isEdit == "1" && (c.isNotBaseField || c.simpleJavaField == 'remarks') && c.name != c.genTable.parentTableFk>
								<th>${c.comments}</th><#assign childColumnCount = childColumnCount + 1 />
									</#if>
								</#list>
								<shiro:hasPermission name="${permissionPrefix}:edit"><th width="10">&nbsp;</th></shiro:hasPermission>
							</tr>
						</thead>
						<tbody id="${child.className?uncap_first}List">
						</tbody>
						<shiro:hasPermission name="${permissionPrefix}:edit"><tfoot>
							<tr><td colspan="${childColumnCount + 2}"><a href="javascript:" onclick="addRow('#${child.className?uncap_first}List', ${child.className?uncap_first}RowIdx, ${child.className?uncap_first}Tpl);${child.className?uncap_first}RowIdx = ${child.className?uncap_first}RowIdx + 1;" class="btn">新增</a></td></tr>
						</tfoot></shiro:hasPermission>
					</table>
					<script type="text/template" id="${child.className?uncap_first}Tpl">//<!--
						<tr id="${child.className?uncap_first}List{{idx}}">
							<td class="hide">
								<input id="${child.className?uncap_first}List{{idx}}_id" name="${child.className?uncap_first}List[{{idx}}].id" type="hidden" value="{{row.id}}"/>
								<input id="${child.className?uncap_first}List{{idx}}_delFlag" name="${child.className?uncap_first}List[{{idx}}].delFlag" type="hidden" value="0"/>
							</td>
							<#list child.columnList as c>
								<#if c.isEdit?? && c.isEdit == "1" && (c.isNotBaseField || c.simpleJavaField == 'remarks') && c.name != c.genTable.parentTableFk>
							<td>
							<#if c.showType == "input">
								<input id="${child.className?uncap_first}List{{idx}}_${c.simpleJavaField}" name="${child.className?uncap_first}List[{{idx}}].${c.javaFieldId}" type="text" value="{{row.${c.javaFieldId}}}"<#if c.dataLength != "0"> maxlength="${c.dataLength}"</#if> class="input-small <#if c.isNull != "1">required</#if><#if c.javaType == "Long" || c.javaType == "Integer"> digits</#if><#if c.javaType == "Double"> number</#if>"/>
							<#elseif c.showType == "textarea">
								<textarea id="${child.className?uncap_first}List{{idx}}_${c.simpleJavaField}" name="${child.className?uncap_first}List[{{idx}}].${c.javaFieldId}" rows="4"<#if c.dataLength != "0"> maxlength="${c.dataLength}"</#if> class="input-small <#if c.isNull != "1">required</#if>">{{row.${c.javaFieldId}}}</textarea>
							<#elseif c.showType == "select">
								<select id="${child.className?uncap_first}List{{idx}}_${c.simpleJavaField}" name="${child.className?uncap_first}List[{{idx}}].${c.javaFieldId}" data-value="{{row.${c.javaFieldId}}}" class="input-small <#if c.isNull != "1">required</#if>">
									<option value=""></option>
									<c:forEach items="${"$"}{fns:getDictList('${c.dictType}')}" var="dict">
										<option value="${"$"}{dict.value}">${"$"}{dict.label}</option>
									</c:forEach>
								</select>
							<#elseif c.showType == "checkbox">
								<c:forEach items="${"$"}{fns:getDictList('${c.dictType}')}" var="dict" varStatus="dictStatus">
									<span><input id="${child.className?uncap_first}List{{idx}}_${c.simpleJavaField}${"$"}{dictStatus.index}" name="${child.className?uncap_first}List[{{idx}}].${c.javaFieldId}" type="checkbox" value="${"$"}{dict.value}" data-value="{{row.${c.javaFieldId}}}"><label for="${child.className?uncap_first}List{{idx}}_${c.simpleJavaField}${"$"}{dictStatus.index}">${"$"}{dict.label}</label></span>
								</c:forEach>
							<#elseif c.showType == "radiobox">
								<c:forEach items="${"$"}{fns:getDictList('${c.dictType}')}" var="dict" varStatus="dictStatus">
									<span><input id="${child.className?uncap_first}List{{idx}}_${c.simpleJavaField}${"$"}{dictStatus.index}" name="${child.className?uncap_first}List[{{idx}}].${c.javaFieldId}" type="radio" value="${"$"}{dict.value}" data-value="{{row.${c.javaFieldId}}}"><label for="${child.className?uncap_first}List{{idx}}_${c.simpleJavaField}${"$"}{dictStatus.index}">${"$"}{dict.label}</label></span>
								</c:forEach>
							<#elseif c.showType == "dateselect">
								<input id="${child.className?uncap_first}List{{idx}}_${c.simpleJavaField}" name="${child.className?uncap_first}List[{{idx}}].${c.javaFieldId}" type="text" readonly="readonly" maxlength="20" class="input-medium Wdate <#if c.isNull != "1">required</#if>"
									value="{{row.${c.javaFieldId}}}" onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',isShowClear:false});"/>
							<#elseif c.showType == "userselect">
								<sys:treeselect id="${child.className?uncap_first}List{{idx}}_${c.simpleJavaField}" name="${child.className?uncap_first}List[{{idx}}].${c.javaFieldId}" value="{{row.${c.javaFieldId}}}" labelName="${child.className?uncap_first}List{{idx}}.${c.javaFieldName}" labelValue="{{row.${c.javaFieldName}}}"
									title="用户" url="/sys/office/treeData?type=3" cssClass="<#if c.isNull != "1">required</#if>" allowClear="true" notAllowSelectParent="true"/>
							<#elseif c.showType == "officeselect">
								<sys:treeselect id="${child.className?uncap_first}List{{idx}}_${c.simpleJavaField}" name="${child.className?uncap_first}List[{{idx}}].${c.javaFieldId}" value="{{row.${c.javaFieldId}}}" labelName="${child.className?uncap_first}List{{idx}}.${c.javaFieldName}" labelValue="{{row.${c.javaFieldName}}}"
									title="部门" url="/sys/office/treeData?type=2" cssClass="<#if c.isNull != "1">required</#if>" allowClear="true" notAllowSelectParent="true"/>
							<#elseif c.showType == "areaselect">
								<sys:treeselect id="${child.className?uncap_first}List{{idx}}_${c.simpleJavaField}" name="${child.className?uncap_first}List[{{idx}}].${c.javaFieldId}" value="{{row.${c.javaFieldId}}}" labelName="${child.className?uncap_first}List{{idx}}.${c.javaFieldName}" labelValue="{{row.${c.javaFieldName}}}"
									title="区域" url="/sys/area/treeData" cssClass="<#if c.isNull != "1">required</#if>" allowClear="true" notAllowSelectParent="true"/>
							<#elseif c.showType == "fileselect">
								<input id="${child.className?uncap_first}List{{idx}}_${c.simpleJavaField}" name="${child.className?uncap_first}List[{{idx}}].${c.simpleJavaField}" type="hidden" value="{{row.${c.javaFieldId}}}"<#if c.dataLength != "0"> maxlength="${c.dataLength}"</#if>/>
								<sys:ckfinder input="${child.className?uncap_first}List{{idx}}_${c.simpleJavaField}" type="files" uploadPath="/${moduleName}<#if subModuleName != "">/${subModuleName}</#if>/${className}" selectMultiple="true"/>
							</#if>
							</td>
								</#if>
							</#list>
							<shiro:hasPermission name="${permissionPrefix}:edit"><td class="text-center" width="10">
								{{#delBtn}}<span class="close" onclick="delRow(this, '#${child.className?uncap_first}List{{idx}}')" title="删除">&times;</span>{{/delBtn}}
							</td></shiro:hasPermission>
						</tr>//-->
					</script>
					<script type="text/javascript">
						var ${child.className?uncap_first}RowIdx = 0, ${child.className?uncap_first}Tpl = $("#${child.className?uncap_first}Tpl").html().replace(/(\/\/\<!\-\-)|(\/\/\-\->)/g,"");
						$(document).ready(function() {
							var data = ${"$"}{fns:toJson(${className}.${child.className?uncap_first}List)};
							for (var i=0; i<data.length; i++){
								addRow('#${child.className?uncap_first}List', ${child.className?uncap_first}RowIdx, ${child.className?uncap_first}Tpl, data[i]);
								${child.className?uncap_first}RowIdx = ${child.className?uncap_first}RowIdx + 1;
							}
						});
					</script>
				</div>
			</div>
		</#list>
		<div class="form-actions">
			<shiro:hasPermission name="${permissionPrefix}:edit"><input id="btnSubmit" class="btn btn-primary" type="submit" value="保 存"/>&nbsp;</shiro:hasPermission>
			<input id="btnCancel" class="btn" type="button" value="返 回" onclick="history.go(-1)"/>
		</div>
	</form:form>
</body>
</html>]]>
	</content>
</template>