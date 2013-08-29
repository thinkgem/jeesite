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
import org.apache.shiro.SecurityUtils;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.hibernate.sql.JoinType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.security.Digests;
import com.thinkgem.jeesite.common.service.BaseService;
import com.thinkgem.jeesite.common.utils.Encodes;
import com.thinkgem.jeesite.common.utils.StringUtils;
import com.thinkgem.jeesite.modules.sys.dao.MenuDao;
import com.thinkgem.jeesite.modules.sys.dao.RoleDao;
import com.thinkgem.jeesite.modules.sys.dao.UserDao;
import com.thinkgem.jeesite.modules.sys.entity.Menu;
import com.thinkgem.jeesite.modules.sys.entity.Role;
import com.thinkgem.jeesite.modules.sys.entity.User;
import com.thinkgem.jeesite.modules.sys.security.SystemAuthorizingRealm;
import com.thinkgem.jeesite.modules.sys.utils.UserUtils;

/**
 * 系统管理，安全相关实体的管理类,包括用户、角色、菜单.
 * @author ThinkGem
 * @version 2013-5-15
 */
@Service
@Transactional(readOnly = true)
public class SystemService extends BaseService {
	
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
	private SystemAuthorizingRealm systemRealm;
	
	@Autowired
	private IdentityService identityService;

	//-- User Service --//
	
	public User getUser(Long id) {
		return userDao.get(id);
	}
	
