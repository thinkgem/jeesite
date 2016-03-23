<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
    <title>图标选择</title>
	<meta name="decorator" content="blank"/>
    <style type="text/css">
    	.page-header {clear:both;margin:0 20px;padding-top:20px;}
		.the-icons {padding:25px 10px 15px;list-style:none;}
		.the-icons li {float:left;width:22%;line-height:25px;margin:2px 5px;cursor:pointer;}
		.the-icons i {margin:1px 5px;font-size:16px;} .the-icons li:hover {background-color:#efefef;}
        .the-icons li.active {background-color:#0088CC;color:#ffffff;}
        .the-icons li:hover i{font-size:20px;}
    </style>
    <script type="text/javascript">
	    $(document).ready(function(){
	    	$("#icons li").click(function(){
	    		$("#icons li").removeClass("active");
	    		$("#icons li i").removeClass("icon-white");
	    		$(this).addClass("active");
	    		$(this).children("i").addClass("icon-white");
	    		$("#icon").val($(this).text());
	    	});
	    	$("#icons li").each(function(){
	    		if ($(this).text()=="${value}"){
	    			$(this).click();
	    		}
	    	});
	    	$("#icons li").dblclick(function(){
	    		top.$.jBox.getBox().find("button[value='ok']").trigger("click");
	    	});
	    });
    </script>
</head>
<body>
<input type="hidden" id="icon" value="${value}" />
<div id="icons">
		
	    <h2 class="page-header"> Web 应用的图标</h2>
	    
	    <ul class="the-icons">
	      <li><i class="icon-adjust"></i>adjust</li>
	      <li><i class="icon-asterisk"></i>asterisk</li>
	      <li><i class="icon-ban-circle"></i>ban-circle</li>
	      <li><i class="icon-bar-chart"></i>bar-chart</li>
	      <li><i class="icon-barcode"></i>barcode</li>
	      <li><i class="icon-beaker"></i>beaker</li>
	      <li><i class="icon-beer"></i>beer</li>
	      <li><i class="icon-bell"></i>bell</li>
	      <li><i class="icon-bell-alt"></i>bell-alt</li>
	      <li><i class="icon-bolt"></i>bolt</li>
	      <li><i class="icon-book"></i>book</li>
	      <li><i class="icon-bookmark"></i>bookmark</li>
	      <li><i class="icon-bookmark-empty"></i>bookmark-empty</li>
	      <li><i class="icon-briefcase"></i>briefcase</li>
	      <li><i class="icon-bullhorn"></i>bullhorn</li>
	      <li><i class="icon-calendar"></i>calendar</li>
	      <li><i class="icon-camera"></i>camera</li>
	      <li><i class="icon-camera-retro"></i>camera-retro</li>
	      <li><i class="icon-certificate"></i>certificate</li>
	      <li><i class="icon-check"></i>check</li>
	      <li><i class="icon-check-empty"></i>check-empty</li>
	      <li><i class="icon-circle"></i>circle</li>
	      <li><i class="icon-circle-blank"></i>circle-blank</li>
	      <li><i class="icon-cloud"></i>cloud</li>
	      <li><i class="icon-cloud-download"></i>cloud-download</li>
	      <li><i class="icon-cloud-upload"></i>cloud-upload</li>
	      <li><i class="icon-coffee"></i>coffee</li>
	      <li><i class="icon-cog"></i>cog</li>
	      <li><i class="icon-cogs"></i>cogs</li>
	      <li><i class="icon-comment"></i>comment</li>
	      <li><i class="icon-comment-alt"></i>comment-alt</li>
	      <li><i class="icon-comments"></i>comments</li>
	      <li><i class="icon-comments-alt"></i>comments-alt</li>
	      <li><i class="icon-credit-card"></i>credit-card</li>
	      <li><i class="icon-dashboard"></i>dashboard</li>
	      <li><i class="icon-desktop"></i>desktop</li>
	      <li><i class="icon-download"></i>download</li>
	      <li><i class="icon-download-alt"></i>download-alt</li>
	    

	      <li><i class="icon-edit"></i>edit</li>
	      <li><i class="icon-envelope"></i>envelope</li>
	      <li><i class="icon-envelope-alt"></i>envelope-alt</li>
	      <li><i class="icon-exchange"></i>exchange</li>
	      <li><i class="icon-exclamation-sign"></i>exclamation-sign</li>
	      <li><i class="icon-external-link"></i>external-link</li>
	      <li><i class="icon-eye-close"></i>eye-close</li>
	      <li><i class="icon-eye-open"></i>eye-open</li>
	      <li><i class="icon-facetime-video"></i>facetime-video</li>
	      <li><i class="icon-fighter-jet"></i>fighter-jet</li>
	      <li><i class="icon-film"></i>film</li>
	      <li><i class="icon-filter"></i>filter</li>
	      <li><i class="icon-fire"></i>fire</li>
	      <li><i class="icon-flag"></i>flag</li>
	      <li><i class="icon-folder-close"></i>folder-close</li>
	      <li><i class="icon-folder-open"></i>folder-open</li>
	      <li><i class="icon-folder-close-alt"></i>folder-close-alt</li>
	      <li><i class="icon-folder-open-alt"></i>folder-open-alt</li>
	      <li><i class="icon-food"></i>food</li>
	      <li><i class="icon-gift"></i>gift</li>
	      <li><i class="icon-glass"></i>glass</li>
	      <li><i class="icon-globe"></i>globe</li>
	      <li><i class="icon-group"></i>group</li>
	      <li><i class="icon-hdd"></i>hdd</li>
	      <li><i class="icon-headphones"></i>headphones</li>
	      <li><i class="icon-heart"></i>heart</li>
	      <li><i class="icon-heart-empty"></i>heart-empty</li>
	      <li><i class="icon-home"></i>home</li>
	      <li><i class="icon-inbox"></i>inbox</li>
	      <li><i class="icon-info-sign"></i>info-sign</li>
	      <li><i class="icon-key"></i>key</li>
	      <li><i class="icon-leaf"></i>leaf</li>
	      <li><i class="icon-laptop"></i>laptop</li>
	      <li><i class="icon-legal"></i>legal</li>
	      <li><i class="icon-lemon"></i>lemon</li>
	      <li><i class="icon-lightbulb"></i>lightbulb</li>
	      <li><i class="icon-lock"></i>lock</li>
	      <li><i class="icon-unlock"></i>unlock</li>
	    

	      <li><i class="icon-magic"></i>magic</li>
	      <li><i class="icon-magnet"></i>magnet</li>
	      <li><i class="icon-map-marker"></i>map-marker</li>
	      <li><i class="icon-minus"></i>minus</li>
	      <li><i class="icon-minus-sign"></i>minus-sign</li>
	      <li><i class="icon-mobile-phone"></i>mobile-phone</li>
	      <li><i class="icon-money"></i>money</li>
	      <li><i class="icon-move"></i>move</li>
	      <li><i class="icon-music"></i>music</li>
	      <li><i class="icon-off"></i>off</li>
	      <li><i class="icon-ok"></i>ok</li>
	      <li><i class="icon-ok-circle"></i>ok-circle</li>
	      <li><i class="icon-ok-sign"></i>ok-sign</li>
	      <li><i class="icon-pencil"></i>pencil</li>
	      <li><i class="icon-picture"></i>picture</li>
	      <li><i class="icon-plane"></i>plane</li>
	      <li><i class="icon-plus"></i>plus</li>
	      <li><i class="icon-plus-sign"></i>plus-sign</li>
	      <li><i class="icon-print"></i>print</li>
	      <li><i class="icon-pushpin"></i>pushpin</li>
	      <li><i class="icon-qrcode"></i>qrcode</li>
	      <li><i class="icon-question-sign"></i>question-sign</li>
	      <li><i class="icon-quote-left"></i>quote-left</li>
	      <li><i class="icon-quote-right"></i>quote-right</li>
	      <li><i class="icon-random"></i>random</li>
	      <li><i class="icon-refresh"></i>refresh</li>
	      <li><i class="icon-remove"></i>remove</li>
	      <li><i class="icon-remove-circle"></i>remove-circle</li>
	      <li><i class="icon-remove-sign"></i>remove-sign</li>
	      <li><i class="icon-reorder"></i>reorder</li>
	      <li><i class="icon-reply"></i>reply</li>
	      <li><i class="icon-resize-horizontal"></i>resize-horizontal</li>
	      <li><i class="icon-resize-vertical"></i>resize-vertical</li>
	      <li><i class="icon-retweet"></i>retweet</li>
	      <li><i class="icon-road"></i>road</li>
	      <li><i class="icon-rss"></i>rss</li>
	      <li><i class="icon-screenshot"></i>screenshot</li>
	      <li><i class="icon-search"></i>search</li>
	    

	      <li><i class="icon-share"></i>share</li>
	      <li><i class="icon-share-alt"></i>share-alt</li>
	      <li><i class="icon-shopping-cart"></i>shopping-cart</li>
	      <li><i class="icon-signal"></i>signal</li>
	      <li><i class="icon-signin"></i>signin</li>
	      <li><i class="icon-signout"></i>signout</li>
	      <li><i class="icon-sitemap"></i>sitemap</li>
	      <li><i class="icon-sort"></i>sort</li>
	      <li><i class="icon-sort-down"></i>sort-down</li>
	      <li><i class="icon-sort-up"></i>sort-up</li>
	      <li><i class="icon-spinner"></i>spinner</li>
	      <li><i class="icon-star"></i>star</li>
	      <li><i class="icon-star-empty"></i>star-empty</li>
	      <li><i class="icon-star-half"></i>star-half</li>
	      <li><i class="icon-tablet"></i>tablet</li>
	      <li><i class="icon-tag"></i>tag</li>
	      <li><i class="icon-tags"></i>tags</li>
	      <li><i class="icon-tasks"></i>tasks</li>
	      <li><i class="icon-thumbs-down"></i>thumbs-down</li>
	      <li><i class="icon-thumbs-up"></i>thumbs-up</li>
	      <li><i class="icon-time"></i>time</li>
	      <li><i class="icon-tint"></i>tint</li>
	      <li><i class="icon-trash"></i>trash</li>
	      <li><i class="icon-trophy"></i>trophy</li>
	      <li><i class="icon-truck"></i>truck</li>
	      <li><i class="icon-umbrella"></i>umbrella</li>
	      <li><i class="icon-upload"></i>upload</li>
	      <li><i class="icon-upload-alt"></i>upload-alt</li>
	      <li><i class="icon-user"></i>user</li>
	      <li><i class="icon-user-md"></i>user-md</li>
	      <li><i class="icon-volume-off"></i>volume-off</li>
	      <li><i class="icon-volume-down"></i>volume-down</li>
	      <li><i class="icon-volume-up"></i>volume-up</li>
	      <li><i class="icon-warning-sign"></i>warning-sign</li>
	      <li><i class="icon-wrench"></i>wrench</li>
	      <li><i class="icon-zoom-in"></i>zoom-in</li>
	      <li><i class="icon-zoom-out"></i>zoom-out</li>
	    </ul>
	
	  
	    <h2 class="page-header">文本编辑器图标</h2>
	  
	    <ul class="the-icons">
	      <li><i class="icon-file"></i>file</li>
	      <li><i class="icon-file-alt"></i>file-alt</li>
	      <li><i class="icon-cut"></i>cut</li>
	      <li><i class="icon-copy"></i>copy</li>
	      <li><i class="icon-paste"></i>paste</li>
	      <li><i class="icon-save"></i>save</li>
	      <li><i class="icon-undo"></i>undo</li>
	      <li><i class="icon-repeat"></i>repeat</li>
	    

	      <li><i class="icon-text-height"></i>text-height</li>
	      <li><i class="icon-text-width"></i>text-width</li>
	      <li><i class="icon-align-left"></i>align-left</li>
	      <li><i class="icon-align-center"></i>align-center</li>
	      <li><i class="icon-align-right"></i>align-right</li>
	      <li><i class="icon-align-justify"></i>align-justify</li>
	      <li><i class="icon-indent-left"></i>indent-left</li>
	      <li><i class="icon-indent-right"></i>indent-right</li>
	    

	      <li><i class="icon-font"></i>font</li>
	      <li><i class="icon-bold"></i>bold</li>
	      <li><i class="icon-italic"></i>italic</li>
	      <li><i class="icon-strikethrough"></i>strikethrough</li>
	      <li><i class="icon-underline"></i>underline</li>
	      <li><i class="icon-link"></i>link</li>
	      <li><i class="icon-paper-clip"></i>paper-clip</li>
	      <li><i class="icon-columns"></i>columns</li>
	    

	      <li><i class="icon-table"></i>table</li>
	      <li><i class="icon-th-large"></i>th-large</li>
	      <li><i class="icon-th"></i>th</li>
	      <li><i class="icon-th-list"></i>th-list</li>
	      <li><i class="icon-list"></i>list</li>
	      <li><i class="icon-list-ol"></i>list-ol</li>
	      <li><i class="icon-list-ul"></i>list-ul</li>
	      <li><i class="icon-list-alt"></i>list-alt</li>
	    </ul>
	
	    <h2 class="page-header">指示方向的图标</h2>
	  
	    <ul class="the-icons">
	      <li><i class="icon-angle-left"></i>angle-left</li>
	      <li><i class="icon-angle-right"></i>angle-right</li>
	      <li><i class="icon-angle-up"></i>angle-up</li>
	      <li><i class="icon-angle-down"></i>angle-down</li>
	      <li><i class="icon-arrow-down"></i>arrow-down</li>
	      <li><i class="icon-arrow-left"></i>arrow-left</li>
	      <li><i class="icon-arrow-right"></i>arrow-right</li>
	      <li><i class="icon-arrow-up"></i>arrow-up</li>
	    

	      <li><i class="icon-caret-down"></i>caret-down</li>
	      <li><i class="icon-caret-left"></i>caret-left</li>
	      <li><i class="icon-caret-right"></i>caret-right</li>
	      <li><i class="icon-caret-up"></i>caret-up</li>
	      <li><i class="icon-chevron-down"></i>chevron-down</li>
	      <li><i class="icon-chevron-left"></i>chevron-left</li>
	      <li><i class="icon-chevron-right"></i>chevron-right</li>
	      <li><i class="icon-chevron-up"></i>chevron-up</li>
	    

	      <li><i class="icon-circle-arrow-down"></i>circle-arrow-down</li>
	      <li><i class="icon-circle-arrow-left"></i>circle-arrow-left</li>
	      <li><i class="icon-circle-arrow-right"></i>circle-arrow-right</li>
	      <li><i class="icon-circle-arrow-up"></i>circle-arrow-up</li>
	      <li><i class="icon-double-angle-left"></i>double-angle-left</li>
	      <li><i class="icon-double-angle-right"></i>double-angle-right</li>
	      <li><i class="icon-double-angle-up"></i>double-angle-up</li>
	      <li><i class="icon-double-angle-down"></i>double-angle-down</li>
	    

	      <li><i class="icon-hand-down"></i>hand-down</li>
	      <li><i class="icon-hand-left"></i>hand-left</li>
	      <li><i class="icon-hand-right"></i>hand-right</li>
	      <li><i class="icon-hand-up"></i>hand-up</li>
	      <li><i class="icon-circle"></i>circle</li>
	      <li><i class="icon-circle-blank"></i>circle-blank</li>
	    </ul>
	  
	
	    <h2 class="page-header">视频播放器图标</h2>
	  
	    <ul class="the-icons">
	      <li><i class="icon-play-circle"></i>play-circle</li>
	      <li><i class="icon-play"></i>play</li>
	      <li><i class="icon-pause"></i>pause</li>
	      <li><i class="icon-stop"></i>stop</li>
	    

	      <li><i class="icon-step-backward"></i>step-backward</li>
	      <li><i class="icon-fast-backward"></i>fast-backward</li>
	      <li><i class="icon-backward"></i>backward</li>
	      <li><i class="icon-forward"></i>forward</li>
	    

	      <li><i class="icon-fast-forward"></i>fast-forward</li>
	      <li><i class="icon-step-forward"></i>step-forward</li>
	      <li><i class="icon-eject"></i>eject</li>
	    

	      <li><i class="icon-fullscreen"></i>fullscreen</li>
	      <li><i class="icon-resize-full"></i>resize-full</li>
	      <li><i class="icon-resize-small"></i>resize-small</li>
	    </ul>
	
	
	    <h2 class="page-header">SNS图标</h2>
	  
	    <ul class="the-icons">
	      <li><i class="icon-phone"></i>phone</li>
	      <li><i class="icon-phone-sign"></i>phone-sign</li>
	      <li><i class="icon-facebook"></i>facebook</li>
	      <li><i class="icon-facebook-sign"></i>facebook-sign</li>
	    

	      <li><i class="icon-twitter"></i>twitter</li>
	      <li><i class="icon-twitter-sign"></i>twitter-sign</li>
	      <li><i class="icon-github"></i>github</li>
	      <li><i class="icon-github-alt"></i>github-alt</li>
	    

	      <li><i class="icon-github-sign"></i>github-sign</li>
	      <li><i class="icon-linkedin"></i>linkedin</li>
	      <li><i class="icon-linkedin-sign"></i>linkedin-sign</li>
	      <li><i class="icon-pinterest"></i>pinterest</li>
	    

	      <li><i class="icon-pinterest-sign"></i>pinterest-sign</li>
	      <li><i class="icon-google-plus"></i>google-plus</li>
	      <li><i class="icon-google-plus-sign"></i>google-plus-sign</li>
	      <li><i class="icon-sign-blank"></i>sign-blank</li>
	    </ul>
	  
	  
	    <h2 class="page-header">医疗图标</h2>
	  
	    <ul class="the-icons">
	      <li><i class="icon-ambulance"></i>ambulance</li>
	      <li><i class="icon-beaker"></i>beaker</li>
	    

	      <li><i class="icon-h-sign"></i>h-sign</li>
	      <li><i class="icon-hospital"></i>hospital</li>
	    

	      <li><i class="icon-medkit"></i>medkit</li>
	      <li><i class="icon-plus-sign-alt"></i>plus-sign-alt</li>
	    

	      <li><i class="icon-stethoscope"></i>stethoscope</li>
	      <li><i class="icon-user-md"></i>user-md</li>
	    </ul>
	<br/><br/>
</div>
</body>