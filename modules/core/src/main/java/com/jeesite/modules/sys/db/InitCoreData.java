/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.modules.sys.db;

import org.quartz.CronTrigger;
import org.springframework.beans.factory.annotation.Autowired;

import com.jeesite.common.callback.MethodCallback;
import com.jeesite.common.config.Global;
import com.jeesite.common.idgen.IdGen;
import com.jeesite.common.lang.StringUtils;
import com.jeesite.common.tests.BaseInitDataTests;
import com.jeesite.modules.gen.entity.GenTable;
import com.jeesite.modules.gen.entity.GenTableColumn;
import com.jeesite.modules.gen.service.GenTableService;
import com.jeesite.modules.job.dao.JobDao;
import com.jeesite.modules.job.entity.JobEntity;
import com.jeesite.modules.msg.task.impl.MsgLocalMergePushTask;
import com.jeesite.modules.msg.task.impl.MsgLocalPushTask;
import com.jeesite.modules.sys.dao.RoleMenuDao;
import com.jeesite.modules.sys.entity.Area;
import com.jeesite.modules.sys.entity.Company;
import com.jeesite.modules.sys.entity.CompanyOffice;
import com.jeesite.modules.sys.entity.Config;
import com.jeesite.modules.sys.entity.DictData;
import com.jeesite.modules.sys.entity.DictType;
import com.jeesite.modules.sys.entity.EmpUser;
import com.jeesite.modules.sys.entity.Employee;
import com.jeesite.modules.sys.entity.EmployeePost;
import com.jeesite.modules.sys.entity.Log;
import com.jeesite.modules.sys.entity.Menu;
import com.jeesite.modules.sys.entity.Module;
import com.jeesite.modules.sys.entity.Office;
import com.jeesite.modules.sys.entity.Post;
import com.jeesite.modules.sys.entity.Role;
import com.jeesite.modules.sys.entity.RoleDataScope;
import com.jeesite.modules.sys.entity.RoleMenu;
import com.jeesite.modules.sys.entity.User;
import com.jeesite.modules.sys.entity.UserDataScope;
import com.jeesite.modules.sys.entity.UserRole;
import com.jeesite.modules.sys.service.AreaService;
import com.jeesite.modules.sys.service.CompanyService;
import com.jeesite.modules.sys.service.ConfigService;
import com.jeesite.modules.sys.service.DictDataService;
import com.jeesite.modules.sys.service.DictTypeService;
import com.jeesite.modules.sys.service.EmpUserService;
import com.jeesite.modules.sys.service.MenuService;
import com.jeesite.modules.sys.service.ModuleService;
import com.jeesite.modules.sys.service.OfficeService;
import com.jeesite.modules.sys.service.PostService;
import com.jeesite.modules.sys.service.RoleService;
import com.jeesite.modules.sys.service.UserService;

/**
 * 初始化核心表数据
 * @author ThinkGem
 * @version 2017-10-22
 */
public class InitCoreData extends BaseInitDataTests {

	@Override
	public void begin() {
		super.begin();
		excelFile = InitCoreData.class.getName().replaceAll("\\.", "/")+".xlsx";
	}
	
	/**
	 * 建表语句执行
	 */
	public void createTable() throws Exception{
		runScript("core.sql");
		runScript("job.sql");
		runScript("test.sql");
	}

	/**
	 * 清理日志表
	 */
	public void initLog() throws Exception{
		clearTable(Log.class);
	}
	
	@Autowired
	private AreaService areaService;
	/**
	 * 区域、行政区划表
	 */
	public void initArea(String... prefixes) throws Exception{
		clearTable(Area.class);
		initExcelData(Area.class, new MethodCallback() {
			@Override
			public Object execute(Object... params) {
				String action = (String)params[0];
				if("save".equals(action)){
					Area entity = (Area)params[1];
					entity.setIsNewRecord(true);
					if (prefixes == null || prefixes.length == 0
							|| StringUtils.startsWithAny(entity.getAreaCode(), prefixes)){
						areaService.save(entity);
					}
					return null;
				}
				return null;
			}
		});
	}
	
	@Autowired
	private ConfigService configService;
	/**
	 * 参数配置表
	 */
	public void initConfig() throws Exception{
		clearTable(Config.class);
		initExcelData(Config.class, new MethodCallback() {
			@Override
			public Object execute(Object... params) {
				String action = (String)params[0];
				if("save".equals(action)){
					Config entity = (Config)params[1];
					entity.setId(IdGen.nextId());
					entity.setIsNewRecord(true);
					configService.save(entity);
					return null;
				}
				return null;
			}
		});
	}

