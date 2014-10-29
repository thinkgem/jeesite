/**
 * There are <a href="https://github.com/thinkgem/jeesite">JeeSite</a> code generation
 */
package com.thinkgem.jeesite.modules.prj.service;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.insightech.er.editor.model.ERDiagram;
import org.insightech.er.editor.model.diagram_contents.element.node.table.ERTable;
import org.insightech.er.editor.persistent.Persistent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.service.BaseService;
import com.thinkgem.jeesite.common.utils.Collections3;
import com.thinkgem.jeesite.common.utils.FileUtils;
import com.thinkgem.jeesite.common.utils.FreeMarkers;
import com.thinkgem.jeesite.common.utils.StringUtils;
import com.thinkgem.jeesite.modules.prj.dao.ProjectDao;
import com.thinkgem.jeesite.modules.prj.entity.Project;
import com.thinkgem.jeesite.modules.prj.utils.SourceColumn;
import com.thinkgem.jeesite.modules.prj.utils.SourceRelation;
import com.thinkgem.jeesite.modules.prj.utils.SourceTable;
import com.thinkgem.jeesite.modules.prj.utils.SourceUtils;

import freemarker.template.Configuration;
import freemarker.template.Template;

/**
 * 项目Service
 * @author liuj
 * @version 2013-12-07
 */
@Component
@Transactional(readOnly = true)
public class ProjectService extends BaseService {

	@Autowired
	private ProjectDao projectDao;
	
	
	public Project get(String id) {
		return projectDao.get(id);
	}
	
	public Page<Project> find(Page<Project> page, Project project) {
		DetachedCriteria dc = projectDao.createDetachedCriteria();
		if (StringUtils.isNotEmpty(project.getName())){
			dc.add(Restrictions.like("name", "%"+project.getName()+"%"));
		}
		dc.add(Restrictions.eq("delFlag", Project.DEL_FLAG_NORMAL));
		dc.addOrder(Order.desc("id"));
		return projectDao.find(page, dc);
	}
	
	
	public Map<String,ERTable> findAllTable(Project project){
		Map<String,ERTable> map = Maps.newLinkedHashMap();
		if(Collections3.isEmpty(project.getErmPathList())){
			return map;
		}
		Persistent persistent = Persistent.getInstance();
		InputStream in = null;
		for(String ermPath:project.getErmPathList()) {
			File file = new File(FileUtils.getAbsolutePath(ermPath));
			try {
				in =new BufferedInputStream(new FileInputStream(file));
				ERDiagram diagram = persistent.load(in);
				List<ERTable> erTableList = diagram.getDiagramContents().getContents().getTableSet().getList();
				for(ERTable erTable:erTableList) {
					map.put(erTable.getPhysicalName(), erTable);
				}
			} catch (FileNotFoundException e) {
				logger.error("文件"+ermPath+"没有找到");
			} catch (Exception e) {
				logger.error("解析文件"+ermPath+"时出错");
			} finally {
				if (in != null) {
					try {
						in.close();
					} catch (IOException e) {
						logger.error("关闭文件"+ermPath+"时出错");
					}
				}
			}
		}
		return map;
	}
	
	
	public String generate(Project project) {
		if(SourceUtils.TEMPLATE_TYPE_ENTITY_AND_DAO.equals(project.getTemplateType())) {
			return generateEntityAndDao(project);
		} else if(SourceUtils.TEMPLATE_TYPE_SINGLE.equals(project.getTemplateType())) {
			return generateSingle(project);
		}
		return null;
	}
	
	
	private String generateSingle(Project project) {
		String templatePath = SourceUtils.getTemplatePath(SourceUtils.TEMPLATE_TYPE_SINGLE);
		String generateRootPath = SourceUtils.getGenerateRootPath();
		String zipPath = generateRootPath+".zip";
		// 代码模板配置
		Configuration cfg = new Configuration();
		try {
			cfg.setDirectoryForTemplateLoading(new File(templatePath));
		} catch (IOException e) {
			logger.error("模板设置失败");
		}
		Map<String,ERTable> map = findAllTable(project);
		for(ERTable erTable:map.values()) {
			SourceTable sourceTable = new SourceTable(project,erTable);
			if(sourceTable.getPhysicalNames().contains("id")) {
				//生成所有Controller
				Map<String, Object> model = Maps.newHashMap();
				TreeSet<String> importList = Sets.newTreeSet();
				String templateFile = "";
				if(sourceTable.isContainsParentIds()) {
					templateFile="tree/controller.ftl";
				} else  if(sourceTable.isContainsProcessInstanceId()) {
					templateFile="process/controller.ftl";
				} else {
					templateFile="normal/controller.ftl";
				}
				importList.addAll(SourceUtils.getImportList(SourceUtils.SOURCE_TYPE_CONTROLLER));
				model.put("sourceTable", sourceTable);
				model.put("importList", importList);
				model.put("f", SourceUtils.getFreemarkerMap());
				// 生成 Entity
				Template template=null;
				try {
					template = cfg.getTemplate(templateFile);
				} catch (IOException e) {
					logger.error("获取模板出错");
				}
				String content = FreeMarkers.renderTemplate(template, model);
				String filePath = generateRootPath+"\\src\\main\\java\\"+ project.getRootPackage().replace(".", "\\") + "\\modules\\" + sourceTable.getModuleName() + "\\controller\\"+ sourceTable.getControllerClassName() +".java"; 
				FileUtils.writeFile(content, filePath);
				//生成所有Service
				
				//生成所有form.jsp
				
				//生成所有list.jsp
				
				//生成所有detail.jsp
			}
		}
		FileUtils.zipFiles(generateRootPath, "", zipPath);
		return zipPath;
	}
	
	
	
