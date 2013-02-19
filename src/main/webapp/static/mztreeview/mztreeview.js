/**---------------------------------------------------------------------------*\
|  Subject:    MzTreeView 2.0
|  NameSpace:  System.Web.UI.WebControls.MzTreeView
|  Author:     meizz
|  Created:    2005-12-28
|  Updated:    2009-10-26
|  Version:    2006-05-03
|-------------------------------------------------------------
|  MSN: huangfr@msn.com   QQ: 112889082   http://www.meizz.com
|  Email: mz@126.com      CSDN ID:meizz   Copyright (c)  meizz
\*---------------------------------------------------------------------------*/
//Usi ng("System.Data.MzData");
//Usi ng("System.Web.Forms.MzEffect");

function MzTreeView(data){MzData.call(this);this.index = this.hashCode;this.dataSource = data;}
t=MzTreeView.Extends(MzData, "MzTreeView");

MzTreeView.addProperty("defaultUrl", "javascript:");
MzTreeView.addProperty("defaultTarget", "_self");
t.iconPath = System.path+"/images/";

//[configuration]
t.showPlus  = true;

t.showRoot = true;
t.showLines = true;
t.showToolTip = true;
t.showNodeIcon = true;

t.autoSort = true;
t.useArrow = true;
t.dynamic  = false;
t.useCheckbox = false;//是否使用复选框
t.isParentCheckbox = false;//选中当前节点时是否选中父节点
t.autoFocused = false;//刷新页面是否自动获得上次焦点
t.useContextMenu = false;
t.convertRootIcon = true;
t.canOperate = false;

t.linkFocus = true;//单击链接是否获得焦点
t.linkCheckbox = true;//单击链接是否选中复选框

MzTreeView.icons = 
{
  line :
  {
    "l0" :"_line0.gif",
    "l1" :"_line1.gif",
    "l2" :"_line2.gif",
    "l3" :"_line3.gif",
    "l4" :"_line4.gif",
    "ll" :"_line5.gif",
    "c0" :"_checkbox0.gif",
    "c1" :"_checkbox1.gif",
    "c2" :"_checkbox2.gif"
  },
  collapse:
  {
    "pm0":"_plus0.gif",
    "pm1":"_plus1.gif",
    "pm2":"_plus2.gif",
    "pm3":"_plus3.gif",

    "root":"root_1.gif",
    "file":"file_1.gif",
    "exit":"exit.gif",
    "folder":"folder_1.gif"
  },
  expand :
  {
    "pm0":"_minus0.gif",
    "pm1":"_minus1.gif",
    "pm2":"_minus2.gif",
    "pm3":"_minus3.gif",

    "folder":"folderopen_1.gif"
  }
};

MzTreeView.textLoading="&#27491;&#22312;&#21152;&#36733;...";

//if(t.autoFocused) {
//    Usi ng("System.Net.MzCookie");
//}

//public: entry
MzTreeView.prototype.render = function()
{
  function loadImg(C){for(var i in C){if("string"==typeof C[i]){
  var a=new Image();a.src=me.iconPath + C[i];C[i]=a;}}}var me=this;
  loadImg(MzTreeView.icons.expand);loadImg(MzTreeView.icons.collapse);
  loadImg(MzTreeView.icons.line);me.firstNode=null;
  //loadCssFile(this.iconPath +"mztreeview.css", "MzTreeView_CSS");

  this.initialize();var str="无数据", i, root=this.rootNode;
  if (root.hasChild){var a = [], c = root.childNodes;me.firstNode=c[0];
  for(i=0;i<c.length;i++)a[i]=c[i].render(i==c.length-1);str=a.join("");}
  setTimeout(function(){me.afterRender();}, 10);
  
  if(this.autoFocused){var path = new MzCookie().get("MzTreeViewSelectedNodePath")
  if(path&&path.length>0) this.focusNodeByPath(path);}

  return "<div class='mztreeview' id='MTV_root_"+ this.index +"' "+
    "onclick='Instance(\""+ this.index +"\").clickHandle(event)' "+
    "ondblclick='Instance(\""+ this.index +"\").dblClickHandle(event)' "+
    ">"+ str +"</div>";
};
MzTreeView.prototype.afterRender=function()
{
  var me = this;
  var root = document.getElementById("MTV_root_"+ me.index);
  if(root)
  {
    if(me.firstNode)(me.currentNode=me.firstNode).focus();
    this.dispatchEvent(new System.Event("onrender"));
    if(me.useArrow) me.attachEventArrow();
    if(!me.showRoot) try{root.childNodes[0].childNodes[0].style.display="none";}catch(e){}
  }
  else setTimeout(function(){me.afterRender();}, 100);
};
//private: attachEvent onTreeKeyDown
MzTreeView.prototype.attachEventArrow = function()
{
  var a = document.getElementById("MTV_root_"+ this.index);if(!a) return;
  var me= this;a.attachEvent("onkeydown", function(e)
  {
    e = window.event || e;var key = e.keyCode || e.which;
    switch(key)
    {
      case 37 :me.focusUpperNode();break;  //Arrow left,  shrink child node
      case 38 :me.focusPreviousSibling();break;  //Arrow up
      case 39 :me.focusLowerNode();break;  //Arrow right, expand child node
      case 40 :me.focusNextSibling();break;  //Arrow down
      case 46 :if(me.canOperate) me.currentNode.removeNode();break; //delete
    }
  });
};
MzTreeView.prototype.appendIcon=function(index, src, flag)
{
  if(/^pm\d$/.test(index))return;
  var a=new Image();a.src=src;
  if(flag){MzTreeView.icons.expand[index]=a;return;}
  MzTreeView.icons.collapse[index]=a;
}
MzTreeView.prototype.setIconPath=function(path)
{
  if(path.length>0) this.iconPath = path.replace(/[\\\/]*$/, "/");
}
//public: loadChildNodes(sourceId)
MzTreeView.prototype.loadChildNodes = function(id)
{
  var n;if(n=this.getNodeById(id)) n.loadChildNodes();
};

