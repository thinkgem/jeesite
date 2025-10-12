/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 * No deletion without permission, or be held responsible to law.
 */
package com.jeesite.modules.sys.db;

import com.jeesite.common.config.Global;
import com.jeesite.common.idgen.IdGen;
import com.jeesite.common.tests.BaseInitDataTests;
import com.jeesite.modules.biz.entity.BizCategory;
import com.jeesite.modules.biz.service.BizCategoryService;
import com.jeesite.modules.gen.utils.GenUtils;
import com.jeesite.modules.job.dao.JobDao;
import com.jeesite.modules.job.entity.JobEntity;
import com.jeesite.modules.msg.task.impl.MsgLocalMergePushTask;
import com.jeesite.modules.msg.task.impl.MsgLocalPushTask;
import com.jeesite.modules.sys.dao.RoleMenuDao;
import com.jeesite.modules.sys.entity.*;
import com.jeesite.modules.sys.entity.Module;
import com.jeesite.modules.sys.service.*;
import org.quartz.CronTrigger;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

/**
 * 初始化核心表数据
 * @author ThinkGem
 * @version 2020-5-26
 */
@Component
@ConditionalOnProperty(name="jeesite.initdata", havingValue="true", matchIfMissing=false)
public class InitCoreData extends BaseInitDataTests {

	public InitCoreData(ConfigService configService, ModuleService moduleService, DictTypeService dictTypeService,
						DictDataService dictDataService, RoleService roleService, MenuService menuService,
						RoleMenuDao roleMenuDao, UserService userService, OfficeService officeService,
						CompanyService companyService, PostService postService, EmpUserService empUserService,
						ObjectProvider<JobDao> jobDao, BizCategoryService bizCategoryService) {
		this.configService = configService;
		this.moduleService = moduleService;
		this.dictTypeService = dictTypeService;
		this.dictDataService = dictDataService;
		this.roleService = roleService;
		this.menuService = menuService;
		this.roleMenuDao = roleMenuDao;
		this.userService = userService;
		this.officeService = officeService;
		this.companyService = companyService;
		this.postService = postService;
		this.empUserService = empUserService;
		this.jobDao = jobDao.getIfAvailable();
		this.bizCategoryService = bizCategoryService;
	}

	@Override
	public boolean initData() throws Exception {
		if (GenUtils.isTableExists(Global.getTablePrefix() + "sys_module")) {
			return true; // 如果表已存在，则无需初始化
		}
		this.runCreateScript("core.sql");
		this.runCreateScript("job.sql");
		GenUtils.clearCache();
		this.initArea();
		this.initConfig();
		this.initModule();
		this.initDict();
		this.initRole();
		this.initMenu();
		this.initUser();
		this.initOffice();
		this.initCompany();
		this.initPost();
		this.initEmpUser();
		this.initJob();
		this.initBizCategory();
		return true;
	}
	
//	private final AreaService areaService;
	/**
	 * 区域、行政区划表
	 */
	public void initArea() throws Exception{
//		clearTable(Area.class);
		if (!checkTable(Area.class)) {
			return;
		}
		runCreateScript("area.sql");
//		initExcelData(Area.class, new MethodCallback() {
//			@Override
//			public Object execute(Object... params) {
//				String action = (String)params[0];
//				if("save".equals(action)){
//					Area entity = (Area)params[1];
//					entity.setIsNewRecord(true);
//					if (prefixes == null || prefixes.length == 0
//							|| StringUtils.startsWithAny(entity.getAreaCode(), prefixes)){
//						areaService.save(entity);
//					}
//					return null;
//				}
//				return null;
//			}
//		});
	}
	
	private final ConfigService configService;
	/**
	 * 参数配置表
	 */
	public void initConfig() throws Exception{
//		clearTable(Config.class);
		initExcelData(Config.class, params -> {
			String action = (String)params[0];
			if("save".equals(action)){
				Config entity = (Config)params[1];
				entity.setId(IdGen.nextId());
				entity.setIsNewRecord(true);
				configService.save(entity);
				return null;
			}
			return null;
		});
	}

