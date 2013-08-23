<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/modules/cms/front/include/taglib.jsp" %>
<!DOCTYPE html>
<html>
<head>
    <title>${category.name}</title>
    <meta name="decorator" content="cms_default_${site.theme}"/>
    <meta name="description" content="${category.description}"/>
    <meta name="keywords" content="${category.keywords}"/>
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

            <div class="contArticle_all">
                <div class="introSpace">${viewConfig_description}</div>
                <!-- 栏目循环列表开始 -->
                <div id="areaList">
                    <c:forEach items="${categoryMap}" var="map" varStatus="status">
                        <!--开始-->
                        <div class='area ${status.index % 2 eq 0 ? 'left':'right'}'>
                            <h3><span><a href="${map.key.url}" target="_blank">${map.key.name} </a></span></h3>
                            <ul>
                                <c:if test="${map.key.module eq 'article'}">
                                    <c:forEach items="${map.value}" var="article">
                                        <li><a href="${article.url}" target="_blank">${fns:abbr(article.title,25)}</a>
                                        </li>
                                    </c:forEach>
                                </c:if>
                                <c:if test="${map.key.module eq 'link'}">
                                    <c:forEach items="${map.value}" var="link">
                                        <li><a href="${link.href}" target="_blank"><c:out value="${link.title}"/></a>
                                        </li>
                                    </c:forEach>
                                </c:if>
                            </ul>
                            <div class="underline_area"></div>
                        </div>
                        <!--模块一结束-->
                    </c:forEach>

                </div>
                <!-- 栏目循环列表结束 -->
            </div>
        </div>
    </div>
    <!-- mainContent E-->

    <!-- sideBar S-->
    <div class="sideBar">
        <page:applyDecorator name="panel" page="layouts/include/sideBarBanner.jsp"/>
        <div class="sideBarList">
            <h3>${category.name}分类列表</h3>
            <ul class="txtList">
                <c:forEach items="${categoryMap}" var="map">
                    <li><a href="${map.key.url}">${map.key.name}</a></li>
                </c:forEach>
            </ul>
        </div>
    </div>
    <script type="text/javascript" language="javascript">
        HeightFix(".sideBar", ".mainContent", "0");
    </script>
    <!-- sideBar E-->
</div>
<div class="clearbox blank"></div>
</body>
</html>