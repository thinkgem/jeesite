package com.jeesite.modules.sys.web;

import javax.annotation.PostConstruct;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.jeesite.common.cache.CacheUtils;
import com.jeesite.common.config.Global;
import com.jeesite.common.lang.StringUtils;
import com.jeesite.common.mybatis.mapper.MapperHelper;
import com.jeesite.common.web.BaseController;
import com.jeesite.modules.sys.utils.UserUtils;

/**
 * 缓存管理
 * @author ThinkGem
 * @version 20167-8-27
 */
@Controller
@RequestMapping(value = "${adminPath}/sys/cache")
public class CacheController extends BaseController {

	/**
	 * 清理全部缓存，可清理属性文件里的缓存
	 * @return
	 */
	@RequiresPermissions("sys:config:edit")
	@RequestMapping(value = "clearAll")
	@ResponseBody
	public String clearAll() {
		Global.clearCache();
		CacheUtils.clearCache();
		MapperHelper.clearCache();
		UserUtils.clearCache();
		return renderResult(Global.TRUE, "清理缓存成功！");
	}
	
	@PostConstruct
	public void postConstruct(){
		String rebel = System.getProperty("rebel.base");
		if (StringUtils.isNotBlank(rebel) && Global
				.getPropertyToBoolean("spring.cache.isClusterMode", "false")){
			logger.info("JRebel: Cache clear...");
			CacheUtils.clearCache();
		}
	}

}
