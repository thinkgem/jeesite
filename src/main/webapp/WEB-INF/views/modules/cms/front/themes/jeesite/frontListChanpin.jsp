<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/modules/cms/front/include/taglib.jsp"%>
<!DOCTYPE html>
<html>
<head>
    <title>${category.name}</title>
    <meta name="decorator" content="cms_default_${site.theme}"/>
    <meta name="description" content="${category.description}" />
    <meta name="keywords" content="${category.keywords}" />
    <link href="${ctxStaticTheme}/PE-propage.css" rel="stylesheet" type="text/css"/>
    <link href="${ctxStaticTheme}/nivo-slider.css" rel="stylesheet" type="text/css"/>
</head>
<body>
<div class="clearbox blank"></div>
<div class="commonBanner">
    <page:applyDecorator name="panel" page="layouts/include/slideBanner.jsp"/>
    <page:applyDecorator name="panel" page="layouts/include/sideBarBanner.jsp"/>
</div>
<div id="content" class="proHome">
    <!-- mainContent S-->
    <h2>Products</h2>
    <div class="proHomeGuide">
        <dl>
            <dt>引导词：</dt>
            <dd>
                动易秉持"<span>严谨务实 精益求精</span>"的产品研发理念，致力于为用户提供最先进的产品和技术，始终围绕网站建设及管理进行持续的技术创新和产品研发，迄今已独立研发众多拥有自主知识产权的互联网著名软件产品。截止至2012年9月份，使用动易系列产品的<span>用户超过82万</span>，是<span>国内最大</span>的网站管理软件及服务提供商。
            </dd>
        </dl>
    </div>

    <div class="proHomeList">
        <dl class="proA">
            <dt>政府机关</dt>
            <dd>    如果您需要构建市/区县级政府门户网站、厅局级政府网站、政府网站群、政府信息公开平台、公众互动平台、在线办事平台、公共服务平台等政府信息化平台，</dd>
            <dd class="proListcon">
                <h4>我们推荐：</h4>
                <ul>
                    <li><a href="/Products/SmartGov/" target="_blank">SmartGov 政府网站管理系统</a></li>
                    <li><a href="/Products/engineering-project-information-public-and-credit-system.html" target="_blank">工程建设领域信息公开与信用建设平台</a></li>
                    <li><a href="/Products/Politics-by-internet.html" target="_blank">网络问政平台</a></li>
                    <li><a href="/Products/SiteGroup/" target="_blank">SiteGroup 站群管理系统</a></li>
                    <li><a href="/Products/SmartFileManager.html" target="_blank">文件服务器管理系统</a></li>
                    <li><a href="/Products/SiteFactory/" target="_blank">SiteFactory 内容管理系统</a></li>
                    <li><a href="/Products/Industry.html" target="_blank">行业门户网站管理系统</a></li>
                    <li><a href="/Products/SmartWeibo.html" target="_blank">微博管理系统</a></li>
                    <li><a href="/Products/WAPCMS.html" target="_blank">WAP网站管理系统</a></li>
                    <li><a href="/Products/PowerEasy-full-text-retrieval-systems.html" target="_blank">全文检索系统</a></li>
                    <li><a href="/Products/PowerEasy-Blog.html" target="_blank">博客管理系统</a></li>
                </ul>
            </dd>
        </dl>

        <dl class="proB">
            <dt>教育机构</dt>
            <dd>    如果您需要构建大/中/小学数字校园网站、幼儿园网站、教育门户网站、学校校庆网站、学生学籍平台、学生成绩管理平台、教育博客平台等教育信息化平台，</dd>
            <dd class="proListcon">
                <h4>我们推荐：</h4>
                <ul>
                    <li><a href="/Products/SmartSchool/" target="_blank">学校网站管理系统</a> <a href="/Products/Xiaoqing/" target="_blank">校庆版</a></li>
                    <li><a href="/Products/SiteGroup/" target="_blank">SiteGroup 站群管理系统</a></li>
                    <li><a href="/Products/SmartIntranetPortal.html" target="_blank">协同办公与知识共享平台</a></li>
                    <li><a href="/Products/SiteFactory/" target="_blank">SiteFactory 内容管理系统</a></li>
                    <li><a href="/Products/SmartWeibo.html" target="_blank">微博管理系统</a></li>
                    <li><a href="/Products/SmartFileManager.html" target="_blank">文件服务器管理系统</a></li>
                    <li><a href="/Products/jobs.html" target="_blank">人才招聘管理系统</a></li>
                    <li><a href="/Products/WAPCMS.html" target="_blank">WAP网站管理系统</a></li>
                    <li><a href="/Products/PowerEasy-full-text-retrieval-systems.html" target="_blank">全文检索系统</a></li>
                    <li><a href="/Products/PowerEasy-Blog.html" target="_blank">博客管理系统</a></li>
                    <li><a href="/Products/SmartDocumentShare.html" target="_blank">文档分享平台</a></li>
                </ul>
            </dd>
        </dl>

        <dl class="proC">
            <dt>B2C电子商务</dt>
            <dd>    如果您需要构建大型企业一体化电子商务平台（制造业、零售业等传统企业）、中小型企业电子商务服务网站（饰品类、化妆品类、网上花店、服装网络直销平台、网上书店、网上手机专卖店、网上数码设备店等），</dd>
            <dd class="proListcon">
                <h4>我们推荐：</h4>
                <ul>
                    <li><a href="/Products/BizIdea/" target="_blank">企业电子商务管理系统</a></li>
                    <li><a href="/Products/BizAssistant/" target="_blank">商务助理</a></li>
                    <li><a href="/Products/SiteGroup/" target="_blank">SiteGroup 站群管理系统</a></li>
                    <li><a href="http://sms.powereasy.net/" target="_blank">短信通平台</a></li>
                    <li><a href="/Products/SmartFileManager.html" target="_blank">文件服务器管理系统</a></li>
                    <li><a href="/Products/jobs.html" target="_blank">人才招聘管理系统</a></li>
                    <li><a href="/Products/Customer-Service.html" target="_blank">客服中心管理系统</a></li>
                    <li><a href="/Products/WAPCMS.html" target="_blank">WAP网站管理系统</a></li>
                    <li><a href="/Products/PowerEasy-full-text-retrieval-systems.html" target="_blank">全文检索系统</a></li>
                    <li><a href="/Products/PowerEasy-Blog.html" target="_blank">博客管理系统</a></li>
                </ul>
            </dd>
        </dl>

        <dl class="proD">
            <dt>医院机构</dt>
            <dd>    如果您需要构建医院网站信息化平台、预约挂号平台、医院内部网络、医院资讯平台等医疗信息化平台，</dd>
            <dd class="proListcon">
                <h4>我们推荐：</h4>
                <ul>
                    <li><a href="/Products/HMS/" target="_blank">医院网站管理系统</a>
                    <li><a href="/Products/HMS/" target="_blank">预约挂号系统</a></li>
                    <li><a href="/Products/SmartIntranetPortal.html" target="_blank">协同办公与知识共享平台</a></li>
                    <li><a href="/Products/SmartWeibo.html" target="_blank">微博管理系统</a></li>
                    <li><a href="/Products/SmartFileManager.html" target="_blank">文件服务器管理系统</a></li>
                    <li><a href="/Products/jobs.html" target="_blank">人才招聘管理系统</a></li>
                    <li><a href="/Products/WAPCMS.html" target="_blank">WAP网站管理系统</a></li>
                    <li><a href="/Products/PowerEasy-full-text-retrieval-systems.html" target="_blank">全文检索系统</a></li>
                    <li><a href="/Products/PowerEasy-Blog.html" target="_blank">博客管理系统</a></li>
                </ul>
            </dd>
        </dl>

        <dl class="proE">
            <dt>媒体行业</dt>
            <dd>    如果您需要构建电视台媒体门户网站、报社门户网站、新闻门户网站、行业资讯平台、微博平台、电视直播平台等媒体信息化、流媒体平台，</dd>
            <dd class="proListcon">
                <h4>我们推荐：</h4>
                <ul>
                    <li><a href="/Products/shipinwangzhanguanlixitong.html" target="_blank">视频网站管理系统</a></li>
                    <li><a href="/Products/PowerEasy-Video-Player.html" target="_blank">PowerPlayback播放器</a></li>
                    <li><a href="/Products/Media-convert-server.html" target="_blank">Media Convert Server 媒体转换服务器</a></li>
                    <li><a href="/Products/Media-Live-Encoder.html" target="_blank">Media Live encoder 媒体直播编码控制台</a></li>
                    <li><a href="/Products/Industry.html" target="_blank">行业门户网站管理系统</a></li>
                    <li><a href="/Products/SmartIntranetPortal.html" target="_blank">协同办公与知识共享平台</a></li>
                    <li><a href="/Products/SmartDocumentShare.html" target="_blank">文档分享平台</a></li>
                    <li><a href="/Products/SiteGroup/" target="_blank">SiteGroup 站群管理系统</a></li>
                    <li><a href="/Products/SmartWeibo.html" target="_blank">微博管理系统</a></li>
                    <li><a href="/Products/SmartFileManager.html" target="_blank">文件服务器管理系统</a></li>
                    <li><a href="http://sms.powereasy.net/">短信通平台</a></li>
                    <li><a href="/Products/jobs.html" target="_blank">人才招聘管理系统</a></li>
                    <li><a href="/Products/WAPCMS.html" target="_blank">WAP网站管理系统</a></li>
                    <li><a href="/Products/PowerEasy-full-text-retrieval-systems.html" target="_blank">全文检索系统</a></li>
                    <li><a href="/Products/PowerEasy-Blog.html" target="_blank">博客管理系统</a></li>
                </ul>
            </dd>
        </dl>

        <dl class="proF">
            <dt>大中小企业</dt>
            <dd>    如果您需要构建企业外网门户网站、企业内网门户网站、企业展示网站、各行业网站等企业信息化平台，</dd>
            <dd class="proListcon">
                <h4>我们推荐：</h4>
                <ul>
                    <li><a href="/Products/SmartIntranetPortal.html" target="_blank">协同办公与知识共享平台</a></li>
                    <li><a href="/Products/SiteFactory/" target="_blank">SiteFactory 内容管理系统</a></li>
                    <li><a href="/Products/SiteGroup/" target="_blank">SiteGroup 站群管理系统</a></li>
                    <li><a href="/Products/SmartFileManager.html" target="_blank">文件服务器管理系统</a></li>
                    <li><a href="/Products/Industry.html" target="_blank">行业门户网站管理系统</a></li>
                    <li><a href="/Products/SmartWeibo.html" target="_blank">微博管理系统</a></li>
                    <li><a href="http://sms.powereasy.net/" target="_blank">短信通平台</a></li>
                    <li><a href="/Products/jobs.html" target="_blank">人才招聘管理系统</a></li>
                    <li><a href="/Products/WAPCMS.html" target="_blank">WAP网站管理系统</a></li>
                    <li><a href="/Products/PowerEasy-full-text-retrieval-systems.html" target="_blank">全文检索系统</a></li>
                    <li><a href="/Products/PowerEasy-Blog.html" target="_blank">博客管理系统</a></li>
                    <li><a href="/Products/SmartDocumentShare.html" target="_blank">文档分享平台</a></li>
                </ul>
            </dd>
        </dl>
    </div>
    <!-- mainContent E-->
</div>
<div><a href="/Category_1556/Index.aspx" target="_blank"><img src="${ctxStaticTheme}/images/pro_banner.jpg" border="0"/></a></div>
<div class="clearbox blank"></div>
</body>
</html>