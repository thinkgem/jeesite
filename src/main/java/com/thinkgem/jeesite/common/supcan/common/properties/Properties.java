/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.common.supcan.common.properties;

import java.util.List;

import com.google.common.collect.Lists;
import com.thinkgem.jeesite.common.supcan.annotation.common.properties.SupExpress;
import com.thinkgem.jeesite.common.supcan.annotation.common.properties.SupProperties;
import com.thinkgem.jeesite.common.utils.ObjectUtils;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

/**
 * 硕正TreeList Properties
 * @author WangZhen
 * @version 2013-11-04
 */
@XStreamAlias("Properties")
public class Properties {
	
	/**
	 * Treelist的ID	串
	 */
	@XStreamAsAttribute
	private String id;

	/**
	 * 主键	串, 下面<col>的列名，复合主键须以逗号分隔
	 */
	@XStreamAsAttribute
	private String key;

	/**
	 * 是否以树展现	true/false
	 */
	@XStreamAsAttribute
	private String isTree = "false";
	
	/**
	 * 是否显示左标尺	true/false
	 * 	editAble=true时，isShowRuler的默认值是true，否则是false;
	 */
	@XStreamAsAttribute
	private String isShowRuler = "false";

	/**
	 * 合计行是否固定在底部、始终显示	true/false
	 */
	@XStreamAsAttribute
	private String isFixTotalRow = "false";

	/**
	 * 合计行的背景色	颜色串
	 */
	@XStreamAsAttribute
	private String totalBgColor = "#FFFFCC";

	/**
	 * 小计行的背景色	颜色串
	 */
	@XStreamAsAttribute
	private String subTotalBgColor = "#FFFFCC";

	/**
	 * 是否允许增删改操作	true/false
	 * editAble=true时，isShowRuler的默认值是true，否则是false;
	 */
	@XStreamAsAttribute
	private String editAble = "false";
	
	/**
	 * 是否允许用户增行操作	true/false
	 * 注1: 仅在editAble=true时生效; 注2: 不影响增删改的API;
	 */
	@XStreamAsAttribute
	private String addRowAble = "true";
	
	/**
	 * 分屏方式	
	 * 		true - 始终显示分隔条;
	 * 		false - 始终不显示分隔条;
	 * 		auto - 自动(超宽时会在左侧浮现分隔条);
	 */
	@XStreamAsAttribute
	private String separateBarStyle = "false";

	/**
	 * 点击列标题是否执行排序	true/false
	 */
	@XStreamAsAttribute
	private String sortAble = "true";

	/**
	 * 是否允许多层表头	true/false
	 */
	@XStreamAsAttribute
	private String multiLayerAble = "false";

	/**
	 * 执行 Load() 函数时的淡入淡出效果	0 - 255, 数值越小效果越明显, 而 0 表示关闭此效果
	 */
	@XStreamAsAttribute
	private String fadeInStep = "0";

	/**
	 * 顶部标题条的背景色	颜色串，可以是以逗号分隔的多个颜色(渐变)
	 */
	@XStreamAsAttribute
	private String headerBgColor = "#FDFDFD,#F0F1EF";

	/**
	 * 顶部标题条的高度	像素数
	 */
	@XStreamAsAttribute
	private String headerHeight = "28";

	/**
	 * 左标尺的背景色，颜色串，可以是以逗号分隔的多个颜色(渐变)
	 */
	@XStreamAsAttribute
	private String leftColor = "#F0F1EF,#FDFDFD";

	/**
	 * 行高像素数
	 */
	@XStreamAsAttribute
	private String rowHeight = "28";

	/**
	 * 当前行的颜色，允许多个颜色渐变
	 * 颜色串, 支持ARGB格式，例如: "#10C0D0E0", 其中 "10" 是 alpha(透明度), "C0D0E0" 是 RGB, 此外, 还支持包含如下边框属性：
	   * 　　	borderWidth - 边框线宽
	 * 	　　borderColor - 边框线色
	 * 	　　borderRound - 边框线的圆角直径
	 * 	示例: "#12FFFFFF,#22EEFFEE; borderWidth=1; borderColor=red; borderRound=8"
	 */
	@XStreamAsAttribute
	private String curSelBgColor = "#F5F5F5,#EDEDED"; // #F5F5F5 #FFE88D
	
	/**
	 * 整行的背景色、文字色表达式 表达式, 如:
	 * 		displayMask = "bgColor=if(price=0, red, transparent); textColor=if(price>2,#000022, blue)" 
	 */
	@XStreamAsAttribute
	private String displayMask;
	
