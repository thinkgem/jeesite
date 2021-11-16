/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 * No deletion without permission, or be held responsible to law.
 */
package com.jeesite.modules.sys.dao;

import com.jeesite.common.dao.CrudDao;
import com.jeesite.common.mybatis.annotation.MyBatisDao;
import com.jeesite.modules.sys.entity.Employee;

/**
 * 员工管理DAO接口
 * @author ThinkGem
 * @version 2017-03-25
 * 通过 user.getRefObj() 获取引用对象时，需要给 \@MyBatisDao 指定引用 entity 类型。
 * 但是，在 4.2.0+ 版本中，新增了 userTypeMap 指定 service，所以无需设置 entity 类型。
 */
@MyBatisDao(entity = Employee.class)
public interface EmployeeDao extends CrudDao<Employee> {

}