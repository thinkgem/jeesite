package com.thinkgem.jeesite.modules.act.rest.servlet;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.safety.Whitelist;

/**
 * xxs安全性增强
 * @author 高强
 * @version 2023-04-19
 */
public class JsoupUtil {
    private static final Whitelist whitelist = Whitelist.basicWithImages();
    /*
     * 配置过滤化参数,不对代码进行格式化
     */
    private static final Document.OutputSettings outputSettings = new Document.OutputSettings().prettyPrint(false);
    static {
        /*
         * 富文本编辑时一些样式是使用style来进行实现的 比如红色字体 所以需要给所有标签添加style属性
         */
        whitelist.addAttributes(":all", "style");
        //whitelist.addAttributes(":all", "img");
     /*  whitelist.addTags("img")
                .addAttributes("img", "align", "alt", "height", "src", "title", "width")
                .addProtocols("img", "src", "/", "");*/

    }
    //private static final MyWhitelist whitelist =new MyWhitelist();
    public static String clean(String content) {
        return Jsoup.clean(content, "", whitelist,outputSettings);
    }

}