	@Autowired
	private ModuleService moduleService;
	/**
	 * 系统模块表
	 */
	public void initModule() throws Exception{
		clearTable(Module.class);
		initExcelData(Module.class, new MethodCallback() {
			@Override
			public Object execute(Object... params) {
				String action = (String)params[0];
				if("save".equals(action)){
					Module entity = (Module)params[1];
					entity.setIsNewRecord(true);
					moduleService.save(entity);
					return null;
				}
				return null;
			}
		});
	}
	
	@Autowired
	private DictTypeService dictTypeService;
	@Autowired
	private DictDataService dictDataService;
	/**
	 * 系统字典、用户字典表
	 */
	public void initDict() throws Exception{
		clearTable(DictType.class);
		initExcelData(DictType.class, new MethodCallback() {
			@Override
			public Object execute(Object... params) {
				String action = (String)params[0];
				if("save".equals(action)){
					DictType entity = (DictType)params[1];
					entity.setId(IdGen.nextId());
					entity.setIsNewRecord(true);
					dictTypeService.save(entity);
					return null;
				}
				return null;
			}
		});

		clearTable(DictData.class);
		initExcelData(DictData.class, new MethodCallback() {
			@Override
			public Object execute(Object... params) {
				String action = (String)params[0];
				if("save".equals(action)){
					DictData entity = (DictData)params[1];
					entity.setIsNewRecord(true);
					dictDataService.save(entity);
					return null;
				}
				return null;
			}
		});
	}
	
	@Autowired
	private RoleService roleService;
	/**
	 * 角色表
	 */
	public void initRole() throws Exception{
		clearTable(Role.class);
		clearTable(RoleMenu.class);
		clearTable(RoleDataScope.class);
		initExcelData(Role.class, new MethodCallback() {
			@Override
			public Object execute(Object... params) {
				String action = (String)params[0];
				if("save".equals(action)){
					Role entity = (Role)params[1];
					entity.setIsNewRecord(true);
					roleService.save(entity);
					return null;
				}
				return null;
			}
		});
	}

	@Autowired
	private MenuService menuService;
	@Autowired
	private RoleMenuDao roleMenuDao;
	/**
	 * 菜单表
	 */
	public void initMenu() throws Exception{
		clearTable(Menu.class);
		clearTable(RoleMenu.class);
		initExcelData(Menu.class, new MethodCallback() {
			@Override
			public Object execute(Object... params) {
				String action = (String)params[0];
				if("save".equals(action)){
					Menu entity = (Menu)params[1];
					entity.setIsNewRecord(true);
					menuService.save(entity);
					RoleMenu rm = new RoleMenu();
					rm.setMenuCode(entity.getMenuCode());
					rm.setRoleCode(Role.CORP_ADMIN_ROLE_CODE);
					roleMenuDao.insert(rm);
					return null;
				}
				return null;
			}
		});
	}
	
	@Autowired
	private UserService userService;
	/**
	 * 用户表
	 */
	public void initUser() throws Exception{
		clearTable(User.class);
		clearTable(UserRole.class);
		clearTable(UserDataScope.class);
		initExcelData(User.class, new MethodCallback() {
			@Override
			public Object execute(Object... params) {
				String action = (String)params[0];
				if("save".equals(action)){
					User entity = (User)params[1];
					entity.setIsNewRecord(true);
					userService.save(entity);
					return null;
				}
				return null;
			}
		});
	}
	
	@Autowired
	private OfficeService officeService;
	/**
	 * 组织机构、部门表
	 */
	public void initOffice() throws Exception{
		clearTable(Office.class);
		initExcelData(Office.class, new MethodCallback() {
			@Override
			public Object execute(Object... params) {
				String action = (String)params[0];
				if("save".equals(action)){
					Office entity = (Office)params[1];
					entity.setIsNewRecord(true);
					officeService.save(entity);
					return null;
				}
				return null;
			}
		});
	}

	@Autowired
	private CompanyService companyService;
	/**
	 * 公司表
	 */
	public void initCompany() throws Exception{
		clearTable(Company.class);
		clearTable(CompanyOffice.class);
		initExcelData(Company.class, new MethodCallback() {
			@Override
			public Object execute(Object... params) {
				String action = (String)params[0];
				if("save".equals(action)){
					Company entity = (Company)params[1];
					entity.setIsNewRecord(true);
					companyService.save(entity);
					return null;
				}
				return null;
			}
		});
	}

