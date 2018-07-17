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



<form:form action="message/actor/edit.do" modelAttribute="msg">

	<form:hidden path="id" />
	<form:hidden path="version" />
	<jstl:if test="${msg.id == 0}">
	<form:hidden path="messageFolder" />
	</jstl:if>
	<form:hidden path="sender" />
	<jstl:if test="${msg.id != 0}">
		<form:hidden path="subject" />
		<form:hidden path="body" />
		<form:hidden path="moment" />
		<form:hidden path="recipient" />
		<form:hidden path="priority" />
	</jstl:if>

	<jstl:if test="${msg.id == 0}">
	<form:label path="recipient">
		<spring:message code="message.recipient" />
	</form:label>
	<form:select path="recipient">
		<form:option value="0" label="------" />
		<form:options items="${recipients}"  itemValue="id" itemLabel="name" />
	</form:select>
	<form:errors cssClass="error" path="recipient" />
	<br />

	<form:label path="subject">
		<spring:message code="message.subject" />
	</form:label>
	<form:input path="subject"/>
	<form:errors cssClass="error" path="subject" />
	<br />

	<form:label path="body">
		<spring:message code="message.body" />
	</form:label>
	<form:textarea path="body" />
	<form:errors cssClass="error" path="body" />
	<br />
	
	<form:label path="moment">
		<spring:message code="message.moment" />
	</form:label>
	<form:input path="moment" readonly="true"/>
	<form:errors cssClass="error" path="moment" />
	<br />
	
	<form:label path="priority">
		<spring:message code="message.priority" />
	</form:label>
	<form:select path="priority">
		<form:option value="-1" label="LOW" />
		<form:option value="0" label="NEUTRAL" />
		<form:option value="1" label="HIGH" />
	</form:select>
	<form:errors cssClass="error" path="priority" />
	<br />
	</jstl:if>
	
	<jstl:if test="${msg.id != 0}">
		<form:label path="messageFolder">
			<spring:message code="message.folder" />
		</form:label>
		<form:select path="messageFolder">
			<form:option value="0" label="------" />
			<form:options items="${folders}"  itemValue="id" itemLabel="name" />
		</form:select>
		<form:errors cssClass="error" path="messageFolder" />
	<br />
	</jstl:if>


	<!-- Buttons -->

	<input type="button" name="Cancel"
		value="<spring:message code="message.cancel" />"
		onclick="javascript: window.location.replace('${cancelURI}');" />
	
	<jstl:if test="${msg.id == 0}">
	<input type="submit" name="send" 
		value="<spring:message code="message.send"/>" />
	</jstl:if>
	<jstl:if test="${msg.id != 0}">
	<input type="submit" name="move" 
		value="<spring:message code="message.move"/>" />

	<input type="submit" name="delete"
		value="<spring:message code="message.delete" />"
		onclick="return confirm('<spring:message code="message.delete.confirm" />')" />
	</jstl:if>
</form:form>