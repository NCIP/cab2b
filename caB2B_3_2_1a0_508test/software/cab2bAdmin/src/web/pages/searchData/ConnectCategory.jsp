<!--Begin content area -->
<%@page import="edu.wustl.cab2b.admin.util.AdminConstants"%>
<link href="css/cab2b.css" rel="stylesheet" type="text/css" />
<script src="javascript/dhtmlwindow.js"></script>
<script src="javascript/queryModule.js"></script>

<!--Begin content area -->
<%
    session.setAttribute(AdminConstants.PAGE_IDENTIFIER, AdminConstants.CALLING_PAGE);
%>

<table width="100%" border="0" cellpadding="0" cellspacing="0">
	<tr>
		<td align="center" valign="top">
		<table width="100%" cellpadding="0" cellspacing="0">
			<tr>
				<td colspan="3" align="left" valign="top" class="newtr">
				<table width="100%" border="0" cellpadding="0" cellspacing="0">
					<tr>
						<td width="75" height="35" colspan="4" align="left" valign="top"
							background="\\">
						<table width="200" border="0" cellpadding="0" cellspacing="0"
							class="td_dgrey">
							<tr>
								<td><img src="images/spacer.gif" width="10" height="1" /></td>
								<td><img src="images/arrow.gif" width="12" height="11"
									hspace="5" align="absmiddle" /><span class="font_bl2_b">InterModel Join</span></td>
								<td align="right" valign="top"><img src="images/c6.gif"
									width="15" height="23" /></td>
							</tr>
						</table>
						</td>
					</tr>
				</table>
				</td>
			</tr>
			<tr>
				<td width="252" rowspan="3" align="left" valign="top"
					class="td_white"><%@ include file="ChooseCategory.jsp"%>
				</td>
				<td width="1" align="right" class="td_dgrey"><img
					src="images/spacer.gif" width="1" height="1" /></td>
				<td align="center" valign="top" class="td_white">
				<table width="99%" border="0" align="center" cellpadding="0"
					cellspacing="0">
					<tr>
						<td align="left" valign="middle" background="images/title_bg.gif"
							class="font_bl3_b"><img src="images/c1.gif" width="12"
							height="24" align="absmiddle" /></td>
						<td align="left" valign="middle" background="images/title_bg.gif"
							class="font_bl3_b"><input type="hidden" name="entityId"
							id="entityId" /></td>
						<td align="left" valign="middle" background="images/title_bg.gif"
							class="font_bl3_b"><input type="hidden"
							name="ConnectCategory" id="curate_or_intermodel" /></td>
						<td align="left" valign="top" background="images/title_bg.gif"
							class="td_dblue2">&nbsp;</td>
						<td align="right" valign="top" background="images/title_bg.gif"
							class="td_dblue2"><img src="images/c2.gif" width="12"
							height="24" /></td>
					</tr>
					<tr>
						<td colspan="5" align="left" valign="top" class="td_orange"><img
							src="images/spacer.gif" width="1" height="1" /></td>
					</tr>
				</table>
				<table width="99%" height="350" border="0" cellpadding="0"
					cellspacing="0">
					<tr>
						<td height="470px">
						<div id="queryTableTd"
							style="overflow: auto; height: 100%; width: 100%"><object
							classid="clsid:D27CDB6E-AE6D-11cf-96B8-444553540000" id="DAG"
							width="100%" height="100%"
							codebase="http://fpdownload.macromedia.com/get/flashplayer/current/swflash.cab">
							<param name="movie"
								value="flexclient/dag/DAG.swf?callingPage=InterModelJoin" />
							<param name="quality" value="high" />
							<param name="bgcolor" value="#869ca7" />
							<param name="allowScriptAccess" value="sameDomain" />
							<embed src="flexclient/dag/DAG.swf?callingPage=InterModelJoin"
								quality="high" bgcolor="#869ca7" width="100%" height="100%"
								name="DAG" align="middle" play="true" loop="false"
								quality="high" allowScriptAccess="sameDomain"
								type="application/x-shockwave-flash"
								pluginspage="http://www.adobe.com/go/getflashplayer">
							</embed> </object> <!--This script is for DAG to run ON IE   --> 
							<script type="text/javascript">
								var objects = document.getElementsByTagName("object");
								for(i=0;i<objects.length;i++){
									window[objects[i].id] = objects[i];
								}
							</script></div>
						</td>
					</tr>
				</table>
				</td>
			</tr>
		</table>
		<table width="100%" border="0" align="center" cellpadding="0"
			cellspacing="0" class="td_white">
			<tr>
				<td>
				<table width="100%" border="0" cellpadding="0" cellspacing="0">
					<tr>
						<td class="td_dgrey"><img src="images/spacer.gif" width="1"
							height="1" /></td>
					</tr>
				</table>
				</td>
			</tr>
			<tr>
				<td height="35" align="right" class="td_grey"><input
					name="Submit2" type="submit" class="font_bl1_b"
					onclick="saveInterModelJoinClasses()" value="Save Join" /> &nbsp;</td>
			</tr>
		</table>
		</td>
	</tr>
	<tr>
		<td align="center" valign="bottom">&nbsp;</td>
	</tr>
</table>
<!--end content area -->
