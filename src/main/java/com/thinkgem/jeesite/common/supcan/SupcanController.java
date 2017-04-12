/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.common.supcan;

import java.lang.reflect.Method;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.thinkgem.jeesite.common.config.Global;
import com.thinkgem.jeesite.common.supcan.annotation.treelist.SupTreeList;
import com.thinkgem.jeesite.common.supcan.annotation.treelist.cols.SupCol;
import com.thinkgem.jeesite.common.supcan.annotation.treelist.cols.SupGroup;
import com.thinkgem.jeesite.common.supcan.treelist.TreeList;
import com.thinkgem.jeesite.common.supcan.treelist.cols.Col;
import com.thinkgem.jeesite.common.supcan.treelist.cols.Group;
import com.thinkgem.jeesite.common.utils.CacheUtils;
import com.thinkgem.jeesite.common.utils.SpringContextHolder;
import com.thinkgem.jeesite.common.utils.StringUtils;
import com.thinkgem.jeesite.common.web.BaseController;

/**
 * 硕正Controller
 * @author ThinkGem
 * @version 2013-11-13
 */
@Controller
@RequestMapping(value = "${adminPath}/supcan")
public class SupcanController extends BaseController {

	private static final String SUPCAN_CACHE = "supcanCache";
	
	/**
	 * 获取硕正树列表描述（根据注解获取XML）
	 * @return
	 */
	@RequestMapping(value = "treeList/{typeAlias}.xml")
	@ResponseBody
	public TreeList treeList(@PathVariable("typeAlias") String typeAlias) {
		
		// 如果使用Cache，并且在Cache里存在，则直接返回。
		boolean useCache = Global.getConfig("supcan.useCache") == "true";
		if (useCache){
			Object object = CacheUtils.get(SUPCAN_CACHE, typeAlias);
			if (object != null){
				return (TreeList)object;
			}
		}
		
		// 实体类型
		Class<?> clazz;
		
		try{
			// 根据别名获取MyBaits注册类型。
			SqlSessionFactory sqlSessionFactory = SpringContextHolder.getBean(SqlSessionFactory.class);
			clazz = sqlSessionFactory.getConfiguration().getTypeAliasRegistry().resolveAlias(typeAlias);
		}catch (Exception e) {
			// 取不到类型，返回空。
			return null;
		}
		
		// 获取硕正注解配置
		SupTreeList supTreeList = clazz.getAnnotation(SupTreeList.class);

		// 没有硕正注解配置，则返回空
		if (supTreeList == null){
			return null;
		}
		
		// 实例化硕正树列表对象
		TreeList treeList = new TreeList(supTreeList);
		
		// 获取表头分组
		Map<String, Group> groupMap = Maps.newHashMap();
		if (supTreeList !=null && supTreeList.groups() != null){
			for (SupGroup supGroup : supTreeList.groups()){
				groupMap.put(supGroup.id(), new Group(supGroup));
			}
		}
		
		// 获取表头列
		List<Object> cols = treeList.getCols();
		for (Method m : clazz.getMethods()){
			SupCol supCol = m.getAnnotation(SupCol.class);
			if (supCol != null){
				
				// 转为为Col对象
				Col col = new Col(supCol);
				if (StringUtils.isBlank(col.getName())){
					col.setName(StringUtils.uncapitalize(StringUtils.substring(m.getName(), 3)));
				}
				
				// 无分组
				if (StringUtils.isBlank(supCol.groupId())){
					cols.add(col);
				}
				// 有分组
				else{
					Group group = groupMap.get(supCol.groupId());
					if (group != null){
						group.getCols().add(col);
					}
				}
			}
		}
		
		// 创建字段排序类
		Comparator<Object> comparator = new Comparator<Object>() {
			@Override
			public int compare(Object o1, Object o2) {
				int sort1 = 0, sort2 = 0;
				if (o1 instanceof Group){
					sort1 = ((Group)o1).getSort();
				}else if (o1 instanceof Col){
					sort1 = ((Col)o1).getSort();
				}
				if (o2 instanceof Group){
					sort2 = ((Group)o2).getSort();
				}else if (o2 instanceof Col){
					sort2 = ((Col)o2).getSort();
				}
				return new Integer(sort1).compareTo(new Integer(sort2));
			}
		};

		// 将列表转换为树结构并排序
		listToTree(cols, groupMap, null, comparator);
		
		// 整体排序
		Collections.sort(cols, comparator);

		// 如果使用Cache，则保存到Cache
		if (useCache){
			CacheUtils.put(SUPCAN_CACHE, typeAlias, treeList);
		}
		
		return treeList;
	}
	
	/**
	 * 将分组转换为树结构
	 * @param list
	 * @param groupMap
	 * @param parentId
	 */
	private void listToTree(List<Object> colList, Map<String, Group> groupMap, String parentId, Comparator<Object> comparator){
		for (Map.Entry<String, Group> e : groupMap.entrySet()){
			Group g = e.getValue();
			if (StringUtils.equals(parentId, g.getParentId())){
				colList.add(g);
				// 判断是否有子节点，有的话则加进去
				for (Map.Entry<String, Group> ec : groupMap.entrySet()){
					Group gc = ec.getValue();
					if (g.getId() != null && g.getId().equals(gc.getParentId())){
						List<Object> childrenList = Lists.newArrayList();
						listToTree(childrenList, groupMap, gc.getParentId(), comparator);
						g.getCols().addAll(childrenList);
						break;
					}
				}
				// 排序
				Collections.sort(g.getCols(), comparator);
			}
		}
	}
	
	/**
	 * 获取硕正树列表描述（注册对象方法获取XML）  测试实例
	 * @return
	 */
	@RequestMapping(value = "treeList/test/test.xml")
	@ResponseBody
	public TreeList treeListTest() {

		// 创建树列表描述对象
		TreeList treeList = new TreeList();
		
		// 设置树列表，表头
		List<Object> cols = treeList.getCols();
		cols.add(new Col("id", "编号"));
		cols.add(new Col("office", "归属部门"));
		cols.add(new Col("loginName", "登录名"));
		cols.add(new Col("name", "名称"));
		cols.add(new Col("remarks", "备注"));
		
		// 设置树列表，多层表头
		
		// 分组1
		Group group = new Group("时间");
		List<Object> groupCol = group.getCols();
		groupCol.add(new Col("createDate", "创建时间"));
		groupCol.add(new Col("updateDate", "更新时间"));
		
		// 分组2
		Group group2 = new Group("时间2");
		List<Object> group2Col = group2.getCols();
		group2Col.add(new Col("createDate2", "创建时间2"));
		group2Col.add(new Col("updateDate2", "更新时间2"));
		
		// 将分组2添加到，分组1的表头
		groupCol.add(group2);

		// 将分组1添加到，主表头
		cols.add(group);
		
		// 返回TreeList描述对象
		return treeList;
	}
}
