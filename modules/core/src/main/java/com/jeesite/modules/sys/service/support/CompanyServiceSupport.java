/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.modules.sys.service.support;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.jeesite.common.collect.ListUtils;
import com.jeesite.common.service.TreeService;
import com.jeesite.modules.sys.dao.CompanyDao;
import com.jeesite.modules.sys.dao.CompanyOfficeDao;
import com.jeesite.modules.sys.entity.Company;
import com.jeesite.modules.sys.entity.CompanyOffice;
import com.jeesite.modules.sys.service.CompanyService;
import com.jeesite.modules.sys.service.DataScopeService;
import com.jeesite.modules.sys.utils.EmpUtils;

/**
 * 公司管理Service
 * @author ThinkGem
 * @version 2016-4-23
 */
@Transactional(readOnly=true)
public class CompanyServiceSupport extends TreeService<CompanyDao, Company>
		implements CompanyService{

	@Autowired
	private CompanyOfficeDao companyOfficeDao;
	
	@Autowired
	private DataScopeService dataScopeService;
	
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
		company.getSqlMap().getDataScope().addFilter("dsf", "Company", "a.company_code", ctrlPermi);
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
	@Transactional(readOnly=false)
	public void save(Company company) {
		if (company.getIsNewRecord()){
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
			companyOfficeDao.insertBatch(list);
		}
		// 清理公司相关缓存
		clearCompanyCache();
	}
	
	/**
	 * 删除公司
	 */
	@Override
	@Transactional(readOnly=false)
	public void delete(Company company) {
		super.delete(company);
		// 清理公司相关缓存
		clearCompanyCache();
	}

	/**
	 * 停用当前节点
	 */
	@Override
	@Transactional(readOnly=false)
	public void updateStatus(Company company) {
		dao.updateStatus(company);
		// 清理公司相关缓存
		clearCompanyCache();
	}
	
	/**
	 * 清理公司相关缓存
	 */
	private void clearCompanyCache(){
//		EmpUtils.removeCache(EmpUtils.CACHE_COMPANY_LIST);
		EmpUtils.removeCache(EmpUtils.CACHE_COMPANY_ALL_LIST);
	}

}