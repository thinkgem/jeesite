package com.thinkgem.jeesite.modules.prj.entity;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.apache.commons.lang3.StringUtils;

import com.google.common.collect.Lists;
import com.thinkgem.jeesite.common.persistence.DataEntity;


/**
 * The persistent class for the prj_project database table.
 * 
 */
@Entity
@Table(name="prj_project")
public class Project extends DataEntity<Project> {
	private static final long serialVersionUID = 1L;
	private Long id;
	private String name;
	private String rootPackage;
	private String ermPath;
	
	private String templateType;

	public Project() {
	}


	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
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