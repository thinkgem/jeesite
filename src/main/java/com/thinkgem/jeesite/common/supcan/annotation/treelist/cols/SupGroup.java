/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.common.supcan.annotation.treelist.cols;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 硕正Group注解
 * @author WangZhen
 * @version 2013-11-12
 */
@Target({ ElementType.ANNOTATION_TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface SupGroup {

	/**
	 * 分组的id，仅用于加载采用该id代替列名的XML/JSON数据
	 */
	String id();
	
	/**
	 * 显示的文字 串 
	 */
	String name() default "";

	/**
	 * 采用的字体, 前面定义的<Font>的序号 数字 指向在<Fonts>中定义的字体的顺序号, 从0开始计数, 等级高于<Properties>中的同名属性
	 */
	String headerFontIndex() default "";

	/**
	 * 文字颜色 颜色串 #000000 
	 */
	String textColor() default "";
	
	/**
	 * 文字对齐 left/center/right center 
	 */
	String align() default "";

	/**
	 * 父级组ID
	 */
	String parentId() default "";
	
	/**
	 * 排序（升序）
	 */
	int sort() default 0;
}
