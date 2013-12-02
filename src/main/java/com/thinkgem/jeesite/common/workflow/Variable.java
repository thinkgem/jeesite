package com.thinkgem.jeesite.common.workflow;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.beanutils.ConvertUtils;

import com.thinkgem.jeesite.common.utils.StringUtils;

public class Variable {

	private String keys;
	private String values;
	private String types;

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

	public Map<String, Object> getVariableMap() {
		Map<String, Object> vars = new HashMap<String, Object>();

		ConvertUtils.register(new DateConverter(), java.util.Date.class);

		if (StringUtils.isBlank(keys)) {
			return vars;
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
			vars.put(key, objectValue);
		}
		return vars;
	}

}
