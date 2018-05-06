/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.act.utils;

import java.util.Map;

import org.apache.commons.beanutils.ConvertUtils;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.common.collect.Maps;
import com.thinkgem.jeesite.common.utils.StringUtils;

/**
 * 流程变量对象
 * @author ThinkGem
 * @version 2013-11-03
 */
public class Variable {

	private Map<String, Object> map = Maps.newHashMap();
	
	private String keys;
	private String values;
	private String types;

	public Variable (){
		
	}
	
	public Variable (Map<String, Object> map){
		this.map = map;
	}

	public String getKeys() {
		return keys;
	}

	public void setKeys(String keys) {
		this.keys = keys;
	}

	public String getValues() {
		return values;
	}

	public void setValues(String values) {
		this.values = values;
	}

	public String getTypes() {
		return types;
	}

	public void setTypes(String types) {
		this.types = types;
	}

	@JsonIgnore
	public Map<String, Object> getVariableMap() {

		ConvertUtils.register(new DateConverter(), java.util.Date.class);

		if (StringUtils.isBlank(keys)) {
			return map;
		}

		String[] arrayKey = keys.split(",");
		String[] arrayValue = values.split(",");
		String[] arrayType = types.split(",");
		for (int i = 0; i < arrayKey.length; i++) {
			String key = arrayKey[i];
			String value = arrayValue[i];
			String type = arrayType[i];

			Class<?> targetType = Enum.valueOf(PropertyType.class, type).getValue();
			Object objectValue = ConvertUtils.convert(value, targetType);
			map.put(key, objectValue);
		}
		return map;
	}

	public Map<String, Object> getMap() {
		return map;
	}

}
