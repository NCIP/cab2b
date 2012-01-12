<%-- @author : Preeti Munot --%>

<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c" %>
<%@page import="java.util.List" %>

<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/styleSheet.css" />

<script src="<%=request.getContextPath()%>/jss/script.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/jss/ajax.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/jss/dynamicExtensions.js" type="text/javascript">
</script>

<html>
	<head>
		<title>Dynamic Extensions</title>
	</head>

	<body onload="initDefineGroupForm();">
		<!-- Menu tabs -->
		<c:set var="groupList" value="${groupForm.groupList}"/>
		<jsp:useBean id="groupList" type="java.util.List"/>

		<c:set var="createGroupAs" value="${groupForm.createGroupAs}"/>
		<jsp:useBean id="createGroupAs" type="java.lang.String"/>

		<c:set var="operationMode" value="${groupForm.operationMode}"/>
		<jsp:useBean id="operationMode" type="java.lang.String"/>

		<html:form styleId = "groupForm"  action="/LoadGroupDefinitionAction">

		<input type="hidden" id="groupOperation" name="groupOperation" value=""/>

	        <table valign="top" align='right' width='100%' height="100%" border='0' cellspacing="0" cellpadding="0" >
		    	<!-- Main Page heading -->
		        <tr valign="top" style = "border-bottom:0px">
		         	<td style="border-left:0px;border-bottom:0px" class="formFieldNoBorders" >
		         		<bean:message key="app.title.MainPageTitle" />
		         	</td>
		        </tr>
				<tr>
					<td style = "border-left:0px;border-top:0px;border-bottom:0px" valign="top">
						<table valign="top" align='left' width='100%' height='100%' cellspacing="0" cellpadding="3" class="tbBordersAllbordersBlack" >
							<tr valign="top">
								<td height="20" class="tabMenuItemSelected" >
									<bean:message key="app.title.DefineGroupTabTitle" />
							   	</td>
							   	<td height="20" class="tabMenuItem" >
									<bean:message key="app.title.DefineFormTabTitle" />
							   	</td>
							   	<td height="20" class="tabMenuItem"  >
									<bean:message key="app.title.BuildFormTabTitle" />
							   	</td>
							   	<td height="20" class="tabMenuItem" >
									<bean:message key="app.title.PreviewTabTitle" />
							   	</td>
							   	<td width="50%" class="tabMenuSeparator" colspan="3">&nbsp;</td>
							</tr>
							<tr height="100%" valign="top" >
								<td height='100%' colspan="7">
									<table valign="top" cellspacing="0" cellpadding="4" align="left" width="100%" height='100%' class="tbBordersAllbordersBlack">
										<tr valign="top">
											<td class="formMessage" colspan="3">
												<bean:message key="app.requiredMessage"/>
											</td>
										</tr>
										<tr valign="top">
											<td class="formMessage" colspan="3">
												<font color="red" ><html:errors/></font>
											</td>
										</tr>
										<tr valign="top">
											<td class="formMessage" colspan="3">&nbsp;</td>
										</tr>

										<tr valign="top" class="rowWithBottomPadding">
											<td class="formRequiredNoticeWithoutBorder" width="2%">*</td>
											<td class="formRequiredLabelWithoutBorder" width="12%">
												<bean:message key="eav.att.GroupType"/> :
											</td>
											<td class="formFieldWithoutBorder" align="left">
												<html:radio styleId="createGroupAs" property="createGroupAs" value="NewGroup" onclick="changeGroupSource(this)">
													<bean:message key="eav.att.NewGroup"/>
												</html:radio>
												<html:radio styleId="createGroupAs" property="createGroupAs" value="ExistingGroup" onclick="changeGroupSource(this)">
													<bean:message key="eav.att.ExistingGroup"/>
												</html:radio>
											</td>
										</tr>
										<!--
										<tr>
											<td>
												<html:hidden styleId="createGroupAs" property="createGroupAs" value="<%=createGroupAs%>"/>
											</td>
										<tr>
										-->
										<tr valign="top" >
											<td colspan="3" valign="top" class="cellWithNoLeftBottomPadding">
												<!-- <div id="groupDetailsDiv"></div>-->
												<span id="groupDetailsDiv">
												</span>
											</td>
										</tr>
										<tr>
											<td height='100%' class="formFieldSized3" colspan="3">&nbsp;</td>
										</tr>
									</table>
								</td>
							</tr>
							<tr height='100%' valign="top">
								<td height='100%' colspan="2" align="left">
									<html:button styleClass="actionButton" property="saveButton" onclick="saveGroup()" onkeydown="saveGroupOnKeyDown(event)">
										<bean:message key="buttons.save" />
	 								</html:button>
									<html:button styleClass="actionButton" property="cancelButton" onclick="showHomePageFromCreateGroup()">
										<bean:message key="buttons.cancel" />
	 								</html:button>
								</td>
								<td height='100%' colspan="5" align="right">
									<html:button styleClass="actionButton" property="nextButton" onclick="showDefineFormJSP()" >
										<bean:message  key="buttons.next" />
	 								</html:button>
								</td>
							</tr>
						</table>
					</td>
				</tr>
			</table>
			<div id="NewGroupDiv" style="display:none">
				<table valign="top" cellspacing="0" cellpadding="4" width="100%" height='100%'>
					<tr valign="top" class="rowWithBottomPadding">
						<td class="formRequiredNoticeWithoutBorder" width="2%">*</td>
						<td class="formRequiredLabelWithoutBorder" width="12%">
							<label for="groupName">
								<bean:message key="eav.att.GroupTitle"/> :
							</label>
						</td>
						<td class="formFieldWithoutBorder">
							<html:text styleClass="formDateSized" styleId="tempgroupNameText" property="tempgroupNameText" />
						</td>
					</tr>

					<tr valign="top" class="rowWithBottomPadding">
						<td class="formRequiredNoticeWithoutBorder" width="2%" >&nbsp;</td>
						<td class="formRequiredLabelWithoutBorder" width="12%">
							<label for="groupDescription">
								<bean:message key="eav.att.Description"/> :
							</label>
						</td>
						<td>
							<html:textarea styleClass="formFieldSmallSized" rows="3" cols="28" styleId="tempgroupDescription" property="tempgroupDescription" value=""/>
						</td>
					 </tr>
				</table>
			</div>
			<div id="ExistingGroupDiv" style="display:none">
				<table valign="top" cellspacing="0" cellpadding="4" align="left" width="100%" height='100%'>
					<tr valign = "top"  class="rowWithBottomPadding">
						<td class="formRequiredNoticeWithoutBorder" width="2%">&nbsp;</td>
						<td class="formRequiredLabelWithoutBorder" width="12%">
							<label for="groupName">
								<bean:message key="eav.att.GroupTitle"/> :
							</label>
						</td>
						<td>
							<html:select styleId="tempgroupName" styleClass="formFieldVerySmallSized" property="tempgroupName" onchange="groupSelected(this)">
								<html:options collection="groupList" labelProperty="name" property="value" />
							</html:select>
						</td>
					</tr>
					<tr valign="top" class="rowWithBottomPadding">
						<td class="formRequiredNoticeWithoutBorder" width="2%" >&nbsp;</td>
						<td class="formRequiredLabelWithoutBorder" width="12%">
							<label for="groupDescription">
								<bean:message key="eav.att.Description"/> :
							</label>
						</td>
						<c:choose>
							<c:when test='${(operationMode=="EditForm")}'>
								<td>
									<html:textarea styleClass="formFieldSmallSized"  rows = "3" cols="28" styleId="tempgroupDescription"  property="tempgroupDescription"/>
								</td>
							</c:when>
							<c:otherwise>
								<td>
									<html:textarea styleClass="formFieldSmallSized"  rows = "3" cols="28" styleId="tempgroupDescription"  property="tempgroupDescription" readonly="true"/>
								</td>
							</c:otherwise>
						</c:choose>
					</tr>
				</table>
			</div>

			<input type="hidden" id="createGroupAsHidden" name="createGroupAsHidden" value="<%=createGroupAs%>" />
			<html:hidden property='operationMode' value="<%=operationMode%>"/>
		</html:form>
	</body>
</html>
