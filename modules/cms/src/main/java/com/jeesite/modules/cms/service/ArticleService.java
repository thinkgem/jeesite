/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 * No deletion without permission, or be held responsible to law.
 */
package com.jeesite.modules.cms.service;

import com.jeesite.common.collect.ListUtils;
import com.jeesite.common.config.Global;
import com.jeesite.common.entity.Page;
import com.jeesite.common.lang.DateUtils;
import com.jeesite.common.lang.StringUtils;
import com.jeesite.common.service.CrudService;
import com.jeesite.modules.cms.dao.ArticleDao;
import com.jeesite.modules.cms.dao.ArticleDataDao;
import com.jeesite.modules.cms.entity.Article;
import com.jeesite.modules.cms.entity.ArticleData;
import com.jeesite.modules.cms.entity.Category;
import com.jeesite.modules.cms.utils.CmsUtils;
import com.jeesite.modules.file.utils.FileUploadUtils;
import com.jeesite.modules.sys.utils.UserUtils;
import io.netty.util.concurrent.DefaultThreadFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 文章表Service
 * @author 长春叭哥、ThinkGem
 * @version 2020-7-24
 */
@Service
public class ArticleService extends CrudService<ArticleDao, Article> {

	@Autowired
	private ArticleDataDao articleDataDao;

	private static ExecutorService updateExpiredWeightThreadPool = new ThreadPoolExecutor(5, 20,
			60L, TimeUnit.SECONDS, new LinkedBlockingQueue<>(),
			new DefaultThreadFactory("cms-update-expired-weight"));
	
	/**
	 * 获取单条数据
	 * @param article
	 * @return
	 */
	@Override
	public Article get(Article article) {
		Article entity = super.get(article);
		if (entity != null && StringUtils.isNotBlank(article.getId())) {
			entity.setArticleData(get(new ArticleData(article.getId())));
		}
		return entity;
	}

	/**
	 * 获取文章详情内容
	 */
	public ArticleData get(ArticleData articleData) {
		return articleDataDao.get(articleData);
	}

	/**
	 * 添加数据权限
	 */
	@Override
	public void addDataScopeFilter(Article entity) {
		
	}
	
	/**
	 * 查询分页数据
	 * @param article 查询条件
	 * @param article page 分页对象
	 * @return
	 */
	@Override
	public Page<Article> findPage(Article article) {
		updateExpiredWeightThreadPool.submit(new Runnable() {
			@Override
			public void run() {
				updateExpiredWeight(article);
			}
		});
		return super.findPage(article);
	}

	/**
	 * 通过编号获取内容标题
	 * @return new Object[]{栏目Id,内容Id,内容标题}
	 */
	public List<Object[]> findByIds(String ids) {
		if (ids == null) {
			return new ArrayList<Object[]>();
		}
		List<Object[]> list = ListUtils.newArrayList();
		String[] idss = StringUtils.split(ids, ",");
		Article where = new Article();
		where.setId_in(idss);
		List<Article> l = dao.findList(where);
		for (Article e : l) {
			list.add(new Object[] { e.getCategory().getId(), e.getId(), StringUtils.abbr(e.getTitle(), 50) });
		}
		return list;
	}
	
	/**
	 * 权重更新
	 * @param article
	 * @author ThinkGem
	 */
	@Transactional
	public void updateExpiredWeight(Article article) {
		// 更新过期的权重，间隔为“6”个小时
		Date updateExpiredWeightDate = CmsUtils.getCache("updateExpiredWeightDateByArticle");
		if (updateExpiredWeightDate == null || (updateExpiredWeightDate != null
				&& updateExpiredWeightDate.getTime() < System.currentTimeMillis())) {
			article.setWeightDate(new Date());
			dao.updateExpiredWeight(article);
			CmsUtils.putCache("updateExpiredWeightDateByArticle", DateUtils.addHours(new Date(), 6));
		}
	}

	/**
	 * 保存数据（插入或更新）
	 * @param article
	 */
	@Override
	@Transactional
	public void save(Article article) {
		Global.assertDemoMode();
		// 设置内容状态
		if (article.getCategory() != null && StringUtils.isNotBlank(article.getCategory().getId())) {
			Category category = CmsUtils.getCategory(article.getCategory().getId());
			// 如果栏目不需要审核，或者当前用户有审核权限，则将该内容设为发布状态
			if (Global.YES.equals(category.getIsNeedAudit())
					&& !UserUtils.getSubject().isPermitted("cms:article:audit")) {
				// 并且不是草稿状态
				if (!article.getStatus().equals(Article.STATUS_DRAFT)){
					article.setStatus(Article.STATUS_AUDIT);
				}
			}
			// 将栏目信息设置到实体对象（全文检索需要）
			article.setCategory(category);
		} else {
			throw new RuntimeException("归属栏目不能为空。");
		}
		// 保存详细内容
		if (article.getIsNewRecord()) {
			dao.insert(article);
			article.getArticleData().setId(article.getId());
			articleDataDao.insert(article.getArticleData());
		} else {
			dao.update(article);
			article.getArticleData().setId(article.getId());
			articleDataDao.update(article.getArticleData());
		}
		// 保存上传图片
		FileUploadUtils.saveFileUpload(article, article.getId(), "article_image");
//		// 保存上传附件
//		FileUploadUtils.saveFileUpload(article, article.getId(), "article_file");
	}

	/**
	 * 更新状态
	 * @param article
	 */
	@Override
	@Transactional
	public void updateStatus(Article article) {
		super.updateStatus(article);
	}

	/**
	 * 获取文章获取文章并点击数加一
	 */
	@Transactional
	public void updateHitsAddOne(String id) {
		dao.updateHitsAddOne(id);
	}

	/**
	 * 删除数据
	 * @param article
	 */
	@Override
	@Transactional
	public void delete(Article article) {
		super.delete(article);
	}

	/**
	 * 文章高级搜索
	 * @param page 分页对象
	 * @param qStr 搜索字符串
	 * @param qand 包含的字符串
	 * @param qnot 不包含的字符串
	 * @param bd 开始日期
	 * @param ed 结束日期
	 */
	public Page<Map<String, Object>> searchPage(Page<Map<String, Object>> page, String qStr,
			String qand, String qnot, String bd, String ed, Map<String, String> params) {
		return page;
	}

}