	/**
	 * 指定标题栏默认字体	<Fonts>中的字体顺序号
	 */
	@XStreamAsAttribute
	private String headerFontIndex;
	
	/**
	 * 设置背景
	 */
	@XStreamAlias("Background")
	private Background packground = new Background();

	/**
	 * 计算列表达式
	 */
	@XStreamAlias("Expresses")
	private List<Express> expresses;
	
	/**
	 * 打印简易配置 标题
	 */
	@XStreamAsAttribute
	private String title;
	
	public Properties() {
		
	}

	public Properties(SupProperties supProperties) {
		this();
		ObjectUtils.annotationToObject(supProperties, this);
		if (supProperties.packground() != null){
			this.packground = new Background(supProperties.packground());
		}
		if (supProperties.expresses() != null){
			for (SupExpress supExpress : supProperties.expresses()){
				if (this.expresses == null){
					this.expresses = Lists.newArrayList();
				}
				this.expresses.add(new Express(supExpress));
			}
		}
	}
	
	public Properties(String id) {
		this();
		this.id = id;
	}

	public Properties(String id, String key) {
		this(id);
		this.key = key;
	}
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getIsTree() {
		return isTree;
	}

	public void setIsTree(String isTree) {
		this.isTree = isTree;
	}

	public String getIsShowRuler() {
		return isShowRuler;
	}

	public void setIsShowRuler(String isShowRuler) {
		this.isShowRuler = isShowRuler;
	}

	public String getIsFixTotalRow() {
		return isFixTotalRow;
	}

	public void setIsFixTotalRow(String isFixTotalRow) {
		this.isFixTotalRow = isFixTotalRow;
	}

	public String getTotalBgColor() {
		return totalBgColor;
	}

	public void setTotalBgColor(String totalBgColor) {
		this.totalBgColor = totalBgColor;
	}

	public String getSubTotalBgColor() {
		return subTotalBgColor;
	}

	public void setSubTotalBgColor(String subTotalBgColor) {
		this.subTotalBgColor = subTotalBgColor;
	}

	public String getEditAble() {
		return editAble;
	}

	public void setEditAble(String editAble) {
		this.editAble = editAble;
	}

	public String getSeparateBarStyle() {
		return separateBarStyle;
	}

	public void setSeparateBarStyle(String separateBarStyle) {
		this.separateBarStyle = separateBarStyle;
	}

	public String getSortAble() {
		return sortAble;
	}

	public void setSortAble(String sortAble) {
		this.sortAble = sortAble;
	}

	public String getMultiLayerAble() {
		return multiLayerAble;
	}

	public void setMultiLayerAble(String multiLayerAble) {
		this.multiLayerAble = multiLayerAble;
	}

	public String getFadeInStep() {
		return fadeInStep;
	}

	public void setFadeInStep(String fadeInStep) {
		this.fadeInStep = fadeInStep;
	}

	public String getHeaderBgColor() {
		return headerBgColor;
	}

	public void setHeaderBgColor(String headerBgColor) {
		this.headerBgColor = headerBgColor;
	}

	public String getHeaderHeight() {
		return headerHeight;
	}

	public void setHeaderHeight(String headerHeight) {
		this.headerHeight = headerHeight;
	}

	public String getLeftColor() {
		return leftColor;
	}

	public void setLeftColor(String leftColor) {
		this.leftColor = leftColor;
	}

	public String getRowHeight() {
		return rowHeight;
	}

	public void setRowHeight(String rowHeight) {
		this.rowHeight = rowHeight;
	}

	public String getCurSelBgColor() {
		return curSelBgColor;
	}

	public void setCurSelBgColor(String curSelBgColor) {
		this.curSelBgColor = curSelBgColor;
	}

	public String getHeaderFontIndex() {
		return headerFontIndex;
	}

	public void setHeaderFontIndex(String headerFontIndex) {
		this.headerFontIndex = headerFontIndex;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Background getPackground() {
		return packground;
	}

	public void setPackground(Background packground) {
		this.packground = packground;
	}

	public List<Express> getExpresses() {
		return expresses;
	}

	public void setExpresses(List<Express> expresses) {
		this.expresses = expresses;
	}

	public String getDisplayMask() {
		return displayMask;
	}

	public void setDisplayMask(String displayMask) {
		this.displayMask = displayMask;
	}

	public String getAddRowAble() {
		return addRowAble;
	}

	public void setAddRowAble(String addRowAble) {
		this.addRowAble = addRowAble;
	}
	
}