//private: build treeline onTreeNodeBuild
MzTreeView.prototype.buildPrefix = function(prefix)
{
  var a=prefix.replace(/^,+/,"").split(",");for(var i=0; i<a.length; i++)
  if (a[i]) a[i]="<img src='"+ MzTreeView.icons.line[a[i]].src +"' alt='' />";
  return this.showLines?(prefix ?a.join(""):""):(prefix ?"<div style='width:"+
    (MzTreeView.icons.collapse["pm3"].width*a.length)+"px'>&nbsp;<\/div>":"");
};
//public: expand/collapse node
MzTreeView.prototype.expand = function(id)
{
  var n;if(n=this.getNodeById(id)) n.expand();
};
MzTreeView.prototype.collapse = function(id)
{
  var n;if(n=this.getNodeById(id)) n.collapse();
};
//private: attachEvent tree onClick
MzTreeView.prototype.clickHandle = function(e)
{
  e = window.event || e;var B;
  e = e.srcElement || e.target;
  if(e.tagName=="A") e = e.parentNode.firstChild;
  if(B=(e.id && 0==e.id.indexOf(this.index +"_")))
  {
    var n=this.currentNode=this.nodes[e.id.substr(e.id.lastIndexOf("_")+1)];
  }
  switch(e.tagName)
  {
    case "IMG" :
      if(B)
      {
        if(e.id.indexOf(this.index +"_expand_")==0){
          n.expanded ? n.collapse() : n.expand();}
        else if(e.id.indexOf(this.index +"_icon_")==0){
          /*n.focus();*/}
        else if(e.id.indexOf(this.index +"_checkbox_")==0){
          n.check(!n.checked);n.upCheck();}
      }
      break;
    case "A":
      if(B){if(this.linkFocus){n.focus();}
          if(this.linkCheckbox){n.check(!n.checked);n.upCheck();}
          this.dispatchEvent(new System.Event("onclick"));}
      break;
    default :
      if(System.ns) e = e.parentNode;
      break;
  }
};
//private: onTreeDoubleClick
MzTreeView.prototype.dblClickHandle = function(e)
{
  e = window.event || e;e = e.srcElement || e.target;
  //if((e.tagName=="A" || e.tagName=="IMG") && e.id)
  if(e.id.indexOf(this.index +"_icon_")==0)
  {
    e=this.nodes[e.id.substr(e.id.lastIndexOf("_") +1)];
    e.expanded ? e.collapse() : e.expand();
    this.currentNode=e;this.dispatchEvent(new System.Event("ondblclick"));
  }
};
//public: focus a treeNode by node's clientIndex
MzTreeView.prototype.focus = function(id)
{
  if(!this.selectedNode) this.selectedNode=this.rootNode;
  var n;if(n=this.getNodeById(id)) n.focus();
};
MzTreeView.prototype.focusNodeByPath = function(path)
{
  if (path.substring(path.length-1,path.length)==this.divider)
	path = path.substring(0,path.length-1);
  var me=this, n;
  if ((n=path.indexOf(this.divider))>0)
  {
    var node = this.getNodeById(path.substring(0, n));
    if(node){if(node!=this.rootNode && !node.expanded) node.expand();
    node.focus();}else return;
    path = path.substr(n + this.divider.length);
    setTimeout(function(){me.focusNodeByPath(path)}, 1);
  }
  else {n=this.getNodeById(path);if(n){n.focus();n.expand();}};
};
//*
MzTreeView.prototype.focusUpperNode = function()
{
  var e = this.selectedNode, r = this.rootNode;if(e){
  if(e.parentNode==r && !e.expanded) return;
  if(e.expanded) e.collapse(); else e.parentNode.focus();}
};

MzTreeView.prototype.focusLowerNode = function()
{
  var e = this.selectedNode;if(e && e.hasChild){
  if(e.expanded)e.childNodes[0].focus();else e.expand();}
}

MzTreeView.prototype.focusPreviousSibling = function()
{
  var e = this.selectedNode;if(e && e.parentNode){
  var c = e.parentNode.childNodes;for(var i=c.length-1; i>=0; i--){
  if(c[i]==e){if(i==0) return;c[i-1].focus();}}}
};

