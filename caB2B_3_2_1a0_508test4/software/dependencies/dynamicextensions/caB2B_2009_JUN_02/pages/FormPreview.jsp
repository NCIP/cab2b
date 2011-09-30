<%-- Jsp Summary                                                                                 	--%>
<%-- ---------------------------------------------------------------------------------------------- --%>
<%-- @author : sujay narkar, chetan_patil --%>

<%-- TagLibs --%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c" %>

<%-- Imports --%>
<%@
	page language="java" contentType="text/html"
%>

<%-- Stylesheet --%>
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/styleSheet.css"/>
<link href="<%=request.getContextPath()%>/css/calanderComponent.css" type=text/css rel=stylesheet/>

<script src="<%=request.getContextPath()%>/jss/calendarComponent.js"></script>
<script src="<%=request.getContextPath()%>/jss/script.js" type="text/javascript"></script>
<script>var imgsrc="images/";</script>
<script src="<%=request.getContextPath()%>/jss/dynamicExtensions.js" type="text/javascript"></script>

<c:set var="containerInterface" value="${previewForm.containerInterface}"/>
<jsp:useBean id="containerInterface" type="edu.common.dynamicextensions.domaininterface.userinterface.ContainerInterface"/>

<html>
	<head>
		<title><bean:message key="table.heading" /></title>
	</head>

	<body onload="loadPreviewForm()">
		<html:form styleId="previewForm" action="/LoadFormPreviewAction" enctype="multipart/form-data" >
			<font color="red" ><html:errors/></font>
			<html:hidden property="entitySaved"/>

			<div id="overDiv" style="position:absolute; visibility:hidden; z-index:1000;"></div>

			<table valign="top" align='left' width='100%' height="100%" border='0' cellspacing="0" cellpadding="0" class="tbBorders1">
				<!-- Main Page heading -->
				<tr><td class="formFieldNoBorders"><bean:message key="app.title.MainPageTitle"/></td></tr>
				<tr valign="top">
					<td>
						<table valign="top" summary="" align='left' width='100%' cellspacing="0" cellpadding="3">
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
							<tr valign="top">
								<td colspan="7">
									<table align='center' width='80%'>
										<tr >
											<td align="center" class="formTitle">
												<logic:messagesPresent message="true">
													<html:messages message="true" id="msg">
														<bean:write name="msg" ignore="true"/>
													</html:messages>
												</logic:messagesPresent>
											</td>
										</tr>
										<tr><td>&nbsp;</td></tr>
										<tr>
											<td>
												<table summary="" cellpadding="3" cellspacing="0" align='center' width='100%'>
													<tr>
														<td class="formMessage" colspan="3">
															<c:out value="${containerInterface.requiredFieldIndicatior}" escapeXml="false" />&nbsp;
															<c:out value="${containerInterface.requiredFieldWarningMessage}" escapeXml="false" />
														</td>
													</tr>
													<tr>
														<td class='formTitle' colspan="3" align='left'>
															<c:set var="entityInterface" value="${containerInterface.entity}" />
															<jsp:useBean id="entityInterface" type="edu.common.dynamicextensions.domaininterface.EntityInterface" />
															<c:out value="${entityInterface.name}" escapeXml="false" />
														</td>
													</tr>
													<c:forEach items="${containerInterface.controlCollection}" var="controlInterface">
													<jsp:useBean id="controlInterface" type="edu.common.dynamicextensions.domaininterface.userinterface.ControlInterface" />
														<tr>
															<td class="formRequiredNotice" width="2%">
																&nbsp;
															</td>
															<td class="formRequiredLabel" width="20%">
																<c:out value="${controlInterface.caption}"/>
															</td>
															<td class="formField">
																<% String generateHTMLStr = controlInterface.generateHTML(); %>
																<% pageContext.setAttribute("generateHTMLStr", generateHTMLStr); %>
																<c:out value="${generateHTMLStr}" escapeXml="false" />
															</td>
														</tr>
													</c:forEach>
												</table>
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
											<td align='left'>
												<input type=button property="backToPrevious" styleClass="actionButton" onclick="backToControlForm()">
													<bean:message  key="buttons.backToPrevious"/>
												</input>
											</td>
											<td align='right'>
												<html:submit styleClass="actionButton" onclick="addDynamicData()">
													<bean:message key="buttons.submit"/>
												</html:submit>
											</td>
										</tr>
									</table>
								</td>
							</tr>
						</table>
					</td>
				</tr>
			</table>
		</html:form>
	</body>
</html>
