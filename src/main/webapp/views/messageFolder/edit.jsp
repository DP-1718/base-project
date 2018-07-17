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



<form:form action="messageFolder/actor/edit.do" modelAttribute="messageFolder">

	<form:hidden path="id" />
	<form:hidden path="version" />
	<form:hidden path="modificable" />
	<form:hidden path="messages" />
	<form:hidden path="actor" />


	<form:label path="name">
		<spring:message code="messageFolder.name" />
	</form:label>
	<form:input path="name" />
	<form:errors cssClass="error" path="name" />

	<br />

	


	<!-- Buttons -->

	<input type="button" name="Cancel"
		value="<spring:message code="messageFolder.cancel" />"
		onclick="javascript: window.location.replace('messageFolder/actor/list.do');" />
	
	<input type="submit" name="save"
		value="<spring:message code="messageFolder.save"/>" />

	<jstl:if test="${messageFolder.id != 0}">
	<input type="submit" name="delete"
		value="<spring:message code="messageFolder.delete" />"
		onclick="return confirm('<spring:message code="messageFolder.delete.confirm" />')" />
	</jstl:if>
</form:form>