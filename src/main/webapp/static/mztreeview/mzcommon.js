/**---------------------------------------------------------------------------*\
|  Subject: JavaScript Framework
|  Author:  meizz
|  Created: 2005-02-27
|  Version: 2006-08-11
|-----------------------------------
|  MSN:huangfr@msn.com  QQ:112889082
|  http://www.meizz.com  Copyright (c) meizz   MIT-style license
|  The above copyright notice and this permission notice shall be
|  included in all copies or substantial portions of the Software
\*---------------------------------------------------------------------------*/

window.System = function(){this.setHashCode();}

System.debug=true; //false
System._codebase={};
try
{
  if (window!=parent && parent.System && parent.System._codebase)
    System._codebase = parent.System._codebase;
  else if ("undefined"!=typeof opener&&opener.System&&opener.System._codebase)
    System._codebase = opener.System._codebase;
  else if ("undefined"!=typeof dialogArguments && dialogArguments.System)
    System._codebase = dialogArguments.System._codebase;
}
catch(ex){}

System.MISSING_ARGUMENT="Missing argument";
System.ARGUMENT_PARSE_ERROR="The argument cannot be parsed";
System.NOT_SUPPORTED_XMLHTTP="Your browser do not support XMLHttp";
System.FILE_NOT_FOUND="File not found";
System.MISCODING="Maybe file encoding is not ANSI or UTF-8";
System.NAMESPACE_ERROR=" nonstandard namespace";

