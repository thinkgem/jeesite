<#assign columnList = sourceTable.sourceColumnList>
package ${sourceTable.servicePackage};

<#list importList as import>
	<#if (import !="")>
import ${import};
	</#if>
</#list>

@Service
@Transactional(readOnly = true)
public class ${sourceTable.serviceClassName} extends BaseService {

	@Autowired
	private ${sourceTable.daoClassName} ${sourceTable.daoClassName?uncap_first};
	
	public ${sourceTable.entityClassName} get(Long id) {
		return ${sourceTable.daoClassName?uncap_first}.get(id);
	}
	
	public Page<${sourceTable.entityClassName}> find(Page<${sourceTable.entityClassName}> page, ${sourceTable.entityClassName} ${sourceTable.entityClassName?uncap_first}) {
		DetachedCriteria dc = ${sourceTable.daoClassName?uncap_first}.createDetachedCriteria();
		if(StringUtils.isNotBlank(${sourceTable.entityClassName?uncap_first}.getIds())) {
			dc.add(Restrictions.in("id", getIdList(${sourceTable.entityClassName?uncap_first}.getIds())));
		}
		<#list columnList as column>
			<#if column.javaClass == 'String'>
		if(StringUtils.isNotBlank(${sourceTable.entityClassName?uncap_first}.get${column.instance}())) {
			dc.add(Restrictions.like("${column.instance?uncap_first}", "%"+${sourceTable.entityClassName?uncap_first}.get${column.instance}()+"%"));
		}
			<#else>
		if(${sourceTable.entityClassName}.get${column.instance}() != null) {
			dc.add(Restrictions.eq("${column.instance?uncap_first}", ${sourceTable.entityClassName?uncap_first}.get${column.instance}()));
		}
			</#if>
		</#list>
		<#if sourceTable.entityExtendType =='DataEntity'>
		if(${sourceTable.entityClassName?uncap_first}.getCreateDateStart()!=null) {
			dc.add(Restrictions.ge("createDate", ${sourceTable.entityClassName?uncap_first}.getCreateDateStart()));
		}
		if(${sourceTable.entityClassName?uncap_first}.getCreateDateEnd()!=null) {
			dc.add(Restrictions.le("createDate", ${sourceTable.entityClassName?uncap_first}.getCreateDateEnd()));
		}
		if(${sourceTable.entityClassName?uncap_first}.getUpdateDateStart()!=null) {
			dc.add(Restrictions.ge("updateDate", ${sourceTable.entityClassName?uncap_first}.getUpdateDateStart()));
		}
		if(${sourceTable.entityClassName?uncap_first}.getCreateDateEnd()!=null) {
			dc.add(Restrictions.le("updateDate", ${sourceTable.entityClassName?uncap_first}.getUpdateDateEnd()));
		}
 		if (StringUtils.isNotBlank(${sourceTable.entityClassName?uncap_first}.getRemarks())){
			dc.add(Restrictions.like("remarks", "%"+${sourceTable.entityClassName?uncap_first}.getRemarks()+"%"));
		}
		</#if>
		<#if sourceTable.containsDelFlag>
		dc.add(Restrictions.eq(${sourceTable.entityClassName}.FIELD_DEL_FLAG, ${sourceTable.entityClassName}.DEL_FLAG_NORMAL));
		</#if>
		dc.addOrder(Order.desc("id"));
		return ${sourceTable.daoClassName?uncap_first}.find(page, dc);
	}
	
	@Transactional(readOnly = false)
	public void save(${sourceTable.entityClassName} ${sourceTable.entityClassName?uncap_first}) {
		${sourceTable.daoClassName?uncap_first}.save(${sourceTable.entityClassName?uncap_first});
	}
	
	@Transactional(readOnly = false)
	public void delete(Long id) {
		${sourceTable.daoClassName?uncap_first}.deleteById(id);
	}

	public List<${sourceTable.entityClassName}> findAllList() {
		return ${sourceTable.daoClassName?uncap_first}.findAllList();
	}
	<#if sourceTable.containsName>

	public ${sourceTable.entityClassName} findByName(String name) {
		return ${sourceTable.daoClassName?uncap_first}.findByName(name);
	}
	</#if>
}
