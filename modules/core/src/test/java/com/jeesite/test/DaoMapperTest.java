/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.test;

import java.util.Date;

import org.apache.commons.beanutils.BeanUtils;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.jeesite.common.collect.ListUtils;
import com.jeesite.common.entity.Page;
import com.jeesite.common.tests.BaseSpringContextTests;
import com.jeesite.modules.sys.dao.AreaDao;
import com.jeesite.modules.sys.dao.CompanyDao;
import com.jeesite.modules.sys.dao.ConfigDao;
import com.jeesite.modules.sys.dao.DictDataDao;
import com.jeesite.modules.sys.dao.UserDao;
import com.jeesite.modules.sys.entity.Area;
import com.jeesite.modules.sys.entity.Company;
import com.jeesite.modules.sys.entity.Config;
import com.jeesite.modules.sys.entity.DictData;
import com.jeesite.modules.sys.entity.User;

/**
 * Mapper测试
 * @author ThinkGem
 * @version 2017年2月25日
 */
public class DaoMapperTest extends BaseSpringContextTests {

	@Autowired
	private ConfigDao configDao;
	@Autowired
	private UserDao userDao;
	@Autowired
	private AreaDao areaDao;
	@Autowired
	private CompanyDao companyDao;
	@Autowired
	private DictDataDao dictDataDao;
	
	@Test
	public void testTableAnnotation() throws Exception{
		try{
			
			Config config = new Config();
			config.setId("1");
			config.setConfigKey("test");
			config.setConfigName("test");
			config.setConfigValue("1");
			config.setIsSys("1");
			Config config2 = (Config)BeanUtils.cloneBean(config);
			config2.setId("2");
			Config config3 = (Config)BeanUtils.cloneBean(config);
			config3.setId("3");
			
			System.out.println(configDao.insert(config));
			System.out.println(configDao.insertBatch(ListUtils.newArrayList(config2, config3)));

			User user = new User();
			user.setUserType(User.USER_TYPE_NONE);
			System.out.println(userDao.findList(user));
			
			Area area = new Area();
			area.setAreaCode("1");
			area.setAreaName("你好");
			area.setStatus("0");
			
			Area where = new Area();
			where.setAreaCode("2");
			where.setId_in(new String[]{"1","2"});
			where.setAreaName("你好2");
			where.setStatus("0");
			
			System.out.println(areaDao.update(area));
			System.out.println(areaDao.updateByEntity(area, where));
			System.out.println(areaDao.updateStatus(area));
			System.out.println(areaDao.updateStatusByEntity(area, where));
			System.out.println(areaDao.delete(area));
			System.out.println(areaDao.delete(where));
			System.out.println(areaDao.deleteByEntity(where));
			System.out.println(areaDao.findList(area));

			Company company = new Company("1");
			company.setCompanyName("a");
			company.setCreateDate_gte(new Date());
			company.setCreateDate_lte(new Date());
			company.setArea(new Area("2"));
			company.getArea().setAreaName("a");
			company.getArea().setCreateDate_gte(new Date());
			company.getArea().setCreateDate_lte(new Date());

			company.setFullName("a");
			company.setViewCode("1");
			company.setParentCode("0");
			company.setParentCodes("0,");
			company.setTreeSort(1);
			company.setTreeSorts("1,");
			company.setTreeLevel(0);
			company.setTreeLeaf("1");
			company.setTreeNames("a");
			System.out.println(companyDao.insert(company));
			
			System.out.println(companyDao.get(company));
			System.out.println(companyDao.findCount(company));
			
			company.setPage(new Page<Company>(1, 20));
			company.setIsQueryChildren(true);
			System.out.println(companyDao.findList(company));
			
			DictData dictData = new DictData();
			dictData.setParentCodes("0,");
			System.out.println(dictDataDao.findByParentCodesLike(dictData));
			System.out.println(dictDataDao.findList(dictData));
			
		}catch(Exception e){
			e.printStackTrace();
			throw new Exception(e);
		}
	}
	
}
