<%@page import="hr.fer.zemris.java.hw15.model.BlogUser"%>
<%@page import="java.util.List"%>
<%@page import="javax.persistence.EntityManager"%>
<%@page import="javax.persistence.EntityManagerFactory"%>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<%
	EntityManagerFactory emf = (EntityManagerFactory) application.getAttribute("my.application.emf");
	EntityManager em = emf.createEntityManager();
	em.getTransaction().begin();
	
	List<BlogUser> authors = em.createQuery("SELECT u FROM BlogUser AS u", BlogUser.class).getResultList();
	request.setAttribute("authors", authors);
%>

<!DOCTYPE html>
<html>
	<head>
		<style type="text/css">
			.error {
		   		font-family: fantasy;
		   		font-weight: bold;
		   		font-size: 0.9em;
		   		color: #FF0000;
		}
		</style>
		<title>Index</title>
	</head>
	<body>
		${requestScope.loggedIn}
		<p><p>
		
		<c:if test="${empty sessionScope['current.user.id']}">
			<form method="post" action="authenticate" accept-charset="UTF-8">
  				Enter your nickname:
  				<input name="nick" type="text" value="${oldNick}">
  				<br><br>
  				Enter your password:
  				<input name="password" type="password">
  				<br><br>
  				<c:if test="${not empty errorMsg}">
  					<p class="error">${errorMsg}</p>
  				</c:if>
  				<input type="submit">
			</form>
		</c:if>
		
		<c:if test="${empty sessionScope['current.user.id']}">
		 	<p>Don't have an account yet? Register <a href="register">here</a>.</p>
		</c:if>
		
		<c:if test="${not empty sessionScope['current.user.id']}">
		 	<p><a href="logout">Logout</a></p>
		</c:if>
		
		<p>Registered authors:</p>
		<ul>
			<c:forEach var="author" items="${requestScope.authors}">
				<li><a href="author/${author.nick}">${author.nick}</a></li>
			</c:forEach>
		</ul>
	</body>
</html>