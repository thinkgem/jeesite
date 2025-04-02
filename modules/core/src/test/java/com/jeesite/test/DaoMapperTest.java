/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 * No deletion without permission, or be held responsible to law.
 */
package com.jeesite.test;

import com.jeesite.common.collect.ListUtils;
import com.jeesite.common.collect.SetUtils;
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
@SpringBootTest(classes = ApplicationTest.class)
public class DaoMapperTest extends BaseSpringContextTests {

	@Autowired
	private PostDao postDao;
	@Autowired
	private UserDao userDao;
	@Autowired
	private AreaDao areaDao;
	@Autowired
	private CompanyDao companyDao;
	@Autowired
	private FileUploadDao fileUploadDao;
	@Autowired
	private EmpUserDao empUserDao;

	@Test
	public void testTableAnnotation() throws Exception{
		try{

			System.out.println("============ 插入测试 ============");
			Post post1 = new Post();
			post1.setId("1");
			post1.setPostName("test");
			post1.setPostType("1");
			post1.setPostSort(1);
			long postInsertNum = postDao.insert(post1);
			Assert.assertEquals("postDao.insert", 1, postInsertNum);
			Assert.assertEquals("postDao.insert result", "1", postDao.get(post1).getId());

			System.out.println("============ 批量插入测试 ============");
			Post post2 = (Post)post1.clone();
			post2.setId("2");
			post2.setPostName("test2");
			Post post3 = (Post)post1.clone();
			post3.setId("3");
			post3.setPostName("test3");
			long postInsertBatchNum = postDao.insertBatch(ListUtils.newArrayList(post2, post3));
			Assert.assertEquals("postDao.insertBatch", 2, postInsertBatchNum);
			Assert.assertEquals("postDao.insertBatch result", "2", postDao.get(post2).getId());
			Assert.assertEquals("postDao.insertBatch result", "3", postDao.get(post3).getId());

			System.out.println("============ 按主键更新测试 ============");
			Post post4 = new Post("2");
			post4.setPostName("test4");
			long postUpdateNum = postDao.update(post4);
			Assert.assertEquals("postDao.update", 1, postUpdateNum);
			Assert.assertEquals("postDao.update result", "test4", postDao.get(post2).getPostName());

			System.out.println("============ 按主键删除测试 ============");
			long postDeleteNum = postDao.phyDelete(post4);
			Assert.assertEquals("postDao.delete", 1, postDeleteNum);
			Assert.assertNull("postDao.delete result", postDao.get(post4));

			System.out.println("============ 按树表更新测试 ============");
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
			Area area4 = (Area) area.clone();
			area4.setAreaCode("4");
			Area area5 = (Area) area.clone();
			area5.setAreaCode("5");
			long areaInsertNum = areaDao.insertBatch(ListUtils.newArrayList(area, area2, area3, area4, area5));
			Assert.assertEquals("areaDao.insert", 5, areaInsertNum);
			Assert.assertEquals("areaDao.insertBatch result", "12345", areaDao
					.get(area).getAreaCode()+areaDao.get(area2).getAreaCode()+areaDao.get(area3).getAreaCode()
					+areaDao.get(area4).getAreaCode()+areaDao.get(area5).getAreaCode());
			area.setAreaName("你好2");
			long areaUpdateNum = areaDao.update(area);
			Assert.assertEquals("areaDao.update", 1, areaUpdateNum);
			Assert.assertEquals("areaDao.update result", "你好2", areaDao.get(area).getAreaName());

			System.out.println("============ 按树表批量更新测试 ============");
			long areaUpdateBatchNum = areaDao.updateBatch(ListUtils.newArrayList(area2, area3));
			Assert.assertEquals("areaDao.updateBatch", 2, areaUpdateBatchNum);
			Assert.assertEquals("areaDao.updateBatch result", "23",
					areaDao.get(area2).getAreaCode()+areaDao.get(area3).getAreaCode());

			System.out.println("============ 自定义更新条件测试 ============");
			Area where = new Area();
			where.setId_in(new String[]{area.getId(), area2.getId()});
			where.setAreaName(area.getAreaName());
			where.setStatus(area.getStatus());
			area.setAreaName("你好3");
			long areaUpdateByEntityNum = areaDao.updateByEntity(area, where);
			Assert.assertEquals("areaDao.updateByEntity", 1, areaUpdateByEntityNum);
			Assert.assertEquals("areaDao.updateByEntity result", "你好3", areaDao.get(area).getAreaName());

			System.out.println("============ 更新数据状态测试 ============");
			long areaStatusNum = areaDao.updateStatus(area);
			Assert.assertEquals("areaDao.updateStatus", 1, areaStatusNum);
			where.setAreaName(area.getAreaName());
			long areaStatusByEntityNum = areaDao.updateStatusByEntity(area, where);
			Assert.assertEquals("areaDao.updateStatusByEntity", 1, areaStatusByEntityNum);

			System.out.println("============ 逻辑删除测试 ============");
			long areaDeleteNum = areaDao.delete(area);
			Assert.assertEquals("areaDao.delete", 1, areaDeleteNum);
			where.setId(area2.getId());
			where.setId_in(new String[]{area2.getId()});
			where.setAreaName(area2.getAreaName());
			where.setStatus(area2.getStatus());
			long areaDeleteByEntityNum = areaDao.deleteByEntity(where);
			Assert.assertEquals("areaDao.deleteByEntity", 1, areaDeleteByEntityNum);

			System.out.println("============ 物理删除测试 ============");
			long areaPhyDeleteNum = areaDao.phyDelete(area3);
			Assert.assertEquals("areaDao.phyDelete", 1, areaPhyDeleteNum);
			long areaPhyDeleteByEntityNum = areaDao.phyDeleteByEntity(area4);
			Assert.assertEquals("areaDao.phyDeleteByEntity", 1, areaPhyDeleteByEntityNum);

			System.out.println("============ 基本查询测试 ============");
			List<Area> areaList = areaDao.findList(area5);
			Assert.assertEquals("areaDao.findList", 1, areaList.size());
			User user = new User();
			user.setUserType(User.USER_TYPE_EMPLOYEE);
			List<User> userList = userDao.findList(user);
			Assert.assertFalse("userDao.findList", userList.isEmpty());

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
			Assert.assertEquals("advanced query init", 3, companyInsertNum);
			Company companyWhere = (Company) company.clone();
			companyWhere.setCompanyCode(null);
			companyWhere.setParentCode(null);
			long companyFindCount = companyDao.findCount(companyWhere);
			Assert.assertEquals("advanced query list", 2, companyFindCount);

			System.out.println("============ 分页测试，查询子节点 ============");
			Company company4 = new Company("1");
			company4.setFullName(company.getFullName());
			company4.setPage(new Page<>(1, 2));
			company4.setIsQueryChildren(true);
			List<Company> companyListPage = companyDao.findList(company4);
			Assert.assertEquals("find page list size", 2, companyListPage.size());
			Assert.assertEquals("find page list get(1)", companyListPage.get(1).getCompanyCode(), company2.getCompanyCode());
			Assert.assertEquals("find page count", 3, company4.getPage().getCount());

			System.out.println("============ 扩展条件语句前带AND容错测试 ============");
			Company company5 = new Company();
			company5.sqlMap().getWhere().disableAutoAddStatusWhere();
			company5.sqlMap().getDataScope().addFilter("dsf",
					"Company", "a.`company_code`", DataScope.CTRL_PERMI_HAVE);
			// 随意给权限过滤 sqlWhere 增加 and or 的容错（结果：相同key进行addFilter使用or，不同使用and）
			company5.sqlMap().getDataScope().addFilter("dsf", "a.area_code = '123'");
			company5.sqlMap().getDataScope().addFilter("dsf2", "and a.area_code = '456'");
			company5.sqlMap().getDataScope().addFilter("dsf3", "or a.area_code = '789'");
			List<Company> companyList = companyDao.findList(company5);
			Assert.assertEquals("companyDao.findList extWhere", 0, companyList.size());

			System.out.println("============ 联合查询未设定columns和attrName为this时测试 ============");
			FileUpload fileUpload = new FileUpload();
			fileUpload.sqlMap().getWhere().and("u.`user_code`", QueryType.EQ, "system123456");
			List<FileUpload> fileUploadList = fileUploadDao.findList(fileUpload);
			System.out.println(fileUploadList);
			Assert.assertEquals("fileUploadDao.findList attrName this", 0, fileUploadList.size());

			System.out.println("============ 树结构基本查询测试 ============");
			Area area6 = new Area();
			area6.setParentCodes("0,370000,%");
			List<Area> area6List = areaDao.findByParentCodesLike(area6);
			System.out.println(area6List);
			Assert.assertFalse("areaDao.findByParentCodesLike", area6List.isEmpty());
			Area area7 = new Area();
			area7.setParentCodes_rightLike("0,370000,");
			List<Area> area7List2 = areaDao.findList(area7);
			System.out.println(area7List2);
			Assert.assertEquals("areaDao.findByParentCodesRightLike", area6List.size(), area7List2.size());

			System.out.println("============ 分页情况下foreach测试 ============");
			EmpUser empUser = new EmpUser();
			empUser.setCodes(new String[]{"SDJN01","SDJN02"});
			empUser.setPage(new Page<>(1, 3));
			List<EmpUser> empUserList = empUserDao.findUserListByOfficeCodes(empUser);
			System.out.println(empUserList);
			Assert.assertFalse("empUserDao.findUserListByOfficeCodes", empUserList.isEmpty());

			System.out.println("\n===========================================\n");

			main(null);

		}catch(Exception e){
			e.printStackTrace();
			throw new Exception(e);
		}
	}

