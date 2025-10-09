/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 * No deletion without permission, or be held responsible to law.
 */
package com.jeesite.common.mapper;

import com.jeesite.common.io.PropertiesUtils;
import org.apache.commons.lang3.LocaleUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

import java.io.IOException;
import java.io.Serial;
import java.util.TimeZone;

/**
 * 简单封装Jackson，实现XML String<->Java Object的Mapper.
 * @author ThinkGem
 * @version 2016-9-2
 */
public class XmlMapper extends com.fasterxml.jackson.dataformat.xml.XmlMapper{
	
	@Serial
	private static final long serialVersionUID = 1L;

	private static final Logger logger = LoggerFactory.getLogger(XmlMapper.class);

	/**
	 * 当前类的实例持有者（静态内部类，延迟加载，懒汉式，线程安全的单例模式）
	 */
	private static final class XmlMapperHolder {
		private static final XmlMapper INSTANCE = new XmlMapper();
	}
	
	/**
	 * 构造方法
	 */
	public XmlMapper() {
		PropertiesUtils props = PropertiesUtils.getInstance();
		// 设置默认语言环境
		props.getPropertyIfNotBlank("lang.defaultLocale", (defaultLocale) -> {
			this.setLocale(LocaleUtils.toLocale(defaultLocale));
		});
		// 设置默认时区
		props.getPropertyIfNotBlank("lang.defaultTimeZone", (defaultTimeZone) -> {
			this.setTimeZone(TimeZone.getTimeZone(defaultTimeZone));
		});
		// Spring ObjectMapper 初始化配置，支持 @JsonView
		new Jackson2ObjectMapperBuilder().configure(this);
	}
	
	/**
	 * Object可以是POJO，也可以是Collection或数组。
	 */
	public String toXmlString(Object object) {
		try {
			return this.writeValueAsString(object);
		} catch (IOException e) {
			logger.warn("write to xml string error: {}", object, e);
			return null;
		}
	}
	
	/**
	 * Object可以是POJO，也可以是Collection或数组。（根据 JsonView 渲染）
	 */
	public String toXmlString(Object object, Class<?> jsonView) {
		try {
			return this.writerWithView(jsonView).writeValueAsString(object);
		} catch (IOException e) {
			logger.warn("write to xml string error: {}", object, e);
			return null;
		}
	}
	
	/**
	 * 反序列化POJO或简单Collection如List<String>.
	 * @see #fromXml(String, Class)
	 */
	public <T> T fromXmlString(String xmlString, Class<T> clazz) {
		if (StringUtils.isEmpty(xmlString) || "<CLOB>".equals(xmlString)) {
			return null;
		}
		try {
			return this.readValue(xmlString, clazz);
		} catch (IOException e) {
			logger.warn("parse xml string error: {}", xmlString, e);
			return null;
		}
	}
	
	/**
	 * 获取当前实例
	 */
	public static XmlMapper getInstance() {
		return XmlMapperHolder.INSTANCE;
	}
	
	/**
	 * 对象转换为XML字符串
	 */
	public static String toXml(Object object){
		return XmlMapper.getInstance().toXmlString(object);
	}
	
	/**
	 * 对象转换为XML字符串（根据 JsonView 渲染）
	 */
	public static String toXml(Object object, Class<?> jsonView){
		return XmlMapper.getInstance().toXmlString(object, jsonView);
	}
	
