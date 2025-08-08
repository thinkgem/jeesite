/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 * No deletion without permission, or be held responsible to law.
 */
package com.jeesite.common.image;

import com.jeesite.common.io.FileUtils;
import com.jeesite.common.lang.StringUtils;
import net.coobird.thumbnailator.Thumbnails;
import net.coobird.thumbnailator.Thumbnails.Builder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * 图片处理工具类
 * @author ThinkGem
 * @version 2018年12月31日
 */
public class ImageUtils {

	private static final Logger logger = LoggerFactory.getLogger(ImageUtils.class);

	/**
	 * 缩略图生成，处理一些较大的图片，防止占用太多的网络资源
	 */
	public static void thumbnails(File imageFile, int maxWidth, int maxHeight, String outputFormat){
		thumbnails(imageFile, maxWidth, maxHeight, outputFormat, imageFile);
	}

	/**
	 * 缩略图生成，处理一些较大的图片，防止占用太多的网络资源
	 */
	public static void thumbnails(File imageFile, int maxWidth, int maxHeight, String outputFormat, File targetFile){
		if (imageFile == null || !imageFile.exists() || (maxWidth <= 0 && maxHeight <= 0)){
			return;
		}
		// 只处理可以压缩的图片，如gif图片压缩后会出现黑色背景的情况
		String extension = FileUtils.getFileExtension(imageFile.getName());
		if (!StringUtils.inString(extension, "png", "jpg", "jpeg", "bmp", "ico")){
			return;
		}
		try{
			BufferedImage bufferedImage = ImageIO.read(imageFile);
			Builder<BufferedImage> bilder = Thumbnails.of(bufferedImage);
			if (bufferedImage != null){
				if (maxWidth > 0){
					if (bufferedImage.getWidth() <= maxWidth){
						bilder.width(bufferedImage.getWidth());
					}else{
						bilder.width(maxWidth);
					}
				}
				if (maxHeight > 0){
					if (bufferedImage.getHeight() <= maxHeight){
						bilder.height(bufferedImage.getHeight());
					}else{
						bilder.height(maxHeight);
					}
				}
				if (StringUtils.isNotBlank(outputFormat)){
					bilder.outputFormat(outputFormat);
				}
				bilder.toFile(targetFile);
			}
		}catch(IOException e){
			logger.error("图片压缩失败：{}", imageFile.getAbsoluteFile(), e);
		}
	}
	
}
