/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 * No deletion without permission, or be held responsible to law.
 */
package com.jeesite.common.codec;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.io.UnsupportedEncodingException;

import com.jeesite.common.lang.StringUtils;

/**
 * MD5不可逆加密工具类
 * @author ThinkGem
 */
public class Md5Utils {

	private static final String MD5 = "MD5";
	
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
			return EncodeUtils.encodeHex(DigestUtils.digest(input.getBytes(EncodeUtils.UTF_8), MD5, null, iterations));
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
	 * @param cutSize 截取大小（前后文件内容）
	 */
	public static String md5File(File file, int cutSize) {
		if (file != null && file.exists()){
			long size = file.length();
			//System.out.println("file size " + Math.floor(size / 1024 / 1024) + "M");
			try(RandomAccessFile randomAccessFile = new RandomAccessFile(file, "r");){
	            if (cutSize != -1 && size >= cutSize){
	            	byte[] bytes = new byte[cutSize];
	    			randomAccessFile.read(bytes);
		            String firstMd5 = EncodeUtils.encodeHex(md5(bytes));
	    			//System.out.println("first md5 val " + firstMd5);
		            if (size > cutSize) {
		    			long startPos = size - cutSize;
		    			long sizeDiff = size - (cutSize * 2);
		    			if (sizeDiff < 0){
		    				startPos += Math.abs(sizeDiff);
		    			}
		    			//System.out.println("last md5 pos " + startPos + " " + size + " "
		    			//		+ Math.floor((size - startPos) / 1024 / 1024) + "M");
		    			bytes = new byte[(int)(size - startPos)];
		    			randomAccessFile.seek(startPos);
		    			randomAccessFile.read(bytes);
		    			String lastMd5 = EncodeUtils.encodeHex(md5(bytes));
		    			//System.out.println("last md5 val " + lastMd5);
		    			return firstMd5.substring(8, 24) + lastMd5.substring(8, 24);
		            }else {
		            	return firstMd5;
		            }
	            }else{
	            	byte[] bytes = new byte[(int)size];
	    			randomAccessFile.read(bytes);
		            return EncodeUtils.encodeHex(md5(bytes));
	            }
	        } catch (IOException e) {
				return StringUtils.EMPTY;
	        }
		}
		return StringUtils.EMPTY;
	}
	
}
