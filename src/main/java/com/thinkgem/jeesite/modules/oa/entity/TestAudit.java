/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.oa.entity;

import com.thinkgem.jeesite.common.persistence.ActEntity;
import com.thinkgem.jeesite.modules.sys.entity.Office;
import com.thinkgem.jeesite.modules.sys.entity.User;

/**
 * 审批Entity
 * @author thinkgem
 * @version 2014-05-16
 */
public class TestAudit extends ActEntity<TestAudit> {
	
	private static final long serialVersionUID = 1L;
	private User 	user;	//	归属用户
	private Office 	office;	//	归属部门
	private String 	post;	//	岗位
	private String 	age;	//	性别
	private String 	edu;	//	学历
	private String 	content;	//	调整原因
	private String 	olda;	//	现行标准 薪酬档级
	private String 	oldb;	//	现行标准 月工资额
	private String 	oldc;	//	现行标准 年薪总额
	private String 	newa;	//	调整后标准 薪酬档级
	private String 	newb;	//	调整后标准 月工资额
	private String 	newc;	//	调整后标准 年薪总额
	private String 	addNum;	//	月增资
	private String 	exeDate;	//	执行时间
	private String 	hrText;		//	人力资源部门意见
	private String 	leadText;	//	分管领导意见
	private String 	mainLeadText;//	集团主要领导意见

	public TestAudit() {
		super();
	}

	public TestAudit(String id){
		super(id);
	}
	
	public String getPost() {
		return post;
	}

	public void setPost(String post) {
		this.post = post;
	}

	public String getAge() {
		return age;
	}

	public void setAge(String age) {
		this.age = age;
	}

	public String getEdu() {
		return edu;
	}

	public void setEdu(String edu) {
		this.edu = edu;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getOlda() {
		return olda;
	}

	public void setOlda(String olda) {
		this.olda = olda;
	}

	public String getOldb() {
		return oldb;
	}

	public void setOldb(String oldb) {
		this.oldb = oldb;
	}

	public String getOldc() {
		return oldc;
	}

	public void setOldc(String oldc) {
		this.oldc = oldc;
	}

	public String getNewa() {
		return newa;
	}

	public void setNewa(String newa) {
		this.newa = newa;
	}

	public String getNewb() {
		return newb;
	}

	public void setNewb(String newb) {
		this.newb = newb;
	}

	public String getNewc() {
		return newc;
	}

	public void setNewc(String newc) {
		this.newc = newc;
	}

	public String getExeDate() {
		return exeDate;
	}

	public void setExeDate(String exeDate) {
		this.exeDate = exeDate;
	}

	public String getHrText() {
		return hrText;
	}

	public void setHrText(String hrText) {
		this.hrText = hrText;
	}

	public String getLeadText() {
		return leadText;
	}

	public void setLeadText(String leadText) {
		this.leadText = leadText;
	}

	public String getMainLeadText() {
		return mainLeadText;
	}

	public void setMainLeadText(String mainLeadText) {
		this.mainLeadText = mainLeadText;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Office getOffice() {
		return office;
	}

	public void setOffice(Office office) {
		this.office = office;
	}

	public String getAddNum() {
		return addNum;
	}

	public void setAddNum(String addNum) {
		this.addNum = addNum;
	}
	
}


