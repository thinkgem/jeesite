<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>请假一览</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		function page(n,s){
			$("#pageNo").val(n);
			$("#pageSize").val(s);
			$("#searchForm").submit();
        	return false;
        }
	</script>
</head>
<body>
	<ul class="nav nav-tabs">
		<li><a href="${ctx}/oa/leave/">待办任务</a></li>
		<li class="active"><a href="${ctx}/oa/leave/list">所有任务</a></li>
		<shiro:hasPermission name="oa:leave:edit"><li><a href="${ctx}/oa/leave/form">请假申请</a></li></shiro:hasPermission>
	</ul>
	<form:form id="searchForm" modelAttribute="leave" action="${ctx}/oa/leave/list" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<div>
			<label>请假编号：&nbsp;</label><form:input path="ids" htmlEscape="false" maxlength="50" class="input-medium" placeholder="多个用逗号或空格隔开"/>
		</div>
		<div style="margin-top:8px;">
			<label>创建时间：</label>
			<input id="createDateStart"  name="createDateStart"  type="text" readonly="readonly" maxlength="20" class="input-medium Wdate" style="width:163px;"
				value="<fmt:formatDate value="${leave.createDateStart}" pattern="yyyy-MM-dd"/>"
					onclick="WdatePicker({dateFmt:'yyyy-MM-dd'});"/>
				　--　
			<input id="createDateEnd" name="createDateEnd" type="text" readonly="readonly" maxlength="20" class="input-medium Wdate" style="width:163px;"
				value="<fmt:formatDate value="${leave.createDateEnd}" pattern="yyyy-MM-dd"/>"
					onclick="WdatePicker({dateFmt:'yyyy-MM-dd'});"/>
			&nbsp;<input id="btnSubmit" class="btn btn-primary" type="submit" value="查询"/>
		</div>
	</form:form>
	<sys:message content="${message}"/>
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
		<thead><tr>
			<th>请假编号</th>
			<th>创建人</th>
			<th>创建时间</th>
			<th>请假原因</th>
			<th>当前节点</th>
			<th>操作</th>
		</tr></thead>
		<tbody>
		<c:forEach items="${page.list}" var="leave">
			<c:set var="task" value="${leave.task }" />
			<c:set var="pi" value="${leave.processInstance }" />
			<c:set var="hpi" value="${leave.historicProcessInstance }" />
			<tr>
				<td>${leave.id}</td>
				<td>${leave.createBy.name}</td>
				<td><fmt:formatDate value="${leave.createDate}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
				<td>${leave.reason}</td>
				<c:if test="${not empty task}">
					<td>${task.name}</td>
					<td><a target="_blank" href="${ctx}/act/task/trace/photo/${task.processDefinitionId}/${task.executionId}">跟踪</a></td>
				</c:if>
				<c:if test="${empty task}">
					<td>已结束</td>
					<td>&nbsp;</td>
				</c:if>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>
