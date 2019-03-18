/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.modules.msg.send;

import java.util.Date;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.jeesite.common.lang.ExceptionUtils;
import com.jeesite.common.lang.ObjectUtils;
import com.jeesite.common.mapper.JsonMapper;
import com.jeesite.common.msg.SmsUtils;
import com.jeesite.common.service.BaseService;
import com.jeesite.modules.msg.entity.MsgPush;
import com.jeesite.modules.msg.entity.content.SmsMsgContent;
import com.jeesite.modules.msg.send.MsgSendService;

/**
 * 短信发送服务实现
 * @author ThinkGem
 * @version 2018年5月13日
 */
@Service
public class SmsSendService extends BaseService implements MsgSendService{

	@Override
	public void sendMessage(MsgPush msgPush) {
		try{
//			String url = Global.getProperty("msg.sms.url");
//			String data = Global.getProperty("msg.sms.data");
//			String prefix = Global.getProperty("msg.sms.prefix", "");
//			String suffix = Global.getProperty("msg.sms.suffix", "");
//			Connection conn = Jsoup.connect(url);
//			conn.postDataCharset("UTF-8");
//			conn.method(Method.POST);
//			for (String param : StringUtils.split(data, "&")){
//				String[] ss = StringUtils.split(param, "=");
//				if (ss.length == 1){
//					conn.data(ss[0], "");
//				}else if (ss.length == 2){
//					conn.data(ss[0], ss[1]);
//				}
//			}
//			// 手机号码
//			conn.data("mobile", msgPush.getReceiveCode());	
//			// 短信内容
//			SmsMsgContent content = msgPush.parseMsgContent(SmsMsgContent.class);
//			conn.data("content", prefix + content.getContent() + suffix);
//			// 发送短信
//			String result = conn.execute().body();
//			String result = "{result:0,message:\"ok\"}"; // 模拟发送结果
//			Map<String, Object> map = JsonMapper.fromJson(result, Map.class);
			
			// 发送短信
			SmsMsgContent content = msgPush.parseMsgContent(SmsMsgContent.class);
			String result = SmsUtils.send(content.getContent(), msgPush.getReceiveCode());
			Map<String, Object> map = JsonMapper.fromJson(result, Map.class);
			
			// 发送成功
			if (ObjectUtils.toInteger(map.get("result")) == 0){
				msgPush.setPushStatus(MsgPush.PUSH_STATUS_SUCCESS);
				msgPush.addPushReturnContent(result);
			}
			// 发送失败
			else{
				throw new RuntimeException(result);
			}
			
		} catch (Exception ex) {
			logger.error("发送短信失败！ ", ex);
			msgPush.setPushDate(new Date());
			msgPush.setPushStatus(MsgPush.PUSH_STATUS_FAIL);
			msgPush.addPushReturnContent(ExceptionUtils.getStackTraceAsString(ex));
		}
	}

}