	@Autowired
	private PostService postService;
	/**
	 * 岗位表
	 */
	public void initPost() throws Exception{
		clearTable(Post.class);
		initExcelData(Post.class, new MethodCallback() {
			@Override
			public Object execute(Object... params) {
				String action = (String)params[0];
				if("save".equals(action)){
					Post entity = (Post)params[1];
					entity.setIsNewRecord(true);
					postService.save(entity);
					return null;
				}
				return null;
			}
		});
	}
	
	@Autowired
	private EmpUserService empUserService;
	/**
	 * 员工、用户表
	 */
	public void initEmpUser() throws Exception{
		clearTable(Employee.class);
		clearTable(EmployeePost.class);
		initExcelData(EmpUser.class, new MethodCallback() {
			@Override
			public Object execute(Object... params) {
				String action = (String)params[0];
				if("set".equals(action)){
					EmpUser entity = (EmpUser)params[1];
					String header = (String)params[2];
					String val = (String)params[3];
					if ("userRoleString".equals(header)){
						entity.setUserRoleString(val);
						return true;
					}else if ("employee.employeePosts".equals(header)){
						entity.getEmployee().setEmployeePosts(new String[]{val});
						return true;
					}
				}
				else if("save".equals(action)){
					EmpUser entity = (EmpUser)params[1];
					entity.setIsNewRecord(true);
					empUserService.save(entity);
					// 设置当前为管理员，否则无法保存用户角色关系
					entity.setCurrentUser(new User(User.SUPER_ADMIN_CODE));
					userService.saveAuth(entity);
					return null;
				}
				return null;
			}
		});
	}

	@Autowired
	private JobDao jobDao; // 默认情况下job是关闭状态，需要注入jobDao
	/**
	 * 初始化消息推送服务
	 */
	public void initMsgPushJob(){
		JobEntity job = new JobEntity(MsgLocalPushTask.class.getSimpleName(), "SYSTEM");
		job.setDescription("消息推送服务 (实时推送)");
		job.setInvokeTarget("msgLocalPushTask.execute()");
		job.setCronExpression("0/3 * * * * ?");
		job.setConcurrent(Global.NO);
		job.setMisfireInstruction(CronTrigger.MISFIRE_INSTRUCTION_DO_NOTHING);
		job.setStatus(JobEntity.STATUS_PAUSED);
		jobDao.insert(job);
		job = new JobEntity(MsgLocalMergePushTask.class.getSimpleName(), "SYSTEM");
		job.setDescription("消息推送服务 (延迟推送)");
		job.setInvokeTarget("msgLocalMergePushTask.execute()");
		job.setCronExpression("0 0/30 * * * ?");
		job.setConcurrent(Global.NO);
		job.setMisfireInstruction(CronTrigger.MISFIRE_INSTRUCTION_DO_NOTHING);
		job.setStatus(JobEntity.STATUS_PAUSED);
		jobDao.insert(job);
	}
	
	@Autowired
	private GenTableService genTableService;
	/**
	 * 代码生成测试数据
	 */
	public void initGenTestData() throws Exception{
		GenTable genTable = new GenTable();
		genTable.setIsNewRecord(true);
		genTable.setTableName("test_data");
		genTable = genTableService.getFromDb(genTable);
		genTable.setIsNewRecord(true);
		genTable.setClassName("TestData");
		genTable.setFunctionAuthor("ThinkGem");
		genTable.setTplCategory("crud");
		genTable.setPackageName("com.jeesite.modules");
		genTable.setModuleName("test");
		genTable.setSubModuleName("");
		genTable.setFunctionName("测试数据");
		genTable.setFunctionNameSimple("数据");
		genTable.getOptionMap().put("isHaveDisableEnable", Global.YES);
		genTable.getOptionMap().put("isHaveDelete", Global.YES);
		genTable.getOptionMap().put("isFileUpload", Global.YES);
		genTable.getOptionMap().put("isImageUpload", Global.YES);
		initGenTableColumn(genTable);
		genTableService.save(genTable);
		// 子表
		GenTable genTableChild = new GenTable();
		genTableChild.setIsNewRecord(true);
		genTableChild.setTableName("test_data_child");
		genTableChild = genTableService.getFromDb(genTableChild);
		genTableChild.setIsNewRecord(true);
		genTableChild.setClassName("TestDataChild");
		genTableChild.setFunctionAuthor("ThinkGem");
		genTableChild.setTplCategory("crud");
		genTableChild.setPackageName("com.jeesite.modules");
		genTableChild.setModuleName("test");
		genTableChild.setSubModuleName("");
		genTableChild.setFunctionName("测试子表");
		genTableChild.setFunctionNameSimple("数据");
		genTableChild.setParentTableName("test_data");
		genTableChild.setParentTableFkName("test_data_id");
		initGenTableColumn(genTableChild);
		genTableService.save(genTableChild);
	}
	
