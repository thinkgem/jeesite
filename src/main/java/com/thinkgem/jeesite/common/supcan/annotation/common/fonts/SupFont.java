/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.common.supcan.annotation.common.fonts;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 硕正Font注解
 * @author WangZhen
 * @version 2013-11-12
 */
@Target({ ElementType.ANNOTATION_TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface SupFont {
	
	/**
	 * 字体名称   微软雅黑  宋体
	 */
	String faceName() default "";

	/**
	 * 字符集 134
	 */
	String charSet() default "";

	/**
	 * Height(或size)是字体的尺寸，单位是字体的逻辑单位，通常采用小于0的数字，
	 * 如果大于0，则高度不包含文字的内部行距(internal-leading)。
	 * 常用的尺寸是-8, -9, -10, -11, -12, -14, -16, -18, -20, -22, -24, -26, -28, -36, -48, -72() ;
	 */
	String height() default "";
	
	/**
	 * 字体加粗 weight=400/700 对应 非粗体/粗体；
	 */
	String weight() default "";

	/**
	 * 字体宽度
	 */
	String width() default "";
	
	/**
	 * 字体斜体
	 */
	String italic() default "";
	
	/**
	 * 字体下划线
	 */
	String underline() default "";

}
