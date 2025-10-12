/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 * No deletion without permission, or be held responsible to law.
 */
package com.jeesite.modules.msg.service.support;

import com.jeesite.common.collect.ListUtils;
import com.jeesite.common.config.Global;
import com.jeesite.common.entity.Page;
import com.jeesite.common.lang.StringUtils;
import com.jeesite.common.service.CrudService;
import com.jeesite.common.web.http.ServletUtils;
import com.jeesite.modules.file.utils.FileUploadUtils;
import com.jeesite.modules.msg.dao.MsgInnerDao;
import com.jeesite.modules.msg.dao.MsgInnerRecordDao;
import com.jeesite.modules.msg.entity.MsgInner;
import com.jeesite.modules.msg.entity.MsgInnerRecord;
import com.jeesite.modules.msg.entity.MsgPush;
import com.jeesite.modules.msg.entity.content.*;
import com.jeesite.modules.msg.service.MsgInnerService;
import com.jeesite.modules.msg.utils.MsgPushUtils;
import com.jeesite.modules.sys.entity.EmpUser;
import com.jeesite.modules.sys.entity.User;
import com.jeesite.modules.sys.service.EmpUserService;
import io.netty.util.concurrent.DefaultThreadFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 内部消息Service
 * @author ThinkGem
 * @version 2019-03-12
 */
