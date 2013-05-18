/**
 * Copyright &copy; 2012-2013 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 */
package com.thinkgem.jeesite.modules.sys.entity;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;
import org.hibernate.annotations.Where;
import org.hibernate.validator.constraints.Length;

import com.google.common.collect.Lists;
import com.thinkgem.jeesite.common.persistence.BaseEntity;

/**
 * 角色Entity
 * @author ThinkGem
 * @version 2013-05-15
 */
@Entity
@Table(name = "sys_role")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Role extends BaseEntity {
	
	private static final long serialVersionUID = 1L;
	private Long id;	 	// 编号
	private Office office;	// 归属机构
	private String name; 	// 角色名称
	private String dataScope; // 数据范围
//	private User user;		// 创建者
	private String delFlag; // 删除标记（0：正常；1：删除）

	private List<User> userList = Lists.newArrayList(); // 拥有用户列表
	private List<Menu> menuList = Lists.newArrayList(); // 拥有菜单列表
	private List<Office> officeList = Lists.newArrayList(); // 按明细设置数据范围
//	private List<Category> categoryList = Lists.newArrayList(); // 拥有内容分类列表

	// 数据范围（1：所有数据；2：所在公司及以下数据；3：所在公司数据；4：所在部门及以下数据；5：所在部门数据；8：仅本人数据；9：按明细设置）
	public static final String DATA_SCOPE_ALL = "1";
	public static final String DATA_SCOPE_COMPANY_AND_CHILD = "2";
	public static final String DATA_SCOPE_COMPANY = "3";
	public static final String DATA_SCOPE_OFFICE_AND_CHILD = "4";
	public static final String DATA_SCOPE_OFFICE = "5";
	public static final String DATA_SCOPE_SELF = "8";
	public static final String DATA_SCOPE_CUSTOM = "9";
	
	public Role() {
		this.delFlag = DEL_FLAG_NORMAL;
		this.dataScope = DATA_SCOPE_CUSTOM;
	}

	public Role(Long id, String name) {
		this();
		this.id = id;
		this.name = name;
	}
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
//	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_sys_role")
//	@SequenceGenerator(name = "seq_sys_role", sequenceName = "seq_sys_role")
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	
	@ManyToOne
	@JoinColumn(name="office_id")
	@NotFound(action = NotFoundAction.IGNORE)
	public Office getOffice() {
		return office;
	}

	public void setOffice(Office office) {
		this.office = office;
	}

	@Length(min=1, max=100)
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDataScope() {
		return dataScope;
	}

	public void setDataScope(String dataScope) {
		this.dataScope = dataScope;
	}

//	@ManyToOne
//	@JoinColumn(name="user_id")
//	@NotFound(action = NotFoundAction.IGNORE)
//	public User getUser() {
//		return user;
//	}
//
//	public void setUser(User user) {
//		this.user = user;
//	}
	
	@Length(min=1, max=1)
	public String getDelFlag() {
		return delFlag;
	}

	public void setDelFlag(String delFlag) {
		this.delFlag = delFlag;
	}
	
	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "sys_user_role", joinColumns = { @JoinColumn(name = "role_id") }, inverseJoinColumns = { @JoinColumn(name = "user_id") })
	@Where(clause="del_flag='"+DEL_FLAG_NORMAL+"'")
	@OrderBy("id") @Fetch(FetchMode.SUBSELECT)
	@NotFound(action = NotFoundAction.IGNORE)
	@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
	public List<User> getUserList() {
		return userList;
	}

	public void setUserList(List<User> userList) {
		this.userList = userList;
	}

	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "sys_role_menu", joinColumns = { @JoinColumn(name = "role_id") }, inverseJoinColumns = { @JoinColumn(name = "menu_id") })
	@Where(clause="del_flag='"+DEL_FLAG_NORMAL+"'")
	@OrderBy("id") @Fetch(FetchMode.SUBSELECT)
	@NotFound(action = NotFoundAction.IGNORE)
	@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
	public List<Menu> getMenuList() {
		return menuList;
	}

	public void setMenuList(List<Menu> menuList) {
		this.menuList = menuList;
	}

	@Transient
	public List<Long> getMenuIdList() {
		List<Long> menuIdList = Lists.newArrayList();
		for (Menu menu : menuList) {
			menuIdList.add(menu.getId());
		}
		return menuIdList;
	}

	@Transient
	public void setMenuIdList(List<Long> menuIdList) {
		menuList = Lists.newArrayList();
		for (Long menuId : menuIdList) {
			Menu menu = new Menu();
			menu.setId(menuId);
			menuList.add(menu);
		}
	}

	@Transient
	public String getMenuIds() {
		List<Long> nameIdList = Lists.newArrayList();
		for (Menu menu : menuList) {
			nameIdList.add(menu.getId());
		}
		return StringUtils.join(nameIdList, ",");
	}
	
	@Transient
	public void setMenuIds(String menuIds) {
		menuList = Lists.newArrayList();
		if (menuIds != null){
			String[] ids = StringUtils.split(menuIds, ",");
			for (String menuId : ids) {
				Menu menu = new Menu();
				menu.setId(new Long(menuId));
				menuList.add(menu);
			}
		}
	}
	
