/**
 * Copyright &copy; 2012-2013 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 */
package com.thinkgem.jeesite.modules.sys.service;

import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.activiti.engine.IdentityService;
import org.activiti.engine.identity.Group;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.shiro.SecurityUtils;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.security.Digests;
import com.thinkgem.jeesite.common.service.BaseService;
import com.thinkgem.jeesite.common.utils.Encodes;
import com.thinkgem.jeesite.modules.sys.dao.MenuDao;
import com.thinkgem.jeesite.modules.sys.dao.RoleDao;
import com.thinkgem.jeesite.modules.sys.dao.UserDao;
import com.thinkgem.jeesite.modules.sys.entity.Menu;
import com.thinkgem.jeesite.modules.sys.entity.Role;
import com.thinkgem.jeesite.modules.sys.entity.User;
import com.thinkgem.jeesite.modules.sys.security.SystemRealm;
import com.thinkgem.jeesite.modules.sys.utils.UserUtils;

/**
 * 系统管理，安全相关实体的管理类,包括用户、角色、菜单.
 * @author ThinkGem
 * @version 2013-4-19
 */
@Service
@Transactional(readOnly = true)
public class SystemService extends BaseService {

	@SuppressWarnings("unused")
	private static Logger logger = LoggerFactory.getLogger(SystemService.class);
	
	public static final String HASH_ALGORITHM = "SHA-1";
	public static final int HASH_INTERATIONS = 1024;
	public static final int SALT_SIZE = 8;
	
	@Autowired
	private UserDao userDao;
	@Autowired
	private RoleDao roleDao;
	@Autowired
	private MenuDao menuDao;
	@Autowired
	private SystemRealm systemRealm;
	@Autowired
	private IdentityService identityService;
	
	//-- User Service --//
	
	public User getUser(Long id) {
		return userDao.findOne(id);
	}
	
	public Page<User> findUser(Page<User> page, User user) {
		DetachedCriteria dc = userDao.createDetachedCriteria();
		User currentUser = UserUtils.getUser();
		if (!currentUser.isAdmin()){
			if (user.getArea()==null || user.getArea().getId()==null){
				user.setArea(currentUser.getArea());
			}
			if (user.getOffice()==null || user.getOffice().getId()==null){
				user.setOffice(currentUser.getOffice());
			}
		}
		dc.createAlias("area", "area");
		if (user.getArea()!=null && user.getArea().getId()!=null){
			dc.add(Restrictions.or(
					Restrictions.eq("area.id", user.getArea().getId()),
					Restrictions.eq("area.parent.id", user.getArea().getId()),
					Restrictions.like("area.parentIds", "%,"+user.getArea().getId()+",%")
					));
		}
		dc.createAlias("office", "office");
		if (user.getOffice()!=null && user.getOffice().getId()!=null){
			dc.add(Restrictions.or(
					Restrictions.eq("office.id", user.getOffice().getId()),
					Restrictions.eq("office.parent.id", user.getOffice().getId()),
					Restrictions.like("office.parentIds", "%,"+user.getOffice().getId()+",%")
					));
		}
		if (StringUtils.isNotEmpty(user.getLoginName())){
			dc.add(Restrictions.like("loginName", "%"+user.getLoginName()+"%"));
		}
		if (StringUtils.isNotEmpty(user.getName())){
			dc.add(Restrictions.like("name", "%"+user.getName()+"%"));
		}
		dc.add(Restrictions.eq("delFlag", User.DEL_FLAG_NORMAL));
		if (!StringUtils.isNotEmpty(page.getOrderBy())){
			dc.addOrder(Order.asc("area.code")).addOrder(Order.asc("office.code")).addOrder(Order.desc("id"));
		}
		return userDao.find(page, dc);
	}

	public User getUserByLoginName(String loginName) {
		return userDao.findByLoginName(loginName);
	}

	@Transactional(readOnly = false)
	public void saveUser(User user) {
		userDao.clear();
		userDao.save(user);
		systemRealm.clearCachedAuthorizationInfo(user.getLoginName());
		
		saveActivitiUser(user);
	}

	@Transactional(readOnly = false)
	public void deleteUser(Long id) {
		userDao.deleteById(id);
		deleteActivitiUser(userDao.findOne(id));
	}
	
	@Transactional(readOnly = false)
	public void updatePasswordById(Long id, String loginName, String newPassword) {
		userDao.updatePasswordById(entryptPassword(newPassword), id);
		systemRealm.clearCachedAuthorizationInfo(loginName);
	}
	
	@Transactional(readOnly = false)
	public void updateUserLoginInfo(Long id) {
		userDao.updateLoginInfo(SecurityUtils.getSubject().getSession().getHost(), new Date(), id);
	}
	
	/**
	 * 生成安全的密码，生成随机的16位salt并经过1024次 sha-1 hash
	 */
	public static String entryptPassword(String plainPassword) {
		byte[] salt = Digests.generateSalt(SALT_SIZE);
		byte[] hashPassword = Digests.sha1(plainPassword.getBytes(), salt, HASH_INTERATIONS);
		return Encodes.encodeHex(salt)+Encodes.encodeHex(hashPassword);
	}
	
	/**
	 * 验证密码
	 * @param plainPassword 明文密码
	 * @param password 密文密码
	 * @return 验证成功返回true
	 */
	public static boolean validatePassword(String plainPassword, String password) {
		byte[] salt = Encodes.decodeHex(password.substring(0,16));
		byte[] hashPassword = Digests.sha1(plainPassword.getBytes(), salt, HASH_INTERATIONS);
		return password.equals(Encodes.encodeHex(salt)+Encodes.encodeHex(hashPassword));
	}
	
	//-- Role Service --//
	