	private final ModuleService moduleService;
	/**
	 * 系统模块表
	 */
	public void initModule() throws Exception{
//		clearTable(Module.class);
		initExcelData(Module.class, params -> {
			String action = (String)params[0];
			if("save".equals(action)){
				Module entity = (Module)params[1];
				entity.setIsNewRecord(true);
				moduleService.save(entity);
				return null;
			}
			return null;
		});
	}
	
	private final DictTypeService dictTypeService;
	private final DictDataService dictDataService;
	/**
	 * 系统字典、用户字典表
	 */
	public void initDict() throws Exception{
//		clearTable(DictType.class);
		initExcelData(DictType.class, params -> {
			String action = (String)params[0];
			if("save".equals(action)){
				DictType entity = (DictType)params[1];
				entity.setId(IdGen.nextId());
				entity.setIsNewRecord(true);
				dictTypeService.save(entity);
				return null;
			}
			return null;
		});

//		clearTable(DictData.class);
		initExcelData(DictData.class, params -> {
			String action = (String)params[0];
			if("save".equals(action)){
				DictData entity = (DictData)params[1];
				entity.setIsNewRecord(true);
				dictDataService.save(entity);
				return null;
			}
			return null;
		});
	}
	
	private final RoleService roleService;
	/**
	 * 角色表
	 */
	public void initRole() throws Exception{
//		clearTable(Role.class);
//		clearTable(RoleMenu.class);
//		clearTable(RoleDataScope.class);
		initExcelData(Role.class, params -> {
			String action = (String)params[0];
			if("save".equals(action)){
				Role entity = (Role)params[1];
				entity.setIsNewRecord(true);
				roleService.save(entity);
				return null;
			}
			return null;
		});
	}

	private final MenuService menuService;
	private final RoleMenuDao roleMenuDao;
	/**
	 * 菜单表
	 */
	public void initMenu() throws Exception{
//		clearTable(Menu.class);
//		clearTable(RoleMenu.class);
		initExcelData(Menu.class, params -> {
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
		});
	}
	
	private final UserService userService;
	/**
	 * 用户表
	 */
	public void initUser() throws Exception{
//		clearTable(User.class);
//		clearTable(UserRole.class);
//		clearTable(UserDataScope.class);
		initExcelData(User.class, params -> {
			String action = (String)params[0];
			if("save".equals(action)){
				User entity = (User)params[1];
				entity.setIsNewRecord(true);
				userService.save(entity);
				return null;
			}
			return null;
		});
	}
	
	private final OfficeService officeService;
	/**
	 * 组织机构、部门表
	 */
	public void initOffice() throws Exception{
//		clearTable(Office.class);
		initExcelData(Office.class, params -> {
			String action = (String)params[0];
			if("save".equals(action)){
				Office entity = (Office)params[1];
				entity.setIsNewRecord(true);
				officeService.save(entity);
				return null;
			}
			return null;
		});
	}

	private final CompanyService companyService;
	/**
	 * 公司表
	 */
	public void initCompany() throws Exception{
//		clearTable(Company.class);
//		clearTable(CompanyOffice.class);
		initExcelData(Company.class, params -> {
			String action = (String)params[0];
			if("save".equals(action)){
				Company entity = (Company)params[1];
				entity.setIsNewRecord(true);
				companyService.save(entity);
				return null;
			}
			return null;
		});
	}

	private final PostService postService;
	/**
	 * 岗位表
	 */
	public void initPost() throws Exception{
//		clearTable(Post.class);
		initExcelData(Post.class, params -> {
			String action = (String)params[0];
			if("save".equals(action)){
				Post entity = (Post)params[1];
				entity.setIsNewRecord(true);
				postService.save(entity);
				return null;
			}
			return null;
		});
	}
	
