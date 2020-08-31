<%@ page import="java.util.Map, hr.fer.zemris.java.hw13.servlets.BandEntry" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<!DOCTYPE html>
<html>
<head>
<style type="text/css">
table.rez td {
	text-align: center;
}
</style>
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/style.jsp">
</head>
<body>
	<h1>Rezultati glasanja</h1>
	<p>Ovo su rezultati glasanja.</p>
	<table border="1" class="rez">
		<thead>
			<tr>
				<th>Bend</th>
				<th>Broj glasova</th>
			</tr>
		</thead>
		<tbody>
			<c:forEach var="item" items="${requestScope.bands}">
				<tr>
					<td>${item.value.bandName}</td>
					<td>${item.value.numberOfVotes}</td>
				</tr>
			</c:forEach>
		</tbody>
	</table>

	<h2>Grafički prikaz rezultata</h2>
	<img alt="Pie-chart" src="${pageContext.request.contextPath}/glasanje-grafika" width="720" height="450" />

	<h2>Rezultati u XLS formatu</h2>
	<p>
		Rezultati u XLS formatu dostupni su <a href="${pageContext.request.contextPath}/glasanje-xls">ovdje</a>.
	</p>

	<h2>Razno</h2>
	<p>Primjeri pjesama pobjedničkih bendova:</p>
	<ul>
		<c:forEach var="item" items="${requestScope.winners}">
			<li><a href="${item.value.songLink}" target="_blank">${item.value.bandName}</a></li>
		</c:forEach>
	</ul>
</body>
</html>