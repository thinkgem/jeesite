/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 * No deletion without permission, or be held responsible to law.
 */
package com.jeesite.common.web.http;

import com.jeesite.common.codec.EncodeUtils;
import com.jeesite.common.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLEngine;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509ExtendedTrustManager;
import java.io.InputStream;
import java.net.Socket;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import java.time.Duration;
import java.util.Map;

/**
 * HTTP 客户端工具类（支持HTTPS）
 * @author ThinkGem
 * @version 2025-03-26
 */
public class HttpClientUtils {

	private final static Logger logger = LoggerFactory.getLogger(HttpClientUtils.class);
	private static final HttpClient client = createHttpClient(60);

	/**
	 * HTTP 的 GET 请求
	 */
	public static String get(String url) {
		return get(url, null, null);
	}

	/**
	 * HTTP 的 GET 请求，传递 Map 格式参数
	 */
	public static String get(String url, Map<String, String> dataMap) {
        return get(url, dataMap, EncodeUtils.UTF_8);
	}
	
	/**
	 * HTTP 的 GET 请求，传递 Map 格式参数，支持指定编码
	 */
	public static String get(String url, Map<String, String> dataMap, String charset) {
		HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(buildUrl(url, dataMap, charset)))
                .GET()
                .build();
        return executeRequest(request);
	}
	
	/**
	 * HTTP 的 GET 请求，增加 ajax 请求头
	 */
	public static String ajaxGet(String url) {
		return ajaxGet(url, null, null);
	}

	/**
	 * HTTP 的 GET 请求，增加 ajax 请求头，传递 Map 格式参数
	 */
	public static String ajaxGet(String url, Map<String, String> dataMap) {
        return ajaxGet(url, dataMap, EncodeUtils.UTF_8);
	}

	/**
	 * HTTP 的 GET 请求，增加 ajax 请求头，传递 Map 格式参数，支持指定编码
	 */
	public static String ajaxGet(String url, Map<String, String> dataMap, String charset) {
		HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(buildUrl(url, dataMap, charset)))
                .header("X-Requested-With", "XMLHttpRequest")
                .GET()
                .build();
        return executeRequest(request);
	}

	/**
	 * 构建表单数据，Map 转换 params，支持指定编码
	 */
	private static String buildUrl(String url, Map<String, String> dataMap, String charset) {
		if (dataMap == null) {
			return url;
		}
		StringBuilder sb = new StringBuilder(url);
        if (!url.contains("?")) {
            sb.append("?");
        } else if (!url.endsWith("&")) {
            sb.append("&");
        }
		return sb + buildFormData(dataMap, charset);
	}

	/**
	 * HTTP 的 POST 请求，传递 Map 格式参数
	 */
	public static String post(String url, Map<String, String> dataMap) {
		return post(url, dataMap, EncodeUtils.UTF_8);
	}

	/**
	 * HTTP 的 POST 请求，传递 Map 格式参数，支持指定编码
	 */
	public static String post(String url, Map<String, String> dataMap, String charset) {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Content-Type", "application/x-www-form-urlencoded")
                .POST(HttpRequest.BodyPublishers.ofString(buildFormData(dataMap, charset)))
                .build();
        return executeRequest(request);
	}

	/**
	 * HTTP 的 POST 请求，增加 ajax 请求头，传递 Map 格式参数
	 */
	public static String ajaxPost(String url, Map<String, String> dataMap) {
		return ajaxPost(url, dataMap, EncodeUtils.UTF_8);
	}

	/**
	 * HTTP 的 POST 请求，增加 ajax 请求头，传递 Map 格式参数，支持指定编码
	 */
	public static String ajaxPost(String url, Map<String, String> dataMap, String charset) {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("X-Requested-With", "XMLHttpRequest")
                .header("Content-Type", "application/x-www-form-urlencoded")
                .POST(HttpRequest.BodyPublishers.ofString(buildFormData(dataMap, charset)))
                .build();
        return executeRequest(request);
	}

	/**
	 * 构建表单数据，Map 转换 params，支持指定编码
	 */
	private static String buildFormData(Map<String, String> dataMap, String charset) {
		return dataMap.entrySet().stream()
                .map(entry -> entry.getKey() + "="
						+ EncodeUtils.encodeUrl(entry.getValue(), charset))
                .reduce((a, b) -> a + "&" + b)
                .orElse(StringUtils.EMPTY);
	}

	/**
	 * HTTP 的 POST 请求，使用 json 请求头，传递 json 格式参数
	 */
	public static String ajaxPostJson(String url, String jsonString) {
		return ajaxPostJson(url, jsonString, EncodeUtils.UTF_8);
	}

	/**
	 * HTTP 的 POST 请求，使用 json 请求头，传递 json 格式参数，支持指定编码
	 */
	public static String ajaxPostJson(String url, String jsonString, String charset) {
		HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("X-Requested-With", "XMLHttpRequest")
                .header("Content-Type", "application/json; charset="
						+ (StringUtils.isNotBlank(charset) ? charset : EncodeUtils.UTF_8))
                .POST(HttpRequest.BodyPublishers.ofString(jsonString))
                .build();
		return executeRequest(request);
	}

	/**
	 * 执行一个 http 请求，传递 HttpRequest 参数
	 */
	public static String executeRequest(HttpRequest request) {
		try {
			return client.send(request, HttpResponse.BodyHandlers.ofString()).body();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
	}

	/**
	 * HTTP 的 GET 请求，返回文件流
	 */
	public static InputStream getInputStream(String url) {
        return getInputStream(url, null);
	}

	/**
	 * HTTP 的 GET 请求，传递 Map 格式参数，返回文件流
	 */
	public static InputStream getInputStream(String url, Map<String, String> dataMap) {
		HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(buildUrl(url, dataMap, EncodeUtils.UTF_8)))
                .GET()
                .build();
        return executeRequestInputStream(request);
	}

	/**
	 * 执行一个 http 请求，传递 HttpRequest 参数，返回文件流
	 */
	public static InputStream executeRequestInputStream(HttpRequest request) {
		try {
			 HttpResponse<InputStream> response = client.send(request, HttpResponse.BodyHandlers.ofInputStream());
			 if (response.statusCode() == 200) {
				 return response.body();
			 } else {
				 logger.info("URL: {} statusCode: {}", request.uri().toString(), response.statusCode());
				 return null;
			 }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
	}

	public static HttpClient createHttpClient(long seconds) {
		HttpClient client;
		try {
			SSLContext sslContext = SSLContext.getInstance("TLS");
			sslContext.init(null, new TrustManager[]{new UnsafeX509ExtendedTrustManager()}, new SecureRandom());
			client = HttpClient.newBuilder()
					.sslContext(sslContext)
					.connectTimeout(Duration.ofSeconds(seconds))
					.build();
		} catch (Exception e) {
			logger.info(e.getMessage(), e);
			client = HttpClient.newHttpClient();
		}
		return client;
	}

	private static final class UnsafeX509ExtendedTrustManager extends X509ExtendedTrustManager {

		private static final X509Certificate[] EMPTY_CERTIFICATES = new X509Certificate[0];

		@Override
		public void checkClientTrusted(X509Certificate[] certificates, String authType) {
		}
		@Override
		public void checkClientTrusted(X509Certificate[] certificates, String authType, Socket socket) {
		}
		@Override
		public void checkClientTrusted(X509Certificate[] certificates, String authType, SSLEngine sslEngine) {
		}
		@Override
		public void checkServerTrusted(X509Certificate[] certificates, String authType) {
		}
		@Override
		public void checkServerTrusted(X509Certificate[] certificates, String authType, Socket socket) {
		}
		@Override
		public void checkServerTrusted(X509Certificate[] certificates, String authType, SSLEngine sslEngine) {
		}
		@Override
		public X509Certificate[] getAcceptedIssuers() {
			return EMPTY_CERTIFICATES;
		}
	}
	
}
