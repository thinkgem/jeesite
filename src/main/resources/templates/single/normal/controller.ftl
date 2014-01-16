package ${sourceTable.controllerPackage};

<#list importList as import>
	<#if (import !="")>
import ${import};
	</#if>
</#list>

@Controller
@RequestMapping(value = "${f.dollar}{adminPath}/${sourceTable.moduleName}/${sourceTable.entityClassName?uncap_first}")
public class ${sourceTable.controllerClassName} extends BaseController {

	@Autowired
	private ${sourceTable.serviceClassName} ${sourceTable.serviceClassName?uncap_first};
	
	@ModelAttribute
	public ${sourceTable.entityClassName} get(@RequestParam(required=false) Long id) {
		if (id != null){
			return ${sourceTable.serviceClassName?uncap_first}.get(id);
		}else{
			return new ${sourceTable.entityClassName}();
		}
	}
	
	@RequiresPermissions("${sourceTable.moduleName}:${sourceTable.entityClassName?uncap_first}:view")
	@RequestMapping(value = {"list", ""})
	public String list(${sourceTable.entityClassName} ${sourceTable.entityClassName?uncap_first}, HttpServletRequest request, HttpServletResponse response, Model model) {
        Page<${sourceTable.entityClassName}> page = ${sourceTable.serviceClassName?uncap_first}.find(new Page<${sourceTable.entityClassName}>(request, response), ${sourceTable.entityClassName?uncap_first}); 
        model.addAttribute("page", page);
		return "modules/${sourceTable.moduleName}/${sourceTable.entityClassName?uncap_first}List";
	}

	@RequiresPermissions("${sourceTable.moduleName}:${sourceTable.entityClassName?uncap_first}:view")
	@RequestMapping(value = "form")
	public String form(${sourceTable.entityClassName} ${sourceTable.entityClassName?uncap_first}, Model model) {
		model.addAttribute("${sourceTable.entityClassName?uncap_first}", ${sourceTable.entityClassName?uncap_first});
		return "modules/${sourceTable.moduleName}/${sourceTable.entityClassName?uncap_first}Form";
	}

	@RequiresPermissions("${sourceTable.moduleName}:${sourceTable.entityClassName?uncap_first}:edit")
	@RequestMapping(value = "save")
	public String save(${sourceTable.entityClassName} ${sourceTable.entityClassName?uncap_first}, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, ${sourceTable.entityClassName?uncap_first})){
			return form(${sourceTable.entityClassName?uncap_first}, model);
		}
		${sourceTable.serviceClassName?uncap_first}.save(${sourceTable.entityClassName?uncap_first});
		<#if sourceTable.containsName>
		addMessage(redirectAttributes, "保存${sourceTable.logicalName}'" + ${sourceTable.entityClassName?uncap_first}.getName() + "'成功");
		<#else>
		addMessage(redirectAttributes, "保存${sourceTable.logicalName}'" + ${sourceTable.entityClassName?uncap_first}.getId() + "'成功");
		</#if>
		return "redirect:"+Global.getAdminPath()+"/${sourceTable.moduleName}/${sourceTable.entityClassName?uncap_first}/?repage";
	}
	
	@RequiresPermissions("${sourceTable.moduleName}:${sourceTable.entityClassName?uncap_first}:edit")
	@RequestMapping(value = "delete")
	public String delete(Long id, RedirectAttributes redirectAttributes) {
		${sourceTable.serviceClassName?uncap_first}.delete(id);
		addMessage(redirectAttributes, "删除${sourceTable.logicalName}成功");
		return "redirect:"+Global.getAdminPath()+"/${sourceTable.moduleName}/${sourceTable.entityClassName?uncap_first}/?repage";
	}
	
	<#if sourceTable.containsName>
	@RequiresUser
	@ResponseBody
	@RequestMapping(value = "checkName")
	public String checkName(String oldName, String name) {
		if (name!=null && name.equals(oldName)) {
			return "true";
		} else if (name!=null && ${sourceTable.serviceClassName?uncap_first}.findByName(name) == null) {
			return "true";
		}
		return "false";
	}
	</#if>

}
