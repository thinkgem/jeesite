<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/modules/cms/front/include/taglib.jsp"%>
<link href="${ctxStatic}/jquery-validation/1.11.1/jquery.validate.min.css" type="text/css" rel="stylesheet" />
<script src="${ctxStatic}/jquery-validation/1.11.1/jquery.validate.min.js" type="text/javascript"></script>
<script src="${ctxStatic}/jquery-validation/1.11.1/jquery.validate.method.min.js" type="text/javascript"></script>
<style type="text/css">.reply{border:1px solid #ddd;background:#fefefe;margin:10px;}</style>
<script type="text/javascript">
	$(document).ready(function() {
		comment(0);
	});
	function commentForm(form){
		$(form).validate({
			rules: {
				validateCode: {remote: "${pageContext.request.contextPath}/servlet/validateCodeServlet"}
			},
			messages: {
				content: {required: "请填写评论内容"},
				validateCode: {remote: "验证码不正确", required: "请填写验证码"}
			},
			submitHandler: function(form){
			    $.post($(form).attr("action"), $(form).serialize(), function(data){
			    	data = eval("("+data+")");
			    	alert(data.message);
			    	if (data.result==1){
			    		page(1);
			    	}
			    });
			},
			errorContainer: form + " .messageBox",
			errorPlacement: function(error, element) {
				if (element.is(":checkbox")||element.is(":radio")||element.parent().is(".input-append")){
					error.appendTo(element.parent().parent());
				} else {
					error.insertAfter(element);
				}
			}
		});
	}
	function comment(id){
		if ($("#commentForm"+id).html()==""){
			$(".validateCodeRefresh").click();
			$(".commentForm").hide(500,function(){$(this).html("");});
			$("#commentForm"+id).html($("#commentFormTpl").html()).show(500);
			$("#commentForm"+id+" input[name='replyId']").val(id);
			commentForm("#commentForm"+id + " form");
		}else{
			$("#commentForm"+id).hide(500,function(){$(this).html("");});
		}
	}
</script>
<h5>评论列表</h5>
<ul>
	<c:forEach items="${page.list}" var="comment">
		<li>
			<h6>姓名: ${comment.name} &nbsp;时间：<fmt:formatDate value="${comment.createDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
				<a href="javascript:comment('${comment.id}')">回复</a></h6>
			<div>${comment.content}</div>
			<div id="commentForm${comment.id}" class="commentForm hide"></div>
		</li>
	</c:forEach>
	<c:if test="${fn:length(page.list) eq 0}">
		<li>暂时还没有人评论！</li>
	</c:if>
</ul>
<div class="pagination">${page}</div>
<h5>我要评论</h5>
<div id="commentForm0"></div>
<script id="commentFormTpl" type="text/javascript"><!--/*-->
	<form:form action="${ctx}/comment" method="post" class="form-horizontal">
		<input type="hidden" name="category.id" value="${comment.category.id}"/>
		<input type="hidden" name="contentId" value="${comment.contentId}"/>
		<input type="hidden" name="title" value="${comment.title}"/>
		<input type="hidden" name="replyId"/>
		<div class="control-group">
			<label class="control-label">留言内容:</label>
			<div class="controls">
				<textarea name="content" rows="4" maxlength="200" class="txt required" style="width:400px;"></textarea>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">姓名:</label>
			<div class="controls">
				<input type="text" name="name" maxlength="11" class="txt required" style="width:100px;" value="匿名"/>
				<label class="mid">验证码:</label><sys:validateCode name="validateCode" />
				<input class="btn mid" type="submit" value="提 交"/>&nbsp;
			</div>
		</div>
		<div class="alert alert-error messageBox" style="display:none">输入有误，请先更正。</div>
	</form:form><!--*/-->
</script>