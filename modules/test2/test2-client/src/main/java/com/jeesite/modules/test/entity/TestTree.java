/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.modules.test.entity;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import com.jeesite.common.entity.DataEntity;
import com.jeesite.common.entity.TreeEntity;
import com.jeesite.common.mybatis.annotation.Column;
import com.jeesite.common.mybatis.annotation.Table;
import com.jeesite.common.mybatis.mapper.query.QueryType;

/**
 * 测试树表Entity
 * @author ThinkGem
 * @version 2018-04-22
 */
@Table(name="test_tree", alias="a", columns={
		@Column(name="tree_code", attrName="treeCode", label="节点编码", isPK=true),
		@Column(includeEntity=TreeEntity.class),
		@Column(name="tree_name", attrName="treeName", label="节点名称", queryType=QueryType.LIKE, isTreeName=true),
		@Column(includeEntity=DataEntity.class),
	}, orderBy="a.tree_sorts, a.tree_code"
)
public class TestTree extends TreeEntity<TestTree> {
	
	private static final long serialVersionUID = 1L;
	private String treeCode;		// 节点编码
	private String treeName;		// 节点名称

	// 表单图片上传和附件上传的接受参数
	private String testTree_image;
	private String testTree_image__del;
	private String testTree_file;
	private String testTree_file__del;
	
	public TestTree() {
		this(null);
	}

	public TestTree(String id){
		super(id);
	}
	
	@Override
	public TestTree getParent() {
		return parent;
	}

	@Override
	public void setParent(TestTree parent) {
		this.parent = parent;
	}
	
	public String getTreeCode() {
		return treeCode;
	}

	public void setTreeCode(String treeCode) {
		this.treeCode = treeCode;
	}
	
	@NotBlank(message="节点名称不能为空")
	@Size(min=0, max=200, message="节点名称长度不能超过 200 个字符")
	public String getTreeName() {
		return treeName;
	}

	public void setTreeName(String treeName) {
		this.treeName = treeName;
	}

	public String getTestTree_image() {
		return testTree_image;
	}

	public void setTestTree_image(String testTree_image) {
		this.testTree_image = testTree_image;
	}

	public String getTestTree_image__del() {
		return testTree_image__del;
	}

	public void setTestTree_image__del(String testTree_image__del) {
		this.testTree_image__del = testTree_image__del;
	}

	public String getTestTree_file() {
		return testTree_file;
	}

	public void setTestTree_file(String testTree_file) {
		this.testTree_file = testTree_file;
	}

	public String getTestTree_file__del() {
		return testTree_file__del;
	}

	public void setTestTree_file__del(String testTree_file__del) {
		this.testTree_file__del = testTree_file__del;
	}
	
}