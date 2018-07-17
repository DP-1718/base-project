<%--
 * action-2.jsp
 *
 * Copyright (C) 2013 Universidad de Sevilla
 * 
 * The use of this project is hereby constrained to the conditions of the 
 * TDG Licence, a copy of which you may download from 
 * http://www.tdg-seville.info/License.html
 --%>

<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl"	uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security" uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>

<h2>
	<spring:message code="message.folder" />: <jstl:out value="${folder.name}" />
</h2>
<display:table name="messages" id="message" requestURI="${requestURI}"
	pagesize="5" class="displaytag">
	
	
	<spring:message code="message.subject" var="subjectHeader"  />
	<display:column property="subject" title="${subjectHeader}" sortable="false" />
	
	<spring:message code="message.body" var="bodyHeader"  />
	<display:column property="body" title="${bodyHeader}" sortable="false" />
	
	<spring:message code="message.priority" var="priorityHeader"  />
	<display:column property="priority" title="${priorityHeader}" sortable="false" />
	
	<spring:message code="message.moment" var="momentHeader"  />
	<display:column property="moment" title="${momentHeader}" sortable="false" format="{0,date,dd/MM/yyyy HH:mm:ss}"/>
	
	<spring:message code="message.sender" var="senderHeader"  />
	<display:column property="sender.name" title="${senderHeader}" sortable="false" />	
	
	<spring:message code="message.recipient" var="recipientHeader"  />
	<display:column property="recipient.name" title="${recipientHeader}" sortable="false" />	
	
	<display:column>
		<a href="message/actor/edit.do?messageId=${message.id}"><spring:message code="message.edit" /></a>
	</display:column>
	
</display:table>

<div>
<a href="message/actor/create.do"><spring:message code="message.create" /></a>
</div>