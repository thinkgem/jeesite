<%@ tag language="java" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/modules/cms/front/include/taglib.jsp"%>
<%@ attribute name="category" type="com.thinkgem.jeesite.modules.cms.entity.Category" required="true" description="栏目对象"%>
<strong>当前位置：</strong><a href="${ctx}/index-${site.id}${urlSuffix}">首页</a><c:forEach items="${fnc:getCategoryListByIds(category.parentIds)}" var="category">
	<c:if test="${category.id ne 1}"> &gt;&gt; <a href="${ctx}/list-${category.id}${urlSuffix}">${category.name}</a></c:if>
</c:forEach> &gt;&gt; <a href="${ctx}/list-${category.id}${urlSuffix}">${category.name}</a>