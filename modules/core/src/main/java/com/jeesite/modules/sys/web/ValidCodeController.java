/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 * No deletion without permission, or be held responsible to law.
 */
package com.jeesite.modules.sys.web;

import com.jeesite.common.config.Global;
import com.jeesite.modules.sys.utils.UserUtils;
import com.jeesite.modules.sys.utils.ValidCodeUtils;
import io.swagger.annotations.Api;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.session.Session;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 验证码控制器
 * @author ThinkGem
 * @version 2019年12月17日
 */
@Controller
@Api(tags = "ValidCode - 验证码服务")
public class ValidCodeController {
	
	@RequestMapping(value="/validCode")
	public void validCode(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// 如果传递了validCode参数，则代表是验证方法，成功返回true，失败返回false
		String validCode = request.getParameter(ValidCodeUtils.VALID_CODE);
		if (StringUtils.isNotBlank(validCode)){
			boolean result = ValidCodeUtils.validate(request, ValidCodeUtils.VALID_CODE, validCode, false);
			response.getOutputStream().print(result ? Global.TRUE : Global.FALSE);
		}
		else{
			// 生成会话
			Session session = UserUtils.getSession();
			// 设置响应头
			response.setContentType("image/png");
	        response.setHeader("Cache-Control", "no-cache, no-store");
	        response.setHeader("Pragma", "no-cache");
	        long time = System.currentTimeMillis();
	        response.setDateHeader("Last-Modified", time);
	        response.setDateHeader("Date", time);
	        response.setDateHeader("Expires", time);
	        // 生成输出验证码
	        String s = ValidCodeUtils.generateCaptcha(response.getOutputStream());
			session.setAttribute(ValidCodeUtils.VALID_CODE, s);
//	        System.out.println(s);
		}
	}
	
}
