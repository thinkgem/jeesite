package com.thinkgem.jeesite.modules.prj.utils;

import java.util.List;
import java.util.TreeSet;

import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.OrderBy;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

import org.hibernate.Cache;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.Where;
import org.insightech.er.editor.model.diagram_contents.element.connection.Relation;
import org.insightech.er.editor.model.diagram_contents.element.node.table.ERTable;
import org.insightech.er.editor.model.diagram_contents.element.node.table.TableView;
import org.insightech.er.editor.model.diagram_contents.element.node.table.column.NormalColumn;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.thinkgem.jeesite.common.utils.StringUtils;
import com.thinkgem.jeesite.modules.prj.entity.Project;

@SuppressWarnings("unused")
public class SourceRelation{
	private Logger logger = LoggerFactory.getLogger(getClass());
	public static final String PARENT_CARDINALITY_1 = "1";
	public static final String PAERNT_CARDINALITY_0_OR_1 = "0..1";
	public static final String CHILD_CARDINALITY_1 = "1";
	public static final String CHILD_CARDINALITY_0_OR_1 = "0..1";
	public static final String CHILD_CARDINALITY_0_OR_n = "0..n";
	public static final String CHILD_CARDINALITY_1_OR_n = "1..n";

	public static final String RELATION_TYPE_ONE_TO_ONE = "OneToOne";
	public static final String RELATION_TYPE_ONE_TO_MANY = "OneToMany";
	public static final String RELATION_TYPE_MANY_TO_ONE= "ManyToOne";
	public static final String RELATION_TYPE_MANY_TO_MANY= "ManyToMany";

	private SourceColumn sourceColumn;
	private Relation relation;
	private boolean outgoing;
	private SourceTable finalSourceTable;
	private SourceTable finalTargetTable;
	
	private SourceRelation(){
		
	}
	public SourceRelation(Relation relation,boolean outgoing){
		this.relation = relation;
		this.outgoing=outgoing;
		this.finalSourceTable = new SourceTable((ERTable)getFinalSourceTableView());
		this.finalTargetTable =  new SourceTable((ERTable)getFinalTargetTableView());
	}
	
	public SourceRelation(SourceColumn sourceColumn,Relation relation,boolean outgoing){
		this.sourceColumn = sourceColumn;
		this.relation = relation;
		this.outgoing=outgoing;
		this.finalSourceTable = new SourceTable((ERTable)getFinalSourceTableView());
		this.finalTargetTable =  new SourceTable((ERTable)getFinalTargetTableView());
	}
	
	public SourceColumn getSourceColumn() {
		return sourceColumn;
	}
	public void setSourceColumn(SourceColumn sourceColumn) {
		this.sourceColumn = sourceColumn;
	}
	public Relation getRelation() {
		return relation;
	}
	public void setRelation(Relation relation) {
		this.relation = relation;
	}
	public SourceTable getFinalSourceTable() {
		return finalSourceTable;
	}
	public void setFinalSourceTable(SourceTable finalSourceTable) {
		this.finalSourceTable = finalSourceTable;
	}
	public SourceTable getFinalTargetTable() {
		return finalTargetTable;
	}
	public void setFinalTargetTable(SourceTable finalTargetTable) {
		this.finalTargetTable = finalTargetTable;
	}
	public boolean isOutgoing() {
		return outgoing;
	}
	public void setOutgoing(boolean outgoing) {
		this.outgoing = outgoing;
	}

	public String getRelationType(){
		if(isOutgoing()) {
			if(CHILD_CARDINALITY_1.equals(relation.getChildCardinality()) || CHILD_CARDINALITY_0_OR_1.equals(relation.getChildCardinality())){
				return RELATION_TYPE_ONE_TO_ONE;
			} else {
				if(relation.getTargetTableView().getExpandedColumns().size()==2) {
					return RELATION_TYPE_MANY_TO_MANY;
				} else {
					return RELATION_TYPE_ONE_TO_MANY;
				}
			}
		} else {
			if(CHILD_CARDINALITY_1.equals(relation.getChildCardinality()) || CHILD_CARDINALITY_0_OR_1.equals(relation.getChildCardinality())){
				return RELATION_TYPE_ONE_TO_ONE;
			} else {
				return RELATION_TYPE_MANY_TO_ONE;
			}
		}
	}
	
	public boolean isMappedBy(){
		boolean mappedBy = false;
		if(RELATION_TYPE_MANY_TO_MANY.equals(getRelationType())) {
			String tableName = relation.getSourceTableView().getPhysicalName();
			TableView joinTable =relation.getTargetTableView();
			if(!joinTable.getExpandedColumns().get(0).getRelationList().get(0).getSourceTableView().getPhysicalName().equals(tableName)){
				mappedBy = true;
			}
		} 
		return mappedBy;
	}   
	