	private final EmpUserService empUserService;
	/**
	 * 员工、用户表
	 */
	public void initEmpUser() throws Exception{
//		clearTable(Employee.class);
//		clearTable(EmployeePost.class);
		initExcelData(EmpUser.class, params -> {
			String action = (String)params[0];
			if("check".equals(action)){
				User user = new User();
				user.setLoginCode("user1");
				return userService.getByLoginCode(user) == null;
			}else if("set".equals(action)){
				EmpUser entity = (EmpUser)params[1];
				String header = (String)params[2];
				String val = (String)params[3];
				if ("userRoleString".equals(header)){
					entity.setUserRoleString(val);
					return true;
				}else if ("employee.employeePosts".equals(header)){
					entity.getEmployee().setEmployeePosts(val);
					return true;
				}
			}else if("save".equals(action)){
				EmpUser entity = (EmpUser)params[1];
				entity.setIsNewRecord(true);
				empUserService.save(entity);
				// 设置当前为管理员，否则无法保存用户角色关系
				entity.currentUser(new User(User.SUPER_ADMIN_CODE));
				userService.saveAuth(entity);
				return null;
			}
			return null;
		});
	}

	private final JobDao jobDao; // 默认情况下job是关闭状态，需要注入jobDao
	/**
	 * 初始化消息推送服务
	 */
	public void initJob(){
		if (jobDao == null || !checkTable(JobEntity.class)) {
			return;
		}
		JobEntity job = new JobEntity(MsgLocalPushTask.class.getSimpleName(), "SYSTEM");
		job.setDescription("消息推送服务 (实时推送)");
		job.setInvokeTarget("msgLocalPushTask.execute()");
		job.setCronExpression("0/3 * * * * ?");
		job.setConcurrent(Global.NO);
		job.setMisfireInstruction(CronTrigger.MISFIRE_INSTRUCTION_DO_NOTHING);
		job.setStatus(JobEntity.STATUS_PAUSED);
		job.setRemarks("实时推送和设定计划推送时间的定时推送消息。");
		jobDao.insert(job);
		job = new JobEntity(MsgLocalMergePushTask.class.getSimpleName(), "SYSTEM");
		job.setDescription("消息推送服务 (合并推送)");
		job.setInvokeTarget("msgLocalMergePushTask.execute()");
		job.setCronExpression("0 0/30 * * * ?");
		job.setConcurrent(Global.NO);
		job.setMisfireInstruction(CronTrigger.MISFIRE_INSTRUCTION_DO_NOTHING);
		job.setStatus(JobEntity.STATUS_PAUSED);
		job.setRemarks("不重要的通知进行汇总，30分钟或更长执行一次，将多条消息合并为一条消息延迟推送给用户。");
		jobDao.insert(job);
		job = new JobEntity("DeleteLogBefore", "SYSTEM");
		job.setDescription("清理访问日志 (3个月前)");
		job.setInvokeTarget("logService.deleteLogBefore(0, 3, 0)");
		job.setCronExpression("0 0 0 1 1/1 ? *");
		job.setConcurrent(Global.NO);
		job.setMisfireInstruction(CronTrigger.MISFIRE_INSTRUCTION_DO_NOTHING);
		job.setStatus(JobEntity.STATUS_PAUSED);
		job.setRemarks("1、清理1年前的所有日志：logService.deleteLogBefore(1, 0, 0)\n" +
				"2、清理6个月前的所有日志：logService.deleteLogBefore(0, 6, 0)\n" +
				"3、清理7天前的所有日志：logService.deleteLogBefore(0, 0, 7)\n" +
				"4、清理1年6个月前的所有日志：logService.deleteLogBefore(1, 6, 0)");
		jobDao.insert(job);
	}

	private final BizCategoryService bizCategoryService;
	public void initBizCategory() throws Exception{
//		clearTable(BizCategory.class);
		initExcelData(BizCategory.class, params -> {
			String action = (String)params[0];
			if("save".equals(action)){
				BizCategory entity = (BizCategory)params[1];
				entity.setIsNewRecord(true);
				bizCategoryService.save(entity);
				return null;
			}
			return null;
		});
	}
	
	@Override
	public int getPhase() {
		return Integer.MIN_VALUE + 1000;  // core 1000, other 5000, upgrade 10000
	}
	
}