MzTreeView.prototype.focusNextSibling = function()
{
  var e = this.selectedNode;if(e && e.parentNode){
  var c = e.parentNode.childNodes;for(var i=0; i<c.length; i++){
  if(c[i]==e){if(i==c.length-1) return;c[i+1].focus();}}}
};
MzTreeView.prototype.appendNode=function(sourceIndex, dataString)
{
  if(!this.canOperate) return;
  var a=sourceIndex.split(this.divider);if(a.length!=2) return;
  var d={},pid=a[0],id=a[1];d[sourceIndex]=dataString;this.appendData(d);
  var node=this.getNodeById(pid);if(!node) return;node.hasChild=true;
  if(!node.isLoaded){node.updateNode();node.expand();} else
  node.appendNode(node.DTO(this.nodePrototype, sourceIndex));
  this.currentNode=this.getNodeById(id);
  this.dispatchEvent(new System.Event("onappendnode"));
  if(this.useCheckbox && node.checked) node.check(true);
};

MzTreeView.prototype.updateNode = function(id)
{
  if(!this.canOperate) return;
  var node;if(node=this.getNodeById(id)) node.updateNode();
};

MzTreeView.prototype.removeNode = function(id)
{
  if(!this.canOperate) return;
  var node;if(node=this.getNodeById(id)) node.removeNode();
};

MzTreeView.prototype.getChecked = function()
{
	var s = '', nodes = this.nodes;
	for(var i in nodes) {
		if(nodes[i].checked){
			if (/^\d+$/.test(nodes[i].id))
				s += nodes[i].id + ',' ;
		}
	}
	return s;
};

MzTreeView.prototype.getCheckedText = function()
{
	var s = '', nodes = this.nodes;
	for(var i in nodes) {
		if(nodes[i].checked){
			if (/^\d+$/.test(nodes[i].id))
				s += nodes[i].text + ',' ;
		}
	}
	return s;
};

//************************ can expand a node all childNodes
MzTreeView.prototype.expandAll = function(id)
{
  if("undefined"==typeof id) return;
  var node=this.getNodeById(id);
  if(!node||node.hasChlid) return;
  node.expandAll();
};
MzTreeView.prototype.collapseAll = function(id)
{
  if("undefined"==typeof id) return;
  var node=this.getNodeById(id);
  if(!node||node.childNodes.length==0) return;
  node.collapseAll();
};
MzTreeView.prototype.expandLevel=function(level)
{
  if(!/\d+/.test(level) || level==0)return;var r;
  if((r=this.rootNode).hasChild)
  {
    for(var i=0, n=r.childNodes.length; i<n; i++)
      r.childNodes[i].expandLevel(level);
  }
};


window.MzTreeNode=function(){MzDataNode.call(this);};
t=MzTreeNode.Extends(MzDataNode, "MzTreeNode");
MzTreeView.prototype.nodePrototype=MzTreeNode;
MzTreeNode.htmlChildPrefix="";
t.checked = t.expanded = false;
t.childPrefix = "";

//private: load all node's node and init
MzTreeNode.prototype.loadChildNodes = function()
{
  MzDataNode.prototype.loadChildNodes.call(this, MzTreeNode);
  if(this.$$caller.useCheckbox)
  {
    var r=/^true$/i, data=this.$$caller.dataSource;
    for(var i=0, n=this.childNodes.length; i<n; i++)
    {
      var node=this.childNodes[i];
      var b=data[node.sourceIndex].getAttribute("checked");
      if(b!=null) node.checked=r.test(b);
      if(!this.$$caller.isParentCheckbox) node.checked=node.parentNode.checked || node.checked;
    }
    if(n>0) this.childNodes[0].upCheck();
  }
};

