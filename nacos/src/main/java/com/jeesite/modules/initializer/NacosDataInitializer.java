/**
 * Copyright (c) 2013-Now https://jeesite.com All rights reserved.
 * No deletion without permission, or be held responsible to law.
 */
package com.jeesite.modules.initializer;

import com.alibaba.nacos.api.config.model.SameConfigPolicy;
import com.alibaba.nacos.api.exception.NacosException;
import com.alibaba.nacos.config.server.model.ConfigAllInfo;
import com.alibaba.nacos.config.server.model.event.ConfigDataChangeEvent;
import com.alibaba.nacos.config.server.service.ConfigCacheService;
import com.alibaba.nacos.config.server.service.ConfigChangePublisher;
import com.alibaba.nacos.config.server.service.repository.ConfigInfoPersistService;
import com.alibaba.nacos.plugin.auth.impl.persistence.User;
import com.alibaba.nacos.plugin.auth.impl.roles.NacosRoleService;
import com.alibaba.nacos.plugin.auth.impl.users.NacosUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.Environment;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 * Nacos 数据初始化器：初始化管理员密码、导入 jeesite-cloud-yml 配置。
 * <p>
 * 严格复制 Nacos HTTP 导入接口 {@code importAndPublishConfig} 的代码路径，
 * 使用 {@code batchInsertOrUpdate} 确保 DB 事务提交和事件发布与官方一致。
 * </p>
 */
public class NacosDataInitializer {

	private static final Logger logger = LoggerFactory.getLogger(NacosDataInitializer.class);

	/** JeeSite 微服务配置文件路径，Docker 挂载路径 */
	private static final String ZIP_DOCKER = "/app/jeesite-cloud-yml.zip";
	/** JeeSite 微服务配置文件路径，相对于 nacos 根目录的路径 */
	private static final String ZIP_CONFIG = "../nacos/db/config/jeesite-cloud-yml.zip";
	
	private static final String GROUP = "jeesite-cloud-yml";
	private static final String USERNAME = "nacos";

	public static void doInitialize(ConfigurableApplicationContext context, Environment env) {
		String adminPassword = env.getProperty("NACOS_ADMIN_PASSWORD",
				env.getProperty("nacos.admin.password", "nacos"));

		if (initAdminUser(context, adminPassword)) {
			return;
		}
		
		importConfigsFromZip(context, adminPassword);
	}

	// ==================== 初始化管理员 ====================

	private static boolean initAdminUser(ConfigurableApplicationContext context, String adminPassword) {
		NacosUserService userService = getBean(context, NacosUserService.class);
		User user = userService.getUser(USERNAME);
		if (user != null) {
			return true; // 已初始化
		}
		userService.createUser(USERNAME, adminPassword);
		NacosRoleService roleService = getBean(context, NacosRoleService.class);
		roleService.addAdminRole(USERNAME);
//		logger.info("[Nacos初始化] 管理员创建成功，角色分配完成");
		return false;
	}

	// ==================== 导入配置（复制 importAndPublishConfig 逻辑） ====================