System.hashCounter=0;
System.currentVersion="20060811";
var t=document.getElementsByTagName("SCRIPT");
t=(System.scriptElement=t[t.length-1]).src.replace(/\\/g, "/");
System.incorporate=function(d,s){for(var i in s)d[i]=s[i];return d;};
System.path=(t.lastIndexOf("/")<0)?".":t.substring(0, t.lastIndexOf("/"));
System.getUniqueId=function(){return "mz_"+(System.hashCounter++).toString(36);};
System.toHashCode=function(e)
{
  if("undefined"!=typeof e.hashCode) return e.hashCode;
  return e.hashCode=System.getUniqueId();
};
System.supportsXmlHttp=function()
{
  return "object"==typeof(System._xmlHttp||(System._xmlHttp=new XMLHttpRequest()));
};
System._getPrototype=function(namespace, argu)
{
  if("undefined"==typeof System._prototypes[namespace])return new System();
  for(var a=[], i=0; i<argu.length; i++) a[i]="argu["+ i +"]";
  return eval("new (System._prototypes['"+namespace+"'])("+a.join(",")+")");
};
System.ie=navigator.userAgent.indexOf("MSIE")>0 && !window.opera;
System.ns=navigator.vendor=="Netscape";
System._alert=function(msg){if(System.debug)alert(msg);};
System._parseResponseText=function(s)
{
    if (null==s||"\uFFFD"==s.charAt(0)){System._alert(System.MISCODING);return "";}
    if ("\xef"==s.charAt(0))s=s.substr(3); //for firefox
    return s.replace(/(^|\n)\s*\/\/+\s*((Using|Import|Include)\((\"|\'))/g,"$1$2");
};

if(window.ActiveXObject && (System.ie || !window.XMLHttpRequest))
{
  window.XMLHttpRequest = function()
  {
    var msxmls=['MSXML3','MSXML2','Microsoft'], ex;
    for(var i=0;i<msxmls.length;i++)
      try{return new ActiveXObject(msxmls[i]+'.XMLHTTP')} catch(ex){}
    System._xmlHttp="mz"; throw new Error(System.NOT_SUPPORTED_XMLHTTP);
  }
}
System.load = function(namespace, path)
{
  try
  {
    if(System.supportsXmlHttp()){path=System._mapPath(namespace, path);
    var x=System._xmlHttp; x.open("GET",path,false); x.send(null);
    if (x.readyState==4)
    {
      if(x.status==0||/^file\:/i.test(path))
        return System._parseResponseText(x.responseText);
      else if(x.status==200)return System._parseResponseText(x.responseText);
      else if(x.status==404)System._alert(namespace+"\n"+System.FILE_NOT_FOUND);
      else throw new Error(x.status +": "+ x.statusText);}
    } else System._alert(System.NOT_SUPPORTED_XMLHTTP);
  }
  catch(ex){System._alert(namespace+"\n"+ex.message);}return "";
};
System._eval = function(namespace, path)
{
  //alert("System._eval(\""+namespace+"\")=\r\n"+System._codebase[namespace]);
  try{if(window.execScript)window.execScript(System._codebase[namespace]);else
  {
    var script=document.createElement("SCRIPT");script.type="text/javascript";
    script.innerHTML="eval(System._codebase['"+ namespace +"']);";
    document.getElementsByTagName("HEAD")[0].appendChild(script);
    setTimeout(function(){script.parentNode.removeChild(script)},99);
  }}catch(ex){/*System._alert("Syntax error on load "+ namespace);*/}
  System._existences[namespace]=System._mapPath(namespace, path);
};
System._exist = function(namespace, path)
{
  if("undefined"==typeof System._existences[namespace]) return false;
  return System._existences[namespace]==System._mapPath(namespace,path);
};
System._mapPath = function(namespace, path)
{
  if("string"==typeof path && path.length>3)return path;//.toLowerCase();
  var p=(System.path+"/"+namespace.replace(/\./g,"/")+".js");//.toLowerCase();
  return p +(("undefined"==typeof path||path) ? "" : "?t="+ Math.random());
};

window.Using = function(namespace, path, rename)
{
  if(System._exist(namespace, path)){
  var s=window[namespace.substr(namespace.lastIndexOf(".")+1)];
  if(s!=System._prototypes[namespace])s=System._prototypes[namespace];return}
  var code=namespace +"."; if(!/((^|\.)[\w\$]+)+$/.test(namespace))
  throw new Error(namespace+System.NAMESPACE_ERROR);
  for(var i=code.indexOf("."); i>-1; i=code.indexOf(".", i+1)){
  var e= code.substring(0,i), s=(e.indexOf(".")==-1) ? "window[\""+e+"\"]":e;
  if(e&&"undefined"==typeof(s)){
  eval(s+"=function(){return System._getPrototype(\""+e+"\", arguments)}");}}
  if("undefined"==typeof path &&"string"==typeof System._codebase[namespace])
  {
    System._eval(namespace, path);}else{if(code=System.load(namespace,path)){
    e = "$"+ System.getUniqueId() +"__id"+ new Date().getTime() +"$";
    s = "function "+e+"(){\r\n"+code+";\r\nSystem._prototypes['";
    code=namespace.substr(namespace.lastIndexOf(".")+1);
    s += namespace+"']=window['"+(rename||code)+"']="+code+";\r\n}"+e+"();";
    System._codebase[namespace]=s;s="";System._eval(namespace, path);}
  }
};
window.Import=function(namespace,path,rename){Using(namespace,path,rename)};
window.Instance=function(hashCode){return System._instances[hashCode]};
window.Include=function(namespace, path)
{
  if(System._exist(namespace, path)) return;
  var code;if(!/((^|\.)[\w\$]+)+$/.test(namespace))
  throw new Error(namespace + System.NAMESPACE_ERROR);
  if("undefined"==typeof path&&"string"==typeof(System._codebase[namespace]))
  {
    System._eval(namespace, path);}else if(System.supportsXmlHttp()){
    if(code=System.load(namespace, path)){System._codebase[namespace]=code;
    System._eval(namespace, path);}}else{
    var script=document.createElement("SCRIPT");script.type="text/javascript";
    script.src=System._existences[namespace]=System._mapPath(namespace,path);
    document.getElementsByTagName("HEAD")[0].appendChild(script);
    setTimeout(function(){script.parentNode.removeChild(script)},99);
  }
};

Function.READ=1;Function.WRITE=2;Function.READ_WRITE=3;
Function.prototype.addProperty=function(name,initValue,r_w)
{
  var capital=name.charAt(0).toUpperCase()+name.substr(1);
  r_w=r_w||Function.READ_WRITE; name="_"+name; var p=this.prototype;
  if("undefined"!=typeof initValue) p[name]=initValue;
  if(r_w&Function.READ) p["get"+ capital]=function(){return this[name];};
  if(r_w&Function.WRITE) p["set"+ capital]=function(v){this[name]=v;};
};
Function.prototype.Extends=function(SuperClass,ClassName)
{
  var op=this.prototype, i, p=this.prototype=new SuperClass();
  if(ClassName)p._className=ClassName; for(i in op)p[i]=op[i];
  if(p.hashCode)delete System._instances[p.hashCode];return p;
};
System._instances={};
System._prototypes=
{
  "System":System,
  "System.Object":System,
  "System.Event":System.Event
};
System._existences=
{
  "System":System._mapPath("System"),
  "System.Event":System._mapPath("System.Event"),
  "System.Object":System._mapPath("System.Object")
};
t=System.Extends(Object, "System"); System.Object = System;
t.decontrol=function(){var t;if(t=this.hashCode)delete System._instances[t]};
t.addEventListeners=function(type, handle)
{
  if("function"!=typeof handle)
    throw new Error(this+" addEventListener: "+handle+" is not a function");
  if(!this._listeners) this._listeners={};
  var id=System.toHashCode(handle), t=this._listeners; 
  if("object"!=typeof t[type]) t[type]={}; t[type][id]=handle;
};
t.removeEventListener=function(type, handle)
{
  if(!this._listeners)this._listeners={}; var t=this._listeners;
  if(!t[type]) return; var id=System.toHashCode(handle);
  if( t[type][id])delete t[type][id];if(t[type])delete t[type];
};
t.dispatchEvent=function(evt)
{
  if(!this._listeners)this._listeners={};
  var i, t =this._listeners, p =evt.type;
  evt.target=evt.srcElement=this; if(this[p])this[p](evt);
  if("object"==typeof t[p]) for(i in t[p]) t[p][i].call(this,evt);
  delete evt.target; delete evt.srcElement;return evt.returnValue;
};
t.setHashCode=function()
{
  System._instances[(this.hashCode=System.getUniqueId())]=this;
};
t.getHashCode=function()
{
  if(!this.hashCode)this.setHashCode(); return this.hashCode;
};
t.toString=function(){return "[object "+(this._className||"Object")+"]";};
System.Event=function(type){this.type=type;};
t=System.Event.Extends(System, "System.Event");
t.returnValue=true;
t.cancelBubble=false;
t.target=t.srcElement=null;
t.stopPropagation=function(){this.cancelBubble=true;};
t.preventDefault =function(){this.returnValue=false;};

//if(System.ie && !System.debug) Incl ude("System.Plugins.IE"); //IE UserData
//if(window.opera) Inclu de("System.Plugins.Opera"); //Opera support
//In cl  ude("System.Global");
//I ncl  ude("System.Web.UI.WebControls.MzTreeView");

/**---------------------------------------------------------------------------*\
|  Subject:    Plugins for Internet Explorer
|  NameSpace:  System.Plugins.IE
|  Author:     meizz
|  Created:    2006-02-24
|  Version:    2006-05-25
|-------------------------------------------------------------
|  MSN: huangfr@msn.com   QQ: 112889082   http://www.meizz.com
|  Email: mz@126.com      CSDN ID:meizz   Copyright (c)  meizz
\*---------------------------------------------------------------------------*/

//System.scriptElement.addBehavior("#default#userdata");

System.encodeNameSpace=function(path)
{
  return path.replace(/\W/g, "_");
}
System.saveUserData=function(key, value)
{
  try
  {
    var t=System.scriptElement;
    var d=new Date(); d.setDate(d.getDate()+1);  //1 day
    t.load(System.encodeNameSpace(key));
    t.setAttribute("code", value);
    t.setAttribute("version", System.currentVersion);
    t.expires=d.toUTCString();
    t.save(System.encodeNameSpace(key));
    return  t.getAttribute("code");
  }
  catch (ex){}
}

System.loadUserData=function(key)
{
  try
  {
    var t=System.scriptElement;
    t.load(System.encodeNameSpace(key));
    if(System.currentVersion!=t.getAttribute("version")){
    if(t.getAttribute("code"))System.deleteUserData(key);
      return null;} return t.getAttribute("code");
  }
  catch (ex){return null;}
}

System.deleteUserData=function(key)
{
  try
  {
    var t=System.scriptElement;
  	t.load(System.encodeNameSpace(key));
    t.expires = new Date(315532799000).toUTCString();
    t.save(System.encodeNameSpace(key));
  }
  catch (ex){}
}

System.load = function(ns, path)
{
  path=System._mapPath(ns, path);
  if(/(\.js|\.html?)$/i.test(path))
  {
    var code=System.loadUserData(ns); if(code){
    return (System._codebase[ns]="//from userdata\r\n"+ code);}
  }
  function s(t){t=System._parseResponseText(t);System.saveUserData(ns,t);return t}
  try
  {
    if(System.supportsXmlHttp())
    {
      var x=System._xmlHttp; x.open("GET",path,false); x.send(null);
      if (x.readyState==4)
      {
        if(/^file\:/i.test(path)||x.status==0)return s(x.responseText);
        else if(x.status==200)   return s(x.responseText);
        else if(x.status==404)System._alert(ns+"\n"+System.FILE_NOT_FOUND);
        else throw new Error(x.status +": "+ x.statusText);
      }
    }
    else System._alert(System.NOT_SUPPORTED_XMLHTTP);
  }
  catch(ex){System._alert(ns+"\n"+ex.message);}return "";
};

/**---------------------------------------------------------------------------*\
|  Subject:    Base Function
|  NameSpace:  System
|  Author:     meizz
|  Created:    2006-02-24
|  Version:    2006-05-25
|-------------------------------------------------------------
|  MSN: huangfr@msn.com   QQ: 112889082   http://www.meizz.com
|  Email: mz@126.com      CSDN ID:meizz   Copyright (c)  meizz
\*---------------------------------------------------------------------------*/

//HTMLElement.getElementById extend
if(document && !document.getElementById)
{
  document.getElementById=function(id)
  {
    if(document.all) return document.all(id); return null;
  }
}
function searchByTagName(e, TAG)
{
  while(e!=null && e.tagName){if(e.tagName==TAG.toUpperCase())
  return(e); e=e.parentNode; } return null;
}
function searchById(e, id)
{
  while(e = e.parentNode){if(e.id==id) return(e);} return null;
}
function loadCssFile(url, uniqueId)
{
  if(document.getElementById(uniqueId)) return;
  if(/\w+\.\w+(\?|$)/.test(url))
  {
    var link  = document.createElement("LINK");
    link.href = url;
    link.id   = uniqueId;
    link.type = "text/css";
    link.rel  = "Stylesheet";
    document.getElementsByTagName("HEAD")[0].appendChild(link);
  }
};
function MzHideShow(bool, i)
{
  var F=MzHideShow;
  if(typeof(i)=="undefined")
  {
    i=0; var e=F.target.style;
    bool=F.target.style.display=="none";
    e.display="";   e.overflow="hidden";
    F._height=parseFloat(F.target.offsetHeight);
    F.height=e.height;
    F.overflow=e.overflow;
  } i=i+F.step;
  if(i>=100)
  {
    var e=F.target.style;
    e.display = bool ? "" : "none";
    F.trigger.src = bool ? F.hideSrc : F.showSrc;
    e.height=F.height;
    e.overflow=F.overflow;
  }
  else
  {
    var n = bool ? i/100: (100-i)/100;
    F.target.style.height=Math.ceil(F._height*n) +"px";
    setTimeout("MzHideShow("+ bool +","+ i +")", F.delay);
  }
}
MzHideShow.step=9;
MzHideShow.delay=1;
MzHideShow.showSrc="/images/show.gif";
MzHideShow.hideSrc="/images/hide.gif";
function hs(e, id)
{
  var o=document.getElementById(id);
  if(o)
  {
    MzHideShow.target = o;
    MzHideShow.trigger = e;
    MzHideShow();
  }
}
/*
function sleep(ms)
{
  var start = new Date().getTime();
  var xh = new XMLHttpRequest();
  while(new Date().getTime()-start<ms)
  {
    try{
      xh.open("GET", "file:///c:\\?mz="+ Math.random(), false);
      xh.send(null);
    }catch(ex){return;}
  }
}

function Sleep(n){var start=new Date().getTime(); //for opera only
while(true) if(new Date().getTime()-start>n) break;}
*/


/*-=< HTMLElement >=-*/
if("undefined"!=typeof HTMLDocument)
{
  if("undefined"==typeof HTMLDocument.prototype.createStyleSheet)
  {
    HTMLDocument.prototype.createStyleSheet=function(url)
    {
      var e;if(/[\w\-\u4e00-\u9fa5]+\.\w+(\?|$)/.test(url))
      {
        e=document.createElement("LINK"); e.type="text/css";
        e.rel="Stylesheet"; e.href=url;}else{
        e=document.createElement("STYLE"); e.type="text/css";
      }
      document.getElementsByTagName("HEAD")[0].appendChild(e);
      return e;
    };
  }
}
if(typeof(HTMLElement)!="undefined" && !window.opera)
{

  HTMLElement.prototype.contains = function(obj)
  {
    if(obj==this)return true;
    if(obj==null)return false;
    return this.contains(obj.parentNode);
  };
  HTMLElement.prototype.__defineGetter__("outerHTML",function()
  {
    var a=this.attributes, str="<"+this.tagName, i=0;for(;i<a.length;i++)
    if(a[i].specified) str+=" "+a[i].name+'="'+a[i].value+'"';
    if(!this.canHaveChildren) return str+" />";
    return str+">"+this.innerHTML+"</"+this.tagName+">";
  });
  HTMLElement.prototype.__defineSetter__("outerHTML",function(s)
  {
    var r = this.ownerDocument.createRange();
    r.setStartBefore(this);
    var df = r.createContextualFragment(s);
    this.parentNode.replaceChild(df, this);
    return s;
  });
  HTMLElement.prototype.__defineGetter__("canHaveChildren",function()
  {
    return !/^(area|base|basefont|col|frame|hr|img|br|input|isindex|link|meta|param)$/.test(this.tagName.toLowerCase());
  });
}
if(!window.attachEvent && window.addEventListener)
{
  Window.prototype.attachEvent = HTMLDocument.prototype.attachEvent=
  HTMLElement.prototype.attachEvent=function(en, func, cancelBubble)
  {
    var cb = cancelBubble ? true : false;
    this.addEventListener(en.toLowerCase().substr(2), func, cb);
  };
  Window.prototype.detachEvent = HTMLDocument.prototype.detachEvent=
  HTMLElement.prototype.detachEvent=function(en, func, cancelBubble)
  {
    var cb = cancelBubble ? true : false;
    this.removeEventListener(en.toLowerCase().substr(2), func, cb);
  };
}/*
if(typeof Event!="undefined" && !window.opera)
{
  Event.prototype.__defineSetter__("returnValue", function(b){
    if (!b) this.preventDefault();
    return b;
  });
  Event.prototype.__defineSetter__("cancelBubble", function(b){
    if (b) this.stopPropagation();
    return b;
  });
  Event.prototype.__defineGetter__("offsetX", function(){return this.layerX;});
  Event.prototype.__defineGetter__("offsetY", function(){return this.layerY;});
  Event.prototype.__defineGetter__("srcElement", function(){
    var node = this.target;
    while (node.nodeType != 1){node = node.parentNode; if(node==null) return null;}
    return node;
  });
}*/

/*-=< Function >=-*/
//apply and call
if(typeof(Function.prototype.apply)!="function")
{
  Function.prototype.apply = function(obj, argu)
  {
    if(obj) obj.constructor.prototype.___caller = this;
    for(var a=[], i=0; i<argu.length; i++) a[i] = "argu["+ i +"]";
    var t = eval((obj ? "obj.___caller" : "this") +"("+ a.join(",") +");");
    if(obj) delete obj.constructor.prototype.___caller; return t;};
    Function.prototype.call = function(obj){
    for(var a=[], i=1; i<arguments.length; i++) a[i-1]=arguments[i];
    return this.apply(obj, a);
  };
}

/*-=< Array >=-*/
//[extended method] push  insert new item
if(typeof(Array.prototype.push)!="function")
{
  Array.prototype.push = function()
  {
    for (var i=0; i<arguments.length; i++)
      this[this.length] = arguments[i];
    return this.length;
  };
}
//[extended method] shift  delete the first item
if(typeof(Array.prototype.shift)!="function")
{
  Array.prototype.shift = function()
  {
    var mm = null;
    if(this.length>0)
    {
      mm = this[0]; for(var i=1; i<this.length; i++)
      this[i-1]=this[i]; this.length=this.length -1;
    }
    return mm;
  };
}
//[extended method] unique  Delete repeated item
Array.prototype.Unique = function()
{
  var a = {}; for(var i=0; i<this.length; i++)
  {
    if(typeof a[this[i]] == "undefined")
      a[this[i]] = 1;
  }
  this.length = 0;
  for(var i in a)
    this[this.length] = i;
  return this;
};
//[extended method] indexOf
if(typeof(Array.prototype.indexOf)!="function")
{
  Array.prototype.indexOf=function(item, start)
  {
    start=start||0; if(start<0)start=Math.max(0,this.length+start);
    for(var i=start;i<this.length;i++){if(this[i]===item)return i;}
    return -1;
  };
}

/*-=< Date >=-*/
//datetime format
Date.prototype.format = function(format)
{
  var o = {
    "M+" : this.getMonth()+1, //month
    "d+" : this.getDate(),    //day
    "h+" : this.getHours(),   //hour
    "m+" : this.getMinutes(), //minute
    "s+" : this.getSeconds(), //second
    "q+" : Math.floor((this.getMonth()+3)/3),  //quarter
    "S" : this.getMilliseconds() //millisecond
  }
  if(/(y+)/.test(format)) format=format.replace(RegExp.$1,
    (this.getFullYear()+"").substr(4 - RegExp.$1.length));
  for(var k in o)if(new RegExp("("+ k +")").test(format))
    format = format.replace(RegExp.$1,
      RegExp.$1.length==1 ? o[k] :
        ("00"+ o[k]).substr((""+ o[k]).length));
  return format;
};

if(typeof(Number.prototype.toFixed)!="function")
{
    Number.prototype.toFixed = function(d)
    {
        var s=this+"";if(!d)d=0;
        if(s.indexOf(".")==-1)s+=".";s+=new Array(d+1).join("0");
        if (new RegExp("^(-|\\+)?(\\d+(\\.\\d{0,"+ (d+1) +"})?)\\d*$").test(s))
        {
            var s="0"+ RegExp.$2, pm=RegExp.$1, a=RegExp.$3.length, b=true;
            if (a==d+2){a=s.match(/\d/g); if (parseInt(a[a.length-1])>4)
            {
                for(var i=a.length-2; i>=0; i--) {a[i] = parseInt(a[i])+1;
                if(a[i]==10){a[i]=0; b=i!=1;} else break;}
            }
            s=a.join("").replace(new RegExp("(\\d+)(\\d{"+d+"})\\d$"),"$1.$2");
        }if(b)s=s.substr(1);return (pm+s).replace(/\.$/, "");} return this+"";
    };
}

/*-=< String >=-*/
String.prototype.trim = function()
{
  return this.replace(/(^[\s\t��]+)|([��\s\t]+$)/g, "");
};
String.prototype.capitalize = function()
{
  return this.charAt(0).toUpperCase() + this.substr(1);
};
String.prototype.getAttribute = function(attribute)
{
  if(new RegExp("(^|;)\\s*"+attribute+"\\s*:\\s*([^;]*)\\s*(;|$)","i").test(this))
  return RegExp.$2.replace(/%3B/gi,";").replace(/%25/g,"%"); return null;
};
String.prototype.setAttribute = function(attribute, value)
{
  value=(""+value).replace(/%/g,"%25").replace(/;/g,"%3B").replace(/\r|\n/g,"");
  return (attribute +":"+ value +";" + this);
};
String.prototype.deleteAttribute = function(attribute)
{
  return this.replace(new RegExp("\\b\\s*"+attribute+"\\s*:\\s*([^;]*)\\s*(;|$)","gi"),"");
};
String.prototype.getQueryString = function(name)
{
  var reg = new RegExp("(^|&|\\?)"+ name +"=([^&]*)(&|$)"), r;
  if (r=this.match(reg)) return unescape(r[2]); return null;
};
String.prototype.sub = function(n)
{
  var r = /[^\x00-\xff]/g;
  if(this.replace(r, "mm").length <= n) return this;
  n = n - 3;
  var m = Math.floor(n/2);
  for(var i=m; i<this.length; i++)
  {
    if(this.substr(0, i).replace(r, "mm").length>=n)
    {
      return this.substr(0, i) +"...";
    }
  }
  return this;
};


/**---------------------------------------------------------------------------*\
|  Subject:    document.cookie
|  NameSpace:  System.Net.MzCookie
|  Author:     meizz
|  Created:    2004-12-07
|  Version:    2006-04-03
|-------------------------------------------------------------
|  MSN: huangfr@msn.com   QQ: 112889082   http://www.meizz.com
|  Email: mz@126.com      CSDN ID:meizz   Copyright (c)  meizz
\*---------------------------------------------------------------------------*/

function MzCookie()
{
  var now = new Date();
  now.setTime(now.getTime() + 1000*60*60*24); //Save 1 days
  this.path = "/";
  this.expires = now;
  this.domain = "";
  this.secure = "";
}
MzCookie.Extends(System, "MzCookie");

//name: cookie name
//value: cookie value
MzCookie.prototype.add  = function(name, value)
{
  document.cookie =
    name + "="+ escape (value) 
    + ";expires=" + this.expires.toGMTString()
    + ";path="+ this.path
    + (this.domain == "" ? "" : ("; domain=" + this.domain))
    + (this.secure ? "; secure" : "");
};

//name: cookie name
MzCookie.prototype.get  = function(name)
{
  var arr,reg=new RegExp("(^| )"+name+"=([^;]*)(;|$)");
  if(arr=document.cookie.match(reg)) return unescape(arr[2]);
  else return null;
};

//name: cookie name
MzCookie.prototype.remove  = function(name)
{
  var now = new Date();
  now.setTime(now.getTime() - 1);
  var V = this.get(name);
  if(V!=null) document.cookie= name + "="+ V 
    +";expires="+ now.toGMTString() + ";path="+ this.path;
};

MzCookie.prototype.setExpires = function(milliseconds)
{
  var now = new Date();
  now.setTime(now.getTime() + milliseconds);
  this.expires = now;
};
