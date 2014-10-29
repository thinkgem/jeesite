/**
 * Copyright &copy; 2012-2013 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 */
package com.thinkgem.jeesite.common.service;

import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Junction;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Lists;
import com.thinkgem.jeesite.modules.sys.entity.Role;
import com.thinkgem.jeesite.modules.sys.entity.User;

/**
 * Service基类
 * @author ThinkGem
 * @version 2013-05-15
 */
public abstract class BaseService {
	
	/**
	 * 日志对象
	 */
	protected Logger logger = LoggerFactory.getLogger(getClass());
	
	/**
	 * 数据范围过滤
	 * @param user 当前用户对象，通过“UserUtils.getUser()”获取
	 * @param officeAlias 机构表别名，例如：dc.createAlias("office", "office");
	 * @param userAlias 用户表别名，传递空，忽略此参数
	 * @return 标准连接条件对象
	 */
	protected static Junction dataScopeFilter(User user, String officeAlias, String userAlias) {

		// 进行权限过滤，多个角色权限范围之间为或者关系。
		List<String> dataScope = Lists.newArrayList();
		Junction junction = Restrictions.disjunction();
		
		// 超级管理员，跳过权限过滤
		if (!user.isAdmin()){
			for (Role r : user.getRoleList()){
				if (!dataScope.contains(r.getDataScope()) && StringUtils.isNotBlank(officeAlias)){
					boolean isDataScopeAll = false;
					if (Role.DATA_SCOPE_ALL.equals(r.getDataScope())){
						isDataScopeAll = true;
					}
					else if (Role.DATA_SCOPE_COMPANY_AND_CHILD.equals(r.getDataScope())){
						junction.add(Restrictions.eq(officeAlias+".id", user.getCompany().getId()));
						junction.add(Restrictions.like(officeAlias+".parentIds", user.getCompany().getParentIds()+user.getCompany().getId()+",%"));
					}
					else if (Role.DATA_SCOPE_COMPANY.equals(r.getDataScope())){
						junction.add(Restrictions.eq(officeAlias+".id", user.getCompany().getId()));
						junction.add(Restrictions.and(Restrictions.eq(officeAlias+".parent.id", user.getCompany().getId()),
								Restrictions.eq(officeAlias+".type", "2"))); // 包括本公司下的部门
					}
					else if (Role.DATA_SCOPE_OFFICE_AND_CHILD.equals(r.getDataScope())){
						junction.add(Restrictions.eq(officeAlias+".id", user.getOffice().getId()));
						junction.add(Restrictions.like(officeAlias+".parentIds", user.getOffice().getParentIds()+user.getOffice().getId()+",%"));
					}
					else if (Role.DATA_SCOPE_OFFICE.equals(r.getDataScope())){
						junction.add(Restrictions.eq(officeAlias+".id", user.getOffice().getId()));
					}
					else if (Role.DATA_SCOPE_CUSTOM.equals(r.getDataScope())){
						junction.add(Restrictions.in(officeAlias+".id", r.getOfficeIdList()));
					}
					//else if (Role.DATA_SCOPE_SELF.equals(r.getDataScope())){
					if (!isDataScopeAll){
						if (StringUtils.isNotBlank(userAlias)){
							junction.add(Restrictions.eq(userAlias+".id", user.getId()));
						}else {
							junction.add(Restrictions.isNull(officeAlias+".id"));
						}
					}else{
						// 如果包含全部权限，则去掉之前添加的所有条件，并跳出循环。
						junction = Restrictions.disjunction();
						break;
					}
					dataScope.add(r.getDataScope());
				}
			}
		}
		return junction;
	}
	
	/**
	 * 数据范围过滤
	 * @param user 当前用户对象，通过“UserUtils.getUser()”获取
	 * @param officeAlias 机构表别名，例如：dc.createAlias("office", "office");
	 * @param userAlias 用户表别名，传递空，忽略此参数
	 * @return ql查询字符串
	 */
	protected static String dataScopeFilterString(User user, String officeAlias, String userAlias) {
		Junction junction = dataScopeFilter(user, officeAlias, userAlias);
		Iterator<Criterion> it = junction.conditions().iterator();
		StringBuilder ql = new StringBuilder();
		ql.append(" and (");
		if (it.hasNext()){
			ql.append(it.next());
		}
		String[] strField = {".parentIds like ", ".type="}; // 需要给字段增加“单引号”的字段。
		while (it.hasNext()) {
			ql.append(" or (");
			String s = it.next().toString();
			for(String field : strField){
				s = s.replaceAll(field + "(\\w.*)", field + "'$1'");
			}
			ql.append(s).append(")");
		}
		ql.append(")");
		return ql.toString();
	}

	protected List<Long> getIdList(String ids) {
		List<Long> idList = Lists.newArrayList();
		if(StringUtils.isNotBlank(ids)) {
			ids = ids.trim().replace("　", ",").replace(" ", ",").replace("，", ",");
			String[] arrId = ids.split(",");
			for(String id:arrId) {
				if(id.matches("\\d*")) {
					idList.add(Long.valueOf(id));
				}
			}
		}
		return idList;
	}
}
