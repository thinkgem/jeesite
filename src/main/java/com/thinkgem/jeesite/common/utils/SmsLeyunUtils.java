package com.thinkgem.jeesite.common.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 乐云短信
 */
public class SmsLeyunUtils {

	private final static Logger logger = LoggerFactory.getLogger(SmsLeyunUtils.class);
	private final static String url = "http://lehuo520.cn/a/sms/api"; //账号的平台url地址
	private final static String data = "username=jeesite&password=jeesite.com"; //账号的平台账号密码
	
	public static void main(String[] args) {
		String phone = "18500000000"; //收短信人手机号码；例如:18500000000  支持多号码，号码之间用英文逗号隔开，最多100个
		String content = "【JeeSite】您好，您的验证码是：123456（请勿透露给其他人）感谢您的使用。"; //输入需要发送内容；例如：你好这是一条测试短信
		String smsid = ""; //短信id，查询短信状态报告时需要，可为空
		String sendTime = ""; //发送时间，为空立即发送
		System.out.println(send(content, phone, sendTime)); //发短信
		System.out.println(status(smsid, phone)); //取状态	
		System.out.println(reply()); //取上行	回复短信
	}

	/**
	 * 发送短信
	 * @param content,phone,sendtime
	 * @return {"result":"0","describing"："提交成功","sms":[{"phone":"18073110001,18073110002","smsid":"83bd18f1d48b4cc9b9fe7810c768ac43","status":"3"}]}
	 */
	public static String send(String content, String phone, String sendTime) {
		String res = "";
		try {
			String param = data + "&phone=" + phone + "&content=" + URLEncoder.encode(content, "UTF-8")
					+ "&sendTime=" + sendTime;
			res = connectURL(url + "/send", param);
		} catch (Exception ex) {
			logger.error(ex.getMessage(), ex);
		}
		return res;

	}

	/**
	 * 获取状态
	 * @param smsid,phone可为空,为空取最近两天未获取状态报告,沦询间隔时间不能低于5分钟
	 * @return 请求错误返回页面示例： {"result":"-1","describing"："帐号不存在，请检查用户名或者密码是否正确","sms":[]} 请求成功返回页面示例：
	 *         {"result":"0","describing"："提交成功","sms":[{"phone":"18073110001","smsid":"83bd18f1d48b4cc9b9fe7810c768ac43","status":"7"},{"phone":"18073110001","smsid":"83bd18f1d48b4cc9b9fe7810c768ac43","status":"8"}]}
	 */
	public static String status(String smsid, String phone) {
		String res = "";
		try {
			String param = data + "&smsid=" + smsid;
			res = connectURL(url + "/status", param);
		} catch (Exception ex) {
			logger.error(ex.getMessage(), ex);
		}
		return res;

	}

	/**
	 * 获取回复
	 * @param smsid:下发短信对应短信ID，taskId同一批任务ID
	 * @return {"result":"0","sms":[{"phone":"18073110001","neirong":"已收到","taskId":"83bd18f1d48b4cc9b9fe7810c768ac43"},"smsId":"83bd18f1d48b48j9b9fe7810c768ac43"}]}
	 */
	public static String reply() {
		String res = "";
		try {
			String param = data;
			res = connectURL(url + "/query", param);
		} catch (Exception ex) {
			logger.error(ex.getMessage(), ex);
		}
		return res;
	}

	/**
	 * 进行http提交
	 * @param
	 * @return
	 * @throws IOException 
	 * @throws Exception 
	 */
	private static String connectURL(String url, String param) throws IOException {
		String res = "";
		long startt = System.currentTimeMillis();
		HttpURLConnection urlConn = null;
		URL url1 = new URL(url);
		urlConn = (HttpURLConnection) url1.openConnection();
		urlConn.setRequestMethod("POST");
		urlConn.setDoOutput(true);
		OutputStream out = null;
		BufferedReader rd = null;
		try{
			out = urlConn.getOutputStream();
			out.write(param.getBytes("UTF-8"));
			out.flush();
			rd = new BufferedReader(new InputStreamReader(urlConn.getInputStream(), "UTF-8"));
			StringBuffer sb = new StringBuffer();
			int ch;
			while ((ch = rd.read()) > -1) {
				sb.append((char) ch);
			}
			res = sb.toString().trim();
		}finally {
			if (out!=null){
				out.close();
			}
			if (rd!=null){
				rd.close();
			}
		}
		long end = System.currentTimeMillis();
		logger.debug("耗时：" + (end - startt));
		return res;
	}

}
