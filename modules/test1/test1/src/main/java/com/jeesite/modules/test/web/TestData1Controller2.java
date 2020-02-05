///**
// * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
// */
//package com.jeesite.modules.test.web;
//
//import java.util.List;
//import java.util.Map;
//
//import org.apache.commons.lang3.StringUtils;
//import org.apache.shiro.authz.annotation.RequiresPermissions;
//import org.springframework.stereotype.Controller;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.ResponseBody;
//
//import com.jeesite.common.collect.MapUtils;
//import com.jeesite.common.config.Global;
//import com.jeesite.common.entity.Page;
//import com.jeesite.common.idgen.IdGen;
//import com.jeesite.common.utils.SpringUtils;
//import com.jeesite.common.web.BaseController;
//import com.jeesite.modules.bpm.entity.BpmEntity;
//import com.jeesite.modules.bpm.entity.client.BpmProcIns;
//import com.jeesite.modules.bpm.entity.client.BpmTask;
//import com.jeesite.modules.bpm.service.client.BpmTaskServiceClient;
//import com.jeesite.modules.bpm.utils.client.BpmUtils;
//import com.jeesite.modules.sys.entity.EmpUser;
//
///**
// * 测试数据Controller
// * @author ThinkGem
// * @version 2020-2-4
// */
//@Controller
//@RequestMapping(value = "${adminPath}/test1/testData")
//public class TestData1Controller2 extends BaseController {
//
//	/**
//	 * 业务流程测试
//	 */
//	@RequiresPermissions("test:testData:edit")
//	@RequestMapping(value = "bpmTest")
//	@ResponseBody
//	public String bpmTest() {
//
//		// 模拟创建一个业务实体对象
//		BpmEntity<?> bpmEntity = new BpmEntity<EmpUser>(){
//			private static final long serialVersionUID = 1L;
//			private String bpmEntityId = IdGen.nextId();
//			@Override
//			public String getId() {
//				return bpmEntityId;
//			}
//		};
//		Map<String, Object> variables = MapUtils.newHashMap();
//		variables.put("leaveDays", 5);
//
//		// 启动流程
//		BpmTask start = BpmUtils.start(bpmEntity, "leave", variables, null);
//		System.out.println("启动流程实例："+start.getProcIns().getId());
//
//		// 根据业务表单获取流程实例
//		BpmProcIns procIns = BpmUtils.getProcIns(bpmEntity, "leave");
//		System.out.println("获取流程实例："+procIns.getId());
//
//		// 查询并完成流程实例下的任务，直到全部任务都已经完成
//		BpmTaskServiceClient bean = SpringUtils.getBean(BpmTaskServiceClient.class);
//		while(true){
//			BpmTask where = new BpmTask(procIns);
//			where.setStatus(BpmTask.STATUS_UNFINISHED);
//			Page<BpmTask> page = bean.findTaskPage(where);
//			List<BpmTask> list = page.getList();
//			System.out.println("====== 任务数："+list.size());
//			if (list.size() == 0){
//				break;
//			}
//			for (BpmTask task : list) {
//				bpmEntity.getBpm().setTaskId(task.getId());
//				if (StringUtils.isBlank(task.getAssignee())){
//					System.out.println("签收任务："+task.getId());
//					bean.claimTask(task);
//				}
//				System.out.println("转办任务："+task.getId());
//				task.setUserCode("admin");
//				bean.turnTask(task);
//				System.out.println("完成任务："+task.getId());
//				BpmUtils.complete(bpmEntity, variables, null);
//			}
//		}
//		return renderResult(Global.TRUE, "业务流程测试完成，请打开控制台，查看执行状态！");
//	}
//	
//}