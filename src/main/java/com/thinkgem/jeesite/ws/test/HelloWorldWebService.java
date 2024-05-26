package com.thinkgem.jeesite.ws.test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.jws.WebParam;
import javax.jws.WebService;

import com.thinkgem.jeesite.modules.sys.entity.User;

@WebService
public interface HelloWorldWebService {

	// 注意接口需要定义@WebParam注解，否则WSDL中的接口请求参数名称会变成arg0, arg1...
	String sayHi(@WebParam(name = "text") String text);

	List<WsResponseUser> userList();
}