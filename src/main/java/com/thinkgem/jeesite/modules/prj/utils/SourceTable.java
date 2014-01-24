package com.thinkgem.jeesite.modules.prj.utils;

import java.util.List;
import java.util.Set;

import org.insightech.er.editor.model.diagram_contents.element.node.table.ERTable;
import org.insightech.er.editor.model.diagram_contents.element.node.table.column.NormalColumn;

import com.google.common.base.CaseFormat;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.thinkgem.jeesite.modules.prj.entity.Project;

public class SourceTable {
	private Project project;
	private ERTable erTable;

	@SuppressWarnings("unused")
	private SourceTable() {
	}

	public SourceTable(ERTable erTable) {
		this.erTable=erTable;
	}
	
	public SourceTable(Project project,ERTable erTable) {
		this.project=project;
		this.erTable=erTable;
	}
	
	public Project getProject() {
		return project;
	}

	public void setProject(Project project) {
		this.project = project;
	}

	public ERTable getErTable() {
		return erTable;
	}

	public void setErTable(ERTable erTable) {
		this.erTable = erTable;
	}
	
	public String getEntityPackage() {
		return getPackageName(SourceUtils.SOURCE_TYPE_ENTITY);
	}
	
	public String getDaoPackage() {
		return getPackageName(SourceUtils.SOURCE_TYPE_DAO);
	}
	
	public String getServicePackage() {
		return getPackageName(SourceUtils.SOURCE_TYPE_SERVICE);
	}
	
	public String getControllerPackage() {
		return getPackageName(SourceUtils.SOURCE_TYPE_CONTROLLER);
	}
	
	public String getEntityClassName() {
		return getClassName(SourceUtils.SOURCE_TYPE_ENTITY);
	}
	
	public String getDaoClassName() {
		return getClassName(SourceUtils.SOURCE_TYPE_DAO);
	}
	
	public String getServiceClassName() {
		return getClassName(SourceUtils.SOURCE_TYPE_SERVICE);
	}
	
	public String getControllerClassName() {
		return getClassName(SourceUtils.SOURCE_TYPE_CONTROLLER);
	}
	
	public String getModuleName() {
		return erTable.getPhysicalName().toLowerCase().split("_")[0];
	}

	//获取所有代码生成用字段
	public List<SourceColumn> getSourceColumnList(){
		List<SourceColumn> list = getAllSourceColumnList();
		Set<String> commonColumn = SourceUtils.getCommonColumns();
		if("DataEntity".equals(getEntityExtendType())) {
			for(int i=list.size()-1;i>=0;i--) {
				if(commonColumn.contains(list.get(i).getNormalColumn().getPhysicalName())) {
					list.remove(i);
				}
			}
		}
		return list;
	}
	
	//获取所有字段
	public List<SourceColumn> getAllSourceColumnList(){
		List<SourceColumn> list = Lists.newArrayList();
		for(NormalColumn normalColumn:erTable.getExpandedColumns()) {
			SourceColumn sourceColumn = new SourceColumn(this,normalColumn);
			list.add(sourceColumn);
		}
		return list;
	}
	
	//获取entity继承类
	public String  getEntityExtendType() {
		String extendType ="DataEntity";
		Set<String> physicalNames = getPhysicalNames();
		Set<String> commonColumn = SourceUtils.getCommonColumns();
		for(String column:commonColumn) {
			if(!physicalNames.contains(column)) {
				extendType = "BaseEntity";
			}
		}
		return extendType;
	}

	public String getPhysicalName(){
		return erTable.getPhysicalName();
	}
	
	public String getLogicalName(){
		return erTable.getLogicalName();
	}
	
	
	public boolean isContainsDelFlag(){
		return getPhysicalNames().contains("del_flag");
	}
	
	public boolean isContainsParentIds(){
		return getPhysicalNames().contains("parent_ids");
	}
	
	public boolean isContainsName(){
		return getPhysicalNames().contains("name");
	}
	
	public boolean isContainsProcessInstanceId(){
		return getPhysicalNames().contains("processInstanceId");
	}
	
	public Set<String> getPhysicalNames(){
		Set<String> set = Sets.newHashSet();
		List<NormalColumn> columns = null;
		columns = erTable.getExpandedColumns();
		for(NormalColumn normalColumn:columns) {
			set.add(normalColumn.getPhysicalName().toLowerCase());
		}
		return set;
	}

	private String getClassName(String sourceType) {
		String result = "";
		if(SourceUtils.SOURCE_TYPE_ENTITY.equals(sourceType)) {
			result = CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.UPPER_CAMEL, getEntityName());
		} else {
			result = CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.UPPER_CAMEL, getEntityName()+"_"+ sourceType );
		}
		return result;
	}
	
	private String getEntityName(){
		String physicalName = erTable.getPhysicalName();
		return physicalName.toLowerCase().substring(physicalName.indexOf("_"));
	}
	
	private String getPackageName(String sourceType) {
		return project.getRootPackage() + ".modules." + getModuleName() + "." + sourceType;
	}
}
