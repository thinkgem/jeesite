/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.common.utils.excel.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Excel注解定义
 * @author ThinkGem
 * @version 2013-03-10
 */
@Target({ElementType.METHOD, ElementType.FIELD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface ExcelField {

	/**
	 * 导出字段名（默认调用当前字段的“get”方法，如指定导出字段为对象，请填写“对象名.对象属性”，例：“area.name”、“office.name”）
	 */
	String attrName() default "";
	
	/**
	 * 导出字段标题（需要添加批注请用“**”分隔，标题**批注，仅对导出模板有效）
	 */
	String title();
	
	/**
	 * 字段类型（0：导出导入；1：仅导出；2：仅导入）
	 */
	Type type() default Type.ALL;
	public enum Type {
		ALL(0),
		EXPORT(1),
		IMPORT(2);
		private final int value;
		Type(int value) { this.value = value; }
		public int value() { return this.value; }
	}

	/**
	 * 导出字段对齐方式（0：自动；1：靠左；2：居中；3：靠右）
	 */
	Align align() default Align.AUTO;
	public enum Align {
		AUTO(0),
		LEFT(1),
		CENTER(2),
		RIGHT(3);
		private final int value;
		Align(int value) { this.value = value; }
		public int value() { return this.value; }
	}
	
	/**
	 * 指定导出列宽（以字符宽度的1/256为单位，假如你想显示5个字符的话，就可以设置5*256，1个汉字占2个字符）
	 */
	int width() default -1;
	
	/**
	 * 导出字段字段排序（升序）
	 */
	int sort() default 0;
	
	/**
	 * 导入时指定列索引（从0开始）在指定读取excel中的指定的列时使用
	 */
	int column() default -1;

	/**
	 * 如果是字典类型，请设置字典的type值
	 */
	String dictType() default "";
	
	/**
	 * 反射类型
	 * MoneyType.class 金额类型转换（保留两位）
	 * DateTimeType.class 日期时间类型转换 yyyy-MM-dd HH:mm:ss
	 */
	Class<?> fieldType() default Class.class;
	
	/**
	 * 数值格式（例如：0.00，yyyy-MM-dd）
	 */
	String dataFormat() default "@";
	
	/**
	 * 字段归属组（针对每一种业务的导入、导出） imp、exp
	 */
	String[] groups() default {};
}
