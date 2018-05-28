/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.modules.sys.web;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.jeesite.common.entity.Page;
import com.jeesite.common.lang.DateUtils;
import com.jeesite.common.web.BaseController;
import com.jeesite.modules.sys.entity.Log;
import com.jeesite.modules.sys.service.LogService;

/**
 * 日志Controller
 * @author ThinkGem
 * @version 2013-6-2
 */
@Controller
@RequestMapping(value = "${adminPath}/sys/log")
public class LogController extends BaseController {

	@Autowired
	private LogService logService;
	
	/**
     * 获取数据
     */
    @ModelAttribute
    public Log get(String id, boolean isNewRecord) {
        return logService.get(id, isNewRecord);
    }
    
	/**
     * 查询列表
     */
    @RequiresPermissions("sys:log:view")
    @RequestMapping(value = "list")
    public String list(Log log, Model model) {
        // 设置默认时间范围，默认当前月
        if (log.getCreateDate_gte() == null){
            log.setCreateDate_gte(DateUtils.setDays(new Date(), 1));
        }
        if (log.getCreateDate_lte() == null){
            log.setCreateDate_lte(DateUtils.addDays(DateUtils.addMonths(log.getCreateDate_gte(), 1), -1));
        }
        model.addAttribute("log", log);
        return "modules/sys/logList";
    }
    
    /**
     * 查询列表数据
     */
    @RequiresPermissions("sys:log:view")
    @RequestMapping(value = "listData")
    @ResponseBody
    public Page<Log> listData(Log log, HttpServletRequest request, HttpServletResponse response) {
        Page<Log> page = logService.findPage(new Page<Log>(request, response), log); 
        return page;
    }
    
    /**
     * 查看编辑表单
     */
    @RequiresPermissions("sys:log:view")
    @RequestMapping(value = "form")
    public String form(Log log, Model model) {
        model.addAttribute("log", log);
        return "modules/sys/logForm";
    }
	
}
