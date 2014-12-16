/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.common.supcan.annotation.common.properties;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 硕正Properties注解
 * @author WangZhen
 * @version 2013-11-12
 */
@Target({ ElementType.ANNOTATION_TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface SupProperties {
	
	/**
	 * Treelist的ID	串
	 */
	String id() default "";

	/**
	 * 主键	串, 下面<col>的列名，复合主键须以逗号分隔
	 */
	String key() default "";

	/**
	 * 是否以树展现	true/false
	 */
	String isTree() default "";
	
	/**
	 * 是否显示左标尺	true/false
	 * 	editAble=true时，isShowRuler的默认值是true，否则是false;
	 */
	String isShowRuler() default "";

	/**
	 * 合计行是否固定在底部、始终显示	true/false
	 */
	String isFixTotalRow() default "";

	/**
	 * 合计行的背景色	颜色串
	 */
	String totalBgColor() default "";

	/**
	 * 小计行的背景色	颜色串
	 */
	String subTotalBgColor() default "";

	/**
	 * 是否允许增删改操作	true/false
	 * editAble=true时，isShowRuler的默认值是true，否则是false;
	 */
	String editAble() default "";
	
	/**
	 * 是否允许用户增行操作	true/false
	 * 注1: 仅在editAble=true时生效; 注2: 不影响增删改的API;
	 */
	String addRowAble() default "";

	/**
	 * 分屏方式	
	 * 		true - 始终显示分隔条;
	 * 		false - 始终不显示分隔条;
	 * 		auto - 自动(超宽时会在左侧浮现分隔条);
	 */
	String separateBarStyle() default "";

	/**
	 * 点击列标题是否执行排序	true/false
	 */
	String sortAble() default "";

	/**
	 * 是否允许多层表头	true/false
	 */
	String multiLayerAble() default "";

	/**
	 * 执行 Load() 函数时的淡入淡出效果	0 - 255, 数值越小效果越明显, 而 0 表示关闭此效果
	 */
	String fadeInStep() default "";

	/**
	 * 顶部标题条的背景色	颜色串，可以是以逗号分隔的多个颜色(渐变)
	 */
	String headerBgColor() default "";

	/**
	 * 顶部标题条的高度	像素数
	 */
	String headerHeight() default "";

	/**
	 * 左标尺的背景色，颜色串，可以是以逗号分隔的多个颜色(渐变)
	 */
	String leftColor() default "";

	/**
	 * 行高像素数
	 */
	String rowHeight() default "";

	/**
	 * 当前行的颜色，允许多个颜色渐变
	 * 颜色串, 支持ARGB格式，例如: "#10C0D0E0", 其中 "10" 是 alpha(透明度), "C0D0E0" 是 RGB, 此外, 还支持包含如下边框属性：
	   * 　　	borderWidth - 边框线宽
	 * 	　　borderColor - 边框线色
	 * 	　　borderRound - 边框线的圆角直径
	 * 	示例: "#12FFFFFF,#22EEFFEE; borderWidth=1; borderColor=red; borderRound=8"
	 */
	String curSelBgColor() default ""; // #F5F5F5 #FFE88D
	
	/**
	 * 整行的背景色、文字色表达式 表达式, 如:
	 * 		displayMask = "bgColor=if(price=0, red, transparent); textColor=if(price>2,#000022, blue)" 
	 */
	String displayMask() default "";
	
	/**
	 * 指定标题栏默认字体	<Fonts>中的字体顺序号 指向在<Fonts>中定义的字体的顺序号, 从0开始计数, 等级高于<Properties>中的同名属性
	 */
	String headerFontIndex() default "";
	
	/**
	 * 设置背景
	 */
	SupBackground packground() default @SupBackground;

	/**
	 * 计算列表达式
	 */
	SupExpress[] expresses() default {};
	
	/**
	 * 打印简易配置 标题
	 */
	String title() default "";

}
