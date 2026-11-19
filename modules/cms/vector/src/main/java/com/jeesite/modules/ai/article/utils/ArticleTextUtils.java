/**
 * Copyright (c) 2013-Now https://jeesite.com All rights reserved.
 * No deletion without permission, or be held responsible to law.
 */
package com.jeesite.modules.ai.article.utils;

import com.jeesite.common.codec.EncodeUtils;
import com.jeesite.common.collect.ListUtils;
import com.jeesite.common.config.Global;
import com.jeesite.common.io.IOUtils;
import com.jeesite.common.lang.StringUtils;
import com.jeesite.modules.cms.entity.CmsArticle;
import com.vladsch.flexmark.html.renderer.LinkType;
import com.vladsch.flexmark.html.renderer.ResolvedLink;
import com.vladsch.flexmark.html2md.converter.FlexmarkHtmlConverter;
import com.vladsch.flexmark.html2md.converter.HtmlLinkResolver;
import com.vladsch.flexmark.html2md.converter.HtmlLinkResolverFactory;
import com.vladsch.flexmark.html2md.converter.HtmlNodeConverterContext;
import jakarta.validation.constraints.NotNull;
import org.apache.tika.Tika;
import org.apache.tika.config.TikaConfig;
import org.apache.tika.exception.TikaException;
import org.apache.tika.io.TikaInputStream;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.mime.MediaType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
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

	/** 附件内容提取的最大文件大小（字节），默认 50MB，参数：cms.vector.attachment.maxFileSize，超过则忽略 */
	private static final long MAX_ATTACHMENT_FILE_SIZE = Global.getConfigToLong(
			"cms.vector.attachment.maxFileSize", "52428800");

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
					// 只读取本项目 userfiles 下载目录内的文件，不请求任何外部地址或其他服务（避免 SSRF 漏洞）
					File file = getLocalUserfilesFile(href);
					if (file != null) {
						try (InputStream is = IOUtils.getFileInputStream(file)) {
							if (is != null) {
								text = getDocumentText(is);
							}
						} catch (Exception e) {
							// 解析失败等均忽略，不能中断文章内容转换
							logger.warn("提取附件内容失败，已忽略。href: {}，{}", href, e.toString());
						}
					}
					if (StringUtils.isNotBlank(text)) {
						attachmentList.add(href + " " + text);
					}
					return new ResolvedLink(LinkType.LINK, href);
				};
			}
		};
	}

	/**
	 * 获取链接对应的本地文件：只允许读取本项目 userfiles 下载目录内的文件，
	 * 外部网站、其他服务地址、越权路径一律返回 null（不发起任何网络请求，避免 SSRF 漏洞）
	 * @param href 链接地址（绝对地址或相对地址）
	 * @author ThinkGem
	 */
	private static File getLocalUserfilesFile(String href) {
		String path = getUrlPath(href);
		if (StringUtils.isBlank(path)) {
			return null;
		}
		try {
			path = EncodeUtils.decodeUrl(path);
		} catch (Exception e) {
			logger.debug("非法的链接地址，已忽略。href: {}，{}", href, e.toString());
			return null;
		}
		// 去掉项目根路径，兼容 "/ctxPath/userfiles/..." 形式的地址
		String ctxPath = Global.getCtxPath();
		if (StringUtils.isNotBlank(ctxPath) && StringUtils.startsWith(path, ctxPath)) {
			path = StringUtils.substring(path, ctxPath.length());
		}
		// 取 userfiles 之后的相对路径，其它目录（含外部地址）一律忽略
		String relativePath = null;
		for (String baseUrl : new String[]{Global.USERFILES_BASE_URL, "/userfiles"}) {
			if (StringUtils.isBlank(baseUrl)) {
				continue;
			}
			int index = StringUtils.lastIndexOf(path, baseUrl);
			if (index < 0) {
				continue;
			}
			String suffix = StringUtils.substring(path, index + baseUrl.length());
			// 必须是 userfiles 目录下的路径，排除 "/userfilesx/..." 之类的相似前缀
			if (StringUtils.startsWith(suffix, "/")) {
				relativePath = suffix;
				break;
			}
		}
		if (StringUtils.isBlank(relativePath)) {
			logger.debug("非本项目 userfiles 目录的文件，已忽略。href: {}", href);
			return null;
		}
		// 路径穿越校验（含 URL 编码后的 ../）
		if (StringUtils.contains(relativePath, "..")) {
			logger.warn("非法的文件路径，已忽略。href: {}", href);
			return null;
		}
		try {
			File file = new File(Global.getUserfilesBaseDir(relativePath));
			String basePath = new File(Global.getUserfilesBaseDir(null)).getCanonicalPath();
			String filePath = file.getCanonicalPath();
			// 真实路径必须位于 userfiles 目录内（防止软链接、编码绕过等越权读取）
			if (!StringUtils.equals(filePath, basePath)
					&& !StringUtils.startsWith(filePath, basePath + File.separator)) {
				logger.warn("文件路径不在 userfiles 目录内，已忽略。href: {}", href);
				return null;
			}
			if (!file.isFile()) {
				return null;
			}
			if (file.length() > MAX_ATTACHMENT_FILE_SIZE) {
				logger.warn("附件超过大小限制，已忽略。href: {}，size: {}", href, file.length());
				return null;
			}
			return file;
		} catch (Exception e) {
			logger.debug("获取文件失败，已忽略。href: {}，{}", href, e.toString());
			return null;
		}
	}

	/**
	 * 获取链接地址中的路径部分：丢弃协议、域名、参数、锚点
	 * @param href 链接地址
	 * @author ThinkGem
	 */
	private static String getUrlPath(String href) {
		String path = StringUtils.trimToEmpty(href);
		int index = StringUtils.indexOf(path, "://");
		if (index >= 0) {
			path = StringUtils.substring(path, index + 3);
			index = StringUtils.indexOf(path, "/");
			path = index >= 0 ? StringUtils.substring(path, index) : StringUtils.EMPTY;
		} else if (StringUtils.startsWith(path, "//")) {
			path = StringUtils.substring(path, 2);
			index = StringUtils.indexOf(path, "/");
			path = index >= 0 ? StringUtils.substring(path, index) : StringUtils.EMPTY;
		}
		return StringUtils.substringBefore(StringUtils.substringBefore(path, "?"), "#");
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