	public Page<User> findUser(Page<User> page, User user) {
		DetachedCriteria dc = userDao.createDetachedCriteria();
		User currentUser = UserUtils.getUser();
		dc.createAlias("company", "company");
		if (user.getCompany()!=null && user.getCompany().getId()!=null){
			dc.add(Restrictions.or(
					Restrictions.eq("company.id", user.getCompany().getId()),
					Restrictions.like("company.parentIds", "%,"+user.getCompany().getId()+",%")
					));
		}
		dc.createAlias("office", "office");
		if (user.getOffice()!=null && user.getOffice().getId()!=null){
			dc.add(Restrictions.or(
					Restrictions.eq("office.id", user.getOffice().getId()),
					Restrictions.like("office.parentIds", "%,"+user.getOffice().getId()+",%")
					));
		}
		// 如果不是超级管理员，则不显示超级管理员用户
		if (!currentUser.isAdmin()){
			dc.add(Restrictions.ne("id", 1L)); 
		}
		dc.add(dataScopeFilter(currentUser, "office", ""));
		//System.out.println(dataScopeFilterString(currentUser, "office", ""));
		if (StringUtils.isNotEmpty(user.getLoginName())){
			dc.add(Restrictions.like("loginName", "%"+user.getLoginName()+"%"));
		}
		if (StringUtils.isNotEmpty(user.getName())){
			dc.add(Restrictions.like("name", "%"+user.getName()+"%"));
		}
		dc.add(Restrictions.eq(User.FIELD_DEL_FLAG, User.DEL_FLAG_NORMAL));
		if (!StringUtils.isNotEmpty(page.getOrderBy())){
			dc.addOrder(Order.asc("company.code")).addOrder(Order.asc("office.code")).addOrder(Order.desc("id"));
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
		systemRealm.clearAllCachedAuthorizationInfo();
		// 如果从未同步过，则与Activiti同步
		synActivitiIndetity();
		// 将当前用户同步到Activiti
		saveActivitiUser(user, user.getId()==null);
	}

	@Transactional(readOnly = false)
	public void deleteUser(Long id) {
		userDao.deleteById(id);
		// 同步到Activiti
		deleteActivitiUser(userDao.get(id));
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
		return roleDao.get(id);
	}

	public Role findRoleByName(String name) {
		return roleDao.findByName(name);
	}
	
	public List<Role> findAllRole(){
		return UserUtils.getRoleList();
	}
	
	@Transactional(readOnly = false)
	public void saveRole(Role role) {
		roleDao.clear();
		roleDao.save(role);
		systemRealm.clearAllCachedAuthorizationInfo();
		// 同步到Activiti
		saveActivitiGroup(role, role.getId()==null);
		UserUtils.removeCache(UserUtils.CACHE_ROLE_LIST);
	}

	@Transactional(readOnly = false)
	public void deleteRole(Long id) {
		roleDao.deleteById(id);
		systemRealm.clearAllCachedAuthorizationInfo();
		// 同步到Activiti
		deleteActivitiGroup(roleDao.get(id));
		UserUtils.removeCache(UserUtils.CACHE_ROLE_LIST);
	}
	
	@Transactional(readOnly = false)
	public Boolean outUserInRole(Role role, Long userId) {
		User user = userDao.get(userId);
		List<Long> roleIds = user.getRoleIdList();
		List<Role> roles = user.getRoleList();
		// 
		if (roleIds.contains(role.getId())) {
			roles.remove(role);
			saveUser(user);
			return true;
		}
		return false;
	}
	
	@Transactional(readOnly = false)
	public User assignUserToRole(Role role, Long userId) {
		User user = userDao.get(userId);
		List<Long> roleIds = user.getRoleIdList();
		if (roleIds.contains(role.getId())) {
			return null;
		}
		user.getRoleList().add(role);
		saveUser(user);		
		return user;
	}

	//-- Menu Service --//
	
	public Menu getMenu(Long id) {
		return menuDao.get(id);
	}

	public List<Menu> findAllMenu(){
		return UserUtils.getMenuList();
	}
	
	@Transactional(readOnly = false)
	public void saveMenu(Menu menu) {
		menu.setParent(this.getMenu(menu.getParent().getId()));
		String oldParentIds = menu.getParentIds(); // 获取修改前的parentIds，用于更新子节点的parentIds
		menu.setParentIds(menu.getParent().getParentIds()+menu.getParent().getId()+",");
		menuDao.clear();
		menuDao.save(menu);
		// 更新子节点 parentIds
		List<Menu> list = menuDao.findByParentIdsLike("%,"+menu.getId()+",%");
		for (Menu e : list){
			e.setParentIds(e.getParentIds().replace(oldParentIds, menu.getParentIds()));
		}
		menuDao.save(list);
		systemRealm.clearAllCachedAuthorizationInfo();
		UserUtils.removeCache(UserUtils.CACHE_MENU_LIST);
	}

	@Transactional(readOnly = false)
	public void deleteMenu(Long id) {
		menuDao.deleteById(id, "%,"+id+",%");
		systemRealm.clearAllCachedAuthorizationInfo();
		UserUtils.removeCache(UserUtils.CACHE_MENU_LIST);
	}
	
	///////////////// Synchronized to the Activiti //////////////////

	/**
	 * 是需要同步Activiti数据，如果从未同步过，则同步数据。
	 */
	private static boolean isSynActivitiIndetity = true;
	public void synActivitiIndetity() {
		if (isSynActivitiIndetity){
			isSynActivitiIndetity = false;
			List<Group> groupList = identityService.createGroupQuery().list();
			List<org.activiti.engine.identity.User> userList = identityService.createUserQuery().list();
			if (groupList.size() == 0 && userList.size() == 0){
		        // 同步角色数据
			 	Iterator<Role> roles = roleDao.findAll().iterator();
			 	while(roles.hasNext()) {
			 		Role role = roles.next();
			 		saveActivitiGroup(role, true);
			 	}
			 	// 同步用户数据
			 	Iterator<User> users = userDao.findAll().iterator();
			 	while(users.hasNext()) {
			 		saveActivitiUser(users.next(), true);
			 	}
			}
		}
	}
	
	private void saveActivitiGroup(Role role, boolean isNew) {
		String groupId = role.getEnname();
		Group group = null;
		if (!isNew){
			group = identityService.createGroupQuery().groupId(groupId).singleResult();
		}
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

	private void saveActivitiUser(User user, boolean isNew) {
		String userId = ObjectUtils.toString(user.getId());
		org.activiti.engine.identity.User activitiUser = null;
		if (!isNew){
			activitiUser = identityService.createUserQuery().userId(userId).singleResult();
		}
		// 是新增用户
		if (activitiUser == null) {
			activitiUser = identityService.newUser(userId);
		} else {
			List<Group> activitiGroups = identityService.createGroupQuery().groupMember(userId).list();
			for (Group group : activitiGroups) {
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
			Role role = roleDao.get(roleId);
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
			identityService.createMembership(userId, roleDao.get(roleId).getEnname());
		}
	}

	private void deleteActivitiUser(User user) {
		if(user!=null) {
			String userId = ObjectUtils.toString(user.getId());
			identityService.deleteUser(userId);
		}
	}
	
	///////////////// Synchronized to the Activiti end //////////////////
	
}
