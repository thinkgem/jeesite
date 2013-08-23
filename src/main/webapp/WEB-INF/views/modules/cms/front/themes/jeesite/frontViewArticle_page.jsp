<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/modules/cms/front/include/taglib.jsp" %>
<!DOCTYPE html>
<html>
<head>
    <title>${fns:abbr(article.title,1000)} - ${site.name}</title>
    <meta name="decorator" content="cms_default_${site.theme}"/>
    <meta name="description" content="${article.description} ${category.description}"/>
    <meta name="keywords" content="${article.keywords} ${category.keywords}"/>
    <link href="${ctxStaticTheme}/PE-intropage.css" rel="stylesheet" type="text/css"/>
</head>
<body>
<div class="clearbox blank"></div>

<div id="content" class="introContent">

    <!-- mainContent S-->
    <div class="mainContent">
        <div class="mainIntroPath"></div>
        <page:applyDecorator name="panel" page="layouts/include/slideBanner.jsp"/>

        <div class="mainIntroCon aboutus">
            <!-- 标题 -->
            <h2><c:out value="${article.title}" escapeXml="false"/></h2>
            ${article.articleData.content}
        </div>
    </div>
    <!-- mainContent E-->
    <page:applyDecorator name="panel" page="layouts/include/sideBar.jsp"/>
</div>
<div class="clearbox blank"></div>
</body>
</html>