html xmlns="http://www.w3.org/1999/xhtml">
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
<title>caBench-To-Bedside</title>
<link rel="shortcut icon" type="image/x-icon" href="images/favicon.ico">
<link href="css/cab2b.css" rel="stylesheet" type="text/css" />
<link href="css/cab2b_1024.css" rel="stylesheet" type="text/css">
<script src="javascript/menu_popup.js">
</script>
<script language="JavaScript" src="javascript/menu_create.js"></script>
<script language="JavaScript" src="javascript/queryModule.js"></script>
<script language="JavaScript1.2">mmLoadMenus();</script>
<script type="text/javascript" src="javascript/ajax.js"></script>

<SCRIPT LANGUAGE="JavaScript">
<!--
var secs
var timerID = null
var timerRunning = false
var delay = 300;

function InitializeTimer()
{
    // Set the length of the timer, in seconds
    secs = 50;
    StopTheClock();
    StartTheTimer();
}

function StopTheClock()
{
    if(timerRunning)
        clearTimeout(timerID)
    timerRunning = false
}

function StartTheTimer()
{
    if(secs==0)
    {
      getUpdatedStatus();
      secs =50;  
      StartTheTimer();
    }
    else
    {
        self.status = secs
        secs = secs - 1
        timerRunning = true
        timerID = self.setTimeout("StartTheTimer()", delay)
    }
}
//-->
</SCRIPT>



</head>
<body onLoad="InitializeTimer();">
<table width="100%" border="0" align="center" cellpadding="0"
	cellspacing="0">
	<tr>
		<td valign="top">
		<table width="100%" border="0" align="center" cellpadding="0"
			cellspacing="0">
			<tr>
				<td width="199" valign="top"><img src="images/logo.gif"
					alt="caBench-to-Bedside" width="252" height="70" class="td_white" /></td>
				<td align="center" valign="middle">
				<table width="100%" border="0" cellspacing="0" cellpadding="0">
					<tr>
						<td align="center" valign="bottom" background="images/bg_top.gif">&nbsp;</td>
						<td align="left" valign="bottom" background="images/bg_top.gif"
							class="td_white">&nbsp;</td>
						<td align="right" valign="top" background="images/bg_top.gif"
							class="td_white"><a href="Logout.action" class="font_bl1_b">Sign
						Out</a><img src="images/spacer.gif" width="10" height="10"
							align="absmiddle"></td>
					</tr>
					<tr>
						<td width="10" align="center" valign="bottom"
							background="images/bg_top.gif">&nbsp;</td>
						<td align="left" valign="bottom" background="images/bg_top.gif"
							class="td_white">
						<table width="293" border="0" cellpadding="0" cellspacing="0">
							<tr>
								<td width="74"><a href="../home.html"
									onMouseOut="MM_swapImgRestore();MM_startTimeout()"
									onMouseOver="MM_swapImage('Image28','','images/home1.gif',1)"><img
									src="images/home2.gif" alt="Home" name="Image28" width="74"
									height="23" border="0" id="Image28" /></a></td>
								<td width="111"><a href="#"
									onMouseOut="MM_swapImgRestore();MM_startTimeout();"
									onMouseOver="MM_swapImage('Image29','','images/sd1.gif',1); MM_showMenu(window.mm_menu_1203113257_0,2,23,null,'Image29')"
									name="link2" id="link1"><img src="images/sd1.gif"
									alt="Search Data" name="Image29" width="118" height="23"
									border="0" id="Image29" /></a></td>
								<td width="108"><a href="#" name="link4" id="link3"
									onMouseOut="MM_swapImgRestore();MM_startTimeout()"
									onMouseOver="MM_swapImage('Image12','','images/exp1.gif',1);MM_showMenu(window.mm_menu_1203115052_0,2,23,null,'Image12')"><img
									src="images/exp2.gif" name="Image12" width="115" height="23"
									border="0" id="Image12" /></a></td>
							</tr>
						</table>
						</td>
						<td align="right" valign="middle" background="images/bg_top.gif"
							class="td_white"><span class="font_bl2_b">Welcome,
						${sessionScope.UserName}<img src="images/spacer.gif" width="10"
							height="10" align="absmiddle"></span></td>
					</tr>


				</table>
				</td>
			</tr>

		</table>
		</td>
	</tr>
</table>

<!--Begin content area -->
<table width="100%" border="0" cellpadding="0" cellspacing="0">
	<c:set var="modelStatus" value="${requestScope.modelStatus}" />
	
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
					<c:forEach var="modelName" items="${requestScope.modelStatus}">

					<c:choose >
					    <c:when test="${modelName.value eq 'Failed'}">
					       <c:set var="image" value="ic_fail.gif" scope="page" />
					    </c:when>
					    <c:when test="${modelName.value eq 'Pass'}">
					       	<c:set var="image" value="ic_pass.gif" scope="page" />
					    </c:when>
					    <c:otherwise>
						    <c:set var="image" value="ic_inprogress.gif" scope="page" />
					    </c:otherwise>
					 </c:choose>
					


						<tr>
							<td class="font_blk_s">&nbsp;</td>
							<td class="font_blk_s">${modelName.key}</td>
							<td class="font_blk_s"><span id="${modelName.key}"><img
								src="images/${pageScope.image}" alt="${modelName.value}"
								width="20" height="20" hspace="3" align="absmiddle">${modelName.value}
							</span></td>
							<td><a href="#" class="font_blk_s">More Details</a></td>
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
				<form id="form2" name="form2" method="post"
					action="LoadModels.action">
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


</body>
<%@page import="java.util.Map"%>
</html>
