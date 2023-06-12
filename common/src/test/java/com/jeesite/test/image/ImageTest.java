/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 * No deletion without permission, or be held responsible to law.
 */
package com.jeesite.test.image;

import com.jeesite.common.image.ImageUtils;
import com.jeesite.common.io.FileUtils;

import java.io.File;

/**
 * 图片压缩测试类
 * @author ThinkGem
 * @version 2023-6-9
 */
public class ImageTest {

	public static void main(String[] args) {
		String baseDir = FileUtils.getProjectPath();
		String imgPath2 = baseDir + "\\target\\zxing.png";
		ImageUtils.thumbnails(new File(imgPath2), 100, 100, "jpg");
	}

}