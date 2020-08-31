<%@ page import="java.util.Calendar" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<% 
	long currentTime = System.currentTimeMillis();
	long startedTime = (long) application.getAttribute("startedTime");
	
	Calendar calendar = Calendar.getInstance();
	calendar.setTimeInMillis(currentTime - startedTime);
	
	long miliseconds = currentTime - startedTime;
	long seconds = miliseconds / 1000;
	long minutes = seconds / 60;
	long hours = minutes / 60;
	long days = hours / 24;
	
	String time = days + " days " + (hours % 24) + " hours " +
					(minutes % 60) + " minutes " + (seconds % 60) + " seconds " +
					(miliseconds % 1000) + " miliseconds.";
	
	request.setAttribute("runningTime", time);
%>

<!DOCTYPE html>
<html>
	<head>
		<link rel="stylesheet" type="text/css" href="style.jsp">
		<title>Running Time</title>
	</head>
	
	<body>
		<h1>Application Running Time</h1>
		<p>The application has been running for: ${requestScope.runningTime}</p>
	</body>
</html>