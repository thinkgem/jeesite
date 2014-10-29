package com.thinkgem.jeesite.modules.prj.utils;

import java.util.List;

import org.insightech.er.editor.model.diagram_contents.element.connection.Relation;
import org.insightech.er.editor.model.diagram_contents.element.node.table.column.NormalColumn;

import com.google.common.base.CaseFormat;
import com.google.common.collect.Lists;
import com.thinkgem.jeesite.common.utils.Collections3;

@SuppressWarnings("unused")
public class SourceColumn{
	private SourceTable sourceTable;
	private NormalColumn normalColumn;
	
	private SourceColumn() {
	}
	public SourceColumn(NormalColumn normalColumn){
		this.normalColumn = normalColumn;
	}
	
	public SourceColumn(SourceTable sourceTable,NormalColumn normalColumn){
		this.sourceTable=sourceTable;
		this.normalColumn = normalColumn;
	}
	
	
	public SourceTable getSourceTable() {
		return sourceTable;
	}
	
	public String getPhysicalName(){
		return normalColumn.getPhysicalName();
	}
	
	public String getLogicalName(){
		return normalColumn.getLogicalName();
	}
	
	public String getDescription(){
		return normalColumn.getDescription();
	}

	public void setSourceTable(SourceTable sourceTable) {
		this.sourceTable = sourceTable;
	}

	public NormalColumn getNormalColumn() {
		return normalColumn;
	}
	public void setNormalColumn(NormalColumn normalColumn) {
		this.normalColumn = normalColumn;
	}
	
	//获取字段的Java类型
	public String getClassName() {
		return getJavaClass().getSimpleName();
	}
	
	//获取字段实例名
	public String getInstance() {
		return CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.UPPER_CAMEL, normalColumn.getPhysicalName());
	}
	
	//获取import名
	public String getImportName() {
		String name = getJavaClass().getCanonicalName();
		if (!name.startsWith("java.lang")) {
			return name;
		} else {
			return "";
		}
	}
	
	//获取字段所有的关联关系
	public List<SourceRelation> getSourceRelationList() {
		List<SourceRelation> list = Lists.newArrayList();
		if(!Collections3.isEmpty(normalColumn.getRelationList())) {
			for(Relation relation:normalColumn.getRelationList()) {
				SourceRelation sourceRelation = new SourceRelation(relation,false);
				list.add(sourceRelation);
			}
 		}		
		if(!Collections3.isEmpty(normalColumn.getOutgoingRelationList())) {
			for(Relation relation:normalColumn.getOutgoingRelationList()) {
				SourceRelation sourceRelation = new SourceRelation(relation,true);
				list.add(sourceRelation);
			}
		}
		return list;
	}

	@SuppressWarnings("rawtypes")
	private Class getJavaClass() {
		return normalColumn.getType().getJavaClass();
	}
}
