<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>

<!DOCTYPE html>
<html>
	<head>
		<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/style.jsp">
		<title>Trigonometric</title>
	</head>
	
	<body>
		<h1>Sines and cosines</h1>
		<table>
			<tr><th>Value</th><th>Sine</th><th>Cosine</th></tr>
			<c:forEach var="brojac" begin="${requestScope.a}" end="${requestScope.b}">
				<tr>
					<td>${brojac}</td>
					<td><fmt:formatNumber type="number" maxFractionDigits="6" value="${requestScope.sines[brojac - requestScope.a]}" /></td>
					<td><fmt:formatNumber type="number" maxFractionDigits="6" value="${requestScope.cosines[brojac - requestScope.a]}" /></td>
				</tr>
			</c:forEach>
		</table>
	</body>
</html>