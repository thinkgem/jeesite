package com.jeesite.test.lang;

import com.jeesite.common.lang.WorkDayUtils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * 工作日计算工具类
 * @author ThinkGem
 * @version 2023-6-9
 */
public class WorkDayUtilsTest {

	public static void main(String[] args) {
		try {
			String strDateStart = "2023-08-01";
			String strDateEnd = "2024-08-31";

			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			Date date_start = sdf.parse(strDateStart);
			Date date_end = sdf.parse(strDateEnd);
			WorkDayUtils app = new WorkDayUtils();
			Calendar cal_start = Calendar.getInstance();
			Calendar cal_end = Calendar.getInstance();
			cal_start.setTime(date_start);
			cal_end.setTime(date_end);
			System.out.println("开始日：" + cal_start.get(Calendar.YEAR) + "-" + (cal_start.get(Calendar.MONTH) + 1)
					+ "-" + cal_start.get(Calendar.DAY_OF_MONTH) + " " + app.getChineseWeek(cal_start));
			System.out.println("结束日：" + cal_end.get(Calendar.YEAR) + "-" + (cal_end.get(Calendar.MONTH) + 1)
					+ "-" + cal_end.get(Calendar.DAY_OF_MONTH) + " " + app.getChineseWeek(cal_end));
			System.out.println("工作日：" + app.getWorkingDay(cal_start, cal_end));
			System.out.println("休息日：" + app.getHolidays(cal_start, cal_end));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}
