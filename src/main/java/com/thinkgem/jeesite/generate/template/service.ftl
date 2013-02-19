package ${packageName}.${moduleName}.service${subModuleName};

import org.apache.commons.lang3.StringUtils;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.service.BaseService;
import ${packageName}.${moduleName}.entity${subModuleName}.${ClassName};
import ${packageName}.${moduleName}.dao${subModuleName}.${ClassName}Dao;

/**
 * ${functionName}Service
 * @author ${classAuthor}
 * @version ${classVersion}
 */
@Component
@Transactional(readOnly = true)
public class ${ClassName}Service extends BaseService {

	@SuppressWarnings("unused")
	private static Logger logger = LoggerFactory.getLogger(${ClassName}Service.class);
	
	@Autowired
	private ${ClassName}Dao ${className}Dao;
	
	public ${ClassName} get(Long id) {
		return ${className}Dao.findOne(id);
	}
	
	public Page<${ClassName}> find(Page<${ClassName}> page, ${ClassName} ${className}) {
		DetachedCriteria dc = ${className}Dao.createDetachedCriteria();
		if (${className}.getUser()!=null && ${className}.getUser().getId()>0){
			dc.add(Restrictions.eq("user.id", ${className}.getUser().getId()));
		}
		if (StringUtils.isNotEmpty(${className}.getName())){
			dc.add(Restrictions.like("name", "%"+${className}.getName()+"%"));
		}
		if (StringUtils.isNotEmpty(${className}.getRemarks())){
			dc.add(Restrictions.like("remarks", "%"+${className}.getRemarks()+"%"));
		}
		dc.add(Restrictions.eq("delFlag", ${ClassName}.DEL_FLAG_NORMAL));
		dc.addOrder(Order.desc("id"));
		return ${className}Dao.find(page, dc);
	}
	
	@Transactional(readOnly = false)
	public void save(${ClassName} ${className}) {
		${className}Dao.save(${className});
	}
	
	@Transactional(readOnly = false)
	public void delete(Long id) {
		${className}Dao.deleteById(id);
	}
	
}
