/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 * No deletion without permission, or be held responsible to law.
 */
package com.jeesite.test.image;

import com.jeesite.common.image.ZxingUtils;
import com.jeesite.common.io.FileUtils;

/**
 * 条形码和二维码编码解码
 * @author ThinkGem
 * @version 2023-6-9
 */
public class ZxingUtilsTest {

	public static void main(String[] args) {
		String baseDir = FileUtils.getProjectPath();

		// 条形码
		String imgPath = baseDir + "\\target\\zxing_EAN13.png";
		String contents = "6923450657713";
		int width = 105, height = 50;

		ZxingUtils.encode(contents, width, height, imgPath);
		System.out.println("finished zxing EAN-13 encode.");

		String decodeContent = ZxingUtils.decode(imgPath);
		System.out.println("解码内容如下：" + decodeContent);
		System.out.println("finished zxing EAN-13 decode.");

		// 二维码
		String imgPath2 = baseDir + "\\target\\zxing.png";
		String contents2 = "Hello Gem, welcome to Zxing!\nEMail [ thinkgem@163.com ]";
		int width2 = 300, height2 = 300;

		ZxingUtils.encode2(contents2, width2, height2, imgPath2);
		System.out.println("finished zxing encode.");

		String decodeContent2 = ZxingUtils.decode2(imgPath2);
		System.out.println("解码内容如下：" + decodeContent2);
		System.out.println("finished zxing decode.");

	}

}