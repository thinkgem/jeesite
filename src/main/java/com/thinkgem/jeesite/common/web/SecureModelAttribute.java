package com.thinkgem.jeesite.common.web;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ ElementType.PARAMETER, ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface SecureModelAttribute {

	String value() default "";

	String[] allowedField() default "*";

	String[] deniedField() default "";

	String[] clearFiled() default "";

}
