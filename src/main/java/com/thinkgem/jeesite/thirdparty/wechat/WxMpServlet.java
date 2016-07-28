package com.thinkgem.jeesite.thirdparty.wechat;

import me.chanjar.weixin.mp.api.WxMpConfigStorage;
import me.chanjar.weixin.mp.api.WxMpMessageRouter;
import me.chanjar.weixin.mp.api.WxMpService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

/**
 * Created by mazekkkk on 16/7/27.
 */
public class WxMpServlet extends HttpServlet {

    private WxMpConfigStorage wxMpConfigStorage;
    private WxMpService wxMpService;
    private WxMpMessageRouter wxMpMessageRouter;

    @Override
    public void init() throws ServletException {
        super.init();
        //初始化微信配置

    }
}
