/**
 * Copyright &copy; 2012-2013 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 */
package com.thinkgem.jeesite.modules.sys.utils;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import com.google.common.collect.Lists;
import com.thinkgem.jeesite.common.utils.CacheUtils;
import com.thinkgem.jeesite.modules.sys.dao.DictDao;
import com.thinkgem.jeesite.modules.sys.entity.Dict;

/**
 * 字典工具类
 * @author ThinkGem
 * @version 2013-01-15
 */
@Component
public class DictUtils implements ApplicationContextAware {

	private static DictDao dictDao;
	
	public static String getDictLabel(String value, String type, String defaultValue){
		if (StringUtils.isNotBlank(type) && StringUtils.isNotBlank(value)){
			for (Dict dict : getDictList()){
				if (type.equals(dict.getType()) && value.equals(dict.getValue())){
					return dict.getLabel();
				}
			}
		}
		return defaultValue;
	}
	
	public static List<Dict> getDictList(String type){
		List<Dict> list = Lists.newArrayList();
		if (StringUtils.isNotBlank(type)){
			for (Dict dict : getDictList()){
				if (type.equals(dict.getType())){
					list.add(dict);
				}
			}
		}
		return list;
	}
	
	public static List<Dict> getDictList() {
		@SuppressWarnings("unchecked")
		List<Dict> dictList = (List<Dict>)CacheUtils.get("dictList");
		if (dictList==null){
			dictList = dictDao.findAllList();
			CacheUtils.put("dictList", dictList);
		}
		return dictList;
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext){
		dictDao = (DictDao)applicationContext.getBean("dictDao");
	}
}
