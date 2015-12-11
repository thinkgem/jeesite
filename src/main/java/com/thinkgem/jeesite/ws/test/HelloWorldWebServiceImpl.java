package com.thinkgem.jeesite.ws.test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.jws.WebParam;
import javax.jws.WebService;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.thinkgem.jeesite.common.utils.SpringContextHolder;
import com.thinkgem.jeesite.modules.sys.dao.UserDao;
import com.thinkgem.jeesite.modules.sys.entity.User;

/***
 * 服务发布后可通过 http://localhost:8080/jeesite/webservice/HelloWorld?wsdl访问
 * 
 * */

@WebService()
public class HelloWorldWebServiceImpl implements HelloWorldWebService {

	public String sayHi(String text) {
		System.out.println("sayHi called");
		return "Hello " + text;
	}

	@Override
	public List<WsResponseUser> userList() {
		UserDao userDao = SpringContextHolder.getBean(UserDao.class);
		User query = new User();
		query.getPage().setPageNo(1);
		query.getPage().setPageSize(100);
		List<User> users = userDao.findList(query);
		List<WsResponseUser> ret = Lists.newArrayList();
		for (User user : users) {
			WsResponseUser resp = new WsResponseUser(user.getName(),
					user.getNo(), user.getLoginName());
			ret.add(resp);
		}
		return ret;
	}

}