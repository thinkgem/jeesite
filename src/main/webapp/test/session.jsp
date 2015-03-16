<%@ page import="org.slf4j.Logger,org.slf4j.LoggerFactory"%>
<%@ page contentType="text/html; charset=GBK"%>
<%@ page import="java.util.*"%>
<html>
<head>
<title>Cluster App Session Test</title>
</head>
<body>
	Server Info:
	<%
	out.println(request.getLocalAddr() + " : " + request.getLocalPort() + "<br>");
	out.println("<br/> SID: " + session.getId() + "<br/>");
	String dataName = request.getParameter("dataName");

	if (dataName != null && dataName.length() > 0) {
		String dataValue = request.getParameter("dataValue");
		session.setAttribute(dataName, dataValue);
	}

	out.print("<br/><b>Session</b><br/>");

	Enumeration e = session.getAttributeNames();

	while (e.hasMoreElements()) {
		String name = (String) e.nextElement();
		String value = session.getAttribute(name).toString();
		out.println(name + " = " + value + "<br>");
		System.out.println(name + " = " + value);
	}
	out.print("<br/><b>Form</b><br/>");
%>
	<form action="" method="POST">
		&nbsp;name: <input type=text size=20 name="dataName"> <br>
		value: <input type=text size=20 name="dataValue"> <br> <input
			type=submit>
	</form>
	
	<%="最大内存: "+(Runtime.getRuntime().maxMemory()/1024/1024)
		+"m, 已分配内存: "+(Runtime.getRuntime().totalMemory()/1024/1024)
		+"m, 已分配内存中的剩余空间: "+(Runtime.getRuntime().freeMemory()/1024/1024)
		+"m, 最大可用内存: "+((Runtime.getRuntime().maxMemory()-Runtime.getRuntime().totalMemory()
				+Runtime.getRuntime().freeMemory())/1024/1024)+"m" %>
				
</body>
</html>