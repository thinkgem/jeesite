/**
 * Copyright (c) 2013-Now https://jeesite.com All rights reserved.
 * No deletion without permission, or be held responsible to law.
 */
package com.jeesite.modules.ai.web;

import com.jeesite.common.config.Global;
import com.jeesite.common.entity.Page;
import com.jeesite.common.web.BaseController;
import com.jeesite.modules.ai.entity.AiAgent;
import com.jeesite.modules.ai.service.AiAgentService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * AI智能体 Controller
 * @author ThinkGem
 * @version 2025-07-04
 */
@ConditionalOnProperty(prefix = "ai", name = "enabled", havingValue = "true", matchIfMissing = true)
@Controller
@RequestMapping(value = "${adminPath}/ai/agent")
public class AiAgentController extends BaseController {

	private final AiAgentService aiAgentService;

	public AiAgentController(AiAgentService aiAgentService) {
		this.aiAgentService = aiAgentService;
	}

	/**
	 * 获取数据
	 */
	@ModelAttribute
	public AiAgent get(String id, boolean isNewRecord) {
		return aiAgentService.get(id, isNewRecord);
	}
	
	/**
	 * 查询列表
	 */
	@RequiresPermissions("ai:agent:view")
	@RequestMapping(value = {"list", ""})
	public String list(AiAgent aiAgent, Model model) {
		model.addAttribute("aiAgent", aiAgent);
		return "modules/ai/aiAgentList";
	}
	
	/**
	 * 查询列表数据
	 */
	@RequiresPermissions("ai:agent:view")
	@RequestMapping(value = "listData")
	@ResponseBody
	public Page<AiAgent> listData(AiAgent aiAgent, HttpServletRequest request, HttpServletResponse response) {
		aiAgent.setPage(new Page<>(request, response));
		Page<AiAgent> page = aiAgentService.findPage(aiAgent);
		return page;
	}

	/**
	 * 查看编辑表单
	 */
	@RequiresPermissions("ai:agent:view")
	@RequestMapping(value = "form")
	public String form(AiAgent aiAgent, Model model) {
		// 回显关联的工具、MCP 服务与向量库绑定（关联表为空时回退 options 中的历史配置）
		aiAgentService.fillRelations(aiAgent);
		model.addAttribute("aiAgent", aiAgent);
		return "modules/ai/aiAgentForm";
	}

	/**
	 * 保存数据
	 */
	@RequiresPermissions("ai:agent:edit")
	@PostMapping(value = "save")
	@ResponseBody
	public String save(@Validated AiAgent aiAgent) {
		Global.assertDemoMode();
		aiAgentService.save(aiAgent);
		return renderResult(Global.TRUE, text("保存智能体成功！"));
	}
	
	/**
	 * 停用数据
	 */
	@RequiresPermissions("ai:agent:edit")
	@RequestMapping(value = "disable")
	@ResponseBody
	public String disable(AiAgent aiAgent) {
		Global.assertDemoMode();
		aiAgent.setStatus(AiAgent.STATUS_DISABLE);
		aiAgentService.updateStatus(aiAgent);
		return renderResult(Global.TRUE, text("停用智能体成功！"));
	}
	
	/**
	 * 启用数据
	 */
	@RequiresPermissions("ai:agent:edit")
	@RequestMapping(value = "enable")
	@ResponseBody
	public String enable(AiAgent aiAgent) {
		Global.assertDemoMode();
		aiAgent.setStatus(AiAgent.STATUS_NORMAL);
		aiAgentService.updateStatus(aiAgent);
		return renderResult(Global.TRUE, text("启用智能体成功！"));
	}
	
	/**
	 * 删除数据
	 */
	@RequiresPermissions("ai:agent:edit")
	@RequestMapping(value = "delete")
	@ResponseBody
	public String delete(AiAgent aiAgent) {
		Global.assertDemoMode();
		aiAgentService.delete(aiAgent);
		return renderResult(Global.TRUE, text("删除智能体成功！"));
	}
	
}
