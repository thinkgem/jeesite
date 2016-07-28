/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.wechat.service;

import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.service.CrudService;
import com.thinkgem.jeesite.modules.wechat.dao.WechatAccountDao;
import com.thinkgem.jeesite.modules.wechat.entity.WechatAccount;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 微信的账户管理Service
 * @author mazekkkk
 * @version 2016-07-27
 */
@Service
@Transactional(readOnly = true)
public class WechatAccountService extends CrudService<WechatAccountDao, WechatAccount> {

	public WechatAccount get(String id) {
		return super.get(id);
	}
	
	public List<WechatAccount> findList(WechatAccount wechatAccount) {
		return super.findList(wechatAccount);
	}
	
	public Page<WechatAccount> findPage(Page<WechatAccount> page, WechatAccount wechatAccount) {
		return super.findPage(page, wechatAccount);
	}
	
	@Transactional(readOnly = false)
	public void save(WechatAccount wechatAccount) {
		super.save(wechatAccount);
	}
	
	@Transactional(readOnly = false)
	public void delete(WechatAccount wechatAccount) {
		super.delete(wechatAccount);
	}
	
}