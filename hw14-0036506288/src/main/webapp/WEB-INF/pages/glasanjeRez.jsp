<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<!DOCTYPE html>
<html>
<head>
<style type="text/css">
table.rez td {
	text-align: center;
}
</style>
<title>Voting Results</title>
</head>
<body>
	<h1>Rezultati glasanja</h1>
	<p>Ovo su rezultati glasanja.</p>
	<table border="1" class="rez">
		<thead>
			<tr>
				<th>Opcija</th>
				<th>Broj glasova</th>
			</tr>
		</thead>
		<tbody>
			<c:forEach var="item" items="${requestScope.pollOptions}">
				<tr>
					<td>${item.optionTitle}</td>
					<td>${item.votesCount}</td>
				</tr>
			</c:forEach>
		</tbody>
	</table>

	<h2>Grafički prikaz rezultata</h2>
	<img alt="Pie-chart" src="${pageContext.request.contextPath}/servleti/glasanje-grafika?id=${param.id}" width="720" height="450" />

	<h2>Rezultati u XLS formatu</h2>
	<p>
		Rezultati u XLS formatu dostupni su <a href="${pageContext.request.contextPath}/servleti/glasanje-xls?id=${param.id}">ovdje</a>.
	</p>

	<h2>Razno</h2>
	<p>Značajni linkovi vezani uz pobjedničke opcije:</p>
	<ul>
		<c:forEach var="item" items="${requestScope.winners}">
			<li><a href="${item.optionLink}" target="_blank">${item.optionTitle}</a></li>
		</c:forEach>
	</ul>
</body>
</html>