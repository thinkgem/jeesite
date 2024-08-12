/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 * No deletion without permission, or be held responsible to law.
 */
package com.jeesite.test.utils.excel;

import com.jeesite.common.collect.ListUtils;
import com.jeesite.common.utils.excel.ExcelExport;
import com.jeesite.common.utils.excel.ExcelImport;
import org.apache.poi.ss.usermodel.Row;

import java.io.File;
import java.util.List;

/**
 * 导出Excel文件测试类
 * @author ThinkGem
 * @version 2020-3-5
 */
public class ExcelExportTest {
	
	/**
	 * 导出测试
	 */
	public static void main(String[] args) throws Throwable {

		File classPath = new File(ExcelExportTest.class.getResource("/").getFile());
		String fileName = classPath.getParentFile().getAbsoluteFile() + "/export.xlsx";

		// 初始化表头
		List<String> headerList = ListUtils.newArrayList();
		for (int i = 1; i <= 10; i++) {
			headerList.add("表头"+i);
		}

		// 初始化数据集
		List<String> rowList = ListUtils.newArrayList();
		for (int i = 1; i <= headerList.size(); i++) {
			rowList.add("数据"+i);
		}
		List<List<String>> dataList = ListUtils.newArrayList();
		for (int i = 1; i <=100; i++) {
			dataList.add(rowList);
		}

		// 创建一个Sheet表，并导入数据
		try(ExcelExport ee = new ExcelExport("表格1", "表格标题1", headerList, null)){

			for (int i = 0; i < dataList.size(); i++) {
				Row row = ee.addRow();
				for (int j = 0; j < dataList.get(i).size(); j++) {
					ee.addCell(row, j, dataList.get(i).get(j));
				}
			}

			// 再创建一个Sheet表，并导入数据
			ee.createSheet("表格2", "表格标题2", headerList, null);
			for (int i = 0; i < dataList.size(); i++) {
				Row row = ee.addRow();
				for (int j = 0; j < dataList.get(i).size(); j++) {
					ee.addCell(row, j, dataList.get(i).get(j)+"2");
				}
			}

			// 输出到文件
			ee.writeFile(fileName);

		}

		System.out.println("Export success.");

		// 按模板导出，从第 3 行开始
		ExcelImport ei = new ExcelImport(fileName, 3);
		try(ExcelExport ee = new ExcelExport(ei)){

			for (int i = 0; i < dataList.size(); i++) {
				Row row = ee.addRow();
				for (int j = 0; j < dataList.get(i).size(); j++) {
					ee.addCell(row, j, dataList.get(i).get(j) + "-plus");
				}
			}

			// 输出到文件
			ee.writeFile(fileName);

		}

		System.out.println("Export success by template.");

	}

}