	public static void main(String[] args) {
		String a, b;

		System.out.println("============ 查询测试 ============");
		a = new Post("1").sqlMap()
				.getWhere().and("name", QueryType.EQ, "abc").toSql();
		b = "a.`status` != #{STATUS_DELETE} AND a.`post_code` = #{sqlMap.where#post_code#EQ1}" +
				" AND a.name = #{sqlMap.where.name#EQ1.val}";
		System.out.println("a >> "+a);System.out.println("b >> "+b);Assert.assertEquals(b, a);

		a = new Post("1").sqlMap().getWhere()
				.and("name", QueryType.IN, new String[]{"1", "2", "3"}).toSql();
		b = "a.`status` != #{STATUS_DELETE} AND a.`post_code` = #{sqlMap.where#post_code#EQ1}" +
				" AND a.name IN ( #{sqlMap.where.name#IN1.val[0]}, #{sqlMap.where.name#IN1.val[1]}," +
				" #{sqlMap.where.name#IN1.val[2]} )";
		System.out.println("a >> "+a);System.out.println("b >> "+b);Assert.assertEquals(b, a);

		a = new Post("1").sqlMap().getWhere()
				.or("name", QueryType.IN, new String[]{"1", "2", "3"}).toSql();
		b = "a.`status` != #{STATUS_DELETE} AND a.`post_code` = #{sqlMap.where#post_code#EQ1}" +
				" OR a.name IN ( #{sqlMap.where.name#IN1.val[0]}, #{sqlMap.where.name#IN1.val[1]}," +
				" #{sqlMap.where.name#IN1.val[2]} )";
		System.out.println("a >> "+a);System.out.println("b >> "+b);Assert.assertEquals(b, a);

		System.out.println("============ 重复赋值测试 ============");
		a = new Post("1").sqlMap().getWhere()
				.and("name", QueryType.LIKE, "abc")
				.and("name", QueryType.LIKE, "def").toSql();
		b = "a.`status` != #{STATUS_DELETE} AND a.`post_code` = #{sqlMap.where#post_code#EQ1}" +
				" AND a.name LIKE #{sqlMap.where.name#LIKE1.val}";
		System.out.println("a >> "+a);System.out.println("b >> "+b);Assert.assertEquals(b, a);

		System.out.println("============ IN、NOT IN 测试 ============");
		a = new Post("1").sqlMap().getWhere()
				.and("name", QueryType.IN, new String[]{"abc","def"})
				.and("name2", QueryType.NOT_IN, ListUtils.newArrayList("abc","def")).toSql();
		b = "a.`status` != #{STATUS_DELETE} AND a.`post_code` = #{sqlMap.where#post_code#EQ1}" +
				" AND a.name IN ( #{sqlMap.where.name#IN1.val[0]}, #{sqlMap.where.name#IN1.val[1]} )" +
				" AND a.name2 NOT IN ( #{sqlMap.where.name2#NOT_IN1.val[0]}, #{sqlMap.where.name2#NOT_IN1.val[1]} )";
		System.out.println("a >> "+a);System.out.println("b >> "+b);Assert.assertEquals(b, a);

		a = new Post("1").sqlMap().getWhere()
				.and("name", QueryType.IN, null)
				.and("name2", QueryType.IN, new String[]{})
				.and("name3", QueryType.NOT_IN, ListUtils.newArrayList()).toSql();
		b = "a.`status` != #{STATUS_DELETE} AND a.`post_code` = #{sqlMap.where#post_code#EQ1}";
		System.out.println("a >> "+a);System.out.println("b >> "+b);Assert.assertEquals(b, a);

		System.out.println("============ 带括号测试 ============");
		a = new Post("1").sqlMap().getWhere()
				.andBracket("name", QueryType.EQ, "abc", 1)
				.or("name", QueryType.EQ, "def", 2)
				.or("name", QueryType.EQ, "ghi", 3).endBracket().toSql();
		b = "a.`status` != #{STATUS_DELETE} AND a.`post_code` = #{sqlMap.where#post_code#EQ1}" +
				" AND ( a.name = #{sqlMap.where.name#EQ1.val} OR a.name = #{sqlMap.where.name#EQ2.val}" +
				" OR a.name = #{sqlMap.where.name#EQ3.val} )";
		System.out.println("a >> "+a);System.out.println("b >> "+b);Assert.assertEquals(b, a);

		a = new Post().sqlMap().getWhere()
				.andBracket("name", QueryType.EQ, "val1", 1)
					.and("name", QueryType.NE, "val2", 11).endBracket(1)
				.orBracket("name", QueryType.NE, "val3", 2)
					.and("name", QueryType.NE, "val4", 22).endBracket(2)
				.orBracket("name", QueryType.NE, "val5", 3)
					.and("name", QueryType.EQ, "val6", 33).endBracket(3)
				.toSql();
		b = "a.`status` != #{STATUS_DELETE}" +
				" AND ( a.name = #{sqlMap.where.name#EQ1.val} AND a.name != #{sqlMap.where.name#NE11.val} )" +
				" OR ( a.name != #{sqlMap.where.name#NE2.val} AND a.name != #{sqlMap.where.name#NE22.val} )" +
				" OR ( a.name != #{sqlMap.where.name#NE3.val} AND a.name = #{sqlMap.where.name#EQ33.val} )";
		System.out.println("a >> "+a);System.out.println("b >> "+b);Assert.assertEquals(b, a);

		System.out.println("============ 带括号部分空值测试 ============");
		a = new Post("1").sqlMap().getWhere()
				.andBracket("name", QueryType.EQ, "", 1)
					.or("name", QueryType.EQ, "def", 2)
					.or("name", QueryType.EQ, "", 3).endBracket().toSql();
		b = "a.`status` != #{STATUS_DELETE} AND a.`post_code` = #{sqlMap.where#post_code#EQ1}" +
				" AND ( a.name = #{sqlMap.where.name#EQ2.val} )";
		System.out.println("a >> "+a);System.out.println("b >> "+b);Assert.assertEquals(b, a);

		a = new Post("1").sqlMap().getWhere()
				.andBracket("name", QueryType.EQ, "abc", 1)
					.or("name", QueryType.EQ, "def", 2)
					.or("name", QueryType.EQ, "", 3).endBracket().toSql();
		b = "a.`status` != #{STATUS_DELETE} AND a.`post_code` = #{sqlMap.where#post_code#EQ1}" +
				" AND ( a.name = #{sqlMap.where.name#EQ1.val} OR a.name = #{sqlMap.where.name#EQ2.val} )";
		System.out.println("a >> "+a);System.out.println("b >> "+b);Assert.assertEquals(b, a);

		a = new Post("1").sqlMap().getWhere()
				.andBracket("name", QueryType.EQ, "", 1)
					.or("name", QueryType.EQ, "def", 2)
					.or("name", QueryType.EQ, "ghi", 3).endBracket().toSql();
		b = "a.`status` != #{STATUS_DELETE} AND a.`post_code` = #{sqlMap.where#post_code#EQ1}" +
				" AND ( a.name = #{sqlMap.where.name#EQ2.val} OR a.name = #{sqlMap.where.name#EQ3.val} )";
		System.out.println("a >> "+a);System.out.println("b >> "+b);Assert.assertEquals(b, a);

		System.out.println("============ 带括号全部空值测试 ============");
		a = new Post("1").sqlMap().getWhere()
				.andBracket("name", QueryType.EQ, "", 1)
					.or("name", QueryType.EQ, "", 2)
					.or("name", QueryType.EQ, "", 3).endBracket().toSql();
		b = "a.`status` != #{STATUS_DELETE} AND a.`post_code` = #{sqlMap.where#post_code#EQ1} ";
		System.out.println("a >> "+a);System.out.println("b >> "+b);Assert.assertEquals(b, a);

		a = new Post().sqlMap().getWhere()
				.andBracket("name", QueryType.EQ, "", 1)
					.or("name", QueryType.EQ, "", 2)
					.or("name", QueryType.EQ, "", 3).endBracket().toSql();
		b = "a.`status` != #{STATUS_DELETE} ";
		System.out.println("a >> "+a);System.out.println("b >> "+b);Assert.assertEquals(b, a);

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
		System.out.println("a >> "+a);System.out.println("b >> "+b);Assert.assertEquals(b, a);

		System.out.println("============ 联合查询，属性名支持指定别名 ============");
		a = new FileUpload().sqlMap().getWhere().and("u.`user_name`", QueryType.EQ, "user1").toSql();
		b = "a.`status` != #{STATUS_DELETE} AND u.`user_name` = #{sqlMap.where.u#_user_name_#EQ1.val}";
		System.out.println("a >> "+a);System.out.println("b >> "+b);Assert.assertEquals(b, a);

		System.out.println("============ 联合查询，返回到当前实体测试 ============");
		FileUpload fileUpload = new FileUpload();
		fileUpload.sqlMap().getWhere().and("create_by", QueryType.IN, new String[]{"user1","user2"});
		a = fileUpload.sqlMap().getWhere().toSql();
		b = "a.`status` != #{STATUS_DELETE} AND a.create_by IN ( #{sqlMap.where.create_by#IN1.val[0]},"
				+ " #{sqlMap.where.create_by#IN1.val[1]} )";
		System.out.println("a >> "+a);System.out.println("b >> "+b);Assert.assertEquals(b, a);

		System.out.println("============ 联合查询，属性为this时也可作为查询条件 ============");
		FileUpload fileUpload2 = new FileUpload();
		fileUpload2.setCreateByName("ThinkGem/JeeSite");
		fileUpload2.sqlMap().getWhere().and("create_by", QueryType.IN, new String[]{"user1","user2"});
		a = fileUpload2.sqlMap().getWhere().toSql();
		b = "a.`status` != #{STATUS_DELETE} AND a.create_by IN ( #{sqlMap.where.create_by#IN1.val[0]},"
				+ " #{sqlMap.where.create_by#IN1.val[1]} ) AND u.`user_name` LIKE #{sqlMap.where#user_name#LIKE1}";
		System.out.println("a >> "+a);System.out.println("b >> "+b);Assert.assertEquals(b, a);

		System.out.println("============ 条件嵌套查询，可替代 andBracket、orBracket、endBracket v5.2.1+ ============");
		a = new Post("1").sqlMap().getWhere()
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
		b = "a.`status` != #{STATUS_DELETE} AND a.`post_code` = #{sqlMap.where#post_code#EQ1} AND a.name = #{sqlMap.where.name#EQ1.val}" +
				" AND (a.name = #{sqlMap.where.n#[0].name#EQ2.val} OR a.name = #{sqlMap.where.n#[0].name#EQ3.val})" +
				" AND ((a.name = #{sqlMap.where.n#[1].n#[0].name#EQ4.val} AND a.name = #{sqlMap.where.n#[1].n#[0].name#EQ_FORCE6.val})" +
				" OR (a.name = #{sqlMap.where.n#[1].n#[1].name#EQ7.val} AND a.name = #{sqlMap.where.n#[1].n#[1].name#EQ8.val}))";
		System.out.println("a >> "+a);System.out.println("b >> "+b);Assert.assertEquals(b, a);

		System.out.println("============ 插入测试，字段权限过滤 v5.11.1 ============");
		Post insertPost = new Post("1");
		insertPost.setPostName("这是岗位名称");
		insertPost.currentUser(new User("system"));
		insertPost.sqlMap().getInsert().setExcludeAttrNames(SetUtils.newHashSet("postName"));
		insertPost.sqlMap().getInsert().setIncludeAttrNames(SetUtils.newHashSet("postCode"));
		a = "INSERT INTO " + insertPost.sqlMap().getInsert().toTableSql();
		a += " (" + insertPost.sqlMap().getInsert().toColumnSql() + ")";
		a += " VALUES (" + insertPost.sqlMap().getInsert().toValuesSql() + ")";
		b = "INSERT INTO `js_sys_post`" +
				" (`status`, `create_by`, `create_date`, `update_by`, `update_date`, `post_code`)" +
				" VALUES (#{status}, #{createBy}, #{createDate}, #{updateBy}, #{updateDate}, #{postCode})";
		System.out.println("a >> "+a);System.out.println("b >> "+b);Assert.assertEquals(b, a);

		System.out.println("============ 更新测试，字段权限过滤，扩展列，扩展条件 v5.11.1 ============");
		Post updatePost = new Post("1");
		updatePost.setPostName("这是岗位名称");
		updatePost.setPostType("ceo");
		updatePost.setPostSort(1);
		updatePost.currentUser(new User("system"));
		updatePost.sqlMap().getUpdate().setExcludeAttrNames(SetUtils.newHashSet("updateBy", "updateDate"));
		updatePost.sqlMap().getUpdate().setIncludeAttrNames(SetUtils.newHashSet("postName", "postType"));
		updatePost.sqlMap().getUpdate().addExtColumnSql("s1", "hot1 = hot1 + 1");
		updatePost.sqlMap().getUpdate().addExtWhereSql("w1", "name1 = '123'");
		a = "UPDATE " + updatePost.sqlMap().getUpdate().toTableSql();
		a += " SET " + updatePost.sqlMap().getUpdate().toColumnSql();
		a += " WHERE " + updatePost.sqlMap().getUpdate().toWhereSql();
		b = "UPDATE `js_sys_post`" +
				" SET `post_name` = #{postName}, `post_type` = #{postType}, hot1 = hot1 + 1" +
				" WHERE `post_code` = #{postCode} AND name1 = '123'";
		System.out.println("a >> "+a);System.out.println("b >> "+b);Assert.assertEquals(b, a);

		System.out.println("============ 查询操作，字段权限过滤，扩展列，扩展表，扩展条件 v5.11.1 ============");
		Post queryPost = new Post("1");
		queryPost.currentUser(new User("system"));
		queryPost.setPostType("ceo");
		queryPost.sqlMap().getColumn().setExcludeAttrNames(SetUtils.newHashSet("postType"));
		queryPost.sqlMap().getColumn().setIncludeAttrNames(SetUtils.newHashSet("postName"));
		queryPost.sqlMap().getColumn().addExtSql("c1", "column1 AS \"column1\"");
		queryPost.sqlMap().getTable().addExtSql("t1", "JOIN test1 b.post_code = a.post_code");
		queryPost.sqlMap().getWhere().addExtSql("w1", "AND a.name1 = '123'");
		a = "SELECT " + queryPost.sqlMap().getColumn().toSql();
		a += " FROM " + queryPost.sqlMap().getTable().toSql();
		a += " WHERE " + queryPost.sqlMap().getWhere().toSql();
		b = "SELECT a.`corp_code` AS \"corpCode\", a.`corp_name` AS \"corpName\", a.`status` AS \"status\", " +
				"a.`create_by` AS \"createBy\", a.`create_date` AS \"createDate\", a.`update_by` AS \"updateBy\", " +
				"a.`update_date` AS \"updateDate\", a.`post_name` AS \"postName\", column1 AS \"column1\" " +
			"FROM `js_sys_post` aJOIN test1 b.post_code = a.post_code " +
			"WHERE a.`status` != #{STATUS_DELETE} AND a.`post_code` = #{sqlMap.where#post_code#EQ1} " +
			"AND a.`post_type` = #{sqlMap.where#post_type#EQ1} AND a.name1 = '123'";
		System.out.println("a >> "+a);System.out.println("b >> "+b);Assert.assertEquals(b, a);

		System.exit(0);
	}
}
