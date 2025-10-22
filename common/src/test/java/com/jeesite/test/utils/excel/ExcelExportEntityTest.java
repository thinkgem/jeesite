/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 * No deletion without permission, or be held responsible to law.
 */
package com.jeesite.test.utils.excel;

import com.jeesite.common.collect.ListUtils;
import com.jeesite.common.utils.excel.ExcelExport;

import java.io.File;
import java.math.BigDecimal;
import java.util.List;

/**
 * 导出Excel文件测试类
 * @author ThinkGem
 * @version 2025-10-21
 */
public class ExcelExportEntityTest {
	
	/**
	 * 导出测试
	 */
	public static void main(String[] args) throws Throwable {

		List<ExcelTestEntity> list = ListUtils.newArrayList();
		for (int i = 1; i <= 50; i++) {
			ExcelTestEntity e = new ExcelTestEntity();
			e.setField1("test"+i);
			e.setField2(123+i);
			e.setField3(BigDecimal.valueOf(12345+i));
			list.add(e);
		}

		File classPath = new File(ExcelExportEntityTest.class.getResource("/").getFile());
		String fileName = classPath.getParentFile().getAbsoluteFile() + "/export_entity.xlsx";

		// 创建一个Sheet表，并导入数据
		try(ExcelExport ee = new ExcelExport("表格1", ExcelTestEntity.class)){
			ee.setDataList(list).writeFile(fileName);
		}

		System.out.println("Export success.");

	}

}
