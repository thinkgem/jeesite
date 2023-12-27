/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 * No deletion without permission, or be held responsible to law.
 */
package com.jeesite.modules.sys.web;

import com.jeesite.common.collect.ListUtils;
import com.jeesite.common.collect.MapUtils;
import com.jeesite.common.config.Global;
import com.jeesite.common.lang.DateUtils;
import com.jeesite.common.lang.StringUtils;
import com.jeesite.common.lang.TimeUtils;
import com.jeesite.common.shiro.realm.LoginInfo;
import com.jeesite.common.shiro.session.SessionDAO;
import com.jeesite.common.web.BaseController;
import com.jeesite.modules.sys.utils.SysCacheUtils;
import com.jeesite.modules.sys.utils.UserUtils;
import io.swagger.annotations.Api;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.support.DefaultSubjectContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

/**
 * 在线用户Controller
 * @author ThinkGem
 * @version 2022-3-10
 */
@Controller
@Api(tags = "Online - 在线用户")
@RequestMapping(value = "${adminPath}/sys/online")
@ConditionalOnProperty(name={"user.enabled","web.core.enabled"}, havingValue="true", matchIfMissing=true)
public class OnlineController extends BaseController{

	@Autowired
	private SessionDAO sessionDAO;

	/**
	 * 在线用户数
	 * @param request
	 * @param response
	 * @author ThinkGem
	 */
	@RequestMapping(value = "count")
	@ResponseBody
	public Integer count(HttpServletRequest request, HttpServletResponse response) {
		// 在线人数统计，缓存3分钟的数据，要求高的业务可缩小间隔时间
		return sessionDAO.getActiveSessionsCount(true, true, 3);
	}

	/**
	 * 在线用户列表
	 * @param model
	 */
	@RequiresPermissions("sys:online:view")
	@RequestMapping(value = "list")
	public String list(Model model) {
		return "modules/sys/onlineList";
	}

	/**
	 * 在线用户列表数据
	 * @author ThinkGem
	 */
	@RequiresPermissions("sys:online:view")
	@RequestMapping(value = "listData")
	@ResponseBody
	public List<Map<String, Object>> listData(String isAllOnline, String isVisitor, String sessionId,
			String userCode, String userName, String userType, String orderBy) {
		List<Map<String, Object>> list = ListUtils.newArrayList();
		boolean excludeLeave = !Global.YES.equals(isAllOnline);
		boolean excludeVisitor = !Global.YES.equals(isVisitor);
 		Collection<Session> sessions = sessionDAO.getActiveSessions(excludeLeave,
				excludeVisitor, null, sessionId, userCode);
		long currentTime = System.currentTimeMillis();
		for (Session session : sessions){
			if (StringUtils.isNotBlank(userName) && !StringUtils.contains((String)session.getAttribute("userName"), userName)){
				continue;
			}
			if (StringUtils.isNotBlank(userType) && !StringUtils.equals((String)session.getAttribute("userType"), userType)){
				continue;
			}
			Map<String, Object> map = MapUtils.newLinkedHashMap();
			// 为了安全性，需要有权限的人才能看
			if (UserUtils.getSubject().isPermitted("sys:online:edit")){
				map.put("id", session.getId().toString());
			}
			map.put("startTimestamp", DateUtils.formatDateTime(session.getStartTimestamp()));
			map.put("lastAccessTime", DateUtils.formatDateTime(session.getLastAccessTime()));
			map.put("timeoutLong", session.getTimeout()-(currentTime-session.getLastAccessTime().getTime()));
			map.put("timeout", TimeUtils.formatTime(session.getTimeout()-(currentTime-session.getLastAccessTime().getTime())));
			map.put("userCode", session.getAttribute("userCode"));
			map.put("userName", session.getAttribute("userName"));
			map.put("userType", session.getAttribute("userType"));
			map.put("deviceType", session.getAttribute("deviceType"));
			map.put("host", session.getHost());
			list.add(map);
		}
		// 本地排序
		if (StringUtils.isBlank(orderBy)){
			orderBy = "lastAccessTime desc";
		}
		orderBy = StringUtils.replace(orderBy, "timeout", "timeoutLong");
		final String[] ss = orderBy.trim().split(" ");
		if (ss.length == 2){
			list.sort((o1, o2) -> {
				Object s1 = o1.get(ss[0]);
				Object s2 = o2.get(ss[0]);
				if (s1 == null || s2 == null) {
					return -1;
				}
				if (StringUtils.endsWith(ss[0], "Long")) {
					if ("asc".equals(ss[1])) {
						return ((Long)s1).compareTo((Long)s2);
					} else {
						return ((Long)s2).compareTo((Long)s1);
					}
				}else{
					if ("asc".equals(ss[1])) {
						return ((String)s1).compareTo((String)s2);
					} else {
						return ((String)s2).compareTo((String)s1);
					}
				}
			});
		}
		return list;
	}
	
	/**
	 * 提出在线用户
	 * @author ThinkGem
	 */
	@RequiresPermissions("sys:online:edit")
	@RequestMapping(value = "tickOut")
	@ResponseBody
	public String kickOut(String sessionId) {
		Session session = sessionDAO.readSession(sessionId);
		if (session != null){
			Map<String, String> onlineTickOutMap = SysCacheUtils.get("onlineTickOutMap");
			if (onlineTickOutMap == null){
				onlineTickOutMap = MapUtils.newConcurrentMap();
			}
			Object pc = session.getAttribute(DefaultSubjectContext.PRINCIPALS_SESSION_KEY);
			if (pc != null && pc instanceof PrincipalCollection){
				Object pp = ((PrincipalCollection)pc).getPrimaryPrincipal();
				if (pp != null) {
					if (pp instanceof LoginInfo){
						LoginInfo loginInfo = ((LoginInfo)pp);
						String key = loginInfo.getId()+"_"+loginInfo.getParam("deviceType", "pc");
						onlineTickOutMap.put(key, StringUtils.EMPTY);
					}
				}
			}
			SysCacheUtils.put("onlineTickOutMap", onlineTickOutMap);
			sessionDAO.delete(session);
			return renderResult(Global.TRUE, text("踢出已成功！"));
		}
		return renderResult(Global.FALSE, text("踢出失败，没有找到该在线用户！"));
	}
	
}
