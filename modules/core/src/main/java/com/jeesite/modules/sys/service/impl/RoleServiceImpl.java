///**
// * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
// */
//package com.jeesite.modules.sys.service.impl;
//
//import java.util.List;
//
//import org.springframework.stereotype.Service;
//
//import com.jeesite.modules.sys.entity.Role;
//import com.jeesite.modules.sys.service.RoleServiceSupport;
//
///**
// * 角色服务扩展实现类
// * @author ThinkGem
// * @version 2018年10月13日
// */
//@Service
//public class RoleServiceImpl extends RoleServiceSupport {
//
//	public RoleServiceImpl() {
//		this.entityClass = Role.class;
//	}
//	
//	@Override
//	public Role get(Role role) {
//		System.out.println("============= role get ");
//		return super.get(role);
//	}
//	
//	@Override
//	public List<Role> findList(Role role) {
//		System.out.println("============= role findList ");
//		return super.findList(role);
//	}
//	
//	@Override
//	public List<Role> findListByUserCode(Role role) {
//		System.out.println("============= role findListByUserCode ");
//		return super.findListByUserCode(role);
//	}
//	
//}
