<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>日志管理</title>
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
		<li class="active"><a href="${ctx}/sys/log/">日志列表</a></li>
	</ul>
	<form:form id="searchForm" action="${ctx}/sys/log/" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<div>
			<label>用户ID：</label><input id="createById" name="createById" type="text" maxlength="50" class="input-small" value="${createById}"/>
			<label>URI：</label><input id="requestUri" name="requestUri" type="text" maxlength="50" class="input-small" value="${requestUri}"/>
			&nbsp;
			<label>开始日期：</label><input id="beginDate" name="beginDate" type="text" readonly="readonly" maxlength="20" class="input-small Wdate"
				value="${beginDate}" onclick="WdatePicker({dateFmt:'yyyy-MM-dd',isShowClear:false});"/>
			<label>结束日期：</label><input id="endDate" name="endDate" type="text" readonly="readonly" maxlength="20" class="input-small Wdate"
				value="${endDate}" onclick="WdatePicker({dateFmt:'yyyy-MM-dd',isShowClear:false});"/>
			<label for="exception"><input id="exception" name="exception" type="checkbox"${exception eq '1'?' checked':''} value="1"/>异常信息</label>
			&nbsp;&nbsp;<input id="btnSubmit" class="btn btn-primary" type="submit" value="查询"/>
		</div>
	</form:form>
	<tags:message content="${message}"/>
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
		<thead><tr><th>所在公司</th><th>所在部门</th><th>操作用户</th><th>URI</th><th>提交方式</th><th>操作者IP</th><th>创建时间</th></thead>
		<tbody>
		<c:forEach items="${page.list}" var="log">
			<tr>
				<td>${log.createBy.company.name}</td>
				<td>${log.createBy.office.name}</td>
				<td>${log.createBy.name}</td>
				<td><strong>${log.requestUri}</strong></td>
				<td>${log.method}</td>
				<td>${log.remoteAddr}</td>
				<td><fmt:formatDate value="${log.createDate}" type="both"/></td>
			</tr>
			<tr>
				<td colspan="8">用户代理: ${log.userAgent}<br/>提交参数: ${fns:escapeHtml(log.params)}
				<c:if test="${not empty log.exception}"><br/>异常信息: <br/><%request.setAttribute("strEnter", "\n"); %>
				${fn:replace(fns:escapeHtml(log.exception), strEnter, '<br/>')}</c:if></td>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>