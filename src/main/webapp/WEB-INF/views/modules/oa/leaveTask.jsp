<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>请假办理</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		$(document).ready(function() {
			$(".handle").click(function(){
				var obj = $(this);
				var taskId = obj.data("tid");
				var leaveId = obj.data("id");
				var tkey=obj.data("tkey");
				//部门领导审批
				if(tkey=="deptLeaderAudit") {
					$.getJSON("${ctx}/oa/leave/detail/" + leaveId , function(data){
						 var html= Mustache.render($("#auditTemplate").html(),data);
						 top.$.jBox(html, { title: "流程办理["+obj.data("tname") + "]",buttons:{"同意":"yes","驳回":"no","取消":"cancel"},submit: function (v, h, f) {
							 //同意
							 if(v=="yes") {
									complete(taskId, [{
										key: 'deptLeaderPass',
										value: true,
										type: 'B'
									}]);
							//驳回
							 } else if (v=="no") {
								 top.$.jBox("<div style='padding:10px;'><textarea id='leaderBackReason' style='width: 250px; height: 60px;'></textarea></div>", { title: "请填写驳回理由", submit: function () {
									 var leaderBackReason=top.$("#leaderBackReason").val();
									 //必须填写驳回理由
									 if($.trim(leaderBackReason)=="") {
										 top.$.jBox.error('请填写驳回理由', '错误');
										 return false;
									 } else {
											complete(taskId, [{
												key: 'deptLeaderPass',
												value: false,
												type: 'B'
											}, {
												key: 'leaderBackReason',
												value: leaderBackReason,
												type: 'S'
											}]);
									 }
								 }
								});
							 }
						 }
						 });
					});
				}
				//人事审批
				if(tkey=="hrAudit") {
					$.getJSON("${ctx}/oa/leave/detail/" + leaveId , function(data){
						 var html= Mustache.render($("#auditTemplate").html(),data);
						 top.$.jBox(html, { title: "流程办理["+obj.data("tname") + "]",buttons:{"同意":"yes","驳回":"no","取消":"cancel"},submit: function (v, h, f) {
							 //同意
							 if(v=="yes") {
									complete(taskId, [{
										key: 'hrPass',
										value: true,
										type: 'B'
									}]);
							 }
							 //驳回
							 else if (v=="no") {
								 top.$.jBox("<div style='padding:10px;'><textarea id='hrBackReason' style='width: 250px; height: 60px;'></textarea></div>", { title: "请填写驳回理由", submit: function () {
									 var hrBackReason=top.$("#hrBackReason").val();
									 //必须填写驳回理由
									 if($.trim(hrBackReason)=="") {
										 top.$.jBox.error('请填写驳回理由', '错误');
										 return false;
									 } else {
										complete(taskId, [{
											key: 'hrPass',
											value: false,
											type: 'B'
										}, {
											key: 'hrBackReason',
											value: hrBackReason,
											type: 'S'
										}]);
									 }
								   }
								});
							 }
						  }
					   });
					});
				}
				//调整申请
				if(tkey=="modifyApply") {
					$.getJSON("${ctx}/oa/leave/detail-with-vars/" + leaveId + "/" + taskId, function(data){
						 var html= Mustache.render($("#modifyApplyTemplate").html(),data);
						 top.$.jBox(html, { title: "流程办理["+obj.data("tname") + "]",buttons:{"重新申请":"yes","放弃申请":"no","取消":"cancel"},submit: function (v, h, f) {
							 //重新申请或者取消申请
							 var reApply=true;
							 if(v=="no") {
								 reApply=false;
							 }
							 if(v=="yes"|| v=="no") {
								complete(taskId, [{
									key: 'reApply',
									value: reApply,
									type: 'B'
								}, {
									key: 'leaveType',
									value: top.$('#modifyApplyContent #leaveType').val(),
									type: 'S'
								}, {
									key: 'startTime',
									value: top.$('#modifyApplyContent #startTime').val(),
									type: 'D'
								}, {
									key: 'endTime',
									value: top.$('#modifyApplyContent #endTime').val(),
									type: 'D'
								}, {
									key: 'reason',
									value: top.$('#modifyApplyContent #reason').val(),
									type: 'S'
								}]);
							 } 
						 	}
						 });
						 top.$("#leaveType").val(data.leaveType);
					});
				}
				
				//销假
				if(tkey=="reportBack") {
					$.getJSON("${ctx}/oa/leave/detail/" +leaveId , function(data){
						 var html= Mustache.render($("#reportBackTemplate").html(),data);
						 top.$.jBox(html, { title: "流程办理["+obj.data("tname") + "]",buttons:{"提交":"yes","取消":"cancel"},submit: function (v, h, f) {
							 //同意
							 if(v=="yes") {
								var realityStartTime = top.$('#realityStartTime').val();
								var realityEndTime = top.$('#realityEndTime').val();	
								if (realityStartTime == '' || realityEndTime=="") {
									top.$.jBox.error('请选择实际开始时间和实际结束日期！');
									return false;
								} else {
									complete(taskId, [{
										key: 'realityStartTime',
										value: realityStartTime,
										type: 'D'
									}, {
										key: 'realityEndTime',
										value: realityEndTime,
										type: 'D'
									}]);
								}
							 }
						 }
						 });
					});
				}
				
			})
		});
		
		/**
		 * 完成任务
		 * @param {Object} taskId
		 */
		function complete(taskId, variables) {
			// 转换JSON为字符串
		    var keys = "", values = "", types = "";
			if (variables) {
				$.each(variables, function(idx) {
					if (keys != "") {
						keys += ",";
						values += ",";
						types += ",";
					}
					keys += this.key;
					values += this.value;
					types += this.type;
				});
			}
			// 发送任务完成请求
		    $.post('${ctx}/act/task/complete/', {
		    	taskId: taskId,
		        "vars.keys": keys,
		        "vars.values": values,
		        "vars.types": types
		    }, function(data) {
		        top.$.jBox.tip('任务完成');
		        //location = '${pageContext.request.contextPath}' + data;
		        location.reload();
		    });
		}

		/**
		 * 签收任务
		 * @param {Object} taskId
		 */
		function claim(taskId) {
			$.get('${ctx}/act/task/claim' ,{taskId: taskId}, function(data) {
	        	top.$.jBox.tip('签收完成');
	            //location = '${pageContext.request.contextPath}' + data;
	        	location.reload();
		    });
		}
	</script>
	<script type="text/template" id="auditTemplate">
		<table class="table table-striped ">
			<tr>
				<td width="100px;">申请人：</td>
				<td>{{user.name}}</td>
			</tr>
			<tr>
				<td>假种：</td>
				<td>{{leaveTypeDictLabel}}</td>
			</tr>
			<tr>
				<td>申请时间：</td>
				<td>{{createDate}}</td>
			</tr>
			<tr>
				<td>请假<font color="red">开始</font>时间：</td>
				<td>{{startTime}}</td>
			</tr>
			<tr>
				<td>请假<font color="red">结束</font>时间：</td>
				<td>{{endTime}}</td>
			</tr>
			<tr>
				<td>请假事由：</td>
				<td>{{reason}}</td>
			</tr>
		</table>
	</script>
	<script type="text/template" id="reportBackTemplate">
		<table class="table table-striped ">
			<tr>
				<td width="100px;">申请人：</td>
				<td>{{user.name}}</td>
			</tr>
			<tr>
				<td>假种：</td>
				<td>{{leaveTypeDictLabel}}</td>
			</tr>
			<tr>
				<td>申请时间：</td>
				<td>{{createDate}}</td>
			</tr>
			<tr>
				<td>请假<font color="red">开始</font>时间：</td>
				<td>{{startTime}}</td>
			</tr>
			<tr>
				<td>请假<font color="red">结束</font>时间：</td>
				<td>{{endTime}}</td>
			</tr>
			<tr>
				<td>请假事由：</td>
				<td>{{reason}}</td>
			</tr>
			<tr>
				<td>实际开始时间：</td>
				<td>
					<input id="realityStartTime" readonly="readonly" maxlength="20" class=" Wdate required"
							onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm',isShowClear:false});"/>
				</td>
			</tr>
			<tr>
				<td>实际结束时间：</td>
				<td>
					<input id="realityEndTime" readonly="readonly" maxlength="20" class=" Wdate required" 
							onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm',isShowClear:false});"/>
				</td>
			</tr>
		</table>
	</script>
	<script type="text/template" id="modifyApplyTemplate">
		<table class="table table-striped " id="modifyApplyContent">
			<tr>
				<td>部门领导意见：</td>
				<td>
					{{variables.leaderBackReason}}
				</td>
			</tr>
			<tr>
				<td>人事意见：</td>
				<td>
					{{variables.hrBackReason}}
				</td>
			</tr>
			<tr>
				<td>假种：</td>
				<td>
					<select id="leaveType" name="leaveType">
						<c:forEach items="${fns:getDictList('oa_leave_type')}" var="leaveType">
							<option value="${leaveType.value}">${leaveType.label}</option>
						</c:forEach>
					</select>
				</td>
			</tr>
			<tr>
				<td>请假<font color="red">开始</font>时间：</td>
				<td>
					<input id="startTime" name="startTime" type="text" readonly="readonly" maxlength="20" class=" Wdate required"
						onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm',isShowClear:false});" value="{{startTime}}"/>
				</td>
			</tr>
			<tr>
				<td>请假<font color="red">结束</font>时间：</td>
				<td>
					<input id="endTime" name="endTime" type="text" readonly="readonly" maxlength="20" class=" Wdate required"
						onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm',isShowClear:false});" value="{{endTime}}"/>
				</td>
			</tr>
			<tr>
				<td>请假事由：</td>
				<td><textarea id="reason" name="reason" class="required" rows="5">{{reason}}</textarea></td>
			</tr>
		</table>
	</script>
