/**
 * There are <a href="https://github.com/thinkgem/jeesite">JeeSite</a> code generation
 */
package com.thinkgem.jeesite.modules.oa.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.thinkgem.jeesite.common.config.Global;
import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.utils.StringUtils;
import com.thinkgem.jeesite.common.web.BaseController;
import com.thinkgem.jeesite.common.workflow.WorkflowUtils;
import com.thinkgem.jeesite.modules.oa.entity.Leave;
import com.thinkgem.jeesite.modules.oa.service.LeaveService;

/**
 * 请假Controller
 * @author liuj
 * @version 2013-04-05
 */
@Controller
@RequestMapping(value = "${adminPath}/oa/leave")
public class LeaveController extends BaseController {

	@Autowired
	protected LeaveService leaveService;

	@Autowired
	protected RuntimeService runtimeService;

	@Autowired
	protected TaskService taskService;
	
	
	@ModelAttribute
	public Leave get(@RequestParam(required=false) String id) {
		if (StringUtils.isNotBlank(id)){
			return leaveService.get(id);
		}else{
			return new Leave();
		}
	}
	
	@RequiresPermissions("oa:leave:view")
	@RequestMapping(value = {"list"})
	public String list(Leave leave, HttpServletRequest request, HttpServletResponse response, Model model) {
        Page<Leave> page = leaveService.find(new Page<Leave>(request, response), leave); 
        model.addAttribute("page", page);
		model.addAttribute("leave", leave);
		return "modules/oa/leaveList";
	}

	@RequiresPermissions("oa:leave:view")
	@RequestMapping(value = "form")
	public String form(Leave leave, Model model) {
		model.addAttribute("leave", leave);
		return "modules/oa/leaveForm";
	}
	

	@RequiresPermissions("oa:leave:edit")
	@RequestMapping(value = "save")
	public String save(Leave leave, Model model, RedirectAttributes redirectAttributes) {
		leaveService.save(leave);
		addMessage(redirectAttributes, "保存请假成功");
		return "redirect:"+Global.getAdminPath()+"/oa/leave/";
	}

	@RequiresPermissions("oa:leave:edit")
	@RequestMapping(value = "delete")
	public String delete(String id, RedirectAttributes redirectAttributes) {
		leaveService.delete(id);
		addMessage(redirectAttributes, "删除请假成功");
		return "redirect:"+Global.getAdminPath()+"/oa/leave/";
	}

	@RequiresPermissions("oa:leave:view")
	@RequestMapping(value = {"list/task", ""})
	public String listTask(Leave leave, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<Leave> page = leaveService.findTodoTasks(new Page<Leave>(request, response), leave); 
        model.addAttribute("page", page);
		model.addAttribute("leave", leave);
		return "modules/oa/leaveTask";
	}
	
	@RequiresPermissions("oa:leave:view")
	@RequestMapping(value = "detail")
	public String detail(Leave leave, Model model) {
		model.addAttribute("leave", leave);
		model.addAttribute("workflowEntity",WorkflowUtils.getWorkflowEntity(leave.getProcessInstanceId()));
		return "modules/oa/leaveDetail";
	}

	@RequiresPermissions("oa:leave:deptLeaderAudit")
	@RequestMapping(value = "deptLeaderAudit")
	public String deptLeaderAudit(Leave leave, RedirectAttributes redirectAttributes) {
		leaveService.deptLeaderAudit(leave);
		addMessage(redirectAttributes, "请假审批成功");
		return "redirect:"+Global.getAdminPath()+"/oa/leave/";
	}

	@RequiresPermissions("oa:leave:hrAudit")
	@RequestMapping(value = "hrAudit")
	public String hrAudit(Leave leave, RedirectAttributes redirectAttributes) {
		leaveService.hrAudit(leave);
		addMessage(redirectAttributes, "请假审批成功");
		return "redirect:"+Global.getAdminPath()+"/oa/leave/";
	}

	@RequiresPermissions("oa:leave:edit")
	@RequestMapping(value = "modifyApply")
	public String modifyApply(Leave leave, RedirectAttributes redirectAttributes) {
		leaveService.modifyApply(leave);
		addMessage(redirectAttributes, "请假调整成功");
		return "redirect:"+Global.getAdminPath()+"/oa/leave/";
	}
	
	@RequiresPermissions("oa:leave:edit")
	@RequestMapping(value = "reportBack")
	public String reportBack(Leave leave, RedirectAttributes redirectAttributes) {
		leaveService.reportBack(leave);
		addMessage(redirectAttributes, "请假销假成功");
		return "redirect:"+Global.getAdminPath()+"/oa/leave/";
	}

}
