<%--L
  Copyright Georgetown University, Washington University.

  Distributed under the OSI-approved BSD 3-Clause License.
  See http://ncip.github.com/cab2b/LICENSE.txt for details.
L--%>

<%-- Jsp Summary                                                                                 	--%>
<%-- ---------------------------------------------------------------------------------------------- --%>
<%-- @author : Deepti Shelar--%>

<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<link rel="stylesheet" type="text/css" href="<%= request.getContextPath()%>/css/styleSheet.css"/>

<html>
	<head>
		<title>Dynamic Extensions</title>
		 <script language="javascript" src="<%= request.getContextPath()%>/jss/dynamicExtensions.js"></script>
	</head>
	<body>
		<html:form styleId = "controlsForm" action="/LoadFormControlsAction" >
			<font color="red" ><html:errors/></font>
			<table border="0" width="100%" height="100%" >
				<tr class="formRequiredLabelWithoutBorder">
					<td align='center'>
						<bean:message  key="user.saveconfirmation" />
					</td>
				</tr>

				<tr>
					<td align="right">
						<html:button styleClass="actionButton" property="prevButton" onclick='showCreateFormJSP();'>
								<bean:message key="buttons.prev"/>
						</html:button>
						<html:button styleClass="actionButton" property="addControlToFormButton" onclick='addControlToForm();'>
								<bean:message key="buttons.addControlToForm"/>
						</html:button>
						<html:reset styleClass="actionButton" property="cancelButton" onclick='closeWindow();'>
								<bean:message key="buttons.cancel"/>
						</html:reset>
					</td>
				</tr>
			</table>
		</html:form>
 	</body>
</html>