//private: single node build to HTML
MzTreeNode.prototype.render = function(last)
{
  var $=this.$$caller, s=$.dataSource[this.sourceIndex],target,data,url;
  var icon=s.getAttribute("icon");
  if(!(target=s.getAttribute("target")))target=$.getDefaultTarget();
  var hint=$.showToolTip ? s.getAttribute("hint") || this.text : "";
  if(!(url=s.getAttribute("url"))) url = $.getDefaultUrl();
  if(data=s.getAttribute("data"))url+=(url.indexOf("?")==-1?"?":"&")+data;

  var id=this.index, s="";
  var isRoot=this.parentNode==$.rootNode;
  if( isRoot && $.convertRootIcon && !icon) icon = "root";
  if(!isRoot)this.childPrefix=this.parentNode.childPrefix+(last?",ll":",l4");
  if(!icon || typeof(MzTreeView.icons.collapse[icon])=="undefined")
  this.icon = this.hasChild ? "folder" : "file"; else this.icon = icon;
  this.line = this.hasChild ? (last ? "pm2" : "pm1") : (last ? "l2" : "l1");
  if(!$.showLines) this.line = this.hasChild ? "pm3" : "ll";

  s += "<div><table border='0' cellpadding='0' cellspacing='0'>"+
       "<tr title='"+ hint +"'><td nowrap='true'>";if (!isRoot && MzTreeNode.htmlChildPrefix)
  s += MzTreeNode.htmlChildPrefix +"</td><td nowrap='true'>";if(!isRoot)
  s += "<img border='0' id='"+ $.index +"_expand_"+ id +"' src='"+
       (this.hasChild ? MzTreeView.icons.collapse[this.line].src : 
       MzTreeView.icons.line[this.line].src)+"'>";if($.showNodeIcon)
  s += "<img border='0' id='"+ $.index +"_icon_"+ id +"' src='"+ 
       MzTreeView.icons.collapse[this.icon].src +"'>";if($.useCheckbox)
  s += "<img border='0' id='"+$.index +"_checkbox_"+ id +"' src='"+ 
       MzTreeView.icons.line["c"+ (this.checked?1:0)].src +"'>";
  s += "</td><td style='padding-left: 3px' nowrap='true'><a href='"+ url +
       "' target='"+ target +"' id='"+$.index +"_link_"+ id +
       "' class='MzTreeView'>"+ this.text +"</a></td></tr></table><div ";
       if(isRoot&&this.text=="") s="<div><div ";
  s += "id='"+$.index+"_tree_"+id+"' style='display:none;'></div></div>";
  return s;
};
//private: build all node's node
MzTreeNode.prototype.buildChildNodes = function()
{
  var me = this, mtv = me.$$caller, box;
  if(box = document.getElementById(mtv.index +"_tree_"+ me.index))
  {
    var a = new Array(me.childNodes.length);
    MzTreeNode.htmlChildPrefix=mtv.buildPrefix(me.childPrefix);
    if(/(^|\s|;)(JS|XML|UL)Data\s*:/i.test(mtv.dataSource[me.sourceIndex])){
    function cond(a, b){if(a.hasChild!=b.hasChild)
      return (a.hasChild ? -1 : 1); else return(a.index>b.index?1:-1);}
    if(mtv.autoSort) me.childNodes=me.childNodes.sort(cond);}
    for(var i=0; i<a.length; i++)a[i]=me.childNodes[i].render(i==a.length-1);
    box.innerHTML=a.join("");if(box.innerHTML=="")box.style.display="none";
    a=null; if(!mtv.isParentCheckbox && me.checked) me.check(me.checked);
  }
};
//private: check checkbox
MzTreeNode.prototype.check = function(checked)
{
  var me=this, mtv=me.$$caller, B=checked?"true":"false", mc=me.childNodes;
  var chk;if(chk=document.getElementById(mtv.index+"_checkbox_"+ me.index)){
  chk.src=MzTreeView.icons.line["c"+((me.checked=(checked==true))?1:0)].src;}
  var x=mtv.index;for(var i=0, chk=mc.length; i<chk; i++)
  setTimeout("Instance('"+x+"').nodes['"+mc[i].index+"'].check("+B+")",1);
};
//private: set checkbox status on childNode has checked
MzTreeNode.prototype.upCheck = function()
{
  var node = this, mtv=node.$$caller, chk;if(node.parentNode){
  for(var i=0; i<node.parentNode.childNodes.length; i++)
  {
    if(node.parentNode.childNodes[i].checked != node.checked)
    {
      while(node=node.parentNode){ node.checked = mtv.isParentCheckbox;
        if (chk = document.getElementById(mtv.index+"_checkbox_"+node.index))
        chk.src = MzTreeView.icons.line["c2"].src;}return;}
    }
    node = node.parentNode;node.checked = this.checked;
    if (chk = document.getElementById(mtv.index +"_checkbox_"+ node.index))
    chk.src = MzTreeView.icons.line["c"+(node.checked?1:0)].src;node.upCheck();
  }
};
//private: expand node
MzTreeNode.prototype.expand = function()
{
  if(!this.hasChild) return;var me=this, $ = me.$$caller;
  var box = document.getElementById($.index +"_tree_"+ this.index);
  if (!box) {System._alert("error in getElementById");return;}

  this.expanded = box.style.display=="none";
  box.style.display = "block";if($.dynamic) MzEffect.show(box);
  var line = document.getElementById($.index+"_expand_"+ this.index);
  var icon = document.getElementById($.index+"_icon_"+ this.index);

  var ies=MzTreeView.icons.expand, ics=MzTreeView.icons.collapse;

  if(line && typeof(ies[this.line])=="object")line.src = ies[this.line].src;
  if(icon && typeof(ies[this.icon])=="object")icon.src = ies[this.icon].src;

  if(!this.isLoaded)
  {
    if(this.hasChild && (this.childNodes.length>200 
      || /(^|\s|;)(JS|XML|UL)Data\s*:/i.test($.dataSource[this.sourceIndex])))
    {
      setTimeout(function(){me.loadChildNodes();me.buildChildNodes();}, 1);
      box.innerHTML="<table border='0' cellspacing='0' cellpadding='0'><tr><td>"+
        $.buildPrefix(this.childPrefix) +"</td><td><img border='0' src='"+
        ics['pm2'].src +"'>"+ (!$.showNodeIcon ? "" : "<img border='0' src='"+
        ics['folder'].src +"'>") +"<a class='selected' href='javascript:'>"+
        MzTreeView.textLoading +"</a></td></tr></table>";
    }
    else{this.loadChildNodes();this.buildChildNodes()};
  }else{
  	if(box.innerHTML=="")box.style.display="none";
  }

  //if($.useCheckbox) this.check(this.checked);
  $.currentNode=this;$.dispatchEvent(new System.Event("onexpand"));
  //where root node's text is empty
  if(this.parentNode==$.rootNode)
  {
    if(this.text=="" && this.hasChild)
    {
      var node = this.childNodes[0];
      line = document.getElementById($.index+"_expand_"+node.index);
      if(node.line.indexOf("pm")==0)
      {
        if(node.line=="pm1") node.line="pm0";
        else if(node.line=="pm2") node.line="pm3";
        line.src= ics[node.line].src;
      }
      else
      {
        if(node.line=="l1") node.line="l0";
        else if(node.line=="l2") node.line="l3";
        line.src= MzTreeView.icons.line[node.line].src;
      }
    }
  }
};
MzTreeNode.prototype.expandAll = function()
{
  if(this.hasChild && !this.expanded) this.expand();
  for(var x=this.$$caller.index, i=0; i<this.childNodes.length; i++)
  {
    var node = this.childNodes[i];if (node.hasChild)
    setTimeout("Instance('"+x+"').nodes['"+ node.index +"'].expandAll()", 1);
  }
};
MzTreeNode.prototype.expandLevel=function(level)
{
  if(level<=0) return;level--;var me=this;
  if(this.hasChild && !this.expanded) this.expand();
  for(var x=this.$$caller.index, i=0, n=this.childNodes.length; i<n; i++)
  {
    var node=this.childNodes[i], d=node.index;if(node.hasChild)
    setTimeout("Instance('"+x+"').nodes['"+d+"'].expandLevel("+level+")",1);
  }
};
MzTreeNode.prototype.collapse=function()
{
  var $ = this.$$caller;
  var box=document.getElementById($.index +"_tree_"+ this.index);
  if (!box) {System._alert("error in getElementByid");return;}

  var line=document.getElementById($.index+"_expand_"+ this.index);
  var icon=document.getElementById($.index+"_icon_"+ this.index);
  if($.dynamic)MzEffect.hide(box);else box.style.display="none";
  box=MzTreeView.icons.collapse;
  if(line) line.src= box[this.line].src;this.expanded=false;
  if(icon) icon.src=(box[this.icon]||box["file"]).src;
  /*if($.selectedNode && 0==$.selectedNode.path.indexOf(this.path)
    && $.selectedNode.path!=this.path) this.focus();*/
  $.currentNode=this;$.dispatchEvent(new System.Event("oncollapse"));
};
MzTreeNode.prototype.collapseAll = function()
{
  if(this.hasChild && this.expanded) this.collapse();
  for(var x=this.$$caller.index, i=0; i<this.childNodes.length; i++)
  {
    var node = this.childNodes[i];if (node.hasChild && node.isLoaded)
    setTimeout("Instance('"+x+"').nodes['"+ node.index +"'].collapseAll()", 1);
  }
};
MzTreeNode.prototype.focus=function()
{
  var $ = this.$$caller, a=$.rootNode, o;
  if(!$.selectedNode) $.selectedNode=a;
  if(a = document.getElementById($.index +"_link_"+ this.index)){
  if(o = document.getElementById($.index +"_link_"+ $.selectedNode.index))
  o.className="";a.className="selected";
  if($.autoFocused){new MzCookie().add("MzTreeViewSelectedNodePath", this.path);
  /*try{a.focus();}catch(ex){}*/}
  $.selectedNode=this;$.currentNode=this;
  $.dispatchEvent(new System.Event("onfocus"));}
};


