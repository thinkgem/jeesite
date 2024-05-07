/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 * No deletion without permission, or be held responsible to law.
 */
package com.jeesite.common.io;

import com.jeesite.common.codec.EncodeUtils;
import com.jeesite.common.collect.SetUtils;
import com.jeesite.common.lang.ObjectUtils;
import com.jeesite.common.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.YamlPropertiesFactoryBean;
import org.springframework.core.env.Environment;
import org.springframework.core.io.Resource;

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.function.Consumer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Properties工具类， 可载入多个properties、yml文件，
 * 相同的属性在最后载入的文件中的值将会覆盖之前的值， 
 * 取不到从System.getProperty()获取。
 * @author ThinkGem
 * @version 2017-12-30
 */
public class PropertiesUtils {
	
	// 默认加载的文件，可通过继承覆盖（若有相同Key，优先加载后面的）
	public static final String[] DEFAULT_CONFIG_FILE = new String[]{
		"classpath:application.yml", "classpath:config/application.yml",
		"classpath:bootstrap.yml", "classpath:config/bootstrap.yml",
		"file:application.yml", "file:config/application.yml",
		"file:bootstrap.yml", "file:config/bootstrap.yml",
	};

	private static Logger logger = PropertiesUtils.initLogger();
	private final Set<String> configSet = SetUtils.newLinkedHashSet();
	private final Properties properties = new Properties();
	private static Environment environment;
	
	/**
	 * 当前类的实例持有者（静态内部类，延迟加载，懒汉式，线程安全的单例模式）
	 */
	private static final class PropertiesLoaderHolder {
		private static PropertiesUtils INSTANCE;
		static {
			releadInstance();
		}
		public static void releadInstance(){
			// 获取平台及模块相关的配置文件
			Set<String> configSet = SetUtils.newLinkedHashSet();
			Resource[] resources = ResourceUtils.getResources("classpath*:/config/jeesite-*.*");
			for(Resource resource : resources){
				configSet.add("classpath:config/"+resource.getFilename());
			}
			//configSet.add("classpath:config/jeesite.yml");
			// 获取全局设置默认的配置文件（以下是支持环境配置的属性文件）
			Set<String> set = SetUtils.newLinkedHashSet();
			for (String configFile : DEFAULT_CONFIG_FILE){
				set.add(configFile);
			}
			// 获取 spring.config.location 外部自定义的配置文件
			String customConfigs = System.getProperty("spring.config.location");
			if (StringUtils.isNotBlank(customConfigs)){
				for (String customConfig : StringUtils.splitComma(customConfigs)){
					if (!customConfig.contains("$")){
						customConfig = org.springframework.util.StringUtils.cleanPath(customConfig);
						if (!ResourceUtils.isUrl(customConfig)){
							customConfig = ResourceUtils.FILE_URL_PREFIX + customConfig;
						}
					}
					set.add(customConfig);
				}
			}
			// 获取 spring.profiles.active 活动环境名称的配置文件
			String[] configFiles = set.toArray(new String[set.size()]);
			String profiles = System.getProperty("spring.profiles.active");
			if (StringUtils.isBlank(profiles)){
				PropertiesUtils propsTemp = new PropertiesUtils(configFiles);
				profiles = propsTemp.getProperty("spring.profiles.active");
			}
			for (String location : configFiles){
				configSet.add(location);
				if (StringUtils.isNotBlank(profiles)){
					for (String pf : StringUtils.splitComma(profiles)) {
						if (location.endsWith(".properties")){
							configSet.add(StringUtils.substringBeforeLast(location, ".properties")
									+ "-" + pf + ".properties");
						}else if (location.endsWith(".yml")){
							configSet.add(StringUtils.substringBeforeLast(location, ".yml")
									+ "-" + pf + ".yml");
						}
					}
				}
			}
			configFiles = configSet.toArray(new String[configSet.size()]);
			logger.debug("Trying: {}", (Object)configFiles);
			INSTANCE = new PropertiesUtils(configFiles);
		}
	}

