/**
 * Copyright (c) 2013-Now https://jeesite.com All rights reserved.
 * No deletion without permission, or be held responsible to law.
 */
package com.jeesite.common.utils.excel.annotation.impl;

import com.jeesite.common.lang.StringUtils;
import com.jeesite.common.utils.excel.annotation.ExcelField;
import com.jeesite.common.utils.excel.fieldtype.FieldType;

import java.io.Serial;
import java.io.Serializable;
import java.lang.annotation.Annotation;

/**
 * Excel 注解实现类
 * @author ThinkGem
 * @version 2020-3-5
 */
public class ExcelFieldImpl implements ExcelField, Serializable {

	@Serial
	private static final long serialVersionUID = 1L;

	private String attrName = StringUtils.EMPTY;
	private String title = StringUtils.EMPTY;
	private Type type = Type.ALL;
	private Align align = Align.AUTO;
	private int width = -1;
	private int words = -1;
	private int sort = 0;
	private int column = -1;
	private String dictType = StringUtils.EMPTY;
	private Class<? extends FieldType> fieldType = FieldType.class;
	private String dataFormat = StringUtils.EMPTY;
	private String[] groups = {};

	@Override
	public String attrName() {
		return attrName;
	}

	@Override
	public String title() {
		return title;
	}

	@Override
	public Type type() {
		return type;
	}

	@Override
	public Align align() {
		return align;
	}

	@Override
	public int width() {
		return width;
	}

	@Override
	public int words() {
		return words;
	}

	@Override
	public int sort() {
		return sort;
	}

	@Override
	public int column() {
		return column;
	}

	@Override
	public String dictType() {
		return dictType;
	}

	@Override
	public Class<? extends FieldType> fieldType() {
		return fieldType;
	}

	@Override
	public String dataFormat() {
		return dataFormat;
	}

	@Override
	public String[] groups() {
		return groups;
	}

	@Override
	public Class<? extends Annotation> annotationType() {
		return ExcelField.class;
	}

	// Setters

	public void setAttrName(String attrName) {
		if (attrName != null) {
			this.attrName = attrName;
		}
	}

	public void setTitle(String title) {
		if (title != null) {
			this.title = title;
		}
	}

	public void setType(Type type) {
		if (type != null) {
			this.type = type;
		}
	}

	public void setAlign(Align align) {
		if (align != null) {
			this.align = align;
		}
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public void setWords(int words) {
		this.words = words;
	}

	public void setSort(int sort) {
		this.sort = sort;
	}

	public void setColumn(int column) {
		this.column = column;
	}

	public void setDictType(String dictType) {
		if (dictType != null) {
			this.dictType = dictType;
		}
	}

	public void setFieldType(Class<? extends FieldType> fieldType) {
		if (fieldType != null) {
			this.fieldType = fieldType;
		}
	}

	public void setDataFormat(String dataFormat) {
		if (dataFormat != null) {
			this.dataFormat = dataFormat;
		}
	}

	public void setGroups(String[] groups) {
		if (groups != null) {
			this.groups = groups;
		}
	}

}