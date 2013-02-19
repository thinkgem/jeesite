/**
 * Copyright &copy; 2012-2013 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 */
package com.thinkgem.jeesite.modules.sys.dao;

import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Component;

import com.thinkgem.jeesite.common.persistence.BaseDao;
import com.thinkgem.jeesite.common.persistence.BaseDaoImpl;
import com.thinkgem.jeesite.modules.sys.entity.Dict;

/**
 * 字典DAO接口
 * @author ThinkGem
 * @version 2013-01-15
 */
public interface DictDao extends DictDaoCustom, CrudRepository<Dict, Long> {
	
	@Modifying
	@Query("update Dict set delFlag='" + Dict.DEL_FLAG_DELETE + "' where id = ?1")
	public int deleteById(Long id);

	@Query("from Dict where delFlag='" + Dict.DEL_FLAG_NORMAL + "' order by sort")
	public List<Dict> findAllList();

	@Query("select type from Dict where delFlag='" + Dict.DEL_FLAG_NORMAL + "' group by type")
	public List<String> findTypeList();
	
//	@Query("from Dict where delFlag='" + Dict.DEL_FLAG_NORMAL + "' and type=?1 order by sort")
//	public List<Dict> findByType(String type);
//	
//	@Query("select label from Dict where delFlag='" + Dict.DEL_FLAG_NORMAL + "' and value=?1 and type=?2")
//	public List<String> findValueByValueAndType(String value, String type);
	
}

/**
 * DAO自定义接口
 * @author ThinkGem
 */
interface DictDaoCustom extends BaseDao<Dict> {

}

/**
 * DAO自定义接口实现
 * @author ThinkGem
 */
@Component
class DictDaoImpl extends BaseDaoImpl<Dict> implements DictDaoCustom {

}
