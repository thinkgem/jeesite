/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.common.supcan.common.fonts;

import com.thinkgem.jeesite.common.supcan.annotation.common.fonts.SupFont;
import com.thinkgem.jeesite.common.utils.ObjectUtils;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

/**
 * 硕正TreeList Properties
 * @author WangZhen
 * @version 2013-11-04
 */
@XStreamAlias("Font")
public class Font {

	/**
	 * 字体名称   微软雅黑  宋体
	 */
	@XStreamAsAttribute
	private String faceName;

	/**
	 * 字符集 134
	 */
	@XStreamAsAttribute
	private String charSet;

	/**
	 * Height(或size)是字体的尺寸，单位是字体的逻辑单位，通常采用小于0的数字，
	 * 如果大于0，则高度不包含文字的内部行距(internal-leading)。
	 * 常用的尺寸是-8, -9, -10, -11, -12, -14, -16, -18, -20, -22, -24, -26, -28, -36, -48, -72;
	 */
	@XStreamAsAttribute
	private String height;
	
	/**
	 * 字体加粗 weight=400/700 对应 非粗体/粗体；
	 */
	@XStreamAsAttribute
	private String weight;

	/**
	 * 字体宽度
	 */
	@XStreamAsAttribute
	private String width;
	
	/**
	 * 字体斜体
	 */
	@XStreamAsAttribute
	private String italic;
	
	/**
	 * 字体下划线
	 */
	@XStreamAsAttribute
	private String underline;
	
	public Font() {
		
	}

	public Font(SupFont supFont) {
		this();
		ObjectUtils.annotationToObject(supFont, this);
	}
	
	public Font(String faceName) {
		this();
		this.faceName = faceName;
	}
	
	public Font(String faceName, String charSet, String height) {
		this(faceName);
		this.charSet = charSet;
		this.height = height;
	}
	
	public Font(String faceName, String charSet, String height, String weight) {
		this(faceName, charSet, height);
		this.weight = weight;
	}

	public String getFaceName() {
		return faceName;
	}

	public void setFaceName(String faceName) {
		this.faceName = faceName;
	}

	public String getCharSet() {
		return charSet;
	}

	public void setCharSet(String charSet) {
		this.charSet = charSet;
	}

	public String getHeight() {
		return height;
	}

	public void setHeight(String height) {
		this.height = height;
	}

	public String getWeight() {
		return weight;
	}

	public void setWeight(String weight) {
		this.weight = weight;
	}

	public String getWidth() {
		return width;
	}

	public void setWidth(String width) {
		this.width = width;
	}

	public String getItalic() {
		return italic;
	}

	public void setItalic(String italic) {
		this.italic = italic;
	}

	public String getUnderline() {
		return underline;
	}

	public void setUnderline(String underline) {
		this.underline = underline;
	}
	
}
