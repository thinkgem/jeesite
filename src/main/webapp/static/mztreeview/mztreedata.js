/**---------------------------------------------------------------------------*\
|  Subject:    Xml String Data
|  NameSpace:  System.Data.MzData
|  Author:     meizz
|  Created:    2005-12-28
|  Version:    2006-06-26
|-------------------------------------------------------------
|  MSN: huangfr@msn.com   QQ: 112889082   http://www.meizz.com
|  Email: mz@126.com      CSDN ID:meizz   Copyright (c)  meizz
\*---------------------------------------------------------------------------*/

function MzData(){System.call(this);}
t=MzData.Extends(System, "MzData");
MzData.addProperty("__", "\x0f", Function.READ);
t.rootId="-1"; t.dividerEncoding = t.divider = "_";
t.indexes = t.jsDataPath = t.xmlDataPath = "";

t.appendIndexes=function(s){this.indexes += this.get__()+ s +this.get__();}
t.getUniqueId=function(){return "MzData"+(MzData.nodeCounter++).toString(36);};
MzData.nodeCounter=0;

window.MzDataNode = function()
{
  this.index= (MzData.nodeCounter++).toString(36);
  this.childNodes=[];
};
t.nodePrototype=MzDataNode;
t=MzDataNode.Extends(System, "MzDataNode");
t.text = t.path = t.sourceIndex="";
t.isLoaded = t.hasChild= false;
t.parentNode = t.$$caller = null;  //instance of System.Data.MzData

//public
MzData.prototype.setDivider=function(d)
{
  this.divider=d; for(var a="", i=0; i<d.length; i++)
  a+=("\'^{[(\\-|+.*?)]}$\"".indexOf(d.charAt(i))>-1?"\\":"")+d.charAt(i);
  this.dividerEncoding = a;
};
MzData.prototype.setJsDataPath=function(path)
{
  if(path.length>0) this.jsDataPath = path.replace(/[\\\/]*$/, "/");
}
MzData.prototype.setXmlDataPath=function(path)
{
  if(path.length>0) this.xmlDataPath = path.replace(/[\\\/]*$/, "/");
}
//private: initialize data node
MzData.prototype.initialize = function()
{
  this.selectedNode=null; this.currentNode=null;
  if("object"!=typeof this.nodes) this.nodes={};
  if("object"!=typeof this.dataSource) this.dataSource={};

  var _=this.get__(), d=this.dividerEncoding, a=[], i;

  for(i in this.dataSource)a[a.length]=i;this.appendIndexes(a.join(_));
  this.dataSource.length=(this.dataSource.length||0)+ a.length;

  a=(MzData.nodeCounter++).toString(36);
  var node=this.nodes[a]=this.rootNode = new this.nodePrototype; //this.imaginaryNode
  node.$$caller=this;node.index=a;node.virgin=this.rootId=="-1";

  if(node.virgin)
  {
    node.id=node.path="-1";
    node.loadChildNodes();
    node.hasChildNodes();
  }
  else
  {
    a=new RegExp("([^"+_+d+"]+)"+ d +"("+ this.rootId +")("+_+"|$)");
    if(a.test(this.indexes))
    {
      a=RegExp.$1 + this.divider + this.rootId;
      node.childNodes[0]=node.DTO(this.nodePrototype, a);
      node.isLoaded=true; node.hasChild=true;
    }
  }
  this._initialized=true;
};
//public: append data onafterload
MzData.prototype.appendData = function(data)
{
  if("object"!=typeof this.dataSource) this.dataSource={}; var a=[],id;
  for(id in data) if(!this.dataSource[id])
  {this.dataSource[id]=data[id];a[a.length]=id;}
  if(this._initialized) this.appendIndexes(a.join(this.get__()));
  this.dataSource.length=(this.dataSource.length||0)+a.length;data=null;a=null;
};
//public: getNode (has Builded) by sourceId
MzData.prototype.getNodeById = function(id)
{
  if(id==this.rootId&&this.rootNode.virgin) return this.rootNode;
  var _=this.get__(), d = this.dividerEncoding;
  var reg=new RegExp("([^"+_+ d +"]+"+ d + id +")("+_+"|$)");
  if(reg.test(this.indexes)){var s=RegExp.$1;
  if(s=this.dataSource[s].getAttribute("index_"+ this.hashCode))
    return this.nodes[s];
  else{/*System._alert("The node isn't initialized!");*/ return null;}}
  /*alert("sourceId="+ id +" is nonexistent!");*/ return null;
};
//public: asynchronous get childNodes from JS File
MzData.prototype.loadJsData = function(JsFileUrl)
{
  var js; if(js = System.load("",JsFileUrl)){try{var d=eval(js);
  if("object"!=d && "object"==typeof(data) && null!=data)d=data;
  this.appendData(d); data=d=null;}catch(e){}}
};
//public: asynchronous get childNodes from XML File
MzData.prototype.loadXmlData = function(url, parentId)
{
  if(System.supportsXmlHttp())
  {
   // Us  ing("System.Xml.MzXmlDocument");
    function Sleep(n){var start=new Date().getTime();
    while(true) if(new Date().getTime()-start>n) break;}
    if("undefined"==typeof parentId) parentId=this.rootId;
    var x=new MzXmlDocument(); x.async=false; x.load(url);
    if(x.readyState==4)
    {
      if(!x.documentElement)
        alert("xmlDoc.documentElement = null, Please update your browser");
      this._loadXmlNodeData(x.documentElement, parentId);
    }
  }
};
//public: asynchronous get childNodes from XML String
MzData.prototype.loadXmlDataString = function(xmlString, parentId)
{
  if(System.supportsXmlHttp())
  {
   // Usi  ng("System.Xml.MzXmlDocument");
    if("undefined"==typeof parentId) parentId=this.rootId;
    var x=new MzXmlDocument(); x.loadXML(xmlString);
    this._loadXmlNodeData(x.documentElement, parentId);
  }
};
MzData.prototype._loadXmlNodeData = function(xmlNode, parentId)
{
  if(!(xmlNode && xmlNode.hasChildNodes())) return;
  for(var k,id,i=0,data={},n=xmlNode.childNodes; i<n.length; i++)
  {
    if(n[i].nodeType==1){id=n[i].getAttribute("id")||this.getUniqueId();
    if(n[i].hasChildNodes()){for(k=0,nic=n[i].childNodes;k<nic.length;k++)
    {
      if(nic[k].nodeType==1){this._loadXmlNodeData(n[i], id);break;}}
    }
    for(var k=0,s="",a=n[i].attributes; k<a.length; k++)
      s=s.setAttribute(a[k].name, a[k].value);
    if(!s.getAttribute("text")) s="text:;"+ s;
    a=parentId + this.divider + id; data[a]=s;}
  }
  this.appendData(data);
};

