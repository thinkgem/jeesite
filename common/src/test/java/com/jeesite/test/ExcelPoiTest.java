/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

/**
 * Excel读写测试
 * @author ThinkGem
 * @version 2016年8月11日
 */
public class ExcelPoiTest {

	public static void main(String[] args) throws Exception {
		File file = new File("e:\\2016年调查表1.xls");
		HSSFWorkbook wb = new HSSFWorkbook(new FileInputStream(file));
		HSSFSheet sheet = wb.getSheetAt(2);
		sheet.getRow(5).getCell(3).setCellValue("山东有限公司");
		sheet.getRow(5).getCell(7).setCellValue("3799991911");
		sheet.getRow(8).getCell(3).setCellValue("174");
		sheet.getRow(8).getCell(7).setCellValue("私营股份有限公司");
		wb.write(new FileOutputStream("e:\\2016年调查表2.xls"));
		wb.close();
		System.out.println("success");
	}

}
