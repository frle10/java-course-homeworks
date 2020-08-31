<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<!DOCTYPE html>
<html>
	<head>
		<title>Grafika</title>
	</head>
	
	<body>
		<ul>
			<c:forEach var="item" items="${requestScope.images}">
				<li><a href="imageInfo?id=${item}">${item}</a></li>
			</c:forEach>
		</ul>
		
		<form action="addImage" method="POST" id="myForm">
 			Ime datoteke:<br><input type="text" name="fileName"><br>
 			Sadržaj datoteke:<br><textarea rows="10" cols="50" form="myForm" name="content" ></textarea><br>
 			<input type="submit" value="Pohrani">
		</form>
	</body>
</html>