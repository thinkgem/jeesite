/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.modules.sys.web;

import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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

import com.jeesite.common.cache.CacheUtils;
import com.jeesite.common.collect.ListUtils;
import com.jeesite.common.collect.MapUtils;
import com.jeesite.common.config.Global;
import com.jeesite.common.lang.DateUtils;
import com.jeesite.common.lang.ObjectUtils;
import com.jeesite.common.lang.StringUtils;
import com.jeesite.common.lang.TimeUtils;
import com.jeesite.common.shiro.realm.LoginInfo;
import com.jeesite.common.shiro.session.SessionDAO;
import com.jeesite.common.web.BaseController;
import com.jeesite.modules.sys.utils.UserUtils;

/**
 * 在线用户Controller
 * @author ThinkGem
 * @version 2016-8-31
 */
@Controller
@RequestMapping(value = "${adminPath}/sys/online")
@ConditionalOnProperty(name="web.core.enabled", havingValue="true", matchIfMissing=true)
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
		return sessionDAO.getActiveSessions(true, true).size();
	}

	/**
	 * 在线用户列表
	 * @param request
	 * @param response
	 * @param model
	 */
	@RequiresPermissions("sys:online:view")
	@RequestMapping(value = "list")
	public String list(Model model) {
		return "modules/sys/onlineList";
	}
	
	/**
	 * 在线用户列表数据
	 * @param request
	 * @param response
	 * @author ThinkGem
	 */
	@RequiresPermissions("sys:online:view")
	@RequestMapping(value = "listData")
	@ResponseBody
	public List<Map<String, Object>> listData(String isAllOnline, String isVisitor, String sessionId, 
			String userCode, String userName, String userType, String orderBy) {
		List<Map<String, Object>> list = ListUtils.newArrayList();
		boolean excludeLeave = isAllOnline==null || !Global.YES.equals(isAllOnline);
		boolean excludeVisitor = isVisitor==null || !Global.YES.equals(isVisitor);
 		Collection<Session> sessions = sessionDAO.getActiveSessions(excludeLeave, 
				excludeVisitor, null, sessionId, userCode);
		long currentTime = System.currentTimeMillis();
		for (Session session : sessions){
			if (StringUtils.isNotBlank(userName) && ((String)session.getAttribute("userName")).contains(userName)){
				continue;
			}
			if (StringUtils.isNotBlank(userType) && ((String)session.getAttribute("userType")).equals(userType)){
				continue;
			}
			Map<String, Object> map = MapUtils.newLinkedHashMap();
			// 为了安全性，需要有权限的人才能看
			if (UserUtils.getSubject().isPermitted("sys:online:edit")){
				map.put("id", session.getId().toString()); 
			}
			map.put("startTimestamp", DateUtils.formatDateTime(session.getStartTimestamp()));
			map.put("lastAccessTime", DateUtils.formatDateTime(session.getLastAccessTime()));
			map.put("timeout", TimeUtils.formatDateAgo(session.getTimeout()-(currentTime-session.getLastAccessTime().getTime())));
			Object pc = session.getAttribute(DefaultSubjectContext.PRINCIPALS_SESSION_KEY);
			if (pc != null && pc instanceof PrincipalCollection){
				LoginInfo loginInfo = (LoginInfo)((PrincipalCollection)pc).getPrimaryPrincipal();
				if (loginInfo != null){
					map.put("userCode", session.getAttribute("userCode"));// principal.getId());
					map.put("userName", session.getAttribute("userName"));// principal.getName());
					map.put("userType", session.getAttribute("userType"));// ObjectUtils.toString(principal.getParam("userType")));
					map.put("deviceType", ObjectUtils.toString(loginInfo.getParam("deviceType")));
				}
			}
			map.put("host", session.getHost());
			list.add(map);
		}
		// 本地排序
		if (StringUtils.isNotBlank(orderBy)){
			final String[] ss = orderBy.trim().split(" ");
			if (ss != null && ss.length == 2){
				Collections.sort(list, new Comparator<Map<String, Object>>() {
					@Override
					public int compare(Map<String, Object> o1, Map<String, Object> o2) {
						String s1 = (String)o1.get(ss[0]);
						String s2 = (String)o2.get(ss[0]);
						if ("asc".equals(ss[1])){
							return s1.compareTo(s2);
						}else{
							return s2.compareTo(s1);
						}
					}});
			}
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
	public String tickOut(String sessionId) {
		Session session = sessionDAO.readSession(sessionId);
		if (session != null){
			Map<String, String> onlineTickOutMap = CacheUtils.get("onlineTickOutMap");
			if (onlineTickOutMap == null){
				onlineTickOutMap = MapUtils.newConcurrentMap();
			}
			Object pc = session.getAttribute(DefaultSubjectContext.PRINCIPALS_SESSION_KEY);
			if (pc != null && pc instanceof PrincipalCollection){
				LoginInfo loginInfo = (LoginInfo)((PrincipalCollection)pc).getPrimaryPrincipal();
				if (loginInfo != null){
					String key = loginInfo.getId()+"_"+loginInfo.getParam("deviceType", "PC");
					onlineTickOutMap.put(key, StringUtils.EMPTY);
				}
			}
			CacheUtils.put("onlineTickOutMap", onlineTickOutMap);
			sessionDAO.delete(session);
			return renderResult(Global.TRUE, "踢出已成功！");
		}
		return renderResult(Global.FALSE, "踢出失败，没有找到该在线用户！");
	}
	
}
