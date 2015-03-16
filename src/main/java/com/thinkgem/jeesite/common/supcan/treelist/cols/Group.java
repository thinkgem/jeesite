/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.common.supcan.treelist.cols;

import java.util.List;

import com.google.common.collect.Lists;
import com.thinkgem.jeesite.common.supcan.annotation.treelist.cols.SupGroup;
import com.thinkgem.jeesite.common.utils.ObjectUtils;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamImplicit;
import com.thoughtworks.xstream.annotations.XStreamOmitField;

/**
 * 硕正TreeList Cols Group
 * @author WangZhen
 * @version 2013-11-04
 */
@XStreamAlias("Group")
public class Group {

	/**
	 * 分组的id，仅用于加载采用该id代替列名的XML/JSON数据
	 */
	@XStreamAsAttribute
	private String id;
	
	/**
	 * 显示的文字 串 
	 */
	@XStreamAsAttribute
	private String name;

	/**
	 * 采用的字体, 前面定义的<Font>的序号 数字  指向在<Fonts>中定义的字体的顺序号, 从0开始计数, 等级高于<Properties>中的同名属性
	 */
	@XStreamAsAttribute
	private String headerFontIndex;

	/**
	 * 文字颜色 颜色串 #000000 
	 */
	@XStreamAsAttribute
	private String textColor;
	
	/**
	 * 文字对齐 left/center/right center 
	 */
	@XStreamAsAttribute
	private String align;
	
	/**
	 * 分组下的列集合
	 */
	@XStreamAlias("Cols")
	@XStreamImplicit
	private List<Object> cols;
	
	/**
	 * 父级组ID，注解定义时有效
	 */
	@XStreamOmitField
	private String parentId;

	/**
	 * 字段排序，注解定义时有效
	 */
	@XStreamOmitField
	private int sort;
	
	public Group() {
		
	}
	
	public Group(String name) {
		this();
		this.name = name;
	}
	
	public Group(String name, List<Object> cols) {
		this(name);
		this.cols = cols;
	}
	
	public Group(SupGroup supGroup){
		ObjectUtils.annotationToObject(supGroup, this);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public List<Object> getCols() {
		if (cols == null){
			cols = Lists.newArrayList();
		}
		return cols;
	}

	public void setCols(List<Object> cols) {
		this.cols = cols;
	}

	public String getHeaderFontIndex() {
		return headerFontIndex;
	}

	public void setHeaderFontIndex(String headerFontIndex) {
		this.headerFontIndex = headerFontIndex;
	}

	public String getTextColor() {
		return textColor;
	}

	public void setTextColor(String textColor) {
		this.textColor = textColor;
	}

	public String getAlign() {
		return align;
	}

	public void setAlign(String align) {
		this.align = align;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getParentId() {
		return parentId;
	}

	public void setParentId(String parentId) {
		this.parentId = parentId;
	}

	public int getSort() {
		return sort;
	}

	public void setSort(int sort) {
		this.sort = sort;
	}
	
}
