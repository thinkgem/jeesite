/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 * No deletion without permission, or be held responsible to law.
 */
package com.jeesite.test.reflect;

import com.jeesite.common.lang.DateUtils;
import com.jeesite.common.lang.ObjectUtils;
import com.jeesite.common.lang.StringUtils;
import com.jeesite.common.lang.TimeUtils;
import com.jeesite.common.reflect.ReflectUtils;
import org.apache.commons.lang3.Validate;
import org.apache.poi.ss.usermodel.DateUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Date;
import java.util.Map;

/**
 * 反射工具类 性能测试
 * @author ThinkGem
 * @version 2023-2-6
 */
@SuppressWarnings("rawtypes")
public class ReflectUtilsTest {

	public static void main(String[] args) {
		int num = 100000;
		for (int i = 0; i < 5; i++) {
			getMethodTest(num, Type.FIELD);
			getMethodTest(num, Type.ASM);
			getMethodTest(num, Type.CACHE);
			getMethodTest(num, Type.NOT_CACHE);
			setMethodTest(num, Type.FIELD);
			setMethodTest(num, Type.ASM);
			setMethodTest(num, Type.CACHE);
			setMethodTest(num, Type.NOT_CACHE);
		}
		ReflectUtils.invokeGetter(Test.class, "staticMethodTest");
		ReflectUtils.invokeSetter(Test.class, "staticMethodTest", "123");
	}

	public static void getMethodTest(int num, Type type) {
		long start = System.currentTimeMillis();
		for (int i = 0; i < num; i++) {
			Test test = new Test("1", "2", "3", 4, 5, 6, new Date());
			for (int j = 1; j <= 7; j++) {
				String name = (j >= 1 && j <= 3 ? "str" : j >=4 && j<= 6 ? "int" : "date") + j;
				Object v = null;
				switch (type) {
					case ASM:
						v = ReflectUtils.invokeGetter(test, name);
						break;
					case CACHE:
						v = ReflectUtils2.invokeGetterCache(test, name);
						break;
					case NOT_CACHE:
						v = ReflectUtils2.invokeGetter(test, name);
						break;
					case FIELD:
						v = ReflectUtils.getFieldValue(test, name);
						break;
				}
				if (v == null) {
					throw new RuntimeException("error.");
				}else if (j == 1 && !v.equals(test.getStr1())) {
					throw new RuntimeException("error.");
				} else if (j == 4 && !v.equals(test.getInt4())) {
					throw new RuntimeException("error.");
				} else if (j == 7 && !v.equals(test.getDate7())) {
					throw new RuntimeException("error.");
				}
			}
		}
		long total = System.currentTimeMillis() - start;
		System.out.println("Get " + type.getVal() + ": " + TimeUtils.formatTime(total));
	}

	public static void setMethodTest(int num, Type type) {
		long start = System.currentTimeMillis();
		for (int i = 0; i < num; i++) {
			Test test = new Test();
			for (int j = 1; j <= 7; j++) {
				String name = (j >= 1 && j <= 3 ? "str" : j >=4 && j<= 6 ? "int" : "date") + j;
				Object v = (j >= 1 && j <= 3 ? ("str" + j) : j >=4 && j<= 6 ? (123 + j) : new Date());
				switch (type) {
					case ASM:
//						MethodAccess ma = MethodAccess.get(test.getClass());
//						ma.invoke(test, SETTER_PREFIX+StringUtils.capitalize(name), v);
						ReflectUtils.invokeSetter(test, name, v);
						break;
					case CACHE:
						ReflectUtils2.invokeSetterCache(test, name, v);
						break;
					case NOT_CACHE:
						ReflectUtils2.invokeSetter(test, name, v);
						break;
					case FIELD:
						ReflectUtils.setFieldValue(test, name, v);
						break;
				}
				if (j == 1 && !v.equals(test.getStr1())) {
					throw new RuntimeException("error.");
				} else if (j == 4 && !v.equals(test.getInt4())) {
					throw new RuntimeException("error.");
				} else if (j == 7 && !v.equals(test.getDate7())) {
					throw new RuntimeException("error.");
				}
			}
		}
		long total = System.currentTimeMillis() - start;
		System.out.println("Set " + type.getVal() + ": " + TimeUtils.formatTime(total));
	}

