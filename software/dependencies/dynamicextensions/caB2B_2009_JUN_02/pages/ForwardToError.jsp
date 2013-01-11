<%--L
  Copyright Georgetown University.

  Distributed under the OSI-approved BSD 3-Clause License.
  See http://ncip.github.com/cab2b/LICENSE.txt for details.
L--%>

<%-- Jsp Summary                                                                                 	--%>
<%-- ---------------------------------------------------------------------------------------------- --%>

<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c" %>
<%@page import="java.lang.Exception"%>

<html>
	<head>
		<title>Dynamic Extensions</title>
		<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/styleSheet.css"/>
	</head>
	<body  class='bodyStyle'>
		<table width="100%" align="center">
			<tr>
				<td class="formTitle">
					Dynamic Extensions - System Exception
				</td>
			</tr>
			<tr>
				<td class="formMessage" align="left">
					<font color="red"><html:errors/></font>
				</td>
			</tr>
			<tr>
				<td class="formMessage">
					<c:set var="exception" value="${requestScope.exceptionString}"/>
					<jsp:useBean id="exception" type="java.lang.String"/>
					
					<!--
					<div style="border:solid 1px; padding:1px; width:800px; height:400px; overflow:auto;">
						 <%=exception%>
						
						
					</div>
					 -->
					 
					Exception occurred.Please contact system administrator.
				</td>
			</tr>
		</table>
	</body>
</html>
