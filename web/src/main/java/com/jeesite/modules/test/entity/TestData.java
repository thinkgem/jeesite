/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.modules.test.entity;

import org.hibernate.validator.constraints.Length;
import java.util.Date;
import com.jeesite.common.mybatis.annotation.JoinTable;
import com.jeesite.common.mybatis.annotation.JoinTable.Type;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.jeesite.modules.sys.entity.User;
import com.jeesite.modules.sys.entity.Office;
import java.util.List;
import com.jeesite.common.collect.ListUtils;

import com.jeesite.common.entity.DataEntity;
import com.jeesite.common.mybatis.annotation.Column;
import com.jeesite.common.mybatis.annotation.Table;
import com.jeesite.common.mybatis.mapper.query.QueryType;

/**
 * 测试数据Entity
 * @author ThinkGem
 * @version 2018-04-22
 */
@Table(name="test_data", alias="a", columns={
		@Column(name="id", attrName="id", label="编号", isPK=true),
		@Column(name="test_input", attrName="testInput", label="单行文本", queryType=QueryType.LIKE),
		@Column(name="test_textarea", attrName="testTextarea", label="多行文本", queryType=QueryType.LIKE),
		@Column(name="test_select", attrName="testSelect", label="下拉框"),
		@Column(name="test_select_multiple", attrName="testSelectMultiple", label="下拉多选"),
		@Column(name="test_radio", attrName="testRadio", label="单选框"),
		@Column(name="test_checkbox", attrName="testCheckbox", label="复选框"),
		@Column(name="test_date", attrName="testDate", label="日期选择", isUpdateForce=true/*是否强制更新（当调用update并且该属性为空的时候，允许更新为空）*/),
		@Column(name="test_datetime", attrName="testDatetime", label="日期时间"),
		@Column(name="test_user_code", attrName="testUser.userCode", label="用户选择"),
		@Column(name="test_office_code", attrName="testOffice.officeCode", label="机构选择"),
		@Column(name="test_area_code", attrName="testAreaCode", label="区域选择"),
		@Column(name="test_area_name", attrName="testAreaName", label="区域名称"),
		@Column(includeEntity=DataEntity.class),
	}, joinTable={
		@JoinTable(type=Type.LEFT_JOIN, entity=User.class, attrName="testUser", alias="u10",
			on="u10.user_code = a.test_user_code", columns={
				@Column(name="user_code", label="用户编码", isPK=true),
				@Column(name="user_name", label="用户名称", isQuery=false),
		}),
		@JoinTable(type=Type.LEFT_JOIN, entity=Office.class, attrName="testOffice", alias="u11",
			on="u11.office_code = a.test_office_code", columns={
				@Column(name="office_code", label="机构编码", isPK=true),
				@Column(name="office_name", label="机构名称", isQuery=false),
		}),
	}, orderBy="a.update_date DESC"
)
public class TestData extends DataEntity<TestData> {
	
	private static final long serialVersionUID = 1L;
	private String testInput;		// 单行文本
	private String testTextarea;		// 多行文本
	private String testSelect;		// 下拉框
	private String testSelectMultiple;		// 下拉多选
	private String testRadio;		// 单选框
	private String testCheckbox;		// 复选框
	private Date testDate;		// 日期选择
	private Date testDatetime;		// 日期时间
	private User testUser;		// 用户选择
	private Office testOffice;		// 机构选择
	private String testAreaCode;		// 区域选择
	private String testAreaName;		// 区域名称
	private List<TestDataChild> testDataChildList = ListUtils.newArrayList();		// 子表列表
	
	public TestData() {
		this(null);
	}

	public TestData(String id){
		super(id);
	}
	
	@Length(min=0, max=200, message="单行文本长度不能超过 200 个字符")
	public String getTestInput() {
		return testInput;
	}

	public void setTestInput(String testInput) {
		this.testInput = testInput;
	}
	
	@Length(min=0, max=200, message="多行文本长度不能超过 200 个字符")
	public String getTestTextarea() {
		return testTextarea;
	}

	public void setTestTextarea(String testTextarea) {
		this.testTextarea = testTextarea;
	}
	
	@Length(min=0, max=10, message="下拉框长度不能超过 10 个字符")
	public String getTestSelect() {
		return testSelect;
	}

	public void setTestSelect(String testSelect) {
		this.testSelect = testSelect;
	}
	
	@Length(min=0, max=200, message="下拉多选长度不能超过 200 个字符")
	public String getTestSelectMultiple() {
		return testSelectMultiple;
	}

	public void setTestSelectMultiple(String testSelectMultiple) {
		this.testSelectMultiple = testSelectMultiple;
	}
	
	@Length(min=0, max=10, message="单选框长度不能超过 10 个字符")
	public String getTestRadio() {
		return testRadio;
	}

	public void setTestRadio(String testRadio) {
		this.testRadio = testRadio;
	}
	
	@Length(min=0, max=200, message="复选框长度不能超过 200 个字符")
	public String getTestCheckbox() {
		return testCheckbox;
	}

	public void setTestCheckbox(String testCheckbox) {
		this.testCheckbox = testCheckbox;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	public Date getTestDate() {
		return testDate;
	}

	public void setTestDate(Date testDate) {
		this.testDate = testDate;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	public Date getTestDatetime() {
		return testDatetime;
	}

	public void setTestDatetime(Date testDatetime) {
		this.testDatetime = testDatetime;
	}
	
	public User getTestUser() {
		return testUser;
	}

	public void setTestUser(User testUser) {
		this.testUser = testUser;
	}
	
	public Office getTestOffice() {
		return testOffice;
	}

	public void setTestOffice(Office testOffice) {
		this.testOffice = testOffice;
	}
	
	@Length(min=0, max=64, message="区域选择长度不能超过 64 个字符")
	public String getTestAreaCode() {
		return testAreaCode;
	}

	public void setTestAreaCode(String testAreaCode) {
		this.testAreaCode = testAreaCode;
	}
	
	@Length(min=0, max=100, message="区域名称长度不能超过 100 个字符")
	public String getTestAreaName() {
		return testAreaName;
	}

	public void setTestAreaName(String testAreaName) {
		this.testAreaName = testAreaName;
	}
	
	public Date getTestDate_gte() {
		return sqlMap.getWhere().getValue("test_date", QueryType.GTE);
	}

	public void setTestDate_gte(Date testDate) {
		sqlMap.getWhere().and("test_date", QueryType.GTE, testDate);
	}
	
	public Date getTestDate_lte() {
		return sqlMap.getWhere().getValue("test_date", QueryType.LTE);
	}

	public void setTestDate_lte(Date testDate) {
		sqlMap.getWhere().and("test_date", QueryType.LTE, testDate);
	}
	
	public Date getTestDatetime_gte() {
		return sqlMap.getWhere().getValue("test_datetime", QueryType.GTE);
	}

	public void setTestDatetime_gte(Date testDatetime) {
		sqlMap.getWhere().and("test_datetime", QueryType.GTE, testDatetime);
	}
	
	public Date getTestDatetime_lte() {
		return sqlMap.getWhere().getValue("test_datetime", QueryType.LTE);
	}

	public void setTestDatetime_lte(Date testDatetime) {
		sqlMap.getWhere().and("test_datetime", QueryType.LTE, testDatetime);
	}
	
	public List<TestDataChild> getTestDataChildList() {
		return testDataChildList;
	}

	public void setTestDataChildList(List<TestDataChild> testDataChildList) {
		this.testDataChildList = testDataChildList;
	}
	
}