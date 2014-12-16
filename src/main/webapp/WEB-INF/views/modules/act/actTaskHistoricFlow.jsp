<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<table class="table table-striped table-bordered table-condensed">
	<tr><th>执行环节</th><th>执行人</th><th>开始时间</th><th>结束时间</th><th>提交意见</th><th>任务历时</th></tr>
	<c:forEach items="${histoicFlowList}" var="act">
		<tr>
			<td>${act.histIns.activityName}</td>
			<td>${act.assigneeName}</td>
			<td><fmt:formatDate value="${act.histIns.startTime}" type="both"/></td>
			<td><fmt:formatDate value="${act.histIns.endTime}" type="both"/></td>
			<td style="word-wrap:break-word;word-break:break-all;">${act.comment}</td>
			<td>${act.durationTime}</td>
		</tr>
	</c:forEach>
</table>