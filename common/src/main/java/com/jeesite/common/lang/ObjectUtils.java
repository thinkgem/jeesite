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
import org.nustaq.serialization.FSTConfiguration;
import org.springframework.beans.BeanUtils;

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
		if (object == null){
			return null;
		}
		long beginTime = System.currentTimeMillis();
		byte[] bytes = null;
		ObjectOutputStream oos = null;
		ByteArrayOutputStream baos = null;
		try {
			baos = new ByteArrayOutputStream();
			oos = new ObjectOutputStream(baos);
			oos.writeObject(object);
			bytes = baos.toByteArray();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			IOUtils.closeQuietly(oos);
			IOUtils.closeQuietly(baos);
		}
		long totalTime = System.currentTimeMillis() - beginTime;
		if (totalTime > 3000){
			System.out.println("Serialize time: " + TimeUtils.formatDateAgo(totalTime));
		}
		return bytes;
	}

	/**
	 * 反序列化对象
	 * @param bytes
	 * @return
	 */
	public static Object unserialize(byte[] bytes) {
		if (bytes == null){
			return null;
		}
		long beginTime = System.currentTimeMillis();
		Object object = null;
		ByteArrayInputStream bais = null;
		ObjectInputStream ois = null;
		try {
			if (bytes.length > 0) {
				bais = new ByteArrayInputStream(bytes);
				ois = new ObjectInputStream(bais);
				object = ois.readObject();
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			IOUtils.closeQuietly(ois);
			IOUtils.closeQuietly(bais);
		}
		long totalTime = System.currentTimeMillis() - beginTime;
		if (totalTime > 3000){
			System.out.println("Unserialize time: " + TimeUtils.formatDateAgo(totalTime));
		}
		return object;
	}

//	// Kryo不是线程安全的，所以要建立一个线程变量，每一个线程实例化一次
//	public static final ThreadLocal<Kryo> kryos = new ThreadLocal<Kryo>() {
//		@Override
//		protected Kryo initialValue() {
//			Kryo kryo = new Kryo();
//			// 设置false关闭注册行为， Kryo支持对注册行为，如kryo.register(SomeClazz.class);
//			// 这会赋予该Class一个从0开始的编号，但Kryo使用注册行为最大的问题在于，
//			// 其不保证同一个Class每一次注册的号码想用，这与注册的顺序有关，也就意味着在不同的机器、
//			// 同一个机器重启前后都有可能拥有不同的编号，这会导致序列化产生问题，所以在分布式项目中，一般关闭注册行为。
//			kryo.setRegistrationRequired(false);
//			// 支持循环引用
//			kryo.setReferences(true);
//			return kryo;
//		};
//	};
//
//	/**
//	 * Kryo序列化对象
//	 * @param object
//	 * @return
//	 */
//	public static byte[] serializeKryo(Object object) {
//		byte[] bytes = null;
//		Output output = null;
//		try {
//			if (object != null) {
//				output = new Output(1024, -1);
//				kryos.get().writeClassAndObject(output, object);
//				bytes = output.toBytes();
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//		} finally {
//			if (output != null) {
//				output.close();
//			}
//		}
//		return bytes;
//	}
//
//	/**
//	 * Kryo反序列化对象
//	 * @param bytes
//	 * @return
//	 */
//	public static Object unserializeKryo(byte[] bytes) {
//		Object object = null;
//		Input input = null;
//		try {
//			if (bytes != null && bytes.length > 0) {
//				input = new Input(bytes, 0, bytes.length);
//				object = kryos.get().readClassAndObject(input);
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//		} finally {
//			if (input != null) {
//				input.close();
//			}
//		}
//		return object;
//	}
	
	// FST序列化配置对象
	private static FSTConfiguration fst = FSTConfiguration.createDefaultConfiguration();
	
	/**
	 * FST 序列化对象
	 * @param object
	 * @return
	 */
	public static byte[] serializeFst(Object object) {
		if (object == null){
			return null;
		}
		long beginTime = System.currentTimeMillis();
		byte[] bytes = fst.asByteArray(object);
		long totalTime = System.currentTimeMillis() - beginTime;
		if (totalTime > 3000){
			System.out.println("Fst serialize time: " + TimeUtils.formatDateAgo(totalTime));
		}
		return bytes;
	}

	/**
	 * FST 反序列化对象
	 * @param bytes
	 * @return
	 */
	public static Object unserializeFst(byte[] bytes) {
		if (bytes == null){
			return null;
		}
		long beginTime = System.currentTimeMillis();
		Object object = fst.asObject(bytes);
		long totalTime = System.currentTimeMillis() - beginTime;
		if (totalTime > 3000){
			System.out.println("Fst unserialize time: " + TimeUtils.formatDateAgo(totalTime));
		}
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
    	byte[] bytes = ObjectUtils.serializeFst(source);
    	Object target = ObjectUtils.unserializeFst(bytes);
	    return target;
	}
	
}
