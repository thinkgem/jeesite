package com.thinkgem.jeesite.common.utils;

//import java.util.List;
//
//import com.baidu.yun.channel.auth.ChannelKeyPair;
//import com.baidu.yun.channel.client.BaiduChannelClient;
//import com.baidu.yun.channel.exception.ChannelClientException;
//import com.baidu.yun.channel.exception.ChannelServerException;
//import com.baidu.yun.channel.model.PushBroadcastMessageRequest;
//import com.baidu.yun.channel.model.PushUnicastMessageRequest;
//import com.baidu.yun.core.log.YunLogEvent;
//import com.baidu.yun.core.log.YunLogHandler;
//import com.thinkgem.jeesite.modules.sys.entity.User;
//import com.thinkgem.jeesite.modules.sys.utils.UserUtils;

public class MobileSendMessage {
	
	public static final String apiKey = "oc0yx4ST3dNQcGbXNQUNE2yn";
	public static final String secretKey = "KpRboB9HiE6wm7I9IHtcMLnNGZY0S27W";
	
//	/**
//	 * 无限制推送广播
//	 * @param title
//	 * @param content
//	 */
//	public static void pushBroadcastMessage(String title,String content) {
//		
//	        ChannelKeyPair pair = new ChannelKeyPair(MobileSendMessage.apiKey, MobileSendMessage.secretKey);
//
//	        // 2. 创建BaiduChannelClient对象实例
//	        BaiduChannelClient channelClient = new BaiduChannelClient(pair);
//	
//	        // 3. 若要了解交互细节，请注册YunLogHandler类
//	        channelClient.setChannelLogHandler(new YunLogHandler() {
//	            @Override
//	            public void onHandle(YunLogEvent event) {
//	                System.out.println(event.getMessage());
//	            }
//	        });
//	        // 4. 创建请求类对象
//            PushBroadcastMessageRequest request = new PushBroadcastMessageRequest();
//            request.setDeviceType(3); // device_type => 1: web 2: pc 3:android   // 4:ios 5:wp
//
//            // request.setMessage("Hello Channel");
//            // 若要通知，
//            request.setMessageType(1);
//            request.setMessage("{\"title\":\""+title+"\",\"description\":\""+content+"\"}");
//            //request.setMessage(notify.toString());
//
//            // 5. 调用pushMessage接口
//            try {
//				 channelClient.pushBroadcastMessage(request);
//			      // 6. 认证推送成功
////	            System.out.println("push amount : " + response.getSuccessAmount());
//            } catch (ChannelClientException e) {
//				e.printStackTrace();
//			} catch (ChannelServerException e) {
//				e.printStackTrace();
//			}
//	
//	}
//	
//	
//	/**
//	 * 多人员手机提醒信息发送
//	 * @param title
//	 * @param content
//	 * @param users
//	 */
//	public static void pushUnicastMessageMessage(String title,String content,List<String> userIds) {
//		
//		ChannelKeyPair pair = new ChannelKeyPair(MobileSendMessage.apiKey, MobileSendMessage.secretKey);
//		
//		// 2. 创建BaiduChannelClient对象实例
//		BaiduChannelClient channelClient = new BaiduChannelClient(pair);
//		
//		// 3. 若要了解交互细节，请注册YunLogHandler类
//		channelClient.setChannelLogHandler(new YunLogHandler() {
//			@Override
//			public void onHandle(YunLogEvent event) {
//				System.out.println(event.getMessage());
//			}
//		});
//		// 4. 创建请求类对象
//		PushUnicastMessageRequest request = new PushUnicastMessageRequest();
//		request.setDeviceType(3); // device_type => 1: web 2: pc 3:android   // 4:ios 5:wp
//		
//		// request.setMessage("Hello Channel");
//		// 若要通知，
//		request.setMessageType(1);
//		request.setMessage("{\"title\":\""+title+"\",\"description\":\""+content+"\"}");
//		
//		// 5. 调用pushMessage接口
//		try {
//			for (String userId : userIds) {
//				User sendUser=UserUtils.get(userId);
//				if(sendUser!=null){
//					request.setChannelId(Long.valueOf(sendUser.getMobileChannelId()));
//					request.setUserId(sendUser.getMobileUserId());
//					channelClient.pushUnicastMessage(request);
//				}
//			}
//			
//			// 6. 认证推送成功
////	            System.out.println("push amount : " + response.getSuccessAmount());
//		} catch (ChannelClientException e) {
//			e.printStackTrace();
//		} catch (ChannelServerException e) {
//			e.printStackTrace();
//		}
//		
//	}
}
