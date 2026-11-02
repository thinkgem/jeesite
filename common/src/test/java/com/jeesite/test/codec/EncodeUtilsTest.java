/**
 * Copyright (c) 2013-Now https://jeesite.com All rights reserved.
 * No deletion without permission, or be held responsible to law.
 */
package com.jeesite.test.codec;

import com.jeesite.common.codec.EncodeUtils;
import com.jeesite.common.lang.StringUtils;

/**
 * 封装各种格式的编码解码工具测试类
 * @author ThinkGem
 * @version 2024-07-22
 */
public class EncodeUtilsTest {

	private static int failed = 0;

	public static void main(String[] args) {
		int i = 0;
		xssFilter(i++, "你好 <script>alert(document.cookie)</script>我还在。");
		xssFilter(i++, "你好 <strong>加粗文字</strong>我还在。");
		xssFilter(i++, "<!--HTML-->你好 \"><strong>加粗文字</strong>我还在。");
		xssFilter(i++, "<!--HTML-->你好 <iframe src=\"abcdef\"></iframe><strong>加粗文字</strong>我还在。");
		xssFilter(i++, "<!--HTML-->你好 <iframe src=\"abcdef\"/><strong>加粗文字</strong>我还在。");
		xssFilter(i++, "<!--HTML-->你好 <iframe src=\"abcdef\"><strong>加粗文字</strong>我还在。");
		xssFilter(i++, "<!--HTML-->你好 <script type=\"text/javascript\">alert(document.cookie)</script>我还在。");
		xssFilter(i++, "<!--HTML-->你好 <script\n type=\"text/javascript\">\nalert(document.cookie)\n</script>我还在。");
		xssFilter(i++, "<!--HTML-->你好 <script src='' onerror='alert(document.cookie)'></script>我还在。");
		xssFilter(i++, "<!--HTML-->你好 <script type=text/javascript>alert()我还在。");
		xssFilter(i++, "<!--HTML-->你好 <script>alert(document.cookie)</script>我还在。");
		xssFilter(i++, "<!--HTML-->你好 <script>window.location='url'我还在。");
		xssFilter(i++, "<!--HTML-->你好 </script></iframe>我还在。");
		xssFilter(i++, "<!--HTML-->你好 eval(abc)我还在。");
		xssFilter(i++, "<!--HTML-->你好 expression(abc)我还在。");
		xssFilter(i++, "<!--HTML-->你好 <img src='abc.jpg' onerror='location='';alert(document.cookie);'></img>我还在。");
		xssFilter(i++, "<!--HTML-->你好 <img src='abc.jpg' onerror='alert(document.cookie);'/>我还在。");
		xssFilter(i++, "<!--HTML-->你好 <img src='abc.jpg' onerror='alert(document.cookie);'>我还在。");
		xssFilter(i++, "<!--HTML-->你好 <a onload='alert(\"abc\")'>hello</a>我还在。");
		xssFilter(i++, "<!--HTML-->你好 <a href=\"/abc\">hello</a>我还在。");
		xssFilter(i++, "<!--HTML-->你好 <a href='/abc'>hello</a>我还在。");
		xssFilter(i++, "<!--HTML-->你好 <a href='vbscript:alert(\"abc\");'>hello</a>我还在。");
		xssFilter(i++, "<!--HTML-->你好 <a href='javascript:alert(\"abc\");'>hello</a>我还在。");
		xssFilter(i++, "<!--HTML-->你好 ?abc=def&hello=123&world={\"a\":1}我还在。");
		xssFilter(i++, "<!--HTML-->你好 ?abc=def&hello=123&world={'a':1}我还在。");
		xssFilter(i++, "<!--HTML-->\"><svg/ONLOAD=confirm(3) />");
		xssFilter(i++, "<!--HTML-->XSS<embed src=\"data:text/html;base64,PHNjcmlwdD5hbGVydCg5KTwvc2NyaXB0Pgo=\">");
		xssFilter(i++, "<!--HTML-->XSS<object data='data:text/html;base64,PHNjcmlwdD5hbGVydCgxKTwvc2NyaXB0Pg=='></object>");
		sqlFilter(i++, "你好 select * from xxx where abc=def and 1=1我还在。", "common");
		sqlFilter(i++, "你好 insert into xxx values(1,2,3,4,5)我还在。", "common");
		sqlFilter(i++, "你好 delete from xxx我还在。", "common");
		sqlFilter(i++, "a.audit_result asc,case when 1 like case when length(database())=6 then 1 else exp(111) end then 1 else 1/0 end", "orderBy");
		sqlFilter(i++, "if(1=2,1,SLEEP(10)), if(mid(database(),{},1)=\\\"{}\\\",a.id,a.login_name)", "orderBy");
		sqlFilter(i++, "a.audit_result asc, b.audit_result2 desc, b.AuditResult3 desc", "orderBy");
		validJdbcUrl(i++, "jdbc:h2:mem:jeesite_db;TRACE_LEVEL_SYSTEM_OUT=3;INIT=CREATE ALIAS IF NOT...");
		validJdbcUrl(i++, "jdbc:h2:mem:jeesite_db;TRACE_LEVEL_SYSTEM_OUT=3;I\\NIT=CREATE ALIAS IF NOT...");
		validJdbcUrl(i++, "jdbc:h2:mem:jeesite_db;TRACE_LEVEL_SYSTEM_OUT=3;I/**/NIT=CREATE ALIAS IF NOT...");

		// CMS 文章正文存储型 XSS 修复验证：事件属性后没有 >，浏览器会自动闭合标签，事件依然生效
		xssFilterCheck(i++, "<!--HTML--><img src=x alt=\"JeeSite PoC\" onerror=document.querySelector('.article-title').textContent='JeeSite_ZeroClick_XSS_Executed'", "onerror", "ZeroClick");
		xssFilterCheck(i++, "<!--HTML--><img src=x onerror=alert(1)", "onerror");
		xssFilterCheck(i++, "<!--HTML--><img src=x onerror=alert(1)<!--", "onerror");
		xssFilterCheck(i++, "<!--HTML--><img src=x onerror=alert(1)/", "onerror");
		xssFilterCheck(i++, "<!--HTML--><img/onerror=alert(1)", "onerror");
		xssFilterCheck(i++, "<!--HTML--><img src=x OnErRoR=alert(1)", "onerror");
		xssFilterCheck(i++, "<!--HTML--><img src=x onerror=\"alert(1)", "onerror");
		xssFilterCheck(i++, "<!--HTML--><details open ontoggle=alert(1)", "ontoggle");
		xssFilterCheck(i++, "<!--HTML--><svg/ONLOAD=confirm(3)", "onload");
		// 危险协议属性后没有 >，以及 HTML 实体编码绕过
		xssFilterCheck(i++, "<!--HTML--><a href=javascript:alert(1)", "javascript:");
		xssFilterCheck(i++, "<!--HTML--><a href=\"jav&#x61;script:alert(1)\">x</a>", "script:");
		xssFilterCheck(i++, "<!--HTML--><a href=\"jav&#97;script:alert(1)\">x</a>", "script:");
		// HTML5 命名字符实体绕过，例如 href=javascript&colon;alert(1)
		xssFilterCheck(i++, "<!--HTML--><a href=javascript&colon;alert(1)>x</a>", "script:");
		xssFilterCheck(i++, "<!--HTML--><a href='java&Tab;script:alert(1)'>x</a>", "script:");
		xssFilterCheck(i++, "<!--HTML--><a href=\"java&NewLine;script:alert(1)\">x</a>", "script:");
		xssFilterCheck(i++, "<!--HTML--><a href=\"javascript&COLON;alert(1)\">x</a>", "script:");
		// 无实体、但含空白干扰或非 href/src 属性（form action、srcset、style、meta content）的协议绕过
		xssFilterCheck(i++, "<!--HTML--><a href=jav&#x09;ascript:alert(1)>x</a>", "script:");
		xssFilterCheck(i++, "<!--HTML--><a href=\"java\tscript:alert(1)\">x</a>", "script:");
		xssFilterCheck(i++, "<!--HTML--><form action=javascript:alert(1)><input type=submit></form>", "javascript:");
		// form action 带引号、值内又含单引号与点号调用，验证不能被绕过
		xssFilterCheck(i++, "<!--HTML--><form action=\"javascript:document.title='FORMXSS'\">"
				+ "<input type=submit id=go value=go></form>", "javascript:", "FORMXSS");
		xssFilterCheck(i++, "<!--HTML--><form action=javascript:document.title='FORMXSS'>"
				+ "<input type=submit id=go value=go></form>", "javascript:", "FORMXSS");
		xssFilterCheck(i++, "<!--HTML--><form action='javascript:document.title=\"FORMXSS\"'>"
				+ "<input type=submit id=go value=go></form>", "javascript:", "FORMXSS");
		// 实体编码与空白干扰的同类变形
		xssFilterCheck(i++, "<!--HTML--><form action=\"jav&#x61;script:document.title='FORMXSS'\">"
				+ "<input type=submit id=go value=go></form>", "script:", "FORMXSS");
		xssFilterCheck(i++, "<!--HTML--><form action=\"java&Tab;script:document.title='FORMXSS'\">"
				+ "<input type=submit id=go value=go></form>", "script:", "FORMXSS");
		xssFilterCheck(i++, "<!--HTML--><img srcset=javascript:alert(1)>", "javascript:");
		// data: 协议按类型区分：可执行类型拦截，正常内联资源保留（不能一刀切）
		xssFilterCheck(i++, "<!--HTML--><a href=\"data:text/html;base64,PHNjcmlwdD5hbGVydCgxKTwvc2NyaXB0Pg==\">x</a>", "data:text/html");
		xssFilterCheck(i++, "<!--HTML--><img src=data:image/svg+xml;base64,PHN2Zz48L3N2Zz4=>", "data:image/svg");
		xssFilterCheck(i++, "<!--HTML--><img src=\"data:text/javascript;base64,YWxlcnQoMSk=\">", "data:text/javascript");
		xssFilterCheck(i++, "<!--HTML--><div style=\"background:url(javascript:alert(1))\">x</div>", "javascript:");
		xssFilterCheck(i++, "<!--HTML--><meta http-equiv=refresh content=\"0;url=javascript:alert(1)\">", "javascript:");
		// SVG 动画标签通过 attributeName、values、from、to 动态写入 href 或事件属性
		xssFilterCheck(i++, "<!--HTML--><svg><a><animate attributeName=href values=javascript:alert(1)></animate>"
				+ "<text x=\"20\" y=\"20\">点击</text></a></svg>", "javascript:", "animate");
		xssFilterCheck(i++, "<!--HTML--><svg><animate attributeName=href values=\"javascript:alert(1)\"/></svg>",
				"javascript:", "animate");
		xssFilterCheck(i++, "<!--HTML--><svg><set attributeName=onmouseover to=alert(1)></set></svg>",
				"onmouseover", "<set");
		xssFilterCheck(i++, "<!--HTML--><svg><animateTransform attributeName=transform to=\"javascript:alert(1)\">"
				+ "</animateTransform></svg>", "javascript:", "animateTransform");
		// 普通属性不能被误判为事件属性（content= 曾被当作 ontent= 删除）
		xssFilterEquals(i++, "<!--HTML--><meta name=\"description\" content=\"页面描述，包含 javascript 关键字与 on the fly 文本\">");

		// 嵌套标签移除后重新拼接
		xssFilterCheck(i++, "<!--HTML--><scr<script>ipt>alert(1)</script>", "script");
		// 正常内容不应被修改
		xssFilterEquals(i++, "<!--HTML--><p class=\"article\">正常内容，工作 on the fly，价格 100% 好评</p>");
		xssFilterEquals(i++, "<!--HTML--><p>a&nbsp;b</p><img src=\"/userfiles/2026/11/02/a.png\" alt=\"图片\">");
		// 合法的 SVG 图形内容不应被误删
		xssFilterEquals(i++, "<!--HTML--><svg width=\"20\" height=\"20\"><circle cx=\"10\" cy=\"10\" r=\"8\"/></svg>");

		// ------- 正常 HTML 富文本（编辑器产出）不允许被误处理 -------
		xssFilterEquals(i++, "<!--HTML--><h1>标题</h1><h2>二级标题</h2><h3>三级标题</h3>");
		xssFilterEquals(i++, "<!--HTML--><p>第一段<strong>加粗</strong><em>斜体</em><u>下划线</u><s>删除线</s></p>");
		xssFilterEquals(i++, "<!--HTML--><p><br></p><hr><p>&nbsp;</p>");
		xssFilterEquals(i++, "<!--HTML--><ul><li>列表一</li><li>列表二</li></ul><ol><li>有序一</li></ol>");
		xssFilterEquals(i++, "<!--HTML--><dl><dt>名词</dt><dd>解释</dd></dl>");
		xssFilterEquals(i++, "<!--HTML--><blockquote><p>引用内容</p></blockquote>");
		xssFilterEquals(i++, "<!--HTML--><table class=\"table\"><thead><tr><th>标题</th></tr></thead>"
				+ "<tbody><tr><td>单元格</td></tr></tbody></table>");
		xssFilterEquals(i++, "<!--HTML--><pre><code class=\"language-java\">int a = 1 &lt; 2 &amp;&amp; 3 &gt; 2;</code></pre>");
		xssFilterEquals(i++, "<!--HTML--><div class=\"article-body\" id=\"content\" style=\"color:#333;font-size:14px\">正文</div>");
		xssFilterEquals(i++, "<!--HTML--><span data-id=\"123\" data-name=\"abc\">普通文本</span>");
		// 相对、站内、锚点、邮件、电话、协议相对等正常链接
		xssFilterEquals(i++, "<!--HTML--><a href=\"/userfiles/2026/11/02/a.png\" title=\"下载\">下载</a>");
		xssFilterEquals(i++, "<!--HTML--><a href=\"https://jeesite.com/index.html?a=1&amp;b=2#top\" target=\"_blank\">JeeSite</a>");
		xssFilterEquals(i++, "<!--HTML--><a href=\"#section-2\" name=\"anchor\">锚点</a>");
		xssFilterEquals(i++, "<!--HTML--><a href=\"mailto:service@jeesite.com\">邮件</a>");
		xssFilterEquals(i++, "<!--HTML--><a href=\"tel:10086\">电话</a>");
		xssFilterEquals(i++, "<!--HTML--><a href=\"//cdn.jeesite.com/jQuery.js\">协议相对</a>");
		// 图片、媒体、编码路径等正常资源引用
		xssFilterEquals(i++, "<!--HTML--><img src=\"/userfiles/2026/11/02/a.png\" alt=\"图片标题\" width=\"100\" height=\"50\" loading=\"lazy\">");
		xssFilterEquals(i++, "<!--HTML--><img src=\"data:image/png;base64,iVBORw0KGgoAAAANSUhEUg==\" alt=\"内联图片\">");
		xssFilterEquals(i++, "<!--HTML--><img src=\"data:image/jpeg;base64,/9j/4AAQSkZJRg==\" alt=\"内联照片\">");
		xssFilterEquals(i++, "<!--HTML--><a href=\"data:application/pdf;base64,JVBERi0xLjQK\">下载 PDF</a>");
		xssFilterEquals(i++, "<!--HTML--><img src=\"/userfiles/2026/11/02/%E4%B8%AD%E6%96%87%E5%90%8D.png\" alt=\"编码路径\">");
		xssFilterEquals(i++, "<!--HTML--><video src=\"/userfiles/2026/11/02/a.mp4\" poster=\"/userfiles/2026/11/02/p.jpg\" controls></video>");
		xssFilterEquals(i++, "<!--HTML--><audio src=\"/userfiles/2026/11/02/a.mp3\" controls></audio>");
		// 文本中出现关键字、百分号、等号、引号、中文标点，不能当成攻击
		xssFilterEquals(i++, "<!--HTML--><p>价格 100% 好评，10% off，a=1&amp;b=2，x=y</p>");
		xssFilterEquals(i++, "<!--HTML--><p>关于 javascript 协议的说明，on the fly 处理</p>");
		xssFilterEquals(i++, "<!--HTML--><p>函数 onclick 与 onerror 的用法说明</p>");
		xssFilterEquals(i++, "<!--HTML--><p>引号“中文”、‘单引号’、反斜杠 \\ 与 &lt;tag&gt; 文本</p>");
		xssFilterEquals(i++, "<!--HTML--><p>代码片段 select * from xxx where 1=1</p>");
		// 纯文本（非 HTML 模式）同样不能被改写
		xssFilterEquals(i++, "你好，世界 100% 好评，a=1&b=2，a/b 与 a\\b");
		xssFilterEquals(i++, "订单号 202611020001，金额 1234.56 元，共 2 件");

		System.out.println("XSS 过滤验证完成，失败数量：" + failed);
		if (failed > 0) {
			throw new IllegalStateException("XSS 过滤验证失败数量：" + failed);
		}
	}

