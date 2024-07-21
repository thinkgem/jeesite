(function ($) {
	if ($.validator.messages.errorMessage && $.validator.messages.errorMessage != ''){ // v4.1.8 可以设置为空不显示
		$("#inputForm .box-footer [class*=col-sm-offset]")
			.append("<div class=\"form-error\">"+$.validator.messages.errorMessage+"</div>");
	}
	$.extend($.validator.defaults, {
		ignore: ":hidden:not(.required),input.select2-focusser", // 忽略验证的class名称
		errorClass: 'has-error',
		errorContainer: ".form-error",
		errorPlacement: function(error, element) {
			if (element.closest('.icheck').length > 0) { 
				element = element.closest(".icheck");
				element.parent().css('position', 'relative');
	            error.insertAfter(element);
	            var pos = element.position1x();
	            error.css({'top': pos.top + element.outerHeight() + 2,
        			'left': pos.left + 5});
	            return;
	        }
			else if (element.next().hasClass('select2')) {
				element = element.next();
				element.parent().css('position', 'relative');
			}
			else if (element.closest('.input-group').length > 0) {
				element = element.closest('.input-group');
				element.parent().css('position', 'relative');
			}
            error.insertAfter(element);
            var pos = element.position1x();
    		error.css({'top': pos.top + element.outerHeight() - 5,
        			'left': pos.left + 5});
		},
		highlight: function (element) {
	        $(element).closest('.form-group').addClass('has-error');
	    },
	    unhighlight: function (element) {
	        $(element).closest('.form-group').removeClass('has-error');
	    },
	    success: function (label) {
	    	label.remove();
	    }
	});
	$.extend($.fn,{
		resetValid: function() {
			var element = $(this), errorLabel;
			if (element.closest('.icheck').length > 0) { 
				errorLabel = element.closest('.icheck').parent();
			}else if (element.closest('.input-group').size() > 0) {
				errorLabel = element.closest('.input-group').parent();
			}else{
				errorLabel = element.parent();
			}
			//log(errorLabel)
			element.closest('.has-error').removeClass('has-error');
			errorLabel.find('label.has-error').remove(); 
		}
	});
}(jQuery));

// 登录账号验证
jQuery.validator.addMethod("userName", function(value, element) {
 return this.optional(element) || /^[\u0391-\uFFE5\w]+$/.test(value);
}, $.validator.messages.userName);

// 真实姓名验证
jQuery.validator.addMethod("realName", function(value, element) {
    return this.optional(element) || /^[\u4e00-\u9fa5]{2,30}$/.test(value);
}, $.validator.messages.realName);

// 数字字母验证
jQuery.validator.addMethod("abc",function(value, element) {
	return this.optional(element) || /^[a-zA-Z0-9_]*$/.test(value);
}, $.validator.messages.abc);

// 数字字母验证（以字母开头）
jQuery.validator.addMethod("abc123",function(value, element) {
	return this.optional(element) || /^[a-zA-Z][a-zA-Z0-9_]*$/.test(value);
}, $.validator.messages.abc);

// 元素值比较验证
jQuery.validator.addMethod("noEqualTo",function(value, element, param) {
	return value != $(param).val();
}, $.validator.messages.noEqualTo);

// 手机号码验证
jQuery.validator.addMethod("mobile", function(value, element) {
	var tel = /^1[3,4,5,6,7,8,9]\d{9}$/g;
	return this.optional(element) || (tel.test(value));
}, $.validator.messages.mobile);

// 固话号码或手机号码验证  
jQuery.validator.addMethod("phone", function(value, element) {
	var tel = /(^0[1-9]{1}\d{8,10}$)|(^1[3,4,5,6,7,8,9]\d{9}$)/g;
	return this.optional(element) || (tel.test(value));
}, $.validator.messages.phone);	

//固话号码验证
jQuery.validator.addMethod("simplePhone", function(value, element) {
var tel = /^(\d{3,4}-?)?\d{7,9}$/g;
return this.optional(element) || (tel.test(value));
}, $.validator.messages.simplePhone);

// 邮政编码验证
jQuery.validator.addMethod("zipCode", function(value, element) {
    var tel = /^[0-9]{6}$/;
    return this.optional(element) || (tel.test(value));
}, $.validator.messages.zipCode);

// 整数验证（支持负整数）
$.validator.addMethod("integer", function( value, element ) {
	return this.optional( element ) || /^-?\d+$/.test( value );
}, $.validator.messages.integer);

// IP v4 验证
$.validator.addMethod("ipv4", function( value, element ) {
	return this.optional( element ) || /^(25[0-5]|2[0-4]\d|[01]?\d\d?)\.(25[0-5]|2[0-4]\d|[01]?\d\d?)\.(25[0-5]|2[0-4]\d|[01]?\d\d?)\.(25[0-5]|2[0-4]\d|[01]?\d\d?)$/i.test( value );
}, $.validator.messages.ipv4);

