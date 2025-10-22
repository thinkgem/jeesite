/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 * No deletion without permission, or be held responsible to law.
 */
package com.jeesite.test.utils.excel;

import com.jeesite.common.utils.excel.annotation.ExcelField;
import com.jeesite.common.utils.excel.annotation.ExcelFields;

import java.math.BigDecimal;

/**
 * Excel 测试数据实体
 * @author ThinkGem
 */
public class ExcelTestEntity {

	// 方式1：声明在字段上
	@ExcelField(title = "字段1", align = ExcelField.Align.CENTER, sort = 10)
	private String field1;
	private Integer field2;
	private BigDecimal field3;

	// 方式2：声明在构造上
	@ExcelFields({
		@ExcelField(title = "字段2", attrName = "field2", align = ExcelField.Align.CENTER, sort = 20),
	})
	public ExcelTestEntity() {}

	public String getField1() {
		return field1;
	}

	public void setField1(String field1) {
		this.field1 = field1;
	}

	public Integer getField2() {
		return field2;
	}

	public void setField2(Integer field2) {
		this.field2 = field2;
	}

	// 方式3：声明在方法上
	@ExcelField(title = "字段3", align = ExcelField.Align.CENTER, sort = 30)
	public BigDecimal getField3() {
		return field3;
	}

	public void setField3(BigDecimal field3) {
		this.field3 = field3;
	}
}