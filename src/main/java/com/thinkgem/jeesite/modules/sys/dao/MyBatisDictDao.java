/**
 * Copyright &copy; 2012-2013 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 */
package com.thinkgem.jeesite.modules.sys.dao;

import java.util.List;

import com.thinkgem.jeesite.common.persistence.MyBatisRepository;
import com.thinkgem.jeesite.modules.sys.entity.Dict;

/**
 * MyBatis字典DAO接口
 * @author ThinkGem
 * @version 2013-8-23
 */
@MyBatisRepository
public interface MyBatisDictDao {
	
    Dict get(Long id);
    
    List<Dict> findAll();
    
}
