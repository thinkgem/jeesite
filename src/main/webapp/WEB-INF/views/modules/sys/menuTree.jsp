<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>菜单导航</title>
	<script type="text/javascript"> 
		$(document).ready(function() {
			$(".accordion-heading a").click(function(){
				$('.accordion-toggle i').removeClass('icon-chevron-down');
				$('.accordion-toggle i').addClass('icon-chevron-right');
				if(!$($(this).attr('href')).hasClass('in')){
					$(this).children('i').removeClass('icon-chevron-right');
					$(this).children('i').addClass('icon-chevron-down');
				}
			});
			$(".accordion-body a").click(function(){
				$("#menu li").removeClass("active");
				$("#menu li i").removeClass("icon-white");
				$(this).parent().addClass("active");
				$(this).children("i").addClass("icon-white");
			});
		});
	</script>
</head>
<body>
	<div class="accordion" id="menu"><c:set var="menuList" value="${fns:getMenuList()}"/><c:set var="firstMenu" value="true"/><c:forEach items="${menuList}" var="menu" varStatus="idxStatus"><c:if test="${menu.parent.id eq 1&&menu.isShow eq 1}">
		<div class="accordion-group">
		    <div class="accordion-heading">
		    	<a class="accordion-toggle" data-toggle="collapse" data-parent="#menu" href="#collapse${menu.id}"><i class="icon-chevron-${firstMenu?'down':'right'}"></i>&nbsp;&nbsp;${menu.name}</a>
		    </div>
		    <div id="collapse${menu.id}" class="accordion-body collapse ${firstMenu?'in':''}"><c:set var="firstMenu" value="false"/>
				<div class="accordion-inner">
					<ul class="nav nav-list"><c:forEach items="${menuList}" var="menuChild"><c:if test="${menuChild.parent.id eq menu.id&&menuChild.isShow eq 1}">
						<li><a href="${fn:indexOf(menuChild.href, '://') eq -1?ctx:''}${menuChild.href}" target="${not empty menuChild.target?menuChild.target:'mainFrame'}" ><i class="icon-${not empty menuChild.icon?menuChild.icon:'circle-arrow-right'}"></i>&nbsp;&nbsp;${menuChild.name}</a></li>
					</c:if></c:forEach></ul>
				</div>
		    </div>
		</div>
	</c:if></c:forEach></div>
</body>
</html>