	/**
	 * XML字符串转换为对象
	 */
	@SuppressWarnings("unchecked")
	public static <T> T fromXml(String jsonString, Class<?> clazz){
		return (T) XmlMapper.getInstance().fromXmlString(jsonString, clazz);
	}
	
//	/**
//	 * xml转map 不带属性
//	 * @param xmlStr
//	 * @param needRootKey 是否需要在返回的map里加根节点键
//	 * @throws DocumentException
//	 */
//	@SuppressWarnings("unchecked")
//	public static Map<String, Object> xmlToMap(String xmlStr, boolean needRootKey) {
//		try {
//			Document doc = DocumentHelper.parseText(xmlStr);
//			Element root = doc.getRootElement();
//			Map<String, Object> map = (Map<String, Object>) xmlToMap(root);
//			if (root.elements().size() == 0 && root.attributes().size() == 0) {
//				return map;
//			}
//			if (needRootKey) {
//				//在返回的map里加根节点键（如果需要）
//				Map<String, Object> rootMap = new HashMap<String, Object>();
//				rootMap.put(root.getName(), map);
//				return rootMap;
//			}
//			return map;
//		} catch (DocumentException e) {
//			e.printStackTrace();
//		}
//		return null;
//	}
//
//	/**
//	 * xml转map 带属性
//	 * @param xmlStr
//	 * @param needRootKey 是否需要在返回的map里加根节点键
//	 * @return
//	 * @throws DocumentException
//	 */
//	@SuppressWarnings("unchecked")
//	public static Map<String, Object> xmlToMapWithAttr(String xmlStr, boolean needRootKey) {
//		try {
//			Document doc = DocumentHelper.parseText(xmlStr);
//			Element root = doc.getRootElement();
//			Map<String, Object> map = (Map<String, Object>) xmlToMapWithAttr(root);
//			if (root.elements().size() == 0 && root.attributes().size() == 0) {
//				return map; //根节点只有一个文本内容
//			}
//			if (needRootKey) {
//				//在返回的map里加根节点键（如果需要）
//				Map<String, Object> rootMap = new HashMap<String, Object>();
//				rootMap.put(root.getName(), map);
//				return rootMap;
//			}
//			return map;
//		} catch (DocumentException e) {
//			e.printStackTrace();
//		}
//		return null;
//	}
//
//	/**
//	 * xml转map 不带属性
//	 * @param element
//	 * @return
//	 */
//	private static Object xmlToMap(Element element) {
//		// System.out.println(element.getName());
//		Map<String, Object> map = new LinkedHashMap<String, Object>();
//		List<Element> elements = element.elements();
//		if (elements.size() == 0) {
//			map.put(element.getName(), element.getText());
//			if (!element.isRootElement()) {
//				return element.getText();
//			}
//		} else if (elements.size() == 1) {
//			map.put(elements.get(0).getName(), xmlToMap(elements.get(0)));
//		} else if (elements.size() > 1) {
//			// 多个子节点的话就得考虑list的情况了，比如多个子节点有节点名称相同的
//			// 构造一个map用来去重
//			Map<String, Element> tempMap = new LinkedHashMap<String, Element>();
//			for (Element ele : elements) {
//				tempMap.put(ele.getName(), ele);
//			}
//			Set<String> keySet = tempMap.keySet();
//			for (String string : keySet) {
//				Namespace namespace = tempMap.get(string).getNamespace();
//				List<Element> elements2 = element.elements(new org.dom4j.QName(string, namespace));
//				// 如果同名的数目大于1则表示要构建list
//				if (elements2.size() > 1) {
//					List<Object> list = new ArrayList<Object>();
//					for (Element ele : elements2) {
//						list.add(xmlToMap(ele));
//					}
//					map.put(string, list);
//				} else {
//					// 同名的数量不大于1则直接递归去
//					map.put(string, xmlToMap(elements2.get(0)));
//				}
//			}
//		}
//
//		return map;
//	}
//
//	/**
//	 * xml转map 带属性
//	 * @param element
//	 * @return
//	 */
//	private static Object xmlToMapWithAttr(Element element) {
//		// System.out.println(element.getName());
//		Map<String, Object> map = new LinkedHashMap<String, Object>();
//		List<Element> elements = element.elements();
//
//		List<Attribute> listAttr = element.attributes(); // 当前节点的所有属性的list
//		boolean hasAttributes = false;
//		for (Attribute attr : listAttr) {
//			hasAttributes = true;
//			map.put("@" + attr.getName(), attr.getValue());
//		}
//
//		if (elements.size() == 0) {
//			// map.put(element.getName(), element.getText());
//			if (hasAttributes) {
//				map.put("#text", element.getText());
//			} else {
//				map.put(element.getName(), element.getText());
//			}
//
//			if (!element.isRootElement()) {
//				// return element.getText();
//				if (!hasAttributes) {
//					return element.getText();
//				}
//			}
//		} else if (elements.size() == 1) {
//			map.put(elements.get(0).getName(), xmlToMapWithAttr(elements.get(0)));
//		} else if (elements.size() > 1) {
//			// 多个子节点的话就得考虑list的情况了，比如多个子节点有节点名称相同的
//			// 构造一个map用来去重
//			Map<String, Element> tempMap = new LinkedHashMap<String, Element>();
//			for (Element ele : elements) {
//				tempMap.put(ele.getName(), ele);
//			}
//			Set<String> keySet = tempMap.keySet();
//			for (String string : keySet) {
//				Namespace namespace = tempMap.get(string).getNamespace();
//				List<Element> elements2 = element.elements(new org.dom4j.QName(string, namespace));
//				// 如果同名的数目大于1则表示要构建list
//				if (elements2.size() > 1) {
//					List<Object> list = new ArrayList<Object>();
//					for (Element ele : elements2) {
//						list.add(xmlToMapWithAttr(ele));
//					}
//					map.put(string, list);
//				} else {
//					// 同名的数量不大于1则直接递归去
//					map.put(string, xmlToMapWithAttr(elements2.get(0)));
//				}
//			}
//		}
//
//		return map;
//	}

}
