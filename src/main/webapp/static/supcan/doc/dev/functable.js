function getXMLDoc(url)
{
	var agnt=navigator.userAgent.toLowerCase();
	var isChrome = false;
	var isIE = (agnt.indexOf("msie")>0 || agnt.indexOf("trident")>0) ? true : false;
	if(!isIE) {
		if(agnt.indexOf("chrome")>0) isChrome = true;
	}
	var xmlDoc;
	if(isChrome) {
		var xhr = new XMLHttpRequest();
		xhr.open("GET", url, false); 
		xhr.send(null);
		xmlDoc = xhr.responseXML; 
	}
	else {
		if(isIE)
			xmlDoc=new ActiveXObject("Microsoft.XMLDOM");
		else
			xmlDoc=document.implementation.createDocument("","",null);
		xmlDoc.async=false;
		xmlDoc.load(url);
	}
	return xmlDoc;
}

function inserttable(funcname, usage, ret, memo, arrPara, example, illu, divFuncTable)
{
	if(illu != "") illu = '<img src="documentg.bmp" title="' +illu+ '">&nbsp;&nbsp;';
	var str = '<div id="' +divFuncTable+funcname+ '"></div><br>';
	str += '<table class="funcTable" width=98% cols=3 cellpadding=4 cellspacing=0 border=1 borderColorLight=#999999 borderColorDark=#999999 align="center">';
	str += '<col width=120><col><col width=64 align=right>';
	str += '<tr bgcolor=#d8d8f8><td style="border-right:0px;font-size:18px;color:#008899;font-weight:900" align=center>' +funcname+ '</td><td  style="border-right:0px;"><b>' +usage+ '</b></td>';
	str += '<td style="border-left:0px;">' +illu+  '<a href="#func_' +divFuncTable+funcname+ '"><img border=0 src="arrowup.gif" title="回到目录"/> </a> </td></tr>';

	var paras = arrPara.length;
	if(paras==0)
		str += '<tr><td align=right>参数</td><td colspan=2>(无)</td></tr>';
	else if(paras==1)
		str += '<tr><td align=right>参数</td><td colspan=2>' +arrPara[0]+ '</td></tr>';
	else {
		for(var i=1; i<=paras; i++) {
			str += '<tr><td align=right>参数' +i+ '</td><td colspan=2>' +arrPara[i-1]+ '</td></tr>';
		}
	}

	if(ret=="") ret = "(无)";
	str += '<tr><td align=right>返回值</td><td colspan=2>' +ret+ '</td></tr>';
	if(memo != "") str += '<tr><td align=right>备注</td><td colspan=2>' +memo+ '</td></tr>';
	if(example != "") str += '<tr bgColor=yellow><td align=right>示例</td><td colspan=2>' +example+ '</td></tr>';
	str += '</table><br>';
	return str;
}

function genfunctable(divFuncTable, divFunc, xmldoc)
{
	if(xmldoc==null) return;
	var root = xmldoc.documentElement;	if(root==null) return;
	var x=xmldoc.getElementsByTagName('category'); if(x==null) return;
	var cats = x.length;

	var str = '<table class="funcTable" width=94% cellpadding=2 cellspacing=0 border=1 borderColorLight=#999999 borderColorDark=#999999 align="center">';
	str += '<tr bgcolor=#eaeaea><th>分类</th><th>函数名</th><th>用途</th></tr>';
	for(i=0; i<cats; i++) {
		var cat = x[i];
		var catName = cat.attributes.getNamedItem("name").nodeValue;
		var xx = cat.getElementsByTagName("function");
		var funcs = xx.length;
		for(j=0; j<funcs; j++) {
			var func = xx[j];
			var funcname = func.attributes.getNamedItem("name").nodeValue;
			var usage = func.getElementsByTagName("usage")[0].childNodes[0].nodeValue;
			var s = '<tr>';
			if(j==0) s+= '<td rowspan=' +funcs+ '>' +catName+ '</td>';
			s +='<td><a href="#' +divFuncTable+funcname+ '"><div id="func_' +divFuncTable+funcname+ '"/>' +funcname+ '</a></td><td>' +usage+ '</td></tr>';
			str += s;
		}
	}
	document.getElementById(divFuncTable).innerHTML = str + '</table>';

	str=' 函数详解:';
	for(i=0; i<cats; i++) {
		var cat = x[i];
		var xx = cat.getElementsByTagName("function");
		var funcs = xx.length;
		for(j=0; j<funcs; j++) {
			var paras=0;
			var usage="";
			var detail="";
			var ret="";
			var memo="";
			var example="";
			var illu="";
			var arrPara=new Array();
			var func = xx[j];
			var funcname = func.attributes.getNamedItem("name").nodeValue;

			var childs = func.childNodes.length;
			for(k=0; k<childs; k++) {
				var xxx = func.childNodes[k];
				if(xxx.nodeType!=1) continue;

				var text = xxx.childNodes[0].nodeValue;
				if(xxx.nodeName == "usage") usage = text;
				else if(xxx.nodeName == "detail") detail = text;
				else if(xxx.nodeName == "return") ret = text;
				else if(xxx.nodeName == "memo") memo = text;
				else if(xxx.nodeName == "example") example = text;
				else if(xxx.nodeName == "illu") illu = text;
				else if(xxx.nodeName == "para") arrPara[paras++]=text;
			}
			if(detail != "") usage=detail;
			str += inserttable(funcname, usage, ret, memo, arrPara, example, illu, divFuncTable);
		}
	}
	document.getElementById(divFunc).innerHTML = str;
}



//Events
function genetr(eventname, meaning, arrp, d1count, d1current)
{
  var str = '<tr>';
  if(d1count>0) {
     if(d1current==0) str += '<td rowspan=' +(d1count+1)+ '>' + eventname + '</td>';
  }
  else
    str += '<td>' + eventname + '</td>';
  str += '<td>' +meaning+ '</td>';
  for(var i=0; i<4; i++) {
    var s='&nbsp;';
    if(i<arrp.length) s = arrp[i];
    str += '<td>' + s + '</td>';
  }
  str += '</tr>';
  return str;
}
function getsame(ele, count, i, name)
{
	var ret = 0;
	for(i++; i<count; i++) {
		var c = ele.childNodes[i];
		if(c.nodeType==3) continue;
		if(c.nodeName != name) break;
		ret++;
	}
	return ret;
}
function geneventtable(divTag, xmldoc, nodename)
{
	if(xmldoc==null) return;
	var x=xmldoc.getElementsByTagName(nodename); if(x==null) return;

	var str = '<table width=98% cellpadding=4 cellspacing=0 border=1 borderColorLight=#999999 borderColorDark=#999999 align="center">';
	str += '<tr bgcolor=#eaeaea><td>Event</td><td>含义</td><td>p1</td><td>p2</td><td>p3</td><td>p4</td></tr>';

	var ele = x[0];
	var count = ele.childNodes.length;
	var samecount=0, samecurrent=0;
	for(i=0; i<count; i++) {
		var c = ele.childNodes[i];
		if(c.nodeType==3) continue;
		var eventname = c.nodeName;
		if(samecount==samecurrent) {
			samecount = getsame(ele, count, i, eventname);
			samecurrent=0;
		}
		else if(samecount>0)
			samecurrent++;

		var meaning="";
		var arrp = new Array(0);
		for(j=0; j<c.childNodes.length; j++) {
			var d=c.childNodes[j];
			if(d.nodeName=="meaning")
				meaning = d.childNodes[0].nodeValue;
			else if(d.nodeName == "p")
				arrp.push(d.childNodes[0].nodeValue);
		}
		str += genetr(eventname, meaning, arrp, samecount, samecurrent);
	}
	str += '</table>';
	document.getElementById(divTag).innerHTML = str;
}
