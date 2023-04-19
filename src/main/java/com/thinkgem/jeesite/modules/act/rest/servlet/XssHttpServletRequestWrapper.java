package com.thinkgem.jeesite.modules.act.rest.servlet;

import com.thinkgem.jeesite.common.utils.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.util.HashMap;
import java.util.Map;

/**
 * xxs安全性增强
 * @author 高强
 * @version 2023-04-19
 */
public class XssHttpServletRequestWrapper extends HttpServletRequestWrapper {
    HttpServletRequest orgRequest = null;
    private boolean isIncludeRichText = false;

    public XssHttpServletRequestWrapper(HttpServletRequest request, boolean isIncludeRichText) {
        super(request);
        orgRequest = request;
        this.isIncludeRichText = isIncludeRichText;
    }

    /**
     * 覆盖getParameter方法，将参数名和参数值都做xss过滤。<br/>
     * 如果需要获得原始的值，则通过super.getParameterValues(name)来获取<br/>
     * getParameterNames,getParameterValues和getParameterMap也可能需要覆盖
     */
    @Override
    public String getParameter(String name) {
        Boolean flag = ("content".equals(name) || "WithHtml".endsWith(name) || "articleData.content".endsWith(name) || "href".endsWith(name));
        if( flag && !isIncludeRichText){
            return super.getParameter(name);
        }
        name = JsoupUtil.clean(name);
        String value = super.getParameter(name);
        if (StringUtils.isNotBlank(value)) {
            value = JsoupUtil.clean(value);
        }
        return value;
    }

    @Override
    public String[] getParameterValues(String name) {
        String[] arr = super.getParameterValues(name);
        Boolean flag = ("content".equals(name) || "WithHtml".endsWith(name) || "articleData.content".endsWith(name) || "href".endsWith(name));
        if( flag && !isIncludeRichText){
            System.out.println("value1 = " + super.getParameter(name));
            return arr;
        }
        if(arr != null){
            for (int i=0;i<arr.length;i++) {
                arr[i] = JsoupUtil.clean(arr[i]);
            }
        }
        return arr;
    }

    @Override
    public Map<String, String[]> getParameterMap(){
        Map<String, String[]> values=super.getParameterMap();
        if (values == null) {
            return null;
        }
        Map<String, String[]> result=new HashMap();
        for(String key:values.keySet()){
            String encodedKey=JsoupUtil.clean(key);
            int count=values.get(key).length;
            String[] encodedValues = new String[count];
            for (int i = 0; i < count; i++){
                encodedValues[i]=JsoupUtil.clean(values.get(key)[i]);
            }
            result.put(encodedKey,encodedValues);
        }
        return result;
    }


    /**
     * 覆盖getHeader方法，将参数名和参数值都做xss过滤。<br/>
     * 如果需要获得原始的值，则通过super.getHeaders(name)来获取<br/>
     * getHeaderNames 也可能需要覆盖
     */
    @Override
    public String getHeader(String name) {
        name = JsoupUtil.clean(name);
        String value = super.getHeader(name);
        if (StringUtils.isNotBlank(value)) {
            value = JsoupUtil.clean(value);
        }
        return value;
    }

    private String cleanXSS(String value) {
        //You'll need to remove the spaces from the html entities below
        value = value.replaceAll("<", "&lt;").replaceAll(">", "&gt;");
        value = value.replaceAll("\\(", "&#40;").replaceAll("\\)", "&#41;");
        value = value.replaceAll("'", "&#39;");
        value = value.replaceAll("eval\\((.*)\\)", "");
        value = value.replaceAll("[\\\"\\\'][\\s]*javascript:(.*)[\\\"\\\']", "\"\"");
        value = value.replaceAll("script", "");
        value = value.replaceAll("\\.\\./", "");
        return value;
    }

    /**
     * 获取最原始的request
     *
     * @return
     */
    public HttpServletRequest getOrgRequest() {
        return orgRequest;
    }

    /**
     * 获取最原始的request的静态方法
     *
     * @return
     */
    public static HttpServletRequest getOrgRequest(HttpServletRequest req) {
        if (req instanceof XssHttpServletRequestWrapper) {
            return ((XssHttpServletRequestWrapper) req).getOrgRequest();
        }

        return req;
    }

}
