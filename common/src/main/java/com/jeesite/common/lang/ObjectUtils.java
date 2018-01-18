/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.common.lang;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Method;

import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.BeanUtils;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.jeesite.common.io.IOUtils;

/**
 * 对象操作工具类, 继承org.apache.commons.lang3.ObjectUtils类
 * @author ThinkGem
 * @version 2014-6-29
 */
public class ObjectUtils extends org.apache.commons.lang3.ObjectUtils {

	/**
	 * 转换为Double类型
	 */
	public static Double toDouble(final Object val) {
		if (val == null) {
			return 0D;
		}
		try {
			return NumberUtils.toDouble(StringUtils.trim(val.toString()));
		} catch (Exception e) {
			return 0D;
		}
	}

	/**
	 * 转换为Float类型
	 */
	public static Float toFloat(final Object val) {
		return toDouble(val).floatValue();
	}

	/**
	 * 转换为Long类型
	 */
	public static Long toLong(final Object val) {
		return toDouble(val).longValue();
	}

	/**
	 * 转换为Integer类型
	 */
	public static Integer toInteger(final Object val) {
		return toLong(val).intValue();
	}

	/**
	 * 转换为Boolean类型 'true', 'on', 'y', 't', 'yes' or '1' (case insensitive) will return true. Otherwise, false is returned.
	 */
	public static Boolean toBoolean(final Object val) {
		if (val == null) {
			return false;
		}
		return BooleanUtils.toBoolean(val.toString()) || "1".equals(val.toString());
	}

	/**
	 * 转换为字符串
	 * @param obj
	 * @return
	 */
	public static String toString(final Object obj) {
		return toString(obj, StringUtils.EMPTY);
	}

	/**
	 * 如果对象为空，则使用defaultVal值
	 * @param obj
	 * @param defaultVal
	 * @return
	 */
	public static String toString(final Object obj, final String defaultVal) {
		return obj == null ? defaultVal : obj.toString();
	}

	/**
	 * 空转空字符串（"" to "" ; null to "" ; "null" to "" ; "NULL" to "" ; "Null" to ""）
	 * @param val 需转换的值
	 * @return 返回转换后的值
	 */
	public static String toStringIgnoreNull(final Object val) {
		return ObjectUtils.toStringIgnoreNull(val, StringUtils.EMPTY);
	}

	/**
	 * 空对象转空字符串 （"" to defaultVal ; null to defaultVal ; "null" to defaultVal ; "NULL" to defaultVal ; "Null" to defaultVal）
	 * @param val 需转换的值
	 * @param defaultVal 默认值
	 * @return 返回转换后的值
	 */
	public static String toStringIgnoreNull(final Object val, String defaultVal) {
		String str = ObjectUtils.toString(val);
		return !"".equals(str) && !"null".equals(str.trim().toLowerCase()) ? str : defaultVal;
	}

	/**
	 * 注解到对象复制，只复制能匹配上的方法。 硕正组件用。
	 * @param annotation
	 * @param object
	 */
	public static void annotationToObject(Object annotation, Object object) {
		if (annotation != null && object != null) {
			Class<?> annotationClass = annotation.getClass();
			Class<?> objectClass = object.getClass();
			for (Method m : objectClass.getMethods()) {
				if (StringUtils.startsWith(m.getName(), "set")) {
					try {
						String s = StringUtils.uncapitalize(StringUtils.substring(m.getName(), 3));
						Object obj = annotationClass.getMethod(s).invoke(annotation);
						if (obj != null && !"".equals(obj.toString())) {
//							if (object == null){
//								object = objectClass.newInstance();
//							}
							m.invoke(object, obj);
						}
					} catch (Exception e) {
						// 忽略所有设置失败方法
					}
				}
			}
		}
	}

	/**
	 * 序列化对象
	 * @param object
	 * @return
	 */
	public static byte[] serialize(Object object) {
		ObjectOutputStream oos = null;
		ByteArrayOutputStream baos = null;
		try {
			if (object != null) {
				baos = new ByteArrayOutputStream();
				oos = new ObjectOutputStream(baos);
				oos.writeObject(object);
				return baos.toByteArray();
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			IOUtils.closeQuietly(oos);
			IOUtils.closeQuietly(baos);
		}
		return null;
	}

	/**
	 * 反序列化对象
	 * @param bytes
	 * @return
	 */
	public static Object unserialize(byte[] bytes) {
		ByteArrayInputStream bais = null;
		ObjectInputStream ois = null;
		try {
			if (bytes != null && bytes.length > 0) {
				bais = new ByteArrayInputStream(bytes);
				ois = new ObjectInputStream(bais);
				return ois.readObject();
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			IOUtils.closeQuietly(ois);
			IOUtils.closeQuietly(bais);
		}
		return null;
	}

	// Kryo不是线程安全的，所以要建立一个线程变量，每一个线程实例化一次
	public static final ThreadLocal<Kryo> kryos = new ThreadLocal<Kryo>() {
		@Override
		protected Kryo initialValue() {
			Kryo kryo = new Kryo();
			return kryo;
		};
	};

	/**
	 * Kryo序列化对象
	 * @param object
	 * @return
	 */
	public static byte[] serializeKryo(Object object) {
//		long beginTime = System.currentTimeMillis();
		byte[] bytes = null;
		Output output = null;
		try {
			if (object != null) {
				output = new Output(1024, -1);
				kryos.get().writeClassAndObject(output, object);
				bytes = output.toBytes();
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (output != null) {
				output.close();
			}
		}
//		GlobalConfig.log(ObjectUtils.class, object + " 序列化用时：" + DateUtils.formatDateTime(System.currentTimeMillis() - beginTime));
		return bytes;
	}

	/**
	 * Kryo反序列化对象
	 * @param bytes
	 * @return
	 */
	public static Object unserializeKryo(byte[] bytes) {
//		long beginTime = System.currentTimeMillis();
		Object object = null;
		Input input = null;
		try {
			if (bytes != null && bytes.length > 0) {
				input = new Input(bytes, 0, bytes.length);
				object = kryos.get().readClassAndObject(input);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (input != null) {
				input.close();
			}
		}
//		GlobalConfig.log(ObjectUtils.class, object + " 反序列化用时：" + DateUtils.formatDateTime(System.currentTimeMillis() - beginTime));
		return object;
	}
	
	/**
	 * 克隆一个对象（完全拷贝）
	 * @param source
	 */
	public static Object cloneBean(Object source){
		if (source == null){
			return null;
		}
    	byte[] bytes = ObjectUtils.serializeKryo(source);
    	Object target = ObjectUtils.unserializeKryo(bytes);
	    return target;
	}
	
	/**
	 * 拷贝一个对象（但是子对象无法拷贝）
	 * @param source
	 * @param ignoreProperties
	 */
	public static Object copyBean(Object source, String... ignoreProperties){
		if (source == null){
			return null;
		}
    	Object target = BeanUtils.instantiate(source.getClass());
	    BeanUtils.copyProperties(source, target, ignoreProperties);
	    return target;
	}
	
}
