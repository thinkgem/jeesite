<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/modules/cms/front/include/taglib.jsp" %>
<!DOCTYPE html>
<html>
<head>
    <title>SideSecondList</title>
</head>
<body>
<div class="sideBarList">
    <h3>${category.name}分类列表</h3>
    <ul class="txtList">
        <c:forEach items="${fnc:getSecondCategoryList(site.id, category, -1, '')}" var="category">
            <li><a href="${category.url}">${category.name}</a></li>
        </c:forEach>
    </ul>
</div>
</body>
</html>