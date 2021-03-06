<%--
 * list.jsp
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

<display:table name="configurationEntries" id="row" requestURI="${requestURI}" pagesize="5" class="displaytag">

	<spring:message code="configuration.name" var="nameHeader"  />
	<display:column property="name" title="${nameHeader}" />
	
	<spring:message code="configuration.value" var="valueHeader"  />
	<display:column property="value" title="${valueHeader}" />
	
	
	<security:authorize access="hasRole('ADMIN')">
						
					<spring:message var="editHeader" code="common.action.edit" />
					<display:column title="${editHeader}">
						<a href="configuration/administrator/edit.do?configurationId=${row.id}"> <spring:message code="common.action.edit" />
						</a>
				</display:column>
		
	</security:authorize>
</display:table>