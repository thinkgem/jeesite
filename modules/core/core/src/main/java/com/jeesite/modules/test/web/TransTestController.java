/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.modules.test.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

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
	
	/**
	 * 查询列表数据
	 */
	@RequestMapping(value = "test")
	@ResponseBody
	public String test(TestData testData) {
		try {
			Class.forName("com.codingapi.txlcn.tc.annotation.LcnTransaction");
		} catch (ClassNotFoundException e) {
			return renderResult(Global.FALSE, "测试失败，没有安装 LCN 模块！");
		}
		try{
			transTestService.transTest(testData);
		}catch (Exception e) {
			logger.debug("事务测试信息，报错回滚：" + e.getMessage(), e);
		}
		boolean bl = transTestService.transValid(testData);
		return renderResult(Global.TRUE, "事务测试"+(bl?"成功，数据已":"失败，数据未")+"回滚！");
	}

}