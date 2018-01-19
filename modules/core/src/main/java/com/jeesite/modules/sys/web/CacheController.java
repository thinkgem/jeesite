package com.jeesite.modules.sys.web;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.jeesite.common.cache.CacheUtils;
import com.jeesite.common.config.Global;
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
		CacheUtils.clearCache();
		UserUtils.clearCache();
		Global.clearCache();
		return renderResult(Global.TRUE, "清理缓存成功！");
	}

}
