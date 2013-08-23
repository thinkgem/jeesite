<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/modules/cms/front/include/taglib.jsp" %>
<%@ taglib prefix="sitemesh" uri="http://www.opensymphony.com/sitemesh/decorator" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" lang="UTF-8">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
    <title><sitemesh:title default="欢迎光临"/></title>
    <%--<script language="javascript" type="text/javascript" src="${ctxStatic}/jquery/jquery-1.9.1.min.js"></script>--%>
    <script language="javascript" type="text/javascript" src="${ctxStaticTheme}/jquery.pack.js"></script>
    <script language="javascript" type="text/javascript" src="${ctxStaticTheme}/jquery.peex.js"></script>
    <script language="javascript" type="text/javascript" src="${ctxStaticTheme}/slide.js"></script>
    <script language="javascript" type="text/javascript" src="${ctxStaticTheme}/js.js"></script>
    <link href="${ctxStaticTheme}/PE-module.css" rel="stylesheet" type="text/css"/>
    <link href="${ctxStaticTheme}/PE-default.css" rel="stylesheet" type="text/css"/>
    <sitemesh:head/>
</head>
<body>
<div class="topNavi">
    <ul>
        <li class="title">动易旗下网站</li>
        <li class="on"><a href="/" title="官方主站"><span class="t1"></span>官方主站</a></li>
        <li><a href="http://tech.powereasy.net/" target="_blank" title="访问动易技术中心获取动易产品相关知识"><span class="t2"></span>技术中心</a></li>
        <li><a href="http://qihang.powereasy.net/" target="_blank" title="访问动易公司官方培训网站"><span class="t3"></span>动易起航</a></li>
        <li><a href="http://sms.powereasy.net/" target="_blank" title="欢迎进入动易短信通网站"><span class="t4"></span>动易短信通</a></li>
        <li><a href="http://moban.powereasy.net/" target="_blank" title="获取动易软件产品的模板资源"><span class="t5"></span>模板中心</a>
        </li>
        <li><a href="http://demo.powereasy.net/" target="_blank" title="体验动易软件产品的强大功能"><span class="t6"></span>体验中心</a>
        </li>
        <li><a href="http://bbs.powereasy.net/" target="_blank" title="欢迎进入动易官方论坛参与讨论"><span class="t7"></span>动易论坛</a>
        </li>
        <li><a href="http://z.powereasy.net/" target="_blank" title="动易站长在线提供专业的站长资讯服务"><span class="t8"></span>站长在线</a>
        </li>
        <li><a href="http://dtop.powereasy.net" target="_blank" title="欢迎进入动易设计团队博客-DTOP"><span
                class="t9"></span>设计团队</a></li>

    </ul>
</div>
<!-- wrap S-->
<div id="wrap">
<div id="header">
    <div class="logoBar">
        <h1 class="logo"><a class="logoLA" href="/" title="动易网络官方主站">动易网络</a>
            <script type="text/javascript" src="/JS/2012TopIAA.js"></script>
        </h1>
        <div class="userPanel"><a class="userLog" href="/User/Login.aspx" rel="externel" id="loginEntry" title="客户自助服务">客户自助服务</a><a
                class="userCon" target="_balnk" href="/PowerEasy/Contact.html" id="aboutUs" title="请与我们联系，为您服务是我们的荣幸">联系我们</a>
        </div>
    </div>
    <div class="navBar">
        <ul id="mainNav" class="mainNav">

            <li class="li1"><a class=" a1${empty category.id?' on':''}" href="${ctx}">首页</a></li>
            <li class="spe"></li>
            <c:forEach items="${fnc:getMainNavList(site.id)}" var="category" varStatus="status">
                <li class="li1"><a class="a1${requestScope.category.id eq category.id||fn:indexOf(requestScope.category.parentIds,category.id) ge 1?' on':''}" href="${category.url}">${category.name}</a></li>
                <li class="spe"></li>
            </c:forEach>
        </ul>
        <script type="text/javascript">
            jQuery(function ($) {
                var navST;
                var name = 'mainNav';
                var t = 200;
                var type = '1';
                var removeOn = 'false';
                var effect = 'fade';
                var appendItem = '#';
                var li = "#" + name + " li";

                if (type == '1') {
                    li = "#" + name + " .li1";
                }
                if (appendItem != '#') { //插入内容
                    var appendHtml = $(appendItem).html();
                    $(li).first().append(appendHtml);
                    $(appendItem).remove();
                }

                if (type == '3') {
                    $("#" + name + " .on1").find("div").first().show();
                }

                $(li).hover(function () {
                    var curItem = $(this);
                    var onNum = (curItem.attr("class").split(" "))[0].replace("li", "")
                    navST = setTimeout(function () {//延时触发
                        $(li).removeClass("on" + onNum);
                        curItem.addClass("on" + onNum);
                        if ($("div:first", curItem).css("display") != "block") {
                            $(li + " .div" + onNum).hide();
                            if (effect == 'fade') $("div:first", curItem).fadeIn(t);
                            else $("div:first", curItem).slideDown(t);
                        }
                        navST = null;
                    }, t);
                }, function () {
                    if (navST != null)clearTimeout(navST);
                    if (type == '1' || type == '2') {
                        if (effect == 'fade') $(this).find("div").first().fadeOut(t);
                        else $(this).find("div").first().slideUp(t);
                    }
                    if (removeOn == 'true') {
                        $(this).removeClass("on1");
                    }
                }, t); //end hover


                if (document.URL.toString().indexOf("Recruitment") != -1) {
                    jQuery("#submenu_job a").addClass("on");
                }

            });
        </script>
        <div id="searchBox" class="siteSearch">
            <input class="searchBtn inputsubmit" type="submit" value="" title="click to search">
            <input class="inputtext" type="text" value="动易全文检索系统">
        </div>
    </div>
