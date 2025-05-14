/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 * No deletion without permission, or be held responsible to law.
 */
package com.jeesite.modules.cms.ai.service;

import com.jeesite.common.collect.ListUtils;
import com.jeesite.common.collect.MapUtils;
import com.jeesite.common.config.Global;
import com.jeesite.common.io.IOUtils;
import com.jeesite.common.lang.StringUtils;
import com.jeesite.common.lang.TimeUtils;
import com.jeesite.common.utils.PageUtils;
import com.jeesite.common.web.http.HttpClientUtils;
import com.jeesite.common.web.http.ServletUtils;
import com.jeesite.modules.cms.entity.Article;
import com.jeesite.modules.cms.service.ArticleVectorStore;
import com.jeesite.modules.cms.utils.CmsUtils;
import com.vladsch.flexmark.html.renderer.LinkType;
import com.vladsch.flexmark.html.renderer.ResolvedLink;
import com.vladsch.flexmark.html2md.converter.FlexmarkHtmlConverter;
import com.vladsch.flexmark.html2md.converter.HtmlLinkResolver;
import com.vladsch.flexmark.html2md.converter.HtmlLinkResolverFactory;
import com.vladsch.flexmark.html2md.converter.HtmlNodeConverterContext;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.tika.Tika;
import org.apache.tika.config.TikaConfig;
import org.apache.tika.exception.TikaException;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.document.Document;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.ai.vectorstore.filter.FilterExpressionBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * CMS 文章向量库存储
 * @author ThinkGem
 */
@Service
public class ArticleVectorStoreImpl implements ArticleVectorStore {

	protected Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private VectorStore vectorStore;

	/**
	 * 保存文章到向量库
	 * @author ThinkGem
	 */
	@Override
	public void save(Article article) {
		Map<String, Object> metadata = MapUtils.newHashMap();
		metadata.put("id", article.getId());
		metadata.put("siteCode", article.getCategory().getSite().getSiteCode());
		metadata.put("categoryCode", article.getCategory().getCategoryCode());
		metadata.put("categoryName", article.getCategory().getCategoryName());
		metadata.put("title", article.getTitle());
		metadata.put("href", article.getHref());
		metadata.put("keywords", article.getKeywords());
		metadata.put("description", article.getDescription());
		metadata.put("url", article.getUrl());
		metadata.put("status", article.getStatus());
		metadata.put("createBy", article.getCreateBy());
		metadata.put("createDate", article.getCreateDate());
		metadata.put("updateBy", article.getUpdateBy());
		metadata.put("updateDate", article.getUpdateDate());
		List<String> attachmentList = ListUtils.newArrayList();
		String content = article.getTitle() + ", " + article.getKeywords() + ", "
				+ article.getDescription() + ", " + FlexmarkHtmlConverter.builder()
					.linkResolverFactory(getHtmlLinkResolverFactory(attachmentList)).build()
					.convert(article.getArticleData().getContent())
				+ ", attachment: " + attachmentList;
		List<Document> documents = List.of(new Document(article.getId(), content, metadata));
		List<Document> splitDocuments = new TokenTextSplitter().apply(documents);
		this.delete(article); // 删除原数据
		ListUtils.pageList(splitDocuments, 10, params -> {
			vectorStore.add((List<Document>)params[0]); // 增加新数据
			return null;
		});
	}

	/**
	 * 解析文章中的连接并提取内容
	 * @author ThinkGem
	 */
	private @NotNull HtmlLinkResolverFactory getHtmlLinkResolverFactory(List<String> attachmentList) {
		HttpServletRequest request = ServletUtils.getRequest();
		return new HtmlLinkResolverFactory() {
			@Override
			public @NotNull Set<Class<?>> getAfterDependents() {
				return Set.of();
			}
			@Override
			public @NotNull Set<Class<?>> getBeforeDependents() {
				return Set.of();
			}
			@Override
			public boolean affectsGlobalScope() {
				return false;
			}
			@Override
			public HtmlLinkResolver apply(HtmlNodeConverterContext htmlNodeConverterContext) {
				return (node, context, resolvedLink) -> {
					if ("a".equalsIgnoreCase(node.nodeName())) {
						String href = node.attributes().get("href"); String url = href;
						if (StringUtils.contains(url, "://")) {
							// 只提取系统允许跳转的附件内容，外部网站内容不进行提取，shiro.allowRedirects 参数设置范围
							if (ServletUtils.isAllowRedirects(request, url)) {
								try (InputStream is = HttpClientUtils.getInputStream(url, null)) {
									if (is != null) {
										String text = getDocumentText(is);
										attachmentList.add(url + text);
									}
								} catch (IOException | TikaException e) {
									logger.error(e.getMessage(), e);
								}
							}
						} else {
							String ctxPath = Global.getCtxPath();
							if (StringUtils.isNotBlank(ctxPath) && StringUtils.startsWith(url, ctxPath)){
								url = url.substring(ctxPath.length());
							}
							try (InputStream is = IOUtils.getFileInputStream(Global.getUserfilesBaseDir(url))){
								if (is != null) {
									String text = getDocumentText(is);
									attachmentList.add(url + text);
								}
							} catch (IOException | TikaException e) {
								logger.error(e.getMessage(), e);
							}
						}
						return new ResolvedLink(LinkType.LINK, href);
					}
					return resolvedLink;
				};
			}
			/**
			 * 获取文章附件中的内容
			 * @author ThinkGem
			 */
			private static @NotNull String getDocumentText(InputStream is) throws IOException, TikaException {
				TikaConfig config = TikaConfig.getDefaultConfig();
				String content = new Tika(config).parseToString(is);
				return content.lines()
					.map(String::strip).filter(line -> !line.isEmpty())
					.reduce((a, b) -> a + System.lineSeparator() + b)
					.orElse(StringUtils.EMPTY);
			}
		};
	}

	/**
	 * 删除向量库文章
	 * @author ThinkGem
	 */
	@Override
	public void delete(Article article) {
		if (StringUtils.isNotBlank(article.getId())) {
			vectorStore.delete(new FilterExpressionBuilder().eq("id", article.getId()).build());
		}
	}

	/**
	 * 重建向量库文章
	 * @author ThinkGem
	 */
	public String rebuild(Article article) {
		logger.debug("开始重建向量库。 siteCode: {}, categoryCode: {}",
				article.getCategory().getSite().getSiteCode(),
				article.getCategory().getCategoryCode());
		long start = System.currentTimeMillis();
		try{
			article.setIsQueryArticleData(true); // 查询文章内容
			PageUtils.findList(article, null, e -> {
				List<Article> list = CmsUtils.getArticleService().findList((Article) e);
				if (!list.isEmpty()) {
					list.forEach(this::save);
					return true;
				}
				return false;
			});
		}catch(Exception ex){
			logger.error("重建向量库失败", ex);
			return "重建向量库失败：" + ex.getMessage();
		}
		String message = "重建向量库完成！ 用时" + TimeUtils.formatTime(System.currentTimeMillis() - start) + "。";
		logger.debug(message);
		return message;
	}

}