	//获取关联实例名
	public String getTargetInstance() {
		String targetUpperInstance = "";
		if(relation.getSource()==relation.getTarget()) {
			if(RELATION_TYPE_ONE_TO_MANY.equals(getRelationType())) {
				targetUpperInstance= "ChildList";
			} else {
				targetUpperInstance= "Parent";
			}
		} else {
			if(RELATION_TYPE_ONE_TO_MANY.equals(getRelationType())) {
				targetUpperInstance=getFinalTargetTable().getEntityClassName()+"List";
			} else if (RELATION_TYPE_MANY_TO_MANY.equals(getRelationType())){
				targetUpperInstance=getFinalTargetTable().getEntityClassName()+"List";
			}else {
				targetUpperInstance=StringUtils.upperFirst(getJoinColumn().replace("_id", ""));
			}
		}
		return targetUpperInstance;
	}
	
	//获取关联字段
	public String getJoinColumn(){
		String joinColumn = "";
		if(RELATION_TYPE_MANY_TO_ONE.equals(getRelationType()) || RELATION_TYPE_MANY_TO_MANY.equals(getRelationType())) {
			joinColumn=relation.getForeignKeyColumns().get(0).getPhysicalName();
		}
		return joinColumn;
	}

	public String getInverseJoinColumn() {
		String inverseJoinColumn = "";
		String tableName = relation.getSourceTableView().getPhysicalName();
		if(RELATION_TYPE_MANY_TO_MANY.equals(getRelationType())) {
			for(Relation r:relation.getTargetTableView().getIncomingRelations()) {
				if(!r.getSourceTableView().getPhysicalName().equals(tableName)) {
					inverseJoinColumn = r.getForeignKeyColumns().get(0).getPhysicalName();
				}
			}
		} 
		return inverseJoinColumn;
	}
	
	public String getJoinTable() {
		String joinTable ="";
		if(RELATION_TYPE_MANY_TO_MANY.equals(getRelationType())) {
			joinTable = relation.getTargetTableView().getPhysicalName();
		}
		return joinTable;
	}
	
	public boolean isTargetNotNull(){
		return CHILD_CARDINALITY_1.equals(relation.getChildCardinality()) || CHILD_CARDINALITY_1_OR_n.equals(relation.getChildCardinality());
	}
	

	public TreeSet<String> getImportList(Project project){
		TreeSet<String> importList = Sets.newTreeSet();
		if(RELATION_TYPE_ONE_TO_ONE.equals(getRelationType())) {
			importList.add(OneToOne.class.getCanonicalName());
			importList.add(PrimaryKeyJoinColumn.class.getCanonicalName());
		} else if(RELATION_TYPE_ONE_TO_MANY.equals(getRelationType())){
			importList.add(Lists.class.getCanonicalName());
			importList.add(List.class.getCanonicalName());
			importList.add(OneToMany.class.getCanonicalName());
			importList.add(OrderBy.class.getCanonicalName());
			importList.add(Fetch.class.getCanonicalName());
			importList.add(NotFound.class.getCanonicalName());
			importList.add(Cache.class.getCanonicalName());
			importList.add(Where.class.getCanonicalName());
		} else if(RELATION_TYPE_MANY_TO_ONE.equals(getRelationType())){
			importList.add(ManyToOne.class.getCanonicalName());
			importList.add(JoinColumn.class.getCanonicalName());
			importList.add(NotFound.class.getCanonicalName());
			importList.add(NotNull.class.getCanonicalName());
		} else if(RELATION_TYPE_MANY_TO_MANY.equals(getRelationType())){
			importList.add(Lists.class.getCanonicalName());
			importList.add(ManyToMany.class.getCanonicalName());
			importList.add(OrderBy.class.getCanonicalName());
			importList.add(Where.class.getCanonicalName());
			importList.add(NotFound.class.getCanonicalName());
			importList.add(Transient.class.getCanonicalName());
			importList.add(Cache.class.getCanonicalName());
			if(!isMappedBy()) {
				importList.add(JoinTable.class.getCanonicalName());
			}
		}
		return importList;
	}
	
	private TableView getFinalTargetTableView(){
		String tableName = getFinalSourceTableView().getPhysicalName();
		TableView tableView = relation.getTargetTableView();
		if(!isOutgoing()) {
			tableView=relation.getSourceTableView();
		}
		if(RELATION_TYPE_MANY_TO_MANY.equals(getRelationType())) {
			TableView joinTable =relation.getTargetTableView();
			for(NormalColumn normalColumn:joinTable.getExpandedColumns()) {
				for(Relation r:normalColumn.getRelationList()) {
					if(!r.getSourceTableView().getPhysicalName().equals(tableName)) {
						tableView = r.getSourceTableView();
						break;
					}
				}
			}
		} 
		return tableView;
	}
	
	private TableView getFinalSourceTableView(){
		TableView tableView = null;
		if(isOutgoing()) {
			tableView = relation.getSourceTableView();
		} else {
			tableView = relation.getTargetTableView();
		}
		return tableView;
	}
	
	
}