	public Role getRole(Long id) {
		return roleDao.findOne(id);
	}

	public Role findRoleByName(String name) {
		return roleDao.findByName(name);
	}
	
	public List<Role> findAllRole(){
		User currentUser = UserUtils.getUser();
		if (!currentUser.isAdmin()){
			return roleDao.findByUserId(currentUser.getId());
		}else{
			return roleDao.findAllList();
		}
	}
	
	@Transactional(readOnly = false)
	public void saveRole(Role role) {
		if (role.getId()==null){
			role.setUser(UserUtils.getUser());
		}
		roleDao.save(role);
		systemRealm.clearAllCachedAuthorizationInfo();
		//同步到Activiti
		saveActivitiGroup(role);
	}

	@Transactional(readOnly = false)
	public void deleteRole(Long id) {
		roleDao.deleteById(id);
		systemRealm.clearAllCachedAuthorizationInfo();
		deleteActivitiGroup(roleDao.findOne(id));
	}

	//-- Menu Service --//
	
	public Menu getMenu(Long id) {
		return menuDao.findOne(id);
	}

	public List<Menu> findAllMenu(){
		return UserUtils.getMenuList();
	}
	
	@Transactional(readOnly = false)
	public void saveMenu(Menu menu) {
		menu.setParent(this.getMenu(menu.getParent().getId()));
		String oldParentIds = menu.getParentIds(); // 获取修改前的parentIds，用于更新子节点的parentIds
		menu.setParentIds(menu.getParent().getParentIds()+menu.getParent().getId()+",");
		if (menu.getId()==null){
			menu.setUser(UserUtils.getUser());
		}
		menuDao.clear();
		menuDao.save(menu);
		// 更新子节点 parentIds
		List<Menu> list = menuDao.findByParentIdsLike("%,"+menu.getId()+",%");
		for (Menu e : list){
			e.setParentIds(e.getParentIds().replace(oldParentIds, menu.getParentIds()));
		}
		menuDao.save(list);
		systemRealm.clearAllCachedAuthorizationInfo();
	}

	@Transactional(readOnly = false)
	public void deleteMenu(Long id) {
		menuDao.deleteById(id, "%,"+id+",%");
		systemRealm.clearAllCachedAuthorizationInfo();
	}
	

	//同步所有系统用户到Activiti
	 public void synActivitiIndetity()  {
	        // 清空工作流用户、角色以及关系
		 	List<org.activiti.engine.identity.User> userList = identityService.createUserQuery().list();
		 	List<Group> groupList = identityService.createGroupQuery().list();
		 	for(Group group:groupList) {
		 		identityService.deleteGroup(group.getId());
		 	}
		 	for(org.activiti.engine.identity.User user:userList) {
		 		identityService.deleteUser(user.getId());
		 	}
	        // 复制角色数据
		 	Iterator<Role> roles = roleDao.findAll().iterator();
		 	while(roles.hasNext()) {
		 		Role role = roles.next();
		 		String groupId = role.getEnname();
	            Group group = identityService.newGroup(groupId);
	            group.setName(role.getName());
	            group.setType(role.getRoleType());
	            identityService.saveGroup(group);
		 	}
		 	Iterator<User> users = userDao.findAll().iterator();
		 	while(users.hasNext()) {
		 		saveActivitiUser(users.next());
		 	}
	  }
	 

		private void saveActivitiGroup(Role role) {
			String groupId = role.getEnname();
			Group group = identityService.createGroupQuery().groupId(groupId).singleResult();
			if (group == null) {
				group = identityService.newGroup(groupId);
			}
			group.setName(role.getName());
			group.setType(role.getRoleType());
			identityService.saveGroup(group);
		}

		public void deleteActivitiGroup(Role role) {
			if(role!=null) {
				String groupId = role.getEnname();
				identityService.deleteGroup(groupId);
			}
		}

		/**
		 * 使用系统用户对象属性设置到Activiti User对象中
		 * 
		 * @param user
		 *            系统用户对象
		 * @param activitiUser
		 *            Activiti User
		 */
		private void saveActivitiUser(User user) {
			String userId = ObjectUtils.toString(user.getId());
			org.activiti.engine.identity.User activitiUser = identityService.createUserQuery().userId(userId).singleResult();
			// 是新增用户
			if (activitiUser == null) {
				activitiUser = identityService.newUser(userId);
			} else {
				List<Group> activitiGroups = identityService.createGroupQuery().groupMember(userId).list();
				for (Group group : activitiGroups) {
					logger.debug("delete group from activit: {}",ToStringBuilder.reflectionToString(group));
					identityService.deleteMembership(userId, group.getId());
				}
			}
			activitiUser.setFirstName(user.getName());
			activitiUser.setLastName(StringUtils.EMPTY);
			activitiUser.setPassword(StringUtils.EMPTY);
			activitiUser.setEmail(user.getEmail());
			identityService.saveUser(activitiUser);
			// 同步用户角色关联数据
			for (Long roleId : user.getRoleIdList()) {
				 Role role = roleDao.findOne(roleId);
		            //查询activiti中是否有该权限
				 	Group group= identityService.createGroupQuery().groupId(role.getEnname()).singleResult();
		            //不存在该权限，新增
		            if(group ==null) {
				 		String groupId = role.getEnname();
			            group = identityService.newGroup(groupId);
			            group.setName(role.getName());
			            group.setType(role.getRoleType());
			            identityService.saveGroup(group);
		            }
				identityService.createMembership(userId, roleDao.findOne(roleId).getEnname());
			}
		}

		private void deleteActivitiUser(User user) {
			if(user!=null) {
				String userId = ObjectUtils.toString(user.getId());
				identityService.deleteUser(userId);
			}
		}
	
}