	/**
	 * XSS 修复验证：过滤结果不应再包含指定的危险内容
	 */
	private static void xssFilterCheck(int num, String text, String... forbiddens) {
		String text2 = EncodeUtils.xssFilter(text);
		boolean pass = true;
		for (String forbidden : forbiddens) {
			if (StringUtils.containsIgnoreCase(text2, forbidden)) {
				pass = false;
				break;
			}
		}
		if (!pass) {
			failed++;
		}
		System.out.println(num + ". [" + (pass ? "PASS" : "FAIL") + "] " + text + "\t ==> \t" + text2);
	}

	/**
	 * XSS 修复验证：正常内容过滤后应保持不变
	 */
	private static void xssFilterEquals(int num, String text) {
		String text2 = EncodeUtils.xssFilter(text);
		boolean pass = text.equals(text2);
		if (!pass) {
			failed++;
		}
		System.out.println(num + ". [" + (pass ? "PASS" : "FAIL") + "] " + text + "\t ==> \t" + text2);
	}

	private static void xssFilter(int num, String text) {
		String text2 = EncodeUtils.xssFilter(text);
		System.out.println(num + ". " + text + "\t ==> \t" + text2 + "\t ==> \t" + text.equals(text2));
	}

	private static void sqlFilter(int num, String text, String source) {
		String text2 = EncodeUtils.sqlFilter(text, source);
		System.out.println(num + ". " + text + "\t ==> \t" + text2 + "\t ==> \t" + text.equals(text2));
	}

	private static void validJdbcUrl(int num, String text) {
		try {
			EncodeUtils.validJdbcUrl(text);
			System.out.println(num + ". " + text + "\t ==> \t" + true);
		} catch (Exception e) {
			System.out.println(num + ". " + text + "\t ==> \t" + e.getMessage());
		}
	}

}
