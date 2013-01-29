<%--L
  Copyright Georgetown University, Washington University.

  Distributed under the OSI-approved BSD 3-Clause License.
  See http://ncip.github.com/cab2b/LICENSE.txt for details.
L--%>

<%-- Jsp Summary                                                                                 	--%>
<%-- ---------------------------------------------------------------------------------------------- --%>
<%-- @author : sujay narkar, chetan patil --%>

<%-- TagLibs --%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/dynamicExtensions.tld" prefix="dynamicExtensions" %>

<%-- Imports --%>
<%@ page language="java" contentType="text/html" %>
<%@ page import="org.apache.struts.action.ActionErrors" %>
<%@ page import="org.apache.struts.action.ActionMessages" %>

<%-- Stylesheet --%>
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/styleSheet.css" />
<link href="<%=request.getContextPath()%>/css/calanderComponent.css" type=text/css rel=stylesheet />

<script src="<%=request.getContextPath()%>/jss/calendarComponent.js"></script>
<script src="<%=request.getContextPath()%>/jss/script.js" type="text/javascript"></script>
<script>var imgsrc="images/";</script>
<script src="<%=request.getContextPath()%>/jss/dynamicExtensions.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/jss/overlib_mini.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/jss/calender.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/jss/calendarComponent.js"></script>
<script src="<%=request.getContextPath()%>/jss/ajax.js"></script>

<c:set var="containerInterface" value="${dataEntryForm.containerInterface}"/>
<jsp:useBean id="containerInterface" type="edu.common.dynamicextensions.domaininterface.userinterface.ContainerInterface"/>

<c:set var="showFormPreview" value="${dataEntryForm.showFormPreview}"/>
<jsp:useBean id="showFormPreview" type="java.lang.String"/>

<c:set var="errorList" value="${dataEntryForm.errorList}"/>
<jsp:useBean id="errorList" type="java.util.List"/>

<c:set var="dataEntryOperation" value="${dataEntryForm.dataEntryOperation}"/>
<jsp:useBean id="dataEntryOperation" type="java.lang.String"/>

<c:set var="recordIdentifier123" value="${dataEntryForm.recordIdentifier}" />
<jsp:useBean id="recordIdentifier123" type="java.lang.String"/>

<c:set var="mode" value="${dataEntryForm.mode}" />
<jsp:useBean id="mode" type="java.lang.String"/>

<c:set var="isTopLevelEntity" value="${dataEntryForm.isTopLevelEntity}" />
<jsp:useBean id="isTopLevelEntity" type="java.lang.Boolean"/>

