<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/modules/cms/front/include/taglib.jsp"%>
<!DOCTYPE html>
<html>
<head>
	<title>首页</title>
	<meta name="decorator" content="cms_default_${site.theme}"/>
	<meta name="description" content="JeeSite ${site.desciption}" />
	<meta name="keywords" content="JeeSite ${site.keywords}" />
	<link href="${ctxStatic}/jquery-validation/1.11.0/jquery.validate.min.css" type="text/css" rel="stylesheet" />
	<script src="${ctxStatic}/jquery-validation/1.11.0/jquery.validate.min.js" type="text/javascript"></script>
	<script src="${ctxStatic}/jquery-validation/1.11.0/jquery.validate.method.min.js" type="text/javascript"></script>
	<script type="text/javascript">
		$(document).ready(function() {
			<c:if test="${not empty message}">alert("${message}");</c:if>
			$("#inputForm").validate({
				rules: {
					validateCode: {remote: "${pageContext.request.contextPath}/servlet/validateCodeServlet"}
				},
				messages: {
					content: {required: "请填写留言内容"},
					validateCode: {remote: "验证码不正确"}
				},
				errorContainer: "#messageBox",
				errorPlacement: function(error, element) {
					if (element.is(":checkbox")||element.is(":radio")){
						error.appendTo(element.parent().parent());
					} else {
						error.insertAfter(element);
					}
				}
			});
		});
		function page(n,s){
			location="${ctx}/guestbook?pageNo="+n+"&pageSize="+s;;
		}
	</script>
</head>
<body>
	<div style="padding:0 0 20px;">
		<h4>公共留言</h4>
		<ul>
			<c:forEach items="${page.list}" var="guestbook">
				<li>
					<h5>姓名: ${guestbook.name} &nbsp;时间：<fmt:formatDate value="${guestbook.createDate}" pattern="yyyy-MM-dd HH:mm:ss"/></h5>
					<p>内容：${guestbook.content}</p>
					<h6>回复人：${guestbook.reUser.name} 时间：<fmt:formatDate value="${guestbook.reDate}" pattern="yyyy-MM-dd HH:mm:ss"/></h6>
					<p>回复内容：${guestbook.reContent}</p>
				</li>
			</c:forEach>
			<c:if test="${fn:length(page.list) eq 0}">
				<li>暂时还没有人留言！</li>
			</c:if>
		</ul>
		<div class="page">${page}</div>
		<h4>我要留言</h4>
		<form:form id="inputForm" action="" method="post" class="form">
			<div class="control-group">
				<label class="control-label">名称:</label>
				<div class="controls">
					<input type="text" name="name" maxlength="11" class="txt required" style="width:300px;"/>
				</div>
			</div>
			<div class="control-group">
				<label class="control-label">邮箱:</label>
				<div class="controls">
					<input type="text" name="email" maxlength="50" class="txt required email" style="width:300px;"/>
				</div>
			</div>
			<div class="control-group">
				<label class="control-label">电话:</label>
				<div class="controls">
					<input type="text" name="phone" maxlength="50" class="txt required phone" style="width:300px;"/>
				</div>
			</div>
			<div class="control-group">
				<label class="control-label">单位:</label>
				<div class="controls">
					<input type="text" name="workunit" maxlength="50" class="txt required" style="width:300px;"/>
				</div>
			</div>
			<div class="control-group">
				<label class="control-label">留言分类:</label>
				<div class="controls">
					<select name="type" class="txt required" style="width:100px;">
						<option value="">请选择</option>
						<c:forEach items="${fns:getDictList('cms_guestbook')}" var="type">
							<option value="${type.value}">${type.label}</option>
						</c:forEach>
					</select>
				</div>
			</div>
			<div class="control-group">
				<label class="control-label">留言内容:</label>
				<div class="controls">
					<textarea name="content" rows="4" maxlength="200" class="txt required" style="width:400px;"></textarea>
				</div>
			</div>
			<div class="control-group">
				<label class="control-label">验证码:</label>
				<div class="controls">
					<input type="text" name="validateCode" maxlength="5" class="txt required" style="font-weight:bold;width:45px;"/>
					<img class="validateCode" src="${pageContext.request.contextPath}/servlet/validateCodeServlet" style="vertical-align:middle;"/>
					<a href="javascript:" onclick="$('.validateCode').attr('src','${pageContext.request.contextPath}/servlet/validateCodeServlet?'+new Date().getTime());">看不清</a>
				</div>
			</div>
			<div class="form-actions">
				<input class="btn" type="submit" value="提 交"/>&nbsp;
			</div>
			<div id="messageBox" class="alert alert-error" style="display:none">输入有误，请先更正。</div>
		</form:form>
	</div>
</body>
</html>