	enum Type {

		FIELD("field"),
		ASM("method asm"),
		CACHE("method use cache"),
		NOT_CACHE("method not use cache");

		private String val;

		Type(String val) {
			this.val = val;
		}

		public String getVal() {
			return val;
		}
	}

	public static class Test {

		private String str1;
		private String str2;
		private String str3;
		private Integer int4;
		private Integer int5;
		private Integer int6;
		private Date date7;

		public Test() {

		}

		public Test(String str1, String str2, String str3, Integer int4, Integer int5, Integer int6, Date date7) {
			this.str1 = str1;
			this.str2 = str2;
			this.str3 = str3;
			this.int4 = int4;
			this.int5 = int5;
			this.int6 = int6;
			this.date7 = date7;
		}

		public String getStr1() {
			return str1;
		}

		public void setStr1(String str1) {
			this.str1 = str1;
		}

		public String getStr2() {
			return str2;
		}

		public void setStr2(String str2) {
			this.str2 = str2;
		}

		public String getStr3() {
			return str3;
		}

		public void setStr3(String str3) {
			this.str3 = str3;
		}

		public Integer getInt4() {
			return int4;
		}

		public void setInt4(Integer int4) {
			this.int4 = int4;
		}

		public Integer getInt5() {
			return int5;
		}

		public void setInt5(Integer int5) {
			this.int5 = int5;
		}

		public Integer getInt6() {
			return int6;
		}

		public void setInt6(Integer int6) {
			this.int6 = int6;
		}

		public Date getDate7() {
			return date7;
		}

		public void setDate7(Date date7) {
			this.date7 = date7;
		}

		public static void getStaticMethodTest() {
			System.out.println("This is a static get method");
		}

		public static void setStaticMethodTest(String val) {
			System.out.println("This is a static set method, set val: " + val);
		}
	}
}

/**
 * 反射工具类. 提供调用getter/setter方法, 访问私有变量, 调用私有方法, 获取泛型类型Class, 被AOP过的真实类等工具函数.
 * @author calvin、ThinkGem
 * @version 2015-11-12
 */
class ReflectUtils2 {

	private static final Logger logger = LoggerFactory.getLogger(ReflectUtils2.class);
	private static final String SETTER_PREFIX = "set";
	private static final String GETTER_PREFIX = "get";
	private static Class baseEntityClass = null;

	/**
	 * 调用Getter方法，
	 * 支持多级，如：对象名.对象名.方法，
	 * 支持静态类及方法调用，
	 * 支持Map
	 */
	@SuppressWarnings("unchecked")
	public static <E> E invokeGetter(Object obj, String propertyName) {
		Object object = obj;
		for (String name : StringUtils.split(propertyName, StringUtils.DOT)){
			if (obj instanceof Map){
				object = ((Map)obj).get(name);
			}else{
				String methodName = GETTER_PREFIX + StringUtils.capitalize(name);
				object = invokeMethod(object, methodName, new Class[] {}, new Object[] {});
			}
		}
		return (E)object;
	}

	/**
	 * 调用Getter方法，
	 * 支持多级，如：对象名.对象名.方法，
	 * 支持静态类及方法调用，
	 * 支持Map
	 */
	@SuppressWarnings("unchecked")
	public static <E> E invokeGetterCache(Object obj, String propertyName) {
		Object object = obj;
		for (String name : StringUtils.split(propertyName, StringUtils.DOT)){
			if (obj instanceof Map){
				object = ((Map)obj).get(name);
			}else{
				String methodName = GETTER_PREFIX + StringUtils.capitalize(name);
				object = ReflectUtils.invokeMethodByName(object, methodName);
			}
		}
		return (E)object;
	}

