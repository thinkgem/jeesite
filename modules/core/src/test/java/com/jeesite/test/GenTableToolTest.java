/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.test;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;

import com.jeesite.common.config.Global;
import com.jeesite.common.lang.StringUtils;
import com.jeesite.common.tests.BaseSpringContextTests;
import com.jeesite.modules.gen.entity.GenTable;
import com.jeesite.modules.gen.entity.GenTableColumn;
import com.jeesite.modules.gen.service.GenTableService;

/**
 * 代码生成工具（API）
 * @author ThinkGem
 * @version 2020-2-1
 */
@ActiveProfiles("test")
@SpringBootTest(classes=ApplicationTest.class)
@Rollback(false)
public class GenTableToolTest extends BaseSpringContextTests {

	@Autowired
	private GenTableService genTableService;
	
	@Test
	public void execute() throws Exception{
		GenTable genTable = new GenTable();
		genTable.setIsNewRecord(true);
		genTable.setTableName("test_data_demo"); // 表名
		genTable = genTableService.getFromDb(genTable);
		genTable.setClassName(StringUtils.capCamelCase(genTable.getTableName())); // 实体类名
		genTable.setFunctionAuthor("ThinkGem");	// 作者名称
		genTable.setTplCategory("crud");		// 生成模板：crud、treeGrid、service、dao、query
		genTable.setPackageName("com.jeesite.modules");// 生成包路径
		genTable.setModuleName("test");			// 生成模块名
		genTable.setSubModuleName("");			// 生成子模块名
		genTable.setFunctionName(genTable.getComments()); 				// 生成功能名
		genTable.setFunctionNameSimple(genTable.getComments()); 		// 功能名（简称）
		genTable.getOptionMap().put("isHaveDisableEnable", Global.NO);	// 是否有停用启用
		genTable.getOptionMap().put("isHaveDelete", Global.YES);		// 是否有删除功能
		genTable.getOptionMap().put("isFileUpload", Global.NO);			// 是否有上传附件
		genTable.getOptionMap().put("isImageUpload", Global.NO);		// 是否有上传图片
		for(GenTableColumn column : genTable.getColumnList()){
			// 字段配置（下拉框）
			if ("trade_type".equals(column.getColumnName())){
				column.setShowType("select");
				column.getOptionMap().put("dictType", "sys_menu_type");
			}
		}
		// 删除旧配置
		genTableService.delete(genTable);
		// 保存新配置
		genTable.setIsNewRecord(true);
		genTableService.save(genTable);
		// 编译并生成代码
		genTable.setGenFlag("1"); // 1：编译输出；2：生成文件
		genTable.setReplaceFile(Global.YES); // 如果存在则替换
		String result = genTableService.generateCode(genTable);
		System.out.println(result);
	}
	
}