// IP v6 验证
$.validator.addMethod("ipv6", function( value, element ) {
	return this.optional( element ) || /^((([0-9A-Fa-f]{1,4}:){7}[0-9A-Fa-f]{1,4})|(([0-9A-Fa-f]{1,4}:){6}:[0-9A-Fa-f]{1,4})|(([0-9A-Fa-f]{1,4}:){5}:([0-9A-Fa-f]{1,4}:)?[0-9A-Fa-f]{1,4})|(([0-9A-Fa-f]{1,4}:){4}:([0-9A-Fa-f]{1,4}:){0,2}[0-9A-Fa-f]{1,4})|(([0-9A-Fa-f]{1,4}:){3}:([0-9A-Fa-f]{1,4}:){0,3}[0-9A-Fa-f]{1,4})|(([0-9A-Fa-f]{1,4}:){2}:([0-9A-Fa-f]{1,4}:){0,4}[0-9A-Fa-f]{1,4})|(([0-9A-Fa-f]{1,4}:){6}((\b((25[0-5])|(1\d{2})|(2[0-4]\d)|(\d{1,2}))\b)\.){3}(\b((25[0-5])|(1\d{2})|(2[0-4]\d)|(\d{1,2}))\b))|(([0-9A-Fa-f]{1,4}:){0,5}:((\b((25[0-5])|(1\d{2})|(2[0-4]\d)|(\d{1,2}))\b)\.){3}(\b((25[0-5])|(1\d{2})|(2[0-4]\d)|(\d{1,2}))\b))|(::([0-9A-Fa-f]{1,4}:){0,5}((\b((25[0-5])|(1\d{2})|(2[0-4]\d)|(\d{1,2}))\b)\.){3}(\b((25[0-5])|(1\d{2})|(2[0-4]\d)|(\d{1,2}))\b))|([0-9A-Fa-f]{1,4}::([0-9A-Fa-f]{1,4}:){0,5}[0-9A-Fa-f]{1,4})|(::([0-9A-Fa-f]{1,4}:){0,6}[0-9A-Fa-f]{1,4})|(([0-9A-Fa-f]{1,4}:){1,7}:))$/i.test( value );
}, $.validator.messages.ipv6);

// QQ号验证
jQuery.validator.addMethod("qq", function(value, element) {
    var tel = /^[1-9][0-9]{4,}$/;
    return this.optional(element) || (tel.test(value));
}, $.validator.messages.qq);

// 全空格验证
jQuery.validator.addMethod("isBlank", function(value, element) {
    var blank = /^[ |　]*$/;
    return this.optional(element) || !(blank.test(value));
}, $.validator.messages.isBlank);
 
