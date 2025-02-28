/*!
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 * No deletion without permission, or be held responsible to law.
 * @author ThinkGem
 * @version 2019-1-6
 */
$(function(){
	
	$('#fp_validType').change(function(){
		var val = $(this).val(), action = '';
		$('.fp-element').addClass('hide').removeClass('block');
		$('.fp-'+val).addClass('block').removeClass('hide');
		setTimeout(function(){
			$('#fp_loginCode').focus();
		}, 100);
		if (val == 'mobile' || val == 'email'){
			var txt = (val == 'mobile' ? '手机' : '邮箱')
			$('#fpValidCode').attr('placeholder', txt+'验证码')
				.attr('data-msg-required', '请填写'+txt+'验证码.');
			$('#sendFpValidCode').val('获取'+txt+'验证码');
			action = ctxPath + '/account/savePwdByValidCode';
		}else if(val == 'question'){
			action = ctxPath + '/account/savePwdByPwdQuestion';
		}
		$('#forgetForm').attr('action', action);
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
	
	$('#sendFpValidCode').click(function() {
		var $this = this;
		js.ajaxSubmit(ctxPath + '/account/getFpValidCode', {
			validType: $('#fp_validType').val(),
			loginCode : $('#fp_loginCode').val(),
			validCode : $('#fp_validCode').val(),
			corpCode : $('#fp_corpCode').val()
		}, function(data){
			js.showMessage(data.message);
			if (data.result == 'true'){
				sendTime($this);
				$('#fpValidCode').focus();
			}else{
				$('#fp_validCodeImg').click();
			}
		});
	});
	
	$('#fp_getQuestion').click(function() {
		js.ajaxSubmit(ctxPath + '/account/getPwdQuestion', {
			loginCode : $('#fp_loginCode').val(),
			validCode : $('#fp_validCode').val(),
			corpCode : $('#fp_corpCode').val()
		}, function(data){
			js.showMessage(data.message);
			if (data.result == 'true'){
				$('#fp_q1').text(data.pwdQuestion);
				$('#fp_q2').text(data.pwdQuestion2);
				$('#fp_q3').text(data.pwdQuestion3);
			}
		});
	});

	if ($.fn.strength){
		$("#fp_password").strength();
	}
	
	$('#forgetForm').validate({
		ignore: ":hidden",
	    submitHandler: function(form) {
	    	var $form = $(form),
				action = $form.attr('action'),
				data = $form.serializeArray(),
				key = window.secretKey||$('#loginKey').data('key');
			if (key != ''){
				for (var i=0, l=data.length; i<l; i++){
					if (data[i].name == 'pwdQuestionAnswer'){
						data[i].value = DesUtils.encode($('#fp_pwdQuestionAnswer').val(), key);
					}else if (data[i].name == 'pwdQuestionAnswer2'){
						data[i].value = DesUtils.encode($('#fp_pwdQuestionAnswer2').val(), key);
					}else if (data[i].name == 'pwdQuestionAnswer3'){
						data[i].value = DesUtils.encode($('#fp_pwdQuestionAnswer3').val(), key);
					}else if (data[i].name == 'password'){
						data[i].value = DesUtils.encode($('#fp_password').val(), key);
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

