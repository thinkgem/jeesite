/**
 * There are <a href="https://github.com/thinkgem/jeesite">JeeSite</a> code generation
 */
package com.thinkgem.jeesite.modules.prj.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.thinkgem.jeesite.common.config.Global;
import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.utils.SpringContextHolder;
import com.thinkgem.jeesite.common.utils.StringUtils;
import com.thinkgem.jeesite.common.web.BaseController;
import com.thinkgem.jeesite.modules.prj.entity.Project;
import com.thinkgem.jeesite.modules.prj.service.ProjectService;
import com.thinkgem.jeesite.modules.sys.entity.User;
import com.thinkgem.jeesite.modules.sys.utils.UserUtils;

/**
 * 项目Controller
 * @author liuj
 * @version 2013-12-07
 */
@Controller
@RequestMapping(value = "${adminPath}/prj/project")
public class ProjectController extends BaseController {

	@Autowired
	private ProjectService projectService;
	
	@ModelAttribute
	public Project get(@RequestParam(required=false) String id) {
		if (StringUtils.isNotBlank(id)){
			return projectService.get(id);
		}else{
			return new Project();
		}
	}
	
	@RequiresPermissions("prj:project:view")
	@RequestMapping(value = {"list", ""})
	public String list(Project project, HttpServletRequest request, HttpServletResponse response, Model model) {	
		String rootPath = SpringContextHolder.getResourceRootRealPath();
		logger.info(rootPath);
		User user = UserUtils.getUser();
		if (!user.isAdmin()){
			project.setCreateBy(user);
		}
        Page<Project> page = projectService.find(new Page<Project>(request, response), project); 
        model.addAttribute("page", page);
		return "modules/prj/projectList";
	}

	@RequiresPermissions("prj:project:view")
	@RequestMapping(value = "form")
	public String form(Project project, Model model) {
		model.addAttribute("project", project);
		return "modules/prj/projectForm";
	}
	
	@RequiresPermissions("prj:project:view")
	@RequestMapping(value = "generate", method=RequestMethod.GET)
	public String generate(Project project, Model model) {
		model.addAttribute("project", project);
		return "modules/prj/projectGenerate";
	}
	
	@RequiresPermissions("prj:project:view")
	@RequestMapping(value = "generate", method=RequestMethod.POST)
	public String generateSource(Project project, Model model) {
		String filePath = projectService.generate(project);
		model.addAttribute("filePath", filePath);
		return "redirect:"+Global.getAdminPath()+"/download";
	}
	
	

	@RequiresPermissions("prj:project:edit")
	@RequestMapping(value = "save")
	public String save(Project project, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, project)){
			return form(project, model);
		}
		projectService.save(project);
		addMessage(redirectAttributes, "保存项目'" + project.getName() + "'成功");
		return "redirect:"+Global.getAdminPath()+"/prj/project/?repage";
	}
	
	@RequiresPermissions("prj:project:edit")
	@RequestMapping(value = "delete")
	public String delete(String id, RedirectAttributes redirectAttributes) {
		projectService.delete(id);
		addMessage(redirectAttributes, "删除项目成功");
		return "redirect:"+Global.getAdminPath()+"/prj/project/?repage";
	}

}
