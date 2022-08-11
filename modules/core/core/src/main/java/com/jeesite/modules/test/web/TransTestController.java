/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 * No deletion without permission, or be held responsible to law.
 */
package com.jeesite.modules.test.web;

import com.jeesite.modules.test.entity.TestTree;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.cloud.commons.lang.StringUtils;
import com.jeesite.common.config.Global;
import com.jeesite.common.web.BaseController;
import com.jeesite.modules.test.entity.TestData;
import com.jeesite.modules.test.service.TransTestService;

/**
 * 分布式事务测试
 * @author ThinkGem
 * @version 2020-1-9
 */
@Controller
@RequestMapping(value = "${adminPath}/trans")
public class TransTestController extends BaseController {

	@Autowired
	private TransTestService transTestService;

	private int i = 0;

	/**
	 * 查询列表数据
	 */
	@RequestMapping(value = "test")
	@ResponseBody
	public String test(TestData testData) {
		String mode = StringUtils.EMPTY;
		try {
			Class.forName("io.seata.spring.annotation.GlobalTransactionScanner");
			mode = "GlobalTransactional";
		} catch (ClassNotFoundException e) { }
		if (StringUtils.isBlank(mode)) {
			return renderResult(Global.FALSE, "测试失败，没有安装 Seata 模块！");
		}
		boolean normal = ++i % 2 == 0; // 正常报错和故意失败模式切换
		try{
			transTestService.transTest(testData, normal);
		}catch (Exception e) {
			logger.debug("事务测试信息，报错回滚：" + e.getMessage(), e);
		}
		if (normal) {
			boolean bl1 = transTestService.transValid(testData);
			boolean bl2 = transTestService.transValid(new TestTree(testData.getId()));
			String message = "事务测试"+(!bl1&&!bl2?"成功，数据已正常":"失败，数据未")+"保存！";
			return renderResult(Global.TRUE, message);
		}else{
			boolean bl = transTestService.transValid(testData);
			String message = "事务测试"+(bl?"成功，数据已":"失败，数据未")+"回滚！";
			if (!bl) {
				message += "请全局搜索“@"+mode+"”关键词，是否将前面的“//”注释去掉？";
				message += "是否开启 seata.enabled 参数配置？";
			}
			return renderResult(Global.TRUE, message);
		}
	}

}