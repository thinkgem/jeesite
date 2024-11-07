// 如果登录页面在框架中，则突破框架直接显示到顶部窗口
try{
	if((window.toTopWindow !== undefined && window.toTopWindow === true)
			&& self.frameElement && self.frameElement.tagName == "IFRAME"){
		js.window.location = ctx + '/login';
	}
}catch(e){}//跨域不抛异常

/*!
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 * No deletion without permission, or be held responsible to law.
 *
 * @author ThinkGem
 * @version 2019-1-6
 */
$(function(){
	
	$('#username, #password').on('focus blur',function(){
		var $this = this;
		setTimeout(function(){
			var bc = $($this).css('borderColor');
			if (bc != ''){
				$($this).prev().css('color', bc);
			}
		}, 100);
	}).blur();
	
	$('#loginTab').on('shown.bs.tab', function(e){
		var related = $(e.relatedTarget).attr('href');
		if (related && related.length > 2){
			related = related.substring(1);
			$(this).parent().find('.tab-pane.' + related).removeClass('active');
		}
		var target = $(e.target).attr('href');
		if (target && target.length > 2){
			target = target.substring(1);
			$(this).parent().find('.tab-pane.' + target).addClass('active');
		}
		var action = $(e.target).attr('action');
		if (action && action != ''){
			$('#loginForm').attr('action', action);
		}
	});
	
	var waitTime = 60;
	function sendTime(o) {
		if (waitTime == 0) {
			o.removeAttribute("disabled");
			o.value = "获取验证码";
			waitTime = 60;
		} else {
			o.setAttribute("disabled", true);
			o.value = "重新发送(" + waitTime + ")";
			waitTime--;
			setTimeout(function() {
				sendTime(o)
			}, 1000);
		}
	}
	
	$('#sendLoginValidCode').click(function() {
		if ($('#validCode').val() == ''){
			$('#isValidCodeLogin').show();
			$('#validCodeImg').click();
		}else{
			var $this = this;
			js.ajaxSubmit(ctxPath + '/account/getLoginValidCode', {
				mobile : $('#mobile').val(),
				validCode : $('#validCode').val()
			}, function(data){
				js.showMessage(data.message);
				if (data.result == 'true'){
					sendTime($this);
					$('#loginValidCode').focus();
					if (data.extMessage && data.userList) {
						js.showMessage(data.extMessage, undefined, 'warning');
						var options = [];
						for (var i=0, l=data.userList.length; i<l; i++) {
							var user = data.userList[i];
							options.push('<option value="' + user.loginCode + '">'
								+ user.userName + ' (' + user.loginCode + ') </option>');
						}
						$('#selectLoginCode').html(options.join(''));
						$('#selectLoginCodeDiv').removeClass('hide');
					} else {
						$('#selectLoginCodeDiv').addClass('hide');
					}
				}else{
					$('#validCodeImg').click();
				}
			});
		}
	});
	
	$('#loginForm').validate({
		ignore: ":hidden",
		submitHandler: function (form) {
			var $form = $(form),
				action = $form.attr('action'),
				data = $form.serializeArray(),
				key = window.secretKey||$('#loginKey').data('key');
			if (key != ''){
				for (var i=0, l=data.length; i<l; i++){
					if (data[i].name == 'username'){
						data[i].value = DesUtils.encode($('#username').val(), key);
					}else if (data[i].name == 'password'){
						data[i].value = DesUtils.encode($('#password').val(), key);
					}else if (data[i].name == 'validCode'){
						data[i].value = DesUtils.encode($('#validCode').val(), key);
					}
				}
			}
			data.push({ name: '__be', value: '1'}); // back-end 标识后端，可减少一些逻辑
			js.ajaxSubmit(action, data, function(data, status, xhr){
				if (data.isValidCodeLogin == true){
					$('#isValidCodeLogin').show();
					$('#validCodeRefresh').click();
				}
				//js.cookie('rememberUserCode', 
				//	($form.find('[name="rememberUserCode"]').is(':checked') ? $('#username').val() : null),
				//	{expires:365, path:(ctxPath && ctxPath != '' ? ctxPath : '/')});
				if(data.result == "false"){
					if (data.message && data.message.length > 0){
						js.showMessage(data.message);
					}
				}else {
					js.loading($('#btnSubmit').data('loading'));
					if (data.__url && data.__url != ''){
						location = data.__url;
					}else{
						location = ctxAdmin + "/index";
					}
				}
			}, 'json', true, $('#btnSubmit').data('loginValid'));
			$('#password').select().focus();
		}
	});
	
	// 登录前页面设置，补充 hash
	var preUrl = $('#loginForm [name=__url]');
	if (preUrl.length > 0 && preUrl.val() != ''){
		preUrl.val(preUrl.val() + window.location.hash);
	}

});