<html>
	<head>
		<title><bean:message key="table.heading" /></title>
	</head>

	<body onload="loadPreviewForm()">
		<html:form styleId="dataEntryForm" action="/ApplyDataEntryFormAction" enctype="multipart/form-data" method="post">
			<div id="overDiv" style="position:absolute; visibility:hidden; z-index:1000;"></div>
		
			<c:choose>
				<c:when test='${showFormPreview == "true"}'>
					<table valign="top" style="border-right:1px" align='center' width='100%' height="500" border='0' cellspacing="0" cellpadding="0" class="tbBorders1" >
				</c:when>
				<c:otherwise>
					<table valign="top" align='center' width='100%' height="500" border='0' cellspacing="0" cellpadding="0">
				</c:otherwise>
			</c:choose>
				<!-- Main Page heading -->
				<tr>
					<td class="formFieldNoBorders">
						<c:if test='${showFormPreview == "true"}'> 
							<bean:message key="app.title.MainPageTitle" />
						</c:if>
					</td>
				</tr>
	 
				<tr valign="top">
					<td>
						<table valign="top" summary="" align='center' width='100%' cellspacing="0" cellpadding="3">
							<c:if test='${showFormPreview == "true"}'> 
								<tr valign="top">
									<td height="20" class="tabMenuItem" onmouseover="changeMenuStyle(this,'tabMenuItemOver'),showCursor()" onmouseout="changeMenuStyle(this,'tabMenuItem'),hideCursor()" onclick="alert('This page is still under construction and will be available in the next release');">
										<bean:message key="app.title.DefineGroupTabTitle" />
									</td>
									<td height="20" class="tabMenuItem" onmouseover="changeMenuStyle(this,'tabMenuItemOver'),showCursor()" onmouseout="changeMenuStyle(this,'tabMenuItem'),hideCursor()" onclick="showFormDefinitionPage()">
										<bean:message key="app.title.DefineFormTabTitle" />
									</td>
									<td height="20" class="tabMenuItem" onmouseover="changeMenuStyle(this,'tabMenuItemOver'),showCursor()" onmouseout="changeMenuStyle(this,'tabMenuItem'),hideCursor()" onclick="backToControlForm()">
										<bean:message key="app.title.BuildFormTabTitle" />
									</td>
									<td height="20" class="tabMenuItemSelected"  >
										<bean:message key="app.title.PreviewTabTitle" />
									</td>
									<td width="50%" class="tabMenuSeparator" colspan="3">&nbsp;</td>
								</tr>
							</c:if>
							
							<tr valign="top">
								<td colspan="7">
									<table align='center' width='100%'>
										<tr>
											<%
												if(errorList.size() != 0)
												{
											%>
												<td align="center" class="formTitleError">
													<c:forEach items="${errorList}" var="error">
														<jsp:useBean id="error" type="java.lang.String"/>
															<c:out value="${error}"/><br />
													</c:forEach>
											<%
												} else {
											%>
												<td align="center" class="formTitle">
											<%
												}
											%>
													<logic:messagesPresent message="true">
														<ul>
															<html:messages id="msg" message="true"> 
																<li><bean:write name="msg"/></li>
															</html:messages>
														</ul>
													</logic:messagesPresent>
											</td>
										</tr>
										<tr><td>&nbsp;</td></tr>
										<tr>
											<td>
												<dynamicExtensions:dynamicUIGenerator containerInterface="<%=containerInterface%>" />
											</td>
										</tr>
									</table>
								</td>
							</tr>
							<tr>
								<td valign="top" colspan="7">
									<table cellpadding="4" cellspacing="5" border="0" align='center'>
										<tr height="5"></tr>
										<tr>
											<td align='center'>						
												<c:choose>
													<c:when test='${showFormPreview=="true"}'>
														<html:submit styleClass="actionButton" onclick="showParentContainerInsertDataPage()">
															<bean:message key="buttons.back" />
														</html:submit>
													</c:when>
													<c:otherwise>
														<html:hidden styleId='isEdit' property="isEdit" value=""/>
														
														<c:if test='${(isTopLevelEntity=="false")}'>
															<html:button styleClass="actionButton" property="ok" onclick="showParentContainerInsertDataPage()" disabled="<%=isTopLevelEntity %>">
																<bean:message key="buttons.submit" />
															</html:button>
														</c:if>
																												
														<c:if test='${(mode=="edit") && (isTopLevelEntity=="true")}'>
															<html:submit styleClass="actionButton" onclick="setInsertDataOperation()">
																<bean:message key="buttons.submit" />
															</html:submit>
															
												<!--  		<c:if test='${(recordIdentifier123 != "")}'>	
																<html:button styleClass="actionButton" property="cancel" onclick="setDeleteDataOperation()">
																	<bean:message key="buttons.delete1" />
																</html:button>
															</c:if>		-->	
															
														</c:if>
														
														<c:if test='${!((mode=="view") && (isTopLevelEntity=="false"))}'>
															<html:button styleClass="actionButton" property="cancel" onclick="cancelInsertData()">
																<bean:message key="buttons.cancel" />
															</html:button>
														</c:if>
														
													</c:otherwise>
												</c:choose>	
											</td>
										</tr>
									</table>
								</td>
							</tr>
						</table>
					</td>
				</tr>
			</table>

			<html:hidden styleId='recordIdentifier' property="recordIdentifier"/>
			<html:hidden styleId='entitySaved' property="entitySaved"/>
			<input type="hidden" id="childContainerId" name="childContainerId" value=""/>
			<input type="hidden" id="childRowId" name="childRowId" value=""/>
			<input type="hidden" id="dataEntryOperation" name="dataEntryOperation" value="<%=dataEntryOperation%>"/>
			<input type="hidden" id="showFormPreview" name="showFormPreview" value="<%=showFormPreview%>"/>
			<input type="hidden" id="mode" name="mode" value="<%=mode%>"/>
		</html:form>
	</body>
</html>
