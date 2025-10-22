/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 * No deletion without permission, or be held responsible to law.
 */
package com.jeesite.test.utils.excel;

import com.jeesite.common.utils.excel.ExcelImport;

import java.io.File;
import java.util.List;

/**
 * 导入Excel文件测试类
 * @author ThinkGem
 * @version 2025-10-21
 */
public class ExcelImportEntityTest {

	/**
	 * 导入测试
	 */
	public static void main(String[] args) throws Throwable {

		File classPath = new File(ExcelExportTest.class.getResource("/").getFile());
		String fileName = classPath.getParentFile().getAbsoluteFile() + "/export_entity.xlsx";
		try(ExcelImport ei = new ExcelImport(fileName, 2, 0)){
			List<ExcelTestEntity> list = ei.getDataList(ExcelTestEntity.class);
			for (ExcelTestEntity e : list) {
				System.out.print(e.getField1()+", ");
				System.out.print(e.getField2()+", ");
				System.out.print(e.getField3()+", ");
				System.out.println();
			}
		}

		System.out.println("Import success.");
	}

}
