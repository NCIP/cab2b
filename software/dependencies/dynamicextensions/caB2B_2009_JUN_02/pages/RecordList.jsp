<%--L
  Copyright Georgetown University.

  Distributed under the OSI-approved BSD 3-Clause License.
  See http://ncip.github.com/cab2b/LICENSE.txt for details.
L--%>

<%-- TagLibs --%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/dynamicExtensions.tld" prefix="dynamicExtensions" %>
<%@
	page language="java" contentType="text/html"
%>

<%-- Stylesheet --%>
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/styleSheet.css" />
<script src="<%=request.getContextPath()%>/jss/dynamicExtensions.js" type="text/javascript"></script>

<c:set var="containerIdentifier" value="${recordListForm.containerIdentifier}"/>
<jsp:useBean id="containerIdentifier" type="java.lang.String"/>

<c:set var="entityRecordList" value="${recordListForm.entityRecordList}"/>
<jsp:useBean id="entityRecordList" type="java.util.List"/>

<c:set var="mode" value="${recordListForm.mode}"/>
<jsp:useBean id="mode" type="java.lang.String"/>

<html>
	<head>
		<title><bean:message key="table.heading" /></title>
	</head>
	<body>
		<html:form styleId='recordListForm' action='/ApplyRecordListAction'>
		<font color="red" ><html:errors/></font>
			<table width='100%' align='center' cellspacing="5" cellspacing="0" border='0'>
				<!-- Messages to be displayed -->
				<tr>
					<td class="formTitle" align="center">
						<html:messages message="true" id="msg">
							<bean:write name="msg" ignore="true"/>
						</html:messages>
					</td>
				</tr>

				<tr>
					<td>
						<dynamicExtensions:displayRecordList containerIdentifier="<%=containerIdentifier%>" entityRecordList="<%=entityRecordList%>" mode="<%=mode%>" />
					</td>
				</tr>
			</table>
			<input type="hidden" id="dataEntryOperation" name="dataEntryOperation" value="insertParentData"/>
		</html:form>
	</body>
</html>
