
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=gb2312" />
<meta name="author" content="My97" /> 
<meta name="keywords" content="日期控件 datepicker calendar 日历控件 javascript js日历控件 带时间 自定义格式 月历控件 日期时间 日期选择" /><title>My97日期控件 功能演示 My97 Datepicker Demo</title>
<link href="../../css/base.css" rel="stylesheet" type="text/css" />
<link href="demo.css" rel="stylesheet" type="text/css" />
</head>
<body>
<iframe src="../../head.asp" scrolling="no" frameborder="0" height="100px" width="100%"></iframe>
<script language="JavaScript" type="text/javascript" src="../../../WdatePicker.js"></script>
<div class="dCenter dBody">	
  <div id="content">
    <h2>一. 简介<a name="m1" id="m1"></a></h2>
    <h3>1. 简介<a name="m11" id="m11"></a></h3>
    <p>目前的版本是:4.8</p>
    <h3>2. 注意事项<a name="m12" id="m12"></a></h3>
    <ul>
      <li>My97DatePicker目录是一个整体,<span class="STYLE1">不可破坏里面的目录结构</span>,也不可对里面的文件改名,可以改目录名</li>
      <li>My97DatePicker.htm是必须文件,<span class="STYLE1">不可删除(4.8以后不存在此文件)</span></li>
      <li>各目录及文件的用途: <br />
        <span class="STYLE2">WdatePicker.js</span> 配置文件,<span class="STYLE1">在调用的地方仅需使用该文件</span>,可多个共存,以xx_WdatePicker.js方式命名<br />
        <span class="STYLE2">config.js</span> 语言和皮肤配置文件,<span class="STYLE1">无需引入(4.8以后合并入WdatePicker.js)</span><br />
        <span class="STYLE2">calendar.js</span> 日期库主文件,<span class="STYLE1">无需引入</span><br />
        <span class="STYLE2">My97DatePicker.htm</span> 临时页面文件,不可删除<span class="STYLE1">(4.8以后不存在此文件)</span><br />
        <span class="STYLE2">目录lang</span> 存放语言文件,你可以根据需要清理或添加语言文件<br />
        <span class="STYLE2">目录skin </span>存放皮肤的相关文件,你可以根据需要清理或添加皮肤文件包</li>
      <li>当WdatePicker.js里的属性:$wdate=true时,在input里加上class=&quot;Wdate&quot;就会在选择框右边出现日期图标,如果您不喜欢这个样式,可以把class=&quot;Wdate&quot;去掉,另外也可以通过修改skin目录下的WdatePicker.css文件来修改样式</li>
    </ul>
    <h3>3. 支持的浏览器<a name="m13" id="m13"></a></h3>
    <p>IE 6.0+ , Firefox 2.0+ , Chrome, Opera 9.5+ , Safari 3.0+<br />
    </p>
    <p>&nbsp; </p>
    <p>注意:IE 8.0是完美支持的,如果你在IE8上使用遇到问题,请与我取得联系,<span class="STYLE1">务必附上能再现你的问题的<strong>纯HTML</strong>代码包</span></p>   
	<h2><a href="2.1.asp"><strong>二. 功能及示例</strong></a><a name="m2" id="m2"></a></h2>
    <h2><a href="3.asp">三. 配置说明</a><a name="m3" id="m3"></a></h2>
    <h2><a href="999.asp">四. 如何使用</a><a name="m4" id="m4"></a></h2>
    <br />
<br /> 
  </div>
  <div style="clear:both"></div>
</div>
<div class="dCenter dBody" style="padding-left:72px">
<script type="text/javascript"><!--
google_ad_client = "ca-pub-6343250634002651";
/* 底部 */
google_ad_slot = "0599809152";
google_ad_width = 728;
google_ad_height = 90;
//-->
</script>
<script type="text/javascript"
src="http://pagead2.googlesyndication.com/pagead/show_ads.js">
</script>
</div>
<div id="footer" class="dCenter">&copy; 2010 <a href="mailto:smallcarrot@163.com">My97</a> All Rights Reserved.&nbsp;&nbsp;&nbsp;&nbsp;<script type="text/javascript">
var _bdhmProtocol = (("https:" == document.location.protocol) ? " https://" : " http://");
document.write(unescape("%3Cscript src='" + _bdhmProtocol + "hm.baidu.com/h.js%3F489957c212e14340592fb2e4921b2f1d' type='text/javascript'%3E%3C/script%3E"));
</script>&nbsp;&nbsp;&nbsp;&nbsp;浙ICP备11060275号
</div>
</body>
</html>