</head>
<body>
	<ul class="nav nav-tabs">
		<li class="active"><a href="${ctx}/oa/leave/">待办任务</a></li>
		<li><a href="${ctx}/oa/leave/list">所有任务</a></li>
		<shiro:hasPermission name="oa:leave:edit"><li><a href="${ctx}/oa/leave/form">请假申请</a></li></shiro:hasPermission>
	</ul>
	<sys:message content="${message}"/>
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
			<tr>
				<th>假种</th>
				<th>申请人</th>
				<th>申请时间</th>
				<th>开始时间</th>
				<th>结束时间</th>
				<th>当前节点</th>
				<th>任务创建时间</th>
				<th>流程状态</th>
				<th>操作</th>
			</tr>
		<tbody>
			<c:forEach items="${leaves}" var="leave">
				<c:set var="task" value="${leave.task}" />
				<c:set var="pi" value="${leave.processInstance}" />
				<tr id="${leave.id }" tid="${task.id}">
					<td>${leave.leaveTypeDictLabel}</td>
					<td>${leave.user.name}</td>
					<td><fmt:formatDate value="${leave.createDate}" type="both"/></td>
					<td><fmt:formatDate value="${leave.startTime}" type="both"/></td>
					<td><fmt:formatDate value="${leave.endTime}" type="both"/></td>
					<td>${task.name}</td>
					<td><fmt:formatDate value="${task.createTime}" type="both"/></td>
					<td>${pi.suspended ? "已挂起" : "正常" }；<b title='流程版本号'>V: ${leave.processDefinition.version}</b></td>
					<td>
						<a target="_blank" href="${ctx}/act/task/trace/photo/${task.processDefinitionId}/${task.executionId}">跟踪</a>
						<c:if test="${empty task.assignee}">
							<a class="claim" href="#" onclick="javescript:claim('${task.id}');">签收</a>
						</c:if>
						<c:if test="${not empty task.assignee}">
							<%-- 此处用tkey记录当前节点的名称 --%>
							<a class="handle" href="#" data-tkey="${task.taskDefinitionKey}" data-tname="${task.name}"  data-id="${leave.id}"  data-tid="${task.id}">办理</a>
						</c:if>
					</td>
				</tr>
			</c:forEach>
		</tbody>
	</table>
</body>
</html>
