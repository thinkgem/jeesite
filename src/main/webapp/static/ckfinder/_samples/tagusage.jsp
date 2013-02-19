<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<!--
 * CKFinder
 * ========
 * http://ckfinder.com
 * Copyright (C) 2007-2010, CKSource - Frederico Knabben. All rights reserved.
 *
 * The software, this file and its contents are subject to the CKFinder
 * License. Please read the license.txt file before using, installing, copying,
 * modifying or distribute this file or part of its contents. The contents of
 * this file is part of the Source Code of CKFinder.
-->
<%@page language="Java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!-- import the tag library -->
<%@ taglib uri="http://ckfinder.com" prefix="ckfinder" %>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<title>CKFinder - Java Sample - Standalone</title>
	<link href="sample.css" rel="stylesheet" type="text/css" />
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
	<meta name="robots" content="noindex, nofollow" />
</head>
<body>
	<h1 class="samples">
		CKFinder - Java Sample - Standalone
	</h1>
	<div class="description">
		CKFinder may be used in standalone mode inside any page, to create a repository
		manager with ease.</div>
	<p style="padding-left: 30px; padding-right: 30px;">
	<!-- CKFinder tag on your JSP page -->
	<ckfinder:ckfinder basePath="../" width="100%" height="500" />
	</p>
	<div id="footer">
		<hr />
		<p>
			CKFinder - Ajax File Manager - <a class="samples" href="http://ckfinder.com/">http://ckfinder.com</a>
		</p>
		<p id="copy">
			Copyright &copy; 2003-2012, <a class="samples" href="http://cksource.com/">CKSource</a> - Frederico Knabben. All rights reserved.
		</p>
	</div>
</body>
</html>
