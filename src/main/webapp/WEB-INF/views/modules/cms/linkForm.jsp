<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>链接管理</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		$(document).ready(function() {
			$("#name").focus();
			$("#inputForm").validate({
				submitHandler: function(form){
					if ($("#categoryId").val()==""){
						$("#categoryName").focus();
						top.$.jBox.tip('请选择归属栏目','warning');
					}else{
						loading('正在提交，请稍等...');
						form.submit();
					}
				}
			});
		});
	</script>
</head>
<body>
	<ul class="nav nav-tabs">
		<li><a href="${ctx}/cms/link/?category.id=${link.category.id}">链接列表</a></li>
		<li class="active"><a href="<c:url value='${fns:getAdminPath()}/cms/link/form?id=${link.id}&category.id=${link.category.id}'><c:param name='category.name' value='${link.category.name}'/></c:url>">链接<shiro:hasPermission name="cms:link:edit">${not empty link.id?'修改':'添加'}</shiro:hasPermission><shiro:lacksPermission name="cms:link:edit">查看</shiro:lacksPermission></a></li>
	</ul><br/>
	
	<form:form id="inputForm" modelAttribute="link" action="${ctx}/cms/link/save" method="post" class="form-horizontal">
		<form:hidden path="id"/>
		<tags:message content="${message}"/>
		
		<div class="control-group">
			<label class="control-label">归属栏目:</label>
			<div class="controls">
                <tags:treeselect id="category" name="category.id" value="${link.category.id}" labelName="category.name" labelValue="${link.category.name}"
					title="栏目" url="/cms/category/treeData" module="link" selectScopeModule="true" notAllowSelectRoot="false" notAllowSelectParent="true" cssClass="required"/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label" for="title">名称:</label>
			<div class="controls">
				<form:input path="title" htmlEscape="false" maxlength="200" class="input-xxlarge required measure-input"/>
				&nbsp;<label>颜色:</label>
				<form:select path="color" class="input-mini">
					<form:option value="" label="默认"/>
					<form:options items="${fns:getDictList('color')}" itemLabel="label" itemValue="value" htmlEscape="false" />
				</form:select>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">链接图片:</label>
			<div class="controls">
				<form:hidden path="image" htmlEscape="false" maxlength="255" class="input-xlarge"/>
				<tags:ckfinder input="image" type="images" uploadPath="/cms/link" selectMultiple="false"/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label" for="href">链接地址:</label>
			<div class="controls">
				<form:input path="href" htmlEscape="false" maxlength="255" class="input-xxlarge"/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label" for="weight">权重:</label>
			<div class="controls">
				<form:input path="weight" htmlEscape="false" maxlength="200" class="input-mini required digits"/>&nbsp;
				<span>
					<input id="weightTop" type="checkbox" onclick="$('#weight').val(this.checked?'999':'0')"><label for="weightTop">置顶</label>
				</span>
				&nbsp;过期时间：
				<input id="weightDate" name="weightDate" type="text" readonly="readonly" maxlength="20" class="input-small Wdate"
					value="<fmt:formatDate value="${link.weightDate}" pattern="yyyy-MM-dd"/>"
					onclick="WdatePicker({dateFmt:'yyyy-MM-dd',isShowClear:true});"/>
				<span class="help-inline">数值越大排序越靠前，过期时间可为空，过期后取消置顶。</span>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label" for="remarks">备注:</label>
			<div class="controls">
				<form:textarea path="remarks" htmlEscape="false" rows="4" maxlength="200" class="input-xxlarge"/>
			</div>
		</div>
		<shiro:hasPermission name="cms:article:audit">
			<div class="control-group">
				<label class="control-label" for="delFlag">发布状态:</label>
				<div class="controls">
					<form:radiobuttons path="delFlag" items="${fns:getDictList('cms_del_flag')}" itemLabel="label" itemValue="value" htmlEscape="false" class="required"/>
					<span class="help-inline"></span>
				</div>
			</div>
		</shiro:hasPermission>
		<div class="form-actions">
			<shiro:hasPermission name="cms:link:edit">
				<input id="btnSubmit" class="btn btn-primary" type="submit" value="保 存"/>&nbsp;
			</shiro:hasPermission>
			<input id="btnCancel" class="btn" type="button" value="返 回" onclick="history.go(-1)"/>
		</div>
	</form:form>
</body>
</html>