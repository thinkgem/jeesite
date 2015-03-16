/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.common.supcan.common.properties;

import com.thinkgem.jeesite.common.supcan.annotation.common.properties.SupExpress;
import com.thinkgem.jeesite.common.utils.ObjectUtils;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamConverter;
import com.thoughtworks.xstream.converters.extended.ToAttributedValueConverter;

/**
 * 硕正TreeList Properties Express
 * @author WangZhen
 * @version 2013-11-04
 */
@XStreamAlias("Express")
@XStreamConverter(value = ToAttributedValueConverter.class, strings = {"text"})
public class Express {

	/**
	 * 是否自动按列的引用关系优化计算顺序  默认值true
	 */
	@XStreamAsAttribute
	private String isOpt;
	
	/**
	 * 文本
	 */
	private String text;

	public Express() {
		
	}
	
	public Express(SupExpress supExpress) {
		this();
		ObjectUtils.annotationToObject(supExpress, this);
	}
	
	public Express(String text) {
		this.text = text;
	}
	
	public Express(String name, String text) {
		this(name);
		this.text = text;
	}
	
	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getIsOpt() {
		return isOpt;
	}

	public void setIsOpt(String isOpt) {
		this.isOpt = isOpt;
	}
	
}
