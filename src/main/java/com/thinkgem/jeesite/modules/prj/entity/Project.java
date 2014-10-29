package com.thinkgem.jeesite.modules.prj.entity;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.apache.commons.lang3.StringUtils;

import com.google.common.collect.Lists;
import com.thinkgem.jeesite.common.persistence.IdEntity;


/**
 * The persistent class for the prj_project database table.
 * 
 */
@Entity
@Table(name="prj_project")
public class Project extends IdEntity<Project> {
	private static final long serialVersionUID = 1L;
	private String name;
	private String rootPackage;
	private String ermPath;
	
	private String templateType;

	public Project() {
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public String getRootPackage() {
		return rootPackage;
	}


	public void setRootPackage(String rootPackage) {
		this.rootPackage = rootPackage;
	}


	public String getErmPath() {
		return ermPath;
	}


	public void setErmPath(String ermPath) {
		this.ermPath = ermPath;
	}
	

	@Transient
	public String getTemplateType() {
		return templateType;
	}

	@Transient
	public void setTemplateType(String templateType) {
		this.templateType = templateType;
	}


	@Transient
	public List<String> getErmPathList() {
		List<String> ermPathList = Lists.newArrayList();
		if(StringUtils.isNotBlank(ermPath)) {
			String[] ermPathArr=ermPath.split("\\|");
			for (int i=1;i<ermPathArr.length;i++) {
				ermPathList.add(ermPathArr[i]);
			}
		}
		return ermPathList;
	}

}