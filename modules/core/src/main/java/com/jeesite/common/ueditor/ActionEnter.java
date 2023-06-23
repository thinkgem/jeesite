package com.jeesite.common.ueditor;

import java.util.Map;

import jakarta.servlet.http.HttpServletRequest;

import com.jeesite.common.config.Global;
import com.jeesite.common.ueditor.define.ActionMap;
import com.jeesite.common.ueditor.define.AppInfo;
import com.jeesite.common.ueditor.define.BaseState;
import com.jeesite.common.ueditor.define.State;
import com.jeesite.common.ueditor.hunter.FileManager;
import com.jeesite.common.ueditor.hunter.ImageHunter;
import com.jeesite.common.ueditor.upload.Uploader;

public class ActionEnter {

    private HttpServletRequest request = null;

    private String rootPath = null;
    private String contextPath = null;

    private String actionType = null;

    private ConfigManager configManager = null;

    public ActionEnter(HttpServletRequest request, String rootPath) {
        this(request, rootPath, request.getParameter("action"));
    }

    public ActionEnter(HttpServletRequest request, String rootPath, String actionType) {
        this.request = request;
        this.rootPath = rootPath;
        this.actionType = actionType;
        this.contextPath = request.getContextPath();
        this.configManager = ConfigManager.getInstance(this.rootPath, this.contextPath, request.getRequestURI());
    }

    public String exec() {
        String callbackName = this.request.getParameter("callback");
        if (callbackName != null) {
            if (!validCallbackName(callbackName)) {
                return new BaseState(false, AppInfo.ILLEGAL).toJSONString();
            }
            return callbackName + "(" + this.invoke() + ");";
        } else {
            return this.invoke();
        }
    }

    public String invoke() {
        if (actionType == null || !ActionMap.mapping.containsKey(actionType)) {
            return new BaseState(false, AppInfo.INVALID_ACTION).toJSONString();
        }
        if (this.configManager == null || !this.configManager.valid()) {
            return new BaseState(false, AppInfo.CONFIG_ERROR).toJSONString();
        }
        State state = null;
        int actionCode = ActionMap.getType(this.actionType);
        Map<String, Object> conf = null;
        switch (actionCode) {
            case ActionMap.CONFIG:
                return this.configManager.getAllConfig().toString();
            case ActionMap.UPLOAD_IMAGE:
            case ActionMap.UPLOAD_SCRAWL:
            case ActionMap.UPLOAD_VIDEO:
            case ActionMap.UPLOAD_FILE:
                if (Global.isDemoMode()) {
                    state = new BaseState(false, "演示模式，不允许操作！");
                    break;
                }
                conf = this.configManager.getConfig(actionCode);
                state = new Uploader(request, conf).doExec();
                break;
            case ActionMap.CATCH_IMAGE:
                if (Global.isDemoMode()) {
                    state = new BaseState(false, "演示模式，不允许操作！");
                    break;
                }
                conf = configManager.getConfig(actionCode);
                String[] list = this.request.getParameterValues((String) conf.get("fieldName"));
                state = new ImageHunter(request, conf).capture(list);
                break;
            case ActionMap.LIST_IMAGE:
            case ActionMap.LIST_FILE:
                if (Global.isDemoMode()) {
                    state = new BaseState(false, "演示模式，不允许操作！");
                    break;
                }
                conf = configManager.getConfig(actionCode);
                int start = this.getStartIndex();
                state = new FileManager(conf).listFile(this.request, start);
                break;
        }
        return state.toJSONString();
    }

    public int getStartIndex() {
        String start = this.request.getParameter("start");
        try {
            return Integer.parseInt(start);
        } catch (Exception e) {
            return 0;
        }
    }

    /**
     * callback参数验证
     */
    public boolean validCallbackName(String name) {
        if (name.matches("^[a-zA-Z_]+[\\w0-9_]*$")) {
            return true;
        }
        return false;

    }

}