</div>
<script type="text/javascript">
    var searchFlag = false;//是否正在提交搜索请求
    function searchAction() {
        var $obj = jQuery("#searchBox");
        var toSearch = function () {
            jQuery(".inputsubmit", $obj).blur();
            if (jQuery(".inputtext", $obj).val() != "") {
                searchFlag = true;
                $obj.css("background-position", "left -0px");
                window.location = "${ctx}/search?q=" + escape(jQuery(".inputtext", $obj).val());
            } else {
                alert("请输入关键词");
            }
        };
        $obj.hover(function () {
            if (!searchFlag)jQuery(this).css("background-position", "left -30px");
        }, function () {
            if (!searchFlag)jQuery(this).css("background-position", "left 0px");
        });
        jQuery(".inputtext", $obj).bind("focus", function () {
            jQuery(this).addClass("inputOn");
            if (jQuery(this).val() == "动易全文检索系统") {
                jQuery(this).val("");
            } else {
                jQuery(this).first().select();
            }
        });
        jQuery(".inputtext", $obj).bind("blur", function () {
            jQuery(this).removeClass("inputOn");
        });
        jQuery(".inputsubmit", $obj).bind("click", toSearch);

        //键盘控制
        jQuery(".inputtext", $obj).keydown(function (e) {
            if (e.which == 13)toSearch();
        });
    }
    searchAction();

    function CheckIsLogin() {
        var $obj = jQuery("#aboutUs");

        $.pe.ajax('checkUserLogin', { success: function (response) {
            var _response = $(response);
            if (_response.find('status').text() == 'ok') {
                $obj.attr("href", "/User/Default.aspx");
                $obj.text("欢迎您，" + _response.find('username').text());
                $obj.after('<span>|</span><a href="/User/">用户中心</a><span>|</span><a href="/User/Logout.aspx">退出登录</a>');
                jQuery("#loginEntry").remove();
                jQuery("#topLink span:first").remove();
            }
            else {
                $obj.attr("href", $obj.attr("href") + "?ReturnUrl=" + escape(location.href));
            }
        }
        });
    }
    CheckIsLogin();
