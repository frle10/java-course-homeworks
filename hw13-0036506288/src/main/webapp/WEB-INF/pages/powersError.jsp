<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<!DOCTYPE html>
<html>
	<head>
		<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/style.jsp">
		<title>Error</title>
	</head>
	
	<body>
		<h1>Error</h1>
		<p>There was an error while calculating powers because you provided invalid parameters!</p>
		<p>The error message is: <i>${requestScope.errorMessage}</i></p>
	</body>
</html>