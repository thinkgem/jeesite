/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 * No deletion without permission, or be held responsible to law.
 */
package com.jeesite.test.lang;

import com.jeesite.common.lang.DateUtils;

import java.text.ParseException;
import java.util.Date;

/**
 * 日期工具测试类
 * @author ThinkGem
 * @version 2023-6-9
 */
public class DateUtilsTest {

	public static void main(String[] args) throws ParseException {
		System.out.println(DateUtils.formatDate(DateUtils.parseDate("2023/3/6")));
		System.out.println(DateUtils.formatDateTime(DateUtils.parseDate("2023-3-6 12:30:15")));
		System.out.println(DateUtils.getDate("yyyy年MM月dd日 E"));
		long time = new Date().getTime()-DateUtils.parseDate("2023-11-19").getTime();
		System.out.println(time/(24*60*60*1000));
		System.out.println(DateUtils.getWeekOfYear(new Date()));
		System.out.println(DateUtils.formatDate(DateUtils.getOfDayFirst(DateUtils.parseDate("2023/3/6")),"yyyy-MM-dd HH:mm:ss.sss"));
		System.out.println(DateUtils.formatDate(DateUtils.getOfDayLast(DateUtils.parseDate("2023/6/6")),"yyyy-MM-dd HH:mm:ss.sss"));
	}
	
}
