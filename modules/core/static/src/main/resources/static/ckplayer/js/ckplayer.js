/*
 * 软件名称：ckplayer
 * 版本:X3
 * 版权：www.ckplayer.com
 * 开源协议：MIT
 */
(function (global, factory) {
  typeof exports === 'object' && typeof module !== 'undefined' ? module.exports = factory() :
  typeof define === 'function' && define.amd ? define(factory) :
  (global = global || self, global.ckplayer = factory());
}(this, function () { 'use strict';
	/*
	 * language
	 * 功能：静态变量,包含播放器用到的默认语言包
	 * 引入其它语言js后该变量将被替换成新引入的语言包
	 */
	var language={
		play:'%E6%92%AD%E6%94%BE',
		pause:'%E6%9A%82%E5%81%9C',
		refresh:'%E9%87%8D%E6%92%AD',
		full:'%E5%85%A8%E5%B1%8F',
		exitFull:'%E9%80%80%E5%87%BA%E5%85%A8%E5%B1%8F',
		webFull:'%E9%A1%B5%E9%9D%A2%E5%85%A8%E5%B1%8F',
		exitWebFull:'%E9%80%80%E5%87%BA%E9%A1%B5%E9%9D%A2%E5%85%A8%E5%B1%8F',
		theatre:'%E5%89%A7%E5%9C%BA%E6%A8%A1%E5%BC%8F',
		exitTheatre:'%E9%80%80%E5%87%BA%E5%89%A7%E5%9C%BA%E6%A8%A1%E5%BC%8F',
		volume:'%E9%9F%B3%E9%87%8F%EF%BC%9A',
		muted:'%E9%9D%99%E9%9F%B3',
		exitmuted:'%E6%81%A2%E5%A4%8D%E9%9F%B3%E9%87%8F',
		seek:'seek%EF%BC%9A',
		waiting:'%E7%BC%93%E5%86%B2',
		live:'%E7%9B%B4%E6%92%AD%E4%B8%AD',
		backLive:'%E8%BF%94%E5%9B%9E%E7%9B%B4%E6%92%AD',
		lookBack:'%E5%9B%9E%E7%9C%8B%EF%BC%9A',
		next:'%E4%B8%8B%E4%B8%80%E9%9B%86',
		screenshot:'%E8%A7%86%E9%A2%91%E6%88%AA%E5%9B%BE',
		smallwindows:'%E5%B0%8F%E7%AA%97%E5%8F%A3%E6%92%AD%E6%94%BE%E5%8A%9F%E8%83%BD',
		playbackrate:'%E5%80%8D%E9%80%9F',
		playbackrateSuffix:'%E5%80%8D',
		track:'%E5%AD%97%E5%B9%95',
		noTrack:'%E6%97%A0%E5%AD%97%E5%B9%95',
		definition:'%E6%B8%85%E6%99%B0%E5%BA%A6',
		switchTo:'%E5%88%87%E6%8D%A2%E6%88%90%EF%BC%9A',
		closeTime:'%7Bseconds%7D%E7%A7%92%E5%90%8E%E5%8F%AF%E5%85%B3%E9%97%AD%E5%B9%BF%E5%91%8A',
		closeAd:'%E5%85%B3%E9%97%AD%E5%B9%BF%E5%91%8A',
		second:'%E7%A7%92',
		details:'%E6%9F%A5%E7%9C%8B%E8%AF%A6%E6%83%85',
		copy:'%E5%A4%8D%E5%88%B6',
		copySucceeded:'%E5%A4%8D%E5%88%B6%E6%88%90%E5%8A%9F%EF%BC%8C%E5%8F%AF%E8%B4%B4%E7%B2%98%EF%BC%81',
		smallwindowsOpen:'%E5%B0%8F%E7%AA%97%E5%8F%A3%E5%8A%9F%E8%83%BD%E5%B7%B2%E5%BC%80%E5%90%AF',
		smallwindowsClose:'%E5%B0%8F%E7%AA%97%E5%8F%A3%E5%8A%9F%E8%83%BD%E5%B7%B2%E5%85%B3%E9%97%AD',
		screenshotStart:'%E6%88%AA%E5%9B%BE%E4%B8%AD%EF%BC%8C%E8%AF%B7%E7%A8%8D%E5%80%99...',
		screenshotClose:'%E6%88%AA%E5%9B%BE%E5%8A%9F%E8%83%BD%E5%B7%B2%E5%85%B3%E9%97%AD',
		loopOpen:'%E5%BE%AA%E7%8E%AF%E6%92%AD%E6%94%BE',
		loopClose:'%E5%B7%B2%E5%85%B3%E9%97%AD%E5%BE%AA%E7%8E%AF%E6%92%AD%E6%94%BE',
		close:'%E5%85%B3%E9%97%AD',
		down:'%E4%B8%8B%E8%BD%BD',
		p50:'50%25',
		p75:'75%25',
		p100:'100%25',
		timeScheduleAdjust:{
			prohibit:'%E8%A7%86%E9%A2%91%E7%A6%81%E6%AD%A2%E6%8B%96%E5%8A%A8',
			prohibitBackOff:'%E8%A7%86%E9%A2%91%E7%A6%81%E6%AD%A2%E9%87%8D%E5%A4%8D%E8%A7%82%E7%9C%8B',
			prohibitForward:'%E8%A7%86%E9%A2%91%E7%A6%81%E6%AD%A2%E5%BF%AB%E8%BF%9B',
			prohibitLookBack:'%E8%A7%86%E9%A2%91%E7%A6%81%E6%AD%A2%E6%92%AD%E6%94%BE%E9%83%A8%E5%88%86%E5%86%85%E5%AE%B9',
			prohibitForwardNotViewed:'%E8%A7%86%E9%A2%91%E7%A6%81%E6%AD%A2%E6%92%AD%E6%94%BE%E6%9C%AA%E8%A7%82%E7%9C%8B%E7%9A%84%E9%83%A8%E5%88%86'
		},
		error:{
			noMessage:'%E6%9C%AA%E7%9F%A5%E9%94%99%E8%AF%AF',
			supportVideoError:'%E8%AF%A5%E6%B5%8F%E8%A7%88%E5%99%A8%E7%89%88%E6%9C%AC%E5%A4%AA%E4%BD%8E%EF%BC%8C%E5%BB%BA%E8%AE%AE%E6%9B%B4%E6%8D%A2%E6%88%90%E5%85%B6%E5%AE%83%E6%B5%8F%E8%A7%88%E5%99%A8',
			videoTypeError:'%E8%AF%A5%E6%B5%8F%E8%A7%88%E5%99%A8%E4%B8%8D%E6%94%AF%E6%8C%81%E6%92%AD%E6%94%BE%E8%AF%A5%E8%A7%86%E9%A2%91%EF%BC%8C%E5%BB%BA%E8%AE%AE%E6%9B%B4%E6%8D%A2%E6%88%90%E5%85%B6%E5%AE%83%E6%B5%8F%E8%A7%88%E5%99%A8',
			loadingFailed:'%E5%8A%A0%E8%BD%BD%E5%A4%B1%E8%B4%A5',
			emptied:'%E8%A7%86%E9%A2%91%E6%96%87%E4%BB%B6%E5%8A%A0%E8%BD%BD%E8%BF%87%E7%A8%8B%E4%B8%AD%E5%87%BA%E7%8E%B0%E9%94%99%E8%AF%AF',
			screenshot:'%E8%A7%86%E9%A2%91%E6%88%AA%E5%9B%BE%E5%A4%B1%E8%B4%A5',
			ajax:'Ajax%E6%95%B0%E6%8D%AE%E8%AF%B7%E6%B1%82%E9%94%99%E8%AF%AF',
			noVideoContainer:'%E6%9C%AA%E6%89%BE%E5%88%B0%E6%94%BE%E7%BD%AE%E8%A7%86%E9%A2%91%E7%9A%84%E5%AE%B9%E5%99%A8'
		}
	};
	/*
	 * videoObjectDefault
	 * 功能：静态变量,默认播放配置，当外部传递过来的配置有未包含在varsDefault里的，则使用varsDefault里的配置
	 */
	var videoObjectDefault= {
		container: '',//视频容器的ID
		volume: 0.8,//默认音量，范围0-1
		poster: '',//封面图片地址
		autoplay: false,//是否自动播放
		loop: false,//是否需要循环播放
		live: false,//是否是直播
		rotate:0,//视频旋转角度
		zoom:0,//默认缩放比例
		ad:null,//广告
		backLive:false,//显示返回直播按钮
		seek: 0,//默认需要跳转的秒数
		next: null,//下一集按钮动作
		loaded: '',//加载播放器后调用的函数
		plug: '',//使用插件
		duration:0,//视频总时间
		preview: null,//预览图片对象
		prompt: null,//提示点功能
		crossOrigin:'',//跨域请求字符
		video: null,//视频地址
		type:'',//视频类型
		playbackrate: 1,//默认倍速
		ended:null,//结束显示的内容
		webFull:false,//页面全屏按钮事件
		theatre:null,//剧场模式
		controls:false,//是否显示自带控制栏
		rightBar:null,//是否开启右边控制栏
		smallWindows:null,//是否启用小窗口模式
		smallWindowsDrag:true,//当处于小窗口模式时是否可拖动播放器
		screenshot:false,//截图功能是否开启
		timeScheduleAdjust:1,//是否可调节播放进度,0不启用，1是启用，2是只能前进（向右拖动），3是只能后退，4是只能前进但能回到第一次拖动时的位置，5是看过的地方可以随意拖动
		logo:'',//logo
		menu:null,//右键菜单
		information:{//关于
			'Load:':'{loadTime} second',
			'Duration:':'{duration} second',
			'Size:':'{videoWidth}x{videoHeight}',
			'Volume:':'{volume}%',
			'Fps:':'{fps}fps',
			'Sudio decoded:':'{audioDecodedByteCount} Byte',
			'Video decoded:':'{videoDecodedByteCount} Byte'
		},
		track:null,//字幕
		title:'',//视频标题
		language:'',//语言包文件
		barHideTime:1500,//控制栏隐藏时间
		playbackrateOpen:true,//是否开启控制栏倍速选项
		playbackrateList:[0.75,1,1.25,1.5,2,4],//倍速配置值
		cookie:null,//开启cookie功能
		domain:null,//指定cookie保存的域
		cookiePath:'/',//指定cookie保存路径
		documentFocusPause:false,//窗口失去焦点后暂停播放
		mouseWheelVolume:2,//是否启用鼠标滚轮调节音量功能，0=不启用，1=启用，2=全屏时才启用
		keyVolume:2,//是否启用键盘控制音量调节，0=不启用，1=启用，2=全屏时才启用
		errorShow:true//是否显示错误信息
	};
	function ckplayerEmbed(videoObj){
		/*
		 * rightMenu
		 * 功能：全局变量,右键内容
		 * 初始化设置menu可替换该变量内容
		 */
		var rightMenu=[
			{
				title:'ckplayer',
				link:'http://www.ckplayer.com'			
			},
			{
				title:'version:X3',
				underline:true
			},
			{
				title:'about',
				click:'aboutShow'
			}
		];
		var vars={},varsTemp={};//保存传递过来的videoObject
		var video=null;//视频播放器对象
		var duration=0;//总时间
		var mutedState='';//默认静音状态
		var recoveryVolume=false;//是否需要在播放时恢复音量
		var waited=true;//是否缓冲结束
		var paused=true;//默认暂停状态
		var loadTime=0;//已加载部分
		var seekTime=0;//需要跳转的时间，初次播放以及切换清晰度后会用该变量记录需要seek的时间
		var oldTime=0,playTime=0,firstSeekTime=-1,maxSeeTime=0;//oldTime=记录上次播放时间，playTime=当前播放时间，firstSeekTime=记录第一次拖动的时间,maxSeeTime=看过的最大时间
		var isChangeDef=true;//是否需要重置清晰度
		var playType='';//播放类别，默认=''，是通过api接口播放，='button'则判定是通过点击按钮播放
		var msgSetTime=null,tipSetTime=null,mouseSetTime=null;//用于显示提示后自动隐藏的计时器
		var adFrontSetTime=null,adPauseSetTime=null;//贴片广告和暂停广告的计时器
		var closeTipFun=null;//关闭提示的函数
		var closeTipMouseOut=null;//鼠标离开节点时执行的函数
		var waitingMessage=true;//显示缓冲提示
		var hidePreviewSetTime=null;//隐藏预览图的计时器
		var pSliderMouseDown=false;//判断是否在进度条上的滑块上按下
		var playbackTime=0;//记录回放时间
		var loadedmetadataNum=0;//记录元数据加载成功次数，记录视频播放数量
		var screenshotImg=null;//记录最新一张截图
		var ad=null;//广告对象
		var frontAdPlay=false,pauseAdPlay=false;//贴片广告是否播放，暂停广告是否播放
		var barShow=true,rightBarShow=true;
		var EventTarget=null,newEvent=null,eventTargetList=null;//注册监听
		var loadedTrack=false;//默认未加载字幕
		var C={};//保存播放器界面上的所有节点
		var CT=null,CK=null,CM=null,CV=null;//CT=播放器容器，页面中已存的，CT>CK>CM>CV>video
		var pugPlayer=null;//插件播放器
		var hls=null;//播放hls
		var loadMeta=true;//第一次加载到元数据
		var app='';//平台类型
		var nowRotate=0;//当前视频旋转角度
		var nowZoom=100;//当前缩放比例
		var smallWindowsState=false;//当前是否处理小窗口状态
		var isDrag=false;//是否在播放器上按下并且拖动
		var ckplayerCookie='ckplayer-player-cookie';
		var cookieName='';//cookie名称
		var cookieTime=0;//cookie保存时间，单位：秒
		var cookieArray=[];//保存当前所有记录
		var focusPause=true;//失去焦点前是否是暂停状态
		var existenceObj=false;//是否使用源码里已有的dom
		/*
		 * into
		 * 功能：初始化，调用播放器则首先调用该函数
		 * @obj=初始化时的配置对象：videoObject
		*/
		var into=function(obj){
			/*
			 * 如果未传递初始化配置对象，则为player变量增加一个add函数，功能相当于into
			 */
			if(isUndefined(obj)){
				player.add=player.into=into;
				return player;
			}
			/*
			 * 转码默认语言包
			 */
			language=decodeURIString(language);
			/*
			 * 检查是否在顶部引入了外部语言包文件，如果引入了，则使用外部语言包替换进language变量
			 */
			if(!isUndefined(window.ckplayerLanguage)){
				language=mergeObj(language,window.ckplayerLanguage);
			}
			/*
			 * 初始化注册事件函数，该函数的作用是将所有相关的事件都注册进一个变量newEvent
			 * newEvent是提供给播放器外部监听函数使用的，如监听时间player.time(function(t){console.log('已播放：'+t);});
			 */
			eventTarget();
			/*
			 * 监听页面标签状态（判断是否失去焦点）
			 */
			documentHidden(function(state){
				eventTarget('visibilityState',state);
			});
			/*
			 * 判断平台类型,主要是判断是否是iphone类型的平台
			 */
			app=getApp();
			/*
			 * 简单复制一下初始化时的默认配置
			 */
			varsTemp={};
			for(var key in videoObjectDefault){
				varsTemp[key]=videoObjectDefault[key];
			}
			/*
			 * 如果初始化配置是字符串并且是以website:开头或url:开头的，则认为需要请求一个json文件来获取配置
			 */
			if(valType(obj)=='string' && (obj.substr(0,8)=='website:' || obj.substr(0,4)=='url:')){
				var ajaxUrl='';
				if(obj.substr(0,8)=='website:'){
					ajaxUrl=obj.substring(8);
				}
				if(obj.substr(0,4)=='url:'){
					ajaxUrl=obj.substring(4);
				}
				ajax({url:ajaxUrl,success:function(data){
					if(data){
						return into(data);
					}
					else{
						showWindowsError(language['error']['ajax']+',url:'+ajaxUrl);
						return null;
					}
				}});
			}
			/*
			 * 如果初始化配置是一个object，则进行下面的操作
			 */
			else if(valType(obj)=='object'){
				varsTemp = standardization(varsTemp, obj);//将obj合并到varsTemp对象里
				if(!isUndefined(obj['container']) && obj['container']){
					CT=$(obj['container']);
					if(CT){//如果播放容器存在，则调用语言判断函数
						return loadLanguage(obj);
					}
					else{//如果播放容器不存在，则则等页面加载完成后运行
						return bodyReady(obj);
					}
				}
				else{//不存在播放容器配置则等页面加载完成后运行
					return bodyReady(obj);
				}			
			}
			else{
				showWindowsError(language['error']['noVideoContainer']);
				return null;
			}
		},
		/*
		 * bodyReady
		 * 功能：页面加载完成后构建播放器
		 * @obj=初始化时的配置对象
		*/
		bodyReady=function (obj){
			documentReady(function(){
				if(!isUndefined(obj['container'])){
					CT=$(obj['container']);
					if(CT){//如果播放容器存在，则调用语言判断函数
						return loadLanguage(obj);
					}
					else{
						showWindowsError(language['error']['noVideoContainer']);
						return null;
					}
				}
				else{//不存在播放容器则重新判断
					return ajaxWebsite(obj);
				}
			});
			return null;
		},
		/*
		 * ajaxWebsite
		 * 功能：页面加载完成后重新初始化
		 * @obj=初始化时的配置对象
		 */
		ajaxWebsite=function (obj){
			if(valType(obj['video'])=='string' && (obj['video'].substr(0,8)=='website:' || obj['video'].substr(0,4)=='url:')){
				varsTemp = standardization(varsTemp, obj);
				var ajaxUrl='';
				if(obj['video'].substr(0,8)=='website:'){
					ajaxUrl=obj['video'].substring(8);
				}
				if(obj['video'].substr(0,4)=='url:'){
					ajaxUrl=obj['video'].substring(4);
				}
				ajax({url:ajaxUrl,success:function(data){
					if(data){
						return into(data);
					}
					else{
						showWindowsError(language['error']['ajax']+',url:'+ajaxUrl);
						return null;
					}
				}});
			}
			else{
				showWindowsError(language['error']['noVideoContainer']);
				return null;
			}
		},
		/*
		 * loadLanguage
		 * 功能：加载Language
		 * @obj=初始化时的配置对象
		*/
		loadLanguage=function (obj){
			vars = standardization(varsTemp, obj);
			if(vars['language']){
				var path=getPath('language')+vars['language']+'.js';
				loadJs(path,function(){
					if(!isUndefined(window['ckplayerLanguage'])){
						language=mergeObj(language,window['ckplayerLanguage']);
					}
					return embed(obj);
				});
			}
			else{
				return embed(obj);
			}
		},
		/*
		 * embed
		 * 功能：构建播放器
		 * @obj=初始化时的配置对象
		*/
		embed=function (obj){
			/*
			 * 判断静音状态
			 */
			mutedState=vars['volume']>0?false:true;
			if(!isUndefined(CT) && CT!=null){
				if(valType(CT)=='htmlarray'){
					CT=CT.eq(0);
				}
			}
			else{
				return null;
			}
			/*
			 * 注册获取fps的函数
			 */
			calculationFps();
			/*
			 * 如果video指向一个已存在的video标签对象
			 */
			existenceObj=valType(vars['video'])=='string' && ((vars['video'].substr(0,1)=='.' && vars['video'].indexOf('/')==-1) || vars['video'].substr(0,1)=='#');
			/*
			 * 清空容器
			 */
			if(existenceObj){
				CK=CT.find('.ckplayer-ckplayer')?CT.find('.ckplayer-ckplayer').eq(0):null;
				if(CK){
					CM=CK.find('.ck-main')?CK.find('.ck-main').eq(0):null;
				}
				if(CM){
					CV=CM.find('.ck-video')?CM.find('.ck-video').eq(0):null;
				}
			}
			if(!CK || !CM || !CV){
				CT.htm('');
				/*
				 * 在播放容器里新建一个总的容器
				 */
				CK=createlDiv('ckplayer-ckplayer');
				CT.append(CK);
				/*
				 * 在总容器里再新建一个容器
				 */
				CM=createlDiv('ck-main');
				CK.append(CM);
				/*
				 * 新建一个放置video标签的容器
				 */
				CV=createlDiv('ck-video');
				CM.append(CV);
			}
			/*
			 * 如果已存在video.则先设置成空
			 */
			if(video){
				video.remove();
				video=null;
			}
			/*
			 * 如果video指向一个已存在的video标签对象，则直接调用该对象
			 */
			if(existenceObj){
				video=$(vars['video'])?$(vars['video']).eq(0):null;
			}
			/*
			 * 新建一个video标签
			 */
			if(!video){
				video = createlVideo();
				video.attr('width','100%').attr('height','100%');
				CV.append(video);
			}
			video.volume=vars['volume'];
			if(vars['autoplay']){
				player.volume(0);
				player.muted();
				recoveryVolume=true;
				video.attr('autoplay','autoplay');
				paused=false;
			}
			else{
				video.attr('preload','metadata');
			}
			CT.loop=vars['loop'];
			if(CT.loop){
				video.attr('loop','loop');
			}
			if(vars['controls']){
				video.controls=true;
			}
			else{
				video.controls=false;
			}
			try{
				if(!existenceObj){
					video.attr('controlslist','nodownload');
					video.attr('x-webkit-airplay','true');
					video.attr('x5-video-orientation','portraint');
					video.attr('playsinline','true');
					video.attr('webkit-playsinline','true');
					video.attr('x5-playsinline','true');
				}
				if(vars['crossOrigin']){
					video.useCORS=true;//解决跨域
		     		video.crossOrigin=vars['crossOrigin'];//解决跨域
				}
			}
			catch(event){}
			/*
			 * 默认设置支持小窗口模式
			 */
			CT.smallWindows=true;
			/*
			 * 默认设置不显示页面全屏按钮
			 */
			CT.webFull=false;
			/*
			 * 默认设置不显示剧场模式按钮
			 */
			CT.theatre=false;
			/*
			 * 加载播放器界面
			 */
			loadFace();
			/*
			 * 判断是否需要默认旋转视频
			 */
			if(vars['rotate']>0){
				player.rotate(vars['rotate']);
			}
			/*
			 * 判断是否需要默认缩放视频
			 */
			if(vars['zoom']>0){
				player.zoom(vars['zoom']);
			}
			/*
			 * 判断是否支持video标签
			 */
			if(isUndefined(video.canPlayType)){
				CT.error={code:5,message:language['error']['supportVideoError']};
				eventTarget('error',CT.error);//注册监听error
				showError();
				return player;
			}
			/*
			 * 注册视频播放器内部监听，监听到事件后注册给CT使用
			 */
			addAllListener();
			/*
			 * 如果需要在视频播放器加载成功后调用相关函数，此时则开始调用
			 */
			if(!isUndefined(vars['loaded'])){
				if(valType(vars['loaded'])=='function'){
					try{
						vars['loaded'](player);
					}
					catch(event){}
				}
				else if(valType(vars['loaded'])=='string'){
					try{
						eval(vars['loaded']+ '(player)');
					}
					catch(event){}
				}
			}
			/*
			 * 播放视频
			 */
			if(!existenceObj){
				changeVideo(vars['video']);
			}
			else{
				changeVideo(video.attr('src'));
			}
			/*
			 * 返回 播放器
			 */
			return player;
		},
		/*
		 * changeVideo
		 * 功能：初始化视频地址或修改视频地址
		*/
		changeVideo=function (vstr){
			var i=0;
			if(video.attr('src') || video.htm()){
				player.pause();
			}
			if(video.attr('src') && !existenceObj){
				video.attr('src','');
				video.removeAttr('src');
			}
			if(!isUndefined(video.textTracks) && video.textTracks.length>0){
				for(i=video.find('track').length-1;i>-1;i--){
					video.find('track').eq(i).remove();
				}
			}
			if(!existenceObj){
				video.htm('');
			}
			if(!isUndefined(vars['ad'])){
				ad=vars['ad'];
			}
			var source='';
			if(valType(vstr)=='string'){
				if(vstr.substr(0,8)=='website:' || vstr.substr(0,4)=='url:'){
					var ajaxUrl='';
					if(vstr.substr(0,8)=='website:'){
						ajaxUrl=vstr.substring(8);
					}
					if(vstr.substr(0,4)=='url:'){
						ajaxUrl=vstr.substring(4);
					}
					ajax({url:ajaxUrl,success:function(data){
						if(data!=null){
							vars = standardization(varsTemp, data);
							changeVideo(vars['video']);
						}
						else{
							CT.error={code:8,message:language['error']['ajax']};
							eventTarget('error',CT.error);//注册监听error
							showError();
						}
					}});
					return;
				}
				else{
					if(vars['plug'] && !canPlay(vstr)){
						plugPlayer(vstr);
					}
					else{
						if(!existenceObj){
							video.attr('src',vstr);
						}
					}
					loadTrack();
				}
			}
			else if(valType(vstr)=='array'){
				for(i=0;i<vstr.length;i++){
					if(valType(vstr[i])=='array'){
						if(vars['plug'] && !canPlay(vstr[i][0])){
							plugPlayer(vstr[i][0]);
						}
						else{
							var type=' type="'+ vstr[i][1]+'"';
							if(vstr[i].length>1){
								source += '<source src="' + decodeURIComponent(vstr[i][0]) + '"' +type + '>';
							}
						}
					}
				}
				if(source){
					video.htm(source);
				}
				loadTrack();
			}
			else if(valType(vstr)=='object'){
				if(vars['plug']){
					plugPlayer(vstr);
				}
				else{
					CT.error={code:10,message:language['error']['emptied']};
					eventTarget('error',CT.error);//注册监听error
					showError();
				}
				loadTrack();
			}
			if(!isUndefined(video.find('source')) && video.find('source').length>0){
				video.find('source').eq(video.find('source').length-1).addListener('error',videoHandler.error);
			}
			checkBar();
			loadLogo();
			if(isChangeDef){
				checkDefinition();
			}
			/*
			 * 对cookie进行相关分析
			*/
			if(vars['cookie']){
				if(valType(vars['cookie'])=='array' && vars['cookie'].length>=2){
					if(vars['cookie'][0] && valType(vars['cookie'][0])=='string'){
						cookieName=vars['cookie'][0];
					}
					if(vars['cookie'][1] && valType(vars['cookie'][1])=='number'){
						cookieTime=vars['cookie'][1];
					}
				}
				if(valType(vars['cookie'])=='object' && !isUndefined(vars['cookie']['name'])){
					cookieName=vars['cookie']['name'];
					if(!isUndefined(vars['cookie']['hour']) && valType(vars['cookie']['hour'])=='number'){
						cookieTime=vars['cookie']['hour'];
					}
				}
				if(valType(vars['cookie'])=='string'){
					cookieName=vars['cookie'];
				}
				if(cookieName){
					cookieName=cookieName.replace(/[ ]*,[ ]*|[ ]+/g, '').replace(/[ ]*;[ ]*|[ ]+/g, '');
				}
			}
			if(!isUndefined(vars['seek']) && !seekTime){
				if(valType(vars['seek'])=='number' && vars['seek']>0){
					seekTime=vars['seek'];
				}
				if(valType(vars['seek'])=='string' && vars['seek']=='cookie' && cookieName){
					var cke=player.cookie(cookieName);
					if(cke){
						seekTime=cke['time'];
					}
					
				}
			}
		},
		/*
		 * plugPlayer
		 * 功能：使用插件进行播放
		*/
		plugPlayer=function (url){
			switch(vars['plug']){
				case 'hls.js':
					hlsPlayer(url);
					break;
				case 'flv.js':
					flvPlayer(url);
					break;
				case 'mpegts.js':
					mpegtsPlayer(url);
					break;
				case 'dash.js':
					dashPlayer(url);
					break;	
				default:
					if(valType(vars['plug'])=='function'){
						vars['plug'](video,url);
					}
					else{
						CT.error={code:10,message:language['error']['emptied']};
						eventTarget('error',CT.error);//注册监听error
						showError();
					}
					
					break;
			}
		},
		/*
		 * hlsPlayer
		 * 功能：使用hls.js插件进行播放
		*/
		hlsPlayer=function (url){
			var path=getPath('hls.js')+'hls.min.js';
			loadJs(path,function(){
				if (!isUndefined(Hls) && Hls.isSupported()) {
					hls = new Hls();
					hls.loadSource(url);
					hls.attachMedia(video);
					hls.on(Hls.Events.ERROR, function(event, data){
						var code=9;
						var message=language['error']['loadingFailed'];
						var fatal=data.fatal;
						if(!isUndefined(data.response)){
							if(!isUndefined(data.response.code)){
								code=data.response.code;
							}
							if(!isUndefined(data.response.text) && data.response.text){
								message=data.response.text;
							}
							else{
								if(!isUndefined(event)){
									message=event;
								}
							}
						}
						CT.error={code:code,message:message};
						eventTarget('error',CT.error);//注册监听error
						if(fatal){
							showError();
						}
					});
				}
				else if(canPlay(url)){
					video.attr('src',url);
				}
			});
		},
		/*
		 * flvPlayer
		 * 功能：使用flv.js插件进行播放
		*/
		flvPlayer=function (url){
			var path=getPath('flv.js')+'flv.min.js';
			loadJs(path,function(){
				if (typeof(flvjs)!='undefined' && !isUndefined(flvjs) && flvjs.isSupported()) {
					var config={
			            type: 'flv',
			            url: url
					};
					if(valType(url)=='object'){
						config=url;
					}
					if(vars['live']){
						config['isLive']=true;
					}
					if(pugPlayer){
						pugPlayer.pause();
	                    pugPlayer.unload();
	                    pugPlayer.detachMediaElement();
	                    pugPlayer.destroy();
						pugPlayer=null;
					}
			        pugPlayer = flvjs.createPlayer(config);
			        pugPlayer.attachMediaElement(video);
			        pugPlayer.load();
			        pugPlayer.on(flvjs.Events.METADATA_ARRIVED, function(res){
			        	videoHandler.loadedMetaData();
			        });
			        pugPlayer.on(flvjs.Events.ERROR, function(errorType, errorDetail, errorInfo){
	                  	CT.error={code:errorInfo['code'],message:errorInfo['msg']};
	                  	eventTarget('error',CT.error);//注册监听error
						showError();
			        });
			    }
			});
		},
		/*
		 * mpegtsPlayer
		 * 功能：使用mpegts.js插件进行播放
		*/
		mpegtsPlayer=function (url){
			var path=getPath('mpegts.js')+'mpegts.js';
			loadJs(path,function(){
				if (typeof(mpegts)!='undefined' && !isUndefined(mpegts) && mpegts.getFeatureList().mseLivePlayback) {
					var config={
			            type: 'mse',
			            url: url
					};
					if(valType(url)=='object'){
						config=url;
					}
					if(vars['live']){
						config['isLive']=true;
					}
					if(pugPlayer){
						pugPlayer.pause();
	                    pugPlayer.unload();
	                    pugPlayer.detachMediaElement();
	                    pugPlayer.destroy();
						pugPlayer=null;
					}
			        pugPlayer = mpegts.createPlayer(config);
			        pugPlayer.attachMediaElement(video);
			        pugPlayer.load();
			        
			        pugPlayer.on(mpegts.Events.METADATA_ARRIVED, function(res){
			        	videoHandler.loadedMetaData();
			        });
			        pugPlayer.on(mpegts.Events.ERROR, function(errorType, errorDetail, errorInfo){
	                  	CT.error={code:errorInfo['code'],message:errorInfo['msg']};
	                  	eventTarget('error',CT.error);//注册监听error
						showError();
			        });
			    }
			});
		},
		/*
		 * canPlay
		 * 功能：判断是否能支持相关视频格式
		*/
		canPlay=function(vStr,num){
			var vTypeArr=[
				{vidType:'video/ogg',codType:'theora, vorbis'},
				{vidType:'video/mp4',codType:'avc1.4D401E, mp4a.40.2'},
				{vidType:'video/mp4',codType:'avc1'},
				{vidType:'video/mp4',codType:'hevc'},
				{vidType:'video/webm',codType:'vp8.0, vorbis'},
				{vidType:'video/webm',codType:'vp9'},
				{vidType:'',codType:'application/x-mpegURL'},
				{vidType:'',codType:'application/vnd.apple.mpegurl'}
			];
			var vType={
				ogg:vTypeArr[0],
				mp4:[vTypeArr[1],vTypeArr[2],vTypeArr[3]],
				webm:[vTypeArr[4],vTypeArr[5]],
				m3u8:[vTypeArr[6],vTypeArr[7]]
			};
			var arr=[];
			var k='';
			var supportType=function(obj){
				var vType=obj['vidType'];
				var str='';
				if(vType){
					str=vType+'; '+ 'codecs="' + obj['codType'] + '"';
				}
				else{
					str=obj['codType'];
				}
				var sup = video.canPlayType(str);
				if(sup == '') {
					sup = 'no';
				}
				return sup;
			};
			var getExtension=function(filepath) {
				return filepath.replace(/.+\./, '');
			};
			if(isUndefined(num)){
				if(valType(vStr)=='string'){
					if(vars['type']){
						for(k in vTypeArr){
							if(vTypeArr[k]['vidType']==vars['type']){
								arr.push(vTypeArr[k]);
							}
						}
					}
					else{
						if(getExtension(vStr) && getExtension(vStr) in vType){
							var temp=vType[getExtension(vStr)];
							if(valType(temp)=='array'){
								for(var i=0;i<temp.length;i++){
									arr.push(temp[i]);
								}
							}
							else{
								arr.push(temp);
							}
						}
					}
				}
			}
			else{
				arr.push(vTypeArr[num]);
			}
			var is=false;
			if(arr.length>0){
				for(var i=0;i<arr.length;i++){
					if(supportType(arr[i])!='no'){
						is=true;
					}
				}
			}
			if(!is && !vars['plug']){
				CT.error={code:6,message:language['error']['videoTypeError']};
				eventTarget('error',CT.error);//注册监听error
				showError();
			}
			return is;
		},
		/*
		 * loadFace
		 * 功能：加载界面
		*/
		loadFace=function(){
			//loading容器
			C['loading']=createlDiv('ck-loading');
			CM.append(C['loading']);
			//默认控制栏容器
			C['bar']=createlDiv('ck-bar');
			CM.append(C['bar']);
			//右侧控制栏容器
			C['rightBar']=createlDiv('ck-right-bar ck-right-bar-hide');
			CM.append(C['rightBar']);
			//顶部容器
			C['topBar']=createlDiv('ck-top-bar ck-top-bar-hide');
			CM.append(C['topBar']);
			//进度栏
			C['bar']['pbox']=createlDiv('ck-bar-progress');
			C['bar']['pbox']['bg']=createlDiv('ck-bar-progress-bg');
			C['bar']['pbox']['load']=createlDiv('ck-bar-progress-load');
			C['bar']['pbox']['bg'].append(C['bar']['pbox']['load']);
			C['bar']['pbox']['play']=createlDiv('ck-bar-progress-play');
			C['bar']['pbox']['bg'].append(C['bar']['pbox']['play']);
			C['bar']['pbox']['mouseLine']=createlDiv('ck-bar-progress-mouseline');
			C['bar']['pbox']['bg'].append(C['bar']['pbox']['mouseLine']);
			C['bar']['pbox']['slider']=createlDiv('ck-bar-progress-slider');
			C['bar']['pbox'].append(C['bar']['pbox']['bg']).append(C['bar']['pbox']['slider']);
			C['bar'].append(C['bar']['pbox']);
			C['bar'].mouseout(barMouseOut).mouseover(barMouseOver);
			if(valType(vars['live'])=='number'){
				C['bar']['pbox']['play'].css({'width':'100%'});
				C['bar']['pbox']['slider'].css({'left':(100-C['bar']['pbox']['slider'].getWidth()*100/C['bar']['pbox']['bg'].getWidth())+'%'});
			}
			//播放暂停按钮组
			C['bar']['playAndPause']=createlDiv('ck-bar-playandpause');
			C['bar'].append(C['bar']['playAndPause']);
			
			C['bar']['playAndPause']['play']=createlButton('ck-bar-btn ck-btn-play');
			C['bar']['playAndPause']['play'].click(player.play);
			C['bar']['playAndPause']['play'].mouseover(function(){tip(this,language['play']);});
			C['bar']['playAndPause'].append(C['bar']['playAndPause']['play']);
			
			C['bar']['playAndPause']['pause']=createlButton('ck-bar-btn ck-btn-pause');
			C['bar']['playAndPause']['pause'].click(player.pause);
			C['bar']['playAndPause']['pause'].mouseover(function(){tip(this,language['pause']);});
			C['bar']['playAndPause'].append(C['bar']['playAndPause']['pause']);
			C['bar']['playAndPause']['pause'].hide();
			C['bar']['playAndPause']['refresh']=createlButton('ck-bar-btn ck-btn-refresh');
			C['bar']['playAndPause']['refresh'].click(player.play);
			C['bar']['playAndPause']['refresh'].mouseover(function(){tip(this,language['refresh']);});
			C['bar']['playAndPause'].append(C['bar']['playAndPause']['refresh']);
			
			//下一集按钮
			C['bar']['nextEpisode']=createlDiv('ck-bar-btn ck-bar-next');
			C['bar'].append(C['bar']['nextEpisode']);
			C['bar']['nextEpisode'].click(nextClick).mouseover(showNextEpisode).mouseout(function(){
				if(!isUndefined(C['next'])){
					player.closeLayer(C['next']);
				}
			});
			if(isUndefined(vars['next'])){
				C['bar']['nextEpisode'].hide();
			}		
			//时间显示框
			var timeDefault=formatSeconds(player.time())+'/'+formatSeconds(duration);
			if(vars['live']){
				timeDefault=language['live'];
			}
			C['time']=createlDiv('ck-bar-time',timeDefault);
			C['bar'].append(C['time']);
			
			//返回直播按钮
			C['bar']['backLive']=createlButton('ck-btn-backlive',language['backLive']);
			C['bar'].append(C['bar']['backLive']);
			C['bar']['backLive'].mouseover(function(){tip(this,language['backLive']);}).click(function(){
				eventTarget('backLive');
			});
			C['bar']['backLive'].hide();
			
			//全屏按钮组	
			C['bar']['fullAndExit']=createlDiv('ck-bar-fullandexit');
			C['bar'].append(C['bar']['fullAndExit']);
			
			C['bar']['fullAndExit']['full']=createlButton('ck-bar-btn ck-btn-full');
			C['bar']['fullAndExit']['full'].click(player.fullOrExit);
			C['bar']['fullAndExit']['full'].mouseover(function(){tip(this,language['full']);});
			C['bar']['fullAndExit'].append(C['bar']['fullAndExit']['full']);
				
			C['bar']['fullAndExit']['exitFull']=createlButton('ck-bar-btn ck-btn-exitfull');
			C['bar']['fullAndExit']['exitFull'].click(player.fullOrExit);
			C['bar']['fullAndExit']['exitFull'].mouseover(function(){tip(this,language['exitFull']);});
			C['bar']['fullAndExit'].append(C['bar']['fullAndExit']['exitFull']);	
				
			//网页全屏按钮		
			C['bar']['webFullAndExit']=createlDiv('ck-bar-webfullandexit');
			C['bar'].append(C['bar']['webFullAndExit']);
			
			C['bar']['webFullAndExit']['webFull']=createlButton('ck-bar-btn ck-btn-webfull');
			C['bar']['webFullAndExit']['webFull'].click(player.webFull);
			C['bar']['webFullAndExit']['webFull'].mouseover(function(){tip(this,language['webFull']);});
			C['bar']['webFullAndExit'].append(C['bar']['webFullAndExit']['webFull']);
				
			C['bar']['webFullAndExit']['exitWebFull']=createlButton('ck-bar-btn ck-btn-exitwebfull');
			C['bar']['webFullAndExit']['exitWebFull'].click(player.exitWebFull);
			C['bar']['webFullAndExit']['exitWebFull'].mouseover(function(){tip(this,language['exitWebFull']);});
			C['bar']['webFullAndExit'].append(C['bar']['webFullAndExit']['exitWebFull']);
			if(!vars['webFull']){
				C['bar']['webFullAndExit'].hide();
			}
			//剧场模式按钮组
			C['bar']['theatreAndExit']=createlDiv('ck-bar-theatreandexit');
			C['bar'].append(C['bar']['theatreAndExit']);
			
			C['bar']['theatreAndExit']['theatre']=createlButton('ck-bar-btn ck-btn-theatre');
			C['bar']['theatreAndExit']['theatre'].click(player.theatre);
			C['bar']['theatreAndExit']['theatre'].mouseover(function(){tip(this,language['theatre']);});
			C['bar']['theatreAndExit'].append(C['bar']['theatreAndExit']['theatre']);
				
			C['bar']['theatreAndExit']['exitTheatre']=createlButton('ck-bar-btn ck-btn-exittheatre');
			C['bar']['theatreAndExit']['exitTheatre'].click(player.exitTheatre);
			C['bar']['theatreAndExit']['exitTheatre'].mouseover(function(){tip(this,language['exitTheatre']);});
			C['bar']['theatreAndExit'].append(C['bar']['theatreAndExit']['exitTheatre']);
			
			if(!vars['theatre']){
				C['bar']['theatreAndExit'].hide();
			}
			//音量容器
			C['bar']['vbox']=createlDiv('ck-bar-volumebox');
			C['bar'].append(C['bar']['vbox']);
			
			C['bar']['vbox']['muted']=createlButton('ck-bar-btn ck-btn-muted');
			C['bar']['vbox']['muted'].click(player.muted);
			C['bar']['vbox'].append(C['bar']['vbox']['muted']);
			
			C['bar']['vbox']['exitMuted']=createlButton('ck-bar-btn ck-btn-exitmuted');
			C['bar']['vbox']['exitMuted'].click(player.exitMuted);
			C['bar']['vbox'].append(C['bar']['vbox']['exitMuted']);
			
			C['bar']['vbox']['volume']=createlDiv('ck-bar-volume');
			C['bar']['vbox'].append(C['bar']['vbox']['volume']);
			C['bar']['vbox']['volume']['box']=createlDiv('ck-bar-volumex');
			C['bar']['vbox']['volume'].append(C['bar']['vbox']['volume']['box']);
			C['bar']['vbox']['volume']['txt']=createlDiv('ck-bar-volume-txt','0');
			C['bar']['vbox']['volume']['box'].append(C['bar']['vbox']['volume']['txt']);
			C['bar']['vbox']['volume']['bg']=createlDiv('ck-bar-volume-bg');
			C['bar']['vbox']['volume']['box'].append(C['bar']['vbox']['volume']['bg']);
			C['bar']['vbox']['volume']['pp']=createlDiv('ck-bar-volume-pp');
			C['bar']['vbox']['volume']['bg'].append(C['bar']['vbox']['volume']['pp']);
			C['bar']['vbox']['volume']['slider']=createlDiv('ck-bar-volume-slider');
			C['bar']['vbox']['volume']['box'].append(C['bar']['vbox']['volume']['slider']);
			C['bar']['vbox']['mouseDown']=false;
			C['bar']['vbox']['volume'].show();
			changeVolumeSlider(vars['volume']);
			volumeDragY();//注册音量调节滑块动作
			C['bar']['vbox']['volume'].attr('style','');
			C['bar']['vbox'].mouseover(function(){changeVolumeSlider(video.muted?0:video.volume);});
			//播速容器
			C['bar']['playbackrate']=createlDiv('ck-bar-playbackrate-box');
			C['bar'].append(C['bar']['playbackrate']);
			C['bar']['playbackrate']['button']=createlDiv('ck-bar-playbackrate',language['playbackrate']);
			C['bar']['playbackrate'].append(C['bar']['playbackrate']['button']);
			C['bar']['playbackrate']['bgbox']=createlDiv('ck-bar-playbackrate-bg-box ck-list-bg-box');
			C['bar']['playbackrate'].append(C['bar']['playbackrate']['bgbox']);
			checkPlaybackrate();
			//字幕容器
			C['bar']['track']=createlDiv('ck-bar-track-box');
			C['bar'].append(C['bar']['track']);
			C['bar']['track']['button']=createlDiv('ck-bar-track',language['track']);
			C['bar']['track'].append(C['bar']['track']['button']);
			C['bar']['track']['bgbox']=createlDiv('ck-bar-track-bg-box ck-list-bg-box');
			C['bar']['track'].append(C['bar']['track']['bgbox']);
			C['bar']['track'].hide();
			//清晰度容器
			C['bar']['definition']=createlDiv('ck-bar-definition-box');
			C['bar'].append(C['bar']['definition']);
			C['bar']['definition']['button']=createlDiv('ck-bar-definition',language['definition']);
			C['bar']['definition'].append(C['bar']['definition']['button']);
			C['bar']['definition']['bgbox']=createlDiv('ck-bar-definition-bg-box ck-list-bg-box');
			C['bar']['definition'].append(C['bar']['definition']['bgbox']);
			C['bar']['definition'].hide();
			//截图按钮
			C['rightBar']['screenshot']=createlButton('ck-bar-btn ck-btn-screenshot');
			C['rightBar'].append(C['rightBar']['screenshot']);
			C['rightBar']['screenshot'].mouseover(function(){tip(this,language['screenshot'],null,'left');});		
			C['rightBar']['screenshot'].click(player.screenshot);
			
			//小窗口按钮组
			C['rightBar']['smallwindows']=createlDiv('ck-right-bar-smallwindows');
			C['rightBar'].append(C['rightBar']['smallwindows']);
			
			C['rightBar']['smallwindows']['open']=createlButton('ck-bar-btn ck-btn-smallwindows-open');
			C['rightBar']['smallwindows']['open'].click(function(){tip(this,language['smallwindowsClose'],null,'left');player.smallWindows(false);});
			C['rightBar']['smallwindows']['open'].mouseover(function(){tip(this,language['smallwindowsOpen'],null,'left');});
			C['rightBar']['smallwindows'].append(C['rightBar']['smallwindows']['open']);
			
			C['rightBar']['smallwindows']['close']=createlButton('ck-bar-btn ck-btn-smallwindows-close');
			C['rightBar']['smallwindows']['close'].click(function(){tip(this,language['smallwindowsOpen'],null,'left');player.smallWindows(true)});
			C['rightBar']['smallwindows']['close'].mouseover(function(){tip(this,language['smallwindowsClose'],null,'left');});
			C['rightBar']['smallwindows'].append(C['rightBar']['smallwindows']['close']);
			//循环按钮组
			C['rightBar']['loop']=createlDiv('ck-right-bar-loop');
			C['rightBar'].append(C['rightBar']['loop']);
			
			C['rightBar']['loop']['open']=createlButton('ck-bar-btn ck-btn-loop-open');
			C['rightBar']['loop']['open'].click(function(){tip(this,language['loopClose'],null,'left');player.loop(false);});
			C['rightBar']['loop']['open'].mouseover(function(){tip(this,language['loopOpen'],null,'left');});
			C['rightBar']['loop'].append(C['rightBar']['loop']['open']);
			
			C['rightBar']['loop']['close']=createlButton('ck-bar-btn ck-btn-loop-close');
			C['rightBar']['loop']['close'].click(function(){tip(this,language['loopOpen'],null,'left');player.loop(true);});
			C['rightBar']['loop']['close'].mouseover(function(){tip(this,language['loopClose'],null,'left');});
			C['rightBar']['loop'].append(C['rightBar']['loop']['close']);
			C['rightBar'].mouseout(rightBarMouseOut).mouseover(rightBarMouseOver);
			//截图显示容器
			C['screenshot']=createlDiv('ck-screenshot');
			CM.append(C['screenshot']);
			C['screenshot']['img']=createlDiv('ck-screenshot-img');
			C['screenshot'].append(C['screenshot']['img']);
			
			C['screenshot']['bar']=createlDiv('ck-screenshot-bar');
			C['screenshot'].append(C['screenshot']['bar']);
			
			C['screenshot']['bar']['down']=createlA(language['down'],'','ck-screenshot-btn ck-screenshot-down');
			C['screenshot']['bar'].append(C['screenshot']['bar']['down']);
			
			C['screenshot']['bar']['close']=createlButton('ck-screenshot-btn ck-screenshot-close',language['close']);
			C['screenshot']['bar']['close'].click(player.closeScreenshot);
			C['screenshot']['bar'].append(C['screenshot']['bar']['close']);
			//顶部内容
			C['topBar']['zoomEle']=createlDiv('ck-top-bar-zoom');
			
			C['topBar']['zoomEle']['zoom50']=createlDiv('ck-top-bar-zoom-container');
			C['topBar']['zoomEle'].append(C['topBar']['zoomEle']['zoom50']);
			C['topBar']['zoomEle']['zoom50']['left']=createlDiv('ck-top-bar-zoom-left');
			C['topBar']['zoomEle']['zoom50'].append(C['topBar']['zoomEle']['zoom50']['left']);
			C['topBar']['zoomEle']['zoom50']['left']['button']=createlDiv('ck-top-bar-zoom-button-50');
			C['topBar']['zoomEle']['zoom50']['left'].append(C['topBar']['zoomEle']['zoom50']['left']['button']);
			C['topBar']['zoomEle']['zoom50']['right']=createlDiv('ck-top-bar-zoom-right',language['p50']);
			C['topBar']['zoomEle']['zoom50'].append(C['topBar']['zoomEle']['zoom50']['right']);
			C['topBar']['zoomEle']['zoom50'].click(function(){player.zoom(50);});
			
			C['topBar']['zoomEle']['zoom75']=createlDiv('ck-top-bar-zoom-container');
			C['topBar']['zoomEle'].append(C['topBar']['zoomEle']['zoom75']);
			C['topBar']['zoomEle']['zoom75']['left']=createlDiv('ck-top-bar-zoom-left');
			C['topBar']['zoomEle']['zoom75'].append(C['topBar']['zoomEle']['zoom75']['left']);
			C['topBar']['zoomEle']['zoom75']['left']['button']=createlDiv('ck-top-bar-zoom-button-75');
			C['topBar']['zoomEle']['zoom75']['left'].append(C['topBar']['zoomEle']['zoom75']['left']['button']);
			C['topBar']['zoomEle']['zoom75']['right']=createlDiv('ck-top-bar-zoom-right',language['p75']);
			C['topBar']['zoomEle']['zoom75'].append(C['topBar']['zoomEle']['zoom75']['right']);
			C['topBar']['zoomEle']['zoom75'].click(function(){player.zoom(75);});
			
			C['topBar']['zoomEle']['zoom100']=createlDiv('ck-top-bar-zoom-container');
			C['topBar']['zoomEle'].append(C['topBar']['zoomEle']['zoom100']);
			C['topBar']['zoomEle']['zoom100']['left']=createlDiv('ck-top-bar-zoom-left');
			C['topBar']['zoomEle']['zoom100'].append(C['topBar']['zoomEle']['zoom100']['left']);
			C['topBar']['zoomEle']['zoom100']['left']['button']=createlDiv('ck-top-bar-zoom-button-100');
			C['topBar']['zoomEle']['zoom100']['left'].append(C['topBar']['zoomEle']['zoom100']['left']['button']);
			C['topBar']['zoomEle']['zoom100']['right']=createlDiv('ck-top-bar-zoom-right',language['p100']);
			C['topBar']['zoomEle']['zoom100'].append(C['topBar']['zoomEle']['zoom100']['right']);
			C['topBar']['zoomEle']['zoom100'].click(function(){player.zoom(100);});
			C['topBar'].append(C['topBar']['zoomEle']);
			C['topBar']['titleEle']=createlDiv('ck-top-bar-title',vars['title']);
			C['topBar'].append(C['topBar']['titleEle']);
			C['topBar']['timeEle']=createlDiv('ck-top-bar-time');
			C['topBar'].append(C['topBar']['timeEle']);
			//中间播放按钮
			C['centerPlay']=createlDiv('ck-center-play');
			C['centerPlay'].click(player.play);
			CM.append(C['centerPlay']);
			//buff
			C['buffer']=createlDiv('ck-buffer');
			CM.append(C['buffer']);
			//消息提示框
			C['message']=createlDiv('ck-message');
			CM.append(C['message']);
			//消息Tip框
			C['tip']=createlDiv('ck-tip');
			CM.append(C['tip']);
			C['tip']['content']=createlDiv('ck-content ck-content-float-auto');
			C['tip'].append(C['tip']['content']);
			C['tip']['triangle']=createlDiv('ck-triangle ck-triangle-auto');
			C['tip'].append(C['tip']['triangle']);
			tip(video,'ckplayer');//初始化显示，为了获取高度
			C['tip'].minHeight=C['tip'].getHeight();
			tip();
			//贴片广告层
			C['ad']=createlDiv('ck-yytf');
			CM.append(C['ad']);
			
			C['ad']['link']=createlDiv('ck-yytf-front-link');
			C['ad'].append(C['ad']['link']);
			C['ad']['link'].click(frontVideoClickHandler);
			
			C['ad']['picture']=createlDiv('ck-yytf-front-picture');
			C['ad'].append(C['ad']['picture']);
			
			C['ad']['top']=createlDiv('ck-yytf-top');
			C['ad'].append(C['ad']['top']);
			
			C['ad']['top']['countDown']=createlDiv('ck-yytf-countdown');
			C['ad']['top'].append(C['ad']['top']['countDown']);
			
			C['ad']['top']['closeTime']=createlDiv('ck-yytf-closetime');
			C['ad']['top'].append(C['ad']['top']['closeTime']);
			
			C['ad']['top']['closeAd']=createlDiv('ck-yytf-closead',language['closeAd']);
			C['ad']['top'].append(C['ad']['top']['closeAd']);
			C['ad']['top']['closeAd'].click(closeFrontAd);
			
			C['ad']['bottom']=createlDiv('ck-yytf-bottom');
			C['ad'].append(C['ad']['bottom']);
			
			C['ad']['bottom']['fullAndExit']=createlDiv('ck-yytf-fullandexit');
			C['ad']['bottom'].append(C['ad']['bottom']['fullAndExit']);
			C['ad']['bottom']['fullAndExit']['full']=createlButton('ck-yytf-btn ck-yytf-fullandexit-full');
			C['ad']['bottom']['fullAndExit']['full'].click(player.full);
			C['ad']['bottom']['fullAndExit'].append(C['ad']['bottom']['fullAndExit']['full']);
			C['ad']['bottom']['fullAndExit']['exitFull']=createlButton('ck-yytf-btn ck-yytf-fullandexit-exitfull');
			C['ad']['bottom']['fullAndExit'].append(C['ad']['bottom']['fullAndExit']['exitFull']);
			C['ad']['bottom']['fullAndExit']['exitFull'].click(player.exitFull);
			
			C['ad']['bottom']['mutedAndExit']=createlDiv('ck-yytf-mutedandexit');
			C['ad']['bottom'].append(C['ad']['bottom']['mutedAndExit']);
			C['ad']['bottom']['mutedAndExit']['muted']=createlButton('ck-yytf-btn ck-yytf-mutedandexit-muted');
			C['ad']['bottom']['mutedAndExit'].append(C['ad']['bottom']['mutedAndExit']['muted']);
			C['ad']['bottom']['mutedAndExit']['muted'].click(player.muted);
			C['ad']['bottom']['mutedAndExit']['exitMuted']=createlButton('ck-yytf-btn ck-yytf-mutedandexit-exitmuted');
			C['ad']['bottom']['mutedAndExit'].append(C['ad']['bottom']['mutedAndExit']['exitMuted']);
			C['ad']['bottom']['mutedAndExit']['exitMuted'].click(player.exitMuted);
			
			C['ad']['bottom']['details']=createlDiv('ck-yytf-details',language['details']);
			C['ad']['bottom'].append(C['ad']['bottom']['details']);
			
			
			
			C['ad'].hide();
			//暂停广告层
			C['adPause']=createlDiv('ck-pause-yytf');
			CM.append(C['adPause']);
			//关于视频
			C['about']=createlDiv('ck-about');
			CM.append(C['about']);		
			C['about']['bar']=createlDiv('ck-about-bar');
			C['about'].append(C['about']['bar']);
			
			C['about']['bar']['copy']=createlButton('ck-bar-btn ck-btn-about-copy');
			C['about']['bar'].append(C['about']['bar']['copy']);
			C['about']['bar']['copy'].click(aboutCopy);
			C['about']['bar']['copy'].mouseover(function(){tip(this,language['copy'],null,'left');});
			
			C['about']['bar']['close']=createlButton('ck-bar-btn ck-btn-about-close');
			C['about']['bar']['close'].click(aboutHide);
			C['about']['bar']['close'].mouseover(function(){tip(this,language['close'],null,'left');});
			C['about']['bar'].append(C['about']['bar']['close']);
			
			C['tempTime']=createlDiv('ck-tempTime');
			CM.append(C['tempTime']);
			C['bar']['playAndPause']['refresh'].hide();
			if(vars['controls']){
				player.bar(false);
				player.rightBar(false);
				C['loading'].hide();
				C['centerPlay'].hide();
			}
			else{
				loadLogo();//加载logo
				loadAbout();//构建关于视频相关内容
				loadMenu();//构建右键
			}
			if(app=='iphone'){
				C['loading'].hide();
			}
			changeTopTime();//修改顶部时间
			CK.mouseout(function(){
				if(!paused){
					hideBar();
				}
			}).mouseover(function(){
				if(!C['bar'].hasClass('ck-bar-out')){
					C['bar'].removeClass('ck-bar-out');
					CM.removeClass('ck-nocursor');
				}
				if(C['rightBar'].css('display')=='block'){
					C['rightBar'].removeClass('ck-right-bar-hide');
				}
				if(C['topBar'].css('display')=='block'){
					C['topBar'].removeClass('ck-top-bar-hide');
				}
			}).mousemove(function(){
				if(C['bar'].hasClass('ck-bar-out')){
					C['bar'].removeClass('ck-bar-out');
					eventTarget('mouseActive',true);
					CM.removeClass('ck-nocursor');
					hideBar();
				}
				if(C['rightBar'].css('display')=='block'){
					C['rightBar'].removeClass('ck-right-bar-hide');
				}
				if(C['topBar'].css('display')=='block'){
					C['topBar'].removeClass('ck-top-bar-hide');
				}
			});
		},
		/*
		 * addAllListener
		 * 功能：注册播放器的全部监听
		*/
		addAllListener=function(){
			video.addListener('stalled',videoHandler.stalled);//在浏览器不论何种原因未能取回媒介数据时运行的脚本
			video.addListener('suspend',videoHandler.suspend);//在媒介数据完全加载之前不论何种原因终止取回媒介数据时运行的脚本
			video.addListener('loadeddata',videoHandler.loadedData);//监听视频数据已加载
			video.addListener('loadstart',videoHandler.loadStart);//在文件开始加载且未实际加载任何数据前运行的脚本
			video.addListener('loadedmetadata',videoHandler.loadedMetaData);//监听视频元数据
			video.addListener('canplay',videoHandler.canPlay);//当文件就绪可以开始播放时运行的脚本（缓冲已足够开始时）
			video.addListener('timeupdate',videoHandler.timeUpDate);//监听视频播放时间
			video.addListener('seeking',videoHandler.seeking);//监听视频跳转中
			video.addListener('seeked',videoHandler.seeked);//监听视频跳转
			video.addListener('waiting',videoHandler.waiting);//监听视频缓冲
			video.addListener('play',videoHandler.play);//监听视频播放事件
			video.addListener('pause',videoHandler.pause);//监听视频暂停事件
			video.addListener('volumechange',videoHandler.volumeChange);//监听音量改变
			video.addListener('ended',videoHandler.ended);//监听播放结束
			video.addListener('error',videoHandler.error);//监听发生故障并且文件突然不可用时运行的脚本（比如连接意外断开时）
			if(!isUndefined(video.emptied)){
				video.addListener('emptied',videoHandler.emptied);//监听播放结束
			}
			//CV.singleClick(player.playOrPause);//监听视频单击
			CV.singleClick(function(){
				if(!isDrag){
					player.playOrPause();
				}
				else{
					isDrag=false;
				}
			});
			CM.doubleClick(player.fullOrExit);//监听视频双击
			$(document).addListener('keydown',videoHandler.keydown);//监听键盘按键
			addListener(window, 'resize', videoHandler.resize);//监听窗口尺寸变化
			if(!isUndefined(vars['smallWindows'])){
				if(valType(vars['smallWindows'])=='boolean' && vars['smallWindows']){
					addListener(window, 'scroll', windowScroll);//监听窗口滚动
				}
			}
			CT.mouseWheel(videoHandler.mouseWheel);
		},
		/*
		 * eventTarget
		 * 功能：注册事件，将事件注册给CT对象供外部监听
		*/
		eventTarget=function(name,obj){
			try{
				if(EventTarget!=null){
					if(isUndefined(obj)){
						obj=null;
					}
					newEvent.dispatchEvent(name,obj);
				}
				else{
					EventTarget = function() {
						this.listeners = {};
					};
					EventTarget.prototype.listeners = null;
					EventTarget.prototype.addEventListener = function(type, callback) {
						if(!(type in this.listeners)) {
							this.listeners[type] = [];
						}
						this.listeners[type].push(callback);
						eventTargetList=this.listeners;
					};
					EventTarget.prototype.removeEventListener = function(type, callback) {
						if(!(type in this.listeners)) {
							return;
						}
						var stack = this.listeners[type];
						for(var i = 0, l = stack.length; i < l; i++) {
							if(stack[i] === callback) {
								stack.splice(i, 1);
								return this.removeEventListener(type, callback);
							}
						}
						eventTargetList=stack;
					};
					EventTarget.prototype.dispatchEvent = function(type,obj) {
						if(!(type in this.listeners)) {
							return;
						}
						var stack = this.listeners[type];
						for(var i = 0, l = stack.length; i < l; i++) {
							stack[i].call(this, obj);
						}
					};
					newEvent = new EventTarget();
				}
			}
			catch(event){console.error(event)}
		},
		/*
		 * changeCookie
		 * 功能：使用cookie记录时间
		*/
		changeCookie=function(time){
			var arr=[];
			var cStr=cookie(ckplayerCookie);
			var i=0,y=0;
			var cTime=Math.floor(Date.now()*0.001);
			time=Math.floor(time*100);
			time=time*0.01;
			if(!cookieTime){
				cookieTime=365*24;
			}
			if(!cStr){
				arr.push([cookieName,time,cTime+cookieTime*3600]);
			}
			else{
				arr=stringToArray(cStr);
				var have=false;
				var tempArr=[[cookieName,time,cTime+cookieTime*3600]];
				for(i=0;i<arr.length;i++){
					if(arr[i][2]>cTime && arr[i][0]!=cookieName && y<19){
						tempArr.push(arr[i]);
						y++;
					}
				}
				arr=tempArr;
			}
			if(arr.length>0){
				cookie(ckplayerCookie,'delete',vars['domain'],vars['cookiePath']);
				cookie(ckplayerCookie,arrayToString(arr),vars['domain'],vars['cookiePath']);
			}
			cookieArray=arr;
		},
		/*
		 * loadTrack
		 * 功能：加载track
		*/
		loadTrack=function(){
			if(isUndefined(video.textTracks)){
				return;
			}
			var i=0;
			if(!isUndefined(vars['track']) && valType(vars['track'])=='array' && !video.find('track')){
				var track='';
				var arr=vars['track'];
				C['bar']['track'].show();
				for(i=0;i<arr.length;i++){
					var def='';
					if(!def && !isUndefined(arr[i]['default']) && arr[i]['default']){
						def=' default';
					}
					track+='<track src="'+arr[i]['src']+'" srclang="'+arr[i]['srclang']+'" kind="'+arr[i]['kind']+'" label="'+arr[i]['label']+'"'+def+'>';
				}
				if(video){
					var html=video.htm()+track;
					video.htm(html);
				}
				if(video.find('track') && !loadedTrack){
					loadedTrack=true;
					checkTrack();
				}
			}
		},
		/*
		 * loadedMetaData
		 * 功能：获取元数据后执行的函数
		*/
		loadedMetaData=function(){
			eventTarget('loadedMetaData',{
				width:CK.getWidth(),
				height:CK.getHeight(),
				videoWidth:CT.videoWidth,
				videoHeight:CT.videoHeight,
				duration:duration,
				volume:CT.volume
			});
			eventTarget('duration',duration);
			if('error' in C){
				C['error'].hide();
			}
			replaceInformation('videoWidth',CT.videoWidth);
			replaceInformation('videoHeight',CT.videoHeight);
			replaceInformation('volume',parseInt(CT.volume*100));
			replaceInformation('duration',parseInt(duration));
			documentHidden(function(state){
				if(vars['documentFocusPause']){
					if(state=='show'){
						if(!focusPause && paused){
							player.play();
						}
					}
					else{
						focusPause=paused;
						player.pause();
					}
				}
			});
		};
		/*
		 * videoHandler
		 * 功能：视频播放对象video注册内部监听调用函数
		*/
		var videoHandler={
			loadStart:function(){
				if(!vars['controls'] && app!='iphone'){
					C['loading'].show();
				}
				C['centerPlay'].hide();
				eventTarget('loadstart');
				
			},
			canPlay:function(){
				C['loading'].hide();
				eventTarget('canplay');
			},
			loadedData:function(){
				eventTarget('loadeddata');
			},
			loadedMetaData:function(){
				duration=this.duration;
				if(!isUndefined(this.duration) && vars['duration']){
					duration=vars['duration'];
				}			
				if(!vars['live']){
					C['time'].htm(formatSeconds(this.currentTime)+'/'+formatSeconds(duration));
					C['tempTime'].htm(formatSeconds(this.currentTime)+'/'+formatSeconds(duration));
				}
				C['loading'].hide();
				if(vars['poster']){
					video.attr('poster',vars['poster']);
				}
				if(!vars['autoplay'] && !vars['controls']){
					C['centerPlay'].show();
					C['buffer'].hide();
				}
				
				CT.duration=duration;
				CT.videoWidth=this.videoWidth;
				CT.videoHeight=this.videoHeight;
				CT.volume=this.volume;
				if(CT.videoWidth || CT.videoHeight || duration){
					loadedMetaData();
				}
				var len = 0;
				if(!isUndefined(this.buffered)){
					len=this.buffered.length;
				}
				if(len>0){
					changeLoad();
				}
				if(!vars['live']){
					changePreview(vars['preview']);//修改预览图片
				}
				else{
					changePreview();//修改预览图片
				}
				if(!isUndefined(vars['prompt']) && valType(vars['prompt'])=='array' && !vars['live']){
					changePrompt();
				}
				else{
					deletePrompt();
				}
				if(isChangeDef){
					checkDefinition();
					isChangeDef=false;
				}
				pSliderMouseDown=false;
				loadedmetadataNum++;
				if(!isUndefined(ad) && !isUndefined(ad['frontPlay']) && ad['frontPlay'] && loadedmetadataNum>1){//如果是贴片广告播放中，则进行播放和计算
					player.play();
					calculationAdFrontTime(duration);//计算贴片广告的时间
				}
				if(!isUndefined(ad) && !isUndefined(ad['frontPlay']) && !ad['frontPlay']){//如果广告播放结束则播放正片
					player.volume(vars['volume']);
					player.play();
				}
				if(!vars['autoplay'] && loadMeta){
					loadMeta=false;
					player.pause();
					setTimeout(player.pause,300);
				}
			},
			timeUpDate:function(){//监听播放时间
				if(!waited){
					waited=true;
					C['buffer'].hide();
					eventTarget('buffer','end');
				}
				var len = this.buffered.length;
				if(len>0){
					changeLoad();
				}
				if(!duration && this.duration){
					duration=this.duration;
					CT.duration=duration;
					CT.videoWidth=this.videoWidth;
					CT.videoHeight=this.videoHeight;
					if(CT.videoWidth || CT.videoHeight || duration){
						loadedMetaData();
					}
				}
				oldTime=playTime;
				playTime=this.currentTime;
				if(maxSeeTime<oldTime){
					maxSeeTime=oldTime;
				}
				changeProgress(playTime);
				eventTarget('time',playTime);
				if(!vars['live']){
					C['time'].htm(formatSeconds(playTime)+'/'+formatSeconds(duration));
					C['tempTime'].htm(formatSeconds(playTime)+'/'+formatSeconds(duration));
				}
				if(!isUndefined(ad) && !isUndefined(ad['frontPlay']) && ad['frontPlay'] && loadedmetadataNum>1){//如果是贴片广告播放中，则进行播放
					calculationAdFrontTime(duration-playTime);//计算贴片广告的时间
				}
				if(!isUndefined(C['error']) && C['error'].css('display')=='block'){
					C['error'].hide();
				}
				if(cookieName){
					changeCookie(playTime);
				}
				replaceInformation('audioDecodedByteCount',this.webkitAudioDecodedByteCount || this.audioDecodedByteCount || 0);
				replaceInformation('videoDecodedByteCount',this.webkitVideoDecodedByteCount || this.videoDecodedByteCount || 0);
			},
			ended:function(){//监听播放结束
				if(!isUndefined(ad) && !isUndefined(ad['frontPlay']) && ad['frontPlay']){//如果是贴片广告播放中，则用广告结束函数对此进行判断
					adFrontEnded();
				}
				else{
					C['bar']['playAndPause']['pause'].hide();
					C['bar']['playAndPause']['play'].hide();
					C['bar']['playAndPause']['refresh'].show();
					eventTarget('ended');
					showEnded();
					message();
				}
			},
			error:function(event){
				if(!isUndefined(event.type)){
					var errorInfo=this.error;
					var errorBak=function(){
						var code=12;
						var msg=language['error']['loadingFailed'];
						if(!isUndefined(errorInfo)){
							if(!isUndefined(errorInfo.code)){
								code=errorInfo.code;
							}
							if(!isUndefined(errorInfo.message)){
								msg=errorInfo.message;
							}
							CT.error={code:code,message:msg};
							showError();
						}
						else{
							CT.error={code:code,message:msg};
							if(event.target.attr('src')){
								showError();
							}
						}
						eventTarget('error',CT.error);//注册监听error
					};
					if(event.type=='error'){
						try{
							if(video.currentSrc){
								ajax({url:video.currentSrc,error:function(info){
									if(info && valType(info)=='object' && 'code' in info && info['code']){
										CT.error=info;
										eventTarget('error',CT.error);//注册监听error
										showError();
									}
									else{
										errorBak();
									}
								},success:function(data){
									if(!data){
										errorBak();
									}
								}});
							}
							else{
								errorBak();
							}
						}
						catch(event){
							errorBak();
						}
					}
				}
			},
			stalled:function(){
				eventTarget('stalled');//注册监听error
				CT.error={code:13,message:'load:stalled'};
				eventTarget('error',CT.error);//注册监听error
			},
			suspend:function(){
				eventTarget('suspend');//注册监听error
			},
			emptied:function(){
				eventTarget('emptied');//注册监听error
				CT.error={code:7,message:language['error']['emptied']};
				eventTarget('error',CT.error);//注册监听error
			},
			seeked:function(){
				if(paused){
					player.play();
				}
				pSliderMouseDown=false;
				eventTarget('seek',{time:this.currentTime,state:'seeked'});
				eventTarget('seeked');
			},
			seeking:function(){
				if(paused){
					player.play();
				}
				var seekingTime=this.currentTime;
				if(firstSeekTime==-1){
					firstSeekTime=seekingTime;//记录第一次拖动的时间
				}
				switch(vars['timeScheduleAdjust']){
					case 0://禁止拖动
						if(oldTime!=seekingTime){
							waitingMessage=false;
							player.seek(oldTime);
							message(language['timeScheduleAdjust']['prohibit']);
							return;
						}
						break;
					case 2://只能前进（向右拖动
						if(seekingTime<oldTime){
							waitingMessage=false;
							player.seek(oldTime);
							message(language['timeScheduleAdjust']['prohibitBackOff']);
							return;
						}
						break;
					case 3://是只能后退
						if(seekingTime>oldTime){
							waitingMessage=false;
							player.seek(oldTime);
							message(language['timeScheduleAdjust']['prohibitForward']);
							return;
						}
						break;
					case 4://只能前进但能回到第一次拖动时的位置
						if(seekingTime<firstSeekTime){
							waitingMessage=false;
							player.seek(firstSeekTime);
							message(language['timeScheduleAdjust']['prohibitLookBack']);
							return;
						}
						break;
					case 5://看过的地方可以随意拖动
						if(seekingTime>maxSeeTime){
							waitingMessage=false;
							player.seek(maxSeeTime);
							message(language['timeScheduleAdjust']['prohibitForwardNotViewed']);
							return;
						}
						break;
				}
				eventTarget('seek',{time:seekingTime,state:'seeking'});
				eventTarget('seeking');
				if(!vars['live']){
					C['time'].htm(formatSeconds(seekingTime)+'/'+formatSeconds(duration));
					C['tempTime'].htm(formatSeconds(seekingTime)+'/'+formatSeconds(duration));
				}
				
			},
			waiting:function(){
				waited=false;
				eventTarget('buffer','start');//注册监听视频缓冲
				C['centerPlay'].hide();
				if(!vars['controls']){
					C['buffer'].show();
				}
				if(!waitingMessage){
					waitingMessage=true;
				}
				else{
					message(language['waiting']);
				}
				
			},
			play:function(){
				paused=false;
				C['bar']['playAndPause']['play'].hide();
				C['bar']['playAndPause']['pause'].show();
				C['bar']['playAndPause']['refresh'].hide();
				C['centerPlay'].hide();
				if(playType=='button'){
					C['bar']['pbox'].removeClass('ck-bar-progress-out');
				}
				else{
					C['bar']['pbox'].addClass('ck-bar-progress-out');
				}
				if(recoveryVolume){
					recoveryVolume=false;
					player.exitMuted();
				}
				hideBar();
				closePauseAd();//关闭暂停广告
				playType='';
				if(!isUndefined(C['ended'])){
					player.closeLayer(C['ended']);
				}
				if('error' in C){
					C['error'].hide();
				}
				eventTarget('play');
				if(!isUndefined(ad)){//如果存在广告
					if(!isUndefined(ad['front'])){//如果存在贴片广告
						if(isUndefined(ad['frontPlay'])){//如果贴片广告还未播放
							ad['frontPlayI']=0;
							ad['frontPlay']=true;//开始播放贴片广告
							player.pause();//暂停视频
							adFrontPlay();
							return;
						}
					}
				}
				if(seekTime && (isUndefined(vars['live']) || (!isUndefined(vars['live']) && valType(vars['live'])=='boolean' && !vars['live'])) && seekTime<parseInt(duration*100)*0.01){//如果默认需要跳转，则进行seek
					player.seek(seekTime);
					seekTime=0;
				}			
			},
			pause:function(){
				paused=true;
				if(isUndefined(ad) || isUndefined(ad['frontPlay']) || (!isUndefined(ad['frontPlay']) && !ad['frontPlay'])){//如果是贴片广告播放中，则进行播放和计算
					if(!isUndefined(ad) && !isUndefined(ad['pause'])){
						if(isUndefined(ad['pausePlayI'])){//如果贴片广告还未播放
							ad['pausePlayI']=0;
						}
						adPausePlay();//播放暂停广告
					}
					else{
						if(!vars['controls']){
							C['centerPlay'].show();
							C['buffer'].hide();
						}
					}
				}
				else{
					if(!vars['controls']){
						C['centerPlay'].show();
						C['buffer'].hide();
					}
				}
				C['bar']['playAndPause']['pause'].hide();
				C['bar']['playAndPause']['play'].show();
				C['bar']['playAndPause']['refresh'].hide();
				C['bar']['pbox'].removeClass('ck-bar-progress-out');
				checkProgressSlider();
				eventTarget('pause');
				C['bar'].removeClass('ck-bar-out');
				CM.removeClass('ck-nocursor');
			},
			volumeChange:function(){
				if(this.volume==0 && !mutedState){
					this.muted=true;
				}
				if(this.volume>0 && mutedState){
					this.muted=false;
				}
				if(mutedState!=this.muted){
					mutedState=this.muted;
					changeMuted(this.muted);
					CT.muted=this.muted;
					eventTarget('muted',CT.muted);//注册静音事件
				}
				CT.volume=this.volume;
				eventTarget('volume',this.volume);//注册音量事件
				if(!isUndefined(C['bar']['vbox'])){
					var vol=parseInt(this.volume*100);
					C['bar']['vbox']['volume']['txt'].htm(vol);
					if(!C['bar']['vbox']['mouseDown']){
						changeVolumeSlider(this.muted?0:this.volume);
					}
					if(!this.muted){//如果非静音状态
						message(language['volume']+vol+'%');
					}
				}
				replaceInformation('volume',parseInt(this.volume*100));
			},
			resize:function(){
				var fullState = document.fullScreen || document.mozFullScreen || document.webkitIsFullScreen || document.msFullscreenElement;
				if(fullState) {
					C['bar']['fullAndExit']['exitFull'].show();
					C['bar']['fullAndExit']['full'].hide();
					C['ad']['bottom']['fullAndExit']['exitFull'].show();
					C['ad']['bottom']['fullAndExit']['full'].hide();
					if(!CM.hasClass('ck-main-full')){
						CM.addClass('ck-main-full');
					}
					message(language['full']);
					C['bar']['webFullAndExit'].hide();
					C['bar']['theatreAndExit'].hide();
					if(!isUndefined(C['menu'])){
						CM.append(C['menu']);
					}
				}
				else {
					fullState=false;
					C['bar']['fullAndExit']['full'].show();
				 	C['bar']['fullAndExit']['exitFull'].hide();
				 	C['ad']['bottom']['fullAndExit']['full'].show();
				 	C['ad']['bottom']['fullAndExit']['exitFull'].hide();
				 	if(CM.hasClass('ck-main-full')){
						CM.removeClass('ck-main-full');
					}
				 	if(!isUndefined(vars['webFull']) && vars['webFull']){
				 		C['bar']['webFullAndExit'].show();
				 	}
				 	if(!isUndefined(vars['theatre']) && !CT.webFull){
						C['bar']['theatreAndExit'].show();
					}
				 	if(!isUndefined(C['menu'])){
						$('body').append(C['menu']);
					}
				 	if(CV.css('width')!=CM.css('width')){
				 		player.zoom(100);
				 	}
				}
				if(CT.full!=fullState){
					CT.full=fullState;
					eventTarget('full',fullState);//注册播放事件
				}
				checkVideoRotate();
				changeProgress(player.time());
			},
			keydown:function(event){
				event=window.event || event;
				var keycode = event.keyCode || event.which;
				var v=player.volume();
				var pre=function(){
					if(event.preventDefault){
				        event.preventDefault();
				    }
				    else{
				        event.returnValue = false;
				    }
				};
				switch(keycode) {
					case 32:
					    pre();
						player.playOrPause();
						break;
					case 37:
						player.fastBack();
						break;
					case 39:
						player.fastNext();
						break;
					case 38:
						if(valType(vars['keyVolume'])=='number' && (vars['keyVolume']==1 || (vars['keyVolume']==2 && CT.full))){
							pre();
							player.volume(v+.1<1?v+=.1:1);
						}
						break;
					case 40:
						if(valType(vars['keyVolume'])=='number' && (vars['keyVolume']==1 || (vars['keyVolume']==2 && CT.full))){
							pre();
							player.volume(v-.1>0?v-=.1:0);
						}
						break;
					default:
						break;
				}
			},
			mouseWheel:function(event){
				if(valType(vars['mouseWheelVolume'])=='number' && (vars['mouseWheelVolume']==1 || (vars['mouseWheelVolume']==2 && CT.full))){
					event=window.event || event;
					var v=player.volume();
					if(event.preventDefault){
				        event.preventDefault();
				    }
				    else{
				        event.returnValue = false;
				    }
					if(event.wheelDelta) {
						if(event.wheelDelta > 0) {
							player.volume(v+.1<1?v+=.1:1);
						}
						if(event.wheelDelta < 0) {
							player.volume(v-.1>0?v-=.1:0);
						}
					} else if(event.detail) {
						if(event.detail > 0) {
							player.volume(v-.1>0?v-=.1:0);
						}
						if(event.detail < 0) {
							player.volume(v+.1<1?v+=.1:1);
						}
					}
				}
				
			}
		};
		/*
		 * adFrontPlay
		 * 功能：播放贴片广告
		*/
		var adFrontPlay=function(){
			if(ad['frontPlayI']==0){//如果是播放第一个广告，则显示相关组件
				allBarHide(false);
				C['ad'].show();
				ad['front']['video']=false;//初始化，不包含视频广告
				//判断是否需要启用固定时间后关闭广告按钮
				if(!isUndefined(ad['front']['closeTime'])){
					if(ad['front']['closeTime']>0){
						C['ad']['top']['closeTime'].show();
						adFrontCloseTime();
					}
					else{
						C['ad']['top']['closeAd'].show();
					}
				}
			}
			if(!isUndefined(ad['front']['node']) && ad['front']['node']!=null){
				CM.append(ad['front']['node']);
				player.closeLayer(ad['front']['node']);
				ad['front']['node']=null;
			}
			C['ad']['picture'].htm('');
			C['ad']['picture'].hide();
			C['ad']['link'].hide();
			if(!isUndefined(ad['front']['list'][ad['frontPlayI']])){
				var adv=ad['front']['list'][ad['frontPlayI']];
				frontAdPlay=true;//贴片广告播放状态
				eventTarget('frontAd',adv);
				if(adv['type']=='picture'){
					adPicturePlay(adv);
				}
				else if(adv['type']=='node'){
					adNode(adv);
				}
				else{
					C['ad']['link'].attr('data-link',adv['link']);
					adVideoPlay(adv['file']);
				}
				if(!isUndefined(adv['link'])){
					C['ad']['bottom']['details'].show();
					C['ad']['bottom']['details'].htm('<a href="'+adv['link']+'" target="blank">'+language['details']+'</a>');
				}
				else{
					C['ad']['bottom']['details'].hide();
				}
			}
		},
		/*
		 * adFrontCloseTime
		 * 功能：播放贴片广告时按指定时间后显示关闭广告按钮
		*/
		adFrontCloseTime=function(){
			if(!isUndefined(ad['front']['closeSetTime'])){
				clearTimeout(ad['front']['closeSetTime']);
				ad['front']['closeSetTime']=null;
			}
			C['ad']['top']['closeTime'].htmReplace('{seconds}',ad['front']['closeTime'],language['closeTime']);
			ad['front']['closeTime']--;
			if(ad['front']['closeTime']>0){
				ad['front']['closeSetTime']=setTimeout(adFrontCloseTime,1000);
			}
			else{
				C['ad']['top']['closeAd'].show();
				C['ad']['top']['closeTime'].hide();
			}
		},
		/*
		 * calculationAdFrontTime
		 * 功能：计算贴片广告倒计时时间以及广告是视频时用于倒计时
		*/
		calculationAdFrontTime=function(t){
			if(!isUndefined(t)){
				ad['front']['list'][ad['frontPlayI']]['time']=t;
			}
			ad['front']['countDown']=0;
			for(var i=ad['frontPlayI'];i<ad['front']['list'].length;i++){
				ad['front']['countDown']+=parseInt(ad['front']['list'][i]['time']);
			}
			C['ad']['top']['countDown'].htm(ad['front']['countDown']+language['second']);
		},
		/*
		 * clearAdFrontSetTime
		 * 功能：关闭贴片广告倒计时的计时器
		*/
		clearAdFrontSetTime=function(){
			if(adFrontSetTime){
				clearTimeout(adFrontSetTime);
				adFrontSetTime=null;
			}
		},
		/*
		 * adFrontCountDown
		 * 功能：贴片广告倒计时
		*/
		adFrontCountDown=function(){
			clearAdFrontSetTime();
			var adv=ad['front']['list'][ad['frontPlayI']];
			if(adv['time']>0){
				adv['time']--;
				ad['front']['countDown']--;
				C['ad']['top']['countDown'].htm(ad['front']['countDown']+language['second']);
				adFrontSetTime=setTimeout(adFrontCountDown,1000);
			}
			else{
				adFrontEnded();
			}
		},
		/*
		 * adFrontEnded
		 * 功能：贴片广告单个播放完成后进行判断是否需要播放下一个广告还是播放正片
		*/
		adFrontEnded=function(){
			if(ad['frontPlayI']<ad['front']['list'].length-1){
				ad['frontPlayI']++;
				adFrontPlay();
			}
			else{//贴片广告播放完成
				if(!isUndefined(ad['front']['node']) && ad['front']['node']!=null){
					CM.append(ad['front']['node']);
					player.closeLayer(ad['front']['node']);
					ad['front']['node']=null;
				}
				C['ad']['picture'].htm('');
				C['ad']['picture'].hide();
				ad['frontPlay']=false;
				C['ad'].hide();
				allBarShow();
				seekTime=vars['seek'];
				if(ad['front']['video']){//如果包含视频广告
					changeVideo(vars['video']);
				}
				else{
					player.play();
				}
				eventTarget('frontAdEnded',true);
				frontAdPlay=false;//贴片广告播放状态
			}
		},
		/*
		 * adVideoPlay
		 * 功能：贴片广告播放视频
		*/
		adVideoPlay=function(vstr){
			if(isUndefined(vstr)) return;
			C['ad']['picture'].hide();
			seekTime=0;
			if(video.attr('src') || video.htm()){
				player.pause();
			}
			if(video.attr('src')){
				video.attr('src','');
				video.removeAttr('src');
			}
			video.htm('');
			if(vars['plug'] && !canPlay(vstr)){
				switch(vars['plug']){
					case 'hls.js':
						hlsPlayer(vstr);
						break;
					case 'flv.js':
						flvPlayer(vstr);
						break;
					case 'mpegts.js':
						mpegtsPlayer(vstr);
						break;
					default:
						CT.error={code:10,message:language['error']['emptied']};
						eventTarget('error',CT.error);//注册监听error
						showError();
						break;
				}
			}
			else{
				video.attr('src',vstr);
			}
			ad['front']['video']=true;//设置包含视频广告
			C['ad']['link'].show();
			C['ad']['bottom']['mutedAndExit'].show();
		},
		/*
		 * adPicturePlay
		 * 功能：贴片广告播放图片
		*/
		adPicturePlay=function(adv){
			if(isUndefined(adv['file'])) return;
			var img = null;
			var imgloadNum=0;
			var imgLoadHandler=function(){
				C['ad']['picture'].show();
				C['ad']['picture'].htm('');
				img = createlImg(adv['file']);
				C['ad']['picture'].append(img);
				img.addListener('load',function(){
					var w=this.getWidth(),h=this.getHeight();
					if(w>0 || h>0){
						if(adv['link']){
							var a=createlA('',adv['link']);
							a.append(this);
							C['ad']['picture'].append(a);
						}
						else{
							C['ad']['picture'].append(this)
						}
						img.unbind();
						img.click(function(){
							eventTarget('frontAdClick',adv);
						});
					}
					else{
						if(imgloadNum<10){
							imgloadNum++;
							imgLoadHandler();
						}
					}
				});
			};
			imgLoadHandler();		
			calculationAdFrontTime();//计算时间
			adFrontCountDown();//开启倒计时
			C['ad']['bottom']['mutedAndExit'].hide();
		},
		/*
		 * adNode
		 * 功能：贴片广告播放外部节点
		*/
		adNode=function(adv){
			if(isUndefined(adv['content'])) return;
			ad['front']['node']=player.layer(adv);
			ad['front']['node'].css({'z-index':1});
			C['ad'].append(ad['front']['node']);
			ad['front']['node'].unbind();
			ad['front']['node'].click(function(){
				eventTarget('frontAdClick',adv);
			});
			calculationAdFrontTime();//计算时间
			adFrontCountDown();//开启倒计时
			C['ad']['bottom']['mutedAndExit'].hide();
		},
		/*
		 * frontVideoClickHandler
		 * 功能：视频广告上方层单击事件
		*/
		frontVideoClickHandler=function(){
			var url=C['ad']['link'].attr('data-link');
			window.open(url,'_blank');
		},
		/*
		 * closeFrontAd
		 * 功能：关闭贴片广告
		*/
		closeFrontAd=function(){
			if(!isUndefined(ad) && !isUndefined(ad['front']) && !isUndefined(ad['front']['closeButtonClick'])){
				if(valType(ad['front']['closeButtonClick'])=='function'){
					try{
						ad['front']['closeButtonClick']();
					}
					catch(event){console.error(event);}
				}
				else if(valType(ad['front']['closeButtonClick'])=='string'){
					try{
						eval(ad['front']['closeButtonClick']+ '()');
					}
					catch(event){console.error(event);}
				}
			}
			else{
				player.closeFrontAd();
			}
		},
		/*
		 * adPausePlay
		 * 功能：播放暂停广告
		*/
		adPausePlay=function(){
			closePauseAd();
			if(!isUndefined(ad) && !isUndefined(ad['pause']) && !isUndefined(ad['pause']['list']) && ad['pause']['list'].length-1>=ad['pausePlayI']){
				var adv=ad['pause']['list'][ad['pausePlayI']];
				if(!isUndefined(ad['pause']['close']) && ad['pause']['close']){
					ad['pauseClose']=createlButton('ck-pause-close');
					ad['pauseClose'].click(function(){
						C['centerPlay'].show();
						C['buffer'].hide();
						closePauseAd();
					});
				}
				if(!isUndefined(adv['file'])){//图片
					var img = null;
					var imgloadNum=0;
					var imgLoadHandler=function(){
						img = createlImg(adv['file']);
						C['adPause'].show();
						C['adPause'].htm('');
						C['adPause'].append(img);
						img.addListener('load',function(){
							var w=this.getWidth(),h=this.getHeight();
							if(w>0 || h>0){
								C['adPause'].htm('');
								if(adv['link']){
									var a=createlA('',adv['link']);
									a.append(this);
									C['adPause'].append(a);
								}
								else{
									C['adPause'].append(this)
								}
							
								C['adPause'].css({
									'width':w+'px',
									'height':h+'px'
								});
								if(ad['pauseClose']){
									C['adPause'].append(ad['pauseClose']);
								}
								img.click(function(){
									eventTarget('pauseAdClick',adv);
								});
							}
							else{
								if(imgloadNum<10){
									imgloadNum++;
									imgLoadHandler();
								}
							}
						});
					};
					imgLoadHandler();
				}
				else if(!isUndefined(adv['content'])){
					ad['pause']['node']=player.layer(adv);
					if(ad['pauseClose']){
						ad['pause']['node'].append(ad['pauseClose']);
					}
					ad['pause']['node'].click(function(){
						eventTarget('pauseAdClick',adv);
					});
				}
				eventTarget('pauseAd',adv);
				if(!isUndefined(adv['time']) && adv['time']>0){
					adPauseSetTime=setTimeout(function(){
						ad['pausePlayI']++;
						if(ad['pausePlayI']>ad['pause']['list'].length-1){
							ad['pausePlayI']=0;
						}
						adPausePlay();
					},adv['time']*1000)
				}
			}
		},
		/*
		 * closePauseAd
		 * 功能：关闭暂停广告
		*/
		closePauseAd=function(){
			if(adPauseSetTime){
				clearTimeout(adPauseSetTime);
				adPauseSetTime=null;
			}
			C['adPause'].hide();
			if(!isUndefined(ad) && !isUndefined(ad['pauseClose']) && ad['pauseClose']!=null){
				ad['pauseClose'].remove();
				ad['pauseClose']=null;
			}
			if(!isUndefined(ad) && !isUndefined(ad['pause']) && !isUndefined(ad['pause']['node']) &&  ad['pause']['node']){
				ad['pause']['node'].unbind();
				player.closeLayer(ad['pause']['node']);
				eventTarget('pauseAdClose');
			}
		};
		/*
		 * player
		 * 功能:开放接口,向外部提供播放器的交互功能列表，包含获取，执行，监听这三种功能
		*/
		var player={
			/*
			 * width
			 * 功能：获取或设置播放器宽度
			 * 这是一个动态的值，会随着全屏切换或播放器本身尺寸变化而变化
			 * @value，支持数字，字符，当存在@value时则设置播放器宽度，@value为空时则获取播放器宽度
			*/
			width:function(value){
				if(!isUndefined(value)){
					if(valType(value)=='number'){
						value+='px';
					}
					CT.css({'width':value});
					checkVideoRotate();
				}
				return CK.getWidth();
			},
			/*
			 * height
			 * 功能：获取或设置播放器显示高度
			 * 这是一个动态的值，会随着全屏切换或播放器本身尺寸变化而变化
			 * @value，支持数字，字符，当存在@value时则设置播放器高度，@value为空时则获取播放器高度
			*/
			height:function(value){
				if(!isUndefined(value)){
					if(valType(value)=='number'){
						value+='px';
					}
					CT.css({'height':value});
					checkVideoRotate();
				}
				return CK.getHeight();
			},
			/*
			 * videoWidth
			 * 功能：获取视频本身的宽度，这是一个固定值
			*/
			videoWidth:function(){
				if(video){
					return video.videoWidth;
				}
				return null;
			},
			/*
			 * videoHeight
			 * 功能：获取视频本身的高度，这是一个固定值
			*/
			videoHeight:function(){
				if(video){
					return video.videoHeight;
				}
				return null;
			},
			/*
			 * height
			 * 功能：获取或设置播放器显示高度
			 * 这是一个动态的值，会随着全屏切换或播放器本身尺寸变化而变化
			 * @value，支持数字，字符，当存在@value时则设置播放器高度，@value为空时则获取播放器高度
			*/
			zoom:function(fn){
				if(!isUndefined(fn)){
					if(valType(fn)=='function'){
						newEvent.addEventListener('zoom',fn);
					}
					if(valType(fn)=='number'){
						var arr=[C['topBar']['zoomEle']['zoom50'],C['topBar']['zoomEle']['zoom75'],C['topBar']['zoomEle']['zoom100']];
						for(var i=0;i<arr.length;i++){
							arr[i].removeClass('ck-top-bar-zoom-container-focus')
						}
						switch(fn){
							case 50:
								arr[0].addClass('ck-top-bar-zoom-container-focus');
								break;
							case 75:
								arr[1].addClass('ck-top-bar-zoom-container-focus');
								break;
							case 100:
								arr[2].addClass('ck-top-bar-zoom-container-focus');
								break;
						}
						if(fn==50 || fn==75 || fn==100){
							nowZoom=fn;
							checkVideoRotate();//调整视频尺寸
							eventTarget('zoom',fn);
						}
					}
				}
				return player;
			},
			/*
			 * rotate
			 * 提供给播放器外部使用
			 * 进行旋转
			 * @obj为新的播放配置对象
			*/
			rotate:function(fn){
				if(!isUndefined(fn)){
					if(valType(fn)=='function'){
						newEvent.addEventListener('rotate',fn);
					}
					if(valType(fn)=='number'){
						var arr=[0,90,180,270];
						var rot=0;
						if(arr.length>fn && fn>0){
							rot=arr[fn];
						}
						else{
							if(arr.indexOf(fn)>-1){
								rot=fn;
							}
						}
						if(rot!=nowRotate){
							eventTarget('rotate',rot);
							nowRotate=rot;
							checkVideoRotate();//调整视频尺寸
						}
					}
				}
			},
			/*
			 * vars
			 * 提供给播放器外部使用
			 * 修改vars，动态切换视频地址
			 * @obj为新的播放配置对象
			*/
			vars:function(obj,val){
				if(isUndefined(obj)){
					return;
				}
				if(valType(obj)=='string' && !isUndefined(val)){
					var temp={};
					temp[obj]=val;
					obj=temp;
				}
				var isChange=false;
				for(var k in obj){
					if(k in vars && obj[k]!=vars[k]){
						isChange=true;
						break;
					}
				}
				if(isChange){
					vars = standardization(vars, obj);
				}
				if(!isUndefined(obj['video'])){
					isChangeDef=true;
					changeVideo(vars['video']);
				}
				if(!isUndefined(obj['title'])){
					C['topBar']['titleEle'].htm(obj['title']);
				}
			},
			/*
			 * 提供给播放器外部使用
			 * 功能：监听视频准备加载
			*/
			loadstart:function(fn){
				if(!isUndefined(fn) && valType(fn)=='function'){
					newEvent.addEventListener('loadstart',fn);
				}
			},
			/*
			 * 提供给播放器外部使用
			 * 功能：监听视频加载
			*/
			loadeddata:function(fn){
				if(!isUndefined(fn) && valType(fn)=='function'){
					newEvent.addEventListener('loadeddata',fn);
				}
			},
			/*
			 * 提供给播放器外部使用
			 * 功能：监听视频元数据加载成功
			*/
			loadedMetaData:function(fn){
				if(!isUndefined(fn) && valType(fn)=='function'){
					newEvent.addEventListener('loadedMetaData',fn);
				}
			},
			/*
			 * 提供给播放器外部使用
			 * 功能：监听视频可以播放
			*/
			canplay:function(fn){
				if(!isUndefined(fn) && valType(fn)=='function'){
					newEvent.addEventListener('canplay',fn);
				}
			},		
			/*
			 * 提供给播放器外部使用
			 * 功能：执行播放操作/监听播放
			 * @fn为函数时则监听视频播放，@fn为空时，则控制视频播放
			*/
			play:function(fn){
				if(valType(fn)=='object'){
					if(fn['target']==C['bar']['playAndPause']['play'] || fn['target']==C['bar']['playAndPause']['refresh']){
						playType='button';
					}
				}
				if(!isUndefined(fn) && valType(fn)=='function'){
					newEvent.addEventListener('play',fn);
				}
				else{
					if(loadedmetadataNum>0 && video){
						try{video.play();}catch(event){console.error(event)}
					}
				}
				return player;
			},
			/*
			 * puase
			 * 功能：执行暂停操作/监听暂停
			 * @fn为函数时则监听视频暂停，@fn为空时，则控制视频暂停
			*/
			pause:function(fn){
				if(!isUndefined(fn) && valType(fn)=='function'){
					newEvent.addEventListener('pause',fn);
				}
				else{
					if(loadedmetadataNum>0 && video){
						try{video.pause();}catch(event){console.error(event)}
					}
				}
				return player;
			},
			/*
			 * playOrPause
			 * 功能：在播放和暂停动作之间进行切换
			*/
			playOrPause:function(){
				if(paused){
					player.play();
				}
				else{
					player.pause();
				}
				return player;
			},
			/*
			 * volume
			 * 功能：调节和监听以及获取音量
			 * @fn为函数时，则进行音量变化的监听，@fn为数字时，则进行音量调节，@fn为空时，则获取当前音量
			*/
			volume:function(fn){
				if(!isUndefined(fn)){
					if(valType(fn)=='function'){
						newEvent.addEventListener('volume', fn);
					}
					else if(valType(fn)=='number' && video){
						if(fn<0)fn=0;
						if(fn>1)fn=1;
						if(video.muted){
							video.volume=fn*.5;
						}
						video.volume=fn;
					}
				}
				else if(video){
					return video.volume;
				}
				return player;
			},
			/*
			 * muted
			 * 功能：执行静音操作/监听静音
			 * @fn为函数时则监听视频静音，@fn为空时，则控制视频静音
			*/
			muted:function(fn){
				if(!isUndefined(fn) && valType(fn)=='function'){
					newEvent.addEventListener('muted', fn);
				}
				else if(video){
					video.muted=true;
				}
				return player;
			},
			/*
			 * exitMuted
			 * 功能：执行取消静音操作
			*/
			exitMuted:function(){
				if(video){
					video.muted=false;
					if(video.volume==0){
						player.volume(vars['volume']>0?vars['volume']:.8);
					}
				}
				return player;
			},
			/*
			 * time
			 * 功能：获取当前播放时间/监听播放时间
			 * @fn为函数时监听当前播放时间，为空时获取当前播放时间
			*/
			time:function(fn){
				if(!isUndefined(fn) && valType(fn)=='function'){
					newEvent.addEventListener('time',fn);
				}
				else if(video){
					return video.currentTime || 0;
				}
			},
			/*
			 * seek
			 * 功能：监听跳转/执行跳转操作
			 * @fn为函数时则监听视频跳转动作，@fn为数字时执行跳转操作
			*/
			seek:function(fn){
				if(!isUndefined(fn)){
					if(valType(fn)=='function'){
						newEvent.addEventListener('seek',fn);
					}
					if(valType(fn)=='number' && video){
						switch(vars['timeScheduleAdjust']){
							case 0://禁止拖动
								message(language['timeScheduleAdjust']['prohibit']);
								return;
								break;
							case 1://可以拖动
								break;
							case 2://只能前进（向右拖动
								if(fn<oldTime){
									message(language['timeScheduleAdjust']['prohibitBackOff']);
									return;
								}
								break;
							case 3://是只能后退
								if(fn>oldTime){
									message(language['timeScheduleAdjust']['prohibitForward']);
									return;
								}
								break;
							case 4://只能前进但能回到第一次拖动时的位置
								if(fn<firstSeekTime){
									waitingMessage=false;
									video.currentTime=firstSeekTime;
									message(language['timeScheduleAdjust']['prohibitLookBack']);
									return;
								}
								break;
							case 5://看过的地方可以随意拖动
								if(fn>maxSeeTime){
									waitingMessage=false;
									video.currentTime=maxSeeTime;
									message(language['timeScheduleAdjust']['prohibitForwardNotViewed']);
									return;
								}
								break;
						}
						pSliderMouseDown=false;
						video.currentTime=fn;
					}
				}
				return player;
			},
			/*
			 * buffer
			 * 功能：监听视频缓冲
			 * @fn为监听执行的函数
			*/
			buffer:function(fn){
				if(!isUndefined(fn) && valType(fn)=='function'){
					newEvent.addEventListener('buffer',fn);
				}
				return player;
			},
			/*
			 * ended
			 * 功能：监听视频播放结束
			 * @fn为监听执行的函数
			*/
			ended:function(fn){
				if(!isUndefined(fn) && valType(fn)=='function'){
					newEvent.addEventListener('ended',fn);
				}
				return player;
			},
			/*
			 * 提供给播放器外部使用
			 * 功能：监听点击下一集按钮的动作
			*/
			next:function(fn){
				if(!isUndefined(fn) && valType(fn)=='function'){
					newEvent.addEventListener('next',fn);
				}
				return player;
			},	
			/*
			 * error
			 * 功能，监听播放器在加载视频过程和播放视频过程中出现的错误
			 * @fn为监听执行的函数
			*/
			error:function(fn){
				if(!isUndefined(fn) && valType(fn)=='function'){
					newEvent.addEventListener('error',fn);
				}
				return player;
			},
			/*
			 * emptied
			 * 功能，监听播放器播放的视频文件发生故障并且文件突然不可用时
			 * @fn为监听执行的函数
			*/
			emptied:function(fn){
				if(!isUndefined(fn) && valType(fn)=='function'){
					newEvent.addEventListener('emptied',fn);
				}
				return player;
			},
			/*
			 * duration
			 * 功能：获取视频总时间/监听视频总时间
			 * @fn为函数时执行监听视频总时间，为空时获取视频总时间
			*/
			duration:function(fn){
				if(!isUndefined(fn) && valType(fn)=='function'){
					newEvent.addEventListener('duration',fn);
				}
				else{
					return duration || 0;
				}
			},
			/*
			 * playbackRate
			 * 功能：监听或返回视频播放速度
			 * @fn为函数时执行监听视频播放速度，为数字时切换播放速度，为空时获取视频播放速度
			*/
			playbackRate:function(fn){
				if(!isUndefined(fn)){
					if(valType(fn)=='function'){
						newEvent.addEventListener('playbackRate',fn);
					}
					else if(valType(fn)=='number' && video){
						video.playbackRate=fn;
						CT.playbackRate=fn;
						eventTarget('playbackRate',fn);
						changePlaybackrateVal(fn);
					}
					
				}
				else{
					return video.playbackRate;
				}
			},
			/*
			 * track
			 * 功能：监听或返回视频的字幕
			 * @fn为函数时执行监听视频字幕，为数字时切换字幕，为空时获取字幕
			*/
			track:function(fn){
				if(!isUndefined(fn)){
					if(valType(fn)=='function'){
						newEvent.addEventListener('track',fn);
					}
					else if(valType(fn)=='number' && video){
						var track=null;
						if(!isUndefined(video.textTracks)){
							track=video.textTracks;
						}
						if(track && valType(track)=='texttracklist'){
							var arr=vars['track'];
							for(var i=track.length-1;i>-1;i--){
								video.find('track').eq(i).remove();
							}
							for(var i=0;i<arr.length;i++){
								var def='';
								arr[i]['default']=false;
								if(i==fn){
									def=' default';
									arr[i]['default']=true;
								}
								track+='<track src="'+arr[i]['src']+'" srclang="'+arr[i]['srclang']+'" kind="'+arr[i]['kind']+'" label="'+arr[i]['label']+'"'+def+'>';
							}
							if(video){
								var html=video.htm()+track;
								video.htm(html);
							}
							changeTrackVal(fn);
						}
					}
					
				}
				else{
					return CT.track;
				}
			},
			/*
			 * fastBack
			 * 功能：快退
			 * @num为数字时快退指定秒数，为空时快退20秒
			*/
			fastBack:function(num){
				if(duration==0) return;
				if(isUndefined(num) || valType(num)!='number'){
					num=20;
				}
				var time=player.time();
				if(time-num>0){
					time-=num;
				}
				else{
					time=0;
				}
				player.seek(time);
				return player;
			},
			/*
			 * fastNext
			 * 功能：快进
			 * @num为数字时快进指定秒数，为空时快进20秒
			*/
			fastNext:function(num){
				if(duration==0) return;
				if(isUndefined(num) || valType(num)!='number'){
					num=20;
				}
				var time=player.time();
				if(time+num<duration){
					time+=num;
				}
				else{
					time=duration;
				}
				player.seek(time);
				return player;
			},
			/*
			 * definition
			 * 功能：监听清晰度切换/操作清晰度切换
			 * @fn为一个函数时监听清晰度切换，为数字时执行清晰度切换，数字为清晰度的编号，从0开始
			*/
			definition:function(fn){
				if(!isUndefined(fn)){
					if(valType(fn)=='function'){
						newEvent.addEventListener('definition',fn);
					}
					if(valType(fn)=='number'){
						changeDefinition(fn);
					}
				}
				return player;
			},
			/*
			 * fps
			 * 功能：监听Fps
			 * @fn为一个函数时监听fps，为空时获取fps
			*/
			fps:function(fn){
				if(!isUndefined(fn) && valType(fn)=='function'){
					newEvent.addEventListener('fps',fn);
				}
				return CT.fps;
			},
			/*
			 * playback
			 * 功能：监听回放操作/返回需要回放的时间点
			*/
			playback:function(fn){
				if(!isUndefined(fn)){
					if(valType(fn)=='function'){
						newEvent.addEventListener('playback',fn);
					}
				}		
				else{
					return playbackTime;
				}
				return player;
			},
			/*
			 * backLive
			 * 功能：显示/隐藏回到直播的按钮
			*/
			backLive:function(fn){
				if(!isUndefined(fn)){
					if(valType(fn)=='function'){
						newEvent.addEventListener('backLive',fn);
					}
					if(valType(fn)=='boolean'){
						if(bool){
							C['bar']['backLive'].show();
						}
						else{
							C['bar']['backLive'].hide();
						}
					}
				}
				return player;
			},
			/*
			 * full
			 * 功能：监听全屏功能/执行全屏操作
			 * @fn为函数时监听用户执行全屏操作，为空时执行全屏操作
			*/
			full:function(fn){
				if(!isUndefined(fn) && valType(fn)=='function'){
					newEvent.addEventListener('full',fn);
				}
				else{
					if(smallWindowsState){
						return;
					}
					if(CT.theatre){
						player.exitTheatre();
					}
					if(CT.webFull){
						player.exitWebFull();
					}				
					var requestMethod = CK.requestFullScreen || //W3C
		                CK.webkitRequestFullScreen || //Chrome等
		                CK.mozRequestFullScreen || //FireFox
		                CK.oRequestFullscreen ||
		                CK.msRequestFullscreen; //IE11
		            if (!isUndefined(requestMethod)) {
		                requestMethod.call(CK);
		            }
		            else if (!isUndefined(window.ActiveXObject)) { //for Internet Explorer
		                var wscript = new ActiveXObject('WScript.Shell');
		                if (wscript !== null) {
		                    wscript.SendKeys('{F11}');
		                }
		            }
		            else if(!isUndefined(video.webkitEnterFullscreen) || !isUndefined(video.webkitEnterFullScreen)){
		            	var fullScreen=video.webkitEnterFullscreen || video.webkitEnterFullScreen;
		            	fullScreen.call(video);
		            }
		            player.zoom(100);
				}
				return player;
			},
			/*
			 * exitFull
			 * 功能：	退出全屏
			*/
			exitFull:function(){
				var exitFullFun = document.exitFullscreen || //W3C
		            document.mozCancelFullScreen || //FireFox
		            document.webkitExitFullscreen || //Chrome等
		            document.oCancelFullScreen || //Chrome等
		            document.msExitFullscreen; //IE11
		        if (!isUndefined(exitFullFun)) {
		            exitFullFun.call(document);
		        } 
		        else if (!isUndefined(window.ActiveXObject)) { //for Internet Explorer
		            var wscript = new ActiveXObject('WScript.Shell');
		            if (wscript !== null) {
		                wscript.SendKeys('{F11}');
		            }
		        }
		        return player;
			},
			/*
			 * fullOrExit
			 * 功能：在全屏和退出全屏之间进行切换
			*/
			fullOrExit:function(){
				var fullState = document.fullScreen || document.mozFullScreen || document.webkitIsFullScreen || document.msFullscreenElement;
				if(fullState){
					player.exitFull();
				}
				else{
					player.full();
				}
				return player;
			},
			/*
			 * webFull
			 * 功能：监听网页全屏/执行网页全屏
			 * @fn为函数时监听网页全屏，为空时执行网页全屏
			*/
			webFull:function(fn){
				if(!isUndefined(fn) && valType(fn)=='function'){
					newEvent.addEventListener('webfull',fn);
				}
				else{
					exitSmallWindows();
					if(!isUndefined(vars['theatre'])){
						C['bar']['theatreAndExit'].hide();
					}
					CK.css({
						'position':'fixed',
						'top':0,
						'left':0,
						'height':'100%',
						'z-index':getMaxZIndex()+1
					});
					C['bar']['webFullAndExit']['webFull'].hide();
					C['bar']['webFullAndExit']['exitWebFull'].show();
					CT.webFull=true;
					eventTarget('webfull',true);
					checkVideoRotate();
				}
				return player;
			},
			/*
			 * exitWebFull
			 * 功能：退出网页全屏
			*/
			exitWebFull:function(){
				CK.css({
					'position':'relative'
				});
				C['bar']['webFullAndExit']['webFull'].show();
				C['bar']['webFullAndExit']['exitWebFull'].hide();
				if(!isUndefined(vars['theatre'])){
					C['bar']['theatreAndExit'].show();
					C['bar']['theatreAndExit']['theatre'].show();
					C['bar']['theatreAndExit']['exitTheatre'].hide();
				}
				CK.attr('style','');
				CT.webFull=false;
				eventTarget('webfull',false);
				checkVideoRotate();
				return player;
			},
			/*
			 * theatre
			 * 功能：监听剧场模式/执行剧场模式
			 * @fn为函数时执行监听剧场模式，为空时执行剧场模式操作
			*/
			theatre:function(fn){
				if(!isUndefined(fn) && valType(fn)=='function'){
					newEvent.addEventListener('theatre',fn);
				}
				else{
					if(valType(vars['theatre'])=='array' && vars['theatre'].length==2){
						var fun=vars['theatre'][0];
						if(valType(fun)=='function'){
							fun();
						}
						if(valType(fun)=='string'){
							eval(fun+ '()');
						}
					}
					else{
						exitSmallWindows();
						CK.css({
							'position':'absolute',
							'top':CT.offset()['top'],
							'left':0,
							'height':CT.getHeight()+'px',
							'z-index':getMaxZIndex()+1
						});
						C['bar']['theatreAndExit']['theatre'].hide();
						C['bar']['theatreAndExit']['exitTheatre'].show();
					}
					
					CT.theatre=true;
					eventTarget('theatre',true);
					checkVideoRotate();
				}
				return player;
			},
			/*
			 * exitTheatre
			 * 功能：退出剧场模式
			*/
			exitTheatre:function(){
				if(valType(vars['theatre'])=='array' && vars['theatre'].length==2){
					var fun=vars['theatre'][1];
					if(valType(fun)=='function'){
						fun();
					}
					if(valType(fun)=='string'){
						eval(fun+ '()');
					}
				}
				else{
					CK.css({
						'position':'relative'
					});
					C['bar']['theatreAndExit']['theatre'].show();
					C['bar']['theatreAndExit']['exitTheatre'].hide();
					CK.attr('style','');
					CT.theatre=false;
				}
				eventTarget('theatre',false);
				checkVideoRotate();
				return player;
			},
			/*
			 * smallWindows
			 * 功能：监听小窗口模式开启关闭/执行开启关闭小窗口模式
			*/
			smallWindows:function(fn){
				if(!isUndefined(fn)){
					if(valType(fn)=='function'){
						newEvent.addEventListener('smallWindows',fn);
					}
					if(valType(fn)=='boolean'){
						if(fn){
							C['rightBar']['smallwindows']['open'].show();
							C['rightBar']['smallwindows']['close'].hide();
							CT.smallWindows=true;
						}
						else{
							C['rightBar']['smallwindows']['open'].hide();
							C['rightBar']['smallwindows']['close'].show();
							CT.smallWindows=false;
						}
						eventTarget('smallWindows',CT.smallWindows);
						checkVideoRotate();
					}
				}
				return CT.smallWindows;
			},
			/*
			 * loop
			 * 功能：监听循环/执行开启关闭循环功能
			 * @fn为函数时是监听循环，@fn为boolean类型时，为true开启循环，为false关闭循环，@fn为空时获取当前循环状态
			*/
			loop:function(fn){
				if(!isUndefined(fn)){
					if(valType(fn)=='function'){
						newEvent.addEventListener('loop',fn);
					}
					if(valType(fn)=='boolean' && video){
						if(fn){
							C['rightBar']['loop']['open'].show();
							C['rightBar']['loop']['close'].hide();
							CT.loop=true;
							video.attr('loop','loop');
							eventTarget('loop',true);
						}
						else{
							C['rightBar']['loop']['open'].hide();
							C['rightBar']['loop']['close'].show();
							CT.loop=false;
							video.attr('loop',false);
							video.removeAttr('loop');
							eventTarget('loop',false);
						}
					}
				}
				return CT.loop;
			},		
			/*
			 * screenshot
			 * 功能：截图 
			 * @fn为函数时监听截图功能，@fn为空时进行截图
			*/
			screenshot:function(fn){
				if(!isUndefined(fn) && valType(fn)=='function'){
					newEvent.addEventListener('screenshot',fn);
				}
				else{
					if(!isUndefined(vars['screenshot']) && vars['screenshot'] && video){
						try {
							message(language['screenshotStart'],true);
							var newCanvas = $(document.createElement('canvas'));
							newCanvas.width = video.videoWidth;
							newCanvas.height = video.videoHeight;
							newCanvas.getContext('2d').drawImage(video, 0, 0, video.videoWidth, video.videoHeight);
							var base64 = newCanvas.toDataURL('image/png');
							eventTarget('screenshot',base64);
							if(screenshotImg){
								screenshotImg.remove();
								screenshotImg=null;
							}
							screenshotImg=createlImg(base64);
							screenshotImg.addListener('load',function(){
								C['screenshot']['img'].append(screenshotImg);
								C['screenshot'].show().css({'width':this.getWidth()+'px'});
							});
							var newBlob=dataURLtoBlob(base64);
							var url = URL.createObjectURL(newBlob);
							var downName='video';
							if(vars['title']){
								downName=vars['title'];
							}
							C['screenshot']['bar']['down'].attr('href',url).attr('download',downName);
							return base64;
						}catch(error) {
							message(language['error']['screenshot'],true);
						}
					}
					else{
						message(language['screenshotClose'],true);
					}
				}
				return player;
			},
			/*
			 * closeScreenshot
			 * 功能：关闭截图显示容器
			*/
			closeScreenshot:function(bool){
				if(!isUndefined(bool) && valType(bool)=='boolean' && !bool){
					C['screenshot'].show();
				}
				else{
					C['screenshot'].hide();
				}
				return player;
			},
			/*
			 * layer
			 * 功能：在播放器中添加一个层
			*/
			layer:function(obj,bar){
				if(isUndefined(obj)){
					return null;
				}
				var cBar=false;
				if(!isUndefined(bar)){
					cBar=bar;
				}
				var ele=null;
				if(valType(obj)=='object'){
					if('class' in obj && 'content' in obj){
						ele=createlDiv(obj['class']);
						ele.htm(obj['content']);
					}
					else if('content' in obj){
						obj=obj['content'];
					}
				}
				if(valType(obj)=='string'){
					if(obj.substr(0,1)=='.' || obj.substr(0,1)=='#'){
						ele=getElement(obj);
					}
					else{
						ele=createlDiv('ck-layer');
						ele.htm(obj);
					}
				}
				if(ele){
					var zIndex=ele.css('z-index');
					if(zIndex=='auto'){
						zIndex=101;
					}
					if(zIndex<101) zIndex=101;
					if(zIndex>200) zIndex=200;
					if(isUndefined(ele.cssDisplay)){
						ele.cssPosition=ele.css('position');
						ele.cssDisplay=ele.css('display');
					}
					ele.css({
						'position': 'absolute',
						'z-index':zIndex
					});
					if(!cBar){
						CM.append(ele);
					}
					else{
						C['bar'].append(ele);
					}
					ele.CK=CK;
					ele.show();
				}
				return ele;
			},
			/*
			 * closeLayer
			 * 功能：关闭一个层
			*/
			closeLayer:function(ele){
				if(valType(ele)=='string'){
					var temp=$(ele);
					if(!isUndefined(temp)){
						if(temp.length>0){
							ele=temp.eq(0);
						}
					}
				}
				if(!isUndefined(ele) && valType(ele)=='htmlobject'){
					if(!isUndefined(ele.captureParentNode)){
						var eleP=ele.captureParentNode;
						if (ele.nextElement) {
							try {
								eleP.insertBefore(ele, ele.nextElement);
							}
							catch (event) {
								eleP.append(ele);
							}
						}
						else if (ele.prevElement) {
							try {
								eleP.insertAfter(ele, ele.prevElement);
							}
							catch (event) {
								eleP.append(ele);
							}
						}
						else {
							eleP.append(ele);
						}
						ele.css({'display':ele.cssDisplay});
					}
					else{
						ele.remove();
					}
					if(!isUndefined(ele.cssDisplay)){
						ele.css({
							'display':ele.cssDisplay,
							'position':ele.cssPosition
						});
					}
				}
				return player;
			},
			/*
			 * message
			 * 在播放器内部显示一条提示信息
			*/
			message:function(str){
				message(str);
				return player;
			},
			/*
			 * closeFrontAd
			 * 功能：关闭贴片广告 
			*/
			closeFrontAd:function(){
				if(frontAdPlay && !isUndefined(ad) && !isUndefined(ad['front']) && !isUndefined(ad['front']['list'])){
					ad['frontPlayI']=ad['front']['list'].length-1;
					adFrontEnded();
					clearAdFrontSetTime();
				}
				return player;
			},
			/*
			 * addListener
			 * 功能：监听功能 
			*/
			addListener:function(name,fn){
				newEvent.addEventListener(name,fn);
			},
			/*
			 * removeListener
			 * 功能：监听功能 
			*/
			removeListener:function(name,fn){
				newEvent.removeEventListener(name,fn);
			},
			/*
			 * bar
			 * 功能：显示/关闭底部控制栏 
			*/
			bar:function(bool){
				barShow=bool;
				if(bool){
					C['bar'].show();
				}
				else{
					C['bar'].hide();
				}
			},
			/*
			 * rightBar
			 * 功能：显示/关闭右边控制栏 
			*/
			rightBar:function(bool){
				rightBarShow=bool;
				if(bool){
					C['rightBar'].show();
				}
				else{
					C['rightBar'].hide();
				}
			},
			/*
			 * mouseActive
			 * 功能，监听鼠标是否经过播放器
			*/
			mouseActive:function(fn){
				if(!isUndefined(fn) && valType(fn)=='function'){
					newEvent.addEventListener('mouseActive',fn);
				}
				return this;
			},
			/*
			 * cookie
			 * 功能，读取cookie
			*/
			cookie:function(name){
				if(name=='delete'){
					cookie(ckplayerCookie,'delete',vars['domain']);
					return null;
				}
				if(!isUndefined(name) && name){
					name=name.replace(/[ ]*,[ ]*|[ ]+/g, '').replace(/[ ]*;[ ]*|[ ]+/g, '');
				}
				var cStr=cookie(ckplayerCookie);
				if(cStr){
					var arr=stringToArray(cStr);
					var tempArr=[];
					var cTime=parseInt(Date.now()*0.001);
					for(i=0;i<arr.length;i++){
						if(parseInt(arr[i][2])>cTime){
							tempArr.push(arr[i]);
						}
					}
					arr=tempArr;
					if(arr.length>0){
						cookie(ckplayerCookie,'delete',vars['domain'],vars['cookiePath']);
						cookie(ckplayerCookie,arrayToString(arr),vars['domain'],vars['cookiePath']);
						for(var i=0;i<arr.length;i++){
							arr[i]={
								name:arr[i][0],
								time:arr[i][1],
								expirationTime:date('Y-m-d H:i:s',arr[i][2]),
								expirationTimeStamp:arr[i][2]
							};
							if(!isUndefined(name) && name==arr[i]['name']){
								return arr[i];
							}
						}
						return arr;
					}
					else{
						cookie(ckplayerCookie,'delete',vars['domain'],vars['cookiePath']);
					}
				}
				return null;
			},
			/*
			 * visibilityState
			 * 功能：监听当前文档是否处于焦点状态 
			*/
			visibilityState:function(fn){
				if(!isUndefined(fn) && valType(fn)=='function'){
					if(!isUndefined(document.visibilityState)){
						fn(document.visibilityState === 'visible'?'show':'hidden');
					}
					newEvent.addEventListener('visibilityState',fn);
				}
				return this;
			},
			/*
			 * remove
			 * 功能：卸载播放器 
			*/
			remove:function(){
				var eventTemp={};
				if(eventTargetList!=null){
					for(var k in eventTargetList){
						eventTemp[k]=eventTargetList[k];
					}
					for(var k in eventTemp){
						var temp=eventTemp[k];
						if(temp){
							for(var i=0;i<temp.length;i++){
								var eve=temp[i];
								newEvent.removeEventListener(k,eve);
							}
						}
						
					}
				}
				if(frontAdPlay){
					this.closeFrontAd();
				}
				if(pugPlayer){
					pugPlayer.pause();
	                pugPlayer.unload();
	                pugPlayer.detachMediaElement();
	                pugPlayer.destroy();
					pugPlayer=null;
				}
				if(hls){
					hls.stopLoad();
					hls.detachMedia();
				}
				loadedmetadataNum=0;
				loadedTrack=false;
				isChangeDef=true;
				if(video){
					video.unbind();
					video.remove();
					video=null;
				}
				
				if(!isUndefined(C['menu'])){
					C['menu'].remove();
				}
				if(CT){
					CT.unbind();
					CT.htm('');
				}
				return null;
			}
		};
		/*
		 * checkBar
		 * 功能：完全获取vars值后判断控制栏上的按钮是否需要显示
		 * 完全获取vars是指视频加载成功后
		*/
		var checkBar=function(){
			//初始化判断
			if(vars['autoplay']){
				C['bar']['playAndPause']['play'].hide();
				C['bar']['playAndPause']['pause'].show();
			}
			else{
				C['bar']['playAndPause']['play'].show();
				C['bar']['playAndPause']['pause'].hide();
			}
			if(mutedState){
				C['bar']['vbox']['muted'].hide();
			}
			else{
				C['bar']['vbox']['exitMuted'].hide();
			}
			if(!isUndefined(vars['next'])){
				C['bar']['nextEpisode'].show();
			}
			else{
				C['bar']['nextEpisode'].hide();
			}
			if(!isUndefined(vars['webFull']) && vars['webFull'] && !CT.full){
				C['bar']['webFullAndExit'].show();
			}
			else{
				C['bar']['webFullAndExit'].hide();
			}
			if(!isUndefined(vars['theatre']) && !CT.full && !CT.webFull){
				C['bar']['theatreAndExit'].show();
			}
			else{
				C['bar']['theatreAndExit'].hide();
			}
			if(!isUndefined(vars['rightBar']) && vars['rightBar']){
				C['rightBar'].show();
			}
			else{
				C['rightBar'].hide();
			}
			if(!vars['live'] || valType(vars['live'])=='number'){
				pSliderMouseDown=false;
				progressDragX();//注册进度滑块动作
			}
			if(vars['live'] || valType(vars['live'])=='number'){
				C['time'].htm(language['live']);
				C['tempTime'].htm(language['live']);
			}
			if(valType(vars['smallWindows'])=='boolean' && !vars['smallWindows']){
				CT.smallWindows=false;
				C['rightBar']['smallwindows']['open'].hide();
				C['rightBar']['smallwindows']['close'].show();
			}
			if(CT.loop){
				C['rightBar']['loop']['open'].show();
				C['rightBar']['loop']['close'].hide();
			}
			else{
				C['rightBar']['loop']['open'].hide();
				C['rightBar']['loop']['close'].show();
			}
			if(!isUndefined(vars['backLive']) && vars['backLive']){
				C['bar']['backLive'].show();
			}
			if(vars['volume']==0){
				player.muted();
			}
		},
		/*
		 * loadLogo
		 * 功能：加载logo
		*/
		loadLogo=function(){
			if(!isUndefined(vars['logo']) && isUndefined(C['logo'])){
				C['logo']=createlDiv('ck-logo');
				createlImg(vars['logo']).addListener('load',function(){
					CM.append(C['logo']);
					C['logo'].append(this);
				}).addListener('error',function(){C['logo'].remove()});
			}
		},
		/*
		 * loadAbout
		 * 功能：加载loadAbout
		*/
		loadAbout=function(){
			if(!isUndefined(vars['information']) && !isUndefined(C['about']) && valType(vars['information'])=='object'){
				var obj=vars['information'];
				var k='';
				var ul=$(document.createElement('ul'));
				var temp='';
				for(k in obj){
					var title=createlDiv('div').attr('class','ck-about-title');
					var content=createlDiv('div').attr('class','ck-about-content');
					var li=$(document.createElement('li'));
					title.htm(k);
					content.htm(obj[k]);
					li.append(title);
					li.append(content);
					ul.append(li);
					temp+=k+':'+obj[k]+'\n';
				}			
				C['about'].append(ul);
				C['about']['textareaText']=temp;
				C['about']['textareaEle']=$(document.createElement('textarea'));
				CM.append(C['about']['textareaEle']);
				C['about']['textareaEle'].hide()
			}
		},
		/*
		 * loadMenu
		 * 功能：加载右键
		*/
		loadMenu=function(){
			if(!isUndefined(vars['menu']) && vars['menu']){
				rightMenu=vars['menu'];
			}
			C['menu']=createlDiv('ckplayer-ckplayer-menu');
			for(var i=0;i<rightMenu.length;i++){
				var m=rightMenu[i];
				var li=createlDiv('ck-li');
				if(!isUndefined(m['underline'])){
					li.addClass('ck-underline');
				}
				if(!isUndefined(m['link']) && !isUndefined(m['title'])){
					li.append(createlA(m['title'],m['link']));
					C['menu'].append(li);
				}
				else if(!isUndefined(m['javascript']) && !isUndefined(m['title'])){
					li.append(createlJsLink(m['title'],m['javascript']));
					C['menu'].append(li);
				}
				else if(!isUndefined(m['click']) && !isUndefined(m['title'])){
					var a=createlJsLink(m['title']);
					a.attr('data-click',m['click'].toString());
					a.click(function(){
						eval($(this).attr('data-click')+'()');
						C['menu'].hide();
					});
					li.append(a);
					C['menu'].append(li);
				}
				else if(!isUndefined(m['title'])){
					li.htm(m['title']);
					C['menu'].append(li);
				}
			}
			$('body').append(C['menu']);
			CM.oncontextmenu = function(eve) {
				var e = eve || window.event;
				var client = getClient(e);
				if (e.button == 2) {
					e.returnvalue = false;
					var x = client['x'] - 2;
					var y = client['y'] - 2;
					C['menu'].show();
					C['menu'].css({
						left: x + 'px',
						top: y + 'px'
					});
					return false;
				}
				return true;
			};
			var menuSetTimeOut=null;
			var clearTime=function(){
				if(menuSetTimeOut){
					clearTimeout(menuSetTimeOut);
					menuSetTimeOut=null;
				}
			};
			var menuMouseOut=function(){
				clearTime();
				menuSetTimeOut=setTimeout(function(){
					C['menu'].hide();
				},200);
			};
			C['menu'].mouseout(menuMouseOut);
			C['menu'].mouseover(clearTime);
		},
		/*
		 * aboutShow
		 * 功能：显示关于视频
		*/
		aboutShow=function(){
			C['about'].show();
			C['menu'].hide();
		},
		/*
		 * aboutHide
		 * 功能：显示关于视频
		*/
		aboutHide=function(){
			C['about'].hide();
		},
		/*
		 * aboutCopy
		 * 功能：拷贝关于视频内容
		*/
		aboutCopy=function(){
			C['about']['textareaEle'].show();
			C['about']['textareaEle'].select();
			document.execCommand('Copy'); // 执行浏览器复制命令
			C['about']['textareaEle'].hide();
			message(language['copySucceeded'],true);
		},
		/*
		 * getBarHeight
		 * 功能：获取控制栏的高
		*/
		getBarHeight=function(){
			var h=C['bar'].getHeight();
			if(parseFloat(C['bar']['pbox'].css('top'))<0){
				h-=parseInt(C['bar']['pbox'].css('top'));
			}
			return h;
		},
		/*
		 * hideBar
		 * 功能：隐藏控制栏，只在播放状态下有效
		*/
		hideBar=function(){
			if(mouseSetTime){
				clearTimeout(mouseSetTime);
				mouseSetTime=null;
			}
			mouseSetTime=setTimeout(function(){
				if(!paused){
					C['bar'].addClass('ck-bar-out');
					eventTarget('mouseActive',false);
					if(CT.full){
						CM.addClass('ck-nocursor');
					}
					if(C['rightBar'].css('display')=='block'){
						C['rightBar'].addClass('ck-right-bar-hide');
					}
					if(C['topBar'].css('display')=='block'){
						C['topBar'].addClass('ck-top-bar-hide');
					}
				}
				mouseSetTime=null;
			},vars['barHideTime']);
		},
		/*
		 * barMouseOut
		 * 功能：鼠标离开控制栏修改播放进度框样式
		*/
		barMouseOut=function(){
			if(!paused){
				C['bar']['pbox'].addClass('ck-bar-progress-out');
				hideBar();
			}
		},
		/*
		 * barMouseOver
		 * 功能：鼠标经过控制栏修改播放进度框样式
		*/
		barMouseOver=function(){
			if(!paused){
				C['bar']['pbox'].removeClass('ck-bar-progress-out');
				if(mouseSetTime){
					clearTimeout(mouseSetTime);
					mouseSetTime=null;
				}
			}
		},
		/*
		 * rightBarMouseOut
		 * 功能：鼠标离开右侧控制栏修改播放进度框样式
		*/
		rightBarMouseOut=function(){
			if(!paused){
				hideBar();
			}
		},
		/*
		 * rightBarMouseOver
		 * 功能：鼠标经过右侧控制栏修改播放进度框样式
		*/
		rightBarMouseOver=function(){
			if(!paused){
				if(mouseSetTime){
					clearTimeout(mouseSetTime);
					mouseSetTime=null;
				}
			}
		},
		/*
		 * checkPlaybackrate
		 * 功能：初始化或重置倍速组件内容
		*/
		checkPlaybackrate=function(){
			var playbackrate=vars['playbackrateList'];
			var pbr=vars['playbackrate'];
			C['bar']['playbackrate']['bg']=createlDiv('ck-list-bg');
			C['bar']['playbackrate']['bgbox'].append(C['bar']['playbackrate']['bg']);
			C['bar']['playbackrate']['bg']['p']=[];
			for(var i=playbackrate.length-1;i>-1;i--){
				var pTemp=createlButton('ck-list-p',playbackrate[i]+language['playbackrateSuffix']);
				pTemp.attr('data-i',i).attr('data-val',playbackrate[i]);
				C['bar']['playbackrate']['bg']['p'].push(pTemp);
				C['bar']['playbackrate']['bg'].append(pTemp);
				pTemp.click(playbackrateClick);
			}
			C['bar']['playbackrate'].hide();
			if(!isUndefined(vars['playbackrateOpen']) && vars['playbackrateOpen']){
				C['bar']['playbackrate'].show();
				C['bar']['playbackrate']['bgbox'].show();
				var w=C['bar']['playbackrate']['bg'].getWidth(),h=C['bar']['playbackrate']['bg'].getHeight();
				var bw=C['bar']['playbackrate']['button'].getWidth();
				C['bar']['playbackrate']['bg'].css({'width':w+10+'px'});
				w=C['bar']['playbackrate']['bg'].getWidth();
				if(getBarHeight()>C['bar'].getHeight()){
					h+=(getBarHeight()-C['bar'].getHeight());
				}
				C['bar']['playbackrate']['bgbox'].attr('style','');
				C['bar']['playbackrate']['bgbox'].css({
					'width':w+'px',
					'height':(h+2)+'px',
					'left':-(w-bw)*0.5+'px'
				});
				C['bar']['playbackrate']['button'].mouseover(function(){
					w=C['bar']['playbackrate']['bg'].getWidth();
					bw=C['bar']['playbackrate']['button'].getWidth();
					C['bar']['playbackrate']['bgbox'].css({			
						'left':-(w-bw)*0.5+'px'
					});
				});
				changePlaybackrateVal(pbr);
			}
		},
		/*
		 * playbackrateClick
		 * 功能：选择播速
		*/
		playbackrateClick=function(e){
			C['bar']['playbackrate']['bgbox'].hide();
			setTimeout(function(){C['bar']['playbackrate']['bgbox'].removeCss('display');},300);
			message(language['switchTo']+$(this).htm());
			player.playbackRate(parseFloat($(this).attr('data-val')));
		},
		/*
		 * changePlaybackrateVal
		 * 功能：根据速度检查播速
		*/
		changePlaybackrateVal=function(num){
			C['bar']['playbackrate']['bg'].find('button').each(function(index,ele){
				var val=parseFloat(ele.attr('data-val'));
				ele.removeClass('ck-list-p-focus');
				if(num==val){
					ele.addClass('ck-list-p-focus');
					if(num!=1){
						C['bar']['playbackrate']['button'].htm(ele.htm());
					}
					else{
						C['bar']['playbackrate']['button'].htm(language['playbackrate']);
					}
				}
				else{
					ele.removeClass('ck-list-p-focus');
				}
			});
		},
		/*
		 * checkTrack
		 * 功能：初始化或重置字幕组件内容
		*/
		checkTrack=function(){
			var track=vars['track'];
			var pbr=-1;
			C['bar']['track']['bgbox'].htm('');
			C['bar']['track']['bg']=createlDiv('ck-list-bg');
			C['bar']['track']['bgbox'].append(C['bar']['track']['bg']);
			C['bar']['track']['bg']['p']=[];
			var pTemp=null;
			for(var i=track.length-1;i>-1;i--){
				pTemp=createlButton('ck-list-p',track[i]['label']);
				pTemp.attr('data-i',i).attr('data-title',track[i]['label']);
				C['bar']['track']['bg']['p'].push(pTemp);
				C['bar']['track']['bg'].append(pTemp);
				if(!isUndefined(track[i]['default']) && track[i]['default']){
					pbr=i;
				}
				pTemp.click(trackClick);
			}
			//
			pTemp=createlButton('ck-list-p',language['noTrack']);
			pTemp.attr('data-i',-1).attr('data-title',language['noTrack']);
			C['bar']['track']['bg']['p'].push(pTemp);
			C['bar']['track']['bg'].append(pTemp);
			pTemp.click(trackClick);
			//
			C['bar']['track'].hide();
			C['bar']['track'].show();
			C['bar']['track']['bgbox'].show();
			var w=C['bar']['track']['bg'].getWidth(),h=C['bar']['track']['bg'].getHeight();
			var bw=C['bar']['track']['button'].getWidth();
			C['bar']['track']['bg'].css({'width':w+10+'px'});
			w=C['bar']['track']['bg'].getWidth();
			if(getBarHeight()>C['bar'].getHeight()){
				h+=(getBarHeight()-C['bar'].getHeight());
			}
			C['bar']['track']['bgbox'].attr('style','');
			C['bar']['track']['bgbox'].css({
				'width':w+'px',
				'height':(h+2)+'px',
				'left':-(w-bw)*0.5+'px'
			});
			C['bar']['track']['button'].mouseover(function(){
				w=C['bar']['track']['bg'].getWidth();
				bw=C['bar']['track']['button'].getWidth();
				C['bar']['track']['bgbox'].css({			
					'left':-(w-bw)*0.5+'px'
				});
			});
			changeTrackVal(pbr);
		},
		/*
		 * trackClick
		 * 功能：选择字幕
		*/
		trackClick=function(e){
			C['bar']['track']['bgbox'].hide();
			setTimeout(function(){C['bar']['track']['bgbox'].removeCss('display');},300);
			message(language['switchTo']+$(this).attr('data-title'));
			player.track(parseFloat($(this).attr('data-i')));
		},
		/*
		 * changeTrackVal
		 * 功能：根据字幕检查
		*/
		changeTrackVal=function(num){
			C['bar']['track']['bg'].find('button').each(function(index,ele){
				var i=parseInt(ele.attr('data-i'));
				if(num==i){
					C['bar']['track']['button'].htm(ele.attr('data-title'));
					if(i>-1){
						CT.track=vars['track'][i];
						eventTarget('track',vars['track'][i]);
					}
					else{
						CT.track=null;
						eventTarget('track',null);
					}
					ele.addClass('ck-list-p-focus');
				}
				else{
					ele.removeClass('ck-list-p-focus');
				}
			});
		},
		/*
		 * checkDefinition
		 * 功能：初始化或重置清晰度组件内容
		*/
		checkDefinition=function(){
			var pi=-1;
			var videoUrl=video['currentSrc'];
			if(!isUndefined(C['bar']['definition']['bg'])){
				C['bar']['definition']['bgbox'].htm('');
				C['bar']['definition']['bgbox'].attr('style','');
				C['bar']['definition']['button'].htm(language['definition']);
			}
			if(valType(vars['video'])=='array'){
				var arr=vars['video'];
				C['bar']['definition']['bg']=createlDiv('ck-list-bg');
				C['bar']['definition']['bgbox'].append(C['bar']['definition']['bg']);
				C['bar']['definition']['bg']['p']=[];
				for(var i=arr.length-1;i>-1;i--){
					var pTemp=createlButton('ck-list-p',arr[i][2]);
					pTemp.attr('data-i',i).attr('data-video',arr[i][0]).attr('data-type',arr[i][1]).attr('data-title',arr[i][2]);
					C['bar']['definition']['bg']['p'].push(pTemp);
					C['bar']['definition']['bg'].append(pTemp);
					if(videoUrl && videoUrl.indexOf(arr[i][0])>-1){
						pi=i;
					}
					pTemp.click(definitionClick);
				}
				C['bar']['definition'].show();
				C['bar']['definition']['bgbox'].show();
				var w=C['bar']['definition']['bg'].getWidth(),h=C['bar']['definition']['bg'].getHeight();
				var bw=C['bar']['definition']['button'].getWidth();
				C['bar']['definition']['bg'].css({'width':w+10+'px'});
				w=C['bar']['definition']['bg'].getWidth();
				if(getBarHeight()>C['bar'].getHeight()){
					h+=(getBarHeight()-C['bar'].getHeight());
				}
				C['bar']['definition']['bgbox'].attr('style','');
				C['bar']['definition']['bgbox'].css({
					'width':w+'px',
					'height':(h+2)+'px',
					'left':-(w-bw)*0.5+'px'
				});
				C['bar']['definition']['button'].mouseover(function(){
					w=C['bar']['definition']['bg'].getWidth();
					bw=C['bar']['definition']['button'].getWidth();
					C['bar']['definition']['bgbox'].css({			
						'left':-(w-bw)*0.5+'px'
					});
				});
			}
			if(pi>-1){
				C['bar']['definition']['bg'].find('button').each(function(index,ele){
					var i=parseInt(ele.attr('data-i'));
					if(pi==i){
						ele.addClass('ck-list-p-focus');
					}
					else{
						ele.removeClass('ck-list-p-focus');
					}
				});
			}
		},
		/*
		 * definitionClick
		 * 功能：选择清晰度
		*/
		definitionClick=function(e){
			C['bar']['definition']['bgbox'].hide();
			setTimeout(function(){
				C['bar']['definition']['bgbox'].removeCss('display');
			},300);
			changeDefinition(parseInt($(this).attr('data-i')));
		},
		/*
		 * changeDefinition
		 * 功能：检查清晰度
		*/
		changeDefinition=function(num){
			C['bar']['definition']['bg'].find('button').each(function(index,ele){
				var i=parseInt(ele.attr('data-i'));
				if(num==i){
					if(!isUndefined(video.textTracks) && video.textTracks.length>0){
						for(var y=video.find('track').length-1;y>-1;y--){
							video.find('track').eq(y).remove();
						}
					}
					seekTime=player.time();
					video.attr('autoplay','autoplay');
					changeVideo(ele.attr('data-video'));
					message(language['switchTo']+ele.htm());
					C['bar']['definition']['button'].htm(ele.htm());
					ele.addClass('ck-list-p-focus');
					eventTarget('definition',{id:i,video:ele.attr('data-video'),title:ele.attr('data-title')});
				}
				else{
					ele.removeClass('ck-list-p-focus');
				}
			});
		},
		/*
		 * deletePrompt
		 * 功能：删除关键点
		*/
		deletePrompt=function(){
			var i=0;
			if(!isUndefined(C['prompt']) && valType(C['prompt'])=='array'){
				for(i=C['prompt'].length;i>-1;i--){
					if(!isUndefined(C['prompt'][i])){
						C['prompt'][i].remove();
					}
				}
				C['prompt']=null;
			}
			C['prompt']=[];
		},
		/*
		 * changePrompt
		 * 功能：构建关键点
		*/
		changePrompt=function(){
			deletePrompt();
			for(var i=0;i<vars['prompt'].length;i++){
				var obj=vars['prompt'][i];
				if(!isUndefined(obj) && 'words' in obj && 'time' in obj && !isUndefined(obj['time']) && !isUndefined(obj['words']) && valType(obj['time'])=='number'){
					var ele=createlDiv('ck-bar-progress-prompt');
					ele.attr('data-words',obj['words']).attr('data-time',obj['time']);
					C['bar']['pbox']['bg'].append(ele);
					var bf=(ele.getWidth()*0.5/C['bar']['pbox']['bg'].getWidth())*50;
					var left=obj['time']/duration*100-bf;
					if(left<0) left=0;
					if(left>100) left=100;
					ele.css({'left':left+'%'});
					ele.mouseover(function(){
						if(this.attr('data-words')){
							showPromptWords(this);
						}
					});
					ele.mouseout(function(){
						if(this.attr('data-words')){
							showPromptWords();
						}
					});
					C['prompt'].push(ele);
				}
			}
		},
		/*
		 * showPromptWords
		 * 功能：显示关键点提示
		*/
		showPromptWords=function(ele){
			if(isUndefined(C['promptWords'])){
				C['promptWords']=createlDiv('ck-prompt-words');
				CM.append(C['promptWords']);
				C['promptWords']['content']=createlDiv('ck-prompt-content');
				C['promptWords'].append(C['promptWords']['content']);
			}
			if(isUndefined(ele)){
				C['promptWords']['content'].htm('');
				C['promptWords'].hide();
				return;
			}
			C['promptWords']['content'].htm(ele.attr('data-words'));
			C['promptWords'].show();
			var pW=C['promptWords'].getWidth(),pH=C['promptWords'].getHeight();
			var bg=C['bar']['pbox']['bg'];
			var time=parseFloat(ele.attr('data-time'));
			var top=CK.getHeight()-getBarHeight();
			var left=ele.offset()['left']-CK.offset()['left']+ele.getWidth()*.5-pW*.5;
			if(!isUndefined(C['preview'])){
				top=CK.getHeight()-parseFloat((getBarHeight()+C['preview']['smallHight']+parseInt(C['preview'].css('margin-bottom'))));
			}
			else{
				top=CK.getHeight()-parseFloat((getBarHeight()+C['tip']['minHeight']+parseInt(C['tip'].css('margin-bottom'))));
				top-=(getBarHeight()-C['bar'].getHeight());
			}
			top-=parseFloat(C['promptWords'].css('margin-bottom'));
			top-=pH;
			left-=parseFloat(C['promptWords'].css('margin-right'));
			if(left<0)left=0;
			if(left>CK.getWidth()-pW)left=CK.getWidth()-pW;
			C['promptWords'].css({
				'top':top+'px',
				'left':left+'px'
			});
		},
		/*
		 * changePreview
		 * 功能：加载或重置预览图片
		*/
		changePreview=function(preview){
			if(!isUndefined(C['preview'])){
				C['preview'].remove();
				C['preview']=null;
			}
			if(!isUndefined(preview)){
				C['preview']=createlDiv('ck-preview');
				CM.append(C['preview']);
				if(isUndefined(vars['preview']['type'])){
					vars['preview']['type']=0;
				}
				if(vars['preview']['type']==1){
					C['preview']['bg']=createlDiv('ck-preview-bg');
					C['preview'].append(C['preview']['bg']);
				}
				var files=[];
				var loadImg=function(bigW,bigH,smallW,smallH){
					if(vars['preview']['type']==1){
						C['preview']['bg']['img']=[];
						for(var i=0;i<files.length;i++){
							for(var y=0;y<bigH/smallH;y++){
								var img=createlDiv('ck-preview-img');
								img.attr('data-img',files[i]).attr('data-i',y);
								img.css({
									'width':bigW+'px',
									'height':smallH+'px',
									'background-position':'left '+(-(y*smallH))+'px'
								});
								C['preview']['bg'].append(img);
								C['preview']['bg']['img'].push(img);
							}
						}
						C['preview']['bg'].css({
							'width':bigW*(bigH/smallH)*(files.length+1)+'px',
							'height':smallH+'px'
						});
					}
					C['preview']['bigWidth']=bigW;
					C['preview']['bigHight']=bigH;
					C['preview']['smallWidth']=smallW;
					C['preview']['smallHight']=smallH;
					if(vars['preview']['type']==1){
						C['preview']['frame']=createlDiv('ck-preview-frame');
						C['preview'].append(C['preview']['frame']);
						C['preview']['frame'].css({
							'width':smallW+'px',
							'height':(smallH-parseFloat(C['preview']['frame'].css('border-width'))*2)+'px',
						});
					}
				};
				if(valType(vars['preview'])=='object'){
					if(valType(vars['preview']['file'])=='array'){
						files=vars['preview']['file'];
						var thumbnail=[10,10];
						if(valType(vars['preview']['thumbnail'])=='array' && vars['preview']['thumbnail'].length>1){
							if(valType(vars['preview']['thumbnail'][0])=='number'){
								thumbnail[0]=vars['preview']['thumbnail'][0];
							}
							if(valType(vars['preview']['thumbnail'][1])=='number'){
								thumbnail[1]=vars['preview']['thumbnail'][1];
							}
							vars['preview']['thumbnail']=thumbnail;
						}
						if(files[0]){
							var img = createlImg(files[0],'ck-preview-load-img').addListener('load',function(){
								loadImg(this.getWidth(),this.getHeight(),this.getWidth()/thumbnail[0],this.getHeight()/thumbnail[1]);
								this.remove();
							});
							CM.append(img);
						}
					}
					else{
						C['preview'].removeChild();
						C['preview']=null;
					}
				}
				else{
					C['preview'].removeChild();
					C['preview']=null;
				}
			}
		},
		/*
		 * showPreview
		 * 功能：显示预览图
		*/
		showPreview=function(time){
			if(hidePreviewSetTime){
				clearTimeout(hidePreviewSetTime);
				hidePreviewSetTime=null;
			}
			if(time<0){
				if(!isUndefined(C['preview'])){
					hidePreviewSetTime=setTimeout(function(){
						C['preview'].hide();
						hidePreviewSetTime=null;
					},200);
					return;
				}
			}
			if(!isUndefined(C['preview']) && !isUndefined(vars['preview']['thumbnail']) && duration>0){
				if(isUndefined(vars['preview']['type'])){
					vars['preview']['type']=0;
				}
				var scale=2,thumbnail=vars['preview']['thumbnail'];
				if(!isUndefined(vars['preview']['scale'])){
					scale=vars['preview']['scale'];
				}
				var divI=0,imgI=0;
				var left=0,frameLeft=0;
				var pW=0;
				var smallW=C['preview']['smallWidth'],smallH=C['preview']['smallHight'];
				var bgXw=time*C['bar']['pbox']['bg'].getWidth()/duration+(C['bar']['pbox']['bg'].offset()['left']-CK.offset()['left']);
				if(C['preview'].css('display')=='none'){
					C['preview'].show();
				}
				C['preview'].css({
					'height':smallH+'px',
				});
				if(vars['preview']['type']==1){
					divI=parseInt(time/(scale*thumbnail[0]));
					imgI=parseInt(time/scale);
					var minI=divI-5,maxI=divI+5;
					if(minI<0) minI=0;
					if(maxI>C['preview']['bg']['img'].length-1){
						maxI=C['preview']['bg']['img'].length-1;
					}
					for(var i=minI;i<maxI;i++){
						if(C['preview']['bg']['img'][i].attr('data-img')){
							var bgPosition=C['preview']['bg']['img'][i].css('background-position');
							C['preview']['bg']['img'][i].css({'background':'url('+C['preview']['bg']['img'][i].attr('data-img')+') no-repeat'});
							C['preview']['bg']['img'][i].attr('data-img','');
							C['preview']['bg']['img'][i].css('background-position',bgPosition);
						}
					}
					pW=C['preview']['frame'].getWidth();
					frameLeft=bgXw-pW*.5;
					left=-(imgI*smallW+smallW*.5)+bgXw;
					if(frameLeft<0){
						frameLeft=0;
						bgXw=(pW-smallW)*0.5;
						left=-(imgI*smallW)+bgXw;
					}
					if(frameLeft>C['preview'].getWidth()-C['preview']['frame'].getWidth()){
						var xz=frameLeft-(C['preview'].getWidth()-C['preview']['frame'].getWidth());
						frameLeft=C['preview'].getWidth()-C['preview']['frame'].getWidth();
						left-=xz;
					}
					C['preview']['bg'].css({'left':left+'px'});
					C['preview']['frame'].css({'left':frameLeft+'px'});
				}
				else{
					var temp=(time/scale)/(thumbnail[0]*thumbnail[1]);
					imgI=parseInt(temp);//计算显示的图片编号
					var smallI=parseInt((temp-imgI)*100);
					var rows=parseInt(smallI/thumbnail[0]);
					var column=smallI%thumbnail[0];
					var files=vars['preview']['file'];
					left=bgXw-smallW*.5;
					if(left<0){
						left=0;
					}
					if(left>CK.getWidth()-smallW){
						left=CK.getWidth()-smallW;
					}
					C['preview'].css({
						'width':C['preview']['smallWidth']+'px',
						'background':'url('+files[imgI]+') no-repeat',
						'background-position':'-'+column*smallW+'px -'+rows*smallH+'px',
						'left':left+'px'
					});
				}
			}
		},
		/*
		 * showError
		 * 功能：显示错误信息
		*/
		showError=function(){
			C['loading'].hide();
			if(vars['controls'] || !vars['errorShow']){
				return;
			}
			if(isUndefined(C['error'])){
				C['error']=createlDiv('ck-error');
				CM.append(C['error']);
			}
			var msg=CT.error['message'];
			if(!msg) msg=language['error']['noMessage'];
			var html=msg+'(code:'+CT.error['code']+')';
			C['error'].htm(html);
			C['error'].show();
		},
		/*
		 * showWindowsError
		 * 功能：显示错误信息
		*/
		showWindowsError=function(error){
			var ele=createlDiv('ckplayer-error',error);
			$('body').append(ele);
		},
		/*
		 * showNextEpisode
		 * 功能：鼠标经过下一集按钮的动作
		*/
		showNextEpisode=function(){
			if(isUndefined(vars['next'])) return;
			if(valType(vars['next'])=='object'){
				if('content' in vars['next']){
					C['next']=player.layer(vars['next']);
					return;
				}
				if('title' in vars['next']){
					tip(C['bar']['nextEpisode'],vars['next']['title']);
					return;
				}
			}
			tip(C['bar']['nextEpisode'],language['next']);
		},
		/*
		 * nextClick
		 * 功能：点击下一集按钮的动作
		*/
		nextClick=function(){
			if(valType(vars['next'])=='object'){
				if('click' in vars['next']){
					if(valType(vars['next']['click'])=='function'){
						vars['next']['click']();
					}
					else if(valType(vars['next']['click'])=='string'){
						eval(vars['next']['click']+'()');
					}
				}
				if('link' in vars['next']){
					window.location.href=vars['next']['link'];
				}
				eventTarget('next');
			}
		},
		/*
		 * showEnded
		 * 功能：播放结束后的显示内容
		*/
		showEnded=function() {
			if(!CT.loop && !isUndefined(vars['ended'])){
				closePauseAd();
				C['ended']=player.layer(vars['ended']);
				if(C['ended']){
					C['centerPlay'].hide();
				}
				return;
			}
		},
		/*
		 * windowScroll
		 * 功能：监听页面滚动
		*/
		windowScroll=function() {
			if(!CT.smallWindows) return;
			if(CT.webFull || CT.theatre || CT.full){
				exitSmallWindows();
				return;
			}
			var scrollTop = document.documentElement.scrollTop || document.body.scrollTop;
			if(scrollTop>CT.offset()['top']+CT.getHeight()){
				if(!CK.hasClass('ckplayer-ckplayer-smallwindow')){
					CK.addClass('ckplayer-ckplayer-smallwindow');
					eventTarget('smallWindows',true);
					smallWindowsState=true;
					if(vars['smallWindowsDrag']){
						drag(true);
					}
					allBarHide();
					checkVideoRotate();
				}
			}
			else{
				exitSmallWindows();
			}
		},
		/*
		 * exitSmallWindows
		 * 退出小窗口模式
		 */
		exitSmallWindows=function(){
			if(CK.hasClass('ckplayer-ckplayer-smallwindow')){
				CK.removeClass('ckplayer-ckplayer-smallwindow');
				if(vars['smallWindowsDrag']){
					drag();
				}
				eventTarget('smallWindows',false);
				smallWindowsState=false;
				allBarShow();
				checkVideoRotate();
			}
		},
		/*
		 * drag
		 * 功能，使节点可拖动
		 * state：是否开启可拖动，默认=false，关闭状态
		*/
		drag=function(state) {
			var doc=$(document);
			var posX = 0,posY = 0,
				posXRecord = 0,posYRecord = 0;
			var open=false;
			if(!isUndefined(state)){
				open=state;
			}
			var mouseDown = function(e) {
				e.preventDefault && e.preventDefault();
				e = e || window.event;
				var client = getClient(e);
				posXRecord = client['x'];
				posYRecord = client['y'];
				doc.mousemove(docMouseMove);
				doc.mouseup(docMouseUp);
				CK.css({
					'cursor':'move'
				});
			};
			var docMouseMove = function(e) {
				e = e || window.event;
				var client = getClient(e);
				var eleOffset = CK.offset();
				posY = client['y']-posYRecord;
				posX = client['x']-posXRecord;
				posXRecord = client['x'];
				posYRecord = client['y'];
				var left=eleOffset['left']+posX;
				var top=eleOffset['top']+posY;
				CK.css({
					'left':left+'px',
					'top':top+'px'
				});
				if(CK.css('position')!='fixed'){
					CK.css({
						'position':'fixed'
					});
				}
				isDrag=true;
			};
			var docMouseUp = function() {
				doc.removeListener('mousemove', docMouseMove);
				doc.removeListener('mouseup', docMouseUp);
				CK.css({
					'cursor':'default'
				});
			};
			if(open){
				CK.mousedown(mouseDown);
			}
			else{
				CK.unbind('mousedown');
			}
		},
		/*
		 * checkVideoRotate
		 * 检查播放器尺寸
		 */
		checkVideoRotate=function(){
			var ckW=CK.getWidth()*nowZoom*0.01,ckH=CK.getHeight()*nowZoom*0.01;
			var vW=CV.getWidth(),vH=CV.getHeight();
			CV.css({
				'transform':'rotate('+nowRotate+'deg)',
				'-ms-transform':'rotate('+nowRotate+'deg)',
				'-moz-transform':'rotate('+nowRotate+'deg)',
				'-webkit-transform':'rotate('+nowRotate+'deg)',
				'-o-transform':'rotate('+nowRotate+'deg)',
				'width':nowZoom+'%',
				'height':nowZoom+'%'
			});
			if(nowRotate!=0 && nowRotate!=180){
				if(vH>ckW && vW>ckH){
					if(vH / ckW > vW / ckH) {
						CV.css({
							'height':nowZoom+'%',
							'width':ckH+'px'
						});
					}
					else {
						CV.css({
							'width':nowZoom+'%',
							'height':ckW+'px'
						});
					}
					return;
				}
				if(vH<ckW && vW<ckH){
					if(ckW>ckH){
						CV.css({
							'height':nowZoom+'%',
							'width':ckH+'px'
						});
					}
					else{
						if(vH / vW >= ckW / ckH) {
							CV.css({
								'width':nowZoom+'%',
								'height':ckW+'px'
							});
						} 
						else {
							CV.css({
								'height':nowZoom+'%',
								'width':ckH+'px'
							});
						}
					}
					return;
				}
				if(vH>=ckW && vW<=ckH){
					CV.css({
						'width':nowZoom+'%',
						'height':ckW+'px'
					});
					return;
				}
				if(vW>=ckH && vH<ckW){
					CV.css({
						'height':nowZoom+'%',
						'width':ckH+'px'
					});
				}
			}
		},
		/*
		 * allBarShow
		 * 功能：控制栏显示
		*/
		allBarShow=function(){
			if(barShow){
				C['bar'].show();
				C['topBar'].show();
			}
			if(rightBarShow){
				C['rightBar'].show();
			}
			
			C['tempTime'].hide();
			if(!isUndefined(C['logo'])){
				C['logo'].show();
			}
		},
		/*
		 * allBarHide
		 * 功能：控制栏隐藏
		*/
		allBarHide=function(tempTime){
			if(isUndefined(tempTime)){
				tempTime=true;
			}
			C['bar'].hide();
			C['topBar'].hide();
			C['rightBar'].hide();
			if(tempTime){
				C['tempTime'].show();
			}
			if(!isUndefined(C['logo'])){
				C['logo'].hide();
			}
		},
		/*
		 * message
		 * 功能：显示提示
		*/
		message=function(str,right){
			C['message'].htm('');
			var msgHide=function(){
				C['message'].removeClass('ck-message-right').removeClass('ck-animate-bouncein').removeClass('ck-animate');
				C['message'].hide();
			};
			if(str){
				C['message'].htm(str);
				C['message'].show();
				if(!C['message'].hasClass('ck-animate')){
					C['message'].addClass('ck-animate ck-animate-bouncein');
				}
				if(!isUndefined(right) && right){
					if(!C['message'].hasClass('ck-message-right')){
						C['message'].addClass('ck-message-right');
					}
				}
				if(msgSetTime){
					clearTimeout(msgSetTime);
					msgSetTime=null;
				}
				msgSetTime=setTimeout(function(){
					msgHide();
					msgSetTime=null;
				},1500);
			}
			else{
				msgHide();
			}
		},
		/*
		 * tip
		 * 功能：显示Tip
		 * @ele：需要显示提示的对象，@str：提示文字，@cl：水平修正距离，@align：对齐方式，只有两种，一种默认对齐（箭头在提示文字下方），一种是左对齐（箭头在提示文字右边）
		*/
		tip=function(ele,str,cl,align){
			C['tip']['content'].htm('');
			if(str){
				C['tip']['content'].htm(str);
				C['tip'].show();
				if(!C['tip'].hasClass('ck-animate')){
					C['tip'].addClass('ck-animate ck-animate-bouncein');
				}
				if(isUndefined(align)){
					align='auto'
				}
				if(align=='left'){
					C['tip']['content'].removeClass('ck-content-float-auto').addClass('ck-content-float-left');
					C['tip']['triangle'].removeClass('ck-triangle-auto').addClass('ck-triangle-left');
				}
				else{
					C['tip']['content'].removeClass('ck-content-float-left').addClass('ck-content-float-auto');
					C['tip']['triangle'].removeClass('ck-triangle-left').addClass('ck-triangle-auto');
				}
				tipResize(ele,cl,align);
				if(isUndefined(closeTipFun)){
					closeTipFun=function(){
						if(tipSetTime){
							clearTimeout(tipSetTime);
							tipSetTime=null;
						}
					};
					closeTipMouseOut=function(ele){
						closeTipFun();
						tipSetTime=setTimeout(function(){
							tip();
							tipSetTime=null;
						},100);
						if(ele){
							ele.removeListener('mouseout',closeTipMouseOut);
						}
					};
				}
				closeTipFun();
				ele.mouseout(function(){closeTipMouseOut(ele)});
			}
			else{
				C['tip'].hide();
				C['tip'].removeClass('ck-animate');
				C['tip'].removeClass('ck-animate-bouncein');
			}
		},
		/*
		 * tipResize
		 * 功能：设置tip坐标
		*/
		tipResize=function(ele,cl,align){
			var offset=ele.offset();
			var ckOffset=CK.offset();
			var w=C['tip'].getWidth(),h=C['tip'].getHeight();
			var ew=ele.getWidth();
			var left=0,top=offset['top']-ckOffset['top']-h;
			if(getBarHeight()>C['bar'].getHeight()){
				top-=(getBarHeight()-C['bar'].getHeight());
			}
			if(w>ew){
				left=offset['left']-ckOffset['left']-(w-ew)*.5;
			}
			else{
				left=offset['left']-ckOffset['left']+(ew-w)*.5;
			}
			if(align=='left'){
				left=offset['left']-ckOffset['left']-w;
				top=offset['top']-ckOffset['top'];
			}
			if(!isUndefined(cl)){
				left=cl['x']-ckOffset['left']-w*.5;
			}
			if(left<0){
				left=0;
			}
			if(top<0){
				top=0;
			}
			if(left>CM.getWidth()-w){
				left=CM.getWidth()-w;
			}
			if(top>CM.getHeight()-h){
				top=CM.getHeight()-h;
			}
			C['tip'].css({
				'left':left+'px',
				'top':top-parseInt(C['tip'].css('margin-bottom'))+'px'
			});
		},
		/*
		 * progressDragX
		 * 功能：制作进度调节框事件
		*/
		progressDragX=function(){
			var bg=C['bar']['pbox']['bg'],
			play=C['bar']['pbox']['play'],
			slider=C['bar']['pbox']['slider'],
			mouseLine=C['bar']['pbox']['mouseLine'];
			slider.unbind();
			bg.unbind();
			var pos = 0,
				posRecord = 0;
			var doc=$(document);
			var sliderDownTime=0;
			var sliderMouseDown = function(e) {
				e.preventDefault && e.preventDefault();
				if(!vars['timeScheduleAdjust']){//禁止拖动
					return;
				}
				e = e || window.event;
				var eleOffset = slider.offset(),bgOffset=bg.offset(),client = getClient(e);
				pSliderMouseDown=true;
				posRecord = client['x'];
				//供比较
				var left = eleOffset['left']-bgOffset['left'];
				sliderDownTime=left*duration/(bg.getWidth()-slider.getWidth());
				slider.removeListener('mousedown', sliderMouseDown);
				slider.removeListener('touchstart', sliderMouseDown);
				doc.mousemove(docMouseMove);
				doc.mouseup(docMouseUp);
				slider.touchmove(docMouseMove);
				slider.touchend(docMouseUp);
				noScrolling(true);
			};
			var docMouseMove = function(e) {
				e = e || window.event;
				var eleOffset = slider.offset(),bgOffset=bg.offset(),client = getClient(e);
				pSliderMouseDown=true;
				pos = posRecord - client['x'];
				posRecord = client['x'];
				var left = eleOffset['left'] - pos-bgOffset['left'];
				if(left<0){
					left=0;
				}
				if(left>bg.getWidth()-slider.getWidth()){
					left=bg.getWidth()-slider.getWidth();
				}
				C['bar']['pbox'].addClass('ck-bar-progress-slider-move');
				//预览时间
				var time='';
				if(!vars['live']){
					time=left*duration/(bg.getWidth()-slider.getWidth());
					switch(vars['timeScheduleAdjust']){
						case 2://只能前进（向右拖动
							if(time<sliderDownTime){
								left=-1000;
								message(language['timeScheduleAdjust']['prohibitBackOff']);
							}
							break;
						case 3://是只能后退
							if(time>sliderDownTime){
								left=-1000;
								message(language['timeScheduleAdjust']['prohibitForward']);
								return;
							}
							break;
						case 4://只能前进但能回到第一次拖动时的位置
							if(time<firstSeekTime){
								left=-1000;
								player.seek(firstSeekTime);
								message(language['timeScheduleAdjust']['prohibitLookBack']);
								return;
							}
							break;
						case 5://看过的地方可以随意拖动
							if(time>maxSeeTime){
								left=-1000;
								player.seek(maxSeeTime);
								message(language['timeScheduleAdjust']['prohibitForwardNotViewed']);
								return;
							}
							break;
					}
					time=formatSeconds(time);
				}
				else{
					time=formatLiveTime(left*getLiveTime()/(bg.getWidth()-slider.getWidth()));
				}
				tip(slider,time);
				if(left>=0){
					slider.css({
						'left': left + 'px'
					});
					play.css({
						'width': left+ slider.getWidth()*.5+ 'px'
					});
				}
				else{
					pSliderMouseDown=false;
				}
				
			};
			var docMouseUp = function() {
				noScrolling();
				doc.removeListener('mousemove', docMouseMove);
				doc.removeListener('mouseup', docMouseUp);
				slider.removeListener('touchmove', docMouseMove);
				slider.removeListener('touchend', docMouseUp);
				slider.mousedown(sliderMouseDown);
				slider.touchstart(sliderMouseDown);
				if(!vars['live']){
					var time=play.getWidth()*duration/(bg.getWidth()-slider.getWidth()*.5);
					if(time>duration){
						time=duration;
					}
					seekTime=0;
					player.seek(time);
				}
				else{
					var playbackTime=parseInt(vars['live']+play.getWidth()*getLiveTime()/(bg.getWidth()-slider.getWidth()*.5));
					eventTarget('playback',{time:playbackTime,date:date('Y-m-d H:i:s',playbackTime)});
					eventTarget('seek',{time:playbackTime,state:'seeking',date:date('Y-m-d H:i:s',playbackTime)});
				}
				C['bar']['pbox'].removeClass('ck-bar-progress-slider-move');
				if(isMouseLeave){
					tip();
				}
			};
			var isMouseLeave=true;//默认鼠标离开了进度按钮上
			var sliderMouseOver=function(){
				var time='';
				if(!vars['live']){
					time=formatSeconds(parseInt(slider.css('left'))*duration/(bg.getWidth()-slider.getWidth()));
				}
				else{
					time=language['live'];
				}
				isMouseLeave=false;
				tip(slider,time);
			};
			
			var sliderMouseLeave=function(){
				isMouseLeave=true;
			};
			slider.mousedown(sliderMouseDown);
			slider.mouseover(sliderMouseOver);
			slider.mouseleave(sliderMouseLeave);
			slider.touchstart(sliderMouseDown);
			var bgMouseDown=function(e){
				e = e || window.event;
				var client = getClient(e);
				var bgOffset=bg.offset();
				var w=client['x']-bgOffset['left'];
				if(CT.full){
					w+=CK.offset()['left'];
				}
				if(!vars['live']){
					var time=duration*w/bg.getWidth();
					switch(vars['timeScheduleAdjust']){
						case 0://禁止拖动
							message(language['timeScheduleAdjust']['prohibit']);
							return;
							break;
						case 2://只能前进（向右拖动
							if(time<oldTime){
								message(language['timeScheduleAdjust']['prohibitBackOff']);
								return;
							}
							break;
						case 3://是只能后退
							if(time>oldTime){
								message(language['timeScheduleAdjust']['prohibitForward']);
								return;
							}
							break;
						case 4://只能前进但能回到第一次拖动时的位置
							if(time<firstSeekTime){
								message(language['timeScheduleAdjust']['prohibitLookBack']);
								return;
							}
							break;
						case 5://看过的地方可以随意拖动
							if(time>maxSeeTime){
								message(language['timeScheduleAdjust']['prohibitForwardNotViewed']);
								return;
							}
							break;
					}
					seekTime=0;
					player.seek(time);
				}
				else{
					var playbackTime=parseInt(vars['live']+getLiveTime()*w/bg.getWidth());
					eventTarget('playback',{time:playbackTime,date:date('Y-m-d H:i:s',playbackTime)});
					eventTarget('seek',{time:playbackTime,state:'seeking',date:date('Y-m-d H:i:s',playbackTime)});
				}
				if(w>bg.getWidth()-slider.getWidth()*.5){
					w=bg.getWidth()-slider.getWidth()*.5
				}
				if(w<slider.getWidth()*.5){
					w=slider.getWidth()*.5;
				}
				pSliderMouseDown=true;
				play.css({
					'width': w+ 'px'
				});
				slider.css({
					'left': w-slider.getWidth()*.5 + 'px'
				});
			};
			var moveTimer=null;
			var bgMouseMove=function(e){
				e = e || window.event;
				var client = getClient(e);
				var bgOffset=bg.offset();
				var w=client['x']-bgOffset['left'];
				clearTime();
				if(CT.full){
					w+=CK.offset()['left'];
				}
				var time=0;
				if(!vars['live']){
					time=duration*w/bg.getWidth();
					showPreview(time);
					time=formatSeconds(time);
				}
				else{
					time=formatLiveTime(getLiveTime()*w/bg.getWidth());
				}
				tip(bg,time,client);
				mouseLine.css({'left':w-(C['bar']['pbox']['mouseLine'].getWidth()*.5)+'px'});
			};
			var moveTimer=null;
			var clearTime=function(){
				if(moveTimer){
					clearTimeout(moveTimer);
					moveTimer=null;
				}
				closeTipFun();
			};
			var bgMouseOut=function(e){
				clearTime();
				moveTimer=setTimeout(function(){
					showPreview(-1);
				},30);
			};
			bg.mousedown(bgMouseDown);
			bg.mousemove(bgMouseMove);
			bg.mouseout(bgMouseOut);
			mouseLine.mouseover(clearTime);
		},
		/*
		 * volumeDragY
		 * 功能：制作音量调节框事件
		*/
		volumeDragY=function() {
			var box=C['bar']['vbox']['volume']['box'],
				bg=C['bar']['vbox']['volume']['bg'],
				pp=C['bar']['vbox']['volume']['pp'],
				slider=C['bar']['vbox']['volume']['slider'],
				doc=$(document);
			var pos = 0,
				posRecord = 0;
			var sliderMouseDown = function(e) {
				e.preventDefault && e.preventDefault();
				noScrolling(true);
				e = e || window.event;
				var client = getClient(e);
				posRecord = client['y'];
				slider.removeListener('mousedown', sliderMouseDown);
				slider.removeListener('touchstart', sliderMouseDown);
				C['bar']['vbox']['mouseDown']=true;
				doc.mousemove(docMouseMove);
				doc.mouseup(docMouseUp);
				slider.touchmove(docMouseMove);
				slider.touchend(docMouseUp);
			};
			var docMouseMove = function(e) {
				e = e || window.event;
				var client = getClient(e);
				var eleOffset = slider.offset();
				var boxOffset=box.offset();
				var bgOffset=bg.offset();
				pos = client['y']-posRecord;
				posRecord = client['y'];
				var sliderTop=eleOffset['top']-boxOffset['top']+pos;
				var minTop=bgOffset['top']-boxOffset['top'],maxTop=bgOffset['top']+bg.getHeight()-boxOffset['top']-slider.getHeight();
				if(sliderTop<minTop){
					sliderTop=minTop;
				}
				if(sliderTop>maxTop){
					sliderTop=maxTop;
				}
				slider.css({
					'top':sliderTop+'px'
				});
				var ppMarginTop=sliderTop-minTop+slider.getHeight()*0.5;
				pp.css({
					'margin-top':ppMarginTop+'px'
				});
				var vol=(bg.getHeight()-ppMarginTop-slider.getHeight()*0.5)/(bg.getHeight()-slider.getHeight());
				player.volume(vol);
			};
			var docMouseUp = function() {
				noScrolling();
				doc.removeListener('mousemove', docMouseMove);
				doc.removeListener('mouseup', docMouseUp);
				slider.removeListener('touchmove', docMouseMove);
				slider.removeListener('touchend', docMouseUp);
				slider.mousedown(sliderMouseDown);
				slider.touchstart(sliderMouseDown);
				C['bar']['vbox']['mouseDown']=false;
			};
			slider.mousedown(sliderMouseDown);
			slider.touchstart(sliderMouseDown);
			var bgMouseDown=function(e){
				e = e || window.event;
				var client = getClient(e);
				var bgOffset=bg.offset();
				var h=client['y']-bgOffset['top'];
				var max=bg.getHeight();
				var vol=1-h/max;
				player.volume(vol);
			};
			bg.mousedown(bgMouseDown);
		},
		/*
		 * changeProgress
		 * 功能：根据时间调节播放进度
		*/
		changeProgress=function(time){
			if((valType(vars['live'])=='boolean' && vars['live'])){
				return;
			}
			if(!pSliderMouseDown){
				var bg=C['bar']['pbox']['bg'],slider=C['bar']['pbox']['slider'];
				var playW=(time/duration)*100;
				
				if(valType(vars['live'])=='number'){
					playW='100';
				}
				C['bar']['pbox']['play'].css({'width':playW+'%'});
				var sliderW=(slider.getWidth()/bg.getWidth())*100;
				var sliderLeft=playW-sliderW*.5;
				if(sliderLeft<0){
					sliderLeft=0;
				}
				if(sliderLeft>100-sliderW){
					sliderLeft=100-sliderW;
				}
				C['bar']['pbox']['slider'].css({'left':sliderLeft+'%'});
				checkProgressSlider();
			}		
		},
		/*
		 * checkProgressSlider
		 * 功能：检查设置播放进度条位置
		 */
		checkProgressSlider=function(){
			var bg=C['bar']['pbox']['bg'],slider=C['bar']['pbox']['slider'];
			var st=0;
			if(C['bar']['pbox'].css('transition-duration')){
				st=parseFloat(C['bar']['pbox'].css('transition-duration'))*1000+20;
			}
			var checkThis=function(){
				var sliderLeft=parseInt(C['bar']['pbox']['slider'].css('left'));
				if(sliderLeft<0){
					sliderLeft=0;
					C['bar']['pbox']['slider'].css({'left':sliderLeft+'px'});
				}
				if(sliderLeft>bg.getWidth()-slider.getWidth()){
					sliderLeft=bg.getWidth()-slider.getWidth();
					C['bar']['pbox']['slider'].css({'left':sliderLeft+'px'});
				}
			};
			setTimeout(checkThis,st);		
		},
		/*
		 * changeTopTime
		 * 功能：修改顶部右侧时间
		*/
		changeTopTime=function(){
			C['topBar']['timeEle'].htm(date('H:i:s'));
			setTimeout(changeTopTime,1000);
		},
		/*
		 * changeVolumeSlider
		 * 功能：根据音量改变滑块位置
		*/
		changeVolumeSlider=function(vol){
			if(!isUndefined(C['bar']['vbox'])){
				var box=C['bar']['vbox']['volume']['box'],
				bg=C['bar']['vbox']['volume']['bg'],
				pp=C['bar']['vbox']['volume']['pp'],
				slider=C['bar']['vbox']['volume']['slider'];
				C['bar']['vbox']['volume']['txt'].htm(parseInt(vol*100));
				var bgH=bg.getHeight();
				if(bgH){
					var ppMarginTop=bgH-vol*bgH;
					pp.css({
						'margin-top':ppMarginTop+'px'
					});
					var sliderTop=bg.offset()['top']-box.offset()['top']+ppMarginTop-slider.getHeight()*0.5;
					slider.css({
						'top':sliderTop+'px'
					});
				}
			}
		},
		/*
		 * changeLoad
		 * 功能：获取视频已加载部分并修改加载进度栏
		*/
		changeLoad=function(){
			if(video){
				var len = video.buffered.length;
				if(len>0){
					var bufferEnd = video.buffered.end(len-1);
					if(loadTime<bufferEnd){
						loadTime=bufferEnd;
					}
					replaceInformation('loadTime',parseInt(loadTime*100)*0.01);
					C['bar']['pbox']['load'].css('width',(loadTime/duration)*100+'%');
				}
			}
		},
		/*
		 * changeMuted
		 * 功能：修改静音和取消静音时按钮切换事件
		*/
		changeMuted=function(b){
			if(b){
				C['bar']['vbox']['muted'].hide();
				C['bar']['vbox']['exitMuted'].show();
				C['ad']['bottom']['mutedAndExit']['muted'].hide();
				C['ad']['bottom']['mutedAndExit']['exitMuted'].show();
				message(language['muted']);
			}
			else{
				C['bar']['vbox']['muted'].show();
				C['bar']['vbox']['exitMuted'].hide();
				C['ad']['bottom']['mutedAndExit']['muted'].show();
				C['ad']['bottom']['mutedAndExit']['exitMuted'].hide()
			}
		},
		/*
		 * replaceInformation
		 * 功能：替换关于里的内容
		*/
		replaceInformation=function(o , n){
			if(isUndefined(C['about']['sourceData']) && !isUndefined(C['about'].find('ul'))){
				C['about']['sourceData']=C['about'].find('ul').eq(0).htm();
				C['about']['objData']={};
				var reg=/{(.*?)}/g;
				var res = C['about']['sourceData'].match(reg);
				while( res = reg.exec(C['about']['sourceData'])){
					C['about']['objData'][res[1]]='0';
				}
			}
			if(!isUndefined(C['about'].find('ul'))){
				var reg=null;
				var htm=C['about']['sourceData'];
				var temp=C['about']['textareaText'];
				if(C['about']['objData']){
					for(var k in C['about']['objData']){
						if(o!=k){
							reg = new RegExp('{'+k+'}' , 'g');
							var val=C['about']['objData'][k];
							if(isUndefined(val)){
								val='0';
							}
							htm=htm.replace(reg , val);
							temp=temp.replace(reg , val);
						}
						
					}
				}
				reg = new RegExp('{'+o+'}' , 'g');
				htm=htm.replace(reg , n || '');
				temp=temp.replace(reg , n || '');
				C['about']['textareaEle'].htm(temp);
				C['about']['objData'][o]=n;
				return C['about'].find('ul').eq(0).htm(htm);
			}
		},
		/*
		 * calculationFps
		 * 功能：获取fps
		*/
		calculationFps=function() {
			var requestAnimationFrame =
				window.requestAnimationFrame ||
				window.webkitRequestAnimationFrame ||
				window.mozRequestAnimationFrame ||
				window.oRequestAnimationFrame ||
				window.msRequestAnimationFrame ||
				function(callback) {
					window.setTimeout(callback, 1000 / 60);
				};
			var e, pe, pid, fps, last, offset;
			fps = 0;
			last = Date.now();
			var step = function() {
				offset = Date.now() - last;
				fps += 1;
				if(offset >= 1000) {
					last += offset;
					appendFps(fps);
					fps = 0;
				}
				requestAnimationFrame(step);
			};
			var	appendFps = function(fps) {
				if(fps!=CT.fps){
					CT.fps=fps;
					replaceInformation('fps',fps);
					eventTarget('fps',fps);
				}
				
			};
			if(isUndefined(CT.fps)){
				CT.fps=0;
			}
			step();
		},
		/*
		 * getElement
		 * 功能：获取一个外部节点
		*/
		getElement=function(content){
			var ele=$(content);
			if(isUndefined(ele)) return;
			if(valType(ele)=='htmlarray'){
				ele=ele[0];
			}
			if(isUndefined(ele.captureParentNode)){
				ele.captureParentNode = ele.parentNode;	
				ele.prevElement=ele.prev();
				ele.nextElement=ele.next();
			}		
			return ele;
		},
		/*
		 * $
		 * 功能：根据ID或className或节点节点名称获取对象
		*/
		$=function(obj, eL) {
			var parent = null;
			if (document) {
				parent = document;
			}
			if (!isUndefined(eL)) {
				parent = eL;
			}
			var res = [];
			if (obj) {
				if (valType(obj) == 'htmlobject' || valType(obj)=='object') {
					res = obj;
					if (!isUndefined(res.ckplayer)) {
						return res;
					}
				} 
				else if (valType(obj) == 'string' && obj!='') {
					switch (obj.substr(0, 1)) {
						case '.':
							obj = obj.substr(1, obj.length);
							if (parent.getElementsByClassName) {
								res = parent.getElementsByClassName(obj);
								if (!res.length) {
									return null;
								}
							} 
							else if (!parent && document.getElementsByClassName) {
								res = document.getElementsByClassName(obj);
								if (!res.length) {
									return null;
								}
							}
							else {
								var reg = new RegExp(' ' + obj + ' ', 'i');
								var ele = null;
								if (parent.getElementsByTagName) {
									ele = parent.getElementsByTagName('*');
								} else {
									ele = document.getElementsByTagName('*');
								}
								for (var i = 0; i < ele.length; i++) {
									if (reg.test(' ' + ele[i].className + ' ')) {
										res.push(ele[i]);
									}
								}
							}
							if (res) {
								if (res.length === 0) {
									res = null;
								}
							}
							break;
						case '#':
							obj = obj.substr(1, obj.length);
							if (parent.getElementById) {
								res = parent.getElementById(obj);
							} else {
								res=document.getElementById(obj);
							}
							break;
						default:
							var reg = new RegExp(' ' + obj + ' ', 'i');
							if (parent.getElementsByTagName) {
								res = parent.getElementsByTagName(obj);
							} else {
								res = document.getElementsByTagName(obj);
							}
							if (res) {
								if (obj == 'body' || obj == 'document' || obj == 'html') {
									res = res[0];
								} else if (res.length === 0) {
									res = null;
								}
							} else {
								res = null;
							}
							break;
					}
				}
				else {
					res = obj;
				}
			}
			else {
				res = document;
			}
			
			if (res) {
				if (valType(res) == 'htmlobject' || valType(res) == 'object' || valType(res) == 'document') {
					res.ckplayer = 'Thanks for using';
					/*
					 * find
					 * 功能：在当前节点中查找指定节点
					*/
					res.find = function(obj) {
						return $(obj, this);
					};
					/*
					 * attr
					 * 功能：修改或获取节点的属性值
					 * @key不能为空，指属性名称，$value不为空则设置属性值，为空则获取属性值
					*/
					res.attr = function(key, value) {
						if (isUndefined(value)) {
							return this.getAttribute(key);
						}
						else {
							this.setAttribute(key, value);
							return this;
						}
					};
					/*
					 * removeAtt
					 * 功能：删除节点的属性值
					 * @key不能为空，指属性名称
					*/
					res.removeAttr = function(key) {
						this.removeAttribute(key);
						return this;
					};
					/*
					 * css
					 * 功能：修改或获取节点的样式样式
					 * @key不能为空，指属性名称，$value不为空则设置属性值，为空则获取属性值
					*/
					res.css = function(key, value) {
						// 拆解字符串并将第二单词首字母大写
						var keyNew = function(str) {
							// 当属性名有横杠时
							if (str.indexOf('-') != -1) {
								var arr = str.split('-');
								var a = arr[0];
								var b = '',
									c = '';
								if (arr.length > 1) {
									b = arr[1].substr(0, 1).toLocaleUpperCase() + arr[1].substr(1, arr[1].length - 1);
								}
								if (arr.length > 2) {
									b = arr[2].substr(0, 1).toLocaleUpperCase() + arr[2].substr(1, arr[2].length - 1);
								}
								return a + b + c;
							}
							// 没有横杠就不进行字符串拆解
							return str;
						};
						if (isUndefined(value)){
							if (!isUndefined(key) && valType(key) == 'string') {
								if (this.currentStyle) {
									return this.currentStyle[key];
								} else {
									return document.defaultView.getComputedStyle(this, null)[key];
								}
							}
							if (isUndefined(key)) {
								if (this.currentStyle) {
									return this.currentStyle;
								} else {
									return document.defaultView.getComputedStyle(this, null);
								}
							}
						}
						// 当传进来的参数key不是一个对象，给节点添加css样式
						if (valType(key) != 'object') {
							var newKey = keyNew(key);
							if (this.length > 1) {
								// 如果this有多个值，那给每个节点都添加样式
								for (var i = 0; i < this.length; i++) {
									this[i].style[newKey] = value;
								}
							} else {
								this.style[newKey] = value;
							}
						} else {
							//如果第一个值是一个对象，遍历这个对象，并将属性名传进函数进行拆解
							for (var item in key) {
								var objKey = keyNew(item);
								if (valType(this) == 'htmlarray') {
									for (var i = 0; i < this.length; i++) {
										this[i].style[objKey] = key[item];
									}
								} else {
									this.style[objKey] = key[item];
								}
							}
						}
						return this;
					};
					res.hasClass = function(cName) {
						if (isUndefined(cName)) return false;
						var reg = new RegExp('(\\s|^)' + cName + '(\\s|$)');
						if (this.className && this.className.match(reg)) {
							return true;
						}
						return false;
					};
					res.addClass = function(cName) {
						if (!this.hasClass(cName)) {
							if (this.className && this.className.substr(this.className.length - 1, 1) != ' ') {
								this.className += ' ';
							}
							this.className += cName;
						};
						return this;
					};
					res.removeClass = function(cName) {
						if (this.hasClass(cName)) {
							this.className = this.className.replace(new RegExp('(\\s|^)' + cName + '(\\s|$)'), ' ');
							if (this.className.substr(this.className.length - 1, 1) == ' ') {
								this.className = this.className.substr(0, this.className.length - 1);
							}
							if (!this.className) {
								this.removeAttribute('class');
							}
						};
						return this;
					};
					res.searchClass = function(cName) {
						var arr = this.className.split(' ');
						for (var i = 0; i < arr.length; i++) {
							if (arr[i].substr(0, cName.length) == cName) {
								return arr[i];
							}
						}
						return this;
					};
					res.removeCss=function(cName){
						var cs=this.attr('style');
						if(cs){
							var arr=cs.split(';');
							var obj={};
							for(var i=0;i<arr.length;i++){
								var arrT=arr[i].split(':');
								if(arrT.length==2 && arrT[0] && arrT[0].trim()!=cName){
									obj[arrT[0].trim()]=arrT[1].trim();
								}
							}
							this.attr('style','');
							this.css(obj);
						}
					};
					res.addListener = function(e, f, t) {
						return addListener(this, e, f, t);
					};
					res.removeListener = function(e, f, t) {
						return removeListener(this, e, f, t);
					};
					res.prev = function() {
						return prev(this);
					};
					res.next = function() {
						return next(this);
					};
					res.append =function(ele){
						this.appendChild(ele);
						return this;
					};
					res.remove = function() {
						if(!isUndefined(this.parentNode)){
							this.parentNode.removeChild(this);
						}
						return null;
					};
					res.bind=function(e, f, t) {
						return addListener(this, e, f, t);
					};
					res.unbind =function(e,f){
						var i=0;var arr=[];
						if(!isUndefined(f)){
							res.removeListener(e,f);
						}
						else if(!isUndefined(e)){
							if(!isUndefined(this.listenerList)){
								arr=this.listenerList;
								for(i=0;i<arr.length;i++){
									if(arr[i][0]==e){
										res.removeListener(e,arr[i][1]);
									}
								}
							}
						}
						else{
							if(!isUndefined(this.listenerList)){
								arr=this.listenerList;
								for(i=0;i<arr.length;i++){
									res.removeListener(arr[i][0],arr[i][1]);
								}
							}
						}
					};
					/*
					 * htm
					 * 功能：设置或获取html
					*/
					res.htm=function(val){
						if(!isUndefined(val)){
							res.innerHTML=val;
						}
						else{
							return res.innerHTML;
						}
					};
					/*
					 * htmReplace
					 * 功能：将html中ar替换成val
					*/
					res.htmReplace=function(ar,val,html){
						if(!isUndefined(val) && !isUndefined(ar)){
							if(isUndefined(this.attr('data-htm'))){
								this.attr('data-htm',html);
							}
							var htm=this.attr('data-htm');
							var reg = new RegExp(ar , 'g');
							htm=htm.replace(reg , val);
							this.htm(htm);
						}
					};
					/*
					 * offset
					 * 功能：获取节点的绝对坐标
					*/
					res.offset = function() {
						var par = this.offsetParent,
							//获取当前节点的父参照物（不一定是父节点）
							left = this.offsetLeft,
							//获取当前节点相对父节点左偏离
							top = this.offsetTop;
							//获取当前节点相对父节点上偏移
						while (par && par.tagName !== 'BODY') {
							//判断是否已经到了最外一层 并且判断父参照物存不存在
							if (!/MSIE 8\.0/.test(navigator.userAgent)){
								//利用正则表达式判断
								left += par.clientLeft;
								top += par.clientTop;
							}
							left += par.offsetLeft;
							//获得节点距离父节点左偏移多少
							top += par.offsetTop;
							//获得节点距离父节点上偏移多少
							par = par.offsetParent;
						}
						return {
							top: top,
							left: left
						}
					};
					/*
					 * fixed
					 * 功能：判断节点是否悬浮于页面
					*/
					res.fixed = function() {
						if (this.css('position') == 'fixed') {
							return true;
						}
						if (!isUndefined(this.offsetParent)) {
							return $(this.offsetParent).fixed();
						}
						return false;
					};
					/*
					 * getWidth
					 * 功能：获取节点的宽
					*/
					res.getWidth = function() {
						return this.offsetWidth;
					};
					/*
					 * getHeight
					 * 功能：获取节点的高
					*/
					res.getHeight = function() {
						return this.offsetHeight;
					};
					if(isUndefined(res.width)){
						res.width=function(){
							return this.offsetWidth;
						}
					}
					if(isUndefined(res.height)){
						res.height=function(){
							return this.offsetWidth;
						}
					}
					/*
					 * resize
					 * 功能：监听窗口尺寸变化
					 * @fn：窗口变化时执行的函数
					*/
					res.resize = function(fn) {
						addListener(window, 'resize', fn);
					};
					/*
					 * click
					 * 功能：节点单击时执行的函数
					 * @fn：执行的函数
					*/
					res.click =function(fn){
						addListener(this,'click',fn);
						return this;
					};
					/*
					 * singleClick
					 * 功能：节点单击事件，当使用该事件时会同时注册双击事件，此时不要使用click函数进行单击事件监听
					 * @fn：执行的函数
					*/
					res.singleClick=function(fn){
						if(isUndefined(this.dbClick)){
							doubleClickEvent(this);
							this.dbClick=true;
						}
						this.addListener('sigClick',fn);
						return this;
					};
					/*
					 * doubleClick
					 * 功能：节点双击事件，当使用该事件时会同时注册双击事件，此时不要使用click函数进行单击事件监听
					 * @fn：执行的函数
					*/
					res.doubleClick=function(fn){
						if(isUndefined(this.dbClick)){
							doubleClickEvent(this);
							this.dbClick=true;
						}
						this.addListener('dobClick',fn);
						return this;
					};
					/*
					 * mouseover
					 * 功能：鼠标经过节点时执行的函数
					 * @fn：执行的函数
					*/
					res.mouseover =function(fn){
						addListener(this,'mouseover',fn);
						return this;
					};
					/*
					 * mouseout
					 * 功能：鼠标离开节点时执行的函数
					 * @fn：执行的函数
					*/
					res.mouseout =function(fn){
						addListener(this,'mouseout',fn);
						return this;
					};
					/*
					 * mousedown
					 * 功能：鼠标在节点上按下时执行的函数
					 * @fn：执行的函数
					*/
					res.mousedown =function(fn){
						addListener(this,'mousedown',fn);
						return this;
					};
					/*
					 * mouseup
					 * 功能：节点上鼠标弹起时执行的函数
					 * @fn：执行的函数
					*/
					res.mouseup =function(fn){
						addListener(this,'mouseup',fn);
						return this;
					};
					/*
					 * mousemove
					 * 功能：鼠标在节点上划行时执行的函数
					 * @fn：执行的函数
					*/
					res.mousemove =function(fn){
						addListener(this,'mousemove',fn);
						return this;
					};
					/*
					 * mouseWheel
					 * 功能：鼠标滚轮在节点上划行时执行的函数
					 * @fn：执行的函数
					*/
					res.mouseWheel =function(fn){
						addListener(this,'mousewheel',fn);
						addListener(this,'DOMMouseScroll',fn,false);
						return this;
					};
					/*
					 * mouseleave
					 * 功能：鼠标指针移出节点时执行的函数
					 * @fn：执行的函数
					*/
					res.mouseleave =function(fn){
						addListener(this,'mouseleave',fn);
						return this;
					};
					/*
					 * touchstart
					 * 功能：移动端鼠标在节点上按下时执行的函数
					 * @fn：执行的函数
					*/
					res.touchstart =function(fn){
						addListener(this,'touchstart',fn);
						return this;
					};
					/*
					 * touchmove
					 * 功能：移动端鼠标在节点上划行时执行的函数
					 * @fn：执行的函数
					*/
					res.touchmove =function(fn){
						addListener(this,'touchmove',fn);
						return this;
					};
					/*
					 * touchend
					 * 功能：移动端节点上鼠标弹起时执行的函数
					 * @fn：执行的函数
					*/
					res.touchend =function(fn){
						addListener(this,'touchend',fn);
						return this;
					};
					/*
					 * show
					 * 功能：显示节点
					*/
					res.show=function(){
						this.css('display','block');
						return this;
					};
					/*
					 * hide
					 * 功能：隐藏节点
					*/
					res.hide=function(){
						this.css('display','none');
						return this;
					};
					/*
						animate
						功能：缓动效果
						parameter:String=需要改变的属性：left,top,width,height,alpha,
						totalTime:Number=运动的总毫秒数
						easing:String=效果名称,
						callBack:完成后的回调函数
					*/
					res.animate=function(parameter,totalTime,easing,callBack) {
						if(isUndefined(this.CK)){
							return res;
						}
						var thisTemp = this;
						var parNode=this.CK;
						var w =parNode.getWidth(),h = parNode.getHeight();
						var speed=10;//跳针时间
						this.timerTween=null;
						this.tweenPlay=true;
						if(isUndefined(parameter)){
							return this;
						}
						if (isUndefined(totalTime) ||  totalTime== 0) {
							totalTime=1000;
						}
						if(isUndefined(easing) || easing==''){
							easing='None.easeIn';
						}
						var effArr = easing.split('.');
						var tween = {
							None: { //均速运动
								easeIn: function(t, b, c, d) {
									return c * t / d + b;
								},
								easeOut: function(t, b, c, d) {
									return c * t / d + b;
								},
								easeInOut: function(t, b, c, d) {
									return c * t / d + b;
								}
							},
							Quadratic: {
								easeIn: function(t, b, c, d) {
									return c * (t /= d) * t + b;
								},
								easeOut: function(t, b, c, d) {
									return - c * (t /= d) * (t - 2) + b;
								},
								easeInOut: function(t, b, c, d) {
									if ((t /= d / 2) < 1) return c / 2 * t * t + b;
									return - c / 2 * ((--t) * (t - 2) - 1) + b;
								}
							},
							Cubic: {
								easeIn: function(t, b, c, d) {
									return c * (t /= d) * t * t + b;
								},
								easeOut: function(t, b, c, d) {
									return c * ((t = t / d - 1) * t * t + 1) + b;
								},
								easeInOut: function(t, b, c, d) {
									if ((t /= d / 2) < 1) return c / 2 * t * t * t + b;
									return c / 2 * ((t -= 2) * t * t + 2) + b;
								}
							},
							Quartic: {
								easeIn: function(t, b, c, d) {
									return c * (t /= d) * t * t * t + b;
								},
								easeOut: function(t, b, c, d) {
									return - c * ((t = t / d - 1) * t * t * t - 1) + b;
								},
								easeInOut: function(t, b, c, d) {
									if ((t /= d / 2) < 1) return c / 2 * t * t * t * t + b;
									return - c / 2 * ((t -= 2) * t * t * t - 2) + b;
								}
							},
							Quintic: {
								easeIn: function(t, b, c, d) {
									return c * (t /= d) * t * t * t * t + b;
								},
								easeOut: function(t, b, c, d) {
									return c * ((t = t / d - 1) * t * t * t * t + 1) + b;
								},
								easeInOut: function(t, b, c, d) {
									if ((t /= d / 2) < 1) return c / 2 * t * t * t * t * t + b;
									return c / 2 * ((t -= 2) * t * t * t * t + 2) + b;
								}
							},
							Sine: {
								easeIn: function(t, b, c, d) {
									return - c * Math.cos(t / d * (Math.PI / 2)) + c + b;
								},
								easeOut: function(t, b, c, d) {
									return c * Math.sin(t / d * (Math.PI / 2)) + b;
								},
								easeInOut: function(t, b, c, d) {
									return - c / 2 * (Math.cos(Math.PI * t / d) - 1) + b;
								}
							},
							Exponential: {
								easeIn: function(t, b, c, d) {
									return (t == 0) ? b: c * Math.pow(2, 10 * (t / d - 1)) + b;
								},
								easeOut: function(t, b, c, d) {
									return (t == d) ? b + c: c * ( - Math.pow(2, -10 * t / d) + 1) + b;
								},
								easeInOut: function(t, b, c, d) {
									if (t == 0) return b;
									if (t == d) return b + c;
									if ((t /= d / 2) < 1) return c / 2 * Math.pow(2, 10 * (t - 1)) + b;
									return c / 2 * ( - Math.pow(2, -10 * --t) + 2) + b;
								}
							},
							Circular: {
								easeIn: function(t, b, c, d) {
									return - c * (Math.sqrt(1 - (t /= d) * t) - 1) + b;
								},
								easeOut: function(t, b, c, d) {
									return c * Math.sqrt(1 - (t = t / d - 1) * t) + b;
								},
								easeInOut: function(t, b, c, d) {
									if ((t /= d / 2) < 1) return - c / 2 * (Math.sqrt(1 - t * t) - 1) + b;
									return c / 2 * (Math.sqrt(1 - (t -= 2) * t) + 1) + b;
								}
							},
							Elastic: {
								easeIn: function(t, b, c, d, a, p) {
									if (t == 0) return b;
									if ((t /= d) == 1) return b + c;
									if (!p) p = d * .3;
									if (!a || a < Math.abs(c)) {
										a = c;
										var s = p / 4;
									} else var s = p / (2 * Math.PI) * Math.asin(c / a);
									return - (a * Math.pow(2, 10 * (t -= 1)) * Math.sin((t * d - s) * (2 * Math.PI) / p)) + b;
								},
								easeOut: function(t, b, c, d, a, p) {
									if (t == 0) return b;
									if ((t /= d) == 1) return b + c;
									if (!p) p = d * .3;
									if (!a || a < Math.abs(c)) {
										a = c;
										var s = p / 4;
									} else var s = p / (2 * Math.PI) * Math.asin(c / a);
									return (a * Math.pow(2, -10 * t) * Math.sin((t * d - s) * (2 * Math.PI) / p) + c + b);
								},
								easeInOut: function(t, b, c, d, a, p) {
									if (t == 0) return b;
									if ((t /= d / 2) == 2) return b + c;
									if (!p) p = d * (.3 * 1.5);
									if (!a || a < Math.abs(c)) {
										a = c;
										var s = p / 4;
									} else var s = p / (2 * Math.PI) * Math.asin(c / a);
									if (t < 1) return - .5 * (a * Math.pow(2, 10 * (t -= 1)) * Math.sin((t * d - s) * (2 * Math.PI) / p)) + b;
									return a * Math.pow(2, -10 * (t -= 1)) * Math.sin((t * d - s) * (2 * Math.PI) / p) * .5 + c + b;
								}
							},
							Back: {
								easeIn: function(t, b, c, d, s) {
									if (s == undefined) s = 1.70158;
									return c * (t /= d) * t * ((s + 1) * t - s) + b;
								},
								easeOut: function(t, b, c, d, s) {
									if (s == undefined) s = 1.70158;
									return c * ((t = t / d - 1) * t * ((s + 1) * t + s) + 1) + b;
								},
								easeInOut: function(t, b, c, d, s) {
									if (s == undefined) s = 1.70158;
									if ((t /= d / 2) < 1) return c / 2 * (t * t * (((s *= (1.525)) + 1) * t - s)) + b;
									return c / 2 * ((t -= 2) * t * (((s *= (1.525)) + 1) * t + s) + 2) + b;
								}
							},
							Bounce: {
								easeIn: function(t, b, c, d) {
									return c - tween.Bounce.easeOut(d - t, 0, c, d) + b;
								},
								easeOut: function(t, b, c, d) {
									if ((t /= d) < (1 / 2.75)) {
										return c * (7.5625 * t * t) + b;
									} else if (t < (2 / 2.75)) {
										return c * (7.5625 * (t -= (1.5 / 2.75)) * t + .75) + b;
									} else if (t < (2.5 / 2.75)) {
										return c * (7.5625 * (t -= (2.25 / 2.75)) * t + .9375) + b;
									} else {
										return c * (7.5625 * (t -= (2.625 / 2.75)) * t + .984375) + b;
									}
								},
								easeInOut: function(t, b, c, d) {
									if (t < d / 2){
										return tween.Bounce.easeIn(t * 2, 0, c, d) * .5 + b;
									}
									else{
										return tween.Bounce.easeOut(t * 2 - d, 0, c, d) * .5 + c * .5 + b;
									}
								}
							}
						};
						if(effArr.length!=2){
							return null;
						}
						if (!(effArr[0] in tween)) {
							return null;
						}
						if (!(effArr[1] in tween[effArr[0]])) {
							return null;
						}
						var tweenFun = tween[effArr[0]][effArr[1]];
						var getStartAndEnd=function(arr){//分析初始化位置和结束位置
							var vars=arr[1];
							var current=0,result=0;
							switch (arr[0]) {
								case 'width':
									current = thisTemp.getWidth();
									if (vars.substring(vars.length - 1, vars.length) == '%') {
										result = parseInt(vars) * h * 0.01;
									}
									else{
										result=parseInt(vars);
									}
									break;
								case 'height':
									current = thisTemp.getHeight();
									if (vars.substring(vars.length - 1, vars.length) == '%') {
										result = parseInt(vars) * h * 0.01;
									}
									else{
										result=parseInt(vars);
									}
									break;
								case 'left':
									if(!isUndefined(thisTemp.css('left'))){
										current=parseInt(thisTemp.css('left'));
									}
									else{
										current = thisTemp.offset()['left']-parNode.offset()['left'];
									}
									if (vars.substring(vars.length - 1, vars.length) == '%') {
										result = parseInt(vars) * w * 0.01;
									}
									else{
										result=parseInt(vars);
									}
									thisTemp.css('right','auto');
									break;
								case 'right':
									if(!isUndefined(thisTemp.css('right'))){
										current=parseInt(thisTemp.css('right'));
									}
									else{
										current = parNode.getWidth()-(thisTemp.offset()['left']-parNode.offset()['left']+thisTemp.getWidth());
									}
									if (vars.substring(vars.length - 1, vars.length) == '%') {
										result = parseInt(vars) * w * 0.01;
									}
									else{
										result=parseInt(vars);
									}
									thisTemp.css('left','auto');
									break;
								case 'top':
									if(!isUndefined(thisTemp.css('top'))){
										current=parseInt(thisTemp.css('top'));
									}
									else{
										current = thisTemp.offset()['top']-parNode.offset()['top'];
									}
									if (vars.substring(vars.length - 1, vars.length) == '%') {
										result = parseInt(vars) * h * 0.01;
									}
									else{
										result=parseInt(vars);
									}
									thisTemp.css('bottom','auto');
									break;
								case 'bottom':
									if(!isUndefined(thisTemp.css('bottom'))){
										current=parseInt(thisTemp.css('bottom'));
									}
									else{
										current = parNode.getHeight()-(thisTemp.offset()['top']-parNode.offset()['top']+thisTemp.getHeight());
									}
									if (vars.substring(vars.length - 1, vars.length) == '%') {
										result = parseInt(vars) * h * 0.01;
									}
									else{
										result=parseInt(vars);
									}
									thisTemp.css('top','auto');
									break;
								case 'alpha':
									if(!isUndefined(thisTemp.css('filter')) && thisTemp.css('filter')!='none'){
										current=Number(thisTemp.css('filter'))*100;
									}
									else if(!isUndefined(thisTemp.css('opacity')) && thisTemp.css('opacity')!='none'){
										current=thisTemp.css('opacity')*100;
									}
									else{
										current = 100;
									}
									if (vars.substring(vars.length - 1, vars.length) == '%') {
										result = parseInt(vars);
									}
									else{
										result=vars*100;
									}
									break;
							}
							result-=current;
							if(current!=result){
								return {
									'type':arr[0],
									'current':current,
									'result':result
								};
							}
							return null;
						};
						var stopTween = function() {
							if (thisTemp.timerTween != null) {
								clearInterval(thisTemp.timerTween);
								thisTemp.timerTween = null;
							}
						};
						parameter=parameter.replace(/[ ]*,[ ]*|[ ]+/g, ';');
						var newCss=[];
						var parameterArr=parameter.split(';');
						for(var i=0;i<parameterArr.length;i++){
							var cssArr=parameterArr[i].split(':');
							if(cssArr.length==2){
								var temp=getStartAndEnd(cssArr);
								if(temp){
									newCss.push(temp)
								}
							}
						}
						var funTime=0;
						var timeFun=function(){
							var styleObj={};
							if(thisTemp.tweenPlay){
								if (funTime < totalTime) {
									funTime += speed;
									for(var i=0;i<newCss.length;i++){
										var ob=newCss[i];
										var ap =Math.ceil(tweenFun(funTime, ob['current'], ob['result'], totalTime));
										switch(ob['type']){
											case 'width':
												styleObj['width']=ap + 'px';
												styleObj['overflow']='hidden';
												break;
											case 'height':
												styleObj['height']=ap + 'px';
												styleObj['overflow']='hidden';
												break;
											case 'left':
												styleObj['left']=ap + 'px';
												break;
											case 'right':
												styleObj['right']=ap + 'px';
												break;
											case 'top':
												styleObj['top']=ap + 'px';
												break;
											case 'bottom':
												styleObj['bottom']=ap + 'px';
												break;
											case 'alpha':
												styleObj['filter']='alpha(opacity:' + ap*0.01 + ')';
												styleObj['opacity']=ap*0.01;
												break;
										}								
									}
									thisTemp.css(styleObj);
								}
								else{
									stopTween();
									if(!isUndefined(callBack)){
										callBack(thisTemp);
									}
								}
							}
						};
						stopTween();
						this.timerTween=setInterval(timeFun,speed);
						return this;
					};
					/*
					 * animatePlay
					 * 功能：播放缓动动画
					 */
					res.animatePlay=function(){
						if(!isUndefined(this.timerTween)){
							this.tweenPlay=true;
						}
						return this;
					};
					/*
					 * animatePause
					 * 功能：暂停缓动动画
					 */
					res.animatePause=function(){
						if(!isUndefined(this.timerTween)){
							this.tweenPlay=false;
						}
						return this;
					};
				} else {
					/*
					 * each
					 * 功能：当相同节点数量大于0时，使用该函数可以进行循环节点
					 * @fn：循环节点时执行的函数
					 */
					res.each = function(fn) {
						for (var i in this) {
							if (this.hasOwnProperty(i)) {
								if (valType(this[i]) == 'htmlobject') {
									fn(i, this[i]);
								} else {
									try {
										if (!this[0].nodeName) fn(i, this[i]);
									} catch (e) {
										fn(i, this[i]);
									}
								}
							}
						}
					};
					/*
					 * eq
					 * 功能：根据索引返回指定节点
					 * @m：为索引数字
					 */
					res.eq = function(m) {
						if (valType(this) == 'htmlarray') {
							if (this.length >= m + 1) {
								return this[m];
							}
							return null;
						}
						return null;
					};
					for (var i = 0; i < res.length; i++) {
						if ($) {
							try {
								res[i] = $(res[i]);
							} catch (event) {}
						}
					}
				}
	
			}
			return res;
		},
		/*
		 * createlDiv
		 * 功能：新建一个DIV节点
		*/
		createlDiv=function(className,html) {
			var ele = document.createElement('div');
			var eleObject = $(ele);
			if (className) {
				eleObject.addClass(className);
			}
			if(html){
				eleObject.htm(html);
			}
			return eleObject;
		},
		/*
		 * createlVideo
		 * 功能：新建一个video节点
		*/
		createlVideo=function() {
			var ele = document.createElement('video');
			var eleObject = $(ele);
			return eleObject;
		},
		/*
		 * createlButton
		 * 功能新建一个button节点
		*/
		createlButton=function(className,html) {
			var ele = document.createElement('button'); 
			var eleObject = $(ele);
			eleObject.attr('type','button');
			if (className) {
				eleObject.addClass(className);
			}
			if(html){
				eleObject.htm(html);
			}
			return eleObject;
		},
		/*
		 * createlA
		 * 功能新建一个a节点
		*/
		createlA=function(html,href,className) {
			var ele = document.createElement('a'); 
			var eleObject = $(ele);
			if(html){
				eleObject.htm(html);
			}
			if(!isUndefined(href) && href){
				eleObject.attr('href',href);
				eleObject.attr('target','_blank');
			}
			if (className) {
				eleObject.addClass(className);
			}
			return eleObject;
		},
		/*
		 * createlJsLink
		 * 功能新建一个a节点
		*/
		createlJsLink=function(html,js) {
			var ele = document.createElement('a'); 
			var eleObject = $(ele);
			if(html){
				eleObject.htm(html);
			}
			if(!isUndefined(js) && js){
				eleObject.attr('href','javascript:'+js+'()');
			}
			else{
				eleObject.attr('href','javascript:;');
			}
			return eleObject;
		},
		/*
		 * createlImg
		 * 功能新建一个img节点
		*/
		createlImg=function(file,className) {
			var ele = document.createElement('img'); 
			var eleObject = $(ele);
			if (className) {
				ele.className = className;
			}
			if(file){
				ele.src=file;
			}
			return eleObject;
		},
		/*
		 * getClient
		 * 功能：获取clientX和clientY
		*/
		getClient=function(event) {
			var eve = event || window.event;
			if (isUndefined(eve)) {
				eve = {
					clientX: 0,
					clientY: 0
				};
			}
			var x=eve.clientX + (document.documentElement.scrollLeft || $('body').scrollLeft),
			y=eve.clientY + (document.documentElement.scrollTop || $('body').scrollTop);
			if(isUndefined(x.toString()) || x.toString()=='NaN'){
				x=eve.touches[0].clientX
			}
			if(isUndefined(y.toString()) || y.toString()=='NaN'){
				y=eve.touches[0].clientY
			}
			if(x.toString()=='NaN'){
				x=0;
			}
			if(y.toString()=='NaN'){
				y=0;
			}
			return {
				x: x,
				y: y
			}
		},
		/*
		 * getLiveTime
		 * 功能：获取直播回放时间
		*/
		getLiveTime=function(){
			var notTime=Date.now();
			var varsTime=vars['live'];
			if(varsTime.toString().length<13){
				varsTime=varsTime*1000;
			}
			return notTime-varsTime;
		},
		/*
		 * formatLiveTime
		 * 功能：将直播的时间戳格式化成标准时间
		*/
		formatLiveTime=function(time){
			var varsTime=vars['live'];
			if(varsTime.toString().length<13){
				varsTime=varsTime*1000;
			}
			varsTime+=time;
			return language['lookBack']+date('H:i:s',varsTime);
		},
		/*
		 * loadJs
		 * 功能：加载js文件并在加载完成后执行callback函数
		 * @file：js文件地址
		 * @callback：加载完成后执行的函数
		*/
		loadJs=function(file, callback) {
			var fn =function() {};
			if(!isUndefined(callback)){
				fn=callback;
			}
			if(checkJs(file)){
				fn();
				return;
			}
			var script = document.createElement('script');
			script.type = 'text/javascript';		
			var isReady = false;
			var timer = null;
			var doReady = function() {
				if(timer){
					clearInterval(timer);
				}
				if(isReady) return;
				isReady = true;
				if(valType(fn) == 'function') {
					fn();
				}
			};
			var bodyLoad = function() {
				timer = setInterval(function() {
					try {
						if(!isUndefined(script.readyState) && script.readyState == 'complete') {
							doReady();
						}
					} catch(event) {};
				}, 10);
			};
			var w3c = function() {
				if(valType(fn) == 'function') {
					fn();
				}
				removeListener(script, 'load', w3c);
				removeListener(script, 'error', w3cError);
			};
			var w3cError = function() {
				if(valType(fn) == 'function') {
					fn();
				}
				removeListener(script, 'load', w3c);
				removeListener(script, 'error', w3cError);
			};
			try {
				addListener(script, 'load', w3c);
				addListener(script, 'error', w3cError);
			} catch(event) {
				bodyLoad();
			}
			script.src = file;
			$('head').eq(0).append(script);
		},
	
		/*
		 * doubleClickEvent
		 * 功能：注册对象双击功能
		 * @ele：注册对象，可以是页面中的任意节点，不支持数组形式
		*/
		doubleClickEvent=function(ele){
			var setTime=null;//注册延时函数
			var clickTime=0;//记录点击时间
			ele=$(ele);
			ele.click(function(){
				if(!setTime){
					setTime=setTimeout(function(){
						try{
							ele.dispatchEvent(new Event('sigClick'));//注册单击，针对视频播放器使用
						}
						catch(event){
							var e = document.createEvent('HTMLEvents');
							e.initEvent('sigClick', false, true);
							ele.dispatchEvent(e);
						}
						clickTime=0;
						setTime=null;
					},260);
				}
				if(!clickTime){
					clickTime=new Date().getTime();
				}
				else{
					if(new Date().getTime()-clickTime<230){//认为是双击
						try{
							ele.dispatchEvent(new Event('dobClick'));//注册双击
						}
						catch(event){
							var e = document.createEvent('HTMLEvents');
							e.initEvent('dobClick', false, true);
							ele.dispatchEvent(e);
						}
						clearTimeout(setTime);
						clickTime=0;
						setTime=null;
					}
				}
			});
		},
		/*
		 * noScrolling
		 * 功能，禁止页面滚动操作
		 */
		noScrolling=function(b){
			if(isUndefined(b)) b=false;
			var bodyScroll=function (event){
				event.preventDefault();
			};
		    if(b){ //禁止滚动
		        $('body').addListener('touchmove', bodyScroll, {passive: false });
		    }
		    else{ //开启滚动
		        $('body').removeListener('touchmove',bodyScroll, {passive: false});
		    }
		};
		return into(videoObj);
	}
	/*
	 * ajax
	 * 功能：ajax功能
	 * @cObj:传递的参数，包含请求地址，请求类型，编码等
	*/
	function ajax(cObj) {
		var callback = null;
		var obj = {
			method: 'get',//请求类型
			dataType: 'json',//请求的数据类型
			charset: 'utf-8',
			async: true,//true表示异步，false表示同步
			url: '',
			data: null,
			success: null,
			error:null
		};
		obj = standardization(obj, cObj);
		if (isUndefined(obj['url'])) {
			return;
		}
		/*
		 * errorFun
		 *功能：执行error
		*/
		var errorFun=function(info){
			if(!isUndefined(obj['error']) && valType(obj['error'])=='function'){
				obj['error'](info);
			}
			else{
				obj['success'](null);
			}
		};
		/*
		 * successFun
		 *功能：执行success
		*/
		var successFun=function(success){
			if(!isUndefined(obj['success']) && valType(obj['success'])=='function'){
				obj['success'](success);
			}
		};
		/*
		 * createXHR
		 *功能：用来为ajax函数提供支持
		*/
		var createXHR=function() {
			if (window.XMLHttpRequest) {
				return new XMLHttpRequest();
			} else if (window.ActiveXObject) {
				try {
					return new ActiveXObject('Microsoft.XMLHTTP');
				} catch(event) {
					try {
						return new ActiveXObject('Msxml2.XMLHTTP');
					} catch(event) {}
				}
			}
		};
		/*
		 * formatParams
		 * 功能：将对象转成地址字符串，为ajax函数提供支持
		 * @obj：传递一个对象
		*/
		var formatParams=function(obj) {
			var arr = [];
			for (var key in obj) {
				arr.push(encodeURIComponent(key) + '=' + encodeURIComponent(obj[key]));
			}
			return arr.join('&');
		};
		if (valType(cObj) != 'object') {
			if(!isUndefined(obj['error'])){
				obj.error(event);
			}
			return;
		}
		
		if (obj.dataType === 'json' || obj.dataType === 'text' || obj.dataType === 'html' || obj.dataType === 'xml') {
			var xhr = createXHR();
			callback = function() {
				//判断http的交互是否成功
				if (xhr.status === 200) {
					if (obj.dataType === 'json') {
						try {
							successFun(eval('(' + xhr.responseText + ')')); //回调传递参数
						}
						catch(event) {
							successFun(null);
						}
					} else {
						successFun(xhr.responseText); //回调传递参数
					}
				} 
				else {
					errorFun({code:xhr.status,message:xhr.statusText});
				}
			};
			obj.data = formatParams(obj.data); //通过params()将名值对转换成字符串
			if (obj.method === 'get' && !isUndefined(obj.data)) {
				if (obj.data != '') {
					if (obj.url.indexOf('?') == -1) {
						obj.url += '?' + obj.data
					} else {
						obj.url += '&' + obj.data;
					}
				}
			}
			if (obj.async === true) { //true表示异步，false表示同步
				addListener(xhr,'readystatechange',function(event){
					if (this.readyState === 4 && callback != null) { //判断对象的状态是否交互完成
						callback(); //回调
					}
				});
			}
			xhr.open(obj.method, obj.url, obj.async);
			if (obj.method === 'post') {
				try{
					xhr.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');
					xhr.setRequestHeader('charset', obj['charset']);
					xhr.send(obj.data);
				}
				catch(event){
					callback();
				}
			}
			else {
				try{
					xhr.send(null); //get方式则填null
				}
				catch(event){
					callback();
				}
			}
			if (obj.async === false) { //同步
				callback();
			}

		}
		else if (obj.dataType === 'jsonp') {
			var oHead = document.getElementsByTagName('head')[0];
			var oScript = document.createElement('script');
			var callbackName = 'callback' + new Date().getTime();
			var params = formatParams(obj.data) + '&callback=' + callbackName; //按时间戳拼接字符串
			//拼接好src
			oScript.src = obj.url.split('?') + '?' + params;
			//插入script标签
			oHead.insertBefore(oScript, oHead.firstChild);
			//jsonp的回调函数
			window[callbackName] = function(json) {
				if(!isUndefined(obj['success']) && valType(obj['success'])==='function'){
					obj['success'](json);
				}
				oHead.removeChild(oScript);
			};
		}
	}
	/*
	 * documentReady
	 * 功能：判断页面加载完成
	 * @fn：页面加载完成后执行的函数
	*/
	function documentReady(fn) {
		var isReady = false;
		var timer = null;
		var doReady = function() {
			if(timer) clearInterval(timer);
			if(isReady) return;
			isReady = true;
			if(valType(fn) == 'function') {
				fn();
			}
		};
		var bodyLoad = function() {
			timer = setInterval(function() {
				try {
					if(!isUndefined(document.readyState) && document.readyState == 'complete') {
						doReady();
					}
				} catch(event) {};
			}, 10);
		};
		var w3c = function() {
			
			if(valType(fn) == 'function') {
				fn();
			}
			removeListener(window, 'load', w3c);
		};
		if(!isUndefined(document.body)){
			if(valType(fn) == 'function') {
				fn();
			}
		}
		else{
			try {
				addListener(window, 'load', w3c);
			} catch(event) {
				bodyLoad();
			}
		}
		
	}
	/*
	 * getPath
	 * 功能：获取该js文件所在路径
	*/
	function getPath(siz) {
		var scriptList = document.scripts,
			thisPath = scriptList[scriptList.length - 1].src;
		for (var i = 0; i < scriptList.length; i++) {
			var scriptName = scriptList[i].getAttribute('name') || scriptList[i].getAttribute('data-name');
			var src = scriptList[i].src.slice(scriptList[i].src.lastIndexOf('/') + 1, scriptList[i].src.lastIndexOf('.'));
			if ((scriptName && (scriptName == 'ckplayer' || scriptName == 'ckplayer.min')) || (scriptList[i].src && (src == 'ckplayer' || src == 'ckplayer.min'))) {
				thisPath = scriptList[i].src;
				break;
			}
		}
		var path=thisPath.substring(0, thisPath.lastIndexOf('/js/') + 1);
		if(!isUndefined(siz)){
			path+=siz+'/';
		}
		return path;
	}
	/*
	 * checkJs
	 * 功能：判断js是否已加载
	 * @file：js文件路径
	*/
	function checkJs(file) {
		var scriptList = document.scripts;
		for (var i = 0; i < scriptList.length; i++) {
			if(scriptList[i].src==file){
				return scriptList[i];
			}
		}
		return false;
	}
	/*
	 * getMaxZIndex
	 * 功能：获取当前页面最大深度
	*/
	function getMaxZIndex() {
		var arr = document.all || document.querySelectorAll('*');
		var maxZ = -1;
		for (var i = 0; i < arr.length; i++) {
			var temp = null;
			try {
				temp = window.getComputedStyle(arr[i], null).zIndex;
			} catch (event) {
				if (arr[i].style) {
					temp = arr[i].style.zIndex;
				}
			}
			if (temp != 'auto' && parseInt(temp) > maxZ) {
				maxZ = parseInt(temp);
			}
		}
		return maxZ;
	}
	/*
	 * getWindowSize
	 * 功能：获取window的宽和高
	*/
	function getWindowSize() {
		return {
			width: window.innerWidth,
			height: window.innerHeight
		};
	}
	/*
	 * standardization
	 * 功能：将对象Object标准化，将n对象替换进o对象
	 * @o:标准化对象，@n：外部传递对象
	*/
	function standardization(o, n) { //n替换进o
		var h = {};
		var k;
		for (k in o) {
			h[k] = o[k];
		}
		for (k in n) {
			if(k in h){
				h[k] = n[k];
			}
		}
		return h;
	}
	/*
	 * mergeObj
	 * 功能：将新对象合并到原对象中，需要确保原对像里有对应的值并且类型一样
	 * @o:原对象，@n：新对象
	*/
	function mergeObj(o,n){
		var h = {};
		var k;
		for (k in o) {
			h[k] = o[k];
		}
		for (k in n) {
			if(k in h){
				switch(valType(h[k])){
					case 'object':
						if(valType(n[k])=='object'){
							h[k] = mergeObj(h[k],n[k]);
						}
						break;
					default:
						if(valType(h[k])==valType(n[k])){
							h[k] = n[k];
						}
						break;
				}
			}
		}
		return h;
	}
	/*
	 * valType
	 * 功能：判断变量类型
	*/
	function valType(val) {
		if(typeof val==='undefined') return 'undefined';
		var str=Object.prototype.toString.call(val).split(' ')[1].replace(']','').toLowerCase();
		if(str.substr(0,4)=='html' && (str.substr(-7)=='element' || str.substr(-8)=='document' || str=='window')){
			str='htmlobject';
		}
		if(str=='htmlcollection' || str=='nodelist'){
			str='htmlarray';
		}
		return str;
	}
	/*
	 * isUndefined
	 * 功能：判断变量是否存在或值是否为undefined
	*/
	function isUndefined(val) {
		try {
			return valType(val)==='undefined' || val === undefined || val === null || (valType(val)==='number' && isNaN(val));
		} catch (event) {
			return true;
		}
		return false;
	}
	/*
	 * prev
	 * 功能：获取指定节点前一个同胞节点
	 * @ele：要获取的节点
	*/
	function prev(ele) {
		var e = ele.previousSibling;
		if (e == null) { //测试同胞节点是否存在，否则返回空
			return null;
		}
		if (e.nodeType == 3) { //如果同胞节点为文本节点
			var t = prev(e);
			if (t && t.nodeType == 1) {
				return t;
			}
		} else {
			if (e.nodeType == 1) { //确认节点为节点节点才返回
				return e;
			} else {
				return false;
			}
		}
	}
	/*
	 * next
	 * 功能：获取指定节点下一个同胞节点
	 * @ele：要获取的节点
	*/
	function next(ele) {
		var e = ele.nextSibling;
		if (e == null) { //测试同胞节点是否存在，否则返回空
			return null;
		}
		if (e.nodeType == 3) { //如果同胞节点为文本节点
			var t = next(e);
			if (t && t.nodeType == 1) {
				return t;
			}
		} else {
			if (e.nodeType == 1) { //确认节点为节点才返回
				return e;
			} else {
				return false;
			}
		}
	}
	/*
	 * addListener
	 * 功能：事件监听
	 * @ele：监听对象，@e：事件名称，@f：返回事件函数
	*/
	function addListener(ele, e, f, t) {
		if (isUndefined(t)) {
			t = false;
		}
		if (ele.addEventListener) {
			try {
				ele.addEventListener(e, f, t);
			} catch (event) {}
		} else if (ele.attachEvent) {
			try {
				ele.attachEvent('on' + e, f);
			} catch (event) {}
		} else {
			ele['on' + e] = f;
		}
		if(isUndefined(ele.listenerList)){
			ele.listenerList=[];
		}
		ele.listenerList.push([e,f]);
		return ele;
	};
	/*
	 * removeListener
	 * 功能：删除事件监听
	 * @ele：监听对象，@e：事件名称，@f：返回事件函数
	*/
	function removeListener(ele, e, f, t) {
		if (isUndefined(t)) {
			t = false;
		}
		if (ele.removeEventListener) {
			try {
				ele.removeEventListener(e, f, t);
			} catch (e) {}
		} else if (ele.detachEvent) {
			try {
				ele.detachEvent('on' + e, f);
			} catch (e) {}
		} else {
			ele['on' + e] = null;
		}
		if(!isUndefined(ele.listenerList)){
			var temp=[];
			for(var i=0;i<ele.listenerList.length;i++){
				if(ele.listenerList[i][0]!=e || ele.listenerList[i][1]!=f){
					temp.push(ele.listenerList[i]);
				}
			}
			ele.listenerList=temp;
		}
		return ele;
	}
	/*
	 * formatSeconds
	 * 功能：將秒转化成时分秒
	*/
	function formatSeconds(val) {
		if(isUndefined(val)) val=0;
		var result = parseInt(val);
		if(isUndefined(result)) result=0;
		if(result<0) result=0;
		var h = Math.floor(result / 3600) < 10 ? '0' + Math.floor(result / 3600) : Math.floor(result / 3600),
		m = Math.floor((result / 60 % 60)) < 10 ? '0' + Math.floor((result / 60 % 60)) : Math.floor((result / 60 % 60)),
		s = Math.floor((result % 60)) < 10 ? '0' + Math.floor((result % 60)) : Math.floor((result % 60)),
		res = '';
		if(h !== '00') res += h+':';
		res += m+':'+s;
		return res;
	}
	/*
	 * date
	 * 功能：格式化的时间字符串
	*/
	function date(format, timestamp) {
		if(isUndefined(timestamp)){
			timestamp=new Date();
		}
		if(timestamp.toString().length<13) timestamp=timestamp*1000;
		var a, jsdate = ((timestamp) ? new Date(timestamp) : new Date());
		var pad = function(n, c) {
			if((n = n + '').length < c) {
				return new Array(++c - n.length).join('0') + n;
			} else {
				return n;
			}
		};
		var txt_weekdays = ['Sunday', 'Monday', 'Tuesday', 'Wednesday', 'Thursday', 'Friday', 'Saturday'];
		var txt_ordin = {
			1: 'st',
			2: 'nd',
			3: 'rd',
			21: 'st',
			22: 'nd',
			23: 'rd',
			31: 'st'
		};
		var txt_months = ['', 'January', 'February', 'March', 'April', 'May', 'June', 'July', 'August', 'September', 'October', 'November', 'December'];
		var f = {
			// Day 
			d: function() {
				return pad(f.j(), 2)
			},
			D: function() {
				return f.l().substr(0, 3)
			},
			j: function() {
				return jsdate.getDate()
			},
			l: function() {
				return txt_weekdays[f.w()]
			},
			N: function() {
				return f.w() + 1
			},
			S: function() {
				return txt_ordin[f.j()] ? txt_ordin[f.j()] : 'th'
			},
			w: function() {
				return jsdate.getDay()
			},
			z: function() {
				return(jsdate - new Date(jsdate.getFullYear() + '/1/1')) / 864e5 >> 0
			},
	
			// Week 
			W: function() {
				var a = f.z(),
					b = 364 + f.L() - a;
				var nd2, nd = (new Date(jsdate.getFullYear() + '/1/1').getDay() || 7) - 1;
				if(b <= 2 && ((jsdate.getDay() || 7) - 1) <= 2 - b) {
					return 1;
				} else {
					if(a <= 2 && nd >= 4 && a >= (6 - nd)) {
						nd2 = new Date(jsdate.getFullYear() - 1 + '/12/31');
						return date('W', Math.round(nd2.getTime() / 1000));
					} else {
						return(1 + (nd <= 3 ? ((a + nd) / 7) : (a - (7 - nd)) / 7) >> 0);
					}
				}
			},
	
			// Month 
			F: function() {
				return txt_months[f.n()]
			},
			m: function() {
				return pad(f.n(), 2)
			},
			M: function() {
				return f.F().substr(0, 3)
			},
			n: function() {
				return jsdate.getMonth() + 1
			},
			t: function() {
				var n;
				if((n = jsdate.getMonth() + 1) == 2) {
					return 28 + f.L();
				} else {
					if(n & 1 && n < 8 || !(n & 1) && n > 7) {
						return 31;
					} else {
						return 30;
					}
				}
			},
	
			// Year 
			L: function() {
				var y = f.Y();
				return(!(y & 3) && (y % 1e2 || !(y % 4e2))) ? 1 : 0
			},
			//o not supported yet 
			Y: function() {
				return jsdate.getFullYear()
			},
			y: function() {
				return(jsdate.getFullYear() + '').slice(2)
			},
	
			// Time 
			a: function() {
				return jsdate.getHours() > 11 ? 'pm' : 'am'
			},
			A: function() {
				return f.a().toUpperCase()
			},
			B: function() {
				// peter paul koch: 
				var off = (jsdate.getTimezoneOffset() + 60) * 60;
				var theSeconds = (jsdate.getHours() * 3600) + (jsdate.getMinutes() * 60) + jsdate.getSeconds() + off;
				var beat = Math.floor(theSeconds / 86.4);
				if(beat > 1000) beat -= 1000;
				if(beat < 0) beat += 1000;
				if((String(beat)).length == 1) beat = '00' + beat;
				if((String(beat)).length == 2) beat = '0' + beat;
				return beat;
			},
			g: function() {
				return jsdate.getHours() % 12 || 12
			},
			G: function() {
				return jsdate.getHours()
			},
			h: function() {
				return pad(f.g(), 2)
			},
			H: function() {
				return pad(jsdate.getHours(), 2)
			},
			i: function() {
				return pad(jsdate.getMinutes(), 2)
			},
			s: function() {
				return pad(jsdate.getSeconds(), 2)
			},
			//u not supported yet 
	
			// Timezone 
			//e not supported yet 
			//I not supported yet 
			O: function() {
				var t = pad(Math.abs(jsdate.getTimezoneOffset() / 60 * 100), 4);
				if(jsdate.getTimezoneOffset() > 0) t = '-' + t;
				else t = '+' + t;
				return t;
			},
			P: function() {
				var O = f.O();
				return(O.substr(0, 3) + ':' + O.substr(3, 2))
			},
			//T not supported yet 
			//Z not supported yet 
	
			// Full Date/Time 
			c: function() {
				return f.Y() + '-' + f.m() + '-' + f.d() + 'T' + f.h() + ':' + f.i() + ':' + f.s() + f.P()
			},
			//r not supported yet 
			U: function() {
				return Math.round(jsdate.getTime() / 1000)
			}
		};
	
		return format.replace(/([a-zA-Z])/g,function(t, s) {
			var ret='';
			if(t != s) {
				// escaped 
				ret = s;
			} else if(f[s]) {
				// a date function exists 
				ret = f[s]();
			} else {
				// nothing special 
				ret = s;
			}
			return ret;
		});
	}
	/*
	 * dataURLtoBlob
	 * 功能，base64转blob
	 */
	function dataURLtoBlob(dataurl) {
		var arr = dataurl.split(','), mime = arr[0].match(/:(.*?);/)[1],bstr = atob(arr[1]), n = bstr.length, u8arr = new Uint8Array(n);
		while (n--) {
			u8arr[n] = bstr.charCodeAt(n);
		}
		return new Blob([u8arr], { type: mime });
	}
	/*
	 * decodeURIString
	 * 功能，解码中文
	 */
	function decodeURIString(obj){
		for(var k in obj){
			if(valType(obj[k])=='string' && obj[k]){
				try{
					obj[k]=decodeURI(obj[k]);
				}
				catch(event){}
			}
			if(valType(obj[k])=='object'){
				obj[k]=decodeURIString(obj[k]);
			}
		}
		return obj;
	}
	/*
	 * getApp
	 * 功能，获取平台类型是否是iphone
	 */
	function getApp(){
		var u = navigator.userAgent.toLowerCase();
		if(u.indexOf('iphone')>-1){
			return 'iphone';
		}
	}
	/*
	 * cookie
	 * 功能，操作cookie
	 */
	function cookie(name,value,domain,path){
		if(isUndefined(domain)){
			domain='';
		}
		if(isUndefined(path)){
			path='/';
		}
		var ckStr=';domain='+domain+';path='+path;
		if(location.protocol=='https'){
			ckStr+=';SameSite=None;Secure=true';
		}
		var set=function(name,value){
			var time = 360*24*60*60*1000;
			var exp = new Date();
			exp.setTime(exp.getTime() + time);
			try{
				document.cookie = name + '='+ escape (value) + ';expires=' + exp.toGMTString()+ckStr;
			}
			catch(event){console.error(event)}
		},
		get=function(name){
			var arr,reg=new RegExp('(^| )'+name+'=([^;]*)(;|$)');
			if(arr=document.cookie.match(reg)){
				return unescape(arr[2]);
			}
			else{
				return null;
			}
		},
		del=function(name){
			var exp = new Date();
			exp.setTime(exp.getTime() - 1);
			var cval=get(name);
			if(cval!=null){
				document.cookie= name + '='+cval+';expires='+exp.toGMTString()+ckStr;
			}
		};
		if(!isUndefined(name) && !isUndefined(value)){
			if(value=='delete'){
				del(name);
				return null;
			}
			else{
				set(name,value);
				return get(name);
			}
			
		}
		else if(!isUndefined(name) && isUndefined(value)){
			return get(name);
		}
		else if(!isUndefined(name)){
			
		}
	}
	/*
	 * arrayToString
	 * 功能，二维数组传化成字符串
	 */
	function arrayToString(arr) {
		var str='';
		if(!isUndefined(arr)){
			for(var i=0;i<arr.length;i++){
				var temp=arr[i];
				if(i>0){
					str+=',';
				}
				for(var y=0;y<temp.length;y++){
					if(y>0){
						str+=';';
					}
					if(valType(temp[y])=='number'){
						str+=parseInt(temp[y]*100);
					}
					else{
						str+=temp[y];
					}
					
				}
			}
		}
		return str;
	}
	/*
	 * stringToArray
	 * 功能，字符串转化成二维数组
	 */
	function stringToArray(str) {
		var newArr=[];
		if(!isUndefined(str)){
			var arr=str.split(',');
			for(var i=0;i<arr.length;i++){
				var temp=arr[i].split(';');
				temp[1]=parseInt(temp[1])*0.01;
				temp[2]=parseInt(temp[2])*0.01;
				newArr.push(temp);
			}
		}
		return newArr;
	}
	/*
	 * documentHidden
	 * 功能，监听页面切换到其它标签，执行fn函数
	 */
	function documentHidden(fn){
		if(!isUndefined(document.visibilityState)){
			fn(document.visibilityState === 'visible'?'show':'hidden');
			addListener(document,'visibilitychange',function(){
				fn(document.visibilityState === 'visible'?'show':'hidden');
			});
		}
	}
	return ckplayerEmbed;
}));