// 校验身份证号码验证
jQuery.validator.addMethod("idcard", function(value, element) {
	//身份证号码验证工具
	var idCardNoUtil = {
		provinceAndCitys: {11:"北京",12:"天津",13:"河北",14:"山西",15:"内蒙古",21:"辽宁",22:"吉林",23:"黑龙江",
			31:"上海",32:"江苏",33:"浙江",34:"安徽",35:"福建",36:"江西",37:"山东",41:"河南",42:"湖北",43:"湖南",44:"广东",
			45:"广西",46:"海南",50:"重庆",51:"四川",52:"贵州",53:"云南",54:"西藏",61:"陕西",62:"甘肃",63:"青海",64:"宁夏",
			65:"新疆",71:"台湾",81:"香港",82:"澳门",91:"国外"},
		powers: ["7","9","10","5","8","4","2","1","6","3","7","9","10","5","8","4","2"],
		parityBit: ["1","0","X","9","8","7","6","5","4","3","2"],
		genders: {male:"男",female:"女"},
		checkAddressCode: function(addressCode){
			var check = /^[1-9]\d{5}$/.test(addressCode);
			if(!check) return false;
			if(idCardNoUtil.provinceAndCitys[parseInt(addressCode.substring(0,2))]){
				return true;
			}else{
				return false;
			}
		},
		checkBirthDayCode: function(birDayCode){
			var check = /^[1-9]\d{3}((0[1-9])|(1[0-2]))((0[1-9])|([1-2][0-9])|(3[0-1]))$/.test(birDayCode);
			if(!check) return false;
			var yyyy = parseInt(birDayCode.substring(0,4),10);
			var mm = parseInt(birDayCode.substring(4,6),10);
			var dd = parseInt(birDayCode.substring(6),10);
			var xdata = new Date(yyyy,mm-1,dd);
			if(xdata > new Date()){
				return false;// 生日不能大于当前日期
			}else if ( ( xdata.getFullYear() == yyyy ) && ( xdata.getMonth () == mm - 1 ) && ( xdata.getDate() == dd ) ){
				return true;
			}else{
				return false;
			}
		},
		getParityBit: function(idCardNo){
			var id17 = idCardNo.substring(0,17);
	
			var power = 0;
			for(var i=0;i<17;i++){
				power += parseInt(id17.charAt(i),10) * parseInt(idCardNoUtil.powers[i]);
			}
	
			var mod = power % 11;
			return idCardNoUtil.parityBit[mod];
		},
		checkParityBit: function(idCardNo){
			var parityBit = idCardNo.charAt(17).toUpperCase();
			if(idCardNoUtil.getParityBit(idCardNo) == parityBit){
				return true;
			}else{
				return false;
			}
		},
		checkIdCardNo: function(idCardNo){
			//15位和18位身份证号码的基本校验
			var check = /^\d{15}|(\d{17}(\d|x|X))$/.test(idCardNo);
			if(!check) return false;
			//判断长度为15位或18位
			if(idCardNo.length==15){
				return idCardNoUtil.check15IdCardNo(idCardNo);
			}else if(idCardNo.length==18){
				return idCardNoUtil.check18IdCardNo(idCardNo);
			}else{
				return false;
			}
		},
	
		//校验15位的身份证号码
		check15IdCardNo: function(idCardNo){
			//15位身份证号码的基本校验
			var check = /^[1-9]\d{7}((0[1-9])|(1[0-2]))((0[1-9])|([1-2][0-9])|(3[0-1]))\d{3}$/.test(idCardNo);
			if(!check) return false;
			//校验地址码
			var addressCode = idCardNo.substring(0,6);
			check = idCardNoUtil.checkAddressCode(addressCode);
			if(!check) return false;
			var birDayCode = '19' + idCardNo.substring(6,12);
			//校验日期码
			return idCardNoUtil.checkBirthDayCode(birDayCode);
		},
	
		//校验18位的身份证号码
		check18IdCardNo: function(idCardNo){
			//18位身份证号码的基本格式校验
			var check = /^[1-9]\d{5}[1-9]\d{3}((0[1-9])|(1[0-2]))((0[1-9])|([1-2][0-9])|(3[0-1]))\d{3}(\d|x|X)$/.test(idCardNo);
			if(!check) return false;
			//校验地址码
			var addressCode = idCardNo.substring(0,6);
			check = idCardNoUtil.checkAddressCode(addressCode);
			if(!check) return false;
			//校验日期码
			var birDayCode = idCardNo.substring(6,14);
			check = idCardNoUtil.checkBirthDayCode(birDayCode);
			if(!check) return false;
			//验证校检码
			return idCardNoUtil.checkParityBit(idCardNo);
		},
	
		formateDateCN: function(day){
			var yyyy =day.substring(0,4);
			var mm = day.substring(4,6);
			var dd = day.substring(6);
			return yyyy + '-' + mm +'-' + dd;
		},
	
		//	获取信息
		getIdCardInfo: function(idCardNo){
			var idCardInfo = {
					gender:"", // 性别
					birthday:"" // 出生日期(yyyy-mm-dd)
			};
			if(idCardNo.length==15){
				var aday = '19' + idCardNo.substring(6,12);
				idCardInfo.birthday=idCardNoUtil.formateDateCN(aday);
				if(parseInt(idCardNo.charAt(14))%2==0){
					idCardInfo.gender=idCardNoUtil.genders.female;
				}else{
					idCardInfo.gender=idCardNoUtil.genders.male;
				}
			}else if(idCardNo.length==18){
				var aday = idCardNo.substring(6,14);
				idCardInfo.birthday=idCardNoUtil.formateDateCN(aday);
				if(parseInt(idCardNo.charAt(16))%2==0){
					idCardInfo.gender=idCardNoUtil.genders.female;
				}else{
					idCardInfo.gender=idCardNoUtil.genders.male;
				}
	
			}
			return idCardInfo;
		},
		getId15:function(idCardNo){
			if(idCardNo.length==15){
				return idCardNo;
			}else if(idCardNo.length==18){
				return idCardNo.substring(0,6) + idCardNo.substring(8,17);
			}else{
				return null;
			}
		},
		getId18: function(idCardNo){
			if(idCardNo.length==15){
				var id17 = idCardNo.substring(0,6) + '19' + idCardNo.substring(6);
				var parityBit = idCardNoUtil.getParityBit(id17);
				return id17 + parityBit;
			}else if(idCardNo.length==18){
				return idCardNo;
			}else{
				return null;
			}
		}
	};
	return this.optional(element) || idCardNoUtil.checkIdCardNo(value);
}, $.validator.messages.idcard);
