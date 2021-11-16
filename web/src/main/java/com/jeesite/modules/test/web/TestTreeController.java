/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 * No deletion without permission, or be held responsible to law.
 */
package com.jeesite.modules.test.web;

import java.util.List;
import java.util.Map;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.jeesite.common.config.Global;
import com.jeesite.common.collect.ListUtils;
import com.jeesite.common.collect.MapUtils;
import com.jeesite.common.lang.StringUtils;
import com.jeesite.common.idgen.IdGen;
import com.jeesite.modules.sys.utils.UserUtils;
import com.jeesite.common.web.BaseController;
import com.jeesite.modules.test.entity.TestTree;
import com.jeesite.modules.test.service.TestTreeService;

/**
 * 测试树表Controller
 * @author ThinkGem
 * @version 2018-04-22
 */
@Controller
@RequestMapping(value = "${adminPath}/test/testTree")
public class TestTreeController extends BaseController {

	@Autowired
	private TestTreeService testTreeService;
	
	/**
	 * 获取数据
	 */
	@ModelAttribute
	public TestTree get(String treeCode, boolean isNewRecord) {
		return testTreeService.get(treeCode, isNewRecord);
	}
	
	/**
	 * 查询列表
	 */
	@RequiresPermissions("test:testTree:view")
	@RequestMapping(value = {"list", ""})
	public String list(TestTree testTree, Model model) {
		model.addAttribute("testTree", testTree);
		return "modules/test/testTreeList";
	}
	
	/**
	 * 查询列表数据
	 */
	@RequiresPermissions("test:testTree:view")
	@RequestMapping(value = "listData")
	@ResponseBody
	public List<TestTree> listData(TestTree testTree) {
		if (StringUtils.isBlank(testTree.getParentCode())) {
			testTree.setParentCode(TestTree.ROOT_CODE);
		}
		if (StringUtils.isNotBlank(testTree.getTreeName())){
			testTree.setParentCode(null);
		}
		if (StringUtils.isNotBlank(testTree.getRemarks())){
			testTree.setParentCode(null);
		}
		List<TestTree> list = testTreeService.findList(testTree);
		return list;
	}

	/**
	 * 查看编辑表单
	 */
	@RequiresPermissions("test:testTree:view")
	@RequestMapping(value = "form")
	public String form(TestTree testTree, Model model) {
		// 创建并初始化下一个节点信息
		testTree = createNextNode(testTree);
		model.addAttribute("testTree", testTree);
		return "modules/test/testTreeForm";
	}
	
	/**
	 * 创建并初始化下一个节点信息，如：排序号、默认值
	 */
	@RequiresPermissions("test:testTree:edit")
	@RequestMapping(value = "createNextNode")
	@ResponseBody
	public TestTree createNextNode(TestTree testTree) {
		if (StringUtils.isNotBlank(testTree.getParentCode())){
			testTree.setParent(testTreeService.get(testTree.getParentCode()));
		}
		if (testTree.getIsNewRecord()) {
			TestTree where = new TestTree();
			where.setParentCode(testTree.getParentCode());
			TestTree last = testTreeService.getLastByParentCode(where);
			// 获取到下级最后一个节点
			if (last != null){
				testTree.setTreeSort(last.getTreeSort() + 30);
				testTree.setTreeCode(IdGen.nextCode(last.getTreeCode()));
			}else if (testTree.getParent() != null){
				testTree.setTreeCode(testTree.getParent().getTreeCode() + "001");
			}
		}
		// 以下设置表单默认数据
		if (testTree.getTreeSort() == null){
			testTree.setTreeSort(TestTree.DEFAULT_TREE_SORT);
		}
		return testTree;
	}

	/**
	 * 保存数据
	 */
	@RequiresPermissions("test:testTree:edit")
	@PostMapping(value = "save")
	@ResponseBody
	public String save(@Validated TestTree testTree) {
		testTreeService.save(testTree);
		return renderResult(Global.TRUE, text("保存数据成功！"));
	}
	
	/**
	 * 停用数据
	 */
	@RequiresPermissions("test:testTree:edit")
	@RequestMapping(value = "disable")
	@ResponseBody
	public String disable(TestTree testTree) {
		TestTree where = new TestTree();
		where.setStatus(TestTree.STATUS_NORMAL);
		where.setParentCodes("," + testTree.getId() + ",");
		long count = testTreeService.findCount(where);
		if (count > 0) {
			return renderResult(Global.FALSE, text("该数据包含未停用的子数据！"));
		}
		testTree.setStatus(TestTree.STATUS_DISABLE);
		testTreeService.updateStatus(testTree);
		return renderResult(Global.TRUE, text("停用数据成功"));
	}
	
	/**
	 * 启用数据
	 */
	@RequiresPermissions("test:testTree:edit")
	@RequestMapping(value = "enable")
	@ResponseBody
	public String enable(TestTree testTree) {
		testTree.setStatus(TestTree.STATUS_NORMAL);
		testTreeService.updateStatus(testTree);
		return renderResult(Global.TRUE, text("启用数据成功"));
	}
	
	/**
	 * 删除数据
	 */
	@RequiresPermissions("test:testTree:edit")
	@RequestMapping(value = "delete")
	@ResponseBody
	public String delete(TestTree testTree) {
		testTreeService.delete(testTree);
		return renderResult(Global.TRUE, text("删除数据成功！"));
	}
	
	/**
	 * 获取树结构数据
	 * @param excludeCode 排除的Code
	 * @param isShowCode 是否显示编码（true or 1：显示在左侧；2：显示在右侧；false or null：不显示）
	 * @return
	 */
	@RequiresPermissions("test:testTree:view")
	@RequestMapping(value = "treeData")
	@ResponseBody
	public List<Map<String, Object>> treeData(String excludeCode, String isShowCode) {
		List<Map<String, Object>> mapList = ListUtils.newArrayList();
		List<TestTree> list = testTreeService.findList(new TestTree());
		for (int i=0; i<list.size(); i++){
			TestTree e = list.get(i);
			// 过滤非正常的数据
			if (!TestTree.STATUS_NORMAL.equals(e.getStatus())){
				continue;
			}
			// 过滤被排除的编码（包括所有子级）
			if (StringUtils.isNotBlank(excludeCode)){
				if (e.getId().equals(excludeCode)){
					continue;
				}
				if (e.getParentCodes().contains("," + excludeCode + ",")){
					continue;
				}
			}
			Map<String, Object> map = MapUtils.newHashMap();
			map.put("id", e.getId());
			map.put("pId", e.getParentCode());
			map.put("name", StringUtils.getTreeNodeName(isShowCode, e.getTreeCode(), e.getTreeName()));
			mapList.add(map);
		}
		return mapList;
	}

	/**
	 * 修复表结构相关数据
	 */
	@RequiresPermissions("test:testTree:edit")
	@RequestMapping(value = "fixTreeData")
	@ResponseBody
	public String fixTreeData(TestTree testTree){
		if (!UserUtils.getUser().isAdmin()){
			return renderResult(Global.FALSE, "操作失败，只有管理员才能进行修复！");
		}
		testTreeService.fixTreeData();
		return renderResult(Global.TRUE, "数据修复成功");
	}
	
}