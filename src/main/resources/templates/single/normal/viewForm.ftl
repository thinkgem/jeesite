<#assign columnList = sourceTable.sourceColumnList>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>${sourceTable.logicalName}管理</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		$(document).ready(function() {
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
	</script>
</head>
<body>
	<ul class="nav nav-tabs">
		<li><a href="${f.dollar}{ctx}/${sourceTable.moduleName}/${sourceTable.entityClassName?uncap_first}/">${sourceTable.logicalName}列表</a></li>
		<li class="active"><a href="${f.dollar}{ctx}/${sourceTable.moduleName}/${sourceTable.entityClassName?uncap_first}/form?id=${f.dollar}{${sourceTable.entityClassName?uncap_first}.id}">${sourceTable.logicalName}<shiro:hasPermission name="${sourceTable.moduleName}:${sourceTable.entityClassName?uncap_first}:edit">${f.dollar}{not empty ${sourceTable.entityClassName?uncap_first}.id?'修改':'添加'}</shiro:hasPermission><shiro:lacksPermission name="${sourceTable.moduleName}:${sourceTable.entityClassName?uncap_first}:edit">查看</shiro:lacksPermission></a></li>
	</ul><br/>
	<form:form id="inputForm" modelAttribute="${sourceTable.entityClassName?uncap_first}" action="${f.dollar}{ctx}/${sourceTable.moduleName}/${sourceTable.entityClassName?uncap_first}/save" method="post" class="form-horizontal">
		<form:hidden path="id"/>
		<#list columnList as column>
			<#if (column.sourceRelationList?size==0)>
				<#if column.javaClass == 'Integer'>
			<div class="control-group">
				<label class="control-label">${column.logicalName}：</label>
				<div class="controls">
					<form:input path="${column.instance?uncap_first}" htmlEscape="false" class="number"/>
				</div>
			</div>
				<#elseif column.javaClass == 'Date'>
			<div class="control-group">
				<label class="control-label">${column.logicalName}:</label>
				<div class="controls">
					<input id="${column.instance?uncap_first}" name="${column.instance?uncap_first}" type="text" readonly="readonly" maxlength="20"  class="Wdate"   value="<fmt:formatDate value="${f.dollar}}{${sourceTable.entityClassName?uncap_first}.${column.instance?uncap_first}}" pattern="yyyy-MM-dd"/>" onclick="WdatePicker({dateFmt:'yyyy-MM-dd'});"/>
				</div>
			</div>
				<#else>
			<div class="control-group">
				<label class="control-label">${column.logicalName}：</label>
				<div class="controls">
					<form:input path="${column.instance?uncap_first}" htmlEscape="false"/>
				</div>
			</div>
				</#if>
			</#if>
		</#list>
		<#if sourceTable.entityExtendType =='DataEntity'>
		<div class="control-group">
			<label class="control-label">备注：</label>
			<div class="controls">
				<form:textarea path="remarks" htmlEscape="false"/>
			</div>
		</div>
		</#if>
		<tags:message content="${f.dollar}{message}"/>
		<div class="form-actions">
			<shiro:hasPermission name="${sourceTable.moduleName}:${sourceTable.entityClassName?uncap_first}:edit"><input id="btnSubmit" class="btn btn-primary" type="submit" value="保 存"/>&nbsp;</shiro:hasPermission>
			<input id="btnCancel" class="btn" type="button" value="返 回" onclick="history.go(-1)"/>
		</div>
	</form:form>
</body>
</html>