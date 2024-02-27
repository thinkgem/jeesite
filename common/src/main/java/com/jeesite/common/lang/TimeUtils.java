/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 * No deletion without permission, or be held responsible to law.
 */
package com.jeesite.common.lang;

import java.util.Date;

/**
 * 时间计算工具类
 * @author ThinkGem
 * @version 2015-6-20
 */
public class TimeUtils {
	
	public static final String[] CN = new String[] {"毫秒", "秒", "分", "时", "天"};
	public static final String[] EN = new String[] {"ms", " second ", " minute ", " hour ", " day "};
	
	public static final String[] AGO_CN = new String[] {"刚刚", "秒前", "分钟前", "小时前", "天前"};
	public static final String[] AGO_EN = new String[] {"just now", " seconds ago", " minutes ago", " hours ago", " days ago"};

	/**
	 * 将毫秒数转换为：xx天，xx时，xx分，xx秒
	 */
	public static String formatTime(long millisecond) {
		return formatTime(millisecond, CN);
	}
	
	/**
	 * 将毫秒数转换为：xx天，xx时，xx分，xx秒
	 */
	public static String formatTime(long millisecond, String[] lang) {
		long ms = millisecond;
		int ss = 1000;
		int mi = ss * 60;
		int hh = mi * 60;
		int dd = hh * 24;
		long day = ms / dd;
		long hour = (ms - day * dd) / hh;
		long minute = (ms - day * dd - hour * hh) / mi;
		long second = (ms - day * dd - hour * hh - minute * mi) / ss;
		StringBuilder sb = new StringBuilder();
		if (ms >= 0 && ms < 1000) {
			sb.append(ms).append(lang[0]);
		} else {
			if (day > 0) {
				ms -= day * dd;
				sb.append(day).append(lang[4]);
			}
			if (day > 0 || hour > 0) {
				ms -= hour * hh;
				sb.append(hour).append(lang[3]);
			}
			if (day > 0 || hour > 0 || minute > 0) {
				ms -= minute * mi;
				sb.append(minute).append(lang[2]);
			}
			if (hour > 0 || minute > 0 || second > 0) {
				ms -= second * ss;
				sb.append(second).append(".").append(ms).append(lang[1]);
			}
		}
		return sb.toString();
	}

//	/**
//	 * 将毫秒数转换为：xx天，xx时，xx分，xx秒（v5.1 替换为 formatTime）
//	 */
//	@Deprecated
//	public static String formatDateAgo(long millisecond) {
//		return formatTime(millisecond, CN);
//	}

	/**
	 * 将过去的时间转为为，刚刚，xx秒，xx分钟，xx小时前、xx天前，大于3天的显示日期
	 */
	public static String formatTimeAgo(String dateTime) {
		return formatTimeAgo(DateUtils.parseDate(dateTime));
	}

	/**
	 * 将过去的时间转为为，刚刚，xx秒，xx分钟，xx小时前、xx天前，大于3天的显示日期
	 */
	public static String formatTimeAgo(Date dateTime) {
		return formatTimeAgo(dateTime, AGO_CN);
	}

	/**
	 * 将过去的时间转为为，刚刚，xx秒，xx分钟，xx小时前、xx天前，大于3天的显示日期
	 */
	public static String formatTimeAgo(Date dateTime, String[] lang) {
		String interval = null;
		// 得出的时间间隔是毫秒
		long time = System.currentTimeMillis() - dateTime.getTime();
		// 如果时间间隔小于10秒则显示“刚刚”time/10得出的时间间隔的单位是秒
		if (time / 1000 < 10 && time / 1000 >= 0) {
			interval = lang[0];
		}
		// 如果时间间隔大于24小时则显示多少天前
		else if (time / 3600000 < 24 * 4 && time / 3600000 >= 24) {
			int d = (int) (time / (3600000 * 24));// 得出的时间间隔的单位是天
			interval = d +  lang[4];
		}
		// 如果时间间隔小于24小时则显示多少小时前
		else if (time / 3600000 < 24 && time / 3600000 >= 1) {
			int h = (int) (time / 3600000);// 得出的时间间隔的单位是小时
			interval = h +  lang[3];
		}
		// 如果时间间隔小于60分钟则显示多少分钟前
		else if (time / 60000 < 60 && time / 60000 >= 1) {
			int m = (int) ((time % 3600000) / 60000);// 得出的时间间隔的单位是分钟
			interval = m +  lang[2];
		}
		// 如果时间间隔小于60秒则显示多少秒前
		else if (time / 1000 < 60 && time / 1000 >= 10) {
			int se = (int) ((time % 60000) / 1000);
			interval = se +  lang[1];
		}
		// 大于3天的，则显示正常的时间，但是不显示秒
		else {
			interval = DateUtils.formatDate(dateTime, "yyyy-MM-dd");
		}
		return interval;
	}

}