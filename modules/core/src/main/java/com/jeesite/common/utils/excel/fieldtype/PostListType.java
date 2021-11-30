/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 * No deletion without permission, or be held responsible to law.
 */
package com.jeesite.common.utils.excel.fieldtype;

import java.util.ArrayList;
import java.util.List;

import com.jeesite.common.collect.ListUtils;
import com.jeesite.common.lang.StringUtils;
import com.jeesite.common.utils.SpringUtils;
import com.jeesite.modules.sys.entity.Post;
import com.jeesite.modules.sys.service.PostService;

/**
 * 字段类型转换
 * @author ThinkGem
 * @version 2020-3-5
 * @example fieldType = PostListType.class
 */
public class PostListType implements FieldType {

	private List<Post> postList;
	
	public PostListType() {
		PostService postService = SpringUtils.getBean(PostService.class);
		postList = postService.findList(new Post());
	}
	
	/**
	 * 获取对象值（导入）
	 */
	public Object getValue(String val) {
		List<String> list = new ArrayList<String>();
		for (String s : StringUtils.split(val, ",")) {
			for (Post e : postList) {
				if (StringUtils.trimToEmpty(s).equals(e.getPostName())) {
					list.add(e.getPostCode());
				}
			}
		}
		return list.size() > 0 ? list.toArray(new String[list.size()]) : null;
	}

	/**
	 * 设置对象值（导出）
	 */
	public String setValue(Object val) {
		if (val != null) {
			@SuppressWarnings("unchecked")
			List<Post> postList = (List<Post>) val;
			return ListUtils.extractToString(postList, "postName", ", ");
		}
		return StringUtils.EMPTY;
	}
	
}
