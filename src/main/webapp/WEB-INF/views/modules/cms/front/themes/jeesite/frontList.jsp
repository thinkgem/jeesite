<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/modules/cms/front/include/taglib.jsp"%>
<!DOCTYPE html>
<html>
<head>
    <title>${category.name}</title>
    <meta name="decorator" content="cms_default_${site.theme}"/>
    <meta name="description" content="${category.description}" />
    <meta name="keywords" content="${category.keywords}" />
    <link href="${ctxStaticTheme}/PE-intropage.css" rel="stylesheet" type="text/css"/>
</head>
<body>
<div class="clearbox blank"></div>

<div id="content" class="introContent">

    <!-- mainContent S-->
    <div class="mainContent">
        <page:applyDecorator name="panel" page="layouts/include/slideBanner.jsp"/>

        <div class="mainIntroCon">
            <h2 class="introPath"><span><a>${category.name}</a></span></h2>
            <div id="articleList">
                <ul class="txtList">
                    <c:if test="${category.module eq 'article'}">
                        <c:forEach items="${page.list}" var="article">
                            <li>
                                <span class="datetime"><fmt:formatDate value="${article.updateDate}" pattern="yyyy-MM-dd"/></span>
                                <a href="${article.url}" target="_blank" title="标题：${article.title}&#xA;点击数：${article.hits}&#xA;发表时间：<fmt:formatDate value="${article.updateDate}" pattern="yy年MM月dd日"/>" style="color:${article.color}">${fns:abbr(article.title,96)}</a>
                            </li>
                        </c:forEach>
                    </c:if>
                    <c:if test="${category.module eq 'link'}">
                        <c:forEach items="${map.value}" var="link">
                            <li><a href="${link.href}" target="_blank"><c:out value="${link.title}"/></a>
                            </li>
                        </c:forEach>
                    </c:if>
                </ul>
            </div>
            <div class="page">${page}</div>
            <script type="text/javascript">
                function page(n,s){
                    location="${category.url}?pageNo="+n+"&pageSize="+s;
                }
            </script>
        </div>
    </div>
    <!-- mainContent E-->
    <page:applyDecorator name="panel" page="layouts/include/sideBar.jsp" />
</div>
<div class="clearbox blank"></div>
</body>
</html>