//	@ManyToMany(fetch = FetchType.LAZY)
//	@JoinTable(name = "sys_role_category", joinColumns = { @JoinColumn(name = "role_id") }, 
//		inverseJoinColumns = { @JoinColumn(name = "category_id") })
//	@Where(clause="del_flag='"+DEL_FLAG_NORMAL+"'")
//	@OrderBy("id") @Fetch(FetchMode.SUBSELECT)
//	@NotFound(action = NotFoundAction.IGNORE)
//	@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
//	public List<Category> getCategoryList() {
//		return categoryList;
//	}
//
//	public void setCategoryList(List<Category> categoryList) {
//		this.categoryList = categoryList;
//	}
//	
//	@Transient
//	public List<Long> getCategoryIdList() {
//		List<Long> categoryIdList = Lists.newArrayList();
//		for (Category category : categoryList) {
//			categoryIdList.add(category.getId());
//		}
//		return categoryIdList;
//	}
//
//	@Transient
//	public void setCategoryIdList(List<Long> categoryIdList) {
//		categoryList = Lists.newArrayList();
//		for (Long categoryId : categoryIdList) {
//			Category category = new Category();
//			category.setId(categoryId);
//			categoryList.add(category);
//		}
//	}
//
//	@Transient
//	public String getCategoryIds() {
//		List<Long> nameIdList = Lists.newArrayList();
//		for (Category category : categoryList) {
//			nameIdList.add(category.getId());
//		}
//		return StringUtils.join(nameIdList, ",");
//	}
//	
//	@Transient
//	public void setCategoryIds(String categoryIds) {
//		categoryList = Lists.newArrayList();
//		if (categoryIds != null){
//			String[] ids = StringUtils.split(categoryIds, ",");
//			for (String categoryId : ids) {
//				Category category = new Category();
//				category.setId(new Long(categoryId));
//				categoryList.add(category);
//			}
//		}
//	}
	
	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(name = "sys_role_office", joinColumns = { @JoinColumn(name = "role_id") }, inverseJoinColumns = { @JoinColumn(name = "office_id") })
	@Where(clause="del_flag='"+DEL_FLAG_NORMAL+"'")
	@OrderBy("id") @Fetch(FetchMode.SUBSELECT)
	@NotFound(action = NotFoundAction.IGNORE)
	@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
	public List<Office> getOfficeList() {
		return officeList;
	}

	public void setOfficeList(List<Office> officeList) {
		this.officeList = officeList;
	}


	@Transient
	public List<Long> getOfficeIdList() {
		List<Long> officeIdList = Lists.newArrayList();
		for (Office office : officeList) {
			officeIdList.add(office.getId());
		}
		return officeIdList;
	}

	@Transient
	public void setOfficeIdList(List<Long> officeIdList) {
		officeList = Lists.newArrayList();
		for (Long officeId : officeIdList) {
			Office office = new Office();
			office.setId(officeId);
			officeList.add(office);
		}
	}

	@Transient
	public String getOfficeIds() {
		List<Long> nameIdList = Lists.newArrayList();
		for (Office office : officeList) {
			nameIdList.add(office.getId());
		}
		return StringUtils.join(nameIdList, ",");
	}
	
	@Transient
	public void setOfficeIds(String officeIds) {
		officeList = Lists.newArrayList();
		if (officeIds != null){
			String[] ids = StringUtils.split(officeIds, ",");
			for (String officeId : ids) {
				Office office = new Office();
				office.setId(new Long(officeId));
				officeList.add(office);
			}
		}
	}
	
//	@ElementCollection
//	@CollectionTable(name = "sys_user_role", joinColumns = { @JoinColumn(name = "role_id") })
//	@Column(name = "user_id")
//	@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
//	public List<Long> getUserIdList() {
//		return userIdList;
//	}
//
//	public void setUserIdList(List<Long> userIdList) {
//		this.userIdList = userIdList;
//	}
	
	/**
	 * 获取权限字符串列表
	 */
	@Transient
	public List<String> getPermissions() {
		List<String> permissions = Lists.newArrayList();
		for (Menu menu : menuList) {
			if (menu.getPermission()!=null && !"".equals(menu.getPermission())){
				permissions.add(menu.getPermission());
			}
		}
		return permissions;
	}

	@Transient
	public boolean isAdmin(){
		return isAdmin(this.id);
	}
	
	@Transient
	public static boolean isAdmin(Long id){
		return id != null && id.equals(1L);
	}
	
//	@Transient
//	public String getMenuNames() {
//		List<String> menuNameList = Lists.newArrayList();
//		for (Menu menu : menuList) {
//			menuNameList.add(menu.getName());
//		}
//		return StringUtils.join(menuNameList, ",");
//	}
}
