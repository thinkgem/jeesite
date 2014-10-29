package com.thinkgem.jeesite.common.workflow;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.beanutils.Converter;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.apache.log4j.Logger;

public class DateConverter implements Converter {

	private static final Logger logger = Logger.getLogger(DateConverter.class);

	private static final String DATETIME_PATTERN = "yyyy-MM-dd HH:mm:ss";

	private static final String DATETIME_PATTERN_NO_SECOND = "yyyy-MM-dd HH:mm";

	private static final String DATE_PATTERN = "yyyy-MM-dd";

	private static final String MONTH_PATTERN = "yyyy-MM";

	@SuppressWarnings("rawtypes")
	public Object convert(Class type, Object value) {
		Object result = null;
		if (type == Date.class) {
			try {
				result = doConvertToDate(value);
			} catch (ParseException e) {
				e.printStackTrace();
			}
		} else if (type == String.class) {
			result = doConvertToString(value);
		}
		return result;
	}

	/**
	 * Convert String to Date
	 *
	 * @param value
	 * @return
	 * @throws ParseException 
	 */
	private Date doConvertToDate(Object value) throws ParseException {
		Date result = null;

		if (value instanceof String) {
			result = DateUtils.parseDate((String) value, new String[] { DATE_PATTERN, DATETIME_PATTERN,
					DATETIME_PATTERN_NO_SECOND, MONTH_PATTERN });

			// all patterns failed, try a milliseconds constructor
			if (result == null && StringUtils.isNotEmpty((String) value)) {

				try {
					result = new Date(new Long((String) value).longValue());
				} catch (Exception e) {
					logger.error("Converting from milliseconds to Date fails!");
					e.printStackTrace();
				}

			}

		} else if (value instanceof Object[]) {
			// let's try to convert the first element only
			Object[] array = (Object[]) value;

			if ((array != null) && (array.length >= 1)) {
				value = array[0];
				result = doConvertToDate(value);
			}

		} else if (Date.class.isAssignableFrom(value.getClass())) {
			result = (Date) value;
		}
		return result;
	}

	/**
	 * Convert Date to String
	 *
	 * @param value
	 * @return
	 */
	private String doConvertToString(Object value) {
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(DATETIME_PATTERN);
		String result = null;
		if (value instanceof Date) {
			result = simpleDateFormat.format(value);
		}
		return result;
	}

}