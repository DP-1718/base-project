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

<display:table name="messageFolders" id="row" requestURI="${requestURI}"
	pagesize="5" class="displaytag">
	
	
	<spring:message code="messageFolder.name" var="nameHeader"  />
	<display:column property="name" title="${nameHeader}" sortable="false" />
	
	<display:column>
		<a href="message/actor/list.do?messageFolderId=${row.id}"><spring:message code="messageFolder.messages.view"/></a>
	</display:column>
	
	<display:column>
		<jstl:if test="${row.modificable == true}">
			<a href="messageFolder/actor/edit.do?messageFolderId=${row.id}"> <spring:message code="messageFolder.edit" /></a>
		</jstl:if>
	</display:column>
	
	
	
				
</display:table>