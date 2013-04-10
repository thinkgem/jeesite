/**
 * Copyright &copy; 2012-2013 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 */
package com.thinkgem.jeesite.common.persistence;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.QueryWrapperFilter;
import org.apache.lucene.search.Sort;
import org.apache.lucene.search.BooleanClause.Occur;
import org.apache.lucene.search.highlight.Formatter;
import org.apache.lucene.search.highlight.Highlighter;
import org.apache.lucene.search.highlight.InvalidTokenOffsetsException;
import org.apache.lucene.search.highlight.QueryScorer;
import org.apache.lucene.search.highlight.SimpleFragmenter;
import org.apache.lucene.search.highlight.SimpleHTMLFormatter;
import org.apache.lucene.util.Version;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.internal.CriteriaImpl;
import org.hibernate.search.FullTextQuery;
import org.hibernate.search.FullTextSession;
import org.hibernate.search.Search;
import org.hibernate.search.filter.impl.CachingWrapperFilter;
import org.hibernate.search.query.DatabaseRetrievalMethod;
import org.hibernate.search.query.ObjectLookupMethod;
import org.wltea.analyzer.lucene.IKAnalyzer;

import com.thinkgem.jeesite.common.utils.Reflections;
import com.thinkgem.jeesite.common.utils.StringUtils;
import com.thinkgem.jeesite.modules.cms.entity.Article;

/**
 * DAO支持类实现
 * @author ThinkGem
 * @version 2013-01-15
 * @param <T>
 */
public class BaseDaoImpl<T> implements BaseDao<T> {

	/**
	 * 获取实体工厂管理对象
	 */
	@PersistenceContext
	private EntityManager entityManager;

	/**
	 * 实体类类型(由构造方法自动赋值)
	 */
	private Class<?> entityClass;
	
	/**
	 * 构造方法，根据实例类自动获取实体类类型
	 */
	public BaseDaoImpl() {
		entityClass = Reflections.getClassGenricType(getClass());
	}

	/**
	 * 获取实体工厂管理对象
	 */
	public EntityManager getEntityManager() {
		return entityManager;
	}

	/**
	 * 获取 Session
	 */
	public Session getSession(){  
	  return (Session) getEntityManager().getDelegate();
	}

	/**
	 * 强制与数据库同步
	 */
	public void flush(){
		getSession().flush();
	}

	/**
	 * 清除缓存数据
	 */
	public void clear(){ 
		getSession().clear();
	}
	
	// -------------- QL Query --------------

	/**
	 * QL 分页查询
	 * @param page
	 * @param qlString
	 * @param parameter
	 * @return
	 */
    @SuppressWarnings("unchecked")
	public Page<T> find(Page<T> page, String qlString, Object... parameter){
		// get count
    	if (!page.isDisabled() && !page.isNotCount()){
	        String countQlString = "select count(*)" + removeSelect(removeOrders(qlString));  
	        page.setCount((Long)createQuery(countQlString, parameter).getSingleResult());
			if (page.getCount() < 1) {
				return page;
			}
    	}
    	// order by
    	String ql = qlString;
		if (StringUtils.isNotBlank(page.getOrderBy())){
			ql += " order by " + page.getOrderBy();
		}
        Query query = createQuery(ql, parameter); 
    	// set page
        if (!page.isDisabled()){
	        query.setFirstResult(page.getFirstResult());
	        query.setMaxResults(page.getMaxResults()); 
        }
        page.setList(query.getResultList());
		return page;
    }
    
    /**
	 * QL 查询
	 * @param qlString
	 * @param parameter
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<T> find(String qlString, Object... parameter){
		return createQuery(qlString, parameter).getResultList();
	}
    
	/**
	 * QL 更新
	 * @param sqlString
	 * @param parameter
	 * @return
	 */
	public int update(String sqlString, Object... parameter){
		return createQuery(sqlString, parameter).executeUpdate();
	}
	
	/**
	 * 创建 QL 查询对象
	 * @param qlString
	 * @param parameter
	 * @return
	 */
	public Query createQuery(String qlString, Object... parameter){
		Query query = getEntityManager().createQuery(qlString);
		setParameter(query, parameter);
		return query;
	}
	
	// -------------- QL Query --------------

