<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
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
		   width: 150px;
                   font-weight: bold;
		   text-align: right;
                   padding-right: 10px;
		}
		.formControls {
		  margin-top: 10px;
		}
		
		textarea {
			margin-left: 50px;
			margin-top: 10px;
		}
		</style>
	</head>

  <body>
	${requestScope.loggedIn}
	<p><p>
	
	<a href="${pageContext.request.contextPath}/servleti/main">Back to index</a>
	<p><p>
	
  <c:choose>
    <c:when test="${blogEntry == null}">
      No entry!
    </c:when>
    <c:otherwise>
      <h1><c:out value="${blogEntry.title}"/></h1>
      <p><c:out value="${blogEntry.text}"/></p>
      
      <c:if test="${sessionScope['current.user.nick'] eq blogEntry.getCreator().getNick()}">
      	<p><a href="edit?id=${blogEntry.id}">Edit</a></p>
      </c:if>
      
      <c:if test="${!blogEntry.comments.isEmpty()}">
      <ul>
      <c:forEach var="e" items="${blogEntry.comments}">
        <li><div style="font-weight: bold">[User=<c:out value="${e.usersEMail}"/>] <c:out value="${e.postedOn}"/></div><div style="padding-left: 10px;"><c:out value="${e.message}"/></div></li>
      </c:forEach>
      </ul>
      </c:if>
      
      <form action="${pageContext.request.contextPath}/servleti/addComment?id=${blogEntry.id}" method="post" accept-charset="UTF-8">
      
      <c:choose>
      	<c:when test="${empty sessionScope['current.user.id']}">
      		<div>
      			<span class="formLabel">Contact e-mail:</span><input type="text" name="email" size="30" value="${form.getUsersEMail()}">
      		<c:if test="${form.hasError('email')}">
      			<div class="error">${form.getError('email')}</div>
      		</c:if>
      		</div>
      		<p><p>
      	</c:when>
      	
      	<c:otherwise>
      		<div>
      			<span class="formLabel">Contact e-mail:</span><input type="text" readonly="readonly" name="email" size="30" value="${sessionScope['current.user.email']}">
      		</div>
      		<p><p>
      	</c:otherwise>
      </c:choose>
      
      <div>
      	<span class="formLabel">Type comment:</span><br>
      	<textarea rows="10" cols="50" name="comment" autofocus="autofocus" placeholder="Type your comment here...">${form.getMessage()}</textarea>
      	<c:if test="${form.hasError('comment')}">
      		<div class="error">${form.getError('comment')}</div>
      	</c:if>
      </div>
      
      <div class="formControls">
      	<input type="submit" value="Add Comment">
      </div>
      </form>
      
    </c:otherwise>
  </c:choose>

  </body>
</html>
