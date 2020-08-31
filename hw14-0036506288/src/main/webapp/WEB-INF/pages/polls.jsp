<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<!DOCTYPE html>
<html>
<head>
	<title>Polls</title>
</head>
<body>
	<h1>Odaberite jednu od ponuđenih anketa kako biste glasali. :)</h1>
	<p>Ankete koje nudimo:</p>
	<ol>
		<c:forEach var="item" items="${requestScope.polls}">
			<li><a href="${pageContext.request.contextPath}/servleti/glasanje?id=${item.id}">${item.title}</a></li>
		</c:forEach>
	</ol>
</body>
</html>