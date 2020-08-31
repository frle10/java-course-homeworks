<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE html>
<html>
	<head>
		<title></title>
	</head>
	
	<body>
		${requestScope.loggedIn}
		<p><p>
		
		<c:choose>
			<c:when test="${empty requestScope.blogs}">
				<p>There are no blog entries registered for user <b><i>${requestScope.nick}</i></b>.</p>
			</c:when>
			
			<c:otherwise>
				<c:forEach var="blog" items="${requestScope.blogs}">
					<a href="${blog.creator.nick}/${blog.id}">${blog.title}</a><br>
				</c:forEach>
			</c:otherwise>
		</c:choose>
		
		<c:if test="${sessionScope['current.user.nick'] eq requestScope.nick}">
			<p><a href="${requestScope.nick}/new">Add a new blog entry</a></p>
		</c:if>
	</body>
</html>