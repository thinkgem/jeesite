package ${packageName}.${moduleName}.web${subModuleName};

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
import ${packageName}.${moduleName}.entity${subModuleName}.${ClassName};
import ${packageName}.${moduleName}.service${subModuleName}.${ClassName}Service;

/**
 * ${functionName}Controller
 * @author ${classAuthor}
 * @version ${classVersion}
 */
@Controller
@RequestMapping(value = BaseController.ADMIN_PATH+"/${urlPrefix}")
public class ${ClassName}Controller extends BaseController {

	@Autowired
	private ${ClassName}Service ${className}Service;
	
	@ModelAttribute
	public ${ClassName} get(@RequestParam(required=false) Long id) {
		if (id != null){
			return ${className}Service.get(id);
		}else{
			return new ${ClassName}();
		}
	}
	
	@RequiresPermissions("${permissionPrefix}:view")
	@RequestMapping(value = {"list", ""})
	public String list(${ClassName} ${className}, Model model) {
		User user = UserUtils.getUser();
		if (!user.isAdmin()){
			${className}.setUser(user);
		}
        Page<${ClassName}> page = ${className}Service.find(new Page<${ClassName}>(request, response), ${className}); 
        model.addAttribute("page", page);
		return "${urlPrefix}List";
	}

	@RequiresPermissions("${permissionPrefix}:view")
	@RequestMapping(value = "form")
	public String form(${ClassName} ${className}, Model model) {
		model.addAttribute("${className}", ${className});
		return "${urlPrefix}Form";
	}

	@RequiresPermissions("${permissionPrefix}:edit")
	@RequestMapping(value = "save")
	public String save(${ClassName} ${className}, RedirectAttributes redirectAttributes) {
		if (beanValidators(redirectAttributes, ${className})){
			${className}Service.save(${className});
			addFlashMessage(redirectAttributes, "保存${functionName}'" + ${className}.getName() + "'成功");
		}
		return "redirect:"+BaseController.ADMIN_PATH+"/${urlPrefix}/?repage";
	}
	
	@RequiresPermissions("${permissionPrefix}:edit")
	@RequestMapping(value = "delete")
	public String delete(Long id, RedirectAttributes redirectAttributes) {
		${className}Service.delete(id);
		addFlashMessage(redirectAttributes, "删除${functionName}成功");
		return "redirect:"+BaseController.ADMIN_PATH+"/${urlPrefix}/?repage";
	}

}
