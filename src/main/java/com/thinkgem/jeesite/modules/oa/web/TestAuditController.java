/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.oa.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.web.BaseController;
import com.thinkgem.jeesite.modules.sys.entity.User;
import com.thinkgem.jeesite.modules.sys.utils.UserUtils;
import com.thinkgem.jeesite.modules.oa.entity.TestAudit;
import com.thinkgem.jeesite.modules.oa.service.TestAuditService;

/**
 * 审批Controller
 * @author thinkgem
 * @version 2014-05-16
 */
@Controller
@RequestMapping(value = "${adminPath}/oa/testAudit")
public class TestAuditController extends BaseController {

	@Autowired
	private TestAuditService testAuditService;
	
	@ModelAttribute
	public TestAudit get(@RequestParam(required=false) String id){//, 
//			@RequestParam(value="act.procInsId", required=false) String procInsId) {
		TestAudit testAudit = null;
		if (StringUtils.isNotBlank(id)){
			testAudit = testAuditService.get(id);
//		}else if (StringUtils.isNotBlank(procInsId)){
//			testAudit = testAuditService.getByProcInsId(procInsId);
		}
		if (testAudit == null){
			testAudit = new TestAudit();
		}
		return testAudit;
	}
	
	@RequiresPermissions("oa:testAudit:view")
	@RequestMapping(value = {"list", ""})
	public String list(TestAudit testAudit, HttpServletRequest request, HttpServletResponse response, Model model) {
		User user = UserUtils.getUser();
		if (!user.isAdmin()){
			testAudit.setCreateBy(user);
		}
        Page<TestAudit> page = testAuditService.findPage(new Page<TestAudit>(request, response), testAudit); 
        model.addAttribute("page", page);
		return "modules/oa/testAuditList";
	}
	
	/**
	 * 申请单填写
	 * @param testAudit
	 * @param model
	 * @return
	 */
	@RequiresPermissions("oa:testAudit:view")
	@RequestMapping(value = "form")
	public String form(TestAudit testAudit, Model model) {
		
		String view = "testAuditForm";
		
		// 查看审批申请单
		if (StringUtils.isNotBlank(testAudit.getId())){//.getAct().getProcInsId())){

			// 环节编号
			String taskDefKey = testAudit.getAct().getTaskDefKey();
			
			// 查看工单
			if(testAudit.getAct().isFinishTask()){
				view = "testAuditView";
			}
			// 修改环节
			else if ("modify".equals(taskDefKey)){
				view = "testAuditForm";
			}
			// 审核环节
			else if ("audit".equals(taskDefKey)){
				view = "testAuditAudit";
//				String formKey = "/oa/testAudit";
//				return "redirect:" + ActUtils.getFormUrl(formKey, testAudit.getAct());
			}
			// 审核环节2
			else if ("audit2".equals(taskDefKey)){
				view = "testAuditAudit";
			}
			// 审核环节3
			else if ("audit3".equals(taskDefKey)){
				view = "testAuditAudit";
			}
			// 审核环节4
			else if ("audit4".equals(taskDefKey)){
				view = "testAuditAudit";
			}
			// 兑现环节
			else if ("apply_end".equals(taskDefKey)){
				view = "testAuditAudit";
			}
		}

		model.addAttribute("testAudit", testAudit);
		return "modules/oa/" + view;
	}
	
	/**
	 * 申请单保存/修改
	 * @param testAudit
	 * @param model
	 * @param redirectAttributes
	 * @return
	 */
	@RequiresPermissions("oa:testAudit:edit")
	@RequestMapping(value = "save")
	public String save(TestAudit testAudit, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, testAudit)){
			return form(testAudit, model);
		}
		testAuditService.save(testAudit);
		addMessage(redirectAttributes, "提交审批'" + testAudit.getUser().getName() + "'成功");
		return "redirect:" + adminPath + "/act/task/todo/";
	}

	/**
	 * 工单执行（完成任务）
	 * @param testAudit
	 * @param model
	 * @return
	 */
	@RequiresPermissions("oa:testAudit:edit")
	@RequestMapping(value = "saveAudit")
	public String saveAudit(TestAudit testAudit, Model model) {
		if (StringUtils.isBlank(testAudit.getAct().getFlag())
				|| StringUtils.isBlank(testAudit.getAct().getComment())){
			addMessage(model, "请填写审核意见。");
			return form(testAudit, model);
		}
		testAuditService.auditSave(testAudit);
		return "redirect:" + adminPath + "/act/task/todo/";
	}
	
	/**
	 * 删除工单
	 * @param id
	 * @param redirectAttributes
	 * @return
	 */
	@RequiresPermissions("oa:testAudit:edit")
	@RequestMapping(value = "delete")
	public String delete(TestAudit testAudit, RedirectAttributes redirectAttributes) {
		testAuditService.delete(testAudit);
		addMessage(redirectAttributes, "删除审批成功");
		return "redirect:" + adminPath + "/oa/testAudit/?repage";
	}

}
