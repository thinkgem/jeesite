<#assign columnList = sourceTable.sourceColumnList>
package ${sourceTable.entityPackage};

<#list importList as import>
	<#if (import !="")>
import ${import};
	</#if>
</#list>

@Entity
@Table(name = "${sourceTable.physicalName}")
@DynamicInsert @DynamicUpdate
public class ${sourceTable.entityClassName} extends ${sourceTable.entityExtendType}<${sourceTable.entityClassName}> {
	private static final long serialVersionUID = 1L;
<#list columnList as column>
	<#assign normalColumn = column.normalColumn>
	<#if (normalColumn.relationList?size==0)>
	private ${column.className} ${column.instance?uncap_first};
	</#if>
	<#if (column.sourceRelationList?size>0)>
		<#list column.sourceRelationList as relation>
			<#if relation.relationType =='OneToMany' || relation.relationType =='ManyToMany' >
	private List<${relation.finalTargetTable.entityClassName}> ${relation.targetInstance?uncap_first} = Lists.newArrayList();
			</#if>
			<#if !(relation.relationType =='OneToMany' || relation.relationType =='ManyToMany') >
	private ${relation.finalTargetTable.entityClassName} ${relation.targetInstance?uncap_first};
			</#if>
		</#list>
	</#if>
</#list>
<#if sourceTable.containsProcessInstanceId>
	private boolean pass;
	private boolean audit;
	private String auditRemarks;
</#if>

	public ${sourceTable.entityClassName}(){
		super();
	}
	
	public ${sourceTable.entityClassName}(Long id){
		this();
		this.id = id;
	}

<#list columnList as column>
	<#assign normalColumn = column.normalColumn>
	<#if (normalColumn.relationList?size==0)>

		<#if normalColumn.physicalName == "id">
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
		</#if>
	public ${column.className} get${column.instance}() {
		return this.${column.instance?uncap_first};
	}
	public void set${column.instance}(${column.className} ${column.instance?uncap_first}) {
		this.${column.instance?uncap_first} = ${column.instance?uncap_first};
	}
	</#if>
	<#if (column.sourceRelationList?size>0)>
		<#list column.sourceRelationList as relation>
			<#if relation.relationType =='OneToOne'>
				<#if relation.outgoing>

	@OneToOne(cascade=CascadeType.ALL)
	@PrimaryKeyJoinColumn
	public ${relation.finalTargetTable.entityClassName} get${relation.targetInstance}() {
		return ${relation.targetInstance?uncap_first};
	}
	public void set${relation.targetInstance}(${relation.finalTargetTable.entityClassName} ${relation.targetInstance?uncap_first}) {
		this.${relation.targetInstance?uncap_first} = ${relation.targetInstance?uncap_first};
	}
				</#if>
				<#if !relation.outgoing>

	@OneToOne(mappedBy="${relation.targetInstance?uncap_first}",cascade=CascadeType.ALL)
	@PrimaryKeyJoinColumn
	public ${relation.finalTargetTable.entityClassName} get${relation.targetInstance}() {
		return this.${relation.targetInstance?uncap_first};
	}
	public void set${relation.targetInstance}(${relation.finalTargetTable.entityClassName} ${relation.targetInstance?uncap_first}) {
		this.${relation.targetInstance?uncap_first} = ${relation.targetInstance?uncap_first};
	}
				</#if>
			</#if>
			<#if relation.relationType =='OneToMany'>

