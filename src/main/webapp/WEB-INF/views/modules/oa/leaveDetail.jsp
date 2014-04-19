<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>请假详细</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		$(document).ready(function() {
			$("#name").focus();
			$("#inputForm").validate({
				submitHandler: function(form){
					loading('正在提交，请稍等...');
					form.submit();
				},
				errorContainer: "#messageBox",
				errorPlacement: function(error, element) {
					$("#messageBox").text("输入有误，请先更正。");
					if (element.is(":checkbox")||element.is(":radio")){
						error.appendTo(element.parent().parent());
					} else {
						error.insertAfter(element);
					}
				}
			});
		});
		function auditPass(isPass) {
			top.$.jBox.confirm("确认提交数据？","系统提示",function(v,h,f){
			    if (v == 'ok') {
					$("#pass").val(isPass);
					$("#inputForm").submit();
			    }
			});
		}
	</script>
</head>
<body>
	<ul class="nav nav-tabs">
		<li><a href="${ctx}/oa/leave/">待办任务</a></li>
		<li><a href="${ctx}/oa/leave/list">所有任务</a></li>
		<li class="active"><a href="${ctx}/oa/leave/detail?id=${leave.id}">请假查看</a></li>
	</ul>
	<form class="form-horizontal">
		<div class="control-group">
			<label class="control-label">请假类型：</label>
			<div class="controls">
				${leave.leaveTypeDictLabel }
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">开始时间：</label>
			<div class="controls">
				${leave.startTime}
		</div>
		</div>
		<div class="control-group">
			<label class="control-label">结束时间：</label>
			<div class="controls">
				${leave.endTime}
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">请假原因：</label>
			<div class="controls">
				${leave.reason}
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">当前状态：</label>
			<div class="controls">
				${leave.processStatus}
			</div>
		</div>
	</form>
	<c:if  test="${not empty workflowEntity.historicTaskInstances}">
		<table id="contentTable" class="table table-striped table-bordered table-condensed">
			<thead><tr>
				<th>节点名称</th>
				<th>审批人</th>
				<th>审批时间</th>
				<th>审批备注</th>
			</tr></thead>
			<tbody>
				<c:forEach items="${workflowEntity.historicTaskInstances}" var="historicTaskInstance">
					<c:if test="${not empty  historicTaskInstance.endTime}">
						<tr>
							<td>${historicTaskInstance.name}</td>
							<td>${fns:getUserById(historicTaskInstance.assignee).name}</td>
							<td><fmt:formatDate value="${historicTaskInstance.endTime}" pattern="yyyy-MM-dd hh:mm:ss"/></td>
							<td>${workflowEntity.commentMap[historicTaskInstance.id]}</td>
						</tr>
					</c:if>
				</c:forEach>
			</tbody>
		</table>
	</c:if>
	<c:if  test="${leave.audit}">
		<c:if test="${leave.processStatus eq '部门领导审批'}">
			<form:form id="inputForm" modelAttribute="leave" action="${ctx}/oa/leave/deptLeaderAudit" method="post" class="form-horizontal">
				<form:hidden path="id"/>
				<form:hidden path="pass"/>
				<div class="control-group">
					<label class="control-label">审批备注：</label>
					<div class="controls">
						<form:textarea path="auditRemarks" class="required" rows="5" maxlength="200"/>
					</div>
				</div>
				<div class="form-actions">
					<input class="btn btn-primary" type="button" value="通过"  onclick="auditPass(true);"/>&nbsp;
					<input id="btnCancel" class="btn" type="button" value="返 回" onclick="history.go(-1)"/>
					<div class="pull-right">
						<input class="btn btn-warning" type="button" value="不通过" onclick="auditPass(false); " style="margin-right: 300px;"/>
					</div>
				</div>
			</form:form>
		</c:if>
		<c:if test="${leave.processStatus eq '调整申请'}">
			<form:form id="inputForm" modelAttribute="leave" action="${ctx}/oa/leave/modifyApply" method="post" class="form-horizontal">
				<form:hidden path="id"/>
				<form:hidden path="pass"/>
				<tags:message content="${message}"/>
				<div class="control-group">
					<label class="control-label">请假类型：</label>
					<div class="controls">
						<form:select path="leaveType" >
							<form:options items="${fns:getDictList('oa_leave_type')}" itemLabel="label" itemValue="value" htmlEscape="false" />
						</form:select>
					</div>
				</div>
				<div class="control-group">
					<label class="control-label">开始时间：</label>
					<div class="controls">
						<input id="startTime" name="startTime" type="text" readonly="readonly" maxlength="20" class="Wdate required"
							value="<fmt:formatDate value="${leave.startTime}" pattern="yyyy-MM-dd HH:mm:ss"/>"
							onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',isShowClear:false});"/>
				</div>
				</div>
				<div class="control-group">
					<label class="control-label">结束时间：</label>
					<div class="controls">
						<input id="endTime" name="endTime" type="text" readonly="readonly" maxlength="20" class="Wdate required"
							value="<fmt:formatDate value="${leave.endTime}" pattern="yyyy-MM-dd HH:mm:ss"/>"
							onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',isShowClear:false});"/>
					</div>
				</div>
				<div class="control-group">
					<label class="control-label">请假原因：</label>
					<div class="controls">
						<form:textarea path="reason" class="required" rows="5" maxlength="255"/>
					</div>
				</div>
				<div class="form-actions">
					<input class="btn btn-primary" type="button" value="保存"  onclick="auditPass(true);"/>&nbsp;
					<input id="btnCancel" class="btn" type="button" value="返 回" onclick="history.go(-1)"/>
					<div class="pull-right">
						<input class="btn btn-warning" type="button" value="取消申请" onclick="auditPass(false); " style="margin-right: 300px;"/>
					</div>
				</div>
			</form:form>
		</c:if>
		<c:if test="${leave.processStatus eq '人事审批'}">
			<form:form id="inputForm" modelAttribute="leave" action="${ctx}/oa/leave/hrAudit" method="post" class="form-horizontal">
				<form:hidden path="id"/>
				<form:hidden path="pass"/>
				<div class="control-group">
					<label class="control-label">审批备注：</label>
					<div class="controls">
						<form:textarea path="auditRemarks" class="required" rows="5" maxlength="200"/>
					</div>
				</div>
				<div class="form-actions">
					<input class="btn btn-primary" type="button" value="通过"  onclick="auditPass(true);"/>&nbsp;
					<input id="btnCancel" class="btn" type="button" value="返 回" onclick="history.go(-1)"/>
					<div class="pull-right">
						<input class="btn btn-warning" type="button" value="不通过" onclick="auditPass(false); " style="margin-right: 300px;"/>
					</div>
				</div>
			</form:form>
		</c:if>
		<c:if test="${leave.processStatus eq '销假'}">
			<form:form id="inputForm" modelAttribute="leave" action="${ctx}/oa/leave/reportBack" method="post" class="form-horizontal">
				<form:hidden path="id"/>
				<form:hidden path="pass"/>
				<div class="control-group">
					<label class="control-label">实际开始时间：</label>
					<div class="controls">
						<input id="realityStartTime" name="realityStartTime" type="text" readonly="readonly" maxlength="20" class="Wdate required"
							value="<fmt:formatDate value="${leave.startTime}" pattern="yyyy-MM-dd HH:mm:ss"/>"
							onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',isShowClear:false});"/>
				</div>
				</div>
				<div class="control-group">
					<label class="control-label">实际结束时间：</label>
					<div class="controls">
						<input id="realityEndTime" name="realityEndTime" type="text" readonly="readonly" maxlength="20" class="Wdate required"
							value="<fmt:formatDate value="${leave.endTime}" pattern="yyyy-MM-dd HH:mm:ss"/>"
							onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',isShowClear:false});"/>
					</div>
				</div>
				<div class="form-actions">
					<input class="btn btn-primary" type="submit" value="保存" />&nbsp;
					<input id="btnCancel" class="btn" type="button" value="返 回" onclick="history.go(-1)"/>
				</div>
			</form:form>
		</c:if>
	</c:if>
</body>
</html>
