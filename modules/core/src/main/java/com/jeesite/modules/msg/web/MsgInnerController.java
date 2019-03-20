/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.modules.msg.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.jeesite.common.config.Global;
import com.jeesite.common.entity.Page;
import com.jeesite.common.lang.StringUtils;
import com.jeesite.common.web.BaseController;
import com.jeesite.modules.msg.entity.MsgInner;
import com.jeesite.modules.msg.entity.MsgInnerRecord;
import com.jeesite.modules.msg.entity.MsgPush;
import com.jeesite.modules.msg.service.MsgInnerService;

/**
 * 内部消息Controller
 * @author ThinkGem
 * @version 2019-03-12
 */
@Controller
@RequestMapping(value = "${adminPath}/msg/msgInner")
public class MsgInnerController extends BaseController {

	@Autowired
	private MsgInnerService msgInnerService;
	
	/**
	 * 获取数据
	 */
	@ModelAttribute
	public MsgInner get(String id, boolean isNewRecord) {
		return msgInnerService.get(id, isNewRecord);
	}
	
	/**
	 * 查询列表
	 */
	@RequiresPermissions("msg:msgInner:view")
	@RequestMapping(value = {"list", ""})
	public String list(MsgInner msgInner, Model model) {
		model.addAttribute("msgInner", msgInner);
		return "modules/msg/msgInnerList";
	}
	
	/**
	 * 查询列表数据
	 */
	@RequiresPermissions("msg:msgInner:view")
	@RequestMapping(value = "listData")
	@ResponseBody
	public Page<MsgInner> listData(MsgInner msgInner, HttpServletRequest request, HttpServletResponse response) {
		msgInner.setPage(new Page<>(request, response));
		msgInner.setRecord(new MsgInnerRecord());
		msgInner.getRecord().setReceiveUserCode(msgInner.getCurrentUser().getUserCode());
		Page<MsgInner> page = msgInnerService.findPage(msgInner);
		return page;
	}

	/**
	 * 查看编辑表单
	 */
	@RequiresPermissions("msg:msgInner:view")
	@RequestMapping(value = "form")
	public String form(MsgInner msgInner, Model model) {
		if (StringUtils.isBlank(msgInner.getNotifyTypes())){
			msgInner.setNotifyTypes(MsgPush.TYPE_PC);
		}
		if (StringUtils.isBlank(msgInner.getContentLevel())){
			msgInner.setContentLevel(MsgInner.CONTENT_LEVEL_1);
		}
		if (StringUtils.isBlank(msgInner.getReceiveType())){
			msgInner.setReceiveType(MsgInner.RECEIVE_TYPE_USER);
		}
		model.addAttribute("msgInner", msgInner);
		return "modules/msg/msgInnerForm";
	}
	
	/**
	 * 查看编辑表单
	 */
	@RequiresPermissions("msg:msgInner:view")
	@RequestMapping(value = "view")
	public String view(MsgInner msgInner, Model model) {
		model.addAttribute("msgInner", msgInner);
		// 根据消息编号和接受者用户名读取内部消息
		msgInnerService.readMsgInnerRecord(msgInner);
		// 查询已读和未读用户列表数据
		MsgInnerRecord msgInnerRecord = new MsgInnerRecord();
		msgInnerRecord.setMsgInnerId(msgInner.getId());
		msgInnerRecord.setReadStatus(MsgInnerRecord.READ_STATUS_READ);
		model.addAttribute("readList", msgInnerService.findRecordList(msgInnerRecord));
		msgInnerRecord.setReadStatus(MsgInnerRecord.READ_STATUS_UNREAD);
		model.addAttribute("unReadList", msgInnerService.findRecordList(msgInnerRecord));
		return "modules/msg/msgInnerView";
	}

	/**
	 * 保存消息
	 */
	@RequiresPermissions("msg:msgInner:edit")
	@PostMapping(value = "save")
	@ResponseBody
	public String save(@Validated MsgInner msgInner, HttpServletRequest request) {
		MsgInner old = super.getWebDataBinderSource(request);
		if (old != null && MsgInner.STATUS_NORMAL.equals(old.getStatus())){
			return renderResult(Global.FALSE, "数据已发布，不允许修改！");
		}
		msgInnerService.save(msgInner);
		return renderResult(Global.TRUE, text("保存消息成功！"));
	}
	
	/**
	 * 删除消息
	 */
	@RequiresPermissions("msg:msgInner:edit")
	@RequestMapping(value = "delete")
	@ResponseBody
	public String delete(MsgInner msgInner, HttpServletRequest request) {
		MsgInner old = super.getWebDataBinderSource(request);
		if (old != null && MsgInner.STATUS_NORMAL.equals(old.getStatus())){
			return renderResult(Global.FALSE, "数据已发布，不允许删除！");
		}
		msgInnerService.delete(msgInner);
		return renderResult(Global.TRUE, text("删除消息成功！"));
	}
	
}