	@OneToMany(mappedBy = "${relation.targetInstance?uncap_first}", fetch=FetchType.LAZY)
			<#if relation.finalTargetTable.containsDelFlag>
	@Where(clause="del_flag='"+DEL_FLAG_NORMAL+"'")
			</#if>
	@OrderBy(value="id") @Fetch(FetchMode.SUBSELECT)
	@NotFound(action = NotFoundAction.IGNORE)
	public List<${relation.finalTargetTable.entityClassName}> get${relation.targetInstance}() {
		return this.${relation.targetInstance?uncap_first};
	}
	public void set${relation.targetInstance}(List<${relation.finalTargetTable.entityClassName}> ${relation.targetInstance?uncap_first}) {
		this.${relation.targetInstance?uncap_first} = ${relation.targetInstance?uncap_first};
	}
			</#if>
			<#if relation.relationType =='ManyToOne'>

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="${relation.joinColumn}")
	@NotFound(action = NotFoundAction.IGNORE)
				<#if relation.targetNotNull>
	@NotNull
				</#if>
	public ${relation.finalTargetTable.entityClassName} get${relation.targetInstance}() {
		return this.${relation.targetInstance?uncap_first};
	}
	public void set${relation.targetInstance}(${relation.finalTargetTable.entityClassName} ${relation.targetInstance?uncap_first}) {
		this.${relation.targetInstance?uncap_first} = ${relation.targetInstance?uncap_first};
	}
			</#if>
			<#if relation.relationType =='ManyToMany'>
			<#if relation.mappedBy>

	@ManyToMany(mappedBy = "${relation.targetInstance?uncap_first}", fetch=FetchType.LAZY)
			<#if relation.finalTargetTable.containsDelFlag>
	@Where(clause="del_flag='"+DEL_FLAG_NORMAL+"'")
			</#if>
	@OrderBy("id") @Fetch(FetchMode.SUBSELECT)
	@NotFound(action = NotFoundAction.IGNORE)
		</#if>
		<#if !relation.mappedBy>

	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(name = "${relation.joinTable}", joinColumns = { @JoinColumn(name = "${relation.joinColumn}") }, inverseJoinColumns = { @JoinColumn(name = "${relation.inverseJoinColumn}") })
			<#if relation.finalTargetTable.containsDelFlag>
	@Where(clause="del_flag='"+DEL_FLAG_NORMAL+"'")
			</#if>
	@OrderBy("id") @Fetch(FetchMode.SUBSELECT)
	@NotFound(action = NotFoundAction.IGNORE)
		</#if>
	public List<${relation.finalTargetTable.entityClassName}> get${relation.targetInstance}() {
		return this.${relation.targetInstance?uncap_first};
	}
	public void set${relation.targetInstance}(List<${relation.finalTargetTable.entityClassName}> ${relation.targetInstance?uncap_first}) {
		this.${relation.targetInstance?uncap_first} = ${relation.targetInstance?uncap_first};
	}

	@Transient
	public List<Long> get${relation.finalTargetTable.entityClassName}IdList() {
		List<Long> ${relation.finalTargetTable.entityClassName?uncap_first}IdList = Lists.newArrayList();
		for (${relation.finalTargetTable.entityClassName} ${relation.finalTargetTable.entityClassName?uncap_first} : ${relation.targetInstance?uncap_first}) {
			${relation.finalTargetTable.entityClassName?uncap_first}IdList.add(${relation.finalTargetTable.entityClassName?uncap_first}.getId());
		}
		return ${relation.finalTargetTable.entityClassName?uncap_first}IdList;
	}
	@Transient
	public void set${relation.finalTargetTable.entityClassName}IdList(List<Long> ${relation.finalTargetTable.entityClassName?uncap_first}IdList) {
		${relation.targetInstance?uncap_first} = Lists.newArrayList();
		for (Long ${relation.finalTargetTable.entityClassName?uncap_first}Id : ${relation.finalTargetTable.entityClassName?uncap_first}IdList) {
			${relation.finalTargetTable.entityClassName} ${relation.finalTargetTable.entityClassName?uncap_first} = new ${relation.finalTargetTable.entityClassName}();
			${relation.finalTargetTable.entityClassName?uncap_first}.setId(${relation.finalTargetTable.entityClassName?uncap_first}Id);
			${relation.targetInstance?uncap_first}.add(${relation.finalTargetTable.entityClassName?uncap_first});
		}
	}
			</#if>
		</#list>
	</#if>
</#list>
<#if sourceTable.containsProcessInstanceId>
	@Transient
	public boolean isPass() {
		return pass;
	}

	@Transient
	public void setPass(boolean pass) {
		this.pass = pass;
	}

	@Transient
	public String getAuditRemarks() {
		return auditRemarks;
	}

	@Transient
	public void setAuditRemarks(String auditRemarks) {
		this.auditRemarks = auditRemarks;
	}

	@Transient
	public boolean isAudit() {
		return audit;
	}

	@Transient
	public void setAudit(boolean audit) {
		this.audit = audit;
	}
</#if>

	@Override
	public int hashCode() {
		return Objects.hashCode(id);
	}

	@Override
	public boolean equals(Object obj) {
	    if (this == obj) {
	        return true;
	    }
	    if (obj == null || getClass() != obj.getClass()) {
	        return false;
	    }
	    ${sourceTable.entityClassName} other = (${sourceTable.entityClassName}) obj;
	    return Objects.equal(this.id, other.id);
	}
}


