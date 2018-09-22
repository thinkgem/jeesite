/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.common.codec;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;

import com.jeesite.common.io.IOUtils;

/**
 * MD5不可逆加密工具类
 * @author ThinkGem
 */
public class Md5Utils {

	private static final String MD5 = "MD5";
	private static final String DEFAULT_ENCODING = "UTF-8";
	
	/**
	 * 对输入字符串进行md5散列.
	 * @param input 加密字符串
	 */
	public static String md5(String input) {
		return md5(input, 1);
	}
	
	/**
	 * 对输入字符串进行md5散列.
	 * @param input 加密字符串
	 * @param iterations 迭代次数
	 */
	public static String md5(String input, int iterations) {
		try {
			return EncodeUtils.encodeHex(DigestUtils.digest(input.getBytes(DEFAULT_ENCODING), MD5, null, iterations));
		} catch (UnsupportedEncodingException e) {
			return StringUtils.EMPTY;
		}
	}
	
	/**
	 * 对输入字符串进行md5散列.
	 * @param input 加密字符串
	 */
	public static byte[] md5(byte[] input) {
		return md5(input, 1);
	}
	
	/**
	 * 对输入字符串进行md5散列.
	 * @param input 加密字符串
	 * @param iterations 迭代次数
	 */
	public static byte[] md5(byte[] input, int iterations) {
		return DigestUtils.digest(input, MD5, null, iterations);
	}
	
	/**
	 * 对文件进行md5散列.
	 */
	public static byte[] md5(InputStream input) throws IOException {
		return DigestUtils.digest(input, MD5);
	}

	/**
	 * 获取文件的MD5值
	 */
	public static String md5File(File file) {
		return md5File(file, -1);
	}
	
	/**
	 * 获取文件的MD5值，支持获取文件部分的MD5值
	 * uploader.md5File(file, 0, 10 * 1024 * 1024)
	 */
	public static String md5File(File file, int size) {
		if (file != null && file.exists()){
	        try (InputStream in = FileUtils.openInputStream(file)){
	            byte[] bytes = null;
	            if (size != -1 && file.length() >= size){
	            	bytes = IOUtils.toByteArray(in, size);
	            }else{
	            	bytes = IOUtils.toByteArray(in);
	            }
	            return EncodeUtils.encodeHex(md5(bytes));
	        } catch (IOException e) {
				return StringUtils.EMPTY;
	        }
		}
		return StringUtils.EMPTY;
	}
	
}
