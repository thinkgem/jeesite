/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 * No deletion without permission, or be held responsible to law.
 */
package com.jeesite.common.io;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * 数据流工具类
 * @author ThinkGem
 * @version 2025-08-08
 */
public class IOUtils extends org.apache.commons.io.IOUtils {

	private static final Logger logger = LoggerFactory.getLogger(IOUtils.class);

	/**
	 * 根据文件路径创建文件输入流处理 以字节为单位（非 unicode ）
	 * @param filePath 文件路径
	 * @return 文件流
	 */
	public static FileInputStream getFileInputStream(String filePath) {
		FileInputStream fileInputStream = null;
		try {
			fileInputStream = new FileInputStream(filePath);
		} catch (FileNotFoundException e) {
			logger.error("文件不存在!", e);
		}
		return fileInputStream;
	}

	/**
	 * 根据文件对象创建文件输入流处理 以字节为单位（非 unicode ）
	 * @param file 文件对象
	 * @return 文件流
	 */
	public static FileInputStream getFileInputStream(File file) {
		FileInputStream fileInputStream = null;
		try {
			fileInputStream = new FileInputStream(file);
		} catch (FileNotFoundException e) {
			logger.error("文件不存在!", e);
		}
		return fileInputStream;
	}

	/**
	 * 根据文件对象创建文件输出流处理 以字节为单位（非 unicode ）
	 * @param file 文件对象
	 * @param append true:文件以追加方式打开,false:则覆盖原文件的内容
	 * @return 文件流
	 */
	public static FileOutputStream getFileOutputStream(File file, boolean append) {
		FileOutputStream fileOutputStream = null;
		try {
			fileOutputStream = new FileOutputStream(file, append);
		} catch (FileNotFoundException e) {
			logger.error("文件不存在!", e);
		}
		return fileOutputStream;
	}

	/**
	 * 根据文件路径创建文件输出流处理 以字节为单位（非 unicode ）
	 * @param filePath 文件路径
	 * @param append true:文件以追加方式打开,false:则覆盖原文件的内容
	 * @return 文件流
	 */
	public static FileOutputStream getFileOutputStream(String filePath, boolean append) {
		FileOutputStream fileOutputStream = null;
		try {
			fileOutputStream = new FileOutputStream(filePath, append);
		} catch (FileNotFoundException e) {
			logger.error("文件不存在!", e);
		}
		return fileOutputStream;
	}

	/**
	 * Closes a <code>Closeable</code> unconditionally.
	 */
	public static void closeQuietly(final InputStream input) {
        closeQuietly((Closeable) input);
    }
	
	/**
	 * Closes a <code>Closeable</code> unconditionally.
	 */
	public static void closeQuietly(final Closeable closeable) {
        try {
            if (closeable != null) {
                closeable.close();
            }
        } catch (final IOException ioe) {
            // ignore
        }
    }

	@Deprecated
	public IOUtils() {
		// empty
	}
}