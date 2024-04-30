/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 * No deletion without permission, or be held responsible to law.
 */
package com.jeesite.modules.test.db;

import com.jeesite.common.datasource.DataSourceHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import com.jeesite.common.config.Global;
import com.jeesite.common.lang.StringUtils;
import com.jeesite.common.tests.BaseInitDataTests;
import com.jeesite.modules.gen.entity.GenTable;
import com.jeesite.modules.gen.entity.GenTableColumn;
import com.jeesite.modules.gen.service.GenTableService;
import com.jeesite.modules.gen.utils.GenUtils;

/**
 * 初始化核心表数据
 * @author ThinkGem
 * @version 2020-5-26
 */
@Component
@ConditionalOnProperty(name="jeesite.initdata", havingValue="true", matchIfMissing=false)
public class InitTestData extends BaseInitDataTests {

	@Override
	public boolean initData() throws Exception {
		if (GenUtils.isTableExists("test_data")) {
			return true; // 如果表已存在，则无需初始化
		}
		runCreateScript("test.sql");
		initGenDemoData();
		return true;
	}

	@Autowired
	private GenTableService genTableService;
	/**
	 * 代码生成测试数据
	 */
	public void initGenDemoData() throws Exception{
		if (!checkTable(GenTable.class)) {
			return;
		}
		if (!checkTable(GenTableColumn.class)) {
			return;
		}
		initGenTestData();
		initGenTreeData();
	}
	/**
	 * 代码生成测试数据
	 */
	private void initGenTestData() throws Exception{
		GenTable genTable = new GenTable();
		genTable.setIsNewRecord(true);
		genTable.setTableName("test_data");
		genTable.setDataSourceName(dataSourceName);
		genTable = genTableService.getFromDb(genTable);
		genTable.setIsNewRecord(true);
		genTable.setClassName("TestData");
		genTable.setFunctionAuthor("ThinkGem");
		genTable.setTplCategory("crud");
		genTable.setPackageName("com.jeesite.modules");
		genTable.setModuleName("test");
		genTable.setSubModuleName("");
		genTable.setFunctionName("测试数据");
		genTable.setFunctionNameSimple("数据");
		genTable.getOptionMap().put("isHaveDisableEnable", Global.YES);
		genTable.getOptionMap().put("isHaveDelete", Global.YES);
		genTable.getOptionMap().put("isFileUpload", Global.YES);
		genTable.getOptionMap().put("isImageUpload", Global.YES);
		initGenTableColumn(genTable);
		DataSourceHolder.setDataSourceName(dataSourceName);
		genTableService.save(genTable);
		// 子表
		GenTable genTableChild = new GenTable();
		genTableChild.setIsNewRecord(true);
		genTableChild.setTableName("test_data_child");
		genTableChild.setDataSourceName(dataSourceName);
		genTableChild = genTableService.getFromDb(genTableChild);
		genTableChild.setIsNewRecord(true);
		genTableChild.setClassName("TestDataChild");
		genTableChild.setFunctionAuthor("ThinkGem");
		genTableChild.setTplCategory("crud");
		genTableChild.setPackageName("com.jeesite.modules");
		genTableChild.setModuleName("test");
		genTableChild.setSubModuleName("");
		genTableChild.setFunctionName("测试子表");
		genTableChild.setFunctionNameSimple("数据");
		genTableChild.setParentTableName("test_data");
		genTableChild.setParentTableFkName("test_data_id");
		initGenTableColumn(genTableChild);
		DataSourceHolder.setDataSourceName(dataSourceName);
		genTableService.save(genTableChild);
	}

	/**
	 * 代码生成测试数据（列初始化）
	 */
	private void initGenTableColumn(GenTable genTable){
		for(GenTableColumn column : genTable.getColumnList()){
			if ("test_input".equals(column.getColumnName())
					|| "test_textarea".equals(column.getColumnName())
					|| "test_select".equals(column.getColumnName())
					|| "test_select_multiple".equals(column.getColumnName())
					|| "test_checkbox".equals(column.getColumnName())
					|| "test_radio".equals(column.getColumnName())
					|| "test_date".equals(column.getColumnName())
					|| "test_datetime".equals(column.getColumnName())
				){
				column.setShowType(StringUtils.substringAfter(
						column.getColumnName(), "test_"));
				if ("test_input".equals(column.getColumnName())
						){
					column.setQueryType("LIKE");
				}
				else if ("test_textarea".equals(column.getColumnName())
						){
					column.setQueryType("LIKE");
					column.getOptionMap().put("isNewLine", Global.YES);
//					column.getOptionMap().put("gridRowCol", "12/2/10");
				}
				else if ("test_select".equals(column.getColumnName())
						|| "test_select_multiple".equals(column.getColumnName())
						|| "test_radio".equals(column.getColumnName())
						|| "test_checkbox".equals(column.getColumnName())
						){
					column.getOptionMap().put("dictType", "sys_menu_type");
					column.getOptionMap().put("dictName", "sys_menu_type");
				}
				else if ("test_date".equals(column.getColumnName())
						|| "test_datetime".equals(column.getColumnName())
						){
					column.setQueryType("BETWEEN");
				}
			}else if ("test_user_code".equals(column.getColumnName())){
				column.setAttrType("com.jeesite.modules.sys.entity.User");
				column.setFullAttrName("testUser");
				column.setShowType("userselect");
			}else if ("test_office_code".equals(column.getColumnName())){
				column.setAttrType("com.jeesite.modules.sys.entity.Office");
				column.setFullAttrName("testOffice");
				column.setShowType("officeselect");
			}else if ("test_area_code".equals(column.getColumnName())){
				column.setFullAttrName("testAreaCode|testAreaName");
				column.setShowType("areaselect");
			}else if ("test_area_name".equals(column.getColumnName())){
				column.setIsEdit(Global.NO);
				column.setIsQuery(Global.NO);
			}else if ("test_data_id".equals(column.getColumnName())){
				column.setFullAttrName("testData");
			}
		}
	}

	/**
	 * 代码生成树表测试数据
	 */
	private void initGenTreeData() throws Exception{
		GenTable genTable = new GenTable();
		genTable.setIsNewRecord(true);
		genTable.setTableName("test_tree");
		genTable.setDataSourceName(dataSourceName);
		genTable = genTableService.getFromDb(genTable);
		genTable.setIsNewRecord(true);
		genTable.setClassName("TestTree");
		genTable.setFunctionAuthor("ThinkGem");
		genTable.setTplCategory("treeGrid");
		genTable.setPackageName("com.jeesite.modules");
		genTable.setModuleName("test");
		genTable.setSubModuleName("");
		genTable.setFunctionName("测试树表");
		genTable.setFunctionNameSimple("数据");
		genTable.getOptionMap().put("isHaveDisableEnable", Global.YES);
		genTable.getOptionMap().put("isHaveDelete", Global.YES);
		genTable.getOptionMap().put("isFileUpload", Global.YES);
		genTable.getOptionMap().put("isImageUpload", Global.YES);
		genTable.getOptionMap().put("treeViewCode", "tree_code");
		genTable.getOptionMap().put("treeViewName", "tree_name");
		initGenTableColumn(genTable);
		DataSourceHolder.setDataSourceName(dataSourceName);
		genTableService.save(genTable);
	}
	
}
