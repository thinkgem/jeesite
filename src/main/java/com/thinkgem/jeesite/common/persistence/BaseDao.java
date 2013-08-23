/**
 * Copyright &copy; 2012-2013 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 */
package com.thinkgem.jeesite.common.persistence;

import java.util.List;

import javax.persistence.EntityManager;

import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.Sort;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.search.FullTextSession;
import org.hibernate.transform.ResultTransformer;

/**
 * DAO支持接口
 * @author ThinkGem
 * @version 2013-05-15
 * @param <T>
 */
public interface BaseDao<T> {

	/**
	 * 获取实体工厂管理对象
	 */
	public EntityManager getEntityManager();
	
	/**
	 * 获取 Session
	 */
	public Session getSession();
	
	/**
	 * 强制与数据库同步
	 */
	public void flush();

	/**
	 * 清除缓存数据
	 */
	public void clear();
	
	// -------------- QL Query --------------

	/**
	 * QL 分页查询
	 * @param page
	 * @param qlString
	 * @param parameter
	 * @return
	 */
    public <E> Page<E> find(Page<E> page, String qlString, Object... parameter);
    
    /**
	 * QL 查询
	 * @param qlString
	 * @param parameter
	 * @return
	 */
	public <E> List<E> find(String qlString, Object... parameter);
    
	/**
	 * QL 更新
	 * @param sqlString
	 * @param parameter
	 * @return
	 */
	public int update(String qlString, Object... parameter);
	
	/**
	 * 创建 QL 查询对象
	 * @param qlString
	 * @param parameter
	 * @return
	 */
	public Query createQuery(String qlString, Object... parameter);
	
	// -------------- SQL Query --------------

    /**
	 * SQL 分页查询
	 * @param page
	 * @param qlString
	 * @param parameter
	 * @return
	 */
    public <E> Page<E> findBySql(Page<E> page, String sqlString, Object... parameter);
    
    /**
	 * SQL 分页查询
	 * @param page
	 * @param qlString
	 * @param resultClass
	 * @param parameter
	 * @return
	 */
    public <E> Page<E> findBySql(Page<E> page, String sqlString, Class<?> resultClass, Object... parameter);

	/**
	 * SQL 查询
	 * @param sqlString
	 * @param parameter
	 * @return
	 */
	public <E> List<E> findBySql(String sqlString, Object... parameter);
	
	/**
	 * SQL 查询
	 * @param sqlString
	 * @param resultClass
	 * @param parameter
	 * @return
	 */
	public <E> List<E> findBySql(String sqlString, Class<?> resultClass, Object... parameter);
	
	/**
	 * SQL 更新
	 * @param sqlString
	 * @param parameter
	 * @return
	 */
	public int updateBySql(String sqlString, Object... parameter);
	
	/**
	 * 创建 SQL 查询对象
	 * @param sqlString
	 * @param parameter
	 * @return
	 */
	public Query createSqlQuery(String sqlString, Object... parameter);
	
	// -------------- Criteria --------------
	
	/**
	 * 分页查询
	 * @param page
	 * @return
	 */
	public Page<T> find(Page<T> page);
	
	/**
	 * 使用检索标准对象分页查询
	 * @param page
	 * @param detachedCriteria
	 * @return
	 */
	public Page<T> find(Page<T> page, DetachedCriteria detachedCriteria);
	
	/**
	 * 使用检索标准对象分页查询
	 * @param page
	 * @param detachedCriteria
	 * @param resultTransformer
	 * @return
	 */
	public Page<T> find(Page<T> page, DetachedCriteria detachedCriteria, ResultTransformer resultTransformer);

	/**
	 * 使用检索标准对象查询
	 * @param detachedCriteria
	 * @return
	 */
	public List<T> find(DetachedCriteria detachedCriteria);
	
	/**
	 * 使用检索标准对象查询
	 * @param detachedCriteria
	 * @param resultTransformer
	 * @return
	 */
	public List<T> find(DetachedCriteria detachedCriteria, ResultTransformer resultTransformer);
	
	/**
	 * 使用检索标准对象查询记录数
	 * @param detachedCriteria
	 * @return
	 */
	public long count(DetachedCriteria detachedCriteria);

	/**
	 * 创建与会话无关的检索标准对象
	 * @param criterions Restrictions.eq("name", value);
	 * @return 
	 */
	public DetachedCriteria createDetachedCriteria(Criterion... criterions);
	
	// -------------- Hibernate search --------------
	
	/**
	 * 获取全文Session
	 */
	public FullTextSession getFullTextSession();
	
	/**
	 * 建立索引
	 */
	public void createIndex();
	
	/**
	 * 全文检索
	 * @param page 分页对象
	 * @param query 关键字查询对象
	 * @param queryFilter 查询过滤对象
	 * @param sort 排序对象
	 * @return 分页对象
	 */
	public Page<T> search(Page<T> page, BooleanQuery query, BooleanQuery queryFilter, Sort sort);
	
	/**
	 * 获取全文查询对象
	 */
	public BooleanQuery getFullTextQuery(BooleanClause... booleanClauses);
	
	/**
	 * 获取全文查询对象
	 * @param q 查询关键字
	 * @param fields 查询字段
	 * @return 全文查询对象
	 */
	public BooleanQuery getFullTextQuery(String q, String... fields);
	
	/**
	 * 设置关键字高亮
	 * @param query 查询对象
	 * @param list 设置高亮的内容列表
	 * @param subLength 截取长度
	 * @param fields 字段名
	 */
	public List<T> keywordsHighlight(BooleanQuery query, List<T> list, int subLength, String... fields);
}