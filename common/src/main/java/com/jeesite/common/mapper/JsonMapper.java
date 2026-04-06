/**
 * Copyright (c) 2013-Now https://jeesite.com All rights reserved.
 * No deletion without permission, or be held responsible to law.
 */
package com.jeesite.common.mapper;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.jeesite.common.codec.EncodeUtils;
import com.jeesite.common.collect.ListUtils;
import com.jeesite.common.io.PropertiesUtils;
import com.jeesite.common.lang.DateUtils;
import com.jeesite.common.lang.ObjectUtils;
import com.jeesite.common.lang.StringUtils;
import org.apache.commons.lang3.LocaleUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tools.jackson.core.JacksonException;
import tools.jackson.core.JsonGenerator;
import tools.jackson.core.JsonParser;
import tools.jackson.core.json.JsonFactory;
import tools.jackson.core.json.JsonReadFeature;
import tools.jackson.databind.*;
import tools.jackson.databind.cfg.MapperConfig;
import tools.jackson.databind.introspect.Annotated;
import tools.jackson.databind.introspect.AnnotatedClass;
import tools.jackson.databind.introspect.AnnotatedMethod;
import tools.jackson.databind.introspect.JacksonAnnotationIntrospector;
import tools.jackson.databind.module.SimpleModule;

import java.io.Serial;
import java.lang.reflect.AnnotatedElement;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;
import java.util.function.Consumer;

/**
 * 封装 Jackson，实现 JSON String 与 Java Object 互转
 * @author ThinkGem、jeesite.com
 * @version 2026-04-04
 */
public class JsonMapper extends tools.jackson.databind.json.JsonMapper {

	@Serial
	private static final long serialVersionUID = 1L;
	protected static final Logger logger = LoggerFactory.getLogger(JsonMapper.class);

	public JsonMapper(Builder builder) {
		super(builder);
	}

	/**
	 * 当前类的实例持有者（静态内部类，延迟加载，懒汉式，线程安全的单例模式）
	 */
	private static final class JsonMapperHolder {
		private static final JsonMapper INSTANCE = JsonMapper.builder().build();
	}

	/**
	 * JsonMapper 构建器扩展
	 * @author ThinkGem
	 */
	public static class Builder extends tools.jackson.databind.json.JsonMapper.Builder {

		public Builder() {
			super(new JsonFactory());
			initialize();
		}

		public void initialize() {
			// 日志类型格式化处理
			this.setDefaultLocaleTimeZone();
			// 为Null时不序列化
			this.changeDefaultPropertyInclusion(incl -> incl.withValueInclusion(JsonInclude.Include.NON_NULL));
			// 允许单引号
			this.enable(JsonReadFeature.ALLOW_SINGLE_QUOTES);
			// 允许不带引号的字段名称
			this.enable(JsonReadFeature.ALLOW_UNQUOTED_PROPERTY_NAMES);
			// 设置输入时忽略在JSON字符串中存在但Java对象实际没有的属性
			this.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
			// 设置注解处理器
			this.dateFormatAnnotation();
			// 空值转换为空字符串
			this.enableNullValueToEmpty();
		}

		/**
		 * 日志类型格式化处理
		 * @author ThinkGem
		 */
		public Builder setDefaultLocaleTimeZone() {
			// 设置默认语言环境
			PropertiesUtils props = PropertiesUtils.getInstance();
			props.getPropertyIfNotBlank("lang.defaultLocale", (defaultLocale) -> {
				this.defaultLocale(LocaleUtils.toLocale(defaultLocale));
			});
			// 设置默认时区
			props.getPropertyIfNotBlank("lang.defaultTimeZone", (defaultTimeZone) -> {
				this.defaultTimeZone(TimeZone.getTimeZone(defaultTimeZone));
			});
			return this;
		}

