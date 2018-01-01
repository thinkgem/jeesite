/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.common.lang;

import java.util.Arrays;
import java.util.Date;

import org.apache.commons.lang3.time.DateFormatUtils;

/**
 * 时间计算工具类
 * @author ThinkGem
 * @version 2015-6-20
 */
public class TimeUtils {
	
	/**
	 * 将时间转换为字符串（xx天，xx时，xx分，xx秒，大于360天显示日期时间）
	 */
	public static String formatDateAgo(long dateTime) {
		StringBuilder sb = new StringBuilder();
		if (dateTime < 1000){
			sb.append(dateTime).append("毫秒");
		}else{
			TimeUtils t = new TimeUtils(dateTime);
			int day = t.get(TimeUtils.DAY);
			int hour = t.get(TimeUtils.HOUR);
			int minute = t.get(TimeUtils.MINUTE);
			int second = t.get(TimeUtils.SECOND);
			if (day > 365){
				return DateUtils.formatDate(new Date(dateTime), "yyyy年MM月dd日 HH时mm分ss秒");
			}
			if (day > 0){
				sb.append(day).append("天");
			}
			if (hour > 0){
				sb.append(hour).append("时");
			}
			if (minute > 0){
				sb.append(minute).append("分");
			}
			if (second > 0){
				sb.append(second).append("秒");
			}
		}
		return sb.toString();
	}

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
		String interval = null;
		// 得出的时间间隔是毫秒
		long time = System.currentTimeMillis() - dateTime.getTime();
		// 如果时间间隔小于10秒则显示“刚刚”time/10得出的时间间隔的单位是秒
		if (time / 1000 < 10 && time / 1000 >= 0) {
			interval = "刚刚";
		}
		// 如果时间间隔大于24小时则显示多少天前
		else if (time / 3600000 < 24*4 && time / 3600000 >= 24) {
			int d = (int) (time / (3600000*24));// 得出的时间间隔的单位是天
			interval = d + "天前";
		}
		// 如果时间间隔小于24小时则显示多少小时前
		else if (time / 3600000 < 24 && time / 3600000 >= 1) {
			int h = (int) (time / 3600000);// 得出的时间间隔的单位是小时
			interval = h + "小时前";
		}
		// 如果时间间隔小于60分钟则显示多少分钟前
		else if (time / 60000 < 60 && time / 60000 >=1) {
			int m = (int) ((time % 3600000) / 60000);// 得出的时间间隔的单位是分钟
			interval = m + "分钟前";
		}
		// 如果时间间隔小于60秒则显示多少秒前
		else if (time / 1000 < 60 && time / 1000 >=10) {
			int se = (int) ((time % 60000) / 1000);
			interval = se + "秒前";
		}
		// 大于3天的，则显示正常的时间，但是不显示秒
		else {
			interval = DateUtils.formatDate(dateTime,"yyyy-MM-dd");
		}
		return interval;
	}
	
    /**
     * 时间字段常量，表示“秒”
     */
    public final static int SECOND = 0;

    /**
     * 时间字段常量，表示“分”
     */
    public final static int MINUTE = 1;

    /**
     * 时间字段常量，表示“时”
     */
    public final static int HOUR = 2;

    /**
     * 时间字段常量，表示“天”
     */
    public final static int DAY = 3;

    /**
     * 各常量允许的最大值
     */
    private final int[] maxFields = { 59, 59, 23, Integer.MAX_VALUE - 1 };

    /**
     * 各常量允许的最小值
     */
    private final int[] minFields = { 0, 0, 0, Integer.MIN_VALUE };

    /**
     * 默认的字符串格式时间分隔符
     */
    private String timeSeparator = ":";

    /**
     * 时间数据容器
     */
    private int[] fields = new int[4];

    /**
     * 无参构造，将各字段置为 0
     */
    public TimeUtils() {
        this(0, 0, 0, 0);
    }

    /**
     * 使用时、分构造一个时间
     * @param hour      小时
     * @param minute    分钟
     */
    public TimeUtils(int hour, int minute) {
        this(0, hour, minute, 0);
    }

    /**
     * 使用时、分、秒构造一个时间
     * @param hour      小时
     * @param minute    分钟
     * @param second    秒
     */
    public TimeUtils(int hour, int minute, int second) {
        this(0, hour, minute, second);
    }

    /**
     * 使用一个字符串构造时间<br>
     * Time time = new Time("14:22:23");
     * @param time      字符串格式的时间，默认采用“:”作为分隔符
     */
    public TimeUtils(String time) {
        this(time, null);
//    	System.out.println(time);
    }

    /**
     * 使用时间毫秒构建时间
     * @param time
     */
    public TimeUtils(long time){
    	this(new Date(time));
    }
    
    /**
     * 使用日期对象构造时间
     * @param date
     */
    public TimeUtils(Date date){
    	this(DateFormatUtils.formatUTC(date, "HH:mm:ss"));
    }

    /**
     * 使用天、时、分、秒构造时间，进行全字符的构造
     * @param day       天
     * @param hour      时
     * @param minute    分
     * @param second    秒
     */
    public TimeUtils(int day, int hour, int minute, int second) {
        initialize(day, hour, minute, second);
    }

    /**
     * 使用一个字符串构造时间，指定分隔符<br>
     * Time time = new Time("14-22-23", "-");
     * @param time      字符串格式的时间
     */
    public TimeUtils(String time, String timeSeparator) {
        if(timeSeparator != null) {
            setTimeSeparator(timeSeparator);
        }
        parseTime(time);
    }

    /**
     * 设置时间字段的值
     * @param field     时间字段常量
     * @param value     时间字段的值
     */
    public void set(int field, int value) {
        if(value < minFields[field]) {
            throw new IllegalArgumentException(value + ", time value must be positive.");
        }
        fields[field] = value % (maxFields[field] + 1);
        // 进行进位计算
        int carry = value / (maxFields[field] + 1);
        if(carry > 0) {
            int upFieldValue = get(field + 1);
            set(field + 1, upFieldValue + carry);
        }
    }

    /**
     * 获得时间字段的值
     * @param field     时间字段常量
     * @return          该时间字段的值
     */
    public int get(int field) {
        if(field < 0 || field > fields.length - 1) {
            throw new IllegalArgumentException(field + ", field value is error.");
        }
        return fields[field];
    }

    /**
     * 将时间进行“加”运算，即加上一个时间
     * @param time      需要加的时间
     * @return          运算后的时间
     */
    public TimeUtils addTime(TimeUtils time) {
    	TimeUtils result = new TimeUtils();
        int up = 0;     // 进位标志
        for (int i = 0; i < fields.length; i++) {
            int sum = fields[i] + time.fields[i] + up;
            up = sum / (maxFields[i] + 1);
            result.fields[i] = sum % (maxFields[i] + 1);
        }
        return result;
    }

    /**
     * 将时间进行“减”运算，即减去一个时间
     * @param time      需要减的时间
     * @return          运算后的时间
     */
    public TimeUtils subtractTime(TimeUtils time) {
    	TimeUtils result = new TimeUtils();
        int down = 0;       // 退位标志
        for (int i = 0, k = fields.length - 1; i < k; i++) {
            int difference = fields[i] + down;
            if (difference >= time.fields[i]) {
                difference -= time.fields[i];
                down = 0;
            } else {
                difference += maxFields[i] + 1 - time.fields[i];
                down = -1;
            }
            result.fields[i] = difference;
        }
        result.fields[DAY] = fields[DAY] - time.fields[DAY] + down;
        return result;
    }

    /**
     * 获得时间字段的分隔符
     * @return
     */
    public String getTimeSeparator() {
        return timeSeparator;
    }

    /**
     * 设置时间字段的分隔符（用于字符串格式的时间）
     * @param timeSeparator     分隔符字符串
     */
    public void setTimeSeparator(String timeSeparator) {
        this.timeSeparator = timeSeparator;
    }

    private void initialize(int day, int hour, int minute, int second) {
        set(DAY, day);
        set(HOUR, hour);
        set(MINUTE, minute);
        set(SECOND, second);
    }

    private void parseTime(String time) {
        if(time == null) {
            initialize(0, 0, 0, 0);
            return;
        }
        String t = time;
        int field = DAY;
        set(field--, 0);
        int p = -1;
        while((p = t.indexOf(timeSeparator)) > -1) {
            parseTimeField(time, t.substring(0, p), field--);
            t = t.substring(p + timeSeparator.length());
        }
        parseTimeField(time, t, field--);
    }

    private void parseTimeField(String time, String t, int field) {
        if(field < SECOND || t.length() < 1) {
            parseTimeException(time);
        }
        char[] chs = t.toCharArray();
        int n = 0;
        for(int i = 0; i < chs.length; i++) {
            if(chs[i] <= ' ') {
                continue;
            }
            if(chs[i] >= '0' && chs[i] <= '9') {
                n = n * 10 + chs[i] - '0';
                continue;
            }
            parseTimeException(time);
        }
        set(field, n);
    }

    private void parseTimeException(String time) {
        throw new IllegalArgumentException(time + ", time format error, HH"
                + this.timeSeparator + "mm" + this.timeSeparator + "ss");
    }

    @Override
	public String toString() {
        StringBuilder sb = new StringBuilder(16);
        sb.append(fields[DAY]).append(',').append(' ');
        buildString(sb, HOUR).append(timeSeparator);
        buildString(sb, MINUTE).append(timeSeparator);
        buildString(sb, SECOND);
        return sb.toString();
    }

    private StringBuilder buildString(StringBuilder sb, int field) {
        if(fields[field] < 10) {
            sb.append('0');
        }
        return sb.append(fields[field]);
    }

    @Override
	public int hashCode() {
        final int PRIME = 31;
        int result = 1;
        result = PRIME * result + Arrays.hashCode(fields);
        return result;
    }

    @Override
	public boolean equals(Object obj) {
        if (this == obj) {
			return true;
		}
        if (obj == null) {
			return false;
		}
        if (getClass() != obj.getClass()) {
			return false;
		}
        final TimeUtils other = (TimeUtils) obj;
        if (!Arrays.equals(fields, other.fields)) {
            return false;
        }
        return true;
    }
    
}