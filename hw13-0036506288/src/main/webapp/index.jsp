<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<!DOCTYPE html>
<html>
	<head>
		<link rel="stylesheet" type="text/css" href="style.jsp">
		<title>Index</title>
	</head>
	
	<body>
		<a href="colors.jsp">Background color chooser</a><br>
		<a href="trigonometric?a=0&b=90">Trigonometric</a>
		<p></p>
		<form action="trigonometric" method="GET">
 			Početni kut:<br><input type="number" name="a" min="0" max="360" step="1" value="0"><br>
 			Završni kut:<br><input type="number" name="b" min="0" max="360" step="1" value="360"><br>
 			<input type="submit" value="Tabeliraj"><input type="reset" value="Reset">
		</form>
		<p><a href="stories/funny.jsp">A funny story</a></p>
		<p><a href="powers?a=1&b=100&n=3">Powers</a></p>
		<p><a href="appinfo.jsp">App info</a></p>
		<p><a href="glasanje">Voting application</a></p>
	</body>
</html>