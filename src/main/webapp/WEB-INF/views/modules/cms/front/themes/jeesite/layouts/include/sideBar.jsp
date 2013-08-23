<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/modules/cms/front/include/taglib.jsp" %>
<!DOCTYPE html>
<html>
<head>
    <title>SideBar</title>
</head>
<body>
<!-- sideBar S-->
<div class="sideBar">
    <page:applyDecorator name="panel" page="layouts/include/sideBarBanner.jsp"/>
    <div class="blank"></div>
    <!-- sideMenu -->
    <div class="sideMenu">
        <ul id="siderTreeNav">
            <li id="siderBgTop"></li>
            <li id="Tr04" class="FirNav"><span>公司信息</span></li>
            <li class="SubNav" style="display:block">
                <a href="" title="公司介绍">公司介绍</a>
                <a href="${ctx}/list-42${urlSuffix}" title="公司动态">公司动态</a>
                <a href="" title="公司环境">公司环境</a>
                <a href="/PowerEasy/213.html" title="发展历程">发展历程</a>
                <a href="/PowerEasy/1180.html" title="组织架构">组织架构</a>
                <a href="/PowerEasy/1181.html" title="企业公民">企业公民</a>
                <a href="" title="相关证书">相关证书</a>
                <a href="" title="大客户" class="Last">大客户</a></li>

            <li id="Tr06" class="FirNav"><span>企业文化</span></li>
            <li class="SubNav">
                <a href="/PowerEasy/1186.html" title="企业文化">企业文化</a>
                <a href="" title="员工活动">员工活动</a>
                <a href="/PowerEasy/630.html" title="网站地图">网站地图</a>
                <a href="/Activity/Anniversary3/" title="三周年纪念专题" target="_blank">三周年纪念专题</a>
                <a href="/Activity/Anniversary4/" title="四周年纪念专题" target="_blank">四周年纪念专题</a>
                <a href="/Activity/Anniversary5/" title="五周年纪念专题" target="_blank" class="Last">五周年纪念专题</a></li>


            <li id="Tr08" class="FirNav"><span>法律声明</span></li>
            <li class="SubNav">
                <a href="/PowerEasy/118.html" title="版权声明">版权声明</a>
                <a href="/PowerEasy/1188.html" title="商标声明">商标声明</a>
                <a href="/PowerEasy/1189.html" title="隐私权声明">隐私权声明</a>
                <a href="/PowerEasy/1191.html" title="相关法律" class="Last">相关法律</a></li>


            <li id="Tr10" class="FirNav"><span>动易博客</span></li>
            <li class="SubNav">
                <a href="" title="产品报道">产品报道</a>
                <a href="" title="专家坐堂">专家坐堂</a>
                <a href="" title="生活偶拾">生活偶拾</a>
                <a href="" title="探究与思考">探究与思考</a>
                <a href="" title="技术交流" class="Last">技术交流</a></li>
            <li id="Tr11" class="FirNav"><span>我与动易的故事</span></li>
            <li class="SubNav">
                <a href="" title="我与动易的故事">我与动易的故事</a>
                <a href="http://bbs.powereasy.net/forum65/list-1.aspx" target="_blank" title="说说我的故事" class="Last">说说我的故事</a>
            </li>
            <li id="Tr12" class="FirNav"><span>合作伙伴</span></li>
            <li class="SubNav">
                <a href="" title="合作伙伴">合作伙伴</a>
                <a href="" title="友情链接" class="Last">友情链接</a>
            </li>
            <li id="Tr13" class="FirNav"><span>联系方式</span></li>
            <li class="SubNav">
                <a href="" title="与我们联系">与我们联系</a>
                <a href="" title="汇款方式">汇款方式</a>
            </li>
            <li id="siderBgBot"></li>
        </ul>
        <script language="javascript" type="text/javascript">
            jQuery("#siderTreeNav").switchTab({defaultIndex: ${empty viewConfig_currentId ? 0 : viewConfig_currentId}, titCell: "li.FirNav", mainCell: "li.SubNav", effect: "fade", titOnClassName: "current", trigger: "click", delayTime: 350});
        </script>
    </div>
</div>
<script type="text/javascript" language="javascript">
    HeightFix(".sideBar", ".mainContent");
</script>
<!-- sideBar E-->
</body>
</html>