	private String generateEntityAndDao(Project project) {
		String templatePath = SourceUtils.getTemplatePath(SourceUtils.TEMPLATE_TYPE_ENTITY_AND_DAO);
		String generateRootPath = SourceUtils.getGenerateRootPath();
		String zipPath = generateRootPath+".zip";
		// 代码模板配置
		Configuration cfg = new Configuration();
		try {
			cfg.setDirectoryForTemplateLoading(new File(templatePath));
		} catch (IOException e) {
			logger.error("模板设置失败");
		}
		Map<String,ERTable> map = findAllTable(project);
		//生成所有Entity
		for(ERTable erTable:map.values()) {
			SourceTable sourceTable = new SourceTable(project,erTable);
			if(sourceTable.getPhysicalNames().contains("id")) {
				// 定义模板变量
				Map<String, Object> model = Maps.newHashMap();
				TreeSet<String> importList = Sets.newTreeSet();
				for(SourceColumn sourceColumn:sourceTable.getSourceColumnList()) {
					//计算需要import的项目
					if(Collections3.isEmpty(sourceColumn.getNormalColumn().getRelationList())){
						String importName = sourceColumn.getImportName();
						if(StringUtils.isNotBlank(importName)) {
							importList.add(importName);
						}
					} 
					if(!Collections3.isEmpty(sourceColumn.getSourceRelationList())) {
						for(SourceRelation sourceRelation:sourceColumn.getSourceRelationList()) {
							importList.addAll(sourceRelation.getImportList(project));
						}
					}
				}
				importList.addAll(SourceUtils.getImportList(SourceUtils.SOURCE_TYPE_ENTITY));
				model.put("sourceTable", sourceTable);
				model.put("importList", importList);
				// 生成 Entity
				Template template=null;
				try {
					template = cfg.getTemplate("entity.ftl");
				} catch (IOException e) {
					logger.error("获取模板出错");
				}
				String content = FreeMarkers.renderTemplate(template, model);
				String filePath = generateRootPath+"\\src\\main\\java\\"+ project.getRootPackage().replace(".", "\\") + "\\modules\\" + sourceTable.getModuleName() + "\\entity\\"+ sourceTable.getEntityClassName() +".java"; 
				FileUtils.writeFile(content, filePath);
			}
		}
		//生成所有Dao
		for(ERTable erTable:map.values()) {
			SourceTable sourceTable = new SourceTable(project,erTable);
			if(sourceTable.getPhysicalNames().contains("id")) {
				// 定义模板变量
				Map<String, Object> model = Maps.newHashMap();
				TreeSet<String> importList = Sets.newTreeSet();
				model.put("sourceTable", sourceTable);
				model.put("importList", importList);
				importList.add(sourceTable.getEntityPackage() + "." + sourceTable.getEntityClassName());
				importList.addAll(SourceUtils.getImportList(SourceUtils.SOURCE_TYPE_DAO));
				// 生成 Entity
				Template template=null;
				try {
					template = cfg.getTemplate("dao.ftl");
				} catch (IOException e) {
					logger.error("获取模板出错");
				}
				String content = FreeMarkers.renderTemplate(template, model);
				String filePath = generateRootPath+"\\src\\main\\java\\"+ project.getRootPackage().replace(".", "\\") + "\\modules\\" + sourceTable.getModuleName() + "\\dao\\"+ sourceTable.getDaoClassName() +".java"; 
				FileUtils.writeFile(content, filePath);
			}
		}
		
		FileUtils.zipFiles(generateRootPath, "", zipPath);
		return zipPath;
	}

	
	@Transactional(readOnly = false)
	public void save(Project project) {
		projectDao.save(project);
	}
	
	@Transactional(readOnly = false)
	public void delete(String id) {
		projectDao.deleteById(id);
	}
	
}