	/**
	 * 代码生成测试数据（列初始化）
	 */
	private void initGenTableColumn(GenTable genTable){
		for(GenTableColumn column : genTable.getColumnList()){
			if ("test_input".equals(column.getColumnName())
					|| "test_textarea".equals(column.getColumnName())
					|| "test_select".equals(column.getColumnName())
					|| "test_select_multiple".equals(column.getColumnName())
					|| "test_checkbox".equals(column.getColumnName())
					|| "test_radio".equals(column.getColumnName())
					|| "test_date".equals(column.getColumnName())
					|| "test_datetime".equals(column.getColumnName())
				){
				column.setShowType(StringUtils.substringAfter(
						column.getColumnName(), "test_"));
				if ("test_input".equals(column.getColumnName())
						){
					column.setQueryType("LIKE");
				}
				else if ("test_textarea".equals(column.getColumnName())
						){
					column.setQueryType("LIKE");
					column.getOptionMap().put("isNewLine", Global.YES);
					column.getOptionMap().put("gridRowCol", "12/2/10");
				}
				else if ("test_select".equals(column.getColumnName())
						|| "test_select_multiple".equals(column.getColumnName())
						|| "test_radio".equals(column.getColumnName())
						|| "test_checkbox".equals(column.getColumnName())
						){
					column.getOptionMap().put("dictType", "sys_menu_type");
					column.getOptionMap().put("dictName", "sys_menu_type");
				}
				else if ("test_date".equals(column.getColumnName())
						|| "test_datetime".equals(column.getColumnName())
						){
					column.setQueryType("BETWEEN");
				}
			}else if ("test_user_code".equals(column.getColumnName())){
				column.setAttrType("com.jeesite.modules.sys.entity.User");
				column.setFullAttrName("testUser");
				column.setShowType("userselect");
			}else if ("test_office_code".equals(column.getColumnName())){
				column.setAttrType("com.jeesite.modules.sys.entity.Office");
				column.setFullAttrName("testOffice");
				column.setShowType("officeselect");
			}else if ("test_area_code".equals(column.getColumnName())){
				column.setFullAttrName("testAreaCode|testAreaName");
				column.setShowType("areaselect");
			}else if ("test_area_name".equals(column.getColumnName())){
				column.setIsEdit(Global.NO);
				column.setIsQuery(Global.NO);
			}else if ("test_data_id".equals(column.getColumnName())){
				column.setFullAttrName("testData");
			}
		}
	}
	
	/**
	 * 代码生成树表测试数据
	 */
	public void initGenTreeData() throws Exception{
		GenTable genTable = new GenTable();
		genTable.setIsNewRecord(true);
		genTable.setTableName("test_tree");
		genTable = genTableService.getFromDb(genTable);
		genTable.setIsNewRecord(true);
		genTable.setClassName("TestTree");
		genTable.setFunctionAuthor("ThinkGem");
		genTable.setTplCategory("treeGrid");
		genTable.setPackageName("com.jeesite.modules");
		genTable.setModuleName("test");
		genTable.setSubModuleName("");
		genTable.setFunctionName("测试树表");
		genTable.setFunctionNameSimple("数据");
		genTable.getOptionMap().put("isHaveDisableEnable", Global.YES);
		genTable.getOptionMap().put("isHaveDelete", Global.YES);
		genTable.getOptionMap().put("isFileUpload", Global.YES);
		genTable.getOptionMap().put("isImageUpload", Global.YES);
		genTable.getOptionMap().put("treeViewCode", "tree_code");
		genTable.getOptionMap().put("treeViewName", "tree_name");
		initGenTableColumn(genTable);
		genTableService.save(genTable);
	}
}
