package com.thinkgem.jeesite.ws.test;

import javax.jws.WebService;

@WebService
public interface HelloWorldWebService {
	String sayHi(String text);
}