		/**
		 * 日期格式化注解处理
		 * @author ThinkGem
		 */
		public Builder dateFormatAnnotation() {
			this.annotationIntrospector(new JacksonAnnotationIntrospector() {
				@Serial
				private static final long serialVersionUID = 1L;
				private static final String[] pattern = new String[] {"yyyy", "MM", "dd", "HH", "mm", "ss", "SSS"};

				@Override
				public Object findSerializer(MapperConfig<?> config, Annotated a) {
					if (a instanceof AnnotatedMethod am) {
						if (DateUtils.isDateType(am.getRawReturnType())) {
							JsonFormat jf = am.getAnnotation(JsonFormat.class);
							if (jf != null && StringUtils.containsAnyIgnoreCase(jf.pattern(), pattern)) {
								return new JeeSiteDateJsonSerializer(jf.pattern());
							}
							return new JeeSiteDateJsonSerializer(null);
						}
					} else if (a instanceof AnnotatedClass) {
						if (DateUtils.isDateType(a.getRawType())) {
							return new JeeSiteDateJsonSerializer(null);
						}
					}
					return super.findSerializer(config, a);
				}

				@Override
				public Object findDeserializer(MapperConfig<?> config, Annotated a) {
					if (a instanceof AnnotatedMethod am) {
						if (am.getParameterCount() > 0 && DateUtils.isDateType(am.getParameterType(0).getRawClass())) {
							AnnotatedElement m = am.getAnnotated();
							JsonFormat jf = m.getAnnotation(JsonFormat.class);
							if (jf != null && StringUtils.containsAnyIgnoreCase(jf.pattern(), pattern)) {
								return new JeeSiteDateJsonDeserializer(jf.pattern());
							}
							return new JeeSiteDateJsonDeserializer(null);
						}
					} else if (a instanceof AnnotatedClass) {
						if (DateUtils.isDateType(a.getRawType())) {
							return new JeeSiteDateJsonDeserializer(null);
						}
					}
					return super.findDeserializer(config, a);
				}
			});
			return this;
		}

		/**
		 * 日期时间序列化处理
		 * @author ThinkGem
		 */
		public static final class JeeSiteDateJsonSerializer extends ValueSerializer<Object> {
			private final String pattern;
			public JeeSiteDateJsonSerializer(String pattern) {
				this.pattern = pattern;
			}
			@Override
			public void serialize(Object dateObj, JsonGenerator gen, SerializationContext context) {
				Date value = ObjectUtils.toDate(dateObj);
				if (value != null) {
					if (StringUtils.isNotBlank(pattern)) {
						gen.writeString(DateUtils.formatDate(value, pattern));
					} else {
						gen.writeString(DateUtils.formatDateTime(value));
					}
				} else {
					gen.writeString("");
				}
			}
		}

		/**
		 * 日期时间反序列化处理
		 * @author ThinkGem
		 */
		public static final class JeeSiteDateJsonDeserializer extends ValueDeserializer<Object> {
			private final String pattern;
			public JeeSiteDateJsonDeserializer(String pattern) {
				this.pattern = pattern;
			}
			@Override
			public Date deserialize(JsonParser parser, DeserializationContext ctx) {
				if (StringUtils.isNotBlank(pattern)) {
					return DateUtils.parseDate(parser.getString(), pattern);
				} else {
					return DateUtils.parseDate(parser.getString());
				}
			}
		}

		/**
		 * 空值转换为空字符串
		 * @author ThinkGem
		 */
		public Builder enableNullValueToEmpty() {
			this.serializerFactory().withNullValueSerializer(new ValueSerializer<Object>() {
				@Override
				public void serialize(Object value, JsonGenerator jgen, SerializationContext context) {
					jgen.writeString(StringUtils.EMPTY);
				}
			});
			return this;
		}

		/**
		 * 开启 XSS 过滤器
		 * @author ThinkGem
		 */
		public Builder enableXssFilter() {
			this.addModule(new SimpleModule().addDeserializer(String.class, new ValueDeserializer<>() {
				@Override
				public String deserialize(JsonParser p, DeserializationContext ctx) {
					String text = p.getString();
					if (text != null) {
						return EncodeUtils.xssFilter(text);
					}
					return null;
				}
			}));
			return this;
		}

		@Override
		public JsonMapper build() {
			return new JsonMapper(this);
		}
	}

	/**
	 * 创建 JsonMapper 构建器
	 * @author ThinkGem
	 */
	public static Builder builder() {
		return new Builder();
	}

	/**
	 * 创建 JsonMapper 构建器
	 * @author ThinkGem
	 */
	public static Builder builder(Consumer<Builder> consumer) {
		Builder b = new Builder();
		consumer.accept(b);
		return b;
	}

	/**
	 * Object可以是POJO，也可以是Collection或数组。
	 */
	public String toJsonString(Object object) {
		try {
			return this.writeValueAsString(object);
		} catch (JacksonException e) {
			logger.warn("write to json string error: {}", object, e);
			return null;
		}
	}
	
