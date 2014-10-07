<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>模板管理</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		$(document).ready(function() {
			$("#value").focus();
			$("#inputForm").validate();
			$(window).resize(function(){
				$("#source").height($(window).height()-$('body').height()+$("#source").height()-8);
			}).resize();
		});
	</script>
</head>
<body>
	<ul class="nav nav-tabs">
		<li class="active"><a>模板管理</a></li>
	</ul><br/>
	
	<form:form id="inputForm" modelAttribute="template" action="${ctx}/cms/template/save" method="post" class="form-horizontal">
        <form:hidden path="name" />
		<tags:message content="${message}"/>
		
		<div class="control-group">
			<label class="control-label" for="filename">文件名:</label>
			<div class="controls">
				<form:input path="filename" htmlEscape="false" maxlength="50" class="required"/>
			</div>
		</div>
		<div class="control-group">
            <form:textarea id="source" path="source" htmlEscape="true" cssStyle="width:100%;"/>
            <%--<tags:ckeditor replace="source" uploadPath="/cms/template" />--%>
		</div>
		<div class="form-actions hide">
			<shiro:hasPermission name="cms:template:edit">
				<input id="btnSubmit" class="btn btn-primary" type="submit" value="保 存"/>&nbsp;
			</shiro:hasPermission>
			<input id="btnCancel" class="btn" type="button" value="返 回" onclick="history.go(-1)"/>
		</div>
	</form:form>
</body>
</html>