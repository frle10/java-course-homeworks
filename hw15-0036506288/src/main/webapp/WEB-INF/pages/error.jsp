<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<!DOCTYPE html>
<html>
	<head>
		<title>Error</title>
	</head>
	
	<body>
		${requestScope.loggedIn}
		<p><p>
		<p>${requestScope.errorMsg}</p>
		<a href="main">Back to index</a>
	</body>
</html>