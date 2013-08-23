<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/modules/cms/front/include/taglib.jsp" %>
<!DOCTYPE html>
<html>
<head>
    <title>SlideBanner</title>
</head>
<body>
<!-- slideBanner S -->
<div class="comBanner comBannerA">
    <div class="hd">
        <ul><li>1</li><li>2</li><li>3</li><li>4</li><li>5</li><li>6</li><li>7</li></ul>
    </div>
    <div class="bd">
        <ul>
            <c:forEach items="${fnc:getArticleList(site.id, 42, 7, 'image:1')}" var="article" varStatus="status">
                <li><a href="${article.url}" target="_blank"><img title="${article.title}" src="${article.imageSrc}" alt="${article.title}" border="0"></a></li>
            </c:forEach>
        </ul>
    </div>
</div>
<script language="javascript" type="text/javascript">
    jQuery(".comBanner").slide({ mainCell: ".bd ul", autoPlay: true, interTime: 6000, delayTime: 1200});
</script>
<!-- slideBanner E -->
</body>
</html>