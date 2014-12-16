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
    	list
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