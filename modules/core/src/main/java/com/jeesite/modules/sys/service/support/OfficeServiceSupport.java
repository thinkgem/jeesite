/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 * No deletion without permission, or be held responsible to law.
 */
package com.jeesite.modules.sys.service.support;

import java.util.List;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.jeesite.common.config.Global;
import com.jeesite.common.service.ServiceException;
import com.jeesite.common.service.TreeService;
import com.jeesite.common.utils.excel.ExcelImport;
import com.jeesite.common.validator.ValidatorUtils;
import com.jeesite.modules.sys.dao.OfficeDao;
import com.jeesite.modules.sys.entity.Office;
import com.jeesite.modules.sys.service.DataScopeService;
import com.jeesite.modules.sys.service.OfficeService;
import com.jeesite.modules.sys.utils.EmpUtils;

/**
 * 机构Service
 * @author ThinkGem
 * @version 2016-4-23
 */
public class OfficeServiceSupport extends TreeService<OfficeDao, Office>
		implements OfficeService{

	@Autowired
	private DataScopeService dataScopeService;
	
	/**
	 * 获取单条数据
	 */
	@Override
	public Office get(Office office) {
		return super.get(office);
	}
	
	/**
	 * 添加数据权限过滤条件
	 */
	@Override
	public void addDataScopeFilter(Office office, String ctrlPermi) {
		office.getSqlMap().getDataScope().addFilter("dsf", "Office", "a.office_code",
				null, ctrlPermi , "office_user");
	}

	/**
	 * 查询组织机构列表
	 */
	@Override
	public List<Office> findList(Office office) {
		return super.findList(office);
	}

	/**
	 * 保存数据（插入或更新）
	 */
	@Override
	@Transactional
	public void save(Office office) {
		if (office.getIsNewRecord()){
			// 生成主键，并验证改主键是否存在，如存在则抛出验证信息
			genIdAndValid(office, office.getViewCode());
			// 当前新数据授权，如果用户有上级数据权限，则当前数据也有相应的数据权限
			dataScopeService.insertIfParentExists(office, "Office");
		}
		super.save(office);
		// 清理部门相关缓存
		clearOfficeCache();
	}

	/**
	 * 导入机构数据
	 * @param file 导入的机构数据文件
	 * @param isUpdateSupport 是否更新支持，如果已存在，则进行更新数据
	 */
	@Transactional
	public String importData(MultipartFile file, Boolean isUpdateSupport) {
		if (file == null){
			throw new ServiceException(text("请选择导入的数据文件！"));
		}
		int successNum = 0; int failureNum = 0;
		StringBuilder successMsg = new StringBuilder();
		StringBuilder failureMsg = new StringBuilder();
		try(ExcelImport ei = new ExcelImport(file, 2, 0)){
			List<Office> list = ei.getDataList(Office.class);
			for (Office office : list) {
				try{
					// 验证数据文件
					ValidatorUtils.validateWithException(office);
					// 验证是否存在这个机构
					Office e = get(office.getOfficeCode());
					if (e == null){
						office.setIsNewRecord(true);
						this.save(office);
						successNum++;
						successMsg.append("<br/>" + successNum + "、机构 " + office.getOfficeCode() + " 导入成功");
					} else if (isUpdateSupport){
						office.setOfficeCode(e.getOfficeCode());
						this.save(office);
						successNum++;
						successMsg.append("<br/>" + successNum + "、机构 " + office.getOfficeCode() + " 更新成功");
					} else {
						failureNum++;
						failureMsg.append("<br/>" + failureNum + "、机构 " + office.getOfficeCode() + " 已存在");
					}
				} catch (Exception e) {
					failureNum++;
					String msg = "<br/>" + failureNum + "、机构 " + office.getOfficeCode() + " 导入失败：";
					if (e instanceof ConstraintViolationException){
						ConstraintViolationException cve = (ConstraintViolationException)e;
						for (ConstraintViolation<?> violation : cve.getConstraintViolations()) {
							msg += Global.getText(violation.getMessage()) + " ("+violation.getPropertyPath()+")";
						}
					}else{
						msg += e.getMessage();
					}
					failureMsg.append(msg);
					logger.error(msg, e);
				}
			}
		} catch (Exception e) {
			failureMsg.append(e.getMessage());
			logger.error(e.getMessage(), e);
		}
		if (failureNum > 0) {
			failureMsg.insert(0, "很抱歉，导入失败！共 " + failureNum + " 条数据格式不正确，错误如下：");
			throw new ServiceException(failureMsg.toString());
		}else{
			successMsg.insert(0, "恭喜您，数据已全部导入成功！共 " + successNum + " 条，数据如下：");
		}
		return successMsg.toString();
	}

	/**
	 * 更新部门状态
	 */
	@Override
	@Transactional
	public void updateStatus(Office office) {
		super.updateStatus(office);
		// 清理部门相关缓存
		clearOfficeCache();
	}

	/**
	 * 删除数据
	 */
	@Override
	@Transactional
	public void delete(Office office) {
		super.delete(office);
		// 清理部门相关缓存
		clearOfficeCache();
	}
	
	/**
	 * 清理部门相关缓存
	 */
	private void clearOfficeCache(){
//		EmpUtils.removeCache(EmpUtils.CACHE_OFFICE_LIST);
		EmpUtils.removeCache(EmpUtils.CACHE_OFFICE_ALL_LIST);
	}

}