//append update remove  --node method
//if the node is not loaded then don't use this method!!
MzTreeNode.prototype.appendNode=function(node)
{
  var $=this.$$caller;if(!$.canOperate) return;
  if(this.hasChild && !this.isLoaded){this.expand();return;}
  this.childNodes.push(node);this.hasChild=this.isLoaded=true;
  var div=document.getElementById($.index +"_tree_"+ this.index);
  if(div.insertAdjacentHTML) div.insertAdjacentHTML("beforeEnd",node.render(true));
  else{var d=document.createElement("DIV");d.innerHTML=node.render(true);
  div.appendChild(d);div.insertBefore(d.firstChild, d);div.removeChild(d);}
  if(this.childNodes.length>1)this.childNodes[this.childNodes.length-2].updateNodeLine();
  else {this.updateNodeLine();this.updateNodeIcon();}this.expand();this.expanded=true;
};
MzTreeNode.prototype.updateNode=function()
{
  var $=this.$$caller;if(!$.canOperate)return;
  $.currentNode=this;var e=new System.Event("onupdatenode");
  $.dispatchEvent(e);if(!e.returnValue) return;

  this.updateNodeLine();
  this.updateNodeIcon();
  this.updateNodeLink();
};
MzTreeNode.prototype.updateNodeLine=function()
{
  var $=this.$$caller, pcs=this.parentNode.childNodes;
  this.hasChild=this.isLoaded?this.childNodes.length>0:this.hasChildNodes();

  var line=document.getElementById($.index +"_expand_"+ this.index);
  if(line){var i=MzTreeView.icons;if($.showLines)
  {
    var b=pcs.indexOf(this)==(pcs.length-1);
    if(b)this.line=this.hasChild?"pm2":"l2";
    else this.line=this.hasChild?"pm1":"l1";
  }
  else   this.line=this.hasChild?"pm3":"ll";
  i=this.hasChild ? (this.expanded?i.expand:i.collapse) : i.line;
  line.src=i[this.line].src;}

};
MzTreeNode.prototype.updateNodeIcon=function()
{
  var $=this.$$caller;
  this.hasChild=this.isLoaded?this.childNodes.length>0:this.hasChildNodes();
  if($.showNodeIcon)
  {
    var icon=document.getElementById($.index +"_icon_"+ this.index);
    var ico=this.$$caller.dataSource[this.sourceIndex].getAttribute("icon");
    if(ico!="folder"&&ico!="file"&&(this.icon=="folder"||this.icon=="file"))
      this.icon = this.hasChild ? "folder" : "file";
    var i=MzTreeView.icons;ico="undefined"==typeof i.expand[this.icon];
    if(this.expanded) {i=ico?i.collapse:i.expand;icon.src=i[this.icon].src;}
    else icon.src=MzTreeView.icons.collapse[this.icon].src;
  }
};
MzTreeNode.prototype.updateNodeText=function()
{
  document.getElementById(this.$$caller.index+"_link_"+this.index).innerHTML=this.text;
};
MzTreeNode.prototype.updateNodeLink=function()
{
  var $=this.$$caller;
  var link=document.getElementById($.index +"_link_"+ this.index);
  var s = $.dataSource[this.sourceIndex], target, url;
  var cs= $.selectedNode==this?"selected":"MzTreeView";
  if(!(target=s.getAttribute("target")))target=$.getDefaultTarget();
  if(!(url=s.getAttribute("url"))) url = $.getDefaultUrl();if($.showToolTip)
  searchByTagName(link, "TR").title=s.getAttribute("hint")||this.text;
  if(data=s.getAttribute("data"))url+=(url.indexOf("?")==-1?"?":"&")+data;
  s="<a target='"+ target +"' id='"+$.index +"_link_"+ this.index +
    "' href='"+ url +"' class='"+ cs +"'>"+ this.text +"</a>";
  link.parentNode.innerHTML=s;
};
MzTreeNode.prototype.removeNode=function()
{
  var $=this.$$caller;if(!$.canOperate) return;
  $.currentNode=this;var evt=new System.Event("onremovenode")
  $.dispatchEvent(evt);if(!evt.returnValue) return;
  if(this.parentNode)
  {
    var div=document.getElementById($.index +"_tree_"+ this.index).parentNode;
    $.indexes=$.indexes.replace($.get__() + this.sourceIndex, "");

    var p=this.parentNode, pcs=p.childNodes, n=pcs.indexOf(this), a=[];
    for(var i=0; i<pcs.length; i++){if(i==n) continue;a.push(pcs[i]);}
    p.childNodes=a;pcs=$.dataSource[this.sourceIndex];
    $.dataSource[this.sourceIndex]=pcs.deleteAttribute("index_"+ $.index);
    if(a.length==0){p.collapse();p.updateNode();MzEffect.hide(div.parentNode);}
    else if(n==a.length){a[a.length-1].updateNode();}pcs=a=null;

    div.parentNode.removeChild(div);p.focus();
  }
};