	/**
	 * 载入多个文件，路径使用Spring Resource格式，相同的属性在最后载入的文件中的值将会覆盖之前的值。
	 */
	public PropertiesUtils(String... configFiles) {
		for (String location : configFiles) {
			Resource resource = ResourceUtils.getResource(location);
			if (resource.exists()){
    			if (location.endsWith(".properties")){
    				try (InputStreamReader is = new InputStreamReader(resource.getInputStream(), EncodeUtils.UTF_8)){
    					properties.load(is);
    					configSet.add(location);
        			} catch (IOException e) {
            			System.err.println("Load " + location + " failure.");
            			e.printStackTrace();
        			}
    			}
    			else if (location.endsWith(".yml")){
    				try {
        				YamlPropertiesFactoryBean bean = new YamlPropertiesFactoryBean();
        				bean.setResources(resource);
        				for (Map.Entry<Object,Object> entry : bean.getObject().entrySet()){
        					properties.put(getStandardKey(ObjectUtils.toString(entry.getKey())),
        							ObjectUtils.toString(entry.getValue()));
        				}
    					configSet.add(location);
    				} catch (Exception e) {
    	    			System.err.println("Load " + location + " failure.");
    	    			e.printStackTrace();
    				}
    			}
			}
		}
		properties.put("configFiles", StringUtils.joinComma(configFiles));
	}
	
	/**
	 * 获取当前加载的属性文件
	 */
	public Set<String> getConfigSet() {
		return configSet;
	}
	
	/**
	 * 获取当前加载的属性数据
	 */
	public Properties getProperties() {
		return properties;
	}
	
	/**
	 * 当前类实例
	 */
	public static PropertiesUtils getInstance(){
		return PropertiesLoaderHolder.INSTANCE;
	}
	
	/**
	 * 重新加载实例（重新实例化，以重新加载属性文件数据）
	 */
	public static void releadInstance(){
		PropertiesLoaderHolder.releadInstance();
	}
	
	// 正则表达式预编译
	private static final Pattern p1 = Pattern.compile("\\$\\{.*?\\}");

	/**
	 * 获取属性值，取不到从System.getProperty()获取，都取不到返回null
	 */
	public String getProperty(String key) {
		if (environment != null){
			String value = environment.getProperty(key);
			if (value != null){
				return value;
			}
		}
        String value = properties.getProperty(getStandardKey(key));
        if (value != null){
	    	Matcher m = p1.matcher(value);
	        while(m.find()) {
	            String g = m.group();
	            String childKey = g.replaceAll("\\$\\{|\\}", "");
	            value = StringUtils.replace(value, g, getProperty(childKey));
	        }
	        return value;
	    }else{
	    	String systemProperty = System.getProperty(key);
			if (systemProperty != null) {
				return systemProperty;
			}
	    }
		return null;
	}

	/**
	 * 获取配置文件中String类型的值，但以System的Property优先，如果都为null则返回defaultValue值
	 */
	public String getProperty(String key, String defaultValue) {
		String value = getProperty(key);
		return value != null ? value : defaultValue;
	}

	/**
	 * 获取配置文件中String类型的值，取不到从System.getProperty获取，取不到，返回空。
	 * 对属性值进行 “,“ 逗号进行 split 分割，并返回
	 * @return 获取不到，返回空defValue默认值
	 */
	public String[] getPropertyToArray(String key, String defValue) {
		return StringUtils.splitComma(getProperty(key, defValue));
	}

	/**
	 * 获取配置文件中Boolean类型的值，取不到从System.getProperty获取，取不到，返回空。
	 * @return 获取不到，返回空defValue默认值，如果结果为非布尔类型，则返回false
	 */
	public Boolean getPropertyToBoolean(String key, String defValue) {
		return ObjectUtils.toBoolean(getProperty(key, defValue));
	}
	
