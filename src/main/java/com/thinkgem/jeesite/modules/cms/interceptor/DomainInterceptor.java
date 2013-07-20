package com.thinkgem.jeesite.modules.cms.interceptor;

import com.thinkgem.jeesite.modules.cms.entity.Site;
import com.thinkgem.jeesite.modules.cms.utils.CmsUtils;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 通过域名访问子站点拦截器
 * User: songlai
 * Date: 13-7-20
 * Time: 上午9:59
 */
public class DomainInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        Site site = null;
        List<Site> list = CmsUtils.getSiteList();
        int size = list.size();
        if(size == 0){
            throw new RuntimeException("系统无站点。");
        }else if(size == 1){
            site = list.get(0);
        }else{
            String server = request.getServerName();
            for(Site s : list){
                if(s.getDomain().equals(server)){
                    site = s;
                }
            }
            if(site == null){
                site = CmsUtils.getSite(Site.defaultSiteId());
            }
        }
        CmsUtils.setSite(site);
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }
}
