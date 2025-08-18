/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 * No deletion without permission, or be held responsible to law.
 */
package com.jeesite.test.codec;

import com.jeesite.common.codec.EncodeUtils;

/**
 * 封装各种格式的编码解码工具测试类
 * @author ThinkGem
 * @version 2024-07-22
 */
public class EncodeUtilsTest {

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
		sqlFilter(i++, "你好 select * from xxx where abc=def and 1=1我还在。", "common");
		sqlFilter(i++, "你好 insert into xxx values(1,2,3,4,5)我还在。", "common");
		sqlFilter(i++, "你好 delete from xxx我还在。", "common");
		sqlFilter(i++, "a.audit_result asc,case when 1 like case when length(database())=6 then 1 else exp(111) end then 1 else 1/0 end", "orderBy");
		sqlFilter(i++, "if(1=2,1,SLEEP(10)), if(mid(database(),{},1)=\\\"{}\\\",a.id,a.login_name)", "orderBy");
		sqlFilter(i++, "a.audit_result asc, b.audit_result2 desc, b.AuditResult3 desc", "orderBy");
	}

	private static void xssFilter(int num, String text) {
		String text2 = EncodeUtils.xssFilter(text);
		System.out.println(num + ". " + text + "\t ==> \t" + text2 + "\t ==> \t" + text.equals(text2));
	}

	private static void sqlFilter(int num, String text, String source) {
		String text2 = EncodeUtils.sqlFilter(text, source);
		System.out.println(num + ". " + text + "\t ==> \t" + text2 + "\t ==> \t" + text.equals(text2));
	}

}
