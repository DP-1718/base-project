<%--
 * edit.jsp
 *
 * Copyright (C) 2013 Universidad de Sevilla
 * 
 * The use of this project is hereby constrained to the conditions of the 
 * TDG Licence, a copy of which you may download from 
 * http://www.tdg-seville.info/License.html
 --%>


<%@page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security"
	uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>



<form:form action="keyword/administrator/edit.do" modelAttribute="keyword">

	<form:hidden path="id" />
	<form:hidden path="version" />
	

	<form:label path="word">
		<spring:message code="keyword.word" />
	</form:label>
	<form:input path="word" />
	<form:errors cssClass="error" path="word" />
	<form:errors></form:errors>
	<br />

	<!-- Buttons -->

	<input type="button" name="Cancel"
		value="<spring:message code="keyword.cancel" />"
		onclick="javascript: window.location.replace('keyword/administrator/list.do');" />
	
	<security:authorize access="hasRole('ADMIN')">
	
	<input type="submit" name="save"
		value="<spring:message code="keyword.save"/>" />
	
	<jstl:if test="${keyword.id != 0}">
	<input type="submit" name="delete"
		value="<spring:message code="keyword.delete" />"
		onclick="return confirm('<spring:message code="keyword.delete.confirm" />')" />
	</jstl:if>
	
	</security:authorize>
	
</form:form>