	/**
	 * 调用Setter方法，仅匹配方法名，
	 * 支持多级，如：对象名.对象名.方法，
	 * 支持静态类及方法调用，
	 * 支持Map
	 */
	@SuppressWarnings("unchecked")
	public static <E> void invokeSetter(Object obj, String propertyName, E value) {
		Object object = obj;
		String[] names = StringUtils.split(propertyName, StringUtils.DOT);
		for (int i=0; i<names.length; i++){
			if(i<names.length-1){
				if (obj instanceof Map){
					object = ((Map)obj).get(names[i]);
				}else{
					String methodName = GETTER_PREFIX + StringUtils.capitalize(names[i]);                            
					Object childObj = ReflectUtils.invokeMethodByName(object, methodName);
					// 如果 get 获取对象为空，并且返回值类型继承自 BaseEntity，则 new 对象，并通过 set 赋予它
					if (childObj == null && object != null){
						Method method = getAccessibleMethod(object, methodName, new Class[] {});
						if (method != null) {
							Class<?> returnType = method.getReturnType();
							try {
								if (baseEntityClass == null) {
									baseEntityClass = Class.forName("com.jeesite.common.entity.BaseEntity");
								}
								if (baseEntityClass.isAssignableFrom(returnType)) {
									childObj = returnType.getDeclaredConstructor().newInstance();
									methodName = SETTER_PREFIX + StringUtils.capitalize(names[i]);
									invokeMethodByName(object, methodName, new Object[] { childObj });
								}
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
					}
					object = childObj;
				}
			}else{
				if (obj instanceof Map){
					((Map)obj).put(names[i], value);
				}else{
					String methodName = SETTER_PREFIX + StringUtils.capitalize(names[i]);
					invokeMethodByName(object, methodName, new Object[] { value });
				}
			}
		}
	}

	/**
	 * 调用Setter方法，仅匹配方法名，
	 * 支持多级，如：对象名.对象名.方法，
	 * 支持静态类及方法调用，
	 * 支持Map
	 */
	@SuppressWarnings("unchecked")
	public static <E> void invokeSetterCache(Object obj, String propertyName, E value) {
		Object object = obj;
		String[] names = StringUtils.split(propertyName, StringUtils.DOT);
		for (int i=0; i<names.length; i++){
			if(i<names.length-1){
				if (obj instanceof Map){
					object = ((Map)obj).get(names[i]);
				}else{
					String methodName = GETTER_PREFIX + StringUtils.capitalize(names[i]);
					Object childObj = ReflectUtils.invokeMethodByName(object, methodName);
					// 如果 get 获取对象为空，并且返回值类型继承自 BaseEntity，则 new 对象，并通过 set 赋予它
					if (childObj == null && object != null){
						Method method = getAccessibleMethod(object, methodName, new Class[] {});
						if (method != null) {
							Class<?> returnType = method.getReturnType();
							try {
								if (baseEntityClass == null) {
									baseEntityClass = Class.forName("com.jeesite.common.entity.BaseEntity");
								}
								if (baseEntityClass.isAssignableFrom(returnType)) {
									childObj = returnType.getDeclaredConstructor().newInstance();
									methodName = SETTER_PREFIX + StringUtils.capitalize(names[i]);
									ReflectUtils.invokeMethodByName(object, methodName, childObj);
								}
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
					}
					object = childObj;
				}
			}else{
				if (obj instanceof Map){
					((Map)obj).put(names[i], value);
				}else{
					String methodName = SETTER_PREFIX + StringUtils.capitalize(names[i]);
					ReflectUtils.invokeMethodByName(object, methodName, value);
				}
			}
		}
	}
	
	/**
	 * 直接读取对象属性值，无视private/protected修饰符，不经过getter函数
	 */
	@SuppressWarnings("unchecked")
	public static <E> E getFieldValue(final Object obj, final String fieldName) {
		Field field = getAccessibleField(obj, fieldName);
		if (field == null) {
			//throw new IllegalArgumentException("在 [" + obj.getClass() + "] 中，没有找到 [" + fieldName + "] 字段 ");
			if (obj != null) {
				logger.debug("在 [" + obj.getClass() + "] 中，没有找到 [" + fieldName + "] 字段 ");
			}
			return null;
		}
		E result = null;
		try {
			result = (E)field.get(obj);
		} catch (IllegalAccessException e) {
			logger.error("不可能抛出的异常: {}", e.getMessage());
		}
		return result;
	}

	/**
	 * 直接设置对象属性值，无视private/protected修饰符，不经过setter函数
	 */
	public static <E> void setFieldValue(final Object obj, final String fieldName, final E value) {
		Field field = getAccessibleField(obj, fieldName);
		if (field == null) {
			//throw new IllegalArgumentException("在 [" + obj.getClass() + "] 中，没有找到 [" + fieldName + "] 字段 ");
			logger.debug("在 [" + obj.getClass() + "] 中，没有找到 [" + fieldName + "] 字段 ");
			return;
		}
		try {
			field.set(obj, value);
		} catch (IllegalAccessException e) {
			logger.error("不可能抛出的异常: {}", e.getMessage());
		}
	}

	/**
	 * 直接调用对象方法，无视private/protected修饰符，
	 * 用于一次性调用的情况，否则应使用getAccessibleMethod()函数获得Method后反复调用，
	 * 同时匹配方法名+参数类型，
	 * 支持静态类及方法调用
	 */
	@SuppressWarnings("unchecked")
	public static <E> E invokeMethod(final Object obj, final String methodName, final Class<?>[] parameterTypes,
			final Object[] args) {
		if (obj == null || methodName == null){
			return null;
		}
		Method method = getAccessibleMethod(obj, methodName, parameterTypes);
		if (method == null) {
			//throw new IllegalArgumentException("在 [" + obj.getClass() + "] 中，没有找到 [" + methodName + "] 方法 ");
			if (obj != null) {
				logger.debug("在 [" + (obj.getClass() == Class.class ? obj : obj.getClass()) + "] 中，没有找到 [" + methodName + "] 方法 ");
			}
			return null;
		}
		try {
			return (E)method.invoke(obj.getClass() == Class.class ? null : obj, args);
		} catch (Exception e) {
			String msg = "method: "+method+", obj: "+obj+", args: "+args+"";
			throw ReflectUtils.convertReflectionExceptionToUnchecked(msg, e);
		}
	}

	/**
	 * 直接调用对象方法，无视private/protected修饰符，
	 * 用于一次性调用的情况，否则应使用getAccessibleMethodByName()函数获得Method后反复调用，
	 * 只匹配函数名，如果有多个同名函数调用第一个，
	 * 支持静态类及方法调用
	 */
	@SuppressWarnings("unchecked")
	public static <E> E invokeMethodByName(final Object obj, final String methodName, final Object[] args) {
		Method method = getAccessibleMethodByName(obj, methodName, args.length);
		if (method == null) {
			// 如果为空不报错，直接返回空。
//			throw new IllegalArgumentException("在 [" + obj.getClass() + "] 中，没有找到 [" + methodName + "] 方法 ");
			if (obj != null) {
				logger.debug("在 [" + (obj.getClass() == Class.class ? obj : obj.getClass()) + "] 中，没有找到 [" + methodName + "] 方法 ");
			}
			return null;
		}
		try {
			// 类型转换（将参数数据类型转换为目标方法参数类型）
			Class<?>[] cs = method.getParameterTypes();
			for (int i=0; i<cs.length; i++){
				if (args[i] != null && !args[i].getClass().equals(cs[i])){
					if (cs[i] == String.class){
						args[i] = ObjectUtils.toString(args[i]);
						if(StringUtils.endsWith((String)args[i], ".0")){
							args[i] = StringUtils.substringBefore((String)args[i], ".0");
						}
					}else if (cs[i] == Integer.class){
						args[i] = ObjectUtils.toInteger(args[i]);
					}else if (cs[i] == Long.class){
						args[i] = ObjectUtils.toLong(args[i]);
					}else if (cs[i] == Double.class){
						args[i] = ObjectUtils.toDouble(args[i]);
					}else if (cs[i] == Float.class){
						args[i] = ObjectUtils.toFloat(args[i]);
					}else if (cs[i] == Date.class){
						if (args[i] instanceof String){
							args[i] = DateUtils.parseDate(args[i]);
						}else{
							// POI Excel 日期格式转换
							args[i] = DateUtil.getJavaDate((Double)args[i]);
						}
					}
				}
			}
			return (E)method.invoke(obj.getClass() == Class.class ? null : obj, args);
		} catch (Exception e) {
			String msg = "method: "+method+", obj: "+obj+", args: "+args+"";
			throw ReflectUtils.convertReflectionExceptionToUnchecked(msg, e);
		}
	}

	/**
	 * 循环向上转型，获取对象的DeclaredField，并强制设置为可访问，
	 * 如向上转型到Object仍无法找到，返回null
	 */
	public static Field getAccessibleField(final Object obj, final String fieldName) {
		// 为空不报错。直接返回 null // Validate.notNull(obj, "object can't be null");
		if (obj == null){
			return null;
		}
		Validate.notBlank(fieldName, "fieldName can't be blank");
		Class<?> clazz = obj.getClass();
		for (Class<?> superClass = clazz; superClass != Object.class; superClass = superClass.getSuperclass()) {
			try {
				Field field = superClass.getDeclaredField(fieldName);
				ReflectUtils.makeAccessible(field);
				return field;
			} catch (NoSuchFieldException e) {//NOSONAR
				// Field不在当前类定义,继续向上转型
				continue;// new add
			}
		}
		return null;
	}

	/**
	 * 循环向上转型，获取对象的DeclaredMethod,并强制设置为可访问，
	 * 如向上转型到Object仍无法找到，返回null，
	 * 匹配函数名+参数类型。
	 * 用于方法需要被多次调用的情况，先使用本函数先取得Method，然后调用Method.invoke(Object obj, Object... args)
	 */
	public static Method getAccessibleMethod(final Object obj, final String methodName,
			final Class<?>... parameterTypes) {
		// 为空不报错。直接返回 null // Validate.notNull(obj, "object can't be null");
		if (obj == null){
			return null;
		}
		Class<?> clazz = obj.getClass();
		if (clazz == Class.class){
			clazz = (Class) obj;
		}
		Validate.notBlank(methodName, "methodName can't be blank");
		for (Class<?> superClass = clazz; superClass != Object.class; superClass = superClass.getSuperclass()) {
			try {
				Method method = superClass.getDeclaredMethod(methodName, parameterTypes);
				ReflectUtils.makeAccessible(method);
				return method;
			} catch (NoSuchMethodException e) {
				// Method不在当前类定义,继续向上转型
				continue;// new add
			}
		}
		return null;
	}

	/**
	 * 循环向上转型，获取对象的DeclaredMethod，并强制设置为可访问，
	 * 如向上转型到Object仍无法找到，返回null，
	 * 只匹配函数名。
	 * 用于方法需要被多次调用的情况，先使用本函数先取得Method，然后调用Method.invoke(Object obj, Object... args)
	 */
	public static Method getAccessibleMethodByName(final Object obj, final String methodName, int argsNum) {
		// 为空不报错。直接返回 null // Validate.notNull(obj, "object can't be null");
		if (obj == null){
			return null;
		}
		Class<?> clazz = obj.getClass();
		if (clazz == Class.class){
			clazz = (Class) obj;
		}
		Validate.notBlank(methodName, "methodName can't be blank");
		for (Class<?> superClass = clazz; superClass != Object.class; superClass = superClass.getSuperclass()) {
			Method[] methods = superClass.getDeclaredMethods();
			for (Method method : methods) {
				if (method.getName().equals(methodName) && method.getParameterTypes().length == argsNum) {
					ReflectUtils.makeAccessible(method);
					return method;
				}
			}
		}
		return null;
	}

}