	/**
	 * 获取配置文件中Integer类型的值，取不到从System.getProperty获取，取不到，返回空。
	 * @return 获取不到，返回空defValue默认值，如果结果为非数值类型，则返回0
	 */
	public Integer getPropertyToInteger(String key, String defValue) {
		return ObjectUtils.toInteger(getProperty(key, defValue));
	}
	
	/**
	 * 获取配置文件中Long类型的值，取不到从System.getProperty获取，取不到，返回空。
	 * @return 获取不到，返回空defValue默认值，如果结果为非数值类型，则返回0
	 */
	public Long getPropertyToLong(String key, String defValue) {
		return ObjectUtils.toLong(getProperty(key, defValue));
	}

	/**
	 * 获取配置文件中的值，如果存在则 Consumer
	 * @author ThinkGem
	 */
	public void getPropertyIfNotBlank(String key, Consumer<String> consumer) {
		String value = getProperty(key, StringUtils.EMPTY);
		if (StringUtils.isNotBlank(value)) {
			consumer.accept(value);
		}
	}

	/**
	 * 获取配置文件中的值，如果存在则转换为 String[] 并 Consumer
	 */
	public void getPropertyToArrayIfNotBlank(String key, Consumer<String[]> consumer) {
		getPropertyIfNotBlank(key, value -> {
			consumer.accept(StringUtils.splitComma(value));
		});
	}

	/**
	 * 获取配置文件中的值，如果存在则转换为 Boolean 并 Consumer
	 */
	public void getPropertyToBooleanIfNotBlank(String key, Consumer<Boolean> consumer) {
		getPropertyIfNotBlank(key, value -> {
			consumer.accept(ObjectUtils.toBoolean(value));
		});
	}

	/**
	 * 获取配置文件中的值，如果存在则转换为 Integer 并 Consumer
	 */
	public void getPropertyToIntegerIfNotBlank(String key, Consumer<Integer> consumer) {
		getPropertyIfNotBlank(key, value -> {
			consumer.accept(ObjectUtils.toInteger(value));
		});
	}

	/**
	 * 获取配置文件中的值，如果存在则转换为 Long 并 Consumer
	 */
	public void getPropertyToLongIfNotBlank(String key, Consumer<Long> consumer) {
		getPropertyIfNotBlank(key, value -> {
			consumer.accept(ObjectUtils.toLong(value));
		});
	}
	
	/**
	 * 获取标准key，去减号并将后一个字母转换为大写
	 * @author Think Gem
	 */
	private String getStandardKey(String key) {
		if (key.startsWith("spring.shardingsphere.")) {
			return key;
		}
        StringBuilder sb = new StringBuilder();
        boolean upperCase = false;
        for (int i = 0; i < key.length(); i++) {
            char c = key.charAt(i);
            if (c == '-') {
                upperCase = true;
            } else if (upperCase) {
                sb.append(Character.toUpperCase(c));
                upperCase = false;
            } else {
                sb.append(c);
            }
        }
        return sb.toString();
	}
	
	/**
	 * 设置环境属性
	 * @param environment
	 */
	public static void setEnvironment(Environment environment) {
		PropertiesUtils.environment = environment;
	}

	/**
	 * 初始化日志路径
	 */
	private static Logger initLogger(){
		String logPath = null;
		if (StringUtils.isNotBlank(System.getProperty("customLogPath"))){
			// 如果 Tomcat 下部署多个项目的时候 logPath 会出现项目之间串用问题，所以启用 customLogPath 名字
			logPath = System.getProperty("customLogPath");
		}else{
			try {
				// 获取当前classes目录
				logPath = ResourceUtils.getResource("/").getFile().getPath();
			} catch (Exception e) {
				// 取不到，取当前工作路径
				logPath = System.getProperty("user.dir");
			}
			// 取当前日志路径下有classes目录，则使用classes目录
			String classesLogPath = FileUtils.path(logPath + "/WEB-INF/classes");
			if (new File(classesLogPath).exists()){
				logPath = classesLogPath;
			}
		}
		System.setProperty("logPath", FileUtils.path(logPath));
		return LoggerFactory.getLogger(PropertiesUtils.class);
	}
	
}
