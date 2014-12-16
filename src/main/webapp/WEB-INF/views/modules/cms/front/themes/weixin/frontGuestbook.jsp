<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/modules/cms/front/include/taglib.jsp"%>
<!DOCTYPE html>
<html>
<head>
	<title>留言板</title>
	<meta name="decorator" content="cms_default_${site.theme}"/>
	<meta name="description" content="JeeSite ${site.description}" />
	<meta name="keywords" content="JeeSite ${site.keywords}" />
	<script src="${ctxStaticTheme}/lyb/jquery-1.7.1.min.js"></script>
	<script src="${ctxStaticTheme}/lyb/jquery.alerts.js" type="text/javascript"></script>
	<link href="${ctxStaticTheme}/lyb/jquery.alerts.css" rel="stylesheet" type="text/css" media="screen" />
	
	<link rel="stylesheet" href="${ctxStaticTheme}/lyb/bootstrap.min.css">
	
	<link href="${ctxStaticTheme}/lyb/yly002.css" rel="stylesheet" type="text/css" />
	
	<script type="text/javascript">
	$(function(){
		$(".lybinput,.ly_reply,.reply,.fanghui").hide();
		
		$(".takeamessage").click(function(){
			if ($(this).parents().next(".lybinput").is(":hidden")){
				$(this).parents().next(".lybinput").show();
			}else{
				$(this).parents().next(".lybinput").hide();
			}
		});
		//留言表单
		$("#ly_submit_bottom").click(function(){		
			if ('' == $("#lyform_bottom input[name=nickname]").val()) {
				jAlert('请输入昵称');
				return false;
			}else if('' == $("#lyform_bottom input[name=content]").val()) {
				jAlert('请输入您的留言');
				return false;
			}else{
				$("#lyform_bottom").submit();
			}
		});
		//回复表单
		$(".reply_submit").click(function(){
			if ('' == $(this).siblings(".nicheng").children().children("input[name=reply_nickname]").val()) {
				jAlert('请输入昵称');
				return false;
			}else if('' == $(this).siblings(".liuyan").children().children("input[name=reply_content]").val()) {
				jAlert('请输入您的回复');
				return false;
			}else{			
				$(this).parents("form").submit();
			}
		});
		
		$(".huifu").click(function(){
			$(this).hide();		
			$(this).parents().next(".ly_reply").show();
			$(this).siblings(".fh,.chakang").hide();		
			$(this).siblings(".quit").show();
		});
		$(".chakang").click(function(){
			$(this).hide();		
			$(this).parents().next().next(".reply").show();
			$(this).siblings(".fanghui").show();
			$(this).siblings(".quit").hide();
		});
		$(".fh").click(function(){
			$(this).parents().next().next(".reply").hide();
			$(this).siblings(".chakang,.huifu").show();		
			$(this).hide();
		});
		$(".quit").click(function(){
			$(this).hide();
			$(this).parents().next(".ly_reply").hide();
			$(this).siblings(".ly_reply").hide();
			$(this).siblings(".huifu").show();
			$(this).siblings(".chakang").show();
			if ($(this).parents().next().next(".reply").is(":visible")){
				$(this).parents().next().next(".reply").hide();
			}		
		});
		//提交留言表单
		$("#ly_submit").click(function(){
			if ('' == $("#lyform input[name=name]").val()) {
				jAlert('请输入昵称');
				return false;
			}else if('' == $("#lyform input[name=content]").val()) {
				jAlert('请输入您的留言');
				return false;
			}else{
				$("#lyform").submit();
			}
		});
		
	});
	
	</script>
</head>
<body>
<div class="wrapper">
    <div class="header">
		<div class="header_img"><img src="${ctxStaticTheme}/lyb/lyb_02.png" /></div>
		<div class="name">${site.title}</div>
		<div class="wrapper_wodeliuyan">
			<!-- <div class="wodeliuyan"><a href="#">我的留言</a></div> -->
		</div>
		<a href="javascript:history.go(-2);" target="_blank"><div class="wrapper_back"></div></a>
	</div>
	<div class="content_wrapper">
		<div class="content">
	    	<div class="djly">
			    <div class="djly_02 takeamessage">
					<img src="${ctxStaticTheme}/lyb/ly_03.png" />	
				</div>            
			</div>
			 <!--<div class="lybinput">	 end lybinput -->
			
				<form class="lybinput" id="lyform" action="" method="POST">   
					<div class="nicheng">
						<div>
					       <input class="nicheng_02 form-control" name="name" value="" type="text" placeholder="请输入昵称" required="required">
					   	</div>
					</div>
				   
				   	<div class="liuyan">
				    	<div>
					       <input  class="nicheng_02 form-control" name="content" id="textinput1" placeholder="请输入您的留言" required="required" value="" type="text">
					    </div>    
				   	</div>
				   
				   	<div class="tijiao" id="ly_submit" >
				    	<div class="tijiao_02">
							<a>提交</a>
					   	</div>
				   	</div>
				</form>
			   <!--</div> end lybinput -->
			   
			<c:forEach items="${page.list}" var="guestbook" varStatus="vs">
				<div class="date"><fmt:formatDate value="${guestbook.createDate}" pattern="yyyy-MM-dd HH:mm:ss"/>			</div>
				<div class="neirong_001">
					<div class="yonghunicheng">${guestbook.name}</div>
					<div class="zhuyaoneirong">${guestbook.content}</div>
				</div>
				<div class="neirong_001_buttom">
					<div class="date_time"></div>
					<div class="shuliang"></div>
					<div class="huifu"><a></a></div>
					<div class="chakang"><a></a></div><div class="fanghui fh"><a>返回</a></div>
					<div class="fanghui quit"><a>取消</a></div>
				</div>
				<div class="reply">
					<div class="neirong_001_smm">
						<div class="zhuyaoneirong_smm"><strong style="color: #000">${guestbook.reUser.name}: </strong>${guestbook.reContent}</div>
					</div>
					<div class="neirong_001_buttom_smm">
						<div class="date_time_smm">${guestbook.reContent}</div>
						<div class="yonghunicheng_smm"></div>
					</div>
				</div>
			</c:forEach>

			<!--<div class="ly_reply"> start ly_reply -->
			
				<form class="ly_reply" action="http://shibo.55zhe.net/wz.php?mod=board&boardid=1299&openid=fromuserid" method="POST">
					<div class="nicheng">
						<div>
							<input class="nicheng_02 form-control rnickname" name="reply_nickname" type="text" value="" placeholder="请输入昵称" />
						</div>    
					</div>
					   
					<div class="liuyan">
						<div>
							<input  class="nicheng_02 form-control" name="reply_content" placeholder="请输入您的回复" value="" type="text">
						</div>    
					</div>
					   
					<div class="tijiao reply_submit">
						<div class="tijiao_02">
							<a>提交</a>
						</div>
					</div>	
				</form>	   
			
			<!--</div> end ly_reply -->
		
					</div><!-- content--><div class="clfl"></div>
	
	</div><!--content_wrapper-->
	<div class="footer">
	感谢您的宝贵意见
	</div>
</div><!-- wrapper -->

</body>
</html>