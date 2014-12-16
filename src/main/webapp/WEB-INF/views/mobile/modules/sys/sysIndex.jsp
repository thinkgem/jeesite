<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<section id="index_section">
	<header>
        <h1 class="title">${fns:getConfig('productName')}</h1>
        <nav class="right">
            <a data-icon="arrow-down-left-2" href="#" id="btnLogout">退出</a>
        </nav>
    </header>
    <article class="active" data-scroll="true">
        <div style="padding: 10px 0 20px;">
        <ul class="list inset demo-list">
            <li data-icon="next" data-selected="selected">
                <span class="icon user"></span>
                <a href="#user_section?test=abc" data-target="section">
                    <strong>用户管理</strong>
                </a>
            </li>
        </ul>
        </div>
    </article>
    <script type="text/javascript">
   		$('#btnLogout').tap(function(){
   			J.confirm('确认提示','确认要退出吗？',function(){
   				$.get("${ctx}/logout", function(){
   					sessionid = '';
   					J.showToast('退出成功！', 'success');
   					J.Router.goTo('#login_section');
   				});
   			});
   		});
    </script>
</section>