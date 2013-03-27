<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<c:set var="ctx" value="${pageContext.request.contextPath}${fns:getFrontPath()}"/>
<c:set var="ctxStatic" value="${pageContext.request.contextPath}/static"/>
<c:set var="ctxStaticFront" value="${ctxStatic}/modules/cms/front"/>
<c:set var="ctxStaticTheme" value="${ctxStaticFront}/themes/${site.theme}"/>
<c:set var="urlSuffix" value="${fns:getUrlSuffix()}"/>