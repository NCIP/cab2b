<%--L
  Copyright Georgetown University, Washington University.

  Distributed under the OSI-approved BSD 3-Clause License.
  See http://ncip.github.com/cab2b/LICENSE.txt for details.
L--%>

<META HTTP-EQUIV="PRAGMA" CONTENT="NO-CACHE">

<%-- TagLibs --%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c" %>

<%-- Imports --%>
<%@
	page language="java" contentType="text/html"
	import="java.lang.String"
	import="java.lang.Long"
	import="edu.common.dynamicextensions.util.global.Variables"
%>

<%-- Stylesheet --%>
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/styleSheet.css" />
<script src="<%=request.getContextPath()%>/jss/dynamicExtensions.js" type="text/javascript"></script>

<html>
	<head>
		<title><bean:message key="table.heading" /></title>
	</head>
	<body>
		<html:form styleId='formsIndexForm' action='/ApplyFormsIndexAction'>
		<font color="red" ><html:errors/></font>
			<table width='100%' align='center' cellspacing="5" cellspacing="0" border='0'>
				<!--<tr class="formMessage">
					<td>
						<h3>
							<bean:message key="table.heading" />(<bean:message key="application.version"/>: <%=Variables.applicationCvsTag%>)
						</h3>
					</td>
				</tr>-->
				<tr>
					<td class="formMessage" align="left">
						<bean:message key="app.formpage.heading" />
					</td>
				</tr>

				<!-- Messages to be displayed -->
				<tr>
					<td class="formTitle" align="center">
						<html:messages message="true" id="msg">
							<bean:write name="msg" ignore="true"/>
						</html:messages>
					</td>
				</tr>

				<!--  Build new form button -->
				<tr align='left'>
					<td>
						<html:submit styleId="buildForm" property="buildForm" styleClass="buttonStyle" onclick='addFormAction()'>
							<bean:message key="buttons.build.form" />
						</html:submit>
					</td>
				</tr>

				<tr>
					<td>
						<div style="border:solid 1px; padding:1px; height:400px; overflow:auto;">
							<!-- table displaying entities already present in the database -->
							<table class="dataTable" width='100%' cellpadding="4" cellspacing="0" border='1' >
								<thead>
									<tr class="formTitle">
										<th width='5%' align='center'>
											<bean:message key="table.serialNumber" />
										</th>
										<th width="25%" align='left'>
											<bean:message key="table.title" />
										</th>
										<th width="5%" align='left'>
											<bean:message key="app.edit" />
										</th>
										<th width="5%" align='left'>
											<bean:message key="app.editRecords" />
										</th>
										<th width="5%" align='left'>
											<bean:message key="app.viewRecords" />
										</th>
										<th width="15%" align='left'>
											<bean:message key="table.date" />
										</th>
										<th width="10%" align='left'>
											<bean:message key="table.createdBy" />
										</th>
										<th width="10%" align='left'>
											<bean:message key="table.status" />
										</th>
									</tr>
								</thead>

								<c:set var="containerCollection" value="${formsIndexForm.containerCollection}"/>
						 		<jsp:useBean id="containerCollection" type="java.util.Collection"/>

								<c:forEach items="${containerCollection}" var="containerInterface" varStatus="elements">
								<jsp:useBean id="containerInterface" type="edu.common.dynamicextensions.domaininterface.userinterface.ContainerInterface" />
									<tr>
										<td>
											<c:out value='${elements.count}' />&nbsp;
										</td>
										<td>
											<%
	 											String cont_Id = containerInterface.getId().toString();
	 											String target = "setDataEntryOperation('/dynamicExtensions/LoadDataEntryFormAction.do?containerIdentifier=" + cont_Id + "&showFormPreview=false');";
	 										%>
											<html:link href='#' onclick="<%=target%>" >
												&nbsp;<c:out value='${containerInterface.caption}' />
											</html:link>
										</td>
										<td>
											<%
	 											target = "setEditOperationMode('/dynamicExtensions/LoadGroupDefinitionAction.do?containerIdentifier=" + cont_Id + "');";
	 										%>
											<html:link href='#' onclick="<%=target%>" >
												<bean:message key="app.edit" />&nbsp;
											</html:link>
										</td>
										<td>
											<%
	 											target = "loadRecordList('/dynamicExtensions/LoadRecordListAction.do?mode=edit&containerIdentifier=" + cont_Id + "');";
	 										%>
											<html:link href='#' onclick="<%=target%>" >
												<bean:message key="app.editRecords" />&nbsp;
											</html:link>
										</td>
										<td>
											<%
	 											target = "loadRecordList('/dynamicExtensions/LoadRecordListAction.do?mode=view&containerIdentifier=" + cont_Id + "');";
	 										%>
											<html:link href='#' onclick="<%=target%>" >
												<bean:message key="app.viewRecords" />&nbsp;
											</html:link>
										</td>
										<td>
											<c:set var="entityInterface" value="${containerInterface.entity}"/>
	 										<jsp:useBean id="entityInterface" type="edu.common.dynamicextensions.domaininterface.EntityInterface"/>
											<c:out value='${entityInterface.createdDate}'/>&nbsp;
										</td>
										<td><c:out value='admin'/>&nbsp</td>
										<td><c:out value='In Progress' />&nbsp;</td>
									</tr>
								</c:forEach>
							</table>
						</div>
					</td>
				</tr>
				<tr><td></td></tr>
				<tr><td></td></tr>
			</table>
			<html:hidden styleId = "operationMode" property="operationMode"/>
			<input type="hidden" id="dataEntryOperation" name="dataEntryOperation" value="insertParentData"/>
		</html:form>
	</body>
</html>
