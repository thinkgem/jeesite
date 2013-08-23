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
        <page:applyDecorator name="panel" page="layouts/include/slideBanner.jsp"/>

        <div class="mainIntroCon">
            <div class="introPath"><a href="${ctx}">${site.name}</a>
                <c:forEach items="${fnc:getCategoryListByIds(category.parentIds)}" var="category">
                    <c:if test="${category.id ne 1}"> » <a href="${category.url}">${category.name}</a></c:if>
                </c:forEach> » <a href="${category.url}">${category.name}</a> » 正文内容
            </div>
            <div class="contArticle_all">
                <!-- 标题 -->
                <h2 class="tit">${article.title}</h2>

                <div class="contArticle_author">
                    <span>发布时间：<fmt:formatDate value="${article.createDate}" pattern="yyyy年MM月dd日"/></span><span><a href="#">1</a>/<a href="#">2</a></span><span class="hits">${article.hits}</span>
                </div>
                <!-- 正文 -->
                <div class="contArticle_text">
                    <div class="" id="fontzoom">${article.articleData.content}</div>
                </div>
                <div class="page">
                    <span id="pe100_page_contentpage" class="pagecss"></span>
                </div>
                <div class="contArticle_bot_text">
                    <span class="contArticle_bot_text_UpdateTime">
                        [<a href="#">收藏</a>]
                        [<a href="#">打印文章</a>]
                    </span>
                    <span>作者：${article.createBy.name}</span>
                    <span>来源：${article.articleData.copyfrom}</span>
                </div>
                <div class="contArticle_bot_page">
                    <div class="cA_b_prew"></div>
                    <div class="cA_b_next"></div>
                </div>
                <div id="commentform"></div>
            </div>
        </div>
    </div>
    <!-- mainContent E-->
    <page:applyDecorator name="panel" page="layouts/include/sideBar.jsp">
        <page:param name="currentId">1</page:param>
    </page:applyDecorator>
</div>
<div class="clearbox blank"></div>
</body>
</html>