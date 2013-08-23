<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/modules/cms/front/include/taglib.jsp" %>
<!DOCTYPE html>
<html>
<head>
    <title>${site.title} - 领先的内容管理系统(CMS)、企业级电子商务平台（B2C）和站群管理系统（SiteGroup）提供商</title>
    <meta name="decorator" content="cms_default_${site.theme}"/>
    <meta name="description" content="JeeSite ${site.description}"/>
    <meta name="keywords" content="JeeSite ${site.keywords}"/>
    <link href="${ctxStaticTheme}/PE-index.css" rel="stylesheet" type="text/css"/>
    <link href="${ctxStaticTheme}/nivo-slider.css" rel="stylesheet" type="text/css"/>

</head>
<body>
<!-- slideBanner S -->
<div id="slideBanner" class="indBannerbg">
    <div id="BannerBG"></div>
    <div class="sidePic">
        <c:forEach items="${fnc:getArticleList(site.id, 40, 2, 'image:1')}" var="article" varStatus="status">
            <a href="${article.url}" target="_blank"><img title="${article.title}" src="${article.imageSrc}" alt="${article.title}"></a>
            <c:if test="${status.index lt 1}"><div class="blank"></div></c:if>
        </c:forEach>
    </div>
    <script type="text/javascript" src="${ctxStaticTheme}/jquery.nivo.slider.pack.js"></script>
    <div id="indBanner">
        <c:forEach items="${fnc:getArticleList(site.id, 39, 10, 'image:1')}" var="article">
            <a href="${article.url}" target="_blank" class="pic"><img title="${article.title}" src="${article.imageSrc}" alt="${article.title}"></a>
        </c:forEach>
    </div>
    <script type="text/javascript">
        $(document).ready(function() {
            $('#indBanner').nivoSlider({
                beforeChange: function () {
                    //$('#slideBanner').attr("class", "indBannerbg" + jQuery(".nivo-controlNav a.active").attr("rel"));
                }
            });
        });
    </script>
</div>
<!-- slideBanner E -->
<div class="clearbox blank"></div>
<div class="Home">
<!--Signing S-->
<div class="homeSigning">
    <ul class="ul2">
        <!-- 签约 -->
        <li class="ann">
            <div class="annCon" id="annCon">
                <div class="annConIn">
                    <ul class="">
                        <c:forEach items="${fnc:getArticleList(site.id, 41, 5, '')}" var="article">
                            <li>
                                <a href="${article.url}" target="_blank"
                                   title="标题：${article.title}&#xA;点击数：${article.hits}&#xA;发表时间：<fmt:formatDate value="${article.updateDate}" pattern="yy年MM月dd日"/>">${article.title}</a>
                            </li>
                        </c:forEach>
                    </ul>
                </div>
            </div>
            <script type="text/javascript">
                jQuery(function ($) {
                    jQuery("#annCon ul").scrollUp();
                });
            </script>
        </li>
        <!-- 统计 -->
        <li class="annStati">
            全球已有 <strong>115,858,745</strong> 家网站使用<a href="/Products/">动易产品</a>搭建
        </li>
    </ul>
