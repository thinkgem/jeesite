<%@ tag language="java" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<%@ attribute name="content" type="java.lang.String" required="true" description="消息内容"%>
<%@ attribute name="type" type="java.lang.String" description="消息类型：info、success、warning、error、loading"%>
<c:if test="${not empty content}">
	<c:if test="${not empty type}"><c:set var="ctype" value="${type}"/></c:if><c:if test="${empty type}"><c:set var="ctype" value="${fn:indexOf(content,'失败') eq -1?'success':'error'}"/></c:if>
	<c:if test="${ctype eq 'error'}">
		<p style="color:red;">${content}</p>
	</c:if>
	<c:if test="${ctype ne 'error'}">
		<p style="color:#468847;">${content}</p>
	</c:if>
</c:if>
<div id="messageBox" class="hide" style="color:red;"></div> 