<%@page import="java.util.Map"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<script language="JavaScript" src="javascript/queryModule.js"></script>
<script type="text/javascript" src="javascript/ajax.js"></script>
<SCRIPT LANGUAGE = "JavaScript">

 timedCount();
var t;

function timedCount()
{
  
  t=setTimeout("callUpdate()",5000);
}

function callUpdate()
{
  getUpdatedStatus();
}

</SCRIPT>


<body onLoad="InitializeTimer();">

  
<table width="100%" border="0" cellpadding="0" cellspacing="0">
	<c:set var="modelList" value="${requestScope.ListOfUserModels}" />
	
	<tr>
		<td align="center" valign="top">
		<table width="100%" cellpadding="0" cellspacing="0">

			<tr>
				<td align="left" valign="top" class="newtr">
				<table width="100%" border="0" cellpadding="0" cellspacing="0">
					<tr>
						<td width="75" height="35" colspan="4" align="left" valign="top"
							background="\\">
						<table width="200" border="0" cellpadding="0" cellspacing="0"
							class="td_dgrey">
							<tr>
								<td><img src="images/spacer.gif" width="10" height="1"></td>
								<td><img src="images/arrow.gif" width="12" height="11"
									hspace="5" align="absmiddle"><span class="font_bl2_b">Load
								Models </span></td>
								<td align="right" valign="top"><img src="images/c6.gif"
									width="15" height="23"></td>
							</tr>
						</table>
						</td>
					</tr>
				</table>
				</td>
			</tr>
			<tr>
				<td align="center" valign="top" class="td_white">
				<table width="99%" border="0" align="center" cellpadding="0"
					cellspacing="0">
					<tr>
						<td align="left" valign="middle" background="images/title_bg.gif"
							class="font_bl3_b"><img src="images/c1.gif" width="12"
							height="24" align="absmiddle" />&nbsp;&nbsp;History</td>
						<td width="12" align="right" valign="top"
							background="images/title_bg.gif" class="td_dblue2"><img
							src="images/c2.gif" width="12" height="24" /></td>
					</tr>
					<tr>
						<td colspan="2" align="left" valign="top" class="td_orange"><img
							src="images/spacer.gif" width="1" height="1"></td>
					</tr>
				</table>
				<table width="99%" border="0" cellspacing="0" cellpadding="3">
					<tr>
						<td>&nbsp;</td>
						<td>&nbsp;</td>
						<td>&nbsp;</td>
						<td>&nbsp;</td>
					</tr>
					<tr class="td_dgrey">
						<td class="font_blk_b">&nbsp;</td>
						<td class="font_blk_b">Models</td>
						<td class="font_blk_b">Status</td>
						<td class="font_blk_b">Details</td>
					</tr>
					<c:forEach var="model" items="${sessionScope.ListOfUserModels}">

						<tr>
							<td class="font_blk_s">&nbsp;</td>
							<td class="font_blk_s">${model.longName} v${model.version}</td>
							<td class="font_blk_s"><span id="${model.longName}"><img
								src="images/ic_inprogress2.gif" alt="${model.longName}"
								width="20" height="20" hspace="3" align="absmiddle">In Progress
							</span></td>
							<td> <span id="${model.longName}_details"> </span></td>
						</tr>

					</c:forEach>
				</table>
				</td>
			</tr>
			<tr>
				<td align="left" valign="top" class="td_white">&nbsp;</td>
			</tr>
		</table>
		<table width="100%" border="0" align="center" cellpadding="0"
			cellspacing="0" class="td_white">
			<tr>
				<td colspan="2">
				<table width="100%" border="0" cellpadding="0" cellspacing="0">
					<tr>
						<td class="td_dgrey"><img src="images/spacer.gif" width="1"
							height="1" /></td>
					</tr>
				</table>
				</td>
			</tr>
			<tr>
				<form id="form2" name="form2" method="post" action="LoadModels.action">
				<td width="30" height="35" align="right" class="td_grey">&nbsp;</td>
				<td align="left" class="td_grey"><input name="Submit2"
					type="submit" class="font_bl1_b" value="Back to Load Models" /></td>
				</form>
			</tr>
		</table>
		</td>
	</tr>

	<tr>
		<td align="center" valign="bottom">&nbsp;</td>
	</tr>
</table>
<!--end content area -->