public class MsgInnerServiceSupport extends CrudService<MsgInnerDao, MsgInner>
		implements MsgInnerService {
	
	@Autowired
	private EmpUserService empUserService;
	@Autowired
	private MsgInnerRecordDao msgInnerRecordDao;

	private static ExecutorService msgPushThreadPool = new ThreadPoolExecutor(5, 20,
			60L, TimeUnit.SECONDS, new LinkedBlockingQueue<>(),
			new DefaultThreadFactory("cms-update-expired-weight"));
	
	/**
	 * 获取单条数据
	 * @param msgInner 主键
	 */
	@Override
	public MsgInner get(MsgInner msgInner) {
		return super.get(msgInner);
	}
	
	/**
	 * 查询分页数据
	 * @param msgInner 查询条件； page 分页对象
	 */
	@Override
	public Page<MsgInner> findPage(MsgInner msgInner) {
		return super.findPage(msgInner);
	}

	/**
	 * 查询消息记录数据
	 */
	public List<MsgInnerRecord> findRecordList(MsgInnerRecord msgInnerRecord){
		if (StringUtils.isBlank(msgInnerRecord.getMsgInnerId())) {
			return ListUtils.newArrayList();
		}
		return msgInnerRecordDao.findList(msgInnerRecord);
	}
	
	/**
	 * 保存数据（插入或更新）
	 * @param msgInner 数据对象
	 */
	@Override
	@Transactional
	public void save(MsgInner msgInner) {
		if (msgInner.getIsNewRecord()){
			User user = msgInner.currentUser();
			msgInner.setSendUserCode(user.getUserCode());
			msgInner.setSendUserName(user.getUserName());
			// 没有设置状态，则默认新增后是草稿状态
			if (StringUtils.isBlank(msgInner.getStatus())){
				msgInner.setStatus(MsgInner.STATUS_DRAFT);
			}
		}
		msgInner.setSendDate(new Date());
		msgInner.setIsAttac(msgInner.getDataMap() != null && StringUtils.isNotBlank((String)msgInner.getDataMap().get("msgInner_file"))
				|| StringUtils.isNotBlank(ServletUtils.getParameter("msgInner_file")) ? Global.YES : Global.NO);
		super.save(msgInner);
		// 保存上传附件
		FileUploadUtils.saveFileUpload(msgInner, msgInner.getId(), "msgInner_file");
		// 发送内部消息
		if (MsgInner.STATUS_NORMAL.equals(msgInner.getStatus())){
			this.updateStatus(msgInner); // 更新状态
			List<EmpUser> empUserList = null;
			if (MsgInner.RECEIVE_TYPE_ALL.equals(msgInner.getReceiveType())){
				EmpUser empUser = new EmpUser();
				empUser.setCodes(new String[]{});
				empUserList = empUserService.findUserList(empUser);
			}else{
				String[] codes = StringUtils.splitComma(msgInner.getReceiveCodes());
				String[] names = StringUtils.splitComma(msgInner.getReceiveNames());
				if (codes != null && names != null && codes.length > 0 && codes.length == names.length){
					EmpUser empUser = new EmpUser();
					empUser.setCodes(codes);
					switch(msgInner.getReceiveType()){
					case MsgInner.RECEIVE_TYPE_USER:
						empUserList = ListUtils.newArrayList();
						for (int i=0; i<codes.length; i++){
							EmpUser e = new EmpUser();
							e.setUserCode(codes[i]);
							e.setUserName(names[i]);
							empUserList.add(e);
						}
						break;
					case MsgInner.RECEIVE_TYPE_OFFICE:
						empUserList = empUserService.findUserListByOfficeCodes(empUser);
						break;
					case MsgInner.RECEIVE_TYPE_ROLE:
						empUserList = empUserService.findUserListByRoleCodes(empUser);
						break;
					case MsgInner.RECEIVE_TYPE_POST:
						empUserList = empUserService.findUserListByPostCodes(empUser);
						break;
					}
				}
			}
			this.saveMsgInnerRecord(msgInner, empUserList);
		}
	}
	
	/**
	 * 保存消息推送记录
	 */
	private void saveMsgInnerRecord(MsgInner msgInner, List<EmpUser> empUserList){
		if (empUserList == null || empUserList.size() <= 0){
			return;
		}
		String[] notifyTypes = StringUtils.splitComma(msgInner.getNotifyTypes());
		List<MsgInnerRecord> recordList = ListUtils.newArrayList();
		empUserList.forEach(user -> {
			MsgInnerRecord r = new MsgInnerRecord();
			r.setMsgInnerId(msgInner.getId());
			r.setReceiveUserCode(user.getUserCode());
			r.setReceiveUserName(user.getUserName());
			r.setReadStatus(MsgInnerRecord.READ_STATUS_UNREAD);
			recordList.add(r);
			// 发送消息通知（消息推送）
			if (notifyTypes != null){
				for (String type : notifyTypes){
					BaseMsgContent msgContent = null;
					if (MsgPush.TYPE_PC.equals(type)){
						msgContent = new PcMsgContent();
						msgContent.setContent(text("你有一条内部消息，点击“详情”进行查阅。"));
						((PcMsgContent)msgContent).addButton(new String[]{text("详情"), 
								Global.getAdminPath()+"/msg/msgInner/view?id="+msgInner.getId()});
					}else if (MsgPush.TYPE_APP.equals(type)){
						msgContent = new AppMsgContent();
					}else if (MsgPush.TYPE_SMS.equals(type)){
						msgContent = new SmsMsgContent();
					}else if (MsgPush.TYPE_EMAIL.equals(type)){
						msgContent = new EmailMsgContent();
					}
					if (msgContent != null){
						msgContent.setTitle(msgInner.getMsgTitle());
						if (msgContent.getContent() == null){
							msgContent.setContent(text("你有一条内部消息请查阅：")
									+ StringUtils.abbr(msgInner.getMsgTitle(), 30));
						}
						msgContent.setMsgPush(new MsgPush());
						msgContent.getMsgPush().setIsRealtimePush(false); // 关闭实时推送，改为手动推送
						MsgPushUtils.push(msgContent, msgInner.getId(),
								MsgInner.class.getSimpleName(), user.getUserCode());
					}
				}
			}
		});
		msgInnerRecordDao.insertBatch(recordList, null);
		// 手动触发消息推送任务
		if (Global.TRUE.equals(Global.getProperty("msg.realtime.enabled"))){
			msgPushThreadPool.submit(new Runnable() {
				@Override
				public void run() {
					try{
						MsgPushUtils.getMsgPushTask().execute();
					}catch(Exception ex){
						logger.error("实时消息发送失败，推送服务配置不正确。", ex);
					}
				}
			});
		}
	}
	
	/**
	 * 根据消息编号和接受者用户名读取内部消息
	 */
	@Transactional
	public void readMsgInnerRecord(MsgInner msgInner){
		if (StringUtils.isBlank(msgInner.getId())) {
			return;
		}
		MsgInnerRecord msgInnerRecord = new MsgInnerRecord();
		msgInnerRecord.setMsgInnerId(msgInner.getId());
		msgInnerRecord.setReceiveUserCode(msgInner.currentUser().getUserCode());
		msgInnerRecord.setReadStatus(MsgInnerRecord.READ_STATUS_READ);
		msgInnerRecord.setReadDate(new Date());
		msgInnerRecordDao.updateReadStatus(msgInnerRecord);
		// 将关联的内部消息通知更新为已读（消息推送）
		MsgPushUtils.readMsgByBiz(msgInner.getId(), MsgInner.class.getSimpleName(),
				msgInnerRecord.getReceiveUserCode());
	}
	
	/**
	 * 更新状态
	 * @param msgInner 数据对象
	 */
	@Override
	@Transactional
	public void updateStatus(MsgInner msgInner) {
		super.updateStatus(msgInner);
	}
	
	/**
	 * 删除数据
	 * @param msgInner 数据对象
	 */
	@Override
	@Transactional
	public void delete(MsgInner msgInner) {
		super.delete(msgInner);
	}
	
}