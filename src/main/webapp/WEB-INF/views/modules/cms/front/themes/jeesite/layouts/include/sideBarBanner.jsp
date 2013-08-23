<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/modules/cms/front/include/taglib.jsp" %>
<!DOCTYPE html>
<html>
<head>
    <title>SideBar</title>
</head>
<body>
<c:forEach items="${fnc:getArticleList(site.id, 59, 1, 'image:1')}" var="article" varStatus="status">
    <div class="center"><a href="${article.url}" target="_blank"><img title="${article.title}" src="${article.imageSrc}" alt="${article.title}" border="0"></a></div>
</c:forEach>
</body>
</html>