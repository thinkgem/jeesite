/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 * No deletion without permission, or be held responsible to law.
 */
package com.jeesite.test.utils.excel;

import com.jeesite.common.utils.excel.ExcelImport;
import org.apache.poi.ss.usermodel.Row;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;

/**
 * 导入Excel文件测试类
 * @author ThinkGem
 * @version 2020-3-5
 */
public class ExcelImportTest {
	
	/**
	 * 导入测试
	 */
	public static void main(String[] args) throws Throwable {

		File classPath = new File(ExcelExportTest.class.getResource("/").getFile());
		String fileName = classPath.getParentFile().getAbsoluteFile() + "/export.xlsx";
		ExcelImport ei = new ExcelImport(fileName, 1);

		for (int i = ei.getDataRowNum(); i < ei.getLastDataRowNum(); i++) {
			Row row = ei.getRow(i);
			if (row == null){
				continue;
			}
			for (int j = 0; j < ei.getLastCellNum(); j++) {
				Object val = ei.getCellValue(row, j);
				System.out.print(val+", ");
			}
			System.out.println();
		}

		System.out.println("Import success.");
	}

}
