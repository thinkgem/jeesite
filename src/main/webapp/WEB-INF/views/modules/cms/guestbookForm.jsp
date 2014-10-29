<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>留言管理</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		$(document).ready(function() {
			$("#reContent").focus();
			$("#inputForm").validate();
		});
	</script>
</head>
<body>
	<ul class="nav nav-tabs">
		<li><a href="${ctx}/cms/guestbook/">留言列表</a></li>
		<li class="active"><a href="${ctx}/cms/guestbook/form?id=${guestbook.id}">留言<shiro:hasPermission name="cms:guestbook:edit">${guestbook.delFlag eq '2'?'审核':'查看'}</shiro:hasPermission><shiro:lacksPermission name="cms:guestbook:edit">查看</shiro:lacksPermission></a></li>
	</ul><br/>
	
	<form:form id="inputForm" modelAttribute="guestbook" action="${ctx}/cms/guestbook/save" method="post" class="form-horizontal">
		<form:hidden path="id"/>
		<form:hidden path="delFlag"/>
		<tags:message content="${message}"/>
		
		<div class="control-group">
			<label class="control-label">名称:</label>
			<div class="controls">
				<c:out value="${guestbook.name}"/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">邮箱:</label>
			<div class="controls">
				<c:out value="${guestbook.email}"/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">电话:</label>
			<div class="controls">
				<c:out value="${guestbook.phone}"/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">单位:</label>
			<div class="controls">
				<c:out value="${guestbook.workunit}"/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">留言分类:</label>
			<div class="controls">
				<span style="font-weight:bold;"><c:out value="${fns:getDictLabel(guestbook.type, 'cms_guestbook', '无')}"/></span>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">IP:</label>
			<div class="controls">
				<c:out value="${guestbook.ip}"/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">留言时间:</label>
			<div class="controls">
				<fmt:formatDate value="${guestbook.createDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">留言内容:</label>
			<div class="controls">
				<form:textarea path="content" htmlEscape="false" rows="4" maxlength="200" class="input-xxlarge" disabled="true"/>
			</div>
		</div>
		<c:if test="${not empty guestbook.reUser}">
			<div class="control-group">
				<label class="control-label">回复人:</label>
				<div class="controls">
					<c:out value="${guestbook.reUser.name}"/>
				</div>
			</div>
			<div class="control-group">
				<label class="control-label">回复时间:</label>
				<div class="controls">
					<fmt:formatDate value="${guestbook.reDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</div>
			</div>
		</c:if>
		<div class="control-group">
			<label class="control-label">回复内容:</label>
			<div class="controls">
				<form:textarea path="reContent" htmlEscape="false" rows="4" maxlength="200" class="required input-xxlarge"/>
			</div>
		</div>
		<div class="form-actions">
			<c:if test="${guestbook.delFlag eq '2'}">
				<shiro:hasPermission name="cms:guestbook:edit">
					<input id="btnSubmit" class="btn btn-success" type="submit" value="通 过" onclick="$('#delFlag').val('0')"/>&nbsp;
					<input id="btnSubmit" class="btn btn-inverse" type="submit" value="驳 回" onclick="$('#delFlag').val('1')"/>&nbsp;
				</shiro:hasPermission>
			</c:if>
			<input id="btnCancel" class="btn" type="button" value="返 回" onclick="history.go(-1)"/>
		</div>
	</form:form>
</body>
</html>