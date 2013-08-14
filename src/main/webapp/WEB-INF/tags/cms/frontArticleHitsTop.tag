<%@ tag language="java" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/modules/cms/front/include/taglib.jsp"%>
<%@ attribute name="category" type="com.thinkgem.jeesite.modules.cms.entity.Category" required="true" description="栏目对象"%>
<%@ attribute name="pageSize" type="java.lang.Integer" required="false" description="页面大小"%>
<%@ attribute name="cssStyle" type="java.lang.String" required="false" description="css样式"%>
<ul class="pt" style="${cssStyle}"><c:forEach items="${fnc:getArticleList(category.site.id, category.id, not empty pageSize?pageSize:8, 'orderBy: \"hits desc\"')}" var="article">
	<li><span>[<fmt:formatDate value="${article.updateDate}" pattern="MM-dd"/>]</span><i class="dot"></i><a href="${ctx}/view-${article.category.id}-${article.id}${urlSuffix}" style="color:${article.color}" title="${article.title}">${fns:abbr(article.title,16)}</a></li>
</c:forEach></ul>