</script>
<!-- content S -->
<sitemesh:body/>
<!-- content E -->
<!-- footer S -->
<div id="footer">
    <!-- footSitemap-->
    <div class="footSitemap">
        <div id="btmNavBtn"><span class="open">全站快速导航</span></div>
        <div class="footCategory">
            <ul id="pe100_2012pe-节点清单">
                <li class="top"><a href="/PowerEasy/" class="top_link"><span class="down">关于动易</span></a>
                    <ul class="sub">
                        <li><a href="/PowerEasy/aboutus.html">公司简介</a></li>
                        <li><a class="fly" href="/PowerEasy/gszs/">相关证书</a></li>
                        <li><a href="/PowerEasy/gshj/">公司环境</a></li>
                        <li><a href="/PowerEasy/yghd/">员工活动</a></li>
                        <li><a href="/PowerEasy/Job.html">人员招聘</a></li>
                        <li><a href="/PowerEasy/Story/">我与动易的故事</a></li>
                        <li><a href="/PowerEasy/Contact.html">动易联系方式</a></li>
                        <li><a href="/PowerEasy/Account.html">汇款方式</a></li>
                    </ul>
                </li>
                <li class="top"><a href="/Products/" class="top_link"><span class="down">产品</span></a>
                    <ul class="sub">
                        <li><a class="fly" href="/Products/SiteFactory/">内容管理系统（CMS）</a></li>
                        <li><a class="fly" href="/Products/BizIdea/">企业电子商务管理系统</a></li>
                        <li><a class="fly" href="/Products/SmartGov/">政府网站管理系统</a></li>
                        <li><a class="fly" href="/Products/SmartSchool/">学校网站管理系统</a></li>
                        <li><a class="fly" href="/Products/SiteGroup/">站群管理系统</a></li>
                        <li><a class="fly" href="/Products/HMS/">医院网站管理系统</a></li>
                        <li><a class="fly" href="/Products/Xiaoqing/">校庆网站管理系统</a></li>
                        <li><a class="fly" href="/Products/BizAssistant/">商务助理</a></li>
                        <li><a href="/Products/SIPAssistant.html">消息助手</a></li>
                        <li><a href="/Products/SmartIntranetPortal.html">协同办公与知识共享平台</a></li>
                        <li><a href="/Products/SmartWeibo.html">微博管理系统</a></li>
                        <li><a href="/Products/Industry.html">行业门户网站管理系统</a></li>
                        <li><a href="/Products/SmartFileManager.html">文件服务器管理系统</a></li>
                        <li><a href="/Products/jobs.html">人才招聘管理系统</a></li>
                        <li><a href="/Products/Customer-Service.html">客服中心管理系统</a></li>
                        <li><a href="/Products/WAPCMS.html">WAP网站管理系统</a></li>
                        <li><a href="/Products/PowerEasy-full-text-retrieval-systems.html">全文检索系统</a></li>
                        <li><a href="/Products/PowerEasy-Blog.html">博客管理系统</a></li>
                        <li><a href="/Products/Politics-by-internet.html">网络问政平台</a></li>
                        <li><a href="/Products/engineering-project-information-public-and-credit-system.html">工程建设领域信息公开与信用建设平台</a>
                        </li>
                        <li><a href="/Products/SmartDocumentShare.html">文档分享平台</a></li>
                        <li><a href="/Products/PowerEasy-Online-Interview-System.html">在线访谈系统</a></li>
                        <li><a href="/Products/PowerEasy-Online-Question-Answering-System.html">在线问答平台</a></li>
                        <li><a href="/Products/Page-Block-Management-System.html">页面区块管理系统</a></li>
                        <li><a href="/Products/Zhengxinpingtai.html">征信平台</a></li>
                        <li><a class="fly" href="/Products/MobileSolutions/">移动客户端</a></li>
                        <li><a href="/Products/Media-convert-server.html">媒体转换服务器</a></li>
                        <li><a href="/Products/Media-Live-Encoder.html">媒体直播编码控制台</a></li>
                        <li><a href="/Products/PowerEasy-Video-Player.html">视频播放器</a></li>
                        <li><a href="/Products/shipinwangzhanguanlixitong.html">视频网站管理系统</a></li>
                    </ul>
                </li>
                <li class="top"><a href="/Blog/" class="top_link"><span class="down">官方博客</span></a>
                    <ul class="sub">
                        <li><a href="/Blog/kuaibao/">产品报道</a></li>
                        <li><a href="/Blog/expert/">专家坐堂</a></li>
                        <li><a href="/Blog/campaign/">生活偶拾</a></li>
                        <li><a href="/Blog/bethink/">探究与思考</a></li>
                        <li><a href="/Blog/tech/">技术交流</a></li>
                    </ul>
                </li>
                <li class="top"><a href="/Solutions/" class="top_link"><span class="down">解决方案</span></a>
                    <ul class="sub">
                        <li><a href="/Solutions/government/">政府机关</a></li>
                        <li><a href="/Solutions/education/">教育科研</a></li>
                        <li><a href="/Solutions/EC/">电子商务</a></li>
                        <li><a href="/Solutions/enterprise/">企业展示</a></li>
                        <li><a href="/Solutions/Media/">新闻媒体</a></li>
                        <li><a href="/Solutions/gateway/">行业门户</a></li>
                        <li><a href="/Solutions/SMS/">短信通</a></li>
                    </ul>
                </li>
                <li class="top"><a href="/Cases/" class="top_link"><span class="down">成功案例</span></a>
                    <ul class="sub">
                        <li><a class="fly" href="/Cases/government/">政府网站类</a></li>
                        <li><a class="fly" href="/Cases/education/">教育网站类</a></li>
                        <li><a href="/Cases/media/">媒体网站类</a></li>
                        <li><a href="/Cases/enterprise/">企业网站类</a></li>
                        <li><a href="/Cases/eshop/">电子商务类</a></li>
                        <li><a href="/Cases/institution/">组织机构类</a></li>
                        <li><a href="/Cases/localportal/">地方门户类</a></li>
                        <li><a href="/Cases/vocationportal/">行业门户类</a></li>
                        <li><a href="/Cases/PESMS/">短信通</a></li>
                    </ul>
                </li>
                <li class="top"><a href="/Soft/" class="top_link"><span class="down">下载中心</span></a>
                    <ul class="sub">
                        <li><a class="fly" href="/Soft/PE_soft/">动易软件</a></li>
                        <li><a href="/Soft/SiteWeaver/">SiteWeaver 相关</a></li>
                        <li><a href="/Soft/PE_Plus/">动易插件</a></li>
                        <li><a href="/Soft/documents/">其他文档</a></li>
                        <li><a href="/Soft/tools/">相关工具</a></li>
                    </ul>
                </li>
                <li class="top"><a href="/HelpYou/" class="top_link"><span class="down">助您选购</span></a>
                    <ul class="sub">
                        <li><a class="fly" href="/HelpYou/Genuine/">正版验证</a></li>
                        <li><a href="/HelpYou/Report/">评测报告</a></li>
                        <li><a href="/HelpYou/Choice/">选择动易</a></li>
                        <li><a href="/HelpYou/License/">授权说明</a></li>
                        <li><a href="/HelpYou/Consultation/">咨询问答</a></li>
                        <li><a class="fly" href="/HelpYou/Knowledge/">相关知识</a></li>
                    </ul>
                </li>
            </ul>
        </div>
        <div id="div4"></div>
    </div>
    <!-- footNav -->
    <div class="footNav">
        <div>
            <a href="/PowerEasy/Contact.html" title="请与我们联系，为您服务是我们的荣幸">联系我们</a><span class="spe">|</span>
            <a href="/PowerEasy/" title="了解动易公司及相关信息">关于动易</a><span class="spe">|</span>
            <a href="/PowerEasy/630.html" title="动易官方网站群网站地图">网站地图</a><span class="spe">|</span>
            <a href="/PowerEasy/1975.html" title="动易相关证书">企业资质</a><span class="spe">|</span>
            <a href="/Link/Partners/" title="了解动易的合作伙伴">合作伙伴</a><span class="spe">|</span>
            <a href="/PowerEasy/Job.html" title="一起加入动易团队！实现我们远大的理想">招贤纳士</a><span class="spe">|</span>
            <a href="/PowerEasy/1191.html" title="相关法律条款">法律条款</a><span class="spe">|</span>
            <a href="/PowerEasy/Account.html" title="相关汇款方式">汇款方式</a><span class="spe">|</span>
            <a href="/PowerEasy/1189.html" title="隐私权声明">隐私权声明</a>
        </div>
    </div>
    <!-- copyright -->
    <p class="copyright">Copyright © 2003- 2012 PowerEasy.All Rights Reserved. 广东动易网络科技有限公司 版权所有 </p>
    <span>国家高新技术企业</span> | <span>国家双软认定企业</span> | <span>广东省民营科技企业</span> | <span>顺德龙腾计划重点扶持企业</span> | <span>通过ISO9001:2008认证</span>
    | <span>通过CMMI 3级认证</span>
    <br/><span>中国电子商务协会会员</span> | <span>广东软件行业协会会员</span> | <span>广东省电子商务协会副会长单位</span> | <span>佛山市电子商务协会副会长</span> |
    <span>佛山市软件行业协会会员</span>
    <br/><span>软件企业编号：粤R-2006-0134</span> | <span>高新技术企业编号：GR201044000042</span> | <span>公安备案编号：4406063010734</span> |
    <a href="http://www.miibeian.gov.cn/" target="_blank">粤ICP备05004015号</a>

    <p>本站基于 <a href="/Products/BizIdea/" target="_blank" title="了解 PowerEasy BizIdea 产品">PowerEasy<sup>&reg;</sup>
        BizIdea<sup>&reg;</sup></a> 制作</p>
</div>
<!-- footer E -->
<!-- wrap E -->
</body>
</html>