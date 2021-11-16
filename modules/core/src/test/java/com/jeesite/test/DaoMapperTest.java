/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 * No deletion without permission, or be held responsible to law.
 */
package com.jeesite.test;

import java.util.Date;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import com.jeesite.common.collect.ListUtils;
import com.jeesite.common.entity.DataScope;
import com.jeesite.common.entity.Page;
import com.jeesite.common.mybatis.mapper.query.QueryType;
import com.jeesite.common.tests.BaseSpringContextTests;
import com.jeesite.modules.file.dao.FileUploadDao;
import com.jeesite.modules.file.entity.FileUpload;
import com.jeesite.modules.sys.dao.AreaDao;
import com.jeesite.modules.sys.dao.CompanyDao;
import com.jeesite.modules.sys.dao.ConfigDao;
import com.jeesite.modules.sys.dao.DictDataDao;
import com.jeesite.modules.sys.dao.EmpUserDao;
import com.jeesite.modules.sys.dao.UserDao;
import com.jeesite.modules.sys.entity.Area;
import com.jeesite.modules.sys.entity.Company;
import com.jeesite.modules.sys.entity.Config;
import com.jeesite.modules.sys.entity.DictData;
import com.jeesite.modules.sys.entity.EmpUser;
import com.jeesite.modules.sys.entity.User;

/**
 * Mapper测试
 * @author ThinkGem
 * @version 2018-08-11
 */
@ActiveProfiles("test")
@SpringBootTest(classes=ApplicationTest.class)
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
	@Autowired
	private FileUploadDao fileUploadDao;
	@Autowired
	private EmpUserDao empUserDao;
	
	@Test
	public void testTableAnnotation() throws Exception{
		try{
			
			System.out.println("============ 插入和批量插入测试 ============");
			Config config = new Config();
			config.setId("1");
			config.setConfigKey("test");
			config.setConfigName("test");
			config.setConfigValue("1");
			config.setIsSys("1");
			Config config2 = (Config)config.clone();
			config2.setId("2");
			config.setConfigKey("test2");
			Config config3 = (Config)config.clone();
			config3.setId("3");
			config.setConfigKey("test3");
			System.out.println(configDao.insert(config));
			System.out.println(configDao.insertBatch(ListUtils.newArrayList(config2, config3)));
			
			System.out.println("============ 更新测试 ============");
			Area area = new Area();
			area.setAreaCode("1");
			area.setAreaName("你好");
			area.setStatus("0");
			Area where = new Area();
			where.setId("2");
			where.setId_in(new String[]{"1","2"});
			where.setAreaName("你好2");
			where.setStatus("0");
			System.out.println(areaDao.update(area));
			System.out.println(areaDao.updateByEntity(area, where));
			System.out.println(areaDao.updateStatus(area));
			System.out.println(areaDao.updateStatusByEntity(area, where));
			
			System.out.println("============ 逻辑删除测试 ============");
			System.out.println(areaDao.delete(area));
			System.out.println(areaDao.delete((Area)where.clone()));
			System.out.println(areaDao.deleteByEntity((Area)where.clone()));

			System.out.println("============ 物理删除测试 ============");
			System.out.println(areaDao.phyDelete((Area)where.clone()));
			System.out.println(areaDao.phyDeleteByEntity((Area)where.clone()));

			System.out.println("============ 基本信息查询测试 ============");
			System.out.println(areaDao.findList(area));
			User user = new User();
			user.setUserType(User.USER_TYPE_NONE);
			System.out.println(userDao.findList(user));
			
			System.out.println("============ 条件嵌套，日期范围，自定义sqlMap测试 ============");
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
			
			System.out.println("============ 分页测试，查询子节点 ============");
			company.setPage(new Page<Company>(1, 20));
			company.setIsQueryChildren(true);
			System.out.println(companyDao.findList(company));
			
			System.out.println("============ 扩展条件语句前带AND容错测试 ============");
			Company company2 = new Company();
			company2.getSqlMap().getWhere().disableAutoAddStatusWhere();
			company2.getSqlMap().getDataScope().addFilter("dsf",
					"Company", "a.`company_code`", DataScope.CTRL_PERMI_HAVE);
			System.out.println(companyDao.findList(company2));

			System.out.println("============ 联合查询未设定columns和attrName为this时测试 ============");
			FileUpload fileUpload = new FileUpload();
			fileUpload.getSqlMap().getWhere().and("u.`user_name`", QueryType.EQ, "user1");
			System.out.println(fileUploadDao.findList(fileUpload));
			
			System.out.println("============ 树结构基本查询测试 ============");
			DictData dictData = new DictData();
			dictData.setParentCodes("0,");
			System.out.println(dictDataDao.findByParentCodesLike(dictData));
			System.out.println(dictDataDao.findList(dictData));

			System.out.println("============ 分页情况下foreach测试 ============");
			EmpUser empUser = new EmpUser();
			empUser.setCodes(new String[]{"SDJN01","SDJN02"});
			empUser.setPage(new Page<>(1, 3));
			List<EmpUser> list = empUserDao.findUserListByOfficeCodes(empUser);
			System.out.println(list);
			
		}catch(Exception e){
			e.printStackTrace();
			throw new Exception(e);
		}
	}
	
	public static void main(String[] args) {
		String a = null, b = null;
		System.out.println("============ 基本测试 ============");
		a = new Config("1").getSqlMap()
				.getWhere().and("name", QueryType.EQ, "abc").toSql();
		b = "a.`id` = #{sqlMap.where#id#EQ1} AND a.name = #{sqlMap.where.name#EQ1.val}";
		System.out.println("a >> "+a);System.out.println("b >> "+b);Assert.assertEquals(a, b);
		
		a = new Config("1").getSqlMap().getWhere()
				.and("name", QueryType.IN, new String[]{"1", "2", "3"}).toSql();
		b = "a.`id` = #{sqlMap.where#id#EQ1} AND a.name IN ( #{sqlMap.where.name#IN1.val[0]},"
				+ " #{sqlMap.where.name#IN1.val[1]}, #{sqlMap.where.name#IN1.val[2]} )";
		System.out.println("a >> "+a);System.out.println("b >> "+b);Assert.assertEquals(a, b);
		
		a = new Config("1").getSqlMap().getWhere()
				.or("name", QueryType.IN, new String[]{"1", "2", "3"}).toSql();
		b = "a.`id` = #{sqlMap.where#id#EQ1} OR a.name IN ( #{sqlMap.where.name#IN1.val[0]},"
				+ " #{sqlMap.where.name#IN1.val[1]}, #{sqlMap.where.name#IN1.val[2]} )";
		System.out.println("a >> "+a);System.out.println("b >> "+b);Assert.assertEquals(a, b);
		
		System.out.println("============ 重复赋值测试 ============");
		a = new Config("1").getSqlMap().getWhere()
				.and("name", QueryType.LIKE, "abc").and("name", QueryType.LIKE, "def").toSql();
		b = "a.`id` = #{sqlMap.where#id#EQ1} AND a.name LIKE #{sqlMap.where.name#LIKE1.val}";
		System.out.println("a >> "+a);System.out.println("b >> "+b);Assert.assertEquals(a, b);
		
		System.out.println("============ IN、NOT IN 测试 ============");
		a = new Config("1").getSqlMap().getWhere()
				.and("name", QueryType.IN, new String[]{"abc","def"})
				.and("name2", QueryType.NOT_IN, ListUtils.newArrayList("abc","def")).toSql();
		b = "a.`id` = #{sqlMap.where#id#EQ1} AND a.name IN ( #{sqlMap.where.name#IN1.val[0]}, #{sqlMap.where.name#IN1.val[1]} )"
				+ " AND a.name2 NOT IN ( #{sqlMap.where.name2#NOT_IN1.val[0]}, #{sqlMap.where.name2#NOT_IN1.val[1]} )";
		System.out.println("a >> "+a);System.out.println("b >> "+b);Assert.assertEquals(a, b);
		
		a = new Config("1").getSqlMap().getWhere()
				.and("name", QueryType.IN, null).and("name2", QueryType.IN, new String[]{})
				.and("name3", QueryType.NOT_IN, ListUtils.newArrayList()).toSql();
		b = "a.`id` = #{sqlMap.where#id#EQ1}";
		System.out.println("a >> "+a);System.out.println("b >> "+b);Assert.assertEquals(a, b);
		
		System.out.println("============ 带括号测试 ============");
		a = new Config("1").getSqlMap().getWhere()
				.andBracket("name", QueryType.EQ, "abc", 1).or("name", QueryType.EQ, "def", 2)
					.or("name", QueryType.EQ, "ghi", 3).endBracket().toSql();
		b = "a.`id` = #{sqlMap.where#id#EQ1} AND ( a.name = #{sqlMap.where.name#EQ1.val}"
				+ " OR a.name = #{sqlMap.where.name#EQ2.val} OR a.name = #{sqlMap.where.name#EQ3.val} )";
		System.out.println("a >> "+a);System.out.println("b >> "+b);Assert.assertEquals(a, b);
		
		a = new Config().getSqlMap().getWhere()
				.andBracket("name", QueryType.EQ, "val1", 1).and("name", QueryType.NE, "val2", 11).endBracket(1)
				.orBracket("name", QueryType.NE, "val3", 2).and("name", QueryType.NE, "val4", 22).endBracket(2)
				.orBracket("name", QueryType.NE, "val5", 3).and("name", QueryType.EQ, "val6", 33).endBracket(3)
				.toSql();
		b = "( a.name = #{sqlMap.where.name#EQ1.val} AND a.name != #{sqlMap.where.name#NE11.val} )"
				+ " OR ( a.name != #{sqlMap.where.name#NE2.val} AND a.name != #{sqlMap.where.name#NE22.val} )"
				+ " OR ( a.name != #{sqlMap.where.name#NE3.val} AND a.name = #{sqlMap.where.name#EQ33.val} )";
		System.out.println("a >> "+a);System.out.println("b >> "+b);Assert.assertEquals(a, b);
		
		System.out.println("============ 带括号部分空值测试 ============");
		a = new Config("1").getSqlMap().getWhere()
				.andBracket("name", QueryType.EQ, "", 1).or("name", QueryType.EQ, "def", 2)
					.or("name", QueryType.EQ, "", 3).endBracket().toSql();
		b = "a.`id` = #{sqlMap.where#id#EQ1} AND ( a.name = #{sqlMap.where.name#EQ2.val} )";
		System.out.println("a >> "+a);System.out.println("b >> "+b);Assert.assertEquals(a, b);
		
		a = new Config("1").getSqlMap().getWhere()
				.andBracket("name", QueryType.EQ, "abc", 1).or("name", QueryType.EQ, "def", 2)
				.or("name", QueryType.EQ, "", 3).endBracket().toSql();
		b = "a.`id` = #{sqlMap.where#id#EQ1} AND ( a.name = #{sqlMap.where.name#EQ1.val}"
				+ " OR a.name = #{sqlMap.where.name#EQ2.val} )";
		System.out.println("a >> "+a);System.out.println("b >> "+b);Assert.assertEquals(a, b);
		
		a = new Config("1").getSqlMap().getWhere()
				.andBracket("name", QueryType.EQ, "", 1).or("name", QueryType.EQ, "def", 2)
				.or("name", QueryType.EQ, "ghi", 3).endBracket().toSql();
		b = "a.`id` = #{sqlMap.where#id#EQ1} AND ( a.name = #{sqlMap.where.name#EQ2.val}"
				+ " OR a.name = #{sqlMap.where.name#EQ3.val} )";
		System.out.println("a >> "+a);System.out.println("b >> "+b);Assert.assertEquals(a, b);
		
		System.out.println("============ 带括号全部空值测试 ============");
		a = new Config("1").getSqlMap().getWhere()
				.andBracket("name", QueryType.EQ, "", 1).or("name", QueryType.EQ, "", 2)
				.or("name", QueryType.EQ, "", 3).endBracket().toSql();
		b = "a.`id` = #{sqlMap.where#id#EQ1} ";
		System.out.println("a >> "+a);System.out.println("b >> "+b);Assert.assertEquals(a, b);
		
		a = new Config().getSqlMap().getWhere()
				.andBracket("name", QueryType.EQ, "", 1).or("name", QueryType.EQ, "", 2)
				.or("name", QueryType.EQ, "", 3).endBracket().toSql();
		b = "";
		System.out.println("a >> "+a);System.out.println("b >> "+b);Assert.assertEquals(a, b);
		
		System.out.println("============ 实体嵌套测试 ============");
		Company company = new Company("1");
		company.setCreateDate_gte(new Date());
		company.setCreateDate_lte(new Date());
		company.setArea(new Area("2"));
		company.getArea().setAreaName("a");
		company.getArea().setCreateDate_gte(new Date());
		company.getArea().setCreateDate_lte(new Date());
		company.getSqlMap().getWhere().disableAutoAddStatusWhere();
		a = company.getSqlMap().getWhere().toSql();
		b = "a.`company_code` = #{sqlMap.where#company_code#EQ1}"
				+ " AND a.`area_code` = #{sqlMap.where#area_code#EQ1}"
				+ " AND a.create_date >= #{sqlMap.where.create_date#GTE1.val}"
				+ " AND a.create_date <= #{sqlMap.where.create_date#LTE1.val}"
				+ " AND b.`area_code` = #{area.sqlMap.where#area_code#EQ1}"
				+ " AND b.create_date >= #{area.sqlMap.where.create_date#GTE1.val}"
				+ " AND b.create_date <= #{area.sqlMap.where.create_date#LTE1.val}";
		System.out.println("a >> "+a);System.out.println("b >> "+b);Assert.assertEquals(a, b);

		System.out.println("============ 联合查询，属性名支持指定别名 ============");
		a = new FileUpload().getSqlMap().getWhere().and("u.`user_name`", QueryType.EQ, "user1").toSql();
		b = "a.`status` != #{STATUS_DELETE} AND u.`user_name` = #{sqlMap.where.u#_user_name_#EQ1.val}";
		System.out.println("a >> "+a);System.out.println("b >> "+b);Assert.assertEquals(a, b);
		
		System.out.println("============ 联合查询，返回到当前实体测试 ============");
		FileUpload fileUpload = new FileUpload();
		fileUpload.getSqlMap().getWhere().and("create_by", QueryType.IN, new String[]{"user1","user2"});
		a = fileUpload.getSqlMap().getWhere().toSql();
		b = "a.`status` != #{STATUS_DELETE} AND a.create_by IN ( #{sqlMap.where.create_by#IN1.val[0]},"
				+ " #{sqlMap.where.create_by#IN1.val[1]} )";
		System.out.println("a >> "+a);System.out.println("b >> "+b);Assert.assertEquals(a, b);
		
		System.out.println("============ 联合查询，属性为this时也可作为查询条件 ============");
		FileUpload fileUpload2 = new FileUpload();
		fileUpload2.setCreateByName("ThinkGem/JeeSite");
		fileUpload2.getSqlMap().getWhere().and("create_by", QueryType.IN, new String[]{"user1","user2"});
		a = fileUpload2.getSqlMap().getWhere().toSql();
		b = "a.`status` != #{STATUS_DELETE} AND a.create_by IN ( #{sqlMap.where.create_by#IN1.val[0]},"
				+ " #{sqlMap.where.create_by#IN1.val[1]} ) AND u.`user_name` LIKE #{sqlMap.where#user_name#LIKE1}";
		System.out.println("a >> "+a);System.out.println("b >> "+b);Assert.assertEquals(a, b);
		
		System.exit(0);
	}
}
