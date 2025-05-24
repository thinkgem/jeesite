/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 * No deletion without permission, or be held responsible to law.
 */
package com.jeesite.common.lang;

import com.jeesite.common.io.PropertiesUtils;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.fury.Fury;
import org.apache.fury.ThreadSafeFury;
import org.apache.fury.config.Language;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.InvocationTargetException;

/**
 * 对象操作工具类，继承 org.apache.commons.lang3.ObjectUtils 类
 * @author ThinkGem
 * @version 2020-3-29
 */
public class ObjectUtils extends org.apache.commons.lang3.ObjectUtils {

	private static final Logger logger = LoggerFactory.getLogger(ObjectUtils.class);

	/**
	 * 当前类的实例持有者（静态内部类，延迟加载，懒汉式，线程安全的单例模式）
	 */
	private static final class Static {
		private static final Boolean isJavaSerialize;
		private static final ThreadSafeFury fury;
		static {
			isJavaSerialize = PropertiesUtils.getInstance()
					.getPropertyToBoolean("isJavaSerialize", "false");
			org.apache.fury.logging.LoggerFactory.useSlf4jLogging(true);
			fury = Fury.builder().withLanguage(Language.JAVA)
                .withRefTracking(true)
                .requireClassRegistration(false)
                .buildThreadSafeFury();
		}
	}

	/**
	 * 转换为 Double 类型
	 */
	public static Double toDouble(final Object val) {
		if (val == null) {
			return 0D;
		}
		try {
			String str = val.toString();
			if (StringUtils.isBlank(str)) {
				return 0D;
			}
			if (StringUtils.contains(str, "*")) {
				Double number = null, d = null;
				for (String s : StringUtils.split(str, "*")) {
					d = Double.parseDouble(StringUtils.trim(s));
					if (number == null) {
						number = d;
					} else {
						number *= d;
					}
				}
				return number;
			}
			return Double.parseDouble(StringUtils.trim(str));
		} catch (Exception e) {
			return 0D;
		}
	}

	/**
	 * 转换为 Float 类型
	 */
	public static Float toFloat(final Object val) {
		return toDouble(val).floatValue();
	}

	/**
	 * 转换为 Long 类型
	 */
	public static Long toLong(final Object val) {
		if (val == null) {
			return 0L;
		}
		try {
			String str = val.toString();
			if (StringUtils.isBlank(str)) {
				return 0L;
			}
			if (StringUtils.contains(str, "*")) {
				Long number = null, d = null;
				for (String s : StringUtils.split(str, "*")) {
					d = Long.parseLong(StringUtils.trim(s));
					if (number == null) {
						number = d;
					} else {
						number *= d;
					}
				}
				return number;
			}
			return Long.parseLong(StringUtils.trim(str));
		} catch (Exception e) {
			return 0L;
		}
	}

	/**
	 * 转换为 Integer 类型
	 */
	public static Integer toInteger(final Object val) {
		return toLong(val).intValue();
	}

	/**
	 * 转换为 Boolean 类型 'true', 'on', 'y', 't', 'yes' or '1'
	 *  (case insensitive) will return true. Otherwise, false is returned.
	 */
	public static Boolean toBoolean(final Object val) {
		if (val == null) {
			return false;
		}
		return BooleanUtils.toBoolean(val.toString()) || "1".equals(val.toString());
	}

	/**
	 * 转换为字符串
	 */
	public static String toString(final Object obj) {
		return toString(obj, StringUtils.EMPTY);
	}

	/**
	 * 转换为字符串，如果对象为空，则使用 defaultVal 值
	 */
	public static String toString(final Object obj, final String defaultVal) {
		return obj == null ? defaultVal : obj.toString();
	}

	/**
	 * 转换为字符串，忽略空值。如 null 字符串，则被认为空值，如下：
	 * 		"" to "" ; null to "" ; "null" to "" ; "NULL" to "" ; "Null" to ""
	 */
	public static String toStringIgnoreNull(final Object val) {
		return ObjectUtils.toStringIgnoreNull(val, StringUtils.EMPTY);
	}

	/**
	 * 转换为字符串，忽略空值。如 null 字符串，则被认为空值，如下：
	 * 		"" to defaultVal ; null to defaultVal ; "null" to defaultVal ; "NULL" to defaultVal ; "Null" to defaultVal
	 */
	public static String toStringIgnoreNull(final Object val, String defaultVal) {
		String str = ObjectUtils.toString(val);
		return !"".equals(str) && !"null".equalsIgnoreCase(str.trim()) ? str : defaultVal;
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
		try {
			Object target = source.getClass().getDeclaredConstructor().newInstance();
			BeanUtils.copyProperties(source, target, ignoreProperties);
			return target;
		} catch (InstantiationException | IllegalAccessException | IllegalArgumentException
				| InvocationTargetException | NoSuchMethodException | SecurityException e) {
			throw ExceptionUtils.unchecked(e);
		}
	}
	
	/**
	 * 克隆一个对象（完全拷贝）
	 * @param source
	 */
	public static Object cloneBean(Object source){
		if (source == null){
			return null;
		}
    	byte[] bytes = ObjectUtils.serialize(source);
		return ObjectUtils.unserialize(bytes);
	}
	
	/**
	 * 序列化对象
	 * @param object
	 * @return
	 */
	public static byte[] serialize(Object object) {
		try {
			if (Static.isJavaSerialize) {
				return ObjectUtils.serializeJava(object);
			} else {
				return ObjectUtils.serializeFury(object);
			}
		} catch (Exception e) {
			logger.error("serialize: {}", e.getMessage());
		}
		return null;
	}

