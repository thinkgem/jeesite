<%@ tag language="java" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/modules/cms/front/include/taglib.jsp"%>
<%@ attribute name="categoryList" type="java.util.List" required="true" description="栏目列表"%>
<c:forEach items="${categoryList}" var="category">
  	<%--<c:if test="${category.inList eq '1'}"> --%>
   		<c:choose>
   			<c:when test="${not empty category.href}">
    			<c:choose>
	    			<c:when test="${fn:indexOf(category.href, '://') eq -1 && fn:indexOf(category.href, 'mailto:') eq -1}">
	    			<c:set var="url" value="${ctx}${category.href}"/></c:when><c:otherwise><c:set var="url" value="${category.href}"/></c:otherwise>
	    		</c:choose>
   			</c:when>
   			<c:otherwise><c:set var="url" value="${ctx}/list-${category.id}${urlSuffix}"/></c:otherwise>
   		</c:choose>
		<li>
			<c:choose><c:when test="${fn:length(category.name) gt 12}">
				<a href="${url}" target="${category.target}" style="line-height:16px;padding-top:3px;">${fn:substring(category.name,0,8)}<br/>${fn:substring(category.name,8,18)}</a>
			</c:when><c:otherwise>
				<a href="${url}" target="${category.target}" ${fn:length(category.name) gt 10?'style="font-size:12px;"':''}>${category.name}</a>
			</c:otherwise></c:choose></li>
	<%--</c:if> --%>
</c:forEach>