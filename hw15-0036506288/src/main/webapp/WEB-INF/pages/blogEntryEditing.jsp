<%@page import="hr.fer.zemris.java.hw15.web.forms.BlogForm"%>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<%
	Long blogID = ((BlogForm) request.getAttribute("blog")).getId();
	String paramID = "";
	
	if(blogID != null) {
		paramID = "?id=" + blogID;
	}
	
	request.setAttribute("idParam", paramID);
%>

<!DOCTYPE html>
<html>
	<head>
		<title>Blog Editor</title>
		<style type="text/css">
		.error {
		   font-family: fantasy;
		   font-weight: bold;
		   font-size: 0.9em;
		   color: #FF0000;
		   padding-left: 110px;
		}
		.formLabel {
		   display: inline-block;
		   width: 100px;
                   font-weight: bold;
		   text-align: right;
                   padding-right: 10px;
		}
		.formControls {
		  margin-top: 10px;
		}
		</style>
	</head>
	
	<body>
		${requestScope.loggedIn}
		<p><p>
		<h1>Blog Entry Editor</h1>
		<p>Welcome to blog entry editor! Here you can create a new entry or edit an existing one.</p>
		
		<form action="${pageContext.request.contextPath}/servleti/save${idParam}" method="post">
			<div>
			 <div>
			  <span class="formLabel">Title</span><input type="text" name="title" value='<c:out value="${blog.title}"/>' size="50">
			 </div>
			 <c:if test="${blog.hasError('title')}">
			 <div class="error"><c:out value="${blog.getError('title')}"/></div>
			 </c:if>
			</div>
	
			<div>
			 <div>
			  <span class="formLabel">Text</span><textarea cols="50" rows="10" name="text">${blog.text}</textarea>
			 </div>
			 <c:if test="${blog.hasError('text')}">
			 <div class="error"><c:out value="${blog.getError('text')}"/></div>
			 </c:if>
			</div>
			
			<div class="formControls">
		  		<span class="formLabel">&nbsp;</span>
		 		<input type="submit" name="method" value="Save">
		 		<input type="submit" name="method" value="Cancel">
		</div>
		</form>
	</body>
</html>