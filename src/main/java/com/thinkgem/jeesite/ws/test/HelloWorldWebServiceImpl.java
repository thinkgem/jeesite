package com.thinkgem.jeesite.ws.test;

import javax.jws.WebService;

@WebService()
public class HelloWorldWebServiceImpl implements HelloWorldWebService {
	
	public HelloWorldWebServiceImpl() {
		System.out.println("HelloWorldWebServiceImpl::constructor()");
	}

	public String sayHi(String text) {
		System.out.println("sayHi called");
		return "Hello " + text;
	}
}