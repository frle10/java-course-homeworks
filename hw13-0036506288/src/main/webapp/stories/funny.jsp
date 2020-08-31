<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<%
	String[] colors = new String[] {"black", "blue", "red", "orange", "green", "violet",
			"cyan", "pink"};
	int index = (int) ((Math.random() * 1e6) % colors.length);
	String color = colors[index];
	request.setAttribute("color", color);
%>

<!DOCTYPE html>
<html>
	<head>
		<style type="text/css">
			p {color: ${requestScope.color};}
		</style>
		<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/style.jsp">
		<title>Funny Story</title>
	</head>
	
	<body>
		<h1>A funny story</h1>
		<p>Here's a funny story for you... :)</p>
		<p>Two men walk into a bar.<br>
		The first man says: "I'll have some H2O."<br>
		The second man says: "I'll have some H2O too."<br>
		The second man died.
		</p>
	</body>
</html>