//public
MzData.prototype.loadUlData=function(HtmlUL, parentId)
{
  if("undefined"==typeof parentId) parentId=this.rootId; var ul;
  if("string"==typeof HtmlUL&&(ul=document.getElementById(HtmlUL)));
  else if("object"==typeof HtmlUL&&(ul=HtmlUL.tagName)&&
    "UL OL".indexOf(ul.toUpperCase())>-1) ul=HtmlUL;
  if("object"==typeof ul)
  {
    var data={}; for(var i=0, n=ul.childNodes; i<n.length; i++)
    {
      if(n[i].nodeType==1 && n[i].tagName=="LI")
      {
        var id=n[i].getAttribute("sourceid")||this.getUniqueId(),txt="",link="";
        for(var k=0; k<n[i].childNodes.length; k++)
        {
          var node=n[i].childNodes[k];
          if(node.nodeType==3) txt += node.nodeValue;
          if(node.nodeType==1)
          {
            switch(node.tagName)
            {
              case "UL":
              case "OL": this.loadUlData(node, id); break;
              case "A" : if(!link) link=node; break;
            }
          }
        }
        var str="";
        if(link)
        {
          str=str.setAttribute("target", link.target);
          str=str.setAttribute("url", link.href);
          str=str.setAttribute("text", link.innerHTML);
        }
        else str = str.setAttribute("text", txt.trim());
        var a=n[i].attributes;
        for(var k=0; k<a.length; k++)
        {
          if(a[k].specified && a[k].name!="style")
            str = str.setAttribute(a[k].name, a[k].value);
        }
        a=parentId + this.divider + id;
        data[a]=str;
      }
    }
    this.appendData(data);
  }
}
//public: check node has child
MzDataNode.prototype.hasChildNodes = function()
{
  var $=this.$$caller;
  this.hasChild=$.indexes.indexOf($.get__()+ this.id + $.divider)>-1
  ||(this.sourceIndex&&(this.get("JSData")!=null||this.get("XMLData")!=null
  || this.get("ULData")!=null)); return this.hasChild;
};
//public: get node attribute
MzDataNode.prototype.get = function(attribName)
{
  if("undefined"!=typeof this[attribName]) return this[attribName];
  else return this.$$caller.dataSource[this.sourceIndex].getAttribute(attribName);
};
//public: set node attribute
MzDataNode.prototype.set = function(attribName, value)
{
  if(typeof(this[attribName])!="undefined") this[attribName]=value; else
  {
    var s=this.$$caller.dataSource[this.sourceIndex];
    this.$$caller.dataSource[this.sourceIndex] = s.setAttribute(attribName,value);
  }
};
//private: load all node's node and init
MzDataNode.prototype.loadChildNodes = function(DataNodeClass)
{
  var $=this.$$caller,r=$.dividerEncoding,_=$.get__(), i, cs;
  var tcn=this.childNodes;tcn.length=0;if(this.sourceIndex){
  if((i=this.get("JSData"))) $.loadJsData((/^\w+\.js(\s|\?|$)/i.test(i)?$.jsDataPath:"")+i);
  if((i=this.get("ULData"))) $.loadUlData(i, this.id);
  if((i=this.get("XMLData")))$.loadXmlData((/^\w+\.xml(\s|\?|$)/i.test(i)?$.xmlDataPath:"")+i,this.id);}
  var reg=new RegExp(_ + this.id + r +"[^"+ _ + r +"]+", "g"); 
  if((cs=$.indexes.match(reg))){for(i=0;i<cs.length;i++){
    tcn[tcn.length]=this.DTO(DataNodeClass, cs[i].substr(_.length));}}
  this.isLoaded = true;
};
MzDataNode.prototype.DTO=function(DataNodeClass, sourceIndex)
{
  var C=DataNodeClass||MzDataNode,$=this.$$caller,d=$.divider,n=new C,s;
  n.$$caller=this.$$caller; s=$.dataSource[n.sourceIndex=sourceIndex];
  n.id=sourceIndex.substr(sourceIndex.indexOf(d)+d.length);
  n.hasChildNodes();n.text=s.getAttribute("text");
  n.parentNode=this;$.nodes[n.index]=n;n.path=this.path+d+n.id;
  $.dataSource[sourceIndex]=s.setAttribute("index_"+ $.hashCode,n.index);
  return n;
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

/**---------------------------------------------------------------------------*\
|  Subject:    Plugins for Opera  XMLHttpRequest
|  NameSpace:  System.Plugins.Opera
|  Author:     meizz
|  Created:    2006-03-24
|  Version:    2006-03-27
|-------------------------------------------------------------
|  MSN: huangfr@msn.com   QQ: 112889082   http://www.meizz.com
|  Email: mz@126.com      CSDN ID:meizz   Copyright (c)  meizz
\*---------------------------------------------------------------------------*/

if (window.opera && !window.XMLHttpRequest) {
  window.XMLHttpRequest = function() {
    this.readyState = 0; // 0=uninitialized,1=loading,2=loaded,3=interactive,4=complete
    this.status = 0; // HTTP status codes
    this.statusText = '';
    this._headers = [];
    this._aborted = false;
    this._async = true;
    this._defaultCharset = 'ISO-8859-1';
    this._getCharset = function() {
      var charset = _defaultCharset;
      var contentType = this.getResponseHeader('Content-type').toUpperCase();
      val = contentType.indexOf('CHARSET=');
      if (val != -1) {
        charset = contentType.substring(val);
      }
      val = charset.indexOf(';');
      if (val != -1) {
        charset = charset.substring(0, val);
      }
      val = charset.indexOf(',');
      if (val != -1) {
        charset = charset.substring(0, val);
      }
      return charset;
    };
    this.abort = function() {
      this._aborted = true;
    };
    this.getAllResponseHeaders = function() {
      return this.getAllResponseHeader('*');
    };
    this.getAllResponseHeader = function(header) {
      var ret = '';
      for (var i = 0; i < this._headers.length; i++) {
        if (header == '*' || this._headers[i].h == header) {
          ret += this._headers[i].h + ': ' + this._headers[i].v + '\n';
        }
      }
      return ret;
    };
    this.getResponseHeader = function(header) {
      var ret = getAllResponseHeader(header);
      var i = ret.indexOf('\n');
      if (i != -1) {
        ret = ret.substring(0, i);
      }
      return ret;
    };
    this.setRequestHeader = function(header, value) {
      this._headers[this._headers.length] = {h:header, v:value};
    };
    this.open = function(method, url, async, user, password) {
      this.method = method;
      this.url = url;
      this._async = true;
      this._aborted = false;
      this._headers = [];
      if (arguments.length >= 3) {
        this._async = async;
      }
      if (arguments.length > 3) {
        opera.postError('XMLHttpRequest.open() - user/password not supported');
      }
      this.readyState = 1;
      if (this.onreadystatechange) {
        this.onreadystatechange();
      }
    };
    this.send = function(data) {
      if (!navigator.javaEnabled()) {
        alert("XMLHttpRequest.send() - Java must be installed and enabled.");
        return;
      }
      if (this._async) {
        setTimeout(this._sendasync, 0, this, data);
        // this is not really asynchronous and won't execute until the current
        // execution context ends
      } else {
        this._sendsync(data);
      }
    }
    this._sendasync = function(req, data) {
      if (!req._aborted) {
        req._sendsync(data);
      }
    };
    this._sendsync = function(data) {
      this.readyState = 2;
      if (this.onreadystatechange) {
        this.onreadystatechange();
      }
      // open connection
      var url = new java.net.URL(new java.net.URL(window.location.href), this.url);
      var conn = url.openConnection();
      for (var i = 0; i < this._headers.length; i++) {
        conn.setRequestProperty(this._headers[i].h, this._headers[i].v);
      }
      this._headers = [];
      if (this.method == 'POST') {
        // POST data
        conn.setDoOutput(true);
        var wr = new java.io.OutputStreamWriter(conn.getOutputStream(), this._getCharset());
        wr.write(data);
        wr.flush();
        wr.close();
      }
      // read response headers
      // NOTE: the getHeaderField() methods always return nulls for me :(
      var gotContentEncoding = false;
      var gotContentLength = false;
      var gotContentType = false;
      var gotDate = false;
      var gotExpiration = false;
      var gotLastModified = false;
      for (var i = 0; ; i++) {
        var hdrName = conn.getHeaderFieldKey(i);
        var hdrValue = conn.getHeaderField(i);
        if (hdrName == null && hdrValue == null) {
          break;
        }
        if (hdrName != null) {
          this._headers[this._headers.length] = {h:hdrName, v:hdrValue};
          switch (hdrName.toLowerCase()) {
            case 'content-encoding': gotContentEncoding = true; break;
            case 'content-length'  : gotContentLength   = true; break;
            case 'content-type'    : gotContentType     = true; break;
            case 'date'            : gotDate            = true; break;
            case 'expires'         : gotExpiration      = true; break;
            case 'last-modified'   : gotLastModified    = true; break;
          }
        }
      }
      // try to fill in any missing header information
      var val;
      val = conn.getContentEncoding();
      if (val != null && !gotContentEncoding) this._headers[this._headers.length] = {h:'Content-encoding', v:val};
      val = conn.getContentLength();
      if (val != -1 && !gotContentLength) this._headers[this._headers.length] = {h:'Content-length', v:val};
      val = conn.getContentType();
      if (val != null && !gotContentType) this._headers[this._headers.length] = {h:'Content-type', v:val};
      val = conn.getDate();
      if (val != 0 && !gotDate) this._headers[this._headers.length] = {h:'Date', v:(new Date(val)).toUTCString()};
      val = conn.getExpiration();
      if (val != 0 && !gotExpiration) this._headers[this._headers.length] = {h:'Expires', v:(new Date(val)).toUTCString()};
      val = conn.getLastModified();
      if (val != 0 && !gotLastModified) this._headers[this._headers.length] = {h:'Last-modified', v:(new Date(val)).toUTCString()};
      // read response data
      var reqdata = '';
      var stream = conn.getInputStream();
      if (stream) {
        var reader = new java.io.BufferedReader(new java.io.InputStreamReader(stream, this._getCharset()));
        var line;
        while ((line = reader.readLine()) != null) {
          if (this.readyState == 2) {
            this.readyState = 3;
            if (this.onreadystatechange) {
              this.onreadystatechange();
            }
          }
          reqdata += line + '\n';
        }
        reader.close();
        this.status = 200;
        this.statusText = 'OK';
        this.responseText = reqdata;
        this.readyState = 4;
        if (this.onreadystatechange) {
          this.onreadystatechange();
        }
        if (this.onload) {
          this.onload();
        }
      } else {
        // error
        this.status = 404;
        this.statusText = 'Not Found';
        this.responseText = '';
        this.readyState = 4;
        if (this.onreadystatechange) {
          this.onreadystatechange();
        }
        if (this.onerror) {
          this.onerror();
        }
      }
    };
  };
  try
  {
  	System.xmlHttp = new XMLHttpRequest();
  }
  catch(ex){}
}

/**---------------------------------------------------------------------------*\
|  Subject:    Mz XmlDocument
|  NameSpace:  System.Xml.MzXmlDocument
|  Author:     meizz
|  Created:    2006-01-23
|  Version:    2006-04-26
|-------------------------------------------------------------
|  MSN: huangfr@msn.com   QQ: 112889082   http://www.meizz.com
|  Email: mz@126.com      CSDN ID:meizz   Copyright (c)  meizz
\*---------------------------------------------------------------------------*/

function MzXmlDocument()
{
  if(document.implementation&&document.implementation.createDocument)
  {
    var doc=document.implementation.createDocument("","",null);
    doc.addEventListener("load",function(e){this.readyState=4;},false);
    doc.readyState=4; return doc;
  }
  else
  {
    var msxmls=["MSXML2","Microsoft","MSXML","MSXML3"];
    for(var i=0;i<msxmls.length;i++)
      try{return new ActiveXObject(msxmls[i]+'.DomDocument')}catch(e){}
    throw new Error("Could not find an installed XML parser!");
  }
}
MzXmlDocument.Extends(System, "MzXmlDocument");

var IE7 = false;  //repair for IE7 2006-04-26
if(/MSIE (\d+(\.\d+)?)/.test(navigator.userAgent))
{
  IE7 = parseFloat(RegExp.$1)>=7;
}

if(System.supportsXmlHttp() && "undefined"!=typeof XMLDocument && !IE7)
{
    (function()
    {
        var _xmlDocPrototype=XMLDocument.prototype;
        _xmlDocPrototype.__proto__={__proto__:_xmlDocPrototype.__proto__};
        var _p=_xmlDocPrototype.__proto__;
        _p.createNode=function(aType,aName,aNamespace)
        {
            switch(aType)
            {
                case 1:
                    if(aNamespace&&aNamespace!="")
                        return this.createElementNS(aNamespace,aName);
                    else return this.createElement(aName);
                case 2:
                    if(aNamespace&&aNamespace!="")
                        return this.createAttributeNS(aNamespace,aName);
                    else return this.createAttribute(aName);
                case 3:
                    default:return this.createTextNode("");
            }
        };
        _p.__realLoad=_xmlDocPrototype.load;
        _p.load=function(sUri)
        {
            this.readyState=0;
            this.__realLoad(sUri);
        };
        _p.loadXML=function(s)
        {
            var doc2=(new DOMParser).parseFromString(s,"text/xml");
            while(this.hasChildNodes())
                this.removeChild(this.lastChild);
            var cs=doc2.childNodes;
            var l=cs.length;
            for(var i=0;i<l;i++)
                this.appendChild(this.importNode(cs[i],true));
        };
        _p.setProperty=function(sName,sValue)
        {
            if(sName=="SelectionNamespaces")
            {
                this._selectionNamespaces={};
                var parts=sValue.split(/\s+/);
                var re= /^xmlns\:([^=]+)\=((\"([^\"]*)\")|(\'([^\']*)\'))$/;
                for(var i=0;i<parts.length;i++){
                    re.test(parts[i]);
                    this._selectionNamespaces[RegExp.$1]=RegExp.$4||RegExp.$6;
                }
            }
        };
        _p.__defineSetter__("onreadystatechange",function(f){
            if(this._onreadystatechange)
                this.removeEventListener("load",this._onreadystatechange,false);
            this._onreadystatechange=f;
            if(f)
                this.addEventListener("load",f,false);return f;
        });
        _p.__defineGetter__("onreadystatechange",function(){
            return this._onreadystatechange;
        });
        MzXmlDocument._mozHasParseError=function(oDoc){
            return!oDoc.documentElement||oDoc.documentElement.localName=="parsererror"&&oDoc.documentElement.getAttribute("xmlns")=="http://www.mozilla.org/newlayout/xml/parsererror.xml";
        };
        _p.__defineGetter__("parseError",function(){
            var hasError=MzXmlDocument._mozHasParseError(this);
            var res={errorCode:0,filepos:0,line:0,linepos:0,reason:"",srcText:"",url:""};
            if(hasError){
                res.errorCode= -1;
                try{
                    res.srcText=this.getElementsByTagName("sourcetext")[0].firstChild.data;
                    res.srcText=res.srcText.replace(/\n\-\^$/,"");
                }
                catch(ex){
                    res.srcText="";
                }
                try{
                    var s=this.documentElement.firstChild.data;
                    var re= /XML Parsing Error\:(.+)\nLocation\:(.+)\nLine Number(\d+)\,Column(\d+)/;
                    var a=re.exec(s);res.reason=a[1];res.url=a[2];res.line=a[3];res.linepos=a[4];
                }
                catch(ex){
                    res.reason="Unknown";
                }
            }
            return res;
        });
        var _nodePrototype=Node.prototype;
        _nodePrototype.__proto__={__proto__:_nodePrototype.__proto__};
        _p=_nodePrototype.__proto__;
        _p.__defineGetter__("xml",function(){
            return(new XMLSerializer).serializeToString(this);
        });
        _p.__defineGetter__("baseName",function(){
            var lParts=this.nodeName.split(":");
            return lParts[lParts.length-1];
        });
        _p.__defineGetter__("text",function(){
            var cs=this.childNodes;
            var l=cs.length;
            var sb=new Array(l);
            for(var i=0;i<l;i++)
                sb[i]=cs[i].text;
            return sb.join("");
        });
        _p.selectNodes=function(sExpr){
            var doc=this.nodeType==9?this:this.ownerDocument;
            var nsRes=doc.createNSResolver(this.nodeType==9?this.documentElement:this);
            var nsRes2;
            if(doc._selectionNamespaces){
                nsRes2=function(s){
                    if(doc._selectionNamespaces[s])
                        return doc._selectionNamespaces[s];
                    return nsRes.lookupNamespaceURI(s);
                };
            }
            else nsRes2=nsRes;
            var xpRes=doc.evaluate(sExpr,this,nsRes2,5,null);
            var res=[];
            var item;
            while((item=xpRes.iterateNext()))
                res.push(item);
            return res;
        };
        _p.selectSingleNode=function(sExpr){
            var doc=this.nodeType==9?this:this.ownerDocument;
            var nsRes=doc.createNSResolver(this.nodeType==9?this.documentElement:this);
            var nsRes2;
            if(doc._selectionNamespaces){
                nsRes2=function(s){
                    if(doc._selectionNamespaces[s])
                        return doc._selectionNamespaces[s];
                    return nsRes.lookupNamespaceURI(s);
                };
            }
            else nsRes2=nsRes;
            var xpRes=doc.evaluate(sExpr,this,nsRes2,9,null);
            return xpRes.singleNodeValue;
        };
        _p.transformNode=function(oXsltNode){
            var doc=this.nodeType==9?this:this.ownerDocument;
            var processor=new XSLTProcessor();
            processor.importStylesheet(oXsltNode);
            var df=processor.transformToFragment(this,doc);
            return df.xml;
        };
        _p.transformNodeToObject=function(oXsltNode,oOutputDocument){
            var doc=this.nodeType==9?this:this.ownerDocument;
            var outDoc=oOutputDocument.nodeType==9?oOutputDocument:oOutputDocument.ownerDocument;
            var processor=new XSLTProcessor();processor.importStylesheet(oXsltNode);
            var df=processor.transformToFragment(this,doc);
            while(oOutputDocument.hasChildNodes())
                oOutputDocument.removeChild(oOutputDocument.lastChild);
            var cs=df.childNodes;
            var l=cs.length;
            for(var i=0;i<l;i++)
                oOutputDocument.appendChild(outDoc.importNode(cs[i],true));
        };
        var _attrPrototype=Attr.prototype;
        _attrPrototype.__proto__={__proto__:_attrPrototype.__proto__};
        _p=_attrPrototype.__proto__;
        _p.__defineGetter__("xml",function(){
            var nv=(new XMLSerializer).serializeToString(this);
            return this.nodeName+"=\""+nv.replace(/\"/g,"&quot;")+"\"";
        });
        var _textPrototype=Text.prototype;
        _textPrototype.__proto__={__proto__:_textPrototype.__proto__};
        _p=_textPrototype.__proto__;
        _p.__defineGetter__("text",function(){
            return this.nodeValue;
        });
    })();
}