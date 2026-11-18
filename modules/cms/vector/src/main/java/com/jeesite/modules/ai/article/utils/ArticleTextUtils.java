/**
 * Copyright (c) 2013-Now https://jeesite.com All rights reserved.
 * No deletion without permission, or be held responsible to law.
 */
package com.jeesite.modules.ai.article.utils;

import com.jeesite.common.collect.ListUtils;
import com.jeesite.common.config.Global;
import com.jeesite.common.io.IOUtils;
import com.jeesite.common.lang.StringUtils;
import com.jeesite.common.web.http.HttpClientUtils;
import com.jeesite.common.web.http.ServletUtils;
import com.jeesite.modules.cms.entity.CmsArticle;
import com.vladsch.flexmark.html.renderer.LinkType;
import com.vladsch.flexmark.html.renderer.ResolvedLink;
import com.vladsch.flexmark.html2md.converter.FlexmarkHtmlConverter;
import com.vladsch.flexmark.html2md.converter.HtmlLinkResolver;
import com.vladsch.flexmark.html2md.converter.HtmlLinkResolverFactory;
import com.vladsch.flexmark.html2md.converter.HtmlNodeConverterContext;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.constraints.NotNull;
import org.apache.tika.Tika;
import org.apache.tika.config.TikaConfig;
import org.apache.tika.exception.TikaException;
import org.apache.tika.io.TikaInputStream;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.mime.MediaType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Set;

/**
 * 文章内容提取工具：把文章正文（HTML）转换为纯文本（Markdown），
 * 并提取正文中链接的附件内容，供向量化、全文检索等场景复用
 * @author ThinkGem
 * @version 2026-11-18
 */
public class ArticleTextUtils {

	private static final Logger logger = LoggerFactory.getLogger(ArticleTextUtils.class);

	/**
	 * 提取文章内容：标题、关键字、描述、正文（HTML 转 Markdown）、正文链接的附件内容
	 * @param article 文章对象
	 */
	public static String extractArticleContent(CmsArticle article) {
		if (article == null) {
			return StringUtils.EMPTY;
		}

		// 正文中的链接附件内容
		List<String> attachmentList = ListUtils.newArrayList();
		String articleContent = StringUtils.EMPTY;
		if (article.getArticleData() != null && StringUtils.isNotBlank(article.getArticleData().getContent())) {
			articleContent = FlexmarkHtmlConverter.builder()
					.linkResolverFactory(getHtmlLinkResolverFactory(attachmentList))
					.build()
					.convert(article.getArticleData().getContent());
		}

		// 组装文本：标题、关键字、描述、正文、附件内容
		List<String> textList = ListUtils.newArrayList();
		if (StringUtils.isNotBlank(article.getTitle())) {
			textList.add(article.getTitle());
		}
		if (StringUtils.isNotBlank(article.getKeywords())) {
			textList.add(article.getKeywords());
		}
		if (StringUtils.isNotBlank(article.getDescription())) {
			textList.add(article.getDescription());
		}
		if (StringUtils.isNotBlank(articleContent)) {
			textList.add(articleContent);
		}
		if (!attachmentList.isEmpty()) {
			textList.add("attachment: " + String.join(", ", attachmentList));
		}
		return String.join(", ", textList);
	}

	/**
	 * 解析文章中的连接并提取内容
	 * @author ThinkGem
	 */
	private static @NotNull HtmlLinkResolverFactory getHtmlLinkResolverFactory(List<String> attachmentList) {
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
					if (!"a".equalsIgnoreCase(node.nodeName())) {
						return resolvedLink;
					}
					String href = node.attributes().get("href");
					if (StringUtils.isBlank(href) || StringUtils.contains(href, "..")) {
						return new ResolvedLink(LinkType.LINK, href);
					}
					String text = null;
					String url = href;
					if (StringUtils.contains(url, "://")) {
						// 只提取系统允许跳转的附件内容，外部网站内容不进行提取，shiro.allowRedirects 参数设置范围
						if (request != null && ServletUtils.isAllowRedirects(request, url)) {
							try (InputStream is = HttpClientUtils.getInputStream(url, null)) {
								if (is != null) {
									text = getDocumentText(is);
								}
							} catch (IOException | TikaException e) {
								logger.error(e.getMessage(), e);
							}
						}
					} else {
						String ctxPath = Global.getCtxPath();
						if (StringUtils.isNotBlank(ctxPath) && StringUtils.startsWith(url, ctxPath)) {
							url = url.substring(ctxPath.length());
						}
						try (InputStream is = IOUtils.getFileInputStream(Global.getUserfilesBaseDir(url))) {
							if (is != null) {
								text = getDocumentText(is);
							}
						} catch (IOException | TikaException e) {
							logger.error(e.getMessage(), e);
						}
					}
					if (StringUtils.isNotBlank(text)) {
						attachmentList.add(url + " " + text);
					}
					return new ResolvedLink(LinkType.LINK, href);
				};
			}
		};
	}

	/**
	 * 获取文档文本内容：HTML 转为 Markdown，其它类型按 Tika 解析并去除空行
	 * @param is 文件输入流
	 */
	public static String getDocumentText(InputStream is) throws IOException, TikaException {
		TikaConfig config = TikaConfig.getDefaultConfig();
		Tika tika = new Tika(config);
		Metadata metadata = new Metadata();
		TikaInputStream stream = TikaInputStream.get(is);
		MediaType mimetype = tika.getDetector().detect(stream, metadata);

		if (mimetype != null && StringUtils.equals(mimetype.getType(), "text")) {
			String text = IOUtils.toString(stream, StandardCharsets.UTF_8);
			if (StringUtils.isNotBlank(text)) {
				return FlexmarkHtmlConverter.builder().build().convert(text);
			} else {
				return text;
			}
		}

		String content = tika.parseToString(stream, metadata);
		return content.lines()
			.map(String::strip).filter(line -> !line.isEmpty())
			.reduce((a, b) -> a + System.lineSeparator() + b)
			.orElse(StringUtils.EMPTY);
	}

}