</div>
<!--Signing E-->
<!--三列内容 S-->
<div class="homeConbox">
    <!--News S-->
    <div id="indNews">
        <h3>公司动态</h3>

        <div class="box slideTxtBox">
            <div class="hd">
                <ul>
                    <li>企业</li>
                    <li>媒体</li>
                    <li>项目签约</li>
                </ul>
            </div>
            <div class="bd">
                <ul class="infoList">
                    <c:forEach items="${fnc:getArticleList(site.id, 42, 4, '')}" var="article">
                        <li>
                            <span class="datetime"><fmt:formatDate value="${article.updateDate}" pattern="yyyy-MM-dd"/></span>
                            <a href="${article.url}" target="_blank"
                               title="标题：${article.title} 发表时间：<fmt:formatDate value="${article.updateDate}" pattern="yy年MM月dd日"/>">${fns:abbr(article.title,56)}</a>
                        </li>
                    </c:forEach>

                    <a href="${ctx}/list-42${urlSuffix}" class="more"> 查看所有资讯 »</a>

                    <div class="clearbox"></div>
                </ul>
                <ul class="infoList">
                    <c:forEach items="${fnc:getArticleList(site.id, 43, 4, '')}" var="article">
                        <li>
                            <span class="datetime"><fmt:formatDate value="${article.updateDate}" pattern="yyyy-MM-dd"/></span>
                            <a href="${article.url}" target="_blank"
                               title="标题：${article.title} 发表时间：<fmt:formatDate value="${article.updateDate}" pattern="yy年MM月dd日"/>">${fns:abbr(article.title,56)}</a>
                        </li>
                    </c:forEach>

                    <a href="${ctx}/list-43${urlSuffix}" class="more"> 查看所有资讯 »</a>
                </ul>
                <ul class="infoList">
                    <c:forEach items="${fnc:getArticleList(site.id, 41, 4, '')}" var="article">
                        <li>
                            <span class="datetime"><fmt:formatDate value="${article.updateDate}" pattern="yyyy-MM-dd"/></span>
                            <a href="${article.url}" target="_blank"
                               title="标题：${article.title} 发表时间：<fmt:formatDate value="${article.updateDate}" pattern="yy年MM月dd日"/>">${fns:abbr(article.title,56)}</a>
                        </li>
                    </c:forEach>

                    <a href="${ctx}/list-41${urlSuffix}" class="more"> 查看所有资讯 »</a>
                </ul>
            </div>
            <script type="text/javascript">
                jQuery("#indNews").slide({effect: "left"});
            </script>
        </div>
    </div>
    <!--News E-->
    <!--Case S-->
    <div id="indCase">
        <h3>优秀案例</h3>

        <div class="box slideTxtBox">
            <div class="hd">
                <ul>
                    <li>政府</li>
                    <li>教育</li>
                    <li>医院</li>
                    <li>企业</li>
                    <li>媒体</li>
                </ul>
            </div>
            <div class="bd">
                <ul class="">
                    <c:set var="category" value="${fnc:getCategory(44)}"/>
                    <li class="intro">${category.description}</li>
                    <li class="mingDan">
                        <c:forEach items="${fnc:getArticleList(site.id, category.id, 12, '')}" var="article" varStatus="status">
                            ${article.title}
                            <c:if test="${status.index lt 11}">&nbsp;/&nbsp;</c:if>
                        </c:forEach>
                    </li>
                    <a href="${category.url}" class="more"> 查看所有案例 »</a>

                    <div class="clearbox"></div>
                    <c:remove var="category" />
                </ul>
                <ul class="">
                    <c:set var="category" value="${fnc:getCategory(45)}"/>
                    <li class="intro">${category.description}</li>
                    <li class="mingDan">
                        <c:forEach items="${fnc:getArticleList(site.id, category.id, 12, '')}" var="article" varStatus="status">
                            ${article.title}
                            <c:if test="${status.index lt 11}">&nbsp;/&nbsp;</c:if>
                        </c:forEach>
                    </li>
                    <a href="${category.url}" class="more"> 查看所有案例 »</a>

                    <div class="clearbox"></div>
                    <c:remove var="category" />
                </ul>
                <ul class="">
                    <c:set var="category" value="${fnc:getCategory(49)}"/>
                    <li class="intro">${category.description}</li>
                    <li class="mingDan">
                        <c:forEach items="${fnc:getArticleList(site.id, category.id, 12, '')}" var="article" varStatus="status">
                            ${article.title}
                            <c:if test="${status.index lt 11}">&nbsp;/&nbsp;</c:if>
                        </c:forEach>
                    </li>
                    <a href="${category.url}" class="more"> 查看所有案例 »</a>

                    <div class="clearbox"></div>
                    <c:remove var="category" />
                </ul>
                <ul class="">
                    <c:set var="category" value="${fnc:getCategory(47)}"/>
                    <li class="intro">${category.description}</li>
                    <li class="mingDan">
                        <c:forEach items="${fnc:getArticleList(site.id, category.id, 12, '')}" var="article" varStatus="status">
                            ${article.title}
                            <c:if test="${status.index lt 11}">&nbsp;/&nbsp;</c:if>
                        </c:forEach>
                    </li>
                    <a href="${category.url}" class="more"> 查看所有案例 »</a>

                    <div class="clearbox"></div>
                    <c:remove var="category" />
                </ul>
                <ul class="">
                    <c:set var="category" value="${fnc:getCategory(46)}"/>
                    <li class="intro">${category.description}</li>
                    <li class="mingDan">
                        <c:forEach items="${fnc:getArticleList(site.id, category.id, 12, '')}" var="article" varStatus="status">
                            ${article.title}
                            <c:if test="${status.index lt 11}">&nbsp;/&nbsp;</c:if>
                        </c:forEach>
                    </li>
                    <a href="${category.url}" class="more"> 查看所有案例 »</a>

                    <div class="clearbox"></div>
                    <c:remove var="category" />
                </ul>
            </div>
            <script type="text/javascript">
                jQuery("#indCase").slide({effect: "left"});
            </script>
        </div>
    </div>
    <!--Case E-->
    <!--Weibo S-->
    <div id="indWeibo">
        <div class="box slideTxtBox">
            <div class="hd">
                <ul>
                    <li class="indWeiboTit">我们在聆听</li>
                    <li>官方博客</li>
                </ul>
            </div>
            <link rel="stylesheet" type="text/css" href="http://tjs.sjs.sinajs.cn/t3/style/css/common/card.css">
            <div class="bd">
                <ul class="weiboAtt">
                    <li class="sina">
                        <h4>动易官方<span>新浪微博</span></h4>

                        <p>http://weibo.com/powereasy</p>
                               <span id="wb_follow_btn1">
                               <a class="weibo_widget_btn" href="http://weibo.com/powereasy" target="_blank">
                                   <em>加关注</em>
                               </a>
                               </span>
                    </li>
                    <li class="qq">
                        <h4>动易官方<span>腾讯微博</span></h4>

                        <p>http://t.qq.com/powereasy</p>
                               <span id="wb_follow_btn2">
                               <a class="weibo_widget_btn" href="http://t.qq.com/powereasy" target="_blank">
                                   <em>加关注</em>
                               </a>
                               </span>
                    </li>
                </ul>
                <ul class="blogList">
                    <c:forEach items="${fnc:getArticleList(site.id, 53, 3, '')}" var="article">
                        <li>
                            <span class="datetime"><fmt:formatDate value="${article.updateDate}" pattern="yyyy-MM-dd"/></span>
                            <a href="${article.url}" target="_blank"
                               title="标题：${article.title}&#xA;点击数：${article.hits}&#xA;发表时间：<fmt:formatDate value="${article.updateDate}" pattern="yy年MM月dd日"/>">${article.title}</a>
                        </li>
                    </c:forEach>
                </ul>
            </div>
            <script type="text/javascript">
                jQuery("#indWeibo").slide({effect: "left"});
            </script>
        </div>
    </div>
    <!--Weibo E-->
</div>
<!--三列内容 E-->
</div>
<div class="clearbox blank"></div>

</body>
</html>