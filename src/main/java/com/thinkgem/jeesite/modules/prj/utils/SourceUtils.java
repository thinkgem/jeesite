package com.thinkgem.jeesite.modules.prj.utils;

import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.stereotype.Repository;

import com.google.common.base.Objects;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.thinkgem.jeesite.common.persistence.BaseDao;
import com.thinkgem.jeesite.common.persistence.Parameter;
import com.thinkgem.jeesite.common.utils.SpringContextHolder;
import com.thinkgem.jeesite.modules.sys.utils.UserUtils;

public class SourceUtils {
	public static String SOURCE_TYPE_ENTITY = "entity";
	public static String SOURCE_TYPE_DAO = "dao";
	public static String SOURCE_TYPE_SERVICE = "service";
	public static String SOURCE_TYPE_CONTROLLER = "controller";
	public static String SOURCE_TYPE_VIEW= "view";
	public static String SOURCE_TYPE_VIEW_MOBILE= "viewMobile";
	
	public static String TEMPLATE_TYPE_ENTITY_AND_DAO = "entityAndDao";
	public static String TEMPLATE_TYPE_SINGLE = "single";
	
	public static String COMMON_COLUMNS = "remarks,create_by,create_date,update_by,update_date,del_flag";

	public static Set<String> getCommonColumns(){
		Set<String> set = Sets.newHashSet();
		for(String column:COMMON_COLUMNS.split(",")) {
			set.add(column);
		}
		return set;
	}
	public static List<String> getImportList(String sourceType) {
		List<String> importList = Lists.newArrayList();
		if(SOURCE_TYPE_ENTITY.equals(sourceType)) {
			importList.add(Id.class.getCanonicalName());
			importList.add(Entity.class.getCanonicalName());
			importList.add(Table.class.getCanonicalName());
			importList.add(DynamicInsert.class.getCanonicalName());
			importList.add(DynamicUpdate.class.getCanonicalName());
			importList.add(GeneratedValue.class.getCanonicalName());
			importList.add(Objects.class.getCanonicalName());
		} else if (SOURCE_TYPE_DAO.equals(sourceType)) {
			importList.add(Repository.class.getCanonicalName());
			importList.add(BaseDao.class.getCanonicalName());
			importList.add(Parameter.class.getCanonicalName());
			importList.add(List.class.getCanonicalName());
		}
		
		return importList;
	}
	
	public static Map<String, String> getFreemarkerMap() {
		Map<String, String> fm = Maps.newHashMap();
		fm.put("at", "@");
		fm.put("dollar", "$");
		fm.put("well", "#");
		return fm;
	}
	
	public static String getTemplatePath(String templateType) {
		return SpringContextHolder.getResourceRootRealPath() + "\\templates\\" + templateType ;
	}
	
	public static String getGenerateRootPath() {
		return SpringContextHolder.getRootRealPath() + "\\userfiles\\" + UserUtils.getUser().getId() + "\\files\\generate\\" + System.currentTimeMillis();
	}
}
