package ${sourceTable.daoPackage};

<#list importList as import>
	<#if (import !="")>
import ${import};
	</#if>
</#list>

@Repository
public class ${sourceTable.daoClassName} extends BaseDao<${sourceTable.entityClassName}> {
	<#if sourceTable.containsDelFlag>

	public List<${sourceTable.entityClassName}> findAllList() {
		return find("from ${sourceTable.entityClassName} where delFlag=:p1 order by id", new Parameter(${sourceTable.entityClassName}.DEL_FLAG_NORMAL));
	}
	<#else>
	public List<${sourceTable.entityClassName}> findAllList() {
		return find("from ${sourceTable.entityClassName} order by id");
	}
	</#if>
	<#if sourceTable.containsParentIds>

	public List<${sourceTable.entityClassName}> findByParentIdsLike(String parentIds){
		return find("from ${sourceTable.entityClassName} where parentIds like :p1", new Parameter(parentIds));
	}
	
	public List<${sourceTable.entityClassName}> findAllChild(Long parentId, String likeParentIds){
		return find("from ${sourceTable.entityClassName} where delFlag=:p1 and (id=:p2 or parent.id=:p2 or parentIds like :p3) order by id", new Parameter(${sourceTable.entityClassName}.DEL_FLAG_NORMAL, parentId, likeParentIds));
	}
	</#if>
	<#if sourceTable.containsName>

	public ${sourceTable.entityClassName} findByName(String name){
		return get("from ${sourceTable.entityClassName} where delFlag = :p1 and name = :p2", new Parameter(${sourceTable.entityClassName}.DEL_FLAG_NORMAL, name));
	}
	</#if>
}