	/**
	 * Object可以是POJO，也可以是Collection或数组。（根据 JsonView 渲染）
	 */
	public String toJsonString(Object object, Class<?> jsonView) {
		try {
			return this.writerWithView(jsonView).writeValueAsString(object);
		} catch (JacksonException e) {
			logger.warn("write to json string error: {}", object, e);
			return null;
		}
	}

//	/**
//	 * 输出JSONP格式数据.
//	 */
//	public String toJsonpString(String functionName, Object object) {
//		return toJsonString(new JSONPObject(functionName, object));
//	}
	
	/**
	 * 反序列化POJO或简单Collection如List<String>.
	 * 如需反序列化复杂Collection如List<MyBean>, 请使用fromJson(String, Class)
	 * @see #fromJson(String, Class)
	 */
	public <T> T fromJsonString(String jsonString, Class<T> clazz) {
		if (StringUtils.isEmpty(jsonString) || "<CLOB>".equals(jsonString)) {
			return null;
		}
		try {
			return this.readValue(jsonString, clazz);
		} catch (JacksonException e) {
			logger.warn("parse json string error: {}", jsonString, e);
			return null;
		}
	}

	/**
	 * 反序列化复杂Collection如List<Bean>, 先使用函数createCollectionType构造类型,然后调用本函数.
	 * @see #createCollectionType(Class, Class...)
	 */
	@SuppressWarnings("unchecked")
	public <T> T fromJsonString(String jsonString, JavaType javaType) {
		if (StringUtils.isEmpty(jsonString) || "<CLOB>".equals(jsonString)) {
			return null;
		}
		try {
			return (T) this.readValue(jsonString, javaType);
		} catch (JacksonException e) {
			logger.warn("parse json string error: {}", jsonString, e);
			return null;
		}
	}

	/**
	 * 构造泛型的Collection Type如:
	 * ArrayList<MyBean>, 则调用constructCollectionType(ArrayList.class,MyBean.class)
	 * HashMap<String,MyBean>, 则调用(HashMap.class,String.class, MyBean.class)
	 */
	public JavaType createCollectionType(Class<?> collectionClass, Class<?>... elementClasses) {
		return this.getTypeFactory().constructParametricType(collectionClass, elementClasses);
	}

	/**
	 * 当JSON里只含有Bean的部分属性時，更新一个已存在Bean，只覆盖该部分的属性.
	 */
	@SuppressWarnings({ "unchecked" })
	public <T> T update(String jsonString, T object) {
		try {
			return (T) this.readerForUpdating(object).readValue(jsonString);
		} catch (JacksonException e) {
			logger.warn("update json string: {} to object: {} error.", jsonString, object, e);
		}
		return null;
	}

	/**
	 * 获取当前实例
	 */
	public static JsonMapper getInstance() {
		return JsonMapperHolder.INSTANCE;
	}

	/**
	 * 对象转换为JSON字符串
	 */
	public static String toJson(Object object){
		return JsonMapper.getInstance().toJsonString(object);
	}
	
	/**
	 * 对象转换为JSON字符串（根据 JsonView 渲染）
	 */
	public static String toJson(Object object, Class<?> jsonView){
		return JsonMapper.getInstance().toJsonString(object, jsonView);
	}
	
//	/**
//	 * 对象转换为JSONP字符串
//	 */
//	public static String toJsonp(String functionName, Object object){
//		return JsonMapper.getInstance().toJsonpString(functionName, object);
//	}
	
	/**
	 * JSON字符串转换为对象
	 */
	@SuppressWarnings("unchecked")
	public static <T> T fromJson(String jsonString, Class<?> clazz){
		return (T) JsonMapper.getInstance().fromJsonString(jsonString, clazz);
	}
	
	/**
	 * JSON字符串转换为 List<Map<String, Object>>
	 */
	public static List<Map<String, Object>> fromJsonForMapList(String jsonString){
		List<Map<String, Object>> result = ListUtils.newArrayList();
		if (StringUtils.startsWith(jsonString, "{")){
			Map<String, Object> map = fromJson(jsonString, Map.class);
			if (map != null){
				result.add(map);
			}
		}else if (StringUtils.startsWith(jsonString, "[")){
			List<Map<String, Object>> list = fromJson(jsonString, List.class);
			if (list != null){
				result = list;
			}
		}
		return result;
	}

}