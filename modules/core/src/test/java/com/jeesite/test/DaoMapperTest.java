/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 * No deletion without permission, or be held responsible to law.
 */
package com.jeesite.test;

import com.jeesite.common.collect.ListUtils;
import com.jeesite.common.entity.DataScope;
import com.jeesite.common.entity.Page;
import com.jeesite.common.idgen.IdGen;
import com.jeesite.common.mybatis.mapper.query.QueryType;
import com.jeesite.common.tests.BaseSpringContextTests;
import com.jeesite.modules.file.dao.FileUploadDao;
import com.jeesite.modules.file.entity.FileUpload;
import com.jeesite.modules.sys.dao.*;
import com.jeesite.modules.sys.entity.*;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Date;
import java.util.List;

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

			System.out.println("============ 插入测试 ============");
			Config config = new Config();
			config.setId("1");
			config.setConfigKey("test");
			config.setConfigName("test");
			config.setConfigValue("1");
			config.setIsSys("1");
			long configInsertNum = configDao.insert(config);
			Assert.assertEquals("configDao.insert", configInsertNum , 1);
			Config configInsertRes = configDao.get(config);
			Assert.assertEquals("configDao.insert result", configInsertRes.getId() , "1");

			System.out.println("============ 批量插入测试 ============");
			Config config2 = (Config)config.clone();
			config2.setId("2");
			config2.setConfigKey("test2");
			Config config3 = (Config)config.clone();
			config3.setId("3");
			config3.setConfigKey("test3");
			long configinsertBatchNum = configDao.insertBatch(ListUtils.newArrayList(config2, config3));
			Assert.assertEquals("configDao.insertBatch", configinsertBatchNum , 2);
			Config configInsertBatchRes2 = configDao.get(config2);
			Assert.assertEquals("configDao.insertBatch result", configInsertBatchRes2.getId() , "2");
			Config configInsertBatchRes3 = configDao.get(config3);
			Assert.assertEquals("configDao.insertBatch result", configInsertBatchRes3.getId() , "3");

			System.out.println("============ 按主键更新测试 ============");
			Area area = new Area();
			area.setAreaCode("1");
			area.setAreaName("你好");
			area.setParentCode("0");
			area.setParentCodes("0,");
			area.setTreeSort(1);
			area.setTreeSorts("1,");
			area.setTreeLevel(0);
			area.setTreeLeaf("1");
			area.setTreeNames(area.getAreaName());
			area.setStatus("0");
			Area area2 = (Area) area.clone();
			area2.setAreaCode("2");
			Area area3 = (Area) area.clone();
			area3.setAreaCode("3");
			long areaInsertNum = areaDao.insertBatch(ListUtils.newArrayList(area, area2, area3));
			Assert.assertEquals("areaDao.insert", areaInsertNum , 3);
			area.setAreaName("你好2");
			long areaUpdateNum = areaDao.update(area);
			Assert.assertEquals("areaDao.update", areaUpdateNum , 1);
			Area areaUpdateRes = areaDao.get(area);
			Assert.assertEquals("areaDao.update result", areaUpdateRes.getAreaName() , "你好2");

			System.out.println("============ 按主键批量更新测试 ============");
			long areaUpdateBatchNum = areaDao.updateBatch(ListUtils.newArrayList(area2, area3));
			Assert.assertEquals("areaDao.update", areaUpdateBatchNum , 2);

			System.out.println("============ 自定义更新条件测试 ============");
			Area where = new Area();
			where.setId(areaUpdateRes.getId());
			where.setId_in(new String[]{areaUpdateRes.getId()});
			where.setAreaName(areaUpdateRes.getAreaName());
			where.setStatus(areaUpdateRes.getStatus());
			long areaUpdateByEntityNum = areaDao.updateByEntity(area, where);
			Assert.assertEquals("areaDao.updateByEntity", areaUpdateByEntityNum , 1);

			System.out.println("============ 更新数据状态测试 ============");
			long areaStatusNum = areaDao.updateStatus(area);
			Assert.assertEquals("areaDao.updateStatus", areaUpdateByEntityNum , 1);
			long areaStatusByEntityNum = areaDao.updateStatusByEntity(area, where);
			Assert.assertEquals("areaDao.updateStatusByEntity", areaUpdateByEntityNum , 1);

			System.out.println("============ 逻辑删除测试 ============");
			long areaDeleteNum = areaDao.delete(area);
			Assert.assertEquals("areaDao.delete", areaDeleteNum , 1);
			where.setStatus("1");
			long areaDeleteByEntityNum = areaDao.deleteByEntity(where);
			Assert.assertEquals("areaDao.deleteByEntity", areaDeleteByEntityNum , 1);

			System.out.println("============ 物理删除测试 ============");
			long areaPhyDeleteNum = areaDao.phyDelete(area2);
			Assert.assertEquals("areaDao.phyDelete", areaPhyDeleteNum , 1);
			long areaPhyDeleteByEntityNum = areaDao.phyDeleteByEntity(area3);
			Assert.assertEquals("areaDao.phyDeleteByEntity", areaPhyDeleteByEntityNum , 1);

			System.out.println("============ 基本查询测试 ============");
			List<Area> areaList = areaDao.findList(area);
			Assert.assertEquals("areaDao.findList", areaList.size() , 1);
			User user = new User();
			user.setUserType(User.USER_TYPE_NONE);
			List<User> userList = userDao.findList(user);
			Assert.assertTrue("userDao.findList", userList.size() > 0);

			System.out.println("============ 条件嵌套，日期范围，自定义sqlMap测试 ============");
			Company company = new Company("1");
			company.setCompanyName("a");
			company.setCreateDate_gte(new Date());
			company.setCreateDate_lte(new Date());
			company.setArea(areaList.get(0));
			company.getArea().setCreateDate_gte(company.getCreateDate_gte());
			company.getArea().setCreateDate_lte(company.getCreateDate_gte());
			company.setFullName(IdGen.nextId());
			company.setViewCode("1");
			company.setParentCode("0");
			company.setParentCodes("0,");
			company.setTreeSort(1);
			company.setTreeSorts("1,");
			company.setTreeLevel(0);
			company.setTreeLeaf("1");
			company.setTreeNames(company.getCompanyName());
			Company company2 = (Company) company.clone();
			company2.setParentCode(company.getCompanyCode());
			company2.setParentCodes("0,1," + company.getCompanyCode());
			company2.setCompanyCode("12");
			Company company3 = (Company) company.clone();
			company3.setParentCode(company.getCompanyCode());
			company3.setParentCodes("0,1," + company.getCompanyCode());
			company3.setCompanyCode("13");
			company3.setCompanyName("b");
			long companyInsertNum = companyDao.insertBatch(ListUtils.newArrayList(company, company2, company3));
			Assert.assertEquals("advanced query init", companyInsertNum , 3);
			Company companyWhere = (Company) company.clone();
			companyWhere.setCompanyCode(null);
			companyWhere.setParentCode(null);
			long companyFindCount = companyDao.findCount(companyWhere);
			Assert.assertEquals("advanced query list", companyFindCount , 2);

			System.out.println("============ 分页测试，查询子节点 ============");
			Company company4 = new Company("1");
			company4.setFullName(company.getFullName());
			company4.setPage(new Page<>(1, 2));
			company4.setIsQueryChildren(true);
			List<Company> companyListPage = companyDao.findList(company4);
			Assert.assertEquals("find page list size", companyListPage.size(), 2);
			Assert.assertEquals("find page list get(1)", companyListPage.get(1).getCompanyCode(), company2.getCompanyCode());
			Assert.assertEquals("find page count", company4.getPage().getCount(), 3);

			System.out.println("============ 扩展条件语句前带AND容错测试 ============");
			Company company5 = new Company();
			company5.sqlMap().getWhere().disableAutoAddStatusWhere();
			company5.sqlMap().getDataScope().addFilter("dsf",
					"Company", "a.`company_code`", DataScope.CTRL_PERMI_HAVE);
			List<Company> companyList = companyDao.findList(company5);
			System.out.println(companyList);
			Assert.assertEquals("companyDao.findList extWhere", companyList.size(), 0);

			System.out.println("============ 联合查询未设定columns和attrName为this时测试 ============");
			FileUpload fileUpload = new FileUpload();
			fileUpload.sqlMap().getWhere().and("u.`user_name`", QueryType.EQ, "user1");
			List<FileUpload> fileUploadList = fileUploadDao.findList(fileUpload);
			System.out.println(fileUploadList);
			Assert.assertEquals("fileUploadDao.findList attrName this", fileUploadList.size(), 0);

			System.out.println("============ 树结构基本查询测试 ============");
			DictData dictData = new DictData();
			dictData.setParentCodes("0,");
			List<DictData> dictDataList = dictDataDao.findByParentCodesLike(dictData);
			Assert.assertTrue("dictDataDao.findByParentCodesLike", dictDataList.size() > 0);
			List<DictData> dictDataList2 = dictDataDao.findList(dictData);
			System.out.println(dictDataList2);
			Assert.assertTrue("dictDataDao.findList", dictDataList2.size() > 0);

			System.out.println("============ 分页情况下foreach测试 ============");
			EmpUser empUser = new EmpUser();
			empUser.setCodes(new String[]{"SDJN01","SDJN02"});
			empUser.setPage(new Page<>(1, 3));
			List<EmpUser> empUserList = empUserDao.findUserListByOfficeCodes(empUser);
			System.out.println(empUserList);
			Assert.assertTrue("empUserDao.findUserListByOfficeCodes", empUserList.size() > 0);

		}catch(Exception e){
			e.printStackTrace();
			throw new Exception(e);
		}
	}

	public static void main(String[] args) {
		String a = null, b = null;
		System.out.println("============ 基本测试 ============");
		a = new Config("1").sqlMap()
				.getWhere().and("name", QueryType.EQ, "abc").toSql();
		b = "a.`id` = #{sqlMap.where#id#EQ1} AND a.name = #{sqlMap.where.name#EQ1.val}";
		System.out.println("a >> "+a);System.out.println("b >> "+b);Assert.assertEquals(a, b);

		a = new Config("1").sqlMap().getWhere()
				.and("name", QueryType.IN, new String[]{"1", "2", "3"}).toSql();
		b = "a.`id` = #{sqlMap.where#id#EQ1} AND a.name IN ( #{sqlMap.where.name#IN1.val[0]},"
				+ " #{sqlMap.where.name#IN1.val[1]}, #{sqlMap.where.name#IN1.val[2]} )";
		System.out.println("a >> "+a);System.out.println("b >> "+b);Assert.assertEquals(a, b);

		a = new Config("1").sqlMap().getWhere()
				.or("name", QueryType.IN, new String[]{"1", "2", "3"}).toSql();
		b = "a.`id` = #{sqlMap.where#id#EQ1} OR a.name IN ( #{sqlMap.where.name#IN1.val[0]},"
				+ " #{sqlMap.where.name#IN1.val[1]}, #{sqlMap.where.name#IN1.val[2]} )";
		System.out.println("a >> "+a);System.out.println("b >> "+b);Assert.assertEquals(a, b);

		System.out.println("============ 重复赋值测试 ============");
		a = new Config("1").sqlMap().getWhere()
				.and("name", QueryType.LIKE, "abc").and("name", QueryType.LIKE, "def").toSql();
		b = "a.`id` = #{sqlMap.where#id#EQ1} AND a.name LIKE #{sqlMap.where.name#LIKE1.val}";
		System.out.println("a >> "+a);System.out.println("b >> "+b);Assert.assertEquals(a, b);

		System.out.println("============ IN、NOT IN 测试 ============");
		a = new Config("1").sqlMap().getWhere()
				.and("name", QueryType.IN, new String[]{"abc","def"})
				.and("name2", QueryType.NOT_IN, ListUtils.newArrayList("abc","def")).toSql();
		b = "a.`id` = #{sqlMap.where#id#EQ1} AND a.name IN ( #{sqlMap.where.name#IN1.val[0]}, #{sqlMap.where.name#IN1.val[1]} )"
				+ " AND a.name2 NOT IN ( #{sqlMap.where.name2#NOT_IN1.val[0]}, #{sqlMap.where.name2#NOT_IN1.val[1]} )";
		System.out.println("a >> "+a);System.out.println("b >> "+b);Assert.assertEquals(a, b);

		a = new Config("1").sqlMap().getWhere()
				.and("name", QueryType.IN, null).and("name2", QueryType.IN, new String[]{})
				.and("name3", QueryType.NOT_IN, ListUtils.newArrayList()).toSql();
		b = "a.`id` = #{sqlMap.where#id#EQ1}";
		System.out.println("a >> "+a);System.out.println("b >> "+b);Assert.assertEquals(a, b);

		System.out.println("============ 带括号测试 ============");
		a = new Config("1").sqlMap().getWhere()
				.andBracket("name", QueryType.EQ, "abc", 1).or("name", QueryType.EQ, "def", 2)
				.or("name", QueryType.EQ, "ghi", 3).endBracket().toSql();
		b = "a.`id` = #{sqlMap.where#id#EQ1} AND ( a.name = #{sqlMap.where.name#EQ1.val}"
				+ " OR a.name = #{sqlMap.where.name#EQ2.val} OR a.name = #{sqlMap.where.name#EQ3.val} )";
		System.out.println("a >> "+a);System.out.println("b >> "+b);Assert.assertEquals(a, b);

		a = new Config().sqlMap().getWhere()
				.andBracket("name", QueryType.EQ, "val1", 1).and("name", QueryType.NE, "val2", 11).endBracket(1)
				.orBracket("name", QueryType.NE, "val3", 2).and("name", QueryType.NE, "val4", 22).endBracket(2)
				.orBracket("name", QueryType.NE, "val5", 3).and("name", QueryType.EQ, "val6", 33).endBracket(3)
				.toSql();
		b = "( a.name = #{sqlMap.where.name#EQ1.val} AND a.name != #{sqlMap.where.name#NE11.val} )"
				+ " OR ( a.name != #{sqlMap.where.name#NE2.val} AND a.name != #{sqlMap.where.name#NE22.val} )"
				+ " OR ( a.name != #{sqlMap.where.name#NE3.val} AND a.name = #{sqlMap.where.name#EQ33.val} )";
		System.out.println("a >> "+a);System.out.println("b >> "+b);Assert.assertEquals(a, b);

		System.out.println("============ 带括号部分空值测试 ============");
		a = new Config("1").sqlMap().getWhere()
				.andBracket("name", QueryType.EQ, "", 1).or("name", QueryType.EQ, "def", 2)
				.or("name", QueryType.EQ, "", 3).endBracket().toSql();
		b = "a.`id` = #{sqlMap.where#id#EQ1} AND ( a.name = #{sqlMap.where.name#EQ2.val} )";
		System.out.println("a >> "+a);System.out.println("b >> "+b);Assert.assertEquals(a, b);

		a = new Config("1").sqlMap().getWhere()
				.andBracket("name", QueryType.EQ, "abc", 1).or("name", QueryType.EQ, "def", 2)
				.or("name", QueryType.EQ, "", 3).endBracket().toSql();
		b = "a.`id` = #{sqlMap.where#id#EQ1} AND ( a.name = #{sqlMap.where.name#EQ1.val}"
				+ " OR a.name = #{sqlMap.where.name#EQ2.val} )";
		System.out.println("a >> "+a);System.out.println("b >> "+b);Assert.assertEquals(a, b);

		a = new Config("1").sqlMap().getWhere()
				.andBracket("name", QueryType.EQ, "", 1).or("name", QueryType.EQ, "def", 2)
				.or("name", QueryType.EQ, "ghi", 3).endBracket().toSql();
		b = "a.`id` = #{sqlMap.where#id#EQ1} AND ( a.name = #{sqlMap.where.name#EQ2.val}"
				+ " OR a.name = #{sqlMap.where.name#EQ3.val} )";
		System.out.println("a >> "+a);System.out.println("b >> "+b);Assert.assertEquals(a, b);

		System.out.println("============ 带括号全部空值测试 ============");
		a = new Config("1").sqlMap().getWhere()
				.andBracket("name", QueryType.EQ, "", 1).or("name", QueryType.EQ, "", 2)
				.or("name", QueryType.EQ, "", 3).endBracket().toSql();
		b = "a.`id` = #{sqlMap.where#id#EQ1} ";
		System.out.println("a >> "+a);System.out.println("b >> "+b);Assert.assertEquals(a, b);

		a = new Config().sqlMap().getWhere()
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
		company.sqlMap().getWhere().disableAutoAddStatusWhere();
		a = company.sqlMap().getWhere().toSql();
		b = "a.`company_code` = #{sqlMap.where#company_code#EQ1}"
				+ " AND a.`area_code` = #{sqlMap.where#area_code#EQ1}"
				+ " AND a.create_date >= #{sqlMap.where.create_date#GTE1.val}"
				+ " AND a.create_date <= #{sqlMap.where.create_date#LTE1.val}"
				+ " AND b.`area_code` = #{area.sqlMap.where#area_code#EQ1}"
				+ " AND b.create_date >= #{area.sqlMap.where.create_date#GTE1.val}"
				+ " AND b.create_date <= #{area.sqlMap.where.create_date#LTE1.val}";
		System.out.println("a >> "+a);System.out.println("b >> "+b);Assert.assertEquals(a, b);

		System.out.println("============ 联合查询，属性名支持指定别名 ============");
		a = new FileUpload().sqlMap().getWhere().and("u.`user_name`", QueryType.EQ, "user1").toSql();
		b = "a.`status` != #{STATUS_DELETE} AND u.`user_name` = #{sqlMap.where.u#_user_name_#EQ1.val}";
		System.out.println("a >> "+a);System.out.println("b >> "+b);Assert.assertEquals(a, b);

		System.out.println("============ 联合查询，返回到当前实体测试 ============");
		FileUpload fileUpload = new FileUpload();
		fileUpload.sqlMap().getWhere().and("create_by", QueryType.IN, new String[]{"user1","user2"});
		a = fileUpload.sqlMap().getWhere().toSql();
		b = "a.`status` != #{STATUS_DELETE} AND a.create_by IN ( #{sqlMap.where.create_by#IN1.val[0]},"
				+ " #{sqlMap.where.create_by#IN1.val[1]} )";
		System.out.println("a >> "+a);System.out.println("b >> "+b);Assert.assertEquals(a, b);

		System.out.println("============ 联合查询，属性为this时也可作为查询条件 ============");
		FileUpload fileUpload2 = new FileUpload();
		fileUpload2.setCreateByName("ThinkGem/JeeSite");
		fileUpload2.sqlMap().getWhere().and("create_by", QueryType.IN, new String[]{"user1","user2"});
		a = fileUpload2.sqlMap().getWhere().toSql();
		b = "a.`status` != #{STATUS_DELETE} AND a.create_by IN ( #{sqlMap.where.create_by#IN1.val[0]},"
				+ " #{sqlMap.where.create_by#IN1.val[1]} ) AND u.`user_name` LIKE #{sqlMap.where#user_name#LIKE1}";
		System.out.println("a >> "+a);System.out.println("b >> "+b);Assert.assertEquals(a, b);

		System.out.println("============ 条件嵌套查询，可替代 andBracket、orBracket、endBracket v5.2.1+ ============");
		a = new Config("1").sqlMap().getWhere()
				.and("name", QueryType.EQ, "abc", 1)
				.and((w) -> w
						.or("name", QueryType.EQ, "def", 2)
						.or("name", QueryType.EQ, "ghi", 3))
				.and((w) -> w
						.or((w2) -> w2
								.and("name", QueryType.EQ, "def", 4)
								.and("name", QueryType.EQ, "", 5)
								.and("name", QueryType.EQ_FORCE, "", 6))
						.or((w2) -> w2
								.and("name", QueryType.EQ, "def", 7)
								.and("name", QueryType.EQ, "ghi", 8)))
				.toSql();
		b = "a.`id` = #{sqlMap.where#id#EQ1} AND a.name = #{sqlMap.where.name#EQ1.val}" +
				" AND (a.name = #{sqlMap.where.n#[0].name#EQ2.val} OR a.name = #{sqlMap.where.n#[0].name#EQ3.val})" +
				" AND ((a.name = #{sqlMap.where.n#[1].n#[0].name#EQ4.val} AND a.name = #{sqlMap.where.n#[1].n#[0].name#EQ_FORCE6.val})" +
				" OR (a.name = #{sqlMap.where.n#[1].n#[1].name#EQ7.val} AND a.name = #{sqlMap.where.n#[1].n#[1].name#EQ8.val}))";
		System.out.println("a >> "+a);System.out.println("b >> "+b);Assert.assertEquals(a, b);

		System.exit(0);
	}
}