/****** MzTreeView Inputer *****/
t=[];
t.push("<table border='0' cellspacing='1' id='MzTreeInputer' widht='100%'>");
t.push("<colgroup><col class='caption'/><col class='content' /></colgroup>");
t.push("<tr><td>&#33410;&#28857;ID</td><td><input class='text' id='mtinputerId' maxlength='16'/></td></tr>");
t.push("<tr><td>&#29238;&#33410;&#28857;ID</td><td><input class='text' id='mtinputerParentId' maxlength='16'/></td></tr>");
t.push("<tr><td>&#33410;&#28857;&#25991;&#23383;</td><td><input class='text' id='mtinputerText' maxlength='64'/></td></tr>");
t.push("<tr><td>&nbsp;</td><td id='mtinputeroption' onclick='MzTreeView.inputerhs(this)'>&#20854;&#23427;&#36873;&#39033; &gt;&gt;&gt;</td></tr>");
t.push("<tr style='display: none'><td>&#33410;&#28857;&#22270;&#26631;</td><td><select id='mtinputerIcon'><option value=''>&#35831;&#36873;&#25321;</option></select></td></tr>");
t.push("<tr style='display: none'><td>&#33410;&#28857;&#38142;&#25509;</td><td><input class='text' maxlength='128' id='mtinputerUrl'/></td></tr>");
t.push("<tr style='display: none'><td>Target</td><td><input class='text' id='mtinputerTarget' maxlength='32'/></td></tr>");
t.push("<tr style='display: none'><td>&#25552;&#31034;&#20449;&#24687;</td><td><input class='text' maxlength='64' id='mtinputerHint'/></td></tr>");
t.push("<tr style='display: none'><td>&#38468;&#21152;&#25968;&#25454;</td><td><input class='text' maxlength='255' id='mtinputerData' title='key=value&key=value&key=value&...'/></td></tr>");
t.push("<tr style='display: none'><td>&#36873;&#20013;&#29366;&#24577;</td><td><input type='checkbox' id='mtinputerCheck'/></td></tr>");
t.push("<tr><td><input class='button' type='button' value='&#30830;&#23450;' /></td><td><input class='button' type='button' value='&#21462;&#28040;' /></td></tr>")
MzTreeView.htmlInputer=t.join("") +"</table>";
MzTreeView.inputerhs=function(td)
{
  var tab = searchByTagName(td, "TABLE");
  var b=tab.rows[td.parentNode.rowIndex+1].style.display=="none";
  for(var i=td.parentNode.rowIndex+1; i<tab.rows.length-1; i++)
  b?MzEffect.show(tab.rows[i]):MzEffect.hide(tab.rows[i]);tab=td.innerHTML;
  td.innerHTML = tab.substring(0, tab.indexOf(" ")) +" "+
  (b ? "&lt;&lt;&lt;" : "&gt;&gt;&gt;");
  tab=document.getElementById("mtinputerIcon")
  b?MzEffect.show(tab):MzEffect.hide(tab);
};
MzTreeView.hideInputer=function()
{
  var inputer=document.getElementById("MzTreeInputer");
  inputer.parentNode.removeChild(inputer);
};

