package com.jeesite.common.msg;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 发送短信（请实现send方法）
 */
public class SmsUtils {

	private static final Logger logger = LoggerFactory.getLogger(SmsUtils.class);
	
	/**
	 * 模拟发送短信
	 * @param content 短信内容
	 * @param mobile 接受手机号码
	 */
	public static String send(String content, String mobile) {
//		PropertiesUtils props = PropertiesUtils.getInstance();
//		String url = props.getProperty("msg.sms.url");
//		String data = props.getProperty("msg.sms.data");
//		String prefix = props.getProperty("msg.sms.prefix", "");
//		String suffix = props.getProperty("msg.sms.suffix", "");
//		Map<String, String> dataMap = MapUtils.newHashMap();
//		for (String param : StringUtils.split(data, "&")){
//			String[] ss = StringUtils.split(param, "=");
//			if (ss.length == 1){
//				dataMap.put(ss[0], "");
//			}else if (ss.length == 2){
//				dataMap.put(ss[0], ss[1]);
//			}
//		}
//		// 手机号码
//		dataMap.put("mobile", mobile);
//		// 短信内容
//		dataMap.put("content", prefix + content + suffix);
//		HttpClientUtils.post(url, dataMap, EncodeUtils.UTF_8);
		logger.debug("短信内容：" + content + "    手机号码：" + mobile);
		logger.warn("短信模拟发送成功！实际并未发送到手机，请实现 " + SmsUtils.class + " 的 send 方法。");
		return "{result:0,message:\"短信模拟发送成功！\"}";
	}
	
}
