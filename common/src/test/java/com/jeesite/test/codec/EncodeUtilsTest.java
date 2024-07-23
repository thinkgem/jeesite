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
		EncodeUtils.xssFilter("1 你好 <script>alert(document.cookie)</script>我还在。");
		EncodeUtils.xssFilter("2 你好 <strong>加粗文字</strong>我还在。");
		EncodeUtils.xssFilter("<!--HTML-->3 你好 \"><strong>加粗文字</strong>我还在。");
		EncodeUtils.xssFilter("<!--HTML-->4 你好 <iframe src=\"abcdef\"></iframe><strong>加粗文字</strong>我还在。");
		EncodeUtils.xssFilter("<!--HTML-->5 你好 <iframe src=\"abcdef\"/><strong>加粗文字</strong>我还在。");
		EncodeUtils.xssFilter("<!--HTML-->6 你好 <iframe src=\"abcdef\"><strong>加粗文字</strong>我还在。");
		EncodeUtils.xssFilter("<!--HTML-->7 你好 <script type=\"text/javascript\">alert(document.cookie)</script>我还在。");
		EncodeUtils.xssFilter("<!--HTML-->8 你好 <script\n type=\"text/javascript\">\nalert(document.cookie)\n</script>我还在。");
		EncodeUtils.xssFilter("<!--HTML-->9 你好 <script src='' onerror='alert(document.cookie)'></script>我还在。");
		EncodeUtils.xssFilter("<!--HTML-->10 你好 <script type=text/javascript>alert()我还在。");
		EncodeUtils.xssFilter("<!--HTML-->11 你好 <script>alert(document.cookie)</script>我还在。");
		EncodeUtils.xssFilter("<!--HTML-->12 你好 <script>window.location='url'我还在。");
		EncodeUtils.xssFilter("<!--HTML-->13 你好 </script></iframe>我还在。");
		EncodeUtils.xssFilter("<!--HTML-->14 你好 eval(abc)我还在。");
		EncodeUtils.xssFilter("<!--HTML-->15 你好 expression(abc)我还在。");
		EncodeUtils.xssFilter("<!--HTML-->16 你好 <img src='abc.jpg' onerror='location='';alert(document.cookie);'></img>我还在。");
		EncodeUtils.xssFilter("<!--HTML-->17 你好 <img src='abc.jpg' onerror='alert(document.cookie);'/>我还在。");
		EncodeUtils.xssFilter("<!--HTML-->18 你好 <img src='abc.jpg' onerror='alert(document.cookie);'>我还在。");
		EncodeUtils.xssFilter("<!--HTML-->19 你好 <a onload='alert(\"abc\")'>hello</a>我还在。");
		EncodeUtils.xssFilter("<!--HTML-->20 你好 <a href=\"/abc\">hello</a>我还在。");
		EncodeUtils.xssFilter("<!--HTML-->21 你好 <a href='/abc'>hello</a>我还在。");
		EncodeUtils.xssFilter("<!--HTML-->22 你好 <a href='vbscript:alert(\"abc\");'>hello</a>我还在。");
		EncodeUtils.xssFilter("<!--HTML-->23 你好 <a href='javascript:alert(\"abc\");'>hello</a>我还在。");
		EncodeUtils.xssFilter("<!--HTML-->24 你好 ?abc=def&hello=123&world={\"a\":1}我还在。");
		EncodeUtils.xssFilter("<!--HTML-->25 你好 ?abc=def&hello=123&world={'a':1}我还在。");
		EncodeUtils.sqlFilter("1 你好 select * from xxx where abc=def and 1=1我还在。");
		EncodeUtils.sqlFilter("2 你好 insert into xxx values(1,2,3,4,5)我还在。");
		EncodeUtils.sqlFilter("3 你好 delete from xxx我还在。");
		EncodeUtils.sqlFilter("4 a.audit_result asc,case when 1 like case when length(database())=6 then 1 else exp(111) end then 1 else 1/0 end", "orderBy");
		EncodeUtils.sqlFilter("5 if(1=2,1,SLEEP(10)), if(mid(database(),{},1)=\\\"{}\\\",a.id,a.login_name)", "orderBy");
		EncodeUtils.sqlFilter("6 a.audit_result asc, b.audit_result2 desc, b.AuditResult3 desc", "orderBy");
	}

}
