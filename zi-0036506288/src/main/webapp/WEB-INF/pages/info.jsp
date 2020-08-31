<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<!DOCTYPE html>
<html>
	<head>
		<title>Info</title>
	</head>
	
	<body>
		Ime datoteke: ${sessionScope.fileName}<br>
		Linija: ${sessionScope.lines}<br>
		Krugova: ${sessionScope.circles}<br>
		Ispunjenih krugova: ${sessionScope.fCircles}<br>
		Ispunjenih trokuta: ${sessionScope.fTriangles}<br>
		
		<p>
		<img alt="Slika" src="dobiSliku?path=${sessionScope.fileName}" border="1">
		<p>
		<a href="main">Početna</a>
	</body>
</html>