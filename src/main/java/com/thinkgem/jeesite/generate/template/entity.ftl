/**
 * There are <a href="https://github.com/thinkgem/jeesite">JeeSite</a> code generation
 */
package ${packageName}.${moduleName}.entity${subModuleName};

import javax.persistence.Entity;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.validator.constraints.Length;

import com.thinkgem.jeesite.common.persistence.IdEntity;

/**
 * ${functionName}Entity
 * @author ${classAuthor}
 * @version ${classVersion}
 */
@Entity
@Table(name = "${tableName}")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class ${ClassName} extends IdEntity<${ClassName}> {
	
	private static final long serialVersionUID = 1L;
	private String id; 		// 编号
	private String name; 	// 名称

	public ${ClassName}() {
		super();
	}

	public ${ClassName}(String id){
		this();
		this.id = id;
	}
	 
	@Length(min=1, max=200)
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
}