	/**
	 * 反序列化对象
	 * @param bytes
	 * @return
	 */
	public static Object unserialize(byte[] bytes) {
		try {
			if (Static.isJavaSerialize) {
				return ObjectUtils.unserializeJava(bytes);
			} else {
				return ObjectUtils.unserializeFury(bytes);
			}
		} catch (Exception e) {
			logger.error("unserialize: {}", e.getMessage());
		}
		return null;
	}
	
	/**
	 * 序列化对象
	 * @param object
	 * @return
	 */
	public static byte[] serializeJava(Object object) {
		if (object == null){
			return null;
		}
		long beginTime = System.currentTimeMillis();
		byte[] bytes = null;
		try (ByteArrayOutputStream baos = new ByteArrayOutputStream();
				ObjectOutputStream oos = new ObjectOutputStream(baos);) {
			oos.writeObject(object);
			bytes = baos.toByteArray();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		long totalTime = System.currentTimeMillis() - beginTime;
		if (totalTime > 30000){
			logger.warn(object.getClass() + " serialize time: " + TimeUtils.formatTime(totalTime));
		}
		return bytes;
	}

	/**
	 * 反序列化对象
	 * @param bytes
	 * @return
	 */
	public static Object unserializeJava(byte[] bytes) {
		if (bytes == null){
			return null;
		}
		long beginTime = System.currentTimeMillis();
		Object object = null;
		if (bytes.length > 0) {
			try (ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
					ObjectInputStream ois = new ObjectInputStream(bais);) {
				object = ois.readObject();
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}
		long totalTime = System.currentTimeMillis() - beginTime;
		if (totalTime > 30000 && object != null){
			logger.warn(object.getClass() + " unserialize time: " + TimeUtils.formatTime(totalTime));
		}
		return object;
	}


	/**
	 * 序列化对象
	 * @param object
	 * @return
	 */
	public static byte[] serializeFury(Object object) {
		if (object == null){
			return null;
		}
		long beginTime = System.currentTimeMillis();
		byte[] bytes = Static.fury.serialize(object);
		long totalTime = System.currentTimeMillis() - beginTime;
		if (totalTime > 30000){
			logger.warn(object.getClass() + " serialize time: " + TimeUtils.formatTime(totalTime));
		}
		return bytes;
	}

	/**
	 * 反序列化对象
	 * @param bytes
	 * @return
	 */
	public static Object unserializeFury(byte[] bytes) {
		if (bytes == null){
			return null;
		}
		long beginTime = System.currentTimeMillis();
		Object object = Static.fury.deserialize(bytes);
		long totalTime = System.currentTimeMillis() - beginTime;
		if (totalTime > 30000 && object != null){
			logger.warn(object.getClass() + " unserialize time: " + TimeUtils.formatTime(totalTime));
		}
		return object;
	}

//	private static ThreadLocal<FSTConfiguration> fstConfiguration =
//				new NamedThreadLocal<FSTConfiguration>("FSTConfiguration") {
//		@Override
//		public FSTConfiguration initialValue() {
//			return FSTConfiguration.createDefaultConfiguration();
//		}
//	};
//
//	/**
//	 * FST 序列化对象
//	 * @param object
//	 * @return
//	 */
//	public static byte[] serializeFst(Object object) {
//		if (object == null){
//			return null;
//		}
//		long beginTime = System.currentTimeMillis();
//		byte[] bytes = fstConfiguration.get().asByteArray(object);
//		long totalTime = System.currentTimeMillis() - beginTime;
//		if (totalTime > 30000){
//			logger.warn(object.getClass() + " fst serialize time: " + TimeUtils.formatTime(totalTime));
//		}
//		return bytes;
//	}
//
//	/**
//	 * FST 反序列化对象
//	 * @param bytes
//	 * @return
//	 */
//	public static Object unserializeFst(byte[] bytes) {
//		if (bytes == null){
//			return null;
//		}
//		long beginTime = System.currentTimeMillis();
//		Object object = fstConfiguration.get().asObject(bytes);
//		long totalTime = System.currentTimeMillis() - beginTime;
//		if (totalTime > 30000 && object != null){
//			logger.warn(object.getClass() + " fst unserialize time: " + TimeUtils.formatTime(totalTime));
//		}
//		return object;
//	}

//	private static Pool<Kryo> kryoPool = new Pool<Kryo>(true, false, 8) {
//		protected Kryo create() {
//			Kryo kryo = new Kryo();
//			kryo.setRegistrationRequired(false);
//			kryo.setReferences(false);
//			return kryo;
//		}
//	};
//	private static Pool<Output> outputPool = new Pool<Output>(true, false, 16) {
//		protected Output create() {
//			return new Output(1024, -1);
//		}
//	};
//	private static Pool<Input> inputPool = new Pool<Input>(true, false, 16) {
//		protected Input create() {
//			return new Input(1024);
//		}
//	};
//
//	/**
//	 * Kryo 序列化对象
//	 * @param object
//	 * @return
//	 */
//	public static byte[] serializeKryo(Object object) {
//		Kryo kryo = kryoPool.obtain();
//		Output output = outputPool.obtain();
//		try {
//			output.reset();
//			kryo.writeClassAndObject(output, object);
//			return output.toBytes();
//		} finally {
//			kryoPool.free(kryo);
//			outputPool.free(output);
//		}
//	}
//
//	/**
//	 * Kryo 反序列化对象
//	 * @param bytes
//	 * @return
//	 */
//	public static Object unserializeKryo(byte[] bytes) {
//		Kryo kryo = kryoPool.obtain();
//		Input input = inputPool.obtain();
//		try {
//			input.setBuffer(bytes);
//			return kryo.readClassAndObject(input);
//		} finally {
//			kryoPool.free(kryo);
//			inputPool.free(input);
//		}
//	}

	@Deprecated
	public ObjectUtils() {
		// empty
	}
}