    /**
	 * SQL 分页查询
	 * @param page
	 * @param qlString
	 * @param parameter
	 * @return
	 */
    @SuppressWarnings("unchecked")
	public Page<T> findBySql(Page<T> page, String qlString, Object... parameter){
		// get count
    	if (!page.isDisabled() && !page.isNotCount()){
	        String countQlString = "select count(*)" + removeSelect(removeOrders(qlString));  
	        page.setCount((Long)createSqlQuery(countQlString, parameter).getSingleResult());
			if (page.getCount() < 1) {
				return page;
			}
    	}
    	// order by
    	String ql = qlString;
		if (StringUtils.isNotBlank(page.getOrderBy())){
			ql += " order by " + page.getOrderBy();
		}
        Query query = createSqlQuery(ql, parameter); 
		// set page
        if (!page.isDisabled()){
	        query.setFirstResult(page.getFirstResult());
	        query.setMaxResults(page.getMaxResults()); 
        }
        page.setList(query.getResultList());
		return page;
    }

	/**
	 * SQL 查询
	 * @param sqlString
	 * @param parameter
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<T> findBySql(String sqlString, Object... parameter){
		return createSqlQuery(sqlString, parameter).getResultList();
	}
	
	/**
	 * SQL 更新
	 * @param sqlString
	 * @param parameter
	 * @return
	 */
	public int updateBySql(String sqlString, Object... parameter){
		return createSqlQuery(sqlString, parameter).executeUpdate();
	}
	
	/**
	 * 创建 SQL 查询对象
	 * @param sqlString
	 * @param parameter
	 * @return
	 */
	public Query createSqlQuery(String sqlString, Object... parameter){
		Query query = getEntityManager().createNativeQuery(sqlString, entityClass);
		setParameter(query, parameter);
		return query;
	}
	
	// -------------- Query Tools --------------

	/**
	 * 设置查询参数
	 * @param query
	 * @param parameter
	 */
	private void setParameter(Query query, Object... parameter){
		if (parameter != null){
			for (int i=0; i<parameter.length; i++){
				query.setParameter(i+1, parameter[i]);
			}
		}
	}
	
    /** 
     * 去除qlString的select子句。 
     * @param hql 
     * @return 
     */  
    private String removeSelect(String qlString){  
        int beginPos = qlString.toLowerCase().indexOf("from");  
        return qlString.substring(beginPos);  
    }  
      
    /** 
     * 去除hql的orderBy子句。 
     * @param hql 
     * @return 
     */  
    private String removeOrders(String qlString) {  
        Pattern p = Pattern.compile("order\\s*by[\\w|\\W|\\s|\\S]*", Pattern.CASE_INSENSITIVE);  
        Matcher m = p.matcher(qlString);  
        StringBuffer sb = new StringBuffer();  
        while (m.find()) {  
            m.appendReplacement(sb, "");  
        }
        m.appendTail(sb);
        return sb.toString();  
    } 
	
	// -------------- Criteria --------------
	
	/**
	 * 分页查询
	 * @param page
	 * @return
	 */
	public Page<T> find(Page<T> page) {
		return find(page, createDetachedCriteria());
	}
	
	/**
	 * 使用检索标准对象分页查询
	 * @param detachedCriteria
	 * @param page
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public Page<T> find(Page<T> page, DetachedCriteria detachedCriteria) {
		// get count
		if (!page.isDisabled() && !page.isNotCount()){
			page.setCount(count(detachedCriteria));
			if (page.getCount() < 1) {
				return page;
			}
		}
		Criteria criteria = detachedCriteria.getExecutableCriteria(getSession());
		criteria.setResultTransformer(Criteria.ROOT_ENTITY);
		// set page
		if (!page.isDisabled()){
	        criteria.setFirstResult(page.getFirstResult());
	        criteria.setMaxResults(page.getMaxResults()); 
		}
		// order by
		if (StringUtils.isNotBlank(page.getOrderBy())){
			for (String order : StringUtils.split(page.getOrderBy(), ",")){
				String[] o = StringUtils.split(order, " ");
				if (o.length==1){
					criteria.addOrder(Order.asc(o[0]));
				}else if (o.length==2){
					if ("DESC".equals(o[1].toUpperCase())){
						criteria.addOrder(Order.desc(o[0]));
					}else{
						criteria.addOrder(Order.asc(o[0]));
					}
				}
			}
		}
		page.setList(criteria.list());
		return page;
	}

	/**
	 * 使用检索标准对象查询
	 * @param detachedCriteria
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<T> find(DetachedCriteria detachedCriteria) {
		Criteria criteria = detachedCriteria.getExecutableCriteria(getSession());
		criteria.setResultTransformer(Criteria.ROOT_ENTITY);
		return criteria.list(); 
	}
	
	/**
	 * 使用检索标准对象查询记录数
	 * @param detachedCriteria
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public long count(DetachedCriteria detachedCriteria) {
		Criteria criteria = detachedCriteria.getExecutableCriteria(getSession());
		long totalCount = 0;
		try {
			// Get orders
			Field field = CriteriaImpl.class.getDeclaredField("orderEntries");
			field.setAccessible(true);
			List orderEntrys = (List)field.get(criteria);
			// Remove orders
			field.set(criteria, new ArrayList());
			// Get count
			criteria.setProjection(Projections.rowCount());
			totalCount = Long.valueOf(criteria.uniqueResult().toString());
			// Clean count
			criteria.setProjection(null);
			// Restore orders
			field.set(criteria, orderEntrys);
		} catch (NoSuchFieldException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		return totalCount;
	}

	/**
	 * 创建与会话无关的检索标准对象
	 * @param criterions Restrictions.eq("name", value);
	 * @return 
	 */
	public DetachedCriteria createDetachedCriteria(Criterion... criterions) {
		DetachedCriteria dc = DetachedCriteria.forClass(entityClass);
		for (Criterion c : criterions) {
			dc.add(c);
		}
		return dc;
	}
	
