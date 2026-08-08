package com.jeesite.common.ueditor;

import com.jeesite.common.io.IOUtils;
import com.jeesite.common.io.ResourceUtils;
import com.jeesite.common.lang.ExceptionUtils;
import com.jeesite.common.lang.StringUtils;
import com.jeesite.common.mapper.JsonMapper;
import com.jeesite.common.ueditor.define.ActionMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 配置管理器
 *
 * @author hancong03@baidu.com
 */
public final class ConfigManager {

	private static final Logger logger = LoggerFactory.getLogger(ConfigManager.class);
    private final String rootPath;
    private static final String defaultConfigFileName = "config/ueditor-core.json";
    private static final String configFileName = "config/ueditor.json";
    private Map<String, Object> jsonConfig = null;
    // 涂鸦上传filename定义
    private final static String SCRAWL_FILE_NAME = "scrawl";
    // 远程图片抓取filename定义
    private final static String REMOTE_FILE_NAME = "remote";

    /*
     * 通过一个给定的路径构建一个配置管理器， 该管理器要求地址路径所在目录下必须存在config.properties文件
     */
    private ConfigManager(String rootPath, String contextPath, String uri) throws FileNotFoundException, IOException {
        rootPath = rootPath.replace("\\", "/");
        this.rootPath = rootPath;
        this.initEnv();
    }

    /**
     * 配置管理器构造工厂
     *
     * @param rootPath    服务器根路径
     * @param contextPath 服务器所在项目路径
     * @param uri         当前访问的uri
     * @return 配置管理器实例或者null
     */
    public static ConfigManager getInstance(String rootPath, String contextPath, String uri) {
        try {
            return new ConfigManager(rootPath, contextPath, uri);
        } catch (Exception e) {
            logger.debug(e.getMessage(), e);
            return null;
        }

    }

    // 验证配置文件加载是否正确
    public boolean valid() {
        return this.jsonConfig != null;
    }

    public String getAllConfig() {
        return JsonMapper.toJson(this.jsonConfig);
    }

    public Map<String, Object> getConfig(int type) {
        Map<String, Object> conf = new HashMap<String, Object>();
        String savePath = null;
        switch (type) {
        	case ActionMap.UPLOAD_FILE:
        		conf.put("isBase64", "false");
        		conf.put("maxSize", this.getLong("fileMaxSize"));
        		conf.put("allowFiles", this.getArray("fileAllowFiles"));
        		conf.put("fieldName", this.getString("fileFieldName"));
        		savePath = this.getString("filePathFormat");
        		break;
        	case ActionMap.UPLOAD_IMAGE:
        		conf.put("isBase64", "false");
        		conf.put("maxSize", this.getLong("imageMaxSize"));
        		conf.put("allowFiles", this.getArray("imageAllowFiles"));
        		conf.put("fieldName", this.getString("imageFieldName"));
        		conf.put("imageCompressEnable", this.getBoolean("imageCompressEnable"));
        		conf.put("imageCompressBorder", this.getInteger("imageCompressBorder"));
        		savePath = this.getString("imagePathFormat");
        		break;
        	case ActionMap.UPLOAD_VIDEO:
        		conf.put("maxSize", this.getLong("videoMaxSize"));
        		conf.put("allowFiles", this.getArray("videoAllowFiles"));
        		conf.put("fieldName", this.getString("videoFieldName"));
        		savePath = this.getString("videoPathFormat");
        		break;
        	case ActionMap.UPLOAD_SCRAWL:
        		conf.put("filename", ConfigManager.SCRAWL_FILE_NAME);
        		conf.put("maxSize", this.getLong("scrawlMaxSize"));
        		conf.put("fieldName", this.getString("scrawlFieldName"));
        		conf.put("isBase64", "true");
        		savePath = this.getString("scrawlPathFormat");
        		break;
        	case ActionMap.CATCH_IMAGE:
        		conf.put("filename", ConfigManager.REMOTE_FILE_NAME);
        		conf.put("filter", this.getArray("catcherLocalDomain"));
        		conf.put("maxSize", this.getLong("catcherMaxSize"));
        		conf.put("allowFiles", this.getArray("catcherAllowFiles"));
        		conf.put("fieldName", this.getString("catcherFieldName") + "[]");
        		savePath = this.getString("catcherPathFormat");
        		break;
        	case ActionMap.LIST_IMAGE:
        		conf.put("allowFiles", this.getArray("imageManagerAllowFiles"));
        		conf.put("dir", this.getString("imageManagerListPath"));
        		conf.put("count", this.getInteger("imageManagerListSize"));
        		break;
        	case ActionMap.LIST_FILE:
        		conf.put("allowFiles", this.getArray("fileManagerAllowFiles"));
        		conf.put("dir", this.getString("fileManagerListPath"));
        		conf.put("count", this.getInteger("fileManagerListSize"));
        		break;
        }
        conf.put("actionCode", type);
        conf.put("savePath", savePath);
        conf.put("rootPath", this.rootPath);
        return conf;
    }

    private void initEnv() {
		String configContent = StringUtils.EMPTY;
		Resource resource = ResourceUtils.getResource(ConfigManager.configFileName);
		if (resource.exists()) {
			try(InputStream is = resource.getInputStream()){
				configContent = IOUtils.toString(is, StandardCharsets.UTF_8);
			}catch (IOException e) {
				throw ExceptionUtils.unchecked(e);
			}
		} else {
			resource = ResourceUtils.getResource(ConfigManager.defaultConfigFileName);
			if (resource.exists()) {
				try(InputStream is = resource.getInputStream()){
					configContent = IOUtils.toString(is, StandardCharsets.UTF_8);
				}catch (IOException e) {
					throw ExceptionUtils.unchecked(e);
				}
			}
		}
        try {
			configContent = configContent.replaceAll("/\\*[\\s\\S]*?\\*/", "");
			this.jsonConfig = JsonMapper.fromJson(configContent, Map.class);
        } catch (Exception e) {
            this.jsonConfig = null;
        }
    }

	private String getString(String key) {
		Object value = this.jsonConfig.get(key);
		return value != null ? value.toString() : null;
	}

	private Integer getInteger(String key) {
		Object value = this.jsonConfig.get(key);
		if (value instanceof Number num) {
			return num.intValue();
		} else if (value instanceof String str) {
			try {
				return Integer.parseInt(str);
			} catch (NumberFormatException e) {
				return null;
			}
		}
		return null;
	}

	private Long getLong(String key) {
		Object value = this.jsonConfig.get(key);
		if (value instanceof Number num) {
			return num.longValue();
		} else if (value instanceof String str) {
			try {
				return Long.parseLong(str);
			} catch (NumberFormatException e) {
				return null;
			}
		}
		return null;
	}

	private Boolean getBoolean(String key) {
		Object value = this.jsonConfig.get(key);
		if (value instanceof Boolean b) {
			return b;
		} else if (value instanceof String str) {
			return "true".equalsIgnoreCase(str);
		}
		return null;
	}

	private String[] getArray(String key) {
		Object value = this.jsonConfig.get(key);
		if (value instanceof String[]) {
			return (String[]) value;  // 直接返回
		} else if (value instanceof List<?> list) {
			return list.stream()
					.map(item -> item != null ? item.toString() : null)
					.toArray(String[]::new);
		}
		return new String[0];
	}

}
