<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@taglib prefix="c" uri="http://java.sun.com/jstl/core" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<HTML>
<HEAD>
<title>caBench-To-Bedside</title>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
<SCRIPT>
window.onload = document.getElementById('modelGroupName').focus();

function validate() {
	if(document.getElementById('modelGroupName').value == "") {
		alert('Please Enter the Model group Name');
	}
}
</SCRIPT>
</head>
<BODY>
<TABLE width="100%">
	<TR>
		<TD height="35" colspan="4" align="left" valign="top" background="\\">
			<TABLE width="200" border="0" cellpadding="0" cellspacing="0" class="td_dgrey">
				<TR>
					<TD><IMG src="images/spacer.gif" width="10" height="1"></TD>
					<TD><IMG src="images/arrow.gif" width="12" height="11" hspace="5" align="absmiddle"><SPAN class="font_bl2_b">Define Model Groups</SPAN></TD>
		            <TD align="right" valign="top"><IMG src="images/c6.gif" width="15" height="23"></TD>
			    </TR>
			</TABLE>
		</TD>
	</TR>
	<TR>
		<TD align="center" valign="top"><table width="100%" cellpadding="0" cellspacing="0" >
			<TR>
				<TD align="center" valign="top" class="td_white">
					<TABLE width="99%" border="0" align="center" cellpadding="0" cellspacing="0"/>
						<FORM method="post" action="SaveModelGroup.action" onsubmit="return validate()">
						<TR>
							<TD><TABLE width="100%" cellpadding="4" cellspacing="2">
							<TR>
									<TD align="left" nowrap><SPAN class="font_blk_s">Model Group Name :</SPAN></TD><TD>
									<INPUT type="text" name="modelGroupName">&nbsp;&nbsp;
									<INPUT type="checkbox" name="secured" value="True"/><SPAN class="font_blk_s">Is Secured</SPAN></TD>
								</TR>
								<TR>
									<TD nowrap>
		
									<span class="font_blk_s">Description&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; :</span></TD>
									<TD style="width:99%" align="left"><TEXTAREA rows="2" cols="27" name="modelGroupDescription"></TEXTAREA></TD> 
								</TR></TABLE></TD>
							</TR>	
							<TR class="td_white" align="left">
								<TD></BR></TD>
								<Table width="99%" border="0" cellpadding="0" cellspacing="0">

									<TD class="font_bl3_b" align="left" background="images/title_bg.gif" valign="middle"><img src="images/c1.gif" align="absmiddle" height="24" width="12">&nbsp;&nbsp;Available&nbsp;&nbsp; Models</TD>
									<TD class="td_dblue2" align="right" background="images/title_bg.gif" valign="middle">
									<TD class="td_dblue2" align="right" background="images/title_bg.gif" valign="top" width="12"><img src="images/c2.gif" height="24" width="12"></TD></TR>
									<TR>
									<TD colspan="3" class="td_orange" align="left" valign="top"><img src="images/spacer.gif" height="1" width="1"></TD>
								</TR>
								</table>
								
							</TR>
							<TR>
								<TD>
									</br>
								</TD>
							</TR>
							<TR align="left">
								<logic:present name="allLoadedModels">
									<logic:iterate name="allLoadedModels" id="entityGroup" type="edu.common.dynamicextensions.domaininterface.EntityGroupInterface">
									<TR>
										<TD>
											&nbsp;&nbsp;<input type="checkbox" name="selectedEntityGroup" value="<bean:write name="entityGroup" property="longName"/>_v<bean:write name="entityGroup" property="version"/>"/>
											<SPAN class="font_bl1_b"><bean:write name="entityGroup" property="longName"/>_v<bean:write name="entityGroup" property="version"/></SPAN>
										</TD>
									</TR>
									<TR>
										<TD>
											&nbsp;&nbsp;<SPAN class="font_blk_s"><bean:write name="entityGroup" property="description"/></SPAN>
										</TD>
									</TR>
									<TR>
										<TD>
											</br>
										</TD>
									</TR>
									</logic:iterate>
								</logic:present>
							</TR>
							</BR>
							<TR class="td_grey">

								<TD colspan="2">
									<img width="1" height="1" src="images/spacer.gif"/>
									<INPUT type="submit" value="Save Group" />
								</TD>
							</TR>

						</FORM>
					</table>
				</td>
			</tr>
		</td>
	</tr>
</table>
</Body>
</HTML>