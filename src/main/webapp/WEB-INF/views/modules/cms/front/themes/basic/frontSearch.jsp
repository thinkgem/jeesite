<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/modules/cms/front/include/taglib.jsp"%>
<!DOCTYPE html>
<html>
<head>
	<title>全站搜索</title>
	<meta name="decorator" content="cms_default_${site.theme}"/>
	<meta name="description" content="${site.description}" />
	<meta name="keywords" content="${site.keywords}" />
	<script src="${ctxStatic}/My97DatePicker/WdatePicker.js" type="text/javascript"></script>
	<style type="text/css">
		form.search{margin:12px 20px 5px;} .page{margin:20px;}
		form.search input.txt{padding:3px;font-size:16px;width:300px;margin:5px;}
		form.search select.txt{padding:3px;font-size:16px;width:308px;margin:5px;}
		form.search input.txt.date{width:133px;}
		form.search .sel{margin-bottom:8px;padding:0 0 10px 5px;border-bottom:1px solid #efefef;font-size:14px;} form.search .act{font-weight:bold;}
		form.search .btn{padding:3px 18px;*padding:1px 0 0;font-size:16px;}
		dl.search{line-height:25px;border-bottom:1px solid #efefef;margin:10px 20px 20px;}
		dl.search dt{border-top:1px solid #efefef;padding:8px 5px 0px;font-size:16px;}
		dl.search dt a.title{color:#0000cc;text-decoration:underline;}
		dl.search dd{margin:0 5px 10px;font-size:14px;color:#555}
		dl.search dd span,dl.search dd a{font-size:12px;color:#008000;}
		dl.search .highlight{color:#DF0037;}
		dl.search dd span.highlight{color:#DF0037;font-size:14px;}
		dl.search dd span.info span.highlight{color:#DF0037;font-size:13px;}
	</style>
	<c:if test="${not empty message}"><script type="text/javascript">alert("${message}");</script></c:if>
</head>
<body>
	<form:form id="searchForm" method="get" class="search">
		<input type="hidden" id="pageNo" name="pageNo" value="${page.pageNo}"/>
		<input type="hidden" id="t" name="t" value="${not empty t?t:'article'}"/>
		<input type="hidden" id="cid" name="cid" value="${cid}"/>
		<input type="hidden" id="a" name="a" value="${not empty t?t:'0'}"/>
		<div class="sel">
			<a href="javascript:" onclick="$('#t').val('article');$('.sel a').removeClass('act');$(this).addClass('act')" class="${empty t || t eq 'article'?'act':''}">文章搜索</a> &nbsp;
			<a href="javascript:" onclick="$('#t').val('guestbook');$('.sel a').removeClass('act');$(this).addClass('act')" class="${t eq 'guestbook'?'act':''}">留言搜索</a>
		</div>
		<c:choose>
			<c:when test="${param.a eq '1'}">
				<table>
					<tr><td>包含以下<strong>任意一个</strong>关键词</td><td><input type="text" name="q" value="${q}" class="txt"/>
						<input type="submit" value="搜  索" class="btn" onclick="$('#a').val('1')"/>
						<input type="submit" value="简单搜索" class="btn" onclick="$('#a').val('0')"/></td></tr>
					<tr><td>包含以下<strong>全部</strong>的关键词</td><td><input type="text" name="qand" value="${qand}" class="txt"/></td></tr>
					<tr><td><strong>不包含</strong>以下关键词</td><td><input type="text" name="qnot" value="${qnot}" class="txt"/></td></tr>
					<tr><td>检索结果每页显示的条数</td><td>
						<select name="pageSize" class="txt">
							<option value="10"${page.pageSize eq '10'?' selected':''}>每页显示10条</option>
							<option value="20"${page.pageSize eq '20'?' selected':''}>每页显示20条</option>
							<option value="30"${page.pageSize eq '30'?' selected':''}>每页显示30条</option>
							<option value="40"${page.pageSize eq '40'?' selected':''}>每页显示40条</option>
							<option value="50"${page.pageSize eq '50'?' selected':''}>每页显示50条</option>
						</select></td></tr>
					<tr><td><c:if test="${empty t || t eq 'article'}">最后更新日期范围</c:if><c:if test="${t eq 'guestbook'}">留言日期范围</c:if></td><td>
						<input id="bd" name="bd" type="text" readonly="readonly" maxlength="20" class="txt date"
							value="${param.bd}" onclick="WdatePicker({dateFmt:'yyyy-MM-dd',isShowClear:true});"/>&nbsp;~&nbsp;
						<input id="ed" name="ed" type="text" readonly="readonly" maxlength="20" class="txt date"
							value="${param.ed}" onclick="WdatePicker({dateFmt:'yyyy-MM-dd',isShowClear:true});"/>
					</td></tr>
				</table>
			</c:when><c:otherwise>
				<input type="hidden" id="pageSize" name="pageSize" value="${page.pageSize}"/>
				<input type="text" name="q" value="${q}" class="txt"/>
				<input type="submit" value="搜  索" class="btn" onclick="$('#a').val('0')"/>
				<input type="submit" value="高级搜索" class="btn" onclick="$('#a').val('1')"/>
			</c:otherwise>
		</c:choose>
	</form:form>
	<dl class="search">
		<c:if test="${empty t || t eq 'article'}">
			<c:forEach items="${page.list}" var="article">
				<dt><a href="${ctx}/view-${article.category.id}-${article.id}${urlSuffix}" class="title" target="_blank">${article.title}</a></dt>
				<dd>${article.description}<span class="info"><br/>发布者：${article.createBy.name} &nbsp; 点击数：${article.hits} &nbsp; 发布时间：<fmt:formatDate value="${article.createDate}" pattern="yyyy-MM-dd HH:mm:ss"/> &nbsp; 更新时间：<fmt:formatDate value="${article.updateDate}" pattern="yyyy-MM-dd HH:mm:ss"/></span>
					&nbsp;&nbsp;<a href="${ctx}/view-${article.category.id}-${article.id}${urlSuffix}" target="_blank">查看全文</a><br/></dd>
			</c:forEach>
		</c:if>
		<c:if test="${t eq 'guestbook'}">
			<c:forEach items="${page.list}" var="guestbook"><dt>${fns:getDictLabel(guestbook.type,'cms_guestbook','')}</dt>
				<dd>${guestbook.content}<span class="info"><br/>姓名：${guestbook.name} &nbsp; 留言时间：<fmt:formatDate value="${guestbook.createDate}" pattern="yyyy-MM-dd HH:mm:ss"/></span></dd>
				<dd>回复：${guestbook.reContent}<span class="info"><br/>回复人：${guestbook.reUser.name} &nbsp; 回复时间：<fmt:formatDate value="${guestbook.reDate}" pattern="yyyy-MM-dd HH:mm:ss"/></span></dd>
			</c:forEach>
		</c:if>
		<c:if test="${fn:length(page.list) eq 0}">
			<dt><c:if test="${empty q}">请键入要查找的关键字。</c:if><c:if test="${not empty q}">抱歉，没有找到与“${q}”相关内容。</c:if><br/><br/>建议：</dt>
			<dd><ul><li>检查输入是否正确；</li><li>简化输入词；</li><li>尝试其他相关词，如同义、近义词等。</li></ul></dd>
		</c:if>
	</dl>
	<div class="pagination">${page}</div>
	<script type="text/javascript">
		function page(n,s){
			$("#pageNo").val(n);
			$("#pageSize").val(s);
			$("#searchForm").submit();
	    	return false;
	    }
	</script>
</body>
</html>