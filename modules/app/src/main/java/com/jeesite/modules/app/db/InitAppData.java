/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 * No deletion without permission, or be held responsible to law.
 */
package com.jeesite.modules.app.db;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import com.jeesite.common.callback.MethodCallback;
import com.jeesite.common.config.Global;
import com.jeesite.common.tests.BaseInitDataTests;
import com.jeesite.modules.app.entity.AppComment;
import com.jeesite.modules.app.entity.AppUpgrade;
import com.jeesite.modules.app.service.AppCommentService;
import com.jeesite.modules.app.service.AppUpgradeService;
import com.jeesite.modules.gen.utils.GenUtils;

/**
 * 初始化APP表及数据
 * @author ThinkGem
 * @version 2021-4-15
 */
@Component
@ConditionalOnProperty(name="jeesite.initdata", havingValue="true", matchIfMissing=false)
public class InitAppData extends BaseInitDataTests {

	@Override
	public boolean initData() throws Exception {
		if (GenUtils.isTableExists(Global.getTablePrefix() + "app_upgrade")) {
			return true; // 如果表已存在，则无需初始化
		}
		this.runCreateScript("app.sql");
		this.initModuleInfo("app");
		this.initModuleMenu("/app/appUpgrade/list");
		this.initModuleDict("app_upgrade_type");
		this.initAppUpgrade();
		this.initAppComment();
		return true;
	}
	
	@Autowired
	private AppUpgradeService appUpgradeService;
	public void initAppUpgrade() throws Exception{
//		clearTable(AppUpgrade.class);
		initExcelData(AppUpgrade.class, params -> {
			String action = (String)params[0];
			if("save".equals(action)){
				AppUpgrade entity = (AppUpgrade)params[1];
				entity.setIsNewRecord(true);
				appUpgradeService.save(entity);
				return null;
			}
			return null;
		});
	}
	
	@Autowired
	private AppCommentService appCommentService;
	public void initAppComment() throws Exception{
//		clearTable(AppComment.class);
		initExcelData(AppComment.class, params -> {
			String action = (String)params[0];
			if("save".equals(action)){
				AppComment entity = (AppComment)params[1];
				entity.setIsNewRecord(true);
				entity.setReplyDate(new Date());
				appCommentService.save(entity);
				return null;
			}
			return null;
		});
	}
	
}
