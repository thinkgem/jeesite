///**
// * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
// */
//package com.jeesite.modules.sys.service.impl;
//
//import java.util.List;
//
//import org.springframework.stereotype.Service;
//
//import com.jeesite.modules.sys.entity.User;
//import com.jeesite.modules.sys.service.UserServiceSupport;
//
///**
// * 用户服务扩展实现类
// * @author ThinkGem
// * @version 2018年10月13日
// */
//@Service
//public class UserServiceImpl extends UserServiceSupport {
//
//	public UserServiceImpl() {
//		this.entityClass = User.class;
//	}
//	
//	@Override
//	public User get(User user) {
//		System.out.println("============= user get ");
//		return super.get(user);
//	}
//	
//	@Override
//	public User getByLoginCode(User user) {
//		System.out.println("============= user getByLoginCode ");
//		return super.getByLoginCode(user);
//	}
//	
//	@Override
//	public User getByUserTypeAndRefCode(User user) {
//		System.out.println("============= user getByUserTypeAndRefCode ");
//		return super.getByUserTypeAndRefCode(user);
//	}
//	
//	@Override
//	public List<User> findList(User user) {
//		System.out.println("============= user findList ");
//		return super.findList(user);
//	}
//	
//}
