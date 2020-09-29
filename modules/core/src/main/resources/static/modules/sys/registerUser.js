/*!
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 * 
 * @author ThinkGem
 * @version 2019-1-6
 */
$(function(){
	
	$('#reg_validType').change(function(){
		var val = $(this).val(), action = '';
		$('.reg-element').addClass('hide').removeClass('block');
		$('.reg-'+val).addClass('block').removeClass('hide');
		setTimeout(function(){
			$('#reg_loginCode').focus();
		}, 100);
		if (val == 'mobile' || val == 'email'){
			var txt = (val == 'mobile' ? '手机' : '邮箱')
			$('#regValidCode').attr('placeholder', txt+'验证码')
				.attr('data-msg-required', '请填写'+txt+'验证码.');
			$('#sendRegValidCode').val('获取'+txt+'验证码');
			action = ctxPath + '/account/saveRegByValidCode';
		}else if(val == 'question'){
			action = ctxPath + '/account/savePwdByPwdQuestion';
		}
		$('#registerForm').attr('action', action);
	}).change();
	
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
	
	$('#sendRegValidCode').click(function() {
		var $this = this;
		js.ajaxSubmit(ctxPath + '/account/getRegValidCode', {
			validType: $('#reg_validType').val(),
			corpCode_ : $('#reg_corpCode').val(),
			corpName_ : $('#reg_corpName').val(),
			loginCode : $('#reg_loginCode').val(),
			userName : $('#reg_userName').val(),
			email : $('#reg_email').val(),
			mobile : $('#reg_mobile').val(),
			userType: $('#reg_userType').val(),
			validCode : $('#reg_validCode').val()
		}, function(data){
			js.showMessage(data.message);
			if (data.result == 'true'){
				sendTime($this);
				$('#regValidCode').focus();
			}else{
				$('#reg_validCodeImg').click();
			}
		});
	});
	
	$('#registerForm').validate({
		ignore: ":hidden",
	    submitHandler: function(form) {
	    	var $form = $(form),
				action = $form.attr('action'),
				data = $form.serializeArray(),
				key = window.secretKey||$('#loginKey').data('key');
			if (key != ''){
				for (var i=0, l=data.length; i<l; i++){
					if (data[i].name == 'password'){
						data[i].value = DesUtils.encode($('#reg_password').val(), key);
					}
				}
			}
			js.ajaxSubmit(action, data, function(data, status, xhr){
				if (data.result == "true"){
					alert(data.message);
					location = ctx + '/login';
				}else{
					js.showMessage(data.message);
				}
			});
	    }
	});
	
	$('#btnReset').click(function(){
		location = ctx + '/login';
	});
	
});

