<?xml version="1.0" encoding="UTF-8" standalone="no"?>
<jsp:root version="2.0" xmlns:jsp="http://java.sun.com/JSP/Page"
	xmlns:c="http://java.sun.com/jsp/jstl/core"
	xmlns:tiles="http://tiles.apache.org/tags-tiles"
	xmlns:fmt="http://java.sun.com/jsp/jstl/fmt"
	xmlns:fn="http://java.sun.com/jsp/jstl/functions"
	xmlns:spring="http://www.springframework.org/tags"
	xmlns:sec="http://www.springframework.org/security/tags"
	xmlns:util="urn:jsptagdir:/WEB-INF/tags/util">
	<tiles:importAttribute />
	
	<c:if test="${score.username eq markUser}">
	 	<c:set var="dataTheme" value="a"/>
	</c:if>
	
	<sec:authorize access="isAuthenticated()">
		<sec:authentication property="principal.username" var="myUserName" />
		<c:if test="${myUserName == score.username}">
		 	<c:set var="dataTheme" value="e"/>
		</c:if>
	</sec:authorize>
	
	<li data-theme="${dataTheme}">
		${score.position}. ${score.username}<span class="ui-li-count">
		<c:if test="${score.totalScore > 0}">
			+ 
		</c:if>
		${score.totalScore}</span>
	</li>
</jsp:root>