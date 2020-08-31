<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE html>
<html>
	<head>
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
		<title>Register</title>
	</head>
	
	<body>
		${requestScope.loggedIn}
		<p><p>
		<h1>Register your account</h1>
		<p>Here you can fill out a registration form to be able to log in to the site and write
		<br>new blogs. :)</p>
		
		<form action="register" method="post" accept-charset="UTF-8">
		
		<div>
			<div>
			<span class="formLabel">Nickname:</span><input name="nick" type="text" size=20 value="${form.getNick()}">
			</div>
  			<c:if test="${form.hasError('nick')}">
		 	<div class="error"><c:out value="${form.getError('nick')}"/></div>
		 	</c:if>
		</div>
  			
  		<div>
  			<span class="formLabel">First name:</span><input name="fn" type="text" size=20 value="${form.getFirstName()}">
  			<c:if test="${form.hasError('fn')}">
		 	<div class="error"><c:out value="${form.getError('fn')}"/></div>
		 	</c:if>
		</div>
  			
  		<div>
  			<span class="formLabel">Last name:</span><input name="ln" type="text" size=20 value="${form.getLastName()}">
  			<c:if test="${form.hasError('ln')}">
		 	<div class="error"><c:out value="${form.getError('ln')}"/></div>
		 	</c:if>
		</div>
  			
  		<div>
  			<span class="formLabel">E-mail:</span><input name="email" type="text" size=50 value="${form.getEmail()}">
  			<c:if test="${form.hasError('email')}">
		 	<div class="error"><c:out value="${form.getError('email')}"/></div>
		 	</c:if>
		</div>
  			
  		<div>
  			<span class="formLabel">Password:</span><input name="password" type="password" size=30>
  			<c:if test="${form.hasError('password')}">
		 	<div class="error"><c:out value="${form.getError('password')}"/></div>
		 	</c:if>
		</div>
		
		<div class="formControls">
  			<input type="submit" name="method" value="Submit">
  			<input type="submit" name="method" value="Cancel">
  		</div>
  			
		</form>
	</body>
</html>