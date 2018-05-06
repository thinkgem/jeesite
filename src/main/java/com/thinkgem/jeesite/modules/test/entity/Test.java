/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.test.entity;

import java.util.Date;

import org.hibernate.validator.constraints.Length;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.thinkgem.jeesite.common.persistence.DataEntity;
import com.thinkgem.jeesite.common.supcan.annotation.treelist.SupTreeList;
import com.thinkgem.jeesite.common.supcan.annotation.treelist.cols.SupCol;
import com.thinkgem.jeesite.common.supcan.annotation.treelist.cols.SupGroup;
import com.thinkgem.jeesite.modules.sys.entity.Office;

/**
 * 测试Entity
 * @author ThinkGem
 * @version 2013-10-17
 */
@SupTreeList(
	groups={
		@SupGroup(id="date", name="日期", sort=50),
		@SupGroup(id="date2", name="日期2", sort=60, parentId="date"),
		@SupGroup(id="date3", name="日期3", sort=70, parentId="date")
})
public class Test extends DataEntity<Test> {
	
	private static final long serialVersionUID = 1L;
	private Office office;	// 归属部门
	private String loginName;// 登录名
	private String name; 	// 名称

	public Test() {
		super();
	}

	public Test(String id){
		super(id);
	}

	@SupCol(text="归属公司", sort = 10, minWidth="125px")
	@JsonSerialize(using = ToStringSerializer.class)
	public Office getOffice() {
		return office;
	}

	public void setOffice(Office office) {
		this.office = office;
	}
	
	@SupCol(text="登录名", sort = 20, minWidth="125px")
	public String getLoginName() {
		return loginName;
	}

	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}

	@SupCol(text="姓名", sort = 30, minWidth="125px")
	@Length(min=1, max=200)
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@SupCol(text="创建时间", sort = 1, groupId="date", width="125px")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	public Date getCreateDate() {
		return createDate;
	}

	@SupCol(text="修改时间", sort = 2, groupId="date", width="125px")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	public Date getUpdateDate() {
		return updateDate;
	}
	
	@SupCol(text="创建时间2", sort = 2, groupId="date2", width="125px")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	public Date getCreateDate2() {
		return createDate;
	}

	@SupCol(text="修改时间2", sort = 1, groupId="date2", width="125px")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	public Date getUpdateDate2() {
		return updateDate;
	}
	
	@SupCol(text="创建时间3", sort = 200, groupId="date3", width="125px")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	public Date getCreateDate3() {
		return createDate;
	}

	@SupCol(text="修改时间3", sort = 1, groupId="date3", width="125px")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	public Date getUpdateDate3() {
		return updateDate;
	}
}


