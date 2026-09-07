/**
 * Copyright (c) 2013-Now https://jeesite.com All rights reserved.
 * No deletion without permission, or be held responsible to law.
 */
package com.jeesite.modules.ai.cms.web;

import com.jeesite.common.config.Global;
import com.jeesite.common.web.BaseController;
import com.jeesite.modules.ai.cms.service.AiCmsImageService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * AI 生图控制器类
 * @author ThinkGem
 */
@RestController
@RequestMapping("${adminPath}/cms/image")
public class CmsAiImageController extends BaseController {

	private final AiCmsImageService aiCmsImageService;

	public CmsAiImageController(AiCmsImageService aiCmsImageService) {
		this.aiCmsImageService = aiCmsImageService;
	}

	/**
	 * 是否启用生图模型
	 * @author ThinkGem
	 * http://127.0.0.1:8980/js/a/cms/image/enabled
	 */
	@RequestMapping(value = "enabled")
	public String enabled() {
		return renderResult(Global.TRUE, aiCmsImageService.isEnabled() ? "已启用生图模型" : "未启用生图模型",
				Map.of("enabled", aiCmsImageService.isEnabled()));
	}

	/**
	 * AI 生图
	 * @author ThinkGem
	 * http://127.0.0.1:8980/js/a/cms/image/generate?prompt=一只可爱的橘猫，坐在窗台上晒太阳
	 * http://127.0.0.1:8980/js/a/cms/image/generate?bizKey=会话ID&bizType=cms-chat&prompt=一只可爱的橘猫
	 */
	@RequestMapping(value = "generate")
	public String generate(String bizKey, String bizType, @RequestParam String prompt) {
		Map<String, Object> data = aiCmsImageService.generateImage(bizKey, bizType, prompt);
		return renderResult(Global.TRUE, "生图成功", data);
	}

}