MzTreeView.prototype.showInputer = function()
{
  var container=document.createElement("DIV");
  container.style.width="100%";
  MzEffect.show(container);
  container.innerHTML = MzTreeView.htmlInputer;
  document.body.appendChild(container);
  var sel = document.getElementById("mtinputerIcon");
  sel.options.length=1;
  for(var i in MzTreeView.icons.collapse) if(!/^pm\d$/.test(i))
  sel.options[sel.options.length]=new Option(i, i, true, true);
  sel.selectedIndex=0;
};

/*---------------------------------------------------------------------------*\
|  Subject:    Html Element effect base
|  NameSpace:  System.Web.Forms.MzEffect
|  Author:     meizz
|  Created:    2006-07-07
|  Version:    2006-08-05
|-----------------------------------
|  MSN:huangfr@msn.com  QQ:112889082
|  http://www.meizz.com  Copyright (c) meizz   MIT-style license
|  The above copyright notice and this permission notice shall be
|  included in all copies or substantial portions of the Software
\*---------------------------------------------------------------------------*/

//op{interval, duration, increased, continual}
function MzEffect()
{
  this.element=MzEffect.check(arguments[0]);
  if(!this.element) return;

  //prevent repeated effect
  this.attributeName = "att_"+ this._className.replace(/\W/g, "_");
  //if(this.element.getAttribute(this.attributeName)) return;
  //else this.element.setAttribute(this.attributeName, "runing", 0);

  this.terminative=false;  //true for terminate effect

  this.options=System.incorporate({
    interval: 10,   //milliseconds
    duration: 800,  //milliseconds
    increased: true,//false for decreasing
    continual: true
  },arguments[1]||{});

  if(this.initialize) this.initialize();
  this.beginTime = new Date().getTime();
  this.endTime = this.beginTime + this.options.duration;
  if(this.options.onbeforestart) this.options.onbeforestart(this);
  if(this.options.continual) this._process(); else if(!this.virgin){
  this.restore=function(){alert(this +".restore() is unconsummated!");};
  this.dispose();}
};
t=MzEffect.Extends(System, "MzEffect");

t._process=function()
{
  var now = new Date().getTime(), me = this, op=this.options;
  if (now>=this.endTime)
  {
    this.render((op.increased===true ? 1 : 0));
    if(this.finish) this.finish();
    if(op.onafterfinish) op.onafterfinish(this);
    this.dispose(); return;
  }
  var schedule = (now-this.beginTime) / op.duration;
  if(op.increased!==true) schedule = 1 - schedule;

  if(op.onbeforeupdate) op.onbeforeupdate(this);
  if(this.render)this.render(schedule);
  if(op.onafterupdate)op.onafterupdate(this);if(!this.terminative)

  this.timer=setTimeout(function(){me._process()},op.interval); else
  this.element.removeAttribute(this.attributeName);
};
t.cancel=function(){if(this.timer)clearTimeout(this.timer);};
t.dispose=function()
{
  //alert(this +" deleting");
  this.element.removeAttribute(this.attributeName);
  delete this.timer;
  delete this.element;
  delete this.options;
  delete this.endTime;
  delete this.beginTime;
  delete this.terminative;
  delete this.attributeName;
};

//static mehtods
MzEffect.check=function(e)
{
  if("object"==typeof e && !e.tagName) return null;
  if("string"==typeof e && !(e=document.getElementById(e)))
  return null; return e;
};
MzEffect.hide=function()
{
  for (var e=null, n=arguments.length, i=0; i<n; i++)
  {
    if(e=MzEffect.check(arguments[i]))
    {
      if(System.ie && !e.style.width && !e.style.height)
        e.style.display="none"; else
      new MzEffect.Opacity(e,
      {
        increased: false,
        duration: 200,
        onafterfinish: function(o){o.element.style.display="none";o.restore();}
      });
    }
  }
};
MzEffect.show=function()
{
  for (var e=null, n=arguments.length, i=0; i<n; i++)
  {
    if(e=MzEffect.check(arguments[i]))
    {
      e.style.display="";
      if(System.ie && !e.style.width && !e.style.height) return;
      new MzEffect.Opacity(e,{duration:360});
    }
  }
};

//op{interval, duration, increased}
MzEffect.Combo=function(effects, op)
{
  this.effects=effects||[];
  if(this.effects.length==0) return;
  MzEffect.apply(this, [this.effects[0].element, op]);
};
t=MzEffect.Combo.Extends(MzEffect, "MzEffect.Combo");
t.render=function(schedule)
{
  for(var i=0; i<this.effects.length; i++)
  {
    this.effects[i].cancel();
    this.effects[i].render(schedule);
  }
};
t.finish=function()
{
  for(var i=0; i<this.effects.length; i++)
  {
    if(this.effects[i].finish) this.effects[i].finish();
    this.effects[i].dispose();
  }
};
t.dispose=function()
{
  MzEffect.prototype.dispose.call(this);
  delete this.effects;
};

//base effect
//op{interval, duration, increased}
MzEffect.Opacity=function(element, op)
{
  MzEffect.apply(this, arguments);
};
MzEffect.Opacity.Extends(MzEffect, "MzEffect.Opacity").initialize=function()
{
  var op=this.options, obj=this.element;
  this.restore=function()
  {
    if(!System.ie)
    {
      obj.style.opacity = op.Effect_opacity;
      obj.style.MozOpacity = op.Effect_MozOpacity;
      obj.style.KHTMLOpacity = op.Effect_KHTMLOpacity;
    }
    else obj.style.filter = op.Effect_filter;
  };
  this.setOpacity=function(opacity){this.render(opacity);};
  this.render=function(opacity)
  {
    if(!System.ie)
    {
      obj.style.opacity = opacity;
      obj.style.MozOpacity = opacity;
      obj.style.KHTMLOpacity = opacity;
    }
    else obj.style.filter = "alpha(opacity:"+Math.round(opacity*100)+")";
  };
  if (System.ie)op.Effect_filter = obj.style.filter;
  else
  {
    op.Effect_opacity = obj.style.opacity;
    op.Effect_MozOpacity = obj.style.MozOpacity;
    op.Effect_KHTMLOpacity = obj.style.KHTMLOpacity;
  }
};


//op{interval, duration, increased}
MzEffect.MoveBy=function(element, x, y, op)
{
  this.endpointX=x;
  this.endpointY=y;
  MzEffect.apply(this, [element, op]);
};
MzEffect.MoveBy.Extends(MzEffect, "MzEffect.MoveBy").initialize=function()
{
  var obj=this.element, op=this.options;
  op.Effect_top  = obj.style.top;
  op.Effect_left = obj.style.left;
  op.Effect_position = obj.style.position;
  this.render=function(schedule)
  {
    var x = this.endpointY * schedule + this.originalY;
    var y = this.endpointX * schedule + this.originalX;
    this.setPosition(x, y);
  };
  this.setPosition=function(x, y)
  {
    obj.style.top  = y +"px";
    obj.style.left = x +"px";
  };
  this.restore=function()
  {
    obj.style.top  = op.Effect_top;
    obj.style.left = op.Effect_left;
    obj.style.position = op.Effect_position;
  };
  this.originalY = parseFloat(obj.style.top  || '0');
  this.originalX = parseFloat(obj.style.left || '0');
  if(obj.style.position == "") obj.style.position = "relative";
};


//op{interval, duration, increased, beginColor, endColor, finalColor}
function n2h(s){s=parseInt(s).toString(16);return ("00"+ s).substr(s.length);}
MzEffect.Glittery=function(element, op)
{
  MzEffect.apply(this, arguments);
};
MzEffect.Glittery.Extends(MzEffect, "MzEffect.Glittery").initialize=function()
{
  var op=this.options, obj=this.element, endColor="#FFFFFF";
  var backColor = (obj.currentStyle||obj.style).backgroundColor;
  if(backColor)
  {
    if(/^\#[\da-z]{6}$/i.test(backColor))endColor=backColor;
    if(backColor.indexOf("rgb(")==0)
    {
      var cols=backColor.substring(4, backColor.length-1).split(",");
      for(var i=0,endColor="#";i<cols.length;i++)endColor+=n2h(cols[i]);
    }
  }
  op.beginColor = op.beginColor || "#FFFF00";
  op.endColor   = op.endColor || endColor;
  op.finalColor = op.finalColor || obj.style.backgroundColor;

  this.colors_base=[
    parseInt(op.beginColor.substring(1,3),16),
    parseInt(op.beginColor.substring(3,5),16),
    parseInt(op.beginColor.substr(5),16)];
  this.colors_var=[
    parseInt(op.endColor.substring(1,3),16)-this.colors_base[0],
    parseInt(op.endColor.substring(3,5),16)-this.colors_base[1],
    parseInt(op.endColor.substr(5),16)-this.colors_base[2]];

  this.finish=function()
  {
    obj.style.backgroundColor = op.finalColor;
  };
  this.render=function(schedule)
  {
    var colors=[
      n2h(Math.round(this.colors_base[0]+(this.colors_var[0]*schedule))),
      n2h(Math.round(this.colors_base[1]+(this.colors_var[1]*schedule))),
      n2h(Math.round(this.colors_base[2]+(this.colors_var[2]*schedule)))];
    obj.style.backgroundColor = "#"+ colors.join("");
  };
  this.dispose=function()
  {
    MzEffect.prototype.dispose.call(this);
    delete this.colors_base;
    delete this.colors_var;
  };
};
