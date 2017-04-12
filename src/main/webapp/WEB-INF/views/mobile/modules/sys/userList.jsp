<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<section id="user_section">
    <header>
        <nav class="left">
            <a href="#" data-icon="previous" data-target="back">返回</a>
        </nav>
        <h1 class="title">用户列表</h1>
    </header>
    <article class="active">
    	<div style="line-height:50px;padding:10px;">
    		手机端功能没有开发，请继续完善。<br/>
    		你如果有比较好的想法或扩展，也希望您共享出自己的一份代码。
    		请联系 thinkgem@163.com 谢谢！<br/>
    	</div>
    </article>
    <script type="text/javascript">
	$('body').delegate('#user_section','pageinit',function(){
	});
	$('body').delegate('#user_section','pageshow',function(){
        var hash = J.Util.parseHash(location.hash);
        console.log(hash.param);
	});
	</script>
</section>