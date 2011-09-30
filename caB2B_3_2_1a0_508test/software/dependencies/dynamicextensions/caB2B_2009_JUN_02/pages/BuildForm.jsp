<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/dynamicExtensions.tld" prefix="dynamicExtensions" %>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c" %>

<%@page import="java.util.List"%>
<%@page import="edu.common.dynamicextensions.ui.webui.util.ControlInformationObject"%>
<%
	List controlInformationObjectList1 = (List)request.getAttribute("controlsList");
%>
<html>
	<!-- HTML Head section -->
	<head>
		<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/styleSheet.css"/>
		<link href="<%=request.getContextPath()%>/css/calanderComponent.css" type="text/css" rel=stylesheet/>
		<link rel="STYLESHEET" type="text/css" href="dhtml_comp/css/dhtmlXGrid.css"/>

		<script src="<%=request.getContextPath()%>/jss/dynamicExtensions.js" type="text/javascript"></script>
		<script src="<%=request.getContextPath()%>/jss/script.js" type="text/javascript"></script>
		<script src="<%=request.getContextPath()%>/jss/overlib_mini.js" type="text/javascript"></script>
		<script src="<%=request.getContextPath()%>/jss/calender.js" type="text/javascript"></script>
		<script src="<%=request.getContextPath()%>/jss/calendarComponent.js"></script>
		<script language="JavaScript" type="text/javascript" src="<%=request.getContextPath()%>/jss/ajax.js"></script>

		<script src="<%=request.getContextPath()%>/dhtml_comp/jss/dhtmlXCommon.js"></script>
		<script src="<%=request.getContextPath()%>/dhtml_comp/jss/dhtmlXGrid.js"></script>
		<script src="<%=request.getContextPath()%>/dhtml_comp/jss/dhtmlXGrid_nxml.js"></script>
		<script src="<%=request.getContextPath()%>/dhtml_comp/jss/dhtmlXGrid_drag.js"></script>
		<script src="<%=request.getContextPath()%>/dhtml_comp/jss/dhtmlXGridCell.js"></script>

		<title>Dynamic Extensions</title>
		<script language="JavaScript" type="text/javascript">
			function initCancelOperation()
			{
				var addControlBtnCaption = '<bean:message  key="buttons.addControlToForm"/>';
				var addControlFormTitle = '<bean:message key="app.title.addAttributes"/>';
				cancelControlOpern(addControlBtnCaption,addControlFormTitle);
			}

			function initGridView()
			{
				mygrid = new dhtmlXGridObject('gridbox');
				mygrid.setImagePath("dhtml_comp/imgs/");
				mygrid.setHeader("#,Name,Type");
				
				if(navigator.userAgent.indexOf("Safari")!= -1 )
				{
					mygrid.setInitWidths("10,70,70");
					mygrid.enableAutoHeigth(true,410);
				}
				else
				{
					mygrid.setInitWidthsP("6,47,47");
					mygrid.enableAutoHeigth(true,510);
				}
				
			
				mygrid.setColAlign("center,left,left")
				mygrid.setColTypes("ch,ed,ed");

				mygrid.enableMultiselect(true)
				mygrid.enableDragAndDrop(true);
				mygrid.setDropHandler(dropFn);
				mygrid.setOnRowSelectHandler(controlSelected);
				mygrid.init();
				loadGridData();
			}

			function loadGridData()
			{
				<%
					if(controlInformationObjectList1!=null)
					{
						int noOfControls = controlInformationObjectList1.size();
						for(int i=0;i<noOfControls;i++)
						{
							ControlInformationObject controlInformationObj = (ControlInformationObject)controlInformationObjectList1.get(i);
							if(controlInformationObj!=null)
							{
								String identifier = controlInformationObj.getIdentifier();
								String gridContentStr = " ,"  + controlInformationObj.getControlName() + " ," + controlInformationObj.getControlType();
								%>
									mygrid.addRow(<%=identifier%>,'<%=gridContentStr%>');
								<%
							}
						}
					}
				%>
			}
		</script>
	</head>

	<!-- Initializations -->
	<c:set var="toolsList" value="${controlsForm.toolsList}"/>
	<jsp:useBean id="toolsList" type="java.util.List"/>

	<c:set var="htmlFile" value="${controlsForm.htmlFile}"/>
	<jsp:useBean id="htmlFile" type="java.lang.String"/>

	<c:set var="rootName" value="${controlsForm.rootName}"/>
 	<jsp:useBean id="rootName" type="java.lang.String"/>

	<c:set var="userSelectedTool" value="${controlsForm.userSelectedTool}"/>
 	<jsp:useBean id="userSelectedTool" type="java.lang.String"/>

	<c:set var="controlInformationObjectList" value="${controlsForm.childList}"/>
	<jsp:useBean id="controlInformationObjectList" type="java.util.List"/>

	<c:set var="selectedControlCaption" value="${controlsForm.selectedControlCaption}"/>
 	<jsp:useBean id="selectedControlCaption" type="java.lang.String"/>

	<!-- Main HTML Code -->
  	<body onload="initBuildForm();initGridView()">
		<html:form styleId="controlsForm" action="/LoadFormControlsAction" method="post" enctype="multipart/form-data">
			<font color="red"><html:errors/></font>

			<%
	  	    	int generator = 0;
	  	    %>
			<table valign='top' style='border-right:0px' border='0' align='right' width='100%' height="100%" cellspacing="0" cellpadding="0">
				<!-- Main Page heading -->
		        <tr style="border-bottom:0px">
		        	<td class="tbBordersAllbordersNone">&nbsp;</td>
		         	<td class="formFieldNoBorders">
		         		<bean:message key="app.title.MainPageTitle"/>
		         	</td>
		        </tr>

		        <tr valign="top">
					<td class="tbBordersAllbordersNone">&nbsp;</td>
		     		<td class="tbBordersAllbordersNone" valign="top">
					  	<table valign="top" summary="" align='left' width='100%' height="100%" cellspacing="0" cellpadding="3" class="tbBordersAllbordersBlack" >
							<!-- tabs start -->
							<tr valign="top" >
								<td class="tabMenuItem" >
									<bean:message key="app.title.DefineGroupTabTitle" />
								</td>
							   	<td class="tabMenuItem" >
									<bean:message key="app.title.DefineFormTabTitle" />
							   	</td>
							   	<td class="tabMenuItemSelected" >
									<bean:message key="app.title.BuildFormTabTitle" />
							   	</td>
							   	<td class="tabMenuItem" >
									<bean:message key="app.title.PreviewTabTitle" />
							   	</td>
							   	<td width="50%" class="tabMenuSeparator" colspan="3">&nbsp;</td>
							</tr>
							<!-- tab end -->

							<tr valign="top" >
								<td valign="top" colspan="7" class="formFieldNoBorders">
									<bean:message key="app.title.formName"/><%=rootName%>
								</td>
							</tr>

							<tr valign="top">
								<td style="padding-left:5px" colspan="7" height='100%' width="100%">
									<table width="100%" height='100%' cellspacing="0" cellpadding="0" valign="top">
										<tr valign="top" height='100%'>
											<td valign = "top" valign="top" width="75%">
												<table class="tbBordersAllbordersBlack" height="100%" width="100%" cellspacing="0" cellpadding="0">
													<thead>
														<tr>
															<c:choose>
																<c:when test='${controlsForm.controlOperation == "Edit"}'>
																	<th id="formTitle" align="left" class="formTitleGray">
																		<bean:message key="app.title.editAttributes" />
																	</th>
																</c:when>
																<c:otherwise>
																	<th id="formTitle" align="left" class="formTitleGray">
																		<bean:message key="app.title.addAttributes" />
																	</th>
																</c:otherwise>
															</c:choose>
														</tr>
													</thead>
													<tr>
														<td>
															<jsp:include page="/pages/toolTypeHTML/CommonControlAttributes.jsp"/>
														</td>
													</tr>
													<tr>
														<td>
															<table width="100%" height='100%'>
																<tr>
																	<td width="15%" height='100%' class="toolBoxTable" align="center">
																		<table valign="top" align="center" height='100%' width='100%' class="tbBordersAllbordersBlack" cellspacing="0" cellpadding="0">
																			<tr height='100%' width='100%' valign="top" style="background-color:#F4F4F5;">
																				<td height='100%' width='100%' align="center">
																					<dynamicExtensions:ToolsMenu id="BuildForm"	toolsList="<%=toolsList%>" onClick="controlSelectedAction" selectedUserOption="<%=userSelectedTool%>"/>
																				</td>
																			</tr>
																			<tr width='100%' valign="bottom" style="background-color:#F4F4F5;">
																				<td align="center" width="100%">
																					<html:button styleClass="formButton" property="addSubFormBtn" onclick="addSubForm()">
																						<bean:message key="eav.caption.AddSubFormControl"/>
																					</html:button>
																				</td>
																			</tr>
																			<tr width='100%' valign="bottom" style="background-color:#F4F4F5;">
																				<td>&nbsp;</td>
																			</tr>
																		</table>
																	</td>
																	<td height='100%'>
																		<table valign="top" align="left" height='100%' width='100%' class="tbBordersAllbordersBlack" cellspacing="0" cellpadding="0">
																			<thead>
																				<tr>
																					<th align="left" class="formTitleGray">
																						<%=selectedControlCaption%><bean:message key="app.formControl.properties"/>
																					</th>
																				</tr>
																			</thead>
																			<tr height='100%' width='100%' valign="top">
																				<td height='100%' width='100%'>
																					<jsp:include page="<%=htmlFile%>"/>
																				</td>
																			<tr>
																		</table>
																	</td>
																</tr>
															</table>
														</td>
													</tr>
												</table>
											</td>

											<td width="2%">&nbsp;</td>

											<td valign="top" height="100%" >
												<table valign="top" height='100%' width="100%" cellspacing="0" cellpadding="0">
													<tr valign="top" height='100%'>
														<td height='100%' width='100%'>
															<table id='controlList' cellspacing="0" class="tbBordersAllbordersBlack" height="100%" width="100%">
																<thead>
																	<tr>
																		<th colspan="3" align="left" class="formTitleGray">
																			<bean:message key="app.formControlsTree.heading"/>
																		</th>
																	</tr>
																</thead>
																<tbody>
																	<tr height="100%" valign="top">
																		<td align="center">
																			<div id="gridbox" width="100%" height="100%" align="center" style="background-color:white;overflow:hidden"/>
																		</td>
																	</tr>
																</tbody>
															</table>
														</td>
													</tr>
												</table>
											</td>
										</tr>
									</table>
								</td>
							</tr>

							<!--Add attributes btn + Controls (UP + Down + Delete) btn) -->
							<tr>
								<td colspan="7">
									<table width="100%" height='100%' valign="top">
										<tr valign="top" height='100%'>
											<td align="right" valign="top" width="75%">
												<html:button styleClass="formButton" property="cancelControlOperation" onclick="initCancelOperation()">
													<bean:message  key="buttons.cancel"/>
												</html:button>
												<html:button styleClass="formButton" property="addControlToFormButton" onclick="addControlToFormTree()">
													<bean:message  key="buttons.addControlToForm"/>
												</html:button>
											</td>
											<td width="2%">&nbsp;</td>
											<td valign="top" height="100%">
												<input type="button" class="groupButton" name="upButton" value="Up" onclick="moveControlsUp()"/>
												<input type="button" class="formButton" name="downButton" value="Down" onclick="moveControlsDown()"/>
												<input type="button" class="formButton" name="deleteButton" value="Remove" onclick="deleteControl()"/>
											</td>
										</tr>
									</table>
								</td>
							</tr>

							<tr>
								<td colspan="2" align="left" class="formLabelBorderless">
									<html:button styleClass="groupButtonMainForm" property="saveButton" onclick="saveEntity()" onkeydown="saveEntityOnKeyDown(event)">
										<bean:message key="buttons.save"/>
									</html:button>
									&nbsp;
									<html:reset styleClass="groupButtonMainForm" property="cancelButton" onclick='showHomePageFromBuildForm()'>
										<bean:message key="buttons.cancel"/>
									</html:reset>
								</td>
								<td colspan="5" align="right" class="formLabelBorderless">
									<html:button styleClass="groupButtonMainForm" property="prevButton" onclick="showNextActionConfirmDialog()">
										<bean:message key="buttons.prev"/>
									</html:button>
									&nbsp;
									<html:button styleClass="groupButtonMainForm" property="showPreviewButton" onclick="showFormPreview()">
										<bean:message key="buttons.next"/>
									</html:button>
								</td>
							</tr>
						</table>
					</td>
				</tr>
			</table>
		  	<html:hidden styleId='operation' property="operation" value=""/>
		  	<html:hidden styleId='selectedAttrib' property="selectedAttrib" value=""/>
			<html:hidden styleId='controlOperation' property="controlOperation"/>
			<html:hidden styleId='selectedControlId' property="selectedControlId"/>
			<html:hidden styleId='validationRules' property="validationRules" value=""/>
			<html:hidden styleId='currentContainerName' property="currentContainerName"/>
			<input type="hidden" name="entitySaved" id='entitySaved'/>
			<input type="hidden" id="previousControl" name="previousControl" value=""/>
			<input type="hidden" name='operationMode' id="operationMode" value="AddSubForm"/>
			<input type="hidden" name="controlsSequenceNumbers" id="controlsSequenceNumbers"/>
			<input type="hidden" id="checkAttribute" name="checkAttribute" value=""/>
	  	</html:form>
  	</body>
</html>
