<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <!-- Le HTML5 shim, for IE6-8 support of HTML elements -->
    <!--[if lt IE 9]>
      <script src="http://html5shim.googlecode.com/svn/trunk/html5.js"></script>
    <![endif]-->
    
<!-- 
	include javascript
	Placed at the end of the document so the pages load faster
-->
	<script type="text/javascript" src="${pageContext.request.contextPath }/resources/jquery/jquery-1.7.1.min.js"></script>
	<script type="text/javascript" src="${pageContext.request.contextPath }/resources/bootstrap/js/bootstrap.min.js"></script>
	<script type="text/javascript" src="${pageContext.request.contextPath }/resources/common/withapi.js"></script>
	<!-- bootstrap css -->
	<link href="${pageContext.request.contextPath }/resources/bootstrap/css/bootstrap.min.css" rel="stylesheet" type="text/css" />
	<link href="${pageContext.request.contextPath }/resources/bootstrap/css/bootstrap-responsive.min.css" rel="stylesheet" type="text/css" />
	<title>withapi.com</title>
</head>
<body>
<div id="warp">
		<div id="header">
			<tiles:insertAttribute name="header" /> 
		</div>
		
		<div id="body">
			<tiles:insertAttribute name="contents" />
		</div>	
		
		<div id="footer">
			<tiles:insertAttribute name="footer" />
		</div>
</div>
</body>
</html>