	private static void importConfigsFromZip(ConfigurableApplicationContext context, String adminPassword) {
		ConfigInfoPersistService configService = getBean(context, ConfigInfoPersistService.class);

		// Docker 挂载路径优先；源码运行时通过 class 位置定位 nacos 目录
		InputStream zipStream = null;
		try {
			java.io.File zipFile = new java.io.File(ZIP_DOCKER);
			if (zipFile.exists()) {
				zipStream = new FileInputStream(zipFile);
			} else {
				// 通过 class 文件位置找到 nacos 模块根目录，再相对定位 config 模块的 zip
				java.io.File nacosDir = resolveNacosDir();
				if (nacosDir != null) {
					zipFile = new java.io.File(nacosDir, ZIP_CONFIG);
					if (zipFile.exists()) {
						zipStream = new FileInputStream(zipFile);
					}
				}
			}
		} catch (IOException e) {
			logger.warn("[Nacos初始化] 读取 ZIP 文件异常: {}", e.getMessage());
		}
		if (zipStream == null) {
			logger.info("[Nacos初始化] ========================================");
			logger.warn("[Nacos初始化] 未找到 ZIP 文件: {}", ZIP_CONFIG);
			logger.info("[Nacos初始化] 导入失败，请登录系统，用户名: {}  密码: {}", USERNAME, adminPassword);
			logger.warn("[Nacos初始化] 进入菜单：配置管理 -> 配置列表，点击 `导入配置` 按钮，弹出导入配置对话框");
			logger.warn("[Nacos初始化] 选择 `/nacos/db/config/jeesite-cloud-yml.zip` 上传");
			logger.info("[Nacos初始化] ========================================");
			return;
		}

		// 1. 从 ZIP 中解析出 ConfigAllInfo 列表
		List<ConfigAllInfo> configInfoList = new ArrayList<>();
		try (ZipInputStream zis = new ZipInputStream(zipStream, StandardCharsets.UTF_8)) {
			ZipEntry entry;
			while ((entry = zis.getNextEntry()) != null) {
				if (entry.isDirectory()) continue;
				String name = entry.getName();
				if (!name.startsWith(GROUP + "/") || !name.endsWith(".yml")) continue;

				String dataId = name.substring((GROUP + "/").length());
				String content = readZipEntryContent(zis);
				if (content.isEmpty()) continue;

				ConfigAllInfo info = new ConfigAllInfo();
				info.setDataId(dataId);
				info.setGroup(GROUP);
				info.setTenant("public");
				info.setContent(content);
				info.setType("yaml");
				configInfoList.add(info);
			}
		} catch (Exception e) {
			logger.error("[Nacos初始化] 读取 ZIP 异常: {}", e.getMessage(), e);
			return;
		}

		if (configInfoList.isEmpty()) {
			logger.warn("[Nacos初始化] ZIP 中无有效配置文件");
			return;
		}

		// 2. 批量写入数据库（与 importAndPublishConfig 完全一致）
		Timestamp time = new Timestamp(System.currentTimeMillis());
		try {
			configService.batchInsertOrUpdate(configInfoList, USERNAME, "127.0.0.1", null, SameConfigPolicy.SKIP);
		} catch (NacosException e) {
			logger.error("[Nacos初始化] batchInsertOrUpdate 异常: {}", e.getMessage(), e);
		}

		// 3. 发布 ConfigDataChangeEvent + 同步刷新缓存
		int imported = 0;
		for (ConfigAllInfo configInfo : configInfoList) {
			try {
				ConfigChangePublisher.notifyConfigChange(
						new ConfigDataChangeEvent(configInfo.getDataId(), configInfo.getGroup(),
								configInfo.getTenant(), time.getTime()));
				// 同步刷新缓存：确保控制台 API 立即可查询到
				ConfigCacheService.dump(configInfo.getDataId(), configInfo.getGroup(),
						configInfo.getTenant(), configInfo.getContent(),
						time.getTime(), configInfo.getType(), null);
//				logger.info("[Nacos初始化] [导入] {} -> 成功", configInfo.getDataId());
				imported++;
			} catch (Exception e) {
				logger.warn("[Nacos初始化] [失败] {} -> {}", configInfo.getDataId(), e.getMessage());
			}
		}

		logger.info("[Nacos初始化] ========================================");
		logger.info("[Nacos初始化] 初始配置，导入完成！成功 {} / 总计 {}", imported, configInfoList.size());
		logger.info("[Nacos初始化] 登录 Nacos，用户名: {}  密码: {}", USERNAME, adminPassword);
		logger.info("[Nacos初始化] ========================================");
	}

	// ==================== 工具 ====================

	/**
	 * 通过 class 文件实际位置反推 nacos 模块根目录。
	 * IDE 中 class 位于 target/classes/ 下，向上 2 级即为 nacos 目录。
	 */
	private static java.io.File resolveNacosDir() {
		try {
			java.net.URL location = NacosDataInitializer.class.getProtectionDomain()
					.getCodeSource().getLocation();
			String path = URLDecoder.decode(location.getPath(), StandardCharsets.UTF_8);
			java.io.File dir = new java.io.File(path);
			if (dir.isDirectory()) {
				// target/classes → target → nacos
				java.io.File nacos = dir.getParentFile().getParentFile();
				if (nacos != null && nacos.isDirectory()) {
					return nacos;
				}
			}
		} catch (Exception ignored) {
			// 非文件系统（如 JAR 包内）回退到 Docker 路径
		}
		return null;
	}

	private static String readZipEntryContent(ZipInputStream zis) throws java.io.IOException {
		StringBuilder sb = new StringBuilder();
		byte[] buf = new byte[4096];
		int len;
		while ((len = zis.read(buf)) != -1) {
			sb.append(new String(buf, 0, len, StandardCharsets.UTF_8));
		}
		return sb.toString();
	}

	private static <T> T getBean(ConfigurableApplicationContext context, Class<T> clazz) {
		try {
			return context.getBean(clazz);
		} catch (Exception e) {
			throw new RuntimeException("[Nacos初始化] 获取 Bean [" + clazz.getSimpleName() + "] 失败: " + e.getMessage());
		}
	}
}
