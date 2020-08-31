<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<!DOCTYPE html>
<html>
<head>
	<title>Voting Options</title>
</head>
<body>
	<h1>${requestScope.poll.title}</h1>
	<p>${requestScope.poll.message}</p>
	<ol>
		<c:forEach var="item" items="${requestScope.pollOptions}">
			<li><a href="${pageContext.request.contextPath}/servleti/glasanje-glasaj?id=${requestScope.poll.id}&optionID=${item.id}">${item.optionTitle}</a></li>
		</c:forEach>
	</ol>
</body>
</html>