	// -------------- Hibernate search --------------
	
	/**
	 * 获取全文Session
	 */
	public FullTextSession getFullTextSession(){
		return Search.getFullTextSession(getSession());
	}
	
	/**
	 * 建立索引
	 */
	public void createIndex(){
		try {
			getFullTextSession().createIndexer(Article.class).startAndWait();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 全文检索
	 * @param page 分页对象
	 * @param query 关键字查询对象
	 * @param queryFilter 查询过滤对象
	 * @param sort 排序对象
	 * @return 分页对象
	 */
	@SuppressWarnings("unchecked")
	public Page<T> search(Page<T> page, BooleanQuery query, BooleanQuery queryFilter, Sort sort){
		
		// 按关键字查询
		FullTextQuery fullTextQuery = getFullTextSession().createFullTextQuery(query, entityClass);
        
		// 过滤无效的内容
		fullTextQuery.setFilter(new CachingWrapperFilter(new QueryWrapperFilter(queryFilter)));
        
        // 按时间排序
		fullTextQuery.setSort(sort);

		// 定义分页
		page.setCount(fullTextQuery.getResultSize());
		fullTextQuery.setFirstResult(page.getFirstResult());
		fullTextQuery.setMaxResults(page.getMaxResults()); 

		// 先从持久化上下文中查找对象，如果没有再从二级缓存中查找
        fullTextQuery.initializeObjectsWith(ObjectLookupMethod.SECOND_LEVEL_CACHE, DatabaseRetrievalMethod.QUERY); 
        
		// 返回结果
		page.setList(fullTextQuery.list());
        
		return page;
	}
	
	/**
	 * 获取全文查询对象
	 */
	public BooleanQuery getFullTextQuery(BooleanClause... booleanClauses){
		BooleanQuery booleanQuery = new BooleanQuery();
		for (BooleanClause booleanClause : booleanClauses){
			booleanQuery.add(booleanClause);
		}
		return booleanQuery;
	}

	/**
	 * 获取全文查询对象
	 * @param q 查询关键字
	 * @param fields 查询字段
	 * @return 全文查询对象
	 */
	public BooleanQuery getFullTextQuery(String q, String... fields){
		Analyzer analyzer = new IKAnalyzer();
		BooleanQuery query = new BooleanQuery();
		try {
			if (StringUtils.isNotBlank(q)){
				for (String field : fields){
					QueryParser parser = new QueryParser(Version.LUCENE_36, field, analyzer);   
					query.add(parser.parse(q), Occur.SHOULD);
				}
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return query;
	}
	
	/**
	 * 设置关键字高亮
	 * @param query 查询对象
	 * @param list 设置高亮的内容列表
	 * @param fields 字段名
	 */
	public List<T> keywordsHighlight(BooleanQuery query, List<T> list, String... fields){
		Analyzer analyzer = new IKAnalyzer();
		Formatter formatter = new SimpleHTMLFormatter("<span class=\"highlight\">", "</span>");   
		Highlighter highlighter = new Highlighter(formatter, new QueryScorer(query)); 
		highlighter.setTextFragmenter(new SimpleFragmenter(130)); 
		for(T entity : list){ 
			try {
				for (String field : fields){
					String text = StringUtils.replaceHtml((String)Reflections.invokeGetter(entity, field));
					String desciption = highlighter.getBestFragment(analyzer,field, text);
					if(desciption!=null){
						Reflections.invokeSetter(entity, fields[0], desciption);
						break;
					}
					Reflections.invokeSetter(entity, fields[0], StringUtils.abbr(text, 130));
				}
				//Reflections.invokeSetter(entity, fields[1], "sdfkjsdlkfjklsdjf");
			} catch (IOException e) {
				e.printStackTrace();
			} catch (InvalidTokenOffsetsException e) {
				e.printStackTrace();
			} 
		}
		return list;
	}
}