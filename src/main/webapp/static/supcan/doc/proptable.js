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

function gentr(hasexample, name, meaning, val, defaultval, sample, inc, exc, memo, lastGroup, groups, groupcurrent, groupcolor)
{
  if(defaultval == '') defaultval = '(无)';
  if(sample == '')
    sample = '&nbsp;';
  else
    sample = '<img src="document.bmp" title="请参见演示页: ' +sample+ '">';
  if(inc == "Edit*") inc = "Edit、EditWithButton的等有输入框的对象";
  if(exc == "Edit*") exc = "Edit、EditWithButton的等有输入框的对象";

  var arrMemo = new Array(0);
  if(memo != "") arrMemo = memo.split('<br>');
  if(inc != "") arrMemo.push('仅用于' + inc);
  if(exc != "") arrMemo.push('对' + exc + '无效;');
  memo="";
  if(arrMemo.length == 1)
    memo = '注: ' + arrMemo[0];
  else {
    for(i=0; i<arrMemo.length; i++) {
      if(arrMemo[i].substring(0, 1)==":")
        memo += arrMemo[i].substr(1) + '<br>';
      else
        memo += '注' + (i+1) + ': ' + arrMemo[i] + ';<br>';
    }
  }

  var td = (memo=="") ? "<td>" : "<td rowspan=2>";
  var str='<tr>';
  if(groups>0) {
    if(groupcurrent==0) {
	str += '<td rowspan=' +groups;
	if(groupcolor!="") str+=' bgColor="'+groupcolor + '"';
	str += '>' +lastGroup.replace(/\//g, '<br>')+ '</td>';
    }
    str += td;
  }
  else {
      if(memo == "")
        str += '<td colspan=2>';
      else
        str += '<td colspan=2 rowspan=2>';
  }

  name = name.replace(/\\/g, '<br>');
  var arr = name.split('/');
  var count = arr.length;
  for(var i=0; i<count; i++) {
    if(i>0) str += ' 或 ';
    str += arr[i];
// str += '<font face="consolas" color=#0077aa>' +arr[i]+ '</font>';
  }
  str += '</td><td>' +meaning+ '</td><td>' +val + '</td>' + td +defaultval+ '</td>';
  if(hasexample) str += td +sample+ '</td>';
  str += '</tr>';
  if(memo != "") str += '<tr><td colspan=2 bgColor=#ffffdd>' + memo+ '</td></tr>';
  return str;
}

function hassample(x)
{
	for(var ele = x[0]; ele!=null; ele=ele.nextSibling) {
		if(ele.nodeName != 'element') continue;
		if(ele.getElementsByTagName("example").length>0) return true;
	}
	return false;
}
function getgroupcount(ele)
{
	var group="";
	var ret = 0;
	for(; ele!=null; ele=ele.nextSibling) {
		if(ele.nodeType==3) continue;

		var gp = ele.getElementsByTagName("group");
		if(gp.length == 0) break;

		var s = gp[0].childNodes[0].nodeValue;
		if(ret == 0) {
			group = s;
			ret = 1;
		}
		else if(group == s)
			ret++;
		else
			break;

		gp = ele.getElementsByTagName("memo");
		if(gp.length>0)
			ret++;
		else {
			gp = ele.getElementsByTagName("includeOnly");
			if(gp.length>0)
				ret++;
			else {
				gp = ele.getElementsByTagName("excludeOnly");
				if(gp.length>0)
					ret++;
			}
		}
	}
	return ret;
}

function genproptable(divTag, xmldoc, zebracolor)
{
	if(xmldoc==null) return;
	var x=xmldoc.getElementsByTagName('elements'); if(x==null) return;
	var content="", sample="";
	var name = x[0].attributes.getNamedItem("name").nodeValue;
	var meaning = x[0].attributes.getNamedItem("meaning").nodeValue;
	var number = x[0].attributes.getNamedItem("number").nodeValue;
	var c = x[0].attributes.getNamedItem("content");
	if(c!=null) content = c.nodeValue;
	c = x[0].attributes.getNamedItem("example");
	if(c!=null) sample = c.nodeValue;

	x=xmldoc.getElementsByTagName('element'); if(x==null) return;
	var hasexample = hassample(x);
	var cols = hasexample ? 6:5;

	var str = '<br><b><font size=4>' +name+ '</font></b>';
	if(sample != "") str += '　　<img src="document.bmp" title="请参见演示页' + sample + '">';
	str += '<br>';
	str += '<table width=100% cellpadding=4 cellspacing=0 border=1 borderColorLight=#999999 borderColorDark=#999999 align="center" cols=' +cols+ '>';
	str += '<tr bgcolor=#e4e4e4><td colspan=' +cols+ '>含义: ' +meaning+ '；数量: ' +number;
	if(content != "") str += ', 内容: ' +content;
	str += '</td></tr>';
	str += '<tr bgcolor=#f1f1f1><td colspan=2>属性名称</td><td>含义</td><td>可用值</td><td>默认值</td>';
	if(hasexample) str += '<td>示例</td>';
	str += '</tr>';

	var lastGroup="", groups=0, groupcurrent=0, zebracount=0;
	for(var ele = x[0]; ele!=null; ele=ele.nextSibling) {
		if(ele.nodeType==3) continue;
		if(ele.nodeName == 'textLine') {
			var text = ele.childNodes[0].nodeValue;
			str += '<tr  bgcolor=#f6f6f6><td colspan=6>' +text+ '</td></tr>';
			continue;
		}

		var count = ele.childNodes.length;
		var defaultval="", val="", inc="", exc="", memo="", group="";
		name = ele.attributes.getNamedItem("name").nodeValue;
		meaning = ele.attributes.getNamedItem("meaning").nodeValue;
		sample="";
		for(j=0; j<count; j++) {
			var text="";
			c = ele.childNodes[j];
			if(c.childNodes.length>0) text = c.childNodes[0].nodeValue;
			if(c.nodeName=="default")
				defaultval = text;
			else if(c.nodeName == "group")
				group = text;
			else if(c.nodeName == "includeOnly")
				inc = text;
			else if(c.nodeName == "excludeOnly")
				exc = text;
			else if(c.nodeName == "example")
				sample = text;
			else if(c.nodeName == "memo") {
				if(memo != "") memo += "<br>";
				memo += text;
			}
			else if(c.nodeName == "datatype") {
				if(text == "bool" || text == "boolean") val = "true/false";
			}
			else if(c.nodeName == "value") {
				var valattr = c.attributes.getNamedItem("meaning");
				if(val != "") val += '<br>';
				if(!valattr)
					val += text;
				else
					val += '<b>' + text + '</b> - ' + valattr.nodeValue + ';';
			}
		}

		var groupcolor="";
		if(lastGroup != group) {
			lastGroup = group;
			groupcurrent=0;
			groups = 0;
			if(group != "") groups = getgroupcount(ele);
			if(groups>0) {
				if(zebracolor != "") {
					zebracount = zebracount==0 ? 1:0;
					if(zebracount==0) groupcolor=zebracolor;
				}
			}
		}
		else
			groupcurrent++;
		str += gentr(hasexample, name, meaning, val, defaultval, sample, inc, exc, memo, lastGroup, groups, groupcurrent, groupcolor);
	}
	str += '</table>';

	//comments
	x=xmldoc.getElementsByTagName('comment');
	if(x!=null) {
		for(i=0; i<x.length; i++) {
			var text = x[i].childNodes[0].nodeValue;
			str += '备注';
			if(x.length>1) str += i+1;
			str +=  ': ' + text + ';<br>';
		}
	}
	document.getElementById(divTag).innerHTML = str;
}
