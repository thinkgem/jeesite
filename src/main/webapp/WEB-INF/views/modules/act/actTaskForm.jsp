<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>任务表单</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		$(document).ready(function() {/*
			loading('正在加载表单，请稍等...');
			$.get("${formUrl}", function(data){
				top.$.jBox.closeTip();
				$("#formContent").html(data==""?"<h4>没有找到表单数据。</h4><br/>表单地址：${formUrl}<br/>":data);
			}).fail(function() {
				top.$.jBox.closeTip();
				$("#formContent").html("<h4>加载表单失败！</h4>表单地址：${formUrl}<br/>");
			});*/
		});
	</script>
</head>
<body>
	<ul class="nav nav-tabs">
		<li><a href="${ctx}/act/task/todo/">待办任务</a></li>
		<li><a href="${ctx}/act/task/historic/">已办任务</a></li>
		<li class="active"><a href="${ctx}/act/task/form?taskId=${act.taskId}&taskName=${act.taskName}&taskDefKey=${act.taskDefKey}&procInsId=${act.procInsId}&procDefId${act.procDefId}">${empty act.procInsId?"新建任务":"任务处理"}</a></li>
	</ul>
	<form:form method="post" class="form-horizontal">
		<sys:message content="${message}"/><%--
		<legend>表单信息</legend> --%>
		<div id="formContent">
			<iframe id="reportFrame" src="${formUrl}" width="100%" height="500" style="border:0;" noresize="noresize"></iframe>
		</div>
		<act:histoicFlow procInsId="${procInsId}" />
	</form:form>
</body>
</html>
