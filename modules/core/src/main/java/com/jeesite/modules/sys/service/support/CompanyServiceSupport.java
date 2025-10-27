/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 * No deletion without permission, or be held responsible to law.
 */
package com.jeesite.modules.sys.service.support;

import com.jeesite.common.collect.ListUtils;
import com.jeesite.common.lang.StringUtils;
import com.jeesite.common.service.TreeService;
import com.jeesite.common.utils.PageUtils;
import com.jeesite.modules.sys.dao.CompanyDao;
import com.jeesite.modules.sys.dao.CompanyOfficeDao;
import com.jeesite.modules.sys.entity.Company;
import com.jeesite.modules.sys.entity.CompanyOffice;
import com.jeesite.modules.sys.entity.EmpUser;
import com.jeesite.modules.sys.service.CompanyService;
import com.jeesite.modules.sys.service.DataScopeService;
import com.jeesite.modules.sys.service.EmpUserService;
import com.jeesite.modules.sys.utils.EmpUtils;
import com.jeesite.modules.sys.utils.UserUtils;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 公司管理Service
 * @author ThinkGem
 * @version 2016-4-23
 */
public class CompanyServiceSupport extends TreeService<CompanyDao, Company>
		implements CompanyService{

	protected final CompanyOfficeDao companyOfficeDao;
	protected final DataScopeService dataScopeService;
	protected final EmpUserService empUserService;

	public CompanyServiceSupport(CompanyOfficeDao companyOfficeDao, DataScopeService dataScopeService,
								 EmpUserService empUserService) {
		this.companyOfficeDao = companyOfficeDao;
		this.dataScopeService = dataScopeService;
		this.empUserService = empUserService;
	}

	/**
	 * 获取单条数据
	 */
	@Override
	public Company get(Company company) {
		return super.get(company);
	}
	
	/**
	 * 添加数据权限过滤条件
	 */
	@Override
	public void addDataScopeFilter(Company company, String ctrlPermi) {
		company.sqlMap().getDataScope().addFilter("dsf", "Company", "a.company_code",
				null, ctrlPermi, "office_user");
	}

	/**
	 * 查询公司列表
	 */
	@Override
	public List<Company> findList(Company company) {
		return super.findList(company);
	}

	/**
	 * 保存公司
	 */
	@Override
	@Transactional
	public void save(Company company) {
		if (company.getIsNewRecord()){
			// 生成主键，并验证改主键是否存在，如存在则抛出验证信息
			genIdAndValid(company, company.getViewCode());
			// 当前新数据授权，如果用户有上级数据权限，则当前数据也有相应的数据权限
			dataScopeService.insertIfParentExists(company, "Company");
		}
		super.save(company);
		// 重新绑定组织和公司之间的关系
		if (StringUtils.isBlank(company.getCompanyCode())){
			return;
		}
		CompanyOffice where = new CompanyOffice();
		where.setCompanyCode(company.getCompanyCode());
		companyOfficeDao.deleteByEntity(where);
		List<CompanyOffice> list = company.getCompanyOfficeList();
		if (ListUtils.isNotEmpty(list)){
			list.forEach(e -> {
				e.setCompanyCode(company.getCompanyCode());
			});
			companyOfficeDao.insertBatch(list, null);
		}
		// 清理公司相关缓存
		clearCompanyCache(company);
	}
	
	/**
	 * 删除公司
	 */
	@Override
	@Transactional
	public void delete(Company company) {
		company.sqlMap().markIdDelete();
		super.delete(company);
		// 清理公司相关缓存
		clearCompanyCache(company);
	}

	/**
	 * 停用当前节点
	 */
	@Override
	@Transactional
	public void updateStatus(Company company) {
		dao.updateStatus(company);
		// 清理公司相关缓存
		clearCompanyCache(company);
	}
	
	/**
	 * 清理公司相关缓存
	 */
	private void clearCompanyCache(Company company){
		EmpUtils.removeCache(EmpUtils.CACHE_COMPANY_ALL_LIST);
		// 清理公司下的用户缓存，包含子公司
		if (company == null || StringUtils.isBlank(company.getCompanyCode())){
			return;
		}
		if (StringUtils.isBlank(company.getParentCode())){
			company = get(company);
			if (company == null){
				return;
			}
		}
		Company where = new Company();
		where.setStatus(Company.STATUS_NORMAL);
		where.setParentCodes(company.getParentCodes() + company.getCompanyCode() + ",%");
		EmpUser empUserWhere = new EmpUser();
		empUserWhere.setCodes(this.findByParentCodesLike(where).stream().map(Company::getCompanyCode).toArray(String[]::new));
		if (empUserWhere.getCodes().length == 0) {
			return;
		}
		PageUtils.findList(empUserWhere, null, e -> {
			List<EmpUser> empUserList = empUserService.findUserListByCompanyCodes((EmpUser)e);
			empUserList.forEach(UserUtils::clearCache);
			return !empUserList.isEmpty();
		});
	}

}