/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 * No deletion without permission, or be held responsible to law.
 */
package com.jeesite.modules.app.web;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.jeesite.common.config.Global;
import com.jeesite.common.web.BaseController;
import com.jeesite.modules.app.entity.AppComment;
import com.jeesite.modules.app.entity.AppUpgrade;
import com.jeesite.modules.app.service.AppCommentService;
import com.jeesite.modules.app.service.AppUpgradeService;

/**
 * APP公共服务 Controller
 * @author ThinkGem
 * @version 2021-04-09
 */
@Controller
@RequestMapping(value = "/app")
public class AppPublicServiceController extends BaseController {

	@Autowired
	private AppUpgradeService appUpgradeService;

	@Autowired
	private AppCommentService appCommentService;
	
	/**
	 * 升级检测
	 */
	@RequestMapping(value = "/upgrade/check")
	@ResponseBody
	public String upgradeCheck(String appCode, Integer appVersion) {
		AppUpgrade appUpgrade = new AppUpgrade();
		appUpgrade.setAppCode(appCode);
		appUpgrade.setUpVersion_gt(appVersion); // 查询大于这个版本的记录
		List<AppUpgrade> list = appUpgradeService.findList(appUpgrade);
		if (list.size() > 0) {
			return renderResult(Global.TRUE, text("检测到新版本！"), list.get(0));
		}
		return renderResult(Global.FALSE, text("当前已是最新版！"));
	}
	
	/**
	 * 意见反馈
	 */
	@PostMapping(value = "/comment/save")
	@ResponseBody
	public String commentSave(@Validated AppComment appComment) {
		appComment.setStatus(AppComment.STATUS_NORMAL);
		appCommentService.save(appComment);
		return renderResult(Global.TRUE, text("我们已收到您的宝贵意见，感谢您的反馈！"));
	}
	
}