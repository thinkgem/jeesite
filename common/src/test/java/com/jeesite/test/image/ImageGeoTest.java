/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 * No deletion without permission, or be held responsible to law.
 */
package com.jeesite.test.image;

import com.jeesite.common.image.ImageGeoUtils;

import java.util.List;
import java.util.Map;

/**
 * 图片地理信息获取测试类
 * @author ThinkGem
 * @version 2025-12-07
 * @code pom.xml 中添加依赖：<pre>{@code
 * 		<dependency>
 * 			<groupId>com.drewnoakes</groupId>
 * 			<artifactId>metadata-extractor</artifactId>
 * 			<version>${metadata-extractor.version}</version>
 * 		</dependency>
 * 	}</pre>
 */
public class ImageGeoTest {

	public static void main(String[] args) {
		String imgPath = "/Users/admin/Pictures/jeesite/IMAG0068.jpg";
		List<Map<String, Object>> geo = ImageGeoUtils.getImageGeo(imgPath);
		System.out.println(geo.isEmpty() ? "